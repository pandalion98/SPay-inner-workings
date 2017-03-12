package com.absolute.android.persistservice;

import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import com.absolute.android.persistence.IABTPing;
import com.samsung.android.smartface.SmartFaceManager;

public class ae extends Thread implements DeathRecipient {
    private l a;
    private v b;
    private String c;
    private int d;
    private boolean e = false;
    private IABTPing f;
    private ag g;

    ae(l lVar, String str, IABTPing iABTPing, int i) {
        super("PingWorkerThread_" + str);
        this.a = lVar;
        this.b = lVar.a;
        this.c = str;
        this.f = iABTPing;
        this.d = i;
    }

    protected void a() {
        c();
        synchronized (this) {
            try {
                this.f.asBinder().linkToDeath(this, 0);
            } catch (Throwable e) {
                this.b.a("Unable to bind to IABTPing interface of application " + this.c + " to register for death of recipient. Already dead?", e);
                this.a.a(this.c, true);
            }
            if (this.g != null) {
                this.g.sendEmptyMessageDelayed(3, (long) (this.d * 1000));
            }
            this.e = true;
        }
    }

    private void c() {
        synchronized (this) {
            while (this.g == null) {
                try {
                    wait();
                } catch (Throwable e) {
                    this.b.a("Ping Thread Interrupted while waiting on handler.", e);
                }
            }
        }
    }

    protected synchronized void b() {
        if (this.g != null) {
            this.g.removeMessages(3);
            this.g.getLooper().quit();
        }
        this.f.asBinder().unlinkToDeath(this, 0);
        this.e = false;
    }

    public void run() {
        Looper.prepare();
        synchronized (this) {
            this.g = new ag();
            notify();
        }
        Looper.loop();
    }

    public void binderDied() {
        this.b.b("ABTPersistenceService Ping Thread got 'binderDied' notification for application " + this.c);
        this.a.a(this.c, true);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Enabled = " + (this.e ? SmartFaceManager.TRUE : SmartFaceManager.FALSE) + " : ");
        stringBuilder.append("Ping interval = " + this.d + " secs");
        return stringBuilder.toString();
    }
}
