package com.samsung.android.contextaware.aggregator;

import com.samsung.android.contextaware.manager.BatchContextProvider;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BatchAggregator extends BatchContextProvider {
    public BatchAggregator(ContextComponent provider) {
        super(provider);
    }

    public final void start(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.start(listener, operation);
    }

    public final void stop(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.stop(listener, operation);
    }
}
