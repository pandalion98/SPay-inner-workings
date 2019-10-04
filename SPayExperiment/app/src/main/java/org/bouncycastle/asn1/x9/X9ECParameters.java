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
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.field.FiniteField
 *  org.bouncycastle.math.field.Polynomial
 *  org.bouncycastle.math.field.PolynomialExtensionField
 */
package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x9.X9Curve;
import org.bouncycastle.asn1.x9.X9ECPoint;
import org.bouncycastle.asn1.x9.X9FieldID;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.Polynomial;
import org.bouncycastle.math.field.PolynomialExtensionField;

public class X9ECParameters
extends ASN1Object
implements X9ObjectIdentifiers {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private ECCurve curve;
    private X9FieldID fieldID;
    private ECPoint g;
    private BigInteger h;
    private BigInteger n;
    private byte[] seed;

    /*
     * Enabled aggressive block sorting
     */
    private X9ECParameters(ASN1Sequence aSN1Sequence) {
        if (!(aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) || !((ASN1Integer)aSN1Sequence.getObjectAt(0)).getValue().equals((Object)ONE)) {
            throw new IllegalArgumentException("bad version in X9ECParameters");
        }
        X9Curve x9Curve = new X9Curve(X9FieldID.getInstance(aSN1Sequence.getObjectAt(1)), ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(2)));
        this.curve = x9Curve.getCurve();
        ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(3);
        this.g = aSN1Encodable instanceof X9ECPoint ? ((X9ECPoint)aSN1Encodable).getPoint() : new X9ECPoint(this.curve, (ASN1OctetString)aSN1Encodable).getPoint();
        this.n = ((ASN1Integer)aSN1Sequence.getObjectAt(4)).getValue();
        this.seed = x9Curve.getSeed();
        if (aSN1Sequence.size() == 6) {
            this.h = ((ASN1Integer)aSN1Sequence.getObjectAt(5)).getValue();
        }
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger) {
        this(eCCurve, eCPoint, bigInteger, ONE, null);
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        this(eCCurve, eCPoint, bigInteger, bigInteger2, null);
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby) {
        this.curve = eCCurve;
        this.g = eCPoint.normalize();
        this.n = bigInteger;
        this.h = bigInteger2;
        this.seed = arrby;
        if (ECAlgorithms.isFpCurve((ECCurve)eCCurve)) {
            this.fieldID = new X9FieldID(eCCurve.getField().getCharacteristic());
            return;
        }
        if (ECAlgorithms.isF2mCurve((ECCurve)eCCurve)) {
            int[] arrn = ((PolynomialExtensionField)eCCurve.getField()).getMinimalPolynomial().getExponentsPresent();
            if (arrn.length == 3) {
                this.fieldID = new X9FieldID(arrn[2], arrn[1]);
                return;
            }
            if (arrn.length == 5) {
                this.fieldID = new X9FieldID(arrn[4], arrn[1], arrn[2], arrn[3]);
                return;
            }
            throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
        }
        throw new IllegalArgumentException("'curve' is of an unsupported type");
    }

    public static X9ECParameters getInstance(Object object) {
        if (object instanceof X9ECParameters) {
            return (X9ECParameters)object;
        }
        if (object != null) {
            return new X9ECParameters(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public ECPoint getG() {
        return this.g;
    }

    public BigInteger getH() {
        if (this.h == null) {
            return ONE;
        }
        return this.h;
    }

    public BigInteger getN() {
        return this.n;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(1L));
        aSN1EncodableVector.add(this.fieldID);
        aSN1EncodableVector.add(new X9Curve(this.curve, this.seed));
        aSN1EncodableVector.add(new X9ECPoint(this.g));
        aSN1EncodableVector.add(new ASN1Integer(this.n));
        if (this.h != null) {
            aSN1EncodableVector.add(new ASN1Integer(this.h));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

