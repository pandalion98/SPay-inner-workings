package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;

abstract class RestoreTransaction {
    private final ContextManager mContextManager;

    protected abstract String getRestoreType();

    protected abstract void runRestore(Listener listener, int i, IContextObserver iContextObserver);

    protected RestoreTransaction(ContextManager manager) {
        this.mContextManager = manager;
    }

    protected final ContextManager getContextManager() {
        return this.mContextManager;
    }
}
