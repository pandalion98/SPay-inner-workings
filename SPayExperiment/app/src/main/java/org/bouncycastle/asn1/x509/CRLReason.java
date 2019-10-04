/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.util.Integers;

public class CRLReason
extends ASN1Object {
    public static final int AA_COMPROMISE = 10;
    public static final int AFFILIATION_CHANGED = 3;
    public static final int CA_COMPROMISE = 2;
    public static final int CERTIFICATE_HOLD = 6;
    public static final int CESSATION_OF_OPERATION = 5;
    public static final int KEY_COMPROMISE = 1;
    public static final int PRIVILEGE_WITHDRAWN = 9;
    public static final int REMOVE_FROM_CRL = 8;
    public static final int SUPERSEDED = 4;
    public static final int UNSPECIFIED = 0;
    public static final int aACompromise = 10;
    public static final int affiliationChanged = 3;
    public static final int cACompromise = 2;
    public static final int certificateHold = 6;
    public static final int cessationOfOperation = 5;
    public static final int keyCompromise = 1;
    public static final int privilegeWithdrawn = 9;
    private static final String[] reasonString = new String[]{"unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise"};
    public static final int removeFromCRL = 8;
    public static final int superseded = 4;
    private static final Hashtable table = new Hashtable();
    public static final int unspecified;
    private ASN1Enumerated value;

    private CRLReason(int n2) {
        this.value = new ASN1Enumerated(n2);
    }

    public static CRLReason getInstance(Object object) {
        if (object instanceof CRLReason) {
            return (CRLReason)object;
        }
        if (object != null) {
            return CRLReason.lookup(ASN1Enumerated.getInstance(object).getValue().intValue());
        }
        return null;
    }

    public static CRLReason lookup(int n2) {
        Integer n3 = Integers.valueOf((int)n2);
        if (!table.containsKey((Object)n3)) {
            table.put((Object)n3, (Object)new CRLReason(n2));
        }
        return (CRLReason)table.get((Object)n3);
    }

    public BigInteger getValue() {
        return this.value.getValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string;
        int n2 = this.getValue().intValue();
        if (n2 < 0 || n2 > 10) {
            string = "invalid";
            do {
                return "CRLReason: " + string;
                break;
            } while (true);
        }
        string = reasonString[n2];
        return "CRLReason: " + string;
    }
}

