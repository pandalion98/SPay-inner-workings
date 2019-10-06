package com.sec.android.app.hwmoduletest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class SensorArrow extends View {
    private static final int CENTER_X = 90;
    private static final int COMPASS_RADIUS = 70;
    private final int COMPLETED_CAL = 2;
    private final int REAL_COMPLETED_CAL = 3;
    private float direction = 45.0f;
    private Paint mArrowPaint;
    private Paint mBGPaint;
    private int mCurrentCal;
    private Paint mTextPaint;
    private Paint mTextPaintCal;

    /* renamed from: mX */
    private int f34mX;
    private String needCalMsg;

    public void setCurrentCal(int cal) {
        this.mCurrentCal = cal;
    }

    public float getDirection() {
        return this.direction;
    }

    public void setDirection(float direction2) {
        this.direction = direction2;
        invalidate();
    }

    public SensorArrow(Context context) {
        super(context);
        initCompassView();
    }

    public SensorArrow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCompassView();
    }

    public SensorArrow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    private void initCompassView() {
        this.f34mX = CENTER_X;
        this.needCalMsg = "Need for calibration";
        this.mArrowPaint = new Paint(1);
        this.mArrowPaint.setColor(-65281);
        this.mArrowPaint.setColor(-65536);
        this.mArrowPaint.setStrokeWidth(4.0f);
        this.mBGPaint = new Paint(1);
        this.mBGPaint.setColor(-16777216);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mTextPaint.setTextSize(15.0f);
        this.mTextPaintCal = new Paint(1);
        this.mTextPaintCal.setColor(-1);
        this.mTextPaintCal.setTextAlign(Align.CENTER);
        this.mTextPaintCal.setTextSize(20.0f);
        setCurrentCal(0);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(170, 1073741824));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas cv) {
        cv.drawCircle((float) this.f34mX, 70.0f, 70.0f, this.mBGPaint);
        draw_arrow(cv, this.mArrowPaint, this.f34mX);
        if (this.mCurrentCal < 2) {
            cv.drawText(this.needCalMsg, (float) this.f34mX, (float) this.f34mX, this.mTextPaint);
            cv.drawText(String.valueOf(this.mCurrentCal), (float) this.f34mX, (float) (this.f34mX + 30), this.mTextPaintCal);
            return;
        }
        cv.drawText(String.valueOf(this.mCurrentCal), (float) this.f34mX, (float) (this.f34mX + 30), this.mTextPaintCal);
    }

    private void draw_arrow(Canvas canvas, Paint p, int x) {
        if (this.mCurrentCal < 2) {
            p.setColor(-65536);
        } else if (this.mCurrentCal == 2) {
            p.setColor(-16711936);
        } else if (this.mCurrentCal >= 3) {
            p.setColor(-16776961);
        }
        canvas.rotate(-this.direction, (float) x, 70.0f);
        canvas.drawLine((float) x, 0.0f, (float) x, 70.0f, p);
    }
}
