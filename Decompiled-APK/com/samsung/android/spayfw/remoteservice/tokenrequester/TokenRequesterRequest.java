package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.m */
public abstract class TokenRequesterRequest<U, V, W extends Response<V>, T extends Request<U, V, W, T>> extends Request<U, V, W, T> {
    protected TokenRequesterRequest(Client client, RequestMethod requestMethod, U u) {
        super(client, requestMethod, u);
    }

    public void bk(String str) {
        addHeader("Device-Tokens", str);
    }

    public String getCardBrand() {
        return bg("Payment-Type");
    }

    public void setCardBrand(String str) {
        addHeader("Payment-Type", str);
    }

    public void bl(String str) {
        addHeader("x-smps-dmid", str);
    }
}
