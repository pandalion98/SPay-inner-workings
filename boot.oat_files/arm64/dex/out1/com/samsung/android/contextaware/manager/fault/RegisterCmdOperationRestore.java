package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class RegisterCmdOperationRestore extends RestoreTransaction {
    protected RegisterCmdOperationRestore(ContextManager manager) {
        super(manager);
    }

    protected final void runRestore(Listener listener, int service, IContextObserver observer) {
        if (listener == null) {
            CaLogger.error("listener is null");
        } else if (listener.getToken() == null) {
            CaLogger.error("token is null");
        } else {
            CaLogger.trace();
            if (listener.getServices().contains(Integer.valueOf(service))) {
                listener.getServices().remove(Integer.valueOf(service));
            }
            getContextManager().stop(listener, ContextList.getInstance().getServiceCode(service), observer, null, 2);
            if (listener.getServices().isEmpty()) {
                listener.getToken().unlinkToDeath(listener, 0);
                ListenerListManager.getInstance().removeListener(listener);
            }
        }
    }

    protected final String getRestoreType() {
        return RestoreManager.REGISTER_CMD_RESTORE;
    }
}
