package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Initiator {
    private String feature;
    private String id;

    public Initiator(String str, String str2) {
        this.id = str;
        this.feature = str2;
    }

    public String getId() {
        return this.id;
    }

    public String getFeature() {
        return this.feature;
    }
}
