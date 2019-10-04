/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.DevicePublicKeyContext;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class RefreshCredentialsRequestData {
    private DevicePublicKeyContext devicePublicKeyContext;
    private SecureContext secureContext;
    private String tokenId;

    public DevicePublicKeyContext getDevicePublicKeyContext() {
        return this.devicePublicKeyContext;
    }

    public SecureContext getRequestContext() {
        return this.secureContext;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setDevicePublicKeyContext(DevicePublicKeyContext devicePublicKeyContext) {
        this.devicePublicKeyContext = devicePublicKeyContext;
    }

    public void setRequestContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }
}

