package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;

public class AlternateContactlessPaymentData {
    private ByteArray AID;
    private ByteArray CIAC_Decline;
    private ByteArray CVR_MaskAnd;
    private ByteArray GPO_Response;
    private ByteArray issuerApplicationData;
    private ByteArray paymentFCI;

    public ByteArray getPaymentFCI() {
        return this.paymentFCI;
    }

    public void setPaymentFCI(ByteArray byteArray) {
        this.paymentFCI = byteArray;
    }

    public ByteArray getGPO_Response() {
        return this.GPO_Response;
    }

    public void setGPO_Response(ByteArray byteArray) {
        this.GPO_Response = byteArray;
    }

    public ByteArray getCVR_MaskAnd() {
        return this.CVR_MaskAnd;
    }

    public void setCVR_MaskAnd(ByteArray byteArray) {
        this.CVR_MaskAnd = byteArray;
    }

    public ByteArray getAID() {
        return this.AID;
    }

    public void setAID(ByteArray byteArray) {
        this.AID = byteArray;
    }

    public ByteArray getCIAC_Decline() {
        return this.CIAC_Decline;
    }

    public void setCIAC_Decline(ByteArray byteArray) {
        this.CIAC_Decline = byteArray;
    }

    public void setIssuerApplicationData(ByteArray byteArray) {
        this.issuerApplicationData = byteArray;
    }

    public ByteArray getIssuerApplicationData() {
        return this.issuerApplicationData;
    }

    public void wipe() {
        Utils.clearByteArray(this.AID);
        Utils.clearByteArray(this.CIAC_Decline);
        Utils.clearByteArray(this.CVR_MaskAnd);
        Utils.clearByteArray(this.GPO_Response);
        Utils.clearByteArray(this.paymentFCI);
    }
}
