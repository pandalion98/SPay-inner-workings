package com.americanexpress.mobilepayments.hceclient.utils.common;

public class TLSClearTokenData {
    private static TLSClearTokenData tlsClearTokenData;
    private String clearTokenData;

    static {
        tlsClearTokenData = new TLSClearTokenData();
    }

    private TLSClearTokenData() {
    }

    public static synchronized TLSClearTokenData getInstance() {
        TLSClearTokenData tLSClearTokenData;
        synchronized (TLSClearTokenData.class) {
            if (tlsClearTokenData == null) {
                tlsClearTokenData = new TLSClearTokenData();
            }
            tLSClearTokenData = tlsClearTokenData;
        }
        return tLSClearTokenData;
    }

    public String getClearTokenData() {
        return this.clearTokenData;
    }

    public void setClearTokenData(String str) {
        this.clearTokenData = str;
    }
}
