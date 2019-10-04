/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class SCrypt {
    private static void BlockMix(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int n2) {
        System.arraycopy((Object)arrn, (int)(-16 + arrn.length), (Object)arrn2, (int)0, (int)16);
        int n3 = arrn.length >>> 1;
        int n4 = 0;
        int n5 = 0;
        for (int i2 = n2 * 2; i2 > 0; --i2) {
            SCrypt.Xor(arrn2, arrn, n5, arrn3);
            Salsa20Engine.salsaCore(8, arrn3, arrn2);
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn4, (int)n4, (int)16);
            n4 = n3 + n5 - n4;
            n5 += 16;
        }
        System.arraycopy((Object)arrn4, (int)0, (Object)arrn, (int)0, (int)arrn4.length);
    }

    private static void Clear(byte[] arrby) {
        if (arrby != null) {
            Arrays.fill((byte[])arrby, (byte)0);
        }
    }

    private static void Clear(int[] arrn) {
        if (arrn != null) {
            Arrays.fill((int[])arrn, (int)0);
        }
    }

    private static void ClearAll(int[][] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            SCrypt.Clear(arrn[i2]);
        }
    }

    private static byte[] MFcrypt(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5) {
        byte[] arrby3;
        int n6 = n3 * 128;
        byte[] arrby4 = SCrypt.SingleIterationPBKDF2(arrby, arrby2, n4 * n6);
        int[] arrn = null;
        int n7 = arrby4.length >>> 2;
        arrn = new int[n7];
        Pack.littleEndianToInt((byte[])arrby4, (int)0, (int[])arrn);
        int n8 = n6 >>> 2;
        for (int i2 = 0; i2 < n7; i2 += n8) {
            SCrypt.SMix(arrn, i2, n2, n3);
        }
        try {
            Pack.intToLittleEndian((int[])arrn, (byte[])arrby4, (int)0);
            arrby3 = SCrypt.SingleIterationPBKDF2(arrby, arrby4, n5);
        }
        catch (Throwable throwable) {
            SCrypt.Clear(arrby4);
            SCrypt.Clear(arrn);
            throw throwable;
        }
        SCrypt.Clear(arrby4);
        SCrypt.Clear(arrn);
        return arrby3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void SMix(int[] arrn, int n2, int n3, int n4) {
        int n5 = n4 * 32;
        int[] arrn2 = new int[16];
        int[] arrn3 = new int[16];
        int[] arrn4 = new int[n5];
        int[] arrn5 = new int[n5];
        int[][] arrarrn = new int[n3][];
        System.arraycopy((Object)arrn, (int)n2, (Object)arrn5, (int)0, (int)n5);
        for (int i2 = 0; i2 < n3; ++i2) {
            arrarrn[i2] = Arrays.clone((int[])arrn5);
            SCrypt.BlockMix(arrn5, arrn2, arrn3, arrn4, n4);
        }
        int n6 = n3 - 1;
        for (int i3 = 0; i3 < n3; ++i3) {
            SCrypt.Xor(arrn5, arrarrn[n6 & arrn5[n5 - 16]], 0, arrn5);
            SCrypt.BlockMix(arrn5, arrn2, arrn3, arrn4, n4);
            continue;
        }
        try {
            System.arraycopy((Object)arrn5, (int)0, (Object)arrn, (int)n2, (int)n5);
        }
        catch (Throwable throwable) {
            SCrypt.ClearAll(arrarrn);
            SCrypt.ClearAll(new int[][]{arrn5, arrn2, arrn3, arrn4});
            throw throwable;
        }
        SCrypt.ClearAll(arrarrn);
        SCrypt.ClearAll(new int[][]{arrn5, arrn2, arrn3, arrn4});
    }

    private static byte[] SingleIterationPBKDF2(byte[] arrby, byte[] arrby2, int n2) {
        PKCS5S2ParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        pKCS5S2ParametersGenerator.init(arrby, arrby2, 1);
        return ((KeyParameter)((PBEParametersGenerator)pKCS5S2ParametersGenerator).generateDerivedMacParameters(n2 * 8)).getKey();
    }

    private static void Xor(int[] arrn, int[] arrn2, int n2, int[] arrn3) {
        for (int i2 = -1 + arrn3.length; i2 >= 0; --i2) {
            arrn3[i2] = arrn[i2] ^ arrn2[n2 + i2];
        }
    }

    public static byte[] generate(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5) {
        if (arrby == null) {
            throw new IllegalArgumentException("Passphrase P must be provided.");
        }
        if (arrby2 == null) {
            throw new IllegalArgumentException("Salt S must be provided.");
        }
        if (n2 <= 1) {
            throw new IllegalArgumentException("Cost parameter N must be > 1.");
        }
        if (n3 == 1 && n2 > 65536) {
            throw new IllegalArgumentException("Cost parameter N must be > 1 and < 65536.");
        }
        if (n3 < 1) {
            throw new IllegalArgumentException("Block size r must be >= 1.");
        }
        int n6 = Integer.MAX_VALUE / (8 * (n3 * 128));
        if (n4 < 1 || n4 > n6) {
            throw new IllegalArgumentException("Parallelisation parameter p must be >= 1 and <= " + n6 + " (based on block size r of " + n3 + ")");
        }
        if (n5 < 1) {
            throw new IllegalArgumentException("Generated key length dkLen must be >= 1.");
        }
        return SCrypt.MFcrypt(arrby, arrby2, n2, n3, n4, n5);
    }
}

