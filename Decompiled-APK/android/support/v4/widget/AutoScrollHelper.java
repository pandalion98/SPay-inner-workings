package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import org.bouncycastle.math.ec.ECFieldElement.F2m;

public abstract class AutoScrollHelper implements OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY;
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    private boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    private boolean mNeedsCancel;
    private boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    private final ClampedScroller mScroller;
    private final View mTarget;

    private static class ClampedScroller {
        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        public ClampedScroller() {
            this.mStartTime = Long.MIN_VALUE;
            this.mStopTime = -1;
            this.mDeltaTime = 0;
            this.mDeltaX = AutoScrollHelper.HORIZONTAL;
            this.mDeltaY = AutoScrollHelper.HORIZONTAL;
        }

        public void setRampUpDuration(int i) {
            this.mRampUpDuration = i;
        }

        public void setRampDownDuration(int i) {
            this.mRampDownDuration = i;
        }

        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = AutoScrollHelper.HORIZONTAL;
            this.mDeltaY = AutoScrollHelper.HORIZONTAL;
        }

        public void requestStop() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentAnimationTimeMillis - this.mStartTime), (int) AutoScrollHelper.HORIZONTAL, this.mRampDownDuration);
            this.mStopValue = getValueAt(currentAnimationTimeMillis);
            this.mStopTime = currentAnimationTimeMillis;
        }

        public boolean isFinished() {
            return this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
        }

        private float getValueAt(long j) {
            if (j < this.mStartTime) {
                return AutoScrollHelper.RELATIVE_UNSPECIFIED;
            }
            if (this.mStopTime < 0 || j < this.mStopTime) {
                return AutoScrollHelper.constrain(((float) (j - this.mStartTime)) / ((float) this.mRampUpDuration), (float) AutoScrollHelper.RELATIVE_UNSPECIFIED, (float) AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY) * 0.5f;
            }
            long j2 = j - this.mStopTime;
            return (AutoScrollHelper.constrain(((float) j2) / ((float) this.mEffectiveRampDown), (float) AutoScrollHelper.RELATIVE_UNSPECIFIED, (float) AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY) * this.mStopValue) + (AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY - this.mStopValue);
        }

        private float interpolateValue(float f) {
            return ((-4.0f * f) * f) + (4.0f * f);
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float interpolateValue = interpolateValue(getValueAt(currentAnimationTimeMillis));
            long j = currentAnimationTimeMillis - this.mDeltaTime;
            this.mDeltaTime = currentAnimationTimeMillis;
            this.mDeltaX = (int) ((((float) j) * interpolateValue) * this.mTargetVelocityX);
            this.mDeltaY = (int) ((((float) j) * interpolateValue) * this.mTargetVelocityY);
        }

        public void setTargetVelocity(float f, float f2) {
            this.mTargetVelocityX = f;
            this.mTargetVelocityY = f2;
        }

        public int getHorizontalDirection() {
            return (int) (this.mTargetVelocityX / Math.abs(this.mTargetVelocityX));
        }

        public int getVerticalDirection() {
            return (int) (this.mTargetVelocityY / Math.abs(this.mTargetVelocityY));
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }
    }

    private class ScrollAnimationRunnable implements Runnable {
        private ScrollAnimationRunnable() {
        }

        public void run() {
            if (AutoScrollHelper.this.mAnimating) {
                if (AutoScrollHelper.this.mNeedsReset) {
                    AutoScrollHelper.this.mNeedsReset = false;
                    AutoScrollHelper.this.mScroller.start();
                }
                ClampedScroller access$300 = AutoScrollHelper.this.mScroller;
                if (access$300.isFinished() || !AutoScrollHelper.this.shouldAnimate()) {
                    AutoScrollHelper.this.mAnimating = false;
                    return;
                }
                if (AutoScrollHelper.this.mNeedsCancel) {
                    AutoScrollHelper.this.mNeedsCancel = false;
                    AutoScrollHelper.this.cancelTargetTouch();
                }
                access$300.computeScrollDelta();
                AutoScrollHelper.this.scrollTargetBy(access$300.getDeltaX(), access$300.getDeltaY());
                ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
            }
        }
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    public abstract void scrollTargetBy(int i, int i2);

    static {
        DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    }

    public AutoScrollHelper(View view) {
        this.mScroller = new ClampedScroller();
        this.mEdgeInterpolator = new AccelerateInterpolator();
        this.mRelativeEdges = new float[]{RELATIVE_UNSPECIFIED, RELATIVE_UNSPECIFIED};
        this.mMaximumEdges = new float[]{NO_MAX, NO_MAX};
        this.mRelativeVelocity = new float[]{RELATIVE_UNSPECIFIED, RELATIVE_UNSPECIFIED};
        this.mMinimumVelocity = new float[]{RELATIVE_UNSPECIFIED, RELATIVE_UNSPECIFIED};
        this.mMaximumVelocity = new float[]{NO_MAX, NO_MAX};
        this.mTarget = view;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int i = (int) ((1575.0f * displayMetrics.density) + 0.5f);
        int i2 = (int) ((displayMetrics.density * 315.0f) + 0.5f);
        setMaximumVelocity((float) i, (float) i);
        setMinimumVelocity((float) i2, (float) i2);
        setEdgeType(VERTICAL);
        setMaximumEdges(NO_MAX, NO_MAX);
        setRelativeEdges(DEFAULT_RELATIVE_EDGE, DEFAULT_RELATIVE_EDGE);
        setRelativeVelocity(DEFAULT_RELATIVE_VELOCITY, DEFAULT_RELATIVE_VELOCITY);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(DEFAULT_RAMP_UP_DURATION);
        setRampDownDuration(DEFAULT_RAMP_UP_DURATION);
    }

    public AutoScrollHelper setEnabled(boolean z) {
        if (this.mEnabled && !z) {
            requestStop();
        }
        this.mEnabled = z;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public AutoScrollHelper setExclusive(boolean z) {
        this.mExclusive = z;
        return this;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f2) {
        this.mMaximumVelocity[HORIZONTAL] = f / 1000.0f;
        this.mMaximumVelocity[VERTICAL] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f2) {
        this.mMinimumVelocity[HORIZONTAL] = f / 1000.0f;
        this.mMinimumVelocity[VERTICAL] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f2) {
        this.mRelativeVelocity[HORIZONTAL] = f / 1000.0f;
        this.mRelativeVelocity[VERTICAL] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setEdgeType(int i) {
        this.mEdgeType = i;
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f2) {
        this.mRelativeEdges[HORIZONTAL] = f;
        this.mRelativeEdges[VERTICAL] = f2;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f2) {
        this.mMaximumEdges[HORIZONTAL] = f;
        this.mMaximumEdges[VERTICAL] = f2;
        return this;
    }

    public AutoScrollHelper setActivationDelay(int i) {
        this.mActivationDelay = i;
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int i) {
        this.mScroller.setRampUpDuration(i);
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int i) {
        this.mScroller.setRampDownDuration(i);
        return this;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z = true;
        if (!this.mEnabled) {
            return false;
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case HORIZONTAL /*0*/:
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
                break;
            case VERTICAL /*1*/:
            case F2m.PPB /*3*/:
                requestStop();
                break;
            case EDGE_TYPE_OUTSIDE /*2*/:
                break;
        }
        this.mScroller.setTargetVelocity(computeTargetVelocity(HORIZONTAL, motionEvent.getX(), (float) view.getWidth(), (float) this.mTarget.getWidth()), computeTargetVelocity(VERTICAL, motionEvent.getY(), (float) view.getHeight(), (float) this.mTarget.getHeight()));
        if (!this.mAnimating && shouldAnimate()) {
            startAnimating();
        }
        if (!(this.mExclusive && this.mAnimating)) {
            z = false;
        }
        return z;
    }

    private boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int verticalDirection = clampedScroller.getVerticalDirection();
        int horizontalDirection = clampedScroller.getHorizontalDirection();
        return (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
    }

    private void startAnimating() {
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (this.mAlreadyDelayed || this.mActivationDelay <= 0) {
            this.mRunnable.run();
        } else {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, (long) this.mActivationDelay);
        }
        this.mAlreadyDelayed = true;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private float computeTargetVelocity(int i, float f, float f2, float f3) {
        float edgeValue = getEdgeValue(this.mRelativeEdges[i], f2, this.mMaximumEdges[i], f);
        if (edgeValue == RELATIVE_UNSPECIFIED) {
            return RELATIVE_UNSPECIFIED;
        }
        float f4 = this.mRelativeVelocity[i];
        float f5 = this.mMinimumVelocity[i];
        float f6 = this.mMaximumVelocity[i];
        f4 *= f3;
        if (edgeValue > RELATIVE_UNSPECIFIED) {
            return constrain(edgeValue * f4, f5, f6);
        }
        return -constrain((-edgeValue) * f4, f5, f6);
    }

    private float getEdgeValue(float f, float f2, float f3, float f4) {
        float f5;
        float constrain = constrain(f * f2, (float) RELATIVE_UNSPECIFIED, f3);
        constrain = constrainEdgeValue(f2 - f4, constrain) - constrainEdgeValue(f4, constrain);
        if (constrain < RELATIVE_UNSPECIFIED) {
            f5 = -this.mEdgeInterpolator.getInterpolation(-constrain);
        } else if (constrain <= RELATIVE_UNSPECIFIED) {
            return RELATIVE_UNSPECIFIED;
        } else {
            f5 = this.mEdgeInterpolator.getInterpolation(constrain);
        }
        return constrain(f5, (float) GroundOverlayOptions.NO_DIMENSION, (float) DEFAULT_RELATIVE_VELOCITY);
    }

    private float constrainEdgeValue(float f, float f2) {
        if (f2 == RELATIVE_UNSPECIFIED) {
            return RELATIVE_UNSPECIFIED;
        }
        switch (this.mEdgeType) {
            case HORIZONTAL /*0*/:
            case VERTICAL /*1*/:
                if (f >= f2) {
                    return RELATIVE_UNSPECIFIED;
                }
                if (f >= RELATIVE_UNSPECIFIED) {
                    return DEFAULT_RELATIVE_VELOCITY - (f / f2);
                }
                if (this.mAnimating && this.mEdgeType == VERTICAL) {
                    return DEFAULT_RELATIVE_VELOCITY;
                }
                return RELATIVE_UNSPECIFIED;
            case EDGE_TYPE_OUTSIDE /*2*/:
                if (f < RELATIVE_UNSPECIFIED) {
                    return f / (-f2);
                }
                return RELATIVE_UNSPECIFIED;
            default:
                return RELATIVE_UNSPECIFIED;
        }
    }

    private static int constrain(int i, int i2, int i3) {
        if (i > i3) {
            return i3;
        }
        if (i < i2) {
            return i2;
        }
        return i;
    }

    private static float constrain(float f, float f2, float f3) {
        if (f > f3) {
            return f3;
        }
        if (f < f2) {
            return f2;
        }
        return f;
    }

    private void cancelTargetTouch() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, RELATIVE_UNSPECIFIED, RELATIVE_UNSPECIFIED, HORIZONTAL);
        this.mTarget.onTouchEvent(obtain);
        obtain.recycle();
    }
}
