package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McDeviceInfo {
    private String applicationCertificateHash;
    private boolean nfcCapable;
    private String osName;
    private String osVersion;

    public void setOsName(String str) {
        this.osName = str;
    }

    public void setOsVersion(String str) {
        this.osVersion = str;
    }

    public void setNfcCapable(boolean z) {
        this.nfcCapable = z;
    }

    public void setApplicationCertificateHash(String str) {
        this.applicationCertificateHash = str;
    }

    public String getOsName() {
        return this.osName;
    }

    public String getOsVersion(String str) {
        return str;
    }

    public boolean getNfcCapable(boolean z) {
        return z;
    }

    public String getApplicationCertificateHash() {
        return this.applicationCertificateHash;
    }
}
