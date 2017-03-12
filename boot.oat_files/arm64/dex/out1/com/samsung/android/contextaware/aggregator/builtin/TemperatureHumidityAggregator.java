package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import android.util.TimeUtils;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.EnvironmentSensorAggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.lib.builtin.TemperatureHumidityCompensationLibEngine;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class TemperatureHumidityAggregator extends EnvironmentSensorAggregator {
    private TemperatureHumidityCompensationLibEngine mCompensationEngine = null;
    private long mSleepTime = 0;
    private long mWakeupTime = 0;

    public TemperatureHumidityAggregator(int version, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, null, null, collectionList, observable);
    }

    protected final void initializeAggregator() {
        this.mCompensationEngine = new TemperatureHumidityCompensationLibEngine();
    }

    protected final void terminateAggregator() {
        this.mCompensationEngine = null;
    }

    protected final double[] compensateForRawData(double[][] rawSensorData) {
        if (rawSensorData == null || rawSensorData.length <= 1) {
            return null;
        }
        double[] temperature = new double[1];
        double[] humidity = new double[1];
        int nLenTemp = rawSensorData[0].length;
        int nLenHumid = rawSensorData[1].length;
        int status = getLoggingStatus();
        if (nLenTemp == 0 || nLenHumid == 0) {
            return null;
        }
        int nLen;
        long startTime;
        if (nLenTemp <= nLenHumid) {
            nLen = nLenTemp * 2;
        } else {
            nLen = nLenHumid * 2;
        }
        double[] compensatedData = new double[nLen];
        this.mWakeupTime = System.currentTimeMillis() * TimeUtils.NANOS_PER_MS;
        CaLogger.info("sleepTime = " + this.mSleepTime + ", wakeupTime = " + this.mWakeupTime);
        long interval = (this.mWakeupTime - this.mSleepTime) / ((long) (nLen / 2));
        if (nLen > 2) {
            startTime = this.mSleepTime + (((this.mWakeupTime - this.mSleepTime) - (((long) ((nLen / 2) - 1)) * interval)) / 2);
        } else {
            startTime = this.mSleepTime + ((this.mWakeupTime - this.mSleepTime) / 2);
        }
        for (int i = 0; i < nLen / 2; i++) {
            if (status == 1) {
                this.mCompensationEngine.native_temperaturehumidity_getLastCompensatedData(temperature, humidity);
                CaLogger.info("getLastCompensatedData : compensatedTemp = " + temperature[0] + ", compensatedHumid = " + humidity[0]);
            } else if (status == 2) {
                long timestamp = startTime + (((long) i) * interval);
                this.mCompensationEngine.native_temperaturehumidity_getCompensatedData(rawSensorData[0][i], rawSensorData[1][i], temperature, humidity, timestamp);
                CaLogger.info("RawData : rawTempData = " + rawSensorData[0][i] + ", rawHumidData = " + rawSensorData[1][i] + ", compensatedTemp = " + temperature[0] + ",  compensatedHumid = " + humidity[0] + ", timestamp = " + timestamp);
            }
            compensatedData[i] = temperature[0];
            compensatedData[i + nLenTemp] = humidity[0];
        }
        this.mSleepTime = this.mWakeupTime;
        return compensatedData;
    }

    protected final void notifyCompensationData(double[] compensationData) {
        String[] names = getContextValueNames();
        int nLen = compensationData.length / 2;
        double[] compensatedTemp = new double[nLen];
        double[] compensatedHumid = new double[nLen];
        for (int i = 0; i < nLen; i++) {
            compensatedTemp[i] = compensationData[i];
            compensatedHumid[i] = compensationData[i + nLen];
        }
        super.getContextBean().putContext(names[0], compensatedTemp);
        super.getContextBean().putContext(names[1], compensatedHumid);
        super.notifyObserver();
    }

    protected final boolean checkCompensationData(double[] compensationData) {
        if (compensationData == null || compensationData.length > 0) {
            return true;
        }
        return false;
    }

    protected final String[] getRawSensorValueNames() {
        return new String[]{"Temperature", "Humidity"};
    }

    public final String[] getContextValueNames() {
        return new String[]{"Temperature", "Humidity"};
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_TEMPERATURE_HUMIDITY.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final void updateApSleep() {
        long timeStamp = super.getTimeStampForApStatus() * TimeUtils.NANOS_PER_MS;
        CaLogger.info("timeStamp = " + Long.toString(timeStamp));
        this.mSleepTime = timeStamp;
        super.updateApSleep();
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected void clearAccordingToRequest() {
        CaLogger.trace();
        super.clearAccordingToRequest();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
