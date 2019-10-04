/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice;

import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

public class d
extends c<String> {
    private String AI;

    protected d(String string, String string2, int n2) {
        super(null, string2, n2);
        this.AI = string;
    }

    public String getFilePath() {
        return this.AI;
    }
}

