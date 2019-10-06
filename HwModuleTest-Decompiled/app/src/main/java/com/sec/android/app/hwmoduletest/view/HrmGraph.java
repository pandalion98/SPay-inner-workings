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

public class HrmGraph extends View {
    private String CLASS_NAME = "HrmGraph";
    private final float GRAPH_SCALING_VALUE = 0.8f;
    private final int INCREASING_COOR = 3;
    private final int INIT_COOR_X = 10;
    private int INIT_COOR_Y = 0;
    private int LIST_SIZE;
    private float MAX_VALUE_LIST;
    private float MIN_VALUE_LIST;
    private Paint mBaseLinePaint;
    private Context mContext;
    private Paint mDataPaint;
    private PathEffect mEffects;
    private ArrayList<Float> mIR = new ArrayList<>();
    private Paint mIRPaint;
    private Path mPathIR = new Path();
    private Path mPathRED = new Path();
    private ArrayList<Float> mRED = new ArrayList<>();
    private Paint mREDPaint;
    private Paint mRectPaint;
    private int mScreenHeight;
    private int mScreenWidth;
    private Paint mTextPaint;

    public HrmGraph(Context context) {
        super(context);
        init(context);
    }

    public HrmGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public HrmGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        WindowManager mWm = (WindowManager) this.mContext.getSystemService("window");
        this.mScreenWidth = mWm.getDefaultDisplay().getWidth();
        this.mScreenHeight = mWm.getDefaultDisplay().getHeight() / 2;
        this.INIT_COOR_Y = (this.mScreenHeight * 9) / 10;
        this.LIST_SIZE = (this.mScreenWidth - 10) / 3;
        this.mEffects = new CornerPathEffect(10.0f);
        this.mIRPaint = new Paint(1);
        this.mIRPaint.setStyle(Style.STROKE);
        this.mIRPaint.setStrokeWidth(6.0f);
        this.mIRPaint.setColor(-65536);
        this.mIRPaint.setPathEffect(this.mEffects);
        this.mREDPaint = new Paint(1);
        this.mREDPaint.setStyle(Style.STROKE);
        this.mREDPaint.setStrokeWidth(4.0f);
        this.mREDPaint.setColor(-16711936);
        this.mREDPaint.setPathEffect(this.mEffects);
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
        this.mRectPaint = new Paint(1);
        this.mRectPaint.setStyle(Style.STROKE);
        this.mRectPaint.setStrokeWidth(1.0f);
        this.mRectPaint.setColor(-16776961);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float testIR = 0.0f;
        float testRED = 0.0f;
        try {
            testIR = ((Float) this.mIR.get(this.mIR.size() - 1)).floatValue();
            testRED = ((Float) this.mRED.get(this.mRED.size() - 1)).floatValue();
        } catch (Exception e) {
            LtUtil.log_i(this.CLASS_NAME, "onDraw", "get value fail");
            StringBuilder sb = new StringBuilder();
            sb.append("Size of IR, RED : ");
            sb.append(this.mIR.size());
            sb.append(", ");
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("LIST_SIZE value : ");
            sb2.append(this.LIST_SIZE);
            LtUtil.log_i(this.CLASS_NAME, "onDraw", sb2.toString());
        }
        canvas.drawColor(Color.rgb(180, 180, 180));
        canvas.drawText("IR :", 10.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(110.0f, 40.0f, 240.0f, 40.0f, this.mIRPaint);
        canvas.drawText(String.valueOf(testIR), 110.0f, 70.0f, this.mTextPaint);
        canvas.drawText("RED :", 260.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(370.0f, 40.0f, 500.0f, 40.0f, this.mREDPaint);
        canvas.drawText(String.valueOf(testRED), 370.0f, 70.0f, this.mTextPaint);
        canvas.drawPath(this.mPathIR, this.mIRPaint);
        canvas.drawPath(this.mPathRED, this.mREDPaint);
        canvas.drawRect(3.0f, 3.0f, (float) (this.mScreenWidth - 3), (float) (this.mScreenHeight - 3), this.mRectPaint);
    }

    public void addValueIR(float comp) {
        this.mIR.add(Float.valueOf(comp));
        if (this.mIR.size() > this.LIST_SIZE) {
            this.mIR.remove(0);
        }
        checkValue();
        setPathIR();
    }

    public void addValueRED(float comp) {
        this.mRED.add(Float.valueOf(comp));
        if (this.mRED.size() > this.LIST_SIZE) {
            this.mRED.remove(0);
        }
        checkValue();
        setPathRED();
    }

    private void checkValue() {
        this.MIN_VALUE_LIST = 20000.0f;
        this.MAX_VALUE_LIST = 0.0f;
        for (int i = 0; i < this.mIR.size(); i++) {
            if (((Float) this.mIR.get(i)).floatValue() < this.MIN_VALUE_LIST) {
                this.MIN_VALUE_LIST = ((Float) this.mIR.get(i)).floatValue();
            } else if (((Float) this.mIR.get(i)).floatValue() > this.MAX_VALUE_LIST) {
                this.MAX_VALUE_LIST = ((Float) this.mIR.get(i)).floatValue();
            }
        }
        for (int i2 = 1; i2 < this.mRED.size(); i2++) {
            if (((Float) this.mRED.get(i2)).floatValue() < this.MIN_VALUE_LIST) {
                this.MIN_VALUE_LIST = ((Float) this.mRED.get(i2)).floatValue();
            } else if (((Float) this.mRED.get(i2)).floatValue() > this.MAX_VALUE_LIST) {
                this.MAX_VALUE_LIST = ((Float) this.mRED.get(i2)).floatValue();
            }
        }
    }

    private void setPathIR() {
        this.mPathIR.reset();
        this.mPathIR.moveTo(10.0f, ((float) this.INIT_COOR_Y) - ((((((Float) this.mIR.get(0)).floatValue() - this.MIN_VALUE_LIST) / (this.MAX_VALUE_LIST - this.MIN_VALUE_LIST)) * ((float) this.mScreenHeight)) * 0.8f));
        for (int i = 1; i < this.mIR.size(); i++) {
            this.mPathIR.lineTo((float) (10 + (i * 3)), ((float) this.INIT_COOR_Y) - ((((((Float) this.mIR.get(i)).floatValue() - this.MIN_VALUE_LIST) / (this.MAX_VALUE_LIST - this.MIN_VALUE_LIST)) * ((float) this.mScreenHeight)) * 0.8f));
        }
        invalidate();
    }

    private void setPathRED() {
        this.mPathRED.reset();
        this.mPathRED.moveTo(10.0f, ((float) this.INIT_COOR_Y) - ((((((Float) this.mRED.get(0)).floatValue() - this.MIN_VALUE_LIST) / (this.MAX_VALUE_LIST - this.MIN_VALUE_LIST)) * ((float) this.mScreenHeight)) * 0.8f));
        for (int i = 1; i < this.mRED.size(); i++) {
            this.mPathRED.lineTo((float) (10 + (i * 3)), ((float) this.INIT_COOR_Y) - ((((((Float) this.mRED.get(i)).floatValue() - this.MIN_VALUE_LIST) / (this.MAX_VALUE_LIST - this.MIN_VALUE_LIST)) * ((float) this.mScreenHeight)) * 0.8f));
        }
        invalidate();
    }
}
