package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.ConcurrentHashMap;

public class RestoreManager {
    public static final String REGISTER_CMD_RESTORE = "REGISTER_CMD_RESTORE";
    public static final String UNREGISTER_CMD_RESTORE = "UNREGISTER_CMD_RESTORE";
    private boolean mIsRestore = false;
    private ConcurrentHashMap<String, RestoreTransaction> mRestoreTransaction = new ConcurrentHashMap();

    protected RestoreManager(ContextManager manager) {
        this.mRestoreTransaction.put(REGISTER_CMD_RESTORE, new RegisterCmdOperationRestore(manager));
        this.mRestoreTransaction.put(UNREGISTER_CMD_RESTORE, new UnregisterCmdOperationRestore(manager));
    }

    protected final void initializeManager() {
        this.mIsRestore = false;
    }

    protected final void setRestoreEnable() {
        this.mIsRestore = true;
    }

    protected final boolean isRestoreEnable() {
        return this.mIsRestore;
    }

    protected final void runRestore(String operation, Listener listener, int service, IContextObserver observer) {
        if (this.mIsRestore) {
            for (RestoreTransaction i : this.mRestoreTransaction.values()) {
                if (operation.equals(i.getRestoreType())) {
                    i.runRestore(listener, service, observer);
                    return;
                }
            }
            return;
        }
        CaLogger.error("mIsRestore is false");
    }
}
