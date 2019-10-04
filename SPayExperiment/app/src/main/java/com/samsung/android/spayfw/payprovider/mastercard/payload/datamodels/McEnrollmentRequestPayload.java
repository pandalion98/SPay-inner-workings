/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfoWrapper;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McDeviceInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSeInfo;
import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McSpsdInfo;

public class McEnrollmentRequestPayload {
    private McCardInfoWrapper cardInfo;
    private String cardletId;
    private McDeviceInfo deviceInfo;
    private String paymentAppInstanceId;
    private McSeInfo seInfo;
    private McSpsdInfo spsdInfo;
    private String tokenizationAuthenticationValue;

    public McCardInfoWrapper getCardInfo() {
        return this.cardInfo;
    }

    public String getCardletId() {
        return this.cardletId;
    }

    public McDeviceInfo getDevice() {
        return this.deviceInfo;
    }

    public String getPaymentAppInstanceID() {
        return this.paymentAppInstanceId;
    }

    public McSeInfo getSeInfo() {
        return this.seInfo;
    }

    public McSpsdInfo getSpsdInfo() {
        return this.spsdInfo;
    }

    public String getTokenizationAuthenticationValue() {
        return this.tokenizationAuthenticationValue;
    }

    public void setCardInfo(McCardInfoWrapper mcCardInfoWrapper) {
        this.cardInfo = mcCardInfoWrapper;
    }

    public void setCardletId(String string) {
        this.cardletId = string;
    }

    public void setDevice(McDeviceInfo mcDeviceInfo) {
        this.deviceInfo = mcDeviceInfo;
    }

    public void setPaymentAppInstanceID(String string) {
        this.paymentAppInstanceId = string;
    }

    public void setSeInfo(McSeInfo mcSeInfo) {
        this.seInfo = mcSeInfo;
    }

    public void setSpsdInfo(McSpsdInfo mcSpsdInfo) {
        this.spsdInfo = mcSpsdInfo;
    }

    public void setTokenizationAuthenticationValue(String string) {
        this.tokenizationAuthenticationValue = string;
    }
}

