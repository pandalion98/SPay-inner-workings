/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

import com.samsung.android.spayfw.payprovider.discover.tokenmanager.models.ClearProfileData;

public class ClearProvisionData {
    private ClearProfileData ProfileData;
    private String algo_id;
    private String call_type;

    public String getAlgoId() {
        return this.algo_id;
    }

    public String getCallType() {
        return this.call_type;
    }

    public ClearProfileData getProfileData() {
        return this.ProfileData;
    }
}

