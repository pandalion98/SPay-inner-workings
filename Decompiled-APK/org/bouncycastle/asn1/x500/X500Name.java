package org.bouncycastle.asn1.x500;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class X500Name extends ASN1Object implements ASN1Choice {
    private static X500NameStyle defaultStyle;
    private int hashCodeValue;
    private boolean isHashCodeCalculated;
    private RDN[] rdns;
    private X500NameStyle style;

    static {
        defaultStyle = BCStyle.INSTANCE;
    }

    public X500Name(String str) {
        this(defaultStyle, str);
    }

    private X500Name(ASN1Sequence aSN1Sequence) {
        this(defaultStyle, aSN1Sequence);
    }

    public X500Name(X500NameStyle x500NameStyle, String str) {
        this(x500NameStyle.fromString(str));
        this.style = x500NameStyle;
    }

    private X500Name(X500NameStyle x500NameStyle, ASN1Sequence aSN1Sequence) {
        this.style = x500NameStyle;
        this.rdns = new RDN[aSN1Sequence.size()];
        int i = 0;
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            int i2 = i + 1;
            this.rdns[i] = RDN.getInstance(objects.nextElement());
            i = i2;
        }
    }

    public X500Name(X500NameStyle x500NameStyle, X500Name x500Name) {
        this.rdns = x500Name.rdns;
        this.style = x500NameStyle;
    }

    public X500Name(X500NameStyle x500NameStyle, RDN[] rdnArr) {
        this.rdns = rdnArr;
        this.style = x500NameStyle;
    }

    public X500Name(RDN[] rdnArr) {
        this(defaultStyle, rdnArr);
    }

    public static X500NameStyle getDefaultStyle() {
        return defaultStyle;
    }

    public static X500Name getInstance(Object obj) {
        return obj instanceof X500Name ? (X500Name) obj : obj != null ? new X500Name(ASN1Sequence.getInstance(obj)) : null;
    }

    public static X500Name getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, true));
    }

    public static X500Name getInstance(X500NameStyle x500NameStyle, Object obj) {
        return obj instanceof X500Name ? getInstance(x500NameStyle, ((X500Name) obj).toASN1Primitive()) : obj != null ? new X500Name(x500NameStyle, ASN1Sequence.getInstance(obj)) : null;
    }

    public static void setDefaultStyle(X500NameStyle x500NameStyle) {
        if (x500NameStyle == null) {
            throw new NullPointerException("cannot set style to null");
        }
        defaultStyle = x500NameStyle;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof X500Name) && !(obj instanceof ASN1Sequence)) {
            return false;
        }
        if (toASN1Primitive().equals(((ASN1Encodable) obj).toASN1Primitive())) {
            return true;
        }
        try {
            return this.style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((ASN1Encodable) obj).toASN1Primitive())));
        } catch (Exception e) {
            return false;
        }
    }

    public ASN1ObjectIdentifier[] getAttributeTypes() {
        int i;
        int i2 = 0;
        for (i = 0; i != this.rdns.length; i++) {
            i2 += this.rdns[i].size();
        }
        ASN1ObjectIdentifier[] aSN1ObjectIdentifierArr = new ASN1ObjectIdentifier[i2];
        i2 = 0;
        for (i = 0; i != this.rdns.length; i++) {
            RDN rdn = this.rdns[i];
            int i3;
            if (rdn.isMultiValued()) {
                AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
                i3 = i2;
                i2 = 0;
                while (i2 != typesAndValues.length) {
                    int i4 = i3 + 1;
                    aSN1ObjectIdentifierArr[i3] = typesAndValues[i2].getType();
                    i2++;
                    i3 = i4;
                }
                i2 = i3;
            } else if (rdn.size() != 0) {
                i3 = i2 + 1;
                aSN1ObjectIdentifierArr[i2] = rdn.getFirst().getType();
                i2 = i3;
            }
        }
        return aSN1ObjectIdentifierArr;
    }

    public RDN[] getRDNs() {
        Object obj = new RDN[this.rdns.length];
        System.arraycopy(this.rdns, 0, obj, 0, obj.length);
        return obj;
    }

    public RDN[] getRDNs(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Object obj = new RDN[this.rdns.length];
        int i = 0;
        for (int i2 = 0; i2 != this.rdns.length; i2++) {
            RDN rdn = this.rdns[i2];
            int i3;
            if (rdn.isMultiValued()) {
                AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
                for (i3 = 0; i3 != typesAndValues.length; i3++) {
                    if (typesAndValues[i3].getType().equals(aSN1ObjectIdentifier)) {
                        i3 = i + 1;
                        obj[i] = rdn;
                        i = i3;
                        break;
                    }
                }
            } else if (rdn.getFirst().getType().equals(aSN1ObjectIdentifier)) {
                i3 = i + 1;
                obj[i] = rdn;
                i = i3;
            }
        }
        Object obj2 = new RDN[i];
        System.arraycopy(obj, 0, obj2, 0, obj2.length);
        return obj2;
    }

    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        this.hashCodeValue = this.style.calculateHashCode(this);
        return this.hashCodeValue;
    }

    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.rdns);
    }

    public String toString() {
        return this.style.toString(this);
    }
}
