/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

public class TLSClearTokenData {
    private static TLSClearTokenData tlsClearTokenData = new TLSClearTokenData();
    private String clearTokenData;

    private TLSClearTokenData() {
    }

    public static TLSClearTokenData getInstance() {
        Class<TLSClearTokenData> class_ = TLSClearTokenData.class;
        synchronized (TLSClearTokenData.class) {
            if (tlsClearTokenData == null) {
                tlsClearTokenData = new TLSClearTokenData();
            }
            TLSClearTokenData tLSClearTokenData = tlsClearTokenData;
            // ** MonitorExit[var2] (shouldn't be in output)
            return tLSClearTokenData;
        }
    }

    public String getClearTokenData() {
        return this.clearTokenData;
    }

    public void setClearTokenData(String string) {
        this.clearTokenData = string;
    }
}

