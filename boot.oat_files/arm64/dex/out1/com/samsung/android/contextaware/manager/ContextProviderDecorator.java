package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;

abstract class ContextProviderDecorator extends ContextComponent {
    protected final ContextComponent mProvider;

    public abstract void start(Listener listener, int i);

    public abstract void stop(Listener listener, int i);

    protected ContextProviderDecorator(ContextComponent provider) {
        this.mProvider = provider;
    }

    public final ContextProvider getContextProvider() {
        return this.mProvider.getContextProvider();
    }
}
