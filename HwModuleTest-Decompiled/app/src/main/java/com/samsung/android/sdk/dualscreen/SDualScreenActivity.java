package com.samsung.android.sdk.dualscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.samsung.android.sdk.dualscreen.SDualScreenConstantsReflector.DualScreenLaunchParams;

public class SDualScreenActivity {
    private static final boolean DEBUG = true;
    public static final int FLAG_COUPLED_TASK_CONTEXTUAL_MODE = DualScreenLaunchParams.FLAG_COUPLED_TASK_CONTEXTUAL_MODE;
    public static final int FLAG_COUPLED_TASK_EXPAND_MODE = DualScreenLaunchParams.FLAG_COUPLED_TASK_EXPAND_MODE;
    private static final String TAG = SDualScreenActivity.class.getSimpleName();
    private static boolean mDualScreenAvailable = false;
    private static boolean mDualScreenAvailableChecked = false;
    private SDualScreenManagerReflector mDualScreenManagerReflector;
    /* access modifiers changed from: private */
    public ScreenChangeListener mScreenChangeListener;

    public enum DualScreen {
        MAIN(SDualScreenManagerReflector.ORDINALS[0]),
        SUB(SDualScreenManagerReflector.ORDINALS[1]),
        EXPANDED(SDualScreenManagerReflector.ORDINALS[2]),
        UNKNOWN(SDualScreenManagerReflector.ORDINALS[3]);
        
        int targetOrdinal;

        private DualScreen(int ordinal) {
            this.targetOrdinal = ordinal;
        }
    }

    public interface ScreenChangeListener {
        void onScreenChanged();
    }

    private SDualScreenActivity() {
    }

    public SDualScreenActivity(Activity activity) {
        if (activity != null) {
            this.mDualScreenManagerReflector = new SDualScreenManagerReflector(activity);
            return;
        }
        throw new NullPointerException("activity is null");
    }

    public DualScreen getScreen() {
        Log.d(TAG, "getScreen()");
        if (isDualScreenAvailable()) {
            DualScreen ret = this.mDualScreenManagerReflector.getScreen();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getScreen() ret=");
            sb.append(ret);
            Log.d(str, sb.toString());
            return ret;
        }
        Log.w(TAG, "DualScreenManager is not loaded");
        return DualScreen.UNKNOWN;
    }

    public void expand() {
        Log.d(TAG, "expand()");
        if (isDualScreenAvailable()) {
            this.mDualScreenManagerReflector.moveToScreen(DualScreen.EXPANDED);
        } else {
            Log.w(TAG, "DualScreenManager is not loaded");
        }
    }

    public void collapse() {
        Log.d(TAG, "collapse()");
        if (isDualScreenAvailable()) {
            boolean ret = this.mDualScreenManagerReflector.moveToScreen(DualScreen.MAIN);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("collapse() ret=");
            sb.append(ret);
            Log.d(str, sb.toString());
            return;
        }
        Log.w(TAG, "DualScreenManager is not loaded");
    }

    public void switchScreen() {
        Log.d(TAG, "switchScreen()");
        if (isDualScreenAvailable()) {
            this.mDualScreenManagerReflector.switchScreen();
        } else {
            Log.w(TAG, "DualScreenManager is not loaded");
        }
    }

    public void swapTopTask() {
        Log.d(TAG, "swapTopTask()");
        if (isDualScreenAvailable()) {
            this.mDualScreenManagerReflector.swapTopTask();
        } else {
            Log.w(TAG, "DualScreenManager is not loaded");
        }
    }

    public static Intent makeIntent(Context context, Intent intent, DualScreen targetScreen, int flags) {
        Log.d(TAG, "makeIntent()");
        Intent intent2 = intent;
        Intent ret = SDualScreenManagerReflector.makeIntent(context, intent, targetScreen, flags);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("makeIntent() : ret=");
        sb.append(ret);
        Log.d(str, sb.toString());
        return ret;
    }

    public static DualScreen getFocusedScreen() {
        Log.d(TAG, "getFocusedScreen()");
        return SDualScreenManagerReflector.getFocusedScreen();
    }

    public SDualScreenCoupledTaskInfo getCoupledTaskInfo() {
        Log.d(TAG, "getCoupledTaskInfo()");
        return this.mDualScreenManagerReflector.getCoupledTaskInfo();
    }

    public void setScreenChangeListener(ScreenChangeListener listener) {
        Log.d(TAG, "setScreenChangeListener()");
        if (isDualScreenAvailable()) {
            this.mScreenChangeListener = listener;
            if (this.mScreenChangeListener != null) {
                this.mDualScreenManagerReflector.setScreenChangedListner(new com.samsung.android.sdk.dualscreen.SDualScreenListener.ScreenChangeListener() {
                    public void onScreenChanged() {
                        SDualScreenActivity.this.mScreenChangeListener.onScreenChanged();
                    }
                });
                return;
            }
            return;
        }
        Log.w(TAG, "DualScreenManager is not loaded");
    }

    private boolean isDualScreenAvailable() {
        if (mDualScreenAvailableChecked) {
            return mDualScreenAvailable;
        }
        mDualScreenAvailable = this.mDualScreenManagerReflector != null && this.mDualScreenManagerReflector.initialized();
        mDualScreenAvailableChecked = DEBUG;
        return mDualScreenAvailable;
    }
}
