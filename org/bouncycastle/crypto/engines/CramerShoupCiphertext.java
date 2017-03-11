package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class CramerShoupCiphertext {
    BigInteger f151e;
    BigInteger u1;
    BigInteger u2;
    BigInteger f152v;

    public CramerShoupCiphertext(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.u1 = bigInteger;
        this.u2 = bigInteger2;
        this.f151e = bigInteger3;
        this.f152v = bigInteger4;
    }

    public CramerShoupCiphertext(byte[] bArr) {
        int bigEndianToInt = Pack.bigEndianToInt(bArr, 0);
        bigEndianToInt += 4;
        this.u1 = new BigInteger(Arrays.copyOfRange(bArr, 4, bigEndianToInt + 4));
        int bigEndianToInt2 = Pack.bigEndianToInt(bArr, bigEndianToInt);
        bigEndianToInt += 4;
        byte[] copyOfRange = Arrays.copyOfRange(bArr, bigEndianToInt, bigEndianToInt + bigEndianToInt2);
        bigEndianToInt += bigEndianToInt2;
        this.u2 = new BigInteger(copyOfRange);
        bigEndianToInt2 = Pack.bigEndianToInt(bArr, bigEndianToInt);
        bigEndianToInt += 4;
        copyOfRange = Arrays.copyOfRange(bArr, bigEndianToInt, bigEndianToInt + bigEndianToInt2);
        bigEndianToInt += bigEndianToInt2;
        this.f151e = new BigInteger(copyOfRange);
        bigEndianToInt2 = Pack.bigEndianToInt(bArr, bigEndianToInt);
        bigEndianToInt += 4;
        copyOfRange = Arrays.copyOfRange(bArr, bigEndianToInt, bigEndianToInt + bigEndianToInt2);
        bigEndianToInt += bigEndianToInt2;
        this.f152v = new BigInteger(copyOfRange);
    }

    public BigInteger getE() {
        return this.f151e;
    }

    public BigInteger getU1() {
        return this.u1;
    }

    public BigInteger getU2() {
        return this.u2;
    }

    public BigInteger getV() {
        return this.f152v;
    }

    public void setE(BigInteger bigInteger) {
        this.f151e = bigInteger;
    }

    public void setU1(BigInteger bigInteger) {
        this.u1 = bigInteger;
    }

    public void setU2(BigInteger bigInteger) {
        this.u2 = bigInteger;
    }

    public void setV(BigInteger bigInteger) {
        this.f152v = bigInteger;
    }

    public byte[] toByteArray() {
        Object toByteArray = this.u1.toByteArray();
        int length = toByteArray.length;
        Object toByteArray2 = this.u2.toByteArray();
        int length2 = toByteArray2.length;
        Object toByteArray3 = this.f151e.toByteArray();
        int length3 = toByteArray3.length;
        Object toByteArray4 = this.f152v.toByteArray();
        int length4 = toByteArray4.length;
        byte[] bArr = new byte[((((length + length2) + length3) + length4) + 16)];
        Pack.intToBigEndian(length, bArr, 0);
        System.arraycopy(toByteArray, 0, bArr, 4, length);
        int i = length + 4;
        Pack.intToBigEndian(length2, bArr, i);
        i += 4;
        System.arraycopy(toByteArray2, 0, bArr, i, length2);
        i += length2;
        Pack.intToBigEndian(length3, bArr, i);
        i += 4;
        System.arraycopy(toByteArray3, 0, bArr, i, length3);
        i += length3;
        Pack.intToBigEndian(length4, bArr, i);
        i += 4;
        System.arraycopy(toByteArray4, 0, bArr, i, length4);
        i += length4;
        return bArr;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("u1: " + this.u1.toString());
        stringBuffer.append("\nu2: " + this.u2.toString());
        stringBuffer.append("\ne: " + this.f151e.toString());
        stringBuffer.append("\nv: " + this.f152v.toString());
        return stringBuffer.toString();
    }
}
