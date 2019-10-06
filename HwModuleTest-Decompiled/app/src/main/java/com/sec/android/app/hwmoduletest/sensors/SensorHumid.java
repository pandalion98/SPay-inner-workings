package com.sec.android.app.hwmoduletest.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.Timer;
import java.util.TimerTask;

public class SensorHumid extends SensorBase implements SensorEventListener {
    private final String CLASS_NAME = "SensorHumid";
    private final boolean DEBUG = true;
    private boolean isHumidMGROn = false;
    private float[] mBuffer_SensorValue_Humid = null;
    private Context mContext;
    private Sensor mHumidSensor = null;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager = null;
    private String[] mSensorValues = null;
    private Timer mTimer = null;

    public SensorHumid(Context mContext2) {
        LtUtil.log_d("SensorHumid", "SensorHumid", "Constructor");
        this.mContext = mContext2;
    }

    public void SensorOn(SensorListener mSensorListener2, int[] sensorID, int interval) {
        this.mSensorListener = mSensorListener2;
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        if (this.mSensorManager != null) {
            for (int i = 0; i < sensorID.length; i++) {
                if (sensorID[i] != 10) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unregistered: ");
                    sb.append(sensorID[i]);
                    LtUtil.log_d("SensorHumid", "SensorOn", sb.toString());
                } else if (this.mHumidSensor == null) {
                    this.mHumidSensor = this.mSensorManager.getDefaultSensor(12);
                    this.mSensorManager.registerListener(this, this.mHumidSensor, 2);
                    LtUtil.log_d("SensorHumid", "SensorOn", "register-HumidSensor");
                }
            }
        } else {
            LtUtil.log_e("SensorHumid", "SensorOn", "SensorManager null !!!");
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                SensorHumid.this.returnSensorValues();
            }
        }, 0, (long) interval);
    }

    public void returnSensorValues() {
        if (this.isHumidMGROn) {
            if (this.mBuffer_SensorValue_Humid != null) {
                SensorListener sensorListener = this.mSensorListener;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.mBuffer_SensorValue_Humid[0]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_Humid[1]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_Humid[2]);
                sensorListener.onSensorValueReceived(10, sb.toString());
            } else {
                this.mSensorListener.onSensorValueReceived(10, "null");
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void SensorOff() {
        LtUtil.log_i("SensorHumid", "sensorOff", "Sensor Off");
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
            LtUtil.log_d("SensorHumid", "mTimer canceled", "...");
        }
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.isHumidMGROn = false;
        this.mSensorManager = null;
        this.mHumidSensor = null;
        this.mBuffer_SensorValue_Humid = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 12) {
            this.isHumidMGROn = true;
            this.mBuffer_SensorValue_Humid = (float[]) event.values.clone();
        }
    }

    public boolean isSensorOn(int sensorID) {
        if (sensorID == 10 && this.mHumidSensor != null) {
            return true;
        }
        return false;
    }

    public float[] returnHumid() {
        LtUtil.log_d("SensorHumid", "returnHumid", dataCheck(this.mBuffer_SensorValue_Humid));
        return this.mBuffer_SensorValue_Humid;
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
