package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;

public class TspPatternStyleHoverX extends BaseActivity {
    private static final String TAG = "TspPatternStyleHoverX";
    public static boolean mIsFinishingChildActivity = false;
    public static boolean mIsFinishingParentActivity = false;
    private int HEIGHT_BASIS = 19;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS_10 = 0;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS_6 = 0;
    protected int KEY_TIMEOUT;
    protected int KEY_TIMER_EXPIRED;
    protected int MILLIS_IN_SEC;
    private int WIDTH_BASIS = 11;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS_10 = 0;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS_6 = 0;
    private float col_height = 0.0f;
    /* access modifiers changed from: private */
    public float col_height_10 = 0.0f;
    /* access modifiers changed from: private */
    public float col_height_6 = 0.0f;
    /* access modifiers changed from: private */
    public float col_start_x_10 = 0.0f;
    /* access modifiers changed from: private */
    public float col_start_x_6 = 0.0f;
    /* access modifiers changed from: private */
    public float col_start_y_10 = 0.0f;
    /* access modifiers changed from: private */
    public float col_start_y_6 = 0.0f;
    private float col_width = 0.0f;
    /* access modifiers changed from: private */
    public float col_width_10 = 0.0f;
    /* access modifiers changed from: private */
    public float col_width_6 = 0.0f;
    private boolean dialog_showing = false;
    /* access modifiers changed from: private */
    public boolean[] draw10;
    /* access modifiers changed from: private */
    public boolean[] draw6;
    /* access modifiers changed from: private */
    public boolean isHovering = false;
    private int mBottommostOfMatrix;
    private int mCenterOfHorizontalOfMatrix;
    private int mCenterOfVerticalOfMatrix;
    private int mLeftmostOfMatrix;
    private ModuleDevice mModuleDevice;
    private int mRightmostOfMatrix;
    /* access modifiers changed from: private */
    public int mScreenHeight;
    /* access modifiers changed from: private */
    public int mScreenWidth;
    private int mTopmostOfMatrix;
    private boolean needFailPopupDispaly = false;
    /* access modifiers changed from: private */
    public boolean remoteCall = false;
    /* access modifiers changed from: private */
    public boolean successTest = true;

    public class MyView extends View implements IGloverEventHandler {
        private boolean isTouchDown;
        private Paint mClickPaint;
        private Paint mEmptyPaint;
        private Bitmap mLineBitmap;
        private Canvas mLineCanvas;
        private Paint mLinePaint;
        private Bitmap mMatrixBitmap;
        private Canvas mMatrixCanvas;
        private Paint mNonClickPaint;
        private float mPreTouchedX = 0.0f;
        private float mPreTouchedY = 0.0f;
        private float mTouchedX = 0.0f;
        private float mTouchedY = 0.0f;

        public MyView(Context context) {
            super(context);
            setKeepScreenOn(true);
            StringBuilder sb = new StringBuilder();
            sb.append("Screen size: ");
            sb.append(TspPatternStyleHoverX.this.mScreenWidth);
            sb.append(" x ");
            sb.append(TspPatternStyleHoverX.this.mScreenHeight);
            LtUtil.log_i(TspPatternStyleHoverX.this.CLASS_NAME, "onCreate", sb.toString());
            this.mMatrixBitmap = Bitmap.createBitmap(TspPatternStyleHoverX.this.mScreenWidth, TspPatternStyleHoverX.this.mScreenHeight, Config.ARGB_8888);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
            this.mLineBitmap = Bitmap.createBitmap(TspPatternStyleHoverX.this.mScreenWidth, TspPatternStyleHoverX.this.mScreenHeight, Config.ARGB_8888);
            this.mLineCanvas = new Canvas(this.mLineBitmap);
            if (TspPatternStyleHoverX.this.isHovering && !"factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
                setOnTouchListener(new OnTouchListener(TspPatternStyleHoverX.this) {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
            setPaint();
            initRect();
            this.isTouchDown = false;
        }

        private void setPaint() {
            this.mLinePaint = new Paint();
            this.mLinePaint.setAntiAlias(true);
            this.mLinePaint.setDither(true);
            this.mLinePaint.setColor(-16777216);
            this.mLinePaint.setStyle(Style.STROKE);
            this.mLinePaint.setStrokeJoin(Join.ROUND);
            this.mLinePaint.setStrokeCap(Cap.SQUARE);
            this.mLinePaint.setStrokeWidth(5.0f);
            this.mLinePaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f}, 1.0f));
            this.mLinePaint.setColor(-16777216);
            this.mClickPaint = new Paint();
            this.mClickPaint.setAntiAlias(false);
            this.mClickPaint.setStyle(Style.FILL);
            this.mClickPaint.setColor(-16711936);
            this.mNonClickPaint = new Paint();
            this.mNonClickPaint.setAntiAlias(false);
            this.mNonClickPaint.setColor(-1);
            this.mEmptyPaint = new Paint();
            this.mEmptyPaint.setAntiAlias(false);
            this.mEmptyPaint.setColor(-1);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(this.mMatrixBitmap, 0.0f, 0.0f, null);
            canvas.drawBitmap(this.mLineBitmap, 0.0f, 0.0f, null);
        }

        public boolean onHoverEvent(MotionEvent event) {
            int action = event.getAction();
            if (TspPatternStyleHoverX.this.isHovering) {
                TOOLTYPE.setToolType(TOOLTYPE.TOOLTYPE_FINGER_HOVER, this.mLinePaint);
                if (action != 7) {
                    switch (action) {
                        case 9:
                            draw_down(event);
                            break;
                        case 10:
                            draw_up(event);
                            break;
                    }
                } else {
                    draw_move(event);
                }
            }
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (event.getToolType(0) == 2) {
                return true;
            }
            if (TspPatternStyleHoverX.this.isHovering) {
                if (GloveReceiver.getEnable()) {
                    TOOLTYPE.setToolType(TOOLTYPE.TOOLTYPE_GLOVE_TOUCH, this.mLinePaint);
                } else {
                    TOOLTYPE.setToolType(TOOLTYPE.TOOLTYPE_FINGER_TOUCH, this.mLinePaint);
                }
            }
            switch (action) {
                case 0:
                    draw_down(event);
                    break;
                case 1:
                    draw_up(event);
                    break;
                case 2:
                    draw_move(event);
                    break;
            }
            return true;
        }

        private void draw_down(MotionEvent event) {
            this.mTouchedX = event.getX();
            this.mTouchedY = event.getY();
            drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
            this.isTouchDown = true;
        }

        private void draw_move(MotionEvent event) {
            if (this.isTouchDown) {
                for (int i = 0; i < event.getHistorySize(); i++) {
                    this.mPreTouchedX = this.mTouchedX;
                    this.mPreTouchedY = this.mTouchedY;
                    this.mTouchedX = event.getHistoricalX(i);
                    this.mTouchedY = event.getHistoricalY(i);
                    drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                    drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY);
                }
                this.mPreTouchedX = this.mTouchedX;
                this.mPreTouchedY = this.mTouchedY;
                this.mTouchedX = event.getX();
                this.mTouchedY = event.getY();
                drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY);
                this.isTouchDown = true;
            }
        }

        private void draw_up(MotionEvent event) {
            if (this.isTouchDown) {
                this.mPreTouchedX = this.mTouchedX;
                this.mPreTouchedY = this.mTouchedY;
                this.mTouchedX = event.getX();
                this.mTouchedY = event.getY();
                if (this.mPreTouchedX == this.mTouchedX && this.mPreTouchedY == this.mTouchedY) {
                    drawPoint(this.mTouchedX, this.mTouchedY);
                }
                this.isTouchDown = false;
            }
        }

        private void drawLine(float preX, float preY, float x, float y) {
            int highX;
            int lowX;
            int highY;
            int lowY;
            this.mLineCanvas.drawLine(preX, preY, x, y, this.mLinePaint);
            if (preX >= x) {
                highX = (int) preX;
                lowX = (int) x;
            } else {
                highX = (int) x;
                lowX = (int) preX;
            }
            if (preY >= y) {
                highY = (int) preY;
                lowY = (int) y;
            } else {
                highY = (int) y;
                lowY = (int) preY;
            }
            invalidate(new Rect(lowX - 6, lowY - 6, highX + 6, highY + 6));
        }

        private void drawPoint(float x, float y) {
            this.mLineCanvas.drawPoint(x, y, this.mLinePaint);
            invalidate(new Rect(((int) x) - 6, ((int) y) - 6, ((int) x) + 6, ((int) y) + 6));
        }

        private void initRect() {
            Paint mRectPaint = new Paint();
            Paint mRectPaintCross = new Paint();
            mRectPaint.setColor(-16777216);
            mRectPaintCross.setColor(-16777216);
            mRectPaintCross.setStyle(Style.STROKE);
            int i = 0;
            int ColX = 0;
            int i2 = 0;
            while (i2 < TspPatternStyleHoverX.this.HEIGHT_BASIS_10 + 1) {
                int ColY = ((int) (TspPatternStyleHoverX.this.col_height_10 * ((float) i2))) + ((int) TspPatternStyleHoverX.this.col_start_y_10);
                int ColX2 = ColX;
                int ColX3 = 0;
                while (true) {
                    int j = ColX3;
                    if (j >= TspPatternStyleHoverX.this.WIDTH_BASIS_10 + 1) {
                        break;
                    }
                    int ColX4 = ((int) (TspPatternStyleHoverX.this.col_width_10 * ((float) j))) + ((int) TspPatternStyleHoverX.this.col_start_x_10);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) TspPatternStyleHoverX.this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) ColX4, (float) TspPatternStyleHoverX.this.mScreenHeight, paint);
                    ColX3 = j + 1;
                    ColX2 = ColX4;
                }
                i2++;
                ColX = ColX2;
            }
            this.mMatrixCanvas.drawRect((((float) TspPatternStyleHoverX.this.mScreenWidth) - TspPatternStyleHoverX.this.col_start_x_10) + 1.0f, 0.0f, (float) TspPatternStyleHoverX.this.mScreenWidth, (float) TspPatternStyleHoverX.this.mScreenHeight, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect(0.0f, (((float) TspPatternStyleHoverX.this.mScreenHeight) - TspPatternStyleHoverX.this.col_start_y_10) + 1.0f, (float) TspPatternStyleHoverX.this.mScreenWidth, (float) TspPatternStyleHoverX.this.mScreenHeight, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect(TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 + 1.0f, TspPatternStyleHoverX.this.col_start_y_10 + TspPatternStyleHoverX.this.col_height_10 + 1.0f, ((float) TspPatternStyleHoverX.this.mScreenWidth) - ((TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10) + 1.0f), ((float) TspPatternStyleHoverX.this.mScreenHeight) - ((TspPatternStyleHoverX.this.col_start_y_10 + TspPatternStyleHoverX.this.col_height_10) + 1.0f), this.mEmptyPaint);
            for (int i3 = 0; i3 < TspPatternStyleHoverX.this.HEIGHT_BASIS_6; i3++) {
                int ColX5 = (int) ((((float) TspPatternStyleHoverX.this.mScreenWidth) / 2.0f) - (TspPatternStyleHoverX.this.col_width_6 / 2.0f));
                int ColY2 = ((int) (TspPatternStyleHoverX.this.col_height_6 * ((float) i3))) + ((int) TspPatternStyleHoverX.this.col_start_y_6);
                this.mMatrixCanvas.drawRect((float) ColX5, (float) ColY2, ((float) ColX5) + TspPatternStyleHoverX.this.col_width_6, ((float) ColY2) + TspPatternStyleHoverX.this.col_height_6, mRectPaintCross);
            }
            while (true) {
                int i4 = i;
                if (i4 < TspPatternStyleHoverX.this.WIDTH_BASIS_6) {
                    int ColY3 = (int) ((((float) TspPatternStyleHoverX.this.mScreenHeight) / 2.0f) - (TspPatternStyleHoverX.this.col_height_6 / 2.0f));
                    int ColX6 = ((int) (TspPatternStyleHoverX.this.col_width_6 * ((float) i4))) + ((int) TspPatternStyleHoverX.this.col_start_x_6);
                    this.mMatrixCanvas.drawRect((float) ColX6, (float) ColY3, ((float) ColX6) + TspPatternStyleHoverX.this.col_width_6, ((float) ColY3) + TspPatternStyleHoverX.this.col_height_6, mRectPaintCross);
                    i = i4 + 1;
                } else {
                    return;
                }
            }
        }

        private void drawRect(float x, float y, Paint paint) {
            if (TspPatternStyleHoverX.this.isHovering && !TOOLTYPE.getIsHoverToolType()) {
                return;
            }
            if (y > ((float) TspPatternStyleHoverX.this.mScreenHeight) || x > ((float) TspPatternStyleHoverX.this.mScreenWidth) || y < 0.0f || x < 0.0f) {
                LtUtil.log_e(TspPatternStyleHoverX.this.CLASS_NAME, "drawRect", "You are out of bounds!");
                return;
            }
            check10RectRegion(x, y, paint);
            check6RectRegion(x, y, paint);
            if (TspPatternStyleHoverX.this.isPass10() && TspPatternStyleHoverX.this.isPass6()) {
                if (TspPatternStyleHoverX.this.successTest) {
                    TspPatternStyleHoverX.this.successTest = false;
                    if (TspPatternStyleHoverX.this.remoteCall) {
                        TspPatternStyleHoverX.this.setResult(-1);
                    }
                    TspPatternStyleHoverX.this.setResult(-1);
                    TspPatternStyleHoverX.this.finish();
                } else {
                    TspPatternStyleHoverX.this.setResult(0);
                    TspPatternStyleHoverX.this.finish();
                }
            }
        }

        private void check10RectRegion(float x, float y, Paint paint) {
            if (x > TspPatternStyleHoverX.this.col_start_x_10 && x < TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 && y > TspPatternStyleHoverX.this.col_start_y_10 && y < ((float) TspPatternStyleHoverX.this.mScreenHeight) - TspPatternStyleHoverX.this.col_start_y_10) {
                int count10y = (int) ((y - TspPatternStyleHoverX.this.col_start_y_10) / TspPatternStyleHoverX.this.col_height_10);
                int startRect10x = (int) TspPatternStyleHoverX.this.col_start_x_10;
                int startRect10y = (int) (TspPatternStyleHoverX.this.col_start_y_10 + (TspPatternStyleHoverX.this.col_height_10 * ((float) count10y)));
                this.mMatrixCanvas.drawRect((float) (startRect10x + 1), (float) (startRect10y + 1), (float) ((int) ((((float) startRect10x) + TspPatternStyleHoverX.this.col_width_10) - 1.0f)), (float) ((int) ((((float) startRect10y) + TspPatternStyleHoverX.this.col_height_10) - 1.0f)), paint);
                invalidate(new Rect(startRect10x - 1, startRect10y - 1, (int) (((float) startRect10x) + TspPatternStyleHoverX.this.col_width_10 + 1.0f), (int) (((float) startRect10y) + TspPatternStyleHoverX.this.col_height_10 + 1.0f)));
                TspPatternStyleHoverX.this.draw10[count10y] = true;
            }
            if (x > ((float) TspPatternStyleHoverX.this.mScreenWidth) - (TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10) && x < ((float) TspPatternStyleHoverX.this.mScreenWidth) - TspPatternStyleHoverX.this.col_start_x_10 && y > TspPatternStyleHoverX.this.col_start_y_10 && y < ((float) TspPatternStyleHoverX.this.mScreenHeight) - TspPatternStyleHoverX.this.col_start_y_10) {
                int count10y2 = (int) ((y - TspPatternStyleHoverX.this.col_start_y_10) / TspPatternStyleHoverX.this.col_height_10);
                int startRect10x2 = (int) (((float) TspPatternStyleHoverX.this.mScreenWidth) - (TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10));
                int startRect10y2 = (int) (TspPatternStyleHoverX.this.col_start_y_10 + (TspPatternStyleHoverX.this.col_height_10 * ((float) count10y2)));
                this.mMatrixCanvas.drawRect((float) (startRect10x2 + 1), (float) (startRect10y2 + 1), (float) ((int) ((((float) startRect10x2) + TspPatternStyleHoverX.this.col_width_10) - 1.0f)), (float) ((int) ((((float) startRect10y2) + TspPatternStyleHoverX.this.col_height_10) - 1.0f)), paint);
                invalidate(new Rect(startRect10x2 - 1, startRect10y2 - 1, (int) (((float) startRect10x2) + TspPatternStyleHoverX.this.col_width_10 + 1.0f), (int) (((float) startRect10y2) + TspPatternStyleHoverX.this.col_height_10 + 1.0f)));
                TspPatternStyleHoverX.this.draw10[TspPatternStyleHoverX.this.HEIGHT_BASIS_10 + count10y2] = true;
            }
            if (y > TspPatternStyleHoverX.this.col_start_y_10 && y < TspPatternStyleHoverX.this.col_start_y_10 + TspPatternStyleHoverX.this.col_height_10 && x > TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 && x < (((float) TspPatternStyleHoverX.this.mScreenWidth) - TspPatternStyleHoverX.this.col_start_x_10) - TspPatternStyleHoverX.this.col_width_10) {
                int count10x = (int) (((x - TspPatternStyleHoverX.this.col_start_x_10) - TspPatternStyleHoverX.this.col_width_10) / TspPatternStyleHoverX.this.col_width_10);
                int startRect10y3 = (int) TspPatternStyleHoverX.this.col_start_y_10;
                int startRect10x3 = (int) (TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 + (TspPatternStyleHoverX.this.col_width_10 * ((float) count10x)));
                this.mMatrixCanvas.drawRect((float) (startRect10x3 + 1), (float) (startRect10y3 + 1), (float) ((int) ((((float) startRect10x3) + TspPatternStyleHoverX.this.col_width_10) - 1.0f)), (float) ((int) ((((float) startRect10y3) + TspPatternStyleHoverX.this.col_height_10) - 1.0f)), paint);
                invalidate(new Rect(startRect10x3 - 1, startRect10y3 - 1, (int) (((float) startRect10x3) + TspPatternStyleHoverX.this.col_width_10 + 1.0f), (int) (((float) startRect10y3) + TspPatternStyleHoverX.this.col_height_10 + 1.0f)));
                TspPatternStyleHoverX.this.draw10[TspPatternStyleHoverX.this.HEIGHT_BASIS_10 + count10x + TspPatternStyleHoverX.this.HEIGHT_BASIS_10] = true;
            }
            if (y > ((float) TspPatternStyleHoverX.this.mScreenHeight) - (TspPatternStyleHoverX.this.col_start_y_10 + TspPatternStyleHoverX.this.col_height_10) && y < ((float) TspPatternStyleHoverX.this.mScreenHeight) - TspPatternStyleHoverX.this.col_start_y_10 && x > TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 && x < (((float) TspPatternStyleHoverX.this.mScreenWidth) - TspPatternStyleHoverX.this.col_start_x_10) - TspPatternStyleHoverX.this.col_width_10) {
                int count10x2 = (int) (((x - TspPatternStyleHoverX.this.col_start_x_10) - TspPatternStyleHoverX.this.col_width_10) / TspPatternStyleHoverX.this.col_width_10);
                int startRect10y4 = (int) (((float) TspPatternStyleHoverX.this.mScreenHeight) - (TspPatternStyleHoverX.this.col_start_y_10 + TspPatternStyleHoverX.this.col_height_10));
                int startRect10x4 = (int) (TspPatternStyleHoverX.this.col_start_x_10 + TspPatternStyleHoverX.this.col_width_10 + (TspPatternStyleHoverX.this.col_width_10 * ((float) count10x2)));
                this.mMatrixCanvas.drawRect((float) (startRect10x4 + 1), (float) (startRect10y4 + 1), (float) ((int) ((((float) startRect10x4) + TspPatternStyleHoverX.this.col_width_10) - 1.0f)), (float) ((int) ((((float) startRect10y4) + TspPatternStyleHoverX.this.col_height_10) - 1.0f)), paint);
                invalidate(new Rect(startRect10x4 - 1, startRect10y4 - 1, (int) (((float) startRect10x4) + TspPatternStyleHoverX.this.col_width_10 + 1.0f), (int) (((float) startRect10y4) + TspPatternStyleHoverX.this.col_height_10 + 1.0f)));
                TspPatternStyleHoverX.this.draw10[(((TspPatternStyleHoverX.this.HEIGHT_BASIS_10 + count10x2) + TspPatternStyleHoverX.this.HEIGHT_BASIS_10) + TspPatternStyleHoverX.this.WIDTH_BASIS_10) - 2] = true;
            }
        }

        private void check6RectRegion(float x, float y, Paint paint) {
            if (x > (((float) TspPatternStyleHoverX.this.mScreenWidth) / 2.0f) - (TspPatternStyleHoverX.this.col_width_6 / 2.0f) && x < (((float) TspPatternStyleHoverX.this.mScreenWidth) / 2.0f) + (TspPatternStyleHoverX.this.col_width_6 / 2.0f) && y > TspPatternStyleHoverX.this.col_start_y_6 && y < ((float) TspPatternStyleHoverX.this.mScreenHeight) - TspPatternStyleHoverX.this.col_start_y_6) {
                int count6y = (int) ((y - TspPatternStyleHoverX.this.col_start_y_6) / TspPatternStyleHoverX.this.col_height_6);
                int startRect6x = (int) ((((float) TspPatternStyleHoverX.this.mScreenWidth) / 2.0f) - (TspPatternStyleHoverX.this.col_width_6 / 2.0f));
                int startRect6y = (int) (TspPatternStyleHoverX.this.col_start_y_6 + (TspPatternStyleHoverX.this.col_height_6 * ((float) count6y)));
                this.mMatrixCanvas.drawRect((float) (startRect6x + 1), (float) (startRect6y + 1), (float) ((int) ((((float) startRect6x) + TspPatternStyleHoverX.this.col_width_6) - 1.0f)), (float) ((int) ((((float) startRect6y) + TspPatternStyleHoverX.this.col_height_6) - 1.0f)), paint);
                invalidate(new Rect(startRect6x - 1, startRect6y - 1, (int) (((float) startRect6x) + TspPatternStyleHoverX.this.col_width_6 + 1.0f), (int) (((float) startRect6y) + TspPatternStyleHoverX.this.col_height_6 + 1.0f)));
                TspPatternStyleHoverX.this.draw6[count6y] = true;
            }
            if (y > (((float) TspPatternStyleHoverX.this.mScreenHeight) / 2.0f) - (TspPatternStyleHoverX.this.col_height_6 / 2.0f) && y < (((float) TspPatternStyleHoverX.this.mScreenHeight) / 2.0f) + (TspPatternStyleHoverX.this.col_height_6 / 2.0f) && x > TspPatternStyleHoverX.this.col_start_x_6 && x < ((float) TspPatternStyleHoverX.this.mScreenWidth) - TspPatternStyleHoverX.this.col_start_x_6) {
                int count6x = (int) ((x - TspPatternStyleHoverX.this.col_start_x_6) / TspPatternStyleHoverX.this.col_width_6);
                int startRect6y2 = (int) ((((float) TspPatternStyleHoverX.this.mScreenHeight) / 2.0f) - (TspPatternStyleHoverX.this.col_height_6 / 2.0f));
                int startRect6x2 = (int) (TspPatternStyleHoverX.this.col_start_x_6 + (TspPatternStyleHoverX.this.col_width_6 * ((float) count6x)));
                this.mMatrixCanvas.drawRect((float) (startRect6x2 + 1), (float) (startRect6y2 + 1), (float) ((int) ((((float) startRect6x2) + TspPatternStyleHoverX.this.col_width_6) - 1.0f)), (float) ((int) ((((float) startRect6y2) + TspPatternStyleHoverX.this.col_height_6) - 1.0f)), paint);
                invalidate(new Rect(startRect6x2 - 1, startRect6y2 - 1, (int) (((float) startRect6x2) + TspPatternStyleHoverX.this.col_width_6 + 1.0f), (int) (((float) startRect6y2) + TspPatternStyleHoverX.this.col_height_6 + 1.0f)));
                TspPatternStyleHoverX.this.draw6[TspPatternStyleHoverX.this.HEIGHT_BASIS_6 + count6x] = true;
            }
        }

        public void onGloveEnableChanged(boolean isEnable) {
            if (isEnable) {
                TOOLTYPE.setToolType(TOOLTYPE.TOOLTYPE_GLOVE_TOUCH);
            } else {
                TOOLTYPE.setToolType(TOOLTYPE.TOOLTYPE_FINGER_TOUCH);
            }
        }
    }

    public TspPatternStyleHoverX() {
        super(TAG);
    }

    private void setTSP() {
        Display mDisplay = ((WindowManager) getApplication().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        mDisplay.getRealSize(outpoint);
        this.mScreenWidth = outpoint.x;
        this.mScreenHeight = outpoint.y;
        this.WIDTH_BASIS = Spec.getInt(Spec.TSP_X_AXIS_CHANNEL);
        this.HEIGHT_BASIS = Spec.getInt(Spec.TSP_Y_AXIS_CHANNEL);
        this.col_height = ((float) this.mScreenHeight) / ((float) this.HEIGHT_BASIS);
        this.col_width = ((float) this.mScreenWidth) / ((float) this.WIDTH_BASIS);
        this.HEIGHT_BASIS_10 = (int) ((((float) this.mScreenHeight) - (this.col_height * 2.0f)) / (this.col_height * 2.5f));
        this.WIDTH_BASIS_10 = (int) ((((float) this.mScreenWidth) - (this.col_width * 2.0f)) / (this.col_width * 2.5f));
        this.col_height_10 = (((float) this.mScreenHeight) - (this.col_height * 2.0f)) / ((float) this.HEIGHT_BASIS_10);
        this.col_width_10 = (((float) this.mScreenWidth) - (this.col_width * 2.0f)) / ((float) this.WIDTH_BASIS_10);
        this.col_start_x_10 = (((float) this.mScreenWidth) - (this.col_width_10 * ((float) this.WIDTH_BASIS_10))) / 2.0f;
        this.col_start_y_10 = (((float) this.mScreenHeight) - (this.col_height_10 * ((float) this.HEIGHT_BASIS_10))) / 2.0f;
        this.HEIGHT_BASIS_6 = (int) ((((float) this.mScreenHeight) - ((this.col_start_y_10 + this.col_height_10) * 2.0f)) / (this.col_height * 1.5f));
        this.WIDTH_BASIS_6 = (int) ((((float) this.mScreenWidth) - ((this.col_start_x_10 + this.col_width_10) * 2.0f)) / (this.col_width * 1.5f));
        this.col_height_6 = (((float) this.mScreenHeight) - ((this.col_start_y_10 + this.col_height_10) * 2.0f)) / ((float) this.HEIGHT_BASIS_6);
        this.col_width_6 = (((float) this.mScreenWidth) - ((this.col_start_x_10 + this.col_width_10) * 2.0f)) / ((float) this.WIDTH_BASIS_6);
        this.col_start_x_6 = this.col_start_x_10 + this.col_width_10;
        this.col_start_y_6 = this.col_start_y_10 + this.col_height_10;
        this.draw10 = new boolean[((this.HEIGHT_BASIS_10 + this.WIDTH_BASIS_10) * 2)];
        this.draw6 = new boolean[(this.HEIGHT_BASIS_6 + this.WIDTH_BASIS_6)];
        for (int i = 0; i < (this.HEIGHT_BASIS_10 + this.WIDTH_BASIS_10) * 2; i++) {
            this.draw10[i] = false;
        }
        for (int i2 = 0; i2 < this.HEIGHT_BASIS_6 + this.WIDTH_BASIS_6; i2++) {
            this.draw6[i2] = false;
        }
        this.mTopmostOfMatrix = 0;
        this.mBottommostOfMatrix = this.HEIGHT_BASIS - 1;
        this.mCenterOfVerticalOfMatrix = this.HEIGHT_BASIS / 2;
        this.mLeftmostOfMatrix = 0;
        this.mRightmostOfMatrix = this.WIDTH_BASIS - 1;
        this.mCenterOfHorizontalOfMatrix = this.WIDTH_BASIS / 2;
        this.KEY_TIMER_EXPIRED = 1;
        this.MILLIS_IN_SEC = 1000;
        this.KEY_TIMEOUT = 2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mModuleDevice = ModuleDevice.instance(this);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        this.isHovering = getIntent().getExtras().getBoolean("isHovering");
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("TEST_TSP_SELF = ");
            sb.append(getIntent().getBooleanExtra("TEST_TSP_SELF", false));
            LtUtil.log_i(this.CLASS_NAME, "onCreate", sb.toString());
            if (!getIntent().getBooleanExtra("TEST_TSP_SELF", true)) {
                this.successTest = false;
                this.needFailPopupDispaly = true;
            }
        } catch (Exception e) {
            LtUtil.log_e(this.CLASS_NAME, "onCreate", "Exception");
        }
        setTSP();
        decideRemote();
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "TouchTest is created");
        setContentView(new MyView(this));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LtUtil.log_d(this.CLASS_NAME, "onResume", null);
        if (this.isHovering) {
            LtUtil.log_d(this.CLASS_NAME, "onResume", "EnableHovering");
            this.mModuleDevice.hoveringOnOFF(EgisFingerprint.MAJOR_VERSION);
            GloveReceiver.registerBroadcastReceiver(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LtUtil.log_d(this.CLASS_NAME, "onPause", null);
        if (this.isHovering) {
            LtUtil.log_d(this.CLASS_NAME, "onPause", "DisableHovering");
            this.mModuleDevice.hoveringOnOFF("0");
            GloveReceiver.unregisterBroadcastReceiver(this);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24 && (!isPass10() || !isPass6())) {
            setResult(0);
            finish();
        }
        return true;
    }

    private void decideRemote() {
        this.remoteCall = getIntent().getBooleanExtra("RemoteCall", false);
    }

    private boolean isNeededCheck(int row, int column) {
        return row == this.mTopmostOfMatrix || row == this.mBottommostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix;
    }

    private boolean isNeededCheck_Hovering(int row, int column) {
        if (row != this.mTopmostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix) {
            return !(row != this.mBottommostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix) || column == this.mLeftmostOfMatrix + 1 || column == this.mRightmostOfMatrix - 1;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean isPass10() {
        boolean isPass = true;
        for (int i = 0; i < ((this.HEIGHT_BASIS_10 + this.WIDTH_BASIS_10) - 2) * 2; i++) {
            isPass = isPass && this.draw10[i];
        }
        return isPass;
    }

    /* access modifiers changed from: private */
    public boolean isPass6() {
        boolean isPass = true;
        for (int i = 0; i < this.HEIGHT_BASIS_6 + this.WIDTH_BASIS_6; i++) {
            isPass = isPass && this.draw6[i];
        }
        return isPass;
    }
}
