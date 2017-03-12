package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.PositioningProviderForExtLib;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CurrentStatusForMovementPositioningRunner extends PositioningProviderForExtLib {
    public CurrentStatusForMovementPositioningRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    public final String getContextType() {
        return ContextType.REQUEST_SENSORHUB_MOVEMENT_FOR_POSITIONING_CURRENT_STATUS.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 1, (byte) 0};
    }

    protected final int parse(int next, byte[] packet) {
        int tmpNext = next;
        String name = getContextValueNames()[0];
        if (name == null || name.isEmpty() || (packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        super.getContextBean().putContext(name, packet[tmpNext]);
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Status"};
    }

    public void occurTimeOut() {
        CaLogger.error(SensorHubErrors.ERROR_TIME_OUT.getMessage());
        super.occurTimeOut();
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

    protected final ITimeOutCheckObserver getTimeOutCheckObserver() {
        return this;
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
