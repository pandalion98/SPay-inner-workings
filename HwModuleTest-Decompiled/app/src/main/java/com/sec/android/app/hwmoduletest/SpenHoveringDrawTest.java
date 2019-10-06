package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class SpenHoveringDrawTest extends BaseActivity {
    private static final String TAG = "SpenHoveringDrawTest";
    private final float MARGINE_X = 0.0f;
    private final float MARGINE_Y = 6.0f;
    private final int SPEC_HEIGHT = 2;
    private final int SPEC_WIDTH = 6;
    /* access modifiers changed from: private */
    public boolean isTestFinish = false;
    private boolean isTestPass = false;
    /* access modifiers changed from: private */
    public boolean isTestStart = false;
    /* access modifiers changed from: private */
    public float mNPixelForOneMM;
    /* access modifiers changed from: private */
    public LinkedList<RectNode> mRectList = new LinkedList<>();

    public class HoverView extends View {
        private long MILLIS_IN_SEC = 100;
        private float col_height = 0.0f;
        private float col_width = 0.0f;
        private boolean isHovering;
        /* access modifiers changed from: private */
        public boolean isInputLock = false;
        private boolean isTesteShouldAtOnce = true;
        private Paint mClickPaint;
        private float mHeightCross = 0.0f;
        private Paint mLineFailPaint;
        private Paint mLinePaint;
        private Bitmap mMatrixBitmap;
        private Canvas mMatrixCanvas;
        private float mPreTouchedX = 0.0f;
        private float mPreTouchedY = 0.0f;
        private String mResultText;
        private int mScreenHeight;
        private int mScreenWidth;
        private float mTouchedX = 0.0f;
        private float mTouchedY = 0.0f;
        private float mWidthBetween = 0.0f;
        private float mWidthCross = 0.0f;
        final /* synthetic */ SpenHoveringDrawTest this$0;

        public HoverView(SpenHoveringDrawTest this$02, Context context) {
            final SpenHoveringDrawTest spenHoveringDrawTest = this$02;
            Context context2 = context;
            this.this$0 = spenHoveringDrawTest;
            super(context2);
            setKeepScreenOn(true);
            Display mDisplay = ((WindowManager) context2.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(outpoint);
            this.mScreenWidth = outpoint.x;
            this.mScreenHeight = outpoint.y;
            StringBuilder sb = new StringBuilder();
            sb.append("mScreenWidth = ");
            sb.append(this.mScreenWidth);
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mScreenHeight = ");
            sb2.append(this.mScreenHeight);
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("nRect(float) = ");
            float f = 6.0f;
            sb3.append((((float) this.mScreenHeight) - ((this$02.mNPixelForOneMM * 6.0f) * 2.0f)) / (this$02.mNPixelForOneMM * 2.0f));
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb3.toString());
            int nRect = (int) ((((float) this.mScreenHeight) - ((this$02.mNPixelForOneMM * 6.0f) * 2.0f)) / (this$02.mNPixelForOneMM * 2.0f));
            float overLapWidth = ((((float) this.mScreenWidth) - (this$02.mNPixelForOneMM * 6.0f)) - ((this$02.mNPixelForOneMM * 0.0f) * 2.0f)) / ((float) (nRect - 1));
            float height = (((float) this.mScreenHeight) - ((this$02.mNPixelForOneMM * 6.0f) * 2.0f)) / ((float) nRect);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("nRect = ");
            sb4.append(nRect);
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append("overLapWidth = ");
            sb5.append(overLapWidth);
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("height = ");
            sb6.append(height);
            LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb6.toString());
            this.mMatrixBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.ARGB_8888), this.mScreenWidth, this.mScreenHeight, false);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            Paint transPainter = new Paint();
            transPainter.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            this.mMatrixCanvas.drawRect(0.0f, 0.0f, (float) this.mScreenWidth, (float) this.mScreenHeight, transPainter);
            setBackgroundResource(C0268R.color.white);
            float currentY = this$02.mNPixelForOneMM * 6.0f;
            float currentX = 0.0f * this$02.mNPixelForOneMM;
            int i = 0;
            while (i < nRect) {
                RectF r = new RectF();
                r.left = currentX;
                r.top = currentY;
                r.right = currentX + (this$02.mNPixelForOneMM * f);
                r.bottom = currentY + height;
                currentX += overLapWidth;
                currentY += height;
                StringBuilder sb7 = new StringBuilder();
                Display mDisplay2 = mDisplay;
                sb7.append("currentX = ");
                sb7.append(currentX);
                sb7.append(", currentY = ");
                sb7.append(currentY);
                LtUtil.log_d(this$02.CLASS_NAME, "HoverView", sb7.toString());
                this$02.mRectList.add(new RectNode(r));
                i++;
                mDisplay = mDisplay2;
                Context context3 = context;
                f = 6.0f;
            }
            setPaint();
            initRect();
            drawTeachingPoint();
            this.isHovering = false;
            if (this.isInputLock) {
                float f2 = currentX;
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        HoverView.this.isInputLock = false;
                    }
                }, this.MILLIS_IN_SEC);
                return;
            }
        }

        private void setPaint() {
            this.mLinePaint = new Paint();
            this.mLinePaint.setAntiAlias(true);
            this.mLinePaint.setColor(-16777216);
            this.mLinePaint.setStyle(Style.STROKE);
            this.mLinePaint.setStrokeJoin(Join.ROUND);
            this.mLinePaint.setStrokeWidth(5.0f);
            this.mLineFailPaint = new Paint();
            this.mLineFailPaint.setAntiAlias(true);
            this.mLineFailPaint.setColor(-65536);
            this.mLineFailPaint.setStrokeWidth(20.0f);
            this.mClickPaint = new Paint();
            this.mClickPaint.setAntiAlias(false);
            this.mClickPaint.setStyle(Style.FILL);
            this.mClickPaint.setColor(-16711936);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(this.mMatrixBitmap, 0.0f, 0.0f, null);
        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (event.getToolType(0) != 2) {
                return true;
            }
            switch (action) {
                case 0:
                    this.mTouchedX = event.getX();
                    this.mTouchedY = event.getY();
                    drawPoint(this.mTouchedX, this.mTouchedY, false);
                    if (!this.this$0.isTestFinish && !this.isInputLock) {
                        this.this$0.isTestFinish = true;
                        drawResultText(false);
                        break;
                    }
                case 1:
                    this.mTouchedX = event.getX();
                    this.mTouchedY = event.getY();
                    drawPoint(this.mTouchedX, this.mTouchedY, false);
                    if (!this.this$0.isTestFinish && !this.isInputLock) {
                        this.this$0.isTestFinish = true;
                        drawResultText(false);
                        break;
                    }
                case 2:
                    for (int i = 0; i < event.getHistorySize(); i++) {
                        this.mPreTouchedX = this.mTouchedX;
                        this.mPreTouchedY = this.mTouchedY;
                        this.mTouchedX = event.getHistoricalX(i);
                        this.mTouchedY = event.getHistoricalY(i);
                        drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, false);
                    }
                    this.mPreTouchedX = this.mTouchedX;
                    this.mPreTouchedY = this.mTouchedY;
                    this.mTouchedX = event.getX();
                    this.mTouchedY = event.getY();
                    drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, false);
                    if (!this.this$0.isTestFinish && !this.isInputLock) {
                        this.this$0.isTestFinish = true;
                        drawResultText(false);
                        break;
                    }
            }
            return true;
        }

        public boolean onHoverEvent(MotionEvent event) {
            int action = event.getAction();
            if (event.getToolType(0) != 2) {
                return true;
            }
            drawByEvent(event);
            return true;
        }

        private void drawByEvent(MotionEvent event) {
            int action = event.getAction();
            if (!this.this$0.isTestFinish) {
                int i = 0;
                if (action != 7) {
                    switch (action) {
                        case 9:
                            this.mTouchedX = event.getX();
                            this.mTouchedY = event.getY();
                            if (!this.isInputLock) {
                                drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                                drawPoint(this.mTouchedX, this.mTouchedY, true);
                                drawHoverText(true);
                                drawTeachingPoint();
                                if (!this.this$0.isTestStart) {
                                    this.this$0.isTestStart = true;
                                }
                                this.isHovering = true;
                                break;
                            }
                            break;
                        case 10:
                            if (this.isHovering) {
                                this.mPreTouchedX = this.mTouchedX;
                                this.mPreTouchedY = this.mTouchedY;
                                this.mTouchedX = event.getX();
                                this.mTouchedY = event.getY();
                                if (!this.isInputLock && this.mPreTouchedX == this.mTouchedX && this.mPreTouchedY == this.mTouchedY) {
                                    drawPoint(this.mTouchedX, this.mTouchedY, true);
                                }
                                this.isHovering = false;
                            }
                            drawHoverText(false);
                            break;
                    }
                } else {
                    while (true) {
                        int i2 = i;
                        if (i2 >= event.getHistorySize()) {
                            break;
                        }
                        this.mPreTouchedX = this.mTouchedX;
                        this.mPreTouchedY = this.mTouchedY;
                        this.mTouchedX = event.getHistoricalX(i2);
                        this.mTouchedY = event.getHistoricalY(i2);
                        if (!this.isInputLock) {
                            drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                            drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, true);
                            drawTeachingPoint();
                        }
                        i = i2 + 1;
                    }
                    this.mPreTouchedX = this.mTouchedX;
                    this.mPreTouchedY = this.mTouchedY;
                    this.mTouchedX = event.getX();
                    this.mTouchedY = event.getY();
                    if (!this.isInputLock) {
                        if (!this.this$0.isTestStart) {
                            this.this$0.isTestStart = true;
                        }
                        this.isHovering = true;
                        drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                        drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, true);
                        drawTeachingPoint();
                    }
                }
            }
        }

        private void drawLine(float preX, float preY, float x, float y, boolean isHover) {
            float f = preX;
            float f2 = preY;
            float f3 = x;
            float f4 = y;
            if (isHover) {
                this.mMatrixCanvas.drawLine(f, f2, f3, f4, this.mLinePaint);
            } else {
                this.mMatrixCanvas.drawLine(f, f2, f3, f4, this.mLineFailPaint);
            }
            if (f >= f3) {
                int highX = (int) f;
                int lowX = (int) f3;
            } else {
                int highX2 = (int) f3;
                int lowX2 = (int) f;
            }
            if (f2 >= f4) {
                int highY = (int) f2;
                int lowY = (int) f4;
            } else {
                int highY2 = (int) f4;
                int lowY2 = (int) f2;
            }
            invalidate();
        }

        private void drawPoint(float x, float y, boolean isHover) {
            if (isHover) {
                this.mMatrixCanvas.drawPoint(x, y, this.mLinePaint);
            } else {
                this.mMatrixCanvas.drawPoint(x, y, this.mLineFailPaint);
            }
            invalidate();
        }

        private void initRect() {
            Paint mRectPaintCross = new Paint();
            mRectPaintCross.setColor(-16777216);
            mRectPaintCross.setStyle(Style.STROKE);
            Iterator it = this.this$0.mRectList.iterator();
            while (it.hasNext()) {
                this.mMatrixCanvas.drawRect(((RectNode) it.next()).getRect(), mRectPaintCross);
            }
        }

        private void drawTeachingPoint() {
            float pxsize = TypedValue.applyDimension(5, 1.0f, getResources().getDisplayMetrics());
            Paint mCirclePaint = new Paint();
            mCirclePaint.setStrokeWidth(pxsize / 4.0f);
            mCirclePaint.setColor(-65536);
            mCirclePaint.setStyle(Style.STROKE);
            this.mMatrixCanvas.drawCircle(((float) this.mScreenWidth) / 2.0f, ((float) this.mScreenHeight) / 2.0f, pxsize, mCirclePaint);
        }

        private void drawRect(float x, float y, Paint paint) {
            checkCrossRectRegion(x, y, paint);
            checkPassNfinishSpenTest();
        }

        private void checkPassNfinishSpenTest() {
            if (this.this$0.isPassCross()) {
                drawResultText(true);
                this.this$0.setResult(-1);
                passAndFinish();
            }
        }

        public void passAndFinish() {
            LtUtil.log_i(this.this$0.CLASS_NAME, "passAndFinish", "Test Passed!");
            this.this$0.finish();
        }

        private void checkCrossRectRegion(float x, float y, Paint paint) {
            Iterator it = this.this$0.mRectList.iterator();
            while (it.hasNext()) {
                RectNode r = (RectNode) it.next();
                if (!r.isTouched && r.getRect().contains(x, y)) {
                    this.mMatrixCanvas.drawRect(r.getRect(), paint);
                    invalidate();
                    r.isTouched = true;
                    int index = this.this$0.mRectList.indexOf(r);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Touched Area: ");
                    sb.append(index);
                    LtUtil.log_d(this.this$0.CLASS_NAME, "checkCrossRectRegion", sb.toString());
                }
            }
        }

        private void drawHoverText(boolean result) {
            Paint paint = new Paint();
            if (result) {
                setBackgroundResource(C0268R.color.lightblue);
                this.mResultText = getResources().getString(C0268R.string.spen_hovertest_result_hovering);
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setTextAlign(Align.CENTER);
                paint.setTextSize(80.0f);
                this.mMatrixCanvas.drawText(this.mResultText, (float) ((this.mScreenWidth / 8) * 5), (float) ((this.mScreenHeight / 8) * 1), paint);
            } else {
                setBackgroundResource(C0268R.color.white);
                paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
                paint.setStyle(Style.FILL);
                this.mMatrixCanvas.drawRect((float) (((this.mScreenWidth / 8) * 2) + ((this.mScreenWidth / 8) / 2)), 10.0f, (float) ((this.mScreenWidth / 8) * 8), (float) ((this.mScreenHeight / 8) * 2), paint);
            }
            invalidate();
        }

        private void drawResultText(boolean result) {
            Paint paint = new Paint();
            if (result) {
                this.mResultText = getResources().getString(C0268R.string.PASS);
                paint.setColor(-16776961);
            } else {
                this.mResultText = getResources().getString(C0268R.string.FAIL);
                paint.setColor(-65536);
            }
            paint.setTextAlign(Align.CENTER);
            paint.setAntiAlias(true);
            paint.setTextSize(150.0f);
            this.mMatrixCanvas.drawText(this.mResultText, (float) ((this.mScreenWidth / 8) * 2), (float) ((this.mScreenHeight / 8) * 6), paint);
            invalidate();
        }
    }

    static class RectNode {
        boolean isTouched = false;
        RectF mRect;

        RectNode(RectF rect) {
            this.mRect = rect;
        }

        public RectF getRect() {
            return this.mRect;
        }
    }

    public SpenHoveringDrawTest() {
        super(TAG);
    }

    private void setTSP() {
        this.mNPixelForOneMM = TypedValue.applyDimension(5, 1.0f, getResources().getDisplayMetrics());
        StringBuilder sb = new StringBuilder();
        sb.append("mNPixelForOneMM = ");
        sb.append(this.mNPixelForOneMM);
        LtUtil.log_d(this.CLASS_NAME, "HoverView", sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setTSP();
        setContentView(new HoverView(this, this));
        LtUtil.setRemoveSystemUI(getWindow(), true);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24 && !isPassCross()) {
            setResult(0);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public boolean isPassCross() {
        Iterator it = this.mRectList.iterator();
        while (it.hasNext()) {
            if (!((RectNode) it.next()).isTouched) {
                return false;
            }
        }
        return true;
    }
}
