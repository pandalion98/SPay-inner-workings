/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class DHParameter
extends ASN1Object {
    ASN1Integer g;
    ASN1Integer l;
    ASN1Integer p;

    public DHParameter(BigInteger bigInteger, BigInteger bigInteger2, int n2) {
        this.p = new ASN1Integer(bigInteger);
        this.g = new ASN1Integer(bigInteger2);
        if (n2 != 0) {
            this.l = new ASN1Integer(n2);
            return;
        }
        this.l = null;
    }

    private DHParameter(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.p = ASN1Integer.getInstance(enumeration.nextElement());
        this.g = ASN1Integer.getInstance(enumeration.nextElement());
        if (enumeration.hasMoreElements()) {
            this.l = (ASN1Integer)enumeration.nextElement();
            return;
        }
        this.l = null;
    }

    public static DHParameter getInstance(Object object) {
        if (object instanceof DHParameter) {
            return (DHParameter)object;
        }
        if (object != null) {
            return new DHParameter(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getG() {
        return this.g.getPositiveValue();
    }

    public BigInteger getL() {
        if (this.l == null) {
            return null;
        }
        return this.l.getPositiveValue();
    }

    public BigInteger getP() {
        return this.p.getPositiveValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.p);
        aSN1EncodableVector.add(this.g);
        if (this.getL() != null) {
            aSN1EncodableVector.add(this.l);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

