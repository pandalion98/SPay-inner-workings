package com.synaptics.bpd.fingerprint;

public class RemoveEnroll {
    public int fingerIndex;
    public String userId;

    public RemoveEnroll() {
    }

    public RemoveEnroll(String userId2, int fingerIndex2) {
        this.userId = userId2;
        this.fingerIndex = fingerIndex2;
    }
}
