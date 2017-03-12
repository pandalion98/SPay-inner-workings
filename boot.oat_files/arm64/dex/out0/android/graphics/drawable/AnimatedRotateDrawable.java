package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedRotateDrawable extends DrawableWrapper implements Animatable {
    private float mCurrentDegrees;
    private float mIncrement;
    private final Runnable mNextFrame;
    private boolean mRunning;
    private AnimatedRotateState mState;

    static final class AnimatedRotateState extends DrawableWrapperState {
        int mFrameDuration = 150;
        int mFramesCount = 12;
        float mPivotX = 0.0f;
        boolean mPivotXRel = false;
        float mPivotY = 0.0f;
        boolean mPivotYRel = false;

        public AnimatedRotateState(AnimatedRotateState orig) {
            super(orig);
            if (orig != null) {
                this.mPivotXRel = orig.mPivotXRel;
                this.mPivotX = orig.mPivotX;
                this.mPivotYRel = orig.mPivotYRel;
                this.mPivotY = orig.mPivotY;
                this.mFramesCount = orig.mFramesCount;
                this.mFrameDuration = orig.mFrameDuration;
            }
        }

        public Drawable newDrawable(Resources res) {
            return new AnimatedRotateDrawable(this, res);
        }
    }

    static /* synthetic */ float access$116(AnimatedRotateDrawable x0, float x1) {
        float f = x0.mCurrentDegrees + x1;
        x0.mCurrentDegrees = f;
        return f;
    }

    public AnimatedRotateDrawable() {
        this(new AnimatedRotateState(null), null);
    }

    public void draw(Canvas canvas) {
        Drawable drawable = getDrawable();
        Rect bounds = drawable.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;
        AnimatedRotateState st = this.mState;
        float px = st.mPivotXRel ? ((float) w) * st.mPivotX : st.mPivotX;
        float py = st.mPivotYRel ? ((float) h) * st.mPivotY : st.mPivotY;
        int saveCount = canvas.save();
        canvas.rotate(this.mCurrentDegrees, ((float) bounds.left) + px, ((float) bounds.top) + py);
        drawable.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public void start() {
        if (!this.mRunning) {
            this.mRunning = true;
            nextFrame();
        }
    }

    public void stop() {
        this.mRunning = false;
        unscheduleSelf(this.mNextFrame);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    private void nextFrame() {
        unscheduleSelf(this.mNextFrame);
        scheduleSelf(this.mNextFrame, SystemClock.uptimeMillis() + ((long) this.mState.mFrameDuration));
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (!visible) {
            unscheduleSelf(this.mNextFrame);
        } else if (changed || restart) {
            this.mCurrentDegrees = 0.0f;
            nextFrame();
        }
        return changed;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.AnimatedRotateDrawable);
        super.inflateWithAttributes(r, parser, a, 0);
        updateStateFromTypedArray(a);
        inflateChildDrawable(r, parser, attrs, theme);
        verifyRequiredAttributes(a);
        a.recycle();
        updateLocalState();
    }

    private void verifyRequiredAttributes(TypedArray a) throws XmlPullParserException {
        if (getDrawable() != null) {
            return;
        }
        if (this.mState.mThemeAttrs == null || this.mState.mThemeAttrs[1] == 0) {
            throw new XmlPullParserException(a.getPositionDescription() + ": <animated-rotate> tag requires a 'drawable' attribute or " + "child tag defining a drawable");
        }
    }

    void updateStateFromTypedArray(TypedArray a) {
        TypedValue tv;
        boolean z = false;
        super.updateStateFromTypedArray(a);
        AnimatedRotateState state = this.mState;
        if (a.hasValue(2)) {
            boolean z2;
            tv = a.peekValue(2);
            if (tv.type == 6) {
                z2 = true;
            } else {
                z2 = false;
            }
            state.mPivotXRel = z2;
            state.mPivotX = state.mPivotXRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
        }
        if (a.hasValue(3)) {
            tv = a.peekValue(3);
            if (tv.type == 6) {
                z = true;
            }
            state.mPivotYRel = z;
            state.mPivotY = state.mPivotYRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
        }
        setFramesCount(a.getInt(5, state.mFramesCount));
        setFramesDuration(a.getInt(4, state.mFrameDuration));
        Drawable dr = a.getDrawable(1);
        if (dr != null) {
            setDrawable(dr);
        }
    }

    public void applyTheme(Theme t) {
        AnimatedRotateState state = this.mState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.AnimatedRotateDrawable);
                try {
                    updateStateFromTypedArray(a);
                    verifyRequiredAttributes(a);
                    a.recycle();
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    a.recycle();
                }
            }
            super.applyTheme(t);
            updateLocalState();
        }
    }

    public void setFramesCount(int framesCount) {
        this.mState.mFramesCount = framesCount;
        this.mIncrement = 360.0f / ((float) this.mState.mFramesCount);
    }

    public void setFramesDuration(int framesDuration) {
        this.mState.mFrameDuration = framesDuration;
    }

    private AnimatedRotateDrawable(AnimatedRotateState state, Resources res) {
        super(state, res);
        this.mNextFrame = new Runnable() {
            public void run() {
                AnimatedRotateDrawable.access$116(AnimatedRotateDrawable.this, AnimatedRotateDrawable.this.mIncrement);
                if (AnimatedRotateDrawable.this.mCurrentDegrees > 360.0f - AnimatedRotateDrawable.this.mIncrement) {
                    AnimatedRotateDrawable.this.mCurrentDegrees = 0.0f;
                }
                AnimatedRotateDrawable.this.invalidateSelf();
                AnimatedRotateDrawable.this.nextFrame();
            }
        };
        this.mState = state;
        updateLocalState();
    }

    private void updateLocalState() {
        this.mIncrement = 360.0f / ((float) this.mState.mFramesCount);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            drawable.setFilterBitmap(true);
            if (drawable instanceof BitmapDrawable) {
                ((BitmapDrawable) drawable).setAntiAlias(true);
            }
        }
    }
}
