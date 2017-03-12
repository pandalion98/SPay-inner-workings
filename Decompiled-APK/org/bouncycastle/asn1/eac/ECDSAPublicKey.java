package org.bouncycastle.asn1.eac;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;

public class ECDSAPublicKey extends PublicKeyDataObject {
    private static final int f31A = 2;
    private static final int f32B = 4;
    private static final int f33F = 64;
    private static final int f34G = 8;
    private static final int f35P = 1;
    private static final int f36R = 16;
    private static final int f37Y = 32;
    private byte[] basePointG;
    private BigInteger cofactorF;
    private BigInteger firstCoefA;
    private int options;
    private BigInteger orderOfBasePointR;
    private BigInteger primeModulusP;
    private byte[] publicPointY;
    private BigInteger secondCoefB;
    private ASN1ObjectIdentifier usage;

    public ECDSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, byte[] bArr, BigInteger bigInteger4, byte[] bArr2, int i) {
        this.usage = aSN1ObjectIdentifier;
        setPrimeModulusP(bigInteger);
        setFirstCoefA(bigInteger2);
        setSecondCoefB(bigInteger3);
        setBasePointG(new DEROctetString(bArr));
        setOrderOfBasePointR(bigInteger4);
        setPublicPointY(new DEROctetString(bArr2));
        setCofactorF(BigInteger.valueOf((long) i));
    }

    public ECDSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] bArr) {
        this.usage = aSN1ObjectIdentifier;
        setPublicPointY(new DEROctetString(bArr));
    }

    ECDSAPublicKey(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        this.options = 0;
        while (objects.hasMoreElements()) {
            Object nextElement = objects.nextElement();
            if (nextElement instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) nextElement;
                switch (aSN1TaggedObject.getTagNo()) {
                    case f35P /*1*/:
                        setPrimeModulusP(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        break;
                    case f31A /*2*/:
                        setFirstCoefA(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        break;
                    case F2m.PPB /*3*/:
                        setSecondCoefB(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        break;
                    case f32B /*4*/:
                        setBasePointG(ASN1OctetString.getInstance(aSN1TaggedObject, false));
                        break;
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        setOrderOfBasePointR(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        break;
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        setPublicPointY(ASN1OctetString.getInstance(aSN1TaggedObject, false));
                        break;
                    case ECCurve.COORD_SKEWED /*7*/:
                        setCofactorF(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        break;
                    default:
                        this.options = 0;
                        throw new IllegalArgumentException("Unknown Object Identifier!");
                }
            }
            throw new IllegalArgumentException("Unknown Object Identifier!");
        }
        if (this.options != f37Y && this.options != CertificateBody.profileType) {
            throw new IllegalArgumentException("All options must be either present or absent!");
        }
    }

    private void setBasePointG(ASN1OctetString aSN1OctetString) {
        if ((this.options & f34G) == 0) {
            this.options |= f34G;
            this.basePointG = aSN1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Base Point G already set");
    }

    private void setCofactorF(BigInteger bigInteger) {
        if ((this.options & f33F) == 0) {
            this.options |= f33F;
            this.cofactorF = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Cofactor F already set");
    }

    private void setFirstCoefA(BigInteger bigInteger) {
        if ((this.options & f31A) == 0) {
            this.options |= f31A;
            this.firstCoefA = bigInteger;
            return;
        }
        throw new IllegalArgumentException("First Coef A already set");
    }

    private void setOrderOfBasePointR(BigInteger bigInteger) {
        if ((this.options & f36R) == 0) {
            this.options |= f36R;
            this.orderOfBasePointR = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Order of base point R already set");
    }

    private void setPrimeModulusP(BigInteger bigInteger) {
        if ((this.options & f35P) == 0) {
            this.options |= f35P;
            this.primeModulusP = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Prime Modulus P already set");
    }

    private void setPublicPointY(ASN1OctetString aSN1OctetString) {
        if ((this.options & f37Y) == 0) {
            this.options |= f37Y;
            this.publicPointY = aSN1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Public Point Y already set");
    }

    private void setSecondCoefB(BigInteger bigInteger) {
        if ((this.options & f32B) == 0) {
            this.options |= f32B;
            this.secondCoefB = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Second Coef B already set");
    }

    public ASN1EncodableVector getASN1EncodableVector(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean z) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(aSN1ObjectIdentifier);
        if (!z) {
            aSN1EncodableVector.add(new UnsignedInteger(f35P, getPrimeModulusP()));
            aSN1EncodableVector.add(new UnsignedInteger(f31A, getFirstCoefA()));
            aSN1EncodableVector.add(new UnsignedInteger(3, getSecondCoefB()));
            aSN1EncodableVector.add(new DERTaggedObject(false, f32B, new DEROctetString(getBasePointG())));
            aSN1EncodableVector.add(new UnsignedInteger(5, getOrderOfBasePointR()));
        }
        aSN1EncodableVector.add(new DERTaggedObject(false, 6, new DEROctetString(getPublicPointY())));
        if (!z) {
            aSN1EncodableVector.add(new UnsignedInteger(7, getCofactorF()));
        }
        return aSN1EncodableVector;
    }

    public byte[] getBasePointG() {
        return (this.options & f34G) != 0 ? this.basePointG : null;
    }

    public BigInteger getCofactorF() {
        return (this.options & f33F) != 0 ? this.cofactorF : null;
    }

    public BigInteger getFirstCoefA() {
        return (this.options & f31A) != 0 ? this.firstCoefA : null;
    }

    public BigInteger getOrderOfBasePointR() {
        return (this.options & f36R) != 0 ? this.orderOfBasePointR : null;
    }

    public BigInteger getPrimeModulusP() {
        return (this.options & f35P) != 0 ? this.primeModulusP : null;
    }

    public byte[] getPublicPointY() {
        return (this.options & f37Y) != 0 ? this.publicPointY : null;
    }

    public BigInteger getSecondCoefB() {
        return (this.options & f32B) != 0 ? this.secondCoefB : null;
    }

    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }

    public boolean hasParameters() {
        return this.primeModulusP != null;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(getASN1EncodableVector(this.usage, false));
    }
}
