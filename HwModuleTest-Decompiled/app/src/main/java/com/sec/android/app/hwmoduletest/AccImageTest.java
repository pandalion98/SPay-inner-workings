package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.view.View;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import java.util.Timer;
import java.util.TimerTask;

public class AccImageTest extends BaseActivity {
    private static final String CLASS_NAME = "AccImageTest";
    private final byte MSG_UPDATE_SENSOR_UI = 1;
    private final int TIMER_TASK_PERIOD = 100;
    AcceSensorTask mAcceSensorTask;
    Sensor mAccelerometerSensor;
    /* access modifiers changed from: private */
    public TextView mAngleText;
    private int mAutoRotateState;
    private int mAutoRotateState2;
    private View mBackground;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                AccImageTest.this.mRawDataText.setText(AccImageTest.this.mAcceSensorTask.getRawdataString());
                AccImageTest.this.mAngleText.setText(AccImageTest.this.mAcceSensorTask.getAngleString());
            }
        }
    };
    /* access modifiers changed from: private */
    public TextView mRawDataText;
    SensorManager mSensorManager;
    Timer mTimer;

    private class AcceSensorTask extends TimerTask implements SensorEventListener {
        private float[] mAcceSensorValues;
        private int[] mAngle;
        private boolean mIsRunningTask;
        private int[] mRawData;

        private AcceSensorTask() {
            this.mIsRunningTask = false;
            this.mAcceSensorValues = new float[3];
            this.mRawData = new int[3];
            this.mAngle = new int[3];
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 1) {
                this.mAcceSensorValues = (float[]) event.values.clone();
                StringBuilder sb = new StringBuilder();
                sb.append("value[0]=");
                sb.append(String.valueOf(this.mAcceSensorValues[0]));
                sb.append(", value[1]=");
                sb.append(String.valueOf(this.mAcceSensorValues[1]));
                sb.append(", value[2]=");
                sb.append(String.valueOf(this.mAcceSensorValues[2]));
                LtUtil.log_d(AccImageTest.CLASS_NAME, "onSensorChanged", sb.toString());
            }
        }

        public void run() {
            if (this.mIsRunningTask) {
                readToAccelerometerSensor();
                AccImageTest.this.mHandler.sendEmptyMessage(1);
            }
        }

        /* access modifiers changed from: private */
        public void resume() {
            this.mIsRunningTask = true;
        }

        /* access modifiers changed from: private */
        public void pause() {
            this.mIsRunningTask = false;
        }

        private void readToAccelerometerSensor() {
            try {
                String[] rawDatas = Kernel.read(Kernel.ACCEL_SENSOR_RAW).trim().split(",");
                this.mRawData[0] = Integer.parseInt(rawDatas[0].trim());
                this.mRawData[1] = Integer.parseInt(rawDatas[1].trim());
                this.mRawData[2] = Integer.parseInt(rawDatas[2].trim());
                StringBuilder sb = new StringBuilder();
                sb.append("raw[0]=");
                sb.append(this.mRawData[0]);
                sb.append(", raw[1]=");
                sb.append(this.mRawData[1]);
                sb.append(", raw[2]=");
                sb.append(this.mRawData[2]);
                LtUtil.log_d(AccImageTest.CLASS_NAME, "readToAccelerometerSensor", sb.toString());
                float realg = (float) Math.sqrt((double) ((this.mRawData[0] * this.mRawData[0]) + (this.mRawData[1] * this.mRawData[1]) + (this.mRawData[2] * this.mRawData[2])));
                this.mAngle[0] = ((int) (((float) Math.asin((double) (((float) this.mRawData[0]) / realg))) * 57.29578f)) * -1;
                this.mAngle[1] = ((int) (((float) Math.asin((double) (((float) this.mRawData[1]) / realg))) * 57.29578f)) * -1;
                this.mAngle[2] = ((int) (((float) Math.acos((double) (((float) this.mRawData[2]) / realg))) * 57.29578f)) - 90;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("angle[0]=");
                sb2.append(this.mAngle[0]);
                sb2.append(", angle[1]=");
                sb2.append(this.mAngle[1]);
                sb2.append(", angle[2]=");
                sb2.append(this.mAngle[2]);
                LtUtil.log_d(AccImageTest.CLASS_NAME, "readToAccelerometerSensor", sb2.toString());
            } catch (Exception e) {
                e.printStackTrace();
                this.mRawData[0] = (int) ((((double) this.mAcceSensorValues[0]) * 1024.0d) / 9.81d);
                this.mRawData[1] = (int) ((((double) this.mAcceSensorValues[1]) * 1024.0d) / 9.81d);
                this.mRawData[2] = (int) ((((double) this.mAcceSensorValues[2]) * 1024.0d) / 9.81d);
                int[] iArr = this.mAngle;
                int[] iArr2 = this.mAngle;
                this.mAngle[2] = 0;
                iArr2[1] = 0;
                iArr[0] = 0;
            }
        }

        /* access modifiers changed from: private */
        public String getRawdataString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ACC Raw Data - x: ");
            sb.append(this.mRawData[0]);
            sb.append(", y: ");
            sb.append(this.mRawData[1]);
            sb.append(", z: ");
            sb.append(this.mRawData[2]);
            String res = sb.toString();
            LtUtil.log_d(AccImageTest.CLASS_NAME, "getRawdataString", res);
            return res;
        }

        /* access modifiers changed from: private */
        public String getAngleString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Angle - x: ");
            sb.append(this.mAngle[0] * -1);
            sb.append(", y: ");
            sb.append(this.mAngle[1] * -1);
            sb.append(", z: ");
            sb.append(this.mAngle[2] * -1);
            String res = sb.toString();
            LtUtil.log_d(AccImageTest.CLASS_NAME, "getAngleString", res);
            return res;
        }
    }

    public AccImageTest() {
        super(CLASS_NAME);
    }

    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(CLASS_NAME, "onCreate", "");
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.acc_image_test);
        this.mBackground = findViewById(C0268R.C0269id.background);
        this.mRawDataText = (TextView) findViewById(C0268R.C0269id.raw_data);
        this.mAngleText = (TextView) findViewById(C0268R.C0269id.angle);
        this.mTimer = new Timer();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mAccelerometerSensor = this.mSensorManager.getDefaultSensor(1);
        this.mAcceSensorTask = new AcceSensorTask();
        this.mTimer.schedule(this.mAcceSensorTask, 0, 100);
        this.mBackground.setBackgroundResource(C0268R.drawable.acc_test_image);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LtUtil.log_i(CLASS_NAME, "onResume", "");
        super.onResume();
        this.mSensorManager.registerListener(this.mAcceSensorTask, this.mAccelerometerSensor, 0);
        this.mAcceSensorTask.resume();
        this.mAutoRotateState = System.getInt(getContentResolver(), "accelerometer_rotation", 0);
        System.putInt(getContentResolver(), "accelerometer_rotation", 1);
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            this.mAutoRotateState2 = System.getInt(getContentResolver(), "accelerometer_rotation_second", 0);
            System.putInt(getContentResolver(), "accelerometer_rotation_second", 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_i(CLASS_NAME, "onPause", "");
        super.onPause();
        this.mAcceSensorTask.pause();
        this.mSensorManager.unregisterListener(this.mAcceSensorTask);
        System.putInt(getContentResolver(), "accelerometer_rotation", this.mAutoRotateState);
        if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER)) {
            System.putInt(getContentResolver(), "accelerometer_rotation_second", this.mAutoRotateState2);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_i(CLASS_NAME, "onDestroy", "");
        super.onDestroy();
        this.mTimer.cancel();
    }
}
