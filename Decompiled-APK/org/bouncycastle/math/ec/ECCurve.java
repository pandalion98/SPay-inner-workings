package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Random;
import org.bouncycastle.math.ec.endo.ECEndomorphism;
import org.bouncycastle.math.ec.endo.GLVEndomorphism;
import org.bouncycastle.math.field.FiniteField;
import org.bouncycastle.math.field.FiniteFields;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.Integers;

public abstract class ECCurve {
    public static final int COORD_AFFINE = 0;
    public static final int COORD_HOMOGENEOUS = 1;
    public static final int COORD_JACOBIAN = 2;
    public static final int COORD_JACOBIAN_CHUDNOVSKY = 3;
    public static final int COORD_JACOBIAN_MODIFIED = 4;
    public static final int COORD_LAMBDA_AFFINE = 5;
    public static final int COORD_LAMBDA_PROJECTIVE = 6;
    public static final int COORD_SKEWED = 7;
    protected ECFieldElement f323a;
    protected ECFieldElement f324b;
    protected BigInteger cofactor;
    protected int coord;
    protected ECEndomorphism endomorphism;
    protected FiniteField field;
    protected ECMultiplier multiplier;
    protected BigInteger order;

    public static abstract class AbstractF2m extends ECCurve {
        protected AbstractF2m(int i, int i2, int i3, int i4) {
            super(buildField(i, i2, i3, i4));
        }

        private static FiniteField buildField(int i, int i2, int i3, int i4) {
            if (i2 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            } else if (i3 == 0) {
                if (i4 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                }
                r0 = new int[ECCurve.COORD_JACOBIAN_CHUDNOVSKY];
                r0[ECCurve.COORD_AFFINE] = ECCurve.COORD_AFFINE;
                r0[ECCurve.COORD_HOMOGENEOUS] = i2;
                r0[ECCurve.COORD_JACOBIAN] = i;
                return FiniteFields.getBinaryExtensionField(r0);
            } else if (i3 <= i2) {
                throw new IllegalArgumentException("k2 must be > k1");
            } else if (i4 <= i3) {
                throw new IllegalArgumentException("k3 must be > k2");
            } else {
                r0 = new int[ECCurve.COORD_LAMBDA_AFFINE];
                r0[ECCurve.COORD_AFFINE] = ECCurve.COORD_AFFINE;
                r0[ECCurve.COORD_HOMOGENEOUS] = i2;
                r0[ECCurve.COORD_JACOBIAN] = i3;
                r0[ECCurve.COORD_JACOBIAN_CHUDNOVSKY] = i4;
                r0[ECCurve.COORD_JACOBIAN_MODIFIED] = i;
                return FiniteFields.getBinaryExtensionField(r0);
            }
        }
    }

    public static abstract class AbstractFp extends ECCurve {
        protected AbstractFp(BigInteger bigInteger) {
            super(FiniteFields.getPrimeField(bigInteger));
        }

        protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
            ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement sqrt = fromBigInteger.square().add(this.a).multiply(fromBigInteger).add(this.b).sqrt();
            if (sqrt == null) {
                throw new IllegalArgumentException("Invalid point compression");
            }
            if (sqrt.testBitZero() != (i == ECCurve.COORD_HOMOGENEOUS)) {
                sqrt = sqrt.negate();
            }
            return createRawPoint(fromBigInteger, sqrt, true);
        }
    }

    public class Config {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;

        Config(int i, ECEndomorphism eCEndomorphism, ECMultiplier eCMultiplier) {
            this.coord = i;
            this.endomorphism = eCEndomorphism;
            this.multiplier = eCMultiplier;
        }

        public ECCurve create() {
            if (ECCurve.this.supportsCoordinateSystem(this.coord)) {
                ECCurve cloneCurve = ECCurve.this.cloneCurve();
                if (cloneCurve == ECCurve.this) {
                    throw new IllegalStateException("implementation returned current curve");
                }
                cloneCurve.coord = this.coord;
                cloneCurve.endomorphism = this.endomorphism;
                cloneCurve.multiplier = this.multiplier;
                return cloneCurve;
            }
            throw new IllegalStateException("unsupported coordinate system");
        }

        public Config setCoordinateSystem(int i) {
            this.coord = i;
            return this;
        }

        public Config setEndomorphism(ECEndomorphism eCEndomorphism) {
            this.endomorphism = eCEndomorphism;
            return this;
        }

        public Config setMultiplier(ECMultiplier eCMultiplier) {
            this.multiplier = eCMultiplier;
            return this;
        }
    }

    public static class F2m extends AbstractF2m {
        private static final int F2M_DEFAULT_COORDS = 6;
        private org.bouncycastle.math.ec.ECPoint.F2m infinity;
        private int k1;
        private int k2;
        private int k3;
        private int f325m;
        private byte mu;
        private BigInteger[] si;

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2) {
            this(i, i2, i3, i4, bigInteger, bigInteger2, null, null);
        }

        public F2m(int i, int i2, int i3, int i4, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(i, i2, i3, i4);
            this.mu = (byte) 0;
            this.si = null;
            this.f325m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.F2m(this, null, null);
            this.a = fromBigInteger(bigInteger);
            this.b = fromBigInteger(bigInteger2);
            this.coord = F2M_DEFAULT_COORDS;
        }

        protected F2m(int i, int i2, int i3, int i4, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger, BigInteger bigInteger2) {
            super(i, i2, i3, i4);
            this.mu = (byte) 0;
            this.si = null;
            this.f325m = i;
            this.k1 = i2;
            this.k2 = i3;
            this.k3 = i4;
            this.order = bigInteger;
            this.cofactor = bigInteger2;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.F2m(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.coord = F2M_DEFAULT_COORDS;
        }

        public F2m(int i, int i2, BigInteger bigInteger, BigInteger bigInteger2) {
            this(i, i2, (int) ECCurve.COORD_AFFINE, ECCurve.COORD_AFFINE, bigInteger, bigInteger2, null, null);
        }

        public F2m(int i, int i2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            this(i, i2, (int) ECCurve.COORD_AFFINE, ECCurve.COORD_AFFINE, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }

        private ECFieldElement solveQuadraticEquation(ECFieldElement eCFieldElement) {
            if (eCFieldElement.isZero()) {
                return eCFieldElement;
            }
            ECFieldElement eCFieldElement2;
            ECFieldElement fromBigInteger = fromBigInteger(ECConstants.ZERO);
            Random random = new Random();
            do {
                ECFieldElement fromBigInteger2 = fromBigInteger(new BigInteger(this.f325m, random));
                ECFieldElement eCFieldElement3 = eCFieldElement;
                eCFieldElement2 = fromBigInteger;
                for (int i = ECCurve.COORD_HOMOGENEOUS; i <= this.f325m - 1; i += ECCurve.COORD_HOMOGENEOUS) {
                    eCFieldElement3 = eCFieldElement3.square();
                    eCFieldElement2 = eCFieldElement2.square().add(eCFieldElement3.multiply(fromBigInteger2));
                    eCFieldElement3 = eCFieldElement3.add(eCFieldElement);
                }
                if (!eCFieldElement3.isZero()) {
                    return null;
                }
            } while (eCFieldElement2.square().add(eCFieldElement2).isZero());
            return eCFieldElement2;
        }

        protected ECCurve cloneCurve() {
            return new F2m(this.f325m, this.k1, this.k2, this.k3, this.a, this.b, this.order, this.cofactor);
        }

        protected ECMultiplier createDefaultMultiplier() {
            return isKoblitz() ? new WTauNafMultiplier() : super.createDefaultMultiplier();
        }

        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
            ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
            ECFieldElement fromBigInteger2 = fromBigInteger(bigInteger2);
            switch (getCoordinateSystem()) {
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                case F2M_DEFAULT_COORDS /*6*/:
                    if (!fromBigInteger.isZero()) {
                        fromBigInteger2 = fromBigInteger2.divide(fromBigInteger).add(fromBigInteger);
                        break;
                    } else if (!fromBigInteger2.square().equals(getB())) {
                        throw new IllegalArgumentException();
                    }
                    break;
            }
            return createRawPoint(fromBigInteger, fromBigInteger2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.F2m(this, eCFieldElement, eCFieldElement2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.F2m(this, eCFieldElement, eCFieldElement2, eCFieldElementArr, z);
        }

        protected ECPoint decompressPoint(int i, BigInteger bigInteger) {
            ECFieldElement sqrt;
            ECFieldElement fromBigInteger = fromBigInteger(bigInteger);
            if (fromBigInteger.isZero()) {
                sqrt = this.b.sqrt();
            } else {
                sqrt = solveQuadraticEquation(fromBigInteger.square().invert().multiply(this.b).add(this.a).add(fromBigInteger));
                if (sqrt != null) {
                    if (sqrt.testBitZero() != (i == ECCurve.COORD_HOMOGENEOUS)) {
                        sqrt = sqrt.addOne();
                    }
                    switch (getCoordinateSystem()) {
                        case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        case F2M_DEFAULT_COORDS /*6*/:
                            sqrt = sqrt.add(fromBigInteger);
                            break;
                        default:
                            sqrt = sqrt.multiply(fromBigInteger);
                            break;
                    }
                }
                sqrt = null;
            }
            if (sqrt != null) {
                return createRawPoint(fromBigInteger, sqrt, true);
            }
            throw new IllegalArgumentException("Invalid point compression");
        }

        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new org.bouncycastle.math.ec.ECFieldElement.F2m(this.f325m, this.k1, this.k2, this.k3, bigInteger);
        }

        public int getFieldSize() {
            return this.f325m;
        }

        public BigInteger getH() {
            return this.cofactor;
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }

        public int getK1() {
            return this.k1;
        }

        public int getK2() {
            return this.k2;
        }

        public int getK3() {
            return this.k3;
        }

        public int getM() {
            return this.f325m;
        }

        synchronized byte getMu() {
            if (this.mu == null) {
                this.mu = Tnaf.getMu(this);
            }
            return this.mu;
        }

        public BigInteger getN() {
            return this.order;
        }

        synchronized BigInteger[] getSi() {
            if (this.si == null) {
                this.si = Tnaf.getSi(this);
            }
            return this.si;
        }

        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }

        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
        }

        public boolean supportsCoordinateSystem(int i) {
            switch (i) {
                case ECCurve.COORD_AFFINE /*0*/:
                case ECCurve.COORD_HOMOGENEOUS /*1*/:
                case F2M_DEFAULT_COORDS /*6*/:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static class Fp extends AbstractFp {
        private static final int FP_DEFAULT_COORDS = 4;
        org.bouncycastle.math.ec.ECPoint.Fp infinity;
        BigInteger f326q;
        BigInteger f327r;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            this(bigInteger, bigInteger2, bigInteger3, null, null);
        }

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5) {
            super(bigInteger);
            this.f326q = bigInteger;
            this.f327r = org.bouncycastle.math.ec.ECFieldElement.Fp.calculateResidue(bigInteger);
            this.infinity = new org.bouncycastle.math.ec.ECPoint.Fp(this, null, null);
            this.a = fromBigInteger(bigInteger2);
            this.b = fromBigInteger(bigInteger3);
            this.order = bigInteger4;
            this.cofactor = bigInteger5;
            this.coord = FP_DEFAULT_COORDS;
        }

        protected Fp(BigInteger bigInteger, BigInteger bigInteger2, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(bigInteger, bigInteger2, eCFieldElement, eCFieldElement2, null, null);
        }

        protected Fp(BigInteger bigInteger, BigInteger bigInteger2, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(bigInteger);
            this.f326q = bigInteger;
            this.f327r = bigInteger2;
            this.infinity = new org.bouncycastle.math.ec.ECPoint.Fp(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.coord = FP_DEFAULT_COORDS;
        }

        protected ECCurve cloneCurve() {
            return new Fp(this.f326q, this.f327r, this.a, this.b, this.order, this.cofactor);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.Fp(this, eCFieldElement, eCFieldElement2, z);
        }

        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            return new org.bouncycastle.math.ec.ECPoint.Fp(this, eCFieldElement, eCFieldElement2, eCFieldElementArr, z);
        }

        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new org.bouncycastle.math.ec.ECFieldElement.Fp(this.f326q, this.f327r, bigInteger);
        }

        public int getFieldSize() {
            return this.f326q.bitLength();
        }

        public ECPoint getInfinity() {
            return this.infinity;
        }

        public BigInteger getQ() {
            return this.f326q;
        }

        public ECPoint importPoint(ECPoint eCPoint) {
            if (!(this == eCPoint.getCurve() || getCoordinateSystem() != ECCurve.COORD_JACOBIAN || eCPoint.isInfinity())) {
                switch (eCPoint.getCurve().getCoordinateSystem()) {
                    case ECCurve.COORD_JACOBIAN /*2*/:
                    case ECCurve.COORD_JACOBIAN_CHUDNOVSKY /*3*/:
                    case FP_DEFAULT_COORDS /*4*/:
                        ECFieldElement fromBigInteger = fromBigInteger(eCPoint.f333x.toBigInteger());
                        ECFieldElement fromBigInteger2 = fromBigInteger(eCPoint.f334y.toBigInteger());
                        ECFieldElement[] eCFieldElementArr = new ECFieldElement[ECCurve.COORD_HOMOGENEOUS];
                        eCFieldElementArr[ECCurve.COORD_AFFINE] = fromBigInteger(eCPoint.zs[ECCurve.COORD_AFFINE].toBigInteger());
                        return new org.bouncycastle.math.ec.ECPoint.Fp(this, fromBigInteger, fromBigInteger2, eCFieldElementArr, eCPoint.withCompression);
                }
            }
            return super.importPoint(eCPoint);
        }

        public boolean supportsCoordinateSystem(int i) {
            switch (i) {
                case ECCurve.COORD_AFFINE /*0*/:
                case ECCurve.COORD_HOMOGENEOUS /*1*/:
                case ECCurve.COORD_JACOBIAN /*2*/:
                case FP_DEFAULT_COORDS /*4*/:
                    return true;
                default:
                    return false;
            }
        }
    }

    protected ECCurve(FiniteField finiteField) {
        this.coord = COORD_AFFINE;
        this.endomorphism = null;
        this.multiplier = null;
        this.field = finiteField;
    }

    public static int[] getAllCoordinateSystems() {
        return new int[]{COORD_AFFINE, COORD_HOMOGENEOUS, COORD_JACOBIAN, COORD_JACOBIAN_CHUDNOVSKY, COORD_JACOBIAN_MODIFIED, COORD_LAMBDA_AFFINE, COORD_LAMBDA_PROJECTIVE, COORD_SKEWED};
    }

    protected void checkPoint(ECPoint eCPoint) {
        if (eCPoint == null || this != eCPoint.getCurve()) {
            throw new IllegalArgumentException("'point' must be non-null and on this curve");
        }
    }

    protected void checkPoints(ECPoint[] eCPointArr) {
        checkPoints(eCPointArr, COORD_AFFINE, eCPointArr.length);
    }

    protected void checkPoints(ECPoint[] eCPointArr, int i, int i2) {
        if (eCPointArr == null) {
            throw new IllegalArgumentException("'points' cannot be null");
        } else if (i < 0 || i2 < 0 || i > eCPointArr.length - i2) {
            throw new IllegalArgumentException("invalid range specified for 'points'");
        } else {
            int i3 = COORD_AFFINE;
            while (i3 < i2) {
                ECPoint eCPoint = eCPointArr[i + i3];
                if (eCPoint == null || this == eCPoint.getCurve()) {
                    i3 += COORD_HOMOGENEOUS;
                } else {
                    throw new IllegalArgumentException("'points' entries must be null or on this curve");
                }
            }
        }
    }

    protected abstract ECCurve cloneCurve();

    public Config configure() {
        return new Config(this.coord, this.endomorphism, this.multiplier);
    }

    protected ECMultiplier createDefaultMultiplier() {
        return this.endomorphism instanceof GLVEndomorphism ? new GLVMultiplier(this, (GLVEndomorphism) this.endomorphism) : new WNafL2RMultiplier();
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2) {
        return createPoint(bigInteger, bigInteger2, false);
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
        return createRawPoint(fromBigInteger(bigInteger), fromBigInteger(bigInteger2), z);
    }

    protected abstract ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z);

    protected abstract ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z);

    public ECPoint decodePoint(byte[] bArr) {
        ECPoint infinity;
        boolean z = true;
        int fieldSize = (getFieldSize() + COORD_SKEWED) / 8;
        byte b = bArr[COORD_AFFINE];
        switch (b) {
            case COORD_AFFINE /*0*/:
                if (bArr.length == COORD_HOMOGENEOUS) {
                    infinity = getInfinity();
                    break;
                }
                throw new IllegalArgumentException("Incorrect length for infinity encoding");
            case COORD_JACOBIAN /*2*/:
            case COORD_JACOBIAN_CHUDNOVSKY /*3*/:
                if (bArr.length != fieldSize + COORD_HOMOGENEOUS) {
                    throw new IllegalArgumentException("Incorrect length for compressed encoding");
                }
                infinity = decompressPoint(b & COORD_HOMOGENEOUS, BigIntegers.fromUnsignedByteArray(bArr, COORD_HOMOGENEOUS, fieldSize));
                if (!infinity.satisfiesCofactor()) {
                    throw new IllegalArgumentException("Invalid point");
                }
                break;
            case COORD_JACOBIAN_MODIFIED /*4*/:
                if (bArr.length == (fieldSize * COORD_JACOBIAN) + COORD_HOMOGENEOUS) {
                    infinity = validatePoint(BigIntegers.fromUnsignedByteArray(bArr, COORD_HOMOGENEOUS, fieldSize), BigIntegers.fromUnsignedByteArray(bArr, fieldSize + COORD_HOMOGENEOUS, fieldSize));
                    break;
                }
                throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
            case COORD_LAMBDA_PROJECTIVE /*6*/:
            case COORD_SKEWED /*7*/:
                if (bArr.length == (fieldSize * COORD_JACOBIAN) + COORD_HOMOGENEOUS) {
                    BigInteger fromUnsignedByteArray = BigIntegers.fromUnsignedByteArray(bArr, COORD_HOMOGENEOUS, fieldSize);
                    BigInteger fromUnsignedByteArray2 = BigIntegers.fromUnsignedByteArray(bArr, fieldSize + COORD_HOMOGENEOUS, fieldSize);
                    boolean testBit = fromUnsignedByteArray2.testBit(COORD_AFFINE);
                    if (b != COORD_SKEWED) {
                        z = COORD_AFFINE;
                    }
                    if (testBit == z) {
                        infinity = validatePoint(fromUnsignedByteArray, fromUnsignedByteArray2);
                        break;
                    }
                    throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
                }
                throw new IllegalArgumentException("Incorrect length for hybrid encoding");
            default:
                throw new IllegalArgumentException("Invalid point encoding 0x" + Integer.toString(b, 16));
        }
        if (b == null || !infinity.isInfinity()) {
            return infinity;
        }
        throw new IllegalArgumentException("Invalid infinity encoding");
    }

    protected abstract ECPoint decompressPoint(int i, BigInteger bigInteger);

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof ECCurve) && equals((ECCurve) obj));
    }

    public boolean equals(ECCurve eCCurve) {
        return this == eCCurve || (eCCurve != null && getField().equals(eCCurve.getField()) && getA().toBigInteger().equals(eCCurve.getA().toBigInteger()) && getB().toBigInteger().equals(eCCurve.getB().toBigInteger()));
    }

    public abstract ECFieldElement fromBigInteger(BigInteger bigInteger);

    public ECFieldElement getA() {
        return this.f323a;
    }

    public ECFieldElement getB() {
        return this.f324b;
    }

    public BigInteger getCofactor() {
        return this.cofactor;
    }

    public int getCoordinateSystem() {
        return this.coord;
    }

    public ECEndomorphism getEndomorphism() {
        return this.endomorphism;
    }

    public FiniteField getField() {
        return this.field;
    }

    public abstract int getFieldSize();

    public abstract ECPoint getInfinity();

    public synchronized ECMultiplier getMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = createDefaultMultiplier();
        }
        return this.multiplier;
    }

    public BigInteger getOrder() {
        return this.order;
    }

    public PreCompInfo getPreCompInfo(ECPoint eCPoint, String str) {
        PreCompInfo preCompInfo;
        checkPoint(eCPoint);
        synchronized (eCPoint) {
            Hashtable hashtable = eCPoint.preCompTable;
            preCompInfo = hashtable == null ? null : (PreCompInfo) hashtable.get(str);
        }
        return preCompInfo;
    }

    public int hashCode() {
        return (getField().hashCode() ^ Integers.rotateLeft(getA().toBigInteger().hashCode(), 8)) ^ Integers.rotateLeft(getB().toBigInteger().hashCode(), 16);
    }

    public ECPoint importPoint(ECPoint eCPoint) {
        if (this == eCPoint.getCurve()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return getInfinity();
        }
        ECPoint normalize = eCPoint.normalize();
        return validatePoint(normalize.getXCoord().toBigInteger(), normalize.getYCoord().toBigInteger(), normalize.withCompression);
    }

    public void normalizeAll(ECPoint[] eCPointArr) {
        normalizeAll(eCPointArr, COORD_AFFINE, eCPointArr.length, null);
    }

    public void normalizeAll(ECPoint[] eCPointArr, int i, int i2, ECFieldElement eCFieldElement) {
        checkPoints(eCPointArr, i, i2);
        switch (getCoordinateSystem()) {
            case COORD_AFFINE /*0*/:
            case COORD_LAMBDA_AFFINE /*5*/:
                if (eCFieldElement != null) {
                    throw new IllegalArgumentException("'iso' not valid for affine coordinates");
                }
            default:
                int i3;
                ECFieldElement[] eCFieldElementArr = new ECFieldElement[i2];
                int[] iArr = new int[i2];
                int i4 = COORD_AFFINE;
                for (int i5 = COORD_AFFINE; i5 < i2; i5 += COORD_HOMOGENEOUS) {
                    ECPoint eCPoint = eCPointArr[i + i5];
                    if (!(eCPoint == null || (eCFieldElement == null && eCPoint.isNormalized()))) {
                        eCFieldElementArr[i4] = eCPoint.getZCoord(COORD_AFFINE);
                        i3 = i4 + COORD_HOMOGENEOUS;
                        iArr[i4] = i + i5;
                        i4 = i3;
                    }
                }
                if (i4 != 0) {
                    ECAlgorithms.montgomeryTrick(eCFieldElementArr, COORD_AFFINE, i4, eCFieldElement);
                    for (i3 = COORD_AFFINE; i3 < i4; i3 += COORD_HOMOGENEOUS) {
                        int i6 = iArr[i3];
                        eCPointArr[i6] = eCPointArr[i6].normalize(eCFieldElementArr[i3]);
                    }
                }
        }
    }

    public void setPreCompInfo(ECPoint eCPoint, String str, PreCompInfo preCompInfo) {
        checkPoint(eCPoint);
        synchronized (eCPoint) {
            Hashtable hashtable = eCPoint.preCompTable;
            if (hashtable == null) {
                hashtable = new Hashtable(COORD_JACOBIAN_MODIFIED);
                eCPoint.preCompTable = hashtable;
            }
            hashtable.put(str, preCompInfo);
        }
    }

    public boolean supportsCoordinateSystem(int i) {
        return i == 0;
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2) {
        ECPoint createPoint = createPoint(bigInteger, bigInteger2);
        if (createPoint.isValid()) {
            return createPoint;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2, boolean z) {
        ECPoint createPoint = createPoint(bigInteger, bigInteger2, z);
        if (createPoint.isValid()) {
            return createPoint;
        }
        throw new IllegalArgumentException("Invalid point coordinates");
    }
}
