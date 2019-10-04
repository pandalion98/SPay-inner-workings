/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package com.mastercard.mobile_api.payment.cld;

public class Background {
    public static final byte[] BLANK_VALUE = new byte[]{-1, -1, -1};
    public static final byte JPEG_TYPE = 2;
    public static final byte REFERENCE_TYPE = 4;
    public static final byte RGB_TYPE = 1;
    public static final byte URL_TYPE = 3;
    private byte backgroundType;
    private byte[] backgroundValue;

    public Background() {
        this.backgroundType = 1;
        this.backgroundValue = BLANK_VALUE;
    }

    public Background(byte[] arrby, int n2, int n3) {
        this.backgroundType = arrby[n2];
        this.backgroundValue = new byte[n3 - 1];
        System.arraycopy((Object)arrby, (int)(n2 + 1), (Object)this.backgroundValue, (int)0, (int)this.backgroundValue.length);
    }

    public byte getBackgroundType() {
        return this.backgroundType;
    }

    public byte[] getBackgroundValue() {
        return this.backgroundValue;
    }

    public void setBackgroundParams(byte by, byte[] arrby) {
        this.backgroundType = by;
        this.backgroundValue = arrby;
    }
}

