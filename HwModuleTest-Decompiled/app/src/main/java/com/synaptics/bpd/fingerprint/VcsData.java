package com.synaptics.bpd.fingerprint;

public class VcsData {
    public byte[] requestData;
    public byte[] responseData;

    public VcsData() {
    }

    public VcsData(byte[] reqData) {
        this.requestData = reqData;
    }
}
