package com.absolute.android.persistservice;

import android.content.Intent;
import android.os.Looper;
import android.util.TimedRemoteCaller;

class a implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ ABTPersistenceService b;

    a(ABTPersistenceService aBTPersistenceService, int i) {
        this.b = aBTPersistenceService;
        this.a = i;
    }

    public void run() {
        Looper.prepare();
        try {
            Thread.sleep(TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
            int i = 5;
            this.b.i.c("Start sending PS_READY intents");
            for (int i2 = 0; i2 <= this.a && this.b.j.b() == 0; i2 += i) {
                Intent intent = new Intent("com.absolute.action.PS_READY");
                intent.addFlags(32);
                this.b.g.sendBroadcast(intent);
                if (i2 >= 60) {
                    i = 20;
                }
                Thread.sleep((long) (i * 1000));
            }
            this.b.i.c("Stop sending PS_READY intents");
        } catch (InterruptedException e) {
        }
    }
}
