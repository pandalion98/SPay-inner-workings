package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.paywave.model.ODAData;
import com.samsung.android.visasdk.paywave.model.PaymentInstrument;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import java.util.ArrayList;

public class ProvisionResponse {
    private ODAData ODAData;
    private String encryptionMetaData;
    private PaymentInstrument paymentInstrument;
    private ArrayList<StepUpRequest> stepUpRequest;
    private TokenInfo tokenInfo;
    private String vProvisionedTokenID;

    public ProvisionResponse() {
        this.stepUpRequest = new ArrayList();
    }

    public String getVProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setVProvisionedTokenID(String str) {
        this.vProvisionedTokenID = str;
    }

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public void setEncryptionMetaData(String str) {
        this.encryptionMetaData = str;
    }

    public PaymentInstrument getPaymentInstrument() {
        return this.paymentInstrument;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ArrayList<StepUpRequest> getStepUpRequest() {
        return this.stepUpRequest;
    }

    public void setStepUpRequest(ArrayList<StepUpRequest> arrayList) {
        this.stepUpRequest = arrayList;
    }

    public ODAData getODAData() {
        return this.ODAData;
    }

    public void setODAData(ODAData oDAData) {
        this.ODAData = oDAData;
    }
}
