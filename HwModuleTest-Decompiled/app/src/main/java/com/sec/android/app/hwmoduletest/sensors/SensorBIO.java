package com.sec.android.app.hwmoduletest.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.util.Timer;
import java.util.TimerTask;

public class SensorBIO extends SensorBase implements SensorEventListener {
    private final String CLASS_NAME = "SensorBIO";
    private final boolean DEBUG = true;
    private boolean isBIOMGROn = false;
    private Sensor mBIOSensor = null;
    private float[] mBuffer_SensorValue_BIO = null;
    private Context mContext;
    private SensorListener mSensorListener;
    private SensorManager mSensorManager = null;
    private String[] mSensorValues = null;
    private Timer mTimer = null;

    public SensorBIO(Context mContext2) {
        LtUtil.log_d("SensorBIO", "SensorBIO", "Constructor");
        this.mContext = mContext2;
    }

    public void SensorOn(SensorListener mSensorListener2, int[] sensorID, int interval) {
        this.mSensorListener = mSensorListener2;
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        if (this.mSensorManager != null) {
            for (int i = 0; i < sensorID.length; i++) {
                if (sensorID[i] != 12) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("unregistered: ");
                    sb.append(sensorID[i]);
                    LtUtil.log_d("SensorBIO", "SensorOn", sb.toString());
                } else if (this.mBIOSensor == null) {
                    this.mBIOSensor = this.mSensorManager.getDefaultSensor(65561);
                    this.mSensorManager.registerListener(this, this.mBIOSensor, 2);
                    LtUtil.log_d("SensorBIO", "SensorOn", "register-BIOSensor");
                }
            }
        } else {
            LtUtil.log_e("SensorBIO", "SensorOn", "SensorManager null !!!");
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                SensorBIO.this.returnSensorValues();
            }
        }, 0, (long) interval);
    }

    public void returnSensorValues() {
        if (this.isBIOMGROn) {
            if (this.mBuffer_SensorValue_BIO != null) {
                SensorListener sensorListener = this.mSensorListener;
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.mBuffer_SensorValue_BIO[0]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_BIO[1]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_BIO[2]);
                sb.append(",");
                sb.append(this.mBuffer_SensorValue_BIO[3]);
                sensorListener.onSensorValueReceived(12, sb.toString());
            } else {
                this.mSensorListener.onSensorValueReceived(12, "null");
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void SensorOff() {
        LtUtil.log_i("SensorBIO", "sensorOff", "Sensor Off");
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
            LtUtil.log_d("SensorBIO", "mTimer canceled", "...");
        }
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.isBIOMGROn = false;
        this.mSensorManager = null;
        this.mBIOSensor = null;
        this.mBuffer_SensorValue_BIO = null;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 65561) {
            this.isBIOMGROn = true;
            this.mBuffer_SensorValue_BIO = (float[]) event.values.clone();
        }
    }

    public boolean isSensorOn(int sensorID) {
        if (sensorID != 12 || this.mBIOSensor == null) {
            return false;
        }
        return true;
    }

    public float[] returnHRM() {
        LtUtil.log_d("SensorBIO", "returnBIO", dataCheck(this.mBuffer_SensorValue_BIO));
        return this.mBuffer_SensorValue_BIO;
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
