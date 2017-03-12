package org.bouncycastle.asn1.oiw;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class ElGamalParameter extends ASN1Object {
    ASN1Integer f39g;
    ASN1Integer f40p;

    public ElGamalParameter(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f40p = new ASN1Integer(bigInteger);
        this.f39g = new ASN1Integer(bigInteger2);
    }

    private ElGamalParameter(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.f40p = (ASN1Integer) objects.nextElement();
        this.f39g = (ASN1Integer) objects.nextElement();
    }

    public static ElGamalParameter getInstance(Object obj) {
        return obj instanceof ElGamalParameter ? (ElGamalParameter) obj : obj != null ? new ElGamalParameter(ASN1Sequence.getInstance(obj)) : null;
    }

    public BigInteger getG() {
        return this.f39g.getPositiveValue();
    }

    public BigInteger getP() {
        return this.f40p.getPositiveValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.f40p);
        aSN1EncodableVector.add(this.f39g);
        return new DERSequence(aSN1EncodableVector);
    }
}
