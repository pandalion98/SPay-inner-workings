/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Vector
 */
package org.bouncycastle.asn1.x500;

import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class X500NameBuilder {
    private Vector rdns = new Vector();
    private X500NameStyle template;

    public X500NameBuilder() {
        this(BCStyle.INSTANCE);
    }

    public X500NameBuilder(X500NameStyle x500NameStyle) {
        this.template = x500NameStyle;
    }

    public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] arraSN1ObjectIdentifier, String[] arrstring) {
        ASN1Encodable[] arraSN1Encodable = new ASN1Encodable[arrstring.length];
        for (int i2 = 0; i2 != arraSN1Encodable.length; ++i2) {
            arraSN1Encodable[i2] = this.template.stringToValue(arraSN1ObjectIdentifier[i2], arrstring[i2]);
        }
        return this.addMultiValuedRDN(arraSN1ObjectIdentifier, arraSN1Encodable);
    }

    public X500NameBuilder addMultiValuedRDN(ASN1ObjectIdentifier[] arraSN1ObjectIdentifier, ASN1Encodable[] arraSN1Encodable) {
        AttributeTypeAndValue[] arrattributeTypeAndValue = new AttributeTypeAndValue[arraSN1ObjectIdentifier.length];
        for (int i2 = 0; i2 != arraSN1ObjectIdentifier.length; ++i2) {
            arrattributeTypeAndValue[i2] = new AttributeTypeAndValue(arraSN1ObjectIdentifier[i2], arraSN1Encodable[i2]);
        }
        return this.addMultiValuedRDN(arrattributeTypeAndValue);
    }

    public X500NameBuilder addMultiValuedRDN(AttributeTypeAndValue[] arrattributeTypeAndValue) {
        this.rdns.addElement((Object)new RDN(arrattributeTypeAndValue));
        return this;
    }

    public X500NameBuilder addRDN(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        this.addRDN(aSN1ObjectIdentifier, this.template.stringToValue(aSN1ObjectIdentifier, string));
        return this;
    }

    public X500NameBuilder addRDN(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.rdns.addElement((Object)new RDN(aSN1ObjectIdentifier, aSN1Encodable));
        return this;
    }

    public X500NameBuilder addRDN(AttributeTypeAndValue attributeTypeAndValue) {
        this.rdns.addElement((Object)new RDN(attributeTypeAndValue));
        return this;
    }

    public X500Name build() {
        RDN[] arrrDN = new RDN[this.rdns.size()];
        for (int i2 = 0; i2 != arrrDN.length; ++i2) {
            arrrDN[i2] = (RDN)this.rdns.elementAt(i2);
        }
        return new X500Name(this.template, arrrDN);
    }
}

