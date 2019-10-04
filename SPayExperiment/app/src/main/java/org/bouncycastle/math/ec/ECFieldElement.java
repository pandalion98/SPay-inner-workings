/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Random
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.LongArray;
import org.bouncycastle.math.raw.Mod;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

public abstract class ECFieldElement
implements ECConstants {
    public abstract ECFieldElement add(ECFieldElement var1);

    public abstract ECFieldElement addOne();

    public int bitLength() {
        return this.toBigInteger().bitLength();
    }

    public abstract ECFieldElement divide(ECFieldElement var1);

    public byte[] getEncoded() {
        return BigIntegers.asUnsignedByteArray((7 + this.getFieldSize()) / 8, this.toBigInteger());
    }

    public abstract String getFieldName();

    public abstract int getFieldSize();

    public abstract ECFieldElement invert();

    public boolean isOne() {
        return this.bitLength() == 1;
    }

    public boolean isZero() {
        return this.toBigInteger().signum() == 0;
    }

    public abstract ECFieldElement multiply(ECFieldElement var1);

    public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return this.multiply(eCFieldElement).subtract(eCFieldElement2.multiply(eCFieldElement3));
    }

    public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
        return this.multiply(eCFieldElement).add(eCFieldElement2.multiply(eCFieldElement3));
    }

    public abstract ECFieldElement negate();

    public abstract ECFieldElement sqrt();

    public abstract ECFieldElement square();

    public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return this.square().subtract(eCFieldElement.multiply(eCFieldElement2));
    }

    public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return this.square().add(eCFieldElement.multiply(eCFieldElement2));
    }

    public abstract ECFieldElement subtract(ECFieldElement var1);

    public boolean testBitZero() {
        return this.toBigInteger().testBit(0);
    }

    public abstract BigInteger toBigInteger();

    public String toString() {
        return this.toBigInteger().toString(16);
    }

    public static class F2m
    extends ECFieldElement {
        public static final int GNB = 1;
        public static final int PPB = 3;
        public static final int TPB = 2;
        private int[] ks;
        private int m;
        private int representation;
        private LongArray x;

        /*
         * Enabled aggressive block sorting
         */
        public F2m(int n, int n2, int n3, int n4, BigInteger bigInteger) {
            if (n3 == 0 && n4 == 0) {
                this.representation = 2;
                this.ks = new int[]{n2};
            } else {
                if (n3 >= n4) {
                    throw new IllegalArgumentException("k2 must be smaller than k3");
                }
                if (n3 <= 0) {
                    throw new IllegalArgumentException("k2 must be larger than 0");
                }
                this.representation = 3;
                this.ks = new int[]{n2, n3, n4};
            }
            this.m = n;
            this.x = new LongArray(bigInteger);
        }

        public F2m(int n, int n2, BigInteger bigInteger) {
            this(n, n2, 0, 0, bigInteger);
        }

        /*
         * Enabled aggressive block sorting
         */
        private F2m(int n, int[] arrn, LongArray longArray) {
            this.m = n;
            int n2 = arrn.length == 1 ? 2 : 3;
            this.representation = n2;
            this.ks = arrn;
            this.x = longArray;
        }

        public static void checkFieldElements(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            if (!(eCFieldElement instanceof F2m) || !(eCFieldElement2 instanceof F2m)) {
                throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
            }
            F2m f2m = (F2m)eCFieldElement;
            F2m f2m2 = (F2m)eCFieldElement2;
            if (f2m.representation != f2m2.representation) {
                throw new IllegalArgumentException("One of the F2m field elements has incorrect representation");
            }
            if (f2m.m != f2m2.m || !Arrays.areEqual(f2m.ks, f2m2.ks)) {
                throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
            }
        }

        @Override
        public ECFieldElement add(ECFieldElement eCFieldElement) {
            LongArray longArray = (LongArray)this.x.clone();
            longArray.addShiftedByWords(((F2m)eCFieldElement).x, 0);
            return new F2m(this.m, this.ks, longArray);
        }

        @Override
        public ECFieldElement addOne() {
            return new F2m(this.m, this.ks, this.x.addOne());
        }

        @Override
        public int bitLength() {
            return this.x.degree();
        }

        @Override
        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return this.multiply(eCFieldElement.invert());
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block5 : {
                block4 : {
                    if (object == this) break block4;
                    if (!(object instanceof F2m)) {
                        return false;
                    }
                    F2m f2m = (F2m)object;
                    if (this.m != f2m.m || this.representation != f2m.representation || !Arrays.areEqual(this.ks, f2m.ks) || !this.x.equals(f2m.x)) break block5;
                }
                return true;
            }
            return false;
        }

        @Override
        public String getFieldName() {
            return "F2m";
        }

        @Override
        public int getFieldSize() {
            return this.m;
        }

        public int getK1() {
            return this.ks[0];
        }

        public int getK2() {
            if (this.ks.length >= 2) {
                return this.ks[1];
            }
            return 0;
        }

        public int getK3() {
            if (this.ks.length >= 3) {
                return this.ks[2];
            }
            return 0;
        }

        public int getM() {
            return this.m;
        }

        public int getRepresentation() {
            return this.representation;
        }

        public int hashCode() {
            return this.x.hashCode() ^ this.m ^ Arrays.hashCode(this.ks);
        }

        @Override
        public ECFieldElement invert() {
            return new F2m(this.m, this.ks, this.x.modInverse(this.m, this.ks));
        }

        @Override
        public boolean isOne() {
            return this.x.isOne();
        }

        @Override
        public boolean isZero() {
            return this.x.isZero();
        }

        @Override
        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new F2m(this.m, this.ks, this.x.modMultiply(((F2m)eCFieldElement).x, this.m, this.ks));
        }

        @Override
        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            return this.multiplyPlusProduct(eCFieldElement, eCFieldElement2, eCFieldElement3);
        }

        @Override
        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            LongArray longArray = this.x;
            LongArray longArray2 = ((F2m)eCFieldElement).x;
            LongArray longArray3 = ((F2m)eCFieldElement2).x;
            LongArray longArray4 = ((F2m)eCFieldElement3).x;
            LongArray longArray5 = longArray.multiply(longArray2, this.m, this.ks);
            LongArray longArray6 = longArray3.multiply(longArray4, this.m, this.ks);
            if (longArray5 == longArray || longArray5 == longArray2) {
                longArray5 = (LongArray)longArray5.clone();
            }
            longArray5.addShiftedByWords(longArray6, 0);
            longArray5.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, longArray5);
        }

        @Override
        public ECFieldElement negate() {
            return this;
        }

        @Override
        public ECFieldElement sqrt() {
            LongArray longArray = this.x;
            if (longArray.isOne() || longArray.isZero()) {
                return this;
            }
            LongArray longArray2 = longArray.modSquareN(-1 + this.m, this.m, this.ks);
            return new F2m(this.m, this.ks, longArray2);
        }

        @Override
        public ECFieldElement square() {
            return new F2m(this.m, this.ks, this.x.modSquare(this.m, this.ks));
        }

        @Override
        public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            return this.squarePlusProduct(eCFieldElement, eCFieldElement2);
        }

        @Override
        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            LongArray longArray = this.x;
            LongArray longArray2 = ((F2m)eCFieldElement).x;
            LongArray longArray3 = ((F2m)eCFieldElement2).x;
            LongArray longArray4 = longArray.square(this.m, this.ks);
            LongArray longArray5 = longArray2.multiply(longArray3, this.m, this.ks);
            if (longArray4 == longArray) {
                longArray4 = (LongArray)longArray4.clone();
            }
            longArray4.addShiftedByWords(longArray5, 0);
            longArray4.reduce(this.m, this.ks);
            return new F2m(this.m, this.ks, longArray4);
        }

        @Override
        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return this.add(eCFieldElement);
        }

        @Override
        public boolean testBitZero() {
            return this.x.testBitZero();
        }

        @Override
        public BigInteger toBigInteger() {
            return this.x.toBigInteger();
        }
    }

    public static class Fp
    extends ECFieldElement {
        BigInteger q;
        BigInteger r;
        BigInteger x;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2) {
            this(bigInteger, Fp.calculateResidue(bigInteger), bigInteger2);
        }

        Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            if (bigInteger3 == null || bigInteger3.signum() < 0 || bigInteger3.compareTo(bigInteger) >= 0) {
                throw new IllegalArgumentException("x value invalid in Fp field element");
            }
            this.q = bigInteger;
            this.r = bigInteger2;
            this.x = bigInteger3;
        }

        static BigInteger calculateResidue(BigInteger bigInteger) {
            int n = bigInteger.bitLength();
            if (n >= 96 && bigInteger.shiftRight(n - 64).longValue() == -1L) {
                return ONE.shiftLeft(n).subtract(bigInteger);
            }
            return null;
        }

        private ECFieldElement checkSqrt(ECFieldElement eCFieldElement) {
            if (eCFieldElement.square().equals((Object)this)) {
                return eCFieldElement;
            }
            return null;
        }

        /*
         * Enabled aggressive block sorting
         */
        private BigInteger[] lucasSequence(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            int n = bigInteger3.bitLength();
            int n2 = bigInteger3.getLowestSetBit();
            BigInteger bigInteger4 = ECConstants.ONE;
            BigInteger bigInteger5 = ECConstants.TWO;
            BigInteger bigInteger6 = ECConstants.ONE;
            BigInteger bigInteger7 = ECConstants.ONE;
            BigInteger bigInteger8 = bigInteger;
            BigInteger bigInteger9 = bigInteger5;
            BigInteger bigInteger10 = bigInteger7;
            BigInteger bigInteger11 = bigInteger6;
            for (int i = n - 1; i >= n2 + 1; --i) {
                BigInteger bigInteger12;
                BigInteger bigInteger13;
                bigInteger11 = this.modMult(bigInteger11, bigInteger10);
                if (bigInteger3.testBit(i)) {
                    bigInteger10 = this.modMult(bigInteger11, bigInteger2);
                    bigInteger4 = this.modMult(bigInteger4, bigInteger8);
                    bigInteger13 = this.modReduce(bigInteger8.multiply(bigInteger9).subtract(bigInteger.multiply(bigInteger11)));
                    bigInteger12 = this.modReduce(bigInteger8.multiply(bigInteger8).subtract(bigInteger10.shiftLeft(1)));
                } else {
                    BigInteger bigInteger14 = this.modReduce(bigInteger4.multiply(bigInteger9).subtract(bigInteger11));
                    BigInteger bigInteger15 = this.modReduce(bigInteger8.multiply(bigInteger9).subtract(bigInteger.multiply(bigInteger11)));
                    BigInteger bigInteger16 = this.modReduce(bigInteger9.multiply(bigInteger9).subtract(bigInteger11.shiftLeft(1)));
                    bigInteger4 = bigInteger14;
                    bigInteger13 = bigInteger16;
                    bigInteger12 = bigInteger15;
                    bigInteger10 = bigInteger11;
                }
                bigInteger8 = bigInteger12;
                bigInteger9 = bigInteger13;
            }
            BigInteger bigInteger17 = this.modMult(bigInteger11, bigInteger10);
            BigInteger bigInteger18 = this.modMult(bigInteger17, bigInteger2);
            BigInteger bigInteger19 = this.modReduce(bigInteger4.multiply(bigInteger9).subtract(bigInteger17));
            BigInteger bigInteger20 = this.modReduce(bigInteger8.multiply(bigInteger9).subtract(bigInteger.multiply(bigInteger17)));
            BigInteger bigInteger21 = this.modMult(bigInteger17, bigInteger18);
            BigInteger bigInteger22 = bigInteger19;
            BigInteger bigInteger23 = bigInteger20;
            BigInteger bigInteger24 = bigInteger21;
            int n3 = 1;
            while (n3 <= n2) {
                bigInteger22 = this.modMult(bigInteger22, bigInteger23);
                bigInteger23 = this.modReduce(bigInteger23.multiply(bigInteger23).subtract(bigInteger24.shiftLeft(1)));
                bigInteger24 = this.modMult(bigInteger24, bigInteger24);
                ++n3;
            }
            return new BigInteger[]{bigInteger22, bigInteger23};
        }

        @Override
        public ECFieldElement add(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, this.modAdd(this.x, eCFieldElement.toBigInteger()));
        }

        @Override
        public ECFieldElement addOne() {
            BigInteger bigInteger = this.x.add(ECConstants.ONE);
            if (bigInteger.compareTo(this.q) == 0) {
                bigInteger = ECConstants.ZERO;
            }
            return new Fp(this.q, this.r, bigInteger);
        }

        @Override
        public ECFieldElement divide(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, this.modMult(this.x, this.modInverse(eCFieldElement.toBigInteger())));
        }

        /*
         * Enabled aggressive block sorting
         */
        public boolean equals(Object object) {
            block5 : {
                block4 : {
                    if (object == this) break block4;
                    if (!(object instanceof Fp)) {
                        return false;
                    }
                    Fp fp = (Fp)object;
                    if (!this.q.equals((Object)fp.q) || !this.x.equals((Object)fp.x)) break block5;
                }
                return true;
            }
            return false;
        }

        @Override
        public String getFieldName() {
            return "Fp";
        }

        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }

        public BigInteger getQ() {
            return this.q;
        }

        public int hashCode() {
            return this.q.hashCode() ^ this.x.hashCode();
        }

        @Override
        public ECFieldElement invert() {
            return new Fp(this.q, this.r, this.modInverse(this.x));
        }

        protected BigInteger modAdd(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger bigInteger3 = bigInteger.add(bigInteger2);
            if (bigInteger3.compareTo(this.q) >= 0) {
                bigInteger3 = bigInteger3.subtract(this.q);
            }
            return bigInteger3;
        }

        protected BigInteger modDouble(BigInteger bigInteger) {
            BigInteger bigInteger2 = bigInteger.shiftLeft(1);
            if (bigInteger2.compareTo(this.q) >= 0) {
                bigInteger2 = bigInteger2.subtract(this.q);
            }
            return bigInteger2;
        }

        protected BigInteger modHalf(BigInteger bigInteger) {
            if (bigInteger.testBit(0)) {
                bigInteger = this.q.add(bigInteger);
            }
            return bigInteger.shiftRight(1);
        }

        protected BigInteger modHalfAbs(BigInteger bigInteger) {
            if (bigInteger.testBit(0)) {
                bigInteger = this.q.subtract(bigInteger);
            }
            return bigInteger.shiftRight(1);
        }

        protected BigInteger modInverse(BigInteger bigInteger) {
            int n = this.getFieldSize();
            int n2 = n + 31 >> 5;
            int[] arrn = Nat.fromBigInteger(n, this.q);
            int[] arrn2 = Nat.fromBigInteger(n, bigInteger);
            int[] arrn3 = Nat.create(n2);
            Mod.invert(arrn, arrn2, arrn3);
            return Nat.toBigInteger(n2, arrn3);
        }

        protected BigInteger modMult(BigInteger bigInteger, BigInteger bigInteger2) {
            return this.modReduce(bigInteger.multiply(bigInteger2));
        }

        /*
         * Enabled aggressive block sorting
         */
        protected BigInteger modReduce(BigInteger bigInteger) {
            if (this.r == null) {
                return bigInteger.mod(this.q);
            }
            boolean bl = bigInteger.signum() < 0;
            if (bl) {
                bigInteger = bigInteger.abs();
            }
            int n = this.q.bitLength();
            boolean bl2 = this.r.equals((Object)ECConstants.ONE);
            while (bigInteger.bitLength() > n + 1) {
                BigInteger bigInteger2 = bigInteger.shiftRight(n);
                BigInteger bigInteger3 = bigInteger.subtract(bigInteger2.shiftLeft(n));
                if (!bl2) {
                    bigInteger2 = bigInteger2.multiply(this.r);
                }
                bigInteger = bigInteger2.add(bigInteger3);
            }
            while (bigInteger.compareTo(this.q) >= 0) {
                bigInteger = bigInteger.subtract(this.q);
            }
            if (!bl) return bigInteger;
            if (bigInteger.signum() == 0) return bigInteger;
            return this.q.subtract(bigInteger);
        }

        protected BigInteger modSubtract(BigInteger bigInteger, BigInteger bigInteger2) {
            BigInteger bigInteger3 = bigInteger.subtract(bigInteger2);
            if (bigInteger3.signum() < 0) {
                bigInteger3 = bigInteger3.add(this.q);
            }
            return bigInteger3;
        }

        @Override
        public ECFieldElement multiply(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, this.modMult(this.x, eCFieldElement.toBigInteger()));
        }

        @Override
        public ECFieldElement multiplyMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.x;
            BigInteger bigInteger2 = eCFieldElement.toBigInteger();
            BigInteger bigInteger3 = eCFieldElement2.toBigInteger();
            BigInteger bigInteger4 = eCFieldElement3.toBigInteger();
            BigInteger bigInteger5 = bigInteger.multiply(bigInteger2);
            BigInteger bigInteger6 = bigInteger3.multiply(bigInteger4);
            return new Fp(this.q, this.r, this.modReduce(bigInteger5.subtract(bigInteger6)));
        }

        @Override
        public ECFieldElement multiplyPlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3) {
            BigInteger bigInteger = this.x;
            BigInteger bigInteger2 = eCFieldElement.toBigInteger();
            BigInteger bigInteger3 = eCFieldElement2.toBigInteger();
            BigInteger bigInteger4 = eCFieldElement3.toBigInteger();
            BigInteger bigInteger5 = bigInteger.multiply(bigInteger2);
            BigInteger bigInteger6 = bigInteger3.multiply(bigInteger4);
            return new Fp(this.q, this.r, this.modReduce(bigInteger5.add(bigInteger6)));
        }

        @Override
        public ECFieldElement negate() {
            if (this.x.signum() == 0) {
                return this;
            }
            return new Fp(this.q, this.r, this.q.subtract(this.x));
        }

        @Override
        public ECFieldElement sqrt() {
            if (this.isZero() || this.isOne()) {
                return this;
            }
            if (!this.q.testBit(0)) {
                throw new RuntimeException("not done yet");
            }
            if (this.q.testBit(1)) {
                BigInteger bigInteger = this.q.shiftRight(2).add(ECConstants.ONE);
                return this.checkSqrt(new Fp(this.q, this.r, this.x.modPow(bigInteger, this.q)));
            }
            if (this.q.testBit(2)) {
                BigInteger bigInteger = this.x.modPow(this.q.shiftRight(3), this.q);
                BigInteger bigInteger2 = this.modMult(bigInteger, this.x);
                if (this.modMult(bigInteger2, bigInteger).equals((Object)ECConstants.ONE)) {
                    return this.checkSqrt(new Fp(this.q, this.r, bigInteger2));
                }
                BigInteger bigInteger3 = this.modMult(bigInteger2, ECConstants.TWO.modPow(this.q.shiftRight(2), this.q));
                return this.checkSqrt(new Fp(this.q, this.r, bigInteger3));
            }
            BigInteger bigInteger = this.q.shiftRight(1);
            if (!this.x.modPow(bigInteger, this.q).equals((Object)ECConstants.ONE)) {
                return null;
            }
            BigInteger bigInteger4 = this.x;
            BigInteger bigInteger5 = this.modDouble(this.modDouble(bigInteger4));
            BigInteger bigInteger6 = bigInteger.add(ECConstants.ONE);
            BigInteger bigInteger7 = this.q.subtract(ECConstants.ONE);
            Random random = new Random();
            do {
                BigInteger bigInteger8;
                if ((bigInteger8 = new BigInteger(this.q.bitLength(), random)).compareTo(this.q) >= 0 || !this.modReduce(bigInteger8.multiply(bigInteger8).subtract(bigInteger5)).modPow(bigInteger, this.q).equals((Object)bigInteger7)) {
                    continue;
                }
                BigInteger[] arrbigInteger = this.lucasSequence(bigInteger8, bigInteger4, bigInteger6);
                BigInteger bigInteger9 = arrbigInteger[0];
                BigInteger bigInteger10 = arrbigInteger[1];
                if (this.modMult(bigInteger10, bigInteger10).equals((Object)bigInteger5)) {
                    return new Fp(this.q, this.r, this.modHalfAbs(bigInteger10));
                }
                if (!bigInteger9.equals((Object)ECConstants.ONE) && !bigInteger9.equals((Object)bigInteger7)) break;
            } while (true);
            return null;
        }

        @Override
        public ECFieldElement square() {
            return new Fp(this.q, this.r, this.modMult(this.x, this.x));
        }

        @Override
        public ECFieldElement squareMinusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            BigInteger bigInteger = this.x;
            BigInteger bigInteger2 = eCFieldElement.toBigInteger();
            BigInteger bigInteger3 = eCFieldElement2.toBigInteger();
            BigInteger bigInteger4 = bigInteger.multiply(bigInteger);
            BigInteger bigInteger5 = bigInteger2.multiply(bigInteger3);
            return new Fp(this.q, this.r, this.modReduce(bigInteger4.subtract(bigInteger5)));
        }

        @Override
        public ECFieldElement squarePlusProduct(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            BigInteger bigInteger = this.x;
            BigInteger bigInteger2 = eCFieldElement.toBigInteger();
            BigInteger bigInteger3 = eCFieldElement2.toBigInteger();
            BigInteger bigInteger4 = bigInteger.multiply(bigInteger);
            BigInteger bigInteger5 = bigInteger2.multiply(bigInteger3);
            return new Fp(this.q, this.r, this.modReduce(bigInteger4.add(bigInteger5)));
        }

        @Override
        public ECFieldElement subtract(ECFieldElement eCFieldElement) {
            return new Fp(this.q, this.r, this.modSubtract(this.x, eCFieldElement.toBigInteger()));
        }

        @Override
        public BigInteger toBigInteger() {
            return this.x;
        }
    }

}

