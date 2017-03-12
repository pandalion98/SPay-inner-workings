package com.android.internal.policy;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.KeyguardManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionLegacyHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings$System;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.IRotationWatcher.Stub;
import android.view.IWindowManager;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.InputQueue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.ims.ImsConferenceState;
import com.android.internal.R;
import com.android.internal.policy.samsung.ZoomKeyController;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.StandaloneActionMode;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.IconMenuPresenter;
import com.android.internal.view.menu.ListMenuPresenter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuDialogHelper;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuView;
import com.android.internal.widget.ActionBarContextView;
import com.android.internal.widget.BackgroundFallback;
import com.android.internal.widget.DecorContentParent;
import com.android.internal.widget.FloatingToolbar;
import com.android.internal.widget.SwipeDismissLayout;
import com.android.internal.widget.SwipeDismissLayout.OnDismissedListener;
import com.android.internal.widget.SwipeDismissLayout.OnSwipeProgressChangedListener;
import com.sec.enterprise.knoxcustom.KnoxCustomManager;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneWindow extends Window implements Callback {
    private static final String ACTION_BAR_TAG = "android:ActionBar";
    private static final int CUSTOM_TITLE_COMPATIBLE_FEATURES = 13505;
    private static final int DEFAULT_BACKGROUND_FADE_DURATION_MS = 300;
    private static final String EXTRA_LAUNCHER_ACTION = "sec.android.intent.extra.LAUNCHER_ACTION";
    static final int FLAG_RESOURCE_SET_ICON = 1;
    static final int FLAG_RESOURCE_SET_ICON_FALLBACK = 4;
    static final int FLAG_RESOURCE_SET_LOGO = 2;
    private static final String FOCUSED_ID_TAG = "android:focusedViewId";
    private static final String LAUNCHER_ACTION_ALL_APPS = "com.android.launcher2.ALL_APPS";
    private static final String MENU_LONG_POLICY_LAUNCH_SFINDER = "SFINDER";
    private static final String PANELS_TAG = "android:Panels";
    private static final boolean SWEEP_OPEN_MENU = false;
    private static final String TAG = "PhoneWindow";
    private static final Transition USE_DEFAULT_TRANSITION = new TransitionSet();
    private static final String VIEWS_TAG = "android:views";
    private static boolean mSFinderEnabled = false;
    private static boolean mSFinderFeatureCached = false;
    private static boolean penState = false;
    static final RotationWatcher sRotationWatcher = new RotationWatcher();
    private Drawable badgeIcon = null;
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    private Boolean mAllowEnterTransitionOverlap;
    private Boolean mAllowReturnTransitionOverlap;
    private boolean mAlwaysReadCloseOnTouchAttr = false;
    private AudioManager mAudioManager;
    private Drawable mBackgroundDrawable;
    private long mBackgroundFadeDurationMillis = -1;
    private int mBackgroundFallbackResource = 0;
    private int mBackgroundResource = 0;
    private ProgressBar mCircularProgressBar;
    private boolean mClipToOutline;
    private boolean mClosingActionMenu;
    private ViewGroup mContentParent;
    private ViewGroup mContentRoot;
    private Scene mContentScene;
    private ContextMenuBuilder mContextMenu;
    final DialogMenuCallback mContextMenuCallback = new DialogMenuCallback(6);
    private MenuDialogHelper mContextMenuHelper;
    private DecorView mDecor;
    private DecorContentParent mDecorContentParent;
    private DrawableFeatureState[] mDrawables;
    private float mElevation;
    private Transition mEnterTransition = null;
    private Transition mExitTransition = null;
    TypedValue mFixedHeightMajor;
    TypedValue mFixedHeightMinor;
    TypedValue mFixedWidthMajor;
    TypedValue mFixedWidthMinor;
    private boolean mForcedNavigationBarColor = false;
    private boolean mForcedStatusBarColor = false;
    private int mFrameResource = 0;
    private ProgressBar mHorizontalProgressBar;
    int mIconRes;
    private int mInvalidatePanelMenuFeatures;
    private boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
        public void run() {
            for (int i = 0; i <= 13; i++) {
                if ((PhoneWindow.this.mInvalidatePanelMenuFeatures & (1 << i)) != 0) {
                    PhoneWindow.this.doInvalidatePanelMenu(i);
                }
            }
            PhoneWindow.this.mInvalidatePanelMenuPosted = false;
            PhoneWindow.this.mInvalidatePanelMenuFeatures = 0;
        }
    };
    private boolean mIsFloating;
    private boolean mIsStartingWindow;
    private KeyguardManager mKeyguardManager;
    private int mLastBackgroundResource = 0;
    private LayoutInflater mLayoutInflater;
    private ImageView mLeftIconView;
    int mLogoRes;
    private MediaController mMediaController;
    final TypedValue mMinWidthMajor = new TypedValue();
    final TypedValue mMinWidthMinor = new TypedValue();
    private int mNavigationBarColor = 0;
    TypedValue mOutsetBottom;
    private Rect mOutsets = new Rect();
    private int mPanelChordingKey;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    private PanelFeatureState mPreparedPanel;
    private Transition mReenterTransition = USE_DEFAULT_TRANSITION;
    int mResourcesSetFlags;
    private Transition mReturnTransition = USE_DEFAULT_TRANSITION;
    private ImageView mRightIconView;
    private Transition mSharedElementEnterTransition = null;
    private Transition mSharedElementExitTransition = null;
    private Transition mSharedElementReenterTransition = USE_DEFAULT_TRANSITION;
    private Transition mSharedElementReturnTransition = USE_DEFAULT_TRANSITION;
    private Boolean mSharedElementsUseOverlay;
    private int mStatusBarColor = 0;
    InputQueue.Callback mTakeInputQueueCallback;
    Callback2 mTakeSurfaceCallback;
    private Rect mTempRect;
    private int mTextColor = 0;
    private CharSequence mTitle = null;
    private int mTitleColor = 0;
    private TextView mTitleView;
    private TransitionManager mTransitionManager;
    private int mUiOptions = 0;
    private int mVolumeControlStreamType = Integer.MIN_VALUE;
    private ZoomKeyController mZoomKeyController;

    protected class DecorView extends FrameLayout implements RootViewSurfaceTaker {
        private static final float FLOATING_MENU_BOTTOM_MARGIN = 48.0f;
        private static final float FLOATING_MENU_TOUCH_THRESHOLD = 6.0f;
        private static final boolean isElasticEnabled = true;
        float boundaryTouchX = 0.0f;
        float initialTouchX = 0.0f;
        float initialTouchY = 0.0f;
        private final BackgroundFallback mBackgroundFallback = new BackgroundFallback();
        private final Rect mBackgroundPadding = new Rect();
        private final int mBarEnterExitDuration;
        private boolean mChanging;
        int mDefaultOpacity = -1;
        private int mDisableSpenGesture = 0;
        private int mDownY;
        private final Rect mDrawingBounds = new Rect();
        private ObjectAnimator mFadeAnim;
        private final int mFeatureId;
        private ActionMode mFloatingActionMode;
        private View mFloatingActionModeOriginatingView;
        private ImageButton mFloatingMenuBtn = null;
        private Runnable mFloatingMenuFakeKeyDownRunnable = null;
        private Runnable mFloatingMenuFakeKeyUpRunnable = null;
        private long mFloatingMenuLastUpdateTime = 0;
        private int[] mFloatingMenuTouchedButtonPos = new int[2];
        private int[] mFloatingMenuTouchedPos = new int[2];
        private FloatingToolbar mFloatingToolbar;
        private OnPreDrawListener mFloatingToolbarPreDrawListener;
        private final Rect mFrameOffsets = new Rect();
        private final Rect mFramePadding = new Rect();
        private final Interpolator mHideInterpolator;
        private boolean mIsPenSelectionMode = false;
        private int mLastBottomInset = 0;
        private boolean mLastHasBottomStableInset = false;
        private boolean mLastHasRightStableInset = false;
        private boolean mLastHasTopStableInset = false;
        private int mLastRightInset = 0;
        private int mLastTopInset = 0;
        private int mLastWindowFlags = 0;
        private Drawable mMenuBackground;
        protected final ColorViewState mNavigationColorViewState = new ColorViewState(2, 134217728, 80, 5, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME, R.id.navigationBarBackground, 0);
        protected View mNavigationGuard;
        private ActionMode mPrimaryActionMode;
        private PopupWindow mPrimaryActionModePopup;
        private ActionBarContextView mPrimaryActionModeView;
        private int mRootScrollY = 0;
        private final Interpolator mShowInterpolator;
        private Runnable mShowPrimaryActionModePopup;
        private int mSpenUspLevel = -1;
        protected final ColorViewState mStatusColorViewState = new ColorViewState(4, 67108864, 48, 3, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME, R.id.statusBarBackground, 1024);
        protected View mStatusGuard;
        private boolean mWatchingForMenu;

        private class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
            private final ActionMode.Callback mWrapped;

            public ActionModeCallback2Wrapper(ActionMode.Callback wrapped) {
                this.mWrapped = wrapped;
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return this.mWrapped.onCreateActionMode(mode, menu);
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                DecorView.this.requestFitSystemWindows();
                return this.mWrapped.onPrepareActionMode(mode, menu);
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return this.mWrapped.onActionItemClicked(mode, item);
            }

            public void onDestroyActionMode(ActionMode mode) {
                boolean isMncApp;
                boolean isPrimary;
                boolean isFloating;
                int i = 1;
                this.mWrapped.onDestroyActionMode(mode);
                if (DecorView.this.mContext.getApplicationInfo().targetSdkVersion >= 23) {
                    isMncApp = true;
                } else {
                    isMncApp = false;
                }
                if (isMncApp) {
                    if (mode == DecorView.this.mPrimaryActionMode) {
                        isPrimary = true;
                    } else {
                        isPrimary = false;
                    }
                    if (mode == DecorView.this.mFloatingActionMode) {
                        isFloating = true;
                    } else {
                        isFloating = false;
                    }
                    if (!isPrimary && mode.getType() == 0) {
                        Log.e(PhoneWindow.TAG, "Destroying unexpected ActionMode instance of TYPE_PRIMARY; " + mode + " was not the current primary action mode! Expected " + DecorView.this.mPrimaryActionMode);
                    }
                    if (!isFloating && mode.getType() == 1) {
                        Log.e(PhoneWindow.TAG, "Destroying unexpected ActionMode instance of TYPE_FLOATING; " + mode + " was not the current floating action mode! Expected " + DecorView.this.mFloatingActionMode);
                    }
                    if (!isFloating && mode.getType() == 99) {
                        Log.e(PhoneWindow.TAG, "Destroying unexpected ActionMode instance of TYPE_FLOATING_TW; " + mode + " was not the current floating action mode! Expected " + DecorView.this.mFloatingActionMode);
                    }
                } else {
                    int i2;
                    if (mode.getType() == 0) {
                        isPrimary = true;
                    } else {
                        isPrimary = false;
                    }
                    if (mode.getType() == 1) {
                        i2 = 1;
                    } else {
                        i2 = 0;
                    }
                    if (mode.getType() != 99) {
                        i = 0;
                    }
                    isFloating = i2 | i;
                }
                if (isPrimary) {
                    if (DecorView.this.mPrimaryActionModePopup != null) {
                        DecorView.this.removeCallbacks(DecorView.this.mShowPrimaryActionModePopup);
                    }
                    if (DecorView.this.mPrimaryActionModeView != null) {
                        DecorView.this.endOnGoingFadeAnimation();
                        DecorView.this.mFadeAnim = ObjectAnimator.ofFloat(DecorView.this.mPrimaryActionModeView, View.ALPHA, new float[]{1.0f, 0.0f});
                        DecorView.this.mFadeAnim.addListener(new AnimatorListener() {
                            public void onAnimationStart(Animator animation) {
                            }

                            public void onAnimationEnd(Animator animation) {
                                DecorView.this.mPrimaryActionModeView.setVisibility(8);
                                if (DecorView.this.mPrimaryActionModePopup != null) {
                                    DecorView.this.mPrimaryActionModePopup.dismiss();
                                }
                                DecorView.this.mPrimaryActionModeView.removeAllViews();
                                DecorView.this.mFadeAnim = null;
                            }

                            public void onAnimationCancel(Animator animation) {
                            }

                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                        DecorView.this.mFadeAnim.start();
                    }
                    DecorView.this.mPrimaryActionMode = null;
                } else if (isFloating) {
                    DecorView.this.cleanupFloatingActionModeViews();
                    DecorView.this.mFloatingActionMode = null;
                }
                if (!(PhoneWindow.this.getCallback() == null || PhoneWindow.this.isDestroyed())) {
                    try {
                        PhoneWindow.this.getCallback().onActionModeFinished(mode);
                    } catch (AbstractMethodError e) {
                    }
                }
                DecorView.this.requestFitSystemWindows();
            }

            public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                if (this.mWrapped instanceof ActionMode.Callback2) {
                    ((ActionMode.Callback2) this.mWrapped).onGetContentRect(mode, view, outRect);
                } else {
                    super.onGetContentRect(mode, view, outRect);
                }
            }
        }

        public DecorView(Context context, int featureId) {
            super(context);
            this.mFeatureId = featureId;
            this.mShowInterpolator = AnimationUtils.loadInterpolator(context, R.interpolator.linear_out_slow_in);
            this.mHideInterpolator = AnimationUtils.loadInterpolator(context, R.interpolator.fast_out_linear_in);
            this.mBarEnterExitDuration = context.getResources().getInteger(R.integer.dock_enter_exit_duration);
            this.mDisableSpenGesture = Settings$System.getInt(this.mContext.getContentResolver(), "disable_pen_gesture", 0);
            PackageManager pm = this.mContext.getPackageManager();
            if (pm != null) {
                this.mSpenUspLevel = pm.getSystemFeatureLevel("com.sec.feature.spen_usp");
            }
        }

        public void setBackgroundFallback(int resId) {
            this.mBackgroundFallback.setDrawable(resId != 0 ? getContext().getDrawable(resId) : null);
            boolean z = getBackground() == null && !this.mBackgroundFallback.hasFallback();
            setWillNotDraw(z);
        }

        public void onDraw(Canvas c) {
            super.onDraw(c);
            this.mBackgroundFallback.draw(PhoneWindow.this.mContentRoot, c, PhoneWindow.this.mContentParent);
        }

        protected void drawKnoxBadge(Canvas canvas) {
            if (getContext().getUserId() >= 100 && !PersonaManager.isBBCContainer(getContext().getUserId()) && PersonaManager.isKnoxMultiwindowsSupported() && PhoneWindow.this.getAttributes().width == -1 && PhoneWindow.this.getAttributes().height == -1) {
                if (PhoneWindow.this.badgeIcon == null) {
                    PhoneWindow.this.badgeIcon = getUserBadgeIcon(getContext().getUserId());
                }
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                int badgeW = PhoneWindow.this.badgeIcon.getIntrinsicWidth();
                int badgeH = PhoneWindow.this.badgeIcon.getIntrinsicHeight();
                if (getViewRootImpl().getMultiWindowStyle().getType() == 2) {
                    Rect bounds = getViewRootImpl().getMultiWindowStyle().getBounds();
                    float resizedWidth = ((float) badgeW) * (((float) width) / ((float) (bounds.right - bounds.left)));
                    PhoneWindow.this.badgeIcon.setBounds(width - ((int) (((double) resizedWidth) + 0.5d)), height - ((int) (((double) (((float) badgeH) * (((float) height) / ((float) (bounds.bottom - bounds.top))))) + 0.5d)), width, height);
                } else {
                    PhoneWindow.this.badgeIcon.setBounds(width - badgeW, height - badgeH, width, height);
                }
                PhoneWindow.this.badgeIcon.draw(canvas);
            }
        }

        protected Drawable getUserBadgeIcon(int userId) {
            UserManager mUm = (UserManager) getContext().getSystemService(ImsConferenceState.USER);
            String userName = Settings$System.getStringForUser(this.mContext.getContentResolver(), "knox_name", userId);
            if (userName == null) {
                return getContext().getResources().getDrawable(R.drawable.ic_knox_badge);
            }
            if (PersonaManager.SECOND_KNOX_NAME.equals(userName)) {
                return getContext().getResources().getDrawable(R.drawable.ic_knox_badge2);
            }
            return getContext().getResources().getDrawable(R.drawable.ic_knox_badge);
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (PhoneWindow.this.mLastBackgroundResource == R.drawable.tw_screen_background_selector_light) {
                Drawable currWindowBackground = getBackground();
                if (currWindowBackground instanceof StateListDrawable) {
                    StateListDrawable statefulWindowBackground = (StateListDrawable) currWindowBackground;
                    int[] states = statefulWindowBackground.getState();
                    if (states != null && states.length > 0 && (statefulWindowBackground.getStateDrawable(0) instanceof BitmapDrawable)) {
                        Drawable newBackground = getContext().getDrawable(PhoneWindow.this.mLastBackgroundResource);
                        if (newBackground != null) {
                            setWindowBackground(newBackground);
                        }
                    }
                }
            }
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            boolean isDown;
            int keyCode = event.getKeyCode();
            if (event.getAction() == 0) {
                isDown = true;
            } else {
                isDown = false;
            }
            if (isDown && event.getRepeatCount() == 0) {
                if (PhoneWindow.this.mPanelChordingKey > 0 && PhoneWindow.this.mPanelChordingKey != keyCode && dispatchKeyShortcutEvent(event)) {
                    return true;
                }
                if (PhoneWindow.this.mPreparedPanel != null && PhoneWindow.this.mPreparedPanel.isOpen && PhoneWindow.this.performPanelShortcut(PhoneWindow.this.mPreparedPanel, keyCode, event, 0)) {
                    return true;
                }
            }
            if (!PhoneWindow.this.isDestroyed()) {
                Window.Callback cb = PhoneWindow.this.getCallback();
                boolean handled = (cb == null || this.mFeatureId >= 0) ? super.dispatchKeyEvent(event) : cb.dispatchKeyEvent(event);
                if (handled) {
                    if (keyCode != 82 || isDown) {
                        return true;
                    }
                    PhoneWindow.this.mPanelChordingKey = 0;
                    return true;
                }
            }
            return isDown ? PhoneWindow.this.onKeyDown(this.mFeatureId, event.getKeyCode(), event) : PhoneWindow.this.onKeyUp(this.mFeatureId, event.getKeyCode(), event);
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent ev) {
            if (PhoneWindow.this.mPreparedPanel == null || !PhoneWindow.this.performPanelShortcut(PhoneWindow.this.mPreparedPanel, ev.getKeyCode(), ev, 1)) {
                Window.Callback cb = PhoneWindow.this.getCallback();
                boolean handled = (cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchKeyShortcutEvent(ev) : cb.dispatchKeyShortcutEvent(ev);
                if (handled) {
                    return true;
                }
                PanelFeatureState st = PhoneWindow.this.getPanelState(0, false);
                if (st != null && PhoneWindow.this.mPreparedPanel == null) {
                    PhoneWindow.this.preparePanel(st, ev);
                    handled = PhoneWindow.this.performPanelShortcut(st, ev.getKeyCode(), ev, 1);
                    st.isPrepared = false;
                    if (handled) {
                        return true;
                    }
                }
                return false;
            } else if (PhoneWindow.this.mPreparedPanel == null) {
                return true;
            } else {
                PhoneWindow.this.mPreparedPanel.isHandled = true;
                return true;
            }
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (this.mSpenUspLevel >= 3) {
                int action = ev.getAction();
                if (ev.getToolType(0) == 2) {
                    switch (action) {
                        case 0:
                            if ((ev.getButtonState() & 32) == 0) {
                                this.mIsPenSelectionMode = false;
                                break;
                            }
                            this.mIsPenSelectionMode = true;
                            ev.setAction(211);
                            break;
                        case 1:
                            if (this.mIsPenSelectionMode) {
                                ev.setAction(212);
                            }
                            this.mIsPenSelectionMode = false;
                            break;
                        case 2:
                            if (this.mIsPenSelectionMode) {
                                ev.setAction(213);
                                break;
                            }
                            break;
                    }
                }
            }
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0) {
                return super.dispatchTouchEvent(ev);
            }
            return cb.dispatchTouchEvent(ev);
        }

        public boolean dispatchTrackballEvent(MotionEvent ev) {
            Window.Callback cb = PhoneWindow.this.getCallback();
            return (cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchTrackballEvent(ev) : cb.dispatchTrackballEvent(ev);
        }

        public boolean dispatchGenericMotionEvent(MotionEvent ev) {
            Window.Callback cb = PhoneWindow.this.getCallback();
            return (cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchGenericMotionEvent(ev) : cb.dispatchGenericMotionEvent(ev);
        }

        public boolean superDispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == 4) {
                int action = event.getAction();
                if (this.mPrimaryActionMode != null) {
                    if (action != 1) {
                        return true;
                    }
                    this.mPrimaryActionMode.finish();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }

        public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
            return super.dispatchKeyShortcutEvent(event);
        }

        public boolean superDispatchTouchEvent(MotionEvent event) {
            return super.dispatchTouchEvent(event);
        }

        public boolean superDispatchTrackballEvent(MotionEvent event) {
            return super.dispatchTrackballEvent(event);
        }

        public boolean superDispatchGenericMotionEvent(MotionEvent event) {
            return super.dispatchGenericMotionEvent(event);
        }

        public boolean onTouchEvent(MotionEvent event) {
            return onInterceptTouchEvent(event);
        }

        private boolean isOutOfBounds(int x, int y) {
            return x < -5 || y < -5 || x > getWidth() + 5 || y > getHeight() + 5;
        }

        public boolean onInterceptTouchEvent(MotionEvent event) {
            if ("sugar".equals(SystemProperties.get("ro.build.scafe.syrup")) && event.getAction() == 0) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
                if (event.getToolType(0) == 2) {
                    imm.setWACOMPen(true);
                    PhoneWindow.penState = true;
                } else if (event.getToolType(0) != 2) {
                    imm.setWACOMPen(false);
                    PhoneWindow.penState = false;
                }
            }
            if (this.mDisableSpenGesture == 0 && event.getToolType(0) == 2 && (event.getButtonState() & 32) != 0 && event.getAction() == 212) {
                event.setAction(214);
            }
            switch (event.getAction() & 255) {
                case 2:
                    if (event.getPointerCount() == 2) {
                        this.boundaryTouchX = this.initialTouchX - event.getRawX();
                        break;
                    }
                    break;
                case 5:
                    this.initialTouchX = event.getRawX();
                    break;
                case 6:
                    this.initialTouchX = 0.0f;
                    if (this.boundaryTouchX > 100.0f && event.getPointerCount() == 2) {
                        String[] list = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()).list(new FilenameFilter() {
                            public boolean accept(File file, String s) {
                                return file.isDirectory();
                            }
                        });
                        if (list != null) {
                            List<String> array = Arrays.asList(list);
                            String[] splits = this.mContext.getPackageName().split("\\.");
                            if (array.contains(splits[splits.length - 1])) {
                                Intent i = new Intent("android.app.elastic");
                                i.putExtra("appDir", splits[splits.length - 1]);
                                i.addFlags(268435456);
                                i.addFlags(536870912);
                                i.addCategory("android.intent.category.DEFAULT");
                                try {
                                    this.mContext.startActivityForKey(i);
                                    break;
                                } catch (ActivityNotFoundException e) {
                                    Log.w(PhoneWindow.TAG, "No activity to launch elastic.", e);
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
            int action = event.getAction();
            if (this.mFeatureId < 0 || action != 0 || !isOutOfBounds((int) event.getX(), (int) event.getY())) {
                return false;
            }
            PhoneWindow.this.closePanel(this.mFeatureId);
            return true;
        }

        public void sendAccessibilityEvent(int eventType) {
            if (!AccessibilityManager.getInstance(this.mContext).isEnabled()) {
                return;
            }
            if ((this.mFeatureId == 0 || this.mFeatureId == 6 || this.mFeatureId == 2 || this.mFeatureId == 5) && getChildCount() == 1) {
                getChildAt(0).sendAccessibilityEvent(eventType);
            } else {
                super.sendAccessibilityEvent(eventType);
            }
        }

        public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (cb == null || PhoneWindow.this.isDestroyed() || !cb.dispatchPopulateAccessibilityEvent(event)) {
                return super.dispatchPopulateAccessibilityEventInternal(event);
            }
            return true;
        }

        protected boolean setFrame(int l, int t, int r, int b) {
            boolean changed = super.setFrame(l, t, r, b);
            if (changed) {
                Rect drawingBounds = this.mDrawingBounds;
                getDrawingRect(drawingBounds);
                Drawable fg = getForeground();
                if (fg != null) {
                    Rect frameOffsets = this.mFrameOffsets;
                    drawingBounds.left += frameOffsets.left;
                    drawingBounds.top += frameOffsets.top;
                    drawingBounds.right -= frameOffsets.right;
                    drawingBounds.bottom -= frameOffsets.bottom;
                    fg.setBounds(drawingBounds);
                    Rect framePadding = this.mFramePadding;
                    drawingBounds.left += framePadding.left - frameOffsets.left;
                    drawingBounds.top += framePadding.top - frameOffsets.top;
                    drawingBounds.right -= framePadding.right - frameOffsets.right;
                    drawingBounds.bottom -= framePadding.bottom - frameOffsets.bottom;
                }
                Drawable bg = getBackground();
                if (bg != null) {
                    bg.setBounds(drawingBounds);
                }
            }
            return changed;
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int mode;
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            boolean isPortrait = metrics.widthPixels < metrics.heightPixels;
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            boolean fixedWidth = false;
            if (widthMode == Integer.MIN_VALUE) {
                TypedValue tvw = isPortrait ? PhoneWindow.this.mFixedWidthMinor : PhoneWindow.this.mFixedWidthMajor;
                if (!(tvw == null || tvw.type == 0)) {
                    int w;
                    if (tvw.type == 5) {
                        w = (int) tvw.getDimension(metrics);
                    } else if (tvw.type == 6) {
                        w = (int) tvw.getFraction((float) metrics.widthPixels, (float) metrics.widthPixels);
                    } else {
                        w = 0;
                    }
                    if (w > 0) {
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(w, MeasureSpec.getSize(widthMeasureSpec)), 1073741824);
                        fixedWidth = true;
                    }
                }
            }
            if (heightMode == Integer.MIN_VALUE) {
                TypedValue tvh = isPortrait ? PhoneWindow.this.mFixedHeightMajor : PhoneWindow.this.mFixedHeightMinor;
                if (!(tvh == null || tvh.type == 0)) {
                    int h;
                    if (tvh.type == 5) {
                        h = (int) tvh.getDimension(metrics);
                    } else if (tvh.type == 6) {
                        h = (int) tvh.getFraction((float) metrics.heightPixels, (float) metrics.heightPixels);
                    } else {
                        h = 0;
                    }
                    if (h > 0) {
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(h, MeasureSpec.getSize(heightMeasureSpec)), 1073741824);
                    }
                }
            }
            getOutsets(PhoneWindow.this.mOutsets);
            if (PhoneWindow.this.mOutsets.top > 0 || PhoneWindow.this.mOutsets.bottom > 0) {
                mode = MeasureSpec.getMode(heightMeasureSpec);
                if (mode != 0) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec((PhoneWindow.this.mOutsets.top + MeasureSpec.getSize(heightMeasureSpec)) + PhoneWindow.this.mOutsets.bottom, mode);
                }
            }
            if (PhoneWindow.this.mOutsets.left > 0 || PhoneWindow.this.mOutsets.right > 0) {
                mode = MeasureSpec.getMode(widthMeasureSpec);
                if (mode != 0) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((PhoneWindow.this.mOutsets.left + MeasureSpec.getSize(widthMeasureSpec)) + PhoneWindow.this.mOutsets.right, mode);
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = getMeasuredWidth();
            boolean measure = false;
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, 1073741824);
            if (!fixedWidth && widthMode == Integer.MIN_VALUE) {
                TypedValue tv = isPortrait ? PhoneWindow.this.mMinWidthMinor : PhoneWindow.this.mMinWidthMajor;
                if (tv.type != 0) {
                    int min;
                    if (tv.type == 5) {
                        min = (int) tv.getDimension(metrics);
                    } else if (tv.type == 6) {
                        min = (int) tv.getFraction((float) metrics.widthPixels, (float) metrics.widthPixels);
                    } else {
                        min = 0;
                    }
                    if (width < min) {
                        widthMeasureSpec = MeasureSpec.makeMeasureSpec(min, 1073741824);
                        measure = true;
                    }
                }
            }
            if (measure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            getOutsets(PhoneWindow.this.mOutsets);
            if (PhoneWindow.this.mOutsets.left > 0) {
                offsetLeftAndRight(-PhoneWindow.this.mOutsets.left);
            }
            if (PhoneWindow.this.mOutsets.top > 0) {
                offsetTopAndBottom(-PhoneWindow.this.mOutsets.top);
            }
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (this.mMenuBackground != null) {
                this.mMenuBackground.draw(canvas);
            }
        }

        public boolean showContextMenuForChild(View originalView) {
            if (PhoneWindow.this.mContextMenu == null) {
                PhoneWindow.this.mContextMenu = new ContextMenuBuilder(getContext());
                PhoneWindow.this.mContextMenu.setCallback(PhoneWindow.this.mContextMenuCallback);
            } else {
                PhoneWindow.this.mContextMenu.clearAll();
            }
            MenuDialogHelper helper = PhoneWindow.this.mContextMenu.show(originalView, originalView.getWindowToken());
            if (helper != null) {
                helper.setPresenterCallback(PhoneWindow.this.mContextMenuCallback);
            } else if (PhoneWindow.this.mContextMenuHelper != null) {
                PhoneWindow.this.mContextMenuHelper.dismiss();
            }
            PhoneWindow.this.mContextMenuHelper = helper;
            if (helper != null) {
                return true;
            }
            return false;
        }

        public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
            return startActionModeForChild(originalView, callback, 0);
        }

        public ActionMode startActionModeForChild(View child, ActionMode.Callback callback, int type) {
            return startActionMode(child, callback, type);
        }

        public ActionMode startActionMode(ActionMode.Callback callback) {
            return startActionMode(callback, 0);
        }

        public ActionMode startActionMode(ActionMode.Callback callback, int type) {
            return startActionMode(this, callback, type);
        }

        private ActionMode startActionMode(View originatingView, ActionMode.Callback callback, int type) {
            ActionMode.Callback2 wrappedCallback = new ActionModeCallback2Wrapper(callback);
            ActionMode mode = null;
            if (!(PhoneWindow.this.getCallback() == null || PhoneWindow.this.isDestroyed())) {
                try {
                    mode = PhoneWindow.this.getCallback().onWindowStartingActionMode(wrappedCallback, type);
                } catch (AbstractMethodError e) {
                    if (type == 0) {
                        try {
                            mode = PhoneWindow.this.getCallback().onWindowStartingActionMode(wrappedCallback);
                        } catch (AbstractMethodError e2) {
                        }
                    }
                }
            }
            if (mode == null) {
                mode = createActionMode(type, wrappedCallback, originatingView);
                if (mode == null || !wrappedCallback.onCreateActionMode(mode, mode.getMenu())) {
                    mode = null;
                } else {
                    setHandledActionMode(mode);
                }
            } else if (mode.getType() == 0) {
                cleanupPrimaryActionMode();
                this.mPrimaryActionMode = mode;
            } else if (mode.getType() == 1 || mode.getType() == 99) {
                if (this.mFloatingActionMode != null) {
                    this.mFloatingActionMode.finish();
                }
                this.mFloatingActionMode = mode;
            }
            if (!(mode == null || PhoneWindow.this.getCallback() == null || PhoneWindow.this.isDestroyed())) {
                try {
                    PhoneWindow.this.getCallback().onActionModeStarted(mode);
                } catch (AbstractMethodError e3) {
                }
            }
            return mode;
        }

        private void cleanupPrimaryActionMode() {
            if (this.mPrimaryActionMode != null) {
                this.mPrimaryActionMode.finish();
                this.mPrimaryActionMode = null;
            }
            if (this.mPrimaryActionModeView != null) {
                this.mPrimaryActionModeView.killMode();
            }
        }

        private void cleanupFloatingActionModeViews() {
            if (this.mFloatingToolbar != null) {
                this.mFloatingToolbar.dismiss();
                this.mFloatingToolbar = null;
            }
            if (this.mFloatingActionModeOriginatingView != null) {
                if (this.mFloatingToolbarPreDrawListener != null) {
                    this.mFloatingActionModeOriginatingView.getViewTreeObserver().removeOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
                    this.mFloatingToolbarPreDrawListener = null;
                }
                this.mFloatingActionModeOriginatingView = null;
            }
        }

        public void startChanging() {
            this.mChanging = true;
        }

        public void finishChanging() {
            this.mChanging = false;
            drawableChanged();
        }

        public void setWindowBackground(Drawable drawable) {
            if (getBackground() != drawable) {
                setBackgroundDrawable(drawable);
                if (drawable != null) {
                    drawable.getPadding(this.mBackgroundPadding);
                } else {
                    this.mBackgroundPadding.setEmpty();
                }
                drawableChanged();
            }
        }

        public void setBackgroundDrawable(Drawable d) {
            super.setBackgroundDrawable(d);
            if (getWindowToken() != null) {
                updateWindowResizeState();
            }
        }

        public void setWindowFrame(Drawable drawable) {
            if (getForeground() != drawable) {
                setForeground(drawable);
                if (drawable != null) {
                    drawable.getPadding(this.mFramePadding);
                } else {
                    this.mFramePadding.setEmpty();
                }
                drawableChanged();
            }
        }

        public void onWindowSystemUiVisibilityChanged(int visible) {
            updateColorViews(null, true);
        }

        public WindowInsets onApplyWindowInsets(WindowInsets insets) {
            this.mFrameOffsets.set(insets.getSystemWindowInsets());
            insets = updateStatusGuard(updateColorViews(insets, true));
            updateNavigationGuard(insets);
            if (getForeground() != null) {
                drawableChanged();
            }
            return insets;
        }

        public boolean isTransitionGroup() {
            return false;
        }

        private WindowInsets updateColorViews(WindowInsets insets, boolean animate) {
            LayoutParams attrs = PhoneWindow.this.getAttributes();
            int sysUiVisibility = attrs.systemUiVisibility | getWindowSystemUiVisibility();
            if (!PhoneWindow.this.mIsFloating) {
                boolean disallowAnimate = (!isLaidOut()) | (((this.mLastWindowFlags ^ attrs.flags) & Integer.MIN_VALUE) != 0 ? 1 : 0);
                this.mLastWindowFlags = attrs.flags;
                if (insets != null) {
                    this.mLastTopInset = Math.min(insets.getStableInsetTop(), insets.getSystemWindowInsetTop());
                    this.mLastBottomInset = Math.min(insets.getStableInsetBottom(), insets.getSystemWindowInsetBottom());
                    this.mLastRightInset = Math.min(insets.getStableInsetRight(), insets.getSystemWindowInsetRight());
                    boolean hasTopStableInset = insets.getStableInsetTop() != 0;
                    disallowAnimate |= hasTopStableInset != this.mLastHasTopStableInset ? 1 : 0;
                    this.mLastHasTopStableInset = hasTopStableInset;
                    boolean hasBottomStableInset = insets.getStableInsetBottom() != 0;
                    disallowAnimate |= hasBottomStableInset != this.mLastHasBottomStableInset ? 1 : 0;
                    this.mLastHasBottomStableInset = hasBottomStableInset;
                    boolean hasRightStableInset = insets.getStableInsetRight() != 0;
                    disallowAnimate |= hasRightStableInset != this.mLastHasRightStableInset ? 1 : 0;
                    this.mLastHasRightStableInset = hasRightStableInset;
                }
                boolean navBarToRightEdge = this.mLastBottomInset == 0 && this.mLastRightInset > 0;
                int navBarSize = navBarToRightEdge ? this.mLastRightInset : this.mLastBottomInset;
                ColorViewState colorViewState = this.mNavigationColorViewState;
                int access$1900 = PhoneWindow.this.mNavigationBarColor;
                boolean z = animate && !disallowAnimate;
                updateColorViewInt(colorViewState, sysUiVisibility, access$1900, navBarSize, navBarToRightEdge, 0, z);
                boolean statusBarNeedsRightInset = navBarToRightEdge && this.mNavigationColorViewState.present;
                int statusBarRightInset = statusBarNeedsRightInset ? this.mLastRightInset : 0;
                ColorViewState colorViewState2 = this.mStatusColorViewState;
                int access$2000 = PhoneWindow.this.mStatusBarColor;
                int i = this.mLastTopInset;
                if (animate && disallowAnimate) {
                    updateColorViewInt(colorViewState2, sysUiVisibility, access$2000, i, false, statusBarRightInset, false);
                } else {
                    updateColorViewInt(colorViewState2, sysUiVisibility, access$2000, i, false, statusBarRightInset, false);
                }
            }
            boolean consumingNavBar = (attrs.flags & Integer.MIN_VALUE) != 0 && (sysUiVisibility & 512) == 0 && (sysUiVisibility & 2) == 0 && attrs.coverMode == 0;
            int consumedRight = consumingNavBar ? this.mLastRightInset : 0;
            int consumedBottom = consumingNavBar ? this.mLastBottomInset : 0;
            if (PhoneWindow.this.mContentRoot != null && (PhoneWindow.this.mContentRoot.getLayoutParams() instanceof MarginLayoutParams)) {
                ViewGroup.LayoutParams lp = (MarginLayoutParams) PhoneWindow.this.mContentRoot.getLayoutParams();
                if (!(lp.rightMargin == consumedRight && lp.bottomMargin == consumedBottom)) {
                    lp.rightMargin = consumedRight;
                    lp.bottomMargin = consumedBottom;
                    PhoneWindow.this.mContentRoot.setLayoutParams(lp);
                    if (insets == null) {
                        requestApplyInsets();
                    }
                }
                if (insets != null) {
                    insets = insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight() - consumedRight, insets.getSystemWindowInsetBottom() - consumedBottom);
                }
            }
            if (insets != null) {
                return insets.consumeStableInsets();
            }
            return insets;
        }

        protected void updateColorViewInt(final ColorViewState state, int sysUiVis, int color, int size, boolean verticalBar, int rightMargin, boolean animate) {
            int resolvedHeight;
            boolean z = size > 0 && (state.systemUiHideFlag & sysUiVis) == 0 && (PhoneWindow.this.getAttributes().flags & state.hideWindowFlag) == 0 && (PhoneWindow.this.getAttributes().flags & Integer.MIN_VALUE) != 0;
            state.present = z;
            boolean show = state.present && (-16777216 & color) != 0 && (PhoneWindow.this.getAttributes().flags & state.translucentFlag) == 0;
            boolean visibilityChanged = false;
            View view = state.view;
            if (verticalBar) {
                resolvedHeight = -1;
            } else {
                resolvedHeight = size;
            }
            int resolvedWidth = verticalBar ? size : -1;
            int resolvedGravity = verticalBar ? state.horizontalGravity : state.verticalGravity;
            FrameLayout.LayoutParams lp;
            if (view != null) {
                int vis = show ? 0 : 4;
                visibilityChanged = state.targetVisibility != vis;
                state.targetVisibility = vis;
                lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (!(lp.height == resolvedHeight && lp.width == resolvedWidth && lp.gravity == resolvedGravity && lp.rightMargin == rightMargin)) {
                    lp.height = resolvedHeight;
                    lp.width = resolvedWidth;
                    lp.gravity = resolvedGravity;
                    lp.rightMargin = rightMargin;
                    view.setLayoutParams(lp);
                }
                if (show) {
                    view.setBackgroundColor(color);
                }
            } else if (show) {
                view = new View(this.mContext);
                state.view = view;
                view.setBackgroundColor(color);
                view.setTransitionName(state.transitionName);
                view.setId(state.id);
                visibilityChanged = true;
                view.setVisibility(4);
                state.targetVisibility = 0;
                lp = new FrameLayout.LayoutParams(resolvedWidth, resolvedHeight, resolvedGravity);
                lp.rightMargin = rightMargin;
                addView(view, (ViewGroup.LayoutParams) lp);
                updateColorViewTranslations();
            }
            if (visibilityChanged) {
                view.animate().cancel();
                if (!animate) {
                    view.setAlpha(1.0f);
                    view.setVisibility(show ? 0 : 4);
                } else if (show) {
                    if (view.getVisibility() != 0) {
                        view.setVisibility(0);
                        view.setAlpha(0.0f);
                    }
                    view.animate().alpha(1.0f).setInterpolator(this.mShowInterpolator).setDuration((long) this.mBarEnterExitDuration);
                } else {
                    view.animate().alpha(0.0f).setInterpolator(this.mHideInterpolator).setDuration((long) this.mBarEnterExitDuration).withEndAction(new Runnable() {
                        public void run() {
                            if (state.view != null) {
                                state.view.setAlpha(1.0f);
                                state.view.setVisibility(4);
                            }
                        }
                    });
                }
            }
        }

        private void updateColorViewTranslations() {
            float f = 0.0f;
            int rootScrollY = this.mRootScrollY;
            if (this.mStatusColorViewState.view != null) {
                this.mStatusColorViewState.view.setTranslationY(rootScrollY > 0 ? (float) rootScrollY : 0.0f);
            }
            if (this.mNavigationColorViewState.view != null) {
                View view = this.mNavigationColorViewState.view;
                if (rootScrollY < 0) {
                    f = (float) rootScrollY;
                }
                view.setTranslationY(f);
            }
        }

        private WindowInsets updateStatusGuard(WindowInsets insets) {
            boolean showStatusGuard = false;
            if (this.mPrimaryActionModeView != null && (this.mPrimaryActionModeView.getLayoutParams() instanceof MarginLayoutParams)) {
                MarginLayoutParams mlp = (MarginLayoutParams) this.mPrimaryActionModeView.getLayoutParams();
                boolean mlpChanged = false;
                if (this.mPrimaryActionModeView.isShown()) {
                    if (PhoneWindow.this.mTempRect == null) {
                        PhoneWindow.this.mTempRect = new Rect();
                    }
                    Rect rect = PhoneWindow.this.mTempRect;
                    PhoneWindow.this.mContentParent.computeSystemWindowInsets(insets, rect);
                    if (mlp.topMargin != (rect.top == 0 ? insets.getSystemWindowInsetTop() : 0)) {
                        mlpChanged = true;
                        mlp.topMargin = insets.getSystemWindowInsetTop();
                        if (this.mStatusGuard == null) {
                            this.mStatusGuard = new View(this.mContext);
                            this.mStatusGuard.setBackgroundColor(this.mContext.getColor(R.color.input_method_navigation_guard));
                            addView(this.mStatusGuard, indexOfChild(this.mStatusColorViewState.view), new FrameLayout.LayoutParams(-1, mlp.topMargin, 8388659));
                        } else {
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mStatusGuard.getLayoutParams();
                            if (lp.height != mlp.topMargin) {
                                lp.height = mlp.topMargin;
                                this.mStatusGuard.setLayoutParams(lp);
                            }
                        }
                    }
                    showStatusGuard = this.mStatusGuard != null;
                    boolean z = ((PhoneWindow.this.getLocalFeatures() & 1024) == 0) && showStatusGuard;
                    insets = insets.consumeSystemWindowInsets(false, z, false, false);
                } else if (mlp.topMargin != 0) {
                    mlpChanged = true;
                    mlp.topMargin = 0;
                }
                if (mlpChanged) {
                    this.mPrimaryActionModeView.setLayoutParams(mlp);
                }
            }
            if (this.mStatusGuard != null) {
                this.mStatusGuard.setVisibility(showStatusGuard ? 0 : 8);
            }
            return insets;
        }

        private void updateNavigationGuard(WindowInsets insets) {
            if (PhoneWindow.this.getAttributes().type == LayoutParams.TYPE_INPUT_METHOD) {
                if (PhoneWindow.this.mContentParent != null && (PhoneWindow.this.mContentParent.getLayoutParams() instanceof MarginLayoutParams)) {
                    MarginLayoutParams mlp = (MarginLayoutParams) PhoneWindow.this.mContentParent.getLayoutParams();
                    mlp.bottomMargin = insets.getSystemWindowInsetBottom();
                    PhoneWindow.this.mContentParent.setLayoutParams(mlp);
                }
                if (this.mNavigationGuard == null) {
                    this.mNavigationGuard = new View(this.mContext);
                    this.mNavigationGuard.setBackgroundColor(this.mContext.getColor(R.color.input_method_navigation_guard));
                    addView(this.mNavigationGuard, indexOfChild(this.mNavigationColorViewState.view), new FrameLayout.LayoutParams(-1, insets.getSystemWindowInsetBottom(), 8388691));
                    return;
                }
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.mNavigationGuard.getLayoutParams();
                lp.height = insets.getSystemWindowInsetBottom();
                this.mNavigationGuard.setLayoutParams(lp);
            }
        }

        private void drawableChanged() {
            if (!this.mChanging) {
                setPadding(this.mFramePadding.left + this.mBackgroundPadding.left, this.mFramePadding.top + this.mBackgroundPadding.top, this.mFramePadding.right + this.mBackgroundPadding.right, this.mFramePadding.bottom + this.mBackgroundPadding.bottom);
                requestLayout();
                invalidate();
                int opacity = -1;
                Drawable bg = getBackground();
                Drawable fg = getForeground();
                if (bg != null) {
                    if (fg == null) {
                        opacity = bg.getOpacity();
                    } else if (this.mFramePadding.left > 0 || this.mFramePadding.top > 0 || this.mFramePadding.right > 0 || this.mFramePadding.bottom > 0) {
                        opacity = -3;
                    } else {
                        int fop = fg.getOpacity();
                        int bop = bg.getOpacity();
                        if (fop == -1 || bop == -1) {
                            opacity = -1;
                        } else if (fop == 0) {
                            opacity = bop;
                        } else if (bop == 0) {
                            opacity = fop;
                        } else {
                            opacity = Drawable.resolveOpacity(fop, bop);
                        }
                    }
                }
                this.mDefaultOpacity = opacity;
                if (this.mFeatureId < 0) {
                    PhoneWindow.this.setDefaultWindowFormat(opacity);
                }
            }
        }

        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
            if (!(!PhoneWindow.this.hasFeature(0) || hasWindowFocus || PhoneWindow.this.mPanelChordingKey == 0)) {
                PhoneWindow.this.closePanel(0);
            }
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (!(cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0)) {
                cb.onWindowFocusChanged(hasWindowFocus);
            }
            if (this.mPrimaryActionMode != null) {
                this.mPrimaryActionMode.onWindowFocusChanged(hasWindowFocus);
            }
            if (this.mFloatingActionMode != null) {
                this.mFloatingActionMode.onWindowFocusChanged(hasWindowFocus);
            }
        }

        void updateWindowResizeState() {
            Drawable bg = getBackground();
            boolean z = bg == null || bg.getOpacity() != -1;
            hackTurnOffWindowResizeAnim(z);
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateWindowResizeState();
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (!(cb == null || PhoneWindow.this.isDestroyed() || this.mFeatureId >= 0)) {
                cb.onAttachedToWindow();
            }
            if (this.mFeatureId == -1) {
                PhoneWindow.this.openPanelsAfterRestore();
            }
            if (isFloatingMenuEnabled()) {
                View rootView = getRootView();
                if ((rootView instanceof DecorView) && !rootView.equals(this)) {
                    setFloatingMenuEnabled(false);
                } else if (getChildCount() > 1 && this.mFloatingMenuBtn.equals(getChildAt(0))) {
                    Log.d(PhoneWindow.TAG, "Change the child order of the floating menu button.");
                    removeView(this.mFloatingMenuBtn);
                    addView(this.mFloatingMenuBtn);
                }
            }
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (cb != null && this.mFeatureId < 0) {
                cb.onDetachedFromWindow();
            }
            if (PhoneWindow.this.mDecorContentParent != null) {
                PhoneWindow.this.mDecorContentParent.dismissPopups();
            }
            if (this.mPrimaryActionModePopup != null) {
                removeCallbacks(this.mShowPrimaryActionModePopup);
                if (this.mPrimaryActionModePopup.isShowing()) {
                    this.mPrimaryActionModePopup.dismiss();
                }
                this.mPrimaryActionModePopup = null;
            }
            if (this.mFloatingToolbar != null) {
                this.mFloatingToolbar.dismiss();
                this.mFloatingToolbar = null;
            }
            PanelFeatureState st = PhoneWindow.this.getPanelState(0, false);
            if (st != null && st.menu != null && this.mFeatureId < 0) {
                st.menu.close();
            }
        }

        public void onCloseSystemDialogs(String reason) {
            if (this.mFeatureId >= 0) {
                PhoneWindow.this.closeAllPanels();
            }
        }

        public Callback2 willYouTakeTheSurface() {
            return this.mFeatureId < 0 ? PhoneWindow.this.mTakeSurfaceCallback : null;
        }

        public InputQueue.Callback willYouTakeTheInputQueue() {
            return this.mFeatureId < 0 ? PhoneWindow.this.mTakeInputQueueCallback : null;
        }

        public void setSurfaceType(int type) {
            PhoneWindow.this.setType(type);
        }

        public void setSurfaceFormat(int format) {
            PhoneWindow.this.setFormat(format);
        }

        public void setSurfaceKeepScreenOn(boolean keepOn) {
            if (keepOn) {
                PhoneWindow.this.addFlags(128);
            } else {
                PhoneWindow.this.clearFlags(128);
            }
        }

        public void onRootViewScrollYChanged(int rootScrollY) {
            this.mRootScrollY = rootScrollY;
            updateColorViewTranslations();
        }

        public void setFloatingMenuEnabled(boolean enable) {
            Log.d(PhoneWindow.TAG, "*FMB* setFloatingMenuEnabled enable : " + enable + ", mFloatingMenuBtn : " + this.mFloatingMenuBtn);
            if (enable) {
                if (this.mFloatingMenuBtn != null) {
                    this.mFloatingMenuBtn.setVisibility(0);
                    return;
                }
                this.mFloatingMenuBtn = new FloatingMenuButton(this.mContext);
                this.mFloatingMenuBtn.setImageResource(R.drawable.tw_fmb_mtrl);
                this.mFloatingMenuBtn.setBackground(null);
                this.mFloatingMenuBtn.setPadding(0, 0, 0, 0);
                this.mFloatingMenuBtn.setFocusable(false);
                this.mFloatingMenuBtn.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        if (action == 0) {
                            DecorView.this.mFloatingMenuTouchedPos[0] = (int) event.getRawX();
                            DecorView.this.mFloatingMenuTouchedPos[1] = (int) event.getRawY();
                            DecorView.this.mFloatingMenuTouchedButtonPos[0] = (int) v.getX();
                            DecorView.this.mFloatingMenuTouchedButtonPos[1] = (int) v.getY();
                        } else if (action == 2) {
                            if (!v.isHardwareAccelerated()) {
                                now = SystemClock.uptimeMillis();
                                long timeDiff = now - DecorView.this.mFloatingMenuLastUpdateTime;
                                if (timeDiff > 0 && timeDiff < 20) {
                                    return false;
                                }
                                DecorView.this.mFloatingMenuLastUpdateTime = now;
                            }
                            int newX = DecorView.this.mFloatingMenuTouchedButtonPos[0] + (((int) event.getRawX()) - DecorView.this.mFloatingMenuTouchedPos[0]);
                            int newY = DecorView.this.mFloatingMenuTouchedButtonPos[1] + (((int) event.getRawY()) - DecorView.this.mFloatingMenuTouchedPos[1]);
                            ViewParent parent = v.getParent();
                            if (parent instanceof View) {
                                int statusBarHeight = v.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
                                newX = Math.min(Math.max(newX, 0), ((View) parent).getWidth() - v.getWidth());
                                newY = Math.min(Math.max(newY, statusBarHeight), ((View) parent).getHeight() - v.getHeight());
                            }
                            v.setX((float) newX);
                            v.setY((float) newY);
                            if ((v instanceof FloatingMenuButton) && ((FloatingMenuButton) v).isViewInTransparentRegion()) {
                                v.requestLayout();
                            }
                        } else if (action == 1) {
                            int moveX = ((int) v.getX()) - DecorView.this.mFloatingMenuTouchedButtonPos[0];
                            int moveY = ((int) v.getY()) - DecorView.this.mFloatingMenuTouchedButtonPos[1];
                            int threshold = (int) (((double) (v.getContext().getResources().getDisplayMetrics().density * DecorView.FLOATING_MENU_TOUCH_THRESHOLD)) + 0.5d);
                            if ((moveX * moveX) + (moveY * moveY) < threshold * threshold) {
                                if (v.isAttachedToWindow() && v.hasWindowFocus()) {
                                    now = SystemClock.uptimeMillis();
                                    KeyEvent keyDown = new KeyEvent(now, now, 0, 82, 0);
                                    KeyEvent keyUp = new KeyEvent(now, now + 30, 1, 82, 0);
                                    InputManager im = InputManager.getInstance();
                                    boolean success = im.injectInputEvent(keyDown, 0) && im.injectInputEvent(keyUp, 0);
                                    if (!success) {
                                        v.post(DecorView.this.mFloatingMenuFakeKeyDownRunnable);
                                        v.post(DecorView.this.mFloatingMenuFakeKeyUpRunnable);
                                    }
                                } else {
                                    Log.d(PhoneWindow.TAG, "Failed to dispatch the MenuEvent : " + v.isAttachedToWindow() + " " + v.hasWindowFocus());
                                }
                            }
                        }
                        return false;
                    }
                });
                this.mFloatingMenuFakeKeyDownRunnable = new Runnable() {
                    public void run() {
                        PhoneWindow.this.onKeyDownPanel(0, new KeyEvent(0, 82));
                    }
                };
                this.mFloatingMenuFakeKeyUpRunnable = new Runnable() {
                    public void run() {
                        PhoneWindow.this.onKeyUpPanel(0, new KeyEvent(1, 82));
                    }
                };
                int bottomMargin = (int) (((double) (this.mContext.getResources().getDisplayMetrics().density * FLOATING_MENU_BOTTOM_MARGIN)) + 0.5d);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
                params.gravity = 85;
                params.bottomMargin = bottomMargin;
                addView(this.mFloatingMenuBtn, (ViewGroup.LayoutParams) params);
            } else if (this.mFloatingMenuBtn != null) {
                this.mFloatingMenuBtn.setVisibility(8);
            }
        }

        private boolean isFloatingMenuEnabled() {
            if (this.mFloatingMenuBtn == null || this.mFloatingMenuBtn.getVisibility() != 0) {
                return false;
            }
            return true;
        }

        private ActionMode createActionMode(int type, ActionMode.Callback2 callback, View originatingView) {
            switch (type) {
                case 1:
                    return createFloatingActionMode(originatingView, callback);
                case 99:
                    return createFloatingActionMode(originatingView, callback, true);
                default:
                    return createStandaloneActionMode(callback);
            }
        }

        private void setHandledActionMode(ActionMode mode) {
            if (mode.getType() == 0) {
                setHandledPrimaryActionMode(mode);
            } else if (mode.getType() == 1) {
                setHandledFloatingActionMode(mode, false);
            } else if (mode.getType() == 99) {
                setHandledFloatingActionMode(mode, true);
            }
        }

        private ActionMode createStandaloneActionMode(ActionMode.Callback callback) {
            ActionMode actionMode = null;
            endOnGoingFadeAnimation();
            cleanupPrimaryActionMode();
            if (this.mPrimaryActionModeView == null) {
                if (PhoneWindow.this.isFloating()) {
                    Context actionBarContext;
                    TypedValue outValue = new TypedValue();
                    Theme baseTheme = this.mContext.getTheme();
                    baseTheme.resolveAttribute(R.attr.actionBarTheme, outValue, true);
                    if (outValue.resourceId != 0) {
                        Theme actionBarTheme = this.mContext.getResources().newTheme();
                        actionBarTheme.setTo(baseTheme);
                        actionBarTheme.applyStyle(outValue.resourceId, true);
                        actionBarContext = new ContextThemeWrapper(this.mContext, 0);
                        actionBarContext.getTheme().setTo(actionBarTheme);
                    } else {
                        actionBarContext = this.mContext;
                    }
                    this.mPrimaryActionModeView = new ActionBarContextView(actionBarContext);
                    this.mPrimaryActionModePopup = new PopupWindow(actionBarContext, null, (int) R.attr.actionModePopupWindowStyle);
                    this.mPrimaryActionModePopup.setWindowLayoutType(2);
                    this.mPrimaryActionModePopup.setContentView(this.mPrimaryActionModeView);
                    this.mPrimaryActionModePopup.setWidth(-1);
                    actionBarContext.getTheme().resolveAttribute(R.attr.actionBarSize, outValue, true);
                    this.mPrimaryActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(outValue.data, actionBarContext.getResources().getDisplayMetrics()));
                    this.mPrimaryActionModePopup.setHeight(-2);
                    this.mShowPrimaryActionModePopup = new Runnable() {
                        public void run() {
                            DecorView.this.mPrimaryActionModePopup.showAtLocation(DecorView.this.mPrimaryActionModeView.getApplicationWindowToken(), 55, 0, 0);
                            DecorView.this.endOnGoingFadeAnimation();
                            DecorView.this.mFadeAnim = ObjectAnimator.ofFloat(DecorView.this.mPrimaryActionModeView, View.ALPHA, new float[]{0.0f, 1.0f});
                            DecorView.this.mFadeAnim.addListener(new AnimatorListener() {
                                public void onAnimationStart(Animator animation) {
                                    DecorView.this.mPrimaryActionModeView.setVisibility(0);
                                }

                                public void onAnimationEnd(Animator animation) {
                                    DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                                    DecorView.this.mFadeAnim = null;
                                }

                                public void onAnimationCancel(Animator animation) {
                                }

                                public void onAnimationRepeat(Animator animation) {
                                }
                            });
                            DecorView.this.mFadeAnim.start();
                        }
                    };
                } else {
                    ViewStub stub = (ViewStub) findViewById(R.id.action_mode_bar_stub);
                    if (stub != null) {
                        this.mPrimaryActionModeView = (ActionBarContextView) stub.inflate();
                    }
                }
            }
            if (this.mPrimaryActionModeView != null) {
                boolean z;
                this.mPrimaryActionModeView.killMode();
                Context context = this.mPrimaryActionModeView.getContext();
                ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
                if (this.mPrimaryActionModePopup == null) {
                    z = true;
                } else {
                    z = false;
                }
                actionMode = new StandaloneActionMode(context, actionBarContextView, callback, z);
            }
            return actionMode;
        }

        private void endOnGoingFadeAnimation() {
            if (this.mFadeAnim != null) {
                this.mFadeAnim.end();
            }
        }

        private void setHandledPrimaryActionMode(ActionMode mode) {
            endOnGoingFadeAnimation();
            this.mPrimaryActionMode = mode;
            this.mPrimaryActionMode.invalidate();
            this.mPrimaryActionModeView.initForMode(this.mPrimaryActionMode);
            if (this.mPrimaryActionModePopup != null) {
                post(this.mShowPrimaryActionModePopup);
            } else {
                this.mFadeAnim = ObjectAnimator.ofFloat(this.mPrimaryActionModeView, View.ALPHA, new float[]{0.0f, 1.0f});
                this.mFadeAnim.addListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                        DecorView.this.mPrimaryActionModeView.setVisibility(0);
                    }

                    public void onAnimationEnd(Animator animation) {
                        DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                        DecorView.this.mFadeAnim = null;
                    }

                    public void onAnimationCancel(Animator animation) {
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                this.mFadeAnim.start();
            }
            this.mPrimaryActionModeView.sendAccessibilityEvent(32);
        }

        private ActionMode createFloatingActionMode(View originatingView, ActionMode.Callback2 callback) {
            return createFloatingActionMode(originatingView, callback, false);
        }

        private ActionMode createFloatingActionMode(View originatingView, ActionMode.Callback2 callback, boolean useSamsungToolbar) {
            if (this.mFloatingActionMode != null) {
                this.mFloatingActionMode.finish();
            }
            cleanupFloatingActionModeViews();
            final FloatingActionMode mode = new FloatingActionMode(this.mContext, callback, originatingView, useSamsungToolbar);
            this.mFloatingActionModeOriginatingView = originatingView;
            this.mFloatingToolbarPreDrawListener = new OnPreDrawListener() {
                public boolean onPreDraw() {
                    mode.updateViewLocationInWindow();
                    return true;
                }
            };
            return mode;
        }

        private void setHandledFloatingActionMode(ActionMode mode) {
            setHandledFloatingActionMode(mode, false);
        }

        private void setHandledFloatingActionMode(ActionMode mode, boolean isSamsung) {
            this.mFloatingActionMode = mode;
            this.mFloatingToolbar = new FloatingToolbar(this.mContext, PhoneWindow.this, isSamsung);
            ((FloatingActionMode) this.mFloatingActionMode).setFloatingToolbar(this.mFloatingToolbar);
            this.mFloatingActionMode.invalidate();
            this.mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
        }
    }

    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        private ActionMenuPresenterCallback() {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (cb == null) {
                return false;
            }
            cb.onMenuOpened(8, subMenu);
            return true;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            PhoneWindow.this.checkCloseActionMenu(menu);
        }
    }

    protected static class ColorViewState {
        final int hideWindowFlag;
        final int horizontalGravity;
        final int id;
        boolean present = false;
        final int systemUiHideFlag;
        int targetVisibility = 4;
        final String transitionName;
        final int translucentFlag;
        final int verticalGravity;
        public View view = null;

        ColorViewState(int systemUiHideFlag, int translucentFlag, int verticalGravity, int horizontalGravity, String transitionName, int id, int hideWindowFlag) {
            this.id = id;
            this.systemUiHideFlag = systemUiHideFlag;
            this.translucentFlag = translucentFlag;
            this.verticalGravity = verticalGravity;
            this.horizontalGravity = horizontalGravity;
            this.transitionName = transitionName;
            this.hideWindowFlag = hideWindowFlag;
        }
    }

    private final class DialogMenuCallback implements Callback, MenuPresenter.Callback {
        private int mFeatureId;
        private MenuDialogHelper mSubMenuHelper;

        public DialogMenuCallback(int featureId) {
            this.mFeatureId = featureId;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu.getRootMenu() != menu) {
                onCloseSubMenu(menu);
            }
            if (allMenusAreClosing) {
                Window.Callback callback = PhoneWindow.this.getCallback();
                if (!(callback == null || PhoneWindow.this.isDestroyed())) {
                    callback.onPanelClosed(this.mFeatureId, menu);
                }
                if (menu == PhoneWindow.this.mContextMenu) {
                    PhoneWindow.this.dismissContextMenu();
                }
                if (this.mSubMenuHelper != null) {
                    this.mSubMenuHelper.dismiss();
                    this.mSubMenuHelper = null;
                }
            }
        }

        public void onCloseSubMenu(MenuBuilder menu) {
            Window.Callback callback = PhoneWindow.this.getCallback();
            if (callback != null && !PhoneWindow.this.isDestroyed()) {
                callback.onPanelClosed(this.mFeatureId, menu.getRootMenu());
            }
        }

        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            Window.Callback callback = PhoneWindow.this.getCallback();
            return (callback == null || PhoneWindow.this.isDestroyed() || !callback.onMenuItemSelected(this.mFeatureId, item)) ? false : true;
        }

        public void onMenuModeChange(MenuBuilder menu) {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null) {
                return false;
            }
            subMenu.setCallback(this);
            this.mSubMenuHelper = new MenuDialogHelper(subMenu);
            this.mSubMenuHelper.show(null);
            return true;
        }
    }

    private static final class DrawableFeatureState {
        int alpha = 255;
        Drawable child;
        Drawable cur;
        int curAlpha = 255;
        Drawable def;
        final int featureId;
        Drawable local;
        int resid;
        Uri uri;

        DrawableFeatureState(int _featureId) {
            this.featureId = _featureId;
        }
    }

    private static class FloatingMenuButton extends ImageButton {
        private boolean mIsViewInTransparentRegion = false;

        public FloatingMenuButton(Context context) {
            super(context);
        }

        public boolean gatherTransparentRegion(Region r) {
            this.mIsViewInTransparentRegion = true;
            return super.gatherTransparentRegion(r);
        }

        public boolean isViewInTransparentRegion() {
            return this.mIsViewInTransparentRegion;
        }
    }

    static final class PanelFeatureState {
        int background;
        View createdPanelView;
        DecorView decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int fullBackground;
        int gravity;
        IconMenuPresenter iconMenuPresenter;
        boolean isCompact;
        boolean isHandled;
        boolean isInExpandedMode;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        int listPresenterTheme;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastExpanded;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;

        private static class SavedState implements Parcelable {
            public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return SavedState.readFromParcel(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
            int featureId;
            boolean isInExpandedMode;
            boolean isOpen;
            Bundle menuState;

            private SavedState() {
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                int i = 1;
                dest.writeInt(this.featureId);
                dest.writeInt(this.isOpen ? 1 : 0);
                if (!this.isInExpandedMode) {
                    i = 0;
                }
                dest.writeInt(i);
                if (this.isOpen) {
                    dest.writeBundle(this.menuState);
                }
            }

            private static SavedState readFromParcel(Parcel source) {
                boolean z = true;
                SavedState savedState = new SavedState();
                savedState.featureId = source.readInt();
                savedState.isOpen = source.readInt() == 1;
                if (source.readInt() != 1) {
                    z = false;
                }
                savedState.isInExpandedMode = z;
                if (savedState.isOpen) {
                    savedState.menuState = source.readBundle();
                }
                return savedState;
            }
        }

        PanelFeatureState(int featureId) {
            this.featureId = featureId;
        }

        public boolean isInListMode() {
            return this.isInExpandedMode || this.isCompact;
        }

        public boolean hasPanelItems() {
            if (this.shownPanelView == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.isCompact || this.isInExpandedMode) {
                return this.listMenuPresenter.getAdapter().getCount() > 0;
            } else if (((ViewGroup) this.shownPanelView).getChildCount() <= 0) {
                return false;
            } else {
                return true;
            }
        }

        public void clearMenuPresenters() {
            if (this.menu != null) {
                this.menu.removeMenuPresenter(this.iconMenuPresenter);
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.iconMenuPresenter = null;
            this.listMenuPresenter = null;
        }

        void setStyle(Context context) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.Theme);
            this.background = a.getResourceId(46, 0);
            this.fullBackground = a.getResourceId(47, 0);
            this.windowAnimations = a.getResourceId(93, 0);
            this.isCompact = a.getBoolean(R.styleable.Theme_panelMenuIsCompact, false);
            this.listPresenterTheme = a.getResourceId(R.styleable.Theme_panelMenuListTheme, R.style.Theme_ExpandedMenu);
            a.recycle();
        }

        void setMenu(MenuBuilder menu) {
            if (menu != this.menu) {
                if (this.menu != null) {
                    this.menu.removeMenuPresenter(this.iconMenuPresenter);
                    this.menu.removeMenuPresenter(this.listMenuPresenter);
                }
                this.menu = menu;
                if (menu != null) {
                    if (this.iconMenuPresenter != null) {
                        menu.addMenuPresenter(this.iconMenuPresenter);
                    }
                    if (this.listMenuPresenter != null) {
                        menu.addMenuPresenter(this.listMenuPresenter);
                    }
                }
            }
        }

        MenuView getListMenuView(Context context, MenuPresenter.Callback cb) {
            if (this.menu == null) {
                return null;
            }
            if (!this.isCompact) {
                getIconMenuView(context, cb);
            }
            if (this.listMenuPresenter == null) {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
                if (outValue.data != 0) {
                    this.listMenuPresenter = new ListMenuPresenter((int) R.layout.tw_option_menu_item_layout, this.listPresenterTheme);
                } else {
                    this.listMenuPresenter = new ListMenuPresenter((int) R.layout.list_menu_item_layout, this.listPresenterTheme);
                }
                this.listMenuPresenter.setCallback(cb);
                this.listMenuPresenter.setId(R.id.list_menu_presenter);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            if (this.iconMenuPresenter != null) {
                this.listMenuPresenter.setItemIndexOffset(this.iconMenuPresenter.getNumActualItemsShown());
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        MenuView getIconMenuView(Context context, MenuPresenter.Callback cb) {
            if (this.menu == null) {
                return null;
            }
            if (this.iconMenuPresenter == null) {
                this.iconMenuPresenter = new IconMenuPresenter(context);
                this.iconMenuPresenter.setCallback(cb);
                this.iconMenuPresenter.setId(R.id.icon_menu_presenter);
                this.menu.addMenuPresenter(this.iconMenuPresenter);
            }
            return this.iconMenuPresenter.getMenuView(this.decorView);
        }

        Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            savedState.isInExpandedMode = this.isInExpandedMode;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        void onRestoreInstanceState(Parcelable state) {
            SavedState savedState = (SavedState) state;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.wasLastExpanded = savedState.isInExpandedMode;
            this.frozenMenuState = savedState.menuState;
            this.createdPanelView = null;
            this.shownPanelView = null;
            this.decorView = null;
        }

        void applyFrozenState() {
            if (this.menu != null && this.frozenMenuState != null) {
                this.menu.restorePresenterStates(this.frozenMenuState);
                this.frozenMenuState = null;
            }
        }
    }

    private class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        private PanelMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            MenuBuilder parentMenu = menu.getRootMenu();
            boolean isSubMenu = parentMenu != menu;
            PhoneWindow phoneWindow = PhoneWindow.this;
            if (isSubMenu) {
                menu = parentMenu;
            }
            PanelFeatureState panel = phoneWindow.findMenuPanel(menu);
            if (panel == null) {
                return;
            }
            if (isSubMenu) {
                PhoneWindow.this.callOnPanelClosed(panel.featureId, panel, parentMenu);
                PhoneWindow.this.closePanel(panel, true);
                return;
            }
            PhoneWindow.this.closePanel(panel, allMenusAreClosing);
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null && PhoneWindow.this.hasFeature(8)) {
                Window.Callback cb = PhoneWindow.this.getCallback();
                if (!(cb == null || PhoneWindow.this.isDestroyed())) {
                    cb.onMenuOpened(8, subMenu);
                }
            }
            return true;
        }
    }

    static class RotationWatcher extends Stub {
        private Handler mHandler;
        private boolean mIsWatching;
        private final Runnable mRotationChanged = new Runnable() {
            public void run() {
                RotationWatcher.this.dispatchRotationChanged();
            }
        };
        private final ArrayList<WeakReference<PhoneWindow>> mWindows = new ArrayList();

        RotationWatcher() {
        }

        public void onRotationChanged(int rotation) throws RemoteException {
            this.mHandler.post(this.mRotationChanged);
        }

        public void addWindow(PhoneWindow phoneWindow) {
            synchronized (this.mWindows) {
                if (!this.mIsWatching) {
                    try {
                        WindowManagerHolder.sWindowManager.watchRotation(this);
                        this.mHandler = new Handler();
                        this.mIsWatching = true;
                    } catch (RemoteException ex) {
                        Log.e(PhoneWindow.TAG, "Couldn't start watching for device rotation", ex);
                    }
                }
                this.mWindows.add(new WeakReference(phoneWindow));
            }
        }

        public void removeWindow(PhoneWindow phoneWindow) {
            synchronized (this.mWindows) {
                int i = 0;
                while (i < this.mWindows.size()) {
                    PhoneWindow win = (PhoneWindow) ((WeakReference) this.mWindows.get(i)).get();
                    if (win == null || win == phoneWindow) {
                        this.mWindows.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        }

        void dispatchRotationChanged() {
            synchronized (this.mWindows) {
                int i = 0;
                while (i < this.mWindows.size()) {
                    PhoneWindow win = (PhoneWindow) ((WeakReference) this.mWindows.get(i)).get();
                    if (win != null) {
                        win.onOptionsPanelRotationChanged();
                        i++;
                    } else {
                        this.mWindows.remove(i);
                    }
                }
            }
        }
    }

    static class WindowManagerHolder {
        static final IWindowManager sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));

        WindowManagerHolder() {
        }
    }

    public PhoneWindow(Context context) {
        super(context);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public final void setContainer(Window container) {
        super.setContainer(container);
    }

    public boolean requestFeature(int featureId) {
        if (this.mContentParent != null) {
            throw new AndroidRuntimeException("requestFeature() must be called before adding content");
        }
        int features = getFeatures();
        int newFeatures = features | (1 << featureId);
        if ((newFeatures & 128) != 0 && (newFeatures & -13506) != 0) {
            throw new AndroidRuntimeException("You cannot combine custom titles with other title features");
        } else if ((features & 2) != 0 && featureId == 8) {
            return false;
        } else {
            if ((features & 256) != 0 && featureId == 1) {
                removeFeature(8);
            }
            if ((features & 256) != 0 && featureId == 11) {
                throw new AndroidRuntimeException("You cannot combine swipe dismissal and the action bar.");
            } else if ((features & 2048) != 0 && featureId == 8) {
                throw new AndroidRuntimeException("You cannot combine swipe dismissal and the action bar.");
            } else if (featureId != 5 || !getContext().getPackageManager().hasSystemFeature("android.hardware.type.watch")) {
                return super.requestFeature(featureId);
            } else {
                throw new AndroidRuntimeException("You cannot use indeterminate progress on a watch.");
            }
        }
    }

    public void setUiOptions(int uiOptions) {
        this.mUiOptions = uiOptions;
    }

    public void setUiOptions(int uiOptions, int mask) {
        this.mUiOptions = (this.mUiOptions & (mask ^ -1)) | (uiOptions & mask);
    }

    public TransitionManager getTransitionManager() {
        return this.mTransitionManager;
    }

    public void setTransitionManager(TransitionManager tm) {
        this.mTransitionManager = tm;
    }

    public Scene getContentScene() {
        return this.mContentScene;
    }

    public void setContentView(int layoutResID) {
        if (this.mContentParent == null) {
            installDecor();
        } else if (!hasFeature(12)) {
            this.mContentParent.removeAllViews();
        }
        if (hasFeature(12)) {
            transitionTo(Scene.getSceneForLayout(this.mContentParent, layoutResID, getContext()));
        } else {
            this.mLayoutInflater.inflate(layoutResID, this.mContentParent);
        }
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (cb != null && !isDestroyed()) {
            cb.onContentChanged();
        }
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (this.mContentParent == null) {
            installDecor();
        } else if (!hasFeature(12)) {
            this.mContentParent.removeAllViews();
        }
        if (hasFeature(12)) {
            view.setLayoutParams(params);
            transitionTo(new Scene(this.mContentParent, view));
        } else {
            this.mContentParent.addView(view, params);
        }
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (cb != null && !isDestroyed()) {
            cb.onContentChanged();
        }
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (this.mContentParent == null) {
            installDecor();
        }
        if (hasFeature(12)) {
            Log.v(TAG, "addContentView does not support content transitions");
        }
        this.mContentParent.addView(view, params);
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (cb != null && !isDestroyed()) {
            cb.onContentChanged();
        }
    }

    private void transitionTo(Scene scene) {
        if (this.mContentScene == null) {
            scene.enter();
        } else {
            this.mTransitionManager.transitionTo(scene);
        }
        this.mContentScene = scene;
    }

    public View getCurrentFocus() {
        return this.mDecor != null ? this.mDecor.findFocus() : null;
    }

    public void takeSurface(Callback2 callback) {
        this.mTakeSurfaceCallback = callback;
    }

    public void takeInputQueue(InputQueue.Callback callback) {
        this.mTakeInputQueueCallback = callback;
    }

    public boolean isFloating() {
        return this.mIsFloating;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mLayoutInflater;
    }

    public void setTitle(CharSequence title) {
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
        } else if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setWindowTitle(title);
        }
        this.mTitle = title;
    }

    @Deprecated
    public void setTitleColor(int textColor) {
        if (this.mTitleView != null) {
            this.mTitleView.setTextColor(textColor);
        }
        this.mTitleColor = textColor;
    }

    public final boolean preparePanel(PanelFeatureState st, KeyEvent event) {
        if (isDestroyed()) {
            return false;
        }
        if (st.isPrepared) {
            return true;
        }
        boolean isActionBarMenu;
        if (!(this.mPreparedPanel == null || this.mPreparedPanel == st)) {
            closePanel(this.mPreparedPanel, false);
        }
        Window.Callback cb = getCallback();
        if (cb != null) {
            st.createdPanelView = cb.onCreatePanelView(st.featureId);
        }
        if (st.featureId == 0 || st.featureId == 8) {
            isActionBarMenu = true;
        } else {
            isActionBarMenu = false;
        }
        if (isActionBarMenu && this.mDecorContentParent != null) {
            this.mDecorContentParent.setMenuPrepared();
        }
        if (st.createdPanelView == null) {
            if (st.menu == null || st.refreshMenuContent) {
                if (st.menu == null && (!initializePanelMenu(st) || st.menu == null)) {
                    return false;
                }
                if (isActionBarMenu && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
                    }
                    this.mDecorContentParent.setMenu(st.menu, this.mActionMenuPresenterCallback);
                }
                st.menu.stopDispatchingItemsChanged();
                if (cb == null || !cb.onCreatePanelMenu(st.featureId, st.menu)) {
                    st.setMenu(null);
                    if (!isActionBarMenu || this.mDecorContentParent == null) {
                        return false;
                    }
                    this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                    return false;
                }
                st.refreshMenuContent = false;
            }
            st.menu.stopDispatchingItemsChanged();
            if (st.frozenActionViewState != null) {
                st.menu.restoreActionViewStates(st.frozenActionViewState);
                st.frozenActionViewState = null;
            }
            if (cb.onPreparePanel(st.featureId, st.createdPanelView, st.menu)) {
                boolean z;
                if (KeyCharacterMap.load(event != null ? event.getDeviceId() : -1).getKeyboardType() != 1) {
                    z = true;
                } else {
                    z = false;
                }
                st.qwertyMode = z;
                st.menu.setQwertyMode(st.qwertyMode);
                st.menu.startDispatchingItemsChanged();
            } else {
                if (isActionBarMenu && this.mDecorContentParent != null) {
                    this.mDecorContentParent.setMenu(null, this.mActionMenuPresenterCallback);
                }
                st.menu.startDispatchingItemsChanged();
                return false;
            }
        }
        st.isPrepared = true;
        st.isHandled = false;
        this.mPreparedPanel = st;
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mDecorContentParent == null) {
            PanelFeatureState st = getPanelState(0, false);
            if (st != null && st.menu != null) {
                if (st.isOpen) {
                    Bundle state = new Bundle();
                    if (st.iconMenuPresenter != null) {
                        st.iconMenuPresenter.saveHierarchyState(state);
                    }
                    if (st.listMenuPresenter != null) {
                        st.listMenuPresenter.saveHierarchyState(state);
                    }
                    clearMenuViews(st);
                    reopenMenu(false);
                    if (st.iconMenuPresenter != null) {
                        st.iconMenuPresenter.restoreHierarchyState(state);
                    }
                    if (st.listMenuPresenter != null) {
                        st.listMenuPresenter.restoreHierarchyState(state);
                        return;
                    }
                    return;
                }
                clearMenuViews(st);
            }
        }
    }

    private static void clearMenuViews(PanelFeatureState st) {
        st.createdPanelView = null;
        st.refreshDecorView = true;
        st.clearMenuPresenters();
    }

    public final void openPanel(int featureId, KeyEvent event) {
        if (featureId != 0 || this.mDecorContentParent == null || !this.mDecorContentParent.canShowOverflowMenu() || ViewConfiguration.get(getContext()).hasPermanentMenuKey()) {
            openPanel(getPanelState(featureId, true), event);
        } else {
            this.mDecorContentParent.showOverflowMenu();
        }
    }

    private void openPanel(PanelFeatureState st, KeyEvent event) {
        if (!st.isOpen && !isDestroyed()) {
            if (st.featureId == 0) {
                Context context = getContext();
                boolean isXLarge = (context.getResources().getConfiguration().screenLayout & 15) == 4;
                boolean isHoneycombApp = context.getApplicationInfo().targetSdkVersion >= 11;
                if (isXLarge && isHoneycombApp && !ViewConfiguration.get(context).hasPermanentMenuKey()) {
                    return;
                }
            }
            Window.Callback cb = getCallback();
            if (cb == null || cb.onMenuOpened(st.featureId, st.menu)) {
                WindowManager wm = getWindowManager();
                if (wm != null && preparePanel(st, event)) {
                    LayoutParams lp;
                    int width = -2;
                    if (st.decorView == null || st.refreshDecorView) {
                        if (st.decorView == null) {
                            if (!initializePanelDecor(st) || st.decorView == null) {
                                return;
                            }
                        } else if (st.refreshDecorView && st.decorView.getChildCount() > 0) {
                            st.decorView.removeAllViews();
                        }
                        if (initializePanelContent(st) && st.hasPanelItems()) {
                            int backgroundResId;
                            ViewGroup.LayoutParams lp2 = st.shownPanelView.getLayoutParams();
                            if (lp2 == null) {
                                lp2 = new ViewGroup.LayoutParams(-2, -2);
                            }
                            if (lp2.width == -1) {
                                backgroundResId = st.fullBackground;
                                width = -1;
                            } else {
                                backgroundResId = st.background;
                            }
                            st.decorView.setWindowBackground(getContext().getDrawable(backgroundResId));
                            ViewParent shownPanelParent = st.shownPanelView.getParent();
                            if (shownPanelParent != null && (shownPanelParent instanceof ViewGroup)) {
                                ((ViewGroup) shownPanelParent).removeView(st.shownPanelView);
                            }
                            if (getContext().getApplicationInfo().hasRtlSupport() && st.decorView.getLayoutDirection() == 1) {
                                st.shownPanelView.setLayoutDirection(1);
                            }
                            st.decorView.addView(st.shownPanelView, lp2);
                            if (!st.shownPanelView.hasFocus()) {
                                st.shownPanelView.requestFocus();
                            }
                        } else {
                            return;
                        }
                    } else if (!initializePanelContent(st) || !st.hasPanelItems()) {
                        Log.v(TAG, "openPanel() : menu is empty");
                        return;
                    } else if (!st.isInListMode()) {
                        width = -1;
                    } else if (st.createdPanelView != null) {
                        lp = st.createdPanelView.getLayoutParams();
                        if (lp != null && lp.width == -1) {
                            width = -1;
                        }
                    }
                    st.isHandled = false;
                    lp = new LayoutParams(width, -2, st.x, st.y, 1003, 8519680, st.decorView.mDefaultOpacity);
                    if (st.isCompact) {
                        lp.gravity = getOptionsPanelGravity();
                        sRotationWatcher.addWindow(this);
                    } else {
                        lp.gravity = st.gravity;
                    }
                    lp.windowAnimations = st.windowAnimations;
                    wm.addView(st.decorView, lp);
                    st.isOpen = true;
                    return;
                }
                return;
            }
            closePanel(st, true);
        }
    }

    public final void closePanel(int featureId) {
        if (featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfiguration.get(getContext()).hasPermanentMenuKey()) {
            this.mDecorContentParent.hideOverflowMenu();
        } else if (featureId == 6) {
            closeContextMenu();
        } else {
            closePanel(getPanelState(featureId, true), true);
        }
    }

    public final void closePanel(PanelFeatureState st, boolean doCallback) {
        if (doCallback && st.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
            checkCloseActionMenu(st.menu);
            return;
        }
        ViewManager wm = getWindowManager();
        if (wm != null && st.isOpen) {
            if (st.decorView != null) {
                wm.removeView(st.decorView);
                if (st.isCompact) {
                    sRotationWatcher.removeWindow(this);
                }
            }
            if (doCallback) {
                callOnPanelClosed(st.featureId, st, null);
            }
        }
        st.isPrepared = false;
        st.isHandled = false;
        st.isOpen = false;
        st.shownPanelView = null;
        if (st.isInExpandedMode) {
            st.refreshDecorView = true;
            st.isInExpandedMode = false;
        }
        if (this.mPreparedPanel == st) {
            this.mPreparedPanel = null;
            this.mPanelChordingKey = 0;
        }
    }

    void checkCloseActionMenu(Menu menu) {
        if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            this.mDecorContentParent.dismissPopups();
            Window.Callback cb = getCallback();
            if (!(cb == null || isDestroyed())) {
                cb.onPanelClosed(8, menu);
            }
            this.mClosingActionMenu = false;
        }
    }

    public final void togglePanel(int featureId, KeyEvent event) {
        PanelFeatureState st = getPanelState(featureId, true);
        if (st.isOpen) {
            closePanel(st, true);
        } else {
            openPanel(st, event);
        }
    }

    public void invalidatePanelMenu(int featureId) {
        this.mInvalidatePanelMenuFeatures |= 1 << featureId;
        if (!this.mInvalidatePanelMenuPosted && this.mDecor != null) {
            this.mDecor.postOnAnimation(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuPosted = true;
        }
    }

    void doPendingInvalidatePanelMenu() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mDecor.removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
        }
    }

    void doInvalidatePanelMenu(int featureId) {
        PanelFeatureState st = getPanelState(featureId, false);
        if (st != null) {
            if (st.menu != null) {
                Bundle savedActionViewStates = new Bundle();
                st.menu.saveActionViewStates(savedActionViewStates);
                if (savedActionViewStates.size() > 0) {
                    st.frozenActionViewState = savedActionViewStates;
                }
                st.menu.stopDispatchingItemsChanged();
                st.menu.clear();
            }
            st.refreshMenuContent = true;
            st.refreshDecorView = true;
            if ((featureId == 8 || featureId == 0) && this.mDecorContentParent != null) {
                st = getPanelState(0, false);
                if (st != null) {
                    st.isPrepared = false;
                    preparePanel(st, null);
                }
            }
        }
    }

    public final boolean onKeyDownPanel(int featureId, KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() != 0) {
            return false;
        }
        this.mPanelChordingKey = keyCode;
        PanelFeatureState st = getPanelState(featureId, false);
        if (st == null || st.isOpen) {
            return false;
        }
        return preparePanel(st, event);
    }

    public final void onKeyUpPanel(int featureId, KeyEvent event) {
        if (this.mPanelChordingKey != 0) {
            this.mPanelChordingKey = 0;
            PanelFeatureState st = getPanelState(featureId, false);
            if (!event.isCanceled()) {
                if ((this.mDecor != null && this.mDecor.mPrimaryActionMode != null) || st == null) {
                    return;
                }
                boolean playSoundEffect;
                if (featureId != 0 || this.mDecorContentParent == null || !this.mDecorContentParent.canShowOverflowMenu() || ViewConfiguration.get(getContext()).hasPermanentMenuKey()) {
                    if (st.isOpen || st.isHandled) {
                        playSoundEffect = st.isOpen;
                        closePanel(st, true);
                    } else if (st.isPrepared) {
                        boolean show = true;
                        if (st.refreshMenuContent) {
                            st.isPrepared = false;
                            show = preparePanel(st, event);
                        }
                        if (show) {
                            EventLog.writeEvent(50001, 0);
                            openPanel(st, event);
                        }
                    }
                } else if (this.mDecorContentParent.isOverflowMenuShowing()) {
                    playSoundEffect = this.mDecorContentParent.hideOverflowMenu();
                } else if (!isDestroyed() && preparePanel(st, event)) {
                    playSoundEffect = this.mDecorContentParent.showOverflowMenu();
                }
            }
        }
    }

    public final void closeAllPanels() {
        if (getWindowManager() != null) {
            PanelFeatureState[] panels = this.mPanels;
            int N = panels != null ? panels.length : 0;
            for (int i = 0; i < N; i++) {
                PanelFeatureState panel = panels[i];
                if (panel != null) {
                    closePanel(panel, true);
                }
            }
            closeContextMenu();
        }
    }

    private synchronized void closeContextMenu() {
        if (this.mContextMenu != null) {
            this.mContextMenu.close();
            dismissContextMenu();
        }
    }

    private synchronized void dismissContextMenu() {
        this.mContextMenu = null;
        if (this.mContextMenuHelper != null) {
            this.mContextMenuHelper.dismiss();
            this.mContextMenuHelper = null;
        }
    }

    public boolean performPanelShortcut(int featureId, int keyCode, KeyEvent event, int flags) {
        return performPanelShortcut(getPanelState(featureId, false), keyCode, event, flags);
    }

    private boolean performPanelShortcut(PanelFeatureState st, int keyCode, KeyEvent event, int flags) {
        if (event.isSystem() || st == null) {
            return false;
        }
        boolean handled = false;
        if ((st.isPrepared || preparePanel(st, event)) && st.menu != null) {
            handled = st.menu.performShortcut(keyCode, event, flags);
        }
        if (!handled) {
            return handled;
        }
        st.isHandled = true;
        if ((flags & 1) != 0 || this.mDecorContentParent != null) {
            return handled;
        }
        closePanel(st, true);
        return handled;
    }

    public boolean performPanelIdentifierAction(int featureId, int id, int flags) {
        boolean z = false;
        PanelFeatureState st = getPanelState(featureId, true);
        if (preparePanel(st, new KeyEvent(0, 82)) && st.menu != null) {
            z = st.menu.performIdentifierAction(id, flags);
            if (this.mDecorContentParent == null) {
                closePanel(st, true);
            }
        }
        return z;
    }

    public PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panels = this.mPanels;
        int N = panels != null ? panels.length : 0;
        for (int i = 0; i < N; i++) {
            PanelFeatureState panel = panels[i];
            if (panel != null && panel.menu == menu) {
                return panel;
            }
        }
        return null;
    }

    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        Window.Callback cb = getCallback();
        if (!(cb == null || isDestroyed())) {
            PanelFeatureState panel = findMenuPanel(menu.getRootMenu());
            if (panel != null) {
                return cb.onMenuItemSelected(panel.featureId, item);
            }
        }
        return false;
    }

    public void onMenuModeChange(MenuBuilder menu) {
        reopenMenu(true);
    }

    private void reopenMenu(boolean toggleMenuMode) {
        PanelFeatureState st;
        if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(getContext()).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
            Window.Callback cb = getCallback();
            if (this.mDecorContentParent.isOverflowMenuShowing() && toggleMenuMode) {
                this.mDecorContentParent.hideOverflowMenu();
                st = getPanelState(0, false);
                if (st != null && cb != null && !isDestroyed()) {
                    cb.onPanelClosed(8, st.menu);
                }
            } else if (cb != null && !isDestroyed()) {
                if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
                    this.mDecor.removeCallbacks(this.mInvalidatePanelMenuRunnable);
                    this.mInvalidatePanelMenuRunnable.run();
                }
                st = getPanelState(0, false);
                if (st != null && st.menu != null && !st.refreshMenuContent && cb.onPreparePanel(0, st.createdPanelView, st.menu)) {
                    cb.onMenuOpened(8, st.menu);
                    this.mDecorContentParent.showOverflowMenu();
                }
            }
        } else if (!(getCallback() instanceof Activity) || ((Activity) getCallback()).getActionBar() == null) {
            st = getPanelState(0, false);
            if (st != null) {
                boolean newExpandedMode = toggleMenuMode ? !st.isInExpandedMode : st.isInExpandedMode;
                st.refreshDecorView = true;
                closePanel(st, false);
                st.isInExpandedMode = newExpandedMode;
                openPanel(st, null);
            }
        }
    }

    protected boolean initializePanelMenu(PanelFeatureState st) {
        Context context = getContext();
        if ((st.featureId == 0 || st.featureId == 8) && this.mDecorContentParent != null) {
            TypedValue outValue = new TypedValue();
            Theme baseTheme = context.getTheme();
            baseTheme.resolveAttribute(R.attr.actionBarTheme, outValue, true);
            Theme widgetTheme = null;
            if (outValue.resourceId != 0) {
                widgetTheme = context.getResources().newTheme();
                widgetTheme.setTo(baseTheme);
                widgetTheme.applyStyle(outValue.resourceId, true);
                widgetTheme.resolveAttribute(R.attr.actionBarWidgetTheme, outValue, true);
            } else {
                baseTheme.resolveAttribute(R.attr.actionBarWidgetTheme, outValue, true);
            }
            if (outValue.resourceId != 0) {
                if (widgetTheme == null) {
                    widgetTheme = context.getResources().newTheme();
                    widgetTheme.setTo(baseTheme);
                }
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            if (widgetTheme != null) {
                Context context2 = new ContextThemeWrapper(context, 0);
                context2.getTheme().setTo(widgetTheme);
                context = context2;
            }
        }
        MenuBuilder menu = new MenuBuilder(context);
        menu.setCallback(this);
        st.setMenu(menu);
        return true;
    }

    protected boolean initializePanelDecor(PanelFeatureState st) {
        st.decorView = new DecorView(getContext(), st.featureId);
        st.gravity = 81;
        st.setStyle(getContext());
        TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.Window, 0, st.listPresenterTheme);
        float elevation = a.getDimension(38, 0.0f);
        if (elevation != 0.0f) {
            st.decorView.setElevation(elevation);
        }
        a.recycle();
        return true;
    }

    protected int getOptionsPanelGravity() {
        try {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
            int smallestScreenWidthDp = getContext().getResources().getConfiguration().smallestScreenWidthDp;
            if (outValue.data == 0 || smallestScreenWidthDp < 600) {
                return WindowManagerHolder.sWindowManager.getPreferredOptionsPanelGravity();
            }
            return WindowManagerHolder.sWindowManager.getPreferredOptionsPanelGravityTablet();
        } catch (RemoteException ex) {
            Log.e(TAG, "Couldn't getOptionsPanelGravity; using default", ex);
            return 81;
        }
    }

    void onOptionsPanelRotationChanged() {
        PanelFeatureState st = getPanelState(0, false);
        if (st != null) {
            LayoutParams lp = st.decorView != null ? (LayoutParams) st.decorView.getLayoutParams() : null;
            if (lp != null) {
                lp.gravity = getOptionsPanelGravity();
                ViewManager wm = getWindowManager();
                if (wm != null) {
                    wm.updateViewLayout(st.decorView, lp);
                }
            }
        }
    }

    protected boolean initializePanelContent(PanelFeatureState st) {
        if (st.createdPanelView != null) {
            st.shownPanelView = st.createdPanelView;
            return true;
        } else if (st.menu == null) {
            return false;
        } else {
            if (this.mPanelMenuPresenterCallback == null) {
                this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback();
            }
            MenuView menuView = st.isInListMode() ? st.getListMenuView(getContext(), this.mPanelMenuPresenterCallback) : st.getIconMenuView(getContext(), this.mPanelMenuPresenterCallback);
            st.shownPanelView = (View) menuView;
            if (st.shownPanelView == null) {
                return false;
            }
            int defaultAnimations = menuView.getWindowAnimations();
            if (defaultAnimations != 0) {
                st.windowAnimations = defaultAnimations;
            }
            return true;
        }
    }

    public boolean performContextMenuIdentifierAction(int id, int flags) {
        return this.mContextMenu != null ? this.mContextMenu.performIdentifierAction(id, flags) : false;
    }

    public final void setElevation(float elevation) {
        this.mElevation = elevation;
        if (this.mDecor != null) {
            this.mDecor.setElevation(elevation);
        }
        dispatchWindowAttributesChanged(getAttributes());
    }

    public final void setClipToOutline(boolean clipToOutline) {
        this.mClipToOutline = clipToOutline;
        if (this.mDecor != null) {
            this.mDecor.setClipToOutline(clipToOutline);
        }
    }

    public final void setBackgroundDrawable(Drawable drawable) {
        int i = 0;
        if (drawable != this.mBackgroundDrawable || this.mBackgroundResource != 0) {
            this.mBackgroundResource = 0;
            this.mBackgroundDrawable = drawable;
            if (this.mDecor != null) {
                this.mDecor.setWindowBackground(drawable);
            }
            if (this.mBackgroundFallbackResource != 0) {
                DecorView decorView = this.mDecor;
                if (drawable == null) {
                    i = this.mBackgroundFallbackResource;
                }
                decorView.setBackgroundFallback(i);
            }
        }
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        if (resId != 0) {
            DrawableFeatureState st = getDrawableState(featureId, true);
            if (st.resid != resId) {
                st.resid = resId;
                st.uri = null;
                st.local = getContext().getDrawable(resId);
                updateDrawable(featureId, st, false);
                return;
            }
            return;
        }
        setFeatureDrawable(featureId, null);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        if (uri != null) {
            DrawableFeatureState st = getDrawableState(featureId, true);
            if (st.uri == null || !st.uri.equals(uri)) {
                st.resid = 0;
                st.uri = uri;
                st.local = loadImageURI(uri);
                updateDrawable(featureId, st, false);
                return;
            }
            return;
        }
        setFeatureDrawable(featureId, null);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        st.resid = 0;
        st.uri = null;
        if (st.local != drawable) {
            st.local = drawable;
            updateDrawable(featureId, st, false);
        }
    }

    public void setFeatureDrawableAlpha(int featureId, int alpha) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        if (st.alpha != alpha) {
            st.alpha = alpha;
            updateDrawable(featureId, st, false);
        }
    }

    protected final void setFeatureDefaultDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        if (st.def != drawable) {
            st.def = drawable;
            updateDrawable(featureId, st, false);
        }
    }

    public final void setFeatureInt(int featureId, int value) {
        updateInt(featureId, value, false);
    }

    protected final void updateDrawable(int featureId, boolean fromActive) {
        DrawableFeatureState st = getDrawableState(featureId, false);
        if (st != null) {
            updateDrawable(featureId, st, fromActive);
        }
    }

    protected void onDrawableChanged(int featureId, Drawable drawable, int alpha) {
        ImageView view;
        if (featureId == 3) {
            view = getLeftIconView();
        } else if (featureId == 4) {
            view = getRightIconView();
        } else {
            return;
        }
        if (drawable != null) {
            drawable.setAlpha(alpha);
            view.setImageDrawable(drawable);
            view.setVisibility(0);
            return;
        }
        view.setVisibility(8);
    }

    protected void onIntChanged(int featureId, int value) {
        if (featureId == 2 || featureId == 5) {
            updateProgressBars(value);
        } else if (featureId == 7) {
            ViewGroup titleContainer = (FrameLayout) findViewById(R.id.title_container);
            if (titleContainer != null) {
                this.mLayoutInflater.inflate(value, titleContainer);
            }
        }
    }

    private void updateProgressBars(int value) {
        ProgressBar circularProgressBar = getCircularProgressBar(true);
        ProgressBar horizontalProgressBar = getHorizontalProgressBar(true);
        int features = getLocalFeatures();
        if (value == -1) {
            if ((features & 4) != 0) {
                if (horizontalProgressBar != null) {
                    int visibility = (horizontalProgressBar.isIndeterminate() || horizontalProgressBar.getProgress() < 10000) ? 0 : 4;
                    horizontalProgressBar.setVisibility(visibility);
                } else {
                    Log.e(TAG, "Horizontal progress bar not located in current window decor");
                }
            }
            if ((features & 32) == 0) {
                return;
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(0);
            } else {
                Log.e(TAG, "Circular progress bar not located in current window decor");
            }
        } else if (value == -2) {
            if ((features & 4) != 0) {
                if (horizontalProgressBar != null) {
                    horizontalProgressBar.setVisibility(8);
                } else {
                    Log.e(TAG, "Horizontal progress bar not located in current window decor");
                }
            }
            if ((features & 32) == 0) {
                return;
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(8);
            } else {
                Log.e(TAG, "Circular progress bar not located in current window decor");
            }
        } else if (value == -3) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(true);
            } else {
                Log.e(TAG, "Horizontal progress bar not located in current window decor");
            }
        } else if (value == -4) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(false);
            } else {
                Log.e(TAG, "Horizontal progress bar not located in current window decor");
            }
        } else if (value >= 0 && value <= 10000) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setProgress(value + 0);
            } else {
                Log.e(TAG, "Horizontal progress bar not located in current window decor");
            }
            if (value < 10000) {
                showProgressBars(horizontalProgressBar, circularProgressBar);
            } else {
                hideProgressBars(horizontalProgressBar, circularProgressBar);
            }
        } else if (Window.PROGRESS_SECONDARY_START <= value && value <= Window.PROGRESS_SECONDARY_END) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setSecondaryProgress(value - 20000);
            } else {
                Log.e(TAG, "Horizontal progress bar not located in current window decor");
            }
            showProgressBars(horizontalProgressBar, circularProgressBar);
        }
    }

    private void showProgressBars(ProgressBar horizontalProgressBar, ProgressBar spinnyProgressBar) {
        int features = getLocalFeatures();
        if (!((features & 32) == 0 || spinnyProgressBar == null || spinnyProgressBar.getVisibility() != 4)) {
            spinnyProgressBar.setVisibility(0);
        }
        if ((features & 4) != 0 && horizontalProgressBar != null && horizontalProgressBar.getProgress() < 10000) {
            horizontalProgressBar.setVisibility(0);
        }
    }

    private void hideProgressBars(ProgressBar horizontalProgressBar, ProgressBar spinnyProgressBar) {
        int features = getLocalFeatures();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        anim.setDuration(1000);
        if (!((features & 32) == 0 || spinnyProgressBar == null || spinnyProgressBar.getVisibility() != 0)) {
            spinnyProgressBar.startAnimation(anim);
            spinnyProgressBar.setVisibility(4);
        }
        if ((features & 4) != 0 && horizontalProgressBar != null && horizontalProgressBar.getVisibility() == 0) {
            horizontalProgressBar.startAnimation(anim);
            horizontalProgressBar.setVisibility(4);
        }
    }

    public void setIcon(int resId) {
        this.mIconRes = resId;
        this.mResourcesSetFlags |= 1;
        this.mResourcesSetFlags &= -5;
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setIcon(resId);
        }
    }

    public void setDefaultIcon(int resId) {
        if ((this.mResourcesSetFlags & 1) == 0) {
            this.mIconRes = resId;
            if (this.mDecorContentParent == null) {
                return;
            }
            if (!this.mDecorContentParent.hasIcon() || (this.mResourcesSetFlags & 4) != 0) {
                if (resId != 0) {
                    this.mDecorContentParent.setIcon(resId);
                    this.mResourcesSetFlags &= -5;
                    return;
                }
                this.mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
                this.mResourcesSetFlags |= 4;
            }
        }
    }

    public void setLogo(int resId) {
        this.mLogoRes = resId;
        this.mResourcesSetFlags |= 2;
        if (this.mDecorContentParent != null) {
            this.mDecorContentParent.setLogo(resId);
        }
    }

    public void setDefaultLogo(int resId) {
        if ((this.mResourcesSetFlags & 2) == 0) {
            this.mLogoRes = resId;
            if (this.mDecorContentParent != null && !this.mDecorContentParent.hasLogo()) {
                this.mDecorContentParent.setLogo(resId);
            }
        }
    }

    public void setLocalFocus(boolean hasFocus, boolean inTouchMode) {
        getViewRootImpl().windowFocusChanged(hasFocus, inTouchMode);
    }

    public void injectInputEvent(InputEvent event) {
        getViewRootImpl().dispatchInputEvent(event);
    }

    private ViewRootImpl getViewRootImpl() {
        if (this.mDecor != null) {
            ViewRootImpl viewRootImpl = this.mDecor.getViewRootImpl();
            if (viewRootImpl != null) {
                return viewRootImpl;
            }
        }
        throw new IllegalStateException("view not added");
    }

    public void takeKeyEvents(boolean get) {
        this.mDecor.setFocusable(get);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        return this.mDecor.superDispatchKeyEvent(event);
    }

    public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
        return this.mDecor.superDispatchKeyShortcutEvent(event);
    }

    public boolean superDispatchTouchEvent(MotionEvent event) {
        return this.mDecor.superDispatchTouchEvent(event);
    }

    public boolean superDispatchTrackballEvent(MotionEvent event) {
        return this.mDecor.superDispatchTrackballEvent(event);
    }

    public boolean superDispatchGenericMotionEvent(MotionEvent event) {
        return this.mDecor.superDispatchGenericMotionEvent(event);
    }

    private int adjustVolumeForRotation(int val) {
        int ret = val;
        KnoxCustomManager knoxCustomManager = EnterpriseDeviceManager.getInstance().getKnoxCustomManager();
        if (knoxCustomManager == null || !knoxCustomManager.getVolumeButtonRotationState()) {
            return ret;
        }
        WindowManager windowService = (WindowManager) getContext().getSystemService("window");
        if (windowService == null) {
            return ret;
        }
        int rot = windowService.getDefaultDisplay().getRotation();
        if (getContext().getResources().getConfiguration().orientation == 2) {
            if (rot == 1 || rot == 2) {
                return ret * -1;
            }
            return ret;
        } else if (rot == 2 || rot == 3) {
            return ret * -1;
        } else {
            return ret;
        }
    }

    private int adjustVolumeControlStreamTypeForKnox(int val) {
        int ret = val;
        KnoxCustomManager knoxCustomManager = EnterpriseDeviceManager.getInstance().getKnoxCustomManager();
        if (knoxCustomManager == null) {
            return ret;
        }
        switch (knoxCustomManager.getVolumeControlStream()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            default:
                return ret;
        }
    }

    protected boolean onKeyDown(int featureId, int keyCode, KeyEvent event) {
        DispatcherState dispatcher = this.mDecor != null ? this.mDecor.getKeyDispatcherState() : null;
        Intent intent;
        switch (keyCode) {
            case 4:
                if (event.getRepeatCount() > 0 || featureId < 0) {
                    return false;
                }
                if (dispatcher != null) {
                    dispatcher.startTracking(event, this);
                }
                return true;
            case 24:
            case 25:
            case 164:
            case 1019:
            case 1020:
                int direction = 0;
                switch (keyCode) {
                    case 24:
                        direction = 1;
                        break;
                    case 25:
                        direction = -1;
                        break;
                    case 164:
                        direction = 101;
                        break;
                }
                int volumeControlStreamType = this.mVolumeControlStreamType;
                direction = adjustVolumeForRotation(direction);
                volumeControlStreamType = adjustVolumeControlStreamTypeForKnox(this.mVolumeControlStreamType);
                if (this.mMediaController != null) {
                    this.mMediaController.adjustVolume(direction, 1);
                } else {
                    MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(this.mVolumeControlStreamType, direction, 4113);
                }
                return true;
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 130:
                if (this.mMediaController == null || !this.mMediaController.dispatchMediaButtonEvent(event)) {
                    return false;
                }
                return true;
            case 82:
                if (featureId < 0) {
                    featureId = 0;
                }
                onKeyDownPanel(featureId, event);
                return true;
            case 1002:
                if (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null || event.getRepeatCount() > 0) {
                    return false;
                }
                intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.setFlags(268435456);
                intent.putExtra(EXTRA_LAUNCHER_ACTION, LAUNCHER_ACTION_ALL_APPS);
                try {
                    getContext().startActivityForKey(intent);
                } catch (ActivityNotFoundException e) {
                }
                return true;
            case 1006:
                if (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null || event.getRepeatCount() > 0) {
                    return false;
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
                if (imm != null) {
                    imm.toggleSoftInput(0, 0);
                }
                return true;
            case 1007:
                if (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null || event.getRepeatCount() > 0) {
                    return false;
                }
                intent = new Intent();
                intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.VoiceSearchActivity");
                intent.addFlags(268435456);
                intent.addFlags(8388608);
                if (getActivityCount(intent) > 0) {
                    getContext().startActivityForKey(intent);
                }
                return true;
            case 1008:
                if (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null || event.getRepeatCount() > 0) {
                    return false;
                }
                intent = Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_EMAIL");
                intent.addFlags(268435456);
                intent.addFlags(8388608);
                try {
                    getContext().startActivityForKey(intent);
                } catch (ActivityNotFoundException e2) {
                    Log.w(TAG, "No activity to launch email", e2);
                }
                return true;
            case 1009:
                return true;
            case 1010:
                return true;
            case 1013:
                if (getKeyguardManager().inKeyguardRestrictedInputMode() || dispatcher == null || event.getRepeatCount() > 0) {
                    return false;
                }
                intent = new Intent();
                intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationComposer");
                intent.addFlags(268435456);
                intent.addFlags(8388608);
                if (getActivityCount(intent) > 0) {
                    try {
                        getContext().startActivityForKey(intent);
                    } catch (ActivityNotFoundException e22) {
                        Log.w(TAG, "No activity to launch mms ConversationComposer.", e22);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    private int getActivityCount(Intent intent) {
        return getContext().getPackageManager().queryIntentActivities(intent, 0).size();
    }

    private KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) getContext().getSystemService("keyguard");
        }
        return this.mKeyguardManager;
    }

    AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) getContext().getSystemService("audio");
        }
        return this.mAudioManager;
    }

    protected boolean onKeyUp(int featureId, int keyCode, KeyEvent event) {
        DispatcherState dispatcher = this.mDecor != null ? this.mDecor.getKeyDispatcherState() : null;
        if (dispatcher != null) {
            dispatcher.handleUpEvent(event);
        }
        switch (keyCode) {
            case 4:
                if (featureId < 0 || !event.isTracking() || event.isCanceled()) {
                    return false;
                }
                if (featureId == 0) {
                    PanelFeatureState st = getPanelState(featureId, false);
                    if (st != null && st.isInExpandedMode) {
                        reopenMenu(true);
                        return true;
                    }
                }
                closePanel(featureId);
                return true;
            case 24:
            case 25:
            case 1019:
            case 1020:
                if (this.mMediaController != null) {
                    this.mMediaController.adjustVolume(0, 4116);
                } else {
                    MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(this.mVolumeControlStreamType, 0, 4116);
                }
                return true;
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 126:
            case 127:
            case 130:
                if (this.mMediaController == null || !this.mMediaController.dispatchMediaButtonEvent(event)) {
                    return false;
                }
                return true;
            case 82:
                if (featureId < 0) {
                    featureId = 0;
                }
                onKeyUpPanel(featureId, event);
                return true;
            case 84:
                if (getKeyguardManager().inKeyguardRestrictedInputMode()) {
                    return false;
                }
                if (event.isTracking() && !event.isCanceled()) {
                    launchDefaultSearch(event);
                }
                return true;
            case 164:
            case 1021:
                getAudioManager().handleKeyUp(event, this.mVolumeControlStreamType);
                return true;
            default:
                return false;
        }
    }

    protected void onActive() {
    }

    public final View getDecorView() {
        if (this.mDecor == null) {
            installDecor();
        }
        return this.mDecor;
    }

    public final View peekDecorView() {
        return this.mDecor;
    }

    public Bundle saveHierarchyState() {
        Bundle outState = new Bundle();
        if (this.mContentParent != null) {
            SparseArray<Parcelable> states = new SparseArray();
            this.mContentParent.saveHierarchyState(states);
            outState.putSparseParcelableArray(VIEWS_TAG, states);
            View focusedView = this.mContentParent.findFocus();
            if (!(focusedView == null || focusedView.getId() == -1)) {
                outState.putInt(FOCUSED_ID_TAG, focusedView.getId());
            }
            SparseArray<Parcelable> panelStates = new SparseArray();
            savePanelState(panelStates);
            if (panelStates.size() > 0) {
                outState.putSparseParcelableArray(PANELS_TAG, panelStates);
            }
            if (this.mDecorContentParent != null) {
                SparseArray<Parcelable> actionBarStates = new SparseArray();
                this.mDecorContentParent.saveToolbarHierarchyState(actionBarStates);
                outState.putSparseParcelableArray(ACTION_BAR_TAG, actionBarStates);
            }
        }
        return outState;
    }

    public void restoreHierarchyState(Bundle savedInstanceState) {
        if (this.mContentParent != null) {
            SparseArray<Parcelable> savedStates = savedInstanceState.getSparseParcelableArray(VIEWS_TAG);
            if (savedStates != null) {
                this.mContentParent.restoreHierarchyState(savedStates);
            }
            int focusedViewId = savedInstanceState.getInt(FOCUSED_ID_TAG, -1);
            if (focusedViewId != -1) {
                View needsFocus = this.mContentParent.findViewById(focusedViewId);
                if (needsFocus != null) {
                    needsFocus.requestFocus();
                } else {
                    Log.w(TAG, "Previously focused view reported id " + focusedViewId + " during save, but can't be found during restore.");
                }
            }
            SparseArray<Parcelable> panelStates = savedInstanceState.getSparseParcelableArray(PANELS_TAG);
            if (panelStates != null) {
                restorePanelState(panelStates);
            }
            if (this.mDecorContentParent != null) {
                SparseArray<Parcelable> actionBarStates = savedInstanceState.getSparseParcelableArray(ACTION_BAR_TAG);
                if (actionBarStates != null) {
                    doPendingInvalidatePanelMenu();
                    this.mDecorContentParent.restoreToolbarHierarchyState(actionBarStates);
                    return;
                }
                Log.w(TAG, "Missing saved instance states for action bar views! State will not be restored.");
            }
        }
    }

    private void savePanelState(SparseArray<Parcelable> icicles) {
        PanelFeatureState[] panels = this.mPanels;
        if (panels != null) {
            for (int curFeatureId = panels.length - 1; curFeatureId >= 0; curFeatureId--) {
                if (panels[curFeatureId] != null) {
                    icicles.put(curFeatureId, panels[curFeatureId].onSaveInstanceState());
                }
            }
        }
    }

    private void restorePanelState(SparseArray<Parcelable> icicles) {
        for (int i = icicles.size() - 1; i >= 0; i--) {
            int curFeatureId = icicles.keyAt(i);
            PanelFeatureState st = getPanelState(curFeatureId, false);
            if (st != null) {
                st.onRestoreInstanceState((Parcelable) icicles.get(curFeatureId));
                invalidatePanelMenu(curFeatureId);
            }
        }
    }

    private void openPanelsAfterRestore() {
        PanelFeatureState[] panels = this.mPanels;
        if (panels != null) {
            for (int i = panels.length - 1; i >= 0; i--) {
                PanelFeatureState st = panels[i];
                if (st != null) {
                    st.applyFrozenState();
                    if (!st.isOpen && st.wasLastOpen) {
                        st.isInExpandedMode = st.wasLastExpanded;
                        openPanel(st, null);
                    }
                }
            }
        }
    }

    protected DecorView generateDecor() {
        return new DecorView(getContext(), -1);
    }

    protected void setFeatureFromAttrs(int featureId, TypedArray attrs, int drawableAttr, int alphaAttr) {
        Drawable d = attrs.getDrawable(drawableAttr);
        if (d != null) {
            requestFeature(featureId);
            setFeatureDefaultDrawable(featureId, d);
        }
        if ((getFeatures() & (1 << featureId)) != 0) {
            int alpha = attrs.getInt(alphaAttr, -1);
            if (alpha >= 0) {
                setFeatureDrawableAlpha(featureId, alpha);
            }
        }
    }

    protected ViewGroup generateLayout(DecorView decor) {
        int layoutResource;
        TypedArray a = getWindowStyle();
        this.mIsFloating = a.getBoolean(4, false);
        int flagsToUpdate = 65792 & (getForcedWindowFlags() ^ -1);
        if (this.mIsFloating) {
            setLayout(-2, -2);
            setFlags(0, flagsToUpdate);
        } else {
            setFlags(65792, flagsToUpdate);
        }
        if (a.getBoolean(3, false)) {
            requestFeature(1);
        } else if (a.getBoolean(15, false)) {
            requestFeature(8);
        }
        if (a.getBoolean(17, false)) {
            requestFeature(9);
        }
        if (a.getBoolean(16, false)) {
            requestFeature(10);
        }
        if (a.getBoolean(25, false)) {
            requestFeature(11);
        }
        if (a.getBoolean(9, false)) {
            setFlags(1024, (getForcedWindowFlags() ^ -1) & 1024);
        }
        if (a.getBoolean(23, false)) {
            setFlags(67108864, 67108864 & (getForcedWindowFlags() ^ -1));
        }
        if (a.getBoolean(24, false)) {
            setFlags(134217728, 134217728 & (getForcedWindowFlags() ^ -1));
        }
        if (a.getBoolean(22, false)) {
            setFlags(33554432, 33554432 & (getForcedWindowFlags() ^ -1));
        }
        if (a.getBoolean(14, false)) {
            setFlags(1048576, 1048576 & (getForcedWindowFlags() ^ -1));
        }
        if (a.getBoolean(18, getContext().getApplicationInfo().targetSdkVersion >= 11)) {
            setFlags(8388608, 8388608 & (getForcedWindowFlags() ^ -1));
        }
        a.getValue(19, this.mMinWidthMajor);
        a.getValue(20, this.mMinWidthMinor);
        if (a.hasValue(49)) {
            if (this.mFixedWidthMajor == null) {
                this.mFixedWidthMajor = new TypedValue();
            }
            a.getValue(49, this.mFixedWidthMajor);
        }
        if (a.hasValue(51)) {
            if (this.mFixedWidthMinor == null) {
                this.mFixedWidthMinor = new TypedValue();
            }
            a.getValue(51, this.mFixedWidthMinor);
        }
        if (a.hasValue(52)) {
            if (this.mFixedHeightMajor == null) {
                this.mFixedHeightMajor = new TypedValue();
            }
            a.getValue(52, this.mFixedHeightMajor);
        }
        if (a.hasValue(50)) {
            if (this.mFixedHeightMinor == null) {
                this.mFixedHeightMinor = new TypedValue();
            }
            a.getValue(50, this.mFixedHeightMinor);
        }
        if (a.getBoolean(26, false)) {
            requestFeature(12);
        }
        if (a.getBoolean(45, false)) {
            requestFeature(13);
        }
        Context context = getContext();
        int targetSdk = context.getApplicationInfo().targetSdkVersion;
        boolean targetPreHoneycomb = targetSdk < 11;
        boolean targetPreIcs = targetSdk < 14;
        boolean targetPreL = targetSdk < 21;
        boolean targetHcNeedsOptions = context.getResources().getBoolean(R.bool.target_honeycomb_needs_options_menu);
        boolean noActionBar = !hasFeature(8) || hasFeature(1);
        if (targetPreHoneycomb || (targetPreIcs && targetHcNeedsOptions && noActionBar)) {
            setNeedsMenuKey(1);
        } else {
            setNeedsMenuKey(2);
        }
        if (!(this.mIsFloating || targetPreL || !a.getBoolean(34, false))) {
            setFlags(Integer.MIN_VALUE, Integer.MIN_VALUE & (getForcedWindowFlags() ^ -1));
        }
        if (!this.mForcedStatusBarColor) {
            this.mStatusBarColor = a.getColor(35, -16777216);
        }
        if (!this.mForcedNavigationBarColor) {
            this.mNavigationBarColor = a.getColor(36, -16777216);
        }
        if (a.getBoolean(46, false)) {
            decor.setSystemUiVisibility(decor.getSystemUiVisibility() | 8192);
        }
        if ((this.mAlwaysReadCloseOnTouchAttr || getContext().getApplicationInfo().targetSdkVersion >= 11) && a.getBoolean(21, false)) {
            setCloseOnTouchOutsideIfNotSet(true);
        }
        LayoutParams params = getAttributes();
        if (!hasSoftInputMode()) {
            params.softInputMode = a.getInt(13, params.softInputMode);
        }
        if (a.getBoolean(11, this.mIsFloating)) {
            if ((getForcedWindowFlags() & 2) == 0) {
                params.flags |= 2;
            }
            if (!haveDimAmount()) {
                params.dimAmount = a.getFloat(0, 0.5f);
            }
        }
        if (params.windowAnimations == 0) {
            params.windowAnimations = a.getResourceId(8, 0);
        }
        if (getContainer() == null) {
            if (this.mBackgroundDrawable == null) {
                if (this.mBackgroundResource == 0) {
                    int resourceId = a.getResourceId(1, 0);
                    this.mLastBackgroundResource = resourceId;
                    this.mBackgroundResource = resourceId;
                }
                if (this.mFrameResource == 0) {
                    this.mFrameResource = a.getResourceId(2, 0);
                }
                this.mBackgroundFallbackResource = a.getResourceId(47, 0);
            }
            this.mElevation = a.getDimension(38, 0.0f);
            this.mClipToOutline = a.getBoolean(39, false);
            this.mTextColor = a.getColor(7, 0);
        }
        int features = getLocalFeatures();
        if ((features & 2048) != 0) {
            layoutResource = R.layout.screen_swipe_dismiss;
        } else if ((features & 24) != 0) {
            if (this.mIsFloating) {
                res = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.dialogTitleIconsDecorLayout, res, true);
                layoutResource = res.resourceId;
            } else {
                layoutResource = R.layout.screen_title_icons;
            }
            removeFeature(8);
        } else if ((features & 36) != 0 && (features & 256) == 0) {
            layoutResource = R.layout.screen_progress;
        } else if ((features & 128) != 0) {
            if (this.mIsFloating) {
                res = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.dialogCustomTitleDecorLayout, res, true);
                layoutResource = res.resourceId;
            } else {
                layoutResource = R.layout.screen_custom_title;
            }
            removeFeature(8);
        } else if ((features & 2) == 0) {
            if (this.mIsFloating) {
                res = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.dialogTitleDecorLayout, res, true);
                layoutResource = res.resourceId;
            } else if ((features & 256) != 0) {
                layoutResource = a.getResourceId(48, R.layout.screen_action_bar);
            } else {
                layoutResource = R.layout.screen_title;
            }
        } else if ((features & 1024) != 0) {
            layoutResource = R.layout.screen_simple_overlay_action_mode;
        } else {
            layoutResource = R.layout.screen_simple;
        }
        this.mDecor.startChanging();
        View in = this.mLayoutInflater.inflate(layoutResource, null);
        decor.addView(in, new ViewGroup.LayoutParams(-1, -1));
        this.mContentRoot = (ViewGroup) in;
        ViewGroup contentParent = (ViewGroup) findViewById(16908290);
        if (contentParent == null) {
            throw new RuntimeException("Window couldn't find content container view");
        }
        if ((features & 32) != 0) {
            ProgressBar progress = getCircularProgressBar(false);
            if (progress != null) {
                progress.setIndeterminate(true);
            }
        }
        if ((features & 2048) != 0) {
            registerSwipeCallbacks();
        }
        if (getContainer() == null) {
            Drawable background;
            Drawable frame;
            if (this.mBackgroundResource != 0) {
                background = getContext().getResources().getDrawable(this.mBackgroundResource, getContext().getTheme(), true);
            } else {
                background = this.mBackgroundDrawable;
            }
            this.mDecor.setWindowBackground(background);
            if (this.mFrameResource != 0) {
                frame = getContext().getDrawable(this.mFrameResource);
            } else {
                frame = null;
            }
            this.mDecor.setWindowFrame(frame);
            this.mDecor.setElevation(this.mElevation);
            this.mDecor.setClipToOutline(this.mClipToOutline);
            if (this.mTitle != null) {
                setTitle(this.mTitle);
            }
            if (this.mTitleColor == 0) {
                this.mTitleColor = this.mTextColor;
            }
            setTitleColor(this.mTitleColor);
        }
        this.mDecor.finishChanging();
        return contentParent;
    }

    public void alwaysReadCloseOnTouchAttr() {
        this.mAlwaysReadCloseOnTouchAttr = true;
    }

    private void installDecor() {
        if (this.mDecor == null) {
            this.mDecor = generateDecor();
            this.mDecor.setDescendantFocusability(262144);
            this.mDecor.setIsRootNamespace(true);
            if (!(this.mInvalidatePanelMenuPosted || this.mInvalidatePanelMenuFeatures == 0)) {
                this.mDecor.postOnAnimation(this.mInvalidatePanelMenuRunnable);
            }
        }
        if (this.mContentParent == null) {
            int cm;
            this.mContentParent = generateLayout(this.mDecor);
            this.mDecor.makeOptionalFitsSystemWindows();
            DecorContentParent decorContentParent = (DecorContentParent) this.mDecor.findViewById(R.id.decor_content_parent);
            if (decorContentParent != null) {
                this.mDecorContentParent = decorContentParent;
                this.mDecorContentParent.setWindowCallback(getCallback());
                if (this.mDecorContentParent.getTitle() == null) {
                    this.mDecorContentParent.setWindowTitle(this.mTitle);
                }
                int localFeatures = getLocalFeatures();
                for (int i = 0; i < 13; i++) {
                    if (((1 << i) & localFeatures) != 0) {
                        this.mDecorContentParent.initFeature(i);
                    }
                }
                this.mDecorContentParent.setUiOptions(this.mUiOptions);
                if ((this.mResourcesSetFlags & 1) != 0 || (this.mIconRes != 0 && !this.mDecorContentParent.hasIcon())) {
                    this.mDecorContentParent.setIcon(this.mIconRes);
                } else if ((this.mResourcesSetFlags & 1) == 0 && this.mIconRes == 0 && !this.mDecorContentParent.hasIcon()) {
                    this.mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
                    this.mResourcesSetFlags |= 4;
                }
                if (!((this.mResourcesSetFlags & 2) == 0 && (this.mLogoRes == 0 || this.mDecorContentParent.hasLogo()))) {
                    this.mDecorContentParent.setLogo(this.mLogoRes);
                }
                PanelFeatureState st = getPanelState(0, false);
                if (!isDestroyed() && ((st == null || st.menu == null) && !this.mIsStartingWindow)) {
                    invalidatePanelMenu(8);
                }
            } else {
                this.mTitleView = (TextView) findViewById(R.id.title);
                if (this.mTitleView != null) {
                    this.mTitleView.setLayoutDirection(this.mDecor.getLayoutDirection());
                    if ((getLocalFeatures() & 2) != 0) {
                        View titleContainer = findViewById(R.id.title_container);
                        if (titleContainer != null) {
                            titleContainer.setVisibility(8);
                        } else {
                            this.mTitleView.setVisibility(8);
                        }
                        if (this.mContentParent instanceof FrameLayout) {
                            ((FrameLayout) this.mContentParent).setForeground(null);
                        }
                    } else {
                        this.mTitleView.setText(this.mTitle);
                    }
                }
            }
            if (this.mDecor.getBackground() == null && this.mBackgroundFallbackResource != 0) {
                this.mDecor.setBackgroundFallback(this.mBackgroundFallbackResource);
            }
            if (hasFeature(13)) {
                if (this.mTransitionManager == null) {
                    int transitionRes = getWindowStyle().getResourceId(27, 0);
                    if (transitionRes != 0) {
                        this.mTransitionManager = TransitionInflater.from(getContext()).inflateTransitionManager(transitionRes, this.mContentParent);
                    } else {
                        this.mTransitionManager = new TransitionManager();
                    }
                }
                this.mEnterTransition = getTransition(this.mEnterTransition, null, 28);
                this.mReturnTransition = getTransition(this.mReturnTransition, USE_DEFAULT_TRANSITION, 40);
                this.mExitTransition = getTransition(this.mExitTransition, null, 29);
                this.mReenterTransition = getTransition(this.mReenterTransition, USE_DEFAULT_TRANSITION, 41);
                this.mSharedElementEnterTransition = getTransition(this.mSharedElementEnterTransition, null, 30);
                this.mSharedElementReturnTransition = getTransition(this.mSharedElementReturnTransition, USE_DEFAULT_TRANSITION, 42);
                this.mSharedElementExitTransition = getTransition(this.mSharedElementExitTransition, null, 31);
                this.mSharedElementReenterTransition = getTransition(this.mSharedElementReenterTransition, USE_DEFAULT_TRANSITION, 43);
                if (this.mAllowEnterTransitionOverlap == null) {
                    this.mAllowEnterTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(33, true));
                }
                if (this.mAllowReturnTransitionOverlap == null) {
                    this.mAllowReturnTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(32, true));
                }
                if (this.mBackgroundFadeDurationMillis < 0) {
                    this.mBackgroundFadeDurationMillis = (long) getWindowStyle().getInteger(37, 300);
                }
                if (this.mSharedElementsUseOverlay == null) {
                    this.mSharedElementsUseOverlay = Boolean.valueOf(getWindowStyle().getBoolean(44, true));
                }
            }
            Context context = getContext();
            int coverMode = 0;
            try {
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(getContext().getPackageName(), 128);
                if (!(appInfo == null || appInfo.metaData == null)) {
                    cm = appInfo.metaData.getInt(LayoutParams.METADATA_COVER_MODE, 0);
                    if (cm != 0) {
                        coverMode = cm;
                    }
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (coverMode == 0) {
                if (!(context instanceof Activity) && (context instanceof ContextWrapper)) {
                    context = ((ContextWrapper) context).getBaseContext();
                }
                if (context != null && (context instanceof Activity)) {
                    try {
                        ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(new ComponentName(context, context.getClass()), 128);
                        if (!(activityInfo == null || activityInfo.metaData == null)) {
                            LayoutParams lp = getAttributes();
                            cm = activityInfo.metaData.getInt(LayoutParams.METADATA_COVER_MODE, 0);
                            if (cm != 0) {
                                coverMode = cm;
                            }
                        }
                    } catch (NameNotFoundException e2) {
                        e2.printStackTrace();
                    } catch (NullPointerException e3) {
                        e3.printStackTrace();
                    }
                }
            }
            if (coverMode != 0) {
                getAttributes().coverMode = coverMode;
            }
            if (!ViewConfiguration.get(getContext()).hasFakeMenuKey() && !this.mIsFloating && getAttributes().needsMenuKey == 1) {
                this.mDecor.setFloatingMenuEnabled(true);
            }
        }
    }

    private Transition getTransition(Transition currentValue, Transition defaultValue, int id) {
        if (currentValue != defaultValue) {
            return currentValue;
        }
        int transitionId = getWindowStyle().getResourceId(id, -1);
        Transition transition = defaultValue;
        if (!(transitionId == -1 || transitionId == R.transition.no_transition)) {
            transition = TransitionInflater.from(getContext()).inflateTransition(transitionId);
            if ((transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0) {
                transition = null;
            }
        }
        return transition;
    }

    private Drawable loadImageURI(Uri uri) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(getContext().getContentResolver().openInputStream(uri), null);
        } catch (Exception e) {
            Log.w(TAG, "Unable to open content: " + uri);
        }
        return drawable;
    }

    private DrawableFeatureState getDrawableState(int featureId, boolean required) {
        if ((getFeatures() & (1 << featureId)) != 0) {
            DrawableFeatureState[] ar = this.mDrawables;
            if (ar == null || ar.length <= featureId) {
                DrawableFeatureState[] nar = new DrawableFeatureState[(featureId + 1)];
                if (ar != null) {
                    System.arraycopy(ar, 0, nar, 0, ar.length);
                }
                ar = nar;
                this.mDrawables = nar;
            }
            DrawableFeatureState st = ar[featureId];
            if (st != null) {
                return st;
            }
            st = new DrawableFeatureState(featureId);
            ar[featureId] = st;
            return st;
        } else if (!required) {
            return null;
        } else {
            throw new RuntimeException("The feature has not been requested");
        }
    }

    private PanelFeatureState getPanelState(int featureId, boolean required) {
        return getPanelState(featureId, required, null);
    }

    private PanelFeatureState getPanelState(int featureId, boolean required, PanelFeatureState convertPanelState) {
        if ((getFeatures() & (1 << featureId)) != 0) {
            PanelFeatureState[] ar = this.mPanels;
            if (ar == null || ar.length <= featureId) {
                PanelFeatureState[] nar = new PanelFeatureState[(featureId + 1)];
                if (ar != null) {
                    System.arraycopy(ar, 0, nar, 0, ar.length);
                }
                ar = nar;
                this.mPanels = nar;
            }
            PanelFeatureState st = ar[featureId];
            if (st != null) {
                return st;
            }
            st = convertPanelState != null ? convertPanelState : new PanelFeatureState(featureId);
            ar[featureId] = st;
            return st;
        } else if (!required) {
            return null;
        } else {
            throw new RuntimeException("The feature has not been requested");
        }
    }

    public final void setChildDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        st.child = drawable;
        updateDrawable(featureId, st, false);
    }

    public final void setChildInt(int featureId, int value) {
        updateInt(featureId, value, false);
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        PanelFeatureState st = getPanelState(0, false);
        if (st == null || st.menu == null || !st.menu.isShortcutKey(keyCode, event)) {
            return false;
        }
        return true;
    }

    private void updateDrawable(int featureId, DrawableFeatureState st, boolean fromResume) {
        if (this.mContentParent != null) {
            int featureMask = 1 << featureId;
            if ((getFeatures() & featureMask) != 0 || fromResume) {
                Drawable drawable = null;
                if (st != null) {
                    drawable = st.child;
                    if (drawable == null) {
                        drawable = st.local;
                    }
                    if (drawable == null) {
                        drawable = st.def;
                    }
                }
                if ((getLocalFeatures() & featureMask) == 0) {
                    if (getContainer() == null) {
                        return;
                    }
                    if (isActive() || fromResume) {
                        getContainer().setChildDrawable(featureId, drawable);
                    }
                } else if (st == null) {
                } else {
                    if (st.cur != drawable || st.curAlpha != st.alpha) {
                        st.cur = drawable;
                        st.curAlpha = st.alpha;
                        onDrawableChanged(featureId, drawable, st.alpha);
                    }
                }
            }
        }
    }

    private void updateInt(int featureId, int value, boolean fromResume) {
        if (this.mContentParent != null) {
            int featureMask = 1 << featureId;
            if ((getFeatures() & featureMask) == 0 && !fromResume) {
                return;
            }
            if ((getLocalFeatures() & featureMask) != 0) {
                onIntChanged(featureId, value);
            } else if (getContainer() != null) {
                getContainer().setChildInt(featureId, value);
            }
        }
    }

    private ImageView getLeftIconView() {
        if (this.mLeftIconView != null) {
            return this.mLeftIconView;
        }
        if (this.mContentParent == null) {
            installDecor();
        }
        ImageView imageView = (ImageView) findViewById(R.id.left_icon);
        this.mLeftIconView = imageView;
        return imageView;
    }

    protected void dispatchWindowAttributesChanged(LayoutParams attrs) {
        super.dispatchWindowAttributesChanged(attrs);
        if (this.mDecor != null) {
            this.mDecor.updateColorViews(null, true);
        }
    }

    private ProgressBar getCircularProgressBar(boolean shouldInstallDecor) {
        if (this.mCircularProgressBar != null) {
            return this.mCircularProgressBar;
        }
        if (this.mContentParent == null && shouldInstallDecor) {
            installDecor();
        }
        this.mCircularProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        if (this.mCircularProgressBar != null) {
            this.mCircularProgressBar.setVisibility(4);
        }
        return this.mCircularProgressBar;
    }

    private ProgressBar getHorizontalProgressBar(boolean shouldInstallDecor) {
        if (this.mHorizontalProgressBar != null) {
            return this.mHorizontalProgressBar;
        }
        if (this.mContentParent == null && shouldInstallDecor) {
            installDecor();
        }
        this.mHorizontalProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        if (this.mHorizontalProgressBar != null) {
            this.mHorizontalProgressBar.setVisibility(4);
        }
        return this.mHorizontalProgressBar;
    }

    private ImageView getRightIconView() {
        if (this.mRightIconView != null) {
            return this.mRightIconView;
        }
        if (this.mContentParent == null) {
            installDecor();
        }
        ImageView imageView = (ImageView) findViewById(R.id.right_icon);
        this.mRightIconView = imageView;
        return imageView;
    }

    private void registerSwipeCallbacks() {
        SwipeDismissLayout swipeDismiss = (SwipeDismissLayout) findViewById(16908290);
        swipeDismiss.setOnDismissedListener(new OnDismissedListener() {
            public void onDismissed(SwipeDismissLayout layout) {
                PhoneWindow.this.dispatchOnWindowDismissed();
            }
        });
        swipeDismiss.setOnSwipeProgressChangedListener(new OnSwipeProgressChangedListener() {
            private static final float ALPHA_DECREASE = 0.5f;
            private boolean mIsTranslucent = false;

            public void onSwipeProgressChanged(SwipeDismissLayout layout, float progress, float translate) {
                int flags;
                LayoutParams newParams = PhoneWindow.this.getAttributes();
                newParams.x = (int) translate;
                newParams.alpha = 1.0f - (ALPHA_DECREASE * progress);
                PhoneWindow.this.setAttributes(newParams);
                if (newParams.x == 0) {
                    flags = 1024;
                } else {
                    flags = 512;
                }
                PhoneWindow.this.setFlags(flags, View.SYSTEM_UI_LAYOUT_FLAGS);
            }

            public void onSwipeCancelled(SwipeDismissLayout layout) {
                LayoutParams newParams = PhoneWindow.this.getAttributes();
                newParams.x = 0;
                newParams.alpha = 1.0f;
                PhoneWindow.this.setAttributes(newParams);
                PhoneWindow.this.setFlags(1024, View.SYSTEM_UI_LAYOUT_FLAGS);
            }
        });
    }

    private void callOnPanelClosed(int featureId, PanelFeatureState panel, Menu menu) {
        Window.Callback cb = getCallback();
        if (cb != null) {
            if (menu == null) {
                if (panel == null && featureId >= 0 && featureId < this.mPanels.length) {
                    panel = this.mPanels[featureId];
                }
                if (panel != null) {
                    menu = panel.menu;
                }
            }
            if ((panel == null || panel.isOpen) && !isDestroyed()) {
                cb.onPanelClosed(featureId, menu);
            }
        }
    }

    private boolean launchDefaultSearch(KeyEvent event) {
        boolean result;
        Window.Callback cb = getCallback();
        if (cb == null || isDestroyed()) {
            result = false;
        } else {
            sendCloseSystemWindows("search");
            int deviceId = event.getDeviceId();
            SearchEvent searchEvent = null;
            if (deviceId != 0) {
                searchEvent = new SearchEvent(InputDevice.getDevice(deviceId));
            }
            try {
                result = cb.onSearchRequested(searchEvent);
            } catch (AbstractMethodError e) {
                Log.e(TAG, "WindowCallback " + cb.getClass().getName() + " does not implement" + " method onSearchRequested(SearchEvent); fa", e);
                result = cb.onSearchRequested();
            }
        }
        if (result || (getContext().getResources().getConfiguration().uiMode & 15) != 4) {
            return result;
        }
        Bundle args = new Bundle();
        args.putInt("android.intent.extra.ASSIST_INPUT_DEVICE_ID", event.getDeviceId());
        return ((SearchManager) getContext().getSystemService("search")).launchLegacyAssist(null, UserHandle.myUserId(), args);
    }

    public void setVolumeControlStream(int streamType) {
        this.mVolumeControlStreamType = streamType;
    }

    public int getVolumeControlStream() {
        return this.mVolumeControlStreamType;
    }

    public void setMediaController(MediaController controller) {
        this.mMediaController = controller;
    }

    public MediaController getMediaController() {
        return this.mMediaController;
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    public void setReturnTransition(Transition transition) {
        this.mReturnTransition = transition;
    }

    public void setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    public void setReenterTransition(Transition transition) {
        this.mReenterTransition = transition;
    }

    public void setSharedElementEnterTransition(Transition sharedElementEnterTransition) {
        this.mSharedElementEnterTransition = sharedElementEnterTransition;
    }

    public void setSharedElementReturnTransition(Transition transition) {
        this.mSharedElementReturnTransition = transition;
    }

    public void setSharedElementExitTransition(Transition sharedElementExitTransition) {
        this.mSharedElementExitTransition = sharedElementExitTransition;
    }

    public void setSharedElementReenterTransition(Transition transition) {
        this.mSharedElementReenterTransition = transition;
    }

    public Transition getEnterTransition() {
        return this.mEnterTransition;
    }

    public Transition getReturnTransition() {
        return this.mReturnTransition == USE_DEFAULT_TRANSITION ? getEnterTransition() : this.mReturnTransition;
    }

    public Transition getExitTransition() {
        return this.mExitTransition;
    }

    public Transition getReenterTransition() {
        return this.mReenterTransition == USE_DEFAULT_TRANSITION ? getExitTransition() : this.mReenterTransition;
    }

    public Transition getSharedElementEnterTransition() {
        return this.mSharedElementEnterTransition;
    }

    public Transition getSharedElementReturnTransition() {
        return this.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION ? getSharedElementEnterTransition() : this.mSharedElementReturnTransition;
    }

    public Transition getSharedElementExitTransition() {
        return this.mSharedElementExitTransition;
    }

    public Transition getSharedElementReenterTransition() {
        return this.mSharedElementReenterTransition == USE_DEFAULT_TRANSITION ? getSharedElementExitTransition() : this.mSharedElementReenterTransition;
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
        this.mAllowEnterTransitionOverlap = Boolean.valueOf(allow);
    }

    public boolean getAllowEnterTransitionOverlap() {
        return this.mAllowEnterTransitionOverlap == null ? true : this.mAllowEnterTransitionOverlap.booleanValue();
    }

    public void setAllowReturnTransitionOverlap(boolean allowExitTransitionOverlap) {
        this.mAllowReturnTransitionOverlap = Boolean.valueOf(allowExitTransitionOverlap);
    }

    public boolean getAllowReturnTransitionOverlap() {
        return this.mAllowReturnTransitionOverlap == null ? true : this.mAllowReturnTransitionOverlap.booleanValue();
    }

    public long getTransitionBackgroundFadeDuration() {
        return this.mBackgroundFadeDurationMillis < 0 ? 300 : this.mBackgroundFadeDurationMillis;
    }

    public void setTransitionBackgroundFadeDuration(long fadeDurationMillis) {
        if (fadeDurationMillis < 0) {
            throw new IllegalArgumentException("negative durations are not allowed");
        }
        this.mBackgroundFadeDurationMillis = fadeDurationMillis;
    }

    public void setSharedElementsUseOverlay(boolean sharedElementsUseOverlay) {
        this.mSharedElementsUseOverlay = Boolean.valueOf(sharedElementsUseOverlay);
    }

    public boolean getSharedElementsUseOverlay() {
        return this.mSharedElementsUseOverlay == null ? true : this.mSharedElementsUseOverlay.booleanValue();
    }

    void sendCloseSystemWindows() {
        sendCloseSystemWindows(getContext(), null);
    }

    void sendCloseSystemWindows(String reason) {
        sendCloseSystemWindows(getContext(), reason);
    }

    public static void sendCloseSystemWindows(Context context, String reason) {
        if (ActivityManagerNative.isSystemReady()) {
            try {
                ActivityManagerNative.getDefault().closeSystemDialogs(reason);
            } catch (RemoteException e) {
            }
        }
    }

    public int getStatusBarColor() {
        return this.mStatusBarColor;
    }

    public void setStatusBarColor(int color) {
        this.mStatusBarColor = color;
        this.mForcedStatusBarColor = true;
        if (this.mDecor != null) {
            this.mDecor.updateColorViews(null, false);
        }
    }

    public int getNavigationBarColor() {
        return this.mNavigationBarColor;
    }

    public void setNavigationBarColor(int color) {
        this.mNavigationBarColor = color;
        this.mForcedNavigationBarColor = true;
        if (this.mDecor != null) {
            this.mDecor.updateColorViews(null, false);
        }
    }

    public void setIsStartingWindow(boolean isStartingWindow) {
        this.mIsStartingWindow = isStartingWindow;
    }

    private ZoomKeyController getZoomKeyController() {
        if (this.mZoomKeyController == null) {
            this.mZoomKeyController = new ZoomKeyController(getContext());
        }
        return this.mZoomKeyController;
    }

    private static void ensureSFinderFeatureCached(Context context) {
        if (context != null && !mSFinderFeatureCached) {
            mSFinderEnabled = context.getPackageManager().hasSystemFeature("com.sec.feature.findo");
            mSFinderFeatureCached = true;
        }
    }

    public void setTransientCocktailBar(boolean disable) {
        LayoutParams lp = getAttributes();
        if (disable) {
            lp.samsungFlags |= 1073741824;
        } else {
            lp.samsungFlags &= -1073741825;
        }
    }

    public void setNavigationGuardVisibility(int vis) {
        if (this.mDecor != null && this.mDecor.mNavigationGuard != null) {
            this.mDecor.mNavigationGuard.setVisibility(vis);
        }
    }
}
