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
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.field.PolynomialExtensionField;

public class X9ECParameters extends ASN1Object implements X9ObjectIdentifiers {
    private static final BigInteger ONE;
    private ECCurve curve;
    private X9FieldID fieldID;
    private ECPoint f73g;
    private BigInteger f74h;
    private BigInteger f75n;
    private byte[] seed;

    static {
        ONE = BigInteger.valueOf(1);
    }

    private X9ECParameters(ASN1Sequence aSN1Sequence) {
        if ((aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) && ((ASN1Integer) aSN1Sequence.getObjectAt(0)).getValue().equals(ONE)) {
            X9Curve x9Curve = new X9Curve(X9FieldID.getInstance(aSN1Sequence.getObjectAt(1)), ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(2)));
            this.curve = x9Curve.getCurve();
            ASN1Encodable objectAt = aSN1Sequence.getObjectAt(3);
            if (objectAt instanceof X9ECPoint) {
                this.f73g = ((X9ECPoint) objectAt).getPoint();
            } else {
                this.f73g = new X9ECPoint(this.curve, (ASN1OctetString) objectAt).getPoint();
            }
            this.f75n = ((ASN1Integer) aSN1Sequence.getObjectAt(4)).getValue();
            this.seed = x9Curve.getSeed();
            if (aSN1Sequence.size() == 6) {
                this.f74h = ((ASN1Integer) aSN1Sequence.getObjectAt(5)).getValue();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("bad version in X9ECParameters");
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger) {
        this(eCCurve, eCPoint, bigInteger, ONE, null);
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        this(eCCurve, eCPoint, bigInteger, bigInteger2, null);
    }

    public X9ECParameters(ECCurve eCCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr) {
        this.curve = eCCurve;
        this.f73g = eCPoint.normalize();
        this.f75n = bigInteger;
        this.f74h = bigInteger2;
        this.seed = bArr;
        if (ECAlgorithms.isFpCurve(eCCurve)) {
            this.fieldID = new X9FieldID(eCCurve.getField().getCharacteristic());
        } else if (ECAlgorithms.isF2mCurve(eCCurve)) {
            int[] exponentsPresent = ((PolynomialExtensionField) eCCurve.getField()).getMinimalPolynomial().getExponentsPresent();
            if (exponentsPresent.length == 3) {
                this.fieldID = new X9FieldID(exponentsPresent[2], exponentsPresent[1]);
            } else if (exponentsPresent.length == 5) {
                this.fieldID = new X9FieldID(exponentsPresent[4], exponentsPresent[1], exponentsPresent[2], exponentsPresent[3]);
            } else {
                throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
            }
        } else {
            throw new IllegalArgumentException("'curve' is of an unsupported type");
        }
    }

    public static X9ECParameters getInstance(Object obj) {
        return obj instanceof X9ECParameters ? (X9ECParameters) obj : obj != null ? new X9ECParameters(ASN1Sequence.getInstance(obj)) : null;
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public ECPoint getG() {
        return this.f73g;
    }

    public BigInteger getH() {
        return this.f74h == null ? ONE : this.f74h;
    }

    public BigInteger getN() {
        return this.f75n;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(1));
        aSN1EncodableVector.add(this.fieldID);
        aSN1EncodableVector.add(new X9Curve(this.curve, this.seed));
        aSN1EncodableVector.add(new X9ECPoint(this.f73g));
        aSN1EncodableVector.add(new ASN1Integer(this.f75n));
        if (this.f74h != null) {
            aSN1EncodableVector.add(new ASN1Integer(this.f74h));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
