package com.mastercard.mobile_api.payment.cld;

public class Background {
    public static final byte[] BLANK_VALUE;
    public static final byte JPEG_TYPE = (byte) 2;
    public static final byte REFERENCE_TYPE = (byte) 4;
    public static final byte RGB_TYPE = (byte) 1;
    public static final byte URL_TYPE = (byte) 3;
    private byte backgroundType;
    private byte[] backgroundValue;

    static {
        BLANK_VALUE = new byte[]{(byte) -1, (byte) -1, (byte) -1};
    }

    public Background() {
        this.backgroundType = RGB_TYPE;
        this.backgroundValue = BLANK_VALUE;
    }

    public Background(byte[] bArr, int i, int i2) {
        this.backgroundType = bArr[i];
        this.backgroundValue = new byte[(i2 - 1)];
        System.arraycopy(bArr, i + 1, this.backgroundValue, 0, this.backgroundValue.length);
    }

    public byte getBackgroundType() {
        return this.backgroundType;
    }

    public byte[] getBackgroundValue() {
        return this.backgroundValue;
    }

    public void setBackgroundParams(byte b, byte[] bArr) {
        this.backgroundType = b;
        this.backgroundValue = bArr;
    }
}
