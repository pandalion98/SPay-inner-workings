/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Random
 */
package org.bouncycastle.math.raw;

import java.util.Random;
import org.bouncycastle.math.raw.Nat;

public abstract class Mod {
    public static void add(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4) {
        int n = arrn.length;
        if (Nat.add(n, arrn2, arrn3, arrn4) != 0) {
            Nat.subFrom(n, arrn, arrn4);
        }
    }

    private static int getTrailingZeroes(int n) {
        int n2 = 0;
        while ((n & 1) == 0) {
            n >>>= 1;
            ++n2;
        }
        return n2;
    }

    public static int inverse32(int n) {
        int n2 = n * (2 - n * n);
        int n3 = n2 * (2 - n * n2);
        int n4 = n3 * (2 - n * n3);
        return n4 * (2 - n * n4);
    }

    private static void inversionResult(int[] arrn, int n, int[] arrn2, int[] arrn3) {
        if (n < 0) {
            Nat.add(arrn.length, arrn2, arrn, arrn3);
            return;
        }
        System.arraycopy((Object)arrn2, (int)0, (Object)arrn3, (int)0, (int)arrn.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int inversionStep(int[] arrn, int[] arrn2, int n, int[] arrn3, int n2) {
        int n3 = arrn.length;
        int n4 = 0;
        while (arrn2[0] == 0) {
            Nat.shiftDownWord(n, arrn2, 0);
            n4 += 32;
        }
        int n5 = Mod.getTrailingZeroes(arrn2[0]);
        if (n5 > 0) {
            Nat.shiftDownBits(n, arrn2, n5, 0);
            n4 += n5;
        }
        int n6 = 0;
        int n7 = n2;
        while (n6 < n4) {
            if ((1 & arrn3[0]) != 0) {
                n7 = n7 < 0 ? (n7 += Nat.addTo(n3, arrn, arrn3)) : (n7 += Nat.subFrom(n3, arrn, arrn3));
            }
            Nat.shiftDownBit(n3, arrn3, n7);
            ++n6;
        }
        return n7;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void invert(int[] arrn, int[] arrn2, int[] arrn3) {
        int n = 0;
        int n2 = arrn.length;
        if (Nat.isZero(n2, arrn2)) {
            throw new IllegalArgumentException("'x' cannot be 0");
        }
        if (Nat.isOne(n2, arrn2)) {
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn3, (int)0, (int)n2);
            return;
        }
        int[] arrn4 = Nat.copy(n2, arrn2);
        int[] arrn5 = Nat.create(n2);
        arrn5[0] = 1;
        int n3 = (1 & arrn4[0]) == 0 ? Mod.inversionStep(arrn, arrn4, n2, arrn5, 0) : 0;
        if (Nat.isOne(n2, arrn4)) {
            Mod.inversionResult(arrn, n3, arrn5, arrn3);
            return;
        }
        int[] arrn6 = Nat.copy(n2, arrn);
        int[] arrn7 = Nat.create(n2);
        int n4 = n3;
        int n5 = n2;
        do {
            if (arrn4[n5 - 1] == 0 && arrn6[n5 - 1] == 0) {
                --n5;
                continue;
            }
            if (Nat.gte(n5, arrn4, arrn6)) {
                Nat.subFrom(n5, arrn6, arrn4);
                n4 = Mod.inversionStep(arrn, arrn4, n5, arrn5, n4 + (Nat.subFrom(n2, arrn7, arrn5) - n));
                if (!Nat.isOne(n5, arrn4)) continue;
                Mod.inversionResult(arrn, n4, arrn5, arrn3);
                return;
            }
            Nat.subFrom(n5, arrn4, arrn6);
            n = Mod.inversionStep(arrn, arrn6, n5, arrn7, n + (Nat.subFrom(n2, arrn5, arrn7) - n4));
            if (Nat.isOne(n5, arrn6)) break;
        } while (true);
        Mod.inversionResult(arrn, n, arrn7, arrn3);
    }

    public static int[] random(int[] arrn) {
        int n = arrn.length;
        Random random = new Random();
        int[] arrn2 = Nat.create(n);
        int n2 = arrn[n - 1];
        int n3 = n2 | n2 >>> 1;
        int n4 = n3 | n3 >>> 2;
        int n5 = n4 | n4 >>> 4;
        int n6 = n5 | n5 >>> 8;
        int n7 = n6 | n6 >>> 16;
        do {
            for (int i = 0; i != n; ++i) {
                arrn2[i] = random.nextInt();
            }
            int n8 = n - 1;
            arrn2[n8] = n7 & arrn2[n8];
        } while (Nat.gte(n, arrn2, arrn));
        return arrn2;
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4) {
        int n = arrn.length;
        if (Nat.sub(n, arrn2, arrn3, arrn4) != 0) {
            Nat.addTo(n, arrn, arrn4);
        }
    }
}

