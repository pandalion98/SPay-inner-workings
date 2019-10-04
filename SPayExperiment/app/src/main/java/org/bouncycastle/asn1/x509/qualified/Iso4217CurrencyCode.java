/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509.qualified;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERPrintableString;

public class Iso4217CurrencyCode
extends ASN1Object
implements ASN1Choice {
    final int ALPHABETIC_MAXSIZE = 3;
    final int NUMERIC_MAXSIZE = 999;
    final int NUMERIC_MINSIZE = 1;
    int numeric;
    ASN1Encodable obj;

    public Iso4217CurrencyCode(int n2) {
        if (n2 > 999 || n2 < 1) {
            throw new IllegalArgumentException("wrong size in numeric code : not in (1..999)");
        }
        this.obj = new ASN1Integer(n2);
    }

    public Iso4217CurrencyCode(String string) {
        if (string.length() > 3) {
            throw new IllegalArgumentException("wrong size in alphabetic code : max size is 3");
        }
        this.obj = new DERPrintableString(string);
    }

    public static Iso4217CurrencyCode getInstance(Object object) {
        if (object == null || object instanceof Iso4217CurrencyCode) {
            return (Iso4217CurrencyCode)object;
        }
        if (object instanceof ASN1Integer) {
            return new Iso4217CurrencyCode(ASN1Integer.getInstance(object).getValue().intValue());
        }
        if (object instanceof DERPrintableString) {
            return new Iso4217CurrencyCode(DERPrintableString.getInstance(object).getString());
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public String getAlphabetic() {
        return ((DERPrintableString)this.obj).getString();
    }

    public int getNumeric() {
        return ((ASN1Integer)this.obj).getValue().intValue();
    }

    public boolean isAlphabetic() {
        return this.obj instanceof DERPrintableString;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.obj.toASN1Primitive();
    }
}

