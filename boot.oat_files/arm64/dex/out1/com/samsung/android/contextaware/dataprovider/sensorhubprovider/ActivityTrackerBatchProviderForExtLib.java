package com.samsung.android.contextaware.dataprovider.sensorhubprovider;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;

public abstract class ActivityTrackerBatchProviderForExtLib extends ExtLibTypeProvider {
    public /* bridge */ /* synthetic */ void enable() {
        super.enable();
    }

    public /* bridge */ /* synthetic */ void occurTimeOut() {
        super.occurTimeOut();
    }

    protected ActivityTrackerBatchProviderForExtLib(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_ACTIVITY_TRACKER_SERVICE;
    }

    protected final String getDependentService() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode();
    }
}
