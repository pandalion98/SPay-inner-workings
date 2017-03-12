package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class SimInfo extends Id {
    private String key;

    public SimInfo(String str) {
        super(str);
    }

    public void setKey(String str) {
        this.key = str;
    }
}
