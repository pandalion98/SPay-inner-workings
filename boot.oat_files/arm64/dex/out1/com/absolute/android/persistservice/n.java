package com.absolute.android.persistservice;

import android.os.Looper;
import java.util.Hashtable;

class n extends Thread {
    final /* synthetic */ l a;
    private Hashtable b = new Hashtable();
    private o c;

    protected n(l lVar) {
        this.a = lVar;
        super("RunningServicesWorkerThread");
    }

    public void run() {
        Looper.prepare();
        synchronized (this) {
            this.c = new o();
            notify();
        }
        Looper.loop();
    }

    protected void a() {
        synchronized (this) {
            while (this.c == null) {
                try {
                    wait();
                } catch (Throwable e) {
                    this.a.a.a("Ping Thread Interrupted while waiting on handler.", e);
                }
            }
        }
    }

    protected synchronized void a(String str, int i) {
        this.c.removeMessages(2, str);
        this.b.put(str, Integer.valueOf(i));
        this.a.a.c("Starting running services monitoring check for: " + str + ", with interval: " + i + " secs.");
        this.c.sendMessageDelayed(this.c.obtainMessage(2, str), ((long) i) * 1000);
    }

    protected synchronized void a(String str) {
        this.b.remove(str);
    }

    protected int b(String str) {
        Integer num = (Integer) this.b.get(str);
        return num != null ? num.intValue() : 0;
    }
}
