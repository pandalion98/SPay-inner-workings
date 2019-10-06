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
import com.goodix.cap.fingerprint.utils.TestResultParser;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Spec;
import com.sec.xmldata.support.Support.TestCase;
import java.util.ArrayList;

public class PowerNoiseGraph extends View {
    private static int CALCULATING = 2;
    private static int FAIL = 0;
    private static int PASS = 1;
    private static final float mFlag = 1000.0f;
    private float GRAPH_SCALE_LIMITE = 1.0f;
    private final int GRAPH_SCALING_COOR = 100;
    private final int INCREASING_COOR = 1;
    private final int INIT_COOR_X = 10;
    private final int INIT_COOR_Y = TestResultParser.TEST_TOKEN_AVG_DIFF_VAL;
    private int LIST_SIZE;
    private float SENSOR_SPEC_MAX = 0.0f;
    private float SENSOR_SPEC_MIN = 0.0f;
    private String TAG = "PowerNoiseGraph";
    private final float TEXT_SCALING_COOR = 57.32484f;
    private float VALUE_MAX = 1.0f;
    private float VALUE_SCALING_X = 1.0f;
    private float VALUE_SCALING_Y = 1.0f;
    private float VALUE_SCALING_Z = 1.0f;
    private float graphSizeSetting = 5.0E-4f;
    private boolean isfirstvalue = true;
    private Paint mBaseLinePaint;
    private Context mContext;
    private PathEffect mEffects;
    private Paint mPNTextPaint;
    private Path mPathX = new Path();
    private Path mPathY = new Path();
    private Path mPathZ = new Path();
    private int mScreenWidth;
    private Paint mTextPaint;
    private String[] mValuePowerNoise = {"0.000", "0.000", "0.000"};
    private float[] mValueRawData = new float[3];
    private ArrayList<Float> mValueX = new ArrayList<>();
    private ArrayList<Float> mValueY = new ArrayList<>();
    private ArrayList<Float> mValueZ = new ArrayList<>();
    private Paint mXPaint;
    private Paint mYPaint;
    private Paint mZPaint;
    private int result = 2;

    public PowerNoiseGraph(Context context) {
        super(context);
        init(context);
    }

    public PowerNoiseGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PowerNoiseGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mScreenWidth = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getWidth();
        try {
            this.SENSOR_SPEC_MIN = Float.parseFloat(Spec.getString(Spec.MAGNETIC_SENSOR_SELFTEST_X_MIN));
            this.SENSOR_SPEC_MAX = Float.parseFloat(Spec.getString(Spec.MAGNETIC_SENSOR_SELFTEST_X_MAX));
        } catch (Exception e) {
            this.SENSOR_SPEC_MIN = -2.14748365E9f;
            this.SENSOR_SPEC_MAX = 2.14748365E9f;
        } catch (Throwable th) {
            this.VALUE_MAX = Math.abs(this.SENSOR_SPEC_MIN);
            this.GRAPH_SCALE_LIMITE = 200.0f / this.VALUE_MAX;
            throw th;
        }
        this.VALUE_MAX = Math.abs(this.SENSOR_SPEC_MIN);
        this.GRAPH_SCALE_LIMITE = 200.0f / this.VALUE_MAX;
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
        this.mBaseLinePaint.setStrokeWidth(1.0f);
        this.mBaseLinePaint.setColor(-16777216);
        this.mBaseLinePaint.setTextSize(30.0f);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setStyle(Style.STROKE);
        this.mTextPaint.setStrokeWidth(1.0f);
        this.mTextPaint.setColor(-16777216);
        this.mTextPaint.setTextSize(15.0f);
        this.mPNTextPaint = new Paint(1);
        this.mPNTextPaint.setStyle(Style.FILL_AND_STROKE);
        this.mPNTextPaint.setStrokeWidth(1.0f);
        this.mPNTextPaint.setColor(-16777216);
        this.mPNTextPaint.setTextSize(30.0f);
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
            textx = this.mValueRawData[0];
            texty = this.mValueRawData[1];
            textz = this.mValueRawData[2];
        } catch (Exception e) {
            LtUtil.log_e(this.TAG, "onDraw", "get value fail");
            StringBuilder sb = new StringBuilder();
            sb.append("Size of x=");
            sb.append(this.mValueX.size());
            sb.append(", y=");
            sb.append(this.mValueY.size());
            sb.append(", z=");
            sb.append(this.mValueZ.size());
            LtUtil.log_e(this.TAG, "onDraw", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LIST_SIZE=");
            sb2.append(this.LIST_SIZE);
            LtUtil.log_e(this.TAG, "onDraw", sb2.toString());
        }
        float textx2 = textx;
        float texty2 = texty;
        float textz2 = textz;
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
        canvas2.drawLine(10.0f, 100.0f, 10.0f, 500.0f, this.mBaseLinePaint);
        canvas2.drawLine(10.0f, 300.0f, (float) (this.mScreenWidth - 10), 300.0f, this.mBaseLinePaint);
        canvas2.drawPath(this.mPathX, this.mXPaint);
        canvas2.drawPath(this.mPathY, this.mYPaint);
        canvas2.drawPath(this.mPathZ, this.mZPaint);
        this.mPNTextPaint.setColor(-16777216);
        this.mPNTextPaint.setTextSize(20.0f);
        canvas2.drawText("Raw Data (ADC)", 10.0f, 525.0f, this.mPNTextPaint);
        canvas2.drawText("X :", 10.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText(String.valueOf(this.mValueRawData[0]), 50.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText("Y :", 160.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText(String.valueOf(this.mValueRawData[1]), 200.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText("Z :", 310.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText(String.valueOf(this.mValueRawData[2]), 350.0f, 550.0f, this.mPNTextPaint);
        canvas2.drawText("Power Noise", 10.0f, 600.0f, this.mPNTextPaint);
        canvas2.drawText("X :", 10.0f, 625.0f, this.mPNTextPaint);
        canvas2.drawText(this.mValuePowerNoise[0], 50.0f, 625.0f, this.mPNTextPaint);
        canvas2.drawText("Y :", 160.0f, 625.0f, this.mPNTextPaint);
        canvas2.drawText(this.mValuePowerNoise[1], 200.0f, 625.0f, this.mPNTextPaint);
        canvas2.drawText("Z :", 310.0f, 625.0f, this.mPNTextPaint);
        canvas2.drawText(this.mValuePowerNoise[2], 350.0f, 625.0f, this.mPNTextPaint);
    }

    public void addValueWithPowerNoise(float x, float y, float z, String[] power_noise, float[] raw_data, int result2) {
        if (this.isfirstvalue) {
            this.VALUE_SCALING_X = x;
            this.VALUE_SCALING_Y = y;
            this.VALUE_SCALING_Z = z;
        } else {
            this.VALUE_SCALING_X = (this.VALUE_SCALING_X + x) / 2.0f;
            this.VALUE_SCALING_Y = (this.VALUE_SCALING_Y + y) / 2.0f;
            this.VALUE_SCALING_Z = (this.VALUE_SCALING_Z + z) / 2.0f;
        }
        this.isfirstvalue = false;
        this.mValueX.add(Float.valueOf(x));
        this.mValueY.add(Float.valueOf(y));
        this.mValueZ.add(Float.valueOf(z));
        if (this.mValueX.size() > this.LIST_SIZE) {
            this.mValueX.remove(0);
            this.mValueY.remove(0);
            this.mValueZ.remove(0);
        }
        this.mValueRawData = raw_data;
        if (result2 != CALCULATING) {
            this.mValuePowerNoise = power_noise;
        }
        this.result = result2;
        setPath();
    }

    private void setPath() {
        this.mPathX.reset();
        this.mPathY.reset();
        this.mPathZ.reset();
        this.mPathX.moveTo(10.0f, 300.0f);
        this.mPathY.moveTo(10.0f, 300.0f);
        this.mPathZ.moveTo(10.0f, 300.0f);
        for (int i = 1; i < this.mValueX.size(); i++) {
            this.mPathX.lineTo((float) ((i * 1) + 10), 300.0f - (checkMaximun(((Float) this.mValueX.get(i)).floatValue()) * this.GRAPH_SCALE_LIMITE));
            this.mPathY.lineTo((float) ((i * 1) + 10), 300.0f - (checkMaximun(((Float) this.mValueY.get(i)).floatValue()) * this.GRAPH_SCALE_LIMITE));
            this.mPathZ.lineTo((float) (10 + (i * 1)), 300.0f - (checkMaximun(((Float) this.mValueZ.get(i)).floatValue()) * this.GRAPH_SCALE_LIMITE));
        }
        invalidate();
    }

    private float checkMaximun(float value) {
        if (value > this.SENSOR_SPEC_MAX) {
            return this.SENSOR_SPEC_MAX;
        }
        if (value < this.SENSOR_SPEC_MIN) {
            return this.SENSOR_SPEC_MIN;
        }
        return value;
    }

    private void setSpecGraphScale() {
        String GraphYRange = TestCase.getString(TestCase.MAGNETIC_POWERNOISE_GRAPH_Y_RANGE);
        if (GraphYRange != null && !GraphYRange.equals("")) {
            this.GRAPH_SCALE_LIMITE = 200.0f / ((float) Integer.parseInt(GraphYRange));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("GRAPH_SCALE_LIMITE :");
        sb.append(String.valueOf(this.GRAPH_SCALE_LIMITE));
        LtUtil.log_d(this.TAG, "setSpecGraphScale", sb.toString());
    }
}
