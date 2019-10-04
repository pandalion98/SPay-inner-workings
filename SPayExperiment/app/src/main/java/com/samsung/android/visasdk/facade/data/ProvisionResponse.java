/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.facade.data.StepUpRequest;
import com.samsung.android.visasdk.paywave.model.ODAData;
import com.samsung.android.visasdk.paywave.model.PaymentInstrument;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import java.util.ArrayList;

public class ProvisionResponse {
    private ODAData ODAData;
    private String encryptionMetaData;
    private PaymentInstrument paymentInstrument;
    private ArrayList<StepUpRequest> stepUpRequest = new ArrayList();
    private TokenInfo tokenInfo;
    private String vProvisionedTokenID;

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public ODAData getODAData() {
        return this.ODAData;
    }

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public ArrayList<StepUpRequest> getStepUpRequest() {
        return this.stepUpRequest;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public String getVProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setEncryptionMetaData(String string) {
        this.encryptionMetaData = string;
    }

    public void setODAData(ODAData oDAData) {
        this.ODAData = oDAData;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public void setStepUpRequest(ArrayList<StepUpRequest> arrayList) {
        this.stepUpRequest = arrayList;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public void setVProvisionedTokenID(String string) {
        this.vProvisionedTokenID = string;
    }
}

