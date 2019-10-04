/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Enumerated;

public class DEREnumerated
extends ASN1Enumerated {
    public DEREnumerated(int n2) {
        super(n2);
    }

    public DEREnumerated(BigInteger bigInteger) {
        super(bigInteger);
    }

    DEREnumerated(byte[] arrby) {
        super(arrby);
    }
}

