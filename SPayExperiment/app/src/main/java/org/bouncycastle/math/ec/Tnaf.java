/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.SimpleBigDecimal;
import org.bouncycastle.math.ec.ZTauElement;

class Tnaf {
    private static final BigInteger MINUS_ONE = ECConstants.ONE.negate();
    private static final BigInteger MINUS_THREE;
    private static final BigInteger MINUS_TWO;
    public static final byte POW_2_WIDTH = 16;
    public static final byte WIDTH = 4;
    public static final ZTauElement[] alpha0;
    public static final byte[][] alpha0Tnaf;
    public static final ZTauElement[] alpha1;
    public static final byte[][] alpha1Tnaf;

    static {
        MINUS_TWO = ECConstants.TWO.negate();
        MINUS_THREE = ECConstants.THREE.negate();
        ZTauElement[] arrzTauElement = new ZTauElement[]{null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, MINUS_ONE), null, new ZTauElement(MINUS_ONE, MINUS_ONE), null, new ZTauElement(ECConstants.ONE, MINUS_ONE), null};
        alpha0 = arrzTauElement;
        alpha0Tnaf = new byte[][]{null, {1}, null, {-1, 0, 1}, null, {1, 0, 1}, null, {-1, 0, 0, 1}};
        ZTauElement[] arrzTauElement2 = new ZTauElement[]{null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, ECConstants.ONE), null, new ZTauElement(MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null};
        alpha1 = arrzTauElement2;
        alpha1Tnaf = new byte[][]{null, {1}, null, {-1, 0, 1}, null, {1, 0, 1}, null, {-1, 0, 0, -1}};
    }

    Tnaf() {
    }

    public static SimpleBigDecimal approximateDivisionByN(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, byte by, int n, int n2) {
        int n3 = n2 + (n + 5) / 2;
        BigInteger bigInteger4 = bigInteger2.multiply(bigInteger.shiftRight(by + (-2 + (n - n3))));
        BigInteger bigInteger5 = bigInteger4.add(bigInteger3.multiply(bigInteger4.shiftRight(n)));
        BigInteger bigInteger6 = bigInteger5.shiftRight(n3 - n2);
        if (bigInteger5.testBit(-1 + (n3 - n2))) {
            bigInteger6 = bigInteger6.add(ECConstants.ONE);
        }
        return new SimpleBigDecimal(bigInteger6, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static BigInteger[] getLucas(byte by, int n, boolean bl) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        if (by != 1 && by != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        if (bl) {
            bigInteger2 = ECConstants.TWO;
            bigInteger = BigInteger.valueOf((long)by);
        } else {
            bigInteger2 = ECConstants.ZERO;
            bigInteger = ECConstants.ONE;
        }
        int n2 = 1;
        BigInteger bigInteger3 = bigInteger2;
        BigInteger bigInteger4 = bigInteger;
        while (n2 < n) {
            BigInteger bigInteger5 = by == 1 ? bigInteger4 : bigInteger4.negate();
            BigInteger bigInteger6 = bigInteger5.subtract(bigInteger3.shiftLeft(1));
            ++n2;
            bigInteger3 = bigInteger4;
            bigInteger4 = bigInteger6;
        }
        return new BigInteger[]{bigInteger3, bigInteger4};
    }

    public static byte getMu(ECCurve.F2m f2m) {
        if (!f2m.isKoblitz()) {
            throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
        }
        if (f2m.getA().isZero()) {
            return -1;
        }
        return 1;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ECPoint.F2m[] getPreComp(ECPoint.F2m f2m, byte by) {
        ECPoint[] arreCPoint = new ECPoint.F2m[16];
        arreCPoint[1] = f2m;
        byte[][] arrby = by == 0 ? alpha0Tnaf : alpha1Tnaf;
        int n = arrby.length;
        int n2 = 3;
        do {
            if (n2 >= n) {
                f2m.getCurve().normalizeAll(arreCPoint);
                return arreCPoint;
            }
            arreCPoint[n2] = Tnaf.multiplyFromTnaf(f2m, arrby[n2]);
            n2 += 2;
        } while (true);
    }

    protected static int getShiftsForCofactor(BigInteger bigInteger) {
        if (bigInteger != null) {
            if (bigInteger.equals((Object)ECConstants.TWO)) {
                return 1;
            }
            if (bigInteger.equals((Object)ECConstants.FOUR)) {
                return 2;
            }
        }
        throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
    }

    public static BigInteger[] getSi(ECCurve.F2m f2m) {
        if (!f2m.isKoblitz()) {
            throw new IllegalArgumentException("si is defined for Koblitz curves only");
        }
        int n = f2m.getM();
        int n2 = f2m.getA().toBigInteger().intValue();
        byte by = f2m.getMu();
        int n3 = Tnaf.getShiftsForCofactor(f2m.getCofactor());
        BigInteger[] arrbigInteger = Tnaf.getLucas(by, n + 3 - n2, false);
        if (by == 1) {
            arrbigInteger[0] = arrbigInteger[0].negate();
            arrbigInteger[1] = arrbigInteger[1].negate();
        }
        return new BigInteger[]{ECConstants.ONE.add(arrbigInteger[1]).shiftRight(n3), ECConstants.ONE.add(arrbigInteger[0]).shiftRight(n3).negate()};
    }

    public static BigInteger getTw(byte by, int n) {
        if (n == 4) {
            if (by == 1) {
                return BigInteger.valueOf((long)6L);
            }
            return BigInteger.valueOf((long)10L);
        }
        BigInteger[] arrbigInteger = Tnaf.getLucas(by, n, false);
        BigInteger bigInteger = ECConstants.ZERO.setBit(n);
        BigInteger bigInteger2 = arrbigInteger[1].modInverse(bigInteger);
        return ECConstants.TWO.multiply(arrbigInteger[0]).multiply(bigInteger2).mod(bigInteger);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ECPoint.F2m multiplyFromTnaf(ECPoint.F2m f2m, byte[] arrby) {
        ECPoint.F2m f2m2 = (ECPoint.F2m)((ECCurve.F2m)f2m.getCurve()).getInfinity();
        int n = -1 + arrby.length;
        while (n >= 0) {
            f2m2 = Tnaf.tau(f2m2);
            if (arrby[n] == 1) {
                f2m2 = f2m2.addSimple(f2m);
            } else if (arrby[n] == -1) {
                f2m2 = f2m2.subtractSimple(f2m);
            }
            --n;
        }
        return f2m2;
    }

    public static ECPoint.F2m multiplyRTnaf(ECPoint.F2m f2m, BigInteger bigInteger) {
        ECCurve.F2m f2m2 = (ECCurve.F2m)f2m.getCurve();
        int n = f2m2.getM();
        byte by = (byte)f2m2.getA().toBigInteger().intValue();
        byte by2 = f2m2.getMu();
        return Tnaf.multiplyTnaf(f2m, Tnaf.partModReduction(bigInteger, n, by, f2m2.getSi(), by2, (byte)10));
    }

    public static ECPoint.F2m multiplyTnaf(ECPoint.F2m f2m, ZTauElement zTauElement) {
        return Tnaf.multiplyFromTnaf(f2m, Tnaf.tauAdicNaf(((ECCurve.F2m)f2m.getCurve()).getMu(), zTauElement));
    }

    public static BigInteger norm(byte by, ZTauElement zTauElement) {
        BigInteger bigInteger = zTauElement.u.multiply(zTauElement.u);
        BigInteger bigInteger2 = zTauElement.u.multiply(zTauElement.v);
        BigInteger bigInteger3 = zTauElement.v.multiply(zTauElement.v).shiftLeft(1);
        if (by == 1) {
            return bigInteger.add(bigInteger2).add(bigInteger3);
        }
        if (by == -1) {
            return bigInteger.subtract(bigInteger2).add(bigInteger3);
        }
        throw new IllegalArgumentException("mu must be 1 or -1");
    }

    public static SimpleBigDecimal norm(byte by, SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2) {
        SimpleBigDecimal simpleBigDecimal3 = simpleBigDecimal.multiply(simpleBigDecimal);
        SimpleBigDecimal simpleBigDecimal4 = simpleBigDecimal.multiply(simpleBigDecimal2);
        SimpleBigDecimal simpleBigDecimal5 = simpleBigDecimal2.multiply(simpleBigDecimal2).shiftLeft(1);
        if (by == 1) {
            return simpleBigDecimal3.add(simpleBigDecimal4).add(simpleBigDecimal5);
        }
        if (by == -1) {
            return simpleBigDecimal3.subtract(simpleBigDecimal4).add(simpleBigDecimal5);
        }
        throw new IllegalArgumentException("mu must be 1 or -1");
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ZTauElement partModReduction(BigInteger bigInteger, int n, byte by, BigInteger[] arrbigInteger, byte by2, byte by3) {
        BigInteger bigInteger2 = by2 == 1 ? arrbigInteger[0].add(arrbigInteger[1]) : arrbigInteger[0].subtract(arrbigInteger[1]);
        BigInteger bigInteger3 = Tnaf.getLucas(by2, n, true)[1];
        ZTauElement zTauElement = Tnaf.round(Tnaf.approximateDivisionByN(bigInteger, arrbigInteger[0], bigInteger3, by, n, by3), Tnaf.approximateDivisionByN(bigInteger, arrbigInteger[1], bigInteger3, by, n, by3), by2);
        return new ZTauElement(bigInteger.subtract(bigInteger2.multiply(zTauElement.u)).subtract(BigInteger.valueOf((long)2L).multiply(arrbigInteger[1]).multiply(zTauElement.v)), arrbigInteger[1].multiply(zTauElement.u).subtract(arrbigInteger[0].multiply(zTauElement.v)));
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ZTauElement round(SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2, byte n) {
        SimpleBigDecimal simpleBigDecimal3;
        SimpleBigDecimal simpleBigDecimal4;
        int n2;
        int n3 = 1;
        int n4 = simpleBigDecimal.getScale();
        if (simpleBigDecimal2.getScale() != n4) {
            throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
        }
        if (n != n3 && n != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        BigInteger bigInteger = simpleBigDecimal.round();
        BigInteger bigInteger2 = simpleBigDecimal2.round();
        SimpleBigDecimal simpleBigDecimal5 = simpleBigDecimal.subtract(bigInteger);
        SimpleBigDecimal simpleBigDecimal6 = simpleBigDecimal2.subtract(bigInteger2);
        SimpleBigDecimal simpleBigDecimal7 = simpleBigDecimal5.add(simpleBigDecimal5);
        SimpleBigDecimal simpleBigDecimal8 = n == n3 ? simpleBigDecimal7.add(simpleBigDecimal6) : simpleBigDecimal7.subtract(simpleBigDecimal6);
        SimpleBigDecimal simpleBigDecimal9 = simpleBigDecimal6.add(simpleBigDecimal6).add(simpleBigDecimal6);
        SimpleBigDecimal simpleBigDecimal10 = simpleBigDecimal9.add(simpleBigDecimal6);
        if (n == n3) {
            simpleBigDecimal4 = simpleBigDecimal5.subtract(simpleBigDecimal9);
            simpleBigDecimal3 = simpleBigDecimal5.add(simpleBigDecimal10);
        } else {
            simpleBigDecimal4 = simpleBigDecimal5.add(simpleBigDecimal9);
            simpleBigDecimal3 = simpleBigDecimal5.subtract(simpleBigDecimal10);
        }
        if (simpleBigDecimal8.compareTo(ECConstants.ONE) >= 0) {
            int n5 = simpleBigDecimal4.compareTo(MINUS_ONE);
            n2 = 0;
            if (n5 < 0) {
                n3 = 0;
                n2 = n;
            }
        } else if (simpleBigDecimal3.compareTo(ECConstants.TWO) >= 0) {
            n2 = n;
            n3 = 0;
        } else {
            n2 = 0;
            n3 = 0;
        }
        if (simpleBigDecimal8.compareTo(MINUS_ONE) >= 0) {
            if (simpleBigDecimal3.compareTo(MINUS_TWO) >= 0) return new ZTauElement(bigInteger.add(BigInteger.valueOf((long)n3)), bigInteger2.add(BigInteger.valueOf((long)n2)));
            n2 = (byte)(-n);
            return new ZTauElement(bigInteger.add(BigInteger.valueOf((long)n3)), bigInteger2.add(BigInteger.valueOf((long)n2)));
        }
        if (simpleBigDecimal4.compareTo(ECConstants.ONE) >= 0) {
            n2 = (byte)(-n);
            return new ZTauElement(bigInteger.add(BigInteger.valueOf((long)n3)), bigInteger2.add(BigInteger.valueOf((long)n2)));
        }
        n3 = -1;
        return new ZTauElement(bigInteger.add(BigInteger.valueOf((long)n3)), bigInteger2.add(BigInteger.valueOf((long)n2)));
    }

    public static ECPoint.F2m tau(ECPoint.F2m f2m) {
        return f2m.tau();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] tauAdicNaf(byte by, ZTauElement zTauElement) {
        if (by != 1 && by != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        int n = Tnaf.norm(by, zTauElement).bitLength();
        int n2 = n > 30 ? n + 4 : 34;
        byte[] arrby = new byte[n2];
        BigInteger bigInteger = zTauElement.u;
        BigInteger bigInteger2 = zTauElement.v;
        int n3 = 0;
        int n4 = 0;
        do {
            BigInteger bigInteger3;
            if (bigInteger.equals((Object)ECConstants.ZERO) && bigInteger2.equals((Object)ECConstants.ZERO)) {
                int n5 = n3 + 1;
                byte[] arrby2 = new byte[n5];
                System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n5);
                return arrby2;
            }
            if (bigInteger.testBit(0)) {
                arrby[n4] = (byte)ECConstants.TWO.subtract(bigInteger.subtract(bigInteger2.shiftLeft(1)).mod(ECConstants.FOUR)).intValue();
                BigInteger bigInteger4 = arrby[n4] == 1 ? bigInteger.clearBit(0) : bigInteger.add(ECConstants.ONE);
                bigInteger3 = bigInteger4;
                n3 = n4;
            } else {
                arrby[n4] = 0;
                bigInteger3 = bigInteger;
            }
            BigInteger bigInteger5 = bigInteger3.shiftRight(1);
            bigInteger = by == 1 ? bigInteger2.add(bigInteger5) : bigInteger2.subtract(bigInteger5);
            bigInteger2 = bigInteger3.shiftRight(1).negate();
            ++n4;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] tauAdicWNaf(byte by, ZTauElement zTauElement, byte by2, BigInteger bigInteger, BigInteger bigInteger2, ZTauElement[] arrzTauElement) {
        if (by != 1 && by != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        int n = Tnaf.norm(by, zTauElement).bitLength();
        int n2 = n > 30 ? by2 + (n + 4) : by2 + 34;
        byte[] arrby = new byte[n2];
        BigInteger bigInteger3 = bigInteger.shiftRight(1);
        BigInteger bigInteger4 = zTauElement.u;
        BigInteger bigInteger5 = zTauElement.v;
        BigInteger bigInteger6 = bigInteger4;
        BigInteger bigInteger7 = bigInteger5;
        int n3 = 0;
        while (!bigInteger6.equals((Object)ECConstants.ZERO) || !bigInteger7.equals((Object)ECConstants.ZERO)) {
            BigInteger bigInteger8;
            if (bigInteger6.testBit(0)) {
                byte by3;
                boolean bl;
                BigInteger bigInteger9;
                BigInteger bigInteger10 = bigInteger6.add(bigInteger7.multiply(bigInteger2)).mod(bigInteger);
                byte by4 = bigInteger10.compareTo(bigInteger3) >= 0 ? (byte)bigInteger10.subtract(bigInteger).intValue() : (byte)bigInteger10.intValue();
                arrby[n3] = by4;
                if (by4 < 0) {
                    by3 = -by4;
                    bl = false;
                } else {
                    by3 = by4;
                    bl = true;
                }
                if (bl) {
                    bigInteger9 = bigInteger6.subtract(arrzTauElement[by3].u);
                    bigInteger7 = bigInteger7.subtract(arrzTauElement[by3].v);
                } else {
                    bigInteger9 = bigInteger6.add(arrzTauElement[by3].u);
                    bigInteger7 = bigInteger7.add(arrzTauElement[by3].v);
                }
                bigInteger8 = bigInteger9;
            } else {
                arrby[n3] = 0;
                bigInteger8 = bigInteger6;
            }
            bigInteger6 = by == 1 ? bigInteger7.add(bigInteger8.shiftRight(1)) : bigInteger7.subtract(bigInteger8.shiftRight(1));
            bigInteger7 = bigInteger8.shiftRight(1).negate();
            ++n3;
        }
        return arrby;
    }
}

