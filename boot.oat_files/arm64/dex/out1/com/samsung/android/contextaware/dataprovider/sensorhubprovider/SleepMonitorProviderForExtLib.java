package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class SleepMonitorProviderForExtLib extends ExtLibTypeProvider {
    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }

    protected SleepMonitorProviderForExtLib(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    protected final byte getInstLibType() {
        return (byte) 37;
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode();
    }
}
