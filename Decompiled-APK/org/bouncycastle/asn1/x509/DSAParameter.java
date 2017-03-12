package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class DSAParameter extends ASN1Object {
    ASN1Integer f60g;
    ASN1Integer f61p;
    ASN1Integer f62q;

    public DSAParameter(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f61p = new ASN1Integer(bigInteger);
        this.f62q = new ASN1Integer(bigInteger2);
        this.f60g = new ASN1Integer(bigInteger3);
    }

    private DSAParameter(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration objects = aSN1Sequence.getObjects();
        this.f61p = ASN1Integer.getInstance(objects.nextElement());
        this.f62q = ASN1Integer.getInstance(objects.nextElement());
        this.f60g = ASN1Integer.getInstance(objects.nextElement());
    }

    public static DSAParameter getInstance(Object obj) {
        return obj instanceof DSAParameter ? (DSAParameter) obj : obj != null ? new DSAParameter(ASN1Sequence.getInstance(obj)) : null;
    }

    public static DSAParameter getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public BigInteger getG() {
        return this.f60g.getPositiveValue();
    }

    public BigInteger getP() {
        return this.f61p.getPositiveValue();
    }

    public BigInteger getQ() {
        return this.f62q.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.f61p);
        aSN1EncodableVector.add(this.f62q);
        aSN1EncodableVector.add(this.f60g);
        return new DERSequence(aSN1EncodableVector);
    }
}
