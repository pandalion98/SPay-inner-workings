package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class PositioningProviderForExtLib extends ExtLibTypeProvider {
    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }

    protected PositioningProviderForExtLib(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    protected final byte getInstLibType() {
        return (byte) 9;
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.getCode();
    }
}
