package com.synaptics.fingerprint;

public class VcsAddInfo {
    public byte[] data;
    public int payloadType;
    public int protectionType;

    public VcsAddInfo() {
        this.payloadType = 1;
        this.protectionType = 2;
    }

    public VcsAddInfo(int payloadType2, int protectionType2) {
        this.payloadType = payloadType2;
        this.protectionType = protectionType2;
    }

    public VcsAddInfo(int protectionType2) {
        this.protectionType = protectionType2;
    }
}
