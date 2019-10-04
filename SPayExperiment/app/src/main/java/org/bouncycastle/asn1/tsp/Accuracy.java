/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.tsp;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

public class Accuracy
extends ASN1Object {
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
        if (aSN1Integer2 != null && (aSN1Integer2.getValue().intValue() < 1 || aSN1Integer2.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
        }
        this.millis = aSN1Integer2;
        if (aSN1Integer3 != null && (aSN1Integer3.getValue().intValue() < 1 || aSN1Integer3.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
        }
        this.micros = aSN1Integer3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private Accuracy(ASN1Sequence aSN1Sequence) {
        this.seconds = null;
        this.millis = null;
        this.micros = null;
        int n2 = 0;
        while (n2 < aSN1Sequence.size()) {
            if (aSN1Sequence.getObjectAt(n2) instanceof ASN1Integer) {
                this.seconds = (ASN1Integer)aSN1Sequence.getObjectAt(n2);
            } else if (aSN1Sequence.getObjectAt(n2) instanceof DERTaggedObject) {
                DERTaggedObject dERTaggedObject = (DERTaggedObject)aSN1Sequence.getObjectAt(n2);
                switch (dERTaggedObject.getTagNo()) {
                    default: {
                        throw new IllegalArgumentException("Invalig tag number");
                    }
                    case 0: {
                        this.millis = ASN1Integer.getInstance(dERTaggedObject, false);
                        if (this.millis.getValue().intValue() >= 1 && this.millis.getValue().intValue() <= 999) break;
                        throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
                    }
                    case 1: {
                        this.micros = ASN1Integer.getInstance(dERTaggedObject, false);
                        if (this.micros.getValue().intValue() >= 1 && this.micros.getValue().intValue() <= 999) break;
                        throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
                    }
                }
            }
            ++n2;
        }
        return;
    }

    public static Accuracy getInstance(Object object) {
        if (object instanceof Accuracy) {
            return (Accuracy)object;
        }
        if (object != null) {
            return new Accuracy(ASN1Sequence.getInstance(object));
        }
        return null;
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

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.seconds != null) {
            aSN1EncodableVector.add(this.seconds);
        }
        if (this.millis != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
        }
        if (this.micros != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.micros));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

