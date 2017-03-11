package com.americanexpress.sdkmodulelib.tlv;

import java.util.Arrays;

public class IssuerIdentificationNumber {
    byte[] iinBytes;

    public IssuerIdentificationNumber(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException("Param iinBytes cannot be null");
        } else if (bArr.length != 3) {
            throw new IllegalArgumentException("Param iinBytes must have length 3, but was " + bArr.length);
        } else {
            this.iinBytes = Arrays.copyOf(bArr, bArr.length);
        }
    }

    public IssuerIdentificationNumber(int i) {
        if (i < 0 || i > 1000000) {
            throw new IllegalArgumentException("IIN must be between 0 and 999999, but was " + i);
        }
        byte[] intToBinaryEncodedDecimalByteArray = Util.intToBinaryEncodedDecimalByteArray(i);
        if (intToBinaryEncodedDecimalByteArray.length != 6) {
            this.iinBytes = Util.resizeArray(intToBinaryEncodedDecimalByteArray, 6);
        } else {
            this.iinBytes = intToBinaryEncodedDecimalByteArray;
        }
    }

    public int getValue() {
        return Util.binaryHexCodedDecimalToInt(this.iinBytes);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(this.iinBytes, this.iinBytes.length);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IssuerIdentificationNumber)) {
            return false;
        }
        IssuerIdentificationNumber issuerIdentificationNumber = (IssuerIdentificationNumber) obj;
        if (this == issuerIdentificationNumber) {
            return true;
        }
        if (Arrays.equals(getBytes(), issuerIdentificationNumber.getBytes())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.iinBytes) + 679;
    }
}
