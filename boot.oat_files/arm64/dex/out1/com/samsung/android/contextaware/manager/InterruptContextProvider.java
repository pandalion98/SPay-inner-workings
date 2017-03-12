package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class InterruptContextProvider extends ContextProviderDecorator {
    public InterruptContextProvider(ContextComponent provider) {
        super(provider);
    }

    public void start(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.initialize();
        this.mProvider.clear();
        this.mProvider.enableForStart(operation);
        this.mProvider.registerApPowerObserver();
        if (operation == 1) {
            this.mProvider.notifyFaultDetectionResult();
        }
    }

    public void stop(Listener listener, int operation) {
        CaLogger.trace();
        this.mProvider.clear();
        this.mProvider.unregisterApPowerObserver();
        this.mProvider.disableForStop(operation);
        if (operation == 1) {
            this.mProvider.notifyFaultDetectionResult();
        }
        this.mProvider.terminate();
    }
}
