package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.Span;
import android.provider.Settings$System;
import android.text.MultiSelection;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.HapticPreDrawListener;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.android.internal.R;
import com.samsung.android.feature.FloatingFeature;
import java.lang.ref.WeakReference;
import java.util.List;

public class ScrollView extends FrameLayout {
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int HOVERSCROLL_DOWN = 2;
    private static final int HOVERSCROLL_HEIGHT_BOTTOM_DP = 25;
    private static final int HOVERSCROLL_HEIGHT_TOP_DP = 25;
    private static final float HOVERSCROLL_SPEED_FASTER = 3000.0f;
    private static final int HOVERSCROLL_UP = 1;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int MSG_HOVERSCROLL_MOVE = 1;
    private static final int MSG_HOVERSCROLL_MOVE_FASTER = 2;
    private static final int MSG_HOVERSCROLL_MOVE_TO_END = 3;
    private static final int MSG_QC_HIDE = 0;
    private static final int QC_BOTTOM = 4;
    private static final int QC_LEFT = 1;
    private static final int QC_RIGHT = 3;
    private static final int QC_STATE_NONE = 0;
    private static final int QC_STATE_PRESSED = 2;
    private static final int QC_STATE_SHOWN = 1;
    private static final int QC_TOP = 2;
    private static final String TAG = "ScrollView";
    private static final int THRESHOLD_RATIO_Y = 80;
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    private int HOVERSCROLL_DELAY;
    private float HOVERSCROLL_SPEED;
    private int QC_ICON_HIDE_DELAY;
    private final int SWITCH_CONTROL_FLING;
    private final float SWITCH_CONTROL_SCROLL_DURATION_DEFAULT;
    private final float SWITCH_CONTROL_SCROLL_DURATION_GAP;
    private final float SWITCH_CONTROL_SCROLL_MAX_DURATION;
    private final float SWITCH_CONTROL_SCROLL_MIN_DURATION;
    private boolean USE_SET_INTEGRATOR_HAPTIC;
    private int mActivePointerId;
    private float mAutoscrollDuration;
    private boolean mCanScroll;
    private View mChildToScrollTo;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowTop;
    @ExportedProperty(category = "layout")
    private boolean mFillViewport;
    private Span mFlingStrictSpan;
    private HapticPreDrawListener mHapticPreDrawListener;
    private boolean mHoverAreaEnter;
    private int mHoverBottomAreaHeight;
    private HoverScrollHandler mHoverHandler;
    private long mHoverRecognitionCurrentTime;
    private long mHoverRecognitionDurationTime;
    private long mHoverRecognitionStartTime;
    private int mHoverScrollDirection;
    private boolean mHoverScrollEnable;
    private int mHoverScrollSpeed;
    private long mHoverScrollStartTime;
    private long mHoverScrollTimeInterval;
    private int mHoverTopAreaHeight;
    private boolean mIgnoreDelaychildPrerssed;
    private boolean mIsBeingDragged;
    private boolean mIsHoverOverscrolled;
    private boolean mIsLayoutDirty;
    private boolean mIsQCBtnFadeInSet;
    private boolean mIsQCBtnFadeOutSet;
    private boolean mIsQCShown;
    private int mLastHapticScrollY;
    private int mLastMotionY;
    private long mLastScroll;
    private int mLastScrollY;
    private boolean mLinear;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedsHoverScroll;
    private int mNestedYOffset;
    private int mOverflingDistance;
    private int mOverscrollDistance;
    protected int mPixelThresholdY;
    private boolean mPreviousTextViewScroll;
    private Drawable mQCBtnDrawable;
    private ValueAnimator mQCBtnFadeInAnimator;
    private final Runnable mQCBtnFadeInRunnable;
    private ValueAnimator mQCBtnFadeOutAnimator;
    private final Runnable mQCBtnFadeOutRunnable;
    private Drawable mQCBtnPressedDrawable;
    private int mQCLocation;
    private Rect mQCRect;
    private int mQCstate;
    private Rect mRequestedChildRect;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private Span mScrollStrictSpan;
    private OverScroller mScroller;
    private int mSipDelta;
    private boolean mSipResizeAnimationRunning;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    private static class HoverScrollHandler extends Handler {
        private final WeakReference<ScrollView> mScrollView;

        HoverScrollHandler(ScrollView sv) {
            this.mScrollView = new WeakReference(sv);
        }

        public void handleMessage(Message msg) {
            ScrollView sv = (ScrollView) this.mScrollView.get();
            if (sv != null) {
                sv.handleMessage(msg);
            }
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int scrollPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            this.scrollPosition = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.scrollPosition);
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
        }
    }

    public void setIgnoreDelaychildPrerssedState(boolean enable) {
        this.mIgnoreDelaychildPrerssed = enable;
    }

    public void updateCustomEdgeGlow(Drawable edgeeffectCustomEdge, Drawable edgeeffectCustomGlow) {
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void setTouchSlop(int value) {
        this.mTouchSlop = value;
    }

    public ScrollView(Context context) {
        this(context, null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.scrollViewStyle);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.USE_SET_INTEGRATOR_HAPTIC = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_FRAMEWORK_ENABLE_INTEGRATOR_HAPTIC");
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mLinear = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mHoverTopAreaHeight = 0;
        this.mHoverBottomAreaHeight = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mIgnoreDelaychildPrerssed = false;
        this.mPreviousTextViewScroll = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.HOVERSCROLL_SPEED = 800.0f;
        this.HOVERSCROLL_DELAY = 15;
        this.QC_ICON_HIDE_DELAY = 30;
        this.mNeedsHoverScroll = false;
        this.SWITCH_CONTROL_FLING = 4000;
        this.SWITCH_CONTROL_SCROLL_DURATION_DEFAULT = 1.6f;
        this.SWITCH_CONTROL_SCROLL_MIN_DURATION = 0.7f;
        this.SWITCH_CONTROL_SCROLL_MAX_DURATION = 2.5f;
        this.SWITCH_CONTROL_SCROLL_DURATION_GAP = 0.3f;
        this.mSipResizeAnimationRunning = false;
        this.mCanScroll = true;
        this.mRequestedChildRect = new Rect();
        this.mSipDelta = 0;
        this.mHoverScrollSpeed = 0;
        this.mIsQCBtnFadeInSet = true;
        this.mIsQCBtnFadeOutSet = true;
        this.mQCBtnFadeInRunnable = new Runnable() {
            public void run() {
                ScrollView.this.playQCBtnFadeIn();
            }
        };
        this.mQCBtnFadeOutRunnable = new Runnable() {
            public void run() {
                ScrollView.this.playQCBtnFadeOut();
            }
        };
        this.mQCLocation = -1;
        this.mQCstate = 0;
        this.mIsQCShown = false;
        initScrollView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollView, defStyleAttr, defStyleRes);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
    }

    public boolean shouldDelayChildPressedState() {
        if (this.mIgnoreDelaychildPrerssed) {
            return false;
        }
        return true;
    }

    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        if (this.mScrollY < length) {
            return ((float) this.mScrollY) / ((float) length);
        }
        return 1.0f;
    }

    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        int span = (getChildAt(0).getBottom() - this.mScrollY) - (getHeight() - this.mPaddingBottom);
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) (MAX_SCROLL_FACTOR * ((float) (this.mBottom - this.mTop)));
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
    }

    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child);
    }

    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    public void addView(View child, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    private boolean canScroll() {
        View child = getChildAt(0);
        if (child == null) {
            return false;
        }
        if (getHeight() < (this.mPaddingTop + child.getHeight()) + this.mPaddingBottom) {
            return true;
        }
        return false;
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != this.mFillViewport) {
            this.mFillViewport = fillViewport;
            requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mFillViewport && MeasureSpec.getMode(heightMeasureSpec) != 0 && getChildCount() > 0) {
            View child = getChildAt(0);
            int height = getMeasuredHeight();
            if (child.getMeasuredHeight() < height) {
                int widthPadding;
                int heightPadding;
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                if (getContext().getApplicationInfo().targetSdkVersion >= 23) {
                    widthPadding = ((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin;
                    heightPadding = ((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin;
                } else {
                    widthPadding = this.mPaddingLeft + this.mPaddingRight;
                    heightPadding = this.mPaddingTop + this.mPaddingBottom;
                }
                child.measure(ViewGroup.getChildMeasureSpec(widthMeasureSpec, widthPadding, lp.width), MeasureSpec.makeMeasureSpec(height - heightPadding, 1073741824));
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int childCount = getChildCount();
        int contentBottom = 0;
        int range = getScrollRange();
        boolean needToScroll = MultiSelection.isNeedToScroll();
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler(this);
        }
        if (this.mHoverTopAreaHeight <= 0 || this.mHoverBottomAreaHeight <= 0) {
            this.mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
            this.mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
        }
        if (childCount != 0) {
            contentBottom = getHeight();
        }
        boolean isPossibleTooltype = event.getToolType(0) == 2;
        int action = event.getAction();
        if (action == 0) {
            if (this.mQCstate == 1) {
                if (this.mIsQCShown && this.mQCRect.contains((int) event.getX(), (int) event.getY())) {
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    if (this.mHoverHandler.hasMessages(0)) {
                        this.mHoverHandler.removeMessages(0);
                    }
                    this.mHoverHandler.sendEmptyMessage(2);
                    this.mQCstate = 2;
                    postInvalidateOnAnimation();
                    return true;
                }
                this.mQCstate = 0;
            }
        } else if (action == 2) {
            if (this.mQCstate == 2) {
                return true;
            }
        } else if ((action == 1 || action == 3) && this.mQCstate == 2) {
            if (this.mHoverHandler.hasMessages(1)) {
                this.mHoverHandler.removeMessages(1);
            }
            if (this.mHoverHandler.hasMessages(2)) {
                this.mHoverHandler.removeMessages(2);
            }
            this.mIsHoverOverscrolled = false;
            this.mQCstate = 0;
            postInvalidateOnAnimation();
            return true;
        }
        if ((y <= this.mHoverTopAreaHeight || y >= contentBottom - this.mHoverBottomAreaHeight) && x > 0 && x <= getRight() && range != 0 && isPossibleTooltype && event.getButtonState() == 32) {
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case 212:
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverScrollStartTime = 0;
                    this.mIsHoverOverscrolled = false;
                    this.mHoverAreaEnter = false;
                    return super.dispatchTouchEvent(event);
                case 213:
                    if (needToScroll) {
                        if (y >= 0 && y <= this.mHoverTopAreaHeight) {
                            if (!this.mHoverAreaEnter) {
                                this.mHoverAreaEnter = true;
                                this.mHoverScrollStartTime = System.currentTimeMillis();
                            }
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                this.mHoverScrollDirection = 2;
                                this.mHoverHandler.sendEmptyMessage(1);
                            }
                        } else if (y < contentBottom - this.mHoverBottomAreaHeight || y > contentBottom) {
                            this.mHoverScrollStartTime = 0;
                            this.mHoverRecognitionStartTime = 0;
                            this.mHoverAreaEnter = false;
                            if (this.mHoverHandler.hasMessages(1)) {
                                this.mHoverHandler.removeMessages(1);
                            }
                            this.mIsHoverOverscrolled = false;
                        } else {
                            if (!this.mHoverAreaEnter) {
                                this.mHoverAreaEnter = true;
                                this.mHoverScrollStartTime = System.currentTimeMillis();
                            }
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                this.mHoverScrollDirection = 1;
                                this.mHoverHandler.sendEmptyMessage(1);
                            }
                        }
                    } else if (this.mPreviousTextViewScroll && this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    this.mPreviousTextViewScroll = needToScroll;
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
        if (this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        this.mIsHoverOverscrolled = false;
        return super.dispatchTouchEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        if (canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                switch (event.getKeyCode()) {
                    case 19:
                        if (event.isAltPressed()) {
                            handled = fullScroll(33);
                        } else {
                            handled = arrowScroll(33);
                        }
                        if (handled) {
                            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(33));
                            break;
                        }
                        break;
                    case 20:
                        if (event.isAltPressed()) {
                            handled = fullScroll(130);
                        } else {
                            handled = arrowScroll(130);
                        }
                        if (handled) {
                            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(130));
                            break;
                        }
                        break;
                    case 62:
                        pageScroll(event.isShiftPressed() ? 33 : 130);
                        break;
                }
            }
            return handled;
        } else if (!isFocused() || event.getKeyCode() == 4) {
            return false;
        } else {
            View currentFocused = findFocus();
            if (currentFocused == this) {
                currentFocused = null;
            }
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 130);
            if (nextFocused == null || nextFocused == this || !nextFocused.requestFocus(130)) {
                return false;
            }
            return true;
        }
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = this.mScrollY;
        View child = getChildAt(0);
        if (y < child.getTop() - scrollY || y >= child.getBottom() - scrollY || x < child.getLeft() || x >= child.getRight()) {
            return false;
        }
        return true;
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean z = false;
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        if (getScrollY() == 0 && !canScrollVertically(1)) {
            return false;
        }
        int y;
        switch (action & 255) {
            case 0:
                y = (int) ev.getY();
                if (!inChild((int) ev.getX(), y)) {
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }
                this.mLastMotionY = y;
                if (this.USE_SET_INTEGRATOR_HAPTIC) {
                    this.mLastHapticScrollY = y;
                }
                this.mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                if (!this.mScroller.isFinished()) {
                    z = true;
                }
                this.mIsBeingDragged = z;
                if (this.mIsBeingDragged && this.mScrollStrictSpan == null) {
                    this.mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
                }
                startNestedScroll(2);
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange())) {
                    postInvalidateOnAnimation();
                }
                stopNestedScroll();
                break;
            case 2:
                int activePointerId = this.mActivePointerId;
                if (activePointerId != -1) {
                    int pointerIndex = ev.findPointerIndex(activePointerId);
                    if (pointerIndex != -1) {
                        y = (int) ev.getY(pointerIndex);
                        if (Math.abs(y - this.mLastMotionY) > this.mTouchSlop && (getNestedScrollAxes() & 2) == 0) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionY = y;
                            initVelocityTrackerIfNotExists();
                            this.mVelocityTracker.addMovement(ev);
                            this.mNestedYOffset = 0;
                            if (this.mScrollStrictSpan == null) {
                                this.mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
                            }
                            ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                                break;
                            }
                        }
                    }
                    Log.e(TAG, "Invalid pointerId=" + activePointerId + " in onInterceptTouchEvent");
                    break;
                }
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return this.mIsBeingDragged;
    }

    public boolean isLockScreenMode() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        boolean isLockState = ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
        context = this.mContext;
        boolean isCoverOpen = true;
        if (Stub.asInterface(ServiceManager.getService("window")) != null) {
            isCoverOpen = true;
        }
        return isLockState || !isCoverOpen;
    }

    public void setHoverScrollMode(boolean flag) {
        if (flag) {
            this.mHoverScrollEnable = true;
        } else {
            this.mHoverScrollEnable = false;
        }
    }

    public void setHoverScrollSpeed(int hoverspeed) {
        this.HOVERSCROLL_SPEED = (float) (hoverspeed + 23);
    }

    public void setHoverScrollDelay(int hoverdelay) {
        this.HOVERSCROLL_DELAY = hoverdelay;
    }

    protected boolean dispatchHoverEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 9) {
            int toolType = ev.getToolType(0);
            this.mNeedsHoverScroll = true;
            if (!(isHoveringUIEnabled() && this.mHoverScrollEnable)) {
                this.mNeedsHoverScroll = false;
            }
            if (this.mNeedsHoverScroll && toolType == 2) {
                boolean isHoveringOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING, 0) == 1;
                boolean isHoverListScrollOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_LIST_SCROLL, 0) == 1;
                if (!(isHoveringOn && isHoverListScrollOn)) {
                    this.mNeedsHoverScroll = false;
                }
            }
            if (this.mNeedsHoverScroll && toolType == 3) {
                this.mNeedsHoverScroll = false;
            }
        }
        if (!this.mNeedsHoverScroll) {
            return super.dispatchHoverEvent(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        int contentBottom = 0;
        int range = getScrollRange();
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler(this);
        }
        if (this.mHoverTopAreaHeight <= 0 || this.mHoverBottomAreaHeight <= 0) {
            this.mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
            this.mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
        }
        if (childCount != 0) {
            contentBottom = getHeight();
        }
        boolean isPossibleTooltype = ev.getToolType(0) == 2;
        if ((y <= this.mHoverTopAreaHeight || y >= contentBottom - this.mHoverBottomAreaHeight) && x > 0 && x <= getRight() && range != 0 && ((y < 0 || y > this.mHoverTopAreaHeight || this.mScrollY > 0 || !this.mIsHoverOverscrolled) && ((y < contentBottom - this.mHoverBottomAreaHeight || y > contentBottom || this.mScrollY < range || !this.mIsHoverOverscrolled) && !((isPossibleTooltype && ev.getButtonState() == 32) || !isPossibleTooltype || isLockScreenMode())))) {
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case 7:
                    if (this.mHoverAreaEnter) {
                        if (y >= 0 && y <= this.mHoverTopAreaHeight) {
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 1) {
                                    showPointerIcon(11);
                                }
                                this.mHoverScrollDirection = 2;
                                this.mHoverHandler.sendEmptyMessage(1);
                                if (isQCSupported() && this.mQCstate == 0 && canScrollVertically(-1)) {
                                    setupQuickController(2);
                                    this.mQCstate = 1;
                                    break;
                                }
                            }
                        } else if (y >= contentBottom - this.mHoverBottomAreaHeight && y <= contentBottom && !this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 2) {
                                showPointerIcon(15);
                            }
                            this.mHoverScrollDirection = 1;
                            this.mHoverHandler.sendEmptyMessage(1);
                            if (isQCSupported() && this.mQCstate == 0 && canScrollVertically(1)) {
                                setupQuickController(4);
                                this.mQCstate = 1;
                                break;
                            }
                        }
                    }
                    this.mHoverAreaEnter = true;
                    ev.setAction(10);
                    return super.dispatchHoverEvent(ev);
                    break;
                case 9:
                    this.mHoverAreaEnter = true;
                    if (y >= 0 && y <= this.mHoverTopAreaHeight) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            showPointerIcon(11);
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                            if (isQCSupported() && canScrollVertically(-1)) {
                                setupQuickController(2);
                                this.mQCstate = 1;
                                break;
                            }
                        }
                    } else if (y >= contentBottom - this.mHoverBottomAreaHeight && y <= contentBottom && !this.mHoverHandler.hasMessages(1)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        showPointerIcon(15);
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(1);
                        if (isQCSupported() && canScrollVertically(1)) {
                            setupQuickController(4);
                            this.mQCstate = 1;
                            break;
                        }
                    }
                    break;
                case 10:
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    showPointerIcon(1);
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverScrollStartTime = 0;
                    this.mIsHoverOverscrolled = false;
                    this.mHoverAreaEnter = false;
                    if (this.mQCstate == 1) {
                        this.mHoverHandler.sendEmptyMessageDelayed(0, (long) this.QC_ICON_HIDE_DELAY);
                    }
                    return super.dispatchHoverEvent(ev);
            }
            return true;
        }
        if (this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
            showPointerIcon(1);
        }
        if (this.mHoverHandler.hasMessages(2)) {
            this.mHoverHandler.removeMessages(2);
        }
        if ((y > this.mHoverTopAreaHeight && y < contentBottom - this.mHoverBottomAreaHeight) || x <= 0 || x > getRight()) {
            this.mIsHoverOverscrolled = false;
        }
        if (this.mHoverAreaEnter || this.mHoverScrollStartTime != 0) {
            showPointerIcon(1);
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        if (this.mQCstate != 0) {
            this.mQCstate = 0;
            postInvalidateOnAnimation();
        }
        return super.dispatchHoverEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        MotionEvent vtev = MotionEvent.obtain(ev);
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 0) {
            this.mNestedYOffset = 0;
        }
        vtev.offsetLocation(0.0f, (float) this.mNestedYOffset);
        ViewParent parent;
        switch (actionMasked) {
            case 0:
                if (getChildCount() != 0) {
                    boolean z = !this.mScroller.isFinished();
                    this.mIsBeingDragged = z;
                    if (z) {
                        parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                        if (this.mFlingStrictSpan != null) {
                            this.mFlingStrictSpan.finish();
                            this.mFlingStrictSpan = null;
                        }
                    }
                    this.mLastMotionY = (int) ev.getY();
                    this.mActivePointerId = ev.getPointerId(0);
                    startNestedScroll(2);
                    break;
                }
                return false;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                    if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                        flingWithNestedDispatch(-initialVelocity);
                    } else if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                    break;
                }
                break;
            case 2:
                int activePointerIndex = ev.findPointerIndex(this.mActivePointerId);
                if (activePointerIndex != -1) {
                    int y = (int) ev.getY(activePointerIndex);
                    int deltaY = this.mLastMotionY - y;
                    if (dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed, this.mScrollOffset)) {
                        deltaY -= this.mScrollConsumed[1];
                        vtev.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                        this.mNestedYOffset += this.mScrollOffset[1];
                    }
                    if (!this.mIsBeingDragged && Math.abs(deltaY) > this.mTouchSlop) {
                        parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        if (deltaY > 0) {
                            deltaY -= this.mTouchSlop;
                        } else {
                            deltaY += this.mTouchSlop;
                        }
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = y - this.mScrollOffset[1];
                        int oldY = this.mScrollY;
                        int range = getScrollRange();
                        int overscrollMode = getOverScrollMode();
                        boolean canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && range > 0);
                        if (!overScrollBy(0, deltaY, 0, this.mScrollY, 0, range, 0, this.mOverscrollDistance, true) || hasNestedScrollingParent()) {
                            if (this.USE_SET_INTEGRATOR_HAPTIC) {
                                hapticScrollTo(y);
                            }
                        } else if (this.mVelocityTracker != null) {
                            this.mVelocityTracker.clear();
                        }
                        int scrolledDeltaY = this.mScrollY - oldY;
                        if (!dispatchNestedScroll(0, scrolledDeltaY, 0, deltaY - scrolledDeltaY, this.mScrollOffset)) {
                            if (canOverscroll) {
                                int pulledToY = oldY + deltaY;
                                if (pulledToY < 0) {
                                    this.mEdgeGlowTop.onPull(((float) deltaY) / ((float) getHeight()), ev.getX(activePointerIndex) / ((float) getWidth()));
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                } else if (pulledToY > range) {
                                    this.mEdgeGlowBottom.onPull(((float) deltaY) / ((float) getHeight()), 1.0f - (ev.getX(activePointerIndex) / ((float) getWidth())));
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                }
                                if (!(this.mEdgeGlowTop == null || (this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished()))) {
                                    postInvalidateOnAnimation();
                                    break;
                                }
                            }
                        }
                        this.mLastMotionY -= this.mScrollOffset[1];
                        vtev.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                        this.mNestedYOffset += this.mScrollOffset[1];
                        break;
                    }
                }
                this.mActivePointerId = ev.getPointerId(0);
                Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                break;
                break;
            case 3:
                if (this.mIsBeingDragged && getChildCount() > 0) {
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                    break;
                }
            case 5:
                int index = ev.getActionIndex();
                this.mLastMotionY = (int) ev.getY(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                if (this.mActivePointerId != -1) {
                    if (ev.findPointerIndex(this.mActivePointerId) >= 0) {
                        this.mLastMotionY = (int) ev.getY(ev.findPointerIndex(this.mActivePointerId));
                        break;
                    }
                }
                Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                break;
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionY = (int) ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    protected void showEdgeEffectIfNecessary(int deltaY, float multiplicator) {
        boolean canOverscroll = true;
        int overscrollMode = getOverScrollMode();
        int range = getScrollRange();
        if (overscrollMode != 0 && (overscrollMode != 1 || range <= 0)) {
            canOverscroll = false;
        }
        int newScrollY = this.mScrollY + deltaY;
        if (canOverscroll) {
            if (newScrollY < 0) {
                Log.d(TAG, "showBlueLightIfNecessary() reached the TOP!");
                this.mEdgeGlowTop.onPull((((float) deltaY) * multiplicator) / ((float) getHeight()));
                if (!this.mEdgeGlowBottom.isFinished()) {
                    this.mEdgeGlowBottom.onRelease();
                }
            } else if (newScrollY > range) {
                Log.d(TAG, "showBlueLightIfNecessary() reached the BOTTOM!");
                this.mEdgeGlowBottom.onPull((((float) deltaY) * multiplicator) / ((float) getHeight()));
                if (!this.mEdgeGlowTop.isFinished()) {
                    this.mEdgeGlowTop.onRelease();
                }
            }
            if (this.mEdgeGlowTop == null) {
                return;
            }
            if (!this.mEdgeGlowBottom.isFinished() || !this.mEdgeGlowTop.isFinished()) {
                invalidate();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if (!this.mIsBeingDragged) {
                        float vscroll = event.getAxisValue(9);
                        if (vscroll != 0.0f) {
                            int delta = (int) (getVerticalScrollFactor() * vscroll);
                            int range = getScrollRange();
                            int oldScrollY = this.mScrollY;
                            int newScrollY = oldScrollY - delta;
                            if (newScrollY < 0) {
                                newScrollY = 0;
                            } else if (newScrollY > range) {
                                newScrollY = range;
                            }
                            if (newScrollY != oldScrollY) {
                                super.scrollTo(this.mScrollX, newScrollY);
                                return true;
                            }
                        }
                    }
                    break;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScroller.isFinished()) {
            super.scrollTo(scrollX, scrollY);
        } else {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = scrollX;
            this.mScrollY = scrollY;
            invalidateParentIfNeeded();
            onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (clampedY) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange());
            }
        }
        awakenScrollBars();
    }

    protected void hapticScrollTo(int y) {
        if (Math.abs(y - this.mLastHapticScrollY) > this.mPixelThresholdY && this.mLastScrollY != this.mScrollY) {
            this.mLastHapticScrollY = y;
            this.mLastScrollY = this.mScrollY;
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        int targetScrollY;
        switch (action) {
            case 4096:
            case R.id.accessibilityActionScrollDown /*16908346*/:
                targetScrollY = Math.min(this.mScrollY + ((getHeight() - this.mPaddingBottom) - this.mPaddingTop), getScrollRange());
                if (targetScrollY == this.mScrollY) {
                    return false;
                }
                smoothScrollTo(0, targetScrollY);
                return true;
            case 8192:
            case R.id.accessibilityActionScrollUp /*16908344*/:
                targetScrollY = Math.max(this.mScrollY - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop), 0);
                if (targetScrollY == this.mScrollY) {
                    return false;
                }
                smoothScrollTo(0, targetScrollY);
                return true;
            case 4194304:
                Log.d(TAG, "ACTION_AUTOSCROLL_ON: auto scroll, canScroll = " + canScroll());
                if (!canScroll()) {
                    return false;
                }
                this.mAutoscrollDuration = 1.6f;
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            case 8388608:
                Log.d(TAG, "ACTION_AUTOSCROLL_OFF");
                if (!canScroll()) {
                    return false;
                }
                if (this.mScroller == null || this.mScroller.isFinished()) {
                    return true;
                }
                this.mScroller.abortAnimation();
                return true;
            case 16777216:
                Log.d(TAG, "ACTION_AUTOSCROLL_UP: CASE auto scroll, canScroll = " + canScroll());
                if (!canScroll()) {
                    return false;
                }
                fling(-4000);
                return true;
            case 33554432:
                Log.d(TAG, "ACTION_AUTOSCROLL_DOWN: CASE auto scroll, canScroll = " + canScroll());
                if (!canScroll()) {
                    return false;
                }
                fling(4000);
                return true;
            case 67108864:
                Log.d(TAG, "ACTION_AUTOSCROLL_TOP");
                if (!canScroll()) {
                    return false;
                }
                smoothScrollToWithDuration(0, 0, 0);
                return true;
            case 268435456:
                Log.d(TAG, "ACTION_AUTOSCROLL_SPEED_UP: speed up, current duration = " + this.mAutoscrollDuration);
                if (!canScroll()) {
                    return false;
                }
                if (this.mAutoscrollDuration > 0.7f) {
                    this.mAutoscrollDuration -= 0.3f;
                }
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            case 536870912:
                Log.d(TAG, "ACTION_AUTOSCROLL_SPEED_DOWN: CASE slow down, current duration = " + this.mAutoscrollDuration);
                if (!canScroll()) {
                    return false;
                }
                if (this.mAutoscrollDuration < 2.5f) {
                    this.mAutoscrollDuration += 0.3f;
                }
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            default:
                return false;
        }
    }

    private void autoScrollWithDuration(float duration) {
        int tempdur = (int) (((float) (getScrollRange() - this.mScrollY)) * duration);
        this.mLinear = true;
        Log.d(TAG, "autoScrollWithDuration() duration = " + tempdur);
        smoothScrollByWithDuration(0, getScrollRange(), tempdur);
        this.mLinear = false;
        this.mScroller.setInterpolator(null);
    }

    public CharSequence getAccessibilityClassName() {
        return ScrollView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            int scrollRange = getScrollRange();
            if (scrollRange > 0) {
                info.setScrollable(true);
                if (this.mScrollY > 0) {
                    info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                    info.addAction(AccessibilityAction.ACTION_SCROLL_UP);
                }
                if (this.mScrollY < scrollRange) {
                    info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                    info.addAction(AccessibilityAction.ACTION_SCROLL_DOWN);
                }
            }
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setScrollable(getScrollRange() > 0);
        event.setScrollX(this.mScrollX);
        event.setScrollY(this.mScrollY);
        event.setMaxScrollX(this.mScrollX);
        event.setMaxScrollY(getScrollRange());
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
        }
        return 0;
    }

    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if (top < viewBottom && viewTop < bottom) {
                boolean viewIsFullyContained = top < viewTop && viewBottom < bottom;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToBoundary = (topFocus && viewTop < focusCandidate.getTop()) || (!topFocus && viewBottom > focusCandidate.getBottom());
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean pageScroll(int direction) {
        boolean down;
        if (direction == 130) {
            down = true;
        } else {
            down = false;
        }
        int height = getHeight();
        if (down) {
            this.mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                if (this.mTempRect.top + height > view.getBottom()) {
                    this.mTempRect.top = view.getBottom() - height;
                }
            }
        } else {
            this.mTempRect.top = getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        this.mTempRect.bottom = this.mTempRect.top + height;
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int direction) {
        boolean down;
        if (direction == 130) {
            down = true;
        } else {
            down = false;
        }
        int height = getHeight();
        this.mTempRect.top = 0;
        this.mTempRect.bottom = height;
        if (down) {
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                this.mTempRect.bottom = view.getBottom() + this.mPaddingBottom;
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
        }
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int direction, int top, int bottom) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == 33;
        View newFocused = findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if (top < containerTop || bottom > containerBottom) {
            doScrollY(up ? top - containerTop : bottom - containerBottom);
        } else {
            handled = false;
        }
        if (newFocused != findFocus()) {
            newFocused.requestFocus(direction);
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJump = getMaxScrollAmount();
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJump, getHeight())) {
            int scrollDelta = maxJump;
            if (direction == 33 && getScrollY() < scrollDelta) {
                scrollDelta = getScrollY();
            } else if (direction == 130 && getChildCount() > 0) {
                int daBottom = getChildAt(0).getBottom();
                int screenBottom = (getScrollY() + getHeight()) - this.mPaddingBottom;
                if (daBottom - screenBottom < maxJump) {
                    scrollDelta = daBottom - screenBottom;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            int i;
            if (direction == 130) {
                i = scrollDelta;
            } else {
                i = -scrollDelta;
            }
            doScrollY(i);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocused.requestFocus(direction);
        }
        if (currentFocused != null && currentFocused.isFocused() && isOffScreen(currentFocused)) {
            int descendantFocusability = getDescendantFocusability();
            setDescendantFocusability(131072);
            requestFocus();
            setDescendantFocusability(descendantFocusability);
        }
        return true;
    }

    private boolean isOffScreen(View descendant) {
        return !isWithinDeltaOfScreen(descendant, 0, getHeight());
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.bottom + delta >= getScrollY() && this.mTempRect.top - delta <= getScrollY() + height;
    }

    private void doScrollY(int delta) {
        if (delta == 0) {
            return;
        }
        if (!this.mSmoothScrollingEnabled || this.mSipResizeAnimationRunning) {
            scrollBy(0, delta);
        } else {
            smoothScrollBy(0, delta);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int maxY = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
                int scrollY = this.mScrollY;
                this.mScroller.startScroll(this.mScrollX, scrollY, 0, Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY);
                postInvalidateOnAnimation();
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    if (this.mFlingStrictSpan != null) {
                        this.mFlingStrictSpan.finish();
                        this.mFlingStrictSpan = null;
                    }
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollByWithDuration(int dx, int dy, int scrollDuration) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int maxY = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
                int scrollY = this.mScrollY;
                dy = Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY;
                this.mScroller.setInterpolator(this.mLinear ? sLinearInterpolator : null);
                this.mScroller.startScroll(this.mScrollX, scrollY, 0, dy, scrollDuration);
                postInvalidateOnAnimation();
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    if (this.mFlingStrictSpan != null) {
                        this.mFlingStrictSpan.finish();
                        this.mFlingStrictSpan = null;
                    }
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - this.mScrollX, y - this.mScrollY);
    }

    public final void smoothScrollToWithDuration(int x, int y, int scrollDuration) {
        smoothScrollByWithDuration(x - this.mScrollX, y - this.mScrollY, scrollDuration);
    }

    protected int computeVerticalScrollRange() {
        int contentHeight = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
        if (getChildCount() == 0) {
            return contentHeight;
        }
        int scrollRange = getChildAt(0).getBottom();
        int scrollY = this.mScrollY;
        int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }
        return scrollRange;
    }

    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        child.measure(ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, child.getLayoutParams().width), MeasureSpec.makeSafeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec), 0));
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, (((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width), MeasureSpec.makeSafeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec), 0));
    }

    public void computeScroll() {
        boolean canOverscroll = true;
        if (this.mScroller.computeScrollOffset()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                int range = getScrollRange();
                int overscrollMode = getOverScrollMode();
                if (overscrollMode != 0 && (overscrollMode != 1 || range <= 0)) {
                    canOverscroll = false;
                }
                if (this.mSipResizeAnimationRunning) {
                    super.scrollTo(x, y);
                } else {
                    overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range, 0, this.mOverflingDistance, false);
                }
                onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll && this.mEdgeGlowTop != null) {
                    if (y < 0 && oldY >= 0) {
                        this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (y > range && oldY <= range) {
                        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        } else if (this.mFlingStrictSpan != null) {
            this.mFlingStrictSpan.finish();
            this.mFlingStrictSpan = null;
        }
    }

    private void scrollToChild(View child) {
        if (child != null) {
            child.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(child, this.mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            if (scrollDelta != 0) {
                scrollBy(0, scrollDelta);
            }
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        boolean scroll;
        int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        if (delta != 0) {
            scroll = true;
        } else {
            scroll = false;
        }
        if (scroll) {
            if (immediate || this.mSipResizeAnimationRunning) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        int childHeight = getChildAt(0).getHeight();
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta;
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollYDelta = 0 + (rect.top - screenTop);
            } else {
                scrollYDelta = 0 + (rect.bottom - screenBottom);
            }
            return Math.min(scrollYDelta, getChildAt(0).getBottom() - screenBottom);
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            if (rect.height() > height) {
                scrollYDelta = 0 - (screenBottom - rect.bottom);
            } else {
                scrollYDelta = 0 - (screenTop - rect.top);
            }
            return Math.max(scrollYDelta, -getScrollY());
        } else if (!this.mSipResizeAnimationRunning || screenBottom <= childHeight) {
            return 0;
        } else {
            return screenBottom - childHeight;
        }
    }

    public void requestChildFocus(View child, View focused) {
        if (this.mIsLayoutDirty) {
            this.mChildToScrollTo = focused;
        } else {
            scrollToChild(focused);
        }
        super.requestChildFocus(child, focused);
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (direction == 2) {
            direction = 130;
        } else if (direction == 1) {
            direction = 33;
        }
        View nextFocus = previouslyFocusedRect == null ? FocusFinder.getInstance().findNextFocus(this, null, direction) : FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null || isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        if (this.mSipResizeAnimationRunning) {
            if (rectangle.top > (getHeight() + getScrollY()) + this.mSipDelta) {
                this.mCanScroll = false;
                this.mRequestedChildRect.set(rectangle);
                return true;
            }
            this.mCanScroll = true;
        }
        return scrollToChildRect(rectangle, immediate);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mScrollStrictSpan != null) {
            this.mScrollStrictSpan.finish();
            this.mScrollStrictSpan = null;
        }
        if (this.mFlingStrictSpan != null) {
            this.mFlingStrictSpan.finish();
            this.mFlingStrictSpan = null;
        }
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            getViewTreeObserver().removeOnPreDrawListener(this.mHapticPreDrawListener);
        }
        if (this.mQCstate != 0) {
            this.mQCstate = 0;
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus && this.mQCstate != 0) {
            this.mQCstate = 0;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!isLaidOut()) {
            if (this.mSavedState != null) {
                this.mScrollY = this.mSavedState.scrollPosition;
                this.mSavedState = null;
            }
            int scrollRange = Math.max(0, (getChildCount() > 0 ? getChildAt(0).getMeasuredHeight() : 0) - (((b - t) - this.mPaddingBottom) - this.mPaddingTop));
            if (this.mScrollY > scrollRange) {
                this.mScrollY = scrollRange;
            } else if (this.mScrollY < 0) {
                this.mScrollY = 0;
            }
        }
        scrollTo(this.mScrollX, this.mScrollY);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int i = 0;
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = findFocus();
        if (currentFocused != null && this != currentFocused) {
            if (this.mSipResizeAnimationRunning || isWithinDeltaOfScreen(currentFocused, 0, oldh)) {
                int i2 = this.mSipDelta;
                if (oldh > h) {
                    i = oldh - h;
                }
                this.mSipDelta = i + i2;
                if (this.mSipResizeAnimationRunning) {
                    currentFocused.requestLayout();
                }
                if (this.mCanScroll) {
                    currentFocused.getDrawingRect(this.mTempRect);
                    offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
                    doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
                }
            }
        }
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if ((theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent)) {
            return true;
        }
        return false;
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
            int i = velocityY;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            this.mScroller.fling(this.mScrollX, this.mScrollY, 0, i, i2, i3, i4, Math.max(0, getChildAt(0).getHeight() - height), i5, height / 2);
            if (this.mFlingStrictSpan == null) {
                this.mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
            }
            postInvalidateOnAnimation();
        }
    }

    private void flingWithoutAcc(int velocityY) {
        Log.d(TAG, "flingWithoutAcc() velocityY = " + velocityY);
        if (getChildCount() > 0) {
            int height = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
            int i = velocityY;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            this.mScroller.fling(this.mScrollX, this.mScrollY, 0, i, i2, i3, i4, Math.max(0, getChildAt(0).getHeight() - height), i5, height / 2, true);
            if (this.mFlingStrictSpan == null) {
                this.mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
            }
            postInvalidateOnAnimation();
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        boolean canFling = (this.mScrollY > 0 || velocityY > 0) && (this.mScrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0.0f, (float) velocityY)) {
            dispatchNestedFling(0.0f, (float) velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        recycleVelocityTracker();
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
        if (this.mScrollStrictSpan != null) {
            this.mScrollStrictSpan.finish();
            this.mScrollStrictSpan = null;
        }
    }

    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, (getWidth() - this.mPaddingRight) - this.mPaddingLeft, child.getWidth());
            y = clamp(y, (getHeight() - this.mPaddingBottom) - this.mPaddingTop, child.getHeight());
            if (x != this.mScrollX || y != this.mScrollY) {
                if (this.USE_SET_INTEGRATOR_HAPTIC) {
                    hapticScrollTo(y);
                }
                super.scrollTo(x, y);
            }
        }
    }

    public void setOverScrollMode(int mode) {
        if (mode == 2) {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        } else if (this.mEdgeGlowTop == null) {
            Context context = getContext();
            this.mEdgeGlowTop = new EdgeEffect(context);
            this.mEdgeGlowBottom = new EdgeEffect(context);
        }
        super.setOverScrollMode(mode);
    }

    protected void onAttachedToWindow() {
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            getViewTreeObserver().addOnPreDrawListener(this.mHapticPreDrawListener);
        }
        super.onAttachedToWindow();
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(2);
    }

    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int oldScrollY = this.mScrollY;
        scrollBy(0, dyUnconsumed);
        int myConsumed = this.mScrollY - oldScrollY;
        dispatchNestedScroll(0, myConsumed, 0, dyUnconsumed - myConsumed, null);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (consumed) {
            return false;
        }
        flingWithNestedDispatch((int) velocityY);
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            int restoreCount;
            int width;
            int height;
            float translateX;
            float translateY;
            int scrollY = this.mScrollY;
            boolean clipToPadding = getClipToPadding();
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                if (clipToPadding) {
                    width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                    height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                    translateX = (float) this.mPaddingLeft;
                    translateY = (float) this.mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0.0f;
                    translateY = 0.0f;
                }
                canvas.translate(translateX, ((float) Math.min(0, scrollY)) + translateY);
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                if (clipToPadding) {
                    width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                    height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                    translateX = (float) this.mPaddingLeft;
                    translateY = (float) this.mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0.0f;
                    translateY = 0.0f;
                }
                canvas.translate(((float) (-width)) + translateX, ((float) (Math.max(getScrollRange(), scrollY) + height)) + translateY);
                canvas.rotate(180.0f, (float) width, 0.0f);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
        drawQuickController(canvas);
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if (my + n > child) {
            return child - my;
        }
        return n;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (this.mContext.getApplicationInfo().targetSdkVersion <= 18 || !(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mSavedState = ss;
        requestLayout();
    }

    protected Parcelable onSaveInstanceState() {
        if (this.mContext.getApplicationInfo().targetSdkVersion <= 18) {
            return super.onSaveInstanceState();
        }
        Parcelable ss = new SavedState(super.onSaveInstanceState());
        ss.scrollPosition = this.mScrollY;
        return ss;
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("fillViewport", this.mFillViewport);
    }

    private void handleMessage(Message msg) {
        int overscrollMode;
        boolean canOverscroll;
        switch (msg.what) {
            case 0:
                this.mQCstate = 0;
                postInvalidateOnAnimation();
                return;
            case 1:
                int range = getScrollRange();
                this.mHoverRecognitionCurrentTime = System.currentTimeMillis();
                this.mHoverRecognitionDurationTime = (this.mHoverRecognitionCurrentTime - this.mHoverRecognitionStartTime) / 1000;
                if (this.mHoverRecognitionCurrentTime - this.mHoverScrollStartTime >= this.mHoverScrollTimeInterval) {
                    int offset;
                    this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, this.HOVERSCROLL_SPEED, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
                    if (this.mHoverRecognitionDurationTime > 2 && this.mHoverRecognitionDurationTime < 4) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.1d);
                    } else if (this.mHoverRecognitionDurationTime >= 4 && this.mHoverRecognitionDurationTime < 5) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.2d);
                    } else if (this.mHoverRecognitionDurationTime >= 5) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.3d);
                    }
                    if (this.mHoverScrollDirection == 2) {
                        offset = this.mHoverScrollSpeed * -1;
                    } else {
                        offset = this.mHoverScrollSpeed * 1;
                    }
                    if (offset < 0 && this.mScrollY > 0) {
                        flingWithoutAcc(offset);
                        this.mHoverHandler.sendEmptyMessageDelayed(1, (long) this.HOVERSCROLL_DELAY);
                        return;
                    } else if (offset <= 0 || this.mScrollY >= range) {
                        overscrollMode = getOverScrollMode();
                        canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && range > 0);
                        if (canOverscroll && !this.mIsHoverOverscrolled) {
                            if (this.mEdgeGlowTop != null) {
                                if (this.mHoverScrollDirection == 2) {
                                    this.mEdgeGlowTop.setSize((getWidth() - this.mPaddingLeft) - this.mPaddingRight, getHeight());
                                    this.mEdgeGlowTop.onPull(0.4f);
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                } else if (this.mHoverScrollDirection == 1) {
                                    this.mEdgeGlowBottom.setSize((getWidth() - this.mPaddingLeft) - this.mPaddingRight, getHeight());
                                    this.mEdgeGlowBottom.onPull(0.4f);
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                }
                            }
                            if (!(this.mEdgeGlowTop == null || (this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished()))) {
                                invalidate();
                            }
                            this.mIsHoverOverscrolled = true;
                        }
                        if (!canOverscroll && !this.mIsHoverOverscrolled) {
                            this.mIsHoverOverscrolled = true;
                            return;
                        }
                        return;
                    } else {
                        flingWithoutAcc(offset);
                        this.mHoverHandler.sendEmptyMessageDelayed(1, (long) this.HOVERSCROLL_DELAY);
                        return;
                    }
                }
                return;
            case 2:
                this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, HOVERSCROLL_SPEED_FASTER, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
                int distanceToMove = this.mQCLocation == 2 ? -this.mHoverScrollSpeed : this.mHoverScrollSpeed;
                int scrollaleRange = getScrollRange();
                if ((distanceToMove >= 0 || this.mScrollY <= 0) && (distanceToMove <= 0 || this.mScrollY >= scrollaleRange)) {
                    overscrollMode = getOverScrollMode();
                    canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && scrollaleRange > 0);
                    if (canOverscroll && !this.mIsHoverOverscrolled) {
                        if (this.mEdgeGlowTop != null) {
                            if (this.mQCLocation == 2) {
                                this.mEdgeGlowTop.setSize((getWidth() - this.mPaddingLeft) - this.mPaddingRight, getHeight());
                                this.mEdgeGlowTop.onPull(0.4f);
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (this.mQCLocation == 4) {
                                this.mEdgeGlowBottom.setSize((getWidth() - this.mPaddingLeft) - this.mPaddingRight, getHeight());
                                this.mEdgeGlowBottom.onPull(0.4f);
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }
                        }
                        if (!(this.mEdgeGlowTop == null || (this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished()))) {
                            invalidate();
                        }
                        this.mIsHoverOverscrolled = true;
                    }
                    if (!canOverscroll && !this.mIsHoverOverscrolled) {
                        this.mIsHoverOverscrolled = true;
                        return;
                    }
                    return;
                }
                flingWithoutAcc(distanceToMove);
                this.mHoverHandler.sendEmptyMessageDelayed(2, (long) this.HOVERSCROLL_DELAY);
                return;
            case 3:
                if (this.mQCLocation == 2) {
                    doScrollToTopEnd();
                    return;
                } else if (this.mQCLocation == 4) {
                    doScrollToBottomEnd();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private boolean showPointerIcon(int iconId) {
        try {
            PointerIcon.setHoveringSpenIcon(iconId, -1);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to change PointerIcon to " + iconId);
            return false;
        }
    }

    private boolean isQCSupported() {
        if (sSpenUspLevel >= 3) {
            return true;
        }
        return false;
    }

    private void setupQuickController(int where) {
        int w = getWidth();
        int h = getHeight();
        int centerX = this.mPaddingLeft + (((w - this.mPaddingLeft) - this.mPaddingRight) / 2);
        int oldLocation = this.mQCLocation;
        this.mQCLocation = where;
        boolean checkBoundary = true;
        String basePkgName = this.mContext.getBasePackageName();
        if (basePkgName != null && basePkgName.contains("cocktailbarservice")) {
            checkBoundary = false;
        }
        if (checkBoundary) {
            int overlappedW;
            int[] locOnScr = new int[]{0, 0};
            getLocationOnScreen(locOnScr);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            if (locOnScr[0] < 0) {
                overlappedW = -locOnScr[0];
                if (overlappedW > this.mPaddingLeft) {
                    centerX += (overlappedW - this.mPaddingLeft) / 2;
                }
            }
            if (locOnScr[0] + w > dm.widthPixels) {
                overlappedW = (locOnScr[0] + w) - dm.widthPixels;
                if (overlappedW > this.mPaddingRight) {
                    centerX -= (overlappedW - this.mPaddingRight) / 2;
                }
            }
        }
        int btnW;
        switch (where) {
            case 1:
                this.mQCRect = new Rect(0, 0, 0, 0);
                break;
            case 2:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up_pressed);
                }
                btnW = this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size);
                this.mQCRect = new Rect(centerX - (btnW / 2), 0, (btnW / 2) + centerX, this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size));
                break;
            case 3:
                this.mQCRect = new Rect(0, 0, 0, 0);
                break;
            case 4:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down_pressed);
                }
                btnW = this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size);
                this.mQCRect = new Rect(centerX - (btnW / 2), h - this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size), (btnW / 2) + centerX, h);
                break;
        }
        this.mQCBtnDrawable.setBounds(this.mQCRect);
        this.mQCBtnPressedDrawable.setBounds(this.mQCRect);
        this.mQCBtnFadeInAnimator = ValueAnimator.ofInt(new int[]{0, 255});
        this.mQCBtnFadeInAnimator.setDuration(150);
        this.mQCBtnFadeInAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    ScrollView.this.mQCBtnDrawable.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                } catch (Exception e) {
                }
            }
        });
        this.mQCBtnFadeOutAnimator = ValueAnimator.ofInt(new int[]{0, 255});
        this.mQCBtnFadeOutAnimator.setDuration(150);
        this.mQCBtnFadeOutAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    ScrollView.this.mQCBtnDrawable.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    ScrollView.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mQCBtnFadeOutAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                try {
                    ScrollView.this.mIsQCShown = false;
                } catch (Exception e) {
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    private void drawQuickController(Canvas canvas) {
        int scrollY;
        int restoreCount;
        if (this.mQCstate == 1 || this.mQCstate == 2) {
            scrollY = this.mScrollY;
            restoreCount = canvas.save();
            canvas.translate(0.0f, (float) scrollY);
            if (this.mQCstate == 1) {
                if (this.mIsQCBtnFadeInSet) {
                    this.mQCBtnDrawable.setAlpha(0);
                    post(this.mQCBtnFadeInRunnable);
                    this.mIsQCBtnFadeInSet = false;
                }
                this.mQCBtnDrawable.draw(canvas);
            } else if (this.mQCstate == 2) {
                this.mQCBtnPressedDrawable.draw(canvas);
            }
            canvas.restoreToCount(restoreCount);
            this.mIsQCShown = true;
            this.mIsQCBtnFadeOutSet = true;
        } else if (this.mIsQCShown) {
            scrollY = this.mScrollY;
            restoreCount = canvas.save();
            canvas.translate(0.0f, (float) scrollY);
            if (this.mIsQCBtnFadeOutSet) {
                post(this.mQCBtnFadeOutRunnable);
                this.mIsQCBtnFadeOutSet = false;
            }
            this.mQCBtnDrawable.draw(canvas);
            canvas.restoreToCount(restoreCount);
            this.mIsQCBtnFadeInSet = true;
        }
    }

    private void playQCBtnFadeIn() {
        if (!this.mQCBtnFadeInAnimator.isRunning()) {
            this.mQCBtnFadeInAnimator.setIntValues(new int[]{0, 255});
            this.mQCBtnFadeInAnimator.start();
        }
    }

    private void playQCBtnFadeOut() {
        if (!this.mQCBtnFadeOutAnimator.isRunning()) {
            this.mQCBtnFadeOutAnimator.setIntValues(new int[]{this.mQCBtnDrawable.getAlpha(), 0});
            this.mQCBtnFadeOutAnimator.start();
        }
    }

    private void doScrollToTopEnd() {
        doScrollY(-computeVerticalScrollOffset());
    }

    private void doScrollToBottomEnd() {
        doScrollY(computeVerticalScrollRange());
    }

    protected boolean dispatchSipResizeAnimationState(boolean isStart) {
        this.mSipResizeAnimationRunning = isStart;
        this.mSipDelta = 0;
        requestLayout();
        if (isStart) {
            this.mRequestedChildRect.set(0, 0, 0, 0);
        } else if (!this.mCanScroll) {
            scrollToChildRect(this.mRequestedChildRect, true);
        }
        this.mCanScroll = true;
        return true;
    }

    public void twSetSmoothScrollEnable(boolean enable) {
        this.mScroller.twSetSmoothScrollEnable(enable);
    }
}
