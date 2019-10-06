package com.sec.android.app.hwmoduletest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.ArrayList;

public class TempHumidGraph extends View {
    private String CLASS_NAME = "TempHumidGraph";
    private final float GRAPH_SCALING_COOR = 8.0f;
    private final int INCREASING_COOR = 1;
    private final int INIT_COOR_X = 10;
    private int INIT_COOR_Y = 0;
    private int LIST_SIZE;
    private final float TEXT_SCALING_COOR = 57.32484f;
    private Paint mBaseLinePaint;
    private Context mContext;
    private Paint mDataPaint;
    private PathEffect mEffects;
    private ArrayList<Float> mHumid = new ArrayList<>();
    private Paint mHumidPaint;
    private Path mPathHumid = new Path();
    private Path mPathTemp = new Path();
    private float[] mRawDataMax = {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
    private float[] mRawDataMin = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
    private int mScreenHeight;
    private int mScreenWidth;
    private ArrayList<Float> mTemp = new ArrayList<>();
    private Paint mTempPaint;
    private Paint mTextPaint;

    public TempHumidGraph(Context context) {
        super(context);
        init(context);
    }

    public TempHumidGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TempHumidGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        WindowManager mWm = (WindowManager) this.mContext.getSystemService("window");
        this.mScreenWidth = mWm.getDefaultDisplay().getWidth();
        this.mScreenHeight = mWm.getDefaultDisplay().getHeight();
        this.INIT_COOR_Y = this.mScreenHeight / 2;
        this.LIST_SIZE = (this.mScreenWidth - 10) / 1;
        this.mEffects = new CornerPathEffect(10.0f);
        this.mTempPaint = new Paint(1);
        this.mTempPaint.setStyle(Style.STROKE);
        this.mTempPaint.setStrokeWidth(6.0f);
        this.mTempPaint.setColor(-65536);
        this.mTempPaint.setPathEffect(this.mEffects);
        this.mHumidPaint = new Paint(1);
        this.mHumidPaint.setStyle(Style.STROKE);
        this.mHumidPaint.setStrokeWidth(4.0f);
        this.mHumidPaint.setColor(-16711936);
        this.mHumidPaint.setPathEffect(this.mEffects);
        this.mBaseLinePaint = new Paint(1);
        this.mBaseLinePaint.setStyle(Style.STROKE);
        this.mBaseLinePaint.setStrokeWidth(1.0f);
        this.mBaseLinePaint.setColor(-16777216);
        this.mBaseLinePaint.setTextSize(30.0f);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setStyle(Style.STROKE);
        this.mTextPaint.setStrokeWidth(1.0f);
        this.mTextPaint.setColor(-16777216);
        this.mTextPaint.setTextSize(30.0f);
        this.mDataPaint = new Paint(1);
        this.mDataPaint.setStyle(Style.FILL_AND_STROKE);
        this.mDataPaint.setStrokeWidth(1.0f);
        this.mDataPaint.setColor(-16777216);
        this.mDataPaint.setTextSize(20.0f);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float testTemp = 0.0f;
        float testHumid = 0.0f;
        try {
            testTemp = ((Float) this.mTemp.get(this.mTemp.size() - 1)).floatValue();
            testHumid = ((Float) this.mHumid.get(this.mHumid.size() - 1)).floatValue();
        } catch (Exception e) {
            LtUtil.log_i(this.CLASS_NAME, "onDraw", "get value fail");
            StringBuilder sb = new StringBuilder();
            sb.append("Size of Temp, Humid : ");
            sb.append(this.mTemp.size());
            sb.append(", ");
            sb.append(this.mHumid.size());
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LIST_SIZE value : ");
            sb2.append(this.LIST_SIZE);
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb2.toString());
        }
        canvas.drawColor(Color.rgb(180, 180, 180));
        canvas.drawText("Temp :", 10.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(110.0f, 40.0f, 240.0f, 40.0f, this.mTempPaint);
        canvas.drawText(String.valueOf(testTemp), 110.0f, 70.0f, this.mTextPaint);
        canvas.drawText("Humid :", 260.0f, 50.0f, this.mBaseLinePaint);
        Canvas canvas2 = canvas;
        canvas2.drawLine(370.0f, 40.0f, 500.0f, 40.0f, this.mHumidPaint);
        canvas.drawText(String.valueOf(testHumid), 370.0f, 70.0f, this.mTextPaint);
        canvas2.drawLine(10.0f, (float) (this.INIT_COOR_Y - 200), 10.0f, (float) (this.INIT_COOR_Y + 200), this.mBaseLinePaint);
        canvas.drawLine(10.0f, (float) this.INIT_COOR_Y, (float) (this.mScreenWidth - 10), (float) this.INIT_COOR_Y, this.mBaseLinePaint);
        canvas.drawPath(this.mPathTemp, this.mTempPaint);
        canvas.drawPath(this.mPathHumid, this.mHumidPaint);
    }

    public void addValueTemp(float comp, float raw) {
        this.mTemp.add(Float.valueOf(comp));
        if (this.mTemp.size() > this.LIST_SIZE) {
            this.mTemp.remove(0);
        }
        setPathTemp();
    }

    public void addValueHumid(float comp, float raw) {
        this.mHumid.add(Float.valueOf(comp));
        if (this.mHumid.size() > this.LIST_SIZE) {
            this.mHumid.remove(0);
        }
        setPathHumid();
    }

    private void setPathTemp() {
        this.mPathTemp.reset();
        this.mPathTemp.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mTemp.get(0)).floatValue() * 8.0f));
        for (int i = 1; i < this.mTemp.size(); i++) {
            this.mPathTemp.lineTo((float) (10 + (i * 1)), ((float) this.INIT_COOR_Y) - (((Float) this.mTemp.get(i)).floatValue() * 8.0f));
        }
        invalidate();
    }

    private void setPathHumid() {
        this.mPathHumid.reset();
        this.mPathHumid.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mHumid.get(0)).floatValue() * 8.0f));
        for (int i = 1; i < this.mTemp.size(); i++) {
            this.mPathHumid.lineTo((float) (10 + (i * 1)), ((float) this.INIT_COOR_Y) - (((Float) this.mHumid.get(i)).floatValue() * 8.0f));
        }
        invalidate();
    }
}
