package com.absolute.android.persistservice;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.os.Handler;
import android.os.Message;
import java.util.List;

class o extends Handler {
    final /* synthetic */ n a;

    private o(n nVar) {
        this.a = nVar;
    }

    public void handleMessage(Message message) {
        if (message.what == 2) {
            String str = (String) message.obj;
            Integer num = (Integer) this.a.b.get(str);
            if (num == null) {
                this.a.a.a.c("Application: " + str + " is no longer being monitored via running services check.");
                return;
            }
            List<RunningServiceInfo> runningServices = ((ActivityManager) this.a.a.c.getSystemService("activity")).getRunningServices(1000);
            Boolean valueOf = Boolean.valueOf(false);
            Boolean bool = valueOf;
            for (RunningServiceInfo runningServiceInfo : runningServices) {
                if (runningServiceInfo.process.contains(str)) {
                    valueOf = Boolean.valueOf(true);
                } else {
                    valueOf = bool;
                }
                bool = valueOf;
            }
            if (bool.booleanValue()) {
                this.a.a.c(str);
            } else {
                this.a.a.a(str, true);
            }
            removeMessages(2, str);
            sendMessageDelayed(obtainMessage(2, str), ((long) num.intValue()) * 1000);
        }
    }
}
