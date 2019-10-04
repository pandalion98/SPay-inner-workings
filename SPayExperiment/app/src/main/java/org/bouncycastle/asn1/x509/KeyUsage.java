/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public class KeyUsage
extends ASN1Object {
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

    public KeyUsage(int n2) {
        this.bitString = new DERBitString(n2);
    }

    private KeyUsage(DERBitString dERBitString) {
        this.bitString = dERBitString;
    }

    public static KeyUsage fromExtensions(Extensions extensions) {
        return KeyUsage.getInstance(extensions.getExtensionParsedValue(Extension.keyUsage));
    }

    public static KeyUsage getInstance(Object object) {
        if (object instanceof KeyUsage) {
            return (KeyUsage)object;
        }
        if (object != null) {
            return new KeyUsage(DERBitString.getInstance(object));
        }
        return null;
    }

    public byte[] getBytes() {
        return this.bitString.getBytes();
    }

    public int getPadBits() {
        return this.bitString.getPadBits();
    }

    public boolean hasUsages(int n2) {
        return (n2 & this.bitString.intValue()) == n2;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.bitString;
    }

    public String toString() {
        byte[] arrby = this.bitString.getBytes();
        if (arrby.length == 1) {
            return "KeyUsage: 0x" + Integer.toHexString((int)(255 & arrby[0]));
        }
        return "KeyUsage: 0x" + Integer.toHexString((int)((255 & arrby[1]) << 8 | 255 & arrby[0]));
    }
}

