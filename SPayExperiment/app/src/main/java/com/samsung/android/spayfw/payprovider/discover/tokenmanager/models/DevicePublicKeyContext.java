/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class DevicePublicKeyContext {
    private String publicKeyCertificateChain;
    private String publicKeyModulus = null;

    public String getPublicKeyCertificateChain() {
        return this.publicKeyCertificateChain;
    }

    public String getPublicKeyModulus() {
        return this.publicKeyModulus;
    }

    public void setPublicKeyCertificateChain(String string) {
        this.publicKeyCertificateChain = string;
    }

    public void setPublicKeyModulus(String string) {
        this.publicKeyModulus = string;
    }
}

