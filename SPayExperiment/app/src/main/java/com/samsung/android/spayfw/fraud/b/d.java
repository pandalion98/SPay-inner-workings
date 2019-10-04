/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.NullPointerException
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.fraud.b;

import com.samsung.android.spayfw.fraud.b.b;
import com.samsung.android.spayfw.fraud.c;
import com.samsung.android.spayfw.fraud.h;
import java.util.List;

class d
extends h {
    public d(c c2) {
        super(c2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected float by() {
        int[] arrn = new int[6];
        b.c c2 = b.bK();
        b.b b2 = b.bL();
        if (c2 == null || b2 == null) {
            com.samsung.android.spayfw.utils.h.a((RuntimeException)new NullPointerException("The database does not contain cardholder info tied to this enrollment."));
            return 0.0055f;
        }
        List<b.c> list = b.bM().bV().bR().bW().bQ();
        List<b.c> list2 = b.bM().bU().bV().bR().bQ();
        List<b.b> list3 = b.bM().bV().bS().bP().bQ();
        List<b.b> list4 = b.bM().bU().bV().bS().bQ();
        int n2 = b.a(c2, list2);
        int n3 = b.b(c2, list);
        if (n2 >= 2) {
            arrn[5] = 1 + arrn[5];
        } else if (n2 == 1) {
            arrn[4] = 1 + arrn[4];
        } else {
            if (n3 >= 2) {
                return 1.0f;
            }
            if (n3 == 1) {
                arrn[2] = 1 + arrn[2];
            }
        }
        int n4 = b.a(b2, list4);
        int n5 = b.b(b2, list3);
        com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "matchingNameCount=" + n2 + ", nonmatchingNameCount=" + n3 + ", matchingAddressCount= " + n4 + ", nonmatchingAddressCount= " + n5);
        if (n4 >= 2) {
            arrn[5] = 1 + arrn[5];
        } else if (n4 == 1) {
            arrn[4] = 1 + arrn[4];
        } else if (n5 >= 3) {
            arrn[1] = 1 + arrn[1];
        } else if (n5 == 2) {
            arrn[2] = 1 + arrn[2];
        }
        int n6 = b.ag("24 hour");
        com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "recent provisioning attempts: " + n6);
        if (n6 >= 6) {
            arrn[2] = 1 + arrn[2];
        }
        if (n6 >= 9) {
            arrn[1] = 1 + arrn[1];
        }
        int n7 = b.ah("7 day");
        com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "recent reset count: " + n7);
        if (n7 > 2) {
            arrn[2] = 1 + arrn[2];
        }
        if (n7 > 10) {
            arrn[1] = 1 + arrn[1];
        }
        int n8 = b.ai("6 month");
        com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "6 month token count: " + n8);
        if (n8 > 0) {
            arrn[4] = 1 + arrn[4];
        }
        int n9 = b.ai("12 month");
        com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "12 month token count: " + n9);
        if (n9 > 0) {
            arrn[5] = 1 + arrn[5];
        }
        if (com.samsung.android.spayfw.utils.h.DEBUG && (arrn[0] != 0 || arrn[3] != 0)) {
            throw new IllegalStateException("Buckets 0 and 3 only exist to make bucket indexes intuitive. They should not be used.");
        }
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            com.samsung.android.spayfw.b.c.d("SimpleRiskScoreV1", "bucket[" + i2 + "] = " + arrn[i2]);
        }
        if (arrn[1] >= 2) {
            return 1.0f;
        }
        if (arrn[1] + arrn[2] >= 2) {
            return 0.055f;
        }
        if (arrn[5] >= 2) {
            return 0.0f;
        }
        if (arrn[4] + arrn[5] >= 2) {
            return 5.5E-4f;
        }
        return 0.0055f;
    }

    @Override
    protected byte[] bz() {
        return null;
    }
}

