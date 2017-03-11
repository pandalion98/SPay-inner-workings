package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class StorageInfo extends Type {
    private String id;

    public StorageInfo() {
        super("TEE");
    }

    public void setId(String str) {
        this.id = str;
    }
}
