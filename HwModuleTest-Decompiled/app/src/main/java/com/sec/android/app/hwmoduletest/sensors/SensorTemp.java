package com.sec.android.app.hwmoduletest.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.Timer;
import java.util.TimerTask;

public class SensorTemp extends SensorBase implements SensorEventListener {
    private final String CLASS_NAME = "SensorTemp";
    private final boolean DEBUG = true;
    private boolean isTempMGROn = false;
    private float[] mBuffer_SensorValue_Temp = null;
    private Context mContext;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager = null;
    private String[] mSensorValues = null;
    private Sensor mTempSensor = null;
    private Timer mTimer = null;

    public SensorTemp(Context mContext2) {
        LtUtil.log_d("SensorTemp", "SensorTemp", "Constructor");
        this.mContext = mContext2;
    }

    public void SensorOn(SensorListener mSensorListener2, int[] sensorID, int interval) {
        this.mSensorListener = mSensorListener2;
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        if (this.mSensorManager != null) {
            for (int i = 0; i < sensorID.length; i++) {
                if (sensorID[i] != 9) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unregistered: ");
                    sb.append(sensorID[i]);
                    LtUtil.log_d("SensorTemp", "SensorOn", sb.toString());
                } else if (this.mTempSensor == null) {
                    this.mTempSensor = this.mSensorManager.getDefaultSensor(13);
                    this.mSensorManager.registerListener(this, this.mTempSensor, 2);
                    LtUtil.log_d("SensorTemp", "SensorOn", "register-TempSensor");
                }
            }
        } else {
            LtUtil.log_e("SensorTemp", "SensorOn", "SensorManager null !!!");
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                SensorTemp.this.returnSensorValues();
            }
        }, 0, (long) interval);
    }

    public void returnSensorValues() {
        if (this.isTempMGROn) {
            if (this.mBuffer_SensorValue_Temp != null) {
                SensorListener sensorListener = this.mSensorListener;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.mBuffer_SensorValue_Temp[0]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_Temp[1]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_Temp[2]);
                sensorListener.onSensorValueReceived(9, sb.toString());
            } else {
                this.mSensorListener.onSensorValueReceived(9, "null");
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void SensorOff() {
        LtUtil.log_i("SensorTemp", "sensorOff", "Sensor Off");
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
            LtUtil.log_d("SensorTemp", "mTimer canceled", "...");
        }
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.isTempMGROn = false;
        this.mSensorManager = null;
        this.mTempSensor = null;
        this.mBuffer_SensorValue_Temp = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 13) {
            this.isTempMGROn = true;
            this.mBuffer_SensorValue_Temp = (float[]) event.values.clone();
        }
    }

    public boolean isSensorOn(int sensorID) {
        if (sensorID == 9 && this.mTempSensor != null) {
            return true;
        }
        return false;
    }

    public float[] returnTemp() {
        LtUtil.log_d("SensorTemp", "returnTemp", dataCheck(this.mBuffer_SensorValue_Temp));
        return this.mBuffer_SensorValue_Temp;
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
