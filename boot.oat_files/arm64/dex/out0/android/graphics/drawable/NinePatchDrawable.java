package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.NinePatch;
import android.graphics.NinePatch.InsetStruct;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class NinePatchDrawable extends Drawable {
    private static final boolean DEFAULT_DITHER = false;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private boolean mEnableShadow;
    private boolean mMutated;
    private NinePatch mNinePatch;
    private NinePatchState mNinePatchState;
    private Insets mOpticalInsets;
    private Rect mPadding;
    private Paint mPaint;
    private int mTargetDensity;
    private PorterDuffColorFilter mTintFilter;

    static final class NinePatchState extends ConstantState {
        boolean mAutoMirrored;
        float mBaseAlpha;
        int mChangingConfigurations;
        boolean mDither;
        NinePatch mNinePatch;
        Insets mOpticalInsets;
        Rect mPadding;
        int mTargetDensity;
        int[] mThemeAttrs;
        ColorStateList mTint;
        Mode mTintMode;

        NinePatchState() {
            this.mThemeAttrs = null;
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mTargetDensity = 160;
            this.mAutoMirrored = false;
        }

        NinePatchState(NinePatch ninePatch, Rect padding) {
            this(ninePatch, padding, null, false, false);
        }

        NinePatchState(NinePatch ninePatch, Rect padding, Rect opticalInsets) {
            this(ninePatch, padding, opticalInsets, false, false);
        }

        NinePatchState(NinePatch ninePatch, Rect padding, Rect opticalInsets, boolean dither, boolean autoMirror) {
            this.mThemeAttrs = null;
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mTargetDensity = 160;
            this.mAutoMirrored = false;
            this.mNinePatch = ninePatch;
            this.mPadding = padding;
            this.mOpticalInsets = Insets.of(opticalInsets);
            this.mDither = dither;
            this.mAutoMirrored = autoMirror;
        }

        NinePatchState(NinePatchState state) {
            this.mThemeAttrs = null;
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mTargetDensity = 160;
            this.mAutoMirrored = false;
            this.mNinePatch = state.mNinePatch;
            this.mTint = state.mTint;
            this.mTintMode = state.mTintMode;
            this.mThemeAttrs = state.mThemeAttrs;
            this.mPadding = state.mPadding;
            this.mOpticalInsets = state.mOpticalInsets;
            this.mBaseAlpha = state.mBaseAlpha;
            this.mDither = state.mDither;
            this.mChangingConfigurations = state.mChangingConfigurations;
            this.mTargetDensity = state.mTargetDensity;
            this.mAutoMirrored = state.mAutoMirrored;
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mTint != null && this.mTint.canApplyTheme());
        }

        public int addAtlasableBitmaps(Collection<Bitmap> atlasList) {
            Bitmap bitmap = this.mNinePatch.getBitmap();
            if (isAtlasable(bitmap) && atlasList.add(bitmap)) {
                return bitmap.getWidth() * bitmap.getHeight();
            }
            return 0;
        }

        public Drawable newDrawable() {
            return new NinePatchDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new NinePatchDrawable(this, res);
        }

        public int getChangingConfigurations() {
            return (this.mTint != null ? this.mTint.getChangingConfigurations() : 0) | this.mChangingConfigurations;
        }
    }

    NinePatchDrawable() {
        this.mOpticalInsets = Insets.NONE;
        this.mTargetDensity = 160;
        this.mBitmapWidth = -1;
        this.mBitmapHeight = -1;
        this.mEnableShadow = true;
        this.mNinePatchState = new NinePatchState();
    }

    @Deprecated
    public NinePatchDrawable(Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding), null);
    }

    public NinePatchDrawable(Resources res, Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding), res);
        this.mNinePatchState.mTargetDensity = this.mTargetDensity;
    }

    public NinePatchDrawable(Resources res, Bitmap bitmap, byte[] chunk, Rect padding, Rect opticalInsets, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding, opticalInsets), res);
        this.mNinePatchState.mTargetDensity = this.mTargetDensity;
    }

    @Deprecated
    public NinePatchDrawable(NinePatch patch) {
        this(new NinePatchState(patch, new Rect()), null);
    }

    public NinePatchDrawable(Resources res, NinePatch patch) {
        this(new NinePatchState(patch, new Rect()), res);
        this.mNinePatchState.mTargetDensity = this.mTargetDensity;
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (density != this.mTargetDensity) {
            if (density == 0) {
                density = 160;
            }
            this.mTargetDensity = density;
            if (this.mNinePatch != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    private static Insets scaleFromDensity(Insets insets, int sdensity, int tdensity) {
        return Insets.of(Bitmap.scaleFromDensity(insets.left, sdensity, tdensity), Bitmap.scaleFromDensity(insets.top, sdensity, tdensity), Bitmap.scaleFromDensity(insets.right, sdensity, tdensity), Bitmap.scaleFromDensity(insets.bottom, sdensity, tdensity));
    }

    private void computeBitmapSize() {
        int sdensity = this.mNinePatch.getDensity();
        int tdensity = this.mTargetDensity;
        if (sdensity == tdensity) {
            this.mBitmapWidth = this.mNinePatch.getWidth();
            this.mBitmapHeight = this.mNinePatch.getHeight();
            this.mOpticalInsets = this.mNinePatchState.mOpticalInsets;
            return;
        }
        this.mBitmapWidth = Bitmap.scaleFromDensity(this.mNinePatch.getWidth(), sdensity, tdensity);
        this.mBitmapHeight = Bitmap.scaleFromDensity(this.mNinePatch.getHeight(), sdensity, tdensity);
        if (!(this.mNinePatchState.mPadding == null || this.mPadding == null)) {
            Rect dest = this.mPadding;
            Rect src = this.mNinePatchState.mPadding;
            if (dest == src) {
                dest = new Rect(src);
                this.mPadding = dest;
            }
            dest.left = Bitmap.scaleFromDensity(src.left, sdensity, tdensity);
            dest.top = Bitmap.scaleFromDensity(src.top, sdensity, tdensity);
            dest.right = Bitmap.scaleFromDensity(src.right, sdensity, tdensity);
            dest.bottom = Bitmap.scaleFromDensity(src.bottom, sdensity, tdensity);
        }
        this.mOpticalInsets = scaleFromDensity(this.mNinePatchState.mOpticalInsets, sdensity, tdensity);
    }

    private void setNinePatch(NinePatch ninePatch) {
        if (this.mNinePatch != ninePatch) {
            this.mNinePatch = ninePatch;
            if (ninePatch != null) {
                computeBitmapSize();
            } else {
                this.mBitmapHeight = -1;
                this.mBitmapWidth = -1;
                this.mOpticalInsets = Insets.NONE;
            }
            invalidateSelf();
        }
    }

    public void draw(Canvas canvas) {
        boolean clearColorFilter;
        int restoreAlpha;
        Rect bounds = getBounds();
        if (this.mTintFilter == null || getPaint().getColorFilter() != null) {
            clearColorFilter = false;
        } else {
            this.mPaint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        }
        boolean needsMirroring = needsMirroring();
        if (needsMirroring) {
            canvas.save();
            canvas.translate((float) (bounds.right - bounds.left), 0.0f);
            canvas.scale(-1.0f, 1.0f);
        }
        if (this.mNinePatchState.mBaseAlpha != 1.0f) {
            restoreAlpha = this.mPaint.getAlpha();
            this.mPaint.setAlpha((int) ((((float) restoreAlpha) * this.mNinePatchState.mBaseAlpha) + 0.5f));
        } else {
            restoreAlpha = -1;
        }
        this.mNinePatch.draw(canvas, bounds, this.mPaint);
        if (needsMirroring) {
            canvas.restore();
        }
        if (clearColorFilter) {
            this.mPaint.setColorFilter(null);
        }
        if (restoreAlpha >= 0) {
            this.mPaint.setAlpha(restoreAlpha);
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mNinePatchState.getChangingConfigurations();
    }

    public boolean getPadding(Rect padding) {
        Rect scaledPadding = this.mPadding;
        if (scaledPadding == null) {
            return false;
        }
        if (needsMirroring()) {
            padding.set(scaledPadding.right, scaledPadding.top, scaledPadding.left, scaledPadding.bottom);
        } else {
            padding.set(scaledPadding);
        }
        if ((((padding.left | padding.top) | padding.right) | padding.bottom) != 0) {
            return true;
        }
        return false;
    }

    public void getOutline(Outline outline) {
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            if (this.mNinePatchState != null) {
                InsetStruct insets = this.mNinePatchState.mNinePatch.getBitmap().getNinePatchInsets();
                if (insets != null) {
                    Rect outlineInsets = insets.outlineRect;
                    if (!this.mEnableShadow) {
                        Bitmap bitmap = this.mNinePatchState.mNinePatch.getBitmap();
                        double outlineRadius = (double) insets.outlineRadius;
                        if (bitmap != null && outlineRadius > 0.0d) {
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            double circumference = Math.abs(Math.cos(Math.toRadians(45.0d)) * outlineRadius);
                            int startX = (outlineInsets.left + ((int) Math.round(outlineRadius))) + 1;
                            int startY = (outlineInsets.top + ((int) Math.round(outlineRadius))) + 1;
                            int endX = ((width - outlineInsets.right) - ((int) Math.round(outlineRadius))) - 2;
                            int endY = ((height - outlineInsets.bottom) - ((int) Math.round(outlineRadius))) - 2;
                            if (checkAvailablePixel(bitmap, (int) (((long) startX) - Math.round(circumference)), (int) (((long) startY) - Math.round(circumference)))) {
                                outline.setRect(bounds);
                                outline.setAlpha(0.0f);
                                return;
                            } else if (checkAvailablePixel(bitmap, (int) (((long) endX) + Math.round(circumference)), (int) (((long) startY) - Math.round(circumference)))) {
                                outline.setRect(bounds);
                                outline.setAlpha(0.0f);
                                return;
                            } else if (checkAvailablePixel(bitmap, (int) (((long) endX) + Math.round(circumference)), (int) (((long) endY) + Math.round(circumference)))) {
                                outline.setRect(bounds);
                                outline.setAlpha(0.0f);
                                return;
                            } else if (checkAvailablePixel(bitmap, (int) (((long) startX) - Math.round(circumference)), (int) (((long) endY) + Math.round(circumference)))) {
                                outline.setRect(bounds);
                                outline.setAlpha(0.0f);
                                return;
                            }
                        }
                    }
                    Outline outline2 = outline;
                    outline2.setRoundRect(outlineInsets.left + bounds.left, outlineInsets.top + bounds.top, bounds.right - outlineInsets.right, bounds.bottom - outlineInsets.bottom, insets.outlineRadius);
                    outline.setAlpha(insets.outlineAlpha * (((float) getAlpha()) / 255.0f));
                    return;
                }
            }
            super.getOutline(outline);
        }
    }

    private boolean checkAvailablePixel(Bitmap bitmap, int x, int y) {
        if (x <= 0 || x >= bitmap.getWidth() || y <= 0 || y >= bitmap.getHeight() || (Color.alpha(bitmap.getPixel(x - 1, y)) <= 0 && Color.alpha(bitmap.getPixel(x, y + 1)) <= 0 && Color.alpha(bitmap.getPixel(x + 1, y + 1)) <= 0)) {
            return false;
        }
        return true;
    }

    public void setEnableShadow(boolean b) {
        this.mEnableShadow = b;
    }

    public Insets getOpticalInsets() {
        if (needsMirroring()) {
            return Insets.of(this.mOpticalInsets.right, this.mOpticalInsets.top, this.mOpticalInsets.left, this.mOpticalInsets.bottom);
        }
        return this.mOpticalInsets;
    }

    public void setAlpha(int alpha) {
        if (this.mPaint != null || alpha != 255) {
            getPaint().setAlpha(alpha);
            invalidateSelf();
        }
    }

    public int getAlpha() {
        if (this.mPaint == null) {
            return 255;
        }
        return getPaint().getAlpha();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mPaint != null || colorFilter != null) {
            getPaint().setColorFilter(colorFilter);
            invalidateSelf();
        }
    }

    public void setTintList(ColorStateList tint) {
        this.mNinePatchState.mTint = tint;
        this.mTintFilter = updateTintFilter(this.mTintFilter, tint, this.mNinePatchState.mTintMode);
        invalidateSelf();
    }

    public void setTintMode(Mode tintMode) {
        this.mNinePatchState.mTintMode = tintMode;
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mNinePatchState.mTint, tintMode);
        invalidateSelf();
    }

    public void setDither(boolean dither) {
        if (this.mPaint != null || dither) {
            getPaint().setDither(dither);
            invalidateSelf();
        }
    }

    public void setAutoMirrored(boolean mirrored) {
        this.mNinePatchState.mAutoMirrored = mirrored;
    }

    private boolean needsMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    public boolean isAutoMirrored() {
        return this.mNinePatchState.mAutoMirrored;
    }

    public void setFilterBitmap(boolean filter) {
        getPaint().setFilterBitmap(filter);
        invalidateSelf();
    }

    public boolean isFilterBitmap() {
        if (this.mPaint == null) {
            return false;
        }
        return getPaint().isFilterBitmap();
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.NinePatchDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        updateLocalState(r);
    }

    private void updateStateFromTypedArray(TypedArray a) throws XmlPullParserException {
        Resources r = a.getResources();
        NinePatchState state = this.mNinePatchState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mDither = a.getBoolean(1, state.mDither);
        int srcResId = a.getResourceId(0, 0);
        if (srcResId != 0) {
            Options options = new Options();
            options.inDither = !state.mDither;
            options.inScreenDensity = r.getDisplayMetrics().noncompatDensityDpi;
            Rect padding = new Rect();
            Rect opticalInsets = new Rect();
            Bitmap bitmap = null;
            try {
                TypedValue value = new TypedValue();
                InputStream is = r.openRawResource(srcResId, value);
                bitmap = BitmapFactory.decodeResourceStream(r, value, is, padding, options);
                is.close();
            } catch (IOException e) {
            }
            if (bitmap == null) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid src attribute");
            } else if (bitmap.getNinePatchChunk() == null) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid 9-patch source image");
            } else {
                bitmap.getOpticalInsets(opticalInsets);
                state.mNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk());
                state.mPadding = padding;
                state.mOpticalInsets = Insets.of(opticalInsets);
            }
        }
        state.mAutoMirrored = a.getBoolean(4, state.mAutoMirrored);
        state.mBaseAlpha = a.getFloat(3, state.mBaseAlpha);
        int tintMode = a.getInt(5, -1);
        if (tintMode != -1) {
            state.mTintMode = Drawable.parseTintMode(tintMode, Mode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(2);
        if (tint != null) {
            state.mTint = tint;
        }
        int densityDpi = r.getDisplayMetrics().densityDpi;
        if (densityDpi == 0) {
            densityDpi = 160;
        }
        state.mTargetDensity = densityDpi;
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        NinePatchState state = this.mNinePatchState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.NinePatchDrawable);
                try {
                    updateStateFromTypedArray(a);
                    a.recycle();
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    a.recycle();
                }
            }
            if (state.mTint != null && state.mTint.canApplyTheme()) {
                state.mTint = state.mTint.obtainForTheme(t);
            }
            updateLocalState(t.getResources());
        }
    }

    public boolean canApplyTheme() {
        return this.mNinePatchState != null && this.mNinePatchState.canApplyTheme();
    }

    public Paint getPaint() {
        if (this.mPaint == null) {
            this.mPaint = new Paint();
            this.mPaint.setDither(false);
        }
        return this.mPaint;
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getMinimumWidth() {
        return this.mBitmapWidth;
    }

    public int getMinimumHeight() {
        return this.mBitmapHeight;
    }

    public int getOpacity() {
        return (this.mNinePatch.hasAlpha() || (this.mPaint != null && this.mPaint.getAlpha() < 255)) ? -3 : -1;
    }

    public Region getTransparentRegion() {
        return this.mNinePatch.getTransparentRegion(getBounds());
    }

    public ConstantState getConstantState() {
        this.mNinePatchState.mChangingConfigurations = getChangingConfigurations();
        return this.mNinePatchState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mNinePatchState = new NinePatchState(this.mNinePatchState);
            this.mNinePatch = this.mNinePatchState.mNinePatch;
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    protected boolean onStateChange(int[] stateSet) {
        NinePatchState state = this.mNinePatchState;
        if (state.mTint == null || state.mTintMode == null) {
            return false;
        }
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
        return true;
    }

    public boolean isStateful() {
        NinePatchState s = this.mNinePatchState;
        return super.isStateful() || (s.mTint != null && s.mTint.isStateful());
    }

    private NinePatchDrawable(NinePatchState state, Resources res) {
        this.mOpticalInsets = Insets.NONE;
        this.mTargetDensity = 160;
        this.mBitmapWidth = -1;
        this.mBitmapHeight = -1;
        this.mEnableShadow = true;
        this.mNinePatchState = state;
        updateLocalState(res);
        this.mNinePatchState.mTargetDensity = this.mTargetDensity;
    }

    private void updateLocalState(Resources res) {
        NinePatchState state = this.mNinePatchState;
        if (res != null) {
            int densityDpi = res.getDisplayMetrics().densityDpi;
            if (densityDpi == 0) {
                densityDpi = 160;
            }
            this.mTargetDensity = densityDpi;
        } else {
            this.mTargetDensity = state.mTargetDensity;
        }
        if (state.mDither) {
            setDither(state.mDither);
        }
        if (state.mPadding != null) {
            this.mPadding = new Rect(state.mPadding);
        }
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
        setNinePatch(state.mNinePatch);
    }
}
