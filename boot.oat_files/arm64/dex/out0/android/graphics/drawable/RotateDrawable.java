package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RotateDrawable extends DrawableWrapper {
    private static final int MAX_LEVEL = 10000;
    private RotateState mState;

    static final class RotateState extends DrawableWrapperState {
        float mCurrentDegrees = 0.0f;
        float mFromDegrees = 0.0f;
        float mPivotX = 0.5f;
        boolean mPivotXRel = true;
        float mPivotY = 0.5f;
        boolean mPivotYRel = true;
        float mToDegrees = 360.0f;

        RotateState(RotateState orig) {
            super(orig);
            if (orig != null) {
                this.mPivotXRel = orig.mPivotXRel;
                this.mPivotX = orig.mPivotX;
                this.mPivotYRel = orig.mPivotYRel;
                this.mPivotY = orig.mPivotY;
                this.mFromDegrees = orig.mFromDegrees;
                this.mToDegrees = orig.mToDegrees;
                this.mCurrentDegrees = orig.mCurrentDegrees;
            }
        }

        public Drawable newDrawable(Resources res) {
            return new RotateDrawable(this, res);
        }
    }

    public RotateDrawable() {
        this(new RotateState(null), null);
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.RotateDrawable);
        super.inflateWithAttributes(r, parser, a, 0);
        updateStateFromTypedArray(a);
        inflateChildDrawable(r, parser, attrs, theme);
        verifyRequiredAttributes(a);
        a.recycle();
    }

    private void verifyRequiredAttributes(TypedArray a) throws XmlPullParserException {
        if (getDrawable() != null) {
            return;
        }
        if (this.mState.mThemeAttrs == null || this.mState.mThemeAttrs[1] == 0) {
            throw new XmlPullParserException(a.getPositionDescription() + ": <rotate> tag requires a 'drawable' attribute or " + "child tag defining a drawable");
        }
    }

    void updateStateFromTypedArray(TypedArray a) {
        boolean z = false;
        super.updateStateFromTypedArray(a);
        RotateState state = this.mState;
        state.mThemeAttrs = a.extractThemeAttrs();
        if (a.hasValue(4)) {
            TypedValue tv;
            boolean z2;
            tv = a.peekValue(4);
            if (tv.type == 6) {
                z2 = true;
            } else {
                z2 = false;
            }
            state.mPivotXRel = z2;
            state.mPivotX = state.mPivotXRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
        }
        if (a.hasValue(5)) {
            tv = a.peekValue(5);
            if (tv.type == 6) {
                z = true;
            }
            state.mPivotYRel = z;
            state.mPivotY = state.mPivotYRel ? tv.getFraction(1.0f, 1.0f) : tv.getFloat();
        }
        state.mFromDegrees = a.getFloat(2, state.mFromDegrees);
        state.mToDegrees = a.getFloat(3, state.mToDegrees);
        state.mCurrentDegrees = state.mFromDegrees;
        Drawable dr = a.getDrawable(1);
        if (dr != null) {
            setDrawable(dr);
        }
    }

    public void applyTheme(Theme t) {
        RotateState state = this.mState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.RotateDrawable);
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
        }
    }

    public void draw(Canvas canvas) {
        Drawable d = getDrawable();
        Rect bounds = d.getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;
        RotateState st = this.mState;
        float px = st.mPivotXRel ? ((float) w) * st.mPivotX : st.mPivotX;
        float py = st.mPivotYRel ? ((float) h) * st.mPivotY : st.mPivotY;
        int saveCount = canvas.save();
        canvas.rotate(st.mCurrentDegrees, ((float) bounds.left) + px, ((float) bounds.top) + py);
        d.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    public void setFromDegrees(float fromDegrees) {
        if (this.mState.mFromDegrees != fromDegrees) {
            this.mState.mFromDegrees = fromDegrees;
            invalidateSelf();
        }
    }

    public float getFromDegrees() {
        return this.mState.mFromDegrees;
    }

    public void setToDegrees(float toDegrees) {
        if (this.mState.mToDegrees != toDegrees) {
            this.mState.mToDegrees = toDegrees;
            invalidateSelf();
        }
    }

    public float getToDegrees() {
        return this.mState.mToDegrees;
    }

    public void setPivotX(float pivotX) {
        if (this.mState.mPivotX != pivotX) {
            this.mState.mPivotX = pivotX;
            invalidateSelf();
        }
    }

    public float getPivotX() {
        return this.mState.mPivotX;
    }

    public void setPivotXRelative(boolean relative) {
        if (this.mState.mPivotXRel != relative) {
            this.mState.mPivotXRel = relative;
            invalidateSelf();
        }
    }

    public boolean isPivotXRelative() {
        return this.mState.mPivotXRel;
    }

    public void setPivotY(float pivotY) {
        if (this.mState.mPivotY != pivotY) {
            this.mState.mPivotY = pivotY;
            invalidateSelf();
        }
    }

    public float getPivotY() {
        return this.mState.mPivotY;
    }

    public void setPivotYRelative(boolean relative) {
        if (this.mState.mPivotYRel != relative) {
            this.mState.mPivotYRel = relative;
            invalidateSelf();
        }
    }

    public boolean isPivotYRelative() {
        return this.mState.mPivotYRel;
    }

    protected boolean onLevelChange(int level) {
        super.onLevelChange(level);
        this.mState.mCurrentDegrees = MathUtils.lerp(this.mState.mFromDegrees, this.mState.mToDegrees, ((float) level) / SensorManager.LIGHT_OVERCAST);
        invalidateSelf();
        return true;
    }

    DrawableWrapperState mutateConstantState() {
        this.mState = new RotateState(this.mState);
        return this.mState;
    }

    private RotateDrawable(RotateState state, Resources res) {
        super(state, res);
        this.mState = state;
    }
}
