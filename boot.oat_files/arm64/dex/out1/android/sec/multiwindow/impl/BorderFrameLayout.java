package android.sec.multiwindow.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class BorderFrameLayout extends FrameLayout {
    private Paint mBorderPaintInner;
    private Paint mBorderPaintOut;
    private boolean mIsBorder = false;

    public BorderFrameLayout(Context context) {
        super(context);
    }

    public BorderFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBorderEnable(boolean isEnable) {
        this.mIsBorder = isEnable;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.mIsBorder && this.mBorderPaintInner != null) {
            if (hasWindowFocus) {
                this.mBorderPaintInner.setColor(-1);
            } else {
                this.mBorderPaintInner.setColor(-7171438);
            }
            invalidate();
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mIsBorder) {
            if (this.mBorderPaintInner == null) {
                this.mBorderPaintInner = new Paint();
                this.mBorderPaintInner.setColor(-1);
                this.mBorderPaintInner.setStrokeWidth(18.0f);
            }
            if (this.mBorderPaintOut == null) {
                this.mBorderPaintOut = new Paint();
                this.mBorderPaintOut.setColor(-7171438);
                this.mBorderPaintOut.setStrokeWidth(6.0f);
            }
            int right = getMeasuredWidth();
            int bottom = getMeasuredHeight();
            canvas.drawLine(0.0f, 0.0f, (float) right, 0.0f, this.mBorderPaintInner);
            canvas.drawLine(0.0f, (float) bottom, (float) right, (float) bottom, this.mBorderPaintInner);
            canvas.drawLine((float) right, 0.0f, (float) right, (float) bottom, this.mBorderPaintInner);
            canvas.drawLine(0.0f, 0.0f, 0.0f, (float) bottom, this.mBorderPaintInner);
            canvas.drawLine(0.0f, 0.0f, (float) right, 0.0f, this.mBorderPaintOut);
            canvas.drawLine(0.0f, (float) bottom, (float) right, (float) bottom, this.mBorderPaintOut);
            canvas.drawLine((float) right, 0.0f, (float) right, (float) bottom, this.mBorderPaintOut);
            canvas.drawLine(0.0f, 0.0f, 0.0f, (float) bottom, this.mBorderPaintOut);
        }
    }
}
