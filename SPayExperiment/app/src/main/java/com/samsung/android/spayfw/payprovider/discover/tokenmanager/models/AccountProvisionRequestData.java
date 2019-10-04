/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.DevicePublicKeyContext;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class AccountProvisionRequestData {
    private DevicePublicKeyContext devicePublicKeyContext;
    private SecureContext secureAccountContext;

    public DevicePublicKeyContext getDevicePublicKeyContext() {
        return this.devicePublicKeyContext;
    }

    public SecureContext getSecureCardContext() {
        return this.secureAccountContext;
    }

    @Deprecated
    public SecureContext getSecureRSAPublicKeyContext() {
        return null;
    }

    public void setDevicePublicKeyContext(DevicePublicKeyContext devicePublicKeyContext) {
        this.devicePublicKeyContext = devicePublicKeyContext;
    }

    public void setSecureCardContext(SecureContext secureContext) {
        this.secureAccountContext = secureContext;
    }

    @Deprecated
    public void setSecureRSAPublicKeyContext(SecureContext secureContext) {
    }
}

