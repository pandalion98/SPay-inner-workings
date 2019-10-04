/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Type;

public class StorageInfo
extends Type {
    private String id;

    public StorageInfo() {
        super("TEE");
    }

    public void setId(String string) {
        this.id = string;
    }
}

