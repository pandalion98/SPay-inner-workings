package com.sec.android.app.hwmoduletest.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.Timer;
import java.util.TimerTask;

public class SensorHRM extends SensorBase implements SensorEventListener {
    private final String CLASS_NAME = "SensorHRM";
    private final boolean DEBUG = true;
    private boolean isHRMMGROn = false;
    private float[] mBuffer_SensorValue_HRM = null;
    private Context mContext;
    private Sensor mHRMSensor = null;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager = null;
    private String[] mSensorValues = null;
    private Timer mTimer = null;

    public SensorHRM(Context mContext2) {
        LtUtil.log_d("SensorHRM", "SensorHRM", "Constructor");
        this.mContext = mContext2;
    }

    public void SensorOn(SensorListener mSensorListener2, int[] sensorID, int interval) {
        this.mSensorListener = mSensorListener2;
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        if (this.mSensorManager != null) {
            for (int i = 0; i < sensorID.length; i++) {
                if (sensorID[i] != 13) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unregistered: ");
                    sb.append(sensorID[i]);
                    LtUtil.log_d("SensorHRM", "SensorOn", sb.toString());
                } else if (this.mHRMSensor == null) {
                    this.mHRMSensor = this.mSensorManager.getDefaultSensor(65562);
                    this.mSensorManager.registerListener(this, this.mHRMSensor, 2);
                    LtUtil.log_d("SensorHRM", "SensorOn", "register-HRMSensor");
                }
            }
        } else {
            LtUtil.log_e("SensorHRM", "SensorOn", "SensorManager null !!!");
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                SensorHRM.this.returnSensorValues();
            }
        }, 0, (long) interval);
    }

    public void returnSensorValues() {
        if (this.isHRMMGROn) {
            if (this.mBuffer_SensorValue_HRM != null) {
                SensorListener sensorListener = this.mSensorListener;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.mBuffer_SensorValue_HRM[0]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_HRM[1]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_HRM[2]);
                sensorListener.onSensorValueReceived(13, sb.toString());
            } else {
                this.mSensorListener.onSensorValueReceived(13, "null");
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void SensorOff() {
        LtUtil.log_i("SensorHRM", "sensorOff", "Sensor Off");
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
            LtUtil.log_d("SensorHRM", "mTimer canceled", "...");
        }
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.isHRMMGROn = false;
        this.mSensorManager = null;
        this.mHRMSensor = null;
        this.mBuffer_SensorValue_HRM = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 65562) {
            this.isHRMMGROn = true;
            this.mBuffer_SensorValue_HRM = (float[]) event.values.clone();
        }
    }

    public boolean isSensorOn(int sensorID) {
        if (sensorID != 13 || this.mHRMSensor == null) {
            return false;
        }
        return true;
    }

    public float[] returnHRM() {
        LtUtil.log_d("SensorHRM", "returnHRM", dataCheck(this.mBuffer_SensorValue_HRM));
        return this.mBuffer_SensorValue_HRM;
    }

    private String dataCheck(float[] data) {
        String result = "";
        if (data == null) {
            return "Data : null";
        }
        for (int i = 0; i < data.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(result);
            sb.append(data[i]);
            result = sb.toString();
            if (i < data.length - 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(result);
                sb2.append(" , ");
                result = sb2.toString();
            }
        }
        return result;
    }
}
