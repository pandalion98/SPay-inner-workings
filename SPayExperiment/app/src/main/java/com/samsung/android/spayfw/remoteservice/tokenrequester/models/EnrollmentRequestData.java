/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardEnrollmentInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Initiator;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Mode;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.WalletInfo;

public class EnrollmentRequestData {
    private Id app;
    private CardEnrollmentInfo card;
    private DeviceInfo device;
    private Mode entry;
    private Initiator initiator;
    private Mode presentation;
    private UserInfo user;
    private WalletInfo wallet;

    public CardEnrollmentInfo getCard() {
        return this.card;
    }

    public Initiator getInitiator() {
        return this.initiator;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setApp(Id id) {
        this.app = id;
    }

    public void setCard(CardEnrollmentInfo cardEnrollmentInfo) {
        this.card = cardEnrollmentInfo;
    }

    public void setDevice(DeviceInfo deviceInfo) {
        this.device = deviceInfo;
    }

    public void setEntry(Mode mode) {
        this.entry = mode;
    }

    public void setInitiator(Initiator initiator) {
        this.initiator = initiator;
    }

    public void setPresentation(Mode mode) {
        this.presentation = mode;
    }

    public void setUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    public void setWallet(WalletInfo walletInfo) {
        this.wallet = walletInfo;
    }
}

