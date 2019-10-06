package com.synaptics.bpd.fingerprint;

public class VcsRequest {
    public Object additionalData;
    public byte[] requestData;
    public int requestType;

    public VcsRequest(int requestType2) {
        this.requestType = requestType2;
    }

    public VcsRequest(int requestType2, byte[] requestData2) {
        this.requestType = requestType2;
        this.requestData = requestData2;
    }

    public VcsRequest(int requestType2, byte[] requestData2, Object additionalData2) {
        this.requestType = requestType2;
        this.requestData = requestData2;
        this.additionalData = additionalData2;
    }
}
