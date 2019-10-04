/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;

public class Cache {
    private String id;
    private Sequence recommendation;
    private String store_name;
    private Wifi[] wifi;

    public String getId() {
        return this.id;
    }

    public Sequence getRecommendation() {
        return this.recommendation;
    }

    public String getStoreName() {
        return this.store_name;
    }

    public Wifi[] getWifi() {
        return this.wifi;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setRecommendation(Sequence sequence) {
        this.recommendation = sequence;
    }

    public void setStoreName(String string) {
        this.store_name = string;
    }

    public void setWifi(Wifi[] arrwifi) {
        this.wifi = arrwifi;
    }
}

