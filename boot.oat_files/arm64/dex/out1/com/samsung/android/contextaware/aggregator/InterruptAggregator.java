package com.samsung.android.contextaware.aggregator;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.InterruptContextProvider;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;

public class InterruptAggregator extends InterruptContextProvider {
    public InterruptAggregator(ContextComponent provider) {
        super(provider);
    }

    public final void start(Listener listener, int operation) {
        CaLogger.trace();
        ((Aggregator) this.mProvider).initializeFaultDetectionResult();
        ((Aggregator) this.mProvider).registerObserver();
        Iterator<ContextComponent> i = ((Aggregator) this.mProvider).getSubCollectors().iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.start(listener, operation);
            }
        }
        super.start(listener, operation);
    }

    public final void stop(Listener listener, int operation) {
        CaLogger.trace();
        ((Aggregator) this.mProvider).initializeFaultDetectionResult();
        Iterator<ContextComponent> i = ((Aggregator) this.mProvider).getSubCollectors().iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.stop(listener, operation);
            }
        }
        super.stop(listener, operation);
    }
}
