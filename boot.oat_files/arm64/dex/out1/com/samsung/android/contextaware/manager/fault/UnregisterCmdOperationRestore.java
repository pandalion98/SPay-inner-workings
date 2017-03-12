package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class UnregisterCmdOperationRestore extends RestoreTransaction {
    protected UnregisterCmdOperationRestore(ContextManager manager) {
        super(manager);
    }

    protected final void runRestore(Listener listener, int service, IContextObserver observer) {
        if (listener == null) {
            CaLogger.error("listener is null");
            return;
        }
        CaLogger.trace();
        getContextManager().start(listener, ContextList.getInstance().getServiceCode(service), observer, 2);
        if (listener.getServices().containsKey(Integer.valueOf(service))) {
            listener.increaseServiceCount(service);
        } else {
            listener.getServices().put(Integer.valueOf(service), Integer.valueOf(1));
        }
    }

    protected final String getRestoreType() {
        return RestoreManager.UNREGISTER_CMD_RESTORE;
    }
}
