package com.samsung.contextservice;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/* renamed from: com.samsung.contextservice.a */
public abstract class Manager {
    private HandlerThread Gx;
    protected Context mContext;
    private Handler mHandler;

    /* renamed from: com.samsung.contextservice.a.1 */
    class Manager extends Handler {
        final /* synthetic */ Manager Gy;

        Manager(Manager manager, Looper looper) {
            this.Gy = manager;
            super(looper);
        }

        public void handleMessage(Message message) {
            this.Gy.m1398c(message);
        }
    }

    public abstract void m1398c(Message message);

    protected Manager(Context context, String str) {
        this.Gx = null;
        this.mHandler = null;
        this.mContext = null;
        this.mContext = context;
        if (this.Gx == null) {
            this.Gx = new HandlerThread(str, 10);
            this.Gx.start();
        }
        onCreate();
    }

    public void onCreate() {
        this.mHandler = new Manager(this, this.Gx.getLooper());
    }

    protected final Handler getHandler() {
        return this.mHandler;
    }
}
