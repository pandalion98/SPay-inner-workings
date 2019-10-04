/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.math.ec.WNafUtil
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.math.ec.WNafUtil;

public class RSAKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private RSAKeyGenerationParameters param;

    protected BigInteger chooseRandomPrime(int n2, BigInteger bigInteger) {
        BigInteger bigInteger2;
        while ((bigInteger2 = new BigInteger(n2, 1, (Random)this.param.getRandom())).mod(bigInteger).equals((Object)ONE) || !bigInteger2.isProbablePrime(this.param.getCertainty()) || !bigInteger.gcd(bigInteger2.subtract(ONE)).equals((Object)ONE)) {
        }
        return bigInteger2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        boolean bl = false;
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = null;
        while (!bl) {
            BigInteger bigInteger;
            BigInteger bigInteger2;
            BigInteger bigInteger3;
            BigInteger bigInteger4;
            BigInteger bigInteger5;
            BigInteger bigInteger6;
            BigInteger bigInteger7;
            int n2 = this.param.getStrength();
            int n3 = (n2 + 1) / 2;
            int n4 = n2 - n3;
            int n5 = n2 / 3;
            int n6 = n2 >> 2;
            BigInteger bigInteger8 = this.param.getPublicExponent();
            BigInteger bigInteger9 = this.chooseRandomPrime(n3, bigInteger8);
            do {
                if ((bigInteger4 = this.chooseRandomPrime(n4, bigInteger8)).subtract(bigInteger9).abs().bitLength() < n5) {
                    continue;
                }
                bigInteger7 = bigInteger9.multiply(bigInteger4);
                if (bigInteger7.bitLength() != n2) {
                    bigInteger9 = bigInteger9.max(bigInteger4);
                    continue;
                }
                if (WNafUtil.getNafWeight((BigInteger)bigInteger7) >= n6) break;
                bigInteger9 = this.chooseRandomPrime(n3, bigInteger8);
            } while (true);
            if (bigInteger9.compareTo(bigInteger4) < 0) {
                bigInteger = bigInteger4;
                bigInteger4 = bigInteger9;
            } else {
                bigInteger = bigInteger9;
            }
            if ((bigInteger6 = bigInteger8.modInverse((bigInteger5 = (bigInteger2 = bigInteger.subtract(ONE)).multiply(bigInteger3 = bigInteger4.subtract(ONE))).divide(bigInteger2.gcd(bigInteger3)))).bitLength() <= n4 || !bigInteger6.equals((Object)bigInteger8.modInverse(bigInteger5))) continue;
            BigInteger bigInteger10 = bigInteger6.remainder(bigInteger2);
            BigInteger bigInteger11 = bigInteger6.remainder(bigInteger3);
            BigInteger bigInteger12 = bigInteger4.modInverse(bigInteger);
            AsymmetricCipherKeyPair asymmetricCipherKeyPair2 = new AsymmetricCipherKeyPair(new RSAKeyParameters(false, bigInteger7, bigInteger8), new RSAPrivateCrtKeyParameters(bigInteger7, bigInteger8, bigInteger6, bigInteger, bigInteger4, bigInteger10, bigInteger11, bigInteger12));
            bl = true;
            asymmetricCipherKeyPair = asymmetricCipherKeyPair2;
        }
        return asymmetricCipherKeyPair;
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (RSAKeyGenerationParameters)keyGenerationParameters;
    }
}

