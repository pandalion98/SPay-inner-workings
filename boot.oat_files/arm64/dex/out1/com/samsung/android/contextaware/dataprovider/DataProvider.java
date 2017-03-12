package com.samsung.android.contextaware.dataprovider;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class DataProvider extends ContextProvider {
    protected abstract void initializeManager();

    protected abstract void terminateManager();

    protected DataProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final void initialize() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null.");
        } else {
            initializeManager();
        }
    }

    protected final void terminate() {
        terminateManager();
    }
}
