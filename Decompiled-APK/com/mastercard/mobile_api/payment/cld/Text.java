package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Text {
    public static final byte BOLD = (byte) 64;
    public static final byte COURIER_NEW = (byte) 4;
    public static final byte FARRINGTON_7B = (byte) 3;
    public static final byte ISO_1073_1_OCR_A = (byte) 1;
    public static final byte ISO_1073_1_OCR_B = (byte) 2;
    public static final byte ITALIC = Byte.MIN_VALUE;
    public static final byte REVERSE_ITALIC = (byte) 16;
    public static final byte TIMES_NEW_ROMAN = (byte) 5;
    public static final byte UNDERLINE = (byte) 32;
    byte[] BtextValue;
    private byte font;
    private int textColor;
    private byte textHorizontalPosition;
    private byte textMode;
    private byte textSize;
    private byte textType;
    private byte textVerticalPosition;

    public Text(byte b, byte[] bArr, int i, int i2) {
        this.textType = b;
        if (i2 < 8) {
            throw new ParsingException();
        }
        this.textVerticalPosition = bArr[i + 0];
        if (this.textVerticalPosition < null || this.textVerticalPosition > (byte) 100) {
            throw new ParsingException();
        }
        this.textHorizontalPosition = bArr[i + 1];
        if (this.textHorizontalPosition < null || this.textHorizontalPosition > (byte) 100) {
            throw new ParsingException();
        }
        this.font = bArr[i + 2];
        this.textMode = bArr[i + 3];
        this.textSize = bArr[i + 4];
        if (this.textSize < null || this.textSize > (byte) 100) {
            throw new ParsingException();
        }
        this.textColor = (((bArr[i + 5] & GF2Field.MASK) << 16) | ((bArr[(i + 5) + 1] & GF2Field.MASK) << 8)) | (bArr[(i + 5) + 2] & GF2Field.MASK);
        this.BtextValue = new byte[(i2 - 8)];
        System.arraycopy(bArr, i + 8, this.BtextValue, 0, i2 - 8);
    }

    public void setBtextValue(byte[] bArr) {
        this.BtextValue = bArr;
    }

    public byte getTextType() {
        return this.textType;
    }

    public void setTextType(byte b) {
        this.textType = b;
    }

    public byte getTextHorizontalPosition() {
        return this.textHorizontalPosition;
    }

    public void setTextHorizontalPosition(byte b) {
        this.textHorizontalPosition = b;
    }

    public byte getTextVerticalPosition() {
        return this.textVerticalPosition;
    }

    public void setTextVerticalPosition(byte b) {
        this.textVerticalPosition = b;
    }

    public byte getFont() {
        return this.font;
    }

    public byte getTextMode() {
        return this.textMode;
    }

    public void setTextMode(byte b) {
        this.textMode = b;
    }

    public boolean isItalic() {
        return (this.textMode & -128) != 0;
    }

    public boolean isBold() {
        return (this.textMode & 64) != 0;
    }

    public boolean isUnderline() {
        return (this.textMode & 32) != 0;
    }

    public boolean isReverseItalic() {
        return (this.textMode & 16) != 0;
    }

    public byte getTextSize() {
        return this.textSize;
    }

    public void setTextSize(byte b) {
        this.textSize = b;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public String getTextValue() {
        return new String(this.BtextValue);
    }

    public void clear() {
        Utils.clearByteArray(this.BtextValue);
    }
}
