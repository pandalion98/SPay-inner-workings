/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;

public class BasicGCMMultiplier
implements GCMMultiplier {
    private int[] H;

    @Override
    public void init(byte[] arrby) {
        this.H = GCMUtil.asInts(arrby);
    }

    @Override
    public void multiplyH(byte[] arrby) {
        int[] arrn = GCMUtil.asInts(arrby);
        GCMUtil.multiply(arrn, this.H);
        GCMUtil.asBytes(arrn, arrby);
    }
}

