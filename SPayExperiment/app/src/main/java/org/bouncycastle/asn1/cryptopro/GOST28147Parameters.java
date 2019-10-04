/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cryptopro;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class GOST28147Parameters
extends ASN1Object {
    private ASN1OctetString iv;
    private ASN1ObjectIdentifier paramSet;

    public GOST28147Parameters(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.iv = (ASN1OctetString)enumeration.nextElement();
        this.paramSet = (ASN1ObjectIdentifier)enumeration.nextElement();
    }

    public static GOST28147Parameters getInstance(Object object) {
        if (object instanceof GOST28147Parameters) {
            return (GOST28147Parameters)object;
        }
        if (object != null) {
            return new GOST28147Parameters(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static GOST28147Parameters getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return GOST28147Parameters.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public ASN1ObjectIdentifier getEncryptionParamSet() {
        return this.paramSet;
    }

    public byte[] getIV() {
        return this.iv.getOctets();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.iv);
        aSN1EncodableVector.add(this.paramSet);
        return new DERSequence(aSN1EncodableVector);
    }
}

