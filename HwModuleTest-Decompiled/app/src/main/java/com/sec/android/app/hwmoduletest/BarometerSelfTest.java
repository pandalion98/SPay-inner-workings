package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import java.text.DecimalFormat;

public class BarometerSelfTest extends BaseActivity {
    private TextView mAltitudeText;
    /* access modifiers changed from: private */
    public float mAltitudeValue = 0.0f;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private Sensor mPressureSensor;
    private TextView mPressureText;
    /* access modifiers changed from: private */
    public float mPressureValue = 1000.0f;
    private TextView mResultText;
    /* access modifiers changed from: private */
    public PressureSensorListener mSensorListener;
    /* access modifiers changed from: private */
    public SensorManager mSensorManager = null;
    private TextView mTempResultText;
    private TextView mTempText;
    /* access modifiers changed from: private */
    public float mTempValue = 1000.0f;

    private class PressureSensorListener implements SensorEventListener {
        private PressureSensorListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 6) {
                BarometerSelfTest.this.mPressureValue = event.values[0];
                BarometerSelfTest barometerSelfTest = BarometerSelfTest.this;
                BarometerSelfTest.this.mSensorManager;
                barometerSelfTest.mAltitudeValue = SensorManager.getAltitude(1013.25f, event.values[0]);
                if (event.values.length >= 3) {
                    BarometerSelfTest.this.mTempValue = event.values[2];
                }
                BarometerSelfTest.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        String tempTemperature = Kernel.read(Kernel.BAROMETE_TEMPERATURE);
                        if (tempTemperature != null) {
                            BarometerSelfTest.this.mTempValue = Float.parseFloat(tempTemperature);
                        }
                        BarometerSelfTest.this.displayResult();
                        BarometerSelfTest.this.mSensorManager.unregisterListener(BarometerSelfTest.this.mSensorListener);
                    }
                }, 600);
            }
        }
    }

    public BarometerSelfTest() {
        super("BarometerSelfTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.barometer_self_test);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mPressureSensor = this.mSensorManager.getDefaultSensor(6);
        this.mSensorListener = new PressureSensorListener();
        this.mTempText = (TextView) findViewById(C0268R.C0269id.barometer_temperature);
        this.mTempResultText = (TextView) findViewById(C0268R.C0269id.barometer_temperature_result);
        this.mPressureText = (TextView) findViewById(C0268R.C0269id.barometer_pressure);
        this.mAltitudeText = (TextView) findViewById(C0268R.C0269id.barometer_altitude);
        this.mResultText = (TextView) findViewById(C0268R.C0269id.result_text);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mSensorListener, this.mPressureSensor, 2);
    }

    /* access modifiers changed from: private */
    public void displayResult() {
        boolean isPass = false;
        TextView textView = this.mPressureText;
        StringBuilder sb = new StringBuilder();
        sb.append("Pressure:  ");
        sb.append(changeDot(this.mPressureValue));
        sb.append(" hPa");
        textView.setText(sb.toString());
        this.mTempText.setText("Temperature:  ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Temperature:");
        sb2.append(this.mTempValue);
        LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
        if (-45.0f > this.mTempValue || this.mTempValue > 85.0f) {
            this.mTempResultText.setText(" Fail ");
            this.mTempResultText.setTextColor(-65536);
        } else {
            this.mTempResultText.setText(" Pass ");
            this.mTempResultText.setTextColor(-16776961);
        }
        TextView textView2 = this.mAltitudeText;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Altitude  ");
        sb3.append(changeDot(this.mAltitudeValue));
        sb3.append(" m");
        textView2.setText(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("pressure=");
        sb4.append(this.mPressureValue);
        sb4.append(", temp=");
        sb4.append(this.mTempValue);
        sb4.append(", Altitude=");
        sb4.append(this.mAltitudeValue);
        LtUtil.log_d(this.CLASS_NAME, "displayResult", sb4.toString());
        if (this.mPressureValue >= 300.0f && this.mPressureValue <= 1100.0f) {
            boolean z = true;
            if ("STM".equalsIgnoreCase(Kernel.read(Kernel.BAROMETE_VENDOR))) {
                if (this.mTempValue < -30.0f || this.mTempValue > 105.0f) {
                    z = false;
                }
                isPass = z;
            } else {
                if (this.mTempValue < -45.0f || this.mTempValue > 85.0f) {
                    z = false;
                }
                isPass = z;
            }
        }
        if (isPass) {
            this.mResultText.setText(C0268R.string.pass);
            this.mResultText.setTextColor(-16776961);
            setResult(-1);
            return;
        }
        this.mResultText.setText(C0268R.string.fail);
        this.mResultText.setTextColor(-65536);
        setResult(0);
    }

    private String changeDot(float value) {
        return String.valueOf(new DecimalFormat("#.##").format((double) value));
    }
}
