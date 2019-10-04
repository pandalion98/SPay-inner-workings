/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.util.Date
 */
package org.bouncycastle.asn1;

import java.util.Date;
import org.bouncycastle.asn1.ASN1UTCTime;

public class DERUTCTime
extends ASN1UTCTime {
    public DERUTCTime(String string) {
        super(string);
    }

    public DERUTCTime(Date date) {
        super(date);
    }

    DERUTCTime(byte[] arrby) {
        super(arrby);
    }
}

