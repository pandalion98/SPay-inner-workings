/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.modes.gcm;

import java.util.Vector;
import org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.util.Arrays;

public class Tables1kGCMExponentiator
implements GCMExponentiator {
    private Vector lookupPowX2;

    private void ensureAvailable(int n2) {
        int n3 = this.lookupPowX2.size();
        if (n3 <= n2) {
            int[] arrn = (int[])this.lookupPowX2.elementAt(n3 - 1);
            do {
                arrn = Arrays.clone((int[])arrn);
                GCMUtil.multiply(arrn, arrn);
                this.lookupPowX2.addElement((Object)arrn);
            } while (++n3 <= n2);
        }
    }

    @Override
    public void exponentiateX(long l2, byte[] arrby) {
        int[] arrn = GCMUtil.oneAsInts();
        int n2 = 0;
        while (l2 > 0L) {
            if ((1L & l2) != 0L) {
                this.ensureAvailable(n2);
                GCMUtil.multiply(arrn, (int[])this.lookupPowX2.elementAt(n2));
            }
            int n3 = n2 + 1;
            l2 >>>= 1;
            n2 = n3;
        }
        GCMUtil.asBytes(arrn, arrby);
    }

    @Override
    public void init(byte[] arrby) {
        int[] arrn = GCMUtil.asInts(arrby);
        if (this.lookupPowX2 != null && Arrays.areEqual((int[])arrn, (int[])((int[])this.lookupPowX2.elementAt(0)))) {
            return;
        }
        this.lookupPowX2 = new Vector(8);
        this.lookupPowX2.addElement((Object)arrn);
    }
}

