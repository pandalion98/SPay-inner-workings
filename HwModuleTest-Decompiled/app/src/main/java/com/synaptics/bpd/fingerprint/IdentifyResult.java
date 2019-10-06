package com.synaptics.bpd.fingerprint;

public class IdentifyResult {
    public byte[] additionalData;
    public int fingerIndex;
    public int[] matchedFingerIndexes;
    public int result;
    public byte[] templateId = new byte[16];
    public String userId;
}
