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

public class AutoRotationRunner extends LibTypeProvider {
    private static final int DEFAULT_DEVICE_TYPE = 0;
    private int mDeviceType = 0;

    public AutoRotationRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_AUTO_ROTATION.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 7;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) this.mDeviceType, (byte) 0};
    }

    public final String[] getContextValueNames() {
        return new String[]{"Angle"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property != 8) {
            return false;
        }
        this.mDeviceType = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
        CaLogger.info("Device Type = " + Double.toString((double) this.mDeviceType));
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
