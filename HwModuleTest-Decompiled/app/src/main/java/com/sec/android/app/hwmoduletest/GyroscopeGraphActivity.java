package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.GyroscopeGraph;

public class GyroscopeGraphActivity extends BaseActivity {
    /* access modifiers changed from: private */
    public GyroscopeGraph mGyroGraph;
    private Sensor mGyroSensor;
    /* access modifiers changed from: private */
    public boolean mIsSubGyro;
    private SensorTestListener mSensorListener = new SensorTestListener();
    private SensorManager mSensorManager;
    private Sensor mSubGyroSensor;

    private class SensorTestListener implements SensorEventListener {
        private SensorTestListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type != 4) {
                if (type == 65689 && GyroscopeGraphActivity.this.mIsSubGyro) {
                    GyroscopeGraphActivity.this.mGyroGraph.addValue(event.values[0], event.values[1], event.values[2]);
                }
            } else if (!GyroscopeGraphActivity.this.mIsSubGyro) {
                GyroscopeGraphActivity.this.mGyroGraph.addValue(event.values[0], event.values[1], event.values[2]);
            }
        }
    }

    public GyroscopeGraphActivity() {
        super("GyroscopeGraphActivity");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_graph_activity);
        this.mGyroGraph = (GyroscopeGraph) findViewById(C0268R.C0269id.gyro_sensor_graph);
        this.mIsSubGyro = getIntent().getBooleanExtra("sub_gyro", false);
        StringBuilder sb = new StringBuilder();
        sb.append(" mIsSubGyro : ");
        sb.append(this.mIsSubGyro);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSubGyroSensor = this.mSensorManager.getDefaultSensor(65689);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mSensorListener, this.mGyroSensor, 2);
        if (this.mSubGyroSensor != null) {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSubGyroSensor, 2);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
