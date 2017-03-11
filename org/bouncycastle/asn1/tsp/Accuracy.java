package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.math.ec.ECCurve;

public class Accuracy extends ASN1Object {
    protected static final int MAX_MICROS = 999;
    protected static final int MAX_MILLIS = 999;
    protected static final int MIN_MICROS = 1;
    protected static final int MIN_MILLIS = 1;
    ASN1Integer micros;
    ASN1Integer millis;
    ASN1Integer seconds;

    protected Accuracy() {
    }

    public Accuracy(ASN1Integer aSN1Integer, ASN1Integer aSN1Integer2, ASN1Integer aSN1Integer3) {
        this.seconds = aSN1Integer;
        if (aSN1Integer2 == null || (aSN1Integer2.getValue().intValue() >= MIN_MILLIS && aSN1Integer2.getValue().intValue() <= MAX_MILLIS)) {
            this.millis = aSN1Integer2;
            if (aSN1Integer3 == null || (aSN1Integer3.getValue().intValue() >= MIN_MILLIS && aSN1Integer3.getValue().intValue() <= MAX_MILLIS)) {
                this.micros = aSN1Integer3;
                return;
            }
            throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
        }
        throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
    }

    private Accuracy(ASN1Sequence aSN1Sequence) {
        this.seconds = null;
        this.millis = null;
        this.micros = null;
        for (int i = 0; i < aSN1Sequence.size(); i += MIN_MILLIS) {
            if (aSN1Sequence.getObjectAt(i) instanceof ASN1Integer) {
                this.seconds = (ASN1Integer) aSN1Sequence.getObjectAt(i);
            } else if (aSN1Sequence.getObjectAt(i) instanceof DERTaggedObject) {
                DERTaggedObject dERTaggedObject = (DERTaggedObject) aSN1Sequence.getObjectAt(i);
                switch (dERTaggedObject.getTagNo()) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        this.millis = ASN1Integer.getInstance(dERTaggedObject, false);
                        if (this.millis.getValue().intValue() >= MIN_MILLIS && this.millis.getValue().intValue() <= MAX_MILLIS) {
                            break;
                        }
                        throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
                    case MIN_MILLIS /*1*/:
                        this.micros = ASN1Integer.getInstance(dERTaggedObject, false);
                        if (this.micros.getValue().intValue() >= MIN_MILLIS && this.micros.getValue().intValue() <= MAX_MILLIS) {
                            break;
                        }
                        throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
                    default:
                        throw new IllegalArgumentException("Invalig tag number");
                }
            } else {
                continue;
            }
        }
    }

    public static Accuracy getInstance(Object obj) {
        return obj instanceof Accuracy ? (Accuracy) obj : obj != null ? new Accuracy(ASN1Sequence.getInstance(obj)) : null;
    }

    public ASN1Integer getMicros() {
        return this.micros;
    }

    public ASN1Integer getMillis() {
        return this.millis;
    }

    public ASN1Integer getSeconds() {
        return this.seconds;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.seconds != null) {
            aSN1EncodableVector.add(this.seconds);
        }
        if (this.millis != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
        }
        if (this.micros != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, MIN_MILLIS, this.micros));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
