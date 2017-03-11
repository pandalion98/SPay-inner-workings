package com.samsung.android.spayfw.payprovider.discover.payment.p017a;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.a.a */
public class CommandApdu {
    private ByteBuffer mData;

    public CommandApdu() {
        this.mData = new ByteBuffer(5);
    }

    public CommandApdu(ByteBuffer byteBuffer) {
        this.mData = byteBuffer;
    }

    public ByteBuffer dj() {
        return this.mData;
    }

    public int dk() {
        return this.mData.getByte(0) & GF2Field.MASK;
    }

    public void setCLA(byte b) {
        this.mData.setByte(0, b);
    }

    public byte getINS() {
        return this.mData.getByte(1);
    }

    public void setINS(byte b) {
        this.mData.setByte(1, b);
    }

    public byte getP1() {
        return this.mData.getByte(2);
    }

    public byte getP2() {
        return this.mData.getByte(3);
    }

    public int getLc() {
        return this.mData.getByte(4) & GF2Field.MASK;
    }

    public int getLength() {
        return this.mData.getSize();
    }

    public ByteBuffer getData() {
        return this.mData.copyBytes(5, getLc() + 5);
    }

    public void setP1P2(short s) {
        this.mData.setShort(2, s);
    }

    public byte dl() {
        if (this.mData.getSize() > (getLc() + 4) + 1) {
            return this.mData.getByte((getLc() + 4) + 1);
        }
        return (byte) -1;
    }
}
