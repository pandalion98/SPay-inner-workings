package com.samsung.android.contextaware.manager.fault;

import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextManager;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class FaultDetectionManager {
    private static volatile FaultDetectionManager instance;
    private CmdProcessResultManager mCmdProcessResultManager;
    private RestoreManager mRestoreManager;

    public static FaultDetectionManager getInstance() {
        if (instance == null) {
            synchronized (FaultDetectionManager.class) {
                if (instance == null) {
                    instance = new FaultDetectionManager();
                }
            }
        }
        return instance;
    }

    public final void initializeManager(ContextManager manager) {
        this.mRestoreManager = new RestoreManager(manager);
    }

    public final void terminateManager() {
        this.mRestoreManager = null;
    }

    public final void registerCmdProcessResultManager(CmdProcessResultManager manager) {
        if (this.mCmdProcessResultManager != null) {
            CaLogger.warning("mCmdProcessResultManager is already registered");
        } else {
            this.mCmdProcessResultManager = manager;
        }
    }

    public final void unregisterCmdProcessResultManager() {
        this.mCmdProcessResultManager = null;
    }

    public final ICmdProcessResultObserver getCmdProcessResultObserver() {
        return this.mCmdProcessResultManager;
    }

    public final void initializeRestoreManager() {
        if (this.mRestoreManager == null) {
            CaLogger.error("mRestoreManager is null");
        } else {
            this.mRestoreManager.initializeManager();
        }
    }

    public final void setRestoreEnable() {
        if (this.mRestoreManager == null) {
            CaLogger.error("mRestoreManager is null");
        } else {
            this.mRestoreManager.setRestoreEnable();
        }
    }

    public final boolean isRestoreEnable() {
        if (this.mRestoreManager != null) {
            return this.mRestoreManager.isRestoreEnable();
        }
        CaLogger.error("mRestoreManager is null");
        return false;
    }

    public final void runRestore(String operation, Listener listener, int service, IContextObserver observer) {
        if (this.mRestoreManager == null) {
            CaLogger.error("mRestoreManager is null");
        } else {
            this.mRestoreManager.runRestore(operation, listener, service, observer);
        }
    }

    public final void updateContextAwareServiceFatalError() {
    }

    public final void updateSensorHubFatalError() {
    }
}
