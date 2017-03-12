package android.view;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.view.InputEventReceiver.Factory;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.widget.RemoteViews;
import com.samsung.android.cover.CoverState;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.io.PrintWriter;
import java.util.ArrayList;

public interface WindowManagerPolicy {
    public static final String ACTION_HDMI_PLUGGED = "android.intent.action.HDMI_PLUGGED";
    public static final int ACTION_PASS_TO_USER = 1;
    public static final boolean CAMERA_ROTATION = true;
    public static final boolean CUSTOM_STARTING_WINDOW = true;
    public static final String EXTRA_FROM_HOME_KEY = "android.intent.extra.FROM_HOME_KEY";
    public static final String EXTRA_HDMI_PLUGGED_STATE = "state";
    public static final int FINISH_LAYOUT_REDO_ANIM = 8;
    public static final int FINISH_LAYOUT_REDO_CONFIG = 2;
    public static final int FINISH_LAYOUT_REDO_FOCUS = 16;
    public static final int FINISH_LAYOUT_REDO_LAYOUT = 1;
    public static final int FINISH_LAYOUT_REDO_WALLPAPER = 4;
    public static final int FLAG_DISABLE_KEY_REPEAT = 134217728;
    public static final int FLAG_FILTERED = 67108864;
    public static final int FLAG_INJECTED = 16777216;
    public static final int FLAG_INTERACTIVE = 536870912;
    public static final int FLAG_PASS_TO_USER = 1073741824;
    public static final int FLAG_TRUSTED = 33554432;
    public static final int FLAG_VIRTUAL = 2;
    public static final int FLAG_WAKE = 1;
    public static final int OFF_BECAUSE_OF_ADMIN = 1;
    public static final int OFF_BECAUSE_OF_PROX_SENSOR = 4;
    public static final int OFF_BECAUSE_OF_TIMEOUT = 3;
    public static final int OFF_BECAUSE_OF_USER = 2;
    public static final int PRESENCE_EXTERNAL = 2;
    public static final int PRESENCE_INTERNAL = 1;
    public static final boolean SUPPORT_COVER = true;
    public static final boolean SUPPORT_MINIMODE = true;
    public static final boolean SUPPORT_MOBILE_KEYBOARD = true;
    public static final boolean SUPPORT_WEARABLE_HMT = true;
    public static final int TRANSIT_ENTER = 1;
    public static final int TRANSIT_EXIT = 2;
    public static final int TRANSIT_HIDE = 4;
    public static final int TRANSIT_PREVIEW_DONE = 5;
    public static final int TRANSIT_SHOW = 3;
    public static final int TSP_POLICY_LIST = 1;
    public static final int TSP_POLICY_NORMAL = 0;
    public static final int USER_ROTATION_FREE = 0;
    public static final int USER_ROTATION_LOCKED = 1;
    public static final boolean WATCH_POINTER = false;
    public static final int WINDOW_INFO_ARRANGE_MINIMIZED = 2;
    public static final int WINDOW_INFO_ARRANGE_RESTORE = 4;
    public static final int WINDOW_INFO_NOTHING_CHANGED = 0;
    public static final int WINDOW_INFO_SIZE_CHANGED = 1;
    public static final int WINDOW_MODE_FREESTYLE = 33554432;
    public static final int WINDOW_MODE_MASK = -16777216;
    public static final int WINDOW_MODE_NORMAL = 16777216;
    public static final int WINDOW_MODE_OPTION_COMMON_EXTERNAL_DISP = 4096;
    public static final int WINDOW_MODE_OPTION_COMMON_FORCE_TITLE_BAR = 262144;
    public static final int WINDOW_MODE_OPTION_COMMON_HIDDEN = 1048576;
    public static final int WINDOW_MODE_OPTION_COMMON_MASK = 16776960;
    public static final int WINDOW_MODE_OPTION_COMMON_MINIMIZED = 2097152;
    public static final int WINDOW_MODE_OPTION_COMMON_NO_TITLE_BAR = 524288;
    public static final int WINDOW_MODE_OPTION_COMMON_OPTION_ZONE_NEXT = 131072;
    public static final int WINDOW_MODE_OPTION_COMMON_OPTION_ZONE_SAME = 65536;
    public static final int WINDOW_MODE_OPTION_COMMON_PINUP = 8388608;
    public static final int WINDOW_MODE_OPTION_COMMON_PINUP_HIDDEN = 16384;
    public static final int WINDOW_MODE_OPTION_COMMON_PREVIEW_SCALE = 1024;
    public static final int WINDOW_MODE_OPTION_COMMON_RESIZE = 4194304;
    public static final int WINDOW_MODE_OPTION_COMMON_SCALE = 2048;
    public static final int WINDOW_MODE_OPTION_COMMON_TOOLKIT = 8192;
    public static final int WINDOW_MODE_OPTION_COMMON_TOOLKIT_MASK = 8921088;
    public static final int WINDOW_MODE_OPTION_COMMON_UNIQUEOP_MASK = 4980736;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_A = 3;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_B = 12;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_C = 1;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_D = 2;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_E = 4;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_F = 8;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_FULL = 15;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_MASK = 15;
    public static final int WINDOW_MODE_OPTION_SPLIT_ZONE_UNKNOWN = 0;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_DISP_HIDE = 64;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_DISP_MASK = 192;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_DISP_SHOW = 128;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_FULL_MASK = 240;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_ZONE_A = 32;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_ZONE_B = 16;
    public static final int WINDOW_MODE_OPTION_TOOLKIT_ZONE_MASK = 48;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_DONTCARE = 0;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_FREESTYLE = 3;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_MASK = 196608;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_MASK_SHIFT = 16;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_NEXT = 2;
    public static final int WINDOW_MODE_OPTION_ZONE_POLICY_SAME = 1;
    public static final int WINDOW_MODE_SHIFT = 24;
    public static final String WINDOW_STYLE_ACTIVITY_FORCE_TITLE_BAR = "forceTitleBar";
    public static final String WINDOW_STYLE_ACTIVITY_FULLSCREEN_ONLY = "fullscreenOnly";
    public static final String WINDOW_STYLE_ACTIVITY_RESIZE_ONLY = "resizeOnly";
    public static final String WINDOW_STYLE_FIXED_RATIO = "fixedRatio";
    public static final String WINDOW_STYLE_FIXED_SIZE = "fixedSize";
    public static final String WINDOW_STYLE_FREESTYLE_ONLY = "freestyleOnly";
    public static final String WINDOW_STYLE_FULL_SIZE = "fullSize";
    public static final String WINDOW_STYLE_HIDE_APP_FROM_MULTIWINDOWLIST = "hideAppFromMultiWindowList";
    public static final String WINDOW_STYLE_ISOLATED_SPLIT = "isolatedSplit";
    public static final String WINDOW_STYLE_NO_TITLE_BAR = "noTitleBar";

    public interface InputConsumer {
        void dismiss();
    }

    public interface OnKeyguardExitResult {
        void onKeyguardExitResult(boolean z);
    }

    public interface PointerEventListener {
        void onPointerEvent(MotionEvent motionEvent);
    }

    public interface ScreenOnListener {
        void onScreenOn();
    }

    public interface WindowManagerFuncs {
        public static final int CAMERA_LENS_COVERED = 1;
        public static final int CAMERA_LENS_COVER_ABSENT = -1;
        public static final int CAMERA_LENS_UNCOVERED = 0;
        public static final int LID_ABSENT = -1;
        public static final int LID_CLOSED = 0;
        public static final int LID_OPEN = 1;
        public static final int PEN_ABSENT = -1;
        public static final int PEN_ATTACHED = 1;
        public static final int PEN_DETACHED = 0;

        InputConsumer addInputConsumer(Looper looper, Factory factory);

        InputConsumer addInputConsumer(Looper looper, Factory factory, int i);

        boolean canGlobalActionsShow();

        void cancelDragForcelyWhenScreenTurnOff(boolean z);

        int getCameraLensCoverState();

        int getLidState();

        int getPenState();

        Object getWindowManagerLock();

        InputChannel monitorInput(String str);

        void multiwindowSettingChanged(boolean z);

        void notifySystemUiVisibility(int i);

        void reboot(boolean z);

        void rebootSafeMode(boolean z);

        void reevaluateStatusBarVisibility();

        void reevaluateStatusBarVisibility(int i);

        void registerPointerEventListener(PointerEventListener pointerEventListener);

        boolean removeAppBackWindow();

        void shutdown(boolean z);

        void switchKeyboardLayout(int i, int i2);

        void unregisterPointerEventListener(PointerEventListener pointerEventListener);
    }

    public static class WindowModeHelper {
        public static int mode(int params) {
            return -16777216 & params;
        }

        public static int option(int params) {
            return 16777215 & params;
        }

        public static int zone(int params) {
            return params & 15;
        }

        public static int zonePolicy(int params) {
            return (196608 & params) >> 16;
        }

        public static int setWindowMode(int fromWindowMode, int toWindowMode) {
            return (toWindowMode & WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_UNIQUEOP_MASK) | (-4980737 & fromWindowMode);
        }
    }

    public interface WindowState {
        void computeFrameLw(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, Rect rect7, Rect rect8);

        void disableHideSViewCoverOnce(boolean z);

        IApplicationToken getAppToken();

        int getAppTokenTaskId();

        LayoutParams getAttrs();

        int getBaseType();

        Rect getContentFrameLw();

        int getCoverMode();

        Rect getDisplayFrameLw();

        int getDisplayId();

        Rect getFrameLw();

        Rect getGivenContentInsetsLw();

        boolean getGivenInsetsPendingLw();

        Rect getGivenVisibleInsetsLw();

        int getGroupId();

        MultiWindowStyle getMultiWindowStyleLw();

        boolean getNeedsMenuLw(WindowState windowState);

        Rect getOverscanFrameLw();

        String getOwningPackage();

        int getOwningUid();

        int getRequestedHeightLw();

        int getRequestedOrientation();

        int getRequestedWidthLw();

        PointF getScaleFactor();

        RectF getShownFrameLw();

        Rect getStackBounds();

        int getStackId();

        int getSurfaceLayer();

        int getSystemUiVisibility();

        int getTargetAppTokenStackId();

        int getTargetAppTokenTaskId();

        WindowState getTopFullWindowInSameTask();

        IApplicationToken getTopPenWindowControllerInStack();

        Rect getVisibleFrameLw();

        boolean hasAppShownWindows();

        boolean hasChild();

        boolean hasDrawnLw();

        boolean hasSubWindow();

        boolean hideLw(boolean z);

        boolean hideLw(boolean z, boolean z2, boolean z3);

        boolean isAlive();

        boolean isAnimatingLw();

        boolean isArrangedUpperSideInputMethod();

        boolean isAttachedTo(WindowState windowState);

        boolean isAttachedWindowFocused();

        boolean isCustomStartingAnimationLw();

        boolean isDefaultDisplay();

        boolean isDimming();

        boolean isDisplayedLw();

        boolean isDrawFinishedLw();

        boolean isDrawnLw();

        boolean isExiting();

        boolean isFixedBounds();

        boolean isFloating();

        boolean isFocused();

        boolean isGoneForLayoutLw();

        boolean isHomeType();

        boolean isInputMethodTargetLw();

        boolean isOnScreen();

        boolean isTopPenWindowControllerVisibleInStack();

        boolean isTransluent();

        boolean isVisibleLw();

        boolean isVisibleOrBehindKeyguardLw();

        boolean isVoiceInteraction();

        void setWillBeHideBlockMain(boolean z);

        boolean showLw(boolean z);

        boolean showLw(boolean z, boolean z2, boolean z3);

        boolean willBeHideBlockMain();

        boolean willBeHideSViewCoverOnce();
    }

    View addBackWindow(int i);

    View addStartingWindow(IBinder iBinder, String str, int i, CompatibilityInfo compatibilityInfo, CharSequence charSequence, int i2, int i3, int i4, int i5, MultiWindowStyle multiWindowStyle, Bitmap bitmap, int i6, int i7, int i8);

    void adjustConfigurationLw(Configuration configuration, int i, int i2);

    int adjustSystemUiVisibilityLw(int i);

    int adjustSystemUiVisibilityLw(int i, int i2);

    void adjustWindowParamsLw(LayoutParams layoutParams);

    void adjustWindowParamsLw(WindowState windowState, LayoutParams layoutParams, int i, int i2);

    void adjustWindowParamsLw(WindowState windowState, LayoutParams layoutParams, int i, int i2, int i3);

    boolean allowAppAnimationsLw();

    void applyPostLayoutPolicyLw(WindowState windowState, LayoutParams layoutParams, WindowState windowState2);

    void beginLayoutLw(boolean z, int i, int i2, int i3);

    void beginLayoutLw(boolean z, int i, int i2, int i3, int i4, boolean z2);

    void beginPostLayoutPolicyLw(int i, int i2);

    void beginPostLayoutPolicyLw(int i, int i2, int i3, boolean z);

    boolean canBeForceHidden(WindowState windowState, LayoutParams layoutParams);

    boolean canBeForceHiddenByNightClock(WindowState windowState, LayoutParams layoutParams);

    boolean canBeForceHiddenBySViewCover(WindowState windowState, LayoutParams layoutParams);

    boolean canBeForceHiddenByVR(WindowState windowState, LayoutParams layoutParams);

    boolean canMagnifyWindow(int i);

    int checkAddPermission(LayoutParams layoutParams, int[] iArr);

    boolean checkShowToOwnerOnly(LayoutParams layoutParams);

    boolean closeMultiWindowTrayBar(boolean z);

    Animation createForceHideEnterAnimation(boolean z, boolean z2);

    Animation createForceHideWallpaperExitAnimation(boolean z);

    void dismissKeyguardLw();

    KeyEvent dispatchUnhandledKey(WindowState windowState, KeyEvent keyEvent, int i);

    void dump(String str, PrintWriter printWriter, String[] strArr);

    void enableKeyguard(boolean z);

    void enableScreenAfterBoot();

    void exitKeyguardSecurely(OnKeyguardExitResult onKeyguardExitResult);

    void finishLayoutLw();

    int finishPostLayoutPolicyLw();

    int finishPostLayoutPolicyLw(int i, boolean z);

    void finishedGoingToSleep(int i);

    void finishedGoingToSleep(int i, int i2);

    void finishedWakingUp();

    int focusChangedLw(WindowState windowState, WindowState windowState2);

    int focusChangedLw(WindowState windowState, WindowState windowState2, int i);

    void forceHideCenterBar(boolean z);

    ArrayList<IApplicationToken> getAppsShowWhenLockedLw();

    Rect getCocktailBarFrame(WindowState windowState, boolean z);

    int getConfigDisplayHeight(int i, int i2, int i3);

    int getConfigDisplayWidth(int i, int i2, int i3);

    void getContentRectLw(Rect rect);

    boolean getCoverStateSwitch();

    int getFloatingStatusBarHeight(WindowState windowState);

    Rect getFloatingWindowPadding(WindowState windowState);

    int getGlobalSystemUiVisibility();

    int getInputMethodWindowVisibleHeightLw();

    void getInsetHintLw(LayoutParams layoutParams, int i, Rect rect, Rect rect2, Rect rect3);

    int getMaxWallpaperLayer();

    int getMinimizeSize();

    int getNonDecorAlphaScreenDisplayHeight(int i, int i2);

    int getNonDecorAlphaScreenDisplayWidth(int i, int i2);

    int getNonDecorDisplayHeight(int i, int i2, int i3);

    int getNonDecorDisplayWidth(int i, int i2, int i3);

    int getSViewCoverHeight(DisplayInfo displayInfo);

    int getSViewCoverWidth(DisplayInfo displayInfo);

    int getScaleWindowResizableSize();

    int getSystemDecorLayerLw();

    int getUserRotationMode();

    WindowState getWinShowWhenLockedLw();

    WindowState getWinShowWhenLockedLw(int i);

    boolean hasNavigationBar();

    void hideBootMessages();

    boolean inKeyguardRestrictedKeyInputMode();

    void init(Context context, IWindowManager iWindowManager, WindowManagerFuncs windowManagerFuncs);

    long interceptKeyBeforeDispatching(WindowState windowState, KeyEvent keyEvent, int i);

    int interceptKeyBeforeQueueing(KeyEvent keyEvent, int i);

    void interceptKeyBeforeQuickAccess(int i, float f, float f2);

    int interceptMotionBeforeQueueingNonInteractive(long j, int i);

    boolean isAllScreenOnFully();

    boolean isAwake();

    boolean isCarModeBarVisible();

    boolean isCocktailRotationAnimationNeeded();

    boolean isDefaultKeyguardRotationAnimationAlwaysUsed();

    boolean isDefaultOrientationForced();

    boolean isForceHideByNightClock();

    boolean isForceHideBySViewCover();

    boolean isForceHideCascade();

    boolean isForceHiding(LayoutParams layoutParams);

    boolean isKeyguardDrawnLw();

    boolean isKeyguardHostWindow(LayoutParams layoutParams);

    boolean isKeyguardLocked();

    boolean isKeyguardSecure();

    boolean isKeyguardShowingAndNotOccluded();

    boolean isKeyguardShowingAndOccluded();

    boolean isKeyguardShowingOrOccluded();

    boolean isLockTaskModeEnabled();

    boolean isMetaKeyEventRequested(ComponentName componentName);

    boolean isNavigationBarVisible();

    boolean isNeedLayoutCoverApplication(WindowState windowState);

    boolean isScreenOn();

    boolean isScreenOn(int i);

    boolean isStatusBarKeyguard();

    boolean isStatusBarSViewCover();

    boolean isStatusBarVisible();

    boolean isSystemKeyEventRequested(int i, ComponentName componentName);

    boolean isTopLevelWindow(int i);

    boolean isWakeupPreventionPackage(String str);

    void keepScreenOnStartedLw();

    void keepScreenOnStoppedLw();

    void layoutWindowLw(WindowState windowState, WindowState windowState2);

    void lockNow(Bundle bundle);

    boolean needForceHide(WindowState windowState, LayoutParams layoutParams, MultiWindowStyle multiWindowStyle, WindowState windowState2, LayoutParams layoutParams2, MultiWindowStyle multiWindowStyle2);

    boolean needHideTrayBar();

    void notifyActivityDrawnForKeyguardLw();

    void notifyCameraLensCoverSwitchChanged(long j, boolean z);

    void notifyCoverSwitchStateChanged(long j, boolean z);

    void notifyDisplayAdded(int i);

    void notifyFoldingSwitchStateChanged(long j, int i);

    void notifyLidSwitchChanged(long j, boolean z);

    void notifyPenSwitchChanged(long j, boolean z);

    void notifyWrapAroundModeChanged(int i);

    void onFixedScreenModeChanged(int i);

    void onLockTaskModeChanged(int i);

    void onMultipleScreenStateChanged(int i, int i2);

    boolean performHapticFeedbackLw(WindowState windowState, int i, boolean z);

    int prepareAddWindowLw(WindowState windowState, LayoutParams layoutParams);

    void removeAdaptiveEvent(String str);

    void removeBackWindow(View view);

    void removeBackWindowForAppLaunching();

    void removeStartingWindow(IBinder iBinder, View view);

    void removeWindowLw(WindowState windowState);

    void requestDefaultKeyguardRotationAnimation(boolean z);

    void requestMetaKeyEvent(ComponentName componentName, boolean z);

    boolean requestSystemKeyEvent(int i, ComponentName componentName, boolean z);

    void requestTransientBars();

    int rotationForOrientationLw(int i, int i2, int i3);

    boolean rotationHasCompatibleMetricsLw(int i, int i2, int i3);

    void screenTurnedOff();

    void screenTurnedOff(int i);

    void screenTurnedOn();

    void screenTurningOn(ScreenOnListener screenOnListener);

    void screenTurningOn(ScreenOnListener screenOnListener, int i);

    int selectAnimationLw(WindowState windowState, int i);

    void selectRotationAnimationLw(int[] iArr);

    void setAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2);

    void setBendedPendingIntent(PendingIntent pendingIntent, Intent intent);

    void setBendedPendingIntentInSecure(PendingIntent pendingIntent, Intent intent);

    boolean setCoverSwitchStateLocked(CoverState coverState);

    void setCurrentOrientationLw(int i);

    void setCurrentUserLw(int i);

    void setDisplayOverscan(Display display, int i, int i2, int i3, int i4);

    void setInitialDisplaySize(Display display, int i, int i2, int i3);

    void setLastInputMethodWindowLw(WindowState windowState, WindowState windowState2);

    void setMultiWindowTrayOpenState(boolean z);

    void setRotationLw(int i);

    void setSafeMode(boolean z);

    void setTouchExplorationEnabled(boolean z);

    void setUserRotationMode(int i, int i2);

    void showBootMessage(CharSequence charSequence, boolean z);

    void showGlobalActions();

    void showRecentApps();

    void showStatusBarByNotification();

    void startKeyguardExitAnimation(long j, long j2);

    void startedGoingToSleep(int i);

    void startedWakingUp();

    void startedWakingUp(int i);

    int subWindowTypeToLayerLw(int i);

    void systemBooted();

    void systemReady();

    void updateAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2);

    boolean updateCocktailBarVisibility(boolean z);

    void updateCursorWindowInputRect(Region region);

    void updateTopActivity(ComponentName componentName);

    void updateTspInputMethodPolicy(WindowState windowState);

    void updateTspViewPolicy(int i);

    void userActivity();

    boolean validateRotationAnimationLw(int i, int i2, boolean z);

    int windowTypeToLayerLw(int i);
}
