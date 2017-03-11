package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class PushProviders {
    private Id gcm;
    private Id spp;

    public void setSpp(Id id) {
        this.spp = id;
    }

    public void setGcm(Id id) {
        this.gcm = id;
    }
}
