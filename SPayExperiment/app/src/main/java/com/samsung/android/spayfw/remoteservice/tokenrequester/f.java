/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;

public class f
extends c<TokenResponseData> {
    private IdvOptionsData Bf;

    protected f(ErrorResponseData errorResponseData, int n2) {
        super(errorResponseData, null, n2);
    }

    protected f(IdvOptionsData idvOptionsData, TokenResponseData tokenResponseData, int n2) {
        super(null, tokenResponseData, n2);
        this.Bf = idvOptionsData;
    }

    protected f(TokenResponseData tokenResponseData, int n2) {
        super(null, tokenResponseData, n2);
    }

    public IdvOptionsData fd() {
        return this.Bf;
    }
}

