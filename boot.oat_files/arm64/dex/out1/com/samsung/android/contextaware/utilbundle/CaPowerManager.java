package com.samsung.android.contextaware.utilbundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.WindowManager;
import com.android.internal.os.SMProviderContract;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CaPowerManager implements IApPowerObservable, IUtilManager {
    private static final int AP_IS_SLEEP = 4099;
    private static final int AP_IS_WAKEUP = 4100;
    private static final int LOG_CONTEXT_NULL = 4097;
    private static final int LOG_INTENT_NULL = 4098;
    private static final int POWER_IS_CONNECTED = 4101;
    private static final int POWER_IS_DISCONNECTED = 4102;
    private static volatile CaPowerManager instance;
    private WakeLock mAPWakeLock;
    private Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            long timeStamp = System.currentTimeMillis();
            if (msg.what == CaPowerManager.LOG_CONTEXT_NULL) {
                CaLogger.info("context is null");
            } else if (msg.what == 4098) {
                CaLogger.info("intent is null");
            } else if (msg.what == CaPowerManager.AP_IS_SLEEP) {
                CaLogger.info("AP_SLEEP");
                CaPowerManager.this.notifyApPowerObserver(-46, timeStamp);
                CaPowerManager.this.sendApStatusToSensorHub(-46);
                CaTimeManager.getInstance().sendCurTimeToSensorHub();
            } else if (msg.what == CaPowerManager.AP_IS_WAKEUP) {
                CaLogger.info("AP_WAKEUP");
                CaPowerManager.this.notifyApPowerObserver(-47, timeStamp);
                CaPowerManager.this.sendApStatusToSensorHub(-47);
            } else if (msg.what == CaPowerManager.POWER_IS_CONNECTED) {
                CaLogger.info("POWER_CONNECTED");
                CaPowerManager.this.sendApStatusToSensorHub(-42);
            } else if (msg.what == CaPowerManager.POWER_IS_DISCONNECTED) {
                CaLogger.info("POWER_DISCONNECTED");
                CaPowerManager.this.sendApStatusToSensorHub(-41);
            }
        }
    };
    private final CopyOnWriteArrayList<IApPowerObserver> mListeners = new CopyOnWriteArrayList();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        private static final String AP_SLEEP = "android.intent.action.SCREEN_OFF";
        private static final String AP_WAKEUP = "android.intent.action.SCREEN_ON";

        public final void onReceive(Context context, Intent intent) {
            if (context == null) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.LOG_CONTEXT_NULL);
            } else if (intent == null) {
                CaPowerManager.this.mHandler.sendEmptyMessage(4098);
            } else if (intent.getAction().equals(AP_SLEEP)) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.AP_IS_SLEEP);
            } else if (intent.getAction().equals(AP_WAKEUP)) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.AP_IS_WAKEUP);
            } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.POWER_IS_CONNECTED);
            } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                CaPowerManager.this.mHandler.sendEmptyMessage(CaPowerManager.POWER_IS_DISCONNECTED);
            }
        }
    };

    public static CaPowerManager getInstance() {
        if (instance == null) {
            synchronized (CaPowerManager.class) {
                if (instance == null) {
                    instance = new CaPowerManager();
                }
            }
        }
        return instance;
    }

    public final void initializeManager(Context context) {
        if (context == null) {
            CaLogger.error("Context is null");
            return;
        }
        this.mContext = context;
        IntentFilter apSleepIntent = new IntentFilter("android.intent.action.SCREEN_OFF");
        IntentFilter apWakeupIntent = new IntentFilter("android.intent.action.SCREEN_ON");
        IntentFilter powConnectedIntent = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        IntentFilter powDisconnectedIntent = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.mContext.registerReceiver(this.mReceiver, apSleepIntent);
        this.mContext.registerReceiver(this.mReceiver, apWakeupIntent);
        this.mContext.registerReceiver(this.mReceiver, powConnectedIntent);
        this.mContext.registerReceiver(this.mReceiver, powDisconnectedIntent);
    }

    public final void terminateManager() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    private void sendApStatusToSensorHub(int status) {
        int result = SensorHubCommManager.getInstance().sendCmdToSensorHub(new byte[]{(byte) status, (byte) 0}, ISensorHubCmdProtocol.INST_LIB_NOTI, (byte) 13);
        if (result != SensorHubErrors.SUCCESS.getCode()) {
            CaLogger.error(SensorHubErrors.getMessage(result));
        }
    }

    public final void acquireAPWakeLock() {
        if (this.mContext == null) {
            CaLogger.error("mContext is null");
            return;
        }
        PowerManager pm = (PowerManager) this.mContext.getSystemService(SMProviderContract.KEY_POWER);
        if (pm == null) {
            CaLogger.error("pm is null");
        } else if (this.mAPWakeLock == null || !this.mAPWakeLock.isHeld()) {
            CaLogger.trace();
            this.mAPWakeLock = pm.newWakeLock(1, "CA_WAKELOCK");
            this.mAPWakeLock.acquire();
        } else {
            CaLogger.warning("WakeLock is already held.");
        }
    }

    public final void releaseAPWakeLock() {
        if (this.mAPWakeLock == null || !this.mAPWakeLock.isHeld()) {
            CaLogger.warning("WakeLock is not held.");
            return;
        }
        CaLogger.trace();
        this.mAPWakeLock.release();
        this.mAPWakeLock = null;
    }

    public final void registerApPowerObserver(IApPowerObserver observer) {
        if (!this.mListeners.contains(observer)) {
            this.mListeners.add(observer);
        }
    }

    public final void unregisterApPowerObserver(IApPowerObserver observer) {
        if (this.mListeners.contains(observer)) {
            this.mListeners.remove(observer);
        }
    }

    public final void notifyApPowerObserver(int status, long timeStamp) {
        Iterator<IApPowerObserver> i = this.mListeners.iterator();
        while (i.hasNext()) {
            IApPowerObserver observer = (IApPowerObserver) i.next();
            if (observer != null) {
                observer.initializePreparedSubCollection();
                observer.updateApPowerStatus(status, timeStamp);
            }
        }
    }

    public final boolean isScreenOn() {
        boolean z = true;
        if (this.mContext == null) {
            CaLogger.error("mContext is null");
            return false;
        }
        int screenStatus = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getState();
        if (screenStatus == 1) {
            CaLogger.debug("Screen Off.");
        } else {
            CaLogger.debug("Screen On.");
        }
        if (screenStatus == 1) {
            z = false;
        }
        return z;
    }
}
