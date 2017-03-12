package android.view.animation;

import android.graphics.Rect;

public class ClipRectTBAnimation extends ClipRectAnimation {
    public ClipRectTBAnimation(int fromT, int fromB, int toT, int toB) {
        super(0, fromT, 0, fromB, 0, toT, 0, toB);
    }

    protected void applyTransformation(float it, Transformation tr) {
        Rect oldClipRect = tr.getClipRect();
        tr.setClipRect(oldClipRect.left, this.mFromRect.top + ((int) (((float) (this.mToRect.top - this.mFromRect.top)) * it)), oldClipRect.right, this.mFromRect.bottom + ((int) (((float) (this.mToRect.bottom - this.mFromRect.bottom)) * it)));
    }
}
