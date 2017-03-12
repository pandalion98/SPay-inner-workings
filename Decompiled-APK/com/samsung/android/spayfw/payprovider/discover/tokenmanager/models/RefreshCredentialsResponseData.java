package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class RefreshCredentialsResponseData {
    private String bundleSeqNum;
    private SecureContext secureContext;
    private String tokenId;

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public SecureContext getResponseContext() {
        return this.secureContext;
    }

    public void setResponseContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }

    public String getBundleSeqNum() {
        return this.bundleSeqNum;
    }

    public void setBundleSeqNum(String str) {
        this.bundleSeqNum = str;
    }
}
