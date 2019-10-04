/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Util;
import java.util.Arrays;

public class IssuerIdentificationNumber {
    byte[] iinBytes;

    public IssuerIdentificationNumber(int n2) {
        if (n2 < 0 || n2 > 1000000) {
            throw new IllegalArgumentException("IIN must be between 0 and 999999, but was " + n2);
        }
        byte[] arrby = Util.intToBinaryEncodedDecimalByteArray(n2);
        if (arrby.length != 6) {
            this.iinBytes = Util.resizeArray(arrby, 6);
            return;
        }
        this.iinBytes = arrby;
    }

    public IssuerIdentificationNumber(byte[] arrby) {
        if (arrby == null) {
            throw new NullPointerException("Param iinBytes cannot be null");
        }
        if (arrby.length != 3) {
            throw new IllegalArgumentException("Param iinBytes must have length 3, but was " + arrby.length);
        }
        this.iinBytes = Arrays.copyOf((byte[])arrby, (int)arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (!(object instanceof IssuerIdentificationNumber)) break block4;
                IssuerIdentificationNumber issuerIdentificationNumber = (IssuerIdentificationNumber)object;
                if (this == issuerIdentificationNumber) {
                    return true;
                }
                if (Arrays.equals((byte[])this.getBytes(), (byte[])issuerIdentificationNumber.getBytes())) break block5;
            }
            return false;
        }
        return true;
    }

    public byte[] getBytes() {
        return Arrays.copyOf((byte[])this.iinBytes, (int)this.iinBytes.length);
    }

    public int getValue() {
        return Util.binaryHexCodedDecimalToInt(this.iinBytes);
    }

    public int hashCode() {
        return 679 + Arrays.hashCode((byte[])this.iinBytes);
    }
}

