package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class ClearProvisionData {
    private ClearProfileData ProfileData;
    private String algo_id;
    private String call_type;

    public ClearProfileData getProfileData() {
        return this.ProfileData;
    }

    public String getCallType() {
        return this.call_type;
    }

    public String getAlgoId() {
        return this.algo_id;
    }
}
