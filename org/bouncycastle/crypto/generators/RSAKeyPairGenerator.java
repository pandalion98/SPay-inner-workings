package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.math.ec.WNafUtil;

public class RSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE;
    private RSAKeyGenerationParameters param;

    static {
        ONE = BigInteger.valueOf(1);
    }

    protected BigInteger chooseRandomPrime(int i, BigInteger bigInteger) {
        while (true) {
            BigInteger bigInteger2 = new BigInteger(i, 1, this.param.getRandom());
            if (!bigInteger2.mod(bigInteger).equals(ONE) && bigInteger2.isProbablePrime(this.param.getCertainty()) && bigInteger.gcd(bigInteger2.subtract(ONE)).equals(ONE)) {
                return bigInteger2;
            }
        }
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        boolean z = false;
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = null;
        while (!z) {
            BigInteger chooseRandomPrime;
            BigInteger multiply;
            BigInteger bigInteger;
            int strength = this.param.getStrength();
            int i = (strength + 1) / 2;
            int i2 = strength - i;
            int i3 = strength / 3;
            int i4 = strength >> 2;
            BigInteger publicExponent = this.param.getPublicExponent();
            BigInteger chooseRandomPrime2 = chooseRandomPrime(i, publicExponent);
            while (true) {
                chooseRandomPrime = chooseRandomPrime(i2, publicExponent);
                if (chooseRandomPrime.subtract(chooseRandomPrime2).abs().bitLength() >= i3) {
                    multiply = chooseRandomPrime2.multiply(chooseRandomPrime);
                    if (multiply.bitLength() == strength) {
                        if (WNafUtil.getNafWeight(multiply) >= i4) {
                            break;
                        }
                        chooseRandomPrime2 = chooseRandomPrime(i, publicExponent);
                    } else {
                        chooseRandomPrime2 = chooseRandomPrime2.max(chooseRandomPrime);
                    }
                }
            }
            if (chooseRandomPrime2.compareTo(chooseRandomPrime) < 0) {
                bigInteger = chooseRandomPrime;
                chooseRandomPrime = chooseRandomPrime2;
            } else {
                bigInteger = chooseRandomPrime2;
            }
            chooseRandomPrime2 = bigInteger.subtract(ONE);
            BigInteger subtract = chooseRandomPrime.subtract(ONE);
            BigInteger multiply2 = chooseRandomPrime2.multiply(subtract);
            BigInteger modInverse = publicExponent.modInverse(multiply2.divide(chooseRandomPrime2.gcd(subtract)));
            if (modInverse.bitLength() > i2 && modInverse.equals(publicExponent.modInverse(multiply2))) {
                z = true;
                asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(new RSAKeyParameters(false, multiply, publicExponent), new RSAPrivateCrtKeyParameters(multiply, publicExponent, modInverse, bigInteger, chooseRandomPrime, modInverse.remainder(chooseRandomPrime2), modInverse.remainder(subtract), chooseRandomPrime.modInverse(bigInteger)));
            }
        }
        return asymmetricCipherKeyPair;
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (RSAKeyGenerationParameters) keyGenerationParameters;
    }
}
