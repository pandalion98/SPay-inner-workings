package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CarryingStatusMonitorRunner extends LibTypeProvider {
    public CarryingStatusMonitorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_CARRYING_STATUS_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_CARRYING_STATUS_MONITOR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) 0, (byte) 1};
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0, (byte) 2};
    }

    public final String[] getContextValueNames() {
        return new String[]{"Type", "Accuracy"};
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        CaLogger.trace();
        String[] names = getContextValueNames();
        if ((packet.length - tmpNext) - 5 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        tmpNext = ((tmpNext + 1) + 1) + 1;
        int tmpNext2 = tmpNext + 1;
        super.getContextBean().putContext(names[0], (short) packet[tmpNext]);
        tmpNext = tmpNext2 + 1;
        super.getContextBean().putContext(names[1], (short) packet[tmpNext2]);
        super.notifyObserver();
        return tmpNext;
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
