package com.sec.android.app.hwmoduletest.modules;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.HashSet;

public class ModuleObject {
    protected static Context mContext = null;
    private static HashSet<BroadcastReceiver> mRegistedReceiverSet = new HashSet<>();
    protected String CLASS_NAME;

    protected ModuleObject(Context context) {
        this(context, "ModuleObject");
    }

    protected ModuleObject(Context context, String className) {
        this.CLASS_NAME = "ModuleObject";
        mContext = context;
        this.CLASS_NAME = className;
    }

    /* access modifiers changed from: protected */
    public Object getSystemService(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("service=");
        sb.append(name);
        LtUtil.log_i(this.CLASS_NAME, "getSystemService", sb.toString());
        return mContext.getSystemService(name);
    }

    /* access modifiers changed from: protected */
    public Context getApplicationContext() {
        return mContext;
    }

    /* access modifiers changed from: protected */
    public ContentResolver getContentResolver() {
        return mContext.getContentResolver();
    }

    /* access modifiers changed from: protected */
    public void sendBroadcast(Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("intent=");
        sb.append(intent.toString());
        LtUtil.log_i(this.CLASS_NAME, "sendBroadcast", sb.toString());
        mContext.sendBroadcast(intent);
    }

    /* access modifiers changed from: protected */
    public void startActivity(Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("activity=");
        sb.append(intent.getClass().getName());
        LtUtil.log_i(this.CLASS_NAME, "startActivity", sb.toString());
        mContext.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void startService(Intent service) {
        StringBuilder sb = new StringBuilder();
        sb.append("service=");
        sb.append(service.getClass().getName());
        LtUtil.log_i(this.CLASS_NAME, "startService", sb.toString());
        mContext.startService(service);
    }

    /* access modifiers changed from: protected */
    public void stopService(Intent service) {
        StringBuilder sb = new StringBuilder();
        sb.append("service=");
        sb.append(service.getClass().getName());
        LtUtil.log_i(this.CLASS_NAME, "stopService", sb.toString());
        mContext.stopService(service);
    }

    /* access modifiers changed from: protected */
    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        mContext.registerReceiver(receiver, filter);
        mRegistedReceiverSet.add(receiver);
    }

    /* access modifiers changed from: protected */
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (mRegistedReceiverSet.contains(receiver)) {
            mContext.unregisterReceiver(receiver);
            mRegistedReceiverSet.remove(receiver);
        }
    }
}
