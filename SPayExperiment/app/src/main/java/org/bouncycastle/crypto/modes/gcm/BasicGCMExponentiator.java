/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.util.Arrays;

public class BasicGCMExponentiator
implements GCMExponentiator {
    private int[] x;

    @Override
    public void exponentiateX(long l2, byte[] arrby) {
        int[] arrn = GCMUtil.oneAsInts();
        if (l2 > 0L) {
            int[] arrn2 = Arrays.clone((int[])this.x);
            do {
                if ((1L & l2) != 0L) {
                    GCMUtil.multiply(arrn, arrn2);
                }
                GCMUtil.multiply(arrn2, arrn2);
            } while ((l2 >>>= 1) > 0L);
        }
        GCMUtil.asBytes(arrn, arrby);
    }

    @Override
    public void init(byte[] arrby) {
        this.x = GCMUtil.asInts(arrby);
    }
}

