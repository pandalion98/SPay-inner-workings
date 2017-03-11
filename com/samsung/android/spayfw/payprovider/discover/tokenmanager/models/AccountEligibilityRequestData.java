package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class AccountEligibilityRequestData {
    private SecureContext secureContext;

    public SecureContext getSecureCardContext() {
        return this.secureContext;
    }

    public void setSecureCardContext(SecureContext secureContext) {
        this.secureContext = secureContext;
    }
}
