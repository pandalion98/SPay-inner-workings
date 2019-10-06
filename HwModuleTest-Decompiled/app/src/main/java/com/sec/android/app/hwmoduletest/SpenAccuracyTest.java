package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.lang.reflect.Array;

public class SpenAccuracyTest extends BaseActivity {
    private static final String TAG = "SpenAccuracyTest";
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS = 28;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS_CROSS = 0;
    private int SIZE_RECT = 4;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS = 16;
    private int WIDTH_BASIS_CROSS = 0;
    private boolean dialog_showing = false;
    /* access modifiers changed from: private */
    public boolean[][] draw;
    /* access modifiers changed from: private */
    public boolean[] drawCross;
    /* access modifiers changed from: private */
    public boolean[][] isDrawArea;
    /* access modifiers changed from: private */
    public boolean isStyleX = false;
    private int mBottommostOfMatrix;
    private Handler mHandler = new Handler();
    private int mLeftmostOfMatrix;
    private ModuleDevice mModuleDevice;
    private int mOldHoveringSetting = 0;
    private int mRightmostOfMatrix;
    private String mTSPHover;
    private String mTestCase;
    private int mTopmostOfMatrix;
    /* access modifiers changed from: private */
    public int passFlag = 0;
    private boolean remoteCall = false;

    public enum ColorSet {
        BLACK("BLACK", Color.rgb(217, 217, 217), "ZK"),
        YELLOWGREEN("YELLOW GREEN", Color.rgb(251, 225, 86), "YZ"),
        LAVENDER("LAVENDER(PURPLE)", Color.rgb(239, 170, 215), "LP"),
        TITANSILVER("TITAN SILVER", Color.rgb(146, 176, 226), "VS"),
        COPPER("COPPER(BROWN)", Color.rgb(192, 127, 101), "PN"),
        BLUESILVER("BLUE SILVER", Color.rgb(108, 169, 232), "BS"),
        SPECIALLAVENDER("SPECIAL LAVENDER", Color.rgb(204, 166, 234), "AS"),
        WHITE("WHITE", Color.rgb(217, 217, 217), "SW"),
        WHITESILVER("WHITESILVER", Color.rgb(146, 176, 226), "WS");
        
        private final String code;
        private final int color;
        private final String name;

        private ColorSet(String _name, int _color, String _code) {
            this.name = _name;
            this.color = _color;
            this.code = _code;
        }

        public String getName() {
            return this.name;
        }

        public int getColor() {
            return this.color;
        }

        public String getCode() {
            return this.code;
        }

        public static int colorFromCode(String colorCode) {
            ColorSet[] values;
            for (ColorSet s : values()) {
                if (s.code.equalsIgnoreCase(colorCode)) {
                    return s.color;
                }
            }
            return Color.rgb(112, 112, 112);
        }
    }

    public class MyView extends View {
        private float col_height = 0.0f;
        private float col_width = 0.0f;
        private boolean isTouchDown;
        private Paint mClickPaint;
        private Paint mEmptyPaint;
        private float mHeightCross = 0.0f;
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
        private float mWidthBetween = 0.0f;
        private float mWidthCross = 0.0f;

        public MyView(Context context) {
            super(context);
            setKeepScreenOn(true);
            Display mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(outpoint);
            this.mScreenWidth = outpoint.x;
            this.mScreenHeight = outpoint.y;
            this.mMatrixBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.RGB_565), this.mScreenWidth, this.mScreenHeight, false);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
            this.col_height = ((float) this.mScreenHeight) / ((float) SpenAccuracyTest.this.HEIGHT_BASIS);
            this.col_width = ((float) this.mScreenWidth) / ((float) SpenAccuracyTest.this.WIDTH_BASIS);
            this.mWidthBetween = this.col_width;
            this.mWidthCross = (((float) this.mScreenWidth) - (this.col_width * 1.0f)) / ((float) ((SpenAccuracyTest.this.HEIGHT_BASIS_CROSS - 1) + 4));
            this.mHeightCross = this.col_height / 2.0f;
            setPaint();
            initRect();
            drawTeachingPoint();
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
            this.mLinePaint.setStrokeWidth(2.0f);
            this.mLinePaint.setColor(ColorSet.colorFromCode(readBleSpenColorCode()));
            this.mClickPaint = new Paint();
            this.mClickPaint.setAntiAlias(false);
            this.mClickPaint.setStyle(Style.FILL);
            this.mClickPaint.setColor(-16711936);
            this.mClickPaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
            this.mNonClickPaint = new Paint();
            this.mNonClickPaint.setAntiAlias(false);
            this.mNonClickPaint.setColor(-1);
            this.mEmptyPaint = new Paint();
            this.mEmptyPaint.setAntiAlias(false);
            this.mEmptyPaint.setColor(-1);
        }

        public String readBleSpenColorCode() {
            String mColorCode = "";
            try {
                Object spenGestureManager = SpenAccuracyTest.this.getSystemService("spengestureservice");
                mColorCode = (String) spenGestureManager.getClass().getMethod("getBleSpenCmfCode", null).invoke(spenGestureManager, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("mColorCode : ");
            sb.append(mColorCode);
            LtUtil.log_d(SpenAccuracyTest.this.CLASS_NAME, "readBleSpenColorCode", sb.toString());
            return mColorCode;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(this.mMatrixBitmap, 0.0f, 0.0f, null);
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getToolType(0) == 2) {
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
                    drawTeachingPoint();
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
                        drawTeachingPoint();
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
                        drawTeachingPoint();
                        this.isTouchDown = true;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void drawTeachingPoint() {
            float pxsize = TypedValue.applyDimension(5, 1.0f, getResources().getDisplayMetrics());
            Paint mCirclePaint = new Paint();
            mCirclePaint.setStrokeWidth(pxsize / 4.0f);
            mCirclePaint.setColor(-65536);
            mCirclePaint.setStyle(Style.STROKE);
            this.mMatrixCanvas.drawCircle((float) (this.mScreenWidth / 2), (float) (this.mScreenHeight / 2), pxsize, mCirclePaint);
        }

        private void drawLine(float preX, float preY, float x, float y) {
            int highX;
            int lowX;
            int highY;
            int lowY;
            this.mMatrixCanvas.drawLine(preX, preY, x, y, this.mLinePaint);
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
            this.mMatrixCanvas.drawPoint(x, y, this.mLinePaint);
            invalidate(new Rect(((int) x) - 6, ((int) y) - 6, ((int) x) + 6, ((int) y) + 6));
        }

        private void initRect() {
            float col_height2 = ((float) this.mScreenHeight) / ((float) SpenAccuracyTest.this.HEIGHT_BASIS);
            float col_width2 = ((float) this.mScreenWidth) / ((float) SpenAccuracyTest.this.WIDTH_BASIS);
            int ColY = 0;
            Paint mRectPaint = new Paint();
            mRectPaint.setColor(-16777216);
            Paint mRectPaintCross = new Paint();
            mRectPaint.setColor(-16777216);
            mRectPaintCross.setColor(-16777216);
            mRectPaintCross.setStyle(Style.STROKE);
            int ColX = 0;
            int i = 0;
            while (i < SpenAccuracyTest.this.HEIGHT_BASIS) {
                ColY = (int) (((float) i) * col_height2);
                int ColX2 = ColX;
                int ColX3 = 0;
                while (true) {
                    int j = ColX3;
                    if (j >= SpenAccuracyTest.this.WIDTH_BASIS) {
                        break;
                    }
                    int ColX4 = (int) (((float) j) * col_width2);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) ColX4, (float) this.mScreenHeight, paint);
                    SpenAccuracyTest.this.draw[i][j] = false;
                    ColX3 = j + 1;
                    ColX2 = ColX4;
                }
                i++;
                ColX = ColX2;
            }
            float StartX = ((float) Math.floor((double) col_width2)) + 1.0f;
            float StartY = 1.0f + ((float) Math.floor((double) col_height2));
            float EndX = (float) Math.floor((double) (((float) (SpenAccuracyTest.this.WIDTH_BASIS - 1)) * col_width2));
            float EndY = (float) Math.floor((double) (((float) (SpenAccuracyTest.this.HEIGHT_BASIS - 1)) * col_height2));
            float EndY2 = EndY;
            float f = EndX;
            this.mMatrixCanvas.drawRect(StartX, StartY, EndX, EndY2, this.mEmptyPaint);
            if (SpenAccuracyTest.this.isStyleX) {
                int ColY2 = ColY;
                int i2 = 0;
                while (i2 < SpenAccuracyTest.this.HEIGHT_BASIS_CROSS) {
                    int ColX5 = (int) (this.mWidthCross * ((float) (i2 + 2)));
                    int ColY3 = (int) ((this.mHeightCross * ((float) i2)) + col_height2);
                    int ColY4 = ColY3;
                    this.mMatrixCanvas.drawRect((float) ColX5, (float) ColY3, ((float) ColX5) + col_width2, ((float) ColY3) + (col_height2 / 2.0f), mRectPaintCross);
                    SpenAccuracyTest.this.drawCross[i2] = false;
                    i2++;
                    ColY2 = ColY4;
                }
                int i3 = 0;
                while (i3 < SpenAccuracyTest.this.HEIGHT_BASIS_CROSS) {
                    int ColX6 = (int) (this.mWidthCross * ((float) (i3 + 2)));
                    int ColY5 = (int) (((float) this.mScreenHeight) - ((this.mHeightCross * ((float) (i3 + 1))) + col_height2));
                    int ColY6 = ColY5;
                    this.mMatrixCanvas.drawRect((float) ColX6, (float) ColY5, ((float) ColX6) + col_width2, ((float) ColY5) + (col_height2 / 2.0f), mRectPaintCross);
                    SpenAccuracyTest.this.drawCross[SpenAccuracyTest.this.HEIGHT_BASIS_CROSS + i3] = false;
                    i3++;
                    ColY2 = ColY6;
                }
                int i4 = ColY2;
            }
        }

        private void drawRect(float x, float y, Paint paint) {
            float col_height2 = ((float) this.mScreenHeight) / ((float) SpenAccuracyTest.this.HEIGHT_BASIS);
            float col_width2 = ((float) this.mScreenWidth) / ((float) SpenAccuracyTest.this.WIDTH_BASIS);
            int countX = (int) (x / col_width2);
            int countY = (int) (y / col_height2);
            float ColX = col_width2 * ((float) countX);
            float ColY = col_height2 * ((float) countY);
            if (countY > SpenAccuracyTest.this.HEIGHT_BASIS - 1 || countX > SpenAccuracyTest.this.WIDTH_BASIS - 1 || countX < 0 || countY < 0) {
                Log.i(SpenAccuracyTest.TAG, "You are out of bounds!");
                return;
            }
            if (!SpenAccuracyTest.this.draw[countY][countX]) {
                SpenAccuracyTest.this.draw[countY][countX] = true;
                if (SpenAccuracyTest.this.draw[countY][countX] && SpenAccuracyTest.this.isDrawArea[countY][countX]) {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width2)), (float) ((int) (ColY + col_height2)), paint);
                    invalidate(new Rect((int) (ColX - 1.0f), (int) (ColY - 1.0f), (int) (ColX + col_width2 + 1.0f), (int) (ColY + col_height2 + 1.0f)));
                }
            }
            if (SpenAccuracyTest.this.isStyleX) {
                checkCrossRectRegion(x, y, countX, countY, paint);
            }
            checkPassNfinishSpenTest();
        }

        private void checkPassNfinishSpenTest() {
            if (SpenAccuracyTest.this.isStyleX) {
                if (SpenAccuracyTest.this.isPass() && SpenAccuracyTest.this.isPassCross() && SpenAccuracyTest.this.passFlag == 0) {
                    SpenAccuracyTest.this.passFlag = SpenAccuracyTest.this.passFlag + 1;
                    SpenAccuracyTest.this.finishEpentest();
                }
            } else if (SpenAccuracyTest.this.isPass() && SpenAccuracyTest.this.passFlag == 0) {
                SpenAccuracyTest.this.passFlag = SpenAccuracyTest.this.passFlag + 1;
                SpenAccuracyTest.this.finishEpentest();
            }
        }

        private void checkCrossRectRegion(float x, float y, int countX, int countY, Paint paint) {
            int i = countY;
            if (i > 0 && i < SpenAccuracyTest.this.HEIGHT_BASIS - 1) {
                int countCrossY = (int) ((y - this.col_height) / this.mHeightCross);
                int startRectTopX = (int) ((this.mWidthCross * ((float) (countCrossY + 2))) - 1.0f);
                int startRectBottomX = (int) ((this.mWidthCross * ((float) ((SpenAccuracyTest.this.HEIGHT_BASIS_CROSS - countCrossY) + 1))) - 1.0f);
                int startRectTopY = (int) (this.col_height + (this.mHeightCross * ((float) countCrossY)));
                int startRectBottomY = startRectTopY;
                if (x > ((float) startRectTopX) && x < ((float) startRectTopX) + this.mWidthBetween) {
                    if (!SpenAccuracyTest.this.drawCross[countCrossY]) {
                        this.mMatrixCanvas.drawRect((float) (startRectTopX + 1), (float) (startRectTopY + 1), (float) ((int) (((float) startRectTopX) + this.mWidthBetween)), (float) ((int) (((float) startRectTopY) + (this.col_height / 2.0f))), paint);
                        invalidate(new Rect(startRectTopX - 1, startRectTopY - 1, (int) (((float) startRectTopX) + this.mWidthBetween + 1.0f), (int) (((float) startRectTopY) + this.col_height + 1.0f)));
                    }
                    if (countCrossY >= 0) {
                        SpenAccuracyTest.this.drawCross[countCrossY] = true;
                    }
                }
                if (x > ((float) startRectBottomX) && x < ((float) startRectBottomX) + this.mWidthBetween) {
                    if (!SpenAccuracyTest.this.drawCross[((SpenAccuracyTest.this.HEIGHT_BASIS_CROSS * 2) - 1) - countCrossY]) {
                        this.mMatrixCanvas.drawRect((float) (startRectBottomX + 1), (float) (startRectBottomY + 1), (float) ((int) (((float) startRectBottomX) + this.mWidthBetween)), (float) ((int) (((float) startRectBottomY) + (this.col_height / 2.0f))), paint);
                        invalidate(new Rect(startRectBottomX - 1, startRectBottomY - 1, (int) (((float) startRectBottomX) + this.mWidthBetween + 1.0f), (int) (((float) startRectBottomY) + this.col_height + 1.0f)));
                    }
                    if (countCrossY >= 0) {
                        SpenAccuracyTest.this.drawCross[((SpenAccuracyTest.this.HEIGHT_BASIS_CROSS * 2) - 1) - countCrossY] = true;
                    }
                }
            }
        }
    }

    public SpenAccuracyTest() {
        super(TAG);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decideRemote();
        LtUtil.log_d(this.CLASS_NAME, "onCreate", "UISpenAccuracyTest is created");
        this.mModuleDevice = ModuleDevice.instance(this);
        this.mTSPHover = HwTestMenu.getEnabled(26);
        StringBuilder sb = new StringBuilder();
        sb.append("mTestCase = ");
        sb.append(this.mTestCase);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mTSPHover = ");
        sb2.append(this.mTSPHover);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb2.toString());
        this.isStyleX = true;
        setGridSizebyModel();
        initGridSettings();
        setContentView(new MyView(this));
        getWindow().addFlags(128);
        fillUpMatrix();
    }

    private void setGridSizebyModel() {
        float pxsize = TypedValue.applyDimension(5, (float) this.SIZE_RECT, getResources().getDisplayMetrics());
        this.WIDTH_BASIS = Spec.getInt(Spec.SPEN_NODE_COUNT_WIDTH);
        if (this.WIDTH_BASIS < 0) {
            this.WIDTH_BASIS = ((int) (((float) getWindowManager().getDefaultDisplay().getWidth()) / pxsize)) + 1;
        }
        this.HEIGHT_BASIS = Spec.getInt(Spec.SPEN_NODE_COUNT_HEIGHT);
        if (this.HEIGHT_BASIS < 0) {
            this.HEIGHT_BASIS = ((int) (((float) getWindowManager().getDefaultDisplay().getHeight()) / pxsize)) + 1;
        }
    }

    private void initGridSettings() {
        this.HEIGHT_BASIS_CROSS = (this.HEIGHT_BASIS - 2) * 2;
        this.WIDTH_BASIS_CROSS = this.HEIGHT_BASIS_CROSS;
        this.draw = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.isDrawArea = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.drawCross = new boolean[(this.HEIGHT_BASIS_CROSS * 2)];
        this.mTopmostOfMatrix = 0;
        this.mBottommostOfMatrix = this.HEIGHT_BASIS - 1;
        this.mLeftmostOfMatrix = 0;
        this.mRightmostOfMatrix = this.WIDTH_BASIS - 1;
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
        return row == this.mTopmostOfMatrix || row == this.mBottommostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix;
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

    /* access modifiers changed from: private */
    public boolean isPassCross() {
        boolean isPassCross = true;
        for (int i = 0; i < this.HEIGHT_BASIS_CROSS * 2; i++) {
            isPassCross = isPassCross && this.drawCross[i];
        }
        return isPassCross;
    }

    /* access modifiers changed from: private */
    public void finishEpentest() {
        if (isOqcsbftt) {
            NVAccessor.setNV(402, NVAccessor.NV_VALUE_PASS);
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SpenAccuracyTest.this.finish();
            }
        }, 200);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 24) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.isStyleX) {
            if (!isPass() || !isPassCross()) {
                setResult(0);
            }
        } else if (!isPass()) {
            setResult(0);
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if ("true".equals(this.mTSPHover)) {
            LtUtil.log_d(this.CLASS_NAME, "onDestroy", "EnableHovering");
            this.mModuleDevice.hoveringOnOFF(EgisFingerprint.MAJOR_VERSION);
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LtUtil.setRemoveSystemUI(getWindow(), true);
        if ("true".equals(this.mTSPHover)) {
            LtUtil.log_d(this.CLASS_NAME, "onCreate", "DisableHovering");
            this.mModuleDevice.hoveringOnOFF("0");
        }
        if (isOqcsbftt) {
            NVAccessor.setNV(402, NVAccessor.NV_VALUE_ENTER);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        finish();
    }
}
