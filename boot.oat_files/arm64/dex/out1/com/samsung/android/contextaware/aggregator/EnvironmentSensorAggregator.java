package com.samsung.android.contextaware.aggregator;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class EnvironmentSensorAggregator extends Aggregator {
    private int mLoggingStatus = 0;

    protected abstract boolean checkCompensationData(double[] dArr);

    protected abstract double[] compensateForRawData(double[][] dArr);

    protected EnvironmentSensorAggregator(int version, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, context, looper, collectionList, observable);
    }

    public final void updateContext(String type, Bundle context) {
        if (context != null) {
            if (getRawSensorValueNames() == null) {
                CaLogger.error("getRawSensorValueNames() is null");
                return;
            }
            int bufSize = getRawSensorValueNames().length;
            if (bufSize <= 0) {
                CaLogger.error("length of getRawSensorValueNames() is zero");
                return;
            }
            this.mLoggingStatus = context.getInt("LoggingStatus");
            double[][] rawData = new double[bufSize][];
            int num = 0;
            String[] arr$ = getRawSensorValueNames();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                rawData[num] = getRawSensorData(context, arr$[i$]);
                if (rawData[num] == null || rawData[num].length <= 0) {
                    CaLogger.error("rawData[" + Integer.toString(num) + "].length is null");
                    return;
                } else {
                    num++;
                    i$++;
                }
            }
            double[] compensationData = compensateForRawData(rawData);
            if (compensationData != null && checkCompensationData(compensationData)) {
                notifyCompensationData(compensationData);
            }
        }
    }

    protected String[] getRawSensorValueNames() {
        return getContextValueNames();
    }

    protected void notifyCompensationData(double[] compensationData) {
        super.getContextBean().putContext(getContextValueNames()[0], compensationData);
        notifyObserver();
    }

    protected final double[] getRawSensorData(Bundle context, String valueName) {
        return context.getDoubleArray(valueName);
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        Iterator<?> i = super.getSubCollectors().iterator();
        while (i.hasNext()) {
            ContextProvider next = (ContextProvider) i.next();
            if (next != null && (next instanceof EnvironmentSensorProvider)) {
                return next.setPropertyValue(property, value);
            }
        }
        return false;
    }

    protected final int getLoggingStatus() {
        return this.mLoggingStatus;
    }
}
