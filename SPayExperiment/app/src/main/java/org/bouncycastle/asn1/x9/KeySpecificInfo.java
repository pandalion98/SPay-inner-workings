/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class KeySpecificInfo
extends ASN1Object {
    private ASN1ObjectIdentifier algorithm;
    private ASN1OctetString counter;

    public KeySpecificInfo(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1OctetString aSN1OctetString) {
        this.algorithm = aSN1ObjectIdentifier;
        this.counter = aSN1OctetString;
    }

    public KeySpecificInfo(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.algorithm = (ASN1ObjectIdentifier)enumeration.nextElement();
        this.counter = (ASN1OctetString)enumeration.nextElement();
    }

    public ASN1ObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }

    public ASN1OctetString getCounter() {
        return this.counter;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.algorithm);
        aSN1EncodableVector.add(this.counter);
        return new DERSequence(aSN1EncodableVector);
    }
}

