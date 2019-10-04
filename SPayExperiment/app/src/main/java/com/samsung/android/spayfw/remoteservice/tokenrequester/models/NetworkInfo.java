/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Name;

public class NetworkInfo
extends Name {
    private NetworkInfo current;
    private String mcc;
    private String mnc;
    private String type;

    public NetworkInfo(String string, String string2, String string3, String string4) {
        super(string2);
        this.type = string;
        this.mcc = string3;
        this.mnc = string4;
    }

    public void setCurrent(NetworkInfo networkInfo) {
        this.current = networkInfo;
    }
}

