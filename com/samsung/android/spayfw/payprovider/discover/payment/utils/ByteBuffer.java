package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class ByteBuffer {
    private byte[] mBytesArray;

    public ByteBuffer(byte[] bArr) {
        this.mBytesArray = null;
        if (bArr != null) {
            this.mBytesArray = new byte[bArr.length];
            System.arraycopy(bArr, 0, this.mBytesArray, 0, bArr.length);
        }
    }

    public ByteBuffer(int i) {
        this.mBytesArray = null;
        this.mBytesArray = new byte[i];
    }

    public int getSize() {
        if (this.mBytesArray == null) {
            return 0;
        }
        return this.mBytesArray.length;
    }

    public byte[] getBytes() {
        return this.mBytesArray;
    }

    public void setBytes(byte[] bArr) {
        if (bArr != null) {
            this.mBytesArray = (byte[]) bArr.clone();
        }
    }

    public static ByteBuffer getFromLong(long j) {
        byte[] bArr = new byte[8];
        int i = -1;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte length = (byte) ((int) (j >> (((bArr.length - i2) - 1) << 3)));
            if (i == -1 && length != null) {
                i = i2;
            }
            bArr[i2] = length;
        }
        if (i != -1 && i < bArr.length) {
            return new ByteBuffer(Arrays.copyOfRange(bArr, i, bArr.length));
        }
        return new ByteBuffer(new byte[]{(byte) 0});
    }

    public static ByteBuffer getFromLong(long j, int i) {
        if (i > 64) {
            i = 64;
        }
        byte[] bArr = new byte[i];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((int) (j >> (((bArr.length - i2) - 1) << 3)));
        }
        return new ByteBuffer(bArr);
    }

    public int getInt() {
        if (this.mBytesArray.length == 1) {
            return this.mBytesArray[0] & GF2Field.MASK;
        }
        if (this.mBytesArray.length == 2) {
            return ((this.mBytesArray[0] & GF2Field.MASK) << 8) | (this.mBytesArray[1] & GF2Field.MASK);
        }
        if (this.mBytesArray.length == 3) {
            return (((this.mBytesArray[0] & GF2Field.MASK) << 16) | ((this.mBytesArray[1] & GF2Field.MASK) << 8)) | (this.mBytesArray[2] & GF2Field.MASK);
        }
        return ((((this.mBytesArray[0] & GF2Field.MASK) << 24) | ((this.mBytesArray[1] & GF2Field.MASK) << 16)) | ((this.mBytesArray[2] & GF2Field.MASK) << 8)) | (this.mBytesArray[3] & GF2Field.MASK);
    }

    public byte getByte(int i) {
        return this.mBytesArray[i];
    }

    public void setByte(int i, byte b) {
        this.mBytesArray[i] = b;
    }

    public ByteBuffer copyBytes(int i, int i2) {
        byte[] bArr = new byte[(i2 - i)];
        System.arraycopy(this.mBytesArray, i, bArr, 0, i2 - i);
        return new ByteBuffer(bArr);
    }

    public void copyBytes(ByteBuffer byteBuffer, int i, int i2, int i3) {
        if (i2 + i3 > getSize()) {
            Object obj = new byte[(getSize() + ((i2 + i3) - getSize()))];
            System.arraycopy(getBytes(), 0, obj, 0, getSize());
            this.mBytesArray = obj;
        }
        System.arraycopy(byteBuffer.getBytes(), i, this.mBytesArray, i2, i3);
    }

    public ByteBuffer clone() {
        return new ByteBuffer(this.mBytesArray);
    }

    public void append(byte[] bArr) {
        if (bArr != null) {
            Object obj = new byte[(this.mBytesArray.length + bArr.length)];
            System.arraycopy(this.mBytesArray, 0, obj, 0, this.mBytesArray.length);
            System.arraycopy(bArr, 0, obj, this.mBytesArray.length, bArr.length);
            this.mBytesArray = obj;
        }
    }

    public void append(ByteBuffer byteBuffer) {
        if (byteBuffer != null) {
            append(byteBuffer.getBytes());
        }
    }

    public void append(byte b) {
        append(new byte[]{b});
    }

    public void setShort(int i, short s) {
        this.mBytesArray[i] = (byte) (s >> 8);
        this.mBytesArray[i + 1] = (byte) (s & GF2Field.MASK);
    }

    public void setInt(int i, int i2) {
        this.mBytesArray[i] = (byte) (i2 >> 24);
        this.mBytesArray[i + 1] = (byte) (i2 >> 16);
        this.mBytesArray[i + 2] = (byte) (i2 >> 8);
        this.mBytesArray[i + 3] = (byte) (i2 & GF2Field.MASK);
    }

    public String toHexString() {
        String str = "0123456789ABCDEF";
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : this.mBytesArray) {
            stringBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15));
            stringBuffer.append("0123456789ABCDEF".charAt(b & 15));
        }
        return stringBuffer.toString();
    }

    public static ByteBuffer fromHexString(String str) {
        int i = 0;
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            str = new String(HCEClientConstants.HEX_ZERO_BYTE);
        }
        if (str.length() % 2 != 0) {
            StringBuffer stringBuffer = new StringBuffer(str);
            stringBuffer.insert(0, LLVARUtil.EMPTY_STRING);
            str = stringBuffer.toString();
        }
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        while (i < length) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
            i += 2;
        }
        return new ByteBuffer(bArr);
    }

    public void setBit(int i, int i2) {
        byte[] bArr = this.mBytesArray;
        int i3 = i - 1;
        bArr[i3] = (byte) (bArr[i3] | (1 << (i2 - 1)));
    }

    public void setBit(byte b) {
        byte[] bArr = this.mBytesArray;
        int i = (b >> 4) - 1;
        bArr[i] = (byte) (bArr[i] | (1 << ((b & 15) - 1)));
    }

    public void clearBit(int i, int i2) {
        byte[] bArr = this.mBytesArray;
        int i3 = i - 1;
        bArr[i3] = (byte) (bArr[i3] & ((1 << (i2 - 1)) ^ -1));
    }

    public boolean checkBit(int i, int i2) {
        return (this.mBytesArray[i + -1] & (1 << (i2 + -1))) != 0;
    }

    public boolean checkBit(byte b) {
        return (this.mBytesArray[(b >> 4) + -1] & (1 << ((b & 15) + -1))) != 0;
    }

    public boolean equals(ByteBuffer byteBuffer) {
        if (byteBuffer == null && getBytes() == null) {
            return true;
        }
        if ((byteBuffer != null || getBytes() == null) && (byteBuffer == null || getBytes() != null)) {
            return toHexString().equals(byteBuffer.toHexString());
        }
        return false;
    }

    public long getLong() {
        long j = 0;
        for (byte b : this.mBytesArray) {
            j = (j << 8) + ((long) (b & GF2Field.MASK));
        }
        return j;
    }

    public ByteBuffer bitAnd(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return clone();
        }
        int size = getSize() < byteBuffer.getSize() ? getSize() : byteBuffer.getSize();
        ByteBuffer byteBuffer2 = new ByteBuffer(getSize());
        for (int i = 0; i < size; i++) {
            byteBuffer2.setByte(i, (byte) (this.mBytesArray[i] & byteBuffer.getByte(i)));
        }
        return byteBuffer2;
    }

    public boolean checkBitAndMatch(ByteBuffer byteBuffer) {
        ByteBuffer bitAnd = bitAnd(byteBuffer);
        if (bitAnd == null) {
            return false;
        }
        if ((!checkZeros(byteBuffer) || !checkZeros(this) || byteBuffer.getSize() != this.mBytesArray.length) && checkZeros(bitAnd)) {
            return false;
        }
        return true;
    }

    private boolean checkZeros(ByteBuffer byteBuffer) {
        for (byte b : byteBuffer.getBytes()) {
            if (b != null) {
                return false;
            }
        }
        return true;
    }
}
