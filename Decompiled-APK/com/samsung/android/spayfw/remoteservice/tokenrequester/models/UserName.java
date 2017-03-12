package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class UserName {
    private String first;
    private String full;
    private String last;

    public UserName(String str) {
        this.first = null;
        this.last = null;
        this.full = str;
    }

    public UserName(String str, String str2) {
        this.first = str;
        this.last = str2;
        this.full = null;
    }

    public String getFull() {
        return this.full;
    }
}
