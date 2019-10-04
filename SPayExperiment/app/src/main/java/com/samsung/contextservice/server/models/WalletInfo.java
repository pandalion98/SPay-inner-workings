/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.contextservice.server.models;

import com.samsung.contextservice.server.models.Id;

public class WalletInfo
extends Id {
    private int since;

    public WalletInfo(String string) {
        super(string);
    }

    void setSince(int n2) {
        this.since = n2;
    }

    public String toString() {
        return this.getId();
    }
}

