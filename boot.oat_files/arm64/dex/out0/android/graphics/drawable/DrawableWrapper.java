package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import java.io.IOException;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class DrawableWrapper extends Drawable implements Callback {
    private Drawable mDrawable;
    private boolean mMutated;
    private DrawableWrapperState mState;

    static abstract class DrawableWrapperState extends ConstantState {
        int mChangingConfigurations;
        ConstantState mDrawableState;
        int[] mThemeAttrs;

        public abstract Drawable newDrawable(Resources resources);

        DrawableWrapperState(DrawableWrapperState orig) {
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mDrawableState = orig.mDrawableState;
            }
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || ((this.mDrawableState != null && this.mDrawableState.canApplyTheme()) || super.canApplyTheme());
        }

        public int addAtlasableBitmaps(Collection<Bitmap> atlasList) {
            ConstantState state = this.mDrawableState;
            if (state != null) {
                return state.addAtlasableBitmaps(atlasList);
            }
            return 0;
        }

        public Drawable newDrawable() {
            return newDrawable(null);
        }

        public int getChangingConfigurations() {
            return (this.mDrawableState != null ? this.mDrawableState.getChangingConfigurations() : 0) | this.mChangingConfigurations;
        }

        public boolean canConstantState() {
            return this.mDrawableState != null;
        }
    }

    DrawableWrapper(DrawableWrapperState state, Resources res) {
        this.mState = state;
        updateLocalState(res);
    }

    public DrawableWrapper(Drawable dr) {
        this.mState = null;
        this.mDrawable = dr;
    }

    private void updateLocalState(Resources res) {
        if (this.mState != null && this.mState.mDrawableState != null) {
            setDrawable(this.mState.mDrawableState.newDrawable(res));
        }
    }

    public void setDrawable(Drawable dr) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        this.mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            dr.setVisible(isVisible(), true);
            dr.setState(getState());
            dr.setLevel(getLevel());
            dr.setBounds(getBounds());
            dr.setLayoutDirection(getLayoutDirection());
            if (this.mState != null) {
                this.mState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    void updateStateFromTypedArray(TypedArray a) {
        DrawableWrapperState state = this.mState;
        if (state != null) {
            state.mChangingConfigurations |= a.getChangingConfigurations();
            state.mThemeAttrs = a.extractThemeAttrs();
        }
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        if (this.mState != null && this.mDrawable != null && this.mDrawable.canApplyTheme()) {
            this.mDrawable.applyTheme(t);
        }
    }

    public boolean canApplyTheme() {
        return (this.mState != null && this.mState.canApplyTheme()) || super.canApplyTheme();
    }

    public void invalidateDrawable(Drawable who) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public void draw(Canvas canvas) {
        if (this.mDrawable != null) {
            this.mDrawable.draw(canvas);
        }
    }

    public int getChangingConfigurations() {
        return ((this.mState != null ? this.mState.getChangingConfigurations() : 0) | super.getChangingConfigurations()) | this.mDrawable.getChangingConfigurations();
    }

    public boolean getPadding(Rect padding) {
        return this.mDrawable != null && this.mDrawable.getPadding(padding);
    }

    public Insets getOpticalInsets() {
        return this.mDrawable != null ? this.mDrawable.getOpticalInsets() : Insets.NONE;
    }

    public void setHotspot(float x, float y) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspot(x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    public void getHotspotBounds(Rect outRect) {
        if (this.mDrawable != null) {
            this.mDrawable.getHotspotBounds(outRect);
        } else {
            outRect.set(getBounds());
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean superChanged = super.setVisible(visible, restart);
        boolean changed = this.mDrawable != null && this.mDrawable.setVisible(visible, restart);
        return superChanged | changed;
    }

    public void setAlpha(int alpha) {
        if (this.mDrawable != null) {
            this.mDrawable.setAlpha(alpha);
        }
    }

    public int getAlpha() {
        return this.mDrawable != null ? this.mDrawable.getAlpha() : 255;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDrawable != null) {
            this.mDrawable.setColorFilter(colorFilter);
        }
    }

    public void setTintList(ColorStateList tint) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintList(tint);
        }
    }

    public void setTintMode(Mode tintMode) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintMode(tintMode);
        }
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawable != null && this.mDrawable.setLayoutDirection(layoutDirection);
    }

    public int getOpacity() {
        return this.mDrawable != null ? this.mDrawable.getOpacity() : -2;
    }

    public boolean isStateful() {
        return this.mDrawable != null && this.mDrawable.isStateful();
    }

    protected boolean onStateChange(int[] state) {
        if (this.mDrawable == null || !this.mDrawable.isStateful()) {
            return false;
        }
        boolean changed = this.mDrawable.setState(state);
        if (!changed) {
            return changed;
        }
        onBoundsChange(getBounds());
        return changed;
    }

    protected boolean onLevelChange(int level) {
        return this.mDrawable != null && this.mDrawable.setLevel(level);
    }

    protected void onBoundsChange(Rect bounds) {
        if (this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }
    }

    public int getIntrinsicWidth() {
        return this.mDrawable != null ? this.mDrawable.getIntrinsicWidth() : -1;
    }

    public int getIntrinsicHeight() {
        return this.mDrawable != null ? this.mDrawable.getIntrinsicHeight() : -1;
    }

    public void getOutline(Outline outline) {
        if (this.mDrawable != null) {
            this.mDrawable.getOutline(outline);
        } else {
            super.getOutline(outline);
        }
    }

    public ConstantState getConstantState() {
        if (this.mState == null || !this.mState.canConstantState()) {
            return null;
        }
        this.mState.mChangingConfigurations = getChangingConfigurations();
        return this.mState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = mutateConstantState();
            if (this.mDrawable != null) {
                this.mDrawable.mutate();
            }
            if (this.mState != null) {
                this.mState.mDrawableState = this.mDrawable != null ? this.mDrawable.getConstantState() : null;
            }
            this.mMutated = true;
        }
        return this;
    }

    DrawableWrapperState mutateConstantState() {
        return this.mState;
    }

    public void clearMutated() {
        super.clearMutated();
        if (this.mDrawable != null) {
            this.mDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    void inflateChildDrawable(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        Drawable dr = null;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                if (dr != null) {
                    setDrawable(dr);
                }
            } else if (type == 2) {
                dr = Drawable.createFromXmlInner(r, parser, attrs, theme);
            }
        }
        if (dr != null) {
            setDrawable(dr);
        }
    }
}
