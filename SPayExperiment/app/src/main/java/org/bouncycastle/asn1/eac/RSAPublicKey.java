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
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.eac.PublicKeyDataObject;
import org.bouncycastle.asn1.eac.UnsignedInteger;

public class RSAPublicKey
extends PublicKeyDataObject {
    private static int exponentValid;
    private static int modulusValid;
    private BigInteger exponent;
    private BigInteger modulus;
    private ASN1ObjectIdentifier usage;
    private int valid = 0;

    static {
        modulusValid = 1;
        exponentValid = 2;
    }

    public RSAPublicKey(ASN1ObjectIdentifier aSN1ObjectIdentifier, BigInteger bigInteger, BigInteger bigInteger2) {
        this.usage = aSN1ObjectIdentifier;
        this.modulus = bigInteger;
        this.exponent = bigInteger2;
    }

    RSAPublicKey(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.usage = ASN1ObjectIdentifier.getInstance(enumeration.nextElement());
        block4 : while (enumeration.hasMoreElements()) {
            UnsignedInteger unsignedInteger = UnsignedInteger.getInstance(enumeration.nextElement());
            switch (unsignedInteger.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("Unknown DERTaggedObject :" + unsignedInteger.getTagNo() + "-> not an Iso7816RSAPublicKeyStructure");
                }
                case 1: {
                    this.setModulus(unsignedInteger);
                    continue block4;
                }
                case 2: 
            }
            this.setExponent(unsignedInteger);
        }
        if (this.valid != 3) {
            throw new IllegalArgumentException("missing argument -> not an Iso7816RSAPublicKeyStructure");
        }
    }

    private void setExponent(UnsignedInteger unsignedInteger) {
        if ((this.valid & exponentValid) == 0) {
            this.valid |= exponentValid;
            this.exponent = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Exponent already set");
    }

    private void setModulus(UnsignedInteger unsignedInteger) {
        if ((this.valid & modulusValid) == 0) {
            this.valid |= modulusValid;
            this.modulus = unsignedInteger.getValue();
            return;
        }
        throw new IllegalArgumentException("Modulus already set");
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.exponent;
    }

    @Override
    public ASN1ObjectIdentifier getUsage() {
        return this.usage;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.usage);
        aSN1EncodableVector.add(new UnsignedInteger(1, this.getModulus()));
        aSN1EncodableVector.add(new UnsignedInteger(2, this.getPublicExponent()));
        return new DERSequence(aSN1EncodableVector);
    }
}

