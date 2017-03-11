package com.samsung.android.visasdk.facade.data;

import com.google.gson.Gson;
import com.samsung.android.visasdk.p023a.JsonWraper;

public class EnrollPanRequest extends JsonWraper {
    private String clientAppID;
    private String consumerEntryMode;
    private String encPaymentInstrument;
    private String encryptionMetaData;
    private String locale;
    private String panSource;

    public String getClientAppID() {
        return this.clientAppID;
    }

    public void setClientAppID(String str) {
        this.clientAppID = str;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String str) {
        this.locale = str;
    }

    public String getPanSource() {
        return this.panSource;
    }

    public void setPanSource(String str) {
        this.panSource = str;
    }

    public String getConsumerEntryMode() {
        return this.consumerEntryMode;
    }

    public void setConsumerEntryMode(String str) {
        this.consumerEntryMode = str;
    }

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public void setEncryptionMetaData(String str) {
        this.encryptionMetaData = str;
    }

    public String getEncPaymentInstrument() {
        return this.encPaymentInstrument;
    }

    public void setEncPaymentInstrument(String str) {
        this.encPaymentInstrument = str;
    }

    public static EnrollPanRequest fromJson(String str) {
        return (EnrollPanRequest) new Gson().fromJson(str, EnrollPanRequest.class);
    }
}
