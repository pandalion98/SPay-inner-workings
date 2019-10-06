package com.maximintegrated.bio.p002uv.eol;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/* renamed from: com.maximintegrated.bio.uv.eol.MaximUVSensorEol */
public class MaximUVSensorEol implements SensorEventListener {
    private static final String LOGTAG = "MaximUVEol";
    private static final int MAX86902_ENHANCED_UV_EOL_HR_MODE = -7;
    private static final int MAX86902_ENHANCED_UV_EOL_SUM_MODE = -6;
    private static final int MAX86902_ENHANCED_UV_EOL_VB_MODE = -5;
    private static final String VERSION = "1.0";
    private int dataFull = 0;
    private MaximUVSensorEolIREvent eolIREvent;
    private MaximUVSensorEolEventListener eolListener = null;
    private MaximUVSensorEolUVEvent eolUVEvent;
    private int sensor_type = 0;

    /* renamed from: sm */
    private SensorManager f5sm;
    private Sensor uvEolSensor;

    public MaximUVSensorEol(Context context, int sensor_type2) {
        Log.i(LOGTAG, "Version:1.0");
        this.sensor_type = sensor_type2;
        this.f5sm = (SensorManager) context.getSystemService("sensor");
        this.uvEolSensor = this.f5sm.getDefaultSensor(sensor_type2);
        String str = LOGTAG;
        StringBuilder sb = new StringBuilder("MaximUVSensor() - uvEolSensor : ");
        sb.append(this.uvEolSensor);
        Log.d(str, sb.toString());
    }

    public Sensor getSensor() {
        return this.uvEolSensor;
    }

    public void registerListener(MaximUVSensorEolEventListener eolListener2) {
        this.eolListener = eolListener2;
        this.eolIREvent = new MaximUVSensorEolIREvent();
        this.eolUVEvent = new MaximUVSensorEolUVEvent();
        this.f5sm.registerListener(this, this.uvEolSensor, 0);
    }

    public void unregisterListener(MaximUVSensorEolEventListener eolListener2) {
        this.f5sm.unregisterListener(this);
        this.eolListener = null;
        this.eolIREvent = null;
        this.eolUVEvent = null;
    }

    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            try {
                if (event.sensor.getType() == this.sensor_type) {
                    int sensor_data = (int) event.values[0];
                    if (event.values[1] < 0.0f) {
                        if (this.eolIREvent != null) {
                            if (event.values[1] == -5.0f) {
                                switch ((sensor_data >> 22) & 3) {
                                    case 0:
                                        this.eolIREvent.vb0 = 262143 & sensor_data;
                                        this.dataFull = 1 | this.dataFull;
                                        break;
                                    case 1:
                                        this.eolIREvent.vb1 = 262143 & sensor_data;
                                        this.dataFull |= 2;
                                        break;
                                    case 2:
                                        this.eolIREvent.vb2 = 262143 & sensor_data;
                                        this.dataFull |= 4;
                                        break;
                                    case 3:
                                        this.eolIREvent.vb3 = 262143 & sensor_data;
                                        this.dataFull |= 8;
                                        break;
                                }
                            } else if (event.values[1] == -6.0f) {
                                this.eolIREvent.sum = sensor_data;
                                this.dataFull |= 16;
                            } else if (event.values[1] == -7.0f) {
                                this.eolIREvent.f6hr = sensor_data;
                                this.dataFull |= 32;
                            }
                            if (this.dataFull == 63) {
                                if (this.eolListener != null) {
                                    String str = LOGTAG;
                                    StringBuilder sb = new StringBuilder("EOL0:");
                                    sb.append(this.eolIREvent.vb0);
                                    sb.append(",");
                                    sb.append(this.eolIREvent.vb1);
                                    sb.append(",");
                                    sb.append(this.eolIREvent.vb2);
                                    sb.append(",");
                                    sb.append(this.eolIREvent.vb3);
                                    sb.append(",");
                                    sb.append(this.eolIREvent.sum);
                                    sb.append(",");
                                    sb.append(this.eolIREvent.f6hr);
                                    Log.d(str, sb.toString());
                                    this.eolListener.onMaximUVSensorEolIRChanged(this.eolIREvent);
                                }
                                this.dataFull = 0;
                            }
                        }
                    } else if (this.eolUVEvent != null) {
                        this.eolUVEvent.adccount = sensor_data * 2;
                        if (this.eolListener != null) {
                            String str2 = LOGTAG;
                            StringBuilder sb2 = new StringBuilder("EOL1:");
                            sb2.append(this.eolUVEvent.adccount);
                            Log.d(str2, sb2.toString());
                            this.eolListener.onMaximUVSensorEolUVChanged(this.eolUVEvent);
                        }
                    }
                }
            } catch (Exception e) {
                String str3 = LOGTAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(e.getMessage());
                Log.d(str3, sb3.toString());
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
