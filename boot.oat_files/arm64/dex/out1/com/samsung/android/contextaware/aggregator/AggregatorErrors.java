package com.samsung.android.contextaware.aggregator;

import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum AggregatorErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown"),
    ERROR_ARRIVING_ON_STATUS_FAULT("ERROR : Status of arriving on POI is fault"),
    ERROR_UPDATED_CONTEXT_TYPE_FAULT("ERROR : Updated context type is fault"),
    ERROR_UPDATED_CONTEXT_NULL_EXCEPTION("ERROR : Updated context is null"),
    ERROR_SUB_COLLECTOR_FALSE("ERROR : Sub collector is false"),
    ERROR_SUB_COLLECTOR_NULL_EXCEPTION("ERROR : Sub collector is null");
    
    private String message;

    private AggregatorErrors(String message) {
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
        for (AggregatorErrors i : values()) {
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
