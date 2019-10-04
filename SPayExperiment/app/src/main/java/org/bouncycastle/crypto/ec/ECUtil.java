/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.math.ec.ECConstants;

class ECUtil {
    ECUtil() {
    }

    static BigInteger generateK(BigInteger bigInteger, SecureRandom secureRandom) {
        BigInteger bigInteger2;
        int n2 = bigInteger.bitLength();
        while ((bigInteger2 = new BigInteger(n2, (Random)secureRandom)).equals((Object)ECConstants.ZERO) || bigInteger2.compareTo(bigInteger) >= 0) {
        }
        return bigInteger2;
    }
}

