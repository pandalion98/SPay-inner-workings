/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Initiator {
    private String feature;
    private String id;

    public Initiator(String string, String string2) {
        this.id = string;
        this.feature = string2;
    }

    public String getFeature() {
        return this.feature;
    }

    public String getId() {
        return this.id;
    }
}

