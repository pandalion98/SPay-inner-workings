/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class CramerShoupCiphertext {
    BigInteger e;
    BigInteger u1;
    BigInteger u2;
    BigInteger v;

    public CramerShoupCiphertext() {
    }

    public CramerShoupCiphertext(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.u1 = bigInteger;
        this.u2 = bigInteger2;
        this.e = bigInteger3;
        this.v = bigInteger4;
    }

    public CramerShoupCiphertext(byte[] arrby) {
        int n2 = Pack.bigEndianToInt((byte[])arrby, (int)0);
        byte[] arrby2 = Arrays.copyOfRange((byte[])arrby, (int)4, (int)(n2 + 4));
        int n3 = n2 + 4;
        this.u1 = new BigInteger(arrby2);
        int n4 = Pack.bigEndianToInt((byte[])arrby, (int)n3);
        int n5 = n3 + 4;
        byte[] arrby3 = Arrays.copyOfRange((byte[])arrby, (int)n5, (int)(n5 + n4));
        int n6 = n5 + n4;
        this.u2 = new BigInteger(arrby3);
        int n7 = Pack.bigEndianToInt((byte[])arrby, (int)n6);
        int n8 = n6 + 4;
        byte[] arrby4 = Arrays.copyOfRange((byte[])arrby, (int)n8, (int)(n8 + n7));
        int n9 = n8 + n7;
        this.e = new BigInteger(arrby4);
        int n10 = Pack.bigEndianToInt((byte[])arrby, (int)n9);
        int n11 = n9 + 4;
        byte[] arrby5 = Arrays.copyOfRange((byte[])arrby, (int)n11, (int)(n11 + n10));
        n11 + n10;
        this.v = new BigInteger(arrby5);
    }

    public BigInteger getE() {
        return this.e;
    }

    public BigInteger getU1() {
        return this.u1;
    }

    public BigInteger getU2() {
        return this.u2;
    }

    public BigInteger getV() {
        return this.v;
    }

    public void setE(BigInteger bigInteger) {
        this.e = bigInteger;
    }

    public void setU1(BigInteger bigInteger) {
        this.u1 = bigInteger;
    }

    public void setU2(BigInteger bigInteger) {
        this.u2 = bigInteger;
    }

    public void setV(BigInteger bigInteger) {
        this.v = bigInteger;
    }

    public byte[] toByteArray() {
        byte[] arrby = this.u1.toByteArray();
        int n2 = arrby.length;
        byte[] arrby2 = this.u2.toByteArray();
        int n3 = arrby2.length;
        byte[] arrby3 = this.e.toByteArray();
        int n4 = arrby3.length;
        byte[] arrby4 = this.v.toByteArray();
        int n5 = arrby4.length;
        byte[] arrby5 = new byte[16 + (n5 + (n4 + (n2 + n3)))];
        Pack.intToBigEndian((int)n2, (byte[])arrby5, (int)0);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby5, (int)4, (int)n2);
        int n6 = n2 + 4;
        Pack.intToBigEndian((int)n3, (byte[])arrby5, (int)n6);
        int n7 = n6 + 4;
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby5, (int)n7, (int)n3);
        int n8 = n7 + n3;
        Pack.intToBigEndian((int)n4, (byte[])arrby5, (int)n8);
        int n9 = n8 + 4;
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby5, (int)n9, (int)n4);
        int n10 = n9 + n4;
        Pack.intToBigEndian((int)n5, (byte[])arrby5, (int)n10);
        int n11 = n10 + 4;
        System.arraycopy((Object)arrby4, (int)0, (Object)arrby5, (int)n11, (int)n5);
        n11 + n5;
        return arrby5;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("u1: " + this.u1.toString());
        stringBuffer.append("\nu2: " + this.u2.toString());
        stringBuffer.append("\ne: " + this.e.toString());
        stringBuffer.append("\nv: " + this.v.toString());
        return stringBuffer.toString();
    }
}

