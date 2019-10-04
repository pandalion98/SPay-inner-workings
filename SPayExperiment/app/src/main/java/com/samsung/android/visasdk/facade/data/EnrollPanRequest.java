/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import com.google.gson.Gson;
import com.samsung.android.visasdk.a.a;

public class EnrollPanRequest
extends a {
    private String clientAppID;
    private String consumerEntryMode;
    private String encPaymentInstrument;
    private String encryptionMetaData;
    private String locale;
    private String panSource;

    public static EnrollPanRequest fromJson(String string) {
        return (EnrollPanRequest)new Gson().fromJson(string, EnrollPanRequest.class);
    }

    public String getClientAppID() {
        return this.clientAppID;
    }

    public String getConsumerEntryMode() {
        return this.consumerEntryMode;
    }

    public String getEncPaymentInstrument() {
        return this.encPaymentInstrument;
    }

    public String getEncryptionMetaData() {
        return this.encryptionMetaData;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getPanSource() {
        return this.panSource;
    }

    public void setClientAppID(String string) {
        this.clientAppID = string;
    }

    public void setConsumerEntryMode(String string) {
        this.consumerEntryMode = string;
    }

    public void setEncPaymentInstrument(String string) {
        this.encPaymentInstrument = string;
    }

    public void setEncryptionMetaData(String string) {
        this.encryptionMetaData = string;
    }

    public void setLocale(String string) {
        this.locale = string;
    }

    public void setPanSource(String string) {
        this.panSource = string;
    }
}

