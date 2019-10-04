/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  java.util.Vector
 */
package org.bouncycastle.crypto.generators;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.NaccacheSternKeyGenerationParameters;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;

public class NaccacheSternKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE;
    private static int[] smallPrimes;
    private NaccacheSternKeyGenerationParameters param;

    static {
        smallPrimes = new int[]{3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557};
        ONE = BigInteger.valueOf((long)1L);
    }

    private static Vector findFirstPrimes(int n2) {
        Vector vector = new Vector(n2);
        for (int i2 = 0; i2 != n2; ++i2) {
            vector.addElement((Object)BigInteger.valueOf((long)smallPrimes[i2]));
        }
        return vector;
    }

    private static BigInteger generatePrime(int n2, int n3, SecureRandom secureRandom) {
        BigInteger bigInteger = new BigInteger(n2, n3, (Random)secureRandom);
        while (bigInteger.bitLength() != n2) {
            bigInteger = new BigInteger(n2, n3, (Random)secureRandom);
        }
        return bigInteger;
    }

    private static int getInt(SecureRandom secureRandom, int n2) {
        int n3;
        int n4;
        if ((n2 & -n2) == n2) {
            return (int)((long)n2 * (long)(Integer.MAX_VALUE & secureRandom.nextInt()) >> 31);
        }
        while ((n3 = Integer.MAX_VALUE & secureRandom.nextInt()) - (n4 = n3 % n2) + (n2 - 1) < 0) {
        }
        return n4;
    }

    private static Vector permuteList(Vector vector, SecureRandom secureRandom) {
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            vector3.addElement(vector.elementAt(i2));
        }
        vector2.addElement(vector3.elementAt(0));
        vector3.removeElementAt(0);
        while (vector3.size() != 0) {
            vector2.insertElementAt(vector3.elementAt(0), NaccacheSternKeyPairGenerator.getInt(secureRandom, 1 + vector2.size()));
            vector3.removeElementAt(0);
        }
        return vector2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        int n2 = this.param.getStrength();
        SecureRandom secureRandom = this.param.getRandom();
        int n3 = this.param.getCertainty();
        boolean bl = this.param.isDebug();
        if (bl) {
            System.out.println("Fetching first " + this.param.getCntSmallPrimes() + " primes.");
        }
        Vector vector = NaccacheSternKeyPairGenerator.permuteList(NaccacheSternKeyPairGenerator.findFirstPrimes(this.param.getCntSmallPrimes()), secureRandom);
        BigInteger bigInteger5 = ONE;
        BigInteger bigInteger6 = ONE;
        BigInteger bigInteger7 = bigInteger5;
        for (int n4 = 0; n4 < vector.size() / 2; bigInteger7 = bigInteger7.multiply((BigInteger)((BigInteger)vector.elementAt((int)n4))), ++n4) {
        }
        for (int n5 = vector.size() / 2; n5 < vector.size(); bigInteger6 = bigInteger6.multiply((BigInteger)((BigInteger)vector.elementAt((int)n5))), ++n5) {
        }
        BigInteger bigInteger8 = bigInteger7.multiply(bigInteger6);
        int n6 = -48 + (n2 - bigInteger8.bitLength());
        BigInteger bigInteger9 = NaccacheSternKeyPairGenerator.generatePrime(1 + n6 / 2, n3, secureRandom);
        BigInteger bigInteger10 = NaccacheSternKeyPairGenerator.generatePrime(1 + n6 / 2, n3, secureRandom);
        long l2 = 0L;
        if (bl) {
            System.out.println("generating p and q");
        }
        BigInteger bigInteger11 = bigInteger9.multiply(bigInteger7).shiftLeft(1);
        BigInteger bigInteger12 = bigInteger10.multiply(bigInteger6).shiftLeft(1);
        do {
            ++l2;
            bigInteger3 = NaccacheSternKeyPairGenerator.generatePrime(24, n3, secureRandom);
            bigInteger = bigInteger3.multiply(bigInteger11).add(ONE);
            if (!bigInteger.isProbablePrime(n3)) continue;
            while (bigInteger3.equals((Object)(bigInteger4 = NaccacheSternKeyPairGenerator.generatePrime(24, n3, secureRandom))) || !(bigInteger2 = bigInteger4.multiply(bigInteger12).add(ONE)).isProbablePrime(n3)) {
            }
            if (!bigInteger8.gcd(bigInteger3.multiply(bigInteger4)).equals((Object)ONE)) continue;
            if (bigInteger.multiply(bigInteger2).bitLength() >= n2) break;
            if (!bl) continue;
            System.out.println("key size too small. Should be " + n2 + " but is actually " + bigInteger.multiply(bigInteger2).bitLength());
        } while (true);
        if (bl) {
            System.out.println("needed " + l2 + " tries to generate p and q.");
        }
        BigInteger bigInteger13 = bigInteger.multiply(bigInteger2);
        BigInteger bigInteger14 = bigInteger.subtract(ONE).multiply(bigInteger2.subtract(ONE));
        long l3 = 0L;
        if (bl) {
            System.out.println("generating g");
        }
        do {
            boolean bl2;
            long l4;
            BigInteger bigInteger15;
            block31 : {
                Vector vector2 = new Vector();
                l4 = l3;
                for (int i2 = 0; i2 != vector.size(); ++l4, ++i2) {
                    BigInteger bigInteger16;
                    BigInteger bigInteger17 = bigInteger14.divide((BigInteger)vector.elementAt(i2));
                    do {
                    } while ((bigInteger16 = new BigInteger(n2, n3, (Random)secureRandom)).modPow(bigInteger17, bigInteger13).equals((Object)ONE));
                    vector2.addElement((Object)bigInteger16);
                }
                bigInteger15 = ONE;
                for (int i3 = 0; i3 < vector.size(); ++i3) {
                    bigInteger15 = bigInteger15.multiply(((BigInteger)vector2.elementAt(i3)).modPow(bigInteger8.divide((BigInteger)vector.elementAt(i3)), bigInteger13)).mod(bigInteger13);
                }
                for (int i4 = 0; i4 < vector.size(); ++i4) {
                    if (!bigInteger15.modPow(bigInteger14.divide((BigInteger)vector.elementAt(i4)), bigInteger13).equals((Object)ONE)) continue;
                    if (bl) {
                        System.out.println("g has order phi(n)/" + vector.elementAt(i4) + "\n g: " + (Object)bigInteger15);
                    }
                    bl2 = true;
                    break block31;
                }
                bl2 = false;
            }
            if (bl2) {
                l3 = l4;
                continue;
            }
            if (bigInteger15.modPow(bigInteger14.divide(BigInteger.valueOf((long)4L)), bigInteger13).equals((Object)ONE)) {
                if (bl) {
                    System.out.println("g has order phi(n)/4\n g:" + (Object)bigInteger15);
                    l3 = l4;
                    continue;
                }
            } else if (bigInteger15.modPow(bigInteger14.divide(bigInteger3), bigInteger13).equals((Object)ONE)) {
                if (bl) {
                    System.out.println("g has order phi(n)/p'\n g: " + (Object)bigInteger15);
                    l3 = l4;
                    continue;
                }
            } else if (bigInteger15.modPow(bigInteger14.divide(bigInteger4), bigInteger13).equals((Object)ONE)) {
                if (bl) {
                    System.out.println("g has order phi(n)/q'\n g: " + (Object)bigInteger15);
                    l3 = l4;
                    continue;
                }
            } else if (bigInteger15.modPow(bigInteger14.divide(bigInteger9), bigInteger13).equals((Object)ONE)) {
                if (bl) {
                    System.out.println("g has order phi(n)/a\n g: " + (Object)bigInteger15);
                    l3 = l4;
                    continue;
                }
            } else if (bigInteger15.modPow(bigInteger14.divide(bigInteger10), bigInteger13).equals((Object)ONE)) {
                if (bl) {
                    System.out.println("g has order phi(n)/b\n g: " + (Object)bigInteger15);
                    l3 = l4;
                    continue;
                }
            } else {
                if (bl) {
                    System.out.println("needed " + l4 + " tries to generate g");
                    System.out.println();
                    System.out.println("found new NaccacheStern cipher variables:");
                    System.out.println("smallPrimes: " + (Object)vector);
                    System.out.println("sigma:...... " + (Object)bigInteger8 + " (" + bigInteger8.bitLength() + " bits)");
                    System.out.println("a:.......... " + (Object)bigInteger9);
                    System.out.println("b:.......... " + (Object)bigInteger10);
                    System.out.println("p':......... " + (Object)bigInteger3);
                    System.out.println("q':......... " + (Object)bigInteger4);
                    System.out.println("p:.......... " + (Object)bigInteger);
                    System.out.println("q:.......... " + (Object)bigInteger2);
                    System.out.println("n:.......... " + (Object)bigInteger13);
                    System.out.println("phi(n):..... " + (Object)bigInteger14);
                    System.out.println("g:.......... " + (Object)bigInteger15);
                    System.out.println();
                }
                return new AsymmetricCipherKeyPair(new NaccacheSternKeyParameters(false, bigInteger15, bigInteger13, bigInteger8.bitLength()), new NaccacheSternPrivateKeyParameters(bigInteger15, bigInteger13, bigInteger8.bitLength(), vector, bigInteger14));
            }
            l3 = l4;
        } while (true);
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (NaccacheSternKeyGenerationParameters)keyGenerationParameters;
    }
}

