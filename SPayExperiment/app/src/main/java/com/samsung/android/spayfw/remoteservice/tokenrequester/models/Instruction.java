/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Instruction {
    private String op;
    private String path;
    private String value;

    public String getOp() {
        return this.op;
    }

    public String getPath() {
        return this.path;
    }

    public String getValue() {
        return this.value;
    }

    public void setOp(String string) {
        this.op = string;
    }

    public void setPath(String string) {
        this.path = string;
    }

    public void setValue(String string) {
        this.value = string;
    }
}

