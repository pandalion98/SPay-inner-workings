/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.util.Arrays
 */
package com.mastercard.mobile_api.bytes;

import com.mastercard.mobile_api.bytes.ByteArray;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DefaultByteArrayImpl
implements ByteArray {
    private static String digits = "0123456789ABCDEF";
    private byte[] data;
    private int length;

    public DefaultByteArrayImpl() {
    }

    public DefaultByteArrayImpl(int n2) {
        this.length = n2;
        this.data = new byte[n2];
    }

    /*
     * Enabled aggressive block sorting
     */
    public DefaultByteArrayImpl(String string) {
        if (string == null) {
            this.length = 0;
            return;
        } else {
            int n2 = string.length();
            if (string.length() % 2 != 0) {
                throw new IllegalArgumentException("Number of characters should be even for a hexadecimal buffer");
            }
            this.length = n2 / 2;
            this.data = new byte[this.length];
            for (int i2 = 0; i2 < this.length; ++i2) {
                String string2 = string.substring(i2 * 2, 2 + i2 * 2);
                this.data[i2] = (byte)Integer.parseInt((String)string2, (int)16);
            }
        }
    }

    public DefaultByteArrayImpl(byte[] arrby) {
        this.length = arrby.length;
        this.data = new byte[this.length];
        System.arraycopy((Object)arrby, (int)0, (Object)this.data, (int)0, (int)this.length);
    }

    public DefaultByteArrayImpl(byte[] arrby, int n2) {
        this.length = n2;
        this.data = new byte[n2];
        System.arraycopy((Object)arrby, (int)0, (Object)this.data, (int)0, (int)n2);
    }

    public DefaultByteArrayImpl(byte[] arrby, int n2, int n3) {
        this.length = n3;
        this.data = new byte[this.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)this.data, (int)0, (int)this.length);
    }

    @Override
    public ByteArray append(ByteArray byteArray) {
        int n2 = this.length;
        if (byteArray.getBytes() == null) {
            return this;
        }
        this.resize(this.length + byteArray.getLength());
        System.arraycopy((Object)byteArray.getBytes(), (int)0, (Object)this.data, (int)n2, (int)byteArray.getLength());
        return this;
    }

    public void appendBuffer(byte[] arrby, int n2, int n3) {
        int n4 = this.length;
        if (n2 > arrby.length) {
            return;
        }
        this.resize(n3 + this.length);
        System.arraycopy((Object)arrby, (int)n2, (Object)this.data, (int)n4, (int)n3);
    }

    @Override
    public ByteArray appendByte(byte by) {
        int n2 = this.length;
        this.resize(1 + this.length);
        this.data[n2] = by;
        return this;
    }

    public void appendByteArray(ByteArray byteArray) {
        int n2 = this.length;
        if (byteArray.getBytes() == null) {
            return;
        }
        this.resize(this.length + byteArray.getLength());
        System.arraycopy((Object)byteArray.getBytes(), (int)0, (Object)this.data, (int)n2, (int)byteArray.getLength());
    }

    @Override
    public void appendByteArrayAsLV(ByteArray byteArray) {
        if (byteArray == null || byteArray.getLength() == 0) {
            this.appendByte((byte)0);
            return;
        }
        int n2 = this.length;
        this.resize(1 + this.length + byteArray.getLength());
        this.data[n2] = (byte)byteArray.getLength();
        this.copyArrayToArray(byteArray, 0, n2 + 1, byteArray.getLength());
    }

    @Override
    public ByteArray appendBytes(byte[] arrby, int n2) {
        int n3 = this.length;
        this.resize(n2 + this.length);
        System.arraycopy((Object)arrby, (int)0, (Object)this.data, (int)n3, (int)n2);
        return this;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ByteArray bitWiseAnd(ByteArray byteArray) {
        if (byteArray == null) {
            return this.clone();
        }
        int n2 = this.getLength() < byteArray.getLength() ? this.getLength() : byteArray.getLength();
        DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(this.getLength());
        int n3 = 0;
        while (n3 < n2) {
            defaultByteArrayImpl.setByte(n3, (byte)(this.data[n3] & byteArray.getByte(n3)));
            ++n3;
        }
        return defaultByteArrayImpl;
    }

    @Override
    public void clear() {
        this.fill((byte)0);
    }

    @Override
    public ByteArray clone() {
        return new DefaultByteArrayImpl(this.getBytes());
    }

    public void copyArrayToArray(ByteArray byteArray, int n2, int n3, int n4) {
        if (byteArray.getBytes() == null) {
            return;
        }
        System.arraycopy((Object)byteArray.getBytes(), (int)n2, (Object)this.data, (int)n3, (int)n4);
    }

    @Override
    public void copyBufferToArray(byte[] arrby, int n2, int n3, int n4) {
        if (arrby == null || n3 + n4 > this.data.length) {
            return;
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.data, (int)n3, (int)n4);
    }

    @Override
    public void copyBytes(ByteArray byteArray, int n2, int n3, int n4) {
        if (n3 + n4 > this.length) {
            this.resize(this.length + (n3 + n4 - this.length));
        }
        System.arraycopy((Object)byteArray.getBytes(), (int)n2, (Object)this.data, (int)n3, (int)n4);
    }

    @Override
    public ByteArray copyOfRange(int n2, int n3) {
        int n4 = n3 - n2;
        return new DefaultByteArrayImpl(Arrays.copyOfRange((byte[])this.data, (int)n2, (int)n3), n4);
    }

    @Override
    public ByteArray fill(byte by) {
        Arrays.fill((byte[])this.data, (byte)by);
        return this;
    }

    @Override
    public byte getByte(int n2) {
        return this.data[n2];
    }

    @Override
    public byte[] getBytes() {
        return this.data;
    }

    @Override
    public String getHexString() {
        return this.toHexString();
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public String getString() {
        return new String(this.data);
    }

    @Override
    public ByteArray getUTF8() {
        try {
            DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(this.getHexString().getBytes("UTF-8"));
            return defaultByteArrayImpl;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
    }

    @Override
    public boolean isEqual(ByteArray byteArray) {
        return Arrays.equals((byte[])this.data, (byte[])byteArray.getBytes());
    }

    @Override
    public ByteArray makeXor(ByteArray byteArray) {
        DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(this.getLength());
        int n2 = 0;
        while (n2 < defaultByteArrayImpl.getLength()) {
            defaultByteArrayImpl.setByte(n2, (byte)(this.getByte(n2) ^ byteArray.getByte(n2)));
            ++n2;
        }
        return defaultByteArrayImpl;
    }

    @Override
    public void parityFix() {
        int n2 = this.getLength();
        int n3 = 0;
        block0 : while (n3 < n2) {
            int n4 = 0;
            int n5 = 1;
            int n6 = 0;
            do {
                if (n4 >= 8) {
                    if (!(n6 & true)) {
                        this.setByte(n3, (byte)(1 ^ this.getByte(n3)));
                    }
                    ++n3;
                    continue block0;
                }
                if ((n5 & this.getByte(n3)) != 0) {
                    ++n6;
                }
                n5 *= 2;
                ++n4;
            } while (true);
            break;
        }
        return;
    }

    public void resize(int n2) {
        if (n2 > this.length) {
            byte[] arrby = new byte[n2];
            if (this.data != null) {
                System.arraycopy((Object)this.data, (int)0, (Object)arrby, (int)0, (int)this.length);
            }
            this.data = arrby;
        }
        this.length = n2;
    }

    @Override
    public void setByte(int n2, byte by) {
        this.data[n2] = by;
    }

    @Override
    public void setBytes(byte[] arrby) {
        this.data = Arrays.copyOf((byte[])arrby, (int)arrby.length);
        this.length = this.data.length;
    }

    @Override
    public void setShort(int n2, short s2) {
        this.data[n2] = (byte)(s2 >> 8);
        this.data[n2 + 1] = (byte)(s2 & 255);
    }

    public String toHexString() {
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = 0;
        while (n2 < this.length) {
            byte by = this.data[n2];
            stringBuffer.append(digits.charAt(15 & by >> 4));
            stringBuffer.append(digits.charAt(by & 15));
            ++n2;
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return this.getHexString();
    }
}

