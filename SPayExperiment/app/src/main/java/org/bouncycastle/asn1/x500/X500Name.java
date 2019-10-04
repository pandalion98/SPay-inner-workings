/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Enumeration
 */
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
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class X500Name
extends ASN1Object
implements ASN1Choice {
    private static X500NameStyle defaultStyle = BCStyle.INSTANCE;
    private int hashCodeValue;
    private boolean isHashCodeCalculated;
    private RDN[] rdns;
    private X500NameStyle style;

    public X500Name(String string) {
        this(defaultStyle, string);
    }

    private X500Name(ASN1Sequence aSN1Sequence) {
        this(defaultStyle, aSN1Sequence);
    }

    public X500Name(X500NameStyle x500NameStyle, String string) {
        this(x500NameStyle.fromString(string));
        this.style = x500NameStyle;
    }

    private X500Name(X500NameStyle x500NameStyle, ASN1Sequence aSN1Sequence) {
        this.style = x500NameStyle;
        this.rdns = new RDN[aSN1Sequence.size()];
        int n2 = 0;
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            RDN[] arrrDN = this.rdns;
            int n3 = n2 + 1;
            arrrDN[n2] = RDN.getInstance(enumeration.nextElement());
            n2 = n3;
        }
    }

    public X500Name(X500NameStyle x500NameStyle, X500Name x500Name) {
        this.rdns = x500Name.rdns;
        this.style = x500NameStyle;
    }

    public X500Name(X500NameStyle x500NameStyle, RDN[] arrrDN) {
        this.rdns = arrrDN;
        this.style = x500NameStyle;
    }

    public X500Name(RDN[] arrrDN) {
        this(defaultStyle, arrrDN);
    }

    public static X500NameStyle getDefaultStyle() {
        return defaultStyle;
    }

    public static X500Name getInstance(Object object) {
        if (object instanceof X500Name) {
            return (X500Name)object;
        }
        if (object != null) {
            return new X500Name(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static X500Name getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return X500Name.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, true));
    }

    public static X500Name getInstance(X500NameStyle x500NameStyle, Object object) {
        if (object instanceof X500Name) {
            return X500Name.getInstance(x500NameStyle, ((X500Name)object).toASN1Primitive());
        }
        if (object != null) {
            return new X500Name(x500NameStyle, ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static void setDefaultStyle(X500NameStyle x500NameStyle) {
        if (x500NameStyle == null) {
            throw new NullPointerException("cannot set style to null");
        }
        defaultStyle = x500NameStyle;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof X500Name) && !(object instanceof ASN1Sequence)) {
            return false;
        }
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)object).toASN1Primitive();
        if (this.toASN1Primitive().equals(aSN1Primitive)) {
            return true;
        }
        try {
            boolean bl = this.style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((ASN1Encodable)object).toASN1Primitive())));
            return bl;
        }
        catch (Exception exception) {
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public ASN1ObjectIdentifier[] getAttributeTypes() {
        int n2 = 0;
        for (int i2 = 0; i2 != this.rdns.length; n2 += this.rdns[i2].size(), ++i2) {
        }
        ASN1ObjectIdentifier[] arraSN1ObjectIdentifier = new ASN1ObjectIdentifier[n2];
        int n3 = 0;
        int n4 = 0;
        while (n3 != this.rdns.length) {
            RDN rDN = this.rdns[n3];
            if (rDN.isMultiValued()) {
                AttributeTypeAndValue[] arrattributeTypeAndValue = rDN.getTypesAndValues();
                int n5 = n4;
                for (int i3 = 0; i3 != arrattributeTypeAndValue.length; ++i3) {
                    int n6 = n5 + 1;
                    arraSN1ObjectIdentifier[n5] = arrattributeTypeAndValue[i3].getType();
                    n5 = n6;
                }
                n4 = n5;
            } else if (rDN.size() != 0) {
                int n7 = n4 + 1;
                arraSN1ObjectIdentifier[n4] = rDN.getFirst().getType();
                n4 = n7;
            }
            ++n3;
        }
        return arraSN1ObjectIdentifier;
    }

    public RDN[] getRDNs() {
        RDN[] arrrDN = new RDN[this.rdns.length];
        System.arraycopy((Object)this.rdns, (int)0, (Object)arrrDN, (int)0, (int)arrrDN.length);
        return arrrDN;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public RDN[] getRDNs(ASN1ObjectIdentifier var1_1) {
        var2_2 = new RDN[this.rdns.length];
        var3_3 = 0;
        var4_4 = 0;
        block0 : do {
            block8 : {
                if (var3_3 == this.rdns.length) {
                    var5_10 = new RDN[var4_4];
                    System.arraycopy((Object)var2_2, (int)0, (Object)var5_10, (int)0, (int)var5_10.length);
                    return var5_10;
                }
                var6_5 = this.rdns[var3_3];
                if (var6_5.isMultiValued()) break block8;
                if (var6_5.getFirst().getType().equals(var1_1)) {
                    var7_6 = var4_4 + 1;
                    var2_2[var4_4] = var6_5;
                    var4_4 = var7_6;
                }
                ** GOTO lbl25
            }
            var8_7 = var6_5.getTypesAndValues();
            var9_8 = 0;
            do {
                block10 : {
                    block9 : {
                        if (var9_8 == var8_7.length) break block9;
                        if (!var8_7[var9_8].getType().equals(var1_1)) break block10;
                        var10_9 = var4_4 + 1;
                        var2_2[var4_4] = var6_5;
                        var4_4 = var10_9;
                    }
                    ++var3_3;
                    continue block0;
                }
                ++var9_8;
            } while (true);
            break;
        } while (true);
    }

    @Override
    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        this.hashCodeValue = this.style.calculateHashCode(this);
        return this.hashCodeValue;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.rdns);
    }

    public String toString() {
        return this.style.toString(this);
    }
}

