package org.bouncycastle.math.ec.tools;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

public class DiscoverEndomorphisms {
    private static final int radix = 16;

    private static boolean areRelativelyPrime(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.gcd(bigInteger2).equals(ECConstants.ONE);
    }

    private static BigInteger[] calculateRange(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return order(bigInteger.subtract(bigInteger2).divide(bigInteger3), bigInteger.add(bigInteger2).divide(bigInteger3));
    }

    private static BigInteger[] chooseShortest(BigInteger[] bigIntegerArr, BigInteger[] bigIntegerArr2) {
        return isShorter(bigIntegerArr, bigIntegerArr2) ? bigIntegerArr : bigIntegerArr2;
    }

    private static void discoverEndomorphism(String str) {
        X9ECParameters byName = ECNamedCurveTable.getByName(str);
        if (byName == null) {
            System.err.println("Unknown curve: " + str);
            return;
        }
        ECCurve curve = byName.getCurve();
        if (ECAlgorithms.isFpCurve(curve)) {
            BigInteger characteristic = curve.getField().getCharacteristic();
            if (curve.getA().isZero() && characteristic.mod(ECConstants.THREE).equals(ECConstants.ONE)) {
                System.out.println("Curve '" + str + "' has a 'GLV Type B' endomorphism with these parameters: ");
                printGLVTypeBParameters(byName);
            }
        }
    }

    private static BigInteger[] extEuclidBezout(BigInteger[] bigIntegerArr) {
        int i = bigIntegerArr[0].compareTo(bigIntegerArr[1]) < 0 ? 1 : 0;
        if (i != 0) {
            swap(bigIntegerArr);
        }
        BigInteger bigInteger = bigIntegerArr[0];
        BigInteger bigInteger2 = bigIntegerArr[1];
        BigInteger bigInteger3 = ECConstants.ONE;
        BigInteger bigInteger4 = ECConstants.ZERO;
        BigInteger bigInteger5 = ECConstants.ZERO;
        BigInteger bigInteger6 = ECConstants.ONE;
        while (bigInteger2.compareTo(ECConstants.ONE) > 0) {
            BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(bigInteger2);
            BigInteger bigInteger7 = divideAndRemainder[0];
            bigInteger = divideAndRemainder[1];
            bigInteger3 = bigInteger3.subtract(bigInteger7.multiply(bigInteger4));
            BigInteger subtract = bigInteger5.subtract(bigInteger7.multiply(bigInteger6));
            bigInteger5 = bigInteger6;
            bigInteger6 = subtract;
            BigInteger bigInteger8 = bigInteger3;
            bigInteger3 = bigInteger4;
            bigInteger4 = bigInteger8;
            BigInteger bigInteger9 = bigInteger;
            bigInteger = bigInteger2;
            bigInteger2 = bigInteger9;
        }
        if (bigInteger2.signum() <= 0) {
            throw new IllegalStateException();
        }
        BigInteger[] bigIntegerArr2 = new BigInteger[]{bigInteger4, bigInteger6};
        if (i != 0) {
            swap(bigIntegerArr2);
        }
        return bigIntegerArr2;
    }

    private static BigInteger[] extEuclidGLV(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = ECConstants.ZERO;
        BigInteger bigInteger4 = ECConstants.ONE;
        BigInteger bigInteger5 = bigInteger3;
        BigInteger bigInteger6 = bigInteger;
        while (true) {
            BigInteger[] divideAndRemainder = bigInteger6.divideAndRemainder(bigInteger2);
            BigInteger bigInteger7 = divideAndRemainder[0];
            BigInteger bigInteger8 = divideAndRemainder[1];
            bigInteger3 = bigInteger5.subtract(bigInteger7.multiply(bigInteger4));
            if (isLessThanSqrt(bigInteger2, bigInteger)) {
                return new BigInteger[]{bigInteger6, bigInteger5, bigInteger2, bigInteger4, bigInteger8, bigInteger3};
            }
            bigInteger5 = bigInteger4;
            bigInteger6 = bigInteger2;
            bigInteger2 = bigInteger8;
            bigInteger4 = bigInteger3;
        }
    }

    private static BigInteger[] intersect(BigInteger[] bigIntegerArr, BigInteger[] bigIntegerArr2) {
        if (bigIntegerArr[0].max(bigIntegerArr2[0]).compareTo(bigIntegerArr[1].min(bigIntegerArr2[1])) > 0) {
            return null;
        }
        return new BigInteger[]{bigIntegerArr[0].max(bigIntegerArr2[0]), bigIntegerArr[1].min(bigIntegerArr2[1])};
    }

    private static boolean isLessThanSqrt(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        int bitLength = abs2.bitLength();
        int bitLength2 = abs.bitLength() * 2;
        return bitLength2 + -1 <= bitLength && (bitLength2 < bitLength || abs.multiply(abs).compareTo(abs2) < 0);
    }

    private static boolean isShorter(BigInteger[] bigIntegerArr, BigInteger[] bigIntegerArr2) {
        boolean z = true;
        BigInteger abs = bigIntegerArr[0].abs();
        BigInteger abs2 = bigIntegerArr[1].abs();
        BigInteger abs3 = bigIntegerArr2[0].abs();
        BigInteger abs4 = bigIntegerArr2[1].abs();
        boolean z2 = abs.compareTo(abs3) < 0;
        if (z2 == (abs2.compareTo(abs4) < 0)) {
            return z2;
        }
        if (abs.multiply(abs).add(abs2.multiply(abs2)).compareTo(abs3.multiply(abs3).add(abs4.multiply(abs4))) >= 0) {
            z = false;
        }
        return z;
    }

    private static boolean isVectorBoundedBySqrt(BigInteger[] bigIntegerArr, BigInteger bigInteger) {
        return isLessThanSqrt(bigIntegerArr[0].abs().max(bigIntegerArr[1].abs()), bigInteger);
    }

    private static BigInteger isqrt(BigInteger bigInteger) {
        BigInteger shiftRight = bigInteger.shiftRight(bigInteger.bitLength() / 2);
        while (true) {
            BigInteger shiftRight2 = shiftRight.add(bigInteger.divide(shiftRight)).shiftRight(1);
            if (shiftRight2.equals(shiftRight)) {
                return shiftRight2;
            }
            shiftRight = shiftRight2;
        }
    }

    public static void main(String[] strArr) {
        if (strArr.length < 1) {
            System.err.println("Expected a list of curve names as arguments");
            return;
        }
        for (String discoverEndomorphism : strArr) {
            discoverEndomorphism(discoverEndomorphism);
        }
    }

    private static BigInteger[] order(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.compareTo(bigInteger2) <= 0) {
            return new BigInteger[]{bigInteger, bigInteger2};
        }
        return new BigInteger[]{bigInteger2, bigInteger};
    }

    private static void printGLVTypeBParameters(X9ECParameters x9ECParameters) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        BigInteger n = x9ECParameters.getN();
        BigInteger solveQuadraticEquation = solveQuadraticEquation(n, ECConstants.ONE, ECConstants.ONE);
        BigInteger[] extEuclidGLV = extEuclidGLV(n, solveQuadraticEquation);
        BigInteger[] bigIntegerArr = new BigInteger[]{extEuclidGLV[2], extEuclidGLV[3].negate()};
        BigInteger[] chooseShortest = chooseShortest(new BigInteger[]{extEuclidGLV[0], extEuclidGLV[1].negate()}, new BigInteger[]{extEuclidGLV[4], extEuclidGLV[5].negate()});
        if (!isVectorBoundedBySqrt(chooseShortest, n) && areRelativelyPrime(bigIntegerArr[0], bigIntegerArr[1])) {
            bigInteger = bigIntegerArr[0];
            BigInteger bigInteger4 = bigIntegerArr[1];
            BigInteger divide = bigInteger.add(bigInteger4.multiply(solveQuadraticEquation)).divide(n);
            BigInteger[] extEuclidBezout = extEuclidBezout(new BigInteger[]{divide.abs(), bigInteger4.abs()});
            bigInteger2 = extEuclidBezout[0];
            bigInteger3 = extEuclidBezout[1];
            if (divide.signum() < 0) {
                bigInteger2 = bigInteger2.negate();
            }
            if (bigInteger4.signum() > 0) {
                bigInteger3 = bigInteger3.negate();
            }
            if (divide.multiply(bigInteger2).subtract(bigInteger4.multiply(bigInteger3)).equals(ECConstants.ONE)) {
                BigInteger subtract = bigInteger3.multiply(n).subtract(bigInteger2.multiply(solveQuadraticEquation));
                bigInteger3 = bigInteger2.negate();
                divide = subtract.negate();
                BigInteger add = isqrt(n.subtract(ECConstants.ONE)).add(ECConstants.ONE);
                BigInteger[] intersect = intersect(calculateRange(bigInteger3, add, bigInteger4), calculateRange(divide, add, bigInteger));
                if (intersect != null) {
                    for (bigInteger3 = intersect[0]; bigInteger3.compareTo(intersect[1]) <= 0; bigInteger3 = bigInteger3.add(ECConstants.ONE)) {
                        BigInteger[] bigIntegerArr2 = new BigInteger[]{subtract.add(bigInteger3.multiply(bigInteger)), bigInteger2.add(bigInteger3.multiply(bigInteger4))};
                        if (isShorter(bigIntegerArr2, chooseShortest)) {
                            chooseShortest = bigIntegerArr2;
                        }
                    }
                }
            } else {
                throw new IllegalStateException();
            }
        }
        ECPoint normalize = x9ECParameters.getG().normalize();
        ECPoint normalize2 = normalize.multiply(solveQuadraticEquation).normalize();
        if (normalize.getYCoord().equals(normalize2.getYCoord())) {
            bigInteger2 = x9ECParameters.getCurve().getField().getCharacteristic();
            bigInteger = bigInteger2.divide(ECConstants.THREE);
            do {
            } while (BigIntegers.createRandomInRange(ECConstants.TWO, bigInteger2.subtract(ECConstants.TWO), new SecureRandom()).modPow(bigInteger, bigInteger2).equals(ECConstants.ONE));
            ECFieldElement fromBigInteger = x9ECParameters.getCurve().fromBigInteger(ECConstants.TWO.modPow(bigInteger, bigInteger2));
            if (!normalize.getXCoord().multiply(fromBigInteger).equals(normalize2.getXCoord())) {
                fromBigInteger = fromBigInteger.square();
                if (!normalize.getXCoord().multiply(fromBigInteger).equals(normalize2.getXCoord())) {
                    throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
                }
            }
            bigInteger3 = bigIntegerArr[0].multiply(chooseShortest[1]).subtract(bigIntegerArr[1].multiply(chooseShortest[0]));
            int bitLength = (n.bitLength() + radix) - (n.bitLength() & 7);
            n = roundQuotient(chooseShortest[1].shiftLeft(bitLength), bigInteger3);
            bigInteger3 = roundQuotient(bigIntegerArr[1].shiftLeft(bitLength), bigInteger3).negate();
            printProperty("Beta", fromBigInteger.toBigInteger().toString(radix));
            printProperty("Lambda", solveQuadraticEquation.toString(radix));
            printProperty("v1", "{ " + bigIntegerArr[0].toString(radix) + ", " + bigIntegerArr[1].toString(radix) + " }");
            printProperty("v2", "{ " + chooseShortest[0].toString(radix) + ", " + chooseShortest[1].toString(radix) + " }");
            printProperty("(OPT) g1", n.toString(radix));
            printProperty("(OPT) g2", bigInteger3.toString(radix));
            printProperty("(OPT) bits", Integer.toString(bitLength));
            return;
        }
        throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
    }

    private static void printProperty(String str, Object obj) {
        StringBuffer stringBuffer = new StringBuffer("  ");
        stringBuffer.append(str);
        while (stringBuffer.length() < 20) {
            stringBuffer.append(' ');
        }
        stringBuffer.append("= ");
        stringBuffer.append(obj.toString());
        System.out.println(stringBuffer.toString());
    }

    private static BigInteger roundQuotient(BigInteger bigInteger, BigInteger bigInteger2) {
        int i = bigInteger.signum() != bigInteger2.signum() ? 1 : 0;
        BigInteger abs = bigInteger.abs();
        BigInteger abs2 = bigInteger2.abs();
        BigInteger divide = abs.add(abs2.shiftRight(1)).divide(abs2);
        return i != 0 ? divide.negate() : divide;
    }

    private static BigInteger solveQuadraticEquation(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        BigInteger toBigInteger = new Fp(bigInteger, bigInteger2.multiply(bigInteger2).subtract(bigInteger3.shiftLeft(2)).mod(bigInteger)).sqrt().toBigInteger();
        if (!toBigInteger.testBit(0)) {
            toBigInteger = bigInteger.subtract(toBigInteger);
        }
        return toBigInteger.shiftRight(1);
    }

    private static void swap(BigInteger[] bigIntegerArr) {
        BigInteger bigInteger = bigIntegerArr[0];
        bigIntegerArr[0] = bigIntegerArr[1];
        bigIntegerArr[1] = bigInteger;
    }
}
