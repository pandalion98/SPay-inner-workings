package com.samsung.android.contextaware.manager.fault;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.manager.ContextAwareService.ServiceHandler;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CmdProcessResultManager implements ICmdProcessResultObserver {
    private final IBinder mBinder;
    private final ServiceHandler mServiceHandler;

    public CmdProcessResultManager(IBinder binder, ServiceHandler handler) {
        this.mBinder = binder;
        this.mServiceHandler = handler;
    }

    public final void updateCmdProcessResult(String type, Bundle context) {
        if (!type.equals(ContextType.CMD_PROCESS_FAULT_DETECTION.getCode())) {
            return;
        }
        if (this.mBinder == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_BINDER_NULL_EXCEPTION.getMessage());
        } else if (this.mServiceHandler == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_SERVICE_HANDLER_NULL_EXCEPTION.getMessage());
        } else if (context == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_CONTEXT_INFO_NULL_EXCEPTION.getMessage());
        } else {
            Bundle tmpContext = (Bundle) context.clone();
            if (tmpContext.getInt("CheckResult") != 0) {
                FaultDetectionManager.getInstance().setRestoreEnable();
            }
            Bundle bundle = new Bundle();
            bundle.putIBinder("Binder", this.mBinder);
            bundle.putInt("Service", tmpContext.getInt("Service"));
            tmpContext.putBundle("Listener", bundle);
            Message msg = Message.obtain();
            msg.what = ContextType.CMD_PROCESS_FAULT_DETECTION.ordinal();
            msg.obj = tmpContext;
            this.mServiceHandler.sendMessage(msg);
        }
    }
}
