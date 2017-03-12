package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class StepCountAlertRunner extends LibTypeProvider {
    private static final int DEFAULT_STEP_COUNT = 10;
    private int mStepCount = 10;

    public StepCountAlertRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_STEP_COUNT_ALERT.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 6;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) this.mStepCount, (byte) 0};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property != 7) {
            return false;
        }
        this.mStepCount = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
        return true;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
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

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
