package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.f */
public class ProvisionResponse extends Response<TokenResponseData> {
    private IdvOptionsData Bf;

    protected ProvisionResponse(ErrorResponseData errorResponseData, int i) {
        super(errorResponseData, null, i);
    }

    protected ProvisionResponse(IdvOptionsData idvOptionsData, TokenResponseData tokenResponseData, int i) {
        super(null, tokenResponseData, i);
        this.Bf = idvOptionsData;
    }

    protected ProvisionResponse(TokenResponseData tokenResponseData, int i) {
        super(null, tokenResponseData, i);
    }

    public IdvOptionsData fd() {
        return this.Bf;
    }
}
