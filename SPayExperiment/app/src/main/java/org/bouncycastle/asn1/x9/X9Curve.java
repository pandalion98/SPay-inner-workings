/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECCurve$F2m
 *  org.bouncycastle.math.ec.ECCurve$Fp
 *  org.bouncycastle.math.ec.ECFieldElement
 */
package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x9.X9FieldElement;
import org.bouncycastle.asn1.x9.X9FieldID;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class X9Curve
extends ASN1Object
implements X9ObjectIdentifiers {
    private ECCurve curve;
    private ASN1ObjectIdentifier fieldIdentifier = null;
    private byte[] seed;

    /*
     * Enabled aggressive block sorting
     */
    public X9Curve(X9FieldID x9FieldID, ASN1Sequence aSN1Sequence) {
        this.fieldIdentifier = x9FieldID.getIdentifier();
        if (this.fieldIdentifier.equals(prime_field)) {
            BigInteger bigInteger = ((ASN1Integer)x9FieldID.getParameters()).getValue();
            X9FieldElement x9FieldElement = new X9FieldElement(bigInteger, (ASN1OctetString)aSN1Sequence.getObjectAt(0));
            X9FieldElement x9FieldElement2 = new X9FieldElement(bigInteger, (ASN1OctetString)aSN1Sequence.getObjectAt(1));
            this.curve = new ECCurve.Fp(bigInteger, x9FieldElement.getValue().toBigInteger(), x9FieldElement2.getValue().toBigInteger());
        } else {
            int n2;
            int n3;
            int n4;
            if (!this.fieldIdentifier.equals(characteristic_two_field)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(x9FieldID.getParameters());
            int n5 = ((ASN1Integer)aSN1Sequence2.getObjectAt(0)).getValue().intValue();
            ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)aSN1Sequence2.getObjectAt(1);
            if (aSN1ObjectIdentifier.equals(tpBasis)) {
                n2 = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(2)).getValue().intValue();
                n3 = 0;
                n4 = 0;
            } else {
                if (!aSN1ObjectIdentifier.equals(ppBasis)) {
                    throw new IllegalArgumentException("This type of EC basis is not implemented");
                }
                ASN1Sequence aSN1Sequence3 = ASN1Sequence.getInstance(aSN1Sequence2.getObjectAt(2));
                n2 = ASN1Integer.getInstance(aSN1Sequence3.getObjectAt(0)).getValue().intValue();
                n4 = ASN1Integer.getInstance(aSN1Sequence3.getObjectAt(1)).getValue().intValue();
                n3 = ASN1Integer.getInstance(aSN1Sequence3.getObjectAt(2)).getValue().intValue();
            }
            X9FieldElement x9FieldElement = new X9FieldElement(n5, n2, n4, n3, (ASN1OctetString)aSN1Sequence.getObjectAt(0));
            ASN1OctetString aSN1OctetString = (ASN1OctetString)aSN1Sequence.getObjectAt(1);
            X9FieldElement x9FieldElement3 = new X9FieldElement(n5, n2, n4, n3, aSN1OctetString);
            ECCurve.F2m f2m = new ECCurve.F2m(n5, n2, n4, n3, x9FieldElement.getValue().toBigInteger(), x9FieldElement3.getValue().toBigInteger());
            this.curve = f2m;
        }
        if (aSN1Sequence.size() == 3) {
            this.seed = ((DERBitString)aSN1Sequence.getObjectAt(2)).getBytes();
        }
    }

    public X9Curve(ECCurve eCCurve) {
        this.curve = eCCurve;
        this.seed = null;
        this.setFieldIdentifier();
    }

    public X9Curve(ECCurve eCCurve, byte[] arrby) {
        this.curve = eCCurve;
        this.seed = arrby;
        this.setFieldIdentifier();
    }

    private void setFieldIdentifier() {
        if (ECAlgorithms.isFpCurve((ECCurve)this.curve)) {
            this.fieldIdentifier = prime_field;
            return;
        }
        if (ECAlgorithms.isF2mCurve((ECCurve)this.curve)) {
            this.fieldIdentifier = characteristic_two_field;
            return;
        }
        throw new IllegalArgumentException("This type of ECCurve is not implemented");
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.fieldIdentifier.equals(prime_field)) {
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getA()).toASN1Primitive());
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getB()).toASN1Primitive());
        } else if (this.fieldIdentifier.equals(characteristic_two_field)) {
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getA()).toASN1Primitive());
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getB()).toASN1Primitive());
        }
        if (this.seed != null) {
            aSN1EncodableVector.add(new DERBitString(this.seed));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

