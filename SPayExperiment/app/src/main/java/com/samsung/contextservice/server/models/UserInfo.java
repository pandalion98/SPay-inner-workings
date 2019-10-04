/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextservice.server.models.Id;
import com.samsung.contextservice.server.models.UserName;

public class UserInfo
extends Id {
    private String country;
    private String language;
    private UserName name;

    public UserInfo(String string) {
        super(string);
    }

    public UserName getName() {
        return this.name;
    }

    public void setCountry(String string) {
        this.country = string;
    }

    public void setLanguage(String string) {
        this.language = string;
    }

    public void setName(UserName userName) {
        this.name = userName;
    }

    public String toString() {
        return this.getId();
    }
}

