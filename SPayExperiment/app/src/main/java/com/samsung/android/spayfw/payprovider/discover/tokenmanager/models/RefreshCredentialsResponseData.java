/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class RefreshCredentialsResponseData {
    private String bundleSeqNum;
    private SecureContext secureContext;
    private String tokenId;

    public String getBundleSeqNum() {
        return this.bundleSeqNum;
    }

    public SecureContext getResponseContext() {
        return this.secureContext;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setBundleSeqNum(String string) {
        this.bundleSeqNum = string;
    }

    public void setResponseContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }
}

