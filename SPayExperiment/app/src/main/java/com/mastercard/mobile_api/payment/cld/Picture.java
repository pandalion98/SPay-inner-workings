/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.tlv.ParsingException;

public class Picture {
    private byte pictureHorizontalPosition;
    private byte pictureScale;
    private byte pictureType;
    private byte[] pictureValue;
    private byte pictureVerticalPosition;

    public Picture(byte by, byte[] arrby, byte by2, byte by3, byte by4) {
        this.setPictureParams(by, arrby, by2, by3, by4);
    }

    public Picture(byte[] arrby, int n2, int n3) {
        if (n3 < 4) {
            throw new ParsingException();
        }
        this.pictureHorizontalPosition = arrby[n2];
        if (this.pictureHorizontalPosition < 0 || this.pictureHorizontalPosition > 100) {
            throw new ParsingException();
        }
        this.pictureVerticalPosition = arrby[n2 + 1];
        if (this.pictureVerticalPosition < 0 || this.pictureVerticalPosition > 100) {
            throw new ParsingException();
        }
        this.pictureScale = arrby[n2 + 2];
        if (this.pictureScale < 0 || this.pictureScale > 100) {
            throw new ParsingException();
        }
        this.pictureType = arrby[n2 + 3];
        this.pictureValue = new byte[n3 - 4];
        System.arraycopy((Object)arrby, (int)(n2 + 4), (Object)this.pictureValue, (int)0, (int)this.pictureValue.length);
    }

    public byte getPictureHorizontalPosition() {
        return this.pictureHorizontalPosition;
    }

    public byte getPictureScale() {
        return this.pictureScale;
    }

    public byte getPictureType() {
        return this.pictureType;
    }

    public byte[] getPictureValue() {
        return this.pictureValue;
    }

    public byte getPictureVerticalPosition() {
        return this.pictureVerticalPosition;
    }

    public void setPictureParams(byte by, byte[] arrby, byte by2, byte by3, byte by4) {
        this.pictureType = by;
        this.pictureValue = arrby;
        this.pictureHorizontalPosition = by2;
        this.pictureVerticalPosition = by3;
        this.pictureScale = by4;
    }
}

