package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Environment {
    private Code country;
    private Location location;
    private String mstSequenceId;

    public void setMstSequenceId(String str) {
        this.mstSequenceId = str;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCountry(Code code) {
        this.country = code;
    }
}
