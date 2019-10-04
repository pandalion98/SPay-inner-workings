/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Name;

public class OsInfo
extends Name {
    private String build;
    private String id;
    private String version;

    public OsInfo(String string, String string2, String string3) {
        super("ANDROID");
        this.id = string;
        this.version = string2;
        this.build = string3;
    }
}

