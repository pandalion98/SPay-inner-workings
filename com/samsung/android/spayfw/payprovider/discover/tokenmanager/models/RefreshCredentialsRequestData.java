package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class RefreshCredentialsRequestData {
    private DevicePublicKeyContext devicePublicKeyContext;
    private SecureContext secureContext;
    private String tokenId;

    public DevicePublicKeyContext getDevicePublicKeyContext() {
        return this.devicePublicKeyContext;
    }

    public void setDevicePublicKeyContext(DevicePublicKeyContext devicePublicKeyContext) {
        this.devicePublicKeyContext = devicePublicKeyContext;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public SecureContext getRequestContext() {
        return this.secureContext;
    }

    public void setRequestContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }
}
