package com.samsung.android.visualeffect.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.Log;
import com.samsung.android.visualeffect.scroll.common.AbstractScrollEffect;
import com.samsung.android.visualeffect.scroll.common.IndexScrollPath;

public class IndexScrollEffect extends AbstractScrollEffect {
    public static final int LEFT_HANDLE = 0;
    public static final int RIGHT_HANDLE = 1;
    private int[] bigTextAlphaWhenOpen = new int[]{255, 205, 155, 105, 55};
    private Paint bigTextPaint;
    private float bigTextSize = 164.0f;
    private float bigTextSizeSpecific = 152.0f;
    private float[] bigTextXOffsetWhenOpen = new float[]{0.0f, 34.0f, 47.0f, 75.0f, 131.0f};
    private float circleCenterX;
    private float circleCenterY;
    private float circleRadius = 140.0f;
    private int handleMode = -1;
    private int paintColor = -855670016;
    private float rightMargin;
    private IndexScrollPath scrollPath;
    private int shownFrameWhenOpen = 1;
    private Paint smallTextPaint;
    private float smallTextSize = 44.0f;
    private float smallTextY = 0.0f;
    private String targetBigText = "";
    private String targetSmallText = "";
    private int textColor = -1;

    public IndexScrollEffect(Context context) {
        super(context);
        Log.d("visualeffectScroll", "IndexScrollEffect : Constructor");
        this.scrollPath = new IndexScrollPath(this.scale);
        this.animationTotalFrame = this.scrollPath.getPathTotal();
        Log.d("visualeffectScroll", "animationTotalFrame = " + this.animationTotalFrame);
        this.circleRadius *= this.scale;
        this.bigTextSize *= this.scale;
        this.smallTextSize *= this.scale;
        this.bigTextSizeSpecific *= this.scale;
        for (int i = 0; i < this.bigTextXOffsetWhenOpen.length; i++) {
            float[] fArr = this.bigTextXOffsetWhenOpen;
            fArr[i] = fArr[i] * this.scale;
        }
        this.shapePaint.setColor(this.paintColor);
        this.circleCenterX = this.scrollPath.getCircleCenterX();
        this.circleCenterY = this.scrollPath.getCircleCenterY();
        this.bigTextPaint = new Paint();
        this.bigTextPaint.setAntiAlias(true);
        this.bigTextPaint.setTypeface(Typeface.DEFAULT);
        this.bigTextPaint.setTextSize(this.bigTextSize);
        this.bigTextPaint.setTextAlign(Align.CENTER);
        this.bigTextPaint.setColor(this.textColor);
        this.smallTextPaint = new Paint();
        this.smallTextPaint.setAntiAlias(true);
        this.smallTextPaint.setTypeface(Typeface.DEFAULT);
        this.smallTextPaint.setTextSize(this.smallTextSize);
        this.smallTextPaint.setTextAlign(Align.LEFT);
        this.smallTextPaint.setColor(this.textColor);
    }

    public void open(float y, float smallTextY, float rightMargin, String bigText, String smallText) {
        this.handleMode = -1;
        open(y, smallTextY, rightMargin, bigText, smallText, -1);
    }

    public void open(float y, float smallTextY, float rightMargin, String bigText, String smallText, int handlePosition) {
        Log.d("visualeffectScroll", "open : " + y + ", " + smallTextY + ", " + rightMargin + ",  " + bigText + ", " + smallText + ", " + handlePosition);
        if (!(this.smallTextY == smallTextY || !this.isOpen || this.animationCurrentFrame == -1)) {
            this.animationCurrentFrame = this.animationTotalFrame - 1;
        }
        if (bigText.equals("ഐ")) {
            this.bigTextPaint.setTextSize(this.bigTextSizeSpecific);
        } else {
            this.bigTextPaint.setTextSize(this.bigTextSize);
        }
        this.handleMode = handlePosition;
        this.targetY = y;
        this.isOpen = true;
        this.isYMoving = true;
        this.isWidthMoving = true;
        this.isFrameMoving = true;
        this.viewWidth = (float) getWidth();
        if (this.animationCurrentFrame == -1) {
            this.currentY = this.targetY;
        }
        startAnimation();
        this.smallTextY = smallTextY;
        this.rightMargin = rightMargin;
        if (bigText != null && smallText != null) {
            this.targetBigText = bigText;
            this.targetSmallText = smallText;
            if (bigText.length() > 1) {
                this.bigTextPaint.getTextBounds(bigText.substring(1), 0, bigText.length() - 1, this.textBounds);
                this.targetWidthOffset = (float) this.textBounds.width();
                this.shownFrameWhenOpen = 1;
                return;
            }
            this.targetWidthOffset = 0.0f;
            this.shownFrameWhenOpen = this.bigTextXOffsetWhenOpen.length;
        }
    }

    public void close() {
        super.close();
        this.currentWidthOffset = 0.0f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.animationCurrentFrame != -1) {
            Path path = this.scrollPath.getPath(this.animationCurrentFrame);
            canvas.save();
            if ((this.handleMode < 0 && this.isLTR) || this.handleMode == 1) {
                canvas.translate(this.viewWidth, this.currentY);
            } else if ((this.handleMode >= 0 || this.isLTR) && this.handleMode != 0) {
                Log.e("visualeffectScroll", "handleMode Parameter Error");
                return;
            } else {
                canvas.translate(0.0f, this.currentY);
                canvas.rotate(180.0f, 0.0f, 0.0f);
            }
            canvas.drawPath(path, this.shapePaint);
            if (this.animationCurrentFrame == this.animationTotalFrame - 1) {
                drawLastFrameCircle(canvas, this.currentWidthOffset);
            }
            canvas.restore();
            if (this.animationCurrentFrame >= this.scrollPath.getPathTotal() - this.shownFrameWhenOpen) {
                int textXOffsetIndex = (this.scrollPath.getPathTotal() - this.animationCurrentFrame) - 1;
                float tx = ((this.targetWidthOffset / 2.0f) - this.circleCenterX) - this.bigTextXOffsetWhenOpen[textXOffsetIndex];
                float bX = this.handleMode < 0 ? this.isLTR ? this.viewWidth - tx : tx : this.handleMode == 1 ? this.viewWidth - tx : tx;
                float bY = (this.currentY + this.circleCenterY) - ((this.bigTextPaint.descent() + this.bigTextPaint.ascent()) / 2.0f);
                this.bigTextPaint.setAlpha(this.bigTextAlphaWhenOpen[textXOffsetIndex]);
                canvas.drawText(this.targetBigText, bX, bY, this.bigTextPaint);
                if (this.animationCurrentFrame >= this.scrollPath.getPathTotal() - 1) {
                    float sX = this.handleMode < 0 ? this.isLTR ? this.viewWidth - this.rightMargin : this.rightMargin : this.handleMode == 1 ? this.viewWidth - this.rightMargin : this.rightMargin;
                    canvas.drawText(this.targetSmallText, sX, this.smallTextY - ((this.smallTextPaint.descent() + this.smallTextPaint.ascent()) / 2.0f), this.smallTextPaint);
                }
            }
        }
    }

    private void drawLastFrameCircle(Canvas canvas, float widthOffset) {
        if (widthOffset > 0.0f) {
            canvas.drawRoundRect((this.circleCenterX - this.circleRadius) - widthOffset, this.circleCenterY - this.circleRadius, this.circleCenterX + this.circleRadius, this.circleCenterY + this.circleRadius, this.circleRadius, this.circleRadius, this.shapePaint);
            return;
        }
        canvas.drawCircle(this.circleCenterX, this.circleCenterY, this.circleRadius, this.shapePaint);
    }

    public void setSmallTextColor(int color) {
        Log.d("visualeffectScroll", "setSmallTextColor : color = " + color);
        this.smallTextPaint.setColor(color);
    }

    public void setBigTextColor(int color) {
        Log.d("visualeffectScroll", "setBigTextColor : color = " + color);
        this.bigTextPaint.setColor(color);
    }

    public void setSmallTextSize(float textSize) {
        Log.d("visualeffectScroll", "setSmallTextSize : textSize = " + textSize);
        this.smallTextPaint.setTextSize(textSize);
    }

    public void setBigTextSize(float textSize) {
        Log.d("visualeffectScroll", "setBigTextSize : textSize = " + textSize);
        this.bigTextPaint.setTextSize(textSize);
    }
}
