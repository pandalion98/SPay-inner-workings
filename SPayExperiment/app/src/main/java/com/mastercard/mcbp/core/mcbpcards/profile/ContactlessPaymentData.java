/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class ContactlessPaymentData {
    private ByteArray AID;
    private int CDOL1_RelatedDataLength;
    private ByteArray CIAC_Decline;
    private ByteArray CIAC_DeclineOnPPMS;
    private ByteArray CVR_MaskAnd;
    private ByteArray GPO_Response;
    private ByteArray ICC_privateKey_a;
    private ByteArray ICC_privateKey_dp;
    private ByteArray ICC_privateKey_dq;
    private ByteArray ICC_privateKey_p;
    private ByteArray ICC_privateKey_q;
    private ByteArray PIN_IV_CVC3_Track2;
    private ByteArray PPSE_FCI;
    private AlternateContactlessPaymentData alternateContactlessPaymentData;
    private ByteArray issuerApplicationData;
    private ByteArray maxRRTime;
    private ByteArray minRRTime;
    private ByteArray paymentFCI;
    private Records[] records;
    private ByteArray trRRTime;

    public ByteArray getAID() {
        return this.AID;
    }

    public AlternateContactlessPaymentData getAlternateContactlessPaymentData() {
        return this.alternateContactlessPaymentData;
    }

    public int getCDOL1_RelatedDataLength() {
        return this.CDOL1_RelatedDataLength;
    }

    public ByteArray getCIAC_Decline() {
        return this.CIAC_Decline;
    }

    public ByteArray getCIAC_DeclineOnPPMS() {
        return this.CIAC_DeclineOnPPMS;
    }

    public ByteArray getCVR_MaskAnd() {
        return this.CVR_MaskAnd;
    }

    public ByteArray getGPO_Response() {
        return this.GPO_Response;
    }

    public ByteArray getICC_privateKey_a() {
        return this.ICC_privateKey_a;
    }

    public ByteArray getICC_privateKey_dp() {
        return this.ICC_privateKey_dp;
    }

    public ByteArray getICC_privateKey_dq() {
        return this.ICC_privateKey_dq;
    }

    public ByteArray getICC_privateKey_p() {
        return this.ICC_privateKey_p;
    }

    public ByteArray getICC_privateKey_q() {
        return this.ICC_privateKey_q;
    }

    public ByteArray getIssuerApplicationData() {
        return this.issuerApplicationData;
    }

    public ByteArray getMaxRRTime() {
        return this.maxRRTime;
    }

    public ByteArray getMinRRTime() {
        return this.minRRTime;
    }

    public ByteArray getPIN_IV_CVC3_Track2() {
        return this.PIN_IV_CVC3_Track2;
    }

    public ByteArray getPPSE_FCI() {
        return this.PPSE_FCI;
    }

    public ByteArray getPaymentFCI() {
        return this.paymentFCI;
    }

    public Records[] getRecords() {
        return this.records;
    }

    public ByteArray getTransmissionRRTime() {
        return this.trRRTime;
    }

    public void setAID(ByteArray byteArray) {
        this.AID = byteArray;
    }

    public void setAlternateContactlessPaymentData(AlternateContactlessPaymentData alternateContactlessPaymentData) {
        this.alternateContactlessPaymentData = alternateContactlessPaymentData;
    }

    public void setCDOL1_RelatedDataLength(int n2) {
        this.CDOL1_RelatedDataLength = n2;
    }

    public void setCIAC_Decline(ByteArray byteArray) {
        this.CIAC_Decline = byteArray;
    }

    public void setCIAC_DeclineOnPPMS(ByteArray byteArray) {
        this.CIAC_DeclineOnPPMS = byteArray;
    }

    public void setCVR_MaskAnd(ByteArray byteArray) {
        this.CVR_MaskAnd = byteArray;
    }

    public void setGPO_Response(ByteArray byteArray) {
        this.GPO_Response = byteArray;
    }

    public void setICC_privateKey_a(ByteArray byteArray) {
        this.ICC_privateKey_a = byteArray;
    }

    public void setICC_privateKey_dp(ByteArray byteArray) {
        this.ICC_privateKey_dp = byteArray;
    }

    public void setICC_privateKey_dq(ByteArray byteArray) {
        this.ICC_privateKey_dq = byteArray;
    }

    public void setICC_privateKey_p(ByteArray byteArray) {
        this.ICC_privateKey_p = byteArray;
    }

    public void setICC_privateKey_q(ByteArray byteArray) {
        this.ICC_privateKey_q = byteArray;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.issuerApplicationData = byteArray;
    }

    public void setMaxRRTime(ByteArray byteArray) {
        this.maxRRTime = byteArray;
    }

    public void setMinRRTime(ByteArray byteArray) {
        this.minRRTime = byteArray;
    }

    public void setPIN_IV_CVC3_Track2(ByteArray byteArray) {
        this.PIN_IV_CVC3_Track2 = byteArray;
    }

    public void setPPSE_FCI(ByteArray byteArray) {
        this.PPSE_FCI = byteArray;
    }

    public void setPaymentFCI(ByteArray byteArray) {
        this.paymentFCI = byteArray;
    }

    public void setRecords(Records[] arrrecords) {
        this.records = arrrecords;
    }

    public void setTransmissionRRTime(ByteArray byteArray) {
        this.trRRTime = byteArray;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void wipe() {
        this.CDOL1_RelatedDataLength = 0;
        this.alternateContactlessPaymentData.wipe();
        Utils.clearByteArray(this.AID);
        Utils.clearByteArray(this.CIAC_Decline);
        Utils.clearByteArray(this.CIAC_DeclineOnPPMS);
        Utils.clearByteArray(this.CVR_MaskAnd);
        Utils.clearByteArray(this.GPO_Response);
        Utils.clearByteArray(this.ICC_privateKey_a);
        Utils.clearByteArray(this.ICC_privateKey_dp);
        Utils.clearByteArray(this.ICC_privateKey_dq);
        Utils.clearByteArray(this.ICC_privateKey_p);
        Utils.clearByteArray(this.ICC_privateKey_q);
        Utils.clearByteArray(this.issuerApplicationData);
        Utils.clearByteArray(this.paymentFCI);
        Utils.clearByteArray(this.PIN_IV_CVC3_Track2);
        Utils.clearByteArray(this.PPSE_FCI);
        Utils.clearByteArray(this.minRRTime);
        Utils.clearByteArray(this.maxRRTime);
        Utils.clearByteArray(this.trRRTime);
        if (this.records == null) return;
        int n2 = this.records.length;
        int n3 = 0;
        if (n2 <= 0) return;
        while (n3 < this.records.length) {
            this.records[n3].wipe();
            ++n3;
        }
        return;
    }
}

