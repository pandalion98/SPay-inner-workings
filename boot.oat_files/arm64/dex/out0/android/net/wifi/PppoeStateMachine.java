package android.net.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.IConnectivityManager;
import android.net.INetworkManagementEventObserver;
import android.net.InterfaceConfiguration;
import android.net.LinkAddress;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.net.RouteInfo;
import android.net.wifi.PPPOEInfo.Status;
import android.os.BatteryManager;
import android.os.INetworkManagementService;
import android.os.INetworkManagementService.Stub;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.android.server.net.BaseNetworkObserver;

public class PppoeStateMachine extends StateMachine {
    private static final boolean PPPOE_ENABLED;
    private static final String PPPOE_INTERFACE = "ppp3";
    private static final int PPPOE_NET_ID = 1001;
    private static final String TAG = "PppoeStateMachine";
    private static final String WIFI_INTERFACE_PROP = "wifi.interface";
    private static final String WLAN_INTERFACE = "wlan0";
    private boolean createNetwok = false;
    private BroadcastReceiver mBroadcastReceiver;
    private ConnectState mConnectState = new ConnectState();
    private ConnectingState mConnectingState = new ConnectingState();
    private Context mContext;
    private DefaultState mDefaultState = new DefaultState();
    private DisconnectingStateState mDisconnectingStateState = new DisconnectingStateState();
    private String mGateWayProp;
    private IntentFilter mIntentFilter;
    private String mLocalProp;
    private INetworkManagementEventObserver mNetworkManagementEventObserver = new BaseNetworkObserver() {
        public void interfaceStatusChanged(String iface, boolean up) {
            Log.d(PppoeStateMachine.TAG, "interfaceStatusChanged, iface=" + iface + " up=" + up);
        }

        public void interfaceLinkStateChanged(String iface, boolean up) {
            Log.d(PppoeStateMachine.TAG, "interfaceLinkStateChanged, iface=" + iface + " up=" + up);
            Log.d(PppoeStateMachine.TAG, "interfaceLinkStateChanged, status=" + PppoeStateMachine.this.mPppoeInfo.status);
            if (PppoeStateMachine.PPPOE_INTERFACE.equals(iface) && PppoeStateMachine.this.mPppoeInfo.status == Status.ONLINE && !up) {
                PppoeStateMachine.this.sendMessage(PppoeManager.CMD_STOP_PPPOE);
            }
        }
    };
    private INetworkManagementService mNetworkManagementService = null;
    private OfflineState mOfflineState = new OfflineState();
    private String mOldDns1;
    private String mOldDns2;
    private OnlineState mOnlineState = new OnlineState();
    private PPPOEConfig mPppoeConfig;
    private PPPOEInfo mPppoeInfo;
    private long mPppoeRunningTime = 0;
    private PppoeUnsupportedState mPppoeUnsupportedState = new PppoeUnsupportedState();
    private AsyncChannel mReplyChannel = new AsyncChannel();
    private boolean mWifiConnected;

    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState = new int[DetailedState.values().length];

        static {
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[DetailedState.DISCONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$net$NetworkInfo$DetailedState[DetailedState.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    class ConnectState extends State {
        int success;

        ConnectState() {
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    this.success = NetworkUtils.stopPppoe();
                    if (this.success != 0) {
                        return true;
                    }
                    PppoeStateMachine.this.transitionTo(PppoeStateMachine.this.mDisconnectingStateState);
                    return true;
                default:
                    return false;
            }
        }
    }

    class ConnectingState extends State {
        String pppoeState;
        int success;

        ConnectingState() {
        }

        public void enter() {
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
            PppoeStateMachine.this.setPppoeInfo(Status.CONNECTING);
            PppoeStateMachine.this.setPppoeState(PppoeManager.PPPOE_STATE_CONNECTING);
            if (PppoeStateMachine.this.mPppoeConfig != null) {
                this.success = NetworkUtils.startPppoe(PppoeStateMachine.this.mPppoeConfig);
                if (this.success == 0) {
                    Log.d(PppoeStateMachine.TAG, "starting pppoe");
                    PppoeStateMachine.this.createNetworkInfo(PppoeStateMachine.PPPOE_INTERFACE, 1001);
                    PppoeStateMachine.this.sendMessageDelayed(PppoeManager.PPPOE_CONNECTING_TIMED_OUT, 400);
                    return;
                }
                PppoeStateMachine.this.transitionTo(PppoeStateMachine.this.mOfflineState);
            }
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    PppoeStateMachine.this.setPppoeState(PppoeManager.PPPOE_STATE_DISCONNECTING);
                    return false;
                case PppoeManager.PPPOE_CONNECTING_TIMED_OUT /*458759*/:
                    this.pppoeState = SystemProperties.get("net.pppoe.state");
                    if (TextUtils.isEmpty(this.pppoeState)) {
                        PppoeStateMachine.this.sendMessageDelayed(PppoeManager.PPPOE_CONNECTING_TIMED_OUT, 400);
                    } else if (this.pppoeState.equals(BatteryManager.EXTRA_ONLINE)) {
                        PppoeStateMachine.this.transitionTo(PppoeStateMachine.this.mOnlineState);
                    } else if (this.pppoeState.equals("offline")) {
                        PppoeStateMachine.this.sendPppoeCompletedBroadcast(PppoeManager.PPPOE_RESULT_STATUS_FAILURE, String.valueOf(this.success));
                        PppoeStateMachine.this.sendMessage(PppoeManager.CMD_STOP_PPPOE);
                    }
                    return true;
                default:
                    return false;
            }
        }

        public void exit() {
            Log.d(PppoeStateMachine.TAG, "exit: " + getName());
            PppoeStateMachine.this.removeMessages(PppoeManager.PPPOE_CONNECTING_TIMED_OUT);
        }
    }

    class DefaultState extends State {
        DefaultState() {
        }

        public void enter() {
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_START_PPPOE /*458753*/:
                    Log.d(PppoeStateMachine.TAG, "alread start");
                    PppoeStateMachine.this.replyToMessage(msg, PppoeManager.CMD_START_PPPOE_FAILED, 2);
                    break;
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    PppoeStateMachine.this.replyToMessage(msg, PppoeManager.CMD_STOP_PPPOE_FAILED, 2);
                    break;
            }
            return true;
        }
    }

    class DisconnectingStateState extends State {
        RouteInfo mRouteinfo;
        boolean ret = true;

        DisconnectingStateState() {
        }

        public void enter() {
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    this.mRouteinfo = new RouteInfo(null, NetworkUtils.numericToInetAddress(PppoeStateMachine.this.mGateWayProp), PppoeStateMachine.PPPOE_INTERFACE, 1);
                    PppoeStateMachine.this.removeNetworkInfo(PppoeStateMachine.PPPOE_INTERFACE, this.mRouteinfo, 1001);
                    PppoeStateMachine.this.transitionTo(PppoeStateMachine.this.mOfflineState);
                    return true;
                default:
                    return false;
            }
        }
    }

    class OfflineState extends State {
        OfflineState() {
        }

        public void enter() {
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
            PppoeStateMachine.this.setPppoeInfo(Status.OFFLINE);
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_START_PPPOE /*458753*/:
                    PppoeStateMachine.this.mPppoeConfig = (PPPOEConfig) msg.obj;
                    if (PppoeStateMachine.this.mPppoeConfig.interf == null) {
                        PppoeStateMachine.this.mPppoeConfig.interf = SystemProperties.get(PppoeStateMachine.WIFI_INTERFACE_PROP, PppoeStateMachine.WLAN_INTERFACE);
                    }
                    PppoeStateMachine.this.transitionTo(PppoeStateMachine.this.mConnectingState);
                    break;
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    class OnlineState extends State {
        OnlineState() {
        }

        public void enter() {
            String[] mDnsProp = new String[]{SystemProperties.get("net.ppp3.dns1"), SystemProperties.get("net.ppp3.dns2")};
            PppoeStateMachine.this.mLocalProp = SystemProperties.get("net.ppp3.local-ip");
            PppoeStateMachine.this.mGateWayProp = SystemProperties.get("net.ppp3.remote-ip");
            RouteInfo mRouteinfo = new RouteInfo(null, NetworkUtils.numericToInetAddress(PppoeStateMachine.this.mGateWayProp), PppoeStateMachine.PPPOE_INTERFACE, 1);
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
            PppoeStateMachine.this.setPppoeInfo(Status.ONLINE);
            PppoeStateMachine.this.setPppoeState(PppoeManager.PPPOE_STATE_CONNECTED);
            PppoeStateMachine.this.mPppoeRunningTime = SystemClock.elapsedRealtime();
            PppoeStateMachine.this.sendPppoeCompletedBroadcast(PppoeManager.PPPOE_RESULT_STATUS_SUCCESS, null);
            PppoeStateMachine.this.updateNetworkInfo(PppoeStateMachine.PPPOE_INTERFACE, PppoeStateMachine.this.mLocalProp, mRouteinfo, mDnsProp, null, 1001);
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_START_PPPOE /*458753*/:
                    PppoeStateMachine.this.sendPppoeCompletedBroadcast(PppoeManager.PPPOE_RESULT_STATUS_ALREADY_ONLINE, null);
                    return true;
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    PppoeStateMachine.this.setPppoeState(PppoeManager.PPPOE_STATE_DISCONNECTING);
                    return false;
                default:
                    return false;
            }
        }

        public void exit() {
            Log.d(PppoeStateMachine.TAG, "exit: " + getName());
            PppoeStateMachine.this.mPppoeRunningTime = 0;
            PppoeStateMachine.this.setPppoeState(PppoeManager.PPPOE_STATE_DISCONNECTED);
        }
    }

    class PppoeUnsupportedState extends State {
        PppoeUnsupportedState() {
        }

        public void enter() {
            Log.d(PppoeStateMachine.TAG, "enter: " + getName());
        }

        public boolean processMessage(Message msg) {
            Log.d(PppoeStateMachine.TAG, getName() + ", " + msg.toString());
            switch (msg.what) {
                case PppoeManager.CMD_START_PPPOE /*458753*/:
                    PppoeStateMachine.this.replyToMessage(msg, PppoeManager.CMD_START_PPPOE_FAILED, 1);
                    break;
                case PppoeManager.CMD_STOP_PPPOE /*458756*/:
                    PppoeStateMachine.this.replyToMessage(msg, PppoeManager.CMD_STOP_PPPOE_FAILED, 1);
                    break;
            }
            return true;
        }
    }

    static {
        boolean z = true;
        if (SystemProperties.getInt("ro.config.pppoe_enable", 0) != 1) {
            z = false;
        }
        PPPOE_ENABLED = z;
    }

    private PppoeStateMachine(Context context) {
        super(TAG);
        this.mContext = context;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    switch (AnonymousClass3.$SwitchMap$android$net$NetworkInfo$DetailedState[((NetworkInfo) intent.getParcelableExtra("networkInfo")).getDetailedState().ordinal()]) {
                        case 1:
                            PppoeStateMachine.this.mWifiConnected = false;
                            PppoeStateMachine.this.sendMessage(PppoeManager.CMD_STOP_PPPOE);
                            return;
                        case 2:
                            Log.d(PppoeStateMachine.TAG, "onreceive: connected");
                            PppoeStateMachine.this.mWifiConnected = true;
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        this.mIntentFilter.addAction(PppoeManager.PPPOE_IP_STATE_CHANGED_ACTION);
        this.mContext.registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
        addState(this.mDefaultState);
        addState(this.mPppoeUnsupportedState, this.mDefaultState);
        addState(this.mOfflineState, this.mDefaultState);
        addState(this.mDisconnectingStateState, this.mDefaultState);
        addState(this.mConnectState, this.mDefaultState);
        addState(this.mConnectingState, this.mConnectState);
        addState(this.mOnlineState, this.mConnectState);
        if (PPPOE_ENABLED) {
            if (this.mNetworkManagementService == null) {
                Log.d(TAG, "create mNetworkManagementService");
                this.mNetworkManagementService = Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
                this.createNetwok = false;
                try {
                    this.mNetworkManagementService.registerObserver(this.mNetworkManagementEventObserver);
                } catch (RemoteException e) {
                    Log.d(TAG, "Error registering observer :" + e);
                }
            }
            setInitialState(this.mOfflineState);
        } else {
            setInitialState(this.mPppoeUnsupportedState);
        }
        this.mPppoeInfo = new PPPOEInfo();
    }

    public static PppoeStateMachine makePppoeStateMachine(Context context) {
        PppoeStateMachine psm = new PppoeStateMachine(context);
        psm.start();
        return psm;
    }

    public PPPOEInfo getPPPOEInfo() {
        Log.d(TAG, "PppoeStateMachine: getPPPOEInfo");
        if (this.mPppoeRunningTime != 0) {
            this.mPppoeInfo.online_time = SystemClock.elapsedRealtime() - this.mPppoeRunningTime;
        } else {
            this.mPppoeInfo.online_time = 0;
        }
        return this.mPppoeInfo;
    }

    private void createNetworkInfo(String iface, int netId) {
        Log.d(TAG, "createNetworkInfo, iface=" + iface);
        if (this.mNetworkManagementService != null && !this.createNetwok) {
            Log.d(TAG, "createPhysicalNetwork");
            try {
                this.mNetworkManagementService.createPhysicalNetwork(netId, null);
                this.mNetworkManagementService.addInterfaceToNetwork(iface, netId);
                this.createNetwok = true;
            } catch (Exception e) {
                Log.d(TAG, "Error creating network 1001: " + e.getMessage());
            }
        }
    }

    private void removeNetworkInfo(String iface, RouteInfo route, int netId) {
        Log.d(TAG, "removeNetworkInfo, netId=" + netId);
        if (this.mNetworkManagementService != null && this.createNetwok) {
            Log.d(TAG, "removeNetwork");
            try {
                this.mNetworkManagementService.setDefaultNetId(IConnectivityManager.Stub.asInterface(ServiceManager.getService(Context.CONNECTIVITY_SERVICE)).getNetIdForInterface(WLAN_INTERFACE));
                this.mNetworkManagementService.flushNetworkDnsCache(netId);
                this.mNetworkManagementService.removeInterfaceFromNetwork(iface, netId);
                this.mNetworkManagementService.removeNetwork(netId);
                this.createNetwok = false;
            } catch (Exception e) {
                Log.d(TAG, "Error remove network 1001: " + e.getMessage());
            }
            restorePppoeDNSProperity();
        }
    }

    private void updateNetworkInfo(String iface, String localAddress, RouteInfo route, String[] servers, String domains, int netId) {
        String mtu = SystemProperties.get("net.pppoe.mtu");
        Log.d(TAG, "updateNetworkInfo, iface=" + iface);
        if (this.mNetworkManagementService != null && this.createNetwok) {
            try {
                InterfaceConfiguration ifcg = this.mNetworkManagementService.getInterfaceConfig(iface);
                if (ifcg != null) {
                    ifcg.setLinkAddress(new LinkAddress(NetworkUtils.numericToInetAddress(localAddress), 24));
                    ifcg.setInterfaceUp();
                    this.mNetworkManagementService.setInterfaceConfig(iface, ifcg);
                }
                if (mtu != null) {
                    Log.d(TAG, "updateNetworkInterface, set mtu");
                    this.mNetworkManagementService.setMtu(iface, Integer.parseInt(mtu));
                }
                Log.d(TAG, "updateNetworkInfo, route=" + route);
                this.mNetworkManagementService.addRoute(netId, route);
                Log.d(TAG, "updateNetworkDnses");
                this.mNetworkManagementService.setDnsServersForNetwork(netId, servers, domains);
                this.mNetworkManagementService.setDefaultNetId(netId);
            } catch (Exception e) {
                Log.d(TAG, "Error configuring interface " + iface + ", :" + e);
            }
            setPppoeDNSProperity(servers);
        }
    }

    private void setPppoeInfo(Status status) {
        this.mPppoeInfo.status = status;
    }

    private void replyToMessage(Message msg, int what, int arg1) {
        if (msg.replyTo != null) {
            Message dstMsg = obtainMessageWithArg2(msg);
            dstMsg.what = what;
            dstMsg.arg1 = arg1;
            this.mReplyChannel.replyToMessage(msg, dstMsg);
        }
    }

    private Message obtainMessageWithArg2(Message srcMsg) {
        Message msg = Message.obtain();
        msg.arg2 = srcMsg.arg2;
        return msg;
    }

    private void setPppoeDNSProperity(String[] dns) {
        String pppoeState = SystemProperties.get("net.pppoe.state");
        Log.d(TAG, "setPppoeDNS, pppoeState=" + pppoeState);
        if (BatteryManager.EXTRA_ONLINE.equals(pppoeState)) {
            if (!(dns[0] == null || dns[0].equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                this.mOldDns1 = SystemProperties.get("net.dns1");
                SystemProperties.set("net.dns1", dns[0]);
            }
            if (dns[1] != null && !dns[1].equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                this.mOldDns2 = SystemProperties.get("net.dns2");
                SystemProperties.set("net.dns2", dns[1]);
            }
        }
    }

    private void restorePppoeDNSProperity() {
        String pppoeState = SystemProperties.get("net.pppoe.state");
        Log.d(TAG, "restorePppoeDNSProperity, pppoeState=" + pppoeState);
        if ("offline".equals(pppoeState)) {
            if (this.mOldDns1 != null) {
                SystemProperties.set("net.dns1", this.mOldDns1);
                this.mOldDns1 = null;
            }
            if (this.mOldDns2 != null) {
                SystemProperties.set("net.dns2", this.mOldDns2);
                this.mOldDns2 = null;
            }
        }
    }

    private void setPppoeState(String pppoeState) {
        if (this.mContext != null) {
            Intent intent = new Intent(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
            intent.putExtra(PppoeManager.EXTRA_PPPOE_STATE, pppoeState);
            this.mContext.sendBroadcast(intent);
        }
    }

    private void sendPppoeCompletedBroadcast(String resultStatus, String errorCode) {
        Intent intent = new Intent(PppoeManager.PPPOE_COMPLETED_ACTION);
        intent.putExtra(PppoeManager.EXTRA_PPPOE_RESULT_STATUS, resultStatus);
        if (resultStatus.equals(PppoeManager.PPPOE_RESULT_STATUS_FAILURE)) {
            intent.putExtra(PppoeManager.EXTRA_PPPOE_RESULT_ERROR_CODE, errorCode);
        }
        this.mContext.sendBroadcast(intent);
    }
}
