package com.samsung.android.contextaware.utilbundle.autotest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Random;

class BypassStressTest extends CmdProcessStressTest {
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public final void onSensorChanged(SensorEvent event) {
        }
    };
    private final SensorManager mSensorManager;

    protected BypassStressTest(Context context, int delayTime) {
        super(delayTime);
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
    }

    protected final void registerListener() {
        if (this.mSensorManager != null) {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(getType()), getSensorRate());
        }
    }

    protected final void unregisterListener() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(getType()));
        }
    }

    protected final void clear() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
    }

    protected final int getType() {
        switch (new Random().nextInt(5)) {
            case 0:
                return 1;
            case 1:
                return 3;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 8;
            default:
                return 0;
        }
    }

    private int getSensorRate() {
        return new Random().nextInt(180) + 20;
    }
}
