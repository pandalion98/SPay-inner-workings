package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.transition.Transition.TransitionListenerAdapter;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.lang.ref.WeakReference;

public class PopupWindow {
    private static final int[] ABOVE_ANCHOR_STATE_SET = new int[]{R.attr.state_above_anchor};
    private static final int ANIMATION_STYLE_DEFAULT = -1;
    private static final int DEFAULT_ANCHORED_GRAVITY = 8388659;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    private static final String TAG = "PopupWindow";
    private boolean mAboveAnchor;
    private Drawable mAboveAnchorBackgroundDrawable;
    private boolean mAllowScrollingAnchorParent;
    private WeakReference<View> mAnchor;
    private int mAnchorXoff;
    private int mAnchorYoff;
    private int mAnchoredGravity;
    private int mAnimationStyle;
    private boolean mAttachedInDecor;
    private boolean mAttachedInDecorSet;
    private Drawable mBackground;
    private View mBackgroundView;
    private Drawable mBelowAnchorBackgroundDrawable;
    private boolean mClipToScreen;
    private boolean mClippingEnabled;
    private View mContentView;
    private Context mContext;
    private PopupDecorView mDecorView;
    private float mDimAmount;
    private boolean mDimBehind;
    private final int[] mDrawingLocation;
    private float mElevation;
    private Transition mEnterTransition;
    private Transition mExitTransition;
    private boolean mFocusable;
    private int mHeight;
    private int mHeightMode;
    private boolean mIgnoreCheekPress;
    private boolean mIgnoreMultiWindowLayout;
    private int mInputMethodMode;
    private boolean mIsDeviceDefaultLight;
    private boolean mIsDropdown;
    private boolean mIsShowing;
    private boolean mIsTransitioningToDismiss;
    private boolean mIsWindowPopupOutsideBound;
    private int mLastHeight;
    private int mLastWidth;
    private boolean mLayoutInScreen;
    private boolean mLayoutInsetDecor;
    private boolean mNotTouchModal;
    private int mOldDisplayId;
    private OnDismissListener mOnDismissListener;
    private final OnScrollChangedListener mOnScrollChangedListener;
    private boolean mOutsideTouchable;
    private boolean mOverlapAnchor;
    private int mPopupHeight;
    private boolean mPopupViewInitialLayoutDirectionInherited;
    private int mPopupWidth;
    private final int[] mScreenLocation;
    private boolean mShowForAllUsers;
    private int mSoftInputMode;
    private int mSplitTouchEnabled;
    private int mStatusBarHeight;
    private final Rect mTempRect;
    private OnTouchListener mTouchInterceptor;
    private boolean mTouchable;
    private int mWidth;
    private int mWidthMode;
    private int mWindowLayoutType;
    private WindowManager mWindowManager;

    public interface OnDismissListener {
        void onDismiss();
    }

    private class PopupBackgroundView extends FrameLayout {
        public PopupBackgroundView(Context context) {
            super(context);
        }

        protected int[] onCreateDrawableState(int extraSpace) {
            if (!PopupWindow.this.mAboveAnchor) {
                return super.onCreateDrawableState(extraSpace);
            }
            int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            View.mergeDrawableStates(drawableState, PopupWindow.ABOVE_ANCHOR_STATE_SET);
            return drawableState;
        }
    }

    private class PopupDecorView extends FrameLayout {
        private boolean mIsPenSelectionMode = false;
        private TransitionListenerAdapter mPendingExitListener;
        private int mSpenUspLevel = -1;

        public PopupDecorView(Context context) {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() != 4) {
                return super.dispatchKeyEvent(event);
            }
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }
            DispatcherState state;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                state = getKeyDispatcherState();
                if (state == null) {
                    return true;
                }
                state.startTracking(event, this);
                return true;
            }
            if (event.getAction() == 1) {
                state = getKeyDispatcherState();
                if (!(state == null || !state.isTracking(event) || event.isCanceled())) {
                    PopupWindow.this.dismiss();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
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
            if (PopupWindow.this.mTouchInterceptor == null || !PopupWindow.this.mTouchInterceptor.onTouch(this, ev)) {
                return super.dispatchTouchEvent(ev);
            }
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() == 0 && (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
                PopupWindow.this.dismiss();
                return true;
            } else if (event.getAction() != 4) {
                return super.onTouchEvent(event);
            } else {
                PopupWindow.this.dismiss();
                return true;
            }
        }

        public void requestEnterTransition(Transition transition) {
            ViewTreeObserver observer = getViewTreeObserver();
            if (observer != null && transition != null) {
                final Transition enterTransition = transition.clone();
                observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        ViewTreeObserver observer = PopupDecorView.this.getViewTreeObserver();
                        if (observer != null) {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                        final Rect epicenter = PopupWindow.this.getRelativeAnchorBounds();
                        enterTransition.setEpicenterCallback(new EpicenterCallback() {
                            public Rect onGetEpicenter(Transition transition) {
                                return epicenter;
                            }
                        });
                        PopupDecorView.this.startEnterTransition(enterTransition);
                    }
                });
            }
        }

        private void startEnterTransition(Transition enterTransition) {
            int i;
            int count = getChildCount();
            for (i = 0; i < count; i++) {
                View child = getChildAt(i);
                enterTransition.addTarget(child);
                child.setVisibility(4);
            }
            TransitionManager.beginDelayedTransition(this, enterTransition);
            for (i = 0; i < count; i++) {
                getChildAt(i).setVisibility(0);
            }
        }

        public void startExitTransition(Transition transition, final TransitionListener listener) {
            if (transition != null) {
                int i;
                this.mPendingExitListener = new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        listener.onTransitionEnd(transition);
                        PopupDecorView.this.mPendingExitListener = null;
                    }
                };
                Transition exitTransition = transition.clone();
                exitTransition.addListener(this.mPendingExitListener);
                int count = getChildCount();
                for (i = 0; i < count; i++) {
                    exitTransition.addTarget(getChildAt(i));
                }
                TransitionManager.beginDelayedTransition(this, exitTransition);
                for (i = 0; i < count; i++) {
                    getChildAt(i).setVisibility(4);
                }
            }
        }

        public void cancelTransitions() {
            TransitionManager.endTransitions(this);
            if (this.mPendingExitListener != null) {
                this.mPendingExitListener.onTransitionEnd(null);
            }
        }
    }

    public PopupWindow(Context context) {
        this(context, null);
    }

    public PopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, (int) R.attr.popupWindowStyle);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Transition exitTransition;
        this.mDrawingLocation = new int[2];
        this.mScreenLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mStatusBarHeight = 0;
        this.mIsWindowPopupOutsideBound = false;
        this.mAnimationStyle = -1;
        this.mShowForAllUsers = false;
        this.mIgnoreMultiWindowLayout = false;
        this.mIsDeviceDefaultLight = false;
        this.mOldDisplayId = 0;
        this.mOnScrollChangedListener = new OnScrollChangedListener() {
            public void onScrollChanged() {
                View anchor = PopupWindow.this.mAnchor != null ? (View) PopupWindow.this.mAnchor.get() : null;
                if (anchor != null && PopupWindow.this.mDecorView != null) {
                    LayoutParams p = (LayoutParams) PopupWindow.this.mDecorView.getLayoutParams();
                    PopupWindow.this.updateAboveAnchor(PopupWindow.this.findDropDownPosition(anchor, p, PopupWindow.this.mAnchorXoff, PopupWindow.this.mAnchorYoff, PopupWindow.this.mAnchoredGravity));
                    PopupWindow.this.update(p.x, p.y, -1, -1, true);
                }
            }
        };
        this.mDimBehind = false;
        this.mDimAmount = 0.0f;
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupWindow, defStyleAttr, defStyleRes);
        Drawable bg = a.getDrawable(0);
        this.mElevation = a.getDimension(3, 0.0f);
        this.mOverlapAnchor = a.getBoolean(2, false);
        if (a.hasValueOrEmpty(1)) {
            int animStyle = a.getResourceId(1, 0);
            if (animStyle == R.style.Animation_PopupWindow) {
                this.mAnimationStyle = -1;
            } else {
                this.mAnimationStyle = animStyle;
            }
        } else {
            this.mAnimationStyle = -1;
        }
        Transition enterTransition = getTransition(a.getResourceId(4, 0));
        if (a.hasValueOrEmpty(5)) {
            exitTransition = getTransition(a.getResourceId(5, 0));
        } else {
            exitTransition = enterTransition == null ? null : enterTransition.clone();
        }
        a.recycle();
        setEnterTransition(enterTransition);
        setExitTransition(exitTransition);
        setBackgroundDrawable(bg);
        TypedValue outValue = new TypedValue();
        TypedValue colorValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, false);
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, colorValue, true);
        if (outValue.data != 0 && colorValue.data == 0) {
            this.mIsDeviceDefaultLight = true;
        }
    }

    public PopupWindow() {
        this(null, 0, 0);
    }

    public PopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public PopupWindow(int width, int height) {
        this(null, width, height);
    }

    public PopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        this.mDrawingLocation = new int[2];
        this.mScreenLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mStatusBarHeight = 0;
        this.mIsWindowPopupOutsideBound = false;
        this.mAnimationStyle = -1;
        this.mShowForAllUsers = false;
        this.mIgnoreMultiWindowLayout = false;
        this.mIsDeviceDefaultLight = false;
        this.mOldDisplayId = 0;
        this.mOnScrollChangedListener = /* anonymous class already generated */;
        this.mDimBehind = false;
        this.mDimAmount = 0.0f;
        if (contentView != null) {
            this.mContext = contentView.getContext();
            this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        }
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    public void setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    private Transition getTransition(int resId) {
        if (!(resId == 0 || resId == R.transition.no_transition)) {
            Transition transition = TransitionInflater.from(this.mContext).inflateTransition(resId);
            if (transition != null) {
                boolean isEmpty = (transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0;
                if (!isEmpty) {
                    return transition;
                }
            }
        }
        return null;
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mBackground = background;
        if (this.mBackground instanceof StateListDrawable) {
            StateListDrawable stateList = this.mBackground;
            int aboveAnchorStateIndex = stateList.getStateDrawableIndex(ABOVE_ANCHOR_STATE_SET);
            int count = stateList.getStateCount();
            int belowAnchorStateIndex = -1;
            for (int i = 0; i < count; i++) {
                if (i != aboveAnchorStateIndex) {
                    belowAnchorStateIndex = i;
                    break;
                }
            }
            if (aboveAnchorStateIndex == -1 || belowAnchorStateIndex == -1) {
                this.mBelowAnchorBackgroundDrawable = null;
                this.mAboveAnchorBackgroundDrawable = null;
                return;
            }
            this.mAboveAnchorBackgroundDrawable = stateList.getStateDrawable(aboveAnchorStateIndex);
            this.mBelowAnchorBackgroundDrawable = stateList.getStateDrawable(belowAnchorStateIndex);
        }
    }

    public float getElevation() {
        return this.mElevation;
    }

    public void setElevation(float elevation) {
        this.mElevation = elevation;
    }

    public int getAnimationStyle() {
        return this.mAnimationStyle;
    }

    public void setIgnoreCheekPress() {
        this.mIgnoreCheekPress = true;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void setContentView(View contentView) {
        if (!isShowing()) {
            this.mContentView = contentView;
            if (this.mContext == null && this.mContentView != null) {
                this.mContext = this.mContentView.getContext();
            }
            if (this.mWindowManager == null && this.mContentView != null) {
                this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
            }
            if (this.mContext != null && !this.mAttachedInDecorSet) {
                setAttachedInDecor(this.mContext.getApplicationInfo().targetSdkVersion >= 22);
            }
        }
    }

    public void setTouchInterceptor(OnTouchListener l) {
        this.mTouchInterceptor = l;
    }

    public boolean isFocusable() {
        return this.mFocusable;
    }

    public void setFocusable(boolean focusable) {
        this.mFocusable = focusable;
    }

    public int getInputMethodMode() {
        return this.mInputMethodMode;
    }

    public void setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
    }

    public void setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
    }

    public int getSoftInputMode() {
        return this.mSoftInputMode;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    public void setTouchable(boolean touchable) {
        this.mTouchable = touchable;
    }

    public boolean isOutsideTouchable() {
        return this.mOutsideTouchable;
    }

    public void setOutsideTouchable(boolean touchable) {
        this.mOutsideTouchable = touchable;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public void setClippingEnabled(boolean enabled) {
        this.mClippingEnabled = enabled;
    }

    public void setClipToScreenEnabled(boolean enabled) {
        this.mClipToScreen = enabled;
        setClippingEnabled(!enabled);
    }

    void setAllowScrollingAnchorParent(boolean enabled) {
        this.mAllowScrollingAnchorParent = enabled;
    }

    public boolean isSplitTouchEnabled() {
        if (this.mSplitTouchEnabled >= 0 || this.mContext == null) {
            if (this.mSplitTouchEnabled != 1) {
                return false;
            }
            return true;
        } else if (this.mContext.getApplicationInfo().targetSdkVersion >= 11) {
            return true;
        } else {
            return false;
        }
    }

    public void setSplitTouchEnabled(boolean enabled) {
        this.mSplitTouchEnabled = enabled ? 1 : 0;
    }

    public boolean isLayoutInScreenEnabled() {
        return this.mLayoutInScreen;
    }

    public void setLayoutInScreenEnabled(boolean enabled) {
        this.mLayoutInScreen = enabled;
    }

    public boolean isAttachedInDecor() {
        return this.mAttachedInDecor;
    }

    public void setAttachedInDecor(boolean enabled) {
        this.mAttachedInDecor = enabled;
        this.mAttachedInDecorSet = true;
    }

    public void setLayoutInsetDecor(boolean enabled) {
        this.mLayoutInsetDecor = enabled;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mWindowLayoutType = layoutType;
    }

    public int getWindowLayoutType() {
        return this.mWindowLayoutType;
    }

    public void setTouchModal(boolean touchModal) {
        this.mNotTouchModal = !touchModal;
    }

    @Deprecated
    public void setWindowLayoutMode(int widthSpec, int heightSpec) {
        this.mWidthMode = widthSpec;
        this.mHeightMode = heightSpec;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setOverlapAnchor(boolean overlapAnchor) {
        this.mOverlapAnchor = overlapAnchor;
    }

    public boolean getOverlapAnchor() {
        return this.mOverlapAnchor;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        showAtLocation(parent.getWindowToken(), gravity, x, y);
    }

    public void showAtLocation(IBinder token, int gravity, int x, int y) {
        if (!isShowing() && this.mContentView != null) {
            TransitionManager.endTransitions(this.mDecorView);
            unregisterForScrollChanged();
            this.mIsShowing = true;
            this.mIsDropdown = false;
            LayoutParams p = createPopupLayoutParams(token);
            preparePopup(p);
            if (gravity != 0) {
                p.gravity = gravity;
            }
            p.x = x;
            p.y = y;
            p.multiWindowFlags |= 32;
            invokePopup(p);
        }
    }

    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAsDropDown(anchor, xoff, yoff, DEFAULT_ANCHORED_GRAVITY);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (!isShowing() && this.mContentView != null) {
            TransitionManager.endTransitions(this.mDecorView);
            registerForScrollChanged(anchor, xoff, yoff, gravity);
            this.mIsShowing = true;
            this.mIsDropdown = true;
            LayoutParams p = createPopupLayoutParams(anchor.getWindowToken());
            preparePopup(p);
            updateAboveAnchor(findDropDownPosition(anchor, p, xoff, yoff, gravity));
            invokePopup(p);
        }
    }

    private void updateAboveAnchor(boolean aboveAnchor) {
        if (aboveAnchor != this.mAboveAnchor) {
            this.mAboveAnchor = aboveAnchor;
            if (this.mBackground != null && this.mBackgroundView != null) {
                if (this.mAboveAnchorBackgroundDrawable == null) {
                    this.mBackgroundView.refreshDrawableState();
                } else if (this.mAboveAnchor) {
                    this.mBackgroundView.setBackground(this.mAboveAnchorBackgroundDrawable);
                } else {
                    this.mBackgroundView.setBackground(this.mBelowAnchorBackgroundDrawable);
                }
            }
        }
    }

    public boolean isAboveAnchor() {
        return this.mAboveAnchor;
    }

    private void preparePopup(LayoutParams p) {
        boolean z = true;
        if (this.mContentView == null || this.mContext == null || this.mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by calling setContentView() before attempting to show the popup.");
        }
        if (this.mDecorView != null) {
            this.mDecorView.cancelTransitions();
        }
        if (this.mBackground != null) {
            this.mBackgroundView = createBackgroundView(this.mContentView);
            this.mBackgroundView.setBackground(this.mBackground);
        } else {
            this.mBackgroundView = this.mContentView;
        }
        this.mDecorView = createDecorView(this.mBackgroundView);
        this.mBackgroundView.setElevation(this.mElevation);
        int surfaceInset = (int) Math.ceil((double) (this.mBackgroundView.getZ() * 2.0f));
        p.surfaceInsets.set(surfaceInset, surfaceInset, surfaceInset, surfaceInset);
        p.hasManualSurfaceInsets = true;
        if (this.mContentView.getRawLayoutDirection() != 2) {
            z = false;
        }
        this.mPopupViewInitialLayoutDirectionInherited = z;
        this.mPopupWidth = p.width;
        this.mPopupHeight = p.height;
        if (this.mPopupWidth == 0 && this.mDecorView != null && this.mContext != null && isCascadeTypeMultiWindow()) {
            this.mDecorView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
            this.mPopupWidth = this.mDecorView.getMeasuredWidth();
        }
    }

    private PopupBackgroundView createBackgroundView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams == null || layoutParams.height != -2) {
            height = -1;
        } else {
            height = -2;
        }
        PopupBackgroundView backgroundView = new PopupBackgroundView(this.mContext);
        backgroundView.addView(contentView, (ViewGroup.LayoutParams) new FrameLayout.LayoutParams(-1, height));
        return backgroundView;
    }

    private PopupDecorView createDecorView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams == null || layoutParams.height != -2) {
            height = -1;
        } else {
            height = -2;
        }
        PopupDecorView decorView = new PopupDecorView(this.mContext);
        decorView.addView(contentView, -1, height);
        decorView.setClipChildren(false);
        decorView.setClipToPadding(false);
        return decorView;
    }

    private void invokePopup(LayoutParams p) {
        if (this.mContext != null) {
            p.packageName = this.mContext.getPackageName();
        }
        PopupDecorView decorView = this.mDecorView;
        decorView.setFitsSystemWindows(this.mLayoutInsetDecor);
        if (this.mShowForAllUsers) {
            p.privateFlags |= 16;
        }
        if (this.mIgnoreMultiWindowLayout) {
            p.multiWindowFlags |= 8;
        }
        setLayoutDirectionFromAnchor();
        this.mWindowManager.addView(decorView, p);
        if (this.mEnterTransition != null) {
            decorView.requestEnterTransition(this.mEnterTransition);
        }
    }

    private void setLayoutDirectionFromAnchor() {
        if (this.mAnchor != null) {
            View anchor = (View) this.mAnchor.get();
            if (anchor != null && this.mPopupViewInitialLayoutDirectionInherited) {
                this.mDecorView.setLayoutDirection(anchor.getLayoutDirection());
            }
        }
    }

    private LayoutParams createPopupLayoutParams(IBinder token) {
        int i;
        LayoutParams p = new LayoutParams();
        p.gravity = DEFAULT_ANCHORED_GRAVITY;
        p.flags = computeFlags(p.flags);
        p.type = this.mWindowLayoutType;
        p.token = token;
        p.softInputMode = this.mSoftInputMode;
        p.windowAnimations = computeAnimationResource();
        if (this.mBackground != null) {
            p.format = this.mBackground.getOpacity();
        } else {
            p.format = -3;
        }
        if (this.mHeightMode < 0) {
            i = this.mHeightMode;
            this.mLastHeight = i;
            p.height = i;
        } else {
            i = this.mHeight;
            this.mLastHeight = i;
            p.height = i;
        }
        if (this.mDimBehind && this.mDimAmount > 0.0f) {
            p.flags |= 2;
            p.dimAmount = this.mDimAmount;
        }
        if (this.mWidthMode < 0) {
            i = this.mWidthMode;
            this.mLastWidth = i;
            p.width = i;
        } else {
            i = this.mWidth;
            this.mLastWidth = i;
            p.width = i;
        }
        p.setTitle("PopupWindow:" + Integer.toHexString(hashCode()));
        return p;
    }

    private int computeFlags(int curFlags) {
        curFlags &= -8815129;
        if (this.mIgnoreCheekPress) {
            curFlags |= 32768;
        }
        if (!this.mFocusable) {
            curFlags |= 8;
            if (this.mInputMethodMode == 1) {
                curFlags |= 131072;
            }
        } else if (this.mInputMethodMode == 2) {
            curFlags |= 131072;
        }
        if (!this.mTouchable) {
            curFlags |= 16;
        }
        if (this.mOutsideTouchable) {
            curFlags |= 262144;
        }
        if (!this.mClippingEnabled) {
            curFlags |= 512;
        }
        if (isSplitTouchEnabled()) {
            curFlags |= 8388608;
        }
        if (this.mLayoutInScreen) {
            curFlags |= 256;
        }
        if (this.mLayoutInsetDecor) {
            curFlags |= 65536;
        }
        if (this.mNotTouchModal) {
            curFlags |= 32;
        }
        if (this.mAttachedInDecor) {
            return curFlags | 1073741824;
        }
        return curFlags;
    }

    private int computeAnimationResource() {
        if (this.mAnimationStyle != -1) {
            return this.mAnimationStyle;
        }
        if (this.mIsDropdown) {
            return this.mAboveAnchor ? R.style.Animation_DropDownUp : R.style.Animation_DropDownDown;
        } else {
            return 0;
        }
    }

    private boolean findDropDownPosition(View anchor, LayoutParams p, int xoff, int yoff, int gravity) {
        int anchorHeight = anchor.getHeight();
        int anchorWidth = anchor.getWidth();
        if (this.mOverlapAnchor) {
            yoff -= anchorHeight;
        }
        anchor.getLocationInWindow(this.mDrawingLocation);
        p.x = this.mDrawingLocation[0] + xoff;
        p.y = (this.mDrawingLocation[1] + anchorHeight) + yoff;
        int hgrav = Gravity.getAbsoluteGravity(gravity, anchor.getLayoutDirection()) & 7;
        if (hgrav == 5) {
            p.x -= this.mPopupWidth - anchorWidth;
        }
        boolean onTop = false;
        p.gravity = 51;
        anchor.getLocationOnScreen(this.mScreenLocation);
        Rect displayFrame = new Rect();
        anchor.getWindowVisibleDisplayFrame(displayFrame);
        int screenY = (this.mScreenLocation[1] + anchorHeight) + yoff;
        View root = anchor.getRootView();
        ViewGroup.LayoutParams vlp = root.getLayoutParams();
        if (vlp instanceof LayoutParams) {
            LayoutParams wlp = (LayoutParams) vlp;
        }
        Rect visibleDisplayFrame = new Rect();
        getVisibleDisplayRect(anchor, visibleDisplayFrame);
        if (this.mIsWindowPopupOutsideBound) {
            displayFrame.set(visibleDisplayFrame);
        }
        if (this.mPopupHeight + screenY > displayFrame.bottom || (p.x + this.mPopupWidth) - root.getWidth() > 0) {
            boolean isOnlyOverScreenWidth = this.mPopupHeight + screenY <= displayFrame.bottom && (p.x + this.mPopupWidth) - root.getWidth() > 0;
            if (this.mAllowScrollingAnchorParent) {
                int scrollX = anchor.getScrollX();
                int scrollY = anchor.getScrollY();
                Rect rect = new Rect(scrollX, scrollY, (this.mPopupWidth + scrollX) + xoff, ((this.mPopupHeight + scrollY) + anchorHeight) + yoff);
                if (this.mIsDeviceDefaultLight) {
                    anchor.requestRectangleOnScreen(rect, false);
                } else {
                    anchor.requestRectangleOnScreen(rect, true);
                }
            }
            anchor.getLocationInWindow(this.mDrawingLocation);
            p.x = this.mDrawingLocation[0] + xoff;
            p.y = (this.mDrawingLocation[1] + anchorHeight) + yoff;
            if (hgrav == 5) {
                p.x -= this.mPopupWidth - anchorWidth;
            }
            anchor.getLocationOnScreen(this.mScreenLocation);
            onTop = ((displayFrame.bottom - this.mScreenLocation[1]) - anchorHeight) - yoff < (this.mScreenLocation[1] - yoff) - displayFrame.top;
            if (onTop) {
                p.gravity = 83;
                p.y = (root.getHeight() - this.mDrawingLocation[1]) + yoff;
                if (p.y < 0) {
                    p.y = 0;
                }
                if (this.mIsDeviceDefaultLight && isOnlyOverScreenWidth) {
                    p.gravity = 51;
                    p.y = (this.mDrawingLocation[1] + anchorHeight) + yoff;
                }
            } else {
                p.y = (this.mDrawingLocation[1] + anchorHeight) + yoff;
            }
            if ((p.x + this.mPopupWidth) - root.getWidth() > 0) {
                p.x -= (p.x + this.mPopupWidth) - root.getWidth();
                if (p.x <= 0) {
                    p.x = 0;
                }
            }
        }
        if (this.mClipToScreen && !isCascadeTypeMultiWindow()) {
            int displayFrameWidth = displayFrame.right - displayFrame.left;
            int right = p.x + p.width;
            int displayFrameLeft = displayFrame.left;
            int displayFrameTop = displayFrame.top;
            if (isMultiWindow()) {
                int[] rootLocation = new int[2];
                root.getLocationOnScreen(rootLocation);
                displayFrameLeft -= rootLocation[0];
                displayFrameTop -= rootLocation[1];
            }
            if (right > displayFrameWidth) {
                p.x -= right - displayFrameWidth;
            }
            if (isMultiWindow() && displayFrameWidth > this.mPopupWidth && p.width < this.mPopupWidth) {
                p.width = this.mPopupWidth;
            }
            if (p.x < displayFrameLeft) {
                p.x = displayFrameLeft;
                p.width = Math.min(p.width, displayFrameWidth);
            }
            if (onTop) {
                int popupTop = (this.mScreenLocation[1] - yoff) - this.mPopupHeight;
                int statusBarHeight = 0;
                try {
                    if (Stub.asInterface(ServiceManager.getService("window")).isStatusBarVisible()) {
                        statusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, e.toString());
                }
                if (popupTop < statusBarHeight) {
                    p.y -= statusBarHeight - popupTop;
                }
            } else {
                p.y = Math.max(p.y, displayFrameTop);
            }
        }
        if (isMultiWindow() && p.y > displayFrame.bottom && !onTop) {
            p.y = (this.mScreenLocation[1] + anchorHeight) + yoff;
        }
        if (isCascadeTypeMultiWindow() && anchor.isAttachedToFullWindow() && p.y < 0 && this.mScreenLocation[1] < 0) {
            p.y = 0;
        }
        p.gravity |= 268435456;
        return onTop;
    }

    private void getVisibleDisplayRect(View anchor, Rect outRect) {
        if (anchor != null && outRect != null) {
            ViewGroup.LayoutParams vlp = anchor.getRootView().getLayoutParams();
            LayoutParams wlp = null;
            if (vlp instanceof LayoutParams) {
                wlp = (LayoutParams) vlp;
                this.mIsWindowPopupOutsideBound = (wlp.flags & 512) != 0;
            }
            Rect visibleDisplayFrame = new Rect();
            this.mStatusBarHeight = 0;
            DisplayMetrics dm = new DisplayMetrics();
            this.mWindowManager.getDefaultDisplay().getMetrics(dm);
            if (!(wlp == null || this.mContext == null || ((wlp.systemUiVisibility | wlp.subtreeSystemUiVisibility) & 1028) != 0)) {
                this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
            }
            Rect displayFrame = new Rect();
            if (isCascadeTypeMultiWindow()) {
                anchor.getWindowVisibleContentFrame(displayFrame);
                visibleDisplayFrame.left = 0;
                visibleDisplayFrame.top = 0;
                visibleDisplayFrame.right += displayFrame.width();
                visibleDisplayFrame.bottom += displayFrame.height();
            } else {
                anchor.getWindowVisibleDisplayFrame(displayFrame);
                visibleDisplayFrame.left = 0;
                visibleDisplayFrame.top = this.mStatusBarHeight;
                visibleDisplayFrame.right = dm.widthPixels;
                visibleDisplayFrame.bottom = dm.heightPixels;
            }
            if (this.mIsWindowPopupOutsideBound) {
                displayFrame.set(visibleDisplayFrame);
            }
            outRect.set(displayFrame);
        }
    }

    public int getMaxAvailableHeight(View anchor) {
        return getMaxAvailableHeight(anchor, 0);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset) {
        return getMaxAvailableHeight(anchor, yOffset, false);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        Rect displayFrame = new Rect();
        anchor.getWindowVisibleDisplayFrame(displayFrame);
        getVisibleDisplayRect(anchor, displayFrame);
        int[] anchorPos = this.mDrawingLocation;
        if (isCascadeTypeMultiWindow()) {
            anchor.getLocationInWindow(anchorPos);
        } else {
            anchor.getLocationOnScreen(anchorPos);
        }
        int bottomEdge = displayFrame.bottom;
        if (ignoreBottomDecorations && !isCascadeTypeMultiWindow()) {
            bottomEdge = anchor.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        int anchorHeight = anchor.getHeight();
        if (this.mOverlapAnchor) {
            anchorHeight = 0;
        }
        int returnedHeight = Math.max((bottomEdge - (anchorPos[1] + anchorHeight)) - yOffset, (anchorPos[1] - displayFrame.top) + yOffset);
        if (this.mBackground == null) {
            return returnedHeight;
        }
        this.mBackground.getPadding(this.mTempRect);
        return returnedHeight - (this.mTempRect.top + this.mTempRect.bottom);
    }

    public void dismiss() {
        if (isShowing() && !this.mIsTransitioningToDismiss) {
            ViewGroup contentHolder;
            final PopupDecorView decorView = this.mDecorView;
            final View contentView = this.mContentView;
            ViewParent contentParent = contentView.getParent();
            if (contentParent instanceof ViewGroup) {
                contentHolder = (ViewGroup) contentParent;
            } else {
                contentHolder = null;
            }
            decorView.cancelTransitions();
            this.mIsShowing = false;
            this.mIsTransitioningToDismiss = true;
            Transition exitTransition = this.mExitTransition;
            if (exitTransition == null || !decorView.isLaidOut()) {
                dismissImmediate(decorView, contentHolder, contentView);
            } else {
                LayoutParams p = (LayoutParams) decorView.getLayoutParams();
                p.flags |= 16;
                p.flags |= 8;
                this.mWindowManager.updateViewLayout(decorView, p);
                final Rect epicenter = getRelativeAnchorBounds();
                exitTransition.setEpicenterCallback(new EpicenterCallback() {
                    public Rect onGetEpicenter(Transition transition) {
                        return epicenter;
                    }
                });
                decorView.startExitTransition(exitTransition, new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        PopupWindow.this.dismissImmediate(decorView, contentHolder, contentView);
                    }
                });
            }
            unregisterForScrollChanged();
            if (this.mOnDismissListener != null) {
                this.mOnDismissListener.onDismiss();
            }
        }
    }

    private Rect getRelativeAnchorBounds() {
        View anchor;
        if (this.mAnchor != null) {
            anchor = (View) this.mAnchor.get();
        } else {
            anchor = null;
        }
        View decor = this.mDecorView;
        if (anchor == null || decor == null) {
            return null;
        }
        int[] anchorLocation = anchor.getLocationOnScreen();
        int[] popupLocation = this.mDecorView.getLocationOnScreen();
        Rect bounds = new Rect(0, 0, anchor.getWidth(), anchor.getHeight());
        bounds.offset(anchorLocation[0] - popupLocation[0], anchorLocation[1] - popupLocation[1]);
        return bounds;
    }

    private void dismissImmediate(View decorView, ViewGroup contentHolder, View contentView) {
        if (decorView.getParent() != null) {
            this.mWindowManager.removeViewImmediate(decorView);
        }
        if (contentHolder != null) {
            contentHolder.removeView(contentView);
        }
        this.mDecorView = null;
        this.mBackgroundView = null;
        this.mIsTransitioningToDismiss = false;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void update() {
        if (isShowing() && this.mContentView != null) {
            LayoutParams p = (LayoutParams) this.mDecorView.getLayoutParams();
            boolean update = false;
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            if (update) {
                setLayoutDirectionFromAnchor();
                this.mWindowManager.updateViewLayout(this.mDecorView, p);
            }
        }
    }

    public void update(int width, int height) {
        LayoutParams p = (LayoutParams) this.mDecorView.getLayoutParams();
        update(p.x, p.y, width, height, false);
    }

    public void update(int x, int y, int width, int height) {
        update(x, y, width, height, false);
    }

    public void update(int x, int y, int width, int height, boolean force) {
        if (width >= 0) {
            this.mLastWidth = width;
            setWidth(width);
        }
        if (height >= 0) {
            this.mLastHeight = height;
            setHeight(height);
        }
        if (isShowing() && this.mContentView != null) {
            LayoutParams p = (LayoutParams) this.mDecorView.getLayoutParams();
            boolean update = force;
            int finalWidth = this.mWidthMode < 0 ? this.mWidthMode : this.mLastWidth;
            if (isMultiWindow()) {
                update = true;
            }
            if (!(width == -1 || p.width == finalWidth)) {
                this.mLastWidth = finalWidth;
                p.width = finalWidth;
                update = true;
            }
            int finalHeight = this.mHeightMode < 0 ? this.mHeightMode : this.mLastHeight;
            if (!(height == -1 || p.height == finalHeight)) {
                this.mLastHeight = finalHeight;
                p.height = finalHeight;
                update = true;
            }
            if (p.x != x) {
                p.x = x;
                update = true;
            }
            if (p.y != y) {
                p.y = y;
                update = true;
            }
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            if (update) {
                setLayoutDirectionFromAnchor();
                this.mWindowManager.updateViewLayout(this.mDecorView, p);
            }
        }
    }

    public void update(View anchor, int width, int height) {
        update(anchor, false, 0, 0, true, width, height);
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        update(anchor, true, xoff, yoff, true, width, height);
    }

    private void update(View anchor, boolean updateLocation, int xoff, int yoff, boolean updateDimension, int width, int height) {
        if (isShowing() && this.mContentView != null) {
            boolean z;
            WeakReference<View> oldAnchor = this.mAnchor;
            boolean needsUpdate = updateLocation && !(this.mAnchorXoff == xoff && this.mAnchorYoff == yoff);
            if (oldAnchor == null || oldAnchor.get() != anchor || (needsUpdate && !this.mIsDropdown)) {
                registerForScrollChanged(anchor, xoff, yoff, this.mAnchoredGravity);
            } else if (needsUpdate) {
                this.mAnchorXoff = xoff;
                this.mAnchorYoff = yoff;
            }
            if (updateDimension) {
                if (width == -1) {
                    width = this.mPopupWidth;
                } else {
                    this.mPopupWidth = width;
                }
                if (height == -1) {
                    height = this.mPopupHeight;
                } else {
                    this.mPopupHeight = height;
                }
            }
            LayoutParams p = (LayoutParams) this.mDecorView.getLayoutParams();
            int x = p.x;
            int y = p.y;
            if (updateLocation) {
                updateAboveAnchor(findDropDownPosition(anchor, p, xoff, yoff, this.mAnchoredGravity));
            } else {
                updateAboveAnchor(findDropDownPosition(anchor, p, this.mAnchorXoff, this.mAnchorYoff, this.mAnchoredGravity));
            }
            int i = p.x;
            int i2 = p.y;
            if (x == p.x && y == p.y) {
                z = false;
            } else {
                z = true;
            }
            update(i, i2, width, height, z);
        }
    }

    private void unregisterForScrollChanged() {
        WeakReference<View> anchorRef = this.mAnchor;
        View anchor = anchorRef == null ? null : (View) anchorRef.get();
        if (anchor != null) {
            anchor.getViewTreeObserver().removeOnScrollChangedListener(this.mOnScrollChangedListener);
        }
        this.mAnchor = null;
    }

    private void registerForScrollChanged(View anchor, int xoff, int yoff, int gravity) {
        unregisterForScrollChanged();
        this.mAnchor = new WeakReference(anchor);
        ViewTreeObserver vto = anchor.getViewTreeObserver();
        if (vto != null) {
            vto.addOnScrollChangedListener(this.mOnScrollChangedListener);
        }
        this.mAnchorXoff = xoff;
        this.mAnchorYoff = yoff;
        this.mAnchoredGravity = gravity;
    }

    public void setShowForAllUsers(boolean enabled) {
        this.mShowForAllUsers = enabled;
    }

    public void setIgnoreMultiWindowLayout(boolean enabled) {
        this.mIgnoreMultiWindowLayout = enabled;
    }

    public void setDimBehind(boolean dimBehind, float dimAmount) {
        this.mDimBehind = dimBehind;
        this.mDimAmount = dimAmount;
    }

    private boolean isMultiWindow() {
        MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
        if (mwStyle == null || mwStyle.isNormal()) {
            return false;
        }
        return true;
    }

    private boolean isCascadeTypeMultiWindow() {
        if (this.mContext != null) {
            MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
            if (mwStyle != null && mwStyle.isCascade()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSplitTypeMultiWindow() {
        if (this.mContext != null) {
            MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
            if (mwStyle != null && mwStyle.isSplit()) {
                return true;
            }
        }
        return false;
    }
}
