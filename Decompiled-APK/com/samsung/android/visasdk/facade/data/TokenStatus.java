package com.samsung.android.visasdk.facade.data;

public enum TokenStatus {
    ACTIVE(com.samsung.android.spayfw.appinterface.TokenStatus.ACTIVE),
    INACTIVE("INACTIVE"),
    RESUME("RESUME"),
    SUSPENDED(com.samsung.android.spayfw.appinterface.TokenStatus.SUSPENDED),
    DELETED("DELETED"),
    NOT_FOUND("NOT_FOUND");
    
    private final String text;

    private TokenStatus(String str) {
        this.text = str;
    }

    public String getStatus() {
        return this.text;
    }

    public String toString() {
        return this.text;
    }
}
