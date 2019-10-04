/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class RemotePaymentData {
    private ByteArray AIP;
    private ByteArray CIAC_Decline;
    private ByteArray CVR_MaskAnd;
    private ByteArray PAN;
    private ByteArray PAN_SequenceNumber;
    private ByteArray applicationExpiryDate;
    private ByteArray issuerApplicationData;
    private ByteArray paymentAccountReference;
    private ByteArray track2_equivalentData;

    public ByteArray getAIP() {
        return this.AIP;
    }

    public ByteArray getApplicationExpiryDate() {
        return this.applicationExpiryDate;
    }

    public ByteArray getCIAC_Decline() {
        return this.CIAC_Decline;
    }

    public ByteArray getCVR_MaskAnd() {
        return this.CVR_MaskAnd;
    }

    public ByteArray getIssuerApplicationData() {
        return this.issuerApplicationData;
    }

    public ByteArray getPAN() {
        return this.PAN;
    }

    public ByteArray getPAN_SequenceNumber() {
        return this.PAN_SequenceNumber;
    }

    public ByteArray getPaymentAccountReference() {
        return this.paymentAccountReference;
    }

    public ByteArray getTrack2_equivalentData() {
        return this.track2_equivalentData;
    }

    public void setAIP(ByteArray byteArray) {
        this.AIP = byteArray;
    }

    public void setApplicationExpiryDate(ByteArray byteArray) {
        this.applicationExpiryDate = byteArray;
    }

    public void setCIAC_Decline(ByteArray byteArray) {
        this.CIAC_Decline = byteArray;
    }

    public void setCVR_MASK_AND(ByteArray byteArray) {
        this.CVR_MaskAnd = byteArray;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.issuerApplicationData = byteArray;
    }

    public void setPAN(ByteArray byteArray) {
        this.PAN = byteArray;
    }

    public void setPANSequenceNumber(ByteArray byteArray) {
        this.PAN_SequenceNumber = byteArray;
    }

    public void setPaymentAccountReference(ByteArray byteArray) {
        this.paymentAccountReference = byteArray;
    }

    public void setTrack2EquivalentData(ByteArray byteArray) {
        this.track2_equivalentData = byteArray;
    }

    public void wipe() {
        Utils.clearByteArray(this.AIP);
        Utils.clearByteArray(this.applicationExpiryDate);
        Utils.clearByteArray(this.CIAC_Decline);
        Utils.clearByteArray(this.CVR_MaskAnd);
        Utils.clearByteArray(this.issuerApplicationData);
        Utils.clearByteArray(this.PAN);
        Utils.clearByteArray(this.PAN_SequenceNumber);
        Utils.clearByteArray(this.track2_equivalentData);
        Utils.clearByteArray(this.paymentAccountReference);
    }
}

