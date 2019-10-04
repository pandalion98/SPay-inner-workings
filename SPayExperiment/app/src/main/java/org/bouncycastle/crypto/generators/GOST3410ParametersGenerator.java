/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410ValidationParameters;

public class GOST3410ParametersGenerator {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private static final BigInteger TWO = BigInteger.valueOf((long)2L);
    private SecureRandom init_random;
    private int size;
    private int typeproc;

    private int procedure_A(int n2, int n3, BigInteger[] arrbigInteger, int n4) {
        while (n2 < 0 || n2 > 65536) {
            n2 = this.init_random.nextInt() / 32768;
        }
        while (n3 < 0 || n3 > 65536 || n3 / 2 == 0) {
            n3 = 1 + this.init_random.nextInt() / 32768;
        }
        BigInteger bigInteger = new BigInteger(Integer.toString((int)n3));
        BigInteger bigInteger2 = new BigInteger("19381");
        BigInteger[] arrbigInteger2 = new BigInteger[]{new BigInteger(Integer.toString((int)n2))};
        int[] arrn = new int[]{n4};
        int n5 = 0;
        int n6 = 0;
        while (arrn[n6] >= 17) {
            int[] arrn2 = new int[1 + arrn.length];
            System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn.length);
            arrn = new int[arrn2.length];
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn, (int)0, (int)arrn2.length);
            arrn[n6 + 1] = arrn[n6] / 2;
            n5 = n6 + 1;
            ++n6;
        }
        BigInteger[] arrbigInteger3 = new BigInteger[n5 + 1];
        arrbigInteger3[n5] = new BigInteger("8003", 16);
        int n7 = n5 - 1;
        int n8 = n7;
        BigInteger[] arrbigInteger4 = arrbigInteger2;
        block3 : for (int i2 = 0; i2 < n5; ++i2) {
            int n9 = arrn[n8] / 16;
            block4 : do {
                BigInteger[] arrbigInteger5 = new BigInteger[arrbigInteger4.length];
                System.arraycopy((Object)arrbigInteger4, (int)0, (Object)arrbigInteger5, (int)0, (int)arrbigInteger4.length);
                arrbigInteger4 = new BigInteger[n9 + 1];
                System.arraycopy((Object)arrbigInteger5, (int)0, (Object)arrbigInteger4, (int)0, (int)arrbigInteger5.length);
                for (int i3 = 0; i3 < n9; ++i3) {
                    arrbigInteger4[i3 + 1] = arrbigInteger4[i3].multiply(bigInteger2).add(bigInteger).mod(TWO.pow(16));
                }
                BigInteger bigInteger3 = new BigInteger("0");
                for (int i4 = 0; i4 < n9; ++i4) {
                    bigInteger3 = bigInteger3.add(arrbigInteger4[i4].multiply(TWO.pow(i4 * 16)));
                }
                arrbigInteger4[0] = arrbigInteger4[n9];
                BigInteger bigInteger4 = TWO.pow(-1 + arrn[n8]).divide(arrbigInteger3[n8 + 1]).add(TWO.pow(-1 + arrn[n8]).multiply(bigInteger3).divide(arrbigInteger3[n8 + 1].multiply(TWO.pow(n9 * 16))));
                if (bigInteger4.mod(TWO).compareTo(ONE) == 0) {
                    bigInteger4 = bigInteger4.add(ONE);
                }
                int n10 = 0;
                do {
                    arrbigInteger3[n8] = arrbigInteger3[n8 + 1].multiply(bigInteger4.add(BigInteger.valueOf((long)n10))).add(ONE);
                    if (arrbigInteger3[n8].compareTo(TWO.pow(arrn[n8])) == 1) continue block4;
                    if (TWO.modPow(arrbigInteger3[n8 + 1].multiply(bigInteger4.add(BigInteger.valueOf((long)n10))), arrbigInteger3[n8]).compareTo(ONE) == 0 && TWO.modPow(bigInteger4.add(BigInteger.valueOf((long)n10)), arrbigInteger3[n8]).compareTo(ONE) != 0) {
                        int n11 = n8 - 1;
                        if (n11 < 0) break block4;
                        n8 = n11;
                        continue block3;
                    }
                    n10 += 2;
                } while (true);
                break;
            } while (true);
            arrbigInteger[0] = arrbigInteger3[0];
            arrbigInteger[1] = arrbigInteger3[1];
            return arrbigInteger4[0].intValue();
        }
        return arrbigInteger4[0].intValue();
    }

    private long procedure_Aa(long l2, long l3, BigInteger[] arrbigInteger, int n2) {
        while (l2 < 0L || l2 > 0x100000000L) {
            l2 = 2 * this.init_random.nextInt();
        }
        while (l3 < 0L || l3 > 0x100000000L || l3 / 2L == 0L) {
            l3 = 1 + 2 * this.init_random.nextInt();
        }
        BigInteger bigInteger = new BigInteger(Long.toString((long)l3));
        BigInteger bigInteger2 = new BigInteger("97781173");
        BigInteger[] arrbigInteger2 = new BigInteger[]{new BigInteger(Long.toString((long)l2))};
        int[] arrn = new int[]{n2};
        int n3 = 0;
        int n4 = 0;
        while (arrn[n4] >= 33) {
            int[] arrn2 = new int[1 + arrn.length];
            System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn.length);
            arrn = new int[arrn2.length];
            System.arraycopy((Object)arrn2, (int)0, (Object)arrn, (int)0, (int)arrn2.length);
            arrn[n4 + 1] = arrn[n4] / 2;
            n3 = n4 + 1;
            ++n4;
        }
        BigInteger[] arrbigInteger3 = new BigInteger[n3 + 1];
        arrbigInteger3[n3] = new BigInteger("8000000B", 16);
        int n5 = n3 - 1;
        int n6 = n5;
        BigInteger[] arrbigInteger4 = arrbigInteger2;
        block3 : for (int i2 = 0; i2 < n3; ++i2) {
            int n7 = arrn[n6] / 32;
            block4 : do {
                BigInteger[] arrbigInteger5 = new BigInteger[arrbigInteger4.length];
                System.arraycopy((Object)arrbigInteger4, (int)0, (Object)arrbigInteger5, (int)0, (int)arrbigInteger4.length);
                arrbigInteger4 = new BigInteger[n7 + 1];
                System.arraycopy((Object)arrbigInteger5, (int)0, (Object)arrbigInteger4, (int)0, (int)arrbigInteger5.length);
                for (int i3 = 0; i3 < n7; ++i3) {
                    arrbigInteger4[i3 + 1] = arrbigInteger4[i3].multiply(bigInteger2).add(bigInteger).mod(TWO.pow(32));
                }
                BigInteger bigInteger3 = new BigInteger("0");
                for (int i4 = 0; i4 < n7; ++i4) {
                    bigInteger3 = bigInteger3.add(arrbigInteger4[i4].multiply(TWO.pow(i4 * 32)));
                }
                arrbigInteger4[0] = arrbigInteger4[n7];
                BigInteger bigInteger4 = TWO.pow(-1 + arrn[n6]).divide(arrbigInteger3[n6 + 1]).add(TWO.pow(-1 + arrn[n6]).multiply(bigInteger3).divide(arrbigInteger3[n6 + 1].multiply(TWO.pow(n7 * 32))));
                if (bigInteger4.mod(TWO).compareTo(ONE) == 0) {
                    bigInteger4 = bigInteger4.add(ONE);
                }
                int n8 = 0;
                do {
                    arrbigInteger3[n6] = arrbigInteger3[n6 + 1].multiply(bigInteger4.add(BigInteger.valueOf((long)n8))).add(ONE);
                    if (arrbigInteger3[n6].compareTo(TWO.pow(arrn[n6])) == 1) continue block4;
                    if (TWO.modPow(arrbigInteger3[n6 + 1].multiply(bigInteger4.add(BigInteger.valueOf((long)n8))), arrbigInteger3[n6]).compareTo(ONE) == 0 && TWO.modPow(bigInteger4.add(BigInteger.valueOf((long)n8)), arrbigInteger3[n6]).compareTo(ONE) != 0) {
                        int n9 = n6 - 1;
                        if (n9 < 0) break block4;
                        n6 = n9;
                        continue block3;
                    }
                    n8 += 2;
                } while (true);
                break;
            } while (true);
            arrbigInteger[0] = arrbigInteger3[0];
            arrbigInteger[1] = arrbigInteger3[1];
            return arrbigInteger4[0].longValue();
        }
        return arrbigInteger4[0].longValue();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void procedure_B(int var1_1, int var2_2, BigInteger[] var3_3) {
        while (var1_1 < 0 || var1_1 > 65536) {
            var1_1 = this.init_random.nextInt() / 32768;
        }
        while (var2_2 < 0 || var2_2 > 65536 || var2_2 / 2 == 0) {
            var2_2 = 1 + this.init_random.nextInt() / 32768;
        }
        var4_4 = new BigInteger[2];
        var5_5 = new BigInteger(Integer.toString((int)var2_2));
        var6_6 = new BigInteger("19381");
        var7_7 = this.procedure_A(var1_1, var2_2, var4_4, 256);
        var8_8 = var4_4[0];
        var9_9 = this.procedure_A(var7_7, var2_2, var4_4, 512);
        var10_10 = var4_4[0];
        var11_11 = new BigInteger[65];
        var11_11[0] = new BigInteger(Integer.toString((int)var9_9));
        do {
            for (var12_12 = 0; var12_12 < 64; ++var12_12) {
                var11_11[var12_12 + 1] = var11_11[var12_12].multiply(var6_6).add(var5_5).mod(GOST3410ParametersGenerator.TWO.pow(16));
            }
            var13_13 = new BigInteger("0");
            for (var14_14 = 0; var14_14 < 64; var13_13 = var13_13.add((BigInteger)var11_11[var14_14].multiply((BigInteger)GOST3410ParametersGenerator.TWO.pow((int)(var14_14 * 16)))), ++var14_14) {
            }
            var11_11[0] = var11_11[64];
            var15_15 = GOST3410ParametersGenerator.TWO.pow(1023).divide(var8_8.multiply(var10_10)).add(GOST3410ParametersGenerator.TWO.pow(1023).multiply(var13_13).divide(var8_8.multiply(var10_10).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (var15_15.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                var15_15 = var15_15.add(GOST3410ParametersGenerator.ONE);
            }
            var16_16 = 0;
            do {
                if ((var17_17 = var8_8.multiply(var10_10).multiply(var15_15.add(BigInteger.valueOf((long)var16_16))).add(GOST3410ParametersGenerator.ONE)).compareTo(GOST3410ParametersGenerator.TWO.pow(1024)) == 1) ** continue;
                if (GOST3410ParametersGenerator.TWO.modPow(var8_8.multiply(var10_10).multiply(var15_15.add(BigInteger.valueOf((long)var16_16))), var17_17).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(var8_8.multiply(var15_15.add(BigInteger.valueOf((long)var16_16))), var17_17).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    var3_3[0] = var17_17;
                    var3_3[1] = var8_8;
                    return;
                }
                var16_16 += 2;
            } while (true);
            break;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void procedure_Bb(long var1_1, long var3_2, BigInteger[] var5_3) {
        var6_4 = var1_1;
        while (var6_4 < 0L || var6_4 > 0x100000000L) {
            var6_4 = 2 * this.init_random.nextInt();
        }
        var8_5 = var3_2;
        while (var8_5 < 0L || var8_5 > 0x100000000L || var8_5 / 2L == 0L) {
            var8_5 = 1 + 2 * this.init_random.nextInt();
        }
        var10_6 = new BigInteger[2];
        var11_7 = new BigInteger(Long.toString((long)var8_5));
        var12_8 = new BigInteger("97781173");
        var13_9 = this.procedure_Aa(var6_4, var8_5, var10_6, 256);
        var15_10 = var10_6[0];
        var16_11 = this.procedure_Aa(var13_9, var8_5, var10_6, 512);
        var18_12 = var10_6[0];
        var19_13 = new BigInteger[33];
        var19_13[0] = new BigInteger(Long.toString((long)var16_11));
        do {
            for (var20_14 = 0; var20_14 < 32; ++var20_14) {
                var19_13[var20_14 + 1] = var19_13[var20_14].multiply(var12_8).add(var11_7).mod(GOST3410ParametersGenerator.TWO.pow(32));
            }
            var21_15 = new BigInteger("0");
            for (var22_16 = 0; var22_16 < 32; var21_15 = var21_15.add((BigInteger)var19_13[var22_16].multiply((BigInteger)GOST3410ParametersGenerator.TWO.pow((int)(var22_16 * 32)))), ++var22_16) {
            }
            var19_13[0] = var19_13[32];
            var23_17 = GOST3410ParametersGenerator.TWO.pow(1023).divide(var15_10.multiply(var18_12)).add(GOST3410ParametersGenerator.TWO.pow(1023).multiply(var21_15).divide(var15_10.multiply(var18_12).multiply(GOST3410ParametersGenerator.TWO.pow(1024))));
            if (var23_17.mod(GOST3410ParametersGenerator.TWO).compareTo(GOST3410ParametersGenerator.ONE) == 0) {
                var23_17 = var23_17.add(GOST3410ParametersGenerator.ONE);
            }
            var24_18 = 0;
            do {
                if ((var25_19 = var15_10.multiply(var18_12).multiply(var23_17.add(BigInteger.valueOf((long)var24_18))).add(GOST3410ParametersGenerator.ONE)).compareTo(GOST3410ParametersGenerator.TWO.pow(1024)) == 1) ** continue;
                if (GOST3410ParametersGenerator.TWO.modPow(var15_10.multiply(var18_12).multiply(var23_17.add(BigInteger.valueOf((long)var24_18))), var25_19).compareTo(GOST3410ParametersGenerator.ONE) == 0 && GOST3410ParametersGenerator.TWO.modPow(var15_10.multiply(var23_17.add(BigInteger.valueOf((long)var24_18))), var25_19).compareTo(GOST3410ParametersGenerator.ONE) != 0) {
                    var5_3[0] = var25_19;
                    var5_3[1] = var15_10;
                    return;
                }
                var24_18 += 2;
            } while (true);
            break;
        } while (true);
    }

    private BigInteger procedure_C(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3;
        BigInteger bigInteger4;
        BigInteger bigInteger5 = bigInteger.subtract(ONE);
        BigInteger bigInteger6 = bigInteger5.divide(bigInteger2);
        int n2 = bigInteger.bitLength();
        while ((bigInteger3 = new BigInteger(n2, (Random)this.init_random)).compareTo(ONE) <= 0 || bigInteger3.compareTo(bigInteger5) >= 0 || (bigInteger4 = bigInteger3.modPow(bigInteger6, bigInteger)).compareTo(ONE) == 0) {
        }
        return bigInteger4;
    }

    /*
     * Enabled aggressive block sorting
     */
    public GOST3410Parameters generateParameters() {
        BigInteger[] arrbigInteger = new BigInteger[2];
        if (this.typeproc == 1) {
            int n2 = this.init_random.nextInt();
            int n3 = this.init_random.nextInt();
            switch (this.size) {
                default: {
                    throw new IllegalArgumentException("Ooops! key size 512 or 1024 bit.");
                }
                case 512: {
                    this.procedure_A(n2, n3, arrbigInteger, 512);
                    break;
                }
                case 1024: {
                    this.procedure_B(n2, n3, arrbigInteger);
                }
            }
            BigInteger bigInteger = arrbigInteger[0];
            BigInteger bigInteger2 = arrbigInteger[1];
            return new GOST3410Parameters(bigInteger, bigInteger2, this.procedure_C(bigInteger, bigInteger2), new GOST3410ValidationParameters(n2, n3));
        }
        long l2 = this.init_random.nextLong();
        long l3 = this.init_random.nextLong();
        switch (this.size) {
            default: {
                throw new IllegalStateException("Ooops! key size 512 or 1024 bit.");
            }
            case 512: {
                this.procedure_Aa(l2, l3, arrbigInteger, 512);
                break;
            }
            case 1024: {
                this.procedure_Bb(l2, l3, arrbigInteger);
            }
        }
        BigInteger bigInteger = arrbigInteger[0];
        BigInteger bigInteger3 = arrbigInteger[1];
        return new GOST3410Parameters(bigInteger, bigInteger3, this.procedure_C(bigInteger, bigInteger3), new GOST3410ValidationParameters(l2, l3));
    }

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.size = n2;
        this.typeproc = n3;
        this.init_random = secureRandom;
    }
}

