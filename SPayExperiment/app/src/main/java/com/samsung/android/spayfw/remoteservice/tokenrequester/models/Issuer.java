/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Name;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Url;

public class Issuer
extends Name {
    private Url logo;

    public Issuer(String string) {
        super(string);
    }

    public Url getLogo() {
        return this.logo;
    }

    public void setLogo(Url url) {
        this.logo = url;
    }
}

