package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve.F2m;
import org.bouncycastle.math.ec.endo.ECEndomorphism;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.PolynomialExtensionField;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class ECAlgorithms {
    static ECPoint implShamirsTrickJsf(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        ECCurve curve = eCPoint.getCurve();
        ECPoint infinity = curve.getInfinity();
        ECPoint add = eCPoint.add(eCPoint2);
        ECPoint subtract = eCPoint.subtract(eCPoint2);
        curve.normalizeAll(new ECPoint[]{eCPoint2, subtract, eCPoint, add});
        ECPoint[] eCPointArr = new ECPoint[]{r4[3].negate(), r4[2].negate(), r4[1].negate(), r4[0].negate(), infinity, r4[0], r4[1], r4[2], r4[3]};
        byte[] generateJSF = WNafUtil.generateJSF(bigInteger, bigInteger2);
        int length = generateJSF.length;
        while (true) {
            length--;
            if (length < 0) {
                return infinity;
            }
            byte b = generateJSF[length];
            infinity = infinity.twicePlus(eCPointArr[((b << 28) >> 28) + ((((b << 24) >> 28) * 3) + 4)]);
        }
    }

    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        boolean z = false;
        boolean z2 = bigInteger.signum() < 0;
        if (bigInteger2.signum() < 0) {
            z = true;
        }
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs.bitLength())));
        int max2 = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs2.bitLength())));
        WNafPreCompInfo precompute = WNafUtil.precompute(eCPoint, max, true);
        WNafPreCompInfo precompute2 = WNafUtil.precompute(eCPoint2, max2, true);
        return implShamirsTrickWNaf(z2 ? precompute.getPreCompNeg() : precompute.getPreComp(), z2 ? precompute.getPreComp() : precompute.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs), z ? precompute2.getPreCompNeg() : precompute2.getPreComp(), z ? precompute2.getPreComp() : precompute2.getPreCompNeg(), WNafUtil.generateWindowNaf(max2, abs2));
    }

    static ECPoint implShamirsTrickWNaf(ECPoint eCPoint, BigInteger bigInteger, ECPointMap eCPointMap, BigInteger bigInteger2) {
        boolean z = false;
        boolean z2 = bigInteger.signum() < 0;
        if (bigInteger2.signum() < 0) {
            z = true;
        }
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(abs.bitLength(), abs2.bitLength()))));
        ECPoint mapPointWithPrecomp = WNafUtil.mapPointWithPrecomp(eCPoint, max, true, eCPointMap);
        WNafPreCompInfo wNafPreCompInfo = WNafUtil.getWNafPreCompInfo(eCPoint);
        WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.getWNafPreCompInfo(mapPointWithPrecomp);
        return implShamirsTrickWNaf(z2 ? wNafPreCompInfo.getPreCompNeg() : wNafPreCompInfo.getPreComp(), z2 ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs), z ? wNafPreCompInfo2.getPreCompNeg() : wNafPreCompInfo2.getPreComp(), z ? wNafPreCompInfo2.getPreComp() : wNafPreCompInfo2.getPreCompNeg(), WNafUtil.generateWindowNaf(max, abs2));
    }

    private static ECPoint implShamirsTrickWNaf(ECPoint[] eCPointArr, ECPoint[] eCPointArr2, byte[] bArr, ECPoint[] eCPointArr3, ECPoint[] eCPointArr4, byte[] bArr2) {
        int max = Math.max(bArr.length, bArr2.length);
        ECPoint infinity = eCPointArr[0].getCurve().getInfinity();
        int i = max - 1;
        int i2 = 0;
        ECPoint eCPoint = infinity;
        while (i >= 0) {
            max = i < bArr.length ? bArr[i] : 0;
            int i3 = i < bArr2.length ? bArr2[i] : 0;
            if ((max | i3) == 0) {
                max = i2 + 1;
            } else {
                ECPoint add;
                ECPoint timesPow2;
                if (max != 0) {
                    add = infinity.add((max < 0 ? eCPointArr2 : eCPointArr)[Math.abs(max) >>> 1]);
                } else {
                    add = infinity;
                }
                if (i3 != 0) {
                    add = add.add((i3 < 0 ? eCPointArr4 : eCPointArr3)[Math.abs(i3) >>> 1]);
                }
                if (i2 > 0) {
                    timesPow2 = eCPoint.timesPow2(i2);
                    max = 0;
                } else {
                    max = i2;
                    timesPow2 = eCPoint;
                }
                eCPoint = timesPow2.twicePlus(add);
            }
            i--;
            i2 = max;
        }
        return i2 > 0 ? eCPoint.timesPow2(i2) : eCPoint;
    }

    static ECPoint implSumOfMultiplies(ECPoint[] eCPointArr, ECPointMap eCPointMap, BigInteger[] bigIntegerArr) {
        int length = eCPointArr.length;
        int i = length << 1;
        boolean[] zArr = new boolean[i];
        WNafPreCompInfo[] wNafPreCompInfoArr = new WNafPreCompInfo[i];
        byte[][] bArr = new byte[i][];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 << 1;
            int i4 = i3 + 1;
            BigInteger bigInteger = bigIntegerArr[i3];
            zArr[i3] = bigInteger.signum() < 0;
            bigInteger = bigInteger.abs();
            BigInteger bigInteger2 = bigIntegerArr[i4];
            zArr[i4] = bigInteger2.signum() < 0;
            BigInteger abs = bigInteger2.abs();
            int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(bigInteger.bitLength(), abs.bitLength()))));
            ECPoint eCPoint = eCPointArr[i2];
            ECPoint mapPointWithPrecomp = WNafUtil.mapPointWithPrecomp(eCPoint, max, true, eCPointMap);
            wNafPreCompInfoArr[i3] = WNafUtil.getWNafPreCompInfo(eCPoint);
            wNafPreCompInfoArr[i4] = WNafUtil.getWNafPreCompInfo(mapPointWithPrecomp);
            bArr[i3] = WNafUtil.generateWindowNaf(max, bigInteger);
            bArr[i4] = WNafUtil.generateWindowNaf(max, abs);
        }
        return implSumOfMultiplies(zArr, wNafPreCompInfoArr, bArr);
    }

    static ECPoint implSumOfMultiplies(ECPoint[] eCPointArr, BigInteger[] bigIntegerArr) {
        int length = eCPointArr.length;
        boolean[] zArr = new boolean[length];
        WNafPreCompInfo[] wNafPreCompInfoArr = new WNafPreCompInfo[length];
        byte[][] bArr = new byte[length][];
        for (int i = 0; i < length; i++) {
            BigInteger bigInteger = bigIntegerArr[i];
            zArr[i] = bigInteger.signum() < 0;
            BigInteger abs = bigInteger.abs();
            int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs.bitLength())));
            wNafPreCompInfoArr[i] = WNafUtil.precompute(eCPointArr[i], max, true);
            bArr[i] = WNafUtil.generateWindowNaf(max, abs);
        }
        return implSumOfMultiplies(zArr, wNafPreCompInfoArr, bArr);
    }

    private static ECPoint implSumOfMultiplies(boolean[] zArr, WNafPreCompInfo[] wNafPreCompInfoArr, byte[][] bArr) {
        int i;
        int i2 = 0;
        for (byte[] length : bArr) {
            i2 = Math.max(i2, length.length);
        }
        ECPoint infinity = wNafPreCompInfoArr[0].getPreComp()[0].getCurve().getInfinity();
        int i3 = i2 - 1;
        int i4 = 0;
        ECPoint eCPoint = infinity;
        while (i3 >= 0) {
            int i5 = 0;
            ECPoint eCPoint2 = infinity;
            while (i5 < r8) {
                ECPoint add;
                byte[] bArr2 = bArr[i5];
                i = i3 < bArr2.length ? bArr2[i3] : 0;
                if (i != 0) {
                    int abs = Math.abs(i);
                    WNafPreCompInfo wNafPreCompInfo = wNafPreCompInfoArr[i5];
                    add = eCPoint2.add(((i < 0) == zArr[i5] ? wNafPreCompInfo.getPreComp() : wNafPreCompInfo.getPreCompNeg())[abs >>> 1]);
                } else {
                    add = eCPoint2;
                }
                i5++;
                eCPoint2 = add;
            }
            if (eCPoint2 == infinity) {
                i = i4 + 1;
            } else {
                ECPoint timesPow2;
                if (i4 > 0) {
                    timesPow2 = eCPoint.timesPow2(i4);
                    i = 0;
                } else {
                    i = i4;
                    timesPow2 = eCPoint;
                }
                eCPoint = timesPow2.twicePlus(eCPoint2);
            }
            i3--;
            i4 = i;
        }
        return i4 > 0 ? eCPoint.timesPow2(i4) : eCPoint;
    }

    static ECPoint implSumOfMultipliesGLV(ECPoint[] eCPointArr, BigInteger[] bigIntegerArr, GLVEndomorphism gLVEndomorphism) {
        int i = 0;
        BigInteger order = eCPointArr[0].getCurve().getOrder();
        BigInteger[] bigIntegerArr2 = new BigInteger[(r4 << 1)];
        int i2 = 0;
        for (int i3 = 0; i3 < r4; i3++) {
            BigInteger[] decomposeScalar = gLVEndomorphism.decomposeScalar(bigIntegerArr[i3].mod(order));
            int i4 = i2 + 1;
            bigIntegerArr2[i2] = decomposeScalar[0];
            i2 = i4 + 1;
            bigIntegerArr2[i4] = decomposeScalar[1];
        }
        ECPointMap pointMap = gLVEndomorphism.getPointMap();
        if (gLVEndomorphism.hasEfficientPointMap()) {
            return implSumOfMultiplies(eCPointArr, pointMap, bigIntegerArr2);
        }
        ECPoint[] eCPointArr2 = new ECPoint[(r4 << 1)];
        for (ECPoint eCPoint : eCPointArr) {
            ECPoint map = pointMap.map(eCPoint);
            int i5 = i + 1;
            eCPointArr2[i] = eCPoint;
            i = i5 + 1;
            eCPointArr2[i5] = map;
        }
        return implSumOfMultiplies(eCPointArr2, bigIntegerArr2);
    }

    public static ECPoint importPoint(ECCurve eCCurve, ECPoint eCPoint) {
        if (eCCurve.equals(eCPoint.getCurve())) {
            return eCCurve.importPoint(eCPoint);
        }
        throw new IllegalArgumentException("Point must be on the same curve");
    }

    public static boolean isF2mCurve(ECCurve eCCurve) {
        FiniteField field = eCCurve.getField();
        return field.getDimension() > 1 && field.getCharacteristic().equals(ECConstants.TWO) && (field instanceof PolynomialExtensionField);
    }

    public static boolean isFpCurve(ECCurve eCCurve) {
        return eCCurve.getField().getDimension() == 1;
    }

    public static void montgomeryTrick(ECFieldElement[] eCFieldElementArr, int i, int i2) {
        montgomeryTrick(eCFieldElementArr, i, i2, null);
    }

    public static void montgomeryTrick(ECFieldElement[] eCFieldElementArr, int i, int i2, ECFieldElement eCFieldElement) {
        int i3 = 0;
        ECFieldElement[] eCFieldElementArr2 = new ECFieldElement[i2];
        eCFieldElementArr2[0] = eCFieldElementArr[i];
        while (true) {
            i3++;
            if (i3 >= i2) {
                break;
            }
            eCFieldElementArr2[i3] = eCFieldElementArr2[i3 - 1].multiply(eCFieldElementArr[i + i3]);
        }
        int i4 = i3 - 1;
        if (eCFieldElement != null) {
            eCFieldElementArr2[i4] = eCFieldElementArr2[i4].multiply(eCFieldElement);
        }
        ECFieldElement invert = eCFieldElementArr2[i4].invert();
        while (i4 > 0) {
            int i5 = i4 - 1;
            i4 += i;
            ECFieldElement eCFieldElement2 = eCFieldElementArr[i4];
            eCFieldElementArr[i4] = eCFieldElementArr2[i5].multiply(invert);
            invert = invert.multiply(eCFieldElement2);
            i4 = i5;
        }
        eCFieldElementArr[i] = invert;
    }

    public static ECPoint referenceMultiply(ECPoint eCPoint, BigInteger bigInteger) {
        BigInteger abs = bigInteger.abs();
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int bitLength = abs.bitLength();
        if (bitLength > 0) {
            if (abs.testBit(0)) {
                infinity = eCPoint;
            }
            for (int i = 1; i < bitLength; i++) {
                eCPoint = eCPoint.twice();
                if (abs.testBit(i)) {
                    infinity = infinity.add(eCPoint);
                }
            }
        }
        return bigInteger.signum() < 0 ? infinity.negate() : infinity;
    }

    public static ECPoint shamirsTrick(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        return validatePoint(implShamirsTrickJsf(eCPoint, bigInteger, importPoint(eCPoint.getCurve(), eCPoint2), bigInteger2));
    }

    public static ECPoint sumOfMultiplies(ECPoint[] eCPointArr, BigInteger[] bigIntegerArr) {
        int i = 1;
        if (eCPointArr == null || bigIntegerArr == null || eCPointArr.length != bigIntegerArr.length || eCPointArr.length < 1) {
            throw new IllegalArgumentException("point and scalar arrays should be non-null, and of equal, non-zero, length");
        }
        int length = eCPointArr.length;
        switch (length) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return eCPointArr[0].multiply(bigIntegerArr[0]);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return sumOfTwoMultiplies(eCPointArr[0], bigIntegerArr[0], eCPointArr[1], bigIntegerArr[1]);
            default:
                ECPoint eCPoint = eCPointArr[0];
                ECCurve curve = eCPoint.getCurve();
                ECPoint[] eCPointArr2 = new ECPoint[length];
                eCPointArr2[0] = eCPoint;
                while (i < length) {
                    eCPointArr2[i] = importPoint(curve, eCPointArr[i]);
                    i++;
                }
                ECEndomorphism endomorphism = curve.getEndomorphism();
                return endomorphism instanceof GLVEndomorphism ? validatePoint(implSumOfMultipliesGLV(eCPointArr2, bigIntegerArr, (GLVEndomorphism) endomorphism)) : validatePoint(implSumOfMultiplies(eCPointArr2, bigIntegerArr));
        }
    }

    public static ECPoint sumOfTwoMultiplies(ECPoint eCPoint, BigInteger bigInteger, ECPoint eCPoint2, BigInteger bigInteger2) {
        ECCurve curve = eCPoint.getCurve();
        ECPoint importPoint = importPoint(curve, eCPoint2);
        if ((curve instanceof F2m) && ((F2m) curve).isKoblitz()) {
            return validatePoint(eCPoint.multiply(bigInteger).add(importPoint.multiply(bigInteger2)));
        }
        ECEndomorphism endomorphism = curve.getEndomorphism();
        if (!(endomorphism instanceof GLVEndomorphism)) {
            return validatePoint(implShamirsTrickWNaf(eCPoint, bigInteger, importPoint, bigInteger2));
        }
        return validatePoint(implSumOfMultipliesGLV(new ECPoint[]{eCPoint, importPoint}, new BigInteger[]{bigInteger, bigInteger2}, (GLVEndomorphism) endomorphism));
    }

    public static ECPoint validatePoint(ECPoint eCPoint) {
        if (eCPoint.isValid()) {
            return eCPoint;
        }
        throw new IllegalArgumentException("Invalid point");
    }
}
