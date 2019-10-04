/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.eac;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.eac.PublicKeyDataObject;
import org.bouncycastle.asn1.eac.UnsignedInteger;

public class ECDSAPublicKey
extends PublicKeyDataObject {
    private static final int A = 2;
    private static final int B = 4;
    private static final int F = 64;
    private static final int G = 8;
    private static final int P = 1;
    private static final int R = 16;
    private static final int Y = 32;
    private byte[] basePointG;
    private BigInteger cofactorF;
    private BigInteger firstCoefA;
    private int options;
    private BigInteger orderOfBasePointR;
    private BigInteger primeModulusP;
    private byte[] publicPointY;
    private BigInteger secondCoefB;
    private ASN1ObjectIdentifier usage;

    public ECDSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, byte[] arrby, BigInteger bigInteger4, byte[] arrby2, int n2) {
        this.usage = aSN1ObjectIdentifier;
        this.setPrimeModulusP(bigInteger);
        this.setFirstCoefA(bigInteger2);
        this.setSecondCoefB(bigInteger3);
        this.setBasePointG(new DEROctetString(arrby));
        this.setOrderOfBasePointR(bigInteger4);
        this.setPublicPointY(new DEROctetString(arrby2));
        this.setCofactorF(BigInteger.valueOf((long)n2));
    }

    public ECDSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] arrby) {
        this.usage = aSN1ObjectIdentifier;
        this.setPublicPointY(new DEROctetString(arrby));
    }

    ECDSAPublicKey(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(enumeration.nextElement());
        this.options = 0;
        block9 : while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            if (object instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)object;
                switch (aSN1TaggedObject.getTagNo()) {
                    default: {
                        this.options = 0;
                        throw new IllegalArgumentException("Unknown Object Identifier!");
                    }
                    case 1: {
                        this.setPrimeModulusP(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        continue block9;
                    }
                    case 2: {
                        this.setFirstCoefA(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        continue block9;
                    }
                    case 3: {
                        this.setSecondCoefB(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        continue block9;
                    }
                    case 4: {
                        this.setBasePointG(ASN1OctetString.getInstance(aSN1TaggedObject, false));
                        continue block9;
                    }
                    case 5: {
                        this.setOrderOfBasePointR(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                        continue block9;
                    }
                    case 6: {
                        this.setPublicPointY(ASN1OctetString.getInstance(aSN1TaggedObject, false));
                        continue block9;
                    }
                    case 7: 
                }
                this.setCofactorF(UnsignedInteger.getInstance(aSN1TaggedObject).getValue());
                continue;
            }
            throw new IllegalArgumentException("Unknown Object Identifier!");
        }
        if (this.options != 32 && this.options != 127) {
            throw new IllegalArgumentException("All options must be either present or absent!");
        }
    }

    private void setBasePointG(ASN1OctetString aSN1OctetString) {
        if ((8 & this.options) == 0) {
            this.options = 8 | this.options;
            this.basePointG = aSN1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Base Point G already set");
    }

    private void setCofactorF(BigInteger bigInteger) {
        if ((64 & this.options) == 0) {
            this.options = 64 | this.options;
            this.cofactorF = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Cofactor F already set");
    }

    private void setFirstCoefA(BigInteger bigInteger) {
        if ((2 & this.options) == 0) {
            this.options = 2 | this.options;
            this.firstCoefA = bigInteger;
            return;
        }
        throw new IllegalArgumentException("First Coef A already set");
    }

    private void setOrderOfBasePointR(BigInteger bigInteger) {
        if ((16 & this.options) == 0) {
            this.options = 16 | this.options;
            this.orderOfBasePointR = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Order of base point R already set");
    }

    private void setPrimeModulusP(BigInteger bigInteger) {
        if ((1 & this.options) == 0) {
            this.options = 1 | this.options;
            this.primeModulusP = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Prime Modulus P already set");
    }

    private void setPublicPointY(ASN1OctetString aSN1OctetString) {
        if ((32 & this.options) == 0) {
            this.options = 32 | this.options;
            this.publicPointY = aSN1OctetString.getOctets();
            return;
        }
        throw new IllegalArgumentException("Public Point Y already set");
    }

    private void setSecondCoefB(BigInteger bigInteger) {
        if ((4 & this.options) == 0) {
            this.options = 4 | this.options;
            this.secondCoefB = bigInteger;
            return;
        }
        throw new IllegalArgumentException("Second Coef B already set");
    }

    public ASN1EncodableVector getASN1EncodableVector(ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(aSN1ObjectIdentifier);
        if (!bl) {
            aSN1EncodableVector.add(new UnsignedInteger(1, this.getPrimeModulusP()));
            aSN1EncodableVector.add(new UnsignedInteger(2, this.getFirstCoefA()));
            aSN1EncodableVector.add(new UnsignedInteger(3, this.getSecondCoefB()));
            aSN1EncodableVector.add(new DERTaggedObject(false, 4, new DEROctetString(this.getBasePointG())));
            aSN1EncodableVector.add(new UnsignedInteger(5, this.getOrderOfBasePointR()));
        }
        aSN1EncodableVector.add(new DERTaggedObject(false, 6, new DEROctetString(this.getPublicPointY())));
        if (!bl) {
            aSN1EncodableVector.add(new UnsignedInteger(7, this.getCofactorF()));
        }
        return aSN1EncodableVector;
    }

    public byte[] getBasePointG() {
        if ((8 & this.options) != 0) {
            return this.basePointG;
        }
        return null;
    }

    public BigInteger getCofactorF() {
        if ((64 & this.options) != 0) {
            return this.cofactorF;
        }
        return null;
    }

    public BigInteger getFirstCoefA() {
        if ((2 & this.options) != 0) {
            return this.firstCoefA;
        }
        return null;
    }

    public BigInteger getOrderOfBasePointR() {
        if ((16 & this.options) != 0) {
            return this.orderOfBasePointR;
        }
        return null;
    }

    public BigInteger getPrimeModulusP() {
        if ((1 & this.options) != 0) {
            return this.primeModulusP;
        }
        return null;
    }

    public byte[] getPublicPointY() {
        if ((32 & this.options) != 0) {
            return this.publicPointY;
        }
        return null;
    }

    public BigInteger getSecondCoefB() {
        if ((4 & this.options) != 0) {
            return this.secondCoefB;
        }
        return null;
    }

    @Override
    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }

    public boolean hasParameters() {
        return this.primeModulusP != null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.getASN1EncodableVector(this.usage, false));
    }
}

