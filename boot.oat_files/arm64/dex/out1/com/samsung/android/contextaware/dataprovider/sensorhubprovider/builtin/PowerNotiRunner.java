package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.sec.enterprise.DeviceInventory;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class PowerNotiRunner extends LibTypeProvider {
    public PowerNotiRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_POWER_NOTI.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) -1;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Noti"};
    }

    public final void updateApPowerStatus(int status, long timeStamp) {
        int data;
        String[] names = getContextValueNames();
        if (status == -47) {
            data = 1;
        } else if (status == -46) {
            data = 2;
        } else {
            return;
        }
        super.getContextBean().putContext(names[0], data);
        super.notifyObserver();
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

    public final Bundle getFaultDetectionResult() {
        if (super.getUsedTotalCount() > 1) {
            return super.getFaultDetectionResult();
        }
        CaLogger.debug(DeviceInventory.DEVICE_INFO_SUCCESS_CALL);
        return super.getFaultDetectionResult(0, ContextAwareServiceErrors.SUCCESS.getMessage());
    }

    public final void clear() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        super.clear();
    }
}
