package com.sec.android.app.hwmoduletest.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Looper;
import com.maximintegrated.bio.p002uv.eol.MaximUVSensorEol;
import com.maximintegrated.bio.p002uv.eol.MaximUVSensorEolEventListener;
import com.maximintegrated.bio.p002uv.eol.MaximUVSensorEolIREvent;
import com.maximintegrated.bio.p002uv.eol.MaximUVSensorEolUVEvent;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.SensorTestMenu;
import java.util.Timer;
import java.util.TimerTask;

public class SensorUV extends SensorBase implements SensorListener, SensorEventListener {
    private static final int INTERVAL = 500;
    public static final int UVA = 1;
    public static final int UVADC = 1;
    public static final int UVB = 2;
    public static final int UVIndex = 0;
    private final String CLASS_NAME = "SensorUV";
    /* access modifiers changed from: private */
    public boolean isUVMGROn = false;
    /* access modifiers changed from: private */
    public String[] mBuffer = null;
    /* access modifiers changed from: private */
    public int[] mBuffer_SensorValue_Cloud = null;
    private float[] mBuffer_SensorValue_UV = null;
    private Context mContext;
    private Maxim mMaximSensor;
    private SensorListener mSensorListener = null;
    private SensorManager mSensorManager = null;
    private Timer mTimer = null;
    private Sensor mUVSensor = null;
    private String mVendor;

    private class Maxim implements MaximUVSensorEolEventListener {
        private int ADC;

        /* renamed from: HR */
        private int f31HR;
        private int SUM;
        private double UVIndex;
        private int VB0;
        private int VB1;
        private int VB2;
        private int VB3;
        private Context mContext;
        MaximUVSensorEol uvEolSensor;

        public Maxim(Context mContext2) {
            LtUtil.log_d("SensorUV", "SensorUV", "Constructor");
            this.mContext = mContext2;
        }

        public boolean SensorOn() {
            if (this.uvEolSensor != null) {
                return true;
            }
            this.uvEolSensor = new MaximUVSensorEol(this.mContext, 65565);
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            this.uvEolSensor.registerListener(this);
            LtUtil.log_d("SensorUV", "SensorOn", "register-UVSensor");
            SensorUV.this.isUVMGROn = true;
            return true;
        }

        public void SensorOff() {
            if (isSensorOn()) {
                this.uvEolSensor.unregisterListener(this);
                this.uvEolSensor = null;
                this.ADC = 0;
                LtUtil.log_d("SensorUV", "SensorOn", "unregister-UVSensor");
            }
        }

        public boolean isSensorOn() {
            if (this.uvEolSensor == null || this.uvEolSensor.getSensor() == null) {
                return false;
            }
            return true;
        }

        public void onMaximUVSensorEolIRChanged(MaximUVSensorEolIREvent event) {
            this.VB0 = event.vb0;
            this.VB1 = event.vb1;
            this.VB2 = event.vb2;
            this.VB3 = event.vb3;
            this.SUM = event.sum;
            this.f31HR = event.f6hr;
            SensorUV.this.mBuffer_SensorValue_Cloud = new int[]{this.VB0, this.VB1, this.VB2, this.VB3, this.SUM, this.f31HR};
            SensorUV.this.returnCloudValues(SensorUV.this.mBuffer_SensorValue_Cloud);
        }

        public void onMaximUVSensorEolUVChanged(MaximUVSensorEolUVEvent event) {
            this.ADC = event.adccount;
            this.UVIndex = (0.0019d * ((double) this.ADC)) - 0.0047d;
            SensorUV.this.mBuffer = new String[]{Double.toString(this.UVIndex), Integer.toString(this.ADC)};
            SensorUV.this.returnSensorValues();
        }
    }

    public SensorUV(Context mContext2) {
        LtUtil.log_i("SensorUV", "SensorUV", "Constructor");
        this.mContext = mContext2;
        this.mVendor = CheckVendorName();
        StringBuilder sb = new StringBuilder();
        sb.append("UV Sensor Vendor is ");
        sb.append(this.mVendor);
        LtUtil.log_i("SensorUV", "SensorUV", sb.toString());
    }

    public SensorUV(SensorListener mSensorListener2, Context mContext2) {
        LtUtil.log_i("SensorUV", "SensorUV", "Constructor");
        this.mSensorListener = mSensorListener2;
        this.mContext = mContext2;
        this.mVendor = CheckVendorName();
        StringBuilder sb = new StringBuilder();
        sb.append("UV Sensor Vendor is ");
        sb.append(this.mVendor);
        LtUtil.log_i("SensorUV", "SensorUV", sb.toString());
    }

    public void SensorOn() {
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
            this.mMaximSensor = new Maxim(this.mContext);
            this.mMaximSensor.SensorOn();
            return;
        }
        SensorOn(65565, 500);
    }

    private void SensorOn(int sensorID, int interval) {
        Context context = this.mContext;
        Context context2 = this.mContext;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        if (this.mSensorManager == null) {
            LtUtil.log_e("SensorUV", "SensorOn", "SensorManager null !!!");
        } else if (!isSensorOn()) {
            this.mUVSensor = this.mSensorManager.getDefaultSensor(65565);
            this.mSensorManager.registerListener(this, this.mUVSensor, 2);
            LtUtil.log_i("SensorUV", "SensorOn", "register-UVSensor");
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                SensorUV.this.returnSensorValues();
            }
        }, 0, (long) interval);
    }

    public void SensorOff() {
        LtUtil.log_i("SensorUV", "sensorOff", "Sensor Off");
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
            this.mMaximSensor.SensorOff();
        } else {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer = null;
                LtUtil.log_d("SensorUV", "mTimer canceled", "...");
            }
            if (this.mSensorManager != null) {
                this.mSensorManager.unregisterListener(this);
            }
        }
        this.isUVMGROn = false;
        this.mSensorManager = null;
        this.mUVSensor = null;
        this.mBuffer_SensorValue_UV = null;
    }

    public boolean isSensorOn() {
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
            if (this.mMaximSensor.isSensorOn()) {
                return true;
            }
            return false;
        } else if (this.mUVSensor != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getADC() {
        String res = "0";
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
            if (this.mBuffer == null || this.mBuffer.length <= 1) {
                return res;
            }
            return this.mBuffer[1];
        } else if (this.mBuffer_SensorValue_UV == null || this.mBuffer_SensorValue_UV.length <= 1) {
            return res;
        } else {
            return String.valueOf(this.mBuffer_SensorValue_UV[1]);
        }
    }

    public String getUVA() {
        String res = "0";
        if (this.mBuffer_SensorValue_UV == null || this.mBuffer_SensorValue_UV.length <= 1) {
            return res;
        }
        return String.valueOf(this.mBuffer_SensorValue_UV[1]);
    }

    public String getUVB() {
        String res = "0";
        if (this.mBuffer_SensorValue_UV == null || this.mBuffer_SensorValue_UV.length <= 2) {
            return res;
        }
        return String.valueOf(this.mBuffer_SensorValue_UV[2]);
    }

    public String getUVIndex() {
        String res = "0";
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
            if (this.mBuffer == null || this.mBuffer.length <= 0) {
                return res;
            }
            return this.mBuffer[0];
        } else if (this.mBuffer_SensorValue_UV == null || this.mBuffer_SensorValue_UV.length <= 0) {
            return res;
        } else {
            return String.format("%.1f", new Object[]{Float.valueOf(this.mBuffer_SensorValue_UV[0])});
        }
    }

    public void returnSensorValues() {
        String res;
        if (this.isUVMGROn) {
            if (this.mBuffer_SensorValue_UV == null && this.mBuffer == null) {
                this.mSensorListener.onSensorValueReceived(11, "null");
            } else {
                String res2 = "";
                if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equalsIgnoreCase(this.mVendor)) {
                    res = res2;
                    for (String append : this.mBuffer) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(res);
                        sb.append(append);
                        sb.append(",");
                        res = sb.toString();
                    }
                } else if (this.mBuffer_SensorValue_UV == null || this.mBuffer_SensorValue_UV.length <= 0) {
                    res = res2;
                } else {
                    String res3 = res2;
                    for (float append2 : this.mBuffer_SensorValue_UV) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(res);
                        sb2.append(append2);
                        sb2.append(",");
                        res3 = sb2.toString();
                    }
                }
                this.mSensorListener.onSensorValueReceived(11, res.substring(0, res.length() - 1));
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void returnCloudValues(int[] buffer_VB) {
        if (this.isUVMGROn) {
            if (buffer_VB != null) {
                String res = "";
                for (int append : buffer_VB) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(res);
                    sb.append(append);
                    sb.append(",");
                    res = sb.toString();
                }
                this.mSensorListener.onSensorValueReceived(14, res.substring(0, res.length() - 1));
            } else {
                this.mSensorListener.onSensorValueReceived(14, "null");
            }
        }
        this.mSensorListener.onSensorValueReceived(0, "");
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 65565) {
            this.isUVMGROn = true;
            this.mBuffer_SensorValue_UV = (float[]) event.values.clone();
        }
    }

    public void onSensorValueReceived(int mSensor, String value) {
    }

    private String CheckVendorName() {
        String str = "";
        try {
            Context context = this.mContext;
            Context context2 = this.mContext;
            return ((SensorManager) context.getSystemService("sensor")).getDefaultSensor(65565).getVendor();
        } catch (NullPointerException e) {
            LtUtil.log_e(e);
            return SensorTestMenu.getTestCase(SensorTestMenu.SENSOR_TEST_NAME_UV);
        }
    }

    public String getVendorName() {
        return this.mVendor;
    }
}
