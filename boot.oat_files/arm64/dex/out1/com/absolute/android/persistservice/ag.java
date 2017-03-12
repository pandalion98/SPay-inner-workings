package com.absolute.android.persistservice;

import android.os.Handler;
import android.os.Message;

class ag extends Handler {
    final /* synthetic */ ae a;

    private ag(ae aeVar) {
        this.a = aeVar;
    }

    public synchronized void handleMessage(Message message) {
        if (message.what == 3 && this.a.e) {
            try {
                this.a.f.ping();
                this.a.a.c(this.a.c);
            } catch (Exception e) {
                this.a.a.a(this.a.c, true);
            }
            removeMessages(3);
            sendEmptyMessageDelayed(3, (long) (this.a.d * 1000));
        }
    }
}
