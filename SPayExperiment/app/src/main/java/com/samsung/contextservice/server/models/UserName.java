/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.server.models;

public class UserName {
    private String first;
    private String full;
    private String last;

    public UserName(String string) {
        this.first = null;
        this.last = null;
        this.full = string;
    }

    public UserName(String string, String string2) {
        this.first = string;
        this.last = string2;
        this.full = null;
    }

    public String getFull() {
        return this.full;
    }
}

