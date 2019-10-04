/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;

public class RandUtils {
    static int nextInt(SecureRandom secureRandom, int n) {
        int n2;
        int n3;
        if ((n & -n) == n) {
            return (int)((long)n * (long)(secureRandom.nextInt() >>> 1) >> 31);
        }
        while ((n2 = secureRandom.nextInt() >>> 1) - (n3 = n2 % n) + (n - 1) < 0) {
        }
        return n3;
    }
}

