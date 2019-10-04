/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mcbp.core.mcbpcards.profile.CardRiskManagementData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;

public class DC_CP_MPP {
    private CardRiskManagementData cardRiskManagementData;
    private ContactlessPaymentData contactlessPaymentData;
    private RemotePaymentData remotePaymentData;

    public CardRiskManagementData getCardRiskManagementData() {
        return this.cardRiskManagementData;
    }

    public ContactlessPaymentData getContactlessPaymentData() {
        return this.contactlessPaymentData;
    }

    public RemotePaymentData getRemotePaymentData() {
        return this.remotePaymentData;
    }

    public void setCardRiskManagementData(CardRiskManagementData cardRiskManagementData) {
        this.cardRiskManagementData = cardRiskManagementData;
    }

    public void setContactlessPaymentData(ContactlessPaymentData contactlessPaymentData) {
        this.contactlessPaymentData = contactlessPaymentData;
    }

    public void setRemotePaymentData(RemotePaymentData remotePaymentData) {
        this.remotePaymentData = remotePaymentData;
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

