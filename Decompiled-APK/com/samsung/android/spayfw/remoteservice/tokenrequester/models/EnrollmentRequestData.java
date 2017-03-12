package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

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

    public void setPresentation(Mode mode) {
        this.presentation = mode;
    }

    public void setUser(UserInfo userInfo) {
        this.user = userInfo;
    }

    public void setWallet(WalletInfo walletInfo) {
        this.wallet = walletInfo;
    }

    public Initiator getInitiator() {
        return this.initiator;
    }

    public void setInitiator(Initiator initiator) {
        this.initiator = initiator;
    }
}
