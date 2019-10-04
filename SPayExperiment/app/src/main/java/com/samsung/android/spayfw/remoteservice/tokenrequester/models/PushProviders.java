/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;

public class PushProviders {
    private Id gcm;
    private Id spp;

    public void setGcm(Id id) {
        this.gcm = id;
    }

    public void setSpp(Id id) {
        this.spp = id;
    }
}

