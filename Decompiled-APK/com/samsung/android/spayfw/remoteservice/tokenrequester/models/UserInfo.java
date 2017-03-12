package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class UserInfo extends Id {
    private String country;
    private String hash;
    private String language;
    private String mask;
    private UserName name;
    private int score;
    private int since;

    public UserInfo(String str) {
        super(str);
        this.score = 3;
    }

    public UserName getName() {
        return this.name;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setHash(String str) {
        this.hash = str;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public void setMask(String str) {
        this.mask = str;
    }

    public void setName(UserName userName) {
        this.name = userName;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public void setSince(int i) {
        this.since = i;
    }
}
