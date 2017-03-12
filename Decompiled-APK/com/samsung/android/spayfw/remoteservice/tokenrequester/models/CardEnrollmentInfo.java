package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;

public class CardEnrollmentInfo extends Type {
    private String brand;
    private JsonObject data;
    private String reference;
    private RiskData risk;

    private static class RiskData {
        private String last4Hash;
        private String lastNameHash;
        private String zipCodeHash;

        private RiskData() {
        }
    }

    public CardEnrollmentInfo(String str) {
        super(str);
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setData(JsonObject jsonObject) {
        this.data = jsonObject;
    }

    public void setReference(String str) {
        this.reference = str;
    }

    public void setRiskData(String str, String str2, String str3) {
        this.risk = new RiskData();
        this.risk.zipCodeHash = str;
        this.risk.lastNameHash = str2;
        this.risk.last4Hash = str3;
    }
}
