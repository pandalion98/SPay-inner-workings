/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McDeviceInfo {
    private String applicationCertificateHash;
    private boolean nfcCapable;
    private String osName;
    private String osVersion;

    public String getApplicationCertificateHash() {
        return this.applicationCertificateHash;
    }

    public boolean getNfcCapable(boolean bl) {
        return bl;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getOsVersion(String string) {
        return string;
    }

    public void setApplicationCertificateHash(String string) {
        this.applicationCertificateHash = string;
    }

    public void setNfcCapable(boolean bl) {
        this.nfcCapable = bl;
    }

    public void setOsName(String string) {
        this.osName = string;
    }

    public void setOsVersion(String string) {
        this.osVersion = string;
    }
}

