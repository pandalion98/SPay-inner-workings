package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BatchContextProvider extends ContextProviderDecorator {
    public BatchContextProvider(ContextComponent provider) {
        super(provider);
    }

    public void start(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.start(listener, operation);
    }

    public void stop(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.stop(listener, operation);
    }
}
