package com.sec.android.app.hwmoduletest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.SensorTestMenu;
import com.sec.xmldata.support.Support.TestCase;
import java.util.ArrayList;

public class AccGraph extends View {
    private String CLASS_NAME = "AccSensorGraph";
    private final boolean DEBUG = true;
    private float GRAPH_SCALING_COOR;
    private final int INCREASING_COOR = 1;
    private final int INIT_COOR_X = 10;
    private int INIT_COOR_Y = 0;
    private int LIST_SIZE;
    private final float TEXT_SCALING_COOR = 57.32484f;
    private Paint mBaseLinePaint;
    private Context mContext;
    private Paint mDataPaint;
    private PathEffect mEffects;
    private Path mPathX = new Path();
    private Path mPathY = new Path();
    private Path mPathZ = new Path();
    private float[] mRawDataMax = {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
    private float[] mRawDataMin = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
    private String mRawDataRange;
    private int mScreenHeight;
    private int mScreenWidth;
    private Paint mTextPaint;
    private ArrayList<Float> mValueX = new ArrayList<>();
    private ArrayList<Float> mValueY = new ArrayList<>();
    private ArrayList<Float> mValueZ = new ArrayList<>();
    private Paint mXPaint;
    private Paint mYPaint;
    private Paint mZPaint;
    private float rate;

    public AccGraph(Context context) {
        super(context);
        init(context);
    }

    public AccGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AccGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.rate = SensorTestMenu.getUIRate("Accelerometer");
        StringBuilder sb = new StringBuilder();
        sb.append("rate : ");
        sb.append(this.rate);
        LtUtil.log_i(this.CLASS_NAME, "init", sb.toString());
        WindowManager mWm = (WindowManager) this.mContext.getSystemService("window");
        this.mScreenWidth = mWm.getDefaultDisplay().getWidth();
        this.mScreenHeight = mWm.getDefaultDisplay().getHeight();
        this.INIT_COOR_Y = this.mScreenHeight / 2;
        this.LIST_SIZE = (this.mScreenWidth - 10) / 1;
        this.mEffects = new CornerPathEffect(10.0f);
        this.mXPaint = new Paint(1);
        this.mXPaint.setStyle(Style.STROKE);
        this.mXPaint.setStrokeWidth(6.0f);
        this.mXPaint.setColor(-65536);
        this.mXPaint.setPathEffect(this.mEffects);
        this.mYPaint = new Paint(1);
        this.mYPaint.setStyle(Style.STROKE);
        this.mYPaint.setStrokeWidth(4.0f);
        this.mYPaint.setColor(-16711936);
        this.mYPaint.setPathEffect(this.mEffects);
        this.mZPaint = new Paint(1);
        this.mZPaint.setStyle(Style.STROKE);
        this.mZPaint.setStrokeWidth(2.0f);
        this.mZPaint.setColor(-16776961);
        this.mZPaint.setPathEffect(this.mEffects);
        this.mBaseLinePaint = new Paint(1);
        this.mBaseLinePaint.setStyle(Style.STROKE);
        if (this.rate == 1.0f) {
            this.mBaseLinePaint.setStrokeWidth(1.0f);
        } else {
            this.mBaseLinePaint.setStrokeWidth(2.0f);
        }
        this.mBaseLinePaint.setColor(-16777216);
        this.mBaseLinePaint.setTextSize(30.0f);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setStyle(Style.STROKE);
        if (this.rate == 1.0f) {
            this.mTextPaint.setStrokeWidth(1.0f);
        } else {
            this.mTextPaint.setStrokeWidth(2.0f);
        }
        this.mTextPaint.setColor(-16777216);
        this.mTextPaint.setTextSize(15.0f);
        this.mDataPaint = new Paint(1);
        this.mDataPaint.setStyle(Style.FILL_AND_STROKE);
        this.mDataPaint.setStrokeWidth(1.0f);
        this.mDataPaint.setColor(-16777216);
        this.mDataPaint.setTextSize(20.0f);
        setSpecGraphScale();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        float textx = 0.0f;
        float texty = 0.0f;
        float textz = 0.0f;
        try {
            textx = ((Float) this.mValueX.get(this.mValueX.size() - 1)).floatValue();
            texty = ((Float) this.mValueY.get(this.mValueY.size() - 1)).floatValue();
            textz = ((Float) this.mValueZ.get(this.mValueZ.size() - 1)).floatValue();
        } catch (Exception e) {
            LtUtil.log_i(this.CLASS_NAME, "onDraw", "get value fail");
            StringBuilder sb = new StringBuilder();
            sb.append("Size of X, Y, Z : ");
            sb.append(this.mValueX.size());
            sb.append(", ");
            sb.append(this.mValueY.size());
            sb.append(", ");
            sb.append(this.mValueZ.size());
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LIST_SIZE value : ");
            sb2.append(this.LIST_SIZE);
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb2.toString());
        }
        float textx2 = textx;
        float texty2 = texty;
        float textz2 = textz;
        canvas2.scale(this.rate, this.rate);
        canvas2.drawColor(-1);
        canvas2.drawText("x :", 10.0f, 50.0f, this.mBaseLinePaint);
        canvas2.drawLine(50.0f, 40.0f, 130.0f, 40.0f, this.mXPaint);
        canvas2.drawText(String.valueOf(textx2), 50.0f, 70.0f, this.mTextPaint);
        canvas2.drawText("y :", 160.0f, 50.0f, this.mBaseLinePaint);
        canvas2.drawLine(200.0f, 40.0f, 280.0f, 40.0f, this.mYPaint);
        canvas2.drawText(String.valueOf(texty2), 200.0f, 70.0f, this.mTextPaint);
        canvas2.drawText("z :", 310.0f, 50.0f, this.mBaseLinePaint);
        canvas2.drawLine(350.0f, 40.0f, 430.0f, 40.0f, this.mZPaint);
        canvas2.drawText(String.valueOf(textz2), 350.0f, 70.0f, this.mTextPaint);
        canvas2.drawLine(10.0f, (float) (this.INIT_COOR_Y - 200), 10.0f, (float) (this.INIT_COOR_Y + 200), this.mBaseLinePaint);
        canvas2.drawLine(10.0f, (float) this.INIT_COOR_Y, (float) (this.mScreenWidth - 10), (float) this.INIT_COOR_Y, this.mBaseLinePaint);
        canvas2.drawPath(this.mPathX, this.mXPaint);
        canvas2.drawPath(this.mPathY, this.mYPaint);
        canvas2.drawPath(this.mPathZ, this.mZPaint);
        canvas2.drawText("Raw Data", 10.0f, (float) (this.INIT_COOR_Y + 200 + 25), this.mDataPaint);
        canvas2.drawText("MIN", 10.0f, (float) (this.INIT_COOR_Y + 200 + 50), this.mDataPaint);
        canvas2.drawText("X :", 10.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMin[0]), 50.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText("Y :", 160.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMin[1]), 200.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText("Z :", 310.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMin[2]), 350.0f, (float) (this.INIT_COOR_Y + 200 + 75), this.mDataPaint);
        canvas2.drawText("MAX", 10.0f, (float) (this.INIT_COOR_Y + 200 + 125), this.mDataPaint);
        canvas2.drawText("X :", 10.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMax[0]), 50.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
        canvas2.drawText("Y :", 160.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMax[1]), 200.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
        canvas2.drawText("Z :", 310.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
        canvas2.drawText(String.valueOf(this.mRawDataMax[2]), 350.0f, (float) (this.INIT_COOR_Y + 200 + 150), this.mDataPaint);
    }

    public void addValue(float x, float y, float z) {
        this.mValueX.add(Float.valueOf(x));
        this.mValueY.add(Float.valueOf(y));
        this.mValueZ.add(Float.valueOf(z));
        if (this.mValueX.size() > this.LIST_SIZE) {
            this.mValueX.remove(0);
            this.mValueY.remove(0);
            this.mValueZ.remove(0);
        }
        calRawData();
        setPath();
    }

    private void calRawData() {
        float tempX = Float.NEGATIVE_INFINITY;
        float tempY = Float.NEGATIVE_INFINITY;
        float tempZ = Float.NEGATIVE_INFINITY;
        try {
            tempX = ((Float) this.mValueX.get(this.mValueX.size() - 1)).floatValue();
            tempY = ((Float) this.mValueY.get(this.mValueY.size() - 1)).floatValue();
            tempZ = ((Float) this.mValueZ.get(this.mValueZ.size() - 1)).floatValue();
        } catch (Exception e) {
            LtUtil.log_i(this.CLASS_NAME, "calRawData", "get value fail");
            StringBuilder sb = new StringBuilder();
            sb.append("Size of X, Y, Z : ");
            sb.append(this.mValueX.size());
            sb.append(", ");
            sb.append(this.mValueY.size());
            sb.append(", ");
            sb.append(this.mValueZ.size());
            LtUtil.log_i(this.CLASS_NAME, "calRawData", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LIST_SIZE value : ");
            sb2.append(this.LIST_SIZE);
            LtUtil.log_i(this.CLASS_NAME, "calRawData", sb2.toString());
        }
        if (tempX < this.mRawDataMin[0]) {
            this.mRawDataMin[0] = tempX;
        }
        if (tempY < this.mRawDataMin[1]) {
            this.mRawDataMin[1] = tempY;
        }
        if (tempZ < this.mRawDataMin[2]) {
            this.mRawDataMin[2] = tempZ;
        }
        if (tempX > this.mRawDataMax[0]) {
            this.mRawDataMax[0] = tempX;
        }
        if (tempY > this.mRawDataMax[1]) {
            this.mRawDataMax[1] = tempY;
        }
        if (tempZ > this.mRawDataMax[2]) {
            this.mRawDataMax[2] = tempZ;
        }
    }

    private void setPath() {
        this.mPathX.reset();
        this.mPathY.reset();
        this.mPathZ.reset();
        this.mPathX.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueX.get(0)).floatValue() * this.GRAPH_SCALING_COOR));
        this.mPathY.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueY.get(0)).floatValue() * this.GRAPH_SCALING_COOR));
        this.mPathZ.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueZ.get(0)).floatValue() * this.GRAPH_SCALING_COOR));
        for (int i = 1; i < this.mValueX.size(); i++) {
            this.mPathX.lineTo((float) ((i * 1) + 10), ((float) this.INIT_COOR_Y) - (((Float) this.mValueX.get(i)).floatValue() * this.GRAPH_SCALING_COOR));
            this.mPathY.lineTo((float) ((i * 1) + 10), ((float) this.INIT_COOR_Y) - (((Float) this.mValueY.get(i)).floatValue() * this.GRAPH_SCALING_COOR));
            this.mPathZ.lineTo((float) (10 + (i * 1)), ((float) this.INIT_COOR_Y) - (((Float) this.mValueZ.get(i)).floatValue() * this.GRAPH_SCALING_COOR));
        }
        invalidate();
    }

    private void setSpecGraphScale() {
        this.mRawDataRange = TestCase.getString(TestCase.ACCSENSOR_DATA_RANGE);
        if (this.mRawDataRange == null || this.mRawDataRange.equals("")) {
            this.GRAPH_SCALING_COOR = 0.005f;
        } else {
            this.GRAPH_SCALING_COOR = 200.0f / ((float) Integer.parseInt(this.mRawDataRange));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("GRAPH_SCALING_COOR :");
        sb.append(String.valueOf(this.GRAPH_SCALING_COOR));
        LtUtil.log_d(this.CLASS_NAME, "setSpecGraphScale", sb.toString());
    }
}
