/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;

public class CardIssuerAppInfo
extends Id {
    private String callback;
    private String description;
    private String download;
    private Art icon;
    private String name;
    private String store;

    public CardIssuerAppInfo(String string) {
        super(string);
    }

    public String getCallback() {
        return this.callback;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDownload() {
        return this.download;
    }

    public Art getIcon() {
        return this.icon;
    }

    public String getName() {
        return this.name;
    }

    public String getStore() {
        return this.store;
    }
}

