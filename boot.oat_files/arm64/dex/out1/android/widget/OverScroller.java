package android.widget;

import android.content.Context;
import android.os.DVFSHelper;
import android.util.BoostFramework;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.android.internal.R;
import java.lang.reflect.Array;

public class OverScroller {
    private static final int DEFAULT_DURATION = 250;
    private static final int FLING_MODE = 1;
    private static final int SCROLL_MODE = 0;
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    private final SplineOverScroller mScrollerY;

    static class SplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
        private static final int DEFAULT_MODE = 1;
        private static final float DISTANCE_M1 = 3.0f;
        private static final float DISTANCE_M2 = 1.5f;
        private static final float DURATION_M1 = 3.0f;
        private static final float DURATION_M2 = 1.8f;
        private static final float END_TENSION = 1.0f;
        private static final float GRAVITY = 2000.0f;
        private static final long HIGHER_TIME_GAP_COMPENSATION = 1;
        private static final long HIGHER_TIME_GAP_MARGIN = 1;
        private static float INFLEXION = INFLEXIONS[1];
        private static final float[] INFLEXIONS = new float[]{0.35f, 0.22f};
        private static final long LOWER_TIME_GAP_COMPENSATION = 1;
        private static final long LOWER_TIME_GAP_MARGIN = 1;
        private static final int MARGIN_COMPENSATION_STARTING_COUNT = 30;
        private static final int NB_SAMPLES = 100;
        public static final int ORIGINAL_MODE = 0;
        public static final int SMOOTH_MODE = 1;
        private static final int SPLINE = 0;
        private static float[] SPLINE_POSITION = SPLINE_POSITIONS[1];
        private static final float[][] SPLINE_POSITIONS = ((float[][]) Array.newInstance(Float.TYPE, new int[]{2, 101}));
        private static float[] SPLINE_TIME = SPLINE_TIMES[1];
        private static final float[][] SPLINE_TIMES = ((float[][]) Array.newInstance(Float.TYPE, new int[]{2, 101}));
        private static final float START_TENSION = 0.5f;
        private static boolean mEnableSmoothFling = true;
        private static boolean mRegulateCurrentTimeInterval = true;
        private int[] fBoostParamVal;
        private int fBoostTimeOut = 0;
        private int flingBoostTimeOut = 0;
        private float mCurrVelocity;
        private int mCurrentPosition;
        private float mDeceleration;
        private int mDuration;
        private int mFinal;
        private boolean mFinished = true;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private boolean mIsDVFSBoosting = false;
        private boolean mIsPerfBoostEnabled = false;
        private boolean mIsPerfLockAcquired = false;
        private int mMaximumVelocity;
        private int mOver;
        private BoostFramework mPerf = null;
        private float mPhysicalCoeff;
        private long mPrevTime = 0;
        private long mPrevTimeGap = 0;
        private int mSplineDistance;
        private int mSplineDuration;
        private int mStart;
        private long mStartTime;
        private int mState = 0;
        private int mUpdateCount = 0;
        private int mVelocity;

        static {
            for (int mode = 0; mode < 2; mode++) {
                float P1 = START_TENSION * INFLEXIONS[mode];
                float P2 = 1.0f - (1.0f * (1.0f - INFLEXIONS[mode]));
                float x_min = 0.0f;
                float y_min = 0.0f;
                for (int i = 0; i < 100; i++) {
                    float x;
                    float coef;
                    float y;
                    float alpha = ((float) i) / 100.0f;
                    float x_max = 1.0f;
                    while (true) {
                        x = x_min + ((x_max - x_min) / 2.0f);
                        coef = (3.0f * x) * (1.0f - x);
                        float tx = ((((1.0f - x) * P1) + (x * P2)) * coef) + ((x * x) * x);
                        if (((double) Math.abs(tx - alpha)) < 1.0E-5d) {
                            break;
                        } else if (tx > alpha) {
                            x_max = x;
                        } else {
                            x_min = x;
                        }
                    }
                    SPLINE_POSITIONS[mode][i] = ((((1.0f - x) * START_TENSION) + x) * coef) + ((x * x) * x);
                    float y_max = 1.0f;
                    while (true) {
                        y = y_min + ((y_max - y_min) / 2.0f);
                        coef = (3.0f * y) * (1.0f - y);
                        float dy = ((((1.0f - y) * START_TENSION) + y) * coef) + ((y * y) * y);
                        if (((double) Math.abs(dy - alpha)) < 1.0E-5d) {
                            break;
                        } else if (dy > alpha) {
                            y_max = y;
                        } else {
                            y_min = y;
                        }
                    }
                    SPLINE_TIMES[mode][i] = ((((1.0f - y) * P1) + (y * P2)) * coef) + ((y * y) * y);
                }
                float[] fArr = SPLINE_POSITIONS[mode];
                SPLINE_TIMES[mode][100] = 1.0f;
                fArr[100] = 1.0f;
            }
        }

        public void setMode(int mode) {
            if (mode >= 0 && mode <= 1) {
                if (mode == 0) {
                    mEnableSmoothFling = false;
                    mRegulateCurrentTimeInterval = false;
                } else {
                    mEnableSmoothFling = true;
                    mRegulateCurrentTimeInterval = true;
                }
                INFLEXION = INFLEXIONS[mode];
                SPLINE_POSITION = SPLINE_POSITIONS[mode];
                SPLINE_TIME = SPLINE_TIMES[mode];
            }
        }

        void setFriction(float friction) {
            this.mFlingFriction = friction;
        }

        SplineOverScroller(Context context) {
            this.mPhysicalCoeff = (386.0878f * (context.getResources().getDisplayMetrics().density * 160.0f)) * 0.84f;
            if (this.mIsPerfBoostEnabled) {
                this.fBoostTimeOut = context.getResources().getInteger(R.integer.flingboost_timeout_param);
                this.fBoostParamVal = context.getResources().getIntArray(R.array.flingboost_param_value);
            }
            if (mEnableSmoothFling) {
                this.mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
            }
            if (this.mPerf == null && this.mIsPerfBoostEnabled) {
                this.mPerf = new BoostFramework();
            }
        }

        void updateScroll(float q) {
            this.mCurrentPosition = this.mStart + Math.round(((float) (this.mFinal - this.mStart)) * q);
        }

        private static float getDeceleration(int velocity) {
            return velocity > 0 ? -2000.0f : GRAVITY;
        }

        private void adjustDuration(int start, int oldFinal, int newFinal) {
            float x = Math.abs(((float) (newFinal - start)) / ((float) (oldFinal - start)));
            int index = (int) (100.0f * x);
            if (index < 100) {
                float x_inf = ((float) index) / 100.0f;
                float x_sup = ((float) (index + 1)) / 100.0f;
                float t_inf = SPLINE_TIME[index];
                this.mDuration = (int) (((float) this.mDuration) * (t_inf + (((x - x_inf) / (x_sup - x_inf)) * (SPLINE_TIME[index + 1] - t_inf))));
            }
        }

        void startScroll(int start, int distance, int duration) {
            this.mFinished = false;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mFinal = start + distance;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        void finish() {
            if (this.mIsDVFSBoosting) {
                DVFSHelper.onSmoothScrollEvent(false);
                this.mIsDVFSBoosting = false;
            }
            if (this.mIsPerfLockAcquired && this.mPerf != null) {
                this.mPerf.perfLockRelease();
                this.mIsPerfLockAcquired = false;
            }
            this.mCurrentPosition = this.mFinal;
            this.mFinished = true;
        }

        void setFinalPosition(int position) {
            this.mFinal = position;
            this.mFinished = false;
        }

        void extendDuration(int extend) {
            this.mDuration = ((int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) + extend;
            this.mFinished = false;
        }

        boolean springback(int start, int min, int max) {
            this.mFinished = true;
            this.mFinal = start;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (start < min) {
                startSpringback(start, min, 0);
            } else if (start > max) {
                startSpringback(start, max, 0);
            }
            if (this.mFinished) {
                return false;
            }
            return true;
        }

        private void startSpringback(int start, int end, int velocity) {
            this.mFinished = false;
            this.mState = 1;
            this.mStart = start;
            this.mCurrentPosition = start;
            this.mFinal = end;
            int delta = start - end;
            this.mDeceleration = getDeceleration(delta);
            this.mVelocity = -delta;
            this.mOver = Math.abs(delta);
            this.mDuration = (int) (1000.0d * Math.sqrt((-2.0d * ((double) delta)) / ((double) this.mDeceleration)));
        }

        void fling(int start, int velocity, int min, int max, int over) {
            this.mOver = over;
            this.mFinished = false;
            this.mVelocity = velocity;
            this.mCurrVelocity = (float) velocity;
            this.mSplineDuration = 0;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStart = start;
            this.mCurrentPosition = start;
            if (start > max || start < min) {
                startAfterEdge(start, min, max, velocity);
                return;
            }
            this.mState = 0;
            double totalDistance = 0.0d;
            if (velocity != 0) {
                int splineFlingDuration = getSplineFlingDuration(velocity);
                this.mSplineDuration = splineFlingDuration;
                this.mDuration = splineFlingDuration;
                totalDistance = getSplineFlingDistance(velocity);
                if (!this.mIsDVFSBoosting && (velocity >= 800 || velocity <= -800)) {
                    DVFSHelper.onSmoothScrollEvent(true);
                    this.mIsDVFSBoosting = true;
                }
                if (this.mPerf != null) {
                    this.mIsPerfLockAcquired = true;
                    if (this.fBoostTimeOut == 0) {
                        this.flingBoostTimeOut = this.mDuration;
                    } else {
                        this.flingBoostTimeOut = this.fBoostTimeOut;
                    }
                    this.mPerf.perfLockAcquire(this.flingBoostTimeOut, this.fBoostParamVal);
                }
            }
            this.mSplineDistance = (int) (((double) Math.signum((float) velocity)) * totalDistance);
            this.mFinal = this.mSplineDistance + start;
            if (this.mFinal < min) {
                adjustDuration(this.mStart, this.mFinal, min);
                this.mFinal = min;
            }
            if (this.mFinal > max) {
                adjustDuration(this.mStart, this.mFinal, max);
                this.mFinal = max;
            }
            if (mRegulateCurrentTimeInterval) {
                this.mUpdateCount = 0;
            }
        }

        private double getSplineDeceleration(int velocity) {
            return Math.log((double) ((INFLEXION * ((float) Math.abs(velocity))) / (this.mFlingFriction * this.mPhysicalCoeff)));
        }

        private double getSplineFlingDistance(int velocity) {
            double l = getSplineDeceleration(velocity);
            double decelMinusOne = ((double) DECELERATION_RATE) - 1.0d;
            if (!mEnableSmoothFling) {
                return ((double) (this.mFlingFriction * this.mPhysicalCoeff)) * Math.exp((((double) DECELERATION_RATE) / decelMinusOne) * l);
            }
            int index = (int) (100.0f * (((float) Math.abs(velocity)) / ((float) this.mMaximumVelocity)));
            if (index > 100) {
                index = 100;
            }
            return ((((double) this.mFlingFriction) * ((double) ((3.0f * (1.0f - SPLINE_POSITION[index])) + DISTANCE_M2))) * ((double) this.mPhysicalCoeff)) * Math.exp((((double) DECELERATION_RATE) / decelMinusOne) * l);
        }

        private int getSplineFlingDuration(int velocity) {
            double l = getSplineDeceleration(velocity);
            double decelMinusOne = ((double) DECELERATION_RATE) - 1.0d;
            if (!mEnableSmoothFling) {
                return (int) (1000.0d * Math.exp(l / decelMinusOne));
            }
            int index = (int) (100.0f * (((float) Math.abs(velocity)) / ((float) this.mMaximumVelocity)));
            if (index > 100) {
                index = 100;
            }
            return (int) ((1000.0d * ((double) ((3.0f * (1.0f - SPLINE_POSITION[index])) + DURATION_M2))) * Math.exp(l / decelMinusOne));
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            float totalDuration = (float) Math.sqrt((2.0d * ((double) ((((((float) velocity) * ((float) velocity)) / 2.0f) / Math.abs(this.mDeceleration)) + ((float) Math.abs(end - start))))) / ((double) Math.abs(this.mDeceleration)));
            this.mStartTime -= (long) ((int) (1000.0f * (totalDuration - (((float) (-velocity)) / this.mDeceleration))));
            this.mStart = end;
            this.mCurrentPosition = end;
            this.mVelocity = (int) ((-this.mDeceleration) * totalDuration);
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            int i;
            if (velocity == 0) {
                i = start - end;
            } else {
                i = velocity;
            }
            this.mDeceleration = getDeceleration(i);
            fitOnBounceCurve(start, end, velocity);
            onEdgeReached();
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            if (start <= min || start >= max) {
                int edge;
                boolean positive = start > max;
                if (positive) {
                    edge = max;
                } else {
                    edge = min;
                }
                int overDistance = start - edge;
                if (overDistance * velocity >= 0) {
                    startBounceAfterEdge(start, edge, velocity);
                    return;
                } else if (getSplineFlingDistance(velocity) > ((double) Math.abs(overDistance))) {
                    fling(start, velocity, positive ? min : start, positive ? start : max, this.mOver);
                    return;
                } else {
                    startSpringback(start, edge, velocity);
                    return;
                }
            }
            Log.e("OverScroller", "startAfterEdge called from a valid position");
            this.mFinished = true;
        }

        void notifyEdgeReached(int start, int end, int over) {
            if (this.mState == 0) {
                this.mOver = over;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                startAfterEdge(start, end, end, (int) this.mCurrVelocity);
            }
        }

        private void onEdgeReached() {
            float velocitySquared = ((float) this.mVelocity) * ((float) this.mVelocity);
            float distance = velocitySquared / (Math.abs(this.mDeceleration) * 2.0f);
            float sign = Math.signum((float) this.mVelocity);
            if (distance > ((float) this.mOver)) {
                this.mDeceleration = ((-sign) * velocitySquared) / (((float) this.mOver) * 2.0f);
                distance = (float) this.mOver;
            }
            this.mOver = (int) distance;
            this.mState = 2;
            int i = this.mStart;
            if (this.mVelocity <= 0) {
                distance = -distance;
            }
            this.mFinal = i + ((int) distance);
            this.mDuration = -((int) ((1000.0f * ((float) this.mVelocity)) / this.mDeceleration));
            if (mRegulateCurrentTimeInterval) {
                this.mUpdateCount = 0;
            }
        }

        boolean continueWhenFinished() {
            switch (this.mState) {
                case 0:
                    if (this.mDuration < this.mSplineDuration) {
                        int i = this.mFinal;
                        this.mStart = i;
                        this.mCurrentPosition = i;
                        this.mVelocity = (int) this.mCurrVelocity;
                        this.mDeceleration = getDeceleration(this.mVelocity);
                        this.mStartTime += (long) this.mDuration;
                        onEdgeReached();
                        break;
                    }
                    return false;
                case 1:
                    return false;
                case 2:
                    this.mStartTime += (long) this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                    break;
            }
            update();
            return true;
        }

        boolean update() {
            long currentTime = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
            long originalCurrentTime = currentTime;
            if (mRegulateCurrentTimeInterval && this.mState == 0) {
                if (this.mUpdateCount > 0) {
                    currentTime = (this.mPrevTime + currentTime) / 2;
                }
                if (this.mUpdateCount > 30) {
                    long currentTimeGap = currentTime - this.mPrevTime;
                    if (currentTimeGap > this.mPrevTimeGap + 1) {
                        currentTime = (this.mPrevTime + this.mPrevTimeGap) + 1;
                    } else if (currentTimeGap < this.mPrevTimeGap - 1) {
                        currentTime = (this.mPrevTime + this.mPrevTimeGap) - 1;
                    }
                }
                if (currentTime < 0) {
                    currentTime = 0;
                }
                this.mPrevTimeGap = currentTime - this.mPrevTime;
                this.mPrevTime = currentTime;
                this.mUpdateCount++;
            }
            if (currentTime == 0) {
                if (this.mDuration > 0) {
                    return true;
                }
                return false;
            } else if (currentTime > ((long) this.mDuration)) {
                return false;
            } else {
                double distance = 0.0d;
                float t;
                switch (this.mState) {
                    case 0:
                        t = ((float) currentTime) / ((float) this.mSplineDuration);
                        int index = (int) (100.0f * t);
                        float distanceCoef = 1.0f;
                        float velocityCoef = 0.0f;
                        if (index < 100) {
                            float t_inf = ((float) index) / 100.0f;
                            float t_sup = ((float) (index + 1)) / 100.0f;
                            float d_inf = SPLINE_POSITION[index];
                            velocityCoef = (SPLINE_POSITION[index + 1] - d_inf) / (t_sup - t_inf);
                            distanceCoef = d_inf + ((t - t_inf) * velocityCoef);
                        }
                        distance = (double) (((float) this.mSplineDistance) * distanceCoef);
                        this.mCurrVelocity = ((((float) this.mSplineDistance) * velocityCoef) / ((float) this.mSplineDuration)) * 1000.0f;
                        break;
                    case 1:
                        t = ((float) currentTime) / ((float) this.mDuration);
                        float t2 = t * t;
                        float sign = Math.signum((float) this.mVelocity);
                        distance = (double) ((((float) this.mOver) * sign) * ((3.0f * t2) - ((2.0f * t) * t2)));
                        this.mCurrVelocity = ((((float) this.mOver) * sign) * 6.0f) * ((-t) + t2);
                        break;
                    case 2:
                        t = ((float) currentTime) / 1000.0f;
                        this.mCurrVelocity = ((float) this.mVelocity) + (this.mDeceleration * t);
                        distance = (double) ((((float) this.mVelocity) * t) + (((this.mDeceleration * t) * t) / 2.0f));
                        break;
                }
                this.mCurrentPosition = this.mStart + ((int) Math.round(distance));
                return true;
            }
        }
    }

    public OverScroller(Context context) {
        this(context, null);
    }

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, boolean flywheel) {
        if (interpolator == null) {
            this.mInterpolator = new ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mFlywheel = flywheel;
        this.mScrollerX = new SplineOverScroller(context);
        this.mScrollerY = new SplineOverScroller(context);
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, float bounceCoefficientX, float bounceCoefficientY, boolean flywheel) {
        this(context, interpolator, flywheel);
    }

    void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            this.mInterpolator = new ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
    }

    public final void setFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mScrollerX.mFinished = this.mScrollerY.mFinished = finished;
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot((double) this.mScrollerX.mCurrVelocity, (double) this.mScrollerY.mCurrVelocity);
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    @Deprecated
    public final int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    @Deprecated
    public void extendDuration(int extend) {
        this.mScrollerX.extendDuration(extend);
        this.mScrollerY.extendDuration(extend);
    }

    @Deprecated
    public void setFinalX(int newX) {
        this.mScrollerX.setFinalPosition(newX);
    }

    @Deprecated
    public void setFinalY(int newY) {
        this.mScrollerY.setFinalPosition(newY);
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        switch (this.mMode) {
            case 0:
                long elapsedTime = AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime;
                int duration = this.mScrollerX.mDuration;
                if (elapsedTime >= ((long) duration)) {
                    abortAnimation();
                    break;
                }
                float q = this.mInterpolator.getInterpolation(((float) elapsedTime) / ((float) duration));
                this.mScrollerX.updateScroll(q);
                this.mScrollerY.updateScroll(q);
                break;
            case 1:
                if (!(this.mScrollerX.mFinished || this.mScrollerX.update() || this.mScrollerX.continueWhenFinished())) {
                    this.mScrollerX.finish();
                }
                if (!(this.mScrollerY.mFinished || this.mScrollerY.update() || this.mScrollerY.continueWhenFinished())) {
                    this.mScrollerY.finish();
                    break;
                }
        }
        return true;
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, 250);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mMode = 0;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mMode = 1;
        boolean spingbackX = this.mScrollerX.springback(startX, minX, maxX);
        boolean spingbackY = this.mScrollerY.springback(startY, minY, maxY);
        if (spingbackX || spingbackY) {
            return true;
        }
        return false;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    protected void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, boolean accDisabled) {
        fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0, accDisabled);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        if (this.mFlywheel && !isFinished()) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum((float) velocityX) == Math.signum(oldVelocityX) && Math.signum((float) velocityY) == Math.signum(oldVelocityY)) {
                velocityX = (int) (((float) velocityX) + oldVelocityX);
                velocityY = (int) (((float) velocityY) + oldVelocityY);
            }
        }
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY, minY, maxY, overY);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY, boolean accDisabled) {
        if (!(!this.mFlywheel || isFinished() || accDisabled)) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum((float) velocityX) == Math.signum(oldVelocityX) && Math.signum((float) velocityY) == Math.signum(oldVelocityY)) {
                velocityX = (int) (((float) velocityX) + oldVelocityX);
                velocityY = (int) (((float) velocityY) + oldVelocityY);
            }
        }
        this.mMode = 1;
        this.mScrollerX.fling(startX, velocityX, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY, minY, maxY, overY);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    public boolean isOverScrolled() {
        return ((this.mScrollerX.mFinished || this.mScrollerX.mState == 0) && (this.mScrollerY.mFinished || this.mScrollerY.mState == 0)) ? false : true;
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime));
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        return !isFinished() && Math.signum(xvel) == Math.signum((float) (this.mScrollerX.mFinal - this.mScrollerX.mStart)) && Math.signum(yvel) == Math.signum((float) (this.mScrollerY.mFinal - this.mScrollerY.mStart));
    }

    public void twSetSmoothScrollEnable(boolean enable) {
        int mode = enable ? 1 : 0;
        this.mScrollerX.setMode(mode);
        this.mScrollerY.setMode(mode);
    }
}
