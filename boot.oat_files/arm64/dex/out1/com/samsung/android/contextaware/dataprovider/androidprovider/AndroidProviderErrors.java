package com.samsung.android.contextaware.dataprovider.androidprovider;

import com.samsung.android.contextaware.manager.fault.IContextAwareErrors;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public enum AndroidProviderErrors implements IContextAwareErrors {
    SUCCESS("Success"),
    ERROR_UNKNOWN("ERROR : Unknown");
    
    private String message;

    private AndroidProviderErrors(String message) {
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
        for (AndroidProviderErrors i : values()) {
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
