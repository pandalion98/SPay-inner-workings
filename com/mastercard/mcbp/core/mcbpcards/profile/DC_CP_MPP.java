package com.mastercard.mcbp.core.mcbpcards.profile;

public class DC_CP_MPP {
    private CardRiskManagementData cardRiskManagementData;
    private ContactlessPaymentData contactlessPaymentData;
    private RemotePaymentData remotePaymentData;

    public ContactlessPaymentData getContactlessPaymentData() {
        return this.contactlessPaymentData;
    }

    public void setContactlessPaymentData(ContactlessPaymentData contactlessPaymentData) {
        this.contactlessPaymentData = contactlessPaymentData;
    }

    public RemotePaymentData getRemotePaymentData() {
        return this.remotePaymentData;
    }

    public void setRemotePaymentData(RemotePaymentData remotePaymentData) {
        this.remotePaymentData = remotePaymentData;
    }

    public CardRiskManagementData getCardRiskManagementData() {
        return this.cardRiskManagementData;
    }

    public void setCardRiskManagementData(CardRiskManagementData cardRiskManagementData) {
        this.cardRiskManagementData = cardRiskManagementData;
    }

    public void wipe() {
        if (this.contactlessPaymentData != null) {
            this.contactlessPaymentData.wipe();
        }
        if (this.remotePaymentData != null) {
            this.remotePaymentData.wipe();
        }
        if (this.cardRiskManagementData != null) {
            this.cardRiskManagementData.wipe();
        }
    }
}
