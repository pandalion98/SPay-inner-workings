/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.StreamUtil;

public class DEROctetString
extends ASN1OctetString {
    public DEROctetString(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable.toASN1Primitive().getEncoded("DER"));
    }

    public DEROctetString(byte[] arrby) {
        super(arrby);
    }

    static void encode(DEROutputStream dEROutputStream, byte[] arrby) {
        dEROutputStream.writeEncoded(4, arrby);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(4, this.string);
    }

    @Override
    int encodedLength() {
        return 1 + StreamUtil.calculateBodyLength(this.string.length) + this.string.length;
    }

    @Override
    boolean isConstructed() {
        return false;
    }
}

