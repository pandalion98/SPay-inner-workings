/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.raw;

import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat192;

public abstract class Nat384 {
    /*
     * Enabled aggressive block sorting
     */
    public static void mul(int[] arrn, int[] arrn2, int[] arrn3) {
        Nat192.mul(arrn, arrn2, arrn3);
        Nat192.mul(arrn, 6, arrn2, 6, arrn3, 12);
        int n = Nat192.addToEachOther(arrn3, 6, arrn3, 12);
        int n2 = n + Nat192.addTo(arrn3, 18, arrn3, 12, n + Nat192.addTo(arrn3, 0, arrn3, 6, 0));
        int[] arrn4 = Nat192.create();
        int[] arrn5 = Nat192.create();
        boolean bl = Nat192.diff(arrn, 6, arrn, 0, arrn4, 0) != Nat192.diff(arrn2, 6, arrn2, 0, arrn5, 0);
        int[] arrn6 = Nat192.createExt();
        Nat192.mul(arrn4, arrn5, arrn6);
        int n3 = bl ? Nat.addTo(12, arrn6, 0, arrn3, 6) : Nat.subFrom(12, arrn6, 0, arrn3, 6);
        Nat.addWordAt(24, n3 + n2, arrn3, 18);
    }

    public static void square(int[] arrn, int[] arrn2) {
        Nat192.square(arrn, arrn2);
        Nat192.square(arrn, 6, arrn2, 12);
        int n = Nat192.addToEachOther(arrn2, 6, arrn2, 12);
        int n2 = n + Nat192.addTo(arrn2, 18, arrn2, 12, n + Nat192.addTo(arrn2, 0, arrn2, 6, 0));
        int[] arrn3 = Nat192.create();
        Nat192.diff(arrn, 6, arrn, 0, arrn3, 0);
        int[] arrn4 = Nat192.createExt();
        Nat192.square(arrn3, arrn4);
        Nat.addWordAt(24, n2 + Nat.subFrom(12, arrn4, 0, arrn2, 6), arrn2, 18);
    }
}

