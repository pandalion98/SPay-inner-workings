package org.bouncycastle.asn1.x509.qualified;

import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERPrintableString;

public class Iso4217CurrencyCode extends ASN1Object implements ASN1Choice {
    final int ALPHABETIC_MAXSIZE;
    final int NUMERIC_MAXSIZE;
    final int NUMERIC_MINSIZE;
    int numeric;
    ASN1Encodable obj;

    public Iso4217CurrencyCode(int i) {
        this.ALPHABETIC_MAXSIZE = 3;
        this.NUMERIC_MINSIZE = 1;
        this.NUMERIC_MAXSIZE = DSRPConstants.DSRP_INPUT_CURRENCY_CODE_MAX;
        if (i > DSRPConstants.DSRP_INPUT_CURRENCY_CODE_MAX || i < 1) {
            throw new IllegalArgumentException("wrong size in numeric code : not in (1..999)");
        }
        this.obj = new ASN1Integer((long) i);
    }

    public Iso4217CurrencyCode(String str) {
        this.ALPHABETIC_MAXSIZE = 3;
        this.NUMERIC_MINSIZE = 1;
        this.NUMERIC_MAXSIZE = DSRPConstants.DSRP_INPUT_CURRENCY_CODE_MAX;
        if (str.length() > 3) {
            throw new IllegalArgumentException("wrong size in alphabetic code : max size is 3");
        }
        this.obj = new DERPrintableString(str);
    }

    public static Iso4217CurrencyCode getInstance(Object obj) {
        if (obj == null || (obj instanceof Iso4217CurrencyCode)) {
            return (Iso4217CurrencyCode) obj;
        }
        if (obj instanceof ASN1Integer) {
            return new Iso4217CurrencyCode(ASN1Integer.getInstance(obj).getValue().intValue());
        }
        if (obj instanceof DERPrintableString) {
            return new Iso4217CurrencyCode(DERPrintableString.getInstance(obj).getString());
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }

    public String getAlphabetic() {
        return ((DERPrintableString) this.obj).getString();
    }

    public int getNumeric() {
        return ((ASN1Integer) this.obj).getValue().intValue();
    }

    public boolean isAlphabetic() {
        return this.obj instanceof DERPrintableString;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.obj.toASN1Primitive();
    }
}
