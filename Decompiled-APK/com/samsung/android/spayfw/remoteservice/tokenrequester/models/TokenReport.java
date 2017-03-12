package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class TokenReport extends Id {
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

    public TokenReport(String str, String str2, String str3) {
        super(str2);
        this.status = str3;
        this.timestamp = System.currentTimeMillis();
        this.notificationId = str;
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

    public void setEvent(String str) {
        this.event = str;
    }
}
