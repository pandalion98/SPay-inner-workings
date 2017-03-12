package android.net;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;

public class DhcpStateMachine extends BaseDhcpStateMachine {
    private static final String ACTION_DHCP_RENEW = "android.net.wifi.DHCP_RENEW";
    private static final int BASE = 196608;
    private static final int CMD_GET_DHCP_RESULTS = 196616;
    public static final int CMD_ON_QUIT = 196614;
    public static final int CMD_POST_DHCP_ACTION = 196613;
    public static final int CMD_PRE_DHCP_ACTION = 196612;
    public static final int CMD_PRE_DHCP_ACTION_COMPLETE = 196615;
    public static final int CMD_RELEASE_DHCP = 196617;
    public static final int CMD_RENEW_DHCP = 196611;
    public static final int CMD_START_DHCP = 196609;
    public static final int CMD_STOP_DHCP = 196610;
    private static final boolean DBG = false;
    public static final int DHCP_FAILURE = 2;
    private static final int DHCP_RENEW = 0;
    public static final int DHCP_SUCCESS = 1;
    private static final int MIN_RENEWAL_TIME_SECS = 60;
    private static final String TAG = "DhcpStateMachine";
    private static final String WAKELOCK_TAG = "DHCP";
    private AlarmManager mAlarmManager;
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private StateMachine mController;
    private State mDefaultState = new DefaultState();
    private WakeLock mDhcpRenewWakeLock;
    private PendingIntent mDhcpRenewalIntent;
    private DhcpResults mDhcpResults;
    private final String mInterfaceName;
    private State mPollingState = new PollingState();
    private boolean mRegisteredForPreDhcpNotification = false;
    private State mRunningState = new RunningState();
    private boolean mStopSendMsgToWsm;
    private State mStoppedState = new StoppedState();
    private State mWaitBeforeRenewalState = new WaitBeforeRenewalState();
    private State mWaitBeforeStartState = new WaitBeforeStartState();

    class DefaultState extends State {
        DefaultState() {
        }

        public void exit() {
            DhcpStateMachine.this.mContext.unregisterReceiver(DhcpStateMachine.this.mBroadcastReceiver);
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_RENEW_DHCP /*196611*/:
                    Log.e(DhcpStateMachine.TAG, "Error! Failed to handle a DHCP renewal on " + DhcpStateMachine.this.mInterfaceName);
                    DhcpStateMachine.this.mDhcpRenewWakeLock.release();
                    break;
                default:
                    Log.e(DhcpStateMachine.TAG, "Error! unhandled message  " + message);
                    break;
            }
            return true;
        }
    }

    public enum DhcpAction {
        START,
        RENEW
    }

    class PollingState extends State {
        private static final long MAX_DELAY_SECONDS = 32;
        private long delaySeconds;

        PollingState() {
        }

        private void scheduleNextResultsCheck() {
            DhcpStateMachine.this.sendMessageDelayed(DhcpStateMachine.this.obtainMessage(DhcpStateMachine.CMD_GET_DHCP_RESULTS), this.delaySeconds * 1000);
            this.delaySeconds *= 2;
            if (this.delaySeconds > 32) {
                this.delaySeconds = 32;
            }
        }

        public void enter() {
            this.delaySeconds = 1;
            scheduleNextResultsCheck();
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_STOP_DHCP /*196610*/:
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                case DhcpStateMachine.CMD_GET_DHCP_RESULTS /*196616*/:
                    if (DhcpStateMachine.this.dhcpSucceeded()) {
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mRunningState);
                        return true;
                    }
                    scheduleNextResultsCheck();
                    return true;
                default:
                    return false;
            }
        }

        public void exit() {
            DhcpStateMachine.this.removeMessages(DhcpStateMachine.CMD_GET_DHCP_RESULTS);
        }
    }

    class RunningState extends State {
        RunningState() {
        }

        public void enter() {
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_START_DHCP /*196609*/:
                    return true;
                case DhcpStateMachine.CMD_STOP_DHCP /*196610*/:
                    DhcpStateMachine.this.mAlarmManager.cancel(DhcpStateMachine.this.mDhcpRenewalIntent);
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                case DhcpStateMachine.CMD_RENEW_DHCP /*196611*/:
                    if (DhcpStateMachine.this.mRegisteredForPreDhcpNotification) {
                        DhcpStateMachine.this.mController.sendMessage(DhcpStateMachine.CMD_PRE_DHCP_ACTION);
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mWaitBeforeRenewalState);
                        return true;
                    }
                    if (!DhcpStateMachine.this.runDhcpRenew()) {
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    }
                    DhcpStateMachine.this.mDhcpRenewWakeLock.release();
                    return true;
                case DhcpStateMachine.CMD_RELEASE_DHCP /*196617*/:
                    DhcpStateMachine.this.mAlarmManager.cancel(DhcpStateMachine.this.mDhcpRenewalIntent);
                    if (!NetworkUtils.releaseDhcpLease(DhcpStateMachine.this.mInterfaceName)) {
                        Log.e(DhcpStateMachine.TAG, "Failed to release Dhcp lease on " + DhcpStateMachine.this.mInterfaceName);
                    }
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                default:
                    return false;
            }
        }
    }

    class StoppedState extends State {
        StoppedState() {
        }

        public void enter() {
            if (!NetworkUtils.stopDhcp(DhcpStateMachine.this.mInterfaceName)) {
                Log.e(DhcpStateMachine.TAG, "Failed to stop Dhcp on " + DhcpStateMachine.this.mInterfaceName);
            }
            DhcpStateMachine.this.mDhcpResults = null;
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_START_DHCP /*196609*/:
                    if (DhcpStateMachine.this.mRegisteredForPreDhcpNotification) {
                        DhcpStateMachine.this.mController.sendMessage(DhcpStateMachine.CMD_PRE_DHCP_ACTION);
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mWaitBeforeStartState);
                        return true;
                    } else if (!DhcpStateMachine.this.runDhcpStart()) {
                        return true;
                    } else {
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mRunningState);
                        return true;
                    }
                case DhcpStateMachine.CMD_STOP_DHCP /*196610*/:
                case DhcpStateMachine.CMD_RELEASE_DHCP /*196617*/:
                    return true;
                default:
                    return false;
            }
        }
    }

    class WaitBeforeRenewalState extends State {
        WaitBeforeRenewalState() {
        }

        public void enter() {
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_START_DHCP /*196609*/:
                    return true;
                case DhcpStateMachine.CMD_STOP_DHCP /*196610*/:
                    DhcpStateMachine.this.mAlarmManager.cancel(DhcpStateMachine.this.mDhcpRenewalIntent);
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                case DhcpStateMachine.CMD_PRE_DHCP_ACTION_COMPLETE /*196615*/:
                    if (DhcpStateMachine.this.runDhcpRenew()) {
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mRunningState);
                        return true;
                    }
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                case DhcpStateMachine.CMD_RELEASE_DHCP /*196617*/:
                    DhcpStateMachine.this.mAlarmManager.cancel(DhcpStateMachine.this.mDhcpRenewalIntent);
                    if (!NetworkUtils.releaseDhcpLease(DhcpStateMachine.this.mInterfaceName)) {
                        Log.e(DhcpStateMachine.TAG, "Failed to release Dhcp lease on " + DhcpStateMachine.this.mInterfaceName);
                    }
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                default:
                    return false;
            }
        }

        public void exit() {
            DhcpStateMachine.this.mDhcpRenewWakeLock.release();
        }
    }

    class WaitBeforeStartState extends State {
        WaitBeforeStartState() {
        }

        public void enter() {
        }

        public boolean processMessage(Message message) {
            switch (message.what) {
                case DhcpStateMachine.CMD_START_DHCP /*196609*/:
                    return true;
                case DhcpStateMachine.CMD_STOP_DHCP /*196610*/:
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                case DhcpStateMachine.CMD_PRE_DHCP_ACTION_COMPLETE /*196615*/:
                    if (DhcpStateMachine.this.runDhcpStart()) {
                        DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mRunningState);
                        return true;
                    }
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mPollingState);
                    return true;
                case DhcpStateMachine.CMD_RELEASE_DHCP /*196617*/:
                    DhcpStateMachine.this.transitionTo(DhcpStateMachine.this.mStoppedState);
                    return true;
                default:
                    return false;
            }
        }
    }

    private DhcpStateMachine(Context context, StateMachine controller, String intf) {
        super(TAG);
        this.mContext = context;
        this.mController = controller;
        this.mInterfaceName = intf;
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        this.mDhcpRenewalIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_DHCP_RENEW, null), 0);
        this.mDhcpRenewWakeLock = ((PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, WAKELOCK_TAG);
        this.mDhcpRenewWakeLock.setReferenceCounted(false);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                DhcpStateMachine.this.mDhcpRenewWakeLock.acquire(40000);
                DhcpStateMachine.this.sendMessage(DhcpStateMachine.CMD_RENEW_DHCP);
            }
        };
        this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter(ACTION_DHCP_RENEW));
        addState(this.mDefaultState);
        addState(this.mStoppedState, this.mDefaultState);
        addState(this.mWaitBeforeStartState, this.mDefaultState);
        addState(this.mPollingState, this.mDefaultState);
        addState(this.mRunningState, this.mDefaultState);
        addState(this.mWaitBeforeRenewalState, this.mDefaultState);
        setInitialState(this.mStoppedState);
    }

    public static DhcpStateMachine makeDhcpStateMachine(Context context, StateMachine controller, String intf) {
        DhcpStateMachine dsm = new DhcpStateMachine(context, controller, intf);
        dsm.start();
        return dsm;
    }

    public void registerForPreDhcpNotification() {
        this.mRegisteredForPreDhcpNotification = true;
    }

    public void doQuit() {
        if (getCurrentState() == this.mRunningState) {
            this.mController.sendMessage(CMD_ON_QUIT);
            this.mStopSendMsgToWsm = true;
        }
        quit();
    }

    protected void onQuitting() {
        if (!this.mStopSendMsgToWsm) {
            this.mController.sendMessage(CMD_ON_QUIT);
        }
    }

    private boolean dhcpSucceeded() {
        DhcpResults dhcpResults = new DhcpResults();
        if (!NetworkUtils.getDhcpResults(this.mInterfaceName, dhcpResults)) {
            return false;
        }
        long leaseDuration = (long) dhcpResults.leaseDuration;
        if (leaseDuration >= 0 && !"ibss".equals(this.mInterfaceName)) {
            if (leaseDuration < 60) {
                leaseDuration = 60;
            }
            this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + (480 * leaseDuration), this.mDhcpRenewalIntent);
        }
        dhcpResults.updateFromDhcpRequest(this.mDhcpResults);
        this.mDhcpResults = dhcpResults;
        this.mController.obtainMessage(CMD_POST_DHCP_ACTION, 1, 0, dhcpResults).sendToTarget();
        return true;
    }

    private boolean runDhcpStart() {
        NetworkUtils.stopDhcp(this.mInterfaceName);
        this.mDhcpResults = null;
        if (NetworkUtils.startDhcp(this.mInterfaceName) && dhcpSucceeded()) {
            return true;
        }
        Log.e(TAG, "DHCP request failed on " + this.mInterfaceName + ": " + NetworkUtils.getDhcpError());
        this.mController.obtainMessage(CMD_POST_DHCP_ACTION, 2, 0).sendToTarget();
        return false;
    }

    private boolean runDhcpRenew() {
        if (NetworkUtils.startDhcpRenew(this.mInterfaceName) && dhcpSucceeded()) {
            return true;
        }
        Log.e(TAG, "DHCP renew failed on " + this.mInterfaceName + ": " + NetworkUtils.getDhcpError());
        this.mController.obtainMessage(CMD_POST_DHCP_ACTION, 2, 0).sendToTarget();
        return false;
    }
}
