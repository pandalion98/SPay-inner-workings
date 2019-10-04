/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECPointMap;
import org.bouncycastle.math.ec.WNafPreCompInfo;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.math.ec.endo.ECEndomorphism;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.PolynomialExtensionField;

public class ECAlgorithms {
    static ECPoint implShamirsTrickJsf(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        ECCurve eCCurve = eCPoint.getCurve();
        ECPoint eCPoint3 = eCCurve.getInfinity();
        ECPoint eCPoint4 = eCPoint.add(eCPoint2);
        ECPoint[] arreCPoint = new ECPoint[]{eCPoint2, eCPoint.subtract(eCPoint2), eCPoint, eCPoint4};
        eCCurve.normalizeAll(arreCPoint);
        ECPoint[] arreCPoint2 = new ECPoint[]{arreCPoint[3].negate(), arreCPoint[2].negate(), arreCPoint[1].negate(), arreCPoint[0].negate(), eCPoint3, arreCPoint[0], arreCPoint[1], arreCPoint[2], arreCPoint[3]};
        byte[] arrby = WNafUtil.generateJSF(bigInteger, bigInteger2);
        int n = arrby.length;
        while (--n >= 0) {
            byte by = arrby[n];
            int n2 = by << 24 >> 28;
            eCPoint3 = eCPoint3.twicePlus(arreCPoint2[(by << 28 >> 28) + (4 + n2 * 3)]);
        }
        return eCPoint3;
    }

    /*
     * Enabled aggressive block sorting
     */
    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        ECPoint[] arreCPoint;
        boolean bl = bigInteger.signum() < 0;
        int n = bigInteger2.signum();
        boolean bl2 = false;
        if (n < 0) {
            bl2 = true;
        }
        BigInteger bigInteger3 = bigInteger.abs();
        BigInteger bigInteger4 = bigInteger2.abs();
        int n2 = Math.max((int)2, (int)Math.min((int)16, (int)WNafUtil.getWindowSize(bigInteger3.bitLength())));
        int n3 = Math.max((int)2, (int)Math.min((int)16, (int)WNafUtil.getWindowSize(bigInteger4.bitLength())));
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.precompute(eCPoint, n2, true);
        WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.precompute(eCPoint2, n3, true);
        ECPoint[] arreCPoint2 = bl ? wNafPreCompInfo.getPreCompNeg() : wNafPreCompInfo.getPreComp();
        ECPoint[] arreCPoint3 = bl2 ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp();
        ECPoint[] arreCPoint4 = bl ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg();
        if (bl2) {
            arreCPoint = wNafPreCompInfo2.getPreComp();
            return ECAlgorithms.implShamirsTrickWNaf(arreCPoint2, arreCPoint4, WNafUtil.generateWindowNaf(n2, bigInteger3), arreCPoint3, arreCPoint, WNafUtil.generateWindowNaf(n3, bigInteger4));
        }
        arreCPoint = wNafPreCompInfo2.getPreCompNeg();
        return ECAlgorithms.implShamirsTrickWNaf(arreCPoint2, arreCPoint4, WNafUtil.generateWindowNaf(n2, bigInteger3), arreCPoint3, arreCPoint, WNafUtil.generateWindowNaf(n3, bigInteger4));
    }

    /*
     * Enabled aggressive block sorting
     */
    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPointMap eCPointMap, BigInteger bigInteger2) {
        ECPoint[] arreCPoint;
        boolean bl = bigInteger.signum() < 0;
        int n = bigInteger2.signum();
        boolean bl2 = false;
        if (n < 0) {
            bl2 = true;
        }
        BigInteger bigInteger3 = bigInteger.abs();
        BigInteger bigInteger4 = bigInteger2.abs();
        int n2 = Math.max((int)2, (int)Math.min((int)16, (int)WNafUtil.getWindowSize(Math.max((int)bigInteger3.bitLength(), (int)bigInteger4.bitLength()))));
        ECPoint eCPoint2 = WNafUtil.mapPointWithPrecomp(eCPoint, n2, true, eCPointMap);
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.getWNafPreCompInfo(eCPoint);
        WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.getWNafPreCompInfo(eCPoint2);
        ECPoint[] arreCPoint2 = bl ? wNafPreCompInfo.getPreCompNeg() : wNafPreCompInfo.getPreComp();
        ECPoint[] arreCPoint3 = bl2 ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp();
        ECPoint[] arreCPoint4 = bl ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg();
        if (bl2) {
            arreCPoint = wNafPreCompInfo2.getPreComp();
            return ECAlgorithms.implShamirsTrickWNaf(arreCPoint2, arreCPoint4, WNafUtil.generateWindowNaf(n2, bigInteger3), arreCPoint3, arreCPoint, WNafUtil.generateWindowNaf(n2, bigInteger4));
        }
        arreCPoint = wNafPreCompInfo2.getPreCompNeg();
        return ECAlgorithms.implShamirsTrickWNaf(arreCPoint2, arreCPoint4, WNafUtil.generateWindowNaf(n2, bigInteger3), arreCPoint3, arreCPoint, WNafUtil.generateWindowNaf(n2, bigInteger4));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ECPoint implShamirsTrickWNaf(ECPoint[] arreCPoint, ECPoint[] arreCPoint2, byte[] arrby, ECPoint[] arreCPoint3, ECPoint[] arreCPoint4, byte[] arrby2) {
        int n = Math.max((int)arrby.length, (int)arrby2.length);
        ECPoint eCPoint = arreCPoint[0].getCurve().getInfinity();
        int n2 = n - 1;
        int n3 = 0;
        ECPoint eCPoint2 = eCPoint;
        do {
            int n4;
            block6 : {
                byte by;
                ECPoint eCPoint3;
                ECPoint eCPoint4;
                block8 : {
                    block7 : {
                        block4 : {
                            byte by2;
                            block5 : {
                                if (n2 < 0) break block4;
                                by2 = n2 < arrby.length ? arrby[n2] : (byte)0;
                                if ((by2 | (by = n2 < arrby2.length ? arrby2[n2] : (byte)0)) != 0) break block5;
                                n4 = n3 + 1;
                                break block6;
                            }
                            if (by2 == 0) break block7;
                            int n5 = Math.abs((int)by2);
                            ECPoint[] arreCPoint5 = by2 < 0 ? arreCPoint2 : arreCPoint;
                            eCPoint3 = eCPoint.add(arreCPoint5[n5 >>> 1]);
                            break block8;
                        }
                        if (n3 <= 0) return eCPoint2;
                        return eCPoint2.timesPow2(n3);
                    }
                    eCPoint3 = eCPoint;
                }
                if (by != 0) {
                    int n6 = Math.abs((int)by);
                    ECPoint[] arreCPoint6 = by < 0 ? arreCPoint4 : arreCPoint3;
                    eCPoint3 = eCPoint3.add(arreCPoint6[n6 >>> 1]);
                }
                if (n3 > 0) {
                    eCPoint4 = eCPoint2.timesPow2(n3);
                    n4 = 0;
                } else {
                    n4 = n3;
                    eCPoint4 = eCPoint2;
                }
                eCPoint2 = eCPoint4.twicePlus(eCPoint3);
            }
            --n2;
            n3 = n4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    static ECPoint implSumOfMultiplies(ECPoint[] arreCPoint, ECPointMap eCPointMap, BigInteger[] arrbigInteger) {
        int n = arreCPoint.length;
        int n2 = n << 1;
        boolean[] arrbl = new boolean[n2];
        WNafPreCompInfo[] arrwNafPreCompInfo = new WNafPreCompInfo[n2];
        byte[][] arrarrby = new byte[n2][];
        int n3 = 0;
        while (n3 < n) {
            int n4 = n3 << 1;
            int n5 = n4 + 1;
            BigInteger bigInteger = arrbigInteger[n4];
            boolean bl = bigInteger.signum() < 0;
            arrbl[n4] = bl;
            BigInteger bigInteger2 = bigInteger.abs();
            BigInteger bigInteger3 = arrbigInteger[n5];
            boolean bl2 = bigInteger3.signum() < 0;
            arrbl[n5] = bl2;
            BigInteger bigInteger4 = bigInteger3.abs();
            int n6 = Math.max((int)2, (int)Math.min((int)16, (int)WNafUtil.getWindowSize(Math.max((int)bigInteger2.bitLength(), (int)bigInteger4.bitLength()))));
            ECPoint eCPoint = arreCPoint[n3];
            ECPoint eCPoint2 = WNafUtil.mapPointWithPrecomp(eCPoint, n6, true, eCPointMap);
            arrwNafPreCompInfo[n4] = WNafUtil.getWNafPreCompInfo(eCPoint);
            arrwNafPreCompInfo[n5] = WNafUtil.getWNafPreCompInfo(eCPoint2);
            arrarrby[n4] = WNafUtil.generateWindowNaf(n6, bigInteger2);
            arrarrby[n5] = WNafUtil.generateWindowNaf(n6, bigInteger4);
            ++n3;
        }
        return ECAlgorithms.implSumOfMultiplies(arrbl, arrwNafPreCompInfo, arrarrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    static ECPoint implSumOfMultiplies(ECPoint[] arreCPoint, BigInteger[] arrbigInteger) {
        int n = arreCPoint.length;
        boolean[] arrbl = new boolean[n];
        WNafPreCompInfo[] arrwNafPreCompInfo = new WNafPreCompInfo[n];
        byte[][] arrarrby = new byte[n][];
        int n2 = 0;
        while (n2 < n) {
            BigInteger bigInteger = arrbigInteger[n2];
            boolean bl = bigInteger.signum() < 0;
            arrbl[n2] = bl;
            BigInteger bigInteger2 = bigInteger.abs();
            int n3 = Math.max((int)2, (int)Math.min((int)16, (int)WNafUtil.getWindowSize(bigInteger2.bitLength())));
            arrwNafPreCompInfo[n2] = WNafUtil.precompute(arreCPoint[n2], n3, true);
            arrarrby[n2] = WNafUtil.generateWindowNaf(n3, bigInteger2);
            ++n2;
        }
        return ECAlgorithms.implSumOfMultiplies(arrbl, arrwNafPreCompInfo, arrarrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ECPoint implSumOfMultiplies(boolean[] arrbl, WNafPreCompInfo[] arrwNafPreCompInfo, byte[][] arrby) {
        int n = arrby.length;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            n2 = Math.max((int)n2, (int)arrby[i].length);
        }
        ECPoint eCPoint = arrwNafPreCompInfo[0].getPreComp()[0].getCurve().getInfinity();
        int n3 = n2 - 1;
        int n4 = 0;
        ECPoint eCPoint2 = eCPoint;
        block1 : do {
            if (n3 < 0) {
                if (n4 <= 0) return eCPoint2;
                return eCPoint2.timesPow2(n4);
            }
            int n5 = 0;
            ECPoint eCPoint3 = eCPoint;
            do {
                ECPoint eCPoint4;
                block10 : {
                    block9 : {
                        int n6;
                        block8 : {
                            if (n5 >= n) break block8;
                            byte[] arrby2 = arrby[n5];
                            byte by = n3 < arrby2.length ? arrby2[n3] : (byte)0;
                            if (by == 0) break block9;
                            int n7 = Math.abs((int)by);
                            WNafPreCompInfo wNafPreCompInfo = arrwNafPreCompInfo[n5];
                            boolean bl = by < 0;
                            ECPoint[] arreCPoint = bl == arrbl[n5] ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg();
                            eCPoint4 = eCPoint3.add(arreCPoint[n7 >>> 1]);
                            break block10;
                        }
                        if (eCPoint3 == eCPoint) {
                            n6 = n4 + 1;
                        } else {
                            ECPoint eCPoint5;
                            if (n4 > 0) {
                                eCPoint5 = eCPoint2.timesPow2(n4);
                                n6 = 0;
                            } else {
                                n6 = n4;
                                eCPoint5 = eCPoint2;
                            }
                            eCPoint2 = eCPoint5.twicePlus(eCPoint3);
                        }
                        --n3;
                        n4 = n6;
                        continue block1;
                    }
                    eCPoint4 = eCPoint3;
                }
                ++n5;
                eCPoint3 = eCPoint4;
            } while (true);
            break;
        } while (true);
    }

    static ECPoint implSumOfMultipliesGLV(ECPoint[] arreCPoint, BigInteger[] arrbigInteger, GLVEndomorphism gLVEndomorphism) {
        int n = 0;
        BigInteger bigInteger = arreCPoint[0].getCurve().getOrder();
        int n2 = arreCPoint.length;
        BigInteger[] arrbigInteger2 = new BigInteger[n2 << 1];
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            BigInteger[] arrbigInteger3 = gLVEndomorphism.decomposeScalar(arrbigInteger[i].mod(bigInteger));
            int n4 = n3 + 1;
            arrbigInteger2[n3] = arrbigInteger3[0];
            n3 = n4 + 1;
            arrbigInteger2[n4] = arrbigInteger3[1];
        }
        ECPointMap eCPointMap = gLVEndomorphism.getPointMap();
        if (gLVEndomorphism.hasEfficientPointMap()) {
            return ECAlgorithms.implSumOfMultiplies(arreCPoint, eCPointMap, arrbigInteger2);
        }
        ECPoint[] arreCPoint2 = new ECPoint[n2 << 1];
        for (int i = 0; i < n2; ++i) {
            ECPoint eCPoint = arreCPoint[i];
            ECPoint eCPoint2 = eCPointMap.map(eCPoint);
            int n5 = n + 1;
            arreCPoint2[n] = eCPoint;
            n = n5 + 1;
            arreCPoint2[n5] = eCPoint2;
        }
        return ECAlgorithms.implSumOfMultiplies(arreCPoint2, arrbigInteger2);
    }

    public static ECPoint importPoint(ECCurve eCCurve, ECPoint eCPoint) {
        if (!eCCurve.equals(eCPoint.getCurve())) {
            throw new IllegalArgumentException("Point must be on the same curve");
        }
        return eCCurve.importPoint(eCPoint);
    }

    public static boolean isF2mCurve(ECCurve eCCurve) {
        FiniteField finiteField = eCCurve.getField();
        return finiteField.getDimension() > 1 && finiteField.getCharacteristic().equals((Object)ECConstants.TWO) && finiteField instanceof PolynomialExtensionField;
    }

    public static boolean isFpCurve(ECCurve eCCurve) {
        return eCCurve.getField().getDimension() == 1;
    }

    public static void montgomeryTrick(ECFieldElement[] arreCFieldElement, int n, int n2) {
        ECAlgorithms.montgomeryTrick(arreCFieldElement, n, n2, null);
    }

    public static void montgomeryTrick(ECFieldElement[] arreCFieldElement, int n, int n2, ECFieldElement eCFieldElement) {
        int n3 = 0;
        ECFieldElement[] arreCFieldElement2 = new ECFieldElement[n2];
        arreCFieldElement2[0] = arreCFieldElement[n];
        while (++n3 < n2) {
            arreCFieldElement2[n3] = arreCFieldElement2[n3 - 1].multiply(arreCFieldElement[n + n3]);
        }
        int n4 = n3 - 1;
        if (eCFieldElement != null) {
            arreCFieldElement2[n4] = arreCFieldElement2[n4].multiply(eCFieldElement);
        }
        ECFieldElement eCFieldElement2 = arreCFieldElement2[n4].invert();
        while (n4 > 0) {
            int n5 = n4 - 1;
            int n6 = n4 + n;
            ECFieldElement eCFieldElement3 = arreCFieldElement[n6];
            arreCFieldElement[n6] = arreCFieldElement2[n5].multiply(eCFieldElement2);
            eCFieldElement2 = eCFieldElement2.multiply(eCFieldElement3);
            n4 = n5;
        }
        arreCFieldElement[n] = eCFieldElement2;
    }

    public static ECPoint referenceMultiply(ECPoint eCPoint, BigInteger bigInteger) {
        BigInteger bigInteger2 = bigInteger.abs();
        ECPoint eCPoint2 = eCPoint.getCurve().getInfinity();
        int n = bigInteger2.bitLength();
        if (n > 0) {
            if (bigInteger2.testBit(0)) {
                eCPoint2 = eCPoint;
            }
            for (int i = 1; i < n; ++i) {
                eCPoint = eCPoint.twice();
                if (!bigInteger2.testBit(i)) continue;
                eCPoint2 = eCPoint2.add(eCPoint);
            }
        }
        if (bigInteger.signum() < 0) {
            eCPoint2 = eCPoint2.negate();
        }
        return eCPoint2;
    }

    public static ECPoint shamirsTrick(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        return ECAlgorithms.validatePoint(ECAlgorithms.implShamirsTrickJsf(eCPoint, bigInteger, ECAlgorithms.importPoint(eCPoint.getCurve(), eCPoint2), bigInteger2));
    }

    public static ECPoint sumOfMultiplies(ECPoint[] arreCPoint, BigInteger[] arrbigInteger) {
        int n;
        ECCurve eCCurve;
        ECPoint[] arreCPoint2;
        if (arreCPoint == null || arrbigInteger == null || arreCPoint.length != arrbigInteger.length || arreCPoint.length < n) {
            throw new IllegalArgumentException("point and scalar arrays should be non-null, and of equal, non-zero, length");
        }
        int n2 = arreCPoint.length;
        switch (n2) {
            default: {
                ECPoint eCPoint = arreCPoint[0];
                eCCurve = eCPoint.getCurve();
                arreCPoint2 = new ECPoint[n2];
                arreCPoint2[0] = eCPoint;
                for (n = 1; n < n2; ++n) {
                    arreCPoint2[n] = ECAlgorithms.importPoint(eCCurve, arreCPoint[n]);
                }
                break;
            }
            case 1: {
                return arreCPoint[0].multiply(arrbigInteger[0]);
            }
            case 2: {
                return ECAlgorithms.sumOfTwoMultiplies(arreCPoint[0], arrbigInteger[0], arreCPoint[n], arrbigInteger[n]);
            }
        }
        ECEndomorphism eCEndomorphism = eCCurve.getEndomorphism();
        if (eCEndomorphism instanceof GLVEndomorphism) {
            return ECAlgorithms.validatePoint(ECAlgorithms.implSumOfMultipliesGLV(arreCPoint2, arrbigInteger, (GLVEndomorphism)eCEndomorphism));
        }
        return ECAlgorithms.validatePoint(ECAlgorithms.implSumOfMultiplies(arreCPoint2, arrbigInteger));
    }

    public static ECPoint sumOfTwoMultiplies(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        ECCurve eCCurve = eCPoint.getCurve();
        ECPoint eCPoint3 = ECAlgorithms.importPoint(eCCurve, eCPoint2);
        if (eCCurve instanceof ECCurve.F2m && ((ECCurve.F2m)eCCurve).isKoblitz()) {
            return ECAlgorithms.validatePoint(eCPoint.multiply(bigInteger).add(eCPoint3.multiply(bigInteger2)));
        }
        ECEndomorphism eCEndomorphism = eCCurve.getEndomorphism();
        if (eCEndomorphism instanceof GLVEndomorphism) {
            return ECAlgorithms.validatePoint(ECAlgorithms.implSumOfMultipliesGLV(new ECPoint[]{eCPoint, eCPoint3}, new BigInteger[]{bigInteger, bigInteger2}, (GLVEndomorphism)eCEndomorphism));
        }
        return ECAlgorithms.validatePoint(ECAlgorithms.implShamirsTrickWNaf(eCPoint, bigInteger, eCPoint3, bigInteger2));
    }

    public static ECPoint validatePoint(ECPoint eCPoint) {
        if (!eCPoint.isValid()) {
            throw new IllegalArgumentException("Invalid point");
        }
        return eCPoint;
    }
}

