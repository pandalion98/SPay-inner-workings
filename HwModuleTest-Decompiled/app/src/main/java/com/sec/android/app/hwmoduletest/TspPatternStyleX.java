package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import com.samsung.android.feature.SemFloatingFeature;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.NVAccessor;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

public class TspPatternStyleX extends BaseActivity {
    private static final String ACTION_OQCSBFTT_READ_DATA = "com.sec.factory.OQCSBFTT.READ_DATA";
    private static final String HWMODULE_TSPTEST_END = "android.intent.action.HWMODULE_TSPTEST_END";
    private static final String TAG = "TspPatternStyleX";
    /* access modifiers changed from: private */
    public static boolean isForcedLandscape = false;
    public static boolean mIsFinishingChildActivity = false;
    public static boolean mIsFinishingParentActivity = false;
    public static boolean mTestEnd = false;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS = 19;
    /* access modifiers changed from: private */
    public int HEIGHT_BASIS_CROSS = 19;
    private byte ID_TOUCH = 4;
    private byte ID_TOUCH_2 = HwModuleTest.ID_TOUCH_2;
    /* access modifiers changed from: private */
    public boolean IsWirelessCharge = false;
    protected int KEY_TIMEOUT;
    protected int KEY_TIMER_EXPIRED;
    /* access modifiers changed from: private */
    public int MARGIN_BOTTOM = 0;
    /* access modifiers changed from: private */
    public int MARGIN_LEFT = 0;
    /* access modifiers changed from: private */
    public int MARGIN_RIGHT = 0;
    /* access modifiers changed from: private */
    public int MARGIN_TOP = 0;
    private String MEASURE = "px";
    protected int MILLIS_IN_SEC;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS = 11;
    /* access modifiers changed from: private */
    public int WIDTH_BASIS_CROSS = 11;
    /* access modifiers changed from: private */
    public boolean[][] click;
    private boolean dialog_showing = false;
    /* access modifiers changed from: private */
    public boolean[][] draw;
    /* access modifiers changed from: private */
    public boolean[] drawCross;
    Editor editor;
    /* access modifiers changed from: private */
    public boolean[][] isDrawArea;
    /* access modifiers changed from: private */
    public boolean isExistMargin = false;
    /* access modifiers changed from: private */
    public boolean isHovering = false;
    private int mBottommostOfMatrix;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int plugged = intent.getIntExtra("plugged", 0);
                StringBuilder sb = new StringBuilder();
                sb.append("action=");
                sb.append(action);
                sb.append(", plugged=");
                sb.append(plugged);
                LtUtil.log_d(TspPatternStyleX.this.CLASS_NAME, "mBroadcastReceiver.onReceive", sb.toString());
                if (plugged == 4) {
                    TspPatternStyleX.this.IsWirelessCharge = true;
                } else {
                    TspPatternStyleX.this.IsWirelessCharge = false;
                }
            } else if (action.equals(TspPatternStyleX.ACTION_OQCSBFTT_READ_DATA)) {
                int item = intent.getIntExtra("item", 0);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ITEM :");
                sb2.append(item);
                LtUtil.log_d(TspPatternStyleX.this.CLASS_NAME, "BroadcaseReceiver onReceive", sb2.toString());
                if (item == 401) {
                    TspPatternStyleX.this.writeLastTouched();
                }
            }
        }
    };
    private int mCenterOfHorizontalOfMatrix;
    private int mCenterOfVerticalOfMatrix;
    private final IntentFilter mIntentFilter = new IntentFilter();
    private int mLeftmostOfMatrix;
    private ModuleDevice mModuleDevice;
    private int mRightmostOfMatrix;
    private int mTestID = 0;
    private int mTopmostOfMatrix;
    private boolean needFailPopupDispaly = false;
    SharedPreferences prefLastTouchPoint;
    /* access modifiers changed from: private */
    public boolean remoteCall = false;
    /* access modifiers changed from: private */
    public boolean successTest = true;
    private BroadcastReceiver testEndReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TspPatternStyleX.HWMODULE_TSPTEST_END)) {
                TspPatternStyleX.this.finish();
            }
        }
    };
    private String testcase;
    private String tunningData;

    public class MyView extends View implements IGloverEventHandler {
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
        private float mWidthCross = 0.0f;

        public MyView(Context context) {
            super(context);
            Display mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point outpoint = new Point();
            mDisplay.getRealSize(outpoint);
            if (TspPatternStyleX.this.isExistMargin) {
                this.mScreenWidth = outpoint.x - (TspPatternStyleX.this.MARGIN_LEFT + TspPatternStyleX.this.MARGIN_RIGHT);
                this.mScreenHeight = outpoint.y - (TspPatternStyleX.this.MARGIN_TOP + TspPatternStyleX.this.MARGIN_BOTTOM);
            } else {
                this.mScreenWidth = outpoint.x;
                this.mScreenHeight = outpoint.y;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Screen size: ");
            sb.append(this.mScreenWidth);
            sb.append(" x ");
            sb.append(this.mScreenHeight);
            LtUtil.log_i(TspPatternStyleX.this.CLASS_NAME, "onCreate", sb.toString());
            if (TspPatternStyleX.this.isExistMargin) {
                this.mMatrixBitmap = Bitmap.createBitmap(this.mScreenWidth + TspPatternStyleX.this.MARGIN_LEFT + TspPatternStyleX.this.MARGIN_RIGHT, this.mScreenHeight + TspPatternStyleX.this.MARGIN_TOP + TspPatternStyleX.this.MARGIN_BOTTOM, Config.RGB_565);
            } else {
                this.mMatrixBitmap = Bitmap.createBitmap(this.mScreenWidth, this.mScreenHeight, Config.RGB_565);
            }
            this.mMatrixCanvas = new Canvas(this.mMatrixBitmap);
            this.mMatrixCanvas.drawColor(-1);
            this.col_height = ((float) this.mScreenHeight) / ((float) TspPatternStyleX.this.HEIGHT_BASIS);
            this.col_width = ((float) this.mScreenWidth) / ((float) TspPatternStyleX.this.WIDTH_BASIS);
            if (TspPatternStyleX.isForcedLandscape) {
                if (TspPatternStyleX.this.isHovering) {
                    this.mHeightCross = (((float) this.mScreenHeight) - (this.col_height * 3.0f)) / ((float) ((TspPatternStyleX.this.WIDTH_BASIS_CROSS - 1) + 4));
                } else {
                    this.mHeightCross = (((float) this.mScreenHeight) - (this.col_height * 1.0f)) / ((float) ((TspPatternStyleX.this.WIDTH_BASIS_CROSS - 1) + 4));
                }
            } else if (TspPatternStyleX.this.isHovering) {
                this.mWidthCross = (((float) this.mScreenWidth) - (this.col_width * 3.0f)) / ((float) ((TspPatternStyleX.this.HEIGHT_BASIS_CROSS - 1) + 4));
            } else {
                this.mWidthCross = (((float) this.mScreenWidth) - (this.col_width * 1.0f)) / ((float) ((TspPatternStyleX.this.HEIGHT_BASIS_CROSS - 1) + 4));
            }
            if (TspPatternStyleX.this.isHovering && !"factory".equalsIgnoreCase(Properties.get(Properties.BINARY_TYPE))) {
                setOnTouchListener(new OnTouchListener(TspPatternStyleX.this) {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
            if (TspPatternStyleX.isForcedLandscape) {
                this.mWidthCross = this.col_width / 2.0f;
            } else {
                this.mHeightCross = this.col_height / 2.0f;
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
            this.mClickPaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
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
        }

        public boolean onHoverEvent(MotionEvent event) {
            int action = event.getAction();
            if (TspPatternStyleX.this.isHovering) {
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
            if (TspPatternStyleX.this.isHovering) {
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
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf((int) this.mTouchedX));
            sb.append(",");
            sb.append(String.valueOf((int) this.mTouchedY));
            TspPatternStyleX.this.editor.putString("LAST_TOUCHED", sb.toString());
            TspPatternStyleX.this.editor.apply();
            drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
            this.isTouchDown = true;
        }

        private void draw_move(MotionEvent event) {
            if (this.isTouchDown) {
                if (!TspPatternStyleX.this.IsWirelessCharge) {
                    for (int i = 0; i < event.getHistorySize(); i++) {
                        this.mPreTouchedX = this.mTouchedX;
                        this.mPreTouchedY = this.mTouchedY;
                        this.mTouchedX = event.getHistoricalX(i);
                        this.mTouchedY = event.getHistoricalY(i);
                        drawRect(this.mTouchedX, this.mTouchedY, this.mClickPaint);
                        drawLine(this.mPreTouchedX, this.mPreTouchedY, this.mTouchedX, this.mTouchedY);
                    }
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
                TspPatternStyleX.this.editor.putString("LAST_TOUCHED", "-1");
                TspPatternStyleX.this.editor.apply();
                this.isTouchDown = false;
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
            int ColX;
            int ColY;
            int ColY2;
            int ColX2;
            int ColY3;
            int ColY4;
            Paint mRectPaint = new Paint();
            Paint mRectPaintCross = new Paint();
            mRectPaint.setColor(-16777216);
            mRectPaintCross.setColor(-16777216);
            mRectPaintCross.setStyle(Style.STROKE);
            for (int i = 0; i <= TspPatternStyleX.this.HEIGHT_BASIS; i++) {
                if (TspPatternStyleX.this.isExistMargin) {
                    int ColY5 = ((int) (this.col_height * ((float) i))) + TspPatternStyleX.this.MARGIN_TOP;
                    this.mMatrixCanvas.drawLine((float) TspPatternStyleX.this.MARGIN_LEFT, (float) ColY5, (float) (this.mScreenWidth + TspPatternStyleX.this.MARGIN_LEFT), (float) ColY5, mRectPaint);
                } else {
                    int ColY6 = (int) (this.col_height * ((float) i));
                    this.mMatrixCanvas.drawLine(0.0f, (float) ColY6, (float) this.mScreenWidth, (float) ColY6, mRectPaint);
                }
            }
            for (int j = 0; j <= TspPatternStyleX.this.WIDTH_BASIS; j++) {
                if (TspPatternStyleX.this.isExistMargin) {
                    int ColX3 = ((int) (this.col_width * ((float) j))) + TspPatternStyleX.this.MARGIN_LEFT;
                    this.mMatrixCanvas.drawLine((float) ColX3, (float) TspPatternStyleX.this.MARGIN_TOP, (float) ColX3, (float) (this.mScreenHeight + TspPatternStyleX.this.MARGIN_TOP), mRectPaint);
                } else {
                    int ColX4 = (int) (this.col_width * ((float) j));
                    this.mMatrixCanvas.drawLine((float) ColX4, 0.0f, (float) ColX4, (float) this.mScreenHeight, mRectPaint);
                }
            }
            for (int i2 = 0; i2 < TspPatternStyleX.this.HEIGHT_BASIS; i2++) {
                for (int j2 = 0; j2 < TspPatternStyleX.this.WIDTH_BASIS; j2++) {
                    TspPatternStyleX.this.draw[i2][j2] = false;
                    TspPatternStyleX.this.click[i2][j2] = false;
                }
            }
            if (TspPatternStyleX.this.isHovering) {
                this.mMatrixCanvas.drawRect(0.0f, 0.0f, this.col_width - 1.0f, (float) this.mScreenHeight, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect(((float) this.mScreenWidth) - this.col_width, 0.0f, (float) this.mScreenWidth, (float) this.mScreenHeight, this.mEmptyPaint);
                this.mMatrixCanvas.drawRect((this.col_width * 2.0f) + 1.0f, this.col_height + 1.0f, (this.col_width * ((float) (TspPatternStyleX.this.WIDTH_BASIS - 2))) - 1.0f, (this.col_height * ((float) (TspPatternStyleX.this.HEIGHT_BASIS - 1))) - 1.0f, this.mEmptyPaint);
            } else if (TspPatternStyleX.this.isExistMargin) {
                this.mMatrixCanvas.drawRect(this.col_width + ((float) TspPatternStyleX.this.MARGIN_LEFT) + 1.0f, this.col_height + ((float) TspPatternStyleX.this.MARGIN_TOP) + 1.0f, ((this.col_width * ((float) (TspPatternStyleX.this.WIDTH_BASIS - 1))) + ((float) TspPatternStyleX.this.MARGIN_LEFT)) - 1.0f, ((this.col_height * ((float) (TspPatternStyleX.this.HEIGHT_BASIS - 1))) + ((float) TspPatternStyleX.this.MARGIN_TOP)) - 1.0f, this.mEmptyPaint);
            } else {
                this.mMatrixCanvas.drawRect(this.col_width + 1.0f, this.col_height + 1.0f, (this.col_width * ((float) (TspPatternStyleX.this.WIDTH_BASIS - 1))) - 1.0f, (this.col_height * ((float) (TspPatternStyleX.this.HEIGHT_BASIS - 1))) - 1.0f, this.mEmptyPaint);
            }
            for (int i3 = 0; i3 < TspPatternStyleX.this.HEIGHT_BASIS_CROSS; i3++) {
                if (TspPatternStyleX.this.isHovering) {
                    ColX2 = ((int) (this.mWidthCross * ((float) (i3 + 2)))) + ((int) this.col_width);
                } else if (TspPatternStyleX.this.isExistMargin) {
                    ColX2 = (int) ((this.mWidthCross * ((float) (i3 + 2))) + ((float) TspPatternStyleX.this.MARGIN_LEFT));
                } else {
                    ColX2 = (int) (this.mWidthCross * ((float) (i3 + 2)));
                }
                if (TspPatternStyleX.isForcedLandscape) {
                    ColY4 = (int) (this.mHeightCross * ((float) (i3 + 2)));
                    this.mMatrixCanvas.drawRect((float) ColX2, (float) ColY4, ((float) ColX2) + (this.col_width / 2.0f), ((float) ColY4) + this.col_height, mRectPaintCross);
                } else {
                    if (TspPatternStyleX.this.isExistMargin) {
                        ColY3 = (int) (this.col_height + (this.mHeightCross * ((float) i3)) + ((float) TspPatternStyleX.this.MARGIN_TOP));
                    } else {
                        ColY3 = (int) (this.col_height + (this.mHeightCross * ((float) i3)));
                    }
                    ColY4 = ColY3;
                    this.mMatrixCanvas.drawRect((float) ColX2, (float) ColY4, ((float) ColX2) + this.col_width, ((float) ColY4) + (this.col_height / 2.0f), mRectPaintCross);
                }
                TspPatternStyleX.this.drawCross[i3] = false;
            }
            for (int i4 = 0; i4 < TspPatternStyleX.this.HEIGHT_BASIS_CROSS; i4++) {
                if (TspPatternStyleX.this.isHovering) {
                    ColX = ((int) (this.mWidthCross * ((float) (i4 + 2)))) + ((int) this.col_width);
                } else if (TspPatternStyleX.this.isExistMargin) {
                    ColX = (int) ((this.mWidthCross * ((float) (i4 + 2))) + ((float) TspPatternStyleX.this.MARGIN_LEFT));
                } else {
                    ColX = (int) (this.mWidthCross * ((float) (i4 + 2)));
                }
                if (TspPatternStyleX.isForcedLandscape) {
                    ColY2 = (int) (((float) this.mScreenHeight) - (this.col_height + (this.mHeightCross * ((float) (i4 + 2)))));
                    this.mMatrixCanvas.drawRect((float) ColX, (float) ColY2, ((float) ColX) + (this.col_width / 2.0f), ((float) ColY2) + this.col_height, mRectPaintCross);
                } else {
                    if (TspPatternStyleX.this.isExistMargin) {
                        ColY = (int) ((((float) this.mScreenHeight) - (this.col_height + (this.mHeightCross * ((float) (i4 + 1))))) + ((float) TspPatternStyleX.this.MARGIN_TOP));
                    } else {
                        ColY = (int) (((float) this.mScreenHeight) - (this.col_height + (this.mHeightCross * ((float) (i4 + 1)))));
                    }
                    ColY2 = ColY;
                    this.mMatrixCanvas.drawRect((float) ColX, (float) ColY2, ((float) ColX) + this.col_width, ((float) ColY2) + (this.col_height / 2.0f), mRectPaintCross);
                }
                TspPatternStyleX.this.drawCross[TspPatternStyleX.this.HEIGHT_BASIS_CROSS + i4] = false;
            }
        }

        private void drawRect(float x, float y, Paint paint) {
            int countX;
            int countY;
            float ColX;
            float ColY;
            if (!TspPatternStyleX.this.isHovering || TOOLTYPE.getIsHoverToolType()) {
                float col_height2 = ((float) this.mScreenHeight) / ((float) TspPatternStyleX.this.HEIGHT_BASIS);
                float col_width2 = ((float) this.mScreenWidth) / ((float) TspPatternStyleX.this.WIDTH_BASIS);
                if (TspPatternStyleX.this.isExistMargin) {
                    countX = (int) ((x - ((float) TspPatternStyleX.this.MARGIN_LEFT)) / col_width2);
                    countY = (int) ((y - ((float) TspPatternStyleX.this.MARGIN_TOP)) / col_height2);
                    ColX = (((float) countX) * col_width2) + ((float) TspPatternStyleX.this.MARGIN_LEFT);
                    ColY = (((float) countY) * col_height2) + ((float) TspPatternStyleX.this.MARGIN_TOP);
                } else {
                    countX = (int) (x / col_width2);
                    countY = (int) (y / col_height2);
                    ColX = ((float) countX) * col_width2;
                    ColY = ((float) countY) * col_height2;
                }
                int countX2 = countX;
                int countY2 = countY;
                float ColY2 = ColY;
                float ColX2 = ColX;
                if (countY2 > TspPatternStyleX.this.HEIGHT_BASIS - 1 || countX2 > TspPatternStyleX.this.WIDTH_BASIS - 1 || countX2 < 0 || countY2 < 0 || x < ((float) TspPatternStyleX.this.MARGIN_LEFT) || y < ((float) TspPatternStyleX.this.MARGIN_TOP)) {
                    LtUtil.log_e(TspPatternStyleX.this.CLASS_NAME, "drawRect", "You are out of bounds!");
                    return;
                }
                if (!TspPatternStyleX.this.draw[countY2][countX2]) {
                    TspPatternStyleX.this.draw[countY2][countX2] = true;
                    if (TspPatternStyleX.this.draw[countY2][countX2] && TspPatternStyleX.this.isDrawArea[countY2][countX2]) {
                        this.mMatrixCanvas.drawRect((float) (((int) ColX2) + 1), (float) (((int) ColY2) + 1), (float) ((int) (ColX2 + col_width2)), (float) ((int) (ColY2 + col_height2)), paint);
                        invalidate(new Rect((int) (ColX2 - 1.0f), (int) (ColY2 - 1.0f), (int) (ColX2 + col_width2 + 1.0f), (int) (ColY2 + col_height2 + 1.0f)));
                    }
                }
                checkCrossRectRegion(x, y, countX2, countY2, paint);
                if (TspPatternStyleX.this.isPass() && TspPatternStyleX.this.isPassCross()) {
                    if (BaseActivity.mIsDualScreenFeatureEnabled) {
                        if (TspPatternStyleX.this.getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
                            TspPatternStyleX.mIsFinishingChildActivity = true;
                        } else {
                            TspPatternStyleX.mIsFinishingParentActivity = true;
                        }
                        if (!TspPatternStyleX.mIsFinishingChildActivity || !TspPatternStyleX.mIsFinishingParentActivity) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Not yet finished!, mIsFinishingChildActivity=");
                            sb.append(TspPatternStyleX.mIsFinishingChildActivity);
                            sb.append(", mIsFinishingParentActivity=");
                            sb.append(TspPatternStyleX.mIsFinishingParentActivity);
                            LtUtil.log_d(TspPatternStyleX.this.CLASS_NAME, "drawRect", sb.toString());
                            return;
                        }
                    }
                    if (TspPatternStyleX.this.successTest) {
                        TspPatternStyleX.this.successTest = false;
                        if (TspPatternStyleX.this.remoteCall) {
                            TspPatternStyleX.this.setResult(-1);
                        }
                        TspPatternStyleX.this.setResult(-1);
                        if (BaseActivity.isOqcsbftt) {
                            NVAccessor.setNV(401, NVAccessor.NV_VALUE_PASS);
                        }
                        TspPatternStyleX.this.finish();
                    } else {
                        TspPatternStyleX.this.setResult(0);
                        TspPatternStyleX.this.finish();
                    }
                }
            }
        }

        private void checkCrossRectRegion(float x, float y, int countX, int countY, Paint paint) {
            int countCrossY;
            int startRectBottomX;
            int startRectTopX;
            int startRectTopY;
            int startRectTopX2;
            int startRectBottomX2;
            int startRectBottomX3;
            int startRectTopX3;
            int startRectBottomX4;
            int startRectTopY2;
            int startRectTopX4;
            int countCrossY2;
            int i = countX;
            int i2 = countY;
            int startRectLandTopY = 0;
            int startRectLandBottomY = 0;
            if (TspPatternStyleX.isForcedLandscape) {
                if (i > 0 && i < TspPatternStyleX.this.WIDTH_BASIS - 1) {
                    int countCrossX = (int) ((x - this.col_width) / this.mWidthCross);
                    int countCrossY3 = (int) ((y - this.col_height) / this.mHeightCross);
                    int startRectTopY3 = (int) (this.mHeightCross * ((float) (countCrossY3 + 2)));
                    int startRectBottomY = startRectTopY3;
                    int startRectLandTopX = (int) (this.mWidthCross * ((float) (countCrossX + 2)));
                    int startRectLandBottomX = startRectLandTopX;
                    if (TspPatternStyleX.this.isHovering) {
                        startRectBottomX3 = (int) ((((float) this.mScreenWidth) - (this.col_width * 2.0f)) - (this.mWidthCross * ((float) (countCrossY3 + 2))));
                        startRectTopX3 = ((int) (this.mWidthCross * ((float) (countCrossY3 + 2)))) + ((int) this.col_width);
                    } else {
                        startRectLandTopY = (int) (this.mHeightCross * ((float) (countCrossX + 2)));
                        int startRectTopX5 = (int) (this.mWidthCross * ((float) (countCrossY3 + 2)));
                        startRectLandBottomY = (int) ((((float) this.mScreenHeight) - (this.col_height * 1.0f)) - (this.mHeightCross * ((float) (countCrossX + 2))));
                        startRectBottomX3 = (int) ((((float) this.mScreenWidth) - (this.col_width * 1.0f)) - (this.mWidthCross * ((float) (countCrossY3 + 1))));
                        startRectTopX3 = startRectTopX5;
                    }
                    if (y <= ((float) startRectLandTopY) || y >= ((float) startRectLandTopY) + this.col_height) {
                        countCrossY2 = countCrossY3;
                        startRectTopX4 = startRectTopX3;
                        startRectTopY2 = startRectTopY3;
                        startRectBottomX4 = startRectBottomX3;
                    } else {
                        if (!TspPatternStyleX.this.drawCross[countCrossX]) {
                            countCrossY2 = countCrossY3;
                            startRectTopX4 = startRectTopX3;
                            startRectTopY2 = startRectTopY3;
                            startRectBottomX4 = startRectBottomX3;
                            this.mMatrixCanvas.drawRect((float) (startRectLandTopX + 1), (float) (startRectLandTopY + 1), (float) ((int) (((float) startRectLandTopX) + (this.col_width / 2.0f))), (float) ((int) (((float) startRectLandTopY) + this.col_height)), paint);
                            invalidate(new Rect(startRectLandTopX - 1, startRectLandTopY - 1, (int) (((float) startRectLandTopX) + (this.col_width / 2.0f) + 1.0f), (int) (((float) startRectLandTopY) + this.col_height + 1.0f)));
                        } else {
                            countCrossY2 = countCrossY3;
                            startRectTopX4 = startRectTopX3;
                            startRectTopY2 = startRectTopY3;
                            startRectBottomX4 = startRectBottomX3;
                        }
                        if (countCrossX >= 0) {
                            TspPatternStyleX.this.drawCross[countCrossX] = true;
                        }
                    }
                    if (y > ((float) startRectLandBottomY) && y < ((float) startRectLandBottomY) + this.col_height) {
                        if (!TspPatternStyleX.this.drawCross[((TspPatternStyleX.this.WIDTH_BASIS_CROSS * 2) - 1) - countCrossX]) {
                            this.mMatrixCanvas.drawRect((float) (startRectLandBottomX + 1), (float) (startRectLandBottomY + 1), (float) ((int) (((float) startRectLandBottomX) + (this.col_width / 2.0f))), (float) ((int) (((float) startRectLandBottomY) + this.col_height)), paint);
                            invalidate(new Rect(startRectLandBottomX - 1, startRectLandBottomY - 1, (int) (((float) startRectLandBottomX) + (this.col_width / 2.0f) + 1.0f), (int) (((float) startRectLandBottomY) + this.col_height + 1.0f)));
                        }
                        if (countCrossX >= 0) {
                            TspPatternStyleX.this.drawCross[((TspPatternStyleX.this.WIDTH_BASIS_CROSS * 2) - 1) - countCrossX] = true;
                        }
                    }
                    int i3 = countCrossX;
                    int i4 = startRectLandTopX;
                    int i5 = countCrossY2;
                    int i6 = startRectTopX4;
                    int i7 = startRectTopY2;
                    int i8 = startRectBottomX4;
                }
            } else if (i2 > 0 && i2 < TspPatternStyleX.this.HEIGHT_BASIS - 1) {
                if (TspPatternStyleX.this.isExistMargin) {
                    countCrossY = (int) (((y - this.col_height) - ((float) TspPatternStyleX.this.MARGIN_TOP)) / this.mHeightCross);
                } else {
                    countCrossY = (int) ((y - this.col_height) / this.mHeightCross);
                }
                int countCrossY4 = countCrossY;
                if (TspPatternStyleX.this.isHovering) {
                    startRectBottomX = (int) ((((float) this.mScreenWidth) - (this.col_width * 2.0f)) - (this.mWidthCross * ((float) (countCrossY4 + 2))));
                    startRectTopX = ((int) (this.mWidthCross * ((float) (countCrossY4 + 2)))) + ((int) this.col_width);
                } else {
                    if (TspPatternStyleX.this.isExistMargin) {
                        startRectTopX2 = (int) ((this.mWidthCross * ((float) (countCrossY4 + 2))) + ((float) TspPatternStyleX.this.MARGIN_LEFT));
                        startRectBottomX2 = (int) (((((float) this.mScreenWidth) - (this.col_width * 1.0f)) - (this.mWidthCross * ((float) (countCrossY4 + 2)))) + ((float) TspPatternStyleX.this.MARGIN_LEFT));
                    } else {
                        startRectTopX2 = (int) (this.mWidthCross * ((float) (countCrossY4 + 2)));
                        startRectBottomX2 = (int) ((((float) this.mScreenWidth) - (this.col_width * 1.0f)) - (this.mWidthCross * ((float) (countCrossY4 + 2))));
                    }
                    startRectTopX = startRectTopX2;
                    startRectBottomX = startRectBottomX2;
                }
                if (TspPatternStyleX.this.isExistMargin) {
                    startRectTopY = (int) (this.col_height + (this.mHeightCross * ((float) countCrossY4)) + ((float) TspPatternStyleX.this.MARGIN_TOP));
                } else {
                    startRectTopY = (int) (this.col_height + (this.mHeightCross * ((float) countCrossY4)));
                }
                int startRectTopY4 = startRectTopY;
                int startRectBottomY2 = startRectTopY4;
                if (x > ((float) startRectTopX) && x < ((float) startRectTopX) + this.col_width) {
                    if (!TspPatternStyleX.this.drawCross[countCrossY4]) {
                        this.mMatrixCanvas.drawRect((float) (startRectTopX + 1), (float) (startRectTopY4 + 1), (float) ((int) (((float) startRectTopX) + this.col_width)), (float) ((int) (((float) startRectTopY4) + (this.col_height / 2.0f))), paint);
                        invalidate(new Rect(startRectTopX - 1, startRectTopY4 - 1, (int) (((float) startRectTopX) + this.col_width + 1.0f), (int) (((float) startRectTopY4) + this.col_height + 1.0f)));
                    }
                    if (countCrossY4 >= 0) {
                        TspPatternStyleX.this.drawCross[countCrossY4] = true;
                    }
                }
                if (x > ((float) startRectBottomX) && x < ((float) startRectBottomX) + this.col_width) {
                    if (!TspPatternStyleX.this.drawCross[((TspPatternStyleX.this.HEIGHT_BASIS_CROSS * 2) - 1) - countCrossY4]) {
                        this.mMatrixCanvas.drawRect((float) (startRectBottomX + 1), (float) (startRectBottomY2 + 1), (float) ((int) (((float) startRectBottomX) + this.col_width)), (float) ((int) (((float) startRectBottomY2) + (this.col_height / 2.0f))), paint);
                        invalidate(new Rect(startRectBottomX - 1, startRectBottomY2 - 1, (int) (((float) startRectBottomX) + this.col_width + 1.0f), (int) (((float) startRectBottomY2) + this.col_height + 1.0f)));
                    }
                    if (countCrossY4 >= 0) {
                        TspPatternStyleX.this.drawCross[((TspPatternStyleX.this.HEIGHT_BASIS_CROSS * 2) - 1) - countCrossY4] = true;
                    }
                }
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

    public TspPatternStyleX() {
        super(TAG);
    }

    private boolean getNodeNum() {
        if (this.mTestID == this.ID_TOUCH) {
            this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH);
        } else if (this.mTestID == this.ID_TOUCH_2) {
            this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH_2);
        }
        if (!this.testcase.contains("NODE")) {
            return false;
        }
        this.testcase = this.testcase.substring(this.testcase.indexOf("NODE(") + "NODE(".length());
        String[] nodeStr = this.testcase.substring(0, this.testcase.indexOf(")")).trim().split(",");
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

    private boolean getMargin() {
        if (this.mTestID == this.ID_TOUCH) {
            this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH);
        } else if (this.mTestID == this.ID_TOUCH_2) {
            this.testcase = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_TOUCH_2);
        }
        if (!this.testcase.contains("MARGIN")) {
            return false;
        }
        this.testcase = this.testcase.substring(this.testcase.indexOf("MARGIN(") + "MARGIN(".length());
        String[] marginStr = this.testcase.substring(0, this.testcase.indexOf(")")).trim().split(",");
        this.MEASURE = marginStr[0];
        this.MARGIN_LEFT = Integer.parseInt(marginStr[1]);
        this.MARGIN_RIGHT = Integer.parseInt(marginStr[2]);
        this.MARGIN_TOP = Integer.parseInt(marginStr[3]);
        this.MARGIN_BOTTOM = Integer.parseInt(marginStr[4]);
        StringBuilder sb = new StringBuilder();
        sb.append("margin(LEFT,RIGHT,TOP,BOTTOM) -> ");
        sb.append(this.MARGIN_LEFT);
        sb.append(",");
        sb.append(this.MARGIN_RIGHT);
        sb.append(",");
        sb.append(this.MARGIN_TOP);
        sb.append(",");
        sb.append(this.MARGIN_BOTTOM);
        LtUtil.log_d(this.CLASS_NAME, "getMargin", sb.toString());
        if ("mm".equalsIgnoreCase(this.MEASURE)) {
            this.MARGIN_LEFT = (int) TypedValue.applyDimension(5, (float) this.MARGIN_LEFT, getResources().getDisplayMetrics());
            this.MARGIN_RIGHT = (int) TypedValue.applyDimension(5, (float) this.MARGIN_RIGHT, getResources().getDisplayMetrics());
            this.MARGIN_TOP = (int) TypedValue.applyDimension(5, (float) this.MARGIN_TOP, getResources().getDisplayMetrics());
            this.MARGIN_BOTTOM = (int) TypedValue.applyDimension(5, (float) this.MARGIN_BOTTOM, getResources().getDisplayMetrics());
        }
        return true;
    }

    private void setTSP() {
        this.isExistMargin = getMargin();
        if (!getNodeNum()) {
            this.WIDTH_BASIS = Spec.getInt(Spec.TSP_X_AXIS_CHANNEL);
            this.HEIGHT_BASIS = Spec.getInt(Spec.TSP_Y_AXIS_CHANNEL);
        }
        if (isForcedLandscape) {
            this.WIDTH_BASIS_CROSS = (this.WIDTH_BASIS - 2) * 2;
            this.HEIGHT_BASIS_CROSS = this.WIDTH_BASIS_CROSS;
        } else {
            this.HEIGHT_BASIS_CROSS = (this.HEIGHT_BASIS - 2) * 2;
            this.WIDTH_BASIS_CROSS = this.HEIGHT_BASIS_CROSS;
        }
        this.click = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.draw = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.isDrawArea = (boolean[][]) Array.newInstance(boolean.class, new int[]{this.HEIGHT_BASIS, this.WIDTH_BASIS});
        this.drawCross = new boolean[(this.HEIGHT_BASIS_CROSS * 2)];
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
        isForcedLandscape = Feature.getBoolean(Feature.FORCE_LANDSCAPE_MODE, false);
        this.isHovering = getIntent().getExtras().getBoolean("isHovering");
        this.mTestID = getIntent().getExtras().getInt("testID");
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
        this.prefLastTouchPoint = getSharedPreferences("LAST_TOUCHED_POINT", 0);
        this.editor = this.prefLastTouchPoint.edit();
        setTSP();
        decideRemote();
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "TouchTest is created");
        setContentView(new MyView(this));
        fillUpMatrix();
        if (mIsDualScreenFeatureEnabled) {
            if (getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
                mIsFinishingChildActivity = false;
            } else {
                mIsFinishingParentActivity = false;
            }
            mTestEnd = false;
            registerReceiver(this.testEndReceiver, new IntentFilter(HWMODULE_TSPTEST_END));
        }
        if (mIsDualScreenFeatureEnabled && !getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
            LtUtil.log_d(this.CLASS_NAME, "onCreate", "start Second Activity");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass(getBaseContext(), getClass());
            intent.putExtra(BaseActivity.SECOND_ACTIVITY, true);
            SDualScreenActivity.makeIntent(this, intent, DualScreen.SUB, SDualScreenActivity.FLAG_COUPLED_TASK_EXPAND_MODE);
            startActivityForResult(intent, 1);
        }
        String tspStateManagerFeature = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_TSP_STATE_MANAGER");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_TSP_STATE_MANAGER: ");
        sb2.append(tspStateManagerFeature);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb2.toString());
        if (tspStateManagerFeature != null && tspStateManagerFeature.contains("deadzone_v2")) {
            String threshold = getBaseContext().getResources().getString(17039924);
            if (threshold != null) {
                this.tunningData = threshold.split("&")[0];
            }
            if (Feature.getBoolean(Feature.SUPPORT_DEADZONE_ON_GRIP)) {
                Kernel.write(Kernel.TSP_COMMAND_CMD, "set_grip_data,2,1,1,0");
            } else {
                Kernel.write(Kernel.TSP_COMMAND_CMD, "set_tunning_data,0,1,0,0");
            }
            String status = Kernel.read(Kernel.TSP_COMMAND_STATUS);
            String result = Kernel.read(Kernel.TSP_COMMAND_RESULT);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Result of set_tunning_data : status= ");
            sb3.append(status);
            sb3.append(", result=");
            sb3.append(result);
            LtUtil.log_i(this.CLASS_NAME, "onCreate", sb3.toString());
        }
        this.mIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        if (isOqcsbftt) {
            this.mIntentFilter.addAction(ACTION_OQCSBFTT_READ_DATA);
        }
        registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        LtUtil.log_d(this.CLASS_NAME, "onStart", "");
        super.onStart();
        ModuleDevice.instance(this).startTSPTest(ModuleDevice.TSP_CMD_DISABLE_DEAD_ZONE);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LtUtil.log_d(this.CLASS_NAME, "onStop", "");
        super.onStop();
        ModuleDevice.instance(this).startTSPTest(ModuleDevice.TSP_CMD_ENABLE_DEAD_ZONE);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LtUtil.setPressureTouchStatus("0");
        LtUtil.log_d(this.CLASS_NAME, "onResume", null);
        if (this.isHovering) {
            LtUtil.log_d(this.CLASS_NAME, "onResume", "EnableHovering");
            this.mModuleDevice.hoveringOnOFF(EgisFingerprint.MAJOR_VERSION);
            GloveReceiver.registerBroadcastReceiver(this);
        }
        if (Feature.getBoolean(Feature.SUPPORT_ENABLE_TSP_DEADZONE)) {
            this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_ENABLE_DEAD_ZONE);
        }
        if (isOqcsbftt) {
            NVAccessor.setNV(401, NVAccessor.NV_VALUE_ENTER);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LtUtil.setPressureTouchStatus(EgisFingerprint.MAJOR_VERSION);
        LtUtil.log_d(this.CLASS_NAME, "onPause", null);
        if (this.isHovering) {
            LtUtil.log_d(this.CLASS_NAME, "onPause", "DisableHovering");
            this.mModuleDevice.hoveringOnOFF("0");
            GloveReceiver.unregisterBroadcastReceiver(this);
        }
        this.editor.putString("LAST_TOUCHED", "-1");
        this.editor.apply();
        if (isOqcsbftt) {
            writeLastTouched();
        }
        if (mIsDualScreenFeatureEnabled) {
            if (!mTestEnd) {
                mTestEnd = true;
                sendBroadcast(new Intent(HWMODULE_TSPTEST_END));
            } else {
                unregisterReceiver(this.testEndReceiver);
            }
        }
        String tspStateManagerFeature = SemFloatingFeature.getInstance().getString("SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_TSP_STATE_MANAGER");
        StringBuilder sb = new StringBuilder();
        sb.append("SEC_FLOATING_FEATURE_FRAMEWORK_SUPPORT_TSP_STATE_MANAGER: ");
        sb.append(tspStateManagerFeature);
        LtUtil.log_d(this.CLASS_NAME, "onPause", sb.toString());
        if (!(tspStateManagerFeature == null || !tspStateManagerFeature.contains("deadzone_v2") || this.tunningData == null)) {
            if (Feature.getBoolean(Feature.SUPPORT_DEADZONE_ON_GRIP)) {
                Kernel.write(Kernel.TSP_COMMAND_CMD, "set_grip_data,2,0");
            } else {
                String str = Kernel.TSP_COMMAND_CMD;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("set_tunning_data,");
                sb2.append(this.tunningData);
                Kernel.write(str, sb2.toString());
            }
            String status = Kernel.read(Kernel.TSP_COMMAND_STATUS);
            String result = Kernel.read(Kernel.TSP_COMMAND_RESULT);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Result of set_tunning_data : status= ");
            sb3.append(status);
            sb3.append(", result=");
            sb3.append(result);
            LtUtil.log_i(this.CLASS_NAME, "onPause", sb3.toString());
        }
        if (Feature.getBoolean(Feature.SUPPORT_ENABLE_TSP_DEADZONE)) {
            this.mModuleDevice.startTSPTest(ModuleDevice.TSP_CMD_DISABLE_DEAD_ZONE);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mBroadcastReceiver);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24 && (!isPass() || !isPassCross())) {
            setResult(0);
            finish();
        }
        return true;
    }

    private void decideRemote() {
        this.remoteCall = getIntent().getBooleanExtra("RemoteCall", false);
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
        return row == this.mTopmostOfMatrix || row == this.mBottommostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix;
    }

    private boolean isNeededCheck_Hovering(int row, int column) {
        if (row != this.mTopmostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix) {
            return !(row != this.mBottommostOfMatrix || column == this.mLeftmostOfMatrix || column == this.mRightmostOfMatrix) || column == this.mLeftmostOfMatrix + 1 || column == this.mRightmostOfMatrix - 1;
        }
        return true;
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
    public void writeLastTouched() {
        String result = null;
        try {
            String secefsOqcsbfttDirectory = Kernel.getFilePath(Kernel.SECEFS_OQCSBFTT_DIRECTORY);
            StringBuilder sb = new StringBuilder();
            sb.append(secefsOqcsbfttDirectory);
            sb.append("/");
            sb.append(401);
            FileOutputStream out = new FileOutputStream(sb.toString());
            result = this.prefLastTouchPoint.getString("LAST_TOUCHED", "-1");
            if (result == null || result.equals("-1")) {
                out.write("NG".getBytes());
            } else {
                out.write(result.getBytes());
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("IOException : ");
            sb2.append(e2.getMessage());
            LtUtil.log_d(this.CLASS_NAME, "writeLastTouched", sb2.toString());
            if (result != null) {
                result.close();
            }
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
}
