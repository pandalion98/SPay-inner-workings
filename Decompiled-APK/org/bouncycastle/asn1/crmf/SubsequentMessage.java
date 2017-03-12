package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Integer;

public class SubsequentMessage extends ASN1Integer {
    public static final SubsequentMessage challengeResp;
    public static final SubsequentMessage encrCert;

    static {
        encrCert = new SubsequentMessage(0);
        challengeResp = new SubsequentMessage(1);
    }

    private SubsequentMessage(int i) {
        super((long) i);
    }

    public static SubsequentMessage valueOf(int i) {
        if (i == 0) {
            return encrCert;
        }
        if (i == 1) {
            return challengeResp;
        }
        throw new IllegalArgumentException("unknown value: " + i);
    }
}
