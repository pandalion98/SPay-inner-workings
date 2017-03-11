package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Cache {
    private String id;
    private Sequence recommendation;
    private String store_name;
    private Wifi[] wifi;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public Wifi[] getWifi() {
        return this.wifi;
    }

    public void setWifi(Wifi[] wifiArr) {
        this.wifi = wifiArr;
    }

    public Sequence getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(Sequence sequence) {
        this.recommendation = sequence;
    }

    public String getStoreName() {
        return this.store_name;
    }

    public void setStoreName(String str) {
        this.store_name = str;
    }
}
