/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import java.util.Map;

public class McSeInfo {
    private String seId;

    public McSeInfo(String string, Map map) {
        this.seId = string;
    }

    public String getSeId() {
        return this.seId;
    }

    public void setSeId(String string) {
        this.seId = string;
    }
}

