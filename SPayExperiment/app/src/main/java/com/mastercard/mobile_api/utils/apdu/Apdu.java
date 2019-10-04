/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mobile_api.utils.apdu;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;

public class Apdu {
    private final ByteArray value;

    public Apdu() {
        this.value = new DefaultByteArrayImpl(5);
    }

    public Apdu(byte by, byte by2, byte by3, byte by4) {
        this();
        this.value.setByte(0, by);
        this.value.setByte(1, by2);
        this.value.setByte(2, by3);
        this.value.setByte(3, by4);
    }

    public Apdu(ByteArray byteArray) {
        this.value = new DefaultByteArrayImpl(byteArray.getBytes());
    }

    public Apdu(byte[] arrby, int n2, int n3) {
        this.value = new DefaultByteArrayImpl(arrby, n2, n3);
    }

    public void appendData(ByteArray byteArray, boolean bl) {
        if (bl) {
            this.setLc((byte)(this.getLc() + byteArray.getLength()));
        }
        this.value.append(byteArray);
    }

    public void clear() {
        this.value.clear();
    }

    public ByteArray getByteArray() {
        return this.value;
    }

    public byte[] getBytes() {
        return this.value.getBytes();
    }

    public byte getCLA() {
        return this.value.getByte(0);
    }

    public byte getINS() {
        return this.value.getByte(1);
    }

    public int getLc() {
        return 255 & this.value.getByte(4);
    }

    public int getLength() {
        return this.value.getLength();
    }

    public byte getP1() {
        return this.value.getByte(2);
    }

    public byte getP2() {
        return this.value.getByte(3);
    }

    public void setCLA(byte by) {
        this.value.setByte(0, by);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDataField(ByteArray byteArray) {
        if (byteArray == null || byteArray.getBytes() == null) {
            return;
        }
        if (this.value.getLength() == 4) {
            this.value.appendByte((byte)byteArray.getLength());
        } else {
            this.value.setByte(4, (byte)byteArray.getLength());
        }
        this.value.append(byteArray);
    }

    public void setINS(byte by) {
        this.value.setByte(1, by);
    }

    public void setLc(byte by) {
        this.value.setByte(4, by);
    }

    public void setP1(byte by) {
        this.value.setByte(2, by);
    }

    public void setP1P2(short s2) {
        this.value.setShort(2, s2);
    }

    public void setP2(byte by) {
        this.value.setByte(3, by);
    }

    public String toHexString() {
        return this.value.getHexString();
    }

    public String toString() {
        return this.value.getHexString();
    }
}

