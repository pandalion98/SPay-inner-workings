package com.samsung.android.dualscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Singleton;
import android.view.WindowManager;
import com.samsung.android.multidisplay.dualscreen.DualScreenUtils;
import com.samsung.android.multidisplay.dualscreen.IDualScreenCallbacks;
import com.samsung.android.multidisplay.dualscreen.IDualScreenManager;
import com.samsung.android.multidisplay.dualscreen.IDualScreenManager.Stub;
import com.samsung.android.sdk.dualscreen.SDualScreenListener.ScreenFocusChangeListener;

public class DualScreenManager {
    public static final String BUNDLE_KEY_ACTIVITY_OPTION = "dualscreen:activityoption";
    public static final String BUNDLE_KEY_DISPLAYID = "dualscreen:displayid";
    public static final String BUNDLE_KEY_NO_ANIM = "dualscreen:noanim";
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_AM;
    public static final boolean DEBUG_VERBOSE;
    public static final boolean DEBUG_WARNING;
    public static final boolean DEBUG_WM;
    public static final int EXPAND_REQUEST_REASON_EXTERNAL_APP_TRIGGER = 201;
    public static final int EXPAND_REQUEST_REASON_OUTSPREAD_FROM_FOLDED = 202;
    public static final int FLAG_FINISH_ALL_ABOVE_CHOSEN_ACTIVITY = 4096;
    public static final int FLAG_FINISH_CALLED_ACTIVITIES = 512;
    public static final int FLAG_FINISH_REMAIN_TOP_ACTIVITY = 8192;
    public static final int FLAG_FINISH_SOURCE_ACTIVITY = 1024;
    public static final int FLAG_FINISH_TASK = 256;
    public static final int FLAG_FINISH_TOP_ACTIVITY = 2048;
    public static final String INTENT_EXTRA_COUPLED = "dualscreen:coupled";
    public static final String INTENT_EXTRA_SCREEN = "dualscreen:screen";
    private static final int REQUIRED_MINIMUM_SDK_VERSION_CODE = 8;
    private static final String REQUIRED_MINIMUM_SDK_VERSION_NAME = "0.8";
    public static final boolean SAFE_DEBUG = (Debug.isProductShip() != 1);
    public static final int SHRINK_REQUEST_REASON_EXTERNAL_APP_TRIGGER = 103;
    public static final int SHRINK_REQUEST_REASON_HOME_LAUNCH = 104;
    public static final int SHRINK_REQUEST_REASON_NEW_TASK_LAUNCH = 102;
    public static final int SHRINK_REQUEST_REASON_SCREEN_OFF = 101;
    public static final String TAG = DualScreenManager.class.getSimpleName();
    public static final String TAG_DUALSCREEN = "[DUALSCREEN]";
    public static final int TRANSIT_EXPAND = 1;
    public static final int TRANSIT_SHRINK = 2;
    private static final int VERSION_CODE = 5;
    private static final String VERSION_NAME = "0.5";
    private static final Singleton<IDualScreenManager> gDefault = new Singleton<IDualScreenManager>() {
        protected IDualScreenManager create() {
            IBinder b = ServiceManager.getService("dualscreen");
            if (DualScreenManager.DEBUG) {
                Log.d(DualScreenManager.TAG, "default service binder = " + b);
            }
            IDualScreenManager service = Stub.asInterface(b);
            if (DualScreenManager.DEBUG) {
                Log.d(DualScreenManager.TAG, "default service = " + service);
            }
            return service;
        }
    };
    private Context mContext = null;
    final IDualScreenCallbacks mDualScreenCallbacks = new IDualScreenCallbacks.Stub() {
        public void screenFocusChanged(DualScreen screen) throws RemoteException {
            ListenerInfo li = DualScreenManager.this.mListenerInfo;
            if (li != null && li.mScreenFocusChangeListener != null) {
                li.mScreenFocusChangeListener.onScreenFocusChanged(screen);
            }
        }
    };
    ListenerInfo mListenerInfo;

    static class ListenerInfo {
        protected ScreenFocusChangeListener mScreenFocusChangeListener;

        ListenerInfo() {
        }
    }

    static {
        boolean z;
        boolean z2 = true;
        if (DEBUG) {
            z = true;
        } else {
            z = false;
        }
        DEBUG_WM = z;
        if (DEBUG) {
            z = true;
        } else {
            z = false;
        }
        DEBUG_AM = z;
        if (DEBUG) {
            z = true;
        } else {
            z = false;
        }
        DEBUG_VERBOSE = z;
        if (!DEBUG) {
            z2 = false;
        }
        DEBUG_WARNING = z2;
    }

    private static IDualScreenManager getDefault() {
        return (IDualScreenManager) gDefault.get();
    }

    private DualScreenManager() {
    }

    public DualScreenManager(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        if (DEBUG) {
            Log.d(TAG, "DualScreenManager() : context=" + context.getClass().getName() + " @0x" + Integer.toHexString(System.identityHashCode(this)) + " caller=" + Debug.getCaller());
        }
        this.mContext = context;
    }

    public boolean canBeCoupled() {
        if (DEBUG) {
            Log.d(TAG, "canBeCoupled()");
        }
        try {
            if (this.mContext instanceof Activity) {
                return getDefault().canBeCoupled(this.mContext.getActivityToken());
            }
            Log.e(TAG, "canBeCoupled() is called from non-Activity context");
            throw new IllegalArgumentException("canBeCoupled() should be called from Activity context");
        } catch (RemoteException e) {
            warningException(e);
            return false;
        }
    }

    public static boolean canBeExpanded(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "canBeExpanded()");
        }
        try {
            return getDefault().canBeExpanded(taskId);
        } catch (RemoteException e) {
            warningException(e);
            return false;
        }
    }

    public void dimScreen(DualScreen screen, boolean enable) {
        if (DEBUG) {
            Log.d(TAG, "dimScreen() : screen=" + screen + ", enable=" + enable);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        } else if (this.mContext instanceof Activity) {
            try {
                if (this.mContext instanceof Activity) {
                    getDefault().dimScreen(this.mContext.getActivityToken(), screen, enable);
                }
            } catch (RemoteException e) {
                warningException(e);
            }
        } else {
            throw new IllegalArgumentException("context should be an Activity instance");
        }
    }

    public static void fixTask(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "fixTask() : taskId=" + taskId);
        }
        if (taskId < 0) {
            throw new IllegalArgumentException("taskId < 0");
        }
        try {
            getDefault().fixTask(taskId);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static void unfixTask(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "unfixTask() : taskId=" + taskId);
        }
        if (taskId < 0) {
            throw new IllegalArgumentException("taskId < 0");
        }
        try {
            getDefault().unfixTask(taskId);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static void fixTopTask(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "pinUp() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            getDefault().fixTopTask(screen);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void focusScreen(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "focusScreen() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            if (this.mContext instanceof Activity) {
                getDefault().focusScreen(this.mContext.getActivityToken(), screen);
            }
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static void forceFocusScreen(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "forceFocusScreen() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            getDefault().forceFocusScreen(screen);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static DualScreen getFocusedScreen() {
        if (DEBUG) {
            Log.d(TAG, "getFocusedScreen()");
        }
        DualScreen ret = DualScreen.UNKNOWN;
        try {
            ret = getDefault().getFocusedScreen();
        } catch (RemoteException e) {
            warningException(e);
        }
        Log.d(TAG, "getFocusedScreen() ret=" + ret);
        return ret;
    }

    public static int getFrameworkVersionCode() {
        if (DEBUG_VERBOSE) {
            Log.v(TAG, "getFrameworkVersionCode() : 5");
        }
        return 5;
    }

    public static String getFrameworkVersionName() {
        if (DEBUG_VERBOSE) {
            Log.v(TAG, "getFrameworkVersionName() : 0.5");
        }
        return VERSION_NAME;
    }

    public int getOrientation() {
        if (DEBUG) {
            Log.d(TAG, "getOrientation()");
        }
        if (this.mContext != null) {
            return getOrientation(DualScreenUtils.displayIdToScreen(this.mContext.getDisplayId()));
        }
        return 0;
    }

    public static int getOrientation(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "getOrientation() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            return getDefault().getOrientation(screen);
        } catch (RemoteException e) {
            warningException(e);
            return 0;
        }
    }

    public static int getRequiredMinimumSdkVersionCode() {
        if (DEBUG_VERBOSE) {
            Log.v(TAG, "getRequiredMinimumSdkVersionCode() : 8");
        }
        return 8;
    }

    public static String getRequiredMinimumSdkVersionName() {
        if (DEBUG_VERBOSE) {
            Log.v(TAG, "getRequiredMinimumSdkVersionName() : 0.8");
        }
        return REQUIRED_MINIMUM_SDK_VERSION_NAME;
    }

    public DualScreen getScreen() {
        if (DEBUG) {
            Log.d(TAG, "getScreen()");
        }
        if (this.mContext != null) {
            DualScreen screen = DualScreenUtils.displayIdToScreen(this.mContext.getDisplayId());
            if (!DEBUG) {
                return screen;
            }
            Log.d(TAG, "getScreen() ret=" + screen);
            return screen;
        }
        if (DEBUG) {
            Log.w(TAG, "getScreen() ret=" + DualScreen.UNKNOWN);
        }
        return DualScreen.UNKNOWN;
    }

    public static DualScreen getScreen(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "getScreen() : taskId=" + taskId);
        }
        if (taskId < 0) {
            throw new IllegalArgumentException("invalid taskId");
        }
        try {
            return getDefault().getScreen(taskId);
        } catch (RemoteException e) {
            warningException(e);
            return DualScreen.UNKNOWN;
        }
    }

    public static boolean getShrinkRequested(Context context) {
        if (DEBUG) {
            Log.d(TAG, "getShrinkRequested()");
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        boolean ret = context.getShrinkRequested();
        if (DEBUG) {
            Log.d(TAG, "getShrinkRequested() ret=" + ret);
        }
        return ret;
    }

    public static TaskInfo getTaskInfo(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "getTaskInfo() : taskId=" + taskId);
        }
        if (taskId < 0) {
            throw new IllegalArgumentException("invalid task id");
        }
        try {
            return getDefault().getTaskInfo(taskId);
        } catch (RemoteException e) {
            warningException(e);
            return null;
        }
    }

    public static int getTopRunningTaskId(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "getTopRunningTaskId() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            return getDefault().getTopRunningTaskIdWithPermission(screen);
        } catch (RemoteException e) {
            warningException(e);
            return -1;
        }
    }

    public static TaskInfo getTopRunningTaskInfo(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "getTopRunningTaskInfo() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            return getDefault().getTopRunningTaskInfo(screen);
        } catch (RemoteException e) {
            warningException(e);
            return null;
        }
    }

    public static WindowManager getWindowManager(Context context, DualScreen screen) {
        return null;
    }

    public static boolean isExpandable(int taskId) {
        if (DEBUG) {
            Log.d(TAG, "isExpandable() taskId=" + taskId);
        }
        try {
            return getDefault().isExpandable(taskId);
        } catch (RemoteException e) {
            warningException(e);
            return false;
        }
    }

    public static boolean isInFixedScreenMode(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "isInFixedScreenMode()");
        }
        try {
            return getDefault().isInFixedScreenMode(screen);
        } catch (RemoteException e) {
            warningException(e);
            return false;
        }
    }

    public static Intent makeIntent(Context context, Intent intent, DualScreen targetScreen, int flags) {
        if (DEBUG) {
            Log.d(TAG, "makeIntent() : targetScreen=" + targetScreen + " flags=" + flags + " intent=" + intent + " context=" + context);
        }
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        } else if (intent == null) {
            throw new IllegalArgumentException("intent is null");
        } else if (targetScreen == null) {
            throw new IllegalArgumentException("targetScreen is null");
        } else if (!DualScreenUtils.hasCoupledTaskFlags(flags) || (context instanceof Activity)) {
            if (context instanceof Activity) {
                IBinder token = ((Activity) context).getActivityToken();
                if (token == null) {
                    throw new IllegalArgumentException("invalid activity token");
                }
                try {
                    boolean canBeCoupled = getDefault().canBeCoupled(token);
                    if (!DualScreenUtils.hasCoupledTaskFlags(flags) || canBeCoupled) {
                        if (!(DualScreenUtils.hasCoupledTaskFlags(flags) || context.getDisplayId() == targetScreen.getDisplayId())) {
                            intent.addFlags(268435456);
                        }
                        intent.getLaunchParams().setScreen(targetScreen);
                        intent.getLaunchParams().setFlags(flags);
                        if (DEBUG) {
                            Log.d(TAG, "makeIntent() intent=" + intent);
                        }
                    } else {
                        intent.getLaunchParams().clearFlags(DualScreenUtils.getCoupledTaskFlags());
                    }
                } catch (RemoteException e) {
                    warningException(e);
                }
            } else {
                Log.d(TAG, "makeIntent called from non-Activity context; forcing Intent.FLAG_ACTIVITY_NEW_TASK for: " + intent);
                intent.addFlags(268435456);
                intent.getLaunchParams().setScreen(targetScreen);
                intent.getLaunchParams().setFlags(flags);
                if (DEBUG) {
                    Log.d(TAG, "makeIntent() intent=" + intent);
                }
            }
            return intent;
        } else {
            throw new IllegalArgumentException("context should be Activity to use FLAG_COUPLED_TASK_XXX flags");
        }
    }

    public void moveToScreen(DualScreen toScreen) {
        DualScreen currentScreen = null;
        if (DEBUG) {
            Log.d(TAG, "moveToScreen() : toScreen=" + toScreen);
        }
        if (this.mContext instanceof Activity) {
            if (this.mContext != null) {
                currentScreen = DualScreenUtils.displayIdToScreen(this.mContext.getDisplayId());
            }
            if (currentScreen == toScreen) {
                throw new IllegalArgumentException("target and source screen are same.");
            } else if (currentScreen == null) {
                throw new IllegalArgumentException("fromScreen is null");
            } else if (toScreen == null) {
                throw new IllegalArgumentException("toScreen is null");
            } else {
                TaskInfo tInfo = getTopRunningTaskInfo(currentScreen);
                int currentTaskId = -1;
                if (tInfo != null) {
                    currentTaskId = tInfo.getTaskId();
                }
                if (currentTaskId < 0) {
                    Log.w(TAG, "moveToScreen() : there is no valid task on currentScreen=" + currentScreen + " currentTaskId=" + currentTaskId);
                    return;
                }
                try {
                    if (this.mContext instanceof Activity) {
                        getDefault().moveTaskToScreen(this.mContext.getActivityToken(), toScreen);
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    warningException(e);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("context should be an Activity instance");
    }

    public static void moveTaskToScreen(DualScreen fromScreen, DualScreen toScreen) {
        if (DEBUG) {
            Log.d(TAG, "moveTaskToScreen() : fromScreen=" + fromScreen + ", toScreen=" + toScreen);
        }
        if (fromScreen == toScreen) {
            throw new IllegalArgumentException("target and source screen are same.");
        } else if (fromScreen == null) {
            throw new IllegalArgumentException("fromScreen is null");
        } else if (toScreen == null) {
            throw new IllegalArgumentException("toScreen is null");
        } else {
            TaskInfo tInfo = getTopRunningTaskInfo(fromScreen);
            int targetTaskId = -1;
            if (tInfo != null) {
                targetTaskId = tInfo.getTaskId();
            }
            if (targetTaskId < 0) {
                Log.w(TAG, "moveTaskToScreen() : there is no valid task on fromScreen=" + fromScreen + " targetTaskId=" + targetTaskId);
            } else {
                moveTaskToScreen(targetTaskId, toScreen, 0, null);
            }
        }
    }

    public static void moveTaskToScreen(int taskId, DualScreen toScreen) {
        if (DEBUG) {
            Log.d(TAG, "moveTaskToScreen() : taskId=" + taskId + ", toScreen=" + toScreen);
        }
        if (toScreen == null) {
            throw new IllegalArgumentException("toScreen is null");
        } else if (taskId < 0) {
            Log.w(TAG, "moveTaskToScreen() : there is no valid task taskId=" + taskId);
        } else {
            moveTaskToScreen(taskId, toScreen, 0, null);
        }
    }

    public static void moveTaskToScreen(int taskId, DualScreen toScreen, int flags, Bundle options) {
        if (DEBUG) {
            Log.d(TAG, "moveTaskToFront() : taskId=" + taskId + ", toScreen=" + toScreen + ", flags=" + flags + ", options=" + options);
        }
        if (taskId < 0) {
            throw new IllegalArgumentException("taskId should be positive value.");
        }
        try {
            getDefault().moveTaskToScreenWithPermission(taskId, toScreen, flags, options);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void overrideNextAppTransition(DualScreen screen, int transit) {
        try {
            if (this.mContext instanceof Activity) {
                getDefault().overrideNextAppTransition(this.mContext.getActivityToken(), screen, transit);
            }
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static void parseDualScreenLaunchParams(Context context, Intent intent) {
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null && context != null) {
                DualScreen currentScreen = DualScreenUtils.displayIdToScreen(context.getDisplayId());
                String screen = b.getString(INTENT_EXTRA_SCREEN);
                DualScreen targetScreen = DualScreen.UNKNOWN;
                if (!(screen == null || screen.length() == 0)) {
                    if (screen.equalsIgnoreCase("main")) {
                        targetScreen = DualScreen.MAIN;
                    } else if (screen.equalsIgnoreCase("sub")) {
                        targetScreen = DualScreen.SUB;
                    } else if (screen.equalsIgnoreCase("full")) {
                        targetScreen = DualScreen.FULL;
                    } else if (screen.equalsIgnoreCase("opposite")) {
                        if (currentScreen == DualScreen.MAIN) {
                            targetScreen = DualScreen.SUB;
                        } else if (currentScreen == DualScreen.SUB) {
                            targetScreen = DualScreen.MAIN;
                        }
                    }
                }
                if (targetScreen != DualScreen.UNKNOWN) {
                    intent.getLaunchParams().setScreen(targetScreen);
                }
                if (context instanceof Activity) {
                    if (DEBUG) {
                        Log.d(TAG, "parseDualScreenLaunchParams : targetScreen=" + targetScreen + " called from Activity context");
                    }
                    if (((Activity) context).getActivityToken() != null) {
                        Boolean coupled = Boolean.valueOf(b.getBoolean(INTENT_EXTRA_COUPLED));
                        if (DEBUG) {
                            Log.d(TAG, "parseDualScreenLaunchParams : coupled=" + coupled);
                        }
                        if (coupled != null && coupled.booleanValue()) {
                            intent.getLaunchParams().addFlags(1);
                        } else if (targetScreen != DualScreen.UNKNOWN && currentScreen != targetScreen) {
                            intent.addFlags(268435456);
                        }
                    }
                } else if (targetScreen != DualScreen.UNKNOWN) {
                    if (DEBUG) {
                        Log.d(TAG, "parseDualScreenLaunchParams : targetScreen=" + targetScreen);
                    }
                    Log.d(TAG, "parseDualScreenLaunchParams : targetScreen=" + targetScreen + " called from non-Activity context; forcing " + "Intent.FLAG_ACTIVITY_NEW_TASK for: " + intent);
                    intent.addFlags(268435456);
                }
            }
        }
    }

    public static void parseDualScreenLaunchParams(Context context, Intent[] intents) {
        if (intents != null) {
            for (Intent intent : intents) {
                parseDualScreenLaunchParams(context, intent);
            }
        }
    }

    public void registerExpandableActivity() {
        if (DEBUG) {
            Log.d(TAG, "registerExpandableActivity() : " + this.mContext);
        }
        if (this.mContext instanceof Activity) {
            try {
                if (this.mContext instanceof Activity) {
                    getDefault().registerExpandableActivity(this.mContext.getActivityToken());
                    return;
                }
                return;
            } catch (RemoteException e) {
                warningException(e);
                return;
            }
        }
        throw new IllegalArgumentException("context should be an Activity instance");
    }

    public void requestOppositeDisplayOrientation(int requestedOrientation) {
        if (DEBUG) {
            Log.d(TAG, "requestOppositeDisplayOrientation() : requestedOrientation=" + requestedOrientation);
        }
        if (this.mContext instanceof Activity) {
            try {
                if (this.mContext instanceof Activity) {
                    getDefault().requestOppositeDisplayOrientation(this.mContext.getActivityToken(), requestedOrientation);
                    return;
                }
                return;
            } catch (RemoteException e) {
                warningException(e);
                return;
            }
        }
        throw new IllegalArgumentException("context should be an Activity instance");
    }

    public void requestExpandedDisplayOrientation(int requestedOrientation) {
        if (DEBUG) {
            Log.d(TAG, "requestExpandedDisplayOrientation() : requestedOrientation=" + requestedOrientation);
        }
        if (this.mContext instanceof Activity) {
            try {
                if (this.mContext instanceof Activity) {
                    getDefault().requestExpandedDisplayOrientation(this.mContext.getActivityToken(), requestedOrientation);
                    return;
                }
                return;
            } catch (RemoteException e) {
                warningException(e);
                return;
            }
        }
        throw new IllegalArgumentException("context should be an Activity instance");
    }

    public static void sendExpandRequest(int targetTaskId) {
        if (DEBUG) {
            Log.d(TAG, "sendExpandRequest() targetTaskId=" + targetTaskId);
        }
        try {
            getDefault().sendExpandRequest(targetTaskId);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public static void sendShrinkRequest(int targetTaskId, DualScreen toScreen) {
        if (DEBUG) {
            Log.d(TAG, "sendShrinkRequest() targetTaskId=" + targetTaskId + " toScreen=" + toScreen);
        }
        try {
            getDefault().sendShrinkRequest(targetTaskId, toScreen);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void setExpandable(boolean expandable) {
        if (DEBUG) {
            Log.d(TAG, "setExpandable() expandable=" + expandable);
        }
        try {
            if (this.mContext instanceof Activity) {
                getDefault().setExpandable(this.mContext.getActivityToken(), expandable);
                return;
            }
            Log.e(TAG, "setExpandable() is called from non-Activity context");
            throw new IllegalArgumentException("setExpandable() should be called from Activity context");
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void setFinishWithCoupledTask(boolean finish) {
        if (DEBUG) {
            Log.d(TAG, "setFinishWithCoupledTask()");
        }
        try {
            if (this.mContext instanceof Activity) {
                getDefault().setFinishWithCoupledTask(this.mContext.getActivityToken(), finish);
                return;
            }
            Log.e(TAG, "setFinishWithCoupledTask() is called from non-Activity context");
            throw new IllegalArgumentException("setFinishWithCoupledTask() should be called from Activity context");
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void finishCoupledActivity(int flags) {
        if (DEBUG) {
            Log.d(TAG, "finishCoupledActivity()");
        }
        try {
            if (this.mContext instanceof Activity) {
                getDefault().finishCoupledActivity(this.mContext.getActivityToken(), flags);
                return;
            }
            Log.e(TAG, "finishCoupledActivity() is called from non-Activity context");
            throw new IllegalArgumentException("finishCoupledActivity() should be called from Activity context");
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void setScreenFocusChangeListener(ScreenFocusChangeListener l) {
        getListenerInfo().mScreenFocusChangeListener = l;
        updateListenerInfo();
    }

    public static void swapTopTask() {
        if (DEBUG) {
            Log.d(TAG, "swapTopTask()");
        }
        try {
            getDefault().swapTopTask();
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void switchScreen() {
        if (DEBUG) {
            Log.d(TAG, "switchScreen()");
        }
        DualScreen currentScreen = getScreen();
        if (currentScreen == DualScreen.MAIN) {
            moveToScreen(DualScreen.SUB);
        } else if (currentScreen == DualScreen.SUB) {
            moveToScreen(DualScreen.MAIN);
        }
    }

    public static void unfixTopTask(DualScreen screen) {
        if (DEBUG) {
            Log.d(TAG, "unfixTopTask() : screen=" + screen);
        }
        if (screen == null) {
            throw new IllegalArgumentException("screen is null");
        }
        try {
            getDefault().unfixTopTask(screen);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    public void unregisterExpandableActivity() {
        if (DEBUG) {
            Log.d(TAG, "unregisterExpandableActivity() : " + this.mContext);
        }
        if (this.mContext instanceof Activity) {
            try {
                if (this.mContext instanceof Activity) {
                    getDefault().unregisterExpandableActivity(this.mContext.getActivityToken());
                    return;
                }
                return;
            } catch (RemoteException e) {
                warningException(e);
                return;
            }
        }
        throw new IllegalArgumentException("context should be an Activity instance");
    }

    ListenerInfo getListenerInfo() {
        if (this.mListenerInfo != null) {
            return this.mListenerInfo;
        }
        registerDualScreenCallbacks(this.mDualScreenCallbacks);
        this.mListenerInfo = new ListenerInfo();
        return this.mListenerInfo;
    }

    void updateListenerInfo() {
        boolean hasRegisteredListener = false;
        if (!(this.mListenerInfo == null || this.mListenerInfo.mScreenFocusChangeListener == null)) {
            hasRegisteredListener = true;
        }
        if (!hasRegisteredListener) {
            unregisterDualScreenCallbacks(this.mDualScreenCallbacks);
            this.mListenerInfo = null;
        }
    }

    private static void warningException(Exception e) {
        Log.w(TAG, "warningException() : caller=" + Debug.getCaller() + e.getMessage());
    }

    private void registerDualScreenCallbacks(IDualScreenCallbacks callback) {
        if (DEBUG) {
            Log.d(TAG, "registerDualScreenCallbacks() : callback=" + callback + " (callers=" + Debug.getCallers(3) + ")");
        }
        try {
            getDefault().registerDualScreenCallbacks(callback);
        } catch (RemoteException e) {
            warningException(e);
        }
    }

    private void unregisterDualScreenCallbacks(IDualScreenCallbacks callback) {
        if (DEBUG) {
            Log.d(TAG, "unregisterDualScreenCallbacks() : callback=" + callback + " (callers=" + Debug.getCallers(3) + ")");
        }
        try {
            getDefault().unregisterDualScreenCallbacks(callback);
        } catch (RemoteException e) {
            warningException(e);
        }
    }
}
