package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class AccountProvisionRequestData {
    private DevicePublicKeyContext devicePublicKeyContext;
    private SecureContext secureAccountContext;

    @Deprecated
    public SecureContext getSecureRSAPublicKeyContext() {
        return null;
    }

    @Deprecated
    public void setSecureRSAPublicKeyContext(SecureContext secureContext) {
    }

    public SecureContext getSecureCardContext() {
        return this.secureAccountContext;
    }

    public void setSecureCardContext(SecureContext secureContext) {
        this.secureAccountContext = secureContext;
    }

    public DevicePublicKeyContext getDevicePublicKeyContext() {
        return this.devicePublicKeyContext;
    }

    public void setDevicePublicKeyContext(DevicePublicKeyContext devicePublicKeyContext) {
        this.devicePublicKeyContext = devicePublicKeyContext;
    }
}
