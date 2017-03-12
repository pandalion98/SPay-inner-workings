package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class DHDomainParameters extends ASN1Object {
    private ASN1Integer f68g;
    private ASN1Integer f69j;
    private ASN1Integer f70p;
    private ASN1Integer f71q;
    private DHValidationParms validationParms;

    public DHDomainParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, DHValidationParms dHValidationParms) {
        if (bigInteger == null) {
            throw new IllegalArgumentException("'p' cannot be null");
        } else if (bigInteger2 == null) {
            throw new IllegalArgumentException("'g' cannot be null");
        } else if (bigInteger3 == null) {
            throw new IllegalArgumentException("'q' cannot be null");
        } else {
            this.f70p = new ASN1Integer(bigInteger);
            this.f68g = new ASN1Integer(bigInteger2);
            this.f71q = new ASN1Integer(bigInteger3);
            this.f69j = new ASN1Integer(bigInteger4);
            this.validationParms = dHValidationParms;
        }
    }

    public DHDomainParameters(ASN1Integer aSN1Integer, ASN1Integer aSN1Integer2, ASN1Integer aSN1Integer3, ASN1Integer aSN1Integer4, DHValidationParms dHValidationParms) {
        if (aSN1Integer == null) {
            throw new IllegalArgumentException("'p' cannot be null");
        } else if (aSN1Integer2 == null) {
            throw new IllegalArgumentException("'g' cannot be null");
        } else if (aSN1Integer3 == null) {
            throw new IllegalArgumentException("'q' cannot be null");
        } else {
            this.f70p = aSN1Integer;
            this.f68g = aSN1Integer2;
            this.f71q = aSN1Integer3;
            this.f69j = aSN1Integer4;
            this.validationParms = dHValidationParms;
        }
    }

    private DHDomainParameters(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 3 || aSN1Sequence.size() > 5) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration objects = aSN1Sequence.getObjects();
        this.f70p = ASN1Integer.getInstance(objects.nextElement());
        this.f68g = ASN1Integer.getInstance(objects.nextElement());
        this.f71q = ASN1Integer.getInstance(objects.nextElement());
        ASN1Encodable next = getNext(objects);
        if (next != null && (next instanceof ASN1Integer)) {
            this.f69j = ASN1Integer.getInstance(next);
            next = getNext(objects);
        }
        if (next != null) {
            this.validationParms = DHValidationParms.getInstance(next.toASN1Primitive());
        }
    }

    public static DHDomainParameters getInstance(Object obj) {
        if (obj == null || (obj instanceof DHDomainParameters)) {
            return (DHDomainParameters) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new DHDomainParameters((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid DHDomainParameters: " + obj.getClass().getName());
    }

    public static DHDomainParameters getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    private static ASN1Encodable getNext(Enumeration enumeration) {
        return enumeration.hasMoreElements() ? (ASN1Encodable) enumeration.nextElement() : null;
    }

    public ASN1Integer getG() {
        return this.f68g;
    }

    public ASN1Integer getJ() {
        return this.f69j;
    }

    public ASN1Integer getP() {
        return this.f70p;
    }

    public ASN1Integer getQ() {
        return this.f71q;
    }

    public DHValidationParms getValidationParms() {
        return this.validationParms;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.f70p);
        aSN1EncodableVector.add(this.f68g);
        aSN1EncodableVector.add(this.f71q);
        if (this.f69j != null) {
            aSN1EncodableVector.add(this.f69j);
        }
        if (this.validationParms != null) {
            aSN1EncodableVector.add(this.validationParms);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
