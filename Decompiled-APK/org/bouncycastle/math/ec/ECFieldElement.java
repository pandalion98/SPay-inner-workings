package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public abstract class ECFieldElement implements ECConstants {

    public static class F2m extends ECFieldElement {
        public static final int GNB = 1;
        public static final int PPB = 3;
        public static final int TPB = 2;
        private int[] ks;
        private int f328m;
        private int representation;
        private LongArray f329x;

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger) {
            int[] iArr;
            if (i3 == 0 && i4 == 0) {
                this.representation = TPB;
                iArr = new int[GNB];
                iArr[0] = i2;
                this.ks = iArr;
            } else if (i3 >= i4) {
                throw new IllegalArgumentException("k2 must be smaller than k3");
            } else if (i3 <= 0) {
                throw new IllegalArgumentException("k2 must be larger than 0");
            } else {
                this.representation = PPB;
                iArr = new int[PPB];
                iArr[0] = i2;
                iArr[GNB] = i3;
                iArr[TPB] = i4;
                this.ks = iArr;
            }
            this.f328m = i;
            this.f329x = new LongArray(bigInteger);
        }

        public F2m(int i, int i2, BigInteger bigInteger) {
            this(i, i2, 0, 0, bigInteger);
        }

        private F2m(int i, int[] iArr, LongArray longArray) {
            this.f328m = i;
            this.representation = iArr.length == GNB ? TPB : PPB;
            this.ks = iArr;
            this.f329x = longArray;
        }

        public static void checkFieldElements(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            if ((eCFieldElement instanceof F2m) && (eCFieldElement2 instanceof F2m)) {
                F2m f2m = (F2m) eCFieldElement;
                F2m f2m2 = (F2m) eCFieldElement2;
                if (f2m.representation != f2m2.representation) {
                    throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
                } else if (f2m.f328m != f2m2.f328m || !Arrays.areEqual(f2m.ks, f2m2.ks)) {
                    throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
                } else {
                    return;
                }
            }
            throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
        }

        public ECFieldElement add(ECFieldElement eCFieldElement) {
            LongArray longArray = (LongArray) this.f329x.clone();
            longArray.addShiftedByWords(((F2m) eCFieldElement).f329x, 0);
            return new F2m(this.f328m, this.ks, longArray);
        }

        public ECFieldElement addOne() {
            return new F2m(this.f328m, this.ks, this.f329x.addOne());
        }

        public int bitLength() {
            return this.f329x.degree();
        }

        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return multiply(eCFieldElement.invert());
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof F2m)) {
                return false;
            }
            F2m f2m = (F2m) obj;
            return this.f328m == f2m.f328m && this.representation == f2m.representation && Arrays.areEqual(this.ks, f2m.ks) && this.f329x.equals(f2m.f329x);
        }

        public String getFieldName() {
            return "F2m";
        }

        public int getFieldSize() {
            return this.f328m;
        }

        public int getK1() {
            return this.ks[0];
        }

        public int getK2() {
            return this.ks.length >= TPB ? this.ks[GNB] : 0;
        }

        public int getK3() {
            return this.ks.length >= PPB ? this.ks[TPB] : 0;
        }

        public int getM() {
            return this.f328m;
        }

        public int getRepresentation() {
            return this.representation;
        }

        public int hashCode() {
            return (this.f329x.hashCode() ^ this.f328m) ^ Arrays.hashCode(this.ks);
        }

        public ECFieldElement invert() {
            return new F2m(this.f328m, this.ks, this.f329x.modInverse(this.f328m, this.ks));
        }

        public boolean isOne() {
            return this.f329x.isOne();
        }

        public boolean isZero() {
            return this.f329x.isZero();
        }

        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new F2m(this.f328m, this.ks, this.f329x.modMultiply(((F2m) eCFieldElement).f329x, this.f328m, this.ks));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            return multiplyPlusProduct(eCFieldElement, eCFieldElement2, eCFieldElement3);
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            LongArray longArray = this.f329x;
            LongArray longArray2 = ((F2m) eCFieldElement).f329x;
            LongArray longArray3 = ((F2m) eCFieldElement2).f329x;
            LongArray longArray4 = ((F2m) eCFieldElement3).f329x;
            LongArray multiply = longArray.multiply(longArray2, this.f328m, this.ks);
            longArray3 = longArray3.multiply(longArray4, this.f328m, this.ks);
            if (multiply == longArray || multiply == longArray2) {
                multiply = (LongArray) multiply.clone();
            }
            multiply.addShiftedByWords(longArray3, 0);
            multiply.reduce(this.f328m, this.ks);
            return new F2m(this.f328m, this.ks, multiply);
        }

        public ECFieldElement negate() {
            return this;
        }

        public ECFieldElement sqrt() {
            LongArray longArray = this.f329x;
            if (longArray.isOne() || longArray.isZero()) {
                return this;
            }
            return new F2m(this.f328m, this.ks, longArray.modSquareN(this.f328m - 1, this.f328m, this.ks));
        }

        public ECFieldElement square() {
            return new F2m(this.f328m, this.ks, this.f329x.modSquare(this.f328m, this.ks));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            return squarePlusProduct(eCFieldElement, eCFieldElement2);
        }

        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            LongArray longArray = this.f329x;
            LongArray longArray2 = ((F2m) eCFieldElement).f329x;
            LongArray longArray3 = ((F2m) eCFieldElement2).f329x;
            LongArray square = longArray.square(this.f328m, this.ks);
            longArray2 = longArray2.multiply(longArray3, this.f328m, this.ks);
            if (square == longArray) {
                square = (LongArray) square.clone();
            }
            square.addShiftedByWords(longArray2, 0);
            square.reduce(this.f328m, this.ks);
            return new F2m(this.f328m, this.ks, square);
        }

        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return add(eCFieldElement);
        }

        public boolean testBitZero() {
            return this.f329x.testBitZero();
        }

        public BigInteger toBigInteger() {
            return this.f329x.toBigInteger();
        }
    }

    public static class Fp extends ECFieldElement {
        BigInteger f330q;
        BigInteger f331r;
        BigInteger f332x;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2) {
            this(bigInteger, calculateResidue(bigInteger), bigInteger2);
        }

        Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            if (bigInteger3 == null || bigInteger3.signum() < 0 || bigInteger3.compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("x value invalid in Fp field element");
            }
            this.f330q = bigInteger;
            this.f331r = bigInteger2;
            this.f332x = bigInteger3;
        }

        static BigInteger calculateResidue(BigInteger bigInteger) {
            int bitLength = bigInteger.bitLength();
            return (bitLength < 96 || bigInteger.shiftRight(bitLength - 64).longValue() != -1) ? null : ONE.shiftLeft(bitLength).subtract(bigInteger);
        }

        private ECFieldElement checkSqrt(ECFieldElement eCFieldElement) {
            return eCFieldElement.square().equals(this) ? eCFieldElement : null;
        }

        private BigInteger[] lucasSequence(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            int bitLength = bigInteger3.bitLength();
            int lowestSetBit = bigInteger3.getLowestSetBit();
            BigInteger bigInteger4 = ECConstants.ONE;
            BigInteger bigInteger5 = ECConstants.TWO;
            BigInteger bigInteger6 = ECConstants.ONE;
            int i = bitLength - 1;
            BigInteger bigInteger7 = bigInteger;
            BigInteger bigInteger8 = bigInteger5;
            BigInteger bigInteger9 = ECConstants.ONE;
            BigInteger bigInteger10 = bigInteger6;
            while (i >= lowestSetBit + 1) {
                bigInteger10 = modMult(bigInteger10, bigInteger9);
                if (bigInteger3.testBit(i)) {
                    bigInteger9 = modMult(bigInteger10, bigInteger2);
                    bigInteger4 = modMult(bigInteger4, bigInteger7);
                    bigInteger5 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger10)));
                    bigInteger6 = modReduce(bigInteger7.multiply(bigInteger7).subtract(bigInteger9.shiftLeft(1)));
                } else {
                    bigInteger5 = modReduce(bigInteger4.multiply(bigInteger8).subtract(bigInteger10));
                    bigInteger9 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger10)));
                    bigInteger4 = bigInteger5;
                    bigInteger5 = modReduce(bigInteger8.multiply(bigInteger8).subtract(bigInteger10.shiftLeft(1)));
                    bigInteger6 = bigInteger9;
                    bigInteger9 = bigInteger10;
                }
                i--;
                bigInteger7 = bigInteger6;
                bigInteger8 = bigInteger5;
            }
            bigInteger9 = modMult(bigInteger10, bigInteger9);
            bigInteger5 = modMult(bigInteger9, bigInteger2);
            bigInteger6 = modReduce(bigInteger4.multiply(bigInteger8).subtract(bigInteger9));
            bigInteger10 = modReduce(bigInteger7.multiply(bigInteger8).subtract(bigInteger.multiply(bigInteger9)));
            bigInteger9 = modMult(bigInteger9, bigInteger5);
            bigInteger5 = bigInteger6;
            bigInteger6 = bigInteger10;
            bigInteger10 = bigInteger9;
            for (bitLength = 1; bitLength <= lowestSetBit; bitLength++) {
                bigInteger5 = modMult(bigInteger5, bigInteger6);
                bigInteger6 = modReduce(bigInteger6.multiply(bigInteger6).subtract(bigInteger10.shiftLeft(1)));
                bigInteger10 = modMult(bigInteger10, bigInteger10);
            }
            return new BigInteger[]{bigInteger5, bigInteger6};
        }

        public ECFieldElement add(ECFieldElement eCFieldElement) {
            return new Fp(this.f330q, this.f331r, modAdd(this.f332x, eCFieldElement.toBigInteger()));
        }

        public ECFieldElement addOne() {
            BigInteger add = this.f332x.add(ECConstants.ONE);
            if (add.compareTo(this.f330q) == 0) {
                add = ECConstants.ZERO;
            }
            return new Fp(this.f330q, this.f331r, add);
        }

        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return new Fp(this.f330q, this.f331r, modMult(this.f332x, modInverse(eCFieldElement.toBigInteger())));
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Fp)) {
                return false;
            }
            Fp fp = (Fp) obj;
            return this.f330q.equals(fp.f330q) && this.f332x.equals(fp.f332x);
        }

        public String getFieldName() {
            return "Fp";
        }

        public int getFieldSize() {
            return this.f330q.bitLength();
        }

        public BigInteger getQ() {
            return this.f330q;
        }

        public int hashCode() {
            return this.f330q.hashCode() ^ this.f332x.hashCode();
        }

        public ECFieldElement invert() {
            return new Fp(this.f330q, this.f331r, modInverse(this.f332x));
        }

        protected BigInteger modAdd(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger add = bigInteger.add(bigInteger2);
            return add.compareTo(this.f330q) >= 0 ? add.subtract(this.f330q) : add;
        }

        protected BigInteger modDouble(BigInteger bigInteger) {
            BigInteger shiftLeft = bigInteger.shiftLeft(1);
            return shiftLeft.compareTo(this.f330q) >= 0 ? shiftLeft.subtract(this.f330q) : shiftLeft;
        }

        protected BigInteger modHalf(BigInteger bigInteger) {
            if (bigInteger.testBit(0)) {
                bigInteger = this.f330q.add(bigInteger);
            }
            return bigInteger.shiftRight(1);
        }

        protected BigInteger modHalfAbs(BigInteger bigInteger) {
            if (bigInteger.testBit(0)) {
                bigInteger = this.f330q.subtract(bigInteger);
            }
            return bigInteger.shiftRight(1);
        }

        protected BigInteger modInverse(BigInteger bigInteger) {
            int fieldSize = getFieldSize();
            int i = (fieldSize + 31) >> 5;
            int[] fromBigInteger = Nat.fromBigInteger(fieldSize, this.f330q);
            int[] fromBigInteger2 = Nat.fromBigInteger(fieldSize, bigInteger);
            int[] create = Nat.create(i);
            Mod.invert(fromBigInteger, fromBigInteger2, create);
            return Nat.toBigInteger(i, create);
        }

        protected BigInteger modMult(BigInteger bigInteger, BigInteger bigInteger2) {
            return modReduce(bigInteger.multiply(bigInteger2));
        }

        protected BigInteger modReduce(BigInteger bigInteger) {
            if (this.f331r == null) {
                return bigInteger.mod(this.f330q);
            }
            Object obj = bigInteger.signum() < 0 ? 1 : null;
            if (obj != null) {
                bigInteger = bigInteger.abs();
            }
            int bitLength = this.f330q.bitLength();
            boolean equals = this.f331r.equals(ECConstants.ONE);
            while (bigInteger.bitLength() > bitLength + 1) {
                BigInteger shiftRight = bigInteger.shiftRight(bitLength);
                BigInteger subtract = bigInteger.subtract(shiftRight.shiftLeft(bitLength));
                if (!equals) {
                    shiftRight = shiftRight.multiply(this.f331r);
                }
                bigInteger = shiftRight.add(subtract);
            }
            while (bigInteger.compareTo(this.f330q) >= 0) {
                bigInteger = bigInteger.subtract(this.f330q);
            }
            return (obj == null || bigInteger.signum() == 0) ? bigInteger : this.f330q.subtract(bigInteger);
        }

        protected BigInteger modSubtract(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger subtract = bigInteger.subtract(bigInteger2);
            return subtract.signum() < 0 ? subtract.add(this.f330q) : subtract;
        }

        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new Fp(this.f330q, this.f331r, modMult(this.f332x, eCFieldElement.toBigInteger()));
        }

        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.f332x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            BigInteger toBigInteger3 = eCFieldElement3.toBigInteger();
            return new Fp(this.f330q, this.f331r, modReduce(bigInteger.multiply(toBigInteger).subtract(toBigInteger2.multiply(toBigInteger3))));
        }

        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.f332x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            BigInteger toBigInteger3 = eCFieldElement3.toBigInteger();
            return new Fp(this.f330q, this.f331r, modReduce(bigInteger.multiply(toBigInteger).add(toBigInteger2.multiply(toBigInteger3))));
        }

        public ECFieldElement negate() {
            return this.f332x.signum() == 0 ? this : new Fp(this.f330q, this.f331r, this.f330q.subtract(this.f332x));
        }

        public ECFieldElement sqrt() {
            if (isZero() || isOne()) {
                return this;
            }
            if (!this.f330q.testBit(0)) {
                throw new RuntimeException("not done yet");
            } else if (this.f330q.testBit(1)) {
                return checkSqrt(new Fp(this.f330q, this.f331r, this.f332x.modPow(this.f330q.shiftRight(2).add(ECConstants.ONE), this.f330q)));
            } else if (this.f330q.testBit(2)) {
                BigInteger modPow = this.f332x.modPow(this.f330q.shiftRight(3), this.f330q);
                r1 = modMult(modPow, this.f332x);
                if (modMult(r1, modPow).equals(ECConstants.ONE)) {
                    return checkSqrt(new Fp(this.f330q, this.f331r, r1));
                }
                return checkSqrt(new Fp(this.f330q, this.f331r, modMult(r1, ECConstants.TWO.modPow(this.f330q.shiftRight(2), this.f330q))));
            } else {
                r1 = this.f330q.shiftRight(1);
                if (!this.f332x.modPow(r1, this.f330q).equals(ECConstants.ONE)) {
                    return null;
                }
                BigInteger bigInteger = this.f332x;
                BigInteger modDouble = modDouble(modDouble(bigInteger));
                BigInteger add = r1.add(ECConstants.ONE);
                BigInteger subtract = this.f330q.subtract(ECConstants.ONE);
                Random random = new Random();
                while (true) {
                    BigInteger bigInteger2 = new BigInteger(this.f330q.bitLength(), random);
                    if (bigInteger2.compareTo(this.f330q) < 0 && modReduce(bigInteger2.multiply(bigInteger2).subtract(modDouble)).modPow(r1, this.f330q).equals(subtract)) {
                        BigInteger[] lucasSequence = lucasSequence(bigInteger2, bigInteger, add);
                        BigInteger bigInteger3 = lucasSequence[0];
                        bigInteger2 = lucasSequence[1];
                        if (modMult(bigInteger2, bigInteger2).equals(modDouble)) {
                            return new Fp(this.f330q, this.f331r, modHalfAbs(bigInteger2));
                        }
                        if (!(bigInteger3.equals(ECConstants.ONE) || bigInteger3.equals(subtract))) {
                            return null;
                        }
                    }
                }
            }
        }

        public ECFieldElement square() {
            return new Fp(this.f330q, this.f331r, modMult(this.f332x, this.f332x));
        }

        public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            BigInteger bigInteger = this.f332x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            return new Fp(this.f330q, this.f331r, modReduce(bigInteger.multiply(bigInteger).subtract(toBigInteger.multiply(toBigInteger2))));
        }

        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            BigInteger bigInteger = this.f332x;
            BigInteger toBigInteger = eCFieldElement.toBigInteger();
            BigInteger toBigInteger2 = eCFieldElement2.toBigInteger();
            return new Fp(this.f330q, this.f331r, modReduce(bigInteger.multiply(bigInteger).add(toBigInteger.multiply(toBigInteger2))));
        }

        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return new Fp(this.f330q, this.f331r, modSubtract(this.f332x, eCFieldElement.toBigInteger()));
        }

        public BigInteger toBigInteger() {
            return this.f332x;
        }
    }

    public abstract ECFieldElement add(ECFieldElement eCFieldElement);

    public abstract ECFieldElement addOne();

    public int bitLength() {
        return toBigInteger().bitLength();
    }

    public abstract ECFieldElement divide(ECFieldElement eCFieldElement);

    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((getFieldSize() + 7) / 8, toBigInteger());
    }

    public abstract String getFieldName();

    public abstract int getFieldSize();

    public abstract ECFieldElement invert();

    public boolean isOne() {
        return bitLength() == 1;
    }

    public boolean isZero() {
        return toBigInteger().signum() == 0;
    }

    public abstract ECFieldElement multiply(ECFieldElement eCFieldElement);

    public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return multiply(eCFieldElement).subtract(eCFieldElement2.multiply(eCFieldElement3));
    }

    public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return multiply(eCFieldElement).add(eCFieldElement2.multiply(eCFieldElement3));
    }

    public abstract ECFieldElement negate();

    public abstract ECFieldElement sqrt();

    public abstract ECFieldElement square();

    public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return square().subtract(eCFieldElement.multiply(eCFieldElement2));
    }

    public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return square().add(eCFieldElement.multiply(eCFieldElement2));
    }

    public abstract ECFieldElement subtract(ECFieldElement eCFieldElement);

    public boolean testBitZero() {
        return toBigInteger().testBit(0);
    }

    public abstract BigInteger toBigInteger();

    public String toString() {
        return toBigInteger().toString(16);
    }
}
