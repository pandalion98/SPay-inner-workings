package com.mastercard.mcbp.core.mcbpcards.profile;

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

    public ByteArray getIssuerApplicationData() {
        return this.issuerApplicationData;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.issuerApplicationData = byteArray;
    }

    public ByteArray getICC_privateKey_a() {
        return this.ICC_privateKey_a;
    }

    public void setICC_privateKey_a(ByteArray byteArray) {
        this.ICC_privateKey_a = byteArray;
    }

    public ByteArray getGPO_Response() {
        return this.GPO_Response;
    }

    public void setGPO_Response(ByteArray byteArray) {
        this.GPO_Response = byteArray;
    }

    public int getCDOL1_RelatedDataLength() {
        return this.CDOL1_RelatedDataLength;
    }

    public void setCDOL1_RelatedDataLength(int i) {
        this.CDOL1_RelatedDataLength = i;
    }

    public ByteArray getCIAC_Decline() {
        return this.CIAC_Decline;
    }

    public void setCIAC_Decline(ByteArray byteArray) {
        this.CIAC_Decline = byteArray;
    }

    public ByteArray getCIAC_DeclineOnPPMS() {
        return this.CIAC_DeclineOnPPMS;
    }

    public void setCIAC_DeclineOnPPMS(ByteArray byteArray) {
        this.CIAC_DeclineOnPPMS = byteArray;
    }

    public AlternateContactlessPaymentData getAlternateContactlessPaymentData() {
        return this.alternateContactlessPaymentData;
    }

    public void setAlternateContactlessPaymentData(AlternateContactlessPaymentData alternateContactlessPaymentData) {
        this.alternateContactlessPaymentData = alternateContactlessPaymentData;
    }

    public ByteArray getICC_privateKey_q() {
        return this.ICC_privateKey_q;
    }

    public void setICC_privateKey_q(ByteArray byteArray) {
        this.ICC_privateKey_q = byteArray;
    }

    public ByteArray getPaymentFCI() {
        return this.paymentFCI;
    }

    public void setPaymentFCI(ByteArray byteArray) {
        this.paymentFCI = byteArray;
    }

    public ByteArray getPPSE_FCI() {
        return this.PPSE_FCI;
    }

    public void setPPSE_FCI(ByteArray byteArray) {
        this.PPSE_FCI = byteArray;
    }

    public ByteArray getCVR_MaskAnd() {
        return this.CVR_MaskAnd;
    }

    public void setCVR_MaskAnd(ByteArray byteArray) {
        this.CVR_MaskAnd = byteArray;
    }

    public ByteArray getICC_privateKey_p() {
        return this.ICC_privateKey_p;
    }

    public void setICC_privateKey_p(ByteArray byteArray) {
        this.ICC_privateKey_p = byteArray;
    }

    public ByteArray getICC_privateKey_dq() {
        return this.ICC_privateKey_dq;
    }

    public void setICC_privateKey_dq(ByteArray byteArray) {
        this.ICC_privateKey_dq = byteArray;
    }

    public ByteArray getAID() {
        return this.AID;
    }

    public void setAID(ByteArray byteArray) {
        this.AID = byteArray;
    }

    public ByteArray getPIN_IV_CVC3_Track2() {
        return this.PIN_IV_CVC3_Track2;
    }

    public void setPIN_IV_CVC3_Track2(ByteArray byteArray) {
        this.PIN_IV_CVC3_Track2 = byteArray;
    }

    public ByteArray getICC_privateKey_dp() {
        return this.ICC_privateKey_dp;
    }

    public void setICC_privateKey_dp(ByteArray byteArray) {
        this.ICC_privateKey_dp = byteArray;
    }

    public Records[] getRecords() {
        return this.records;
    }

    public void setRecords(Records[] recordsArr) {
        this.records = recordsArr;
    }

    public ByteArray getMinRRTime() {
        return this.minRRTime;
    }

    public void setMinRRTime(ByteArray byteArray) {
        this.minRRTime = byteArray;
    }

    public ByteArray getMaxRRTime() {
        return this.maxRRTime;
    }

    public void setMaxRRTime(ByteArray byteArray) {
        this.maxRRTime = byteArray;
    }

    public ByteArray getTransmissionRRTime() {
        return this.trRRTime;
    }

    public void setTransmissionRRTime(ByteArray byteArray) {
        this.trRRTime = byteArray;
    }

    public void wipe() {
        int i = 0;
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
        if (this.records != null && this.records.length > 0) {
            while (i < this.records.length) {
                this.records[i].wipe();
                i++;
            }
        }
    }
}
