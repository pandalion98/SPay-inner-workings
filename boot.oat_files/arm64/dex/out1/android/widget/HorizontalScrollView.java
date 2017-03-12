package android.widget;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings$System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.HapticPreDrawListener;
import android.view.IWindowManager.Stub;
import android.view.InputDevice;
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
import com.android.internal.R;
import com.android.internal.widget.ScrollingTabContainerView;
import com.samsung.android.feature.FloatingFeature;
import java.lang.ref.WeakReference;
import java.util.List;

public class HorizontalScrollView extends FrameLayout {
    private static final int ANIMATED_SCROLL_GAP = 250;
    private static final int HOVERSCROLL_LEFT = 1;
    private static final int HOVERSCROLL_RIGHT = 2;
    private static final float HOVERSCROLL_SPEED_FASTER = 30.0f;
    private static final int HOVERSCROLL_WIDTH_DP = 25;
    private static final int INVALID_POINTER = -1;
    private static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final int MSG_HOVERSCROLL_MOVE = 1;
    private static final int MSG_HOVERSCROLL_MOVE_FASTER = 2;
    private static final int MSG_HOVERSCROLL_MOVE_TO_END = 3;
    private static final int MSG_TIMEOUT = 4;
    private static final int QC_BOTTOM = 4;
    private static final int QC_LEFT = 1;
    private static final int QC_RIGHT = 3;
    private static final int QC_STATE_NONE = 0;
    private static final int QC_STATE_PRESSED = 2;
    private static final int QC_STATE_SHOWN = 1;
    private static final int QC_TOP = 2;
    private static final String TAG = "HorizontalScrollView";
    private static final int THRESHOLD_RATIO_X = 48;
    private static final int TIMEOUT_DELAY = 100;
    private int HOVERSCROLL_DELAY;
    private int HOVERSCROLL_SPEED;
    private boolean USE_SET_INTEGRATOR_HAPTIC;
    private int mActivePointerId;
    private boolean mChangedTheme;
    private View mChildToScrollTo;
    private EdgeEffect mEdgeGlowLeft;
    private EdgeEffect mEdgeGlowRight;
    @ExportedProperty(category = "layout")
    private boolean mFillViewport;
    private HapticPreDrawListener mHapticPreDrawListener;
    private boolean mHoverAreaEnter;
    private int mHoverAreaWidth;
    private HoverScrollHandler mHoverHandler;
    private long mHoverRecognitionCurrentTime;
    private long mHoverRecognitionDurationTime;
    private long mHoverRecognitionStartTime;
    private int mHoverScrollDirection;
    private boolean mHoverScrollEnable;
    private int mHoverScrollSpeed;
    private long mHoverScrollStartTime;
    private long mHoverScrollTimeInterval;
    private boolean mIsBeingDragged;
    private boolean mIsHoverOverscrolled;
    private boolean mIsLayoutDirty;
    private boolean mIsQCShown;
    private boolean mIsThemeDeviceDefaultFamily;
    private int mLastHapticScrollX;
    private int mLastMotionX;
    private long mLastScroll;
    private int mLastScrollX;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedsHoverScroll;
    private int mOverflingDistance;
    private int mOverscrollDistance;
    private int mPixelThresholdX;
    private Drawable mQCBtnDrawable;
    private Drawable mQCBtnPressedDrawable;
    private int mQCLocation;
    private Rect mQCRect;
    private int mQCstate;
    private SavedState mSavedState;
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    private boolean mUseRatioMaintainedImage;
    private VelocityTracker mVelocityTracker;

    private static class HoverScrollHandler extends Handler {
        private final WeakReference<HorizontalScrollView> mScrollView;

        HoverScrollHandler(HorizontalScrollView sv) {
            this.mScrollView = new WeakReference(sv);
        }

        public void handleMessage(Message msg) {
            HorizontalScrollView sv = (HorizontalScrollView) this.mScrollView.get();
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
        public int scrollOffsetFromStart;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            this.scrollOffsetFromStart = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.scrollOffsetFromStart);
        }

        public String toString() {
            return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollOffsetFromStart + "}";
        }
    }

    public void updateCustomEdgeGlow(Drawable edgeeffectCustomEdge, Drawable edgeeffectCustomGlow) {
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void setTouchSlop(int value) {
        this.mTouchSlop = value;
    }

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.horizontalScrollViewStyle);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.USE_SET_INTEGRATOR_HAPTIC = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_FRAMEWORK_ENABLE_INTEGRATOR_HAPTIC");
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mHoverAreaWidth = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.HOVERSCROLL_SPEED = 15;
        this.HOVERSCROLL_DELAY = 15;
        this.mNeedsHoverScroll = false;
        this.mUseRatioMaintainedImage = false;
        this.mHoverScrollSpeed = 0;
        this.mQCLocation = -1;
        this.mQCstate = 0;
        this.mIsQCShown = false;
        initScrollView();
        TypedArray a = context.obtainStyledAttributes(attrs, android.R.styleable.HorizontalScrollView, defStyleAttr, defStyleRes);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        this.mIsThemeDeviceDefaultFamily = outValue.data != 0;
        boolean z = this.mIsThemeDeviceDefaultFamily && Settings$System.getString(context.getContentResolver(), "current_sec_active_themepackage") != null && context.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_560;
        this.mChangedTheme = z;
    }

    protected float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        if (this.mScrollX < length) {
            return ((float) this.mScrollX) / ((float) length);
        }
        return 1.0f;
    }

    protected float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        int span = (getChildAt(0).getRight() - this.mScrollX) - (getWidth() - this.mPaddingRight);
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public int getMaxScrollAmount() {
        return (int) (MAX_SCROLL_FACTOR * ((float) (this.mRight - this.mLeft)));
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

    protected void onAttachedToWindow() {
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            getViewTreeObserver().addOnPreDrawListener(this.mHapticPreDrawListener);
        }
        super.onAttachedToWindow();
    }

    protected void onDetachedFromWindow() {
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            getViewTreeObserver().removeOnPreDrawListener(this.mHapticPreDrawListener);
        }
        if (this.mQCstate != 0) {
            this.mQCstate = 0;
        }
        super.onDetachedFromWindow();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus && this.mQCstate != 0) {
            this.mQCstate = 0;
        }
    }

    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        }
        super.addView(child);
    }

    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    public void addView(View child, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("HorizontalScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    private boolean canScroll() {
        View child = getChildAt(0);
        if (child == null) {
            return false;
        }
        if (getWidth() < (this.mPaddingLeft + child.getWidth()) + this.mPaddingRight) {
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
        if (this.mFillViewport && MeasureSpec.getMode(widthMeasureSpec) != 0) {
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                int width = getMeasuredWidth();
                if (child.getMeasuredWidth() < width) {
                    child.measure(MeasureSpec.makeMeasureSpec((width - this.mPaddingLeft) - this.mPaddingRight, 1073741824), ViewGroup.getChildMeasureSpec(heightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, ((FrameLayout.LayoutParams) child.getLayoutParams()).height));
                }
            }
            if (this.mChangedTheme) {
                twUpdateBackgroundBounds();
            }
        }
    }

    public void twUseRatioMaintainedImage() {
        this.mUseRatioMaintainedImage = true;
    }

    protected void twUpdateBackgroundBounds() {
        Drawable background = getBackground();
        if (background != null) {
            if (this.mChangedTheme && this.mUseRatioMaintainedImage && (background instanceof BitmapDrawable) && background.getIntrinsicWidth() < getWidth()) {
                background.setBounds(0, 0, getRight(), (int) ((((float) getWidth()) / ((float) background.getIntrinsicWidth())) * ((float) background.getIntrinsicHeight())));
                return;
            }
            super.twUpdateBackgroundBounds();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == 0) {
            if (this.mQCstate == 1) {
                if (this.mIsQCShown && this.mQCRect.contains((int) event.getX(), (int) event.getY())) {
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
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
        return super.dispatchTouchEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        if (canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                int direction = 0;
                switch (event.getKeyCode()) {
                    case 21:
                        if (event.isAltPressed()) {
                            handled = fullScroll(17);
                        } else {
                            handled = arrowScroll(17);
                        }
                        direction = 17;
                        break;
                    case 22:
                        if (event.isAltPressed()) {
                            handled = fullScroll(66);
                        } else {
                            handled = arrowScroll(66);
                        }
                        direction = 66;
                        break;
                    case 62:
                        pageScroll(event.isShiftPressed() ? 17 : 66);
                        break;
                }
                if (handled) {
                    playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                }
            }
            return handled;
        } else if (!isFocused()) {
            return false;
        } else {
            View currentFocused = findFocus();
            if (currentFocused == this) {
                currentFocused = null;
            }
            View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 66);
            if (nextFocused == null || nextFocused == this || !nextFocused.requestFocus(66)) {
                return false;
            }
            return true;
        }
    }

    private boolean inChild(int x, int y) {
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollX = this.mScrollX;
        View child = getChildAt(0);
        if (y < child.getTop() || y >= child.getBottom() || x < child.getLeft() - scrollX || x >= child.getRight() - scrollX) {
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
        int x;
        int pointerIndex;
        switch (action & 255) {
            case 0:
                x = (int) ev.getX();
                if (!inChild(x, (int) ev.getY())) {
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }
                this.mLastMotionX = x;
                if (this.USE_SET_INTEGRATOR_HAPTIC) {
                    this.mLastHapticScrollX = x;
                }
                this.mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                if (!this.mScroller.isFinished() && canScroll()) {
                    z = true;
                }
                this.mIsBeingDragged = z;
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                    postInvalidateOnAnimation();
                    break;
                }
                break;
            case 2:
                int activePointerId = this.mActivePointerId;
                if (activePointerId != -1) {
                    pointerIndex = ev.findPointerIndex(activePointerId);
                    if (pointerIndex != -1) {
                        x = (int) ev.getX(pointerIndex);
                        if (Math.abs(x - this.mLastMotionX) > this.mTouchSlop) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = x;
                            initVelocityTrackerIfNotExists();
                            this.mVelocityTracker.addMovement(ev);
                            if (this.mParent != null) {
                                this.mParent.requestDisallowInterceptTouchEvent(true);
                                break;
                            }
                        }
                    }
                    Log.e(TAG, "Invalid pointerId=" + activePointerId + " in onInterceptTouchEvent");
                    break;
                }
                break;
            case 5:
                int index = ev.getActionIndex();
                this.mLastMotionX = (int) ev.getX(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                onSecondaryPointerUp(ev);
                pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                if (pointerIndex >= 0) {
                    this.mLastMotionX = (int) ev.getX(pointerIndex);
                    break;
                }
                return true;
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
        this.HOVERSCROLL_SPEED = hoverspeed;
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
        int contentRightSide = 0;
        int range = getScrollRange();
        if (this.mHoverAreaWidth <= 0) {
            this.mHoverAreaWidth = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
        }
        if (childCount != 0) {
            contentRightSide = getWidth() - this.mPaddingBottom;
        }
        boolean isPossibleTooltype = ev.getToolType(0) == 2;
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler(this);
        }
        if ((x <= this.mHoverAreaWidth || x >= contentRightSide - this.mHoverAreaWidth) && range != 0 && ((x < 0 || x > this.mHoverAreaWidth || this.mScrollX > 0 || !this.mIsHoverOverscrolled) && ((x < contentRightSide - this.mHoverAreaWidth || x > contentRightSide || this.mScrollX < range || !this.mIsHoverOverscrolled) && !((isPossibleTooltype && ev.getButtonState() == 32) || !isPossibleTooltype || isLockScreenMode())))) {
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            if (action == 7) {
                resetTimeout();
            }
            switch (action) {
                case 7:
                    if (this.mHoverAreaEnter) {
                        if (x >= 0 && x <= this.mHoverAreaWidth) {
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 2) {
                                    showPointerIcon(17);
                                }
                                this.mHoverScrollDirection = 1;
                                this.mHoverHandler.sendEmptyMessage(1);
                                if (isQCSupported() && this.mQCstate == 0 && canScrollHorizontally(-1)) {
                                    setupQuickController(1);
                                    this.mQCstate = 1;
                                    break;
                                }
                            }
                        } else if (x >= contentRightSide - this.mHoverAreaWidth && x <= contentRightSide && !this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 1) {
                                showPointerIcon(13);
                            }
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                            if (isQCSupported() && this.mQCstate == 0 && canScrollHorizontally(1)) {
                                setupQuickController(3);
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
                    if (x >= 0 && x <= this.mHoverAreaWidth) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            showPointerIcon(17);
                            this.mHoverScrollDirection = 1;
                            this.mHoverHandler.sendEmptyMessage(1);
                            if (isQCSupported() && canScrollHorizontally(-1)) {
                                setupQuickController(1);
                                this.mQCstate = 1;
                                break;
                            }
                        }
                    } else if (x >= contentRightSide - this.mHoverAreaWidth && x <= contentRightSide && !this.mHoverHandler.hasMessages(1)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        showPointerIcon(13);
                        this.mHoverScrollDirection = 2;
                        this.mHoverHandler.sendEmptyMessage(1);
                        if (isQCSupported() && canScrollHorizontally(1)) {
                            setupQuickController(3);
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
                        InputManager im = InputManager.getInstance();
                        if (!this.mQCRect.contains(x, y) || (im != null && im.getScanCodeState(-1, InputDevice.SOURCE_ANY, 320) == 0)) {
                            this.mQCstate = 0;
                            postInvalidateOnAnimation();
                        }
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
        if (x > this.mHoverAreaWidth && x < contentRightSide - this.mHoverAreaWidth) {
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

    private void resetTimeout() {
        if (this.mHoverHandler != null) {
            if (this.mHoverHandler.hasMessages(4)) {
                this.mHoverHandler.removeMessages(4);
            }
            this.mHoverHandler.sendEmptyMessageDelayed(4, 100);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(ev);
        ViewParent parent;
        switch (ev.getAction() & 255) {
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
                    }
                    this.mLastMotionX = (int) ev.getX();
                    this.mActivePointerId = ev.getPointerId(0);
                    break;
                }
                return false;
            case 1:
                if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                    if (getChildCount() > 0) {
                        if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                            fling(-initialVelocity);
                        } else if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                            postInvalidateOnAnimation();
                        }
                    }
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    if (this.mEdgeGlowLeft != null) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
                        break;
                    }
                }
                break;
            case 2:
                int activePointerIndex = ev.findPointerIndex(this.mActivePointerId);
                if (activePointerIndex != -1) {
                    int x = (int) ev.getX(activePointerIndex);
                    int deltaX = this.mLastMotionX - x;
                    if (!this.mIsBeingDragged && Math.abs(deltaX) > this.mTouchSlop) {
                        parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                        this.mIsBeingDragged = true;
                        deltaX = deltaX > 0 ? deltaX - this.mTouchSlop : deltaX + this.mTouchSlop;
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionX = x;
                        int oldX = this.mScrollX;
                        int oldY = this.mScrollY;
                        int range = getScrollRange();
                        int overscrollMode = getOverScrollMode();
                        boolean canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && range > 0);
                        if (overScrollBy(deltaX, 0, this.mScrollX, 0, range, 0, this.mOverscrollDistance, 0, true)) {
                            this.mVelocityTracker.clear();
                        } else if (this.USE_SET_INTEGRATOR_HAPTIC) {
                            hapticScrollTo(x);
                        }
                        if (canOverscroll) {
                            int pulledToX = oldX + deltaX;
                            if (pulledToX < 0) {
                                this.mEdgeGlowLeft.onPull(((float) deltaX) / ((float) getWidth()), 1.0f - (ev.getY(activePointerIndex) / ((float) getHeight())));
                                if (!this.mEdgeGlowRight.isFinished()) {
                                    this.mEdgeGlowRight.onRelease();
                                }
                            } else if (pulledToX > range) {
                                this.mEdgeGlowRight.onPull(((float) deltaX) / ((float) getWidth()), ev.getY(activePointerIndex) / ((float) getHeight()));
                                if (!this.mEdgeGlowLeft.isFinished()) {
                                    this.mEdgeGlowLeft.onRelease();
                                }
                            }
                            if (!(this.mEdgeGlowLeft == null || (this.mEdgeGlowLeft.isFinished() && this.mEdgeGlowRight.isFinished()))) {
                                postInvalidateOnAnimation();
                                break;
                            }
                        }
                    }
                }
                this.mActivePointerId = ev.getPointerId(0);
                Log.e(TAG, "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
                break;
                break;
            case 3:
                if (this.mIsBeingDragged && getChildCount() > 0) {
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                        postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    this.mIsBeingDragged = false;
                    recycleVelocityTracker();
                    if (this.mEdgeGlowLeft != null) {
                        this.mEdgeGlowLeft.onRelease();
                        this.mEdgeGlowRight.onRelease();
                        break;
                    }
                }
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = (int) ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if (!this.mIsBeingDragged) {
                        float hscroll;
                        if ((event.getMetaState() & 1) != 0) {
                            hscroll = -event.getAxisValue(9);
                        } else {
                            hscroll = event.getAxisValue(10);
                        }
                        if (hscroll != 0.0f) {
                            int delta = (int) (getHorizontalScrollFactor() * hscroll);
                            int range = getScrollRange();
                            int oldScrollX = this.mScrollX;
                            int newScrollX = oldScrollX + delta;
                            if (newScrollX < 0) {
                                newScrollX = 0;
                            } else if (newScrollX > range) {
                                newScrollX = range;
                            }
                            if (newScrollX != oldScrollX) {
                                super.scrollTo(newScrollX, this.mScrollY);
                                return true;
                            }
                        }
                    }
                    break;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public boolean shouldDelayChildPressedState() {
        return true;
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
            if (clampedX) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0);
            }
        }
        awakenScrollBars();
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        int targetScrollX;
        switch (action) {
            case 4096:
            case R.id.accessibilityActionScrollRight /*16908347*/:
                if (!isEnabled()) {
                    return false;
                }
                targetScrollX = Math.min(this.mScrollX + ((getWidth() - this.mPaddingLeft) - this.mPaddingRight), getScrollRange());
                if (targetScrollX == this.mScrollX) {
                    return false;
                }
                smoothScrollTo(targetScrollX, 0);
                return true;
            case 8192:
            case R.id.accessibilityActionScrollLeft /*16908345*/:
                if (!isEnabled()) {
                    return false;
                }
                targetScrollX = Math.max(0, this.mScrollX - ((getWidth() - this.mPaddingLeft) - this.mPaddingRight));
                if (targetScrollX == this.mScrollX) {
                    return false;
                }
                smoothScrollTo(targetScrollX, 0);
                return true;
            default:
                return false;
        }
    }

    public CharSequence getAccessibilityClassName() {
        return HorizontalScrollView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        int scrollRange = getScrollRange();
        if (scrollRange > 0) {
            info.setScrollable(true);
            if (isEnabled() && this.mScrollX > 0) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_LEFT);
            }
            if (isEnabled() && this.mScrollX < scrollRange) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_RIGHT);
            }
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setScrollable(getScrollRange() > 0);
        event.setScrollX(this.mScrollX);
        event.setScrollY(this.mScrollY);
        event.setMaxScrollX(getScrollRange());
        event.setMaxScrollY(this.mScrollY);
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getWidth() - ((getWidth() - this.mPaddingLeft) - this.mPaddingRight));
        }
        return 0;
    }

    private View findFocusableViewInMyBounds(boolean leftFocus, int left, View preferredFocusable) {
        int fadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        int leftWithoutFadingEdge = left + fadingEdgeLength;
        int rightWithoutFadingEdge = (getWidth() + left) - fadingEdgeLength;
        return (preferredFocusable == null || preferredFocusable.getLeft() >= rightWithoutFadingEdge || preferredFocusable.getRight() <= leftWithoutFadingEdge) ? findFocusableViewInBounds(leftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge) : preferredFocusable;
    }

    private View findFocusableViewInBounds(boolean leftFocus, int left, int right) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();
            if (left < viewRight && viewLeft < right) {
                boolean viewIsFullyContained = left < viewLeft && viewRight < right;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToBoundary = (leftFocus && viewLeft < focusCandidate.getLeft()) || (!leftFocus && viewRight > focusCandidate.getRight());
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
        boolean right;
        if (direction == 66) {
            right = true;
        } else {
            right = false;
        }
        int width = getWidth();
        if (right) {
            this.mTempRect.left = getScrollX() + width;
            if (getChildCount() > 0) {
                View view = getChildAt(0);
                if (this.mTempRect.left + width > view.getRight()) {
                    this.mTempRect.left = view.getRight() - width;
                }
            }
        } else {
            this.mTempRect.left = getScrollX() - width;
            if (this.mTempRect.left < 0) {
                this.mTempRect.left = 0;
            }
        }
        this.mTempRect.right = this.mTempRect.left + width;
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    public boolean fullScroll(int direction) {
        boolean right;
        if (direction == 66) {
            right = true;
        } else {
            right = false;
        }
        int width = getWidth();
        this.mTempRect.left = 0;
        this.mTempRect.right = width;
        if (right && getChildCount() > 0) {
            View view = getChildAt(0);
            this.mTempRect.right = view.getRight();
            this.mTempRect.left = this.mTempRect.right - width;
        }
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    private boolean scrollAndFocus(int direction, int left, int right) {
        boolean handled = true;
        int width = getWidth();
        int containerLeft = getScrollX();
        int containerRight = containerLeft + width;
        boolean goLeft = direction == 17;
        View newFocused = findFocusableViewInBounds(goLeft, left, right);
        if (newFocused == null) {
            newFocused = this;
        }
        if (left < containerLeft || right > containerRight) {
            doScrollX(goLeft ? left - containerLeft : right - containerRight);
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
        int scrollDelta;
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJump)) {
            scrollDelta = maxJump;
            if (direction == 17 && getScrollX() < scrollDelta) {
                scrollDelta = getScrollX();
            } else if (direction == 66 && getChildCount() > 0) {
                int daRight = getChildAt(0).getRight();
                int screenRight = getScrollX() + getWidth();
                if (daRight - screenRight < maxJump) {
                    scrollDelta = daRight - screenRight;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            int i;
            if (direction == 66) {
                i = scrollDelta;
            } else {
                i = -scrollDelta;
            }
            doScrollX(i);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            if (scrollDelta == 0) {
                return false;
            }
            doScrollX(scrollDelta);
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
        return !isWithinDeltaOfScreen(descendant, 0);
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.right + delta >= getScrollX() && this.mTempRect.left - delta <= getScrollX() + getWidth();
    }

    private void doScrollX(int delta) {
        if (delta == 0) {
            return;
        }
        if (this.mSmoothScrollingEnabled) {
            smoothScrollBy(delta, 0);
        } else {
            scrollBy(delta, 0);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int maxX = Math.max(0, getChildAt(0).getWidth() - ((getWidth() - this.mPaddingRight) - this.mPaddingLeft));
                int scrollX = this.mScrollX;
                this.mScroller.startScroll(scrollX, this.mScrollY, Math.max(0, Math.min(scrollX + dx, maxX)) - scrollX, 0);
                postInvalidateOnAnimation();
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - this.mScrollX, y - this.mScrollY);
    }

    protected int computeHorizontalScrollRange() {
        int contentWidth = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
        if (getChildCount() == 0) {
            return contentWidth;
        }
        int scrollRange = getChildAt(0).getRight();
        int scrollX = this.mScrollX;
        int overscrollRight = Math.max(0, scrollRange - contentWidth);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }
        return scrollRange;
    }

    protected int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        child.measure(MeasureSpec.makeMeasureSpec(0, 0), ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, child.getLayoutParams().height));
    }

    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, 0), ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
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
                overScrollBy(x - oldX, y - oldY, oldX, oldY, range, 0, this.mOverflingDistance, 0, false);
                onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll) {
                    if (x < 0 && oldX >= 0) {
                        this.mEdgeGlowLeft.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (x > range && oldX <= range) {
                        this.mEdgeGlowRight.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            scrollBy(scrollDelta, 0);
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
            if (immediate) {
                scrollBy(delta, 0);
            } else {
                smoothScrollBy(delta, 0);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getHorizontalFadingEdgeLength();
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        int scrollXDelta;
        if (rect.right > screenRight && rect.left > screenLeft) {
            if (rect.width() > width) {
                scrollXDelta = 0 + (rect.left - screenLeft);
            } else {
                scrollXDelta = 0 + (rect.right - screenRight);
            }
            return Math.min(scrollXDelta, getChildAt(0).getRight() - screenRight);
        } else if (rect.left >= screenLeft || rect.right >= screenRight) {
            return 0;
        } else {
            if (rect.width() > width) {
                scrollXDelta = 0 - (screenRight - rect.right);
            } else {
                scrollXDelta = 0 - (screenLeft - rect.left);
            }
            return Math.max(scrollXDelta, -getScrollX());
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
            direction = 66;
        } else if (direction == 1) {
            direction = 17;
        }
        View nextFocus = previouslyFocusedRect == null ? FocusFinder.getInstance().findNextFocus(this, null, direction) : FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null || isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidth = 0;
        int childMargins = 0;
        if (getChildCount() > 0) {
            childWidth = getChildAt(0).getMeasuredWidth();
            FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) getChildAt(0).getLayoutParams();
            childMargins = childParams.leftMargin + childParams.rightMargin;
        }
        layoutChildren(l, t, r, b, childWidth > (((r - l) - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - childMargins);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!isLaidOut()) {
            int scrollRange = Math.max(0, childWidth - (((r - l) - this.mPaddingLeft) - this.mPaddingRight));
            if (this.mSavedState != null) {
                this.mScrollX = isLayoutRtl() ? scrollRange - this.mSavedState.scrollOffsetFromStart : this.mSavedState.scrollOffsetFromStart;
                this.mSavedState = null;
            } else if (isLayoutRtl()) {
                this.mScrollX = scrollRange - this.mScrollX;
            }
            if (this.mScrollX > scrollRange) {
                this.mScrollX = scrollRange;
            } else if (this.mScrollX < 0) {
                this.mScrollX = 0;
            }
        }
        scrollTo(this.mScrollX, this.mScrollY);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = findFocus();
        if (currentFocused != null && this != currentFocused && isWithinDeltaOfScreen(currentFocused, this.mRight - this.mLeft)) {
            currentFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
            doScrollX(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
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

    public void fling(int velocityX) {
        if (getChildCount() > 0) {
            int width = (getWidth() - this.mPaddingRight) - this.mPaddingLeft;
            this.mScroller.fling(this.mScrollX, this.mScrollY, velocityX, 0, 0, Math.max(0, getChildAt(0).getWidth() - width), 0, 0, width / 2, 0);
            boolean movingRight = velocityX > 0;
            View currentFocused = findFocus();
            View newFocused = findFocusableViewInMyBounds(movingRight, this.mScroller.getFinalX(), currentFocused);
            if (newFocused == null) {
                newFocused = this;
            }
            if (newFocused != currentFocused) {
                newFocused.requestFocus(movingRight ? 66 : 17);
            }
            postInvalidateOnAnimation();
        }
    }

    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, (getWidth() - this.mPaddingRight) - this.mPaddingLeft, child.getWidth());
            y = clamp(y, (getHeight() - this.mPaddingBottom) - this.mPaddingTop, child.getHeight());
            if (x != this.mScrollX || y != this.mScrollY) {
                if (this.USE_SET_INTEGRATOR_HAPTIC) {
                    hapticScrollTo(x);
                }
                super.scrollTo(x, y);
            }
        }
    }

    private void hapticScrollTo(int x) {
        if (Math.abs(x - this.mLastHapticScrollX) > this.mPixelThresholdX && this.mLastScrollX != this.mScrollX) {
            if (!this.mHapticPreDrawListener.mSkipHapticCalls) {
                this.mHapticPreDrawListener.mSkipHapticCalls = true;
            }
            this.mLastHapticScrollX = x;
            this.mLastScrollX = this.mScrollX;
        }
    }

    public void setOverScrollMode(int mode) {
        if (mode == 2) {
            this.mEdgeGlowLeft = null;
            this.mEdgeGlowRight = null;
        } else if (this.mEdgeGlowLeft == null) {
            Context context = getContext();
            this.mEdgeGlowLeft = new EdgeEffect(context);
            this.mEdgeGlowRight = new EdgeEffect(context);
            boolean IsParentThemeDeviceDefault = false;
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
            if (outValue.data != 0) {
                IsParentThemeDeviceDefault = true;
            }
            if (IsParentThemeDeviceDefault && (this instanceof ScrollingTabContainerView)) {
                this.mEdgeGlowLeft.setColor(1610612736);
                this.mEdgeGlowRight.setColor(1610612736);
            }
        }
        super.setOverScrollMode(mode);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowLeft != null) {
            int restoreCount;
            int height;
            int scrollX = this.mScrollX;
            if (!this.mEdgeGlowLeft.isFinished()) {
                restoreCount = canvas.save();
                height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + this.mPaddingTop), (float) Math.min(0, scrollX));
                this.mEdgeGlowLeft.setSize(height, getWidth());
                if (this.mEdgeGlowLeft.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowRight.isFinished()) {
                restoreCount = canvas.save();
                int width = getWidth();
                height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                canvas.rotate(90.0f);
                canvas.translate((float) (-this.mPaddingTop), (float) (-(Math.max(getScrollRange(), scrollX) + width)));
                this.mEdgeGlowRight.setSize(height, width);
                if (this.mEdgeGlowRight.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
        if (this.mQCstate != 0) {
            drawQuickController(canvas);
        }
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
        ss.scrollOffsetFromStart = isLayoutRtl() ? -this.mScrollX : this.mScrollX;
        return ss;
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("layout:fillViewPort", this.mFillViewport);
    }

    private void handleMessage(Message msg) {
        int overscrollMode;
        boolean canOverscroll;
        switch (msg.what) {
            case 1:
                int range = getScrollRange();
                this.mHoverRecognitionCurrentTime = System.currentTimeMillis();
                this.mHoverRecognitionDurationTime = (this.mHoverRecognitionCurrentTime - this.mHoverRecognitionStartTime) / 1000;
                if (this.mHoverRecognitionCurrentTime - this.mHoverScrollStartTime >= this.mHoverScrollTimeInterval) {
                    int offset;
                    if (this.mHoverRecognitionDurationTime > 2 && this.mHoverRecognitionDurationTime < 4) {
                        this.mHoverScrollSpeed = this.HOVERSCROLL_SPEED + 2;
                    } else if (this.mHoverRecognitionDurationTime >= 4 && this.mHoverRecognitionDurationTime < 5) {
                        this.mHoverScrollSpeed = this.HOVERSCROLL_SPEED + 4;
                    } else if (this.mHoverRecognitionDurationTime >= 5) {
                        this.mHoverScrollSpeed = this.HOVERSCROLL_SPEED + 6;
                    } else {
                        this.mHoverScrollSpeed = this.HOVERSCROLL_SPEED;
                    }
                    if (this.mHoverScrollDirection == 1) {
                        offset = this.mHoverScrollSpeed * -1;
                    } else {
                        offset = this.mHoverScrollSpeed * 1;
                    }
                    if (offset < 0 && this.mScrollX > 0) {
                        scrollBy(offset, 0);
                        this.mHoverHandler.sendEmptyMessageDelayed(1, (long) this.HOVERSCROLL_DELAY);
                        return;
                    } else if (offset <= 0 || this.mScrollX >= range) {
                        overscrollMode = getOverScrollMode();
                        canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && range > 0);
                        if (canOverscroll && !this.mIsHoverOverscrolled) {
                            if (this.mEdgeGlowLeft != null) {
                                if (this.mHoverScrollDirection == 1) {
                                    this.mEdgeGlowLeft.setSize((getHeight() - this.mPaddingTop) - this.mPaddingBottom, getWidth());
                                    this.mEdgeGlowLeft.onPull(0.4f);
                                    if (!this.mEdgeGlowRight.isFinished()) {
                                        this.mEdgeGlowRight.onRelease();
                                    }
                                } else if (this.mHoverScrollDirection == 2) {
                                    this.mEdgeGlowRight.setSize((getHeight() - this.mPaddingTop) - this.mPaddingBottom, getWidth());
                                    this.mEdgeGlowRight.onPull(0.4f);
                                    if (!this.mEdgeGlowLeft.isFinished()) {
                                        this.mEdgeGlowLeft.onRelease();
                                    }
                                }
                            }
                            if (!(this.mEdgeGlowLeft == null || (this.mEdgeGlowLeft.isFinished() && this.mEdgeGlowRight.isFinished()))) {
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
                        scrollBy(offset, 0);
                        this.mHoverHandler.sendEmptyMessageDelayed(1, (long) this.HOVERSCROLL_DELAY);
                        return;
                    }
                }
                return;
            case 2:
                this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, HOVERSCROLL_SPEED_FASTER, this.mContext.getResources().getDisplayMetrics()) + MAX_SCROLL_FACTOR);
                int distanceToMove = this.mQCLocation == 1 ? -this.mHoverScrollSpeed : this.mHoverScrollSpeed;
                int scrollaleRange = getScrollRange();
                if ((distanceToMove >= 0 || this.mScrollX <= 0) && (distanceToMove <= 0 || this.mScrollX >= scrollaleRange)) {
                    overscrollMode = getOverScrollMode();
                    canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && scrollaleRange > 0);
                    if (canOverscroll && !this.mIsHoverOverscrolled) {
                        if (this.mEdgeGlowLeft != null) {
                            if (this.mQCLocation == 1) {
                                this.mEdgeGlowLeft.setSize((getHeight() - this.mPaddingTop) - this.mPaddingBottom, getWidth());
                                this.mEdgeGlowLeft.onPull(0.4f);
                                if (!this.mEdgeGlowRight.isFinished()) {
                                    this.mEdgeGlowRight.onRelease();
                                }
                            } else if (this.mQCLocation == 3) {
                                this.mEdgeGlowRight.setSize((getHeight() - this.mPaddingTop) - this.mPaddingBottom, getWidth());
                                this.mEdgeGlowRight.onPull(0.4f);
                                if (!this.mEdgeGlowLeft.isFinished()) {
                                    this.mEdgeGlowLeft.onRelease();
                                }
                            }
                        }
                        if (!(this.mEdgeGlowLeft == null || (this.mEdgeGlowLeft.isFinished() && this.mEdgeGlowRight.isFinished()))) {
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
                scrollBy(distanceToMove, 0);
                this.mHoverHandler.sendEmptyMessageDelayed(2, (long) this.HOVERSCROLL_DELAY);
                return;
            case 4:
                if (this.mHoverHandler != null && this.mHoverHandler.hasMessages(1)) {
                    this.mHoverHandler.removeMessages(1);
                    return;
                }
                return;
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
        return false;
    }

    private void setupQuickController(int where) {
        int w = getWidth();
        int h = getHeight();
        Rect bounds = null;
        int oldLocation = this.mQCLocation;
        this.mQCLocation = where;
        int btnW;
        int btnH;
        switch (where) {
            case 1:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up_pressed);
                }
                btnW = this.mQCBtnDrawable.getIntrinsicWidth();
                btnH = this.mQCBtnDrawable.getIntrinsicHeight();
                bounds = new Rect(-((h / 2) + (btnW / 2)), 0, -((h / 2) - (btnW / 2)), btnH);
                this.mQCRect = new Rect(0, (h - btnH) / 2, btnW, ((h - btnH) / 2) + btnH);
                break;
            case 2:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up_pressed);
                }
                btnW = this.mQCBtnDrawable.getIntrinsicWidth();
                bounds = new Rect((w - btnW) / 2, 0, ((w - btnW) / 2) + btnW, this.mQCBtnDrawable.getIntrinsicHeight());
                this.mQCRect = bounds;
                break;
            case 3:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down_pressed);
                }
                btnW = this.mQCBtnDrawable.getIntrinsicWidth();
                btnH = this.mQCBtnDrawable.getIntrinsicHeight();
                bounds = new Rect(-((h / 2) + (btnW / 2)), w - btnH, -((h / 2) - (btnW / 2)), w);
                this.mQCRect = new Rect(w - btnW, (h - btnH) / 2, w, ((h - btnH) / 2) + btnH);
                break;
            case 4:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down_pressed);
                }
                btnW = this.mQCBtnDrawable.getIntrinsicWidth();
                bounds = new Rect((w - btnW) / 2, h - this.mQCBtnDrawable.getIntrinsicHeight(), ((w - btnW) / 2) + btnW, h);
                this.mQCRect = bounds;
                break;
        }
        if (bounds != null) {
            this.mQCBtnDrawable.setBounds(bounds);
            this.mQCBtnPressedDrawable.setBounds(bounds);
        }
    }

    private void drawQuickController(Canvas canvas) {
        if (this.mQCstate == 1 || this.mQCstate == 2) {
            int restoreCount = canvas.save();
            if (this.mQCLocation == 1 || this.mQCLocation == 3) {
                int scrollX = this.mScrollX;
                canvas.rotate(270.0f);
                canvas.translate((float) (-this.mPaddingTop), (float) scrollX);
            } else {
                canvas.translate((float) this.mPaddingLeft, (float) this.mScrollY);
            }
            if (this.mQCstate == 1) {
                this.mQCBtnDrawable.draw(canvas);
            } else if (this.mQCstate == 2) {
                this.mQCBtnPressedDrawable.draw(canvas);
            }
            canvas.restoreToCount(restoreCount);
            this.mIsQCShown = true;
            return;
        }
        this.mIsQCShown = false;
    }
}
