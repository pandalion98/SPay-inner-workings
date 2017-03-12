package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Mode {
    private String mode;
    private String[] modes;

    public Mode(String str) {
        this.mode = str;
    }

    public String[] getModes() {
        return this.modes;
    }
}
