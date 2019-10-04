/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.SecureContext;

public class AccountEligibilityRequestData {
    private SecureContext secureContext;

    public SecureContext getSecureCardContext() {
        return this.secureContext;
    }

    public void setSecureCardContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }
}

