package com.mastercard.mobile_api.bytes;

import com.mastercard.mobile_api.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class AndroidByteArray implements ByteArray {
    private byte[] data;
    private int length;

    public AndroidByteArray(int i) {
        this.length = i;
        this.data = new byte[i];
    }

    public AndroidByteArray(String str) {
        int i = 0;
        if (str == null) {
            this.length = 0;
            return;
        }
        int length = str.length();
        if (str.length() % 2 != 0) {
            throw new IllegalArgumentException("Number of characters should be even for a hexadecimal buffer");
        }
        this.length = length / 2;
        this.data = new byte[this.length];
        while (i < this.length) {
            this.data[i] = (byte) Integer.parseInt(str.substring(i * 2, (i * 2) + 2), 16);
            i++;
        }
    }

    public AndroidByteArray(byte[] bArr) {
        this.length = bArr.length;
        this.data = new byte[this.length];
        System.arraycopy(bArr, 0, this.data, 0, this.length);
    }

    public AndroidByteArray(byte[] bArr, int i) {
        this.length = i;
        this.data = new byte[i];
        System.arraycopy(bArr, 0, this.data, 0, i);
    }

    public AndroidByteArray(byte[] bArr, int i, int i2) {
        this.length = i2;
        this.data = new byte[this.length];
        System.arraycopy(bArr, i, this.data, 0, this.length);
    }

    public byte[] getBytes() {
        return this.data;
    }

    public void setBytes(byte[] bArr) {
        this.data = Arrays.copyOf(bArr, bArr.length);
        this.length = this.data.length;
    }

    public int getLength() {
        return this.length;
    }

    public ByteArray append(ByteArray byteArray) {
        int i = this.length;
        if (byteArray.getBytes() != null) {
            resize(this.length + byteArray.getLength());
            System.arraycopy(byteArray.getBytes(), 0, this.data, i, byteArray.getLength());
        }
        return this;
    }

    public void setShort(int i, short s) {
        this.data[i] = (byte) (s >> 8);
        this.data[i + 1] = (byte) (s & GF2Field.MASK);
    }

    public void copyBufferToArray(byte[] bArr, int i, int i2, int i3) {
        if (bArr != null && i2 + i3 <= this.data.length) {
            System.arraycopy(bArr, i, this.data, i2, i3);
        }
    }

    public void copyArrayToArray(ByteArray byteArray, int i, int i2, int i3) {
        if (byteArray.getBytes() != null) {
            System.arraycopy(byteArray.getBytes(), i, this.data, i2, i3);
        }
    }

    public void setByte(int i, byte b) {
        this.data[i] = b;
    }

    public byte getByte(int i) {
        return this.data[i];
    }

    public void resize(int i) {
        if (i > this.length) {
            Object obj = new byte[i];
            if (this.data != null) {
                System.arraycopy(this.data, 0, obj, 0, this.length);
                Utils.clearByteArray(this.data);
            }
            this.data = obj;
        }
        this.length = i;
    }

    public void appendByteArray(ByteArray byteArray) {
        int i = this.length;
        if (byteArray.getBytes() != null) {
            resize(this.length + byteArray.getLength());
            System.arraycopy(byteArray.getBytes(), 0, this.data, i, byteArray.getLength());
        }
    }

    public void appendBuffer(byte[] bArr, int i, int i2) {
        int i3 = this.length;
        if (i <= bArr.length) {
            resize(this.length + i2);
            System.arraycopy(bArr, i, this.data, i3, i2);
        }
    }

    public ByteArray appendByte(byte b) {
        int i = this.length;
        resize(this.length + 1);
        this.data[i] = b;
        return this;
    }

    public ByteArray appendBytes(byte[] bArr, int i) {
        int i2 = this.length;
        resize(this.length + i);
        System.arraycopy(bArr, 0, this.data, i2, i);
        return this;
    }

    public void appendByteArrayAsLV(ByteArray byteArray) {
        if (byteArray == null || byteArray.getLength() == 0) {
            appendByte((byte) 0);
            return;
        }
        int i = this.length;
        resize((this.length + 1) + byteArray.getLength());
        this.data[i] = (byte) byteArray.getLength();
        copyArrayToArray(byteArray, 0, i + 1, byteArray.getLength());
    }

    public String getHexString() {
        return toHexString();
    }

    public String getString() {
        return new String(this.data);
    }

    public void copyBytes(ByteArray byteArray, int i, int i2, int i3) {
        if (i2 + i3 > this.length) {
            resize(this.length + ((i2 + i3) - this.length));
        }
        System.arraycopy(byteArray.getBytes(), i, this.data, i2, i3);
    }

    public boolean isEqual(ByteArray byteArray) {
        return Arrays.equals(this.data, byteArray.getBytes());
    }

    public ByteArray fill(byte b) {
        Arrays.fill(this.data, b);
        return this;
    }

    public String toHexString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.length; i++) {
            byte b = this.data[i];
            String str = "0123456789ABCDEF";
            stringBuffer.append(str.charAt((b >> 4) & 15));
            stringBuffer.append(str.charAt(b & 15));
        }
        return stringBuffer.toString();
    }

    public void clear() {
        fill((byte) 0);
    }

    public ByteArray copyOfRange(int i, int i2) {
        return new AndroidByteArray(Arrays.copyOfRange(this.data, i, i2), i2 - i);
    }

    public ByteArray makeXor(ByteArray byteArray) {
        ByteArray androidByteArray = new AndroidByteArray(getLength());
        for (int i = 0; i < androidByteArray.getLength(); i++) {
            androidByteArray.setByte(i, (byte) (getByte(i) ^ byteArray.getByte(i)));
        }
        return androidByteArray;
    }

    public void parityFix() {
        int length = getLength();
        for (int i = 0; i < length; i++) {
            int i2 = 1;
            int i3 = 0;
            for (int i4 = 0; i4 < 8; i4++) {
                if ((getByte(i) & i2) != 0) {
                    i3++;
                }
                i2 *= 2;
            }
            if ((i3 & 1) == 0) {
                setByte(i, (byte) (getByte(i) ^ 1));
            }
        }
    }

    public ByteArray getUTF8() {
        try {
            return new AndroidByteArray(getHexString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public ByteArray clone() {
        return new AndroidByteArray(getBytes());
    }

    public ByteArray bitWiseAnd(ByteArray byteArray) {
        if (byteArray == null) {
            return clone();
        }
        int length = getLength() < byteArray.getLength() ? getLength() : byteArray.getLength();
        ByteArray androidByteArray = new AndroidByteArray(getLength());
        for (int i = 0; i < length; i++) {
            androidByteArray.setByte(i, (byte) (this.data[i] & byteArray.getByte(i)));
        }
        return androidByteArray;
    }
}
