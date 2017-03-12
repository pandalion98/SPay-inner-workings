package org.bouncycastle.asn1.ua;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class DSTU4145BinaryField extends ASN1Object {
    private int f44j;
    private int f45k;
    private int f46l;
    private int f47m;

    public DSTU4145BinaryField(int i, int i2) {
        this(i, i2, 0, 0);
    }

    public DSTU4145BinaryField(int i, int i2, int i3, int i4) {
        this.f47m = i;
        this.f45k = i2;
        this.f44j = i3;
        this.f46l = i4;
    }

    private DSTU4145BinaryField(ASN1Sequence aSN1Sequence) {
        this.f47m = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).getPositiveValue().intValue();
        if (aSN1Sequence.getObjectAt(1) instanceof ASN1Integer) {
            this.f45k = ((ASN1Integer) aSN1Sequence.getObjectAt(1)).getPositiveValue().intValue();
        } else if (aSN1Sequence.getObjectAt(1) instanceof ASN1Sequence) {
            ASN1Sequence instance = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
            this.f45k = ASN1Integer.getInstance(instance.getObjectAt(0)).getPositiveValue().intValue();
            this.f44j = ASN1Integer.getInstance(instance.getObjectAt(1)).getPositiveValue().intValue();
            this.f46l = ASN1Integer.getInstance(instance.getObjectAt(2)).getPositiveValue().intValue();
        } else {
            throw new IllegalArgumentException("object parse error");
        }
    }

    public static DSTU4145BinaryField getInstance(Object obj) {
        return obj instanceof DSTU4145BinaryField ? (DSTU4145BinaryField) obj : obj != null ? new DSTU4145BinaryField(ASN1Sequence.getInstance(obj)) : null;
    }

    public int getK1() {
        return this.f45k;
    }

    public int getK2() {
        return this.f44j;
    }

    public int getK3() {
        return this.f46l;
    }

    public int getM() {
        return this.f47m;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer((long) this.f47m));
        if (this.f44j == 0) {
            aSN1EncodableVector.add(new ASN1Integer((long) this.f45k));
        } else {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(new ASN1Integer((long) this.f45k));
            aSN1EncodableVector2.add(new ASN1Integer((long) this.f44j));
            aSN1EncodableVector2.add(new ASN1Integer((long) this.f46l));
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
