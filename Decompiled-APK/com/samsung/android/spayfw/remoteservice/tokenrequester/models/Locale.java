package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Locale {
    private String country;
    private String language;
    private String variant;

    public Locale(String str, String str2) {
        this.language = str;
        this.country = str2;
    }

    public void setVariant(String str) {
        this.variant = str;
    }
}
