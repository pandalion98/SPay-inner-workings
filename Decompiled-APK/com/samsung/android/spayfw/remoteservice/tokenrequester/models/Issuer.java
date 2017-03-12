package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Issuer extends Name {
    private Url logo;

    public Issuer(String str) {
        super(str);
    }

    public Url getLogo() {
        return this.logo;
    }

    public void setLogo(Url url) {
        this.logo = url;
    }
}
