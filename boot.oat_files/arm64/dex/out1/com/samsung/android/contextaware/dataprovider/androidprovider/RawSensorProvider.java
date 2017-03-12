package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class RawSensorProvider extends AndroidProvider {
    protected static final int DEFAULT_SENSOR_RATE = 60000;
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public final void onSensorChanged(SensorEvent event) {
            RawSensorProvider.this.getContextBean().putContext("SystemTime", System.currentTimeMillis());
            RawSensorProvider.this.getContextBean().putContext("TimeStamp", event.timestamp);
            RawSensorProvider.this.getContextBean().putContext("Accuracy", event.accuracy);
            String[] names = RawSensorProvider.this.getContextValueNames();
            int valueLength = Math.min(names.length, event.values.length);
            for (int i = 0; i < valueLength; i++) {
                RawSensorProvider.this.getContextBean().putContext(names[i], event.values[i]);
            }
            RawSensorProvider.this.notifyObserver();
        }
    };
    private SensorManager mSensorManager;
    private final int mSensorRate;

    protected abstract int getSensorType();

    protected RawSensorProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable, int rate) {
        super(version, context, looper, observable);
        this.mSensorRate = rate;
    }

    public void enable() {
        registerSensorListener();
    }

    public void disable() {
        unregisterSensorListener();
    }

    private void registerSensorListener() {
        if (this.mSensorManager == null || this.mSensorListener == null) {
            CaLogger.error("cannot register the sensor listener");
        } else {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSensorManager.getDefaultSensor(getSensorType()), this.mSensorRate);
        }
    }

    protected void unregisterSensorListener() {
        if (this.mSensorManager == null || this.mSensorListener == null) {
            CaLogger.error("cannot unregister the sensor listener");
        } else {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
    }

    protected final void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
            return;
        }
        this.mSensorManager = (SensorManager) super.getContext().getSystemService("sensor");
        if (this.mSensorManager == null) {
            CaLogger.error("cannot create the SensorManager object");
        }
    }

    protected final void terminateManager() {
        this.mSensorManager = null;
    }
}
