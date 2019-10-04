/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserName;

public class UserInfo
extends Id {
    private String country;
    private String hash;
    private String language;
    private String mask;
    private UserName name;
    private int score = 3;
    private int since;

    public UserInfo(String string) {
        super(string);
    }

    public UserName getName() {
        return this.name;
    }

    public void setCountry(String string) {
        this.country = string;
    }

    public void setHash(String string) {
        this.hash = string;
    }

    public void setLanguage(String string) {
        this.language = string;
    }

    public void setMask(String string) {
        this.mask = string;
    }

    public void setName(UserName userName) {
        this.name = userName;
    }

    public void setScore(int n2) {
        this.score = n2;
    }

    public void setSince(int n2) {
        this.since = n2;
    }
}

