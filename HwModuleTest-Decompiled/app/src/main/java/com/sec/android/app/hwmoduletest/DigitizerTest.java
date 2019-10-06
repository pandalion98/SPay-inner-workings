package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.goodix.cap.fingerprint.Constants;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Spec;
import java.lang.reflect.Array;

public class DigitizerTest extends BaseActivity {
    private static final int AMETA_PEN_ON = 33554432;
    private static final int PLAY_1000_HZ = 101;
    private static final int PLAY_300_HZ = 100;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS;
    private int SIZE_RECT = 4;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS;
    /* access modifiers changed from: private */
    public boolean[][] draw;
    /* access modifiers changed from: private */
    public boolean[][] isDrawArea;
    private int mBottommostOfMatrix = 0;
    private int mCenterOfHorizontalOfMatrix = 0;
    private int mCenterOfVerticalOfMatrix = 0;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private boolean mIsReceiverPlaying = false;
    /* access modifiers changed from: private */
    public boolean mIsWacom = true;
    private int mLeftmostOfMatrix = 0;
    private MediaPlayer mRcvMediaPlayer;
    private int mRightmostOfMatrix = 0;
    private int mTopmostOfMatrix = 0;
    private View mView;
    private AudioManager mVolume;
    /* access modifiers changed from: private */
    public int passFlag = 0;
    /* access modifiers changed from: private */
    public boolean remoteCall = false;

    public class MyView extends View {
        private boolean isTouchDown;
        private Paint mClickPaint;
        private boolean mDefectFlag = true;
        private Paint mEmptyPaint;
        private Paint mLinePaint;
        private Bitmap mMatrixBitmap;
        private Canvas mMatrixCanvas;
        private Paint mNonClickPaint;
        private float mPreTouchedX = 0.0f;
        private float mPreTouchedY = 0.0f;
        private int mScreenHeight;
        private int mScreenWidth;
        private Paint mTextPaint;
        private float mTouchedX = 0.0f;
        private float mTouchedY = 0.0f;

        public MyView(Context context) {
            super(context);
            Display mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(outpoint);
            this.mScreenWidth = outpoint.x;
            this.mScreenHeight = outpoint.y;
            this.mMatrixBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.RGB_565), this.mScreenWidth, this.mScreenHeight, false);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
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
            this.mLinePaint.setStrokeWidth(2.0f);
            this.mLinePaint.setColor(-16777216);
            this.mClickPaint = new Paint();
            this.mClickPaint.setAntiAlias(false);
            this.mClickPaint.setColor(-16711936);
            this.mNonClickPaint = new Paint();
            this.mNonClickPaint.setAntiAlias(false);
            this.mNonClickPaint.setColor(-1);
            this.mEmptyPaint = new Paint();
            this.mEmptyPaint.setAntiAlias(false);
            this.mEmptyPaint.setColor(-256);
            this.mTextPaint = new Paint();
            this.mTextPaint.setAntiAlias(false);
            this.mTextPaint.setColor(-65536);
            this.mTextPaint.setTextSize(100.0f);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(this.mMatrixBitmap, 0.0f, 0.0f, null);
        }

        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == 4) {
                return super.onKeyDown(keyCode, event);
            }
            switch (keyCode) {
                case 24:
                    DigitizerTest.this.finish();
                    break;
                case Constants.CMD_TEST_CANCEL /*25*/:
                    initializeView();
                    break;
            }
            return true;
        }

        /* access modifiers changed from: private */
        public void initializeView() {
            this.isTouchDown = false;
            DigitizerTest.this.passFlag = 0;
            DigitizerTest.this.receiverStop();
            this.mMatrixCanvas.drawRect(0.0f, 0.0f, (float) this.mScreenWidth, (float) this.mScreenHeight, this.mNonClickPaint);
            initRect();
            invalidate();
        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            StringBuilder sb = new StringBuilder();
            sb.append("action: ");
            sb.append(action);
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "onTouchEvent", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("event.getButtonState(): ");
            sb2.append(event.getButtonState());
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "onTouchEvent", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("event.getToolType(): ");
            sb3.append(event.getToolType(0));
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "onTouchEvent", sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("isTouchDown: ");
            sb4.append(this.isTouchDown);
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "onTouchEvent", sb4.toString());
            if (DigitizerTest.this.mIsWacom) {
                if (event.getToolType(0) == 2) {
                    drawByEvent(event);
                } else if (event.getToolType(0) != 2) {
                    checkDefect(event);
                }
            }
            return true;
        }

        private void checkDefect(MotionEvent event) {
            int action = event.getAction();
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", "+++++++++++++++++++++++++++++++++++++++++++++++");
            StringBuilder sb = new StringBuilder();
            sb.append("action: ");
            sb.append(action);
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", sb.toString());
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", "MotionEvent.ACTION_DOWN 0");
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", "MotionEvent.ACTION_MOVE 2");
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", "MotionEvent.ACTION_UP 1");
            LtUtil.log_d(DigitizerTest.this.CLASS_NAME, "checkDefect", "+++++++++++++++++++++++++++++++++++++++++++++++");
            switch (action) {
                case 0:
                    this.mDefectFlag = false;
                    break;
                case 1:
                    if (!this.mDefectFlag) {
                        this.mDefectFlag = true;
                        return;
                    } else {
                        finishTestFail();
                        return;
                    }
            }
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
                case 3:
                    LtUtil.log_i(DigitizerTest.this.CLASS_NAME, "drawByEvent", "ACTION_UP!!");
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
                    if (!DigitizerTest.this.isPass()) {
                        finishTestFail();
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
            float col_height = ((float) this.mScreenHeight) / ((float) DigitizerTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) DigitizerTest.this.WIDTH_BASIS);
            Paint mRectPaint = new Paint();
            mRectPaint.setColor(-16777216);
            for (int i = 0; i < DigitizerTest.this.HEIGHT_BASIS; i++) {
                for (int j = 0; j < DigitizerTest.this.WIDTH_BASIS; j++) {
                    if (DigitizerTest.this.isDrawArea[i][j]) {
                        this.mMatrixCanvas.drawRect(col_width * ((float) j), col_height * ((float) i), col_width * ((float) (j + 1)), col_height * ((float) (i + 1)), this.mEmptyPaint);
                    }
                }
            }
            int count = 0;
            int count2 = 0;
            int i2 = 0;
            while (i2 < DigitizerTest.this.HEIGHT_BASIS) {
                int ColY = (int) (((float) i2) * col_height);
                int ColX = count2;
                int j2 = 0;
                while (j2 < DigitizerTest.this.WIDTH_BASIS) {
                    int ColX2 = (int) (((float) j2) * col_width);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX2, (float) ColY, (float) this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX2, (float) ColY, (float) ColX2, (float) this.mScreenHeight, paint);
                    this.mMatrixCanvas.drawLine(((float) count) * col_width, ((float) count) * col_height, ((float) (count + 1)) * col_width, ((float) count) * col_height, paint);
                    this.mMatrixCanvas.drawLine(((float) count) * col_width, ((float) count) * col_height, ((float) count) * col_width, ((float) (count + 1)) * col_height, paint);
                    this.mMatrixCanvas.drawLine(((float) count) * col_width, ((float) (count + 1)) * col_height, ((float) (count + 1)) * col_width, ((float) (count + 1)) * col_height, paint);
                    this.mMatrixCanvas.drawLine(((float) (count + 1)) * col_width, ((float) count) * col_height, ((float) (count + 1)) * col_width, ((float) (count + 1)) * col_height, paint);
                    DigitizerTest.this.draw[i2][j2] = false;
                    j2++;
                    ColX = ColX2;
                }
                count++;
                i2++;
                count2 = ColX;
            }
        }

        private void finishTestPass() {
            DigitizerTest.this.receiverStart(101);
            this.mTextPaint.setColor(-16776961);
            this.mMatrixCanvas.drawText("PASS", ((float) this.mScreenWidth) / 2.0f, 300.0f, this.mTextPaint);
            invalidate();
            DigitizerTest.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    DigitizerTest.this.receiverStop();
                    MyView.this.initializeView();
                }
            }, 2000);
        }

        private void finishTestFail() {
            if (DigitizerTest.this.passFlag == 0) {
                DigitizerTest.this.passFlag = 1;
                DigitizerTest.this.receiverStart(100);
                this.mTextPaint.setColor(-65536);
                this.mMatrixCanvas.drawText("FAIL", ((float) this.mScreenWidth) / 2.0f, 300.0f, this.mTextPaint);
                invalidate();
                DigitizerTest.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        DigitizerTest.this.receiverStop();
                    }
                }, 2000);
            }
        }

        private boolean checkPreviousRect(float x, float y) {
            int countX = (int) (x / (((float) this.mScreenWidth) / ((float) DigitizerTest.this.WIDTH_BASIS)));
            int countY = (int) (y / (((float) this.mScreenHeight) / ((float) DigitizerTest.this.HEIGHT_BASIS)));
            if (countY <= DigitizerTest.this.HEIGHT_BASIS - 1 && countX <= DigitizerTest.this.WIDTH_BASIS - 1) {
                return countY + -1 < 0 || countX + -1 < 0 || !DigitizerTest.this.isDrawArea[countY + -1][countX + -1] || DigitizerTest.this.draw[countY + -1][countX + -1];
            }
            LtUtil.log_e(DigitizerTest.this.CLASS_NAME, "checkPreviousRect", "You are out of bounds!");
            return false;
        }

        private void drawRect(float x, float y, Paint paint) {
            float col_height = ((float) this.mScreenHeight) / ((float) DigitizerTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) DigitizerTest.this.WIDTH_BASIS);
            int countX = (int) (x / col_width);
            int countY = (int) (y / col_height);
            float ColX = ((float) countX) * col_width;
            float ColY = ((float) countY) * col_height;
            if (countY > DigitizerTest.this.HEIGHT_BASIS - 1 || countX > DigitizerTest.this.WIDTH_BASIS - 1) {
                LtUtil.log_e(DigitizerTest.this.CLASS_NAME, "drawRect", "You are out of bounds!");
                return;
            }
            if (!DigitizerTest.this.draw[countY][countX]) {
                DigitizerTest.this.draw[countY][countX] = true;
                if (!DigitizerTest.this.draw[countY][countX] || !DigitizerTest.this.isDrawArea[countY][countX]) {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), this.mNonClickPaint);
                } else {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), paint);
                }
                invalidate(new Rect((int) (ColX - 1.0f), (int) (ColY - 1.0f), (int) (ColX + col_width + 1.0f), (int) (ColY + col_height + 1.0f)));
            }
            if (DigitizerTest.this.isPass() && DigitizerTest.this.passFlag == 0) {
                DigitizerTest.this.passFlag = DigitizerTest.this.passFlag + 1;
                if (DigitizerTest.this.remoteCall) {
                    DigitizerTest.this.setResult(-1);
                }
                finishTestPass();
            }
        }
    }

    public DigitizerTest() {
        super("DigitizerTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", "TouchTest is created");
        LtUtil.setRemoveSystemUI(getWindow(), true);
        decideRemote();
        setGridSizebyModel();
        initGridSettings();
        fillUpMatrix();
        this.mView = new MyView(this);
        this.mView.setKeepScreenOn(true);
        setContentView(this.mView);
        this.mView.setFocusableInTouchMode(true);
        getWindow().addFlags(128);
        this.mVolume = (AudioManager) getSystemService("audio");
    }

    private void setGridSizebyModel() {
        this.HEIGHT_BASIS = Spec.getInt(Spec.DIGITIZER_HEIGHT_BASIS);
        this.WIDTH_BASIS = Spec.getInt(Spec.DIGITIZER_WIDTH_BASIS);
        if (this.HEIGHT_BASIS == 0 || this.WIDTH_BASIS == 0) {
            int nScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int nScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
            float pxsize = TypedValue.applyDimension(5, (float) this.SIZE_RECT, getResources().getDisplayMetrics());
            this.WIDTH_BASIS = ((int) (((float) nScreenWidth) / pxsize)) + 1;
            this.HEIGHT_BASIS = ((int) (((float) nScreenHeight) / pxsize)) + 1;
            return;
        }
        this.HEIGHT_BASIS = Spec.getInt(Spec.DIGITIZER_HEIGHT_BASIS);
        this.WIDTH_BASIS = Spec.getInt(Spec.DIGITIZER_WIDTH_BASIS);
    }

    private void initGridSettings() {
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
                this.isDrawArea[row][column] = false;
            }
        }
        if (this.HEIGHT_BASIS == 52) {
            this.isDrawArea[2][0] = true;
            this.isDrawArea[3][0] = true;
            this.isDrawArea[4][1] = true;
            this.isDrawArea[5][1] = true;
            this.isDrawArea[5][2] = true;
            this.isDrawArea[6][2] = true;
            this.isDrawArea[7][3] = true;
            this.isDrawArea[8][3] = true;
            this.isDrawArea[8][4] = true;
            this.isDrawArea[9][4] = true;
            this.isDrawArea[10][5] = true;
            this.isDrawArea[11][6] = true;
            this.isDrawArea[12][6] = true;
            this.isDrawArea[13][7] = true;
            this.isDrawArea[14][7] = true;
            this.isDrawArea[14][8] = true;
            this.isDrawArea[15][8] = true;
            this.isDrawArea[16][9] = true;
            this.isDrawArea[17][9] = true;
            this.isDrawArea[17][10] = true;
            this.isDrawArea[18][10] = true;
            this.isDrawArea[19][11] = true;
            this.isDrawArea[20][11] = true;
            this.isDrawArea[20][12] = true;
            this.isDrawArea[21][12] = true;
            this.isDrawArea[22][13] = true;
            this.isDrawArea[23][13] = true;
            this.isDrawArea[23][14] = true;
            this.isDrawArea[24][14] = true;
            this.isDrawArea[24][14] = true;
            this.isDrawArea[25][14] = true;
            this.isDrawArea[25][15] = true;
            this.isDrawArea[26][15] = true;
            this.isDrawArea[27][16] = true;
            this.isDrawArea[28][16] = true;
            this.isDrawArea[29][17] = true;
            this.isDrawArea[30][18] = true;
            this.isDrawArea[31][18] = true;
            this.isDrawArea[32][19] = true;
            this.isDrawArea[33][19] = true;
            this.isDrawArea[33][20] = true;
            this.isDrawArea[34][20] = true;
            this.isDrawArea[35][21] = true;
            this.isDrawArea[36][21] = true;
            this.isDrawArea[36][22] = true;
            this.isDrawArea[37][22] = true;
            this.isDrawArea[38][23] = true;
            this.isDrawArea[39][23] = true;
            this.isDrawArea[39][24] = true;
            this.isDrawArea[40][24] = true;
            this.isDrawArea[40][25] = true;
            this.isDrawArea[41][25] = true;
            this.isDrawArea[42][26] = true;
            this.isDrawArea[43][26] = true;
            this.isDrawArea[44][27] = true;
            this.isDrawArea[45][27] = true;
            this.isDrawArea[46][28] = true;
            this.isDrawArea[47][28] = true;
            this.isDrawArea[47][29] = true;
            this.isDrawArea[48][29] = true;
            this.isDrawArea[49][30] = true;
            this.isDrawArea[50][31] = true;
            this.isDrawArea[51][31] = true;
            return;
        }
        float rate = ((float) this.WIDTH_BASIS) / (((float) this.HEIGHT_BASIS) - 2.0f);
        for (int row2 = 1; row2 < this.HEIGHT_BASIS - 1; row2++) {
            int column2 = (int) (((float) (row2 - 1)) * rate);
            int nextcolumn = (int) (((float) row2) * rate);
            this.isDrawArea[row2][column2] = true;
            if (column2 != nextcolumn) {
                this.isDrawArea[row2 - 1][column2] = true;
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

    /* access modifiers changed from: private */
    public void receiverStart(int hertz) {
        if (!this.mIsReceiverPlaying) {
            this.mIsReceiverPlaying = true;
            this.mVolume.setStreamVolume(3, this.mVolume.getStreamMaxVolume(3), 0);
            if (hertz == 100) {
                this.mRcvMediaPlayer = MediaPlayer.create(this, C0268R.raw.lf_300hz);
            } else {
                this.mRcvMediaPlayer = MediaPlayer.create(this, C0268R.raw.mp3_1khz);
            }
            this.mRcvMediaPlayer.setLooping(true);
            try {
                Thread.sleep(130);
            } catch (InterruptedException e) {
            }
            this.mRcvMediaPlayer.start();
        }
    }

    /* access modifiers changed from: private */
    public void receiverStop() {
        if (this.mIsReceiverPlaying) {
            this.mRcvMediaPlayer.stop();
            this.mRcvMediaPlayer.release();
            this.mIsReceiverPlaying = false;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
