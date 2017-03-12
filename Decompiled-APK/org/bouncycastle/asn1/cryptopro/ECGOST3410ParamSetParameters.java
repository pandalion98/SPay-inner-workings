package org.bouncycastle.asn1.cryptopro;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class ECGOST3410ParamSetParameters extends ASN1Object {
    ASN1Integer f22a;
    ASN1Integer f23b;
    ASN1Integer f24p;
    ASN1Integer f25q;
    ASN1Integer f26x;
    ASN1Integer f27y;

    public ECGOST3410ParamSetParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, int i, BigInteger bigInteger5) {
        this.f22a = new ASN1Integer(bigInteger);
        this.f23b = new ASN1Integer(bigInteger2);
        this.f24p = new ASN1Integer(bigInteger3);
        this.f25q = new ASN1Integer(bigInteger4);
        this.f26x = new ASN1Integer((long) i);
        this.f27y = new ASN1Integer(bigInteger5);
    }

    public ECGOST3410ParamSetParameters(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.f22a = (ASN1Integer) objects.nextElement();
        this.f23b = (ASN1Integer) objects.nextElement();
        this.f24p = (ASN1Integer) objects.nextElement();
        this.f25q = (ASN1Integer) objects.nextElement();
        this.f26x = (ASN1Integer) objects.nextElement();
        this.f27y = (ASN1Integer) objects.nextElement();
    }

    public static ECGOST3410ParamSetParameters getInstance(Object obj) {
        if (obj == null || (obj instanceof ECGOST3410ParamSetParameters)) {
            return (ECGOST3410ParamSetParameters) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new ECGOST3410ParamSetParameters((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid GOST3410Parameter: " + obj.getClass().getName());
    }

    public static ECGOST3410ParamSetParameters getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public BigInteger getA() {
        return this.f22a.getPositiveValue();
    }

    public BigInteger getP() {
        return this.f24p.getPositiveValue();
    }

    public BigInteger getQ() {
        return this.f25q.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.f22a);
        aSN1EncodableVector.add(this.f23b);
        aSN1EncodableVector.add(this.f24p);
        aSN1EncodableVector.add(this.f25q);
        aSN1EncodableVector.add(this.f26x);
        aSN1EncodableVector.add(this.f27y);
        return new DERSequence(aSN1EncodableVector);
    }
}
