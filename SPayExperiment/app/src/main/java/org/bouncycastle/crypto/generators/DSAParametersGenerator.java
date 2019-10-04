/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.BigIntegers
 *  org.bouncycastle.util.encoders.Hex
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Hex;

public class DSAParametersGenerator {
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int L;
    private int N;
    private int certainty;
    private Digest digest;
    private SecureRandom random;
    private int usageIndex;
    private boolean use186_3;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
        TWO = BigInteger.valueOf((long)2L);
    }

    public DSAParametersGenerator() {
        this(new SHA1Digest());
    }

    public DSAParametersGenerator(Digest digest) {
        this.digest = digest;
    }

    private static BigInteger calculateGenerator_FIPS186_2(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigInteger3;
        BigInteger bigInteger4 = bigInteger.subtract(ONE).divide(bigInteger2);
        BigInteger bigInteger5 = bigInteger.subtract(TWO);
        while ((bigInteger3 = BigIntegers.createRandomInRange((BigInteger)TWO, (BigInteger)bigInteger5, (SecureRandom)secureRandom).modPow(bigInteger4, bigInteger)).bitLength() <= 1) {
        }
        return bigInteger3;
    }

    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        return DSAParametersGenerator.calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }

    private static BigInteger calculateGenerator_FIPS186_3_Verifiable(Digest digest, BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby, int n2) {
        BigInteger bigInteger3 = bigInteger.subtract(ONE).divide(bigInteger2);
        byte[] arrby2 = Hex.decode((String)"6767656E");
        byte[] arrby3 = new byte[2 + (1 + (arrby.length + arrby2.length))];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)arrby.length);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)arrby.length, (int)arrby2.length);
        arrby3[-3 + arrby3.length] = (byte)n2;
        byte[] arrby4 = new byte[digest.getDigestSize()];
        for (int i2 = 1; i2 < 65536; ++i2) {
            DSAParametersGenerator.inc(arrby3);
            DSAParametersGenerator.hash(digest, arrby3, arrby4);
            BigInteger bigInteger4 = new BigInteger(1, arrby4).modPow(bigInteger3, bigInteger);
            if (bigInteger4.compareTo(TWO) < 0) continue;
            return bigInteger4;
        }
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private DSAParameters generateParameters_FIPS186_2() {
        var1_1 = new byte[20];
        var2_2 = new byte[20];
        var3_3 = new byte[20];
        var4_4 = new byte[20];
        var5_5 = (-1 + this.L) / 160;
        var6_6 = new byte[this.L / 8];
        if (this.digest instanceof SHA1Digest) ** GOTO lbl18
        throw new IllegalStateException("can only use SHA-1 for generating FIPS 186-2 parameters");
        {
            var4_4[0] = (byte)(-128 | var4_4[0]);
            var4_4[19] = (byte)(1 | var4_4[19]);
            var8_8 = new BigInteger(1, var4_4);
            if (!var8_8.isProbablePrime(this.certainty)) ** GOTO lbl18
            var9_9 = Arrays.clone((byte[])var1_1);
            DSAParametersGenerator.inc(var9_9);
            var10_10 = 0;
            do {
                block8 : {
                    if (var10_10 < 4096) break block8;
lbl18: // 3 sources:
                    this.random.nextBytes(var1_1);
                    DSAParametersGenerator.hash(this.digest, var1_1, var2_2);
                    System.arraycopy((Object)var1_1, (int)0, (Object)var3_3, (int)0, (int)var1_1.length);
                    DSAParametersGenerator.inc(var3_3);
                    DSAParametersGenerator.hash(this.digest, var3_3, var3_3);
                    var7_7 = 0;
                    do {
                        if (var7_7 == var4_4.length) continue block0;
                        var4_4[var7_7] = (byte)(var2_2[var7_7] ^ var3_3[var7_7]);
                        ++var7_7;
                    } while (true);
                }
                for (var11_11 = 0; var11_11 < var5_5; ++var11_11) {
                    DSAParametersGenerator.inc(var9_9);
                    DSAParametersGenerator.hash(this.digest, var9_9, var2_2);
                    System.arraycopy((Object)var2_2, (int)0, (Object)var6_6, (int)(var6_6.length - (var11_11 + 1) * var2_2.length), (int)var2_2.length);
                }
                DSAParametersGenerator.inc(var9_9);
                DSAParametersGenerator.hash(this.digest, var9_9, var2_2);
                System.arraycopy((Object)var2_2, (int)(var2_2.length - (var6_6.length - var5_5 * var2_2.length)), (Object)var6_6, (int)0, (int)(var6_6.length - var5_5 * var2_2.length));
                var6_6[0] = (byte)(-128 | var6_6[0]);
                var12_12 = new BigInteger(1, var6_6);
                var13_13 = var12_12.subtract(var12_12.mod(var8_8.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                if (var13_13.bitLength() == this.L && var13_13.isProbablePrime(this.certainty)) {
                    return new DSAParameters(var13_13, var8_8, DSAParametersGenerator.calculateGenerator_FIPS186_2(var13_13, var8_8, this.random), new DSAValidationParameters(var1_1, var10_10));
                }
                ++var10_10;
            } while (true);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private DSAParameters generateParameters_FIPS186_3() {
        var1_1 = this.digest;
        var2_2 = 8 * var1_1.getDigestSize();
        var3_3 = new byte[this.N / 8];
        var4_4 = (-1 + this.L) / var2_2;
        var5_5 = (-1 + this.L) % var2_2;
        var6_6 = new byte[var1_1.getDigestSize()];
        block0 : do lbl-1000: // 3 sources:
        {
            this.random.nextBytes(var3_3);
            DSAParametersGenerator.hash(var1_1, var3_3, var6_6);
            var7_7 = new BigInteger(1, var6_6).mod(DSAParametersGenerator.ONE.shiftLeft(-1 + this.N));
            var8_8 = DSAParametersGenerator.ONE.shiftLeft(-1 + this.N).add(var7_7).add(DSAParametersGenerator.ONE).subtract(var7_7.mod(DSAParametersGenerator.TWO));
            if (!var8_8.isProbablePrime(this.certainty)) ** GOTO lbl-1000
            var9_9 = Arrays.clone((byte[])var3_3);
            var10_10 = 4 * this.L;
            var11_11 = 0;
            do {
                if (var11_11 >= var10_10) continue block0;
                var12_12 = DSAParametersGenerator.ZERO;
                var14_14 = 0;
                for (var13_13 = 0; var13_13 <= var4_4; var12_12 = var12_12.add((BigInteger)var18_17.shiftLeft((int)var14_14)), ++var13_13, var14_14 += var2_2) {
                    DSAParametersGenerator.inc(var9_9);
                    DSAParametersGenerator.hash(var1_1, var9_9, var6_6);
                    var18_17 = new BigInteger(1, var6_6);
                    if (var13_13 != var4_4) continue;
                    var18_17 = var18_17.mod(DSAParametersGenerator.ONE.shiftLeft(var5_5));
                }
                var15_15 = var12_12.add(DSAParametersGenerator.ONE.shiftLeft(-1 + this.L));
                var16_16 = var15_15.subtract(var15_15.mod(var8_8.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                if (var16_16.bitLength() == this.L && var16_16.isProbablePrime(this.certainty)) {
                    if (this.usageIndex < 0) return new DSAParameters(var16_16, var8_8, DSAParametersGenerator.calculateGenerator_FIPS186_3_Unverifiable(var16_16, var8_8, this.random), new DSAValidationParameters(var3_3, var11_11));
                    var17_18 = DSAParametersGenerator.calculateGenerator_FIPS186_3_Verifiable(var1_1, var16_16, var8_8, var3_3, this.usageIndex);
                    if (var17_18 == null) return new DSAParameters(var16_16, var8_8, DSAParametersGenerator.calculateGenerator_FIPS186_3_Unverifiable(var16_16, var8_8, this.random), new DSAValidationParameters(var3_3, var11_11));
                    return new DSAParameters(var16_16, var8_8, var17_18, new DSAValidationParameters(var3_3, var11_11, this.usageIndex));
                }
                ++var11_11;
            } while (true);
            break;
        } while (true);
    }

    private static int getDefaultN(int n2) {
        if (n2 > 1024) {
            return 256;
        }
        return 160;
    }

    private static void hash(Digest digest, byte[] arrby, byte[] arrby2) {
        digest.update(arrby, 0, arrby.length);
        digest.doFinal(arrby2, 0);
    }

    private static void inc(byte[] arrby) {
        int n2 = -1 + arrby.length;
        do {
            block4 : {
                block3 : {
                    byte by;
                    if (n2 < 0) break block3;
                    arrby[n2] = by = (byte)(255 & 1 + arrby[n2]);
                    if (by == 0) break block4;
                }
                return;
            }
            --n2;
        } while (true);
    }

    public DSAParameters generateParameters() {
        if (this.use186_3) {
            return this.generateParameters_FIPS186_3();
        }
        return this.generateParameters_FIPS186_2();
    }

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.use186_3 = false;
        this.L = n2;
        this.N = DSAParametersGenerator.getDefaultN(n2);
        this.certainty = n3;
        this.random = secureRandom;
    }

    public void init(DSAParameterGenerationParameters dSAParameterGenerationParameters) {
        this.use186_3 = true;
        this.L = dSAParameterGenerationParameters.getL();
        this.N = dSAParameterGenerationParameters.getN();
        this.certainty = dSAParameterGenerationParameters.getCertainty();
        this.random = dSAParameterGenerationParameters.getRandom();
        this.usageIndex = dSAParameterGenerationParameters.getUsageIndex();
        if (this.L < 1024 || this.L > 3072 || this.L % 1024 != 0) {
            throw new IllegalArgumentException("L values must be between 1024 and 3072 and a multiple of 1024");
        }
        if (this.L == 1024 && this.N != 160) {
            throw new IllegalArgumentException("N must be 160 for L = 1024");
        }
        if (this.L == 2048 && this.N != 224 && this.N != 256) {
            throw new IllegalArgumentException("N must be 224 or 256 for L = 2048");
        }
        if (this.L == 3072 && this.N != 256) {
            throw new IllegalArgumentException("N must be 256 for L = 3072");
        }
        if (8 * this.digest.getDigestSize() < this.N) {
            throw new IllegalStateException("Digest output size too small for value of N");
        }
    }
}

