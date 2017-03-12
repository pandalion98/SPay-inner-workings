package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum ContextAwareServiceErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown"),
    ERROR_NOT_SUPPORT_CMD("ERROR : not support commend"),
    ERROR_DEPENDENT_SERVICE_NULL_EXCEPTION("ERROR : Dependent service is null"),
    ERROR_DEPENDENT_SERVICE_NOT_REGISTERED("ERROR : Dependent service isn't registered"),
    ERROR_LISTENER_NOT_REGISTERED("ERROR : Listener isn't registered"),
    ERROR_LISTENER_NULL_EXCEPTION("ERROR : Listener is null"),
    ERROR_BINDER_NULL_EXCEPTION("ERROR : Binder is null"),
    ERROR_LOOPER_NULL_EXCEPTION("ERROR : Looper is null"),
    ERROR_CONTEXT_NULL_EXCEPTION("ERROR : Context is null"),
    ERROR_SERVICE_HANDLER_NULL_EXCEPTION("ERROR : Service handler is null"),
    ERROR_CONTEXT_INFO_NULL_EXCEPTION("ERROR : Context information is null"),
    ERROR_SUB_COLLECTION("ERROR : Sub-Collection operation is fault"),
    ERROR_BUNDLE_NULL_EXCEPTION("ERROR : Bundle is null"),
    ERROR_SERVICE_FAULT("Service is fault"),
    ERROR_VERSION_SETTING("ERROR : Version is already set"),
    ERROR_SERVICE_CODE_NULL_EXCEPTION("ERROR : Service code is null"),
    ERROR_SERVICE_NOT_RUNNING("ERROR : Service is not running"),
    ERROR_CONTEXT_PROVIDER_FAULT("ERROR : Context provider is fault"),
    ERROR_SERVICE_COUNT_FAULT("Service count is fault"),
    ERROR_TIME_OUT("ERROR : Time out");
    
    private String message;

    private ContextAwareServiceErrors(String message) {
        this.message = message;
    }

    public final int getCode() {
        return ordinal();
    }

    public final String getMessage() {
        return this.message;
    }

    public static final String getMessage(int code) {
        String msg = "";
        for (ContextAwareServiceErrors i : values()) {
            if (i.ordinal() == code) {
                msg = i.message;
                break;
            }
        }
        if (msg.isEmpty()) {
            CaLogger.error("Message code is fault");
        }
        return msg;
    }

    public void notifyFatalError() {
    }
}
