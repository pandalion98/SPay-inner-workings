package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.google.gson.annotations.SerializedName;

public class ZIP_MS_TransactionProfile {
    @SerializedName("ZIP/MS_Mode_AFL")
    private String ZIP_MS_Mode_AFL;
    @SerializedName("ZIP/MS_Mode_AIP")
    private String ZIP_MS_Mode_AIP;

    public String getZIP_MS_Mode_AIP() {
        return ClearProfileData.logger("ZIP_MS_Mode_AIP", this.ZIP_MS_Mode_AIP);
    }

    public String getZIP_MS_Mode_AFL() {
        return ClearProfileData.logger("ZIP_MS_Mode_AFL", this.ZIP_MS_Mode_AFL);
    }
}
