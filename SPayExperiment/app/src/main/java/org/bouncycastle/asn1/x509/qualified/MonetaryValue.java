/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.x509.qualified;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.qualified.Iso4217CurrencyCode;

public class MonetaryValue
extends ASN1Object {
    private ASN1Integer amount;
    private Iso4217CurrencyCode currency;
    private ASN1Integer exponent;

    private MonetaryValue(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.currency = Iso4217CurrencyCode.getInstance(enumeration.nextElement());
        this.amount = ASN1Integer.getInstance(enumeration.nextElement());
        this.exponent = ASN1Integer.getInstance(enumeration.nextElement());
    }

    public MonetaryValue(Iso4217CurrencyCode iso4217CurrencyCode, int n2, int n3) {
        this.currency = iso4217CurrencyCode;
        this.amount = new ASN1Integer(n2);
        this.exponent = new ASN1Integer(n3);
    }

    public static MonetaryValue getInstance(Object object) {
        if (object instanceof MonetaryValue) {
            return (MonetaryValue)object;
        }
        if (object != null) {
            return new MonetaryValue(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getAmount() {
        return this.amount.getValue();
    }

    public Iso4217CurrencyCode getCurrency() {
        return this.currency;
    }

    public BigInteger getExponent() {
        return this.exponent.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.currency);
        aSN1EncodableVector.add(this.amount);
        aSN1EncodableVector.add(this.exponent);
        return new DERSequence(aSN1EncodableVector);
    }
}

