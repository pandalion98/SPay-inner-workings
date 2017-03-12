package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class DevicePublicKeyContext {
    private String publicKeyCertificateChain;
    private String publicKeyModulus;

    public DevicePublicKeyContext() {
        this.publicKeyModulus = null;
    }

    public String getPublicKeyModulus() {
        return this.publicKeyModulus;
    }

    public void setPublicKeyModulus(String str) {
        this.publicKeyModulus = str;
    }

    public String getPublicKeyCertificateChain() {
        return this.publicKeyCertificateChain;
    }

    public void setPublicKeyCertificateChain(String str) {
        this.publicKeyCertificateChain = str;
    }
}
