package com.samsung.android.visasdk.facade.data;

public enum VerifyingType {
    NO_CD_CVM((byte) 0),
    PASSCODE((byte) 1),
    OTHER_CD_CVM((byte) 2),
    MOBILE_DEVICE((byte) 3),
    SIGNATURE((byte) 13),
    ONLINE_PIN((byte) 14);
    
    private final byte type;

    private VerifyingType(byte b) {
        this.type = b;
    }

    public byte getByte() {
        return this.type;
    }
}
