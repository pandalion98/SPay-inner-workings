package android.widget;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.SystemProperties;
import android.view.View;
import com.android.internal.R;

public class ScrollBarDrawable extends Drawable implements Callback {
    private static final boolean TABLET_KK = "latte".equals(SystemProperties.get("ro.build.scafe"));
    private static Boolean isDeviceDefault;
    private static Boolean isThemeHoloDark;
    private int mAlpha = 255;
    private boolean mAlwaysDrawHorizontalTrack;
    private boolean mAlwaysDrawVerticalTrack;
    private boolean mBoundsChanged;
    private int mClickableScrollbarTouchArea = 0;
    private final Rect mClickableThumbRect = new Rect();
    private ColorFilter mColorFilter;
    private int mExtent;
    private boolean mHasSetAlpha;
    private boolean mHasSetColorFilter;
    private Drawable mHorizontalThumb;
    private Drawable mHorizontalTrack;
    private boolean mMutated;
    private int mOffset;
    private int mRange;
    private boolean mRangeChanged;
    private ColorMatrixColorFilter mTwCMCF;
    private View mTwParent;
    private boolean mVertical;
    private Drawable mVerticalThumb;
    private Drawable mVerticalTrack;

    public ScrollBarDrawable(View parent) {
        this.mTwParent = parent;
        this.mClickableScrollbarTouchArea = (int) this.mTwParent.getContext().getResources().getDimension(R.dimen.tw_clickable_scrollbar_touch_area);
        TypedArray ta = this.mTwParent.getContext().obtainStyledAttributes(R.styleable.Theme);
        isDeviceDefault = Boolean.valueOf(ta.getBoolean(R.styleable.Theme_parentIsDeviceDefault, false));
        isThemeHoloDark = Boolean.valueOf(ta.getBoolean(R.styleable.Theme_parentIsThemeHoloDark, false));
        ta.recycle();
    }

    private void twMakeColorFilter() {
        float[] mat;
        ColorMatrix cm = new ColorMatrix();
        if (TABLET_KK && isDeviceDefault.booleanValue() && isThemeHoloDark.booleanValue()) {
            mat = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 19.0f, 0.0f, 0.0f, 0.0f, 0.0f, 139.0f, 0.0f, 0.0f, 0.0f, 0.0f, 188.0f, 0.0f, 0.0f, 0.0f, 5.0f, 0.0f};
        } else if (TABLET_KK && isDeviceDefault.booleanValue() && !isThemeHoloDark.booleanValue()) {
            mat = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 19.0f, 0.0f, 0.0f, 0.0f, 0.0f, 139.0f, 0.0f, 0.0f, 0.0f, 0.0f, 188.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        } else {
            mat = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 174.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f};
        }
        cm.set(mat);
        this.mTwCMCF = new ColorMatrixColorFilter(cm);
    }

    public void setAlwaysDrawHorizontalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawHorizontalTrack = alwaysDrawTrack;
    }

    public void setAlwaysDrawVerticalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawVerticalTrack = alwaysDrawTrack;
    }

    public boolean getAlwaysDrawVerticalTrack() {
        return this.mAlwaysDrawVerticalTrack;
    }

    public boolean getAlwaysDrawHorizontalTrack() {
        return this.mAlwaysDrawHorizontalTrack;
    }

    public void setParameters(int range, int offset, int extent, boolean vertical) {
        if (this.mVertical != vertical) {
            this.mVertical = vertical;
            this.mBoundsChanged = true;
        }
        if (this.mRange != range || this.mOffset != offset || this.mExtent != extent) {
            this.mRange = range;
            this.mOffset = offset;
            this.mExtent = extent;
            this.mRangeChanged = true;
        }
    }

    public void draw(Canvas canvas) {
        boolean vertical = this.mVertical;
        int extent = this.mExtent;
        int range = this.mRange;
        boolean drawTrack = true;
        boolean drawThumb = true;
        if (extent <= 0 || range <= extent) {
            drawTrack = vertical ? this.mAlwaysDrawVerticalTrack : this.mAlwaysDrawHorizontalTrack;
            drawThumb = false;
        }
        Rect r = getBounds();
        if (!canvas.quickReject((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, EdgeType.AA)) {
            if (drawTrack) {
                drawTrack(canvas, r, vertical);
            }
            if (drawThumb) {
                int size = vertical ? r.height() : r.width();
                int minLength = (vertical ? r.width() : r.height()) * 2;
                int length = Math.round((((float) size) * ((float) extent)) / ((float) range));
                if (length < minLength) {
                    length = minLength;
                }
                int offset = Math.round((((float) (size - length)) * ((float) this.mOffset)) / ((float) (range - extent)));
                if (offset > size - length) {
                    offset = size - length;
                }
                drawThumb(canvas, r, offset, length, vertical);
            }
        }
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mBoundsChanged = true;
    }

    public boolean isStateful() {
        return (this.mVerticalTrack != null && this.mVerticalTrack.isStateful()) || ((this.mVerticalThumb != null && this.mVerticalThumb.isStateful()) || ((this.mHorizontalTrack != null && this.mHorizontalTrack.isStateful()) || ((this.mHorizontalThumb != null && this.mHorizontalThumb.isStateful()) || super.isStateful())));
    }

    protected boolean onStateChange(int[] state) {
        boolean changed = super.onStateChange(state);
        if (this.mVerticalTrack != null) {
            changed |= this.mVerticalTrack.setState(state);
        }
        if (this.mVerticalThumb != null) {
            changed |= this.mVerticalThumb.setState(state);
        }
        if (this.mHorizontalTrack != null) {
            changed |= this.mHorizontalTrack.setState(state);
        }
        if (this.mHorizontalThumb != null) {
            return changed | this.mHorizontalThumb.setState(state);
        }
        return changed;
    }

    private void drawTrack(Canvas canvas, Rect bounds, boolean vertical) {
        Drawable track;
        if (vertical) {
            track = this.mVerticalTrack;
        } else {
            track = this.mHorizontalTrack;
        }
        if (track != null) {
            if (this.mBoundsChanged) {
                track.setBounds(bounds);
            }
            track.draw(canvas);
        }
    }

    private void drawThumb(Canvas canvas, Rect bounds, int offset, int length, boolean vertical) {
        Rect clickableThumbRect = this.mClickableThumbRect;
        boolean changed = this.mRangeChanged || this.mBoundsChanged;
        Drawable thumb;
        if (vertical) {
            if (this.mVerticalThumb != null) {
                thumb = this.mVerticalThumb;
                if (changed) {
                    thumb.setBounds(bounds.left, bounds.top + offset, bounds.right, (bounds.top + offset) + length);
                    clickableThumbRect.inset(-this.mClickableScrollbarTouchArea, -this.mClickableScrollbarTouchArea);
                    if (this.mTwParent != null) {
                        this.mTwParent.mTwVerticalScrollbarRect.set(clickableThumbRect);
                    }
                }
                thumb.draw(canvas);
            }
        } else if (this.mHorizontalThumb != null) {
            thumb = this.mHorizontalThumb;
            if (changed) {
                thumb.setBounds(bounds.left + offset, bounds.top, (bounds.left + offset) + length, bounds.bottom);
                clickableThumbRect.inset(-this.mClickableScrollbarTouchArea, -this.mClickableScrollbarTouchArea);
                if (this.mTwParent != null) {
                    this.mTwParent.mTwHorizontalScrollbarRect.set(clickableThumbRect);
                }
            }
            thumb.draw(canvas);
        }
    }

    public void setVerticalThumbDrawable(Drawable thumb) {
        if (this.mVerticalThumb != null) {
            this.mVerticalThumb.setCallback(null);
        }
        propagateCurrentState(thumb);
        this.mVerticalThumb = thumb;
    }

    public void setVerticalTrackDrawable(Drawable track) {
        if (this.mVerticalTrack != null) {
            this.mVerticalTrack.setCallback(null);
        }
        propagateCurrentState(track);
        this.mVerticalTrack = track;
    }

    public void setHorizontalThumbDrawable(Drawable thumb) {
        if (this.mHorizontalThumb != null) {
            this.mHorizontalThumb.setCallback(null);
        }
        propagateCurrentState(thumb);
        this.mHorizontalThumb = thumb;
    }

    public void setHorizontalTrackDrawable(Drawable track) {
        if (this.mHorizontalTrack != null) {
            this.mHorizontalTrack.setCallback(null);
        }
        propagateCurrentState(track);
        this.mHorizontalTrack = track;
    }

    private void propagateCurrentState(Drawable d) {
        if (d != null) {
            if (this.mMutated) {
                d.mutate();
            }
            d.setState(getState());
            d.setCallback(this);
            if (this.mHasSetAlpha) {
                d.setAlpha(this.mAlpha);
            }
            if (this.mHasSetColorFilter) {
                d.setColorFilter(this.mColorFilter);
            }
        }
    }

    public int getSize(boolean vertical) {
        if (vertical) {
            if (this.mVerticalTrack != null) {
                return this.mVerticalTrack.getIntrinsicWidth();
            }
            if (this.mVerticalThumb != null) {
                return this.mVerticalThumb.getIntrinsicWidth();
            }
            return 0;
        } else if (this.mHorizontalTrack != null) {
            return this.mHorizontalTrack.getIntrinsicHeight();
        } else {
            if (this.mHorizontalThumb != null) {
                return this.mHorizontalThumb.getIntrinsicHeight();
            }
            return 0;
        }
    }

    public ScrollBarDrawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            if (this.mVerticalTrack != null) {
                this.mVerticalTrack.mutate();
            }
            if (this.mVerticalThumb != null) {
                this.mVerticalThumb.mutate();
            }
            if (this.mHorizontalTrack != null) {
                this.mHorizontalTrack.mutate();
            }
            if (this.mHorizontalThumb != null) {
                this.mHorizontalThumb.mutate();
            }
            this.mMutated = true;
        }
        return this;
    }

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
        this.mHasSetAlpha = true;
        if (this.mVerticalTrack != null) {
            this.mVerticalTrack.setAlpha(alpha);
        }
        if (this.mVerticalThumb != null) {
            this.mVerticalThumb.setAlpha(alpha);
        }
        if (this.mHorizontalTrack != null) {
            this.mHorizontalTrack.setAlpha(alpha);
        }
        if (this.mHorizontalThumb != null) {
            this.mHorizontalThumb.setAlpha(alpha);
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        this.mHasSetColorFilter = true;
        if (this.mVerticalTrack != null) {
            this.mVerticalTrack.setColorFilter(colorFilter);
        }
        if (this.mVerticalThumb != null) {
            this.mVerticalThumb.setColorFilter(colorFilter);
        }
        if (this.mHorizontalTrack != null) {
            this.mHorizontalTrack.setColorFilter(colorFilter);
        }
        if (this.mHorizontalThumb != null) {
            this.mHorizontalThumb.setColorFilter(colorFilter);
        }
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public int getOpacity() {
        return -3;
    }

    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    public String toString() {
        return "ScrollBarDrawable: range=" + this.mRange + " offset=" + this.mOffset + " extent=" + this.mExtent + (this.mVertical ? " V" : " H");
    }
}
