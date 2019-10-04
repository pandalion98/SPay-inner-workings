/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.math.ec.tools;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.util.BigIntegers;

public class DiscoverEndomorphisms {
    private static final int radix = 16;

    private static boolean areRelativelyPrime(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.gcd(bigInteger2).equals((Object)ECConstants.ONE);
    }

    private static BigInteger[] calculateRange(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return DiscoverEndomorphisms.order(bigInteger.subtract(bigInteger2).divide(bigInteger3), bigInteger.add(bigInteger2).divide(bigInteger3));
    }

    private static BigInteger[] chooseShortest(BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2) {
        if (DiscoverEndomorphisms.isShorter(arrbigInteger, arrbigInteger2)) {
            return arrbigInteger;
        }
        return arrbigInteger2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void discoverEndomorphism(String string) {
        X9ECParameters x9ECParameters = ECNamedCurveTable.getByName(string);
        if (x9ECParameters == null) {
            System.err.println("Unknown curve: " + string);
            return;
        } else {
            ECCurve eCCurve = x9ECParameters.getCurve();
            if (!ECAlgorithms.isFpCurve(eCCurve)) return;
            {
                BigInteger bigInteger = eCCurve.getField().getCharacteristic();
                if (!eCCurve.getA().isZero() || !bigInteger.mod(ECConstants.THREE).equals((Object)ECConstants.ONE)) return;
                {
                    System.out.println("Curve '" + string + "' has a 'GLV Type B' endomorphism with these parameters: ");
                    DiscoverEndomorphisms.printGLVTypeBParameters(x9ECParameters);
                    return;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static BigInteger[] extEuclidBezout(BigInteger[] arrbigInteger) {
        boolean bl = arrbigInteger[0].compareTo(arrbigInteger[1]) < 0;
        if (bl) {
            DiscoverEndomorphisms.swap(arrbigInteger);
        }
        BigInteger bigInteger = arrbigInteger[0];
        BigInteger bigInteger2 = arrbigInteger[1];
        BigInteger bigInteger3 = ECConstants.ONE;
        BigInteger bigInteger4 = ECConstants.ZERO;
        BigInteger bigInteger5 = ECConstants.ZERO;
        BigInteger bigInteger6 = ECConstants.ONE;
        while (bigInteger2.compareTo(ECConstants.ONE) > 0) {
            BigInteger[] arrbigInteger2 = bigInteger.divideAndRemainder(bigInteger2);
            BigInteger bigInteger7 = arrbigInteger2[0];
            BigInteger bigInteger8 = arrbigInteger2[1];
            BigInteger bigInteger9 = bigInteger3.subtract(bigInteger7.multiply(bigInteger4));
            BigInteger bigInteger10 = bigInteger5.subtract(bigInteger7.multiply(bigInteger6));
            bigInteger5 = bigInteger6;
            bigInteger6 = bigInteger10;
            bigInteger3 = bigInteger4;
            bigInteger4 = bigInteger9;
            bigInteger = bigInteger2;
            bigInteger2 = bigInteger8;
        }
        if (bigInteger2.signum() <= 0) {
            throw new IllegalStateException();
        }
        BigInteger[] arrbigInteger3 = new BigInteger[]{bigInteger4, bigInteger6};
        if (bl) {
            DiscoverEndomorphisms.swap(arrbigInteger3);
        }
        return arrbigInteger3;
    }

    private static BigInteger[] extEuclidGLV(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = ECConstants.ZERO;
        BigInteger bigInteger4 = ECConstants.ONE;
        BigInteger bigInteger5 = bigInteger3;
        BigInteger bigInteger6 = bigInteger;
        do {
            BigInteger[] arrbigInteger = bigInteger6.divideAndRemainder(bigInteger2);
            BigInteger bigInteger7 = arrbigInteger[0];
            BigInteger bigInteger8 = arrbigInteger[1];
            BigInteger bigInteger9 = bigInteger5.subtract(bigInteger7.multiply(bigInteger4));
            if (DiscoverEndomorphisms.isLessThanSqrt(bigInteger2, bigInteger)) {
                return new BigInteger[]{bigInteger6, bigInteger5, bigInteger2, bigInteger4, bigInteger8, bigInteger9};
            }
            bigInteger5 = bigInteger4;
            bigInteger6 = bigInteger2;
            bigInteger2 = bigInteger8;
            bigInteger4 = bigInteger9;
        } while (true);
    }

    private static BigInteger[] intersect(BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2) {
        BigInteger bigInteger;
        BigInteger bigInteger2 = arrbigInteger[0].max(arrbigInteger2[0]);
        if (bigInteger2.compareTo(bigInteger = arrbigInteger[1].min(arrbigInteger2[1])) > 0) {
            return null;
        }
        return new BigInteger[]{bigInteger2, bigInteger};
    }

    private static boolean isLessThanSqrt(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = bigInteger.abs();
        BigInteger bigInteger4 = bigInteger2.abs();
        int n = bigInteger4.bitLength();
        int n2 = 2 * bigInteger3.bitLength();
        return n2 - 1 <= n && (n2 < n || bigInteger3.multiply(bigInteger3).compareTo(bigInteger4) < 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean isShorter(BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2) {
        boolean bl;
        boolean bl2 = true;
        BigInteger bigInteger = arrbigInteger[0].abs();
        BigInteger bigInteger2 = arrbigInteger[bl2].abs();
        BigInteger bigInteger3 = arrbigInteger2[0].abs();
        BigInteger bigInteger4 = arrbigInteger2[bl2].abs();
        boolean bl3 = bigInteger.compareTo(bigInteger3) < 0 ? bl2 : false;
        if (bl3 == (bl = bigInteger2.compareTo(bigInteger4) < 0 ? bl2 : false)) {
            return bl3;
        }
        if (bigInteger.multiply(bigInteger).add(bigInteger2.multiply(bigInteger2)).compareTo(bigInteger3.multiply(bigInteger3).add(bigInteger4.multiply(bigInteger4))) >= 0) return false;
        return bl2;
    }

    private static boolean isVectorBoundedBySqrt(BigInteger[] arrbigInteger, BigInteger bigInteger) {
        return DiscoverEndomorphisms.isLessThanSqrt(arrbigInteger[0].abs().max(arrbigInteger[1].abs()), bigInteger);
    }

    private static BigInteger isqrt(BigInteger bigInteger) {
        BigInteger bigInteger2 = bigInteger.shiftRight(bigInteger.bitLength() / 2);
        BigInteger bigInteger3;
        while (!(bigInteger3 = bigInteger2.add(bigInteger.divide(bigInteger2)).shiftRight(1)).equals((Object)bigInteger2)) {
            bigInteger2 = bigInteger3;
        }
        return bigInteger3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void main(String[] arrstring) {
        if (arrstring.length < 1) {
            System.err.println("Expected a list of curve names as arguments");
            return;
        } else {
            for (int i = 0; i < arrstring.length; ++i) {
                DiscoverEndomorphisms.discoverEndomorphism(arrstring[i]);
            }
        }
    }

    private static BigInteger[] order(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.compareTo(bigInteger2) <= 0) {
            return new BigInteger[]{bigInteger, bigInteger2};
        }
        return new BigInteger[]{bigInteger2, bigInteger};
    }

    private static void printGLVTypeBParameters(X9ECParameters x9ECParameters) {
        BigInteger bigInteger = x9ECParameters.getN();
        BigInteger bigInteger2 = DiscoverEndomorphisms.solveQuadraticEquation(bigInteger, ECConstants.ONE, ECConstants.ONE);
        BigInteger[] arrbigInteger = DiscoverEndomorphisms.extEuclidGLV(bigInteger, bigInteger2);
        BigInteger[] arrbigInteger2 = new BigInteger[]{arrbigInteger[2], arrbigInteger[3].negate()};
        BigInteger[] arrbigInteger3 = new BigInteger[]{arrbigInteger[0], arrbigInteger[1].negate()};
        BigInteger[] arrbigInteger4 = new BigInteger[]{arrbigInteger[4], arrbigInteger[5].negate()};
        BigInteger[] arrbigInteger5 = DiscoverEndomorphisms.chooseShortest(arrbigInteger3, arrbigInteger4);
        if (!DiscoverEndomorphisms.isVectorBoundedBySqrt(arrbigInteger5, bigInteger) && DiscoverEndomorphisms.areRelativelyPrime(arrbigInteger2[0], arrbigInteger2[1])) {
            BigInteger bigInteger3 = arrbigInteger2[0];
            BigInteger bigInteger4 = arrbigInteger2[1];
            BigInteger bigInteger5 = bigInteger3.add(bigInteger4.multiply(bigInteger2)).divide(bigInteger);
            BigInteger[] arrbigInteger6 = new BigInteger[]{bigInteger5.abs(), bigInteger4.abs()};
            BigInteger[] arrbigInteger7 = DiscoverEndomorphisms.extEuclidBezout(arrbigInteger6);
            BigInteger bigInteger6 = arrbigInteger7[0];
            BigInteger bigInteger7 = arrbigInteger7[1];
            if (bigInteger5.signum() < 0) {
                bigInteger6 = bigInteger6.negate();
            }
            if (bigInteger4.signum() > 0) {
                bigInteger7 = bigInteger7.negate();
            }
            if (!bigInteger5.multiply(bigInteger6).subtract(bigInteger4.multiply(bigInteger7)).equals((Object)ECConstants.ONE)) {
                throw new IllegalStateException();
            }
            BigInteger bigInteger8 = bigInteger7.multiply(bigInteger).subtract(bigInteger6.multiply(bigInteger2));
            BigInteger bigInteger9 = bigInteger6.negate();
            BigInteger bigInteger10 = bigInteger8.negate();
            BigInteger bigInteger11 = DiscoverEndomorphisms.isqrt(bigInteger.subtract(ECConstants.ONE)).add(ECConstants.ONE);
            BigInteger[] arrbigInteger8 = DiscoverEndomorphisms.intersect(DiscoverEndomorphisms.calculateRange(bigInteger9, bigInteger11, bigInteger4), DiscoverEndomorphisms.calculateRange(bigInteger10, bigInteger11, bigInteger3));
            if (arrbigInteger8 != null) {
                BigInteger bigInteger12 = arrbigInteger8[0];
                while (bigInteger12.compareTo(arrbigInteger8[1]) <= 0) {
                    BigInteger[] arrbigInteger9 = new BigInteger[]{bigInteger8.add(bigInteger12.multiply(bigInteger3)), bigInteger6.add(bigInteger12.multiply(bigInteger4))};
                    if (DiscoverEndomorphisms.isShorter(arrbigInteger9, arrbigInteger5)) {
                        arrbigInteger5 = arrbigInteger9;
                    }
                    bigInteger12 = bigInteger12.add(ECConstants.ONE);
                }
            }
        }
        ECPoint eCPoint = x9ECParameters.getG().normalize();
        ECPoint eCPoint2 = eCPoint.multiply(bigInteger2).normalize();
        if (!eCPoint.getYCoord().equals((Object)eCPoint2.getYCoord())) {
            throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
        }
        BigInteger bigInteger13 = x9ECParameters.getCurve().getField().getCharacteristic();
        BigInteger bigInteger14 = bigInteger13.divide(ECConstants.THREE);
        SecureRandom secureRandom = new SecureRandom();
        while (BigIntegers.createRandomInRange(ECConstants.TWO, bigInteger13.subtract(ECConstants.TWO), secureRandom).modPow(bigInteger14, bigInteger13).equals((Object)ECConstants.ONE)) {
        }
        ECFieldElement eCFieldElement = x9ECParameters.getCurve().fromBigInteger(ECConstants.TWO.modPow(bigInteger14, bigInteger13));
        if (!eCPoint.getXCoord().multiply(eCFieldElement).equals((Object)eCPoint2.getXCoord())) {
            eCFieldElement = eCFieldElement.square();
            if (!eCPoint.getXCoord().multiply(eCFieldElement).equals((Object)eCPoint2.getXCoord())) {
                throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
            }
        }
        BigInteger bigInteger15 = arrbigInteger2[0].multiply(arrbigInteger5[1]).subtract(arrbigInteger2[1].multiply(arrbigInteger5[0]));
        int n = 16 + bigInteger.bitLength() - (7 & bigInteger.bitLength());
        BigInteger bigInteger16 = DiscoverEndomorphisms.roundQuotient(arrbigInteger5[1].shiftLeft(n), bigInteger15);
        BigInteger bigInteger17 = DiscoverEndomorphisms.roundQuotient(arrbigInteger2[1].shiftLeft(n), bigInteger15).negate();
        DiscoverEndomorphisms.printProperty("Beta", eCFieldElement.toBigInteger().toString(16));
        DiscoverEndomorphisms.printProperty("Lambda", bigInteger2.toString(16));
        DiscoverEndomorphisms.printProperty("v1", "{ " + arrbigInteger2[0].toString(16) + ", " + arrbigInteger2[1].toString(16) + " }");
        DiscoverEndomorphisms.printProperty("v2", "{ " + arrbigInteger5[0].toString(16) + ", " + arrbigInteger5[1].toString(16) + " }");
        DiscoverEndomorphisms.printProperty("(OPT) g1", bigInteger16.toString(16));
        DiscoverEndomorphisms.printProperty("(OPT) g2", bigInteger17.toString(16));
        DiscoverEndomorphisms.printProperty("(OPT) bits", Integer.toString((int)n));
    }

    private static void printProperty(String string, Object object) {
        StringBuffer stringBuffer = new StringBuffer("  ");
        stringBuffer.append(string);
        while (stringBuffer.length() < 20) {
            stringBuffer.append(' ');
        }
        stringBuffer.append("= ");
        stringBuffer.append(object.toString());
        System.out.println(stringBuffer.toString());
    }

    /*
     * Enabled aggressive block sorting
     */
    private static BigInteger roundQuotient(BigInteger bigInteger, BigInteger bigInteger2) {
        boolean bl = bigInteger.signum() != bigInteger2.signum();
        BigInteger bigInteger3 = bigInteger.abs();
        BigInteger bigInteger4 = bigInteger2.abs();
        BigInteger bigInteger5 = bigInteger3.add(bigInteger4.shiftRight(1)).divide(bigInteger4);
        if (bl) {
            return bigInteger5.negate();
        }
        return bigInteger5;
    }

    private static BigInteger solveQuadraticEquation(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        BigInteger bigInteger4 = new ECFieldElement.Fp(bigInteger, bigInteger2.multiply(bigInteger2).subtract(bigInteger3.shiftLeft(2)).mod(bigInteger)).sqrt().toBigInteger();
        if (!bigInteger4.testBit(0)) {
            bigInteger4 = bigInteger.subtract(bigInteger4);
        }
        return bigInteger4.shiftRight(1);
    }

    private static void swap(BigInteger[] arrbigInteger) {
        BigInteger bigInteger = arrbigInteger[0];
        arrbigInteger[0] = arrbigInteger[1];
        arrbigInteger[1] = bigInteger;
    }
}

