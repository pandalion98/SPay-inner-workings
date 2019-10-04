/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;

public class TokenReport
extends Id {
    public static final String ERROR_PAYMENT_FRAMEWORK = "ERROR-20000";
    public static final String ERROR_PAYMENT_FRAMEWORK_DATA = "ERROR-21000";
    public static final String ERROR_PAYMENT_NETWORK_LIBRARY = "ERROR-40000";
    public static final String ERROR_PAYMENT_NETWORK_LIBRARY_AUTHENTICATION = "ERROR-41000";
    public static final String ERROR_SYSTEM = "ERROR-50000";
    public static final String ERROR_TRUSTED_APP = "ERROR-30000";
    public static final String ERROR_WALLET_APPLICATION = "ERROR-10000";
    private JsonObject data;
    private ErrorReport error;
    private String event;
    private transient String notificationId;
    private String status;
    private long timestamp;

    public TokenReport(String string, String string2, String string3) {
        super(string2);
        this.status = string3;
        this.timestamp = System.currentTimeMillis();
        this.notificationId = string;
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public void setError(ErrorReport errorReport) {
        this.error = errorReport;
    }

    public void setEvent(String string) {
        this.event = string;
    }
}

