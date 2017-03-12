package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.SystemClock;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import com.android.internal.R;

public class ScaleGestureDetector {
    private static final int ANCHORED_SCALE_MODE_DOUBLE_TAP = 1;
    private static final int ANCHORED_SCALE_MODE_NONE = 0;
    private static final int ANCHORED_SCALE_MODE_STYLUS = 2;
    private static final float SCALE_FACTOR = 0.5f;
    private static final String TAG = "ScaleGestureDetector";
    private static final long TOUCH_STABILIZE_TIME = 128;
    private float AREA_RATE_THRESHOLD;
    private float AREA_THRESHOLD;
    private int mAnchoredScaleMode;
    private float mAnchoredScaleStartX;
    private float mAnchoredScaleStartY;
    private boolean mAreaRateCalculating;
    private final Context mContext;
    private float mCurrLenBeforeSqrt;
    private float mCurrSpanX;
    private float mCurrSpanY;
    private long mCurrTime;
    private boolean mEventBeforeOrAboveStartingGestureEvent;
    private float mFocusX;
    private float mFocusY;
    private GestureDetector mGestureDetector;
    private final Handler mHandler;
    private boolean mInProgress;
    private final OnScaleGestureListener mListener;
    private float mPrevLenBeforeSqrt;
    private float mPrevSpanX;
    private float mPrevSpanY;
    private long mPrevTime;
    private boolean mQuickScaleEnabled;
    private final SaveState mStateCurrent;
    private boolean mStylusScaleEnabled;
    private float mTempLenBeforeSqrt;
    private int mTouchHistoryDirection;
    private float mTouchHistoryLastAccepted;
    private long mTouchHistoryLastAcceptedTime;
    private float mTouchLower;
    private int mTouchMinMajor;
    private float mTouchUpper;
    private boolean mUpdatePrevious;
    private boolean mUseTwoFingerSweep;

    public interface OnScaleGestureListener {
        boolean onScale(ScaleGestureDetector scaleGestureDetector);

        boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector);

        void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
    }

    static class SaveState {
        float mLenBeforeSqrt;
        float mSpanX;
        float mSpanY;
        float maxX;
        float maxY;
        float minX;
        float minY;

        public SaveState() {
            reset();
        }

        void reset() {
            this.maxY = 0.0f;
            this.maxX = 0.0f;
            this.minY = 0.0f;
            this.minX = 0.0f;
            this.mLenBeforeSqrt = 0.0f;
            this.mSpanX = 0.0f;
            this.mSpanY = 0.0f;
        }
    }

    public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener listener) {
        this(context, listener, null);
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener listener, Handler handler) {
        this.mUpdatePrevious = true;
        this.mAreaRateCalculating = false;
        this.mUseTwoFingerSweep = false;
        this.mTempLenBeforeSqrt = 0.0f;
        this.AREA_THRESHOLD = 6000.0f;
        this.AREA_RATE_THRESHOLD = 1.0f;
        this.mStateCurrent = new SaveState();
        this.mAnchoredScaleMode = 0;
        this.mContext = context;
        this.mListener = listener;
        Resources res = context.getResources();
        this.AREA_THRESHOLD *= res.getDisplayMetrics().density;
        this.mTouchMinMajor = res.getDimensionPixelSize(R.dimen.config_minScalingTouchMajor);
        this.mHandler = handler;
        int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        if (targetSdkVersion > 18) {
            setQuickScaleEnabled(true);
        }
        if (targetSdkVersion > 22) {
            setStylusScaleEnabled(true);
        }
    }

    private void addTouchHistory(MotionEvent ev) {
        long currentTime = SystemClock.uptimeMillis();
        int count = ev.getPointerCount();
        boolean accept = currentTime - this.mTouchHistoryLastAcceptedTime >= TOUCH_STABILIZE_TIME;
        float total = 0.0f;
        int sampleCount = 0;
        for (int i = 0; i < count; i++) {
            boolean hasLastAccepted = !Float.isNaN(this.mTouchHistoryLastAccepted);
            int historySize = ev.getHistorySize();
            int pointerSampleCount = historySize + 1;
            int h = 0;
            while (h < pointerSampleCount) {
                float major;
                if (h < historySize) {
                    major = ev.getHistoricalTouchMajor(i, h);
                } else {
                    major = ev.getTouchMajor(i);
                }
                if (major < ((float) this.mTouchMinMajor)) {
                    major = (float) this.mTouchMinMajor;
                }
                total += major;
                if (Float.isNaN(this.mTouchUpper) || major > this.mTouchUpper) {
                    this.mTouchUpper = major;
                }
                if (Float.isNaN(this.mTouchLower) || major < this.mTouchLower) {
                    this.mTouchLower = major;
                }
                if (hasLastAccepted) {
                    int directionSig = (int) Math.signum(major - this.mTouchHistoryLastAccepted);
                    if (directionSig != this.mTouchHistoryDirection || (directionSig == 0 && this.mTouchHistoryDirection == 0)) {
                        this.mTouchHistoryDirection = directionSig;
                        this.mTouchHistoryLastAcceptedTime = h < historySize ? ev.getHistoricalEventTime(h) : ev.getEventTime();
                        accept = false;
                    }
                }
                h++;
            }
            sampleCount += pointerSampleCount;
        }
        float avg = total / ((float) sampleCount);
        if (accept) {
            float newAccepted = ((this.mTouchUpper + this.mTouchLower) + avg) / 3.0f;
            this.mTouchUpper = (this.mTouchUpper + newAccepted) / 2.0f;
            this.mTouchLower = (this.mTouchLower + newAccepted) / 2.0f;
            this.mTouchHistoryLastAccepted = newAccepted;
            this.mTouchHistoryDirection = 0;
            this.mTouchHistoryLastAcceptedTime = ev.getEventTime();
        }
    }

    private void clearTouchHistory() {
        this.mTouchUpper = Float.NaN;
        this.mTouchLower = Float.NaN;
        this.mTouchHistoryLastAccepted = Float.NaN;
        this.mTouchHistoryDirection = 0;
        this.mTouchHistoryLastAcceptedTime = 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mCurrTime = event.getEventTime();
        int action = event.getActionMasked();
        if (this.mQuickScaleEnabled) {
            this.mGestureDetector.onTouchEvent(event);
        }
        boolean isStylusButtonDown = (event.getButtonState() & 32) != 0;
        boolean anchoredScaleCancelled = this.mAnchoredScaleMode == 2 && !isStylusButtonDown;
        boolean streamComplete = action == 1 || action == 3 || anchoredScaleCancelled;
        if (action == 0 || streamComplete) {
            if (this.mInProgress) {
                this.mListener.onScaleEnd(this);
                this.mInProgress = false;
                this.mAnchoredScaleMode = 0;
            } else if (inAnchoredScaleMode() && streamComplete) {
                this.mInProgress = false;
                this.mAnchoredScaleMode = 0;
            }
            if (streamComplete) {
                clearTouchHistory();
                return true;
            }
        }
        if (this.mInProgress) {
            if (action == 2 || action == 213) {
                getArea(event);
                if (this.mStateCurrent.mLenBeforeSqrt <= 0.0f) {
                    return true;
                }
                this.mCurrSpanX = this.mStateCurrent.mSpanX;
                this.mCurrSpanY = this.mStateCurrent.mSpanY;
                this.mCurrLenBeforeSqrt = this.mStateCurrent.mLenBeforeSqrt;
                this.mUpdatePrevious = this.mListener.onScale(this);
            } else {
                this.mListener.onScaleEnd(this);
                reset();
            }
            if (this.mUpdatePrevious) {
                this.mPrevSpanX = this.mCurrSpanX;
                this.mPrevSpanY = this.mCurrSpanY;
                this.mPrevLenBeforeSqrt = this.mCurrLenBeforeSqrt;
                this.mPrevTime = this.mCurrTime;
            }
        } else if (event.getPointerCount() == 4 || action == 1 || action == 3) {
            reset();
        } else {
            if (this.mStylusScaleEnabled && !inAnchoredScaleMode() && !streamComplete && isStylusButtonDown) {
                this.mAnchoredScaleStartX = event.getX();
                this.mAnchoredScaleStartY = event.getY();
                this.mAnchoredScaleMode = 2;
            }
            getArea(event);
            boolean configChanged = action == 0 || action == 6 || action == 5 || anchoredScaleCancelled;
            if (configChanged) {
                this.mCurrSpanX = this.mStateCurrent.mSpanX;
                this.mCurrSpanY = this.mStateCurrent.mSpanY;
                this.mCurrLenBeforeSqrt = this.mStateCurrent.mLenBeforeSqrt;
            }
            if (this.mStateCurrent.mLenBeforeSqrt > this.AREA_THRESHOLD) {
                if (!(this.mAreaRateCalculating || this.mUseTwoFingerSweep)) {
                    this.mTempLenBeforeSqrt = this.mStateCurrent.mLenBeforeSqrt;
                    this.mAreaRateCalculating = true;
                }
                if (this.mUseTwoFingerSweep) {
                    this.mPrevLenBeforeSqrt = this.mStateCurrent.mLenBeforeSqrt;
                }
                float areaRate = this.mAreaRateCalculating ? this.mStateCurrent.mLenBeforeSqrt > this.mTempLenBeforeSqrt ? this.mStateCurrent.mLenBeforeSqrt / this.mTempLenBeforeSqrt : this.mTempLenBeforeSqrt / this.mStateCurrent.mLenBeforeSqrt : this.mStateCurrent.mLenBeforeSqrt > this.mPrevLenBeforeSqrt ? this.mStateCurrent.mLenBeforeSqrt / this.mPrevLenBeforeSqrt : this.mPrevLenBeforeSqrt / this.mStateCurrent.mLenBeforeSqrt;
                boolean scaleDecision = this.mUseTwoFingerSweep ? areaRate >= this.AREA_RATE_THRESHOLD : this.mAreaRateCalculating && areaRate > this.AREA_RATE_THRESHOLD;
                if (scaleDecision) {
                    float f = this.mStateCurrent.mSpanX;
                    this.mCurrSpanX = f;
                    this.mPrevSpanX = f;
                    f = this.mStateCurrent.mSpanY;
                    this.mCurrSpanY = f;
                    this.mPrevSpanY = f;
                    this.mPrevTime = this.mCurrTime;
                    f = this.mStateCurrent.mLenBeforeSqrt;
                    this.mCurrLenBeforeSqrt = f;
                    this.mPrevLenBeforeSqrt = f;
                    this.mInProgress = this.mListener.onScaleBegin(this);
                    Log.i(TAG, "TwScaleGestureDetector");
                    this.mAreaRateCalculating = false;
                }
            } else if ((action == 2 || action == 213) && this.mUpdatePrevious) {
                this.mPrevSpanX = this.mCurrSpanX;
                this.mPrevSpanY = this.mCurrSpanY;
                this.mPrevLenBeforeSqrt = this.mCurrLenBeforeSqrt;
                this.mPrevTime = this.mCurrTime;
            }
        }
        return true;
    }

    private void getArea(MotionEvent event) {
        boolean bInitialized = false;
        float focusX = 0.0f;
        float focusY = 0.0f;
        this.mStateCurrent.reset();
        addTouchHistory(event);
        float x;
        float y;
        if (inAnchoredScaleMode()) {
            boolean z;
            x = event.getX();
            y = event.getY();
            focusX = this.mAnchoredScaleStartX;
            focusY = this.mAnchoredScaleStartY;
            this.mStateCurrent.mSpanX = (focusX > x ? focusX - x : x - focusX) + this.mTouchHistoryLastAccepted;
            this.mStateCurrent.mSpanY = (focusY > y ? focusY - y : y - focusY) + this.mTouchHistoryLastAccepted;
            this.mStateCurrent.mLenBeforeSqrt = this.mStateCurrent.mSpanY * this.mStateCurrent.mSpanY;
            if (y < focusY) {
                z = true;
            } else {
                z = false;
            }
            this.mEventBeforeOrAboveStartingGestureEvent = z;
        } else {
            int count = event.getPointerCount();
            for (int i = 0; i < count; i++) {
                x = event.getX(i);
                y = event.getY(i);
                if (bInitialized) {
                    if (this.mStateCurrent.minX > x) {
                        this.mStateCurrent.minX = x;
                    }
                    if (this.mStateCurrent.minY > y) {
                        this.mStateCurrent.minY = y;
                    }
                    if (this.mStateCurrent.maxX < x) {
                        this.mStateCurrent.maxX = x;
                    }
                    if (this.mStateCurrent.maxY < y) {
                        this.mStateCurrent.maxY = y;
                    }
                } else {
                    this.mStateCurrent.minX = x;
                    this.mStateCurrent.maxX = x;
                    this.mStateCurrent.minY = y;
                    this.mStateCurrent.maxY = y;
                    bInitialized = true;
                }
                focusX += x;
                focusY += y;
            }
            focusX /= (float) count;
            focusY /= (float) count;
            this.mStateCurrent.mSpanX = this.mStateCurrent.maxX - this.mStateCurrent.minX;
            this.mStateCurrent.mSpanY = this.mStateCurrent.maxY - this.mStateCurrent.minY;
            this.mStateCurrent.mLenBeforeSqrt = (this.mStateCurrent.mSpanX * this.mStateCurrent.mSpanX) + (this.mStateCurrent.mSpanY * this.mStateCurrent.mSpanY);
        }
        this.mFocusX = focusX;
        this.mFocusY = focusY;
    }

    private void reset() {
        this.mInProgress = false;
        this.mUpdatePrevious = true;
        this.mAreaRateCalculating = false;
        this.mAnchoredScaleMode = 0;
        clearTouchHistory();
    }

    private boolean inAnchoredScaleMode() {
        return this.mAnchoredScaleMode != 0;
    }

    public void setQuickScaleEnabled(boolean scales) {
        this.mQuickScaleEnabled = scales;
        if (this.mQuickScaleEnabled && this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(this.mContext, new SimpleOnGestureListener() {
                int mQuickScaleDoubleTapY;
                int mQuickScaleSpanSlop = ViewConfiguration.get(ScaleGestureDetector.this.mContext).getScaledTouchSlop();

                public boolean onDoubleTap(MotionEvent e) {
                    ScaleGestureDetector.this.mAnchoredScaleStartX = e.getX();
                    ScaleGestureDetector.this.mAnchoredScaleStartY = e.getY();
                    this.mQuickScaleDoubleTapY = (int) e.getY();
                    return true;
                }

                public boolean onDoubleTapEvent(MotionEvent e) {
                    if (((int) Math.abs(((float) this.mQuickScaleDoubleTapY) - e.getY())) > this.mQuickScaleSpanSlop) {
                        ScaleGestureDetector.this.mAnchoredScaleMode = 1;
                    }
                    return true;
                }
            }, this.mHandler);
            this.mGestureDetector.setIsLongpressEnabled(false);
        }
    }

    public boolean isQuickScaleEnabled() {
        return this.mQuickScaleEnabled;
    }

    public void setStylusScaleEnabled(boolean scales) {
        this.mStylusScaleEnabled = scales;
    }

    public boolean isStylusScaleEnabled() {
        return this.mStylusScaleEnabled;
    }

    public boolean isInProgress() {
        return this.mInProgress;
    }

    public boolean isEdgeZoomInProgress() {
        return false;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public float getCurrentSpan() {
        return FloatMath.sqrt(this.mCurrLenBeforeSqrt);
    }

    public float getCurrentSpanX() {
        return Math.abs(this.mCurrSpanX);
    }

    public float getCurrentSpanY() {
        return Math.abs(this.mCurrSpanY);
    }

    public float getPreviousSpan() {
        return FloatMath.sqrt(this.mPrevLenBeforeSqrt);
    }

    public float getPreviousSpanX() {
        return Math.abs(this.mPrevSpanX);
    }

    public float getPreviousSpanY() {
        return Math.abs(this.mPrevSpanY);
    }

    public float getScaleFactor() {
        if (inAnchoredScaleMode()) {
            boolean scaleUp = (this.mEventBeforeOrAboveStartingGestureEvent && this.mCurrLenBeforeSqrt < this.mPrevLenBeforeSqrt) || (!this.mEventBeforeOrAboveStartingGestureEvent && this.mCurrLenBeforeSqrt > this.mPrevLenBeforeSqrt);
            float spanDiff = Math.abs(1.0f - FloatMath.sqrt(this.mCurrLenBeforeSqrt / this.mPrevLenBeforeSqrt)) * SCALE_FACTOR;
            if (this.mPrevLenBeforeSqrt <= 0.0f) {
                return 1.0f;
            }
            return scaleUp ? 1.0f + spanDiff : 1.0f - spanDiff;
        } else if (this.mPrevLenBeforeSqrt > 0.0f) {
            return FloatMath.sqrt(this.mCurrLenBeforeSqrt / this.mPrevLenBeforeSqrt);
        } else {
            return 1.0f;
        }
    }

    public long getTimeDelta() {
        return this.mCurrTime - this.mPrevTime;
    }

    public long getEventTime() {
        return this.mCurrTime;
    }

    public void setAreaThreshold(float threshold) {
        this.AREA_THRESHOLD = threshold;
    }

    public float getAreaThreshold() {
        return this.AREA_THRESHOLD;
    }

    public void setAreaRateThreshold(float areaRate) {
        this.AREA_RATE_THRESHOLD = areaRate;
    }

    public float getAreaRateThreshold() {
        return this.AREA_RATE_THRESHOLD;
    }

    public boolean getEnableEdgeZoom() {
        return false;
    }

    public void setEnableEdgeZoom(boolean enabled) {
    }

    public void setUseTwoFingerSweep(boolean enabled) {
        this.mUseTwoFingerSweep = enabled;
    }
}
