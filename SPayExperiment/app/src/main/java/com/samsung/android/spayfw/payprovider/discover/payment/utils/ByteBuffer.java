/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Character
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import java.util.Arrays;

public class ByteBuffer {
    private byte[] mBytesArray = null;

    public ByteBuffer(int n2) {
        this.mBytesArray = new byte[n2];
    }

    public ByteBuffer(byte[] arrby) {
        if (arrby == null) {
            return;
        }
        this.mBytesArray = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)this.mBytesArray, (int)0, (int)arrby.length);
    }

    private boolean checkZeros(ByteBuffer byteBuffer) {
        byte[] arrby = byteBuffer.getBytes();
        int n2 = arrby.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (arrby[i2] == 0) continue;
            return false;
        }
        return true;
    }

    public static ByteBuffer fromHexString(String string) {
        int n2 = 0;
        if (string == null) {
            return null;
        }
        if (string.length() == 0) {
            string = new String("00");
        }
        if (string.length() % 2 != 0) {
            StringBuffer stringBuffer = new StringBuffer(string);
            stringBuffer.insert(0, '0');
            string = stringBuffer.toString();
        }
        int n3 = string.length();
        byte[] arrby = new byte[n3 / 2];
        while (n2 < n3) {
            arrby[n2 / 2] = (byte)((Character.digit((char)string.charAt(n2), (int)16) << 4) + Character.digit((char)string.charAt(n2 + 1), (int)16));
            n2 += 2;
        }
        return new ByteBuffer(arrby);
    }

    public static ByteBuffer getFromLong(long l2) {
        byte[] arrby = new byte[8];
        int n2 = -1;
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            byte by = (byte)(l2 >> (-1 + (arrby.length - i2) << 3));
            if (n2 == -1 && by != 0) {
                n2 = i2;
            }
            arrby[i2] = by;
        }
        if (n2 == -1 || n2 >= arrby.length) {
            return new ByteBuffer(new byte[]{0});
        }
        return new ByteBuffer(Arrays.copyOfRange((byte[])arrby, (int)n2, (int)arrby.length));
    }

    public static ByteBuffer getFromLong(long l2, int n2) {
        if (n2 > 64) {
            n2 = 64;
        }
        byte[] arrby = new byte[n2];
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            arrby[i2] = (byte)(l2 >> (-1 + (arrby.length - i2) << 3));
        }
        return new ByteBuffer(arrby);
    }

    public void append(byte by) {
        this.append(new byte[]{by});
    }

    public void append(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return;
        }
        this.append(byteBuffer.getBytes());
    }

    public void append(byte[] arrby) {
        if (arrby == null) {
            return;
        }
        byte[] arrby2 = new byte[this.mBytesArray.length + arrby.length];
        System.arraycopy((Object)this.mBytesArray, (int)0, (Object)arrby2, (int)0, (int)this.mBytesArray.length);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)this.mBytesArray.length, (int)arrby.length);
        this.mBytesArray = arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ByteBuffer bitAnd(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return this.clone();
        }
        int n2 = this.getSize() < byteBuffer.getSize() ? this.getSize() : byteBuffer.getSize();
        ByteBuffer byteBuffer2 = new ByteBuffer(this.getSize());
        int n3 = 0;
        while (n3 < n2) {
            byteBuffer2.setByte(n3, (byte)(this.mBytesArray[n3] & byteBuffer.getByte(n3)));
            ++n3;
        }
        return byteBuffer2;
    }

    public boolean checkBit(byte by) {
        return (this.mBytesArray[-1 + (by >> 4)] & 1 << -1 + (by & 15)) != 0;
    }

    public boolean checkBit(int n2, int n3) {
        return (this.mBytesArray[n2 - 1] & 1 << n3 - 1) != 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean checkBitAndMatch(ByteBuffer byteBuffer) {
        boolean bl = true;
        ByteBuffer byteBuffer2 = this.bitAnd(byteBuffer);
        if (byteBuffer2 == null) {
            return false;
        }
        if (this.checkZeros(byteBuffer) && this.checkZeros(this)) {
            if (byteBuffer.getSize() == this.mBytesArray.length) return bl;
        }
        if (!this.checkZeros(byteBuffer2)) return bl;
        return false;
    }

    public void clearBit(int n2, int n3) {
        byte[] arrby = this.mBytesArray;
        int n4 = n2 - 1;
        arrby[n4] = (byte)(arrby[n4] & (-1 ^ 1 << n3 - 1));
    }

    public ByteBuffer clone() {
        return new ByteBuffer(this.mBytesArray);
    }

    public ByteBuffer copyBytes(int n2, int n3) {
        byte[] arrby = new byte[n3 - n2];
        System.arraycopy((Object)this.mBytesArray, (int)n2, (Object)arrby, (int)0, (int)(n3 - n2));
        return new ByteBuffer(arrby);
    }

    public void copyBytes(ByteBuffer byteBuffer, int n2, int n3, int n4) {
        if (n3 + n4 > this.getSize()) {
            byte[] arrby = new byte[this.getSize() + (n3 + n4 - this.getSize())];
            System.arraycopy((Object)this.getBytes(), (int)0, (Object)arrby, (int)0, (int)this.getSize());
            this.mBytesArray = arrby;
        }
        System.arraycopy((Object)byteBuffer.getBytes(), (int)n2, (Object)this.mBytesArray, (int)n3, (int)n4);
    }

    public boolean equals(ByteBuffer byteBuffer) {
        if (byteBuffer == null && this.getBytes() == null) {
            return true;
        }
        if (byteBuffer == null && this.getBytes() != null || byteBuffer != null && this.getBytes() == null) {
            return false;
        }
        return this.toHexString().equals((Object)byteBuffer.toHexString());
    }

    public byte getByte(int n2) {
        return this.mBytesArray[n2];
    }

    public byte[] getBytes() {
        return this.mBytesArray;
    }

    public int getInt() {
        if (this.mBytesArray.length == 1) {
            return 255 & this.mBytesArray[0];
        }
        if (this.mBytesArray.length == 2) {
            return (255 & this.mBytesArray[0]) << 8 | 255 & this.mBytesArray[1];
        }
        if (this.mBytesArray.length == 3) {
            return (255 & this.mBytesArray[0]) << 16 | (255 & this.mBytesArray[1]) << 8 | 255 & this.mBytesArray[2];
        }
        return (255 & this.mBytesArray[0]) << 24 | (255 & this.mBytesArray[1]) << 16 | (255 & this.mBytesArray[2]) << 8 | 255 & this.mBytesArray[3];
    }

    public long getLong() {
        long l2 = 0L;
        for (int i2 = 0; i2 < this.mBytesArray.length; ++i2) {
            l2 = (long)(255 & this.mBytesArray[i2]) + (l2 << 8);
        }
        return l2;
    }

    public int getSize() {
        if (this.mBytesArray == null) {
            return 0;
        }
        return this.mBytesArray.length;
    }

    public void setBit(byte by) {
        byte[] arrby = this.mBytesArray;
        int n2 = -1 + (by >> 4);
        arrby[n2] = (byte)(arrby[n2] | 1 << -1 + (by & 15));
    }

    public void setBit(int n2, int n3) {
        byte[] arrby = this.mBytesArray;
        int n4 = n2 - 1;
        arrby[n4] = (byte)(arrby[n4] | 1 << n3 - 1);
    }

    public void setByte(int n2, byte by) {
        this.mBytesArray[n2] = by;
    }

    public void setBytes(byte[] arrby) {
        if (arrby == null) {
            return;
        }
        this.mBytesArray = (byte[])arrby.clone();
    }

    public void setInt(int n2, int n3) {
        this.mBytesArray[n2] = (byte)(n3 >> 24);
        this.mBytesArray[n2 + 1] = (byte)(n3 >> 16);
        this.mBytesArray[n2 + 2] = (byte)(n3 >> 8);
        this.mBytesArray[n2 + 3] = (byte)(n3 & 255);
    }

    public void setShort(int n2, short s2) {
        this.mBytesArray[n2] = (byte)(s2 >> 8);
        this.mBytesArray[n2 + 1] = (byte)(s2 & 255);
    }

    public String toHexString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.mBytesArray.length; ++i2) {
            byte by = this.mBytesArray[i2];
            stringBuffer.append("0123456789ABCDEF".charAt(15 & by >> 4));
            stringBuffer.append("0123456789ABCDEF".charAt(by & 15));
        }
        return stringBuffer.toString();
    }
}

