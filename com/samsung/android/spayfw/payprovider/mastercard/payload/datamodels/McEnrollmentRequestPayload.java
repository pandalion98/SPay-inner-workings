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

    public void setDevice(McDeviceInfo mcDeviceInfo) {
        this.deviceInfo = mcDeviceInfo;
    }

    public void setSeInfo(McSeInfo mcSeInfo) {
        this.seInfo = mcSeInfo;
    }

    public void setCardInfo(McCardInfoWrapper mcCardInfoWrapper) {
        this.cardInfo = mcCardInfoWrapper;
    }

    public void setCardletId(String str) {
        this.cardletId = str;
    }

    public void setSpsdInfo(McSpsdInfo mcSpsdInfo) {
        this.spsdInfo = mcSpsdInfo;
    }

    public void setPaymentAppInstanceID(String str) {
        this.paymentAppInstanceId = str;
    }

    public void setTokenizationAuthenticationValue(String str) {
        this.tokenizationAuthenticationValue = str;
    }

    public McDeviceInfo getDevice() {
        return this.deviceInfo;
    }

    public McSeInfo getSeInfo() {
        return this.seInfo;
    }

    public McCardInfoWrapper getCardInfo() {
        return this.cardInfo;
    }

    public String getCardletId() {
        return this.cardletId;
    }

    public McSpsdInfo getSpsdInfo() {
        return this.spsdInfo;
    }

    public String getPaymentAppInstanceID() {
        return this.paymentAppInstanceId;
    }

    public String getTokenizationAuthenticationValue() {
        return this.tokenizationAuthenticationValue;
    }
}
