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

public class Tables8kGCMMultiplier
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
            var2_2 = new int[]{32, 16, 4};
            this.M = (int[][][])Array.newInstance((Class)Integer.TYPE, (int[])var2_2);
        } else if (Arrays.areEqual((byte[])this.H, (byte[])var1_1)) {
            return;
        }
        this.H = Arrays.clone((byte[])var1_1);
        GCMUtil.asInts(var1_1, this.M[1][8]);
        for (var3_3 = 4; var3_3 >= 1; var3_3 >>= 1) {
            GCMUtil.multiplyP(this.M[1][var3_3 + var3_3], this.M[1][var3_3]);
        }
        GCMUtil.multiplyP(this.M[1][1], this.M[0][8]);
        for (var4_4 = 4; var4_4 >= 1; var4_4 >>= 1) {
            GCMUtil.multiplyP(this.M[0][var4_4 + var4_4], this.M[0][var4_4]);
        }
        var5_5 = 0;
        do lbl-1000: // 4 sources:
        {
            for (var6_6 = 2; var6_6 < 16; var6_6 += var6_6) {
                for (var8_8 = 1; var8_8 < var6_6; ++var8_8) {
                    GCMUtil.xor(this.M[var5_5][var6_6], this.M[var5_5][var8_8], this.M[var5_5][var6_6 + var8_8]);
                }
            }
            if (++var5_5 == 32) return;
            if (var5_5 <= 1) ** GOTO lbl-1000
            var7_7 = 8;
            do {
                if (var7_7 <= 0) ** continue;
                GCMUtil.multiplyP8(this.M[var5_5 - 2][var7_7], this.M[var5_5][var7_7]);
                var7_7 >>= 1;
            } while (true);
            break;
        } while (true);
    }

    @Override
    public void multiplyH(byte[] arrby) {
        int[] arrn = new int[4];
        for (int i2 = 15; i2 >= 0; --i2) {
            int[] arrn2 = this.M[i2 + i2][15 & arrby[i2]];
            arrn[0] = arrn[0] ^ arrn2[0];
            arrn[1] = arrn[1] ^ arrn2[1];
            arrn[2] = arrn[2] ^ arrn2[2];
            arrn[3] = arrn[3] ^ arrn2[3];
            int[] arrn3 = this.M[1 + (i2 + i2)][(240 & arrby[i2]) >>> 4];
            arrn[0] = arrn[0] ^ arrn3[0];
            arrn[1] = arrn[1] ^ arrn3[1];
            arrn[2] = arrn[2] ^ arrn3[2];
            arrn[3] = arrn[3] ^ arrn3[3];
        }
        Pack.intToBigEndian((int[])arrn, (byte[])arrby, (int)0);
    }
}

