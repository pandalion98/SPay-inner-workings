/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.mastercard.mobile_api.payment.cld;

import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.tlv.ParsingException;

public class Text {
    public static final byte BOLD = 64;
    public static final byte COURIER_NEW = 4;
    public static final byte FARRINGTON_7B = 3;
    public static final byte ISO_1073_1_OCR_A = 1;
    public static final byte ISO_1073_1_OCR_B = 2;
    public static final byte ITALIC = -128;
    public static final byte REVERSE_ITALIC = 16;
    public static final byte TIMES_NEW_ROMAN = 5;
    public static final byte UNDERLINE = 32;
    byte[] BtextValue;
    private byte font;
    private int textColor;
    private byte textHorizontalPosition;
    private byte textMode;
    private byte textSize;
    private byte textType;
    private byte textVerticalPosition;

    public Text() {
    }

    public Text(byte by, byte[] arrby, int n2, int n3) {
        this.textType = by;
        if (n3 < 8) {
            throw new ParsingException();
        }
        this.textVerticalPosition = arrby[n2 + 0];
        if (this.textVerticalPosition < 0 || this.textVerticalPosition > 100) {
            throw new ParsingException();
        }
        this.textHorizontalPosition = arrby[n2 + 1];
        if (this.textHorizontalPosition < 0 || this.textHorizontalPosition > 100) {
            throw new ParsingException();
        }
        this.font = arrby[n2 + 2];
        this.textMode = arrby[n2 + 3];
        this.textSize = arrby[n2 + 4];
        if (this.textSize < 0 || this.textSize > 100) {
            throw new ParsingException();
        }
        this.textColor = (255 & arrby[n2 + 5]) << 16 | (255 & arrby[1 + (n2 + 5)]) << 8 | 255 & arrby[2 + (n2 + 5)];
        this.BtextValue = new byte[n3 - 8];
        System.arraycopy((Object)arrby, (int)(n2 + 8), (Object)this.BtextValue, (int)0, (int)(n3 - 8));
    }

    public void clear() {
        Utils.clearByteArray(this.BtextValue);
    }

    public byte getFont() {
        return this.font;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public byte getTextHorizontalPosition() {
        return this.textHorizontalPosition;
    }

    public byte getTextMode() {
        return this.textMode;
    }

    public byte getTextSize() {
        return this.textSize;
    }

    public byte getTextType() {
        return this.textType;
    }

    public String getTextValue() {
        return new String(this.BtextValue);
    }

    public byte getTextVerticalPosition() {
        return this.textVerticalPosition;
    }

    public boolean isBold() {
        return (64 & this.textMode) != 0;
    }

    public boolean isItalic() {
        return (-128 & this.textMode) != 0;
    }

    public boolean isReverseItalic() {
        return (16 & this.textMode) != 0;
    }

    public boolean isUnderline() {
        return (32 & this.textMode) != 0;
    }

    public void setBtextValue(byte[] arrby) {
        this.BtextValue = arrby;
    }

    public void setTextColor(int n2) {
        this.textColor = n2;
    }

    public void setTextHorizontalPosition(byte by) {
        this.textHorizontalPosition = by;
    }

    public void setTextMode(byte by) {
        this.textMode = by;
    }

    public void setTextSize(byte by) {
        this.textSize = by;
    }

    public void setTextType(byte by) {
        this.textType = by;
    }

    public void setTextVerticalPosition(byte by) {
        this.textVerticalPosition = by;
    }
}

