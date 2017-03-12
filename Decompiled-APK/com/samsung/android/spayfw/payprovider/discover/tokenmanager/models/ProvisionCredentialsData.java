package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class ProvisionCredentialsData {
    private String bundleSeqNum;
    private SecureContext secureContext;
    private String tokenId;

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public String getBundleSeqNum() {
        return this.bundleSeqNum;
    }

    public void setBundleSeqNum(String str) {
        this.bundleSeqNum = str;
    }

    public SecureContext getSecureProvisionCredentialsRequestContext() {
        return this.secureContext;
    }

    public void setSecureProvisionCredentialsRequestContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }
}
