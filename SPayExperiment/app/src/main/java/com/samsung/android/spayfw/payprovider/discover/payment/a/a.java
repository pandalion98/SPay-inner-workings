/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.discover.payment.a;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class a {
    private ByteBuffer mData;

    public a() {
        this.mData = new ByteBuffer(5);
    }

    public a(ByteBuffer byteBuffer) {
        this.mData = byteBuffer;
    }

    public ByteBuffer dj() {
        return this.mData;
    }

    public int dk() {
        return 255 & this.mData.getByte(0);
    }

    public byte dl() {
        if (this.mData.getSize() > 1 + (4 + this.getLc())) {
            return this.mData.getByte(1 + (4 + this.getLc()));
        }
        return -1;
    }

    public ByteBuffer getData() {
        return this.mData.copyBytes(5, 5 + this.getLc());
    }

    public byte getINS() {
        return this.mData.getByte(1);
    }

    public int getLc() {
        return 255 & this.mData.getByte(4);
    }

    public int getLength() {
        return this.mData.getSize();
    }

    public byte getP1() {
        return this.mData.getByte(2);
    }

    public byte getP2() {
        return this.mData.getByte(3);
    }

    public void setCLA(byte by) {
        this.mData.setByte(0, by);
    }

    public void setINS(byte by) {
        this.mData.setByte(1, by);
    }

    public void setP1P2(short s2) {
        this.mData.setShort(2, s2);
    }
}

