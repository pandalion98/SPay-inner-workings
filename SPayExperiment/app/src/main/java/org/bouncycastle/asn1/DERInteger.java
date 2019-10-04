/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Integer;

public class DERInteger
extends ASN1Integer {
    public DERInteger(long l2) {
        super(l2);
    }

    public DERInteger(BigInteger bigInteger) {
        super(bigInteger);
    }

    public DERInteger(byte[] arrby) {
        super(arrby, true);
    }
}

