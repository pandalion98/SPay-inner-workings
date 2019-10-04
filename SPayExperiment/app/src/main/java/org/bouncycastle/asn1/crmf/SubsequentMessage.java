/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Integer;

public class SubsequentMessage
extends ASN1Integer {
    public static final SubsequentMessage challengeResp;
    public static final SubsequentMessage encrCert;

    static {
        encrCert = new SubsequentMessage(0);
        challengeResp = new SubsequentMessage(1);
    }

    private SubsequentMessage(int n2) {
        super(n2);
    }

    public static SubsequentMessage valueOf(int n2) {
        if (n2 == 0) {
            return encrCert;
        }
        if (n2 == 1) {
            return challengeResp;
        }
        throw new IllegalArgumentException("unknown value: " + n2);
    }
}

