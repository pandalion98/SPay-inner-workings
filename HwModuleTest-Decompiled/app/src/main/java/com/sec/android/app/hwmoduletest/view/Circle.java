package com.sec.android.app.hwmoduletest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Circle extends View {
    private Paint mPaint;
    private float radius;

    /* renamed from: x */
    private float f32x;

    /* renamed from: y */
    private float f33y;

    public Circle(Context context) {
        super(context);
        init(context);
    }

    public Circle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mPaint = new Paint(1);
        this.mPaint.setColor(-16777216);
    }

    public void setPoint(float px, float py) {
        this.f32x = px;
        this.f33y = py;
    }

    public void setRadius(float pradius) {
        this.radius = pradius;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(this.f32x, this.f33y, this.radius, this.mPaint);
    }
}
