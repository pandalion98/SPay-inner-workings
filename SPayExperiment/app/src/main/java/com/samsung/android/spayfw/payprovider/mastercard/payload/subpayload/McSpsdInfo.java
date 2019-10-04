/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCasdJwk;

public class McSpsdInfo {
    private String aid;
    private String appletInstanceAid;
    private String casdPkCertificate;
    private McCasdJwk casdPkJwk;
    private String rgk;
    private String spsdSequenceCounter;

    public String getAid() {
        return this.aid;
    }

    public String getAppletInstanceAid() {
        return this.spsdSequenceCounter;
    }

    public String getCasdPkCertificate(String string) {
        return string;
    }

    public String getRgk(String string) {
        return string;
    }

    public String getSpsdSequenceCounter() {
        return this.spsdSequenceCounter;
    }

    public void setAid(String string) {
        this.aid = string;
    }

    public void setAppletInstanceAid(String string) {
        this.appletInstanceAid = string;
    }

    public void setCasdPkCertificate(String string) {
        this.casdPkCertificate = string;
    }

    public void setCasdPkJwk(McCasdJwk mcCasdJwk) {
        this.casdPkJwk = mcCasdJwk;
    }

    public void setRgk(String string) {
        this.rgk = string;
    }

    public void setSpsdSequenceCounter(String string) {
        this.spsdSequenceCounter = string;
    }
}

