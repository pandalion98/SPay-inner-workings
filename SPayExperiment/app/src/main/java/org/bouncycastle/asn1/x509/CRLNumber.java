/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;

public class CRLNumber
extends ASN1Object {
    private BigInteger number;

    public CRLNumber(BigInteger bigInteger) {
        this.number = bigInteger;
    }

    public static CRLNumber getInstance(Object object) {
        if (object instanceof CRLNumber) {
            return (CRLNumber)object;
        }
        if (object != null) {
            return new CRLNumber(ASN1Integer.getInstance(object).getValue());
        }
        return null;
    }

    public BigInteger getCRLNumber() {
        return this.number;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new ASN1Integer(this.number);
    }

    public String toString() {
        return "CRLNumber: " + (Object)this.getCRLNumber();
    }
}

