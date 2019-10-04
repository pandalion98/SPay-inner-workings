/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.util.Enumeration
 *  java.util.Hashtable
 */
package org.bouncycastle.asn1.x500.style;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public abstract class AbstractX500NameStyle
implements X500NameStyle {
    private int calcHashCode(ASN1Encodable aSN1Encodable) {
        return IETFUtils.canonicalize(IETFUtils.valueToString(aSN1Encodable)).hashCode();
    }

    public static Hashtable copyHashTable(Hashtable hashtable) {
        Hashtable hashtable2 = new Hashtable();
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            hashtable2.put(object, hashtable.get(object));
        }
        return hashtable2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean foundMatch(boolean bl, RDN rDN, RDN[] arrrDN) {
        boolean bl2;
        if (bl) {
            int n2 = -1 + arrrDN.length;
            do {
                bl2 = false;
                if (n2 < 0) return bl2;
                if (arrrDN[n2] != null && this.rdnAreEqual(rDN, arrrDN[n2])) {
                    arrrDN[n2] = null;
                    return true;
                }
                --n2;
            } while (true);
        }
        int n3 = 0;
        do {
            int n4 = arrrDN.length;
            bl2 = false;
            if (n3 == n4) return bl2;
            if (arrrDN[n3] != null && this.rdnAreEqual(rDN, arrrDN[n3])) {
                arrrDN[n3] = null;
                return true;
            }
            ++n3;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean areEqual(X500Name x500Name, X500Name x500Name2) {
        RDN[] arrrDN;
        RDN[] arrrDN2 = x500Name.getRDNs();
        if (arrrDN2.length == (arrrDN = x500Name2.getRDNs()).length) {
            boolean bl = arrrDN2[0].getFirst() != null && arrrDN[0].getFirst() != null ? !arrrDN2[0].getFirst().getType().equals(arrrDN[0].getFirst().getType()) : false;
            int n2 = 0;
            do {
                if (n2 == arrrDN2.length) {
                    return true;
                }
                if (!this.foundMatch(bl, arrrDN2[n2], arrrDN)) break;
                ++n2;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int calculateHashCode(X500Name x500Name) {
        RDN[] arrrDN = x500Name.getRDNs();
        int n2 = 0;
        int n3 = 0;
        while (n2 != arrrDN.length) {
            if (arrrDN[n2].isMultiValued()) {
                AttributeTypeAndValue[] arrattributeTypeAndValue = arrrDN[n2].getTypesAndValues();
                int n4 = n3;
                for (int i2 = 0; i2 != arrattributeTypeAndValue.length; ++i2) {
                    n4 = n4 ^ arrattributeTypeAndValue[i2].getType().hashCode() ^ this.calcHashCode(arrattributeTypeAndValue[i2].getValue());
                }
                n3 = n4;
            } else {
                n3 = n3 ^ arrrDN[n2].getFirst().getType().hashCode() ^ this.calcHashCode(arrrDN[n2].getFirst().getValue());
            }
            ++n2;
        }
        return n3;
    }

    protected ASN1Encodable encodeStringValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        return new DERUTF8String(string);
    }

    protected boolean rdnAreEqual(RDN rDN, RDN rDN2) {
        return IETFUtils.rDNAreEqual(rDN, rDN2);
    }

    @Override
    public ASN1Encodable stringToValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (string.length() != 0 && string.charAt(0) == '#') {
            try {
                ASN1Encodable aSN1Encodable = IETFUtils.valueFromHexString(string, 1);
                return aSN1Encodable;
            }
            catch (IOException iOException) {
                throw new RuntimeException("can't recode value for oid " + aSN1ObjectIdentifier.getId());
            }
        }
        if (string.length() != 0 && string.charAt(0) == '\\') {
            string = string.substring(1);
        }
        return this.encodeStringValue(aSN1ObjectIdentifier, string);
    }
}

