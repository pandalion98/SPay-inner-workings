/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.ua;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class DSTU4145BinaryField
extends ASN1Object {
    private int j;
    private int k;
    private int l;
    private int m;

    public DSTU4145BinaryField(int n2, int n3) {
        this(n2, n3, 0, 0);
    }

    public DSTU4145BinaryField(int n2, int n3, int n4, int n5) {
        this.m = n2;
        this.k = n3;
        this.j = n4;
        this.l = n5;
    }

    private DSTU4145BinaryField(ASN1Sequence aSN1Sequence) {
        this.m = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).getPositiveValue().intValue();
        if (aSN1Sequence.getObjectAt(1) instanceof ASN1Integer) {
            this.k = ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getPositiveValue().intValue();
            return;
        }
        if (aSN1Sequence.getObjectAt(1) instanceof ASN1Sequence) {
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
            this.k = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(0)).getPositiveValue().intValue();
            this.j = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(1)).getPositiveValue().intValue();
            this.l = ASN1Integer.getInstance(aSN1Sequence2.getObjectAt(2)).getPositiveValue().intValue();
            return;
        }
        throw new IllegalArgumentException("object parse error");
    }

    public static DSTU4145BinaryField getInstance(Object object) {
        if (object instanceof DSTU4145BinaryField) {
            return (DSTU4145BinaryField)object;
        }
        if (object != null) {
            return new DSTU4145BinaryField(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public int getK1() {
        return this.k;
    }

    public int getK2() {
        return this.j;
    }

    public int getK3() {
        return this.l;
    }

    public int getM() {
        return this.m;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(this.m));
        if (this.j == 0) {
            aSN1EncodableVector.add(new ASN1Integer(this.k));
            do {
                return new DERSequence(aSN1EncodableVector);
                break;
            } while (true);
        }
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        aSN1EncodableVector2.add(new ASN1Integer(this.k));
        aSN1EncodableVector2.add(new ASN1Integer(this.j));
        aSN1EncodableVector2.add(new ASN1Integer(this.l));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        return new DERSequence(aSN1EncodableVector);
    }
}

