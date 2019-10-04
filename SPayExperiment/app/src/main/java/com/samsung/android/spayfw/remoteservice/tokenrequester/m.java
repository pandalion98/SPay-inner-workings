/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;

public abstract class m<U, V, W extends c<V>, T extends Request<U, V, W, T>>
extends Request<U, V, W, T> {
    protected m(Client client, Client.HttpRequest.RequestMethod requestMethod, U u2) {
        super(client, requestMethod, u2);
    }

    public void bk(String string) {
        this.addHeader("Device-Tokens", string);
    }

    public void bl(String string) {
        this.addHeader("x-smps-dmid", string);
    }

    public String getCardBrand() {
        return this.bg("Payment-Type");
    }

    public void setCardBrand(String string) {
        this.addHeader("Payment-Type", string);
    }
}

