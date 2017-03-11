package com.mastercard.mobile_api.utils.apdu;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Apdu {
    private final ByteArray value;

    public Apdu() {
        this.value = new DefaultByteArrayImpl(5);
    }

    public Apdu(byte b, byte b2, byte b3, byte b4) {
        this();
        this.value.setByte(0, b);
        this.value.setByte(1, b2);
        this.value.setByte(2, b3);
        this.value.setByte(3, b4);
    }

    public Apdu(byte[] bArr, int i, int i2) {
        this.value = new DefaultByteArrayImpl(bArr, i, i2);
    }

    public Apdu(ByteArray byteArray) {
        this.value = new DefaultByteArrayImpl(byteArray.getBytes());
    }

    public void setDataField(ByteArray byteArray) {
        if (byteArray != null && byteArray.getBytes() != null) {
            if (this.value.getLength() == 4) {
                this.value.appendByte((byte) byteArray.getLength());
            } else {
                this.value.setByte(4, (byte) byteArray.getLength());
            }
            this.value.append(byteArray);
        }
    }

    public byte getCLA() {
        return this.value.getByte(0);
    }

    public void setCLA(byte b) {
        this.value.setByte(0, b);
    }

    public byte getINS() {
        return this.value.getByte(1);
    }

    public void setINS(byte b) {
        this.value.setByte(1, b);
    }

    public byte getP1() {
        return this.value.getByte(2);
    }

    public void setP1(byte b) {
        this.value.setByte(2, b);
    }

    public byte getP2() {
        return this.value.getByte(3);
    }

    public void setP2(byte b) {
        this.value.setByte(3, b);
    }

    public void setP1P2(short s) {
        this.value.setShort(2, s);
    }

    public int getLc() {
        return this.value.getByte(4) & GF2Field.MASK;
    }

    public void setLc(byte b) {
        this.value.setByte(4, b);
    }

    public int getLength() {
        return this.value.getLength();
    }

    public byte[] getBytes() {
        return this.value.getBytes();
    }

    public String toHexString() {
        return this.value.getHexString();
    }

    public String toString() {
        return this.value.getHexString();
    }

    public void appendData(ByteArray byteArray, boolean z) {
        if (z) {
            setLc((byte) (getLc() + byteArray.getLength()));
        }
        this.value.append(byteArray);
    }

    public void clear() {
        this.value.clear();
    }

    public ByteArray getByteArray() {
        return this.value;
    }
}
