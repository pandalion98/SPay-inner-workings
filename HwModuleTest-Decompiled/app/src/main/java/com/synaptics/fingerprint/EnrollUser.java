package com.synaptics.fingerprint;

public class EnrollUser {
    public int fingerIndex;
    public int mode = 0;
    public String userId;

    public EnrollUser() {
    }

    public EnrollUser(String userId2, int fingerIndex2) {
        this.userId = userId2;
        this.fingerIndex = fingerIndex2;
    }
}
