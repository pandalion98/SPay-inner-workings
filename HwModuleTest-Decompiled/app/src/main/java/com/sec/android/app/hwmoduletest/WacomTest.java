package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Spec;
import java.lang.reflect.Array;
import java.util.Calendar;

public class WacomTest extends BaseActivity {
    private static final int AMETA_PEN_ON = 33554432;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS = 19;
    protected int KEY_TIMEOUT = 2;
    protected int KEY_TIMER_EXPIRED = 1;
    protected int MILLIS_IN_SEC = 1000;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS = 11;
    /* access modifiers changed from: private */
    public boolean[][] click;
    /* access modifiers changed from: private */
    public boolean[][] draw;
    /* access modifiers changed from: private */
    public boolean[][] isDrawArea;
    private int mBottommostOfMatrix = 0;
    private int mCenterOfHorizontalOfMatrix = 0;
    private int mCenterOfVerticalOfMatrix = 0;
    private long mCurrentTime = 0;
    /* access modifiers changed from: private */
    public boolean mIsPressedBackkey = false;
    /* access modifiers changed from: private */
    public boolean mIsWacom = true;
    private int mLeftmostOfMatrix = 0;
    private int mRightmostOfMatrix = 0;
    protected Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == WacomTest.this.KEY_TIMER_EXPIRED) {
                WacomTest.this.mIsPressedBackkey = false;
                LtUtil.log_e(WacomTest.this.CLASS_NAME, "mTimerHandler", "KEY_TIMER_EXPIRED");
            }
        }
    };
    private int mTopmostOfMatrix = 0;
    /* access modifiers changed from: private */
    public int passFlag = 0;
    /* access modifiers changed from: private */
    public boolean remoteCall = false;

    public class MyView extends View {
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
        private int mScreenHeight;
        private int mScreenWidth;
        private float mTouchedX = 0.0f;
        private float mTouchedY = 0.0f;

        public MyView(Context context) {
            super(context);
            setKeepScreenOn(true);
            this.mScreenWidth = WacomTest.this.getWindowManager().getDefaultDisplay().getWidth();
            this.mScreenHeight = WacomTest.this.getWindowManager().getDefaultDisplay().getHeight();
            this.mMatrixBitmap = Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.ARGB_8888);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
            this.mLineBitmap = Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.ARGB_8888);
            this.mLineCanvas = new Canvas(this.mLineBitmap);
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
            this.mLinePaint.setColor(-16777216);
            this.mClickPaint = new Paint();
            this.mClickPaint.setAntiAlias(false);
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

        public boolean onTouchEvent(MotionEvent event) {
            if (WacomTest.this.mIsWacom) {
                if (event.getMetaState() == 33554432) {
                    drawByEvent(event);
                }
            } else if (event.getMetaState() != 33554432) {
                drawByEvent(event);
            }
            return true;
        }

        private void drawByEvent(MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.mTouchedX = event.getX();
                    this.mTouchedY = event.getY();
                    drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                    this.isTouchDown = true;
                    return;
                case 1:
                    if (this.isTouchDown) {
                        this.mPreTouchedX = this.mTouchedX;
                        this.mPreTouchedY = this.mTouchedY;
                        this.mTouchedX = event.getX();
                        this.mTouchedY = event.getY();
                        if (this.mPreTouchedX == this.mTouchedX && this.mPreTouchedY == this.mTouchedY) {
                            drawPoint(this.mTouchedX, this.mTouchedY);
                        }
                        this.isTouchDown = false;
                        return;
                    }
                    return;
                case 2:
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
                        return;
                    }
                    return;
                default:
                    return;
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
            float col_height = ((float) this.mScreenHeight) / ((float) WacomTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) WacomTest.this.WIDTH_BASIS);
            Paint mRectPaint = new Paint();
            mRectPaint.setColor(-16777216);
            int ColX = 0;
            int i = 0;
            while (i < WacomTest.this.HEIGHT_BASIS) {
                int ColY = (int) (((float) i) * col_height);
                int ColX2 = ColX;
                int ColX3 = 0;
                while (true) {
                    int j = ColX3;
                    if (j >= WacomTest.this.WIDTH_BASIS) {
                        break;
                    }
                    int ColX4 = (int) (((float) j) * col_width);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) ColX4, (float) this.mScreenHeight, paint);
                    WacomTest.this.draw[i][j] = false;
                    WacomTest.this.click[i][j] = false;
                    ColX3 = j + 1;
                    ColX2 = ColX4;
                }
                i++;
                ColX = ColX2;
            }
            this.mMatrixCanvas.drawRect(col_width + 1.0f, col_height + 1.0f, (((float) (WacomTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (WacomTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect((((float) ((WacomTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, col_height + 1.0f, (((float) (WacomTest.this.WIDTH_BASIS - 1)) * col_width) - 1.0f, (((float) (WacomTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect(col_width + 1.0f, (((float) ((WacomTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (WacomTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (WacomTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect((((float) ((WacomTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, (((float) ((WacomTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (WacomTest.this.WIDTH_BASIS - 1)) * col_width) - 1.0f, (((float) (WacomTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
        }

        private void drawRect(float x, float y, Paint paint) {
            float col_height = ((float) this.mScreenHeight) / ((float) WacomTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) WacomTest.this.WIDTH_BASIS);
            int countX = (int) (x / col_width);
            int countY = (int) (y / col_height);
            float ColX = ((float) countX) * col_width;
            float ColY = ((float) countY) * col_height;
            if (countY > WacomTest.this.HEIGHT_BASIS - 1 || countX > WacomTest.this.WIDTH_BASIS - 1) {
                LtUtil.log_e(WacomTest.this.CLASS_NAME, "drawRect", "You are out of bounds!");
                return;
            }
            if (!WacomTest.this.draw[countY][countX]) {
                WacomTest.this.draw[countY][countX] = true;
                if (!WacomTest.this.draw[countY][countX] || !WacomTest.this.isDrawArea[countY][countX]) {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), this.mNonClickPaint);
                } else {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), paint);
                }
                invalidate(new Rect((int) (ColX - 1.0f), (int) (ColY - 1.0f), (int) (ColX + col_width + 1.0f), (int) (ColY + col_height + 1.0f)));
            }
            if (WacomTest.this.isPass() && WacomTest.this.passFlag == 0) {
                WacomTest.this.passFlag = WacomTest.this.passFlag + 1;
                if (WacomTest.this.remoteCall) {
                    WacomTest.this.setResult(-1);
                    LtUtil.log_i(WacomTest.this.CLASS_NAME, "drawRect", "Wacom Test: Pass");
                }
                WacomTest.this.finish();
            }
        }
    }

    public WacomTest() {
        super("WacomTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setGridSizebyModel();
        initGridSettings();
        setContentView(new MyView(this));
        getWindow().addFlags(128);
        fillUpMatrix();
    }

    private void setGridSizebyModel() {
        this.HEIGHT_BASIS = Spec.getInt(Spec.WACOM_HEIGHT_BASIS);
        this.WIDTH_BASIS = Spec.getInt(Spec.WACOM_WIDTH_BASIS);
        if (this.HEIGHT_BASIS != 0 && this.WIDTH_BASIS != 0) {
            this.HEIGHT_BASIS = Spec.getInt(Spec.WACOM_HEIGHT_BASIS);
            this.WIDTH_BASIS = Spec.getInt(Spec.WACOM_WIDTH_BASIS);
        }
    }

    private void initGridSettings() {
        this.click = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.draw = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.isDrawArea = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.mTopmostOfMatrix = 0;
        this.mBottommostOfMatrix = this.HEIGHT_BASIS - 1;
        this.mCenterOfVerticalOfMatrix = this.HEIGHT_BASIS / 2;
        this.mLeftmostOfMatrix = 0;
        this.mRightmostOfMatrix = this.WIDTH_BASIS - 1;
        this.mCenterOfHorizontalOfMatrix = this.WIDTH_BASIS / 2;
    }

    private void decideRemote() {
        this.remoteCall = getIntent().getBooleanExtra("RemoteCall", false);
    }

    private void fillUpMatrix() {
        for (int row = 0; row < this.HEIGHT_BASIS; row++) {
            for (int column = 0; column < this.WIDTH_BASIS; column++) {
                if (isNeededCheck(row, column)) {
                    this.isDrawArea[row][column] = true;
                } else {
                    this.isDrawArea[row][column] = false;
                }
            }
        }
    }

    private boolean isNeededCheck(int row, int column) {
        return row == this.mTopmostOfMatrix || row == this.mBottommostOfMatrix || row == this.mCenterOfVerticalOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix || column == this.mCenterOfHorizontalOfMatrix;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        finish();
    }

    /* access modifiers changed from: private */
    public boolean isPass() {
        boolean isPass = true;
        int i = 0;
        while (i < this.HEIGHT_BASIS) {
            boolean isPass2 = isPass;
            for (int j = 0; j < this.WIDTH_BASIS; j++) {
                boolean z = true;
                if (this.isDrawArea[i][j]) {
                    if (!isPass2 || !this.draw[i][j]) {
                        z = false;
                    }
                    isPass2 = z;
                }
            }
            i++;
            isPass = isPass2;
        }
        return isPass;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 3) {
            return true;
        }
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        LtUtil.log_e(this.CLASS_NAME, "onKeyDown", "This is back_key");
        if (!this.mIsPressedBackkey) {
            this.mCurrentTime = Calendar.getInstance().getTimeInMillis();
            this.mIsPressedBackkey = true;
            startTimer();
        } else {
            this.mIsPressedBackkey = false;
            Calendar rightNow = Calendar.getInstance();
            StringBuilder sb = new StringBuilder();
            sb.append("rightNow.getTimeInMillis() = ");
            sb.append(rightNow.getTimeInMillis());
            sb.append("mCurrentTime = ");
            sb.append(this.mCurrentTime);
            LtUtil.log_e(this.CLASS_NAME, "onKeyDown", sb.toString());
            if (rightNow.getTimeInMillis() <= this.mCurrentTime + (((long) this.KEY_TIMEOUT) * ((long) this.MILLIS_IN_SEC))) {
                finish();
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void startTimer() {
        this.mTimerHandler.sendEmptyMessageDelayed(this.KEY_TIMER_EXPIRED, ((long) this.KEY_TIMEOUT) * ((long) this.MILLIS_IN_SEC));
    }
}
