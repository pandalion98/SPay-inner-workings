/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class PatchData {
    String op = "replace";
    String path;
    Object value;

    public void setOp(String string) {
        this.op = string;
    }

    public void setPath(String string) {
        this.path = string;
    }

    public void setValue(Object object) {
        this.value = object;
    }
}

