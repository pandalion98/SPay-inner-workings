package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

abstract class ExtLibTypeProvider extends SensorHubProvider implements ITimeOutCheckObserver {
    protected abstract int parse(int i, byte[] bArr);

    protected ExtLibTypeProvider(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    protected final byte getInstructionForEnable() {
        return ISensorHubCmdProtocol.INST_LIB_GETVALUE;
    }

    protected final byte getInstructionForDisable() {
        return (byte) 0;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = parse(next, packet);
        if (getTimeOutCheckManager().getHandler() != null) {
            if (!getTimeOutCheckManager().getHandler().isAlive()) {
                CaLogger.error(SensorHubErrors.ERROR_TIME_OUT_CHECK_THREAD_NOT_ALIVE.getMessage());
            } else if (getTimeOutCheckManager().getService() == null) {
                CaLogger.error(SensorHubErrors.ERROR_TIME_OUT_CHECK_SERVICE_NULL_EXCEPTION.getMessage());
            } else if (tmpNext >= 0) {
                getTimeOutCheckManager().getHandler().interrupt();
                notifyObserver();
                notifyFaultDetectionResult();
            }
        }
        return tmpNext;
    }

    protected final boolean checkNotifyCondition() {
        return !getTimeOutCheckManager().isTimeOut();
    }

    public void enable() {
        clear();
        super.enable();
    }

    public final void disable() {
    }

    public void occurTimeOut() {
        CaLogger.error(SensorHubErrors.ERROR_TIME_OUT.getMessage());
        super.getTimeOutCheckManager().setTimeOutOccurence(true);
        notifyCmdProcessResultObserver(getContextTypeOfFaultDetection(), getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage()));
    }
}
