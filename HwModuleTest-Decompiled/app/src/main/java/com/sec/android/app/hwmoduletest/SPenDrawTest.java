package com.sec.android.app.hwmoduletest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.InvalidDisplayException;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class SPenDrawTest extends BaseActivity implements OnHoverListener {
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS = 28;
    /* access modifiers changed from: private */
    public int SIZE_RECT = 4;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS = 16;
    private boolean isHoveringTest = true;
    public int mCocktatilBarSize = 0;
    private ComponentName mComponentName;
    public Point mDisplayRect = new Point();
    public int mIsCurrentScreenState = 0;
    private boolean mIsEnabledPointerLocation = false;
    public boolean mIsLandscape;
    private View mTouchView;

    public class TouchView extends View implements OnHoverListener {
        private boolean isHover;
        private boolean isPenDetect;
        private boolean isTouchDown;
        private Paint mEmptyPaint;
        private float mHoverCircleSize;
        private Paint mHoverPaint;
        private float mHoveredX = 0.0f;
        private float mHoveredY = 0.0f;
        private Paint mInfoBackgroundPaint;
        private Bitmap mInfoBitmap;
        private Canvas mInfoCanvas;
        private Paint mInfoPaint;
        private Paint mLevelPaint;
        private Paint mLinePaint;
        private Bitmap mMatrixBitmap;
        private Canvas mMatrixCanvas;
        private float mPreHoveredX = 0.0f;
        private float mPreHoveredY = 0.0f;
        private float mPreTouchedX = 0.0f;
        private float mPreTouchedY = 0.0f;
        private int mScreenHeight;
        private int mScreenWidth;
        private float mTouchedX = 0.0f;
        private float mTouchedY = 0.0f;

        public TouchView(Context context) {
            super(context);
            setKeepScreenOn(true);
            Display mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(SPenDrawTest.this.mDisplayRect);
            mDisplay.getRealSize(outpoint);
            if (mDisplay.getRotation() == 0 || mDisplay.getRotation() == 2) {
                SPenDrawTest.this.mIsLandscape = false;
            } else {
                SPenDrawTest.this.mIsLandscape = true;
            }
            SPenDrawTest.this.mIsCurrentScreenState = mDisplay.getRotation();
            this.mScreenWidth = outpoint.x;
            this.mScreenHeight = outpoint.y;
            StringBuilder sb = new StringBuilder();
            sb.append("Screen size: ");
            sb.append(this.mScreenWidth);
            sb.append(" x ");
            sb.append(this.mScreenHeight);
            LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "TouchView", sb.toString());
            Resources r = getResources();
            float pxsize = TypedValue.applyDimension(5, (float) SPenDrawTest.this.SIZE_RECT, r.getDisplayMetrics());
            SPenDrawTest.this.WIDTH_BASIS = ((int) (((float) this.mScreenWidth) / pxsize)) + 1;
            SPenDrawTest.this.HEIGHT_BASIS = ((int) (((float) this.mScreenHeight) / pxsize)) + 1;
            this.mHoverCircleSize = TypedValue.applyDimension(5, 1.0f, r.getDisplayMetrics()) / 2.0f;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("channel number: ");
            sb2.append(SPenDrawTest.this.WIDTH_BASIS);
            sb2.append(" x ");
            sb2.append(SPenDrawTest.this.HEIGHT_BASIS);
            LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "TouchView", sb2.toString());
            this.mMatrixBitmap = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.RGB_565), this.mScreenWidth, this.mScreenHeight, false);
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
            setPaint();
            initRect();
            drawInfo(0.0f, 0.0f, 0.0f);
            this.isTouchDown = false;
            this.isPenDetect = false;
            this.isHover = false;
        }

        private void setPaint() {
            this.mLinePaint = new Paint();
            this.mLinePaint.setAntiAlias(true);
            this.mLinePaint.setDither(true);
            this.mLinePaint.setColor(-16776961);
            this.mLinePaint.setStyle(Style.STROKE);
            this.mLinePaint.setStrokeJoin(Join.ROUND);
            this.mLinePaint.setStrokeCap(Cap.SQUARE);
            this.mLinePaint.setStrokeWidth(3.0f);
            this.mLinePaint.setColor(-16776961);
            this.mHoverPaint = new Paint();
            this.mHoverPaint.setAntiAlias(true);
            this.mHoverPaint.setDither(true);
            this.mHoverPaint.setColor(-16711936);
            this.mHoverPaint.setStyle(Style.FILL);
            this.mHoverPaint.setStrokeWidth(1.0f);
            this.mHoverPaint.setColor(-16711936);
            this.mEmptyPaint = new Paint();
            this.mEmptyPaint.setAntiAlias(false);
            this.mEmptyPaint.setColor(-1);
            this.mInfoPaint = new Paint();
            this.mInfoPaint.setAntiAlias(true);
            this.mInfoPaint.setStyle(Style.FILL);
            this.mInfoPaint.setColor(-16777216);
            this.mInfoBackgroundPaint = new Paint();
            this.mInfoBackgroundPaint.setAntiAlias(false);
            this.mInfoBackgroundPaint.setARGB(192, 255, 255, 255);
            this.mLevelPaint = new Paint();
            this.mLevelPaint.setAntiAlias(false);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.drawBitmap(this.mMatrixBitmap, 0.0f, 0.0f, null);
            canvas.drawBitmap(this.mInfoBitmap, 0.0f, 0.0f, null);
            if (this.isHover && SPenDrawTest.this.isHoveringEnbale()) {
                canvas.drawCircle(this.mHoveredX, this.mHoveredY, this.mHoverCircleSize, this.mHoverPaint);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            if (event.getToolType(0) == 2) {
                switch (action) {
                    case 0:
                        this.mTouchedX = event.getX();
                        this.mTouchedY = event.getY();
                        this.isTouchDown = true;
                        this.isPenDetect = true;
                        break;
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
                            this.isPenDetect = false;
                            break;
                        }
                        break;
                    case 2:
                        if (this.isTouchDown) {
                            for (int i = 0; i < event.getHistorySize(); i++) {
                                this.mPreTouchedX = this.mTouchedX;
                                this.mPreTouchedY = this.mTouchedY;
                                this.mTouchedX = event.getHistoricalX(i);
                                this.mTouchedY = event.getHistoricalY(i);
                                drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, true);
                            }
                            this.mPreTouchedX = this.mTouchedX;
                            this.mPreTouchedY = this.mTouchedY;
                            this.mTouchedX = event.getX();
                            this.mTouchedY = event.getY();
                            drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY, true);
                            this.isTouchDown = true;
                            this.isPenDetect = true;
                            break;
                        }
                        break;
                }
                drawInfo(this.mTouchedX, this.mTouchedY, event.getPressure());
            } else {
                drawInfo(0.0f, 0.0f, 0.0f);
            }
            return true;
        }

        public boolean onHover(View v, MotionEvent event) {
            int action = event.getAction();
            if (action != 7) {
                switch (action) {
                    case 9:
                        LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", "ACTION_HOVER_ENTER ");
                        this.mHoveredX = event.getX();
                        this.mHoveredY = event.getY();
                        LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", "ACTION_HOVER_ENTER test start!");
                        this.isPenDetect = true;
                        this.isHover = true;
                        break;
                    case 10:
                        LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", "ACTION_HOVER_EXIT");
                        this.isPenDetect = false;
                        this.isHover = false;
                        StringBuilder sb = new StringBuilder();
                        sb.append("PREX,Y:");
                        sb.append(this.mPreHoveredX);
                        sb.append(",");
                        sb.append(this.mPreHoveredY);
                        LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", sb.toString());
                        invalidate(new Rect(((int) this.mHoveredX) - 7, ((int) this.mHoveredY) - 7, ((int) this.mHoveredX) + 7, ((int) this.mHoveredY) + 7));
                        this.mHoveredX = event.getX();
                        this.mHoveredY = event.getY();
                        break;
                }
            } else {
                LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", "ACTION_HOVER_MOVE");
                for (int i = 0; i < event.getHistorySize(); i++) {
                    this.mPreHoveredX = this.mHoveredX;
                    this.mPreHoveredY = this.mHoveredY;
                    this.mTouchedX = event.getHistoricalX(i);
                    this.mTouchedY = event.getHistoricalY(i);
                    if (SPenDrawTest.this.isHoveringEnbale()) {
                        drawLine(this.mPreHoveredX, this.mPreHoveredY, this.mHoveredX, this.mHoveredY, false);
                    }
                }
                this.mPreHoveredX = this.mHoveredX;
                this.mPreHoveredY = this.mHoveredY;
                this.mHoveredX = event.getX();
                this.mHoveredY = event.getY();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("PreX,Y:");
                sb2.append(this.mPreHoveredX);
                sb2.append(",");
                sb2.append(this.mPreHoveredY);
                LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("CurX,Y:");
                sb3.append(this.mHoveredX);
                sb3.append(",");
                sb3.append(this.mHoveredY);
                LtUtil.log_i(SPenDrawTest.this.CLASS_NAME, "onHover", sb3.toString());
                if (SPenDrawTest.this.isHoveringEnbale()) {
                    drawLine(this.mPreHoveredX, this.mPreHoveredY, this.mHoveredX, this.mHoveredY, false);
                }
                this.isPenDetect = true;
                this.isHover = true;
            }
            drawInfo(this.mHoveredX, this.mHoveredY, 0.0f);
            return false;
        }

        private void drawLine(float preX, float preY, float x, float y, boolean bTouch) {
            int highX;
            int lowX;
            int highY;
            int lowY;
            if (bTouch) {
                this.mMatrixCanvas.drawLine(preX, preY, x, y, this.mLinePaint);
            }
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
            if (bTouch) {
                invalidate(new Rect(lowX - 3, lowY - 3, highX + 3, highY + 3));
            } else {
                invalidate(new Rect(lowX - 7, lowY - 7, highX + 7, highY + 7));
            }
        }

        private void drawPoint(float x, float y) {
            this.mMatrixCanvas.drawPoint(x, y, this.mLinePaint);
            invalidate(new Rect(((int) x) - 3, ((int) y) - 3, ((int) x) + 3, ((int) y) + 3));
        }

        public void drawInfo(float x, float y, float pressure) {
            String result;
            String result2;
            String str = "";
            int itemW = this.mScreenWidth / 5;
            Matrix mtx = new Matrix();
            mtx.reset();
            this.mInfoBitmap = Bitmap.createBitmap(this.mMatrixBitmap, 0, 0, this.mScreenWidth, 31, mtx, true);
            this.mInfoCanvas = new Canvas(this.mInfoBitmap);
            this.mInfoCanvas.drawRect(0.0f, 0.0f, (float) (this.mScreenWidth - 1), 30.0f, this.mInfoBackgroundPaint);
            this.mInfoPaint.setTextSize(25.0f);
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append("   Scr: ");
            String result3 = sb.toString();
            if (SPenDrawTest.this.mIsLandscape) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(result3);
                sb2.append("LAND");
                result = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(result3);
                sb3.append("PORT");
                result = sb3.toString();
            }
            this.mInfoCanvas.drawText(result, (float) (itemW * 0), 26.0f, this.mInfoPaint);
            if (this.isPenDetect) {
                this.mLevelPaint.setStrokeWidth(1.0f);
                this.mLevelPaint.setARGB(192, 100, 100, 200);
                this.mLevelPaint.setStyle(Style.FILL);
                this.mInfoCanvas.drawRect((float) (itemW * 1), 1.0f, (float) ((itemW * 2) - 5), 30.0f, this.mLevelPaint);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append("  PDCT : ");
            String result4 = sb4.toString();
            if (this.isPenDetect) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(result4);
                sb5.append("On ");
                result2 = sb5.toString();
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(result4);
                sb6.append("Off");
                result2 = sb6.toString();
            }
            this.mInfoCanvas.drawText(result2, (float) (itemW * 1), 26.0f, this.mInfoPaint);
            StringBuilder sb7 = new StringBuilder();
            sb7.append("");
            sb7.append(" X: ");
            String result5 = sb7.toString();
            StringBuilder sb8 = new StringBuilder();
            sb8.append(result5);
            sb8.append(String.format("%.2f ", new Object[]{Float.valueOf(x)}));
            this.mInfoCanvas.drawText(sb8.toString(), (float) (itemW * 2), 26.0f, this.mInfoPaint);
            StringBuilder sb9 = new StringBuilder();
            sb9.append("");
            sb9.append(" Y: ");
            String result6 = sb9.toString();
            StringBuilder sb10 = new StringBuilder();
            sb10.append(result6);
            sb10.append(String.format("%.2f ", new Object[]{Float.valueOf(y)}));
            this.mInfoCanvas.drawText(sb10.toString(), (float) (itemW * 3), 26.0f, this.mInfoPaint);
            this.mLevelPaint.setStyle(Style.STROKE);
            this.mLevelPaint.setStrokeWidth(1.0f);
            this.mLevelPaint.setARGB(192, 255, 0, 0);
            this.mInfoCanvas.drawRect((float) (itemW * 4), 1.0f, (float) ((itemW * 5) - 1), 30.0f, this.mLevelPaint);
            this.mLevelPaint.setStyle(Style.FILL);
            this.mInfoCanvas.drawRect((float) (itemW * 4), 1.0f, ((float) (itemW * 4)) + (((float) itemW) * pressure), 30.0f, this.mLevelPaint);
            StringBuilder sb11 = new StringBuilder();
            sb11.append("");
            sb11.append(" Prs : ");
            String result7 = sb11.toString();
            StringBuilder sb12 = new StringBuilder();
            sb12.append(result7);
            sb12.append(String.format("%.3f", new Object[]{Float.valueOf(pressure)}));
            this.mInfoCanvas.drawText(sb12.toString(), (float) (itemW * 4), 26.0f, this.mInfoPaint);
            invalidate(new Rect(0, 0, this.mScreenWidth - 1, 40));
        }

        public void initRect() {
            float col_height = ((float) this.mScreenHeight) / ((float) SPenDrawTest.this.HEIGHT_BASIS);
            float col_width = ((float) this.mScreenWidth) / ((float) SPenDrawTest.this.WIDTH_BASIS);
            Paint mRectPaint = new Paint();
            this.mEmptyPaint.setColor(-1);
            this.mMatrixCanvas.drawRect(0.0f, 0.0f, (float) this.mScreenWidth, (float) this.mScreenHeight, this.mEmptyPaint);
            mRectPaint.setColor(-16777216);
            int ColX = 0;
            int i = 0;
            while (i < SPenDrawTest.this.HEIGHT_BASIS) {
                int ColY = (int) (((float) i) * col_height);
                int ColX2 = ColX;
                int ColX3 = 0;
                while (true) {
                    int j = ColX3;
                    if (j >= SPenDrawTest.this.WIDTH_BASIS) {
                        break;
                    }
                    int ColX4 = (int) (((float) j) * col_width);
                    Paint paint = mRectPaint;
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) this.mScreenWidth, (float) ColY, paint);
                    this.mMatrixCanvas.drawLine((float) ColX4, (float) ColY, (float) ColX4, (float) this.mScreenHeight, paint);
                    ColX3 = j + 1;
                    ColX2 = ColX4;
                }
                i++;
                ColX = ColX2;
            }
        }
    }

    public SPenDrawTest() {
        super("SPenDrawTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.blank_view);
        ((LinearLayout) findViewById(C0268R.C0269id.view)).setOnHoverListener(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (this.mComponentName != null) {
            Intent intent = new Intent();
            intent.setComponent(this.mComponentName);
            stopService(intent);
            this.mComponentName = null;
        }
        disablePointerLocation();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mIsEnabledPointerLocation) {
            disablePointerLocation();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (!this.mIsEnabledPointerLocation) {
            enablePointerLocation();
        }
        LtUtil.setRemoveSystemUI(getWindow(), true);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public boolean onTouchEvent(MotionEvent event) {
        LtUtil.log_i(this.CLASS_NAME, "onTouchEvent", event.toString());
        if (this.mTouchView == null) {
            return false;
        }
        if (this.mIsCurrentScreenState == 1) {
            event.setLocation(event.getX(), event.getY() + ((float) this.mCocktatilBarSize));
        } else if (this.mIsCurrentScreenState == 2) {
            event.setLocation(event.getX() + ((float) this.mCocktatilBarSize), event.getY());
        }
        return this.mTouchView.onTouchEvent(event);
    }

    public boolean onHover(View v, MotionEvent event) {
        LtUtil.log_i(this.CLASS_NAME, "onHover", event.toString());
        if (this.mTouchView == null) {
            return false;
        }
        if (this.mIsCurrentScreenState == 1) {
            event.setLocation(event.getX(), event.getY() + ((float) this.mCocktatilBarSize));
        } else if (this.mIsCurrentScreenState == 2) {
            event.setLocation(event.getX() + ((float) this.mCocktatilBarSize), event.getY());
        }
        return ((OnHoverListener) this.mTouchView).onHover(v, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24) {
            this.isHoveringTest = true ^ this.isHoveringTest;
            Context applicationContext = getApplicationContext();
            StringBuilder sb = new StringBuilder();
            sb.append("Hovering draw ");
            sb.append(this.isHoveringTest);
            Toast.makeText(applicationContext, sb.toString(), 0).show();
            finish();
        } else if (keyCode == 25) {
            LtUtil.log_i(this.CLASS_NAME, "onKeyDown", "Volume key pressed");
            ((TouchView) this.mTouchView).initRect();
            ((TouchView) this.mTouchView).drawInfo(0.0f, 0.0f, 0.0f);
            this.mTouchView.invalidate();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isHoveringEnbale() {
        return this.isHoveringTest;
    }

    private void enablePointerLocation() {
        if (this.mTouchView == null) {
            this.mTouchView = new TouchView(this);
        }
        WindowManager wm = (WindowManager) getSystemService("window");
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.type = 2015;
        lp.flags = TestResultParser.TEST_TOKEN_DOWN_VALUE;
        lp.format = -3;
        lp.setTitle(this.CLASS_NAME);
        try {
            wm.addView(this.mTouchView, lp);
        } catch (BadTokenException bte) {
            LtUtil.log_e(bte);
        } catch (InvalidDisplayException ide) {
            LtUtil.log_e(ide);
        } catch (NullPointerException ne) {
            LtUtil.log_e(ne);
        }
        this.mIsEnabledPointerLocation = true;
    }

    private void disablePointerLocation() {
        if (this.mTouchView != null) {
            this.mTouchView.setOnHoverListener(null);
        }
        if (this.mTouchView != null) {
            ((WindowManager) getSystemService("window")).removeView(this.mTouchView);
            this.mTouchView = null;
        }
        this.mIsEnabledPointerLocation = false;
    }
}
