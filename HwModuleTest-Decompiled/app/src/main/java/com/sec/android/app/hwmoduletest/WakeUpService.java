package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class WakeUpService extends BroadcastReceiver {
    private static final String TAG = "WakeUpService";
    private final String ACTION_WAKE_UP = "com.sec.factory.WakeUp";
    private WakeLock mWakeUp = null;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        boolean isAcquireWakelock = intent.getExtras().getBoolean("isAcquireWakelock");
        StringBuilder sb = new StringBuilder();
        sb.append("onReceive action=");
        sb.append(action);
        LtUtil.log_d(TAG, "onReceive", sb.toString());
        if (action.equals("com.sec.factory.WakeUp") && this.mWakeUp == null) {
            PowerManager pm = (PowerManager) context.getSystemService("power");
            this.mWakeUp = pm.newWakeLock(805306394, TAG);
            if (!pm.isScreenOn() && !this.mWakeUp.isHeld()) {
                this.mWakeUp.acquire();
                LtUtil.log_i(TAG, "ACTION_WAKE_UP", "Wake up by sensorHub test cmd");
                if (!isAcquireWakelock) {
                    this.mWakeUp.release();
                    LtUtil.log_i(TAG, "ACTION_WAKE_UP", "FULL WAKELOCK OFF");
                }
                this.mWakeUp = null;
            }
        }
    }
}
