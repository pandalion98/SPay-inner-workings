package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.tlv.ParsingException;

public class Picture {
    private byte pictureHorizontalPosition;
    private byte pictureScale;
    private byte pictureType;
    private byte[] pictureValue;
    private byte pictureVerticalPosition;

    public Picture(byte[] bArr, int i, int i2) {
        if (i2 < 4) {
            throw new ParsingException();
        }
        this.pictureHorizontalPosition = bArr[i];
        if (this.pictureHorizontalPosition < null || this.pictureHorizontalPosition > (byte) 100) {
            throw new ParsingException();
        }
        this.pictureVerticalPosition = bArr[i + 1];
        if (this.pictureVerticalPosition < null || this.pictureVerticalPosition > (byte) 100) {
            throw new ParsingException();
        }
        this.pictureScale = bArr[i + 2];
        if (this.pictureScale < null || this.pictureScale > (byte) 100) {
            throw new ParsingException();
        }
        this.pictureType = bArr[i + 3];
        this.pictureValue = new byte[(i2 - 4)];
        System.arraycopy(bArr, i + 4, this.pictureValue, 0, this.pictureValue.length);
    }

    public Picture(byte b, byte[] bArr, byte b2, byte b3, byte b4) {
        setPictureParams(b, bArr, b2, b3, b4);
    }

    public byte getPictureHorizontalPosition() {
        return this.pictureHorizontalPosition;
    }

    public byte getPictureVerticalPosition() {
        return this.pictureVerticalPosition;
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

    public void setPictureParams(byte b, byte[] bArr, byte b2, byte b3, byte b4) {
        this.pictureType = b;
        this.pictureValue = bArr;
        this.pictureHorizontalPosition = b2;
        this.pictureVerticalPosition = b3;
        this.pictureScale = b4;
    }
}
