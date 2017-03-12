package android.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.LinearInterpolator;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsSeekBar extends ProgressBar {
    private static final boolean ELASTIC_ENABLED = true;
    private static final int MUTE_VIB_DISTANCE_LVL = 400;
    private static final int MUTE_VIB_DURATION = 500;
    private static final int MUTE_VIB_TOTAL = 4;
    private static final int NO_ALPHA = 255;
    private static final boolean SCAFE_ENABLED = "2016A".equals(SystemProperties.get("ro.build.scafe.version"));
    private static final String TAG = "AbsSeekBar";
    private static boolean mIsThemeDeviceDefaultFamily;
    private ColorStateList DEFAULT_ACTIVATED_PROGRESS_COLOR;
    private ColorStateList DEFAULT_ACTIVATED_THUMB_COLOR;
    private ColorStateList DEFAULT_NORMAL_PROGRESS_COLOR;
    private ColorStateList OVERLAP_ACTIVATED_PROGRESS_COLOR;
    private ColorStateList OVERLAP_ACTIVATED_THUMB_COLOR;
    private ColorStateList OVERLAP_NORMAL_PROGRESS_COLOR;
    private int animationCurrnetFrame;
    private int animationTotalFrame;
    private int circleTotal;
    private Side currentSide;
    private int currentSkipFrameWhenActionDown;
    private Status currentStatus;
    private int delayedFramesBeforeReduce;
    private boolean mAllowedSeeBarAnimation;
    private int mCurrentProgressLevel;
    private float mDisabledAlpha;
    private Drawable mDivider;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private int mHoveringLevel;
    private boolean mIsDragging;
    private boolean mIsDraggingForSliding;
    private boolean mIsFirstSetProgress;
    private boolean mIsFluidEnabled;
    private boolean mIsMuteNow;
    private boolean mIsOpenTheme;
    boolean mIsUserSeekable;
    private int mKeyProgressIncrement;
    private boolean mLargeFont;
    private AnimatorSet mMuteAnimationSet;
    private Drawable mOverlapBackground;
    private int mOverlapPoint;
    private Drawable mOverlapPrimary;
    private int mScaledTouchSlop;
    private Drawable mSplitProgress;
    private boolean mSplitTrack;
    private final Rect mTempRect;
    private Drawable mThumb;
    private int mThumbOffset;
    private int mThumbPosX;
    private float mThumbPosXfloat;
    private int mThumbPosY;
    private float mThumbPosYfloat;
    private ColorStateList mThumbTintList;
    private Mode mThumbTintMode;
    private float mTouchDownX;
    private float mTouchDownY;
    float mTouchProgressOffset;
    private boolean mUseMuteAnimation;
    private Paint paint;
    private int pressedThumbWidth;
    private int previousProgress;
    private float progressBarHeight;
    private float progressBarWidth;
    private int skipFrameWhenActionDown;
    private SeekBarFluidPath sliderPath;
    private float tailWidthFromCircleCenter;
    private int targetFrame;

    private enum Side {
        RIGHT,
        LEFT,
        NONE
    }

    private enum Status {
        STOP,
        TAIL_OPEN,
        TAIL_CLOSE
    }

    public AbsSeekBar(Context context) {
        super(context);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeeBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mIsFluidEnabled = false;
        this.delayedFramesBeforeReduce = 5;
        this.skipFrameWhenActionDown = 3;
        this.mLargeFont = false;
        this.currentSide = Side.NONE;
        this.currentStatus = Status.STOP;
        this.mIsOpenTheme = false;
    }

    public AbsSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeeBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mIsFluidEnabled = false;
        this.delayedFramesBeforeReduce = 5;
        this.skipFrameWhenActionDown = 3;
        this.mLargeFont = false;
        this.currentSide = Side.NONE;
        this.currentStatus = Status.STOP;
        this.mIsOpenTheme = false;
    }

    public AbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeeBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mIsFluidEnabled = false;
        this.delayedFramesBeforeReduce = 5;
        this.skipFrameWhenActionDown = 3;
        this.mLargeFont = false;
        this.currentSide = Side.NONE;
        this.currentStatus = Status.STOP;
        this.mIsOpenTheme = false;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBar, defStyleAttr, defStyleRes);
        setThumb(a.getDrawable(0));
        if (a.hasValue(4)) {
            this.mThumbTintMode = Drawable.parseTintMode(a.getInt(4, -1), this.mThumbTintMode);
            this.mHasThumbTintMode = true;
        }
        if (a.hasValue(3)) {
            this.mThumbTintList = a.getColorStateList(3);
            this.mHasThumbTint = true;
        }
        this.mSplitTrack = a.getBoolean(2, false);
        setThumbOffset(a.getDimensionPixelOffset(1, getThumbOffset()));
        boolean useDisabledAlpha = a.getBoolean(5, true);
        a.recycle();
        if (useDisabledAlpha) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Theme, 0, 0);
            this.mDisabledAlpha = ta.getFloat(3, 0.5f);
            ta.recycle();
        } else {
            this.mDisabledAlpha = 1.0f;
        }
        applyThumbTint();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.DEFAULT_NORMAL_PROGRESS_COLOR = colorToColorStateList(context.getResources().getColor(R.color.tw_progress_color_control_normal));
        this.DEFAULT_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(context.getResources().getColor(R.color.tw_progress_color_control_activated));
        this.DEFAULT_ACTIVATED_THUMB_COLOR = colorToColorStateList(context.getResources().getColor(R.color.tw_thumb_color_control_activated));
        this.OVERLAP_NORMAL_PROGRESS_COLOR = colorToColorStateList(context.getResources().getColor(R.color.tw_progress_color_overlap_normal));
        this.OVERLAP_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(context.getResources().getColor(R.color.tw_progress_color_overlap_activated));
        this.OVERLAP_ACTIVATED_THUMB_COLOR = this.OVERLAP_ACTIVATED_PROGRESS_COLOR;
        this.mAllowedSeeBarAnimation = context.getResources().getBoolean(R.bool.tw_seekbar_sliding_animation);
        if (this.mAllowedSeeBarAnimation) {
            initMuteAnimation();
        }
        if (isFluidEnabled()) {
            seekBarFluidInit();
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        mIsThemeDeviceDefaultFamily = outValue.data != 0;
    }

    private void seekBarFluidInit() {
        if (this.paint == null) {
            this.paint = new Paint();
        }
        this.paint.setAntiAlias(true);
        setFluidColor(this.mContext.getResources().getColor(R.color.tw_thumb_color_control_activated));
        if (this.mThumb != null) {
            this.pressedThumbWidth = this.mThumb.getIntrinsicHeight();
        }
        if (this.sliderPath == null) {
            this.sliderPath = new SeekBarFluidPath((float) this.pressedThumbWidth);
        }
        this.animationTotalFrame = this.sliderPath.getPathTotal();
        this.circleTotal = this.sliderPath.getCircleTotal();
        this.previousProgress = getProgress();
        this.tailWidthFromCircleCenter = this.sliderPath.getTailWidthFromCircleCenter();
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        if (this.mThumb == null || thumb == this.mThumb) {
            needUpdate = false;
        } else {
            this.mThumb.setCallback(null);
            needUpdate = true;
        }
        if (thumb != null) {
            thumb.setCallback(this);
            if (canResolveLayoutDirection()) {
                thumb.setLayoutDirection(getLayoutDirection());
            }
            if (checkMode(3)) {
                this.mThumbOffset = thumb.getIntrinsicHeight() / 2;
            } else {
                this.mThumbOffset = thumb.getIntrinsicWidth() / 2;
            }
            if (needUpdate && !(thumb.getIntrinsicWidth() == this.mThumb.getIntrinsicWidth() && thumb.getIntrinsicHeight() == this.mThumb.getIntrinsicHeight())) {
                requestLayout();
            }
        }
        this.mThumb = thumb;
        applyThumbTint();
        invalidate();
        if (needUpdate) {
            updateThumbAndTrackPos(getWidth(), getHeight());
            if (thumb != null && thumb.isStateful()) {
                thumb.setState(getDrawableState());
            }
        }
        if (isFluidEnabled() && this.mThumb != null) {
            this.pressedThumbWidth = this.mThumb.getIntrinsicHeight();
        }
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    public void setThumbTintColor(int color) {
        if (!colorToColorStateList(color).equals(this.DEFAULT_ACTIVATED_THUMB_COLOR)) {
            this.DEFAULT_ACTIVATED_THUMB_COLOR = colorToColorStateList(color);
        }
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
        if (isFluidEnabled()) {
            if (this.mThumbTintList == null) {
                setFluidColor(0);
            } else {
                setFluidColor(this.mThumbTintList.getDefaultColor());
            }
        }
        this.DEFAULT_ACTIVATED_THUMB_COLOR = tint;
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(Mode tintMode) {
        this.mThumbTintMode = tintMode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    private void applyThumbTint() {
        if (this.mThumb == null) {
            return;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.mThumb = this.mThumb.mutate();
            if (this.mHasThumbTint) {
                this.mThumb.setTintList(this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                this.mThumb.setTintMode(this.mThumbTintMode);
            }
            if (this.mThumb.isStateful()) {
                this.mThumb.setState(getDrawableState());
            }
        }
    }

    public int getThumbOffset() {
        return this.mThumbOffset;
    }

    public void setThumbOffset(int thumbOffset) {
        this.mThumbOffset = thumbOffset;
        invalidate();
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public void setKeyProgressIncrement(int increment) {
        if (increment < 0) {
            increment = -increment;
        }
        this.mKeyProgressIncrement = increment;
    }

    public int getKeyProgressIncrement() {
        return this.mKeyProgressIncrement;
    }

    public synchronized void setMax(int max) {
        super.setMax(max);
        this.mIsFirstSetProgress = true;
        if (this.mKeyProgressIncrement == 0 || getMax() / this.mKeyProgressIncrement > 20) {
            setKeyProgressIncrement(Math.max(1, Math.round(((float) getMax()) / 20.0f)));
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mThumb || super.verifyDrawable(who);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mThumb != null) {
            this.mThumb.jumpToCurrentState();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null && this.mDisabledAlpha < 1.0f) {
            progressDrawable.setAlpha(isEnabled() ? 255 : (int) (255.0f * this.mDisabledAlpha));
        }
        Drawable thumb = this.mThumb;
        if (thumb != null && thumb.isStateful()) {
            thumb.setState(getDrawableState());
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mThumb != null) {
            this.mThumb.setHotspot(x, y);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
        boolean isMuteAnimationNeeded;
        int targetLevel = (int) (10000.0f * scale);
        if (!this.mUseMuteAnimation || this.mIsFirstSetProgress || this.mIsDraggingForSliding) {
            isMuteAnimationNeeded = false;
        } else {
            isMuteAnimationNeeded = true;
        }
        if (isMuteAnimationNeeded && this.mCurrentProgressLevel != 0 && targetLevel == 0) {
            startMuteAnimation();
            return;
        }
        cancelMuteAnimation();
        this.mIsFirstSetProgress = false;
        this.mCurrentProgressLevel = targetLevel;
        super.onProgressRefresh(scale, fromUser, progress);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (isFluidEnabled()) {
            openTail(getProgress());
        }
    }

    private void openTail(int progress) {
        if (this.previousProgress != progress) {
            this.progressBarWidth = (float) ((getWidth() - getPaddingLeft()) - getPaddingRight());
            this.progressBarHeight = (float) ((getHeight() - getPaddingTop()) - getPaddingBottom());
            if (this.currentSkipFrameWhenActionDown <= 0) {
                if (this.previousProgress < progress) {
                    if (checkMode(3)) {
                        if ((this.progressBarHeight * ((float) getProgress())) / ((float) getMax()) < this.tailWidthFromCircleCenter) {
                            return;
                        }
                    } else if ((this.progressBarWidth * ((float) getProgress())) / ((float) getMax()) < this.tailWidthFromCircleCenter) {
                        return;
                    }
                    this.currentSide = Side.RIGHT;
                    this.targetFrame = (this.animationTotalFrame - 1) + this.delayedFramesBeforeReduce;
                } else if (this.previousProgress > progress) {
                    if (checkMode(3)) {
                        if (this.progressBarHeight * (1.0f - (((float) getProgress()) / ((float) getMax()))) < this.tailWidthFromCircleCenter) {
                            return;
                        }
                    } else if (this.progressBarWidth * (1.0f - (((float) getProgress()) / ((float) getMax()))) < this.tailWidthFromCircleCenter) {
                        return;
                    }
                    this.currentSide = Side.LEFT;
                    this.targetFrame = (-this.circleTotal) + 1;
                }
                this.currentStatus = Status.TAIL_OPEN;
            } else {
                this.currentSkipFrameWhenActionDown--;
            }
            this.previousProgress = progress;
        }
    }

    private void closeTail() {
        this.currentStatus = Status.TAIL_CLOSE;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void updateThumbAndTrackPos(int w, int h) {
        if (checkMode(3)) {
            updateThumbAndTrackPosInVertical(w, h);
            return;
        }
        int trackOffset;
        int thumbOffset;
        int paddedHeight = (h - this.mPaddingTop) - this.mPaddingBottom;
        Drawable track = getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackHeight = Math.min(this.mMaxHeight, paddedHeight);
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        int fluidOffset = 0;
        int offsetHeight;
        if (thumbHeight > trackHeight) {
            offsetHeight = (paddedHeight - thumbHeight) / 2;
            if (isFluidEnabled() && (thumbHeight - trackHeight) % 2 != 0) {
                fluidOffset = 1;
            }
            trackOffset = (((thumbHeight - trackHeight) / 2) + offsetHeight) + fluidOffset;
            thumbOffset = offsetHeight + 0;
        } else {
            offsetHeight = (paddedHeight - trackHeight) / 2;
            trackOffset = offsetHeight + 0;
            thumbOffset = offsetHeight + ((trackHeight - thumbHeight) / 2);
        }
        if (track != null) {
            track.setBounds(0, trackOffset, (w - this.mPaddingRight) - this.mPaddingLeft, trackOffset + trackHeight);
        }
        if (thumb != null) {
            setThumbPos(w, thumb, getScale(), thumbOffset);
        }
        updateSplitProgress();
    }

    private void updateThumbAndTrackPosInVertical(int w, int h) {
        int trackOffset;
        int thumbOffset;
        Drawable track = getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackWidth = Math.min(this.mMaxWidth, (w - this.mPaddingLeft) - this.mPaddingRight);
        int thumbWidth = thumb == null ? 0 : thumb.getIntrinsicHeight();
        if (thumbWidth > trackWidth) {
            trackOffset = (thumbWidth - trackWidth) / 2;
            thumbOffset = 0;
        } else {
            trackOffset = 0;
            thumbOffset = (trackWidth - thumbWidth) / 2;
        }
        if (track != null) {
            track.setBounds(trackOffset, 0, ((w - this.mPaddingRight) - trackOffset) - this.mPaddingLeft, (h - this.mPaddingBottom) - this.mPaddingTop);
        }
        if (thumb != null) {
            setThumbPosInVertical(h, thumb, getScale(), thumbOffset);
        }
    }

    private float getScale() {
        int max = getMax();
        return max > 0 ? ((float) getProgress()) / ((float) max) : 0.0f;
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int offset) {
        if (checkMode(3)) {
            setThumbPosInVertical(getHeight(), thumb, scale, offset);
            return;
        }
        int top;
        int bottom;
        int left;
        int available = (w - this.mPaddingLeft) - this.mPaddingRight;
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available = (available - thumbWidth) + (this.mThumbOffset * 2);
        int thumbPos = (int) ((((float) available) * scale) + 0.5f);
        if (offset == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            top = oldBounds.top;
            bottom = oldBounds.bottom;
        } else {
            top = offset;
            bottom = offset + thumbHeight;
        }
        if (isLayoutRtl() && this.mMirrorForRtl) {
            left = available - thumbPos;
        } else {
            left = thumbPos;
        }
        int right = left + thumbWidth;
        Drawable background = getBackground();
        if (background != null) {
            int offsetX = this.mPaddingLeft - this.mThumbOffset;
            int offsetY = this.mPaddingTop;
            background.setHotspotBounds(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
        this.mThumbPosX = this.mPaddingLeft + left;
        this.mThumbPosY = ((thumbHeight / 2) + top) + this.mPaddingTop;
        this.mThumbPosXfloat = (float) this.mThumbPosX;
        this.mThumbPosYfloat = (((float) top) + (((float) thumbHeight) / 2.0f)) + ((float) this.mPaddingTop);
        updateSplitProgress();
    }

    private void setThumbPosInVertical(int h, Drawable thumb, float scale, int offset) {
        int left;
        int right;
        int available = (h - this.mPaddingTop) - this.mPaddingBottom;
        int thumbWidth = thumb.getIntrinsicHeight();
        int thumbHeight = thumb.getIntrinsicHeight();
        available = (available - thumbHeight) + (this.mThumbOffset * 2);
        int thumbPos = (int) ((((float) available) * scale) + 0.5f);
        if (offset == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            left = oldBounds.left;
            right = oldBounds.right;
        } else {
            left = offset;
            right = offset + thumbWidth;
        }
        int top = available - thumbPos;
        int bottom = top + thumbHeight;
        Drawable background = getBackground();
        if (background != null) {
            Rect bounds = thumb.getBounds();
            int offsetX = this.mPaddingLeft;
            int offsetY = this.mPaddingTop - this.mThumbOffset;
            background.setHotspotBounds(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
        this.mThumbPosX = ((thumbWidth / 2) + left) + this.mPaddingLeft;
        this.mThumbPosY = this.mPaddingTop + top;
        this.mThumbPosXfloat = (((float) left) + (((float) thumbWidth) / 2.0f)) + ((float) this.mPaddingLeft);
        this.mThumbPosYfloat = (float) this.mThumbPosY;
    }

    private void updateSplitProgress() {
        if (checkMode(4)) {
            Drawable d = this.mSplitProgress;
            Rect base = getCurrentDrawable().getBounds();
            if (d != null) {
                if (isLayoutRtl() && this.mMirrorForRtl) {
                    d.setBounds(this.mThumbPosX, base.top, getWidth() - this.mPaddingRight, base.bottom);
                } else {
                    d.setBounds(this.mPaddingLeft, base.top, this.mThumbPosX, base.bottom);
                }
            }
            int w = getWidth();
            int h = getHeight();
            if (this.mDivider != null) {
                this.mDivider.setBounds((int) (((float) (w / 2)) - ((this.mDensity * 4.0f) / 2.0f)), (int) (((float) (h / 2)) - ((this.mDensity * 22.0f) / 2.0f)), (int) (((float) (w / 2)) + ((this.mDensity * 4.0f) / 2.0f)), (int) (((float) (h / 2)) + ((this.mDensity * 22.0f) / 2.0f)));
            }
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (this.mThumb != null) {
            this.mThumb.setLayoutDirection(layoutDirection);
        }
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (checkMode(4)) {
            this.mSplitProgress.draw(canvas);
            this.mDivider.draw(canvas);
        }
        drawThumb(canvas);
        if (isFluidEnabled() && this.mIsDragging) {
            if (this.currentStatus == Status.TAIL_CLOSE) {
                if (this.currentSide == Side.RIGHT) {
                    this.animationCurrnetFrame--;
                    drawFrame(canvas);
                } else if (this.currentSide == Side.LEFT) {
                    this.animationCurrnetFrame++;
                    drawFrame(canvas);
                }
                if (this.animationCurrnetFrame == 0) {
                    this.currentSide = Side.NONE;
                }
            } else if (this.currentStatus == Status.TAIL_OPEN) {
                if (this.currentSide == Side.RIGHT) {
                    if (this.animationCurrnetFrame < this.circleTotal) {
                        this.animationCurrnetFrame = this.circleTotal;
                    }
                    this.animationCurrnetFrame++;
                    if (this.animationCurrnetFrame >= this.targetFrame) {
                        this.animationCurrnetFrame = this.targetFrame;
                        closeTail();
                    }
                } else if (this.currentSide == Side.LEFT) {
                    if (this.animationCurrnetFrame > (-this.circleTotal)) {
                        this.animationCurrnetFrame = -this.circleTotal;
                    }
                    this.animationCurrnetFrame--;
                    if (this.animationCurrnetFrame <= this.targetFrame) {
                        this.animationCurrnetFrame = this.targetFrame;
                        closeTail();
                    }
                } else {
                    closeTail();
                }
                drawFrame(canvas);
            }
        }
    }

    private void drawFrame(Canvas canvas) {
        float xOffset;
        float yOffset;
        int tempIndex = this.animationCurrnetFrame < 0 ? -this.animationCurrnetFrame : this.animationCurrnetFrame;
        if (tempIndex > this.animationTotalFrame - 1) {
            tempIndex = this.animationTotalFrame - 1;
        }
        Path path = this.sliderPath.getPath(tempIndex);
        float ratio = ((float) getProgress()) / ((float) getMax());
        if (checkMode(3)) {
            xOffset = this.mThumbPosXfloat;
            yOffset = ((float) getPaddingTop()) + (this.progressBarHeight * (1.0f - ratio));
        } else {
            xOffset = (isLayoutRtl() && this.mMirrorForRtl) ? ((float) getPaddingLeft()) + (this.progressBarWidth * (1.0f - ratio)) : ((float) getPaddingLeft()) + (this.progressBarWidth * ratio);
            yOffset = this.mThumbPosYfloat;
        }
        canvas.save();
        if (checkMode(3)) {
            if (this.animationCurrnetFrame < 0) {
                canvas.rotate(90.0f, xOffset, yOffset);
            } else {
                canvas.rotate(-90.0f, xOffset, yOffset);
            }
        } else if (isLayoutRtl() && this.mMirrorForRtl) {
            if (this.animationCurrnetFrame > 0) {
                canvas.rotate(180.0f, xOffset, yOffset);
            }
        } else if (this.animationCurrnetFrame < 0) {
            canvas.rotate(180.0f, xOffset, yOffset);
        }
        canvas.translate(xOffset, yOffset);
        canvas.drawPath(path, this.paint);
        canvas.restore();
        invalidate(0, 0, getWidth(), getHeight());
    }

    void drawTrack(Canvas canvas) {
        Drawable thumbDrawable = this.mThumb;
        if (thumbDrawable == null || !this.mSplitTrack) {
            super.drawTrack(canvas);
        } else {
            Insets insets = thumbDrawable.getOpticalInsets();
            Rect tempRect = this.mTempRect;
            thumbDrawable.copyBounds(tempRect);
            tempRect.offset(this.mPaddingLeft - this.mThumbOffset, this.mPaddingTop);
            tempRect.left += insets.left;
            tempRect.right -= insets.right;
            int saveCount = canvas.save();
            canvas.clipRect(tempRect, Op.DIFFERENCE);
            super.drawTrack(canvas);
            canvas.restoreToCount(saveCount);
        }
        if (!checkInvalidatedDualColorMode()) {
            canvas.save();
            if (checkMode(3)) {
                canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
            } else if (isLayoutRtl() && this.mMirrorForRtl) {
                canvas.translate((float) (getWidth() - this.mPaddingRight), (float) this.mPaddingTop);
                canvas.scale(LayoutParams.BRIGHTNESS_OVERRIDE_NONE, 1.0f);
            } else {
                canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
            }
            this.mOverlapBackground.draw(canvas);
            if (getProgress() > this.mOverlapPoint) {
                this.mOverlapPrimary.draw(canvas);
            }
            canvas.restore();
        }
    }

    void drawThumb(Canvas canvas) {
        if (this.mThumb != null) {
            canvas.save();
            if (checkMode(3)) {
                canvas.translate((float) this.mPaddingLeft, (float) (this.mPaddingTop - this.mThumbOffset));
            } else {
                canvas.translate((float) (this.mPaddingLeft - this.mThumbOffset), (float) this.mPaddingTop);
            }
            this.mThumb.draw(canvas);
            canvas.restore();
        }
    }

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbHeight = 0;
        synchronized (this) {
            Drawable d = getCurrentDrawable();
            int dw = 0;
            int dh = 0;
            if (d != null) {
                if (checkMode(3)) {
                    int thumbWidth = this.mThumb == null ? 0 : this.mThumb.getIntrinsicHeight();
                    dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicHeight()));
                    dh = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicWidth()));
                    dw = Math.max(thumbWidth, dw);
                } else {
                    if (this.mThumb != null) {
                        thumbHeight = this.mThumb.getIntrinsicHeight();
                    }
                    dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicWidth()));
                    dh = Math.max(thumbHeight, Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicHeight())));
                }
            }
            setMeasuredDimension(View.resolveSizeAndState(dw + (this.mPaddingLeft + this.mPaddingRight), widthMeasureSpec, 0), View.resolveSizeAndState(dh + (this.mPaddingTop + this.mPaddingBottom), heightMeasureSpec, 0));
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mIsUserSeekable || !isEnabled()) {
            return false;
        }
        if (isFluidEnabled() && event.getAction() == 0) {
            this.currentSkipFrameWhenActionDown = getMax() >= 100 ? this.skipFrameWhenActionDown : 1;
            this.animationCurrnetFrame = 0;
            this.currentSide = Side.NONE;
        }
        switch (event.getAction()) {
            case 0:
                this.mIsDraggingForSliding = false;
                if (isInScrollingContainer()) {
                    this.mTouchDownX = event.getX();
                    this.mTouchDownY = event.getY();
                    return true;
                }
                setPressed(true);
                if (this.mThumb != null) {
                    invalidate(this.mThumb.getBounds());
                }
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();
                return true;
            case 1:
                if (this.mIsDraggingForSliding) {
                    this.mIsDraggingForSliding = false;
                }
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();
                return true;
            case 2:
                this.mIsDraggingForSliding = true;
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    return true;
                }
                float x = event.getX();
                float y = event.getY();
                if ((checkMode(3) || Math.abs(x - this.mTouchDownX) <= ((float) this.mScaledTouchSlop)) && (!checkMode(3) || Math.abs(y - this.mTouchDownY) <= ((float) this.mScaledTouchSlop))) {
                    return true;
                }
                setPressed(true);
                if (this.mThumb != null) {
                    invalidate(this.mThumb.getBounds());
                }
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();
                return true;
            case 3:
                this.mIsDraggingForSliding = false;
                if (this.mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
                return true;
            default:
                return true;
        }
    }

    private void setHotspot(float x, float y) {
        Drawable bg = getBackground();
        if (bg != null) {
            bg.setHotspot(x, y);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        if (checkMode(3)) {
            trackTouchEventInVertical(event);
            return;
        }
        float scale;
        int width = getWidth();
        int available = (width - this.mPaddingLeft) - this.mPaddingRight;
        int x = (int) event.getX();
        float progress = 0.0f;
        if (isLayoutRtl() && this.mMirrorForRtl) {
            if (x > width - this.mPaddingRight) {
                scale = 0.0f;
            } else if (x < this.mPaddingLeft) {
                scale = 1.0f;
            } else {
                scale = ((float) ((available - x) + this.mPaddingLeft)) / ((float) available);
                progress = this.mTouchProgressOffset;
            }
        } else if (x < this.mPaddingLeft) {
            scale = 0.0f;
        } else if (x > width - this.mPaddingRight) {
            scale = 1.0f;
        } else {
            scale = ((float) (x - this.mPaddingLeft)) / ((float) available);
            progress = this.mTouchProgressOffset;
        }
        int max = getMax();
        float basicWidth = 1.0f / ((float) max);
        if (scale > 0.0f && scale < 1.0f && scale % basicWidth > basicWidth / 2.0f) {
            scale += basicWidth / 2.0f;
        }
        progress += ((float) max) * scale;
        setHotspot((float) x, (float) ((int) event.getY()));
        setProgress((int) progress, true);
    }

    private void trackTouchEventInVertical(MotionEvent event) {
        float scale;
        int height = getHeight();
        int available = (height - this.mPaddingTop) - this.mPaddingBottom;
        int y = height - ((int) event.getY());
        float progress = 0.0f;
        if (y < this.mPaddingBottom) {
            scale = 0.0f;
        } else if (y > height - this.mPaddingTop) {
            scale = 1.0f;
        } else {
            scale = ((float) (y - this.mPaddingBottom)) / ((float) available);
            progress = this.mTouchProgressOffset;
        }
        progress += ((float) getMax()) * scale;
        setHotspot((float) ((int) event.getX()), (float) ((int) event.getY()));
        setProgress((int) progress, true);
    }

    private void attemptClaimDrag() {
        if (this.mParent != null) {
            this.mParent.requestDisallowInterceptTouchEvent(true);
        }
    }

    void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    void onKeyChange() {
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            int increment = this.mKeyProgressIncrement;
            if (checkMode(3)) {
                switch (keyCode) {
                    case 19:
                        break;
                    case 20:
                        increment = -increment;
                        break;
                }
                if (isLayoutRtl()) {
                    increment = -increment;
                }
                if (setProgress(getProgress() + increment, true)) {
                    onKeyChange();
                    return true;
                }
            }
            switch (keyCode) {
                case 21:
                    increment = -increment;
                    break;
                case 22:
                    break;
                default:
                    break;
            }
            if (isLayoutRtl()) {
                increment = -increment;
            }
            if (setProgress(getProgress() + increment, true)) {
                onKeyChange();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean setProgress(int progress, boolean fromUser) {
        boolean superRet = super.setProgress(progress, fromUser);
        updateWarningMode(progress);
        updateDualColorMode();
        return superRet;
    }

    public CharSequence getAccessibilityClassName() {
        return AbsSeekBar.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            int progress = getProgress();
            if (progress > 0) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
            }
            if (progress < getMax()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
            }
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        if (action != 4096 && action != 8192) {
            return false;
        }
        int increment = Math.max(1, Math.round(((float) getMax()) / 5.0f));
        if (mIsThemeDeviceDefaultFamily) {
            increment = Math.max(1, Math.round(((float) getMax()) / 15.0f));
        }
        if (action == 8192) {
            increment = -increment;
        }
        if (!setProgress(getProgress() + increment, true)) {
            return false;
        }
        onKeyChange();
        return true;
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, getScale(), Integer.MIN_VALUE);
            invalidate();
        }
    }

    void onStartTrackingHover(int hoverLevel, int posX, int posY) {
    }

    void onStopTrackingHover() {
    }

    void onHoverChanged(int hoverLevel, int posX, int posY) {
    }

    private void trackHoverEvent(int posX, int posY) {
        float scale;
        int width = getWidth();
        int available = (width - this.mPaddingLeft) - this.mPaddingRight;
        float hoverLevel = 0.0f;
        if (posX < this.mPaddingLeft) {
            scale = 0.0f;
        } else if (posX > width - this.mPaddingRight) {
            scale = 1.0f;
        } else {
            scale = ((float) (posX - this.mPaddingLeft)) / ((float) available);
            hoverLevel = this.mTouchProgressOffset;
        }
        this.mHoveringLevel = (int) (hoverLevel + (((float) getMax()) * scale));
    }

    public void setHoverPopupType(int type) {
        if (isHoveringUIEnabled()) {
            if (type == 3) {
                getHoverPopupWindow().setPopupGravity(12849);
                getHoverPopupWindow().setPopupPosOffset(0, getMeasuredHeight() / 2);
                getHoverPopupWindow().setHoverDetectTime(200);
            }
            super.setHoverPopupType(type);
        }
    }

    public boolean onHoverEvent(MotionEvent event) {
        boolean isPossibleTooltype = false;
        if (event.getToolType(0) == 2 || event.getToolType(0) == 1) {
            isPossibleTooltype = true;
        }
        if (isHoveringUIEnabled() && isPossibleTooltype) {
            int action = event.getAction();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 9) {
                trackHoverEvent(x, y);
                onStartTrackingHover(this.mHoveringLevel, x, y);
            } else if (action == 7) {
                trackHoverEvent(x, y);
                onHoverChanged(this.mHoveringLevel, x, y);
                if (this.mHoverPopupType == 3) {
                    getHoverPopupWindow().setHoveringPoint((int) event.getRawX(), (int) event.getRawY());
                    getHoverPopupWindow().updateHoverPopup();
                }
            } else if (action == 10) {
                onStopTrackingHover();
            }
        }
        return super.onHoverEvent(event);
    }

    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
    }

    public int getThumbCentralX() {
        return this.mThumbPosX;
    }

    public int getThumbCentralY() {
        return this.mThumbPosY;
    }

    public int getThumbWith() {
        return this.mThumb.getIntrinsicWidth();
    }

    public int getThumbHeight() {
        return this.mThumb.getIntrinsicHeight();
    }

    public void setProgressDrawableForAutoBrightness(boolean auto) {
    }

    public void setMode(int mode) {
        super.setMode(mode);
        switch (mode) {
            case 0:
                setProgressTintList(this.DEFAULT_ACTIVATED_PROGRESS_COLOR);
                setThumbTintList(this.DEFAULT_ACTIVATED_THUMB_COLOR);
                break;
            case 1:
                updateWarningMode(getProgress());
                break;
            case 3:
                setThumb(this.mContext.getDrawable(R.drawable.tw_scrubber_control_vertical_material_anim));
                break;
            case 4:
                this.mSplitProgress = this.mContext.getDrawable(R.drawable.tw_split_seekbar_primary_progress_material);
                this.mDivider = this.mContext.getDrawable(R.drawable.tw_split_seekbar_vertical_bar_material);
                updateSplitProgress();
                break;
        }
        invalidate();
    }

    public void setSplitProgressDrawable(Drawable drawable) {
        if (this.mSplitProgress != null) {
            this.mSplitProgress = drawable;
        }
    }

    public void setDividerDrawable(Drawable drawable) {
        if (this.mDivider != null) {
            this.mDivider = drawable;
        }
    }

    public void setOverlapBackgroundForDualColor(int color) {
        ColorStateList mOverlapColor = colorToColorStateList(color);
        if (!mOverlapColor.equals(this.OVERLAP_NORMAL_PROGRESS_COLOR)) {
            this.OVERLAP_NORMAL_PROGRESS_COLOR = mOverlapColor;
        }
        this.OVERLAP_ACTIVATED_PROGRESS_COLOR = this.OVERLAP_NORMAL_PROGRESS_COLOR;
        this.mLargeFont = true;
    }

    public void setOverlapPointForDualColor(int value) {
        if (value < getMax() && value != 0) {
            if (value == -1) {
                this.mOverlapPoint = value;
                setProgressTintList(this.DEFAULT_ACTIVATED_PROGRESS_COLOR);
                setThumbTintList(this.DEFAULT_ACTIVATED_THUMB_COLOR);
            } else {
                if (checkMode(0)) {
                    this.mOverlapPrimary = this.mContext.getDrawable(R.drawable.tw_scrubber_progress_horizontal_material_extra);
                    this.mOverlapBackground = this.mContext.getDrawable(R.drawable.tw_scrubber_progress_horizontal_material_extra);
                } else if (checkMode(3)) {
                    this.mOverlapPrimary = this.mContext.getDrawable(R.drawable.tw_scrubber_progress_vertical_material_extra);
                    this.mOverlapBackground = this.mContext.getDrawable(R.drawable.tw_scrubber_progress_vertical_material_extra);
                } else {
                    return;
                }
                this.mOverlapPoint = value;
                updateDualColorMode();
            }
            invalidate();
        }
    }

    private void updateDualColorMode() {
        if (!checkInvalidatedDualColorMode()) {
            this.mOverlapPrimary.setTintList(this.OVERLAP_ACTIVATED_PROGRESS_COLOR);
            this.mOverlapBackground.setTintList(this.OVERLAP_NORMAL_PROGRESS_COLOR);
            if (!this.mLargeFont) {
                if (getProgress() > this.mOverlapPoint) {
                    setProgressOverlapTintList(this.OVERLAP_ACTIVATED_PROGRESS_COLOR);
                    setThumbOverlapTintList(this.OVERLAP_ACTIVATED_THUMB_COLOR);
                } else {
                    setProgressTintList(this.DEFAULT_ACTIVATED_PROGRESS_COLOR);
                    setThumbTintList(this.DEFAULT_ACTIVATED_THUMB_COLOR);
                }
            }
            updateBoundsForDualColor();
        }
    }

    private void updateBoundsForDualColor() {
        if (getCurrentDrawable() != null && !checkInvalidatedDualColorMode()) {
            Rect base = getCurrentDrawable().getBounds();
            int maxProgress = getMax();
            int curProgress = getProgress();
            if (checkMode(0)) {
                int width = base.right - base.left;
                int left = (int) (((float) base.left) + (((float) width) * (((float) this.mOverlapPoint) / ((float) maxProgress))));
                this.mOverlapBackground.setBounds(left, base.top, base.right, base.bottom);
                this.mOverlapPrimary.setBounds(left, base.top, Math.min((int) (((float) base.left) + (((float) width) * (((float) curProgress) / ((float) maxProgress)))), base.right), base.bottom);
            } else if (checkMode(3)) {
                int height = base.bottom - base.top;
                int bottom = (int) (((float) base.top) + (((float) height) * (((float) (maxProgress - this.mOverlapPoint)) / ((float) maxProgress))));
                this.mOverlapBackground.setBounds(base.left, base.top, base.right, bottom);
                this.mOverlapPrimary.setBounds(base.left, (int) (((float) base.top) + (((float) height) * (((float) (maxProgress - curProgress)) / ((float) maxProgress)))), base.right, bottom);
            }
        }
    }

    private boolean checkInvalidatedDualColorMode() {
        return this.mOverlapPoint == -1 || this.mOverlapBackground == null;
    }

    protected void updateDrawableBounds(int w, int h) {
        super.updateDrawableBounds(w, h);
        updateThumbAndTrackPos(w, h);
        updateBoundsForDualColor();
    }

    private ColorStateList colorToColorStateList(int color) {
        return new ColorStateList(new int[][]{new int[0]}, new int[]{color});
    }

    private void updateWarningMode(int progress) {
        boolean isMax = true;
        if (checkMode(1)) {
            if (progress != getMax()) {
                isMax = false;
            }
            if (isMax) {
                setProgressOverlapTintList(this.OVERLAP_ACTIVATED_PROGRESS_COLOR);
                setThumbOverlapTintList(this.OVERLAP_ACTIVATED_THUMB_COLOR);
                return;
            }
            setProgressTintList(this.DEFAULT_ACTIVATED_PROGRESS_COLOR);
            setThumbTintList(this.DEFAULT_ACTIVATED_THUMB_COLOR);
        }
    }

    private void initMuteAnimation() {
        this.mMuteAnimationSet = new AnimatorSet();
        List<Animator> list = new ArrayList();
        int duration = 500 / 8;
        int distance = 400;
        for (int i = 0; i < 8; i++) {
            boolean isGoingDirection;
            ValueAnimator progressZeroAnimation;
            if (i % 2 == 0) {
                isGoingDirection = true;
            } else {
                isGoingDirection = false;
            }
            if (isGoingDirection) {
                progressZeroAnimation = ValueAnimator.ofInt(new int[]{0, distance});
            } else {
                progressZeroAnimation = ValueAnimator.ofInt(new int[]{distance, 0});
            }
            progressZeroAnimation.setDuration((long) duration);
            progressZeroAnimation.setInterpolator(new LinearInterpolator());
            progressZeroAnimation.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    AbsSeekBar.this.mCurrentProgressLevel = ((Integer) animation.getAnimatedValue()).intValue();
                    AbsSeekBar.this.onSlidingRefresh(AbsSeekBar.this.mCurrentProgressLevel);
                }
            });
            list.add(progressZeroAnimation);
            if (isGoingDirection) {
                distance = (int) (((double) distance) * 0.6d);
            }
        }
        this.mMuteAnimationSet.playSequentially(list);
    }

    private void cancelMuteAnimation() {
        if (this.mMuteAnimationSet != null && this.mMuteAnimationSet.isRunning()) {
            this.mMuteAnimationSet.cancel();
        }
    }

    private void startMuteAnimation() {
        cancelMuteAnimation();
        if (this.mMuteAnimationSet != null) {
            this.mMuteAnimationSet.start();
        }
    }

    public void setMuteAnimation(boolean use) {
        if (this.mAllowedSeeBarAnimation) {
            this.mUseMuteAnimation = use;
        }
    }

    protected void onSlidingRefresh(int level) {
        super.onSlidingRefresh(level);
        float scale = ((float) level) / 10000.0f;
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
    }

    public void setDefaultColorForVolumePanel(boolean isClearCoverOpened) {
        if (isClearCoverOpened) {
            this.DEFAULT_NORMAL_PROGRESS_COLOR = colorToColorStateList(Color.parseColor("#ffe3e0e0"));
            this.DEFAULT_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(Color.parseColor("#ff56c0e5"));
            this.DEFAULT_ACTIVATED_THUMB_COLOR = colorToColorStateList(Color.parseColor("#ff56c0e5"));
            this.OVERLAP_NORMAL_PROGRESS_COLOR = colorToColorStateList(Color.parseColor("#fff7cdbd"));
            this.OVERLAP_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(Color.parseColor("#fff1662f"));
            this.OVERLAP_ACTIVATED_THUMB_COLOR = colorToColorStateList(Color.parseColor("#fff1662f"));
            return;
        }
        this.DEFAULT_NORMAL_PROGRESS_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_progress_color_control_normal));
        this.DEFAULT_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_progress_color_control_activated));
        this.DEFAULT_ACTIVATED_THUMB_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_thumb_color_control_activated));
        this.OVERLAP_NORMAL_PROGRESS_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_progress_color_overlap_normal));
        this.OVERLAP_ACTIVATED_PROGRESS_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_progress_color_overlap_activated));
        this.OVERLAP_ACTIVATED_THUMB_COLOR = colorToColorStateList(this.mContext.getResources().getColor(R.color.tw_progress_color_overlap_activated));
    }

    public void setFluidEnabled(boolean enabled) {
        if (Settings$System.getString(this.mContext.getContentResolver(), "current_sec_active_themepackage") != null) {
            this.mIsOpenTheme = true;
        } else {
            this.mIsOpenTheme = false;
        }
        if (!SCAFE_ENABLED || this.mIsOpenTheme) {
            this.mIsFluidEnabled = false;
        } else {
            this.mIsFluidEnabled = enabled;
        }
        if (this.mIsFluidEnabled) {
            seekBarFluidInit();
        }
    }

    public boolean isFluidEnabled() {
        return this.mIsFluidEnabled;
    }

    public void setFluidColor(int color) {
        if (this.paint == null) {
            this.paint = new Paint();
        }
        this.paint.setColor(color);
    }

    private void setThumbOverlapTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
        if (!isFluidEnabled()) {
            return;
        }
        if (this.mThumbTintList == null) {
            setFluidColor(0);
        } else {
            setFluidColor(this.mThumbTintList.getDefaultColor());
        }
    }

    public void setProgressTintList(ColorStateList tint) {
        super.setProgressTintList(tint);
        this.DEFAULT_ACTIVATED_PROGRESS_COLOR = tint;
    }

    private void setProgressOverlapTintList(ColorStateList tint) {
        super.setProgressTintList(tint);
    }
}
