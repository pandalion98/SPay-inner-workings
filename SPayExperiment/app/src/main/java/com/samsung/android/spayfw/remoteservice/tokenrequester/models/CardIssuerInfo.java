/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerAppInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ContactInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Name;

public class CardIssuerInfo
extends Name {
    private CardIssuerAppInfo app;
    private ContactInfo contacts;
    private Eula privacy;
    private Eula tnc;

    public CardIssuerInfo(String string) {
        super(string);
    }

    public CardIssuerAppInfo getApp() {
        return this.app;
    }

    public ContactInfo getContacts() {
        return this.contacts;
    }

    public Eula getPrivacy() {
        return this.privacy;
    }

    public Eula getTnc() {
        return this.tnc;
    }
}

