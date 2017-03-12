package com.samsung.android.toolbox;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings.System;
import android.util.Log;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.toolbox.ITwToolBoxService.Stub;

public class TwToolBoxService extends Stub {
    public static final String ACTION_ASSISTIVE_LIGHT_OFF = "com.samsung.intent.action.ASSISTIVELIGHT_OFF";
    public static final String ACTION_ASSISTIVE_LIGHT_ON = "com.samsung.intent.action.ASSISTIVELIGHT_ON";
    public static final String ACTION_EARPHONE = "android.intent.action.HEADSET_PLUG";
    public static final String ACTION_LOCK_TASK_MODE = "com.samsung.android.action.LOCK_TASK_MODE";
    public static final String SETTINGS_CAR_MODE = "car_mode_on";
    public static final String SETTINGS_KIDS_MODE = "kids_home_mode";
    public static final String SETTINGS_SHOW_TOOLBOX = "toolbox_onoff";
    public static final String SETTINGS_SHOW_TOOLBOX_EARPHONES_ONLY = "toolbox_earphones_only";
    public static final String SETTINGS_SHOW_TOOLBOX_FLOATING_X_RATIO = "tw_tool_box_floating_x_ratio";
    public static final String SETTINGS_SHOW_TOOLBOX_FLOATING_Y_RATIO = "tw_tool_box_floating_y_ratio";
    public static final String SETTINGS_TOOLBOX_PACKAGE_LIST = "toolbox_apps";
    public static final String SETTINGS_TOOLBOX_PACKAGE_LIST_DEFAULT = "0;com.sec.android.app.camera/00;com.sec.android.app.sbrowser/00;com.sec.android.app.voicenote/00;com.android.contacts/00;com.samsung.android.app.memo/00";
    private static String TAG = "toolbox";
    public static final int TOOLBOX_FUNCTION_SHORTCUT_ACTION_MEMO = 5;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_MAGNIFIER = 4;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_QUICK_CONNECT = 1;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_SCRAP_BOOK = 6;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_SCREEN_WRITE = 3;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_S_FINDER = 0;
    public static final int TOOLBOX_FUNCTION_SHORTCUT_TORCH_LIGHT = 2;
    public static final int TOOLBOX_MESSAGE_OPTION_FADE_IN = 2;
    public static final int TOOLBOX_MESSAGE_OPTION_FADE_OUT = 4;
    public static final int TOOLBOX_MESSAGE_OPTION_FORCE_NO_SHOW = 32;
    public static final int TOOLBOX_MESSAGE_OPTION_WINDOW_FOCUS_OFF = 16;
    public static final int TOOLBOX_MESSAGE_OPTION_WINDOW_FOCUS_ON = 8;
    public static final int TOOLBOX_MESSAGE_VISIBILITY = 1;
    public static final int TOOLBOX_MESSAGE_WINDOW_DETACHED_FROM_WINDOW = 3;
    public static final int TOOLBOX_MESSAGE_WINDOW_FOCUS_CHANGED = 2;
    public static final boolean TOOLBOX_SUPPORT = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_SETTINGS_DEFAULT_TOOLBOX");
    private ActivityManager mActivityManager;
    final RemoteCallbackList<ITwToolBoxServiceCallback> mCallbacks = new RemoteCallbackList();
    private ContentResolver mContentResolver;
    private final Handler mH = new Handler();
    private String mPackageList;
    ContentObserver mPackageListObserver;
    private PackageManager mPackageManager;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                Log.d(TwToolBoxService.TAG, "TwToolBoxService ACTION_USER_SWITCHED");
                TwToolBoxService.this.mPackageList = System.getStringForUser(TwToolBoxService.this.mContentResolver, TwToolBoxService.SETTINGS_TOOLBOX_PACKAGE_LIST, -2);
            }
        }
    };

    public TwToolBoxService(Context context) {
        this.mContentResolver = context.getContentResolver();
        this.mPackageManager = context.getPackageManager();
        this.mActivityManager = (ActivityManager) context.getSystemService("activity");
        this.mPackageList = System.getStringForUser(this.mContentResolver, SETTINGS_TOOLBOX_PACKAGE_LIST, -2);
        if (this.mPackageList == null) {
            this.mPackageList = SETTINGS_TOOLBOX_PACKAGE_LIST_DEFAULT;
        }
        this.mPackageListObserver = new ContentObserver(this.mH) {
            public void onChange(boolean selfChange) {
                TwToolBoxService.this.mPackageList = System.getStringForUser(TwToolBoxService.this.mContentResolver, TwToolBoxService.SETTINGS_TOOLBOX_PACKAGE_LIST, -2);
            }
        };
        this.mContentResolver.registerContentObserver(System.getUriFor(SETTINGS_TOOLBOX_PACKAGE_LIST), false, this.mPackageListObserver, -1);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.USER_SWITCHED");
        context.registerReceiver(this.mReceiver, filter);
    }

    public boolean unregisterCallback(ITwToolBoxServiceCallback callback) throws RemoteException {
        return callback == null ? false : this.mCallbacks.unregister(callback);
    }

    public boolean registerCallback(ITwToolBoxServiceCallback callback) throws RemoteException {
        return callback == null ? false : this.mCallbacks.register(callback);
    }

    public boolean isContain(int x, int y) throws RemoteException {
        boolean result;
        synchronized (this.mCallbacks) {
            if (this.mCallbacks.beginBroadcast() > 0) {
                result = ((ITwToolBoxServiceCallback) this.mCallbacks.getBroadcastItem(0)).isContain(x, y);
            } else {
                result = false;
            }
            this.mCallbacks.finishBroadcast();
        }
        return result;
    }

    public void sendMessage(String pkg, int message, int value) throws RemoteException {
        synchronized (this.mCallbacks) {
            if (this.mCallbacks.beginBroadcast() > 0) {
                ((ITwToolBoxServiceCallback) this.mCallbacks.getBroadcastItem(0)).receiveMessage(pkg, message, value);
            }
            this.mCallbacks.finishBroadcast();
        }
    }

    public String getToolList() {
        Log.d(TAG, "TwToolBoxService getToolList()");
        return this.mPackageList;
    }

    public void setToolList(String toolList) {
        Log.d(TAG, "TwToolBoxService setToolList() Deprecated.");
    }
}
