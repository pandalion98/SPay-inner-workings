package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HallICPoint extends View {
    private static final int LINEWIDTH = 3;
    private Paint LinePaint;
    private Paint mPaint;
    private int mScreenHeight;
    private int mScreenWidth;
    private float radius;

    /* renamed from: x */
    private float f12x;

    /* renamed from: y */
    private float f13y;

    public HallICPoint(Context context) {
        super(context);
        init(context);
    }

    public HallICPoint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public HallICPoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(-16777216);
        this.LinePaint = new Paint(1);
        this.LinePaint.setColor(-12303292);
        this.LinePaint.setStrokeWidth(3.0f);
    }

    public void setPoint(float px, float py) {
        this.f12x = px;
        this.f13y = py;
    }

    public void setRadius(float pradius) {
        this.radius = pradius;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
        this.LinePaint.setColor(color);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(this.f12x, this.f13y, this.radius, this.mPaint);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.f12x, this.f13y + (this.radius * 4.0f), this.f12x, this.f13y - (this.radius * 4.0f), this.LinePaint);
        canvas2.drawLine(this.f12x + (this.radius * 2.8f), this.f13y + (this.radius * 2.8f), this.f12x - (this.radius * 2.8f), this.f13y - (this.radius * 2.8f), this.LinePaint);
        canvas2.drawLine(this.f12x + (this.radius * 4.0f), this.f13y, this.f12x - (this.radius * 4.0f), this.f13y, this.LinePaint);
        canvas2.drawLine(this.f12x + (this.radius * 2.8f), this.f13y - (this.radius * 2.8f), this.f12x - (this.radius * 2.8f), this.f13y + (this.radius * 2.8f), this.LinePaint);
    }
}
