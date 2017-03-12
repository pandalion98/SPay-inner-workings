package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class DHParameter extends ASN1Object {
    ASN1Integer f41g;
    ASN1Integer f42l;
    ASN1Integer f43p;

    public DHParameter(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        this.f43p = new ASN1Integer(bigInteger);
        this.f41g = new ASN1Integer(bigInteger2);
        if (i != 0) {
            this.f42l = new ASN1Integer((long) i);
        } else {
            this.f42l = null;
        }
    }

    private DHParameter(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.f43p = ASN1Integer.getInstance(objects.nextElement());
        this.f41g = ASN1Integer.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.f42l = (ASN1Integer) objects.nextElement();
        } else {
            this.f42l = null;
        }
    }

    public static DHParameter getInstance(Object obj) {
        return obj instanceof DHParameter ? (DHParameter) obj : obj != null ? new DHParameter(ASN1Sequence.getInstance(obj)) : null;
    }

    public BigInteger getG() {
        return this.f41g.getPositiveValue();
    }

    public BigInteger getL() {
        return this.f42l == null ? null : this.f42l.getPositiveValue();
    }

    public BigInteger getP() {
        return this.f43p.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.f43p);
        aSN1EncodableVector.add(this.f41g);
        if (getL() != null) {
            aSN1EncodableVector.add(this.f42l);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
