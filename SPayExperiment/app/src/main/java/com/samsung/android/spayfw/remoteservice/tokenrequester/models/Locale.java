/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Locale {
    private String country;
    private String language;
    private String variant;

    public Locale(String string, String string2) {
        this.language = string;
        this.country = string2;
    }

    public void setVariant(String string) {
        this.variant = string;
    }
}

