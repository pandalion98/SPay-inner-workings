package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class OsInfo extends Name {
    private String build;
    private String id;
    private String version;

    public OsInfo(String str, String str2, String str3) {
        super("ANDROID");
        this.id = str;
        this.version = str2;
        this.build = str3;
    }
}
