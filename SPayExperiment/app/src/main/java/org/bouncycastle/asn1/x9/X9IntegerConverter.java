/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 */
package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

public class X9IntegerConverter {
    public int getByteLength(ECCurve eCCurve) {
        return (7 + eCCurve.getFieldSize()) / 8;
    }

    public int getByteLength(ECFieldElement eCFieldElement) {
        return (7 + eCFieldElement.getFieldSize()) / 8;
    }

    public byte[] integerToBytes(BigInteger bigInteger, int n2) {
        byte[] arrby = bigInteger.toByteArray();
        if (n2 < arrby.length) {
            byte[] arrby2 = new byte[n2];
            System.arraycopy((Object)arrby, (int)(arrby.length - arrby2.length), (Object)arrby2, (int)0, (int)arrby2.length);
            return arrby2;
        }
        if (n2 > arrby.length) {
            byte[] arrby3 = new byte[n2];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)(arrby3.length - arrby.length), (int)arrby.length);
            return arrby3;
        }
        return arrby;
    }
}

