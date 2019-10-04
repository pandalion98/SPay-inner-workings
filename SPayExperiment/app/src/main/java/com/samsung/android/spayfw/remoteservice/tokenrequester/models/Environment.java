/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Code;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Location;

public class Environment {
    private Code country;
    private Location location;
    private String mstSequenceId;

    public void setCountry(Code code) {
        this.country = code;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMstSequenceId(String string) {
        this.mstSequenceId = string;
    }
}

