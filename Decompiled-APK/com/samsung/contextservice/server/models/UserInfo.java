package com.samsung.contextservice.server.models;

public class UserInfo extends Id {
    private String country;
    private String language;
    private UserName name;

    public UserInfo(String str) {
        super(str);
    }

    public UserName getName() {
        return this.name;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setLanguage(String str) {
        this.language = str;
    }

    public void setName(UserName userName) {
        this.name = userName;
    }

    public String toString() {
        return getId();
    }
}
