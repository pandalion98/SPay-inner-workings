package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.models.Eula;

public class CardIssuerInfo extends Name {
    private CardIssuerAppInfo app;
    private ContactInfo contacts;
    private Eula privacy;
    private Eula tnc;

    public CardIssuerInfo(String str) {
        super(str);
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
