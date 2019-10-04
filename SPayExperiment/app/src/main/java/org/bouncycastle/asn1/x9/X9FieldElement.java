/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECFieldElement$F2m
 *  org.bouncycastle.math.ec.ECFieldElement$Fp
 */
package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.math.ec.ECFieldElement;

public class X9FieldElement
extends ASN1Object {
    private static X9IntegerConverter converter = new X9IntegerConverter();
    protected ECFieldElement f;

    public X9FieldElement(int n2, int n3, int n4, int n5, ASN1OctetString aSN1OctetString) {
        this((ECFieldElement)new ECFieldElement.F2m(n2, n3, n4, n5, new BigInteger(1, aSN1OctetString.getOctets())));
    }

    public X9FieldElement(BigInteger bigInteger, ASN1OctetString aSN1OctetString) {
        this((ECFieldElement)new ECFieldElement.Fp(bigInteger, new BigInteger(1, aSN1OctetString.getOctets())));
    }

    public X9FieldElement(ECFieldElement eCFieldElement) {
        this.f = eCFieldElement;
    }

    public ECFieldElement getValue() {
        return this.f;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        int n2 = converter.getByteLength(this.f);
        return new DEROctetString(converter.integerToBytes(this.f.toBigInteger(), n2));
    }
}

