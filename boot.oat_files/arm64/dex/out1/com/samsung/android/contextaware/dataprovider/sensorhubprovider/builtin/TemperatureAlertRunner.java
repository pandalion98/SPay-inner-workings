package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class TemperatureAlertRunner extends LibTypeProvider {
    private static final int DEFAULT_HIGH_TEMPERATURE = 127;
    private static final boolean DEFAULT_IS_INCLUDING = true;
    private static final int DEFAULT_LOW_TEMPERATURE = 70;
    private int mHighTemperature = 127;
    private boolean mIsIncluding = true;
    private int mLowTemperature = 70;

    public TemperatureAlertRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_TEMPERATURE_ALERT.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_TEMPERATURE_ALERT_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] packet = new byte[3];
        packet[0] = (byte) this.mLowTemperature;
        packet[1] = (byte) this.mHighTemperature;
        if (this.mIsIncluding) {
            packet[2] = (byte) 1;
        } else {
            packet[2] = (byte) 0;
        }
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 20) {
            this.mLowTemperature = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Low Temperature = " + Integer.toString(this.mLowTemperature));
            return true;
        } else if (property == 21) {
            this.mHighTemperature = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("High Temperature = " + Integer.toString(this.mHighTemperature));
            return true;
        } else if (property != 22) {
            return false;
        } else {
            this.mIsIncluding = ((boolean[]) ((ContextAwarePropertyBundle) value).getValue())[0];
            CaLogger.info("Is Including= " + Boolean.toString(this.mIsIncluding));
            return true;
        }
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
