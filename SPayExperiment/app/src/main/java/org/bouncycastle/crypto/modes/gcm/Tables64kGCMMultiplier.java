/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.reflect.Array
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.modes.gcm;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.GCMUtil;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Tables64kGCMMultiplier
implements GCMMultiplier {
    private byte[] H;
    private int[][][] M;

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void init(byte[] var1_1) {
        if (this.M == null) {
            var2_2 = new int[]{16, 256, 4};
            this.M = (int[][][])Array.newInstance((Class)Integer.TYPE, (int[])var2_2);
        } else if (Arrays.areEqual((byte[])this.H, (byte[])var1_1)) {
            return;
        }
        this.H = Arrays.clone((byte[])var1_1);
        GCMUtil.asInts(var1_1, this.M[0][128]);
        for (var3_3 = 64; var3_3 >= 1; var3_3 >>= 1) {
            GCMUtil.multiplyP(this.M[0][var3_3 + var3_3], this.M[0][var3_3]);
        }
        var4_4 = 0;
        do {
            for (var5_5 = 2; var5_5 < 256; var5_5 += var5_5) {
                for (var7_7 = 1; var7_7 < var5_5; ++var7_7) {
                    GCMUtil.xor(this.M[var4_4][var5_5], this.M[var4_4][var7_7], this.M[var4_4][var5_5 + var7_7]);
                }
            }
            if (++var4_4 == 16) return;
            var6_6 = 128;
            do {
                if (var6_6 <= 0) ** continue;
                GCMUtil.multiplyP8(this.M[var4_4 - 1][var6_6], this.M[var4_4][var6_6]);
                var6_6 >>= 1;
            } while (true);
            break;
        } while (true);
    }

    @Override
    public void multiplyH(byte[] arrby) {
        int[] arrn = new int[4];
        for (int i2 = 15; i2 >= 0; --i2) {
            int[] arrn2 = this.M[i2][255 & arrby[i2]];
            arrn[0] = arrn[0] ^ arrn2[0];
            arrn[1] = arrn[1] ^ arrn2[1];
            arrn[2] = arrn[2] ^ arrn2[2];
            arrn[3] = arrn[3] ^ arrn2[3];
        }
        Pack.intToBigEndian((int[])arrn, (byte[])arrby, (int)0);
    }
}

