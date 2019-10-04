/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Hashtable
 *  java.util.Random
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Random;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.GLVMultiplier;
import org.bouncycastle.math.ec.PreCompInfo;
import org.bouncycastle.math.ec.Tnaf;
import org.bouncycastle.math.ec.WNafL2RMultiplier;
import org.bouncycastle.math.ec.WTauNafMultiplier;
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
    protected ECFieldElement a;
    protected ECFieldElement b;
    protected BigInteger cofactor;
    protected int coord = 0;
    protected ECEndomorphism endomorphism = null;
    protected FiniteField field;
    protected ECMultiplier multiplier = null;
    protected BigInteger order;

    protected ECCurve(FiniteField finiteField) {
        this.field = finiteField;
    }

    public static int[] getAllCoordinateSystems() {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    }

    protected void checkPoint(ECPoint eCPoint) {
        if (eCPoint == null || this != eCPoint.getCurve()) {
            throw new IllegalArgumentException("'point' must be non-null and on this curve");
        }
    }

    protected void checkPoints(ECPoint[] arreCPoint) {
        this.checkPoints(arreCPoint, 0, arreCPoint.length);
    }

    protected void checkPoints(ECPoint[] arreCPoint, int n, int n2) {
        if (arreCPoint == null) {
            throw new IllegalArgumentException("'points' cannot be null");
        }
        if (n < 0 || n2 < 0 || n > arreCPoint.length - n2) {
            throw new IllegalArgumentException("invalid range specified for 'points'");
        }
        for (int i = 0; i < n2; ++i) {
            ECPoint eCPoint = arreCPoint[n + i];
            if (eCPoint == null || this == eCPoint.getCurve()) continue;
            throw new IllegalArgumentException("'points' entries must be null or on this curve");
        }
    }

    protected abstract ECCurve cloneCurve();

    public Config configure() {
        return new Config(this.coord, this.endomorphism, this.multiplier);
    }

    protected ECMultiplier createDefaultMultiplier() {
        if (this.endomorphism instanceof GLVEndomorphism) {
            return new GLVMultiplier(this, (GLVEndomorphism)this.endomorphism);
        }
        return new WNafL2RMultiplier();
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2) {
        return this.createPoint(bigInteger, bigInteger2, false);
    }

    public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean bl) {
        return this.createRawPoint(this.fromBigInteger(bigInteger), this.fromBigInteger(bigInteger2), bl);
    }

    protected abstract ECPoint createRawPoint(ECFieldElement var1, ECFieldElement var2, boolean var3);

    protected abstract ECPoint createRawPoint(ECFieldElement var1, ECFieldElement var2, ECFieldElement[] var3, boolean var4);

    /*
     * Enabled aggressive block sorting
     */
    public ECPoint decodePoint(byte[] arrby) {
        ECPoint eCPoint;
        int n = 1;
        int n2 = (7 + this.getFieldSize()) / 8;
        byte by = arrby[0];
        switch (by) {
            default: {
                throw new IllegalArgumentException("Invalid point encoding 0x" + Integer.toString((int)by, (int)16));
            }
            case 0: {
                if (arrby.length != n) {
                    throw new IllegalArgumentException("Incorrect length for infinity encoding");
                }
                eCPoint = this.getInfinity();
                break;
            }
            case 2: 
            case 3: {
                if (arrby.length != n2 + 1) {
                    throw new IllegalArgumentException("Incorrect length for compressed encoding");
                }
                eCPoint = this.decompressPoint(by & 1, BigIntegers.fromUnsignedByteArray(arrby, n, n2));
                if (eCPoint.satisfiesCofactor()) break;
                throw new IllegalArgumentException("Invalid point");
            }
            case 4: {
                if (arrby.length != 1 + n2 * 2) {
                    throw new IllegalArgumentException("Incorrect length for uncompressed encoding");
                }
                eCPoint = this.validatePoint(BigIntegers.fromUnsignedByteArray(arrby, n, n2), BigIntegers.fromUnsignedByteArray(arrby, n2 + 1, n2));
                break;
            }
            case 6: 
            case 7: {
                if (arrby.length != 1 + n2 * 2) {
                    throw new IllegalArgumentException("Incorrect length for hybrid encoding");
                }
                BigInteger bigInteger = BigIntegers.fromUnsignedByteArray(arrby, n, n2);
                BigInteger bigInteger2 = BigIntegers.fromUnsignedByteArray(arrby, n2 + 1, n2);
                int n3 = bigInteger2.testBit(0);
                if (by != 7) {
                    n = 0;
                }
                if (n3 != n) {
                    throw new IllegalArgumentException("Inconsistent Y coordinate in hybrid encoding");
                }
                eCPoint = this.validatePoint(bigInteger, bigInteger2);
            }
        }
        if (by != 0 && eCPoint.isInfinity()) {
            throw new IllegalArgumentException("Invalid infinity encoding");
        }
        return eCPoint;
    }

    protected abstract ECPoint decompressPoint(int var1, BigInteger var2);

    public boolean equals(Object object) {
        return this == object || object instanceof ECCurve && this.equals((ECCurve)object);
    }

    public boolean equals(ECCurve eCCurve) {
        return this == eCCurve || eCCurve != null && this.getField().equals((Object)eCCurve.getField()) && this.getA().toBigInteger().equals((Object)eCCurve.getA().toBigInteger()) && this.getB().toBigInteger().equals((Object)eCCurve.getB().toBigInteger());
    }

    public abstract ECFieldElement fromBigInteger(BigInteger var1);

    public ECFieldElement getA() {
        return this.a;
    }

    public ECFieldElement getB() {
        return this.b;
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

    public ECMultiplier getMultiplier() {
        ECCurve eCCurve = this;
        synchronized (eCCurve) {
            if (this.multiplier == null) {
                this.multiplier = this.createDefaultMultiplier();
            }
            ECMultiplier eCMultiplier = this.multiplier;
            return eCMultiplier;
        }
    }

    public BigInteger getOrder() {
        return this.order;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PreCompInfo getPreCompInfo(ECPoint eCPoint, String string) {
        this.checkPoint(eCPoint);
        ECPoint eCPoint2 = eCPoint;
        synchronized (eCPoint2) {
            Hashtable hashtable = eCPoint.preCompTable;
            if (hashtable != null) return (PreCompInfo)hashtable.get((Object)string);
            return null;
        }
    }

    public int hashCode() {
        return this.getField().hashCode() ^ Integers.rotateLeft(this.getA().toBigInteger().hashCode(), 8) ^ Integers.rotateLeft(this.getB().toBigInteger().hashCode(), 16);
    }

    public ECPoint importPoint(ECPoint eCPoint) {
        if (this == eCPoint.getCurve()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return this.getInfinity();
        }
        ECPoint eCPoint2 = eCPoint.normalize();
        return this.validatePoint(eCPoint2.getXCoord().toBigInteger(), eCPoint2.getYCoord().toBigInteger(), eCPoint2.withCompression);
    }

    public void normalizeAll(ECPoint[] arreCPoint) {
        this.normalizeAll(arreCPoint, 0, arreCPoint.length, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void normalizeAll(ECPoint[] arreCPoint, int n, int n2, ECFieldElement eCFieldElement) {
        int[] arrn;
        int n3;
        ECFieldElement[] arreCFieldElement;
        this.checkPoints(arreCPoint, n, n2);
        switch (this.getCoordinateSystem()) {
            default: {
                arreCFieldElement = new ECFieldElement[n2];
                arrn = new int[n2];
                n3 = 0;
                for (int i = 0; i < n2; ++i) {
                    ECPoint eCPoint = arreCPoint[n + i];
                    if (eCPoint == null || eCFieldElement == null && eCPoint.isNormalized()) continue;
                    arreCFieldElement[n3] = eCPoint.getZCoord(0);
                    int n4 = n3 + 1;
                    arrn[n3] = n + i;
                    n3 = n4;
                }
                break;
            }
            case 0: 
            case 5: {
                if (eCFieldElement == null) return;
                {
                    throw new IllegalArgumentException("'iso' not valid for affine coordinates");
                }
            }
        }
        if (n3 == 0) return;
        {
            ECAlgorithms.montgomeryTrick(arreCFieldElement, 0, n3, eCFieldElement);
            for (int i = 0; i < n3; ++i) {
                int n5 = arrn[i];
                arreCPoint[n5] = arreCPoint[n5].normalize(arreCFieldElement[i]);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setPreCompInfo(ECPoint eCPoint, String string, PreCompInfo preCompInfo) {
        this.checkPoint(eCPoint);
        ECPoint eCPoint2 = eCPoint;
        synchronized (eCPoint2) {
            Hashtable hashtable = eCPoint.preCompTable;
            if (hashtable == null) {
                eCPoint.preCompTable = hashtable = new Hashtable(4);
            }
            hashtable.put((Object)string, (Object)preCompInfo);
            return;
        }
    }

    public boolean supportsCoordinateSystem(int n) {
        return n == 0;
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2) {
        ECPoint eCPoint = this.createPoint(bigInteger, bigInteger2);
        if (!eCPoint.isValid()) {
            throw new IllegalArgumentException("Invalid point coordinates");
        }
        return eCPoint;
    }

    public ECPoint validatePoint(BigInteger bigInteger, BigInteger bigInteger2, boolean bl) {
        ECPoint eCPoint = this.createPoint(bigInteger, bigInteger2, bl);
        if (!eCPoint.isValid()) {
            throw new IllegalArgumentException("Invalid point coordinates");
        }
        return eCPoint;
    }

    public static abstract class AbstractF2m
    extends ECCurve {
        protected AbstractF2m(int n, int n2, int n3, int n4) {
            super(AbstractF2m.buildField(n, n2, n3, n4));
        }

        private static FiniteField buildField(int n, int n2, int n3, int n4) {
            if (n2 == 0) {
                throw new IllegalArgumentException("k1 must be > 0");
            }
            if (n3 == 0) {
                if (n4 != 0) {
                    throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
                }
                return FiniteFields.getBinaryExtensionField(new int[]{0, n2, n});
            }
            if (n3 <= n2) {
                throw new IllegalArgumentException("k2 must be > k1");
            }
            if (n4 <= n3) {
                throw new IllegalArgumentException("k3 must be > k2");
            }
            return FiniteFields.getBinaryExtensionField(new int[]{0, n2, n3, n4, n});
        }
    }

    public static abstract class AbstractFp
    extends ECCurve {
        protected AbstractFp(BigInteger bigInteger) {
            super(FiniteFields.getPrimeField(bigInteger));
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        protected ECPoint decompressPoint(int n, BigInteger bigInteger) {
            boolean bl;
            ECFieldElement eCFieldElement = this.fromBigInteger(bigInteger);
            ECFieldElement eCFieldElement2 = eCFieldElement.square().add(this.a).multiply(eCFieldElement).add(this.b).sqrt();
            if (eCFieldElement2 == null) {
                throw new IllegalArgumentException("Invalid point compression");
            }
            boolean bl2 = eCFieldElement2.testBitZero();
            if (bl2 != (bl = n == 1)) {
                eCFieldElement2 = eCFieldElement2.negate();
            }
            return this.createRawPoint(eCFieldElement, eCFieldElement2, true);
        }
    }

    public class Config {
        protected int coord;
        protected ECEndomorphism endomorphism;
        protected ECMultiplier multiplier;

        Config(int n, ECEndomorphism eCEndomorphism, ECMultiplier eCMultiplier) {
            this.coord = n;
            this.endomorphism = eCEndomorphism;
            this.multiplier = eCMultiplier;
        }

        public ECCurve create() {
            if (!ECCurve.this.supportsCoordinateSystem(this.coord)) {
                throw new IllegalStateException("unsupported coordinate system");
            }
            ECCurve eCCurve = ECCurve.this.cloneCurve();
            if (eCCurve == ECCurve.this) {
                throw new IllegalStateException("implementation returned current curve");
            }
            eCCurve.coord = this.coord;
            eCCurve.endomorphism = this.endomorphism;
            eCCurve.multiplier = this.multiplier;
            return eCCurve;
        }

        public Config setCoordinateSystem(int n) {
            this.coord = n;
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

    public static class F2m
    extends AbstractF2m {
        private static final int F2M_DEFAULT_COORDS = 6;
        private ECPoint.F2m infinity;
        private int k1;
        private int k2;
        private int k3;
        private int m;
        private byte mu = 0;
        private BigInteger[] si = null;

        public F2m(int n, int n2, int n3, int n4, BigInteger bigInteger, BigInteger bigInteger2) {
            this(n, n2, n3, n4, bigInteger, bigInteger2, null, null);
        }

        public F2m(int n, int n2, int n3, int n4, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(n, n2, n3, n4);
            this.m = n;
            this.k1 = n2;
            this.k2 = n3;
            this.k3 = n4;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.infinity = new ECPoint.F2m(this, null, null);
            this.a = this.fromBigInteger(bigInteger);
            this.b = this.fromBigInteger(bigInteger2);
            this.coord = 6;
        }

        protected F2m(int n, int n2, int n3, int n4, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger, BigInteger bigInteger2) {
            super(n, n2, n3, n4);
            this.m = n;
            this.k1 = n2;
            this.k2 = n3;
            this.k3 = n4;
            this.order = bigInteger;
            this.cofactor = bigInteger2;
            this.infinity = new ECPoint.F2m(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.coord = 6;
        }

        public F2m(int n, int n2, BigInteger bigInteger, BigInteger bigInteger2) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, null, null);
        }

        public F2m(int n, int n2, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
            this(n, n2, 0, 0, bigInteger, bigInteger2, bigInteger3, bigInteger4);
        }

        private ECFieldElement solveQuadraticEquation(ECFieldElement eCFieldElement) {
            ECFieldElement eCFieldElement2;
            if (eCFieldElement.isZero()) {
                return eCFieldElement;
            }
            ECFieldElement eCFieldElement3 = this.fromBigInteger(ECConstants.ZERO);
            Random random = new Random();
            do {
                ECFieldElement eCFieldElement4 = this.fromBigInteger(new BigInteger(this.m, random));
                ECFieldElement eCFieldElement5 = eCFieldElement;
                eCFieldElement2 = eCFieldElement3;
                for (int i = 1; i <= -1 + this.m; ++i) {
                    ECFieldElement eCFieldElement6 = eCFieldElement5.square();
                    eCFieldElement2 = eCFieldElement2.square().add(eCFieldElement6.multiply(eCFieldElement4));
                    eCFieldElement5 = eCFieldElement6.add(eCFieldElement);
                }
                if (eCFieldElement5.isZero()) continue;
                return null;
            } while (eCFieldElement2.square().add(eCFieldElement2).isZero());
            return eCFieldElement2;
        }

        @Override
        protected ECCurve cloneCurve() {
            return new F2m(this.m, this.k1, this.k2, this.k3, this.a, this.b, this.order, this.cofactor);
        }

        @Override
        protected ECMultiplier createDefaultMultiplier() {
            if (this.isKoblitz()) {
                return new WTauNafMultiplier();
            }
            return super.createDefaultMultiplier();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint createPoint(BigInteger bigInteger, BigInteger bigInteger2, boolean bl) {
            ECFieldElement eCFieldElement = this.fromBigInteger(bigInteger);
            ECFieldElement eCFieldElement2 = this.fromBigInteger(bigInteger2);
            switch (this.getCoordinateSystem()) {
                default: {
                    return this.createRawPoint(eCFieldElement, eCFieldElement2, bl);
                }
                case 5: 
                case 6: 
            }
            if (eCFieldElement.isZero()) {
                if (eCFieldElement2.square().equals((Object)this.getB())) return this.createRawPoint(eCFieldElement, eCFieldElement2, bl);
                throw new IllegalArgumentException();
            }
            eCFieldElement2 = eCFieldElement2.divide(eCFieldElement).add(eCFieldElement);
            return this.createRawPoint(eCFieldElement, eCFieldElement2, bl);
        }

        @Override
        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
            return new ECPoint.F2m(this, eCFieldElement, eCFieldElement2, bl);
        }

        @Override
        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
            return new ECPoint.F2m(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
        }

        /*
         * Exception decompiling
         */
        @Override
        protected ECPoint decompressPoint(int var1_1, BigInteger var2_2) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.ConfusedCFRException: Extractable last case doesn't follow previous
            // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:478)
            // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:61)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:372)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
            // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
            // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
            // org.benf.cfr.reader.entities.g.p(Method.java:396)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
            // org.benf.cfr.reader.entities.d.c(ClassFile.java:773)
            // org.benf.cfr.reader.entities.d.e(ClassFile.java:870)
            // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
            // org.benf.cfr.reader.b.a(Driver.java:128)
            // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
            // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
            // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
            // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
            // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
            // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
            // java.lang.Thread.run(Thread.java:764)
            throw new IllegalStateException("Decompilation failed");
        }

        @Override
        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, bigInteger);
        }

        @Override
        public int getFieldSize() {
            return this.m;
        }

        public BigInteger getH() {
            return this.cofactor;
        }

        @Override
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
            return this.m;
        }

        byte getMu() {
            F2m f2m = this;
            synchronized (f2m) {
                if (this.mu == 0) {
                    this.mu = Tnaf.getMu(this);
                }
                byte by = this.mu;
                return by;
            }
        }

        public BigInteger getN() {
            return this.order;
        }

        BigInteger[] getSi() {
            F2m f2m = this;
            synchronized (f2m) {
                if (this.si == null) {
                    this.si = Tnaf.getSi(this);
                }
                BigInteger[] arrbigInteger = this.si;
                return arrbigInteger;
            }
        }

        public boolean isKoblitz() {
            return this.order != null && this.cofactor != null && this.b.isOne() && (this.a.isZero() || this.a.isOne());
        }

        public boolean isTrinomial() {
            return this.k2 == 0 && this.k3 == 0;
        }

        @Override
        public boolean supportsCoordinateSystem(int n) {
            switch (n) {
                default: {
                    return false;
                }
                case 0: 
                case 1: 
                case 6: 
            }
            return true;
        }
    }

    public static class Fp
    extends AbstractFp {
        private static final int FP_DEFAULT_COORDS = 4;
        ECPoint.Fp infinity;
        BigInteger q;
        BigInteger r;

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
            this(bigInteger, bigInteger2, bigInteger3, null, null);
        }

        public Fp(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5) {
            super(bigInteger);
            this.q = bigInteger;
            this.r = ECFieldElement.Fp.calculateResidue(bigInteger);
            this.infinity = new ECPoint.Fp(this, null, null);
            this.a = this.fromBigInteger(bigInteger2);
            this.b = this.fromBigInteger(bigInteger3);
            this.order = bigInteger4;
            this.cofactor = bigInteger5;
            this.coord = 4;
        }

        protected Fp(BigInteger bigInteger, BigInteger bigInteger2, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(bigInteger, bigInteger2, eCFieldElement, eCFieldElement2, null, null);
        }

        protected Fp(BigInteger bigInteger, BigInteger bigInteger2, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, BigInteger bigInteger3, BigInteger bigInteger4) {
            super(bigInteger);
            this.q = bigInteger;
            this.r = bigInteger2;
            this.infinity = new ECPoint.Fp(this, null, null);
            this.a = eCFieldElement;
            this.b = eCFieldElement2;
            this.order = bigInteger3;
            this.cofactor = bigInteger4;
            this.coord = 4;
        }

        @Override
        protected ECCurve cloneCurve() {
            return new Fp(this.q, this.r, this.a, this.b, this.order, this.cofactor);
        }

        @Override
        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
            return new ECPoint.Fp(this, eCFieldElement, eCFieldElement2, bl);
        }

        @Override
        protected ECPoint createRawPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
            return new ECPoint.Fp(this, eCFieldElement, eCFieldElement2, arreCFieldElement, bl);
        }

        @Override
        public ECFieldElement fromBigInteger(BigInteger bigInteger) {
            return new ECFieldElement.Fp(this.q, this.r, bigInteger);
        }

        @Override
        public int getFieldSize() {
            return this.q.bitLength();
        }

        @Override
        public ECPoint getInfinity() {
            return this.infinity;
        }

        public BigInteger getQ() {
            return this.q;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public ECPoint importPoint(ECPoint eCPoint) {
            if (this == eCPoint.getCurve() || this.getCoordinateSystem() != 2 || eCPoint.isInfinity()) return super.importPoint(eCPoint);
            switch (eCPoint.getCurve().getCoordinateSystem()) {
                default: {
                    return super.importPoint(eCPoint);
                }
                case 2: 
                case 3: 
                case 4: 
            }
            ECFieldElement eCFieldElement = this.fromBigInteger(eCPoint.x.toBigInteger());
            ECFieldElement eCFieldElement2 = this.fromBigInteger(eCPoint.y.toBigInteger());
            ECFieldElement[] arreCFieldElement = new ECFieldElement[]{this.fromBigInteger(eCPoint.zs[0].toBigInteger())};
            return new ECPoint.Fp(this, eCFieldElement, eCFieldElement2, arreCFieldElement, eCPoint.withCompression);
        }

        @Override
        public boolean supportsCoordinateSystem(int n) {
            switch (n) {
                default: {
                    return false;
                }
                case 0: 
                case 1: 
                case 2: 
                case 4: 
            }
            return true;
        }
    }

}

