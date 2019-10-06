package com.sec.android.app.hwmoduletest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import java.lang.reflect.Array;

public class FingerPrintResultView_touch extends View {
    private String CLASS_NAME = "FingerPrintResultView";
    private float GRAPH_MAX_VALUE = 255.0f;
    private float GRAPH_X_SCALING = 3.0f;
    private float GRAPH_Y_SCALING = 0.15f;
    private final int INIT_COOR_X = 50;
    private int INIT_COOR_Y = 0;
    private int SCLAING = 1;
    private final int TEXTSIZE_RATIO = 40;
    private int mCol = 3;
    private int mColWidth;
    private Context mContext;
    private Paint mLinePaint;
    private Paint mNumberPaint;
    private Rect mRect = new Rect();
    private Paint mResultFailPaint;
    private Paint mResultPassPaint;
    private int mRow = 3;
    private int mRowHeight;
    private int mScreenHeight;
    private int mScreenHeight_Max = 550;
    private int mScreenTitleHeight = 500;
    private int mScreenTitleHeight_Max = TestResultParser.TEST_TOKEN_GET_DR_TIMESTAMP_TIME;
    private int mScreenWidth;
    public String mSerialNumber = "";
    private int mSpecRow = 6;
    private int mSpecRowHeight;
    private Paint mTextCenterPaint;
    private Paint mTextPaint;
    private String[][] mTexts = {new String[]{null}};
    private final int paddingHeight = 10;
    private final int paddingWidth = 10;

    public FingerPrintResultView_touch(Context context) {
        super(context);
        init(context);
    }

    public FingerPrintResultView_touch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public FingerPrintResultView_touch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mNumberPaint = new Paint(1);
        this.mNumberPaint.setStyle(Style.FILL_AND_STROKE);
        this.mNumberPaint.setStrokeWidth(1.0f);
        this.mNumberPaint.setColor(-16777216);
        this.mNumberPaint.setTextAlign(Align.RIGHT);
        this.mNumberPaint.setTextSize(40.0f);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setStyle(Style.FILL_AND_STROKE);
        this.mTextPaint.setStrokeWidth(1.0f);
        this.mTextPaint.setColor(-16777216);
        this.mTextPaint.setTextSize(40.0f);
        this.mResultPassPaint = new Paint(1);
        this.mResultPassPaint.setStyle(Style.FILL_AND_STROKE);
        this.mResultPassPaint.setStrokeWidth(1.0f);
        this.mResultPassPaint.setColor(-16776961);
        this.mResultPassPaint.setTextSize(50.0f);
        this.mResultPassPaint.setTextAlign(Align.CENTER);
        this.mResultFailPaint = new Paint(1);
        this.mResultFailPaint.setStyle(Style.FILL_AND_STROKE);
        this.mResultFailPaint.setStrokeWidth(1.0f);
        this.mResultFailPaint.setColor(-65536);
        this.mResultFailPaint.setTextSize(50.0f);
        this.mResultFailPaint.setTextAlign(Align.CENTER);
        this.mLinePaint = new Paint(1);
        this.mLinePaint.setStyle(Style.STROKE);
        this.mLinePaint.setStrokeWidth(1.0f);
        this.mLinePaint.setColor(-7829368);
        this.mTextCenterPaint = new Paint(1);
        this.mTextCenterPaint.setStyle(Style.FILL_AND_STROKE);
        this.mTextCenterPaint.setStrokeWidth(1.0f);
        this.mTextCenterPaint.setColor(-16777216);
        this.mTextCenterPaint.setTextAlign(Align.CENTER);
        this.mTextCenterPaint.setTextSize(50.0f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i = 0;
        while (i < this.mRow) {
            int j = 0;
            while (j < this.mCol) {
                if (this.mTexts[i][j] != null) {
                    int k = j + 1;
                    while (k < this.mCol && this.mTexts[i][k] == null) {
                        k++;
                    }
                    this.mRect.top = (this.mRowHeight * i) + 10;
                    this.mRect.bottom = ((i + 1) * this.mRowHeight) + 10;
                    this.mRect.left = (this.mColWidth * j) + 10;
                    this.mRect.right = 10 + (this.mColWidth * k);
                    canvas.drawRect(this.mRect, this.mLinePaint);
                    if ("PASS".equals(this.mTexts[i][j])) {
                        canvas.drawText(this.mTexts[i][j], (float) (this.mRect.left + ((this.mRect.right - this.mRect.left) / 2)), (float) (this.mRect.top + ((this.mRowHeight / 4) * 3)), this.mResultPassPaint);
                    } else if ("FAIL".equals(this.mTexts[i][j])) {
                        canvas.drawText(this.mTexts[i][j], (float) (this.mRect.left + ((this.mRect.right - this.mRect.left) / 2)), (float) (this.mRect.top + ((this.mRowHeight / 4) * 3)), this.mResultFailPaint);
                    } else if (isNumeric(this.mTexts[i][j])) {
                        canvas.drawText(this.mTexts[i][j], (float) (this.mRect.left + (((this.mRect.right - this.mRect.left) / 4) * 3)), (float) (this.mRect.top + ((this.mRowHeight / 4) * 3)), this.mNumberPaint);
                    } else {
                        canvas.drawText(this.mTexts[i][j], (float) (this.mRect.left + ((this.mRect.right - this.mRect.left) / 2)), (float) (this.mRect.top + ((this.mRowHeight / 4) * 3)), this.mTextCenterPaint);
                    }
                    j = k - 1;
                }
                j++;
            }
            i++;
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public void setRowCol(int row, int col) {
        this.mRow = row;
        this.mCol = col;
        this.mTexts = null;
        this.mTexts = (String[][]) Array.newInstance(String.class, new int[]{this.mRow, this.mCol});
        this.mRowHeight = this.mScreenHeight_Max / this.mRow;
        this.mColWidth = this.mScreenWidth / this.mCol;
        for (int i = 0; i < this.mRow; i++) {
            for (int j = 0; j < this.mCol; j++) {
                this.mTexts[i][j] = null;
            }
        }
    }

    public void setCanvasSize(int w, int h) {
        this.mScreenWidth = w - 20;
        this.mScreenHeight_Max = h - 20;
        int textSize = this.mScreenWidth / 40;
        this.mTextPaint.setTextSize((float) textSize);
        this.mTextCenterPaint.setTextSize((float) textSize);
        this.mNumberPaint.setTextSize((float) textSize);
        this.mResultPassPaint.setTextSize((float) (textSize + 10));
        this.mResultFailPaint.setTextSize((float) (textSize + 10));
    }

    public void setTextSize(int size) {
        this.mTextPaint.setTextSize((float) size);
        this.mNumberPaint.setTextSize((float) size);
        this.mResultPassPaint.setTextSize((float) (size + 10));
        this.mResultFailPaint.setTextSize((float) (size + 10));
    }

    public void setTextData(int row, int col, String text) {
        if (row < this.mRow && col < this.mCol) {
            this.mTexts[row][col] = text;
        }
    }
}
