/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;

public class WalletInfo
extends Id {
    private int since;

    public WalletInfo(String string) {
        super(string);
    }

    void setSince(int n2) {
        this.since = n2;
    }
}

