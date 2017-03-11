package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class KeyUsage extends ASN1Object {
    public static final int cRLSign = 2;
    public static final int dataEncipherment = 16;
    public static final int decipherOnly = 32768;
    public static final int digitalSignature = 128;
    public static final int encipherOnly = 1;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int keyEncipherment = 32;
    public static final int nonRepudiation = 64;
    private DERBitString bitString;

    public KeyUsage(int i) {
        this.bitString = new DERBitString(i);
    }

    private KeyUsage(DERBitString dERBitString) {
        this.bitString = dERBitString;
    }

    public static KeyUsage fromExtensions(Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.keyUsage));
    }

    public static KeyUsage getInstance(Object obj) {
        return obj instanceof KeyUsage ? (KeyUsage) obj : obj != null ? new KeyUsage(DERBitString.getInstance(obj)) : null;
    }

    public byte[] getBytes() {
        return this.bitString.getBytes();
    }

    public int getPadBits() {
        return this.bitString.getPadBits();
    }

    public boolean hasUsages(int i) {
        return (this.bitString.intValue() & i) == i;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.bitString;
    }

    public String toString() {
        byte[] bytes = this.bitString.getBytes();
        if (bytes.length == encipherOnly) {
            return "KeyUsage: 0x" + Integer.toHexString(bytes[0] & GF2Field.MASK);
        }
        return "KeyUsage: 0x" + Integer.toHexString((bytes[0] & GF2Field.MASK) | ((bytes[encipherOnly] & GF2Field.MASK) << keyAgreement));
    }
}
