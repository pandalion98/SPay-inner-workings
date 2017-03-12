package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McSpsdInfo {
    private String aid;
    private String appletInstanceAid;
    private String casdPkCertificate;
    private McCasdJwk casdPkJwk;
    private String rgk;
    private String spsdSequenceCounter;

    public void setAid(String str) {
        this.aid = str;
    }

    public void setAppletInstanceAid(String str) {
        this.appletInstanceAid = str;
    }

    public void setSpsdSequenceCounter(String str) {
        this.spsdSequenceCounter = str;
    }

    public void setRgk(String str) {
        this.rgk = str;
    }

    public void setCasdPkCertificate(String str) {
        this.casdPkCertificate = str;
    }

    public void setCasdPkJwk(McCasdJwk mcCasdJwk) {
        this.casdPkJwk = mcCasdJwk;
    }

    public String getAid() {
        return this.aid;
    }

    public String getAppletInstanceAid() {
        return this.spsdSequenceCounter;
    }

    public String getSpsdSequenceCounter() {
        return this.spsdSequenceCounter;
    }

    public String getRgk(String str) {
        return str;
    }

    public String getCasdPkCertificate(String str) {
        return str;
    }
}
