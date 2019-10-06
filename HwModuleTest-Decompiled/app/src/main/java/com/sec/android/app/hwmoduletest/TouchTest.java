package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Spec;
import java.lang.reflect.Array;

public class TouchTest extends BaseActivity {
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS = 19;
    protected int KEY_TIMEOUT;
    protected int KEY_TIMER_EXPIRED;
    protected int MILLIS_IN_SEC;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS = 11;
    /* access modifiers changed from: private */
    public boolean[][] click = ((boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS}));
    /* access modifiers changed from: private */
    public boolean[][] draw = ((boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS}));
    /* access modifiers changed from: private */
    public boolean[][] isDrawArea = ((boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS}));
    /* access modifiers changed from: private */
    public boolean isHovering = false;
    /* access modifiers changed from: private */
    public boolean iscompleted = false;
    private int mBottommostOfMatrix = (this.HEIGHT_BASIS - 1);
    private int mCenterOfHorizontalOfMatrix = (this.WIDTH_BASIS / 2);
    private int mCenterOfVerticalOfMatrix = (this.HEIGHT_BASIS / 2);
    private int mLeftmostOfMatrix = 0;
    private int mRightmostOfMatrix = (this.WIDTH_BASIS - 1);
    private int mTopmostOfMatrix = 0;

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
            Display mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(outpoint);
            this.mScreenWidth = outpoint.x;
            this.mScreenHeight = outpoint.y;
            StringBuilder sb = new StringBuilder();
            sb.append("Screen size: ");
            sb.append(this.mScreenWidth);
            sb.append(" x ");
            sb.append(this.mScreenHeight);
            LtUtil.log_d(TouchTest.this.CLASS_NAME, "MyView", sb.toString());
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
            this.mLinePaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f}, 1.0f));
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

        public boolean onHoverEvent(MotionEvent event) {
            int action = event.getAction();
            if (TouchTest.this.isHovering) {
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
            if (event.getToolType(0) != 2 && !TouchTest.this.isHovering) {
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
            float col_height = ((float) this.mScreenHeight) / ((float) TouchTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) TouchTest.this.WIDTH_BASIS);
            Paint mRectPaint = new Paint();
            mRectPaint.setColor(-16777216);
            int ColX = 0;
            int i = 0;
            while (i < TouchTest.this.HEIGHT_BASIS) {
                int ColY = (int) (((float) i) * col_height);
                int ColX2 = ColX;
                int ColX3 = 0;
                while (true) {
                    int j = ColX3;
                    if (j >= TouchTest.this.WIDTH_BASIS) {
                        break;
                    }
                    int ColX4 = (int) (((float) j) * col_width);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) ColX4, (float) this.mScreenHeight, paint);
                    TouchTest.this.draw[i][j] = false;
                    TouchTest.this.click[i][j] = false;
                    ColX3 = j + 1;
                    ColX2 = ColX4;
                }
                i++;
                ColX = ColX2;
            }
            if (TouchTest.this.isHovering) {
                this.mMatrixCanvas.drawRect(0.0f, 0.0f, col_width - 1.0f, (float) this.mScreenHeight, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect(((float) this.mScreenWidth) - col_width, 0.0f, (float) this.mScreenWidth, (float) this.mScreenHeight, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect((col_width * 2.0f) + 1.0f, col_height + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect((((float) ((TouchTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, col_height + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS - 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect((2.0f * col_width) + 1.0f, (((float) ((TouchTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect((((float) ((TouchTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, (((float) ((TouchTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS - 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
                return;
            }
            this.mMatrixCanvas.drawRect(col_width + 1.0f, col_height + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect((((float) ((TouchTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, col_height + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS - 1)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS / 2)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect(col_width + 1.0f, (((float) ((TouchTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS / 2)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
            this.mMatrixCanvas.drawRect((((float) ((TouchTest.this.WIDTH_BASIS / 2) + 1)) * col_width) + 1.0f, (((float) ((TouchTest.this.HEIGHT_BASIS / 2) + 1)) * col_height) + 1.0f, (((float) (TouchTest.this.WIDTH_BASIS - 1)) * col_width) - 1.0f, (((float) (TouchTest.this.HEIGHT_BASIS - 1)) * col_height) - 1.0f, this.mEmptyPaint);
        }

        private void drawRect(float x, float y, Paint paint) {
            float col_height = ((float) this.mScreenHeight) / ((float) TouchTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) TouchTest.this.WIDTH_BASIS);
            int countX = (int) (x / col_width);
            int countY = (int) (y / col_height);
            float ColX = ((float) countX) * col_width;
            float ColY = ((float) countY) * col_height;
            if (countY > TouchTest.this.HEIGHT_BASIS - 1 || countX > TouchTest.this.WIDTH_BASIS - 1 || countX < 0 || countY < 0) {
                LtUtil.log_e(TouchTest.this.CLASS_NAME, "drawRect", "Out of bounds");
                return;
            }
            if (!TouchTest.this.draw[countY][countX]) {
                TouchTest.this.draw[countY][countX] = true;
                if (!TouchTest.this.draw[countY][countX] || !TouchTest.this.isDrawArea[countY][countX]) {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), this.mNonClickPaint);
                } else {
                    this.mMatrixCanvas.drawRect((float) (((int) ColX) + 1), (float) (((int) ColY) + 1), (float) ((int) (ColX + col_width)), (float) ((int) (ColY + col_height)), paint);
                }
                invalidate(new Rect((int) (ColX - 1.0f), (int) (ColY - 1.0f), (int) (ColX + col_width + 1.0f), (int) (ColY + col_height + 1.0f)));
            }
            if (TouchTest.this.isPass() && !TouchTest.this.iscompleted) {
                TouchTest.this.iscompleted = true;
                startActivityForResult(new Intent(TouchTest.this, TouchTestPass.class), 0);
            }
        }
    }

    public TouchTest() {
        super("TouchTest");
    }

    private boolean getNodeNum() {
        String testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH);
        if (!testcase.contains("NODE")) {
            return false;
        }
        String testcase2 = testcase.substring(testcase.indexOf("NODE(") + "NODE(".length());
        String[] nodeStr = testcase2.substring(0, testcase2.indexOf(")")).trim().split(",");
        this.WIDTH_BASIS = Integer.parseInt(nodeStr[0]);
        this.HEIGHT_BASIS = Integer.parseInt(nodeStr[1]);
        StringBuilder sb = new StringBuilder();
        sb.append("node(X,Y) -> ");
        sb.append(this.WIDTH_BASIS);
        sb.append(",");
        sb.append(this.HEIGHT_BASIS);
        LtUtil.log_d(this.CLASS_NAME, "getNodeNum", sb.toString());
        return true;
    }

    private void setTSP() {
        if (!getNodeNum()) {
            this.WIDTH_BASIS = Spec.getInt(Spec.TSP_X_AXIS_CHANNEL);
            this.HEIGHT_BASIS = Spec.getInt(Spec.TSP_Y_AXIS_CHANNEL);
        }
        this.click = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.draw = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.isDrawArea = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
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
        LtUtil.setRemoveSystemUI(getWindow(), true);
        this.isHovering = getIntent().getExtras().getBoolean("isHovering");
        setTSP();
        setContentView(new MyView(this));
        fillUpMatrix();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        LtUtil.log_d(this.CLASS_NAME, "onStart", "");
        super.onStart();
        ModuleDevice.instance(this).startTSPTest(ModuleDevice.TSP_CMD_DISABLE_DEAD_ZONE);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        try {
            if (!getIntent().getBooleanExtra("TEST_TSP_SELF", true)) {
                startActivityForResult(new Intent(this, TspFailPop.class), 0);
            }
        } catch (Exception e) {
            LtUtil.log_d(this.CLASS_NAME, "onResume", "Exception");
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LtUtil.log_d(this.CLASS_NAME, "onStop", "");
        super.onStop();
        ModuleDevice.instance(this).startTSPTest(ModuleDevice.TSP_CMD_ENABLE_DEAD_ZONE);
    }

    private void fillUpMatrix() {
        for (int row = 0; row < this.HEIGHT_BASIS; row++) {
            for (int column = 0; column < this.WIDTH_BASIS; column++) {
                if (this.isHovering && isNeededCheck_Hovering(row, column)) {
                    this.isDrawArea[row][column] = true;
                } else if (this.isHovering || !isNeededCheck(row, column)) {
                    this.isDrawArea[row][column] = false;
                } else {
                    this.isDrawArea[row][column] = true;
                }
            }
        }
    }

    private boolean isNeededCheck(int row, int column) {
        return row == this.mTopmostOfMatrix || row == this.mBottommostOfMatrix || row == this.mCenterOfVerticalOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix || column == this.mCenterOfHorizontalOfMatrix;
    }

    private boolean isNeededCheck_Hovering(int row, int column) {
        if (row == this.mTopmostOfMatrix && column != this.mLeftmostOfMatrix && column != this.mRightmostOfMatrix) {
            return true;
        }
        if ((row == this.mBottommostOfMatrix && column != this.mLeftmostOfMatrix && column != this.mRightmostOfMatrix) || column == this.mLeftmostOfMatrix + 1 || column == this.mRightmostOfMatrix - 1) {
            return true;
        }
        return !(row != this.mCenterOfVerticalOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix) || column == this.mCenterOfHorizontalOfMatrix;
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
        if (keyCode == 4) {
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode) {
            case 24:
                finish();
                break;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        finish();
    }

    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
