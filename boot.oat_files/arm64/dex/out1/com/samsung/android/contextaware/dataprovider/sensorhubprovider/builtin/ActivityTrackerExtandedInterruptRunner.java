package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ActivityTrackerExtandedInterruptRunner extends ActivityTrackerProvider {
    private static final int DEFAULT_ACTIVITY_TYPE = 0;
    private static final int DEFAULT_BATCHING_PERIOD = 30;
    private int mActivityType = 0;
    private int mBatchingPeriod = 30;

    public ActivityTrackerExtandedInterruptRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final int getActivityType() {
        return this.mActivityType;
    }

    protected final int getBatchingPeriod() {
        return this.mBatchingPeriod;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.getCode();
    }

    protected final byte getModeType() {
        return (byte) 3;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 38) {
            int activity = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Activity Type = " + Integer.toString(activity));
            this.mActivityType |= 1 << activity;
            return true;
        } else if (property != 39) {
            return false;
        } else {
            int period = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Batching Period = " + Integer.toString(period));
            this.mBatchingPeriod = period;
            return true;
        }
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
