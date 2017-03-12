package com.samsung.android.visualeffect.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import com.samsung.android.visualeffect.scroll.common.AbstractScrollEffect;
import com.samsung.android.visualeffect.scroll.common.FastScrollPath;

public class FastScrollEffect extends AbstractScrollEffect {
    private float circleRadius = 70.0f;
    private String listText = "";
    private CharSequence listTextCharSequence;
    private TextPaint listTextPaint;
    private float listTextSize = 68.0f;
    private float maxRoundRectWidth = 1200.0f;
    private float minRoundRectWidth = 816.0f;
    private int paintColor = -855747515;
    private float roundRectWidth = 0.0f;
    private FastScrollPath scrollPath;
    private int textColor = -328966;
    private float textShapeMargin = 64.0f;
    private Paint textShapePaint;

    public FastScrollEffect(Context context) {
        super(context);
        Log.d("visualeffectScroll", "ListScrollEffect : Constructor");
        this.scrollPath = new FastScrollPath(this.scale);
        this.animationTotalFrame = this.scrollPath.getPathTotal();
        Log.d("visualeffectScroll", "animationTotalFrame = " + this.animationTotalFrame);
        this.listTextSize *= this.scale;
        this.circleRadius *= this.scale;
        this.textShapeMargin *= this.scale;
        this.minRoundRectWidth *= this.scale;
        this.maxRoundRectWidth *= this.scale;
        this.shapePaint.setColor(this.paintColor);
        this.textShapePaint = new Paint();
        this.textShapePaint.setColor(this.paintColor);
        this.textShapePaint.setStyle(Style.FILL);
        this.textShapePaint.setAntiAlias(true);
        this.listTextPaint = new TextPaint();
        this.listTextPaint.setAntiAlias(true);
        this.listTextPaint.setTypeface(Typeface.DEFAULT);
        this.listTextPaint.setTextSize(this.listTextSize);
        this.listTextPaint.setTextAlign(Align.CENTER);
        this.listTextPaint.setColor(this.textColor);
    }

    public void open(float y) {
        open(y, null);
    }

    public void open(float y, String listText) {
        Log.d("visualeffectScroll", "open : y = " + y + ",  listText = " + listText);
        this.targetY = y;
        boolean z = (listText == null || listText == "") ? false : true;
        this.isDrawText = z;
        if (this.isDrawText) {
            if (this.listText != listText) {
                this.listText = listText;
                this.listTextCharSequence = listText;
                this.listTextPaint.getTextBounds(listText, 0, listText.length(), this.textBounds);
                this.targetWidthOffset = (float) this.textBounds.width();
            }
            if (!this.isOpen) {
                this.currentWidthOffset = this.targetWidthOffset;
            }
        }
        this.isOpen = true;
        this.isYMoving = true;
        this.isWidthMoving = true;
        this.isFrameMoving = true;
        this.viewWidth = (float) getWidth();
        if (this.animationCurrentFrame == -1) {
            this.currentY = this.targetY;
        }
        startAnimation();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.animationCurrentFrame != -1) {
            Path path = this.scrollPath.getPath(this.animationCurrentFrame);
            canvas.save();
            if (this.isLTR) {
                canvas.translate(this.viewWidth, this.currentY);
            } else {
                canvas.translate(0.0f, this.currentY);
                canvas.rotate(180.0f, 0.0f, 0.0f);
            }
            canvas.drawPath(path, this.shapePaint);
            canvas.restore();
            if (this.isDrawText) {
                drawLastFrameCircle(canvas, this.currentWidthOffset);
                float bX = this.viewWidth / 2.0f;
                float bY = this.currentY - ((this.listTextPaint.descent() + this.listTextPaint.ascent()) / 2.0f);
                this.listTextPaint.setAlpha(this.currentListTextAlpha);
                canvas.drawText(this.listTextCharSequence.toString(), bX, bY, this.listTextPaint);
            }
        }
    }

    private void drawLastFrameCircle(Canvas canvas, float widthOffset) {
        float left = 0.0f;
        float right = 0.0f;
        if (this.roundRectWidth > 0.0f) {
            left = (this.viewWidth / 2.0f) - (this.roundRectWidth / 2.0f);
            right = (this.viewWidth / 2.0f) + (this.roundRectWidth / 2.0f);
            float avail = this.roundRectWidth - (this.textShapeMargin * 2.0f);
            if (widthOffset > avail) {
                this.listTextCharSequence = TextUtils.ellipsize(this.listTextCharSequence, this.listTextPaint, avail, TruncateAt.END);
            }
        } else if (widthOffset > 0.0f) {
            float tempWidth = widthOffset + (this.textShapeMargin * 2.0f);
            if (tempWidth < this.minRoundRectWidth) {
                left = (this.viewWidth - this.minRoundRectWidth) / 2.0f;
                right = (this.viewWidth + this.minRoundRectWidth) / 2.0f;
            } else if (tempWidth > this.maxRoundRectWidth) {
                left = (this.viewWidth - this.maxRoundRectWidth) / 2.0f;
                right = (this.viewWidth + this.maxRoundRectWidth) / 2.0f;
                this.listTextCharSequence = TextUtils.ellipsize(this.listTextCharSequence, this.listTextPaint, this.maxRoundRectWidth - (this.textShapeMargin * 2.0f), TruncateAt.END);
            } else {
                left = ((this.viewWidth / 2.0f) - (widthOffset / 2.0f)) - this.textShapeMargin;
                right = ((this.viewWidth / 2.0f) + (widthOffset / 2.0f)) + this.textShapeMargin;
            }
        }
        float top = this.currentY - this.circleRadius;
        float bottom = this.currentY + this.circleRadius;
        this.textShapePaint.setAlpha(this.currentTextShapeAlpha);
        canvas.drawRoundRect(left, top, right, bottom, this.circleRadius, this.circleRadius, this.textShapePaint);
    }

    public void setColor(int color) {
        super.setColor(color);
        this.textShapePaint.setColor(color);
    }

    public void setListTextColor(int color) {
        Log.d("visualeffectScroll", "setListTextColor : color = " + color);
        this.listTextPaint.setColor(color);
    }

    public void setListTextSize(float textSize) {
        Log.d("visualeffectScroll", "setListTextSize : textSize = " + textSize);
        this.listTextPaint.setTextSize(textSize);
    }

    public void setRoundRectWidth(float width) {
        Log.d("visualeffectScroll", "setRoundRectWidth : width = " + width);
        this.roundRectWidth = width;
    }
}
