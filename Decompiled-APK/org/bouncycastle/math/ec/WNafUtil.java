package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public abstract class WNafUtil {
    private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS;
    private static final byte[] EMPTY_BYTES;
    private static final int[] EMPTY_INTS;
    private static final ECPoint[] EMPTY_POINTS;
    public static final String PRECOMP_NAME = "bc_wnaf";

    static {
        DEFAULT_WINDOW_SIZE_CUTOFFS = new int[]{13, 41, EACTags.COEXISTANT_TAG_ALLOCATION_AUTHORITY, 337, 897, 2305};
        EMPTY_BYTES = new byte[0];
        EMPTY_INTS = new int[0];
        EMPTY_POINTS = new ECPoint[0];
    }

    public static int[] generateCompactNaf(BigInteger bigInteger) {
        if ((bigInteger.bitLength() >>> 16) != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        } else if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        } else {
            int i;
            BigInteger add = bigInteger.shiftLeft(1).add(bigInteger);
            int bitLength = add.bitLength();
            int[] iArr = new int[(bitLength >> 1)];
            BigInteger xor = add.xor(bigInteger);
            int i2 = bitLength - 1;
            int i3 = 0;
            bitLength = 0;
            int i4 = 1;
            while (i4 < i2) {
                if (xor.testBit(i4)) {
                    int i5 = bitLength + 1;
                    iArr[bitLength] = ((bigInteger.testBit(i4) ? -1 : 1) << 16) | i3;
                    i = i4 + 1;
                    i4 = 1;
                    bitLength = i5;
                } else {
                    int i6 = i4;
                    i4 = i3 + 1;
                    i = i6;
                }
                i3 = i4;
                i4 = i + 1;
            }
            i = bitLength + 1;
            iArr[bitLength] = PKIFailureInfo.notAuthorized | i3;
            return iArr.length > i ? trim(iArr, i) : iArr;
        }
    }

    public static int[] generateCompactWindowNaf(int i, BigInteger bigInteger) {
        if (i == 2) {
            return generateCompactNaf(bigInteger);
        }
        if (i < 2 || i > 16) {
            throw new IllegalArgumentException("'width' must be in the range [2, 16]");
        } else if ((bigInteger.bitLength() >>> 16) != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        } else if (bigInteger.signum() == 0) {
            return EMPTY_INTS;
        } else {
            int[] iArr = new int[((bigInteger.bitLength() / i) + 1)];
            int i2 = 1 << i;
            int i3 = i2 - 1;
            int i4 = i2 >>> 1;
            int i5 = 0;
            int i6 = 0;
            boolean z = false;
            while (i5 <= bigInteger.bitLength()) {
                if (bigInteger.testBit(i5) == z) {
                    i5++;
                } else {
                    bigInteger = bigInteger.shiftRight(i5);
                    int intValue = bigInteger.intValue() & i3;
                    if (z) {
                        intValue++;
                    }
                    z = (intValue & i4) != 0;
                    int i7 = z ? intValue - i2 : intValue;
                    intValue = i6 > 0 ? i5 - 1 : i5;
                    i5 = i6 + 1;
                    iArr[i6] = intValue | (i7 << 16);
                    i6 = i5;
                    i5 = i;
                }
            }
            return iArr.length > i6 ? trim(iArr, i6) : iArr;
        }
    }

    public static byte[] generateJSF(BigInteger bigInteger, BigInteger bigInteger2) {
        byte[] bArr = new byte[(Math.max(bigInteger.bitLength(), bigInteger2.bitLength()) + 1)];
        int i = 0;
        int i2 = 0;
        BigInteger bigInteger3 = bigInteger;
        int i3 = 0;
        BigInteger bigInteger4 = bigInteger2;
        int i4 = 0;
        while (true) {
            if ((i4 | i3) == 0 && bigInteger3.bitLength() <= i && bigInteger4.bitLength() <= i) {
                break;
            }
            int i5;
            int intValue = ((bigInteger3.intValue() >>> i) + i4) & 7;
            int intValue2 = ((bigInteger4.intValue() >>> i) + i3) & 7;
            int i6 = intValue & 1;
            if (i6 != 0) {
                i6 -= intValue & 2;
                if (intValue + i6 == 4 && (intValue2 & 3) == 2) {
                    i5 = -i6;
                    i6 = intValue2 & 1;
                    if (i6 != 0) {
                        i6 -= intValue2 & 2;
                        if (intValue2 + i6 == 4 && (intValue & 3) == 2) {
                            intValue = -i6;
                            i6 = (i4 << 1) != i5 + 1 ? i4 ^ 1 : i4;
                            i4 = (i3 << 1) != intValue + 1 ? i3 ^ 1 : i3;
                            i3 = i + 1;
                            if (i3 != 30) {
                                i3 = 0;
                                bigInteger3 = bigInteger3.shiftRight(30);
                                bigInteger4 = bigInteger4.shiftRight(30);
                            }
                            i = i2 + 1;
                            bArr[i2] = (byte) ((intValue & 15) | (i5 << 4));
                            i2 = i;
                            i = i3;
                            i3 = i4;
                            i4 = i6;
                        }
                    }
                    intValue = i6;
                    if ((i4 << 1) != i5 + 1) {
                    }
                    if ((i3 << 1) != intValue + 1) {
                    }
                    i3 = i + 1;
                    if (i3 != 30) {
                        i3 = 0;
                        bigInteger3 = bigInteger3.shiftRight(30);
                        bigInteger4 = bigInteger4.shiftRight(30);
                    }
                    i = i2 + 1;
                    bArr[i2] = (byte) ((intValue & 15) | (i5 << 4));
                    i2 = i;
                    i = i3;
                    i3 = i4;
                    i4 = i6;
                }
            }
            i5 = i6;
            i6 = intValue2 & 1;
            if (i6 != 0) {
                i6 -= intValue2 & 2;
                intValue = -i6;
                if ((i4 << 1) != i5 + 1) {
                }
                if ((i3 << 1) != intValue + 1) {
                }
                i3 = i + 1;
                if (i3 != 30) {
                    i3 = 0;
                    bigInteger3 = bigInteger3.shiftRight(30);
                    bigInteger4 = bigInteger4.shiftRight(30);
                }
                i = i2 + 1;
                bArr[i2] = (byte) ((intValue & 15) | (i5 << 4));
                i2 = i;
                i = i3;
                i3 = i4;
                i4 = i6;
            }
            intValue = i6;
            if ((i4 << 1) != i5 + 1) {
            }
            if ((i3 << 1) != intValue + 1) {
            }
            i3 = i + 1;
            if (i3 != 30) {
                i3 = 0;
                bigInteger3 = bigInteger3.shiftRight(30);
                bigInteger4 = bigInteger4.shiftRight(30);
            }
            i = i2 + 1;
            bArr[i2] = (byte) ((intValue & 15) | (i5 << 4));
            i2 = i;
            i = i3;
            i3 = i4;
            i4 = i6;
        }
        return bArr.length > i2 ? trim(bArr, i2) : bArr;
    }

    public static byte[] generateNaf(BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        }
        BigInteger add = bigInteger.shiftLeft(1).add(bigInteger);
        int bitLength = add.bitLength() - 1;
        byte[] bArr = new byte[bitLength];
        BigInteger xor = add.xor(bigInteger);
        int i = 1;
        while (i < bitLength) {
            int i2;
            if (xor.testBit(i)) {
                bArr[i - 1] = (byte) (bigInteger.testBit(i) ? -1 : 1);
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2 + 1;
        }
        bArr[bitLength - 1] = (byte) 1;
        return bArr;
    }

    public static byte[] generateWindowNaf(int i, BigInteger bigInteger) {
        if (i == 2) {
            return generateNaf(bigInteger);
        }
        if (i < 2 || i > 8) {
            throw new IllegalArgumentException("'width' must be in the range [2, 8]");
        } else if (bigInteger.signum() == 0) {
            return EMPTY_BYTES;
        } else {
            byte[] bArr = new byte[(bigInteger.bitLength() + 1)];
            int i2 = 1 << i;
            int i3 = i2 - 1;
            int i4 = i2 >>> 1;
            int i5 = 0;
            int i6 = 0;
            boolean z = false;
            while (i5 <= bigInteger.bitLength()) {
                if (bigInteger.testBit(i5) == z) {
                    i5++;
                } else {
                    bigInteger = bigInteger.shiftRight(i5);
                    int intValue = bigInteger.intValue() & i3;
                    if (z) {
                        intValue++;
                    }
                    z = (intValue & i4) != 0;
                    if (z) {
                        intValue -= i2;
                    }
                    if (i6 > 0) {
                        i5--;
                    }
                    i6 += i5;
                    i5 = i6 + 1;
                    bArr[i6] = (byte) intValue;
                    i6 = i5;
                    i5 = i;
                }
            }
            return bArr.length > i6 ? trim(bArr, i6) : bArr;
        }
    }

    public static int getNafWeight(BigInteger bigInteger) {
        return bigInteger.signum() == 0 ? 0 : bigInteger.shiftLeft(1).add(bigInteger).xor(bigInteger).bitCount();
    }

    public static WNafPreCompInfo getWNafPreCompInfo(ECPoint eCPoint) {
        return getWNafPreCompInfo(eCPoint.getCurve().getPreCompInfo(eCPoint, PRECOMP_NAME));
    }

    public static WNafPreCompInfo getWNafPreCompInfo(PreCompInfo preCompInfo) {
        return (preCompInfo == null || !(preCompInfo instanceof WNafPreCompInfo)) ? new WNafPreCompInfo() : (WNafPreCompInfo) preCompInfo;
    }

    public static int getWindowSize(int i) {
        return getWindowSize(i, DEFAULT_WINDOW_SIZE_CUTOFFS);
    }

    public static int getWindowSize(int i, int[] iArr) {
        int i2 = 0;
        while (i2 < iArr.length && i >= iArr[i2]) {
            i2++;
        }
        return i2 + 2;
    }

    public static ECPoint mapPointWithPrecomp(ECPoint eCPoint, int i, boolean z, ECPointMap eCPointMap) {
        int i2 = 0;
        ECCurve curve = eCPoint.getCurve();
        WNafPreCompInfo precompute = precompute(eCPoint, i, z);
        ECPoint map = eCPointMap.map(eCPoint);
        PreCompInfo wNafPreCompInfo = getWNafPreCompInfo(curve.getPreCompInfo(map, PRECOMP_NAME));
        ECPoint twice = precompute.getTwice();
        if (twice != null) {
            wNafPreCompInfo.setTwice(eCPointMap.map(twice));
        }
        ECPoint[] preComp = precompute.getPreComp();
        ECPoint[] eCPointArr = new ECPoint[preComp.length];
        for (int i3 = 0; i3 < preComp.length; i3++) {
            eCPointArr[i3] = eCPointMap.map(preComp[i3]);
        }
        wNafPreCompInfo.setPreComp(eCPointArr);
        if (z) {
            ECPoint[] eCPointArr2 = new ECPoint[eCPointArr.length];
            while (i2 < eCPointArr2.length) {
                eCPointArr2[i2] = eCPointArr[i2].negate();
                i2++;
            }
            wNafPreCompInfo.setPreCompNeg(eCPointArr2);
        }
        curve.setPreCompInfo(map, PRECOMP_NAME, wNafPreCompInfo);
        return map;
    }

    public static WNafPreCompInfo precompute(ECPoint eCPoint, int i, boolean z) {
        int i2;
        ECCurve curve = eCPoint.getCurve();
        Object wNafPreCompInfo = getWNafPreCompInfo(curve.getPreCompInfo(eCPoint, PRECOMP_NAME));
        int max = 1 << Math.max(0, i - 2);
        ECPoint[] preComp = wNafPreCompInfo.getPreComp();
        if (preComp == null) {
            preComp = EMPTY_POINTS;
            i2 = 0;
        } else {
            i2 = preComp.length;
        }
        if (i2 < max) {
            ECPoint[] resizeTable = resizeTable(preComp, max);
            if (max == 1) {
                resizeTable[0] = eCPoint.normalize();
                preComp = resizeTable;
            } else {
                int i3;
                if (i2 == 0) {
                    resizeTable[0] = eCPoint;
                    i3 = 1;
                } else {
                    i3 = i2;
                }
                ECFieldElement eCFieldElement = null;
                if (max == 2) {
                    resizeTable[1] = eCPoint.threeTimes();
                } else {
                    int i4;
                    ECPoint twice = wNafPreCompInfo.getTwice();
                    ECPoint eCPoint2 = resizeTable[i3 - 1];
                    if (twice == null) {
                        twice = resizeTable[0].twice();
                        wNafPreCompInfo.setTwice(twice);
                        if (ECAlgorithms.isFpCurve(curve) && curve.getFieldSize() >= 64) {
                            switch (curve.getCoordinateSystem()) {
                                case CipherSpiExt.DECRYPT_MODE /*2*/:
                                case F2m.PPB /*3*/:
                                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                                    ECFieldElement zCoord = twice.getZCoord(0);
                                    twice = curve.createPoint(twice.getXCoord().toBigInteger(), twice.getYCoord().toBigInteger());
                                    eCFieldElement = zCoord.square();
                                    ECPoint scaleY = eCPoint2.scaleX(eCFieldElement).scaleY(eCFieldElement.multiply(zCoord));
                                    if (i2 != 0) {
                                        eCPoint2 = scaleY;
                                        eCFieldElement = zCoord;
                                        i4 = i3;
                                        break;
                                    }
                                    resizeTable[0] = scaleY;
                                    eCPoint2 = scaleY;
                                    eCFieldElement = zCoord;
                                    i4 = i3;
                                    break;
                            }
                        }
                    }
                    i4 = i3;
                    while (i4 < max) {
                        i3 = i4 + 1;
                        eCPoint2 = eCPoint2.add(twice);
                        resizeTable[i4] = eCPoint2;
                        i4 = i3;
                    }
                }
                curve.normalizeAll(resizeTable, i2, max - i2, eCFieldElement);
                preComp = resizeTable;
            }
        }
        wNafPreCompInfo.setPreComp(preComp);
        if (z) {
            ECPoint[] eCPointArr;
            int i5;
            ECPoint[] preCompNeg = wNafPreCompInfo.getPreCompNeg();
            if (preCompNeg == null) {
                eCPointArr = new ECPoint[max];
                i5 = 0;
            } else {
                i5 = preCompNeg.length;
                eCPointArr = i5 < max ? resizeTable(preCompNeg, max) : preCompNeg;
            }
            while (i5 < max) {
                eCPointArr[i5] = preComp[i5].negate();
                i5++;
            }
            wNafPreCompInfo.setPreCompNeg(eCPointArr);
        }
        curve.setPreCompInfo(eCPoint, PRECOMP_NAME, wNafPreCompInfo);
        return wNafPreCompInfo;
    }

    private static ECPoint[] resizeTable(ECPoint[] eCPointArr, int i) {
        Object obj = new ECPoint[i];
        System.arraycopy(eCPointArr, 0, obj, 0, eCPointArr.length);
        return obj;
    }

    private static byte[] trim(byte[] bArr, int i) {
        Object obj = new byte[i];
        System.arraycopy(bArr, 0, obj, 0, obj.length);
        return obj;
    }

    private static int[] trim(int[] iArr, int i) {
        Object obj = new int[i];
        System.arraycopy(iArr, 0, obj, 0, obj.length);
        return obj;
    }
}
