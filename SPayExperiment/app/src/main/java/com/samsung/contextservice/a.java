/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class a {
    private HandlerThread Gx = null;
    protected Context mContext = null;
    private Handler mHandler = null;

    protected a(Context context, String string) {
        this.mContext = context;
        if (this.Gx == null) {
            this.Gx = new HandlerThread(string, 10);
            this.Gx.start();
        }
        this.onCreate();
    }

    public abstract void c(Message var1);

    protected final Handler getHandler() {
        return this.mHandler;
    }

    public void onCreate() {
        this.mHandler = new Handler(this.Gx.getLooper()){

            public void handleMessage(Message message) {
                a.this.c(message);
            }
        };
    }

}

