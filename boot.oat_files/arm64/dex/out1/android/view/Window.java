package android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemProperties;
import android.sec.enterprise.auditlog.AuditEvents;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.SurfaceHolder.Callback2;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowStyle;

public abstract class Window {
    @Deprecated
    protected static final int DEFAULT_FEATURES = 65;
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
    public static final int FEATURE_ACTIVITY_TRANSITIONS = 13;
    public static final int FEATURE_CONTENT_TRANSITIONS = 12;
    public static final int FEATURE_CONTEXT_MENU = 6;
    public static final int FEATURE_CUSTOM_TITLE = 7;
    public static final int FEATURE_INDETERMINATE_PROGRESS = 5;
    public static final int FEATURE_LEFT_ICON = 3;
    public static final int FEATURE_MAX = 13;
    public static final int FEATURE_NO_TITLE = 1;
    public static final int FEATURE_OPTIONS_PANEL = 0;
    public static final int FEATURE_PROGRESS = 2;
    public static final int FEATURE_RIGHT_ICON = 4;
    public static final int FEATURE_SWIPE_TO_DISMISS = 11;
    public static final int ID_ANDROID_CONTENT = 16908290;
    public static final String NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME = "android:navigation:background";
    public static final int PROGRESS_END = 10000;
    public static final int PROGRESS_INDETERMINATE_OFF = -4;
    public static final int PROGRESS_INDETERMINATE_ON = -3;
    public static final int PROGRESS_SECONDARY_END = 30000;
    public static final int PROGRESS_SECONDARY_START = 20000;
    public static final int PROGRESS_START = 0;
    public static final int PROGRESS_VISIBILITY_OFF = -2;
    public static final int PROGRESS_VISIBILITY_ON = -1;
    private static final String PROPERTY_HARDWARE_UI = "persist.sys.ui.hw";
    private static final String PROPERTY_HARDWARE_UI_DBG = "debug.hwui.enable";
    public static final String STATUS_BAR_BACKGROUND_TRANSITION_NAME = "android:status:background";
    private Window mActiveChild;
    private String mAppName;
    private IBinder mAppToken;
    private Callback mCallback;
    private boolean mCloseOnTouchOutside = false;
    private Window mContainer;
    private final Context mContext;
    private int mDefaultWindowFormat = -1;
    private boolean mDestroyed;
    private int mFeatures;
    private int mForcedWindowFlags = 0;
    private boolean mHardwareAccelerated;
    private boolean mHasChildren = false;
    private boolean mHasSoftInputMode = false;
    private boolean mHaveDimAmount = false;
    private boolean mHaveWindowFormat = false;
    private boolean mIsActive = false;
    private int mLocalFeatures;
    private boolean mNoWindowDecor = false;
    private OnWindowDismissedCallback mOnWindowDismissedCallback;
    private boolean mSetCloseOnTouchOutside = false;
    private final LayoutParams mWindowAttributes = new LayoutParams();
    private WindowManager mWindowManager;
    private TypedArray mWindowStyle;

    public interface Callback {
        boolean dispatchGenericMotionEvent(MotionEvent motionEvent);

        boolean dispatchKeyEvent(KeyEvent keyEvent);

        boolean dispatchKeyShortcutEvent(KeyEvent keyEvent);

        boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        boolean dispatchTouchEvent(MotionEvent motionEvent);

        boolean dispatchTrackballEvent(MotionEvent motionEvent);

        void onActionModeFinished(ActionMode actionMode);

        void onActionModeStarted(ActionMode actionMode);

        void onAttachedToWindow();

        void onContentChanged();

        boolean onCreatePanelMenu(int i, Menu menu);

        View onCreatePanelView(int i);

        void onDetachedFromWindow();

        boolean onMenuItemSelected(int i, MenuItem menuItem);

        boolean onMenuOpened(int i, Menu menu);

        void onPanelClosed(int i, Menu menu);

        boolean onPreparePanel(int i, View view, Menu menu);

        boolean onSearchRequested();

        boolean onSearchRequested(SearchEvent searchEvent);

        void onWindowAttributesChanged(LayoutParams layoutParams);

        void onWindowFocusChanged(boolean z);

        ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback);

        ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback, int i);
    }

    public interface OnWindowDismissedCallback {
        void onWindowDismissed();
    }

    public abstract void addContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract void alwaysReadCloseOnTouchAttr();

    public abstract void closeAllPanels();

    public abstract void closePanel(int i);

    public abstract View getCurrentFocus();

    public abstract View getDecorView();

    public abstract LayoutInflater getLayoutInflater();

    public abstract int getNavigationBarColor();

    public abstract int getStatusBarColor();

    public abstract int getVolumeControlStream();

    public abstract void invalidatePanelMenu(int i);

    public abstract boolean isFloating();

    public abstract boolean isShortcutKey(int i, KeyEvent keyEvent);

    protected abstract void onActive();

    public abstract void onConfigurationChanged(Configuration configuration);

    public abstract void openPanel(int i, KeyEvent keyEvent);

    public abstract View peekDecorView();

    public abstract boolean performContextMenuIdentifierAction(int i, int i2);

    public abstract boolean performPanelIdentifierAction(int i, int i2, int i3);

    public abstract boolean performPanelShortcut(int i, int i2, KeyEvent keyEvent, int i3);

    public abstract void restoreHierarchyState(Bundle bundle);

    public abstract Bundle saveHierarchyState();

    public abstract void setBackgroundDrawable(Drawable drawable);

    public abstract void setChildDrawable(int i, Drawable drawable);

    public abstract void setChildInt(int i, int i2);

    public abstract void setContentView(int i);

    public abstract void setContentView(View view);

    public abstract void setContentView(View view, ViewGroup.LayoutParams layoutParams);

    public abstract void setFeatureDrawable(int i, Drawable drawable);

    public abstract void setFeatureDrawableAlpha(int i, int i2);

    public abstract void setFeatureDrawableResource(int i, int i2);

    public abstract void setFeatureDrawableUri(int i, Uri uri);

    public abstract void setFeatureInt(int i, int i2);

    public abstract void setNavigationBarColor(int i);

    public abstract void setNavigationGuardVisibility(int i);

    public abstract void setStatusBarColor(int i);

    public abstract void setTitle(CharSequence charSequence);

    @Deprecated
    public abstract void setTitleColor(int i);

    public abstract void setVolumeControlStream(int i);

    public abstract boolean superDispatchGenericMotionEvent(MotionEvent motionEvent);

    public abstract boolean superDispatchKeyEvent(KeyEvent keyEvent);

    public abstract boolean superDispatchKeyShortcutEvent(KeyEvent keyEvent);

    public abstract boolean superDispatchTouchEvent(MotionEvent motionEvent);

    public abstract boolean superDispatchTrackballEvent(MotionEvent motionEvent);

    public abstract void takeInputQueue(android.view.InputQueue.Callback callback);

    public abstract void takeKeyEvents(boolean z);

    public abstract void takeSurface(Callback2 callback2);

    public abstract void togglePanel(int i, KeyEvent keyEvent);

    public Window(Context context) {
        this.mContext = context;
        int defaultFeatures = getDefaultFeatures(context);
        this.mLocalFeatures = defaultFeatures;
        this.mFeatures = defaultFeatures;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final TypedArray getWindowStyle() {
        TypedArray typedArray;
        synchronized (this) {
            if (this.mWindowStyle == null) {
                this.mWindowStyle = this.mContext.obtainStyledAttributes(R.styleable.Window);
            }
            typedArray = this.mWindowStyle;
        }
        return typedArray;
    }

    public void setContainer(Window container) {
        this.mContainer = container;
        if (container != null) {
            this.mFeatures |= 2;
            this.mLocalFeatures |= 2;
            container.mHasChildren = true;
        }
    }

    public final Window getContainer() {
        return this.mContainer;
    }

    public final boolean hasChildren() {
        return this.mHasChildren;
    }

    public final void destroy() {
        this.mDestroyed = true;
    }

    public final boolean isDestroyed() {
        return this.mDestroyed;
    }

    public void setWindowManager(WindowManager wm, IBinder appToken, String appName) {
        setWindowManager(wm, appToken, appName, false);
    }

    public void setWindowManager(WindowManager wm, IBinder appToken, String appName, boolean hardwareAccelerated) {
        boolean z = false;
        this.mAppToken = appToken;
        this.mAppName = appName;
        if ((hardwareAccelerated && SystemProperties.getBoolean(PROPERTY_HARDWARE_UI_DBG, true)) || SystemProperties.getBoolean(PROPERTY_HARDWARE_UI, false)) {
            z = true;
        }
        this.mHardwareAccelerated = z;
        if (wm == null) {
            wm = (WindowManager) this.mContext.getSystemService("window");
        }
        this.mWindowManager = ((WindowManagerImpl) wm).createLocalWindowManager(this);
    }

    void adjustLayoutParamsForSubWindow(LayoutParams wp) {
        CharSequence curTitle = wp.getTitle();
        String title;
        if (wp.type >= 1000 && wp.type <= LayoutParams.LAST_SUB_WINDOW) {
            if (wp.token == null) {
                View decor = peekDecorView();
                if (decor != null) {
                    wp.token = decor.getWindowToken();
                }
            }
            if (curTitle == null || curTitle.length() == 0) {
                if (wp.type == 1001) {
                    title = AuditEvents.MEDIA;
                } else if (wp.type == 1004) {
                    title = "MediaOvr";
                } else if (wp.type == 1000) {
                    title = "Panel";
                } else if (wp.type == 1002) {
                    title = "SubPanel";
                } else if (wp.type == 1005) {
                    title = "AboveSubPanel";
                } else if (wp.type == 1003) {
                    title = "AtchDlg";
                } else {
                    title = Integer.toString(wp.type);
                }
                if (this.mAppName != null) {
                    title = title + ":" + this.mAppName;
                }
                wp.setTitle(title);
            }
        } else if (wp.type < 2000 || wp.type > LayoutParams.LAST_SYSTEM_WINDOW) {
            if (wp.token == null) {
                wp.token = this.mContainer == null ? this.mAppToken : this.mContainer.mAppToken;
            }
            if ((curTitle == null || curTitle.length() == 0) && this.mAppName != null) {
                wp.setTitle(this.mAppName);
            }
        } else if (curTitle == null || curTitle.length() == 0) {
            title = "Sys" + Integer.toString(wp.type);
            if (this.mAppName != null) {
                title = title + ":" + this.mAppName;
            }
            wp.setTitle(title);
        }
        if (wp.packageName == null) {
            wp.packageName = this.mContext.getPackageName();
        }
        if (this.mHardwareAccelerated) {
            wp.flags |= 16777216;
        }
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public final Callback getCallback() {
        return this.mCallback;
    }

    public final void setOnWindowDismissedCallback(OnWindowDismissedCallback dcb) {
        this.mOnWindowDismissedCallback = dcb;
    }

    public final void dispatchOnWindowDismissed() {
        if (this.mOnWindowDismissedCallback != null) {
            this.mOnWindowDismissedCallback.onWindowDismissed();
        }
    }

    public void setLayout(int width, int height) {
        LayoutParams attrs = getAttributes();
        attrs.width = width;
        attrs.height = height;
        dispatchWindowAttributesChanged(attrs);
    }

    public void setGravity(int gravity) {
        LayoutParams attrs = getAttributes();
        attrs.gravity = gravity;
        dispatchWindowAttributesChanged(attrs);
    }

    public void setType(int type) {
        LayoutParams attrs = getAttributes();
        attrs.type = type;
        dispatchWindowAttributesChanged(attrs);
    }

    public void setFormat(int format) {
        LayoutParams attrs = getAttributes();
        if (format != 0) {
            attrs.format = format;
            this.mHaveWindowFormat = true;
        } else {
            attrs.format = this.mDefaultWindowFormat;
            this.mHaveWindowFormat = false;
        }
        dispatchWindowAttributesChanged(attrs);
    }

    public void setWindowAnimations(int resId) {
        LayoutParams attrs = getAttributes();
        attrs.windowAnimations = resId;
        dispatchWindowAttributesChanged(attrs);
    }

    public void setSoftInputMode(int mode) {
        LayoutParams attrs = getAttributes();
        if (mode != 0) {
            attrs.softInputMode = mode;
            this.mHasSoftInputMode = true;
        } else {
            this.mHasSoftInputMode = false;
        }
        dispatchWindowAttributesChanged(attrs);
    }

    public void addFlags(int flags) {
        setFlags(flags, flags);
    }

    public void addPrivateFlags(int flags) {
        setPrivateFlags(flags, flags);
    }

    public void clearFlags(int flags) {
        setFlags(0, flags);
    }

    public void setFlags(int flags, int mask) {
        LayoutParams attrs = getAttributes();
        attrs.flags = (attrs.flags & (mask ^ -1)) | (flags & mask);
        this.mForcedWindowFlags |= mask;
        dispatchWindowAttributesChanged(attrs);
    }

    private void setPrivateFlags(int flags, int mask) {
        LayoutParams attrs = getAttributes();
        attrs.privateFlags = (attrs.privateFlags & (mask ^ -1)) | (flags & mask);
        dispatchWindowAttributesChanged(attrs);
    }

    protected void setNeedsMenuKey(int value) {
        LayoutParams attrs = getAttributes();
        attrs.needsMenuKey = value;
        dispatchWindowAttributesChanged(attrs);
    }

    protected void dispatchWindowAttributesChanged(LayoutParams attrs) {
        if (this.mCallback != null) {
            this.mCallback.onWindowAttributesChanged(attrs);
        }
    }

    public void setDimAmount(float amount) {
        LayoutParams attrs = getAttributes();
        attrs.dimAmount = amount;
        this.mHaveDimAmount = true;
        dispatchWindowAttributesChanged(attrs);
    }

    public void setAttributes(LayoutParams a) {
        this.mWindowAttributes.copyFrom(a);
        dispatchWindowAttributesChanged(this.mWindowAttributes);
    }

    public final LayoutParams getAttributes() {
        return this.mWindowAttributes;
    }

    protected final int getForcedWindowFlags() {
        return this.mForcedWindowFlags;
    }

    protected final boolean hasSoftInputMode() {
        return this.mHasSoftInputMode;
    }

    public void setCloseOnTouchOutside(boolean close) {
        this.mCloseOnTouchOutside = close;
        this.mSetCloseOnTouchOutside = true;
    }

    public void setCloseOnTouchOutsideIfNotSet(boolean close) {
        if (!this.mSetCloseOnTouchOutside) {
            this.mCloseOnTouchOutside = close;
            this.mSetCloseOnTouchOutside = true;
        }
    }

    public void setDisableWallpaperTouchEvents(boolean disable) {
        setPrivateFlags(disable ? 2048 : 0, 2048);
    }

    public boolean shouldCloseOnTouch(Context context, MotionEvent event) {
        if (this.mCloseOnTouchOutside && event.getAction() == 0 && isOutOfBounds(context, event) && peekDecorView() != null) {
            return true;
        }
        return false;
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        View decorView = getDecorView();
        return x < (-slop) || y < (-slop) || x > decorView.getWidth() + slop || y > decorView.getHeight() + slop;
    }

    public boolean requestFeature(int featureId) {
        int i;
        int flag = 1 << featureId;
        this.mFeatures |= flag;
        int i2 = this.mLocalFeatures;
        if (this.mContainer != null) {
            i = (this.mContainer.mFeatures ^ -1) & flag;
        } else {
            i = flag;
        }
        this.mLocalFeatures = i | i2;
        return (this.mFeatures & flag) != 0;
    }

    protected void removeFeature(int featureId) {
        int flag = 1 << featureId;
        this.mFeatures &= flag ^ -1;
        int i = this.mLocalFeatures;
        if (this.mContainer != null) {
            flag &= this.mContainer.mFeatures ^ -1;
        }
        this.mLocalFeatures = i & (flag ^ -1);
    }

    public final void makeActive() {
        if (this.mContainer != null) {
            if (this.mContainer.mActiveChild != null) {
                this.mContainer.mActiveChild.mIsActive = false;
            }
            this.mContainer.mActiveChild = this;
        }
        this.mIsActive = true;
        onActive();
    }

    public final boolean isActive() {
        return this.mIsActive;
    }

    public View findViewById(int id) {
        return getDecorView().findViewById(id);
    }

    public void setElevation(float elevation) {
    }

    public void setClipToOutline(boolean clipToOutline) {
    }

    public void setBackgroundDrawableResource(int resId) {
        setBackgroundDrawable(this.mContext.getDrawable(resId));
    }

    protected final int getFeatures() {
        return this.mFeatures;
    }

    public static int getDefaultFeatures(Context context) {
        int features = 0;
        Resources res = context.getResources();
        if (res.getBoolean(R.bool.config_defaultWindowFeatureOptionsPanel)) {
            features = 0 | 1;
        }
        if (res.getBoolean(R.bool.config_defaultWindowFeatureContextMenu)) {
            return features | 64;
        }
        return features;
    }

    public boolean hasFeature(int feature) {
        return (getFeatures() & (1 << feature)) != 0;
    }

    protected final int getLocalFeatures() {
        return this.mLocalFeatures;
    }

    protected void setDefaultWindowFormat(int format) {
        this.mDefaultWindowFormat = format;
        if (!this.mHaveWindowFormat) {
            LayoutParams attrs = getAttributes();
            attrs.format = format;
            dispatchWindowAttributesChanged(attrs);
        }
    }

    protected boolean haveDimAmount() {
        return this.mHaveDimAmount;
    }

    public void setMediaController(MediaController controller) {
    }

    public MediaController getMediaController() {
        return null;
    }

    public boolean isNoWindowDecor() {
        return this.mNoWindowDecor;
    }

    public void setNoWindowDecor(boolean noWindowDecor) {
        this.mNoWindowDecor = noWindowDecor;
    }

    public void onMultiWindowStyleChanged(MultiWindowStyle style, int notifyReason) {
    }

    public void onMultiWindowConfigurationChanged(int configDiff) {
    }

    public void onMultiWindowFocusChanged(int notifyReason, boolean focus, boolean keepInputMethod) {
    }

    public boolean hasStackFocus() {
        return false;
    }

    public void exitByCloseBtn() {
    }

    public void arrangeScaleStackBound() {
    }

    public void handlePause() {
    }

    public Object getMultiPhoneWindowEvent() {
        return null;
    }

    public boolean isTouchBlocked() {
        return false;
    }

    public void onVisibilityChanged(boolean show) {
    }

    public View getContentRootContainer() {
        return null;
    }

    public void setAppRequestOrientation(int requestedOrientation) {
    }

    public void setSubWindow(Window subWindow) {
    }

    public void changeStackFocus(boolean focus) {
    }

    public void setTransientCocktailBar(boolean disable) {
    }

    public void setUiOptions(int uiOptions) {
    }

    public void setUiOptions(int uiOptions, int mask) {
    }

    public void setIcon(int resId) {
    }

    public void setDefaultIcon(int resId) {
    }

    public void setLogo(int resId) {
    }

    public void setDefaultLogo(int resId) {
    }

    public void setLocalFocus(boolean hasFocus, boolean inTouchMode) {
    }

    public void injectInputEvent(InputEvent event) {
    }

    public TransitionManager getTransitionManager() {
        return null;
    }

    public void setTransitionManager(TransitionManager tm) {
        throw new UnsupportedOperationException();
    }

    public Scene getContentScene() {
        return null;
    }

    public void setEnterTransition(Transition transition) {
    }

    public void setReturnTransition(Transition transition) {
    }

    public void setExitTransition(Transition transition) {
    }

    public void setReenterTransition(Transition transition) {
    }

    public Transition getEnterTransition() {
        return null;
    }

    public Transition getReturnTransition() {
        return null;
    }

    public Transition getExitTransition() {
        return null;
    }

    public Transition getReenterTransition() {
        return null;
    }

    public void setSharedElementEnterTransition(Transition transition) {
    }

    public void setSharedElementReturnTransition(Transition transition) {
    }

    public Transition getSharedElementEnterTransition() {
        return null;
    }

    public Transition getSharedElementReturnTransition() {
        return null;
    }

    public void setSharedElementExitTransition(Transition transition) {
    }

    public void setSharedElementReenterTransition(Transition transition) {
    }

    public Transition getSharedElementExitTransition() {
        return null;
    }

    public Transition getSharedElementReenterTransition() {
        return null;
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
    }

    public boolean getAllowEnterTransitionOverlap() {
        return true;
    }

    public void setAllowReturnTransitionOverlap(boolean allow) {
    }

    public boolean getAllowReturnTransitionOverlap() {
        return true;
    }

    public long getTransitionBackgroundFadeDuration() {
        return 0;
    }

    public void setTransitionBackgroundFadeDuration(long fadeDurationMillis) {
    }

    public boolean getSharedElementsUseOverlay() {
        return true;
    }

    public void setSharedElementsUseOverlay(boolean sharedElementsUseOverlay) {
    }
}
