/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Hashtable
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.prng.drbg;

import java.util.Hashtable;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.util.Integers;

class Utils {
    static final Hashtable maxSecurityStrengths = new Hashtable();

    static {
        maxSecurityStrengths.put((Object)"SHA-1", (Object)Integers.valueOf((int)128));
        maxSecurityStrengths.put((Object)"SHA-224", (Object)Integers.valueOf((int)192));
        maxSecurityStrengths.put((Object)"SHA-256", (Object)Integers.valueOf((int)256));
        maxSecurityStrengths.put((Object)"SHA-384", (Object)Integers.valueOf((int)256));
        maxSecurityStrengths.put((Object)"SHA-512", (Object)Integers.valueOf((int)256));
        maxSecurityStrengths.put((Object)"SHA-512/224", (Object)Integers.valueOf((int)192));
        maxSecurityStrengths.put((Object)"SHA-512/256", (Object)Integers.valueOf((int)256));
    }

    Utils() {
    }

    static int getMaxSecurityStrength(Digest digest) {
        return (Integer)maxSecurityStrengths.get((Object)digest.getAlgorithmName());
    }

    static int getMaxSecurityStrength(Mac mac) {
        String string = mac.getAlgorithmName();
        return (Integer)maxSecurityStrengths.get((Object)string.substring(0, string.indexOf("/")));
    }

    /*
     * Enabled aggressive block sorting
     */
    static byte[] hash_df(Digest digest, byte[] arrby, int n2) {
        int n3 = 0;
        byte[] arrby2 = new byte[(n2 + 7) / 8];
        int n4 = arrby2.length / digest.getDigestSize();
        byte[] arrby3 = new byte[digest.getDigestSize()];
        int n5 = 1;
        for (int i2 = 0; i2 <= n4; ++n5, ++i2) {
            digest.update((byte)n5);
            digest.update((byte)(n2 >> 24));
            digest.update((byte)(n2 >> 16));
            digest.update((byte)(n2 >> 8));
            digest.update((byte)n2);
            digest.update(arrby, 0, arrby.length);
            digest.doFinal(arrby3, 0);
            int n6 = arrby2.length - i2 * arrby3.length > arrby3.length ? arrby3.length : arrby2.length - i2 * arrby3.length;
            System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)(i2 * arrby3.length), (int)n6);
        }
        if (n2 % 8 != 0) {
            int n7 = 8 - n2 % 8;
            for (int i3 = 0; i3 != arrby2.length; ++i3) {
                int n8 = 255 & arrby2[i3];
                arrby2[i3] = (byte)(n8 >>> n7 | n3 << 8 - n7);
                n3 = n8;
            }
        }
        return arrby2;
    }

    static boolean isTooLarge(byte[] arrby, int n2) {
        return arrby != null && arrby.length > n2;
    }
}

