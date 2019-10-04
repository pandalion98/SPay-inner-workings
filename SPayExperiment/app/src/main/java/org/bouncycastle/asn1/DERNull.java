/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1OutputStream;

public class DERNull
extends ASN1Null {
    public static final DERNull INSTANCE = new DERNull();
    private static final byte[] zeroBytes = new byte[0];

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(5, zeroBytes);
    }

    @Override
    int encodedLength() {
        return 2;
    }

    @Override
    boolean isConstructed() {
        return false;
    }
}

