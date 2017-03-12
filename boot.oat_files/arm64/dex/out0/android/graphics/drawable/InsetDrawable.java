package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Rect;
import android.util.AttributeSet;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class InsetDrawable extends DrawableWrapper {
    private InsetState mState;
    private final Rect mTmpRect;

    static final class InsetState extends DrawableWrapperState {
        int mInsetBottom = 0;
        int mInsetLeft = 0;
        int mInsetRight = 0;
        int mInsetTop = 0;

        InsetState(InsetState orig) {
            super(orig);
            if (orig != null) {
                this.mInsetLeft = orig.mInsetLeft;
                this.mInsetTop = orig.mInsetTop;
                this.mInsetRight = orig.mInsetRight;
                this.mInsetBottom = orig.mInsetBottom;
            }
        }

        public Drawable newDrawable(Resources res) {
            return new InsetDrawable(this, res);
        }
    }

    InsetDrawable() {
        this(new InsetState(null), null);
    }

    public InsetDrawable(Drawable drawable, int inset) {
        this(drawable, inset, inset, inset, inset);
    }

    public InsetDrawable(Drawable drawable, int insetLeft, int insetTop, int insetRight, int insetBottom) {
        this(new InsetState(null), null);
        this.mState.mInsetLeft = insetLeft;
        this.mState.mInsetTop = insetTop;
        this.mState.mInsetRight = insetRight;
        this.mState.mInsetBottom = insetBottom;
        setDrawable(drawable);
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.InsetDrawable);
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
            throw new XmlPullParserException(a.getPositionDescription() + ": <inset> tag requires a 'drawable' attribute or " + "child tag defining a drawable");
        }
    }

    void updateStateFromTypedArray(TypedArray a) {
        super.updateStateFromTypedArray(a);
        InsetState state = this.mState;
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 1:
                    Drawable dr = a.getDrawable(attr);
                    if (dr == null) {
                        break;
                    }
                    setDrawable(dr);
                    break;
                case 2:
                    state.mInsetLeft = a.getDimensionPixelOffset(attr, state.mInsetLeft);
                    break;
                case 3:
                    state.mInsetRight = a.getDimensionPixelOffset(attr, state.mInsetRight);
                    break;
                case 4:
                    state.mInsetTop = a.getDimensionPixelOffset(attr, state.mInsetTop);
                    break;
                case 5:
                    state.mInsetBottom = a.getDimensionPixelOffset(attr, state.mInsetBottom);
                    break;
                case 6:
                    int inset = a.getDimensionPixelOffset(attr, Integer.MIN_VALUE);
                    if (inset == Integer.MIN_VALUE) {
                        break;
                    }
                    state.mInsetLeft = inset;
                    state.mInsetTop = inset;
                    state.mInsetRight = inset;
                    state.mInsetBottom = inset;
                    break;
                default:
                    break;
            }
        }
    }

    public void applyTheme(Theme t) {
        InsetState state = this.mState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.InsetDrawable);
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

    public boolean getPadding(Rect padding) {
        boolean pad = super.getPadding(padding);
        if (needMirroring()) {
            padding.left += this.mState.mInsetRight;
            padding.right += this.mState.mInsetLeft;
        } else {
            padding.left += this.mState.mInsetLeft;
            padding.right += this.mState.mInsetRight;
        }
        padding.top += this.mState.mInsetTop;
        padding.bottom += this.mState.mInsetBottom;
        if (pad || (((this.mState.mInsetLeft | this.mState.mInsetRight) | this.mState.mInsetTop) | this.mState.mInsetBottom) != 0) {
            return true;
        }
        return false;
    }

    public Insets getOpticalInsets() {
        Insets contentInsets = super.getOpticalInsets();
        return Insets.of(contentInsets.left + this.mState.mInsetLeft, contentInsets.top + this.mState.mInsetTop, contentInsets.right + this.mState.mInsetRight, contentInsets.bottom + this.mState.mInsetBottom);
    }

    public int getOpacity() {
        InsetState state = this.mState;
        int opacity = getDrawable().getOpacity();
        if (opacity != -1) {
            return opacity;
        }
        if (state.mInsetLeft > 0 || state.mInsetTop > 0 || state.mInsetRight > 0 || state.mInsetBottom > 0) {
            return -3;
        }
        return opacity;
    }

    protected void onBoundsChange(Rect bounds) {
        Rect r = this.mTmpRect;
        r.set(bounds);
        r.left = (needMirroring() ? this.mState.mInsetRight : this.mState.mInsetLeft) + r.left;
        r.top += this.mState.mInsetTop;
        r.right -= needMirroring() ? this.mState.mInsetLeft : this.mState.mInsetRight;
        r.bottom -= this.mState.mInsetBottom;
        super.onBoundsChange(r);
    }

    public int getIntrinsicWidth() {
        return (getDrawable().getIntrinsicWidth() + this.mState.mInsetLeft) + this.mState.mInsetRight;
    }

    public int getIntrinsicHeight() {
        return (getDrawable().getIntrinsicHeight() + this.mState.mInsetTop) + this.mState.mInsetBottom;
    }

    public void getOutline(Outline outline) {
        getDrawable().getOutline(outline);
    }

    DrawableWrapperState mutateConstantState() {
        this.mState = new InsetState(this.mState);
        return this.mState;
    }

    private InsetDrawable(InsetState state, Resources res) {
        super(state, res);
        this.mTmpRect = new Rect();
        this.mState = state;
    }

    private boolean needMirroring() {
        return getDrawable() != null && getDrawable().isAutoMirrored() && getDrawable().getLayoutDirection() == 1;
    }
}
