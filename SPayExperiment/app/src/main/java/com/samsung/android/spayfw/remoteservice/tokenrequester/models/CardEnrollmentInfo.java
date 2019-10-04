/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Type;

public class CardEnrollmentInfo
extends Type {
    private String brand;
    private JsonObject data;
    private String reference;
    private RiskData risk;

    public CardEnrollmentInfo(String string) {
        super(string);
    }

    public void setBrand(String string) {
        this.brand = string;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public void setReference(String string) {
        this.reference = string;
    }

    public void setRiskData(String string, String string2, String string3) {
        this.risk = new RiskData();
        this.risk.zipCodeHash = string;
        this.risk.lastNameHash = string2;
        this.risk.last4Hash = string3;
    }

    private static class RiskData {
        private String last4Hash;
        private String lastNameHash;
        private String zipCodeHash;

        private RiskData() {
        }
    }

}

