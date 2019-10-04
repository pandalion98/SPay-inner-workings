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
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.field.FiniteField
 *  org.bouncycastle.math.field.Polynomial
 *  org.bouncycastle.math.field.PolynomialExtensionField
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.asn1.ua;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ua.DSTU4145BinaryField;
import org.bouncycastle.asn1.ua.DSTU4145PointEncoder;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.Polynomial;
import org.bouncycastle.math.field.PolynomialExtensionField;
import org.bouncycastle.util.Arrays;

public class DSTU4145ECBinary
extends ASN1Object {
    ASN1Integer a;
    ASN1OctetString b;
    ASN1OctetString bp;
    DSTU4145BinaryField f;
    ASN1Integer n;
    BigInteger version;

    private DSTU4145ECBinary(ASN1Sequence aSN1Sequence) {
        block3 : {
            int n2;
            block2 : {
                this.version = BigInteger.valueOf((long)0L);
                boolean bl = aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject;
                n2 = 0;
                if (!bl) break block2;
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Sequence.getObjectAt(0);
                if (!aSN1TaggedObject.isExplicit() || aSN1TaggedObject.getTagNo() != 0) break block3;
                this.version = ASN1Integer.getInstance(aSN1TaggedObject.getLoadedObject()).getValue();
                n2 = 1;
            }
            this.f = DSTU4145BinaryField.getInstance(aSN1Sequence.getObjectAt(n2));
            int n3 = n2 + 1;
            this.a = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(n3));
            int n4 = n3 + 1;
            this.b = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n4));
            int n5 = n4 + 1;
            this.n = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(n5));
            this.bp = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n5 + 1));
            return;
        }
        throw new IllegalArgumentException("object parse error");
    }

    /*
     * Enabled aggressive block sorting
     */
    public DSTU4145ECBinary(ECDomainParameters eCDomainParameters) {
        this.version = BigInteger.valueOf((long)0L);
        ECCurve eCCurve = eCDomainParameters.getCurve();
        if (!ECAlgorithms.isF2mCurve((ECCurve)eCCurve)) {
            throw new IllegalArgumentException("only binary domain is possible");
        }
        int[] arrn = ((PolynomialExtensionField)eCCurve.getField()).getMinimalPolynomial().getExponentsPresent();
        if (arrn.length == 3) {
            this.f = new DSTU4145BinaryField(arrn[2], arrn[1]);
        } else if (arrn.length == 5) {
            this.f = new DSTU4145BinaryField(arrn[4], arrn[1], arrn[2], arrn[3]);
        }
        this.a = new ASN1Integer(eCCurve.getA().toBigInteger());
        this.b = new DEROctetString(eCCurve.getB().getEncoded());
        this.n = new ASN1Integer(eCDomainParameters.getN());
        this.bp = new DEROctetString(DSTU4145PointEncoder.encodePoint(eCDomainParameters.getG()));
    }

    public static DSTU4145ECBinary getInstance(Object object) {
        if (object instanceof DSTU4145ECBinary) {
            return (DSTU4145ECBinary)object;
        }
        if (object != null) {
            return new DSTU4145ECBinary(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getA() {
        return this.a.getValue();
    }

    public byte[] getB() {
        return Arrays.clone((byte[])this.b.getOctets());
    }

    public DSTU4145BinaryField getField() {
        return this.f;
    }

    public byte[] getG() {
        return Arrays.clone((byte[])this.bp.getOctets());
    }

    public BigInteger getN() {
        return this.n.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version.compareTo(BigInteger.valueOf((long)0L)) != 0) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, new ASN1Integer(this.version)));
        }
        aSN1EncodableVector.add(this.f);
        aSN1EncodableVector.add(this.a);
        aSN1EncodableVector.add(this.b);
        aSN1EncodableVector.add(this.n);
        aSN1EncodableVector.add(this.bp);
        return new DERSequence(aSN1EncodableVector);
    }
}

