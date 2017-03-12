package com.samsung.android.visualeffect.scroll.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class AbstractScrollEffect extends View {
    private final long ANIMATION_DELAY_TIME = 10;
    protected final String TAG = "visualeffectScroll";
    private final float WIDTH_SPEED = 0.6f;
    private final float Y_SPEED = 0.5f;
    protected int animationCurrentFrame = -1;
    protected int animationTotalFrame;
    protected int currentListTextAlpha = 0;
    protected int currentTextShapeAlpha = 0;
    protected float currentWidthOffset;
    protected float currentY;
    protected boolean isDrawText = false;
    protected boolean isFrameMoving = false;
    protected boolean isLTR = true;
    protected boolean isLoop = false;
    protected boolean isOpen = false;
    protected boolean isWidthMoving = false;
    protected boolean isYMoving = false;
    protected Handler mHandler;
    protected float scale;
    protected Paint shapePaint;
    protected float targetWidthOffset;
    protected float targetY;
    protected Rect textBounds;
    protected float viewWidth;
    private boolean yAniEnabled = false;

    public AbstractScrollEffect(Context context) {
        boolean z = false;
        super(context);
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            z = true;
        }
        setIsLTR(z);
        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        this.scale = ((float) dpi) / 640.0f;
        Log.d("visualeffectScroll", "dpi = " + dpi + ", scale = " + this.scale);
        this.shapePaint = new Paint();
        this.shapePaint.setStyle(Style.FILL);
        this.shapePaint.setAntiAlias(true);
        this.textBounds = new Rect();
        setHandler();
    }

    public void close() {
        Log.d("visualeffectScroll", "close");
        this.isOpen = false;
        this.isFrameMoving = true;
        this.isWidthMoving = false;
        startAnimation();
    }

    public void clearEffect() {
        Log.d("visualeffectScroll", "clearEffect");
        this.animationCurrentFrame = -1;
        this.isOpen = false;
        this.isYMoving = false;
        this.isWidthMoving = false;
        this.isFrameMoving = false;
        stopAnimation();
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setColor(int color) {
        Log.d("visualeffectScroll", "setColor : color = " + color);
        this.shapePaint.setColor(color);
    }

    public void setLayout(int l, int t, int r, int b) {
        Log.d("visualeffectScroll", "setLayout : l = " + l + ", t = " + t + ", r = " + r + ", b = " + b);
        layout(l, t, r, b);
    }

    protected void setHandler() {
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (!AbstractScrollEffect.this.yAniEnabled) {
                    AbstractScrollEffect.this.currentY = AbstractScrollEffect.this.targetY;
                    AbstractScrollEffect.this.isYMoving = false;
                } else if (AbstractScrollEffect.this.currentY != AbstractScrollEffect.this.targetY) {
                    if (Math.abs(AbstractScrollEffect.this.targetY - AbstractScrollEffect.this.currentY) < 0.5f) {
                        AbstractScrollEffect.this.currentY = AbstractScrollEffect.this.targetY;
                        AbstractScrollEffect.this.isYMoving = false;
                    } else {
                        AbstractScrollEffect abstractScrollEffect = AbstractScrollEffect.this;
                        abstractScrollEffect.currentY += (AbstractScrollEffect.this.targetY - AbstractScrollEffect.this.currentY) * 0.5f;
                    }
                }
                if (AbstractScrollEffect.this.isOpen) {
                    if (AbstractScrollEffect.this.animationTotalFrame > AbstractScrollEffect.this.animationCurrentFrame + 1) {
                        abstractScrollEffect = AbstractScrollEffect.this;
                        abstractScrollEffect.animationCurrentFrame++;
                    } else {
                        AbstractScrollEffect.this.isFrameMoving = false;
                    }
                } else if (AbstractScrollEffect.this.animationCurrentFrame > -1) {
                    abstractScrollEffect = AbstractScrollEffect.this;
                    abstractScrollEffect.animationCurrentFrame--;
                } else {
                    AbstractScrollEffect.this.isFrameMoving = false;
                }
                AbstractScrollEffect.this.currentListTextAlpha = (int) ((((float) AbstractScrollEffect.this.animationCurrentFrame) / ((float) (AbstractScrollEffect.this.animationTotalFrame - 1))) * 255.0f);
                AbstractScrollEffect.this.currentTextShapeAlpha = (int) (((float) AbstractScrollEffect.this.currentListTextAlpha) * 0.8f);
                if (!(!AbstractScrollEffect.this.isOpen || AbstractScrollEffect.this.isFrameMoving || AbstractScrollEffect.this.currentWidthOffset == AbstractScrollEffect.this.targetWidthOffset)) {
                    if (Math.abs(AbstractScrollEffect.this.targetWidthOffset - AbstractScrollEffect.this.currentWidthOffset) < 0.5f) {
                        AbstractScrollEffect.this.currentWidthOffset = AbstractScrollEffect.this.targetWidthOffset;
                        AbstractScrollEffect.this.isWidthMoving = false;
                    } else {
                        abstractScrollEffect = AbstractScrollEffect.this;
                        abstractScrollEffect.currentWidthOffset += (AbstractScrollEffect.this.targetWidthOffset - AbstractScrollEffect.this.currentWidthOffset) * 0.6f;
                    }
                }
                if (!(AbstractScrollEffect.this.isYMoving || AbstractScrollEffect.this.isFrameMoving || AbstractScrollEffect.this.isWidthMoving)) {
                    AbstractScrollEffect.this.stopAnimation();
                }
                AbstractScrollEffect.this.invalidate();
                if (AbstractScrollEffect.this.isLoop) {
                    AbstractScrollEffect.this.mHandler.sendEmptyMessageDelayed(0, 10);
                }
            }
        };
    }

    protected void startAnimation() {
        if (!this.isLoop) {
            this.isLoop = true;
            this.mHandler.sendEmptyMessageDelayed(0, 10);
        }
    }

    protected void stopAnimation() {
        this.isLoop = false;
    }

    protected void setIsLTR(boolean isLTR) {
        Log.d("visualeffectScroll", "setIsLTR : isLTR = " + isLTR);
        this.isLTR = isLTR;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("visualeffectScroll", "onConfigurationChanged : LayoutDirection = " + newConfig.getLayoutDirection());
        clearEffect();
        setIsLTR(newConfig.getLayoutDirection() == 0);
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            clearEffect();
        }
    }
}
