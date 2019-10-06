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
import java.util.ArrayList;

public class GyroscopeGraph extends View {
    private String CLASS_NAME = "GyroSensorGraph";
    private final int GRAPH_SCALING_COOR = 15;
    private final int INCREASING_COOR = 1;
    private final int INIT_COOR_X = 10;
    private int INIT_COOR_Y = 0;
    private int LIST_SIZE;
    private final float TEXT_SCALING_COOR = 57.32484f;
    private Paint mBaseLinePaint;
    private Context mContext;
    private PathEffect mEffects;
    private Path mPathX = new Path();
    private Path mPathY = new Path();
    private Path mPathZ = new Path();
    private int mScreenHeight;
    private int mScreenWidth;
    private Paint mTextPaint;
    private ArrayList<Float> mValueX = new ArrayList<>();
    private ArrayList<Float> mValueY = new ArrayList<>();
    private ArrayList<Float> mValueZ = new ArrayList<>();
    private Paint mXPaint;
    private Paint mYPaint;
    private Paint mZPaint;

    public GyroscopeGraph(Context context) {
        super(context);
        init(context);
    }

    public GyroscopeGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public GyroscopeGraph(Context context, AttributeSet attrs) {
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
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float textx = 0.0f;
        float texty = 0.0f;
        float textz = 0.0f;
        try {
            textx = ((Float) this.mValueX.get(this.mValueX.size() - 1)).floatValue() * 57.32484f;
            texty = ((Float) this.mValueY.get(this.mValueY.size() - 1)).floatValue() * 57.32484f;
            textz = ((Float) this.mValueZ.get(this.mValueZ.size() - 1)).floatValue() * 57.32484f;
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
        canvas.drawColor(-1);
        canvas.drawText("x :", 10.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(50.0f, 40.0f, 130.0f, 40.0f, this.mXPaint);
        canvas.drawText(String.valueOf(textx2), 50.0f, 70.0f, this.mTextPaint);
        canvas.drawText("y :", 160.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(200.0f, 40.0f, 280.0f, 40.0f, this.mYPaint);
        canvas.drawText(String.valueOf(texty2), 200.0f, 70.0f, this.mTextPaint);
        canvas.drawText("z :", 310.0f, 50.0f, this.mBaseLinePaint);
        canvas.drawLine(350.0f, 40.0f, 430.0f, 40.0f, this.mZPaint);
        canvas.drawText(String.valueOf(textz2), 350.0f, 70.0f, this.mTextPaint);
        canvas.drawLine(10.0f, (float) (this.INIT_COOR_Y - 200), 10.0f, (float) (this.INIT_COOR_Y + 200), this.mBaseLinePaint);
        canvas.drawLine(10.0f, (float) this.INIT_COOR_Y, (float) (this.mScreenWidth - 10), (float) this.INIT_COOR_Y, this.mBaseLinePaint);
        canvas.drawPath(this.mPathX, this.mXPaint);
        canvas.drawPath(this.mPathY, this.mYPaint);
        canvas.drawPath(this.mPathZ, this.mZPaint);
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
        setPath();
    }

    private void setPath() {
        this.mPathX.reset();
        this.mPathY.reset();
        this.mPathZ.reset();
        this.mPathX.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueX.get(0)).floatValue() * 15.0f));
        this.mPathY.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueY.get(0)).floatValue() * 15.0f));
        this.mPathZ.moveTo(10.0f, ((float) this.INIT_COOR_Y) - (((Float) this.mValueZ.get(0)).floatValue() * 15.0f));
        for (int i = 1; i < this.mValueX.size(); i++) {
            this.mPathX.lineTo((float) ((i * 1) + 10), ((float) this.INIT_COOR_Y) - (((Float) this.mValueX.get(i)).floatValue() * 15.0f));
            this.mPathY.lineTo((float) ((i * 1) + 10), ((float) this.INIT_COOR_Y) - (((Float) this.mValueY.get(i)).floatValue() * 15.0f));
            this.mPathZ.lineTo((float) (10 + (i * 1)), ((float) this.INIT_COOR_Y) - (((Float) this.mValueZ.get(i)).floatValue() * 15.0f));
        }
        invalidate();
    }
}
