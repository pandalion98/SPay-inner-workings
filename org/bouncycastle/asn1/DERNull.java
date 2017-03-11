package org.bouncycastle.asn1;

public class DERNull extends ASN1Null {
    public static final DERNull INSTANCE;
    private static final byte[] zeroBytes;

    static {
        INSTANCE = new DERNull();
        zeroBytes = new byte[0];
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(5, zeroBytes);
    }

    int encodedLength() {
        return 2;
    }

    boolean isConstructed() {
        return false;
    }
}
