package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class GeneralSubtree extends ASN1Object {
    private static final BigInteger ZERO;
    private GeneralName base;
    private ASN1Integer maximum;
    private ASN1Integer minimum;

    static {
        ZERO = BigInteger.valueOf(0);
    }

    private GeneralSubtree(ASN1Sequence aSN1Sequence) {
        this.base = GeneralName.getInstance(aSN1Sequence.getObjectAt(0));
        ASN1TaggedObject instance;
        switch (aSN1Sequence.size()) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                instance = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(1));
                switch (instance.getTagNo()) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        this.minimum = ASN1Integer.getInstance(instance, false);
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        this.maximum = ASN1Integer.getInstance(instance, false);
                    default:
                        throw new IllegalArgumentException("Bad tag number: " + instance.getTagNo());
                }
            case F2m.PPB /*3*/:
                instance = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(1));
                if (instance.getTagNo() != 0) {
                    throw new IllegalArgumentException("Bad tag number for 'minimum': " + instance.getTagNo());
                }
                this.minimum = ASN1Integer.getInstance(instance, false);
                instance = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(2));
                if (instance.getTagNo() != 1) {
                    throw new IllegalArgumentException("Bad tag number for 'maximum': " + instance.getTagNo());
                }
                this.maximum = ASN1Integer.getInstance(instance, false);
            default:
                throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
    }

    public GeneralSubtree(GeneralName generalName) {
        this(generalName, null, null);
    }

    public GeneralSubtree(GeneralName generalName, BigInteger bigInteger, BigInteger bigInteger2) {
        this.base = generalName;
        if (bigInteger2 != null) {
            this.maximum = new ASN1Integer(bigInteger2);
        }
        if (bigInteger == null) {
            this.minimum = null;
        } else {
            this.minimum = new ASN1Integer(bigInteger);
        }
    }

    public static GeneralSubtree getInstance(Object obj) {
        return obj == null ? null : obj instanceof GeneralSubtree ? (GeneralSubtree) obj : new GeneralSubtree(ASN1Sequence.getInstance(obj));
    }

    public static GeneralSubtree getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return new GeneralSubtree(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public GeneralName getBase() {
        return this.base;
    }

    public BigInteger getMaximum() {
        return this.maximum == null ? null : this.maximum.getValue();
    }

    public BigInteger getMinimum() {
        return this.minimum == null ? ZERO : this.minimum.getValue();
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.base);
        if (!(this.minimum == null || this.minimum.getValue().equals(ZERO))) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.minimum));
        }
        if (this.maximum != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.maximum));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
