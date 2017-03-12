package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.SystemClock;
import android.util.SparseArray;
import java.util.Collection;

public class DrawableContainer extends Drawable implements Callback {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha = 255;
    private Runnable mAnimationRunnable;
    private int mCurIndex = -1;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex = -1;
    private boolean mMutated;

    public static abstract class DrawableContainerState extends ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        boolean mComputedConstantSize;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize = false;
        int mConstantWidth;
        boolean mDither = true;
        SparseArray<ConstantStateFuture> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration = 0;
        int mExitFadeDuration = 0;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        boolean mPaddingChecked;
        final Resources mRes;
        boolean mStateful;
        ColorStateList mTintList;
        Mode mTintMode;
        boolean mVariablePadding = false;

        private static class ConstantStateFuture {
            private final ConstantState mConstantState;

            private ConstantStateFuture(Drawable source) {
                this.mConstantState = source.getConstantState();
            }

            public Drawable get(DrawableContainerState state) {
                Drawable result;
                if (state.mRes == null) {
                    result = this.mConstantState.newDrawable();
                } else {
                    result = this.mConstantState.newDrawable(state.mRes);
                }
                result.setLayoutDirection(state.mLayoutDirection);
                result.setCallback(state.mOwner);
                if (state.mMutated) {
                    result.mutate();
                }
                return result;
            }

            public boolean canApplyTheme() {
                return this.mConstantState.canApplyTheme();
            }
        }

        DrawableContainerState(DrawableContainerState orig, DrawableContainer owner, Resources res) {
            this.mOwner = owner;
            if (res == null) {
                res = orig != null ? orig.mRes : null;
            }
            this.mRes = res;
            if (orig != null) {
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = orig.mVariablePadding;
                this.mConstantSize = orig.mConstantSize;
                this.mDither = orig.mDither;
                this.mMutated = orig.mMutated;
                this.mLayoutDirection = orig.mLayoutDirection;
                this.mEnterFadeDuration = orig.mEnterFadeDuration;
                this.mExitFadeDuration = orig.mExitFadeDuration;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mColorFilter = orig.mColorFilter;
                this.mHasColorFilter = orig.mHasColorFilter;
                this.mTintList = orig.mTintList;
                this.mTintMode = orig.mTintMode;
                this.mHasTintList = orig.mHasTintList;
                this.mHasTintMode = orig.mHasTintMode;
                this.mConstantPadding = orig.getConstantPadding();
                this.mPaddingChecked = true;
                this.mConstantWidth = orig.getConstantWidth();
                this.mConstantHeight = orig.getConstantHeight();
                this.mConstantMinimumWidth = orig.getConstantMinimumWidth();
                this.mConstantMinimumHeight = orig.getConstantMinimumHeight();
                this.mComputedConstantSize = true;
                this.mOpacity = orig.getOpacity();
                this.mCheckedOpacity = true;
                this.mStateful = orig.isStateful();
                this.mCheckedStateful = true;
                Drawable[] origDr = orig.mDrawables;
                this.mDrawables = new Drawable[origDr.length];
                this.mNumChildren = orig.mNumChildren;
                SparseArray<ConstantStateFuture> origDf = orig.mDrawableFutures;
                if (origDf != null) {
                    this.mDrawableFutures = origDf.clone();
                } else {
                    this.mDrawableFutures = new SparseArray(this.mNumChildren);
                }
                int N = this.mNumChildren;
                for (int i = 0; i < N; i++) {
                    if (origDr[i] != null) {
                        if (origDr[i].getConstantState() != null) {
                            this.mDrawableFutures.put(i, new ConstantStateFuture(origDr[i]));
                        } else {
                            this.mDrawables[i] = origDr[i];
                        }
                    }
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final int addChild(Drawable dr) {
            int pos = this.mNumChildren;
            if (pos >= this.mDrawables.length) {
                growArray(pos, pos + 10);
            }
            dr.setVisible(false, true);
            dr.setCallback(this.mOwner);
            this.mDrawables[pos] = dr;
            this.mNumChildren++;
            this.mChildrenChangingConfigurations |= dr.getChangingConfigurations();
            this.mCheckedStateful = false;
            this.mCheckedOpacity = false;
            this.mConstantPadding = null;
            this.mPaddingChecked = false;
            this.mComputedConstantSize = false;
            return pos;
        }

        final int getCapacity() {
            return this.mDrawables.length;
        }

        private final void createAllFutures() {
            if (this.mDrawableFutures != null) {
                int futureCount = this.mDrawableFutures.size();
                for (int keyIndex = 0; keyIndex < futureCount; keyIndex++) {
                    this.mDrawables[this.mDrawableFutures.keyAt(keyIndex)] = ((ConstantStateFuture) this.mDrawableFutures.valueAt(keyIndex)).get(this);
                }
                this.mDrawableFutures = null;
            }
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final Drawable[] getChildren() {
            createAllFutures();
            return this.mDrawables;
        }

        public final Drawable getChild(int index) {
            Drawable result = this.mDrawables[index];
            if (result != null) {
                return result;
            }
            if (this.mDrawableFutures != null) {
                int keyIndex = this.mDrawableFutures.indexOfKey(index);
                if (keyIndex >= 0) {
                    Drawable prepared = ((ConstantStateFuture) this.mDrawableFutures.valueAt(keyIndex)).get(this);
                    this.mDrawables[index] = prepared;
                    this.mDrawableFutures.removeAt(keyIndex);
                    if (this.mDrawableFutures.size() == 0) {
                        this.mDrawableFutures = null;
                    }
                    return prepared;
                }
            }
            return null;
        }

        final boolean setLayoutDirection(int layoutDirection, int currentIndex) {
            boolean changed = false;
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    boolean childChanged = drawables[i].setLayoutDirection(layoutDirection);
                    if (i == currentIndex) {
                        changed = childChanged;
                    }
                }
            }
            this.mLayoutDirection = layoutDirection;
            return changed;
        }

        final void applyTheme(Theme theme) {
            if (theme != null) {
                createAllFutures();
                int N = this.mNumChildren;
                Drawable[] drawables = this.mDrawables;
                int i = 0;
                while (i < N) {
                    if (drawables[i] != null && drawables[i].canApplyTheme()) {
                        drawables[i].applyTheme(theme);
                        this.mChildrenChangingConfigurations |= drawables[i].getChangingConfigurations();
                    }
                    i++;
                }
            }
        }

        public boolean canApplyTheme() {
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                Drawable d = drawables[i];
                if (d == null) {
                    ConstantStateFuture future = (ConstantStateFuture) this.mDrawableFutures.get(i);
                    if (future != null && future.canApplyTheme()) {
                        return true;
                    }
                } else if (d.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        private void mutate() {
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    drawables[i].mutate();
                }
            }
            this.mMutated = true;
        }

        final void clearMutated() {
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i] != null) {
                    drawables[i].clearMutated();
                }
            }
            this.mMutated = false;
        }

        public final void setVariablePadding(boolean variable) {
            this.mVariablePadding = variable;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            if (this.mConstantPadding != null || this.mPaddingChecked) {
                return this.mConstantPadding;
            }
            createAllFutures();
            Rect r = null;
            Rect t = new Rect();
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i].getPadding(t)) {
                    if (r == null) {
                        r = new Rect(0, 0, 0, 0);
                    }
                    if (t.left > r.left) {
                        r.left = t.left;
                    }
                    if (t.top > r.top) {
                        r.top = t.top;
                    }
                    if (t.right > r.right) {
                        r.right = t.right;
                    }
                    if (t.bottom > r.bottom) {
                        r.bottom = t.bottom;
                    }
                }
            }
            this.mPaddingChecked = true;
            this.mConstantPadding = r;
            return r;
        }

        public final void setConstantSize(boolean constant) {
            this.mConstantSize = constant;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final int getConstantWidth() {
            if (!this.mComputedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getConstantHeight() {
            if (!this.mComputedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mComputedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mComputedConstantSize) {
                computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        protected void computeConstantSize() {
            this.mComputedConstantSize = true;
            createAllFutures();
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i = 0; i < N; i++) {
                Drawable dr = drawables[i];
                int s = dr.getIntrinsicWidth();
                if (s > this.mConstantWidth) {
                    this.mConstantWidth = s;
                }
                s = dr.getIntrinsicHeight();
                if (s > this.mConstantHeight) {
                    this.mConstantHeight = s;
                }
                s = dr.getMinimumWidth();
                if (s > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = s;
                }
                s = dr.getMinimumHeight();
                if (s > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = s;
                }
            }
        }

        public final void setEnterFadeDuration(int duration) {
            this.mEnterFadeDuration = duration;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final void setExitFadeDuration(int duration) {
            this.mExitFadeDuration = duration;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            createAllFutures();
            this.mCheckedOpacity = true;
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            int op = N > 0 ? drawables[0].getOpacity() : -2;
            for (int i = 1; i < N; i++) {
                op = Drawable.resolveOpacity(op, drawables[i].getOpacity());
            }
            this.mOpacity = op;
            return op;
        }

        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            createAllFutures();
            this.mCheckedStateful = true;
            int N = this.mNumChildren;
            Drawable[] drawables = this.mDrawables;
            for (int i = 0; i < N; i++) {
                if (drawables[i].isStateful()) {
                    this.mStateful = true;
                    return true;
                }
            }
            this.mStateful = false;
            return false;
        }

        public void growArray(int oldSize, int newSize) {
            Drawable[] newDrawables = new Drawable[newSize];
            System.arraycopy(this.mDrawables, 0, newDrawables, 0, oldSize);
            this.mDrawables = newDrawables;
        }

        public synchronized boolean canConstantState() {
            boolean z = false;
            synchronized (this) {
                if (this.mCheckedConstantState) {
                    z = this.mCanConstantState;
                } else {
                    createAllFutures();
                    this.mCheckedConstantState = true;
                    int N = this.mNumChildren;
                    Drawable[] drawables = this.mDrawables;
                    for (int i = 0; i < N; i++) {
                        if (drawables[i].getConstantState() == null) {
                            this.mCanConstantState = false;
                            break;
                        }
                    }
                    this.mCanConstantState = true;
                    z = true;
                }
            }
            return z;
        }

        public int addAtlasableBitmaps(Collection<Bitmap> atlasList) {
            int N = this.mNumChildren;
            int pixelCount = 0;
            for (int i = 0; i < N; i++) {
                ConstantState state = getChild(i).getConstantState();
                if (state != null) {
                    pixelCount += state.addAtlasableBitmaps(atlasList);
                }
            }
            return pixelCount;
        }
    }

    public void draw(Canvas canvas) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.draw(canvas);
        }
        if (this.mLastDrawable != null) {
            this.mLastDrawable.draw(canvas);
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }

    private boolean needsMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    public boolean getPadding(Rect padding) {
        boolean result;
        Rect r = this.mDrawableContainerState.getConstantPadding();
        if (r != null) {
            padding.set(r);
            result = (((r.left | r.top) | r.bottom) | r.right) != 0;
        } else {
            result = this.mCurrDrawable != null ? this.mCurrDrawable.getPadding(padding) : super.getPadding(padding);
        }
        if (needsMirroring()) {
            int left = padding.left;
            padding.left = padding.right;
            padding.right = left;
        }
        return result;
    }

    public Insets getOpticalInsets() {
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.getOpticalInsets();
        }
        return Insets.NONE;
    }

    public void getOutline(Outline outline) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.getOutline(outline);
        }
    }

    public void setAlpha(int alpha) {
        if (!this.mHasAlpha || this.mAlpha != alpha) {
            this.mHasAlpha = true;
            this.mAlpha = alpha;
            if (this.mCurrDrawable == null) {
                return;
            }
            if (this.mEnterAnimationEnd == 0) {
                this.mCurrDrawable.mutate().setAlpha(alpha);
            } else {
                animate(false);
            }
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setDither(boolean dither) {
        if (this.mDrawableContainerState.mDither != dither) {
            this.mDrawableContainerState.mDither = dither;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.mutate().setDither(this.mDrawableContainerState.mDither);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = true;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.mutate().setColorFilter(colorFilter);
            }
        }
    }

    public void setTintList(ColorStateList tint) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != tint) {
            this.mDrawableContainerState.mTintList = tint;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.mutate().setTintList(tint);
            }
        }
    }

    public void setTintMode(Mode tintMode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mTintMode != tintMode) {
            this.mDrawableContainerState.mTintMode = tintMode;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.mutate().setTintMode(tintMode);
            }
        }
    }

    public void setEnterFadeDuration(int ms) {
        this.mDrawableContainerState.mEnterFadeDuration = ms;
    }

    public void setExitFadeDuration(int ms) {
        this.mDrawableContainerState.mExitFadeDuration = ms;
    }

    protected void onBoundsChange(Rect bounds) {
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setBounds(bounds);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setBounds(bounds);
        }
    }

    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mirrored) {
            this.mDrawableContainerState.mAutoMirrored = mirrored;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.mutate().setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    public void jumpToCurrentState() {
        boolean changed = false;
        if (this.mLastDrawable != null) {
            this.mLastDrawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            changed = true;
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.mutate().setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0) {
            this.mExitAnimationEnd = 0;
            changed = true;
        }
        if (this.mEnterAnimationEnd != 0) {
            this.mEnterAnimationEnd = 0;
            changed = true;
        }
        if (changed) {
            invalidateSelf();
        }
    }

    public void setHotspot(float x, float y) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setHotspot(x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            this.mHotspotBounds.set(left, top, right, bottom);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    public void getHotspotBounds(Rect outRect) {
        if (this.mHotspotBounds != null) {
            outRect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    protected boolean onStateChange(int[] state) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setState(state);
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.setState(state);
        }
        return false;
    }

    protected boolean onLevelChange(int level) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setLevel(level);
        }
        if (this.mCurrDrawable != null) {
            return this.mCurrDrawable.setLevel(level);
        }
        return false;
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawableContainerState.setLayoutDirection(layoutDirection, getCurrentIndex());
    }

    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        return this.mCurrDrawable != null ? this.mCurrDrawable.getIntrinsicWidth() : -1;
    }

    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        return this.mCurrDrawable != null ? this.mCurrDrawable.getIntrinsicHeight() : -1;
    }

    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        return this.mCurrDrawable != null ? this.mCurrDrawable.getMinimumWidth() : 0;
    }

    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        return this.mCurrDrawable != null ? this.mCurrDrawable.getMinimumHeight() : 0;
    }

    public void invalidateDrawable(Drawable who) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who == this.mCurrDrawable && getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setVisible(visible, restart);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(visible, restart);
        }
        return changed;
    }

    public int getOpacity() {
        return (this.mCurrDrawable == null || !this.mCurrDrawable.isVisible()) ? -2 : this.mDrawableContainerState.getOpacity();
    }

    public void setCurrentIndex(int index) {
        selectDrawable(index);
    }

    public int getCurrentIndex() {
        return this.mCurIndex;
    }

    public boolean selectDrawable(int idx) {
        if (idx == this.mCurIndex) {
            return false;
        }
        long now = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            if (this.mLastDrawable != null) {
                this.mLastDrawable.setVisible(false, false);
            }
            if (this.mCurrDrawable != null) {
                this.mLastDrawable = this.mCurrDrawable;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = ((long) this.mDrawableContainerState.mExitFadeDuration) + now;
            } else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0;
            }
        } else if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(false, false);
        }
        if (idx < 0 || idx >= this.mDrawableContainerState.mNumChildren) {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        } else {
            Drawable d = this.mDrawableContainerState.getChild(idx);
            this.mCurrDrawable = d;
            this.mCurIndex = idx;
            if (d != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = ((long) this.mDrawableContainerState.mEnterFadeDuration) + now;
                }
                initializeDrawableForDisplay(d);
            }
        }
        if (!(this.mEnterAnimationEnd == 0 && this.mExitAnimationEnd == 0)) {
            if (this.mAnimationRunnable == null) {
                this.mAnimationRunnable = new Runnable() {
                    public void run() {
                        DrawableContainer.this.animate(true);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            } else {
                unscheduleSelf(this.mAnimationRunnable);
            }
            animate(true);
        }
        invalidateSelf();
        return true;
    }

    private void initializeDrawableForDisplay(Drawable d) {
        d.mutate();
        if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
            d.setAlpha(this.mAlpha);
        }
        if (this.mDrawableContainerState.mHasColorFilter) {
            d.setColorFilter(this.mDrawableContainerState.mColorFilter);
        } else {
            if (this.mDrawableContainerState.mHasTintList) {
                d.setTintList(this.mDrawableContainerState.mTintList);
            }
            if (this.mDrawableContainerState.mHasTintMode) {
                d.setTintMode(this.mDrawableContainerState.mTintMode);
            }
        }
        d.setVisible(isVisible(), true);
        d.setDither(this.mDrawableContainerState.mDither);
        d.setState(getState());
        d.setLevel(getLevel());
        d.setBounds(getBounds());
        d.setLayoutDirection(getLayoutDirection());
        d.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
        Rect hotspotBounds = this.mHotspotBounds;
        if (hotspotBounds != null) {
            d.setHotspotBounds(hotspotBounds.left, hotspotBounds.top, hotspotBounds.right, hotspotBounds.bottom);
        }
    }

    void animate(boolean schedule) {
        this.mHasAlpha = true;
        long now = SystemClock.uptimeMillis();
        boolean animating = false;
        if (this.mCurrDrawable == null) {
            this.mEnterAnimationEnd = 0;
        } else if (this.mEnterAnimationEnd != 0) {
            if (this.mEnterAnimationEnd <= now) {
                this.mCurrDrawable.mutate().setAlpha(this.mAlpha);
                this.mEnterAnimationEnd = 0;
            } else {
                this.mCurrDrawable.mutate().setAlpha(((255 - (((int) ((this.mEnterAnimationEnd - now) * 255)) / this.mDrawableContainerState.mEnterFadeDuration)) * this.mAlpha) / 255);
                animating = true;
            }
        }
        if (this.mLastDrawable == null) {
            this.mExitAnimationEnd = 0;
        } else if (this.mExitAnimationEnd != 0) {
            if (this.mExitAnimationEnd <= now) {
                this.mLastDrawable.setVisible(false, false);
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0;
            } else {
                this.mLastDrawable.mutate().setAlpha((this.mAlpha * (((int) ((this.mExitAnimationEnd - now) * 255)) / this.mDrawableContainerState.mExitFadeDuration)) / 255);
                animating = true;
            }
        }
        if (schedule && animating) {
            scheduleSelf(this.mAnimationRunnable, 16 + now);
        }
    }

    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    public void applyTheme(Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    public ConstantState getConstantState() {
        if (!this.mDrawableContainerState.canConstantState()) {
            return null;
        }
        this.mDrawableContainerState.mChangingConfigurations = getChangingConfigurations();
        return this.mDrawableContainerState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState clone = cloneConstantState();
            clone.mutate();
            setConstantState(clone);
            this.mMutated = true;
        }
        return this;
    }

    DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    protected void setConstantState(DrawableContainerState state) {
        this.mDrawableContainerState = state;
        if (this.mCurIndex >= 0) {
            this.mCurrDrawable = state.getChild(this.mCurIndex);
            if (this.mCurrDrawable != null) {
                initializeDrawableForDisplay(this.mCurrDrawable);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }
}
