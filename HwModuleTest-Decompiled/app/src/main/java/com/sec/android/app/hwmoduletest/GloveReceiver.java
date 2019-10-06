package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/* compiled from: TspPatternStyleX */
class GloveReceiver extends BroadcastReceiver {
    public static final String CLASS_NAME = "GloveReceiver";
    public static final String INTENT_GLOVE = "com.samsung.glove.ENABLE";
    public static final String INTENT_GLOVE_EXTRA = "gloveEnable";
    private static IGloverEventHandler mHandler;
    private static GloveReceiver mInstance;
    private static boolean mIsEnable = false;

    /* compiled from: TspPatternStyleX */
    interface IGloverEventHandler {
        void onGloveEnableChanged(boolean z);
    }

    GloveReceiver() {
    }

    public static boolean getEnable() {
        return mIsEnable;
    }

    public static void setHandler(IGloverEventHandler l) {
        mHandler = l;
    }

    public void onReceive(Context context, Intent intent) {
        if (INTENT_GLOVE.equals(intent.getAction())) {
            boolean isEnable = intent.getBooleanExtra(INTENT_GLOVE_EXTRA, false);
            mIsEnable = isEnable;
            String str = CLASS_NAME;
            StringBuilder sb = new StringBuilder();
            sb.append("onReceive com.samsung.glove.ENABLE isEnable: ");
            sb.append(isEnable);
            Log.d(str, sb.toString());
            if (mHandler != null) {
                mHandler.onGloveEnableChanged(isEnable);
            }
        }
    }

    public static GloveReceiver getInstance() {
        if (mInstance == null) {
            mInstance = new GloveReceiver();
        }
        return mInstance;
    }

    public static void registerBroadcastReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_GLOVE);
        context.registerReceiver(getInstance(), filter);
    }

    public static void unregisterBroadcastReceiver(Context context) {
        if (mInstance != null) {
            context.unregisterReceiver(mInstance);
        }
    }
}
