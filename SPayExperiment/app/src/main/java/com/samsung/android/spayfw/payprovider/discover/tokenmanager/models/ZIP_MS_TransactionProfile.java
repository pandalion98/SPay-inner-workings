/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearProfileData;

public class ZIP_MS_TransactionProfile {
    @SerializedName(value="ZIP/MS_Mode_AFL")
    private String ZIP_MS_Mode_AFL;
    @SerializedName(value="ZIP/MS_Mode_AIP")
    private String ZIP_MS_Mode_AIP;

    public String getZIP_MS_Mode_AFL() {
        return ClearProfileData.logger("ZIP_MS_Mode_AFL", this.ZIP_MS_Mode_AFL);
    }

    public String getZIP_MS_Mode_AIP() {
        return ClearProfileData.logger("ZIP_MS_Mode_AIP", this.ZIP_MS_Mode_AIP);
    }
}

