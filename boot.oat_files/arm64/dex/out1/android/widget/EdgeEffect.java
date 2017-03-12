package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowFacade;

public class EdgeEffect {
    private static final float ABSORB_START_ALPHA = 0.11f;
    private static final double ANGLE = 0.5235987755982988d;
    private static final float COS = ((float) Math.cos(ANGLE));
    private static final float EDGE_CONTROL_POINT_HEIGHT_NON_TAB_IN_DIP = 29.0f;
    private static final float EDGE_CONTROL_POINT_HEIGHT_TAB_IN_DIP = 19.0f;
    private static final float EDGE_PADDING_NON_TAB_IN_DIP = 5.0f;
    private static final float EDGE_PADDING_TAB_IN_DIP = 3.0f;
    private static final float EPSILON = 0.001f;
    private static final float MAX_ALPHA = 0.15f;
    private static final float MAX_GLOW_SCALE = 2.0f;
    private static final int MAX_VELOCITY = 10000;
    private static final int MIN_VELOCITY = 100;
    private static final int PULL_DECAY_TIME = 2000;
    private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.4f;
    private static final float PULL_GLOW_BEGIN = 0.0f;
    private static final int PULL_TIME = 167;
    private static final int RECEDE_TIME = 600;
    private static final float SIN = ((float) Math.sin(ANGLE));
    private static final int STATE_ABSORB = 2;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PULL = 1;
    private static final int STATE_PULL_DECAY = 4;
    private static final int STATE_RECEDE = 3;
    private static final float TAB_HEIGHT_BUFFER_IN_DIP = 5.0f;
    private static final String TAG = "EdgeEffect";
    private static final int VELOCITY_GLOW_FACTOR = 6;
    private final Rect mBounds = new Rect();
    private float mDisplacement = 0.5f;
    private final DisplayMetrics mDisplayMetrics;
    private float mDuration;
    private float mEdgeControlPointHeight;
    private float mEdgePadding;
    private float mGlowAlpha;
    private float mGlowAlphaFinish;
    private float mGlowAlphaStart;
    private float mGlowScaleY;
    private float mGlowScaleYFinish;
    private float mGlowScaleYStart;
    private final Interpolator mInterpolator;
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private float mPullDistance;
    private long mStartTime;
    private int mState = 0;
    private final float mTabHeight;
    private final float mTabHeightBuffer;
    private float mTargetDisplacement = 0.5f;

    public EdgeEffect(Context context) {
        this.mPaint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.EdgeEffect);
        int themeColor = a.getColor(0, -10066330);
        a.recycle();
        this.mPaint.setColor((16777215 & themeColor) | 855638016);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        this.mInterpolator = new DecelerateInterpolator();
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        this.mTabHeight = dipToPixels(85.0f);
        this.mTabHeightBuffer = dipToPixels(5.0f);
    }

    private float dipToPixels(float dipValue) {
        return TypedValue.applyDimension(1, dipValue, this.mDisplayMetrics);
    }

    public void setSize(int width, int height) {
        float r = (((float) width) * 0.75f) / SIN;
        float h = r - (COS * r);
        float or = (((float) height) * 0.75f) / SIN;
        float oh = or - (COS * or);
        if (((float) width) <= this.mTabHeight + this.mTabHeightBuffer) {
            this.mEdgePadding = dipToPixels(EDGE_PADDING_TAB_IN_DIP);
            this.mEdgeControlPointHeight = dipToPixels(EDGE_CONTROL_POINT_HEIGHT_TAB_IN_DIP);
        } else {
            this.mEdgePadding = dipToPixels(5.0f);
            this.mEdgeControlPointHeight = dipToPixels(EDGE_CONTROL_POINT_HEIGHT_NON_TAB_IN_DIP);
        }
        this.mBounds.set(this.mBounds.left, this.mBounds.top, width, (int) Math.min((float) height, h));
    }

    public boolean isFinished() {
        return this.mState == 0;
    }

    public void finish() {
        this.mState = 0;
    }

    public void onPull(float deltaDistance) {
        onPull(deltaDistance, 0.5f);
    }

    public void onPull(float deltaDistance, float displacement) {
        long now = AnimationUtils.currentAnimationTimeMillis();
        this.mTargetDisplacement = displacement;
        if (this.mState != 4 || ((float) (now - this.mStartTime)) >= this.mDuration) {
            if (this.mState != 1) {
                this.mGlowScaleY = Math.max(0.0f, this.mGlowScaleY);
            }
            this.mState = 1;
            this.mStartTime = now;
            this.mDuration = 167.0f;
            this.mPullDistance += deltaDistance;
            float min = Math.min(MAX_ALPHA, this.mGlowAlpha + (PULL_DISTANCE_ALPHA_GLOW_FACTOR * Math.abs(deltaDistance)));
            this.mGlowAlphaStart = min;
            this.mGlowAlpha = min;
            if (this.mPullDistance == 0.0f) {
                this.mGlowScaleYStart = 0.0f;
                this.mGlowScaleY = 0.0f;
            } else {
                float scale = (float) (Math.max(0.0d, (1.0d - (1.0d / Math.sqrt((double) (Math.abs(this.mPullDistance) * ((float) this.mBounds.height()))))) - 0.3d) / 0.7d);
                this.mGlowScaleYStart = scale;
                this.mGlowScaleY = scale;
            }
            this.mGlowAlphaFinish = this.mGlowAlpha;
            this.mGlowScaleYFinish = this.mGlowScaleY;
        }
    }

    public void onRelease() {
        this.mPullDistance = 0.0f;
        if (this.mState == 1 || this.mState == 4) {
            this.mState = 3;
            this.mGlowAlphaStart = this.mGlowAlpha;
            this.mGlowScaleYStart = this.mGlowScaleY;
            this.mGlowAlphaFinish = 0.0f;
            this.mGlowScaleYFinish = 0.0f;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 600.0f;
        }
    }

    public void onAbsorb(int velocity) {
        this.mState = 2;
        velocity = Math.min(Math.max(100, Math.abs(velocity)), 10000);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mDuration = (((float) velocity) * 0.02f) + MAX_ALPHA;
        this.mGlowAlphaStart = ABSORB_START_ALPHA;
        this.mGlowScaleYStart = Math.max(this.mGlowScaleY, 0.0f);
        this.mGlowScaleYFinish = Math.min(0.025f + ((((float) ((velocity / 100) * velocity)) * 1.5E-4f) / MAX_GLOW_SCALE), 1.0f);
        this.mGlowAlphaFinish = Math.max(this.mGlowAlphaStart, Math.min(((float) (velocity * 6)) * 1.0E-5f, MAX_ALPHA));
        this.mTargetDisplacement = 0.5f;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public boolean draw(Canvas canvas) {
        update();
        int count = canvas.save();
        canvas.scale(1.0f, Math.min(this.mGlowScaleY, 1.0f), (float) this.mBounds.centerX(), 0.0f);
        float controlX = (((1.0f - 0.5f) / MAX_GLOW_SCALE) * ((float) this.mBounds.width())) + (((0.5f + (Math.max(0.0f, Math.min(this.mDisplacement, 1.0f)) - 0.5f)) * 0.5f) * ((float) this.mBounds.width()));
        float controlY = this.mEdgeControlPointHeight + this.mEdgePadding;
        float topDistance = ((float) this.mBounds.width()) * MultiWindowFacade.SPLIT_MIN_WEIGHT;
        this.mPath.reset();
        this.mPath.moveTo(0.0f, 0.0f);
        this.mPath.lineTo(0.0f, this.mEdgePadding);
        this.mPath.cubicTo(controlX - topDistance, controlY, controlX + topDistance, controlY, (float) this.mBounds.width(), this.mEdgePadding);
        this.mPath.lineTo((float) this.mBounds.width(), 0.0f);
        this.mPath.close();
        this.mPaint.setAlpha((int) (255.0f * this.mGlowAlpha));
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restoreToCount(count);
        boolean oneLastFrame = false;
        if (this.mState == 3 && this.mGlowScaleY == 0.0f) {
            this.mState = 0;
            oneLastFrame = true;
        }
        return this.mState != 0 || oneLastFrame;
    }

    public int getMaxHeight() {
        return (int) ((((float) this.mBounds.height()) * MAX_GLOW_SCALE) + 0.5f);
    }

    private void update() {
        float t = Math.min(((float) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) / this.mDuration, 1.0f);
        float interp = this.mInterpolator.getInterpolation(t);
        this.mGlowAlpha = this.mGlowAlphaStart + ((this.mGlowAlphaFinish - this.mGlowAlphaStart) * interp);
        this.mGlowScaleY = this.mGlowScaleYStart + ((this.mGlowScaleYFinish - this.mGlowScaleYStart) * interp);
        this.mDisplacement = (this.mDisplacement + this.mTargetDisplacement) / MAX_GLOW_SCALE;
        if (t >= 0.999f) {
            switch (this.mState) {
                case 1:
                    this.mState = 4;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 2000.0f;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0f;
                    this.mGlowScaleYFinish = 0.0f;
                    return;
                case 2:
                    this.mState = 3;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 600.0f;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0f;
                    this.mGlowScaleYFinish = 0.0f;
                    return;
                case 3:
                    this.mState = 0;
                    return;
                case 4:
                    this.mState = 3;
                    return;
                default:
                    return;
            }
        }
    }
}
