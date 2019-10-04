/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.util.Date
 */
package org.bouncycastle.asn1;

import java.util.Date;
import org.bouncycastle.asn1.ASN1GeneralizedTime;

public class DERGeneralizedTime
extends ASN1GeneralizedTime {
    public DERGeneralizedTime(String string) {
        super(string);
    }

    public DERGeneralizedTime(Date date) {
        super(date);
    }

    DERGeneralizedTime(byte[] arrby) {
        super(arrby);
    }
}

