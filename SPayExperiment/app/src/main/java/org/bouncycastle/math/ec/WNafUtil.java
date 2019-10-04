/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.PreCompInfo;
import org.bouncycastle.math.ec.WNafPreCompInfo;

public abstract class WNafUtil {
    private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS = new int[]{13, 41, 121, 337, 897, 2305};
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int[] EMPTY_INTS = new int[0];
    private static final ECPoint[] EMPTY_POINTS = new ECPoint[0];
    public static final String PRECOMP_NAME = "bc_wnaf";

    /*
     * Enabled aggressive block sorting
     */
    public static int[] generateCompactNaf(BigInteger bigInteger) {
        if (bigInteger.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        }
        BigInteger bigInteger2 = bigInteger.shiftLeft(1).add(bigInteger);
        int n = bigInteger2.bitLength();
        int[] arrn = new int[n >> 1];
        BigInteger bigInteger3 = bigInteger2.xor(bigInteger);
        int n2 = n - 1;
        int n3 = 0;
        int n4 = 0;
        int n5 = 1;
        while (n5 < n2) {
            int n6;
            int n7;
            if (!bigInteger3.testBit(n5)) {
                int n8 = n3 + 1;
                int n9 = n5;
                n6 = n8;
                n7 = n9;
            } else {
                int n10 = bigInteger.testBit(n5) ? -1 : 1;
                int n11 = n4 + 1;
                arrn[n4] = n3 | n10 << 16;
                n7 = n5 + 1;
                n6 = 1;
                n4 = n11;
            }
            int n12 = n7 + 1;
            n3 = n6;
            n5 = n12;
        }
        int n13 = n4 + 1;
        arrn[n4] = 65536 | n3;
        if (arrn.length > n13) {
            return WNafUtil.trim(arrn, n13);
        }
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int[] generateCompactWindowNaf(int n, BigInteger bigInteger) {
        if (n == 2) {
            return WNafUtil.generateCompactNaf(bigInteger);
        }
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("'width' must be in the range [2, 16]");
        }
        if (bigInteger.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        }
        int[] arrn = new int[1 + bigInteger.bitLength() / n];
        int n2 = 1 << n;
        int n3 = n2 - 1;
        int n4 = n2 >>> 1;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (n5 <= bigInteger.bitLength()) {
            if (bigInteger.testBit(n5) == bl) {
                ++n5;
                continue;
            }
            bigInteger = bigInteger.shiftRight(n5);
            int n7 = n3 & bigInteger.intValue();
            if (bl) {
                ++n7;
            }
            bl = (n7 & n4) != 0;
            int n8 = bl ? n7 - n2 : n7;
            int n9 = n6 > 0 ? n5 - 1 : n5;
            int n10 = n6 + 1;
            arrn[n6] = n9 | n8 << 16;
            n6 = n10;
            n5 = n;
        }
        if (arrn.length > n6) {
            return WNafUtil.trim(arrn, n6);
        }
        return arrn;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] generateJSF(BigInteger bigInteger, BigInteger bigInteger2) {
        byte[] arrby = new byte[1 + Math.max((int)bigInteger.bitLength(), (int)bigInteger2.bitLength())];
        int n = 0;
        int n2 = 0;
        BigInteger bigInteger3 = bigInteger;
        int n3 = 0;
        BigInteger bigInteger4 = bigInteger2;
        int n4 = 0;
        while (n4 | n3 || bigInteger3.bitLength() > n || bigInteger4.bitLength() > n) {
            int n5 = 7 & n4 + (bigInteger3.intValue() >>> n);
            int n6 = 7 & n3 + (bigInteger4.intValue() >>> n);
            int n7 = n5 & 1;
            int n8 = n7 != 0 && n5 + (n7 -= n5 & 2) == 4 && (n6 & 3) == 2 ? -n7 : n7;
            int n9 = n6 & 1;
            int n10 = n9 != 0 && n6 + (n9 -= n6 & 2) == 4 && (n5 & 3) == 2 ? -n9 : n9;
            int n11 = n4 << 1 == n8 + 1 ? n4 ^ 1 : n4;
            int n12 = n3 << 1 == n10 + 1 ? n3 ^ 1 : n3;
            int n13 = n + 1;
            if (n13 == 30) {
                n13 = 0;
                bigInteger3 = bigInteger3.shiftRight(30);
                bigInteger4 = bigInteger4.shiftRight(30);
            }
            int n14 = n2 + 1;
            arrby[n2] = (byte)(n8 << 4 | n10 & 15);
            n2 = n14;
            n = n13;
            n3 = n12;
            n4 = n11;
        }
        if (arrby.length > n2) {
            return WNafUtil.trim(arrby, n2);
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] generateNaf(BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        }
        BigInteger bigInteger2 = bigInteger.shiftLeft(1).add(bigInteger);
        int n = -1 + bigInteger2.bitLength();
        byte[] arrby = new byte[n];
        BigInteger bigInteger3 = bigInteger2.xor(bigInteger);
        int n2 = 1;
        do {
            int n3;
            if (n2 >= n) {
                arrby[n - 1] = 1;
                return arrby;
            }
            if (bigInteger3.testBit(n2)) {
                int n4 = n2 - 1;
                int n5 = bigInteger.testBit(n2) ? -1 : 1;
                arrby[n4] = n5;
                n3 = n2 + 1;
            } else {
                n3 = n2;
            }
            n2 = n3 + 1;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] generateWindowNaf(int n, BigInteger bigInteger) {
        if (n == 2) {
            return WNafUtil.generateNaf(bigInteger);
        }
        if (n < 2 || n > 8) {
            throw new IllegalArgumentException("'width' must be in the range [2, 8]");
        }
        if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        }
        byte[] arrby = new byte[1 + bigInteger.bitLength()];
        int n2 = 1 << n;
        int n3 = n2 - 1;
        int n4 = n2 >>> 1;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (n5 <= bigInteger.bitLength()) {
            if (bigInteger.testBit(n5) == bl) {
                ++n5;
                continue;
            }
            bigInteger = bigInteger.shiftRight(n5);
            int n7 = n3 & bigInteger.intValue();
            if (bl) {
                ++n7;
            }
            if (bl = (n7 & n4) != 0) {
                n7 -= n2;
            }
            if (n6 > 0) {
                --n5;
            }
            int n8 = n6 + n5;
            int n9 = n8 + 1;
            arrby[n8] = (byte)n7;
            n6 = n9;
            n5 = n;
        }
        if (arrby.length > n6) {
            return WNafUtil.trim(arrby, n6);
        }
        return arrby;
    }

    public static int getNafWeight(BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return 0;
        }
        return bigInteger.shiftLeft(1).add(bigInteger).xor(bigInteger).bitCount();
    }

    public static WNafPreCompInfo getWNafPreCompInfo(ECPoint eCPoint) {
        return WNafUtil.getWNafPreCompInfo(eCPoint.getCurve().getPreCompInfo(eCPoint, PRECOMP_NAME));
    }

    public static WNafPreCompInfo getWNafPreCompInfo(PreCompInfo preCompInfo) {
        if (preCompInfo != null && preCompInfo instanceof WNafPreCompInfo) {
            return (WNafPreCompInfo)preCompInfo;
        }
        return new WNafPreCompInfo();
    }

    public static int getWindowSize(int n) {
        return WNafUtil.getWindowSize(n, DEFAULT_WINDOW_SIZE_CUTOFFS);
    }

    public static int getWindowSize(int n, int[] arrn) {
        int n2 = 0;
        while (n2 < arrn.length && n >= arrn[n2]) {
            ++n2;
        }
        return n2 + 2;
    }

    public static ECPoint mapPointWithPrecomp(ECPoint eCPoint, int n, boolean bl, ECPointMap eCPointMap) {
        int n2 = 0;
        ECCurve eCCurve = eCPoint.getCurve();
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(eCPoint, n, bl);
        ECPoint eCPoint2 = eCPointMap.map(eCPoint);
        WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.getWNafPreCompInfo(eCCurve.getPreCompInfo(eCPoint2, PRECOMP_NAME));
        ECPoint eCPoint3 = wNafPreCompInfo.getTwice();
        if (eCPoint3 != null) {
            wNafPreCompInfo2.setTwice(eCPointMap.map(eCPoint3));
        }
        ECPoint[] arreCPoint = wNafPreCompInfo.getPreComp();
        ECPoint[] arreCPoint2 = new ECPoint[arreCPoint.length];
        for (int i = 0; i < arreCPoint.length; ++i) {
            arreCPoint2[i] = eCPointMap.map(arreCPoint[i]);
        }
        wNafPreCompInfo2.setPreComp(arreCPoint2);
        if (bl) {
            ECPoint[] arreCPoint3 = new ECPoint[arreCPoint2.length];
            while (n2 < arreCPoint3.length) {
                arreCPoint3[n2] = arreCPoint2[n2].negate();
                ++n2;
            }
            wNafPreCompInfo2.setPreCompNeg(arreCPoint3);
        }
        eCCurve.setPreCompInfo(eCPoint2, PRECOMP_NAME, wNafPreCompInfo2);
        return eCPoint2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static WNafPreCompInfo precompute(ECPoint var0, int var1_1, boolean var2_2) {
        block13 : {
            block16 : {
                block15 : {
                    block14 : {
                        var3_3 = var0.getCurve();
                        var4_4 = WNafUtil.getWNafPreCompInfo(var3_3.getPreCompInfo(var0, "bc_wnaf"));
                        var5_5 = 1 << Math.max((int)0, (int)(var1_1 - 2));
                        var6_6 = var4_4.getPreComp();
                        if (var6_6 == null) {
                            var6_6 = WNafUtil.EMPTY_POINTS;
                            var7_7 = 0;
                        } else {
                            var7_7 = var6_6.length;
                        }
                        if (var7_7 >= var5_5) break block13;
                        var11_8 = WNafUtil.resizeTable(var6_6, var5_5);
                        if (var5_5 != 1) break block14;
                        var11_8[0] = var0.normalize();
                        var6_6 = var11_8;
                        break block13;
                    }
                    if (var7_7 == 0) {
                        var11_8[0] = var0;
                        var12_9 = 1;
                    } else {
                        var12_9 = var7_7;
                    }
                    var13_10 = null;
                    if (var5_5 != 2) break block15;
                    var11_8[1] = var0.threeTimes();
                    break block16;
                }
                var14_11 = var4_4.getTwice();
                var15_12 = var11_8[var12_9 - 1];
                if (var14_11 != null) ** GOTO lbl-1000
                var14_11 = var11_8[0].twice();
                var4_4.setTwice(var14_11);
                if (!ECAlgorithms.isFpCurve(var3_3) || var3_3.getFieldSize() < 64) ** GOTO lbl-1000
                switch (var3_3.getCoordinateSystem()) {
                    default: lbl-1000: // 3 sources:
                    {
                        var16_13 = var12_9;
                        break;
                    }
                    case 2: 
                    case 3: 
                    case 4: {
                        var18_15 = var14_11.getZCoord(0);
                        var14_11 = var3_3.createPoint(var14_11.getXCoord().toBigInteger(), var14_11.getYCoord().toBigInteger());
                        var19_16 = var18_15.square();
                        var20_17 = var19_16.multiply(var18_15);
                        var21_18 = var15_12.scaleX(var19_16).scaleY(var20_17);
                        if (var7_7 == 0) {
                            var11_8[0] = var21_18;
                            var15_12 = var21_18;
                            var13_10 = var18_15;
                            var16_13 = var12_9;
                            break;
                        }
                        var15_12 = var21_18;
                        var13_10 = var18_15;
                        var16_13 = var12_9;
                    }
                }
                while (var16_13 < var5_5) {
                    var17_14 = var16_13 + 1;
                    var11_8[var16_13] = var15_12 = var15_12.add(var14_11);
                    var16_13 = var17_14;
                }
            }
            var3_3.normalizeAll(var11_8, var7_7, var5_5 - var7_7, var13_10);
            var6_6 = var11_8;
        }
        var4_4.setPreComp(var6_6);
        if (var2_2) {
            var8_19 = var4_4.getPreCompNeg();
            if (var8_19 == null) {
                var10_20 = new ECPoint[var5_5];
                var9_21 = 0;
            } else {
                var9_21 = var8_19.length;
                var10_20 = var9_21 < var5_5 ? WNafUtil.resizeTable(var8_19, var5_5) : var8_19;
            }
            while (var9_21 < var5_5) {
                var10_20[var9_21] = var6_6[var9_21].negate();
                ++var9_21;
            }
            var4_4.setPreCompNeg(var10_20);
        }
        var3_3.setPreCompInfo(var0, "bc_wnaf", var4_4);
        return var4_4;
    }

    private static ECPoint[] resizeTable(ECPoint[] arreCPoint, int n) {
        ECPoint[] arreCPoint2 = new ECPoint[n];
        System.arraycopy((Object)arreCPoint, (int)0, (Object)arreCPoint2, (int)0, (int)arreCPoint.length);
        return arreCPoint2;
    }

    private static byte[] trim(byte[] arrby, int n) {
        byte[] arrby2 = new byte[n];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        return arrby2;
    }

    private static int[] trim(int[] arrn, int n) {
        int[] arrn2 = new int[n];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn2, (int)0, (int)arrn2.length);
        return arrn2;
    }
}

