/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Hashtable
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;

public abstract class ECPoint {
    protected static ECFieldElement[] EMPTY_ZS = new ECFieldElement[0];
    protected ECCurve curve;
    protected Hashtable preCompTable = null;
    protected boolean withCompression;
    protected ECFieldElement x;
    protected ECFieldElement y;
    protected ECFieldElement[] zs;

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, ECPoint.getInitialZCoords(eCCurve));
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement) {
        this.curve = eCCurve;
        this.x = eCFieldElement;
        this.y = eCFieldElement2;
        this.zs = arreCFieldElement;
    }

    /*
     * Exception decompiling
     */
    protected static ECFieldElement[] getInitialZCoords(ECCurve var0) {
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

    public abstract ECPoint add(ECPoint var1);

    protected void checkNormalized() {
        if (!this.isNormalized()) {
            throw new IllegalStateException("point not in normal form");
        }
    }

    protected ECPoint createScaledPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return this.getCurve().createRawPoint(this.getRawXCoord().multiply(eCFieldElement), this.getRawYCoord().multiply(eCFieldElement2), this.withCompression);
    }

    protected abstract ECPoint detach();

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ECPoint)) {
            return false;
        }
        return this.equals((ECPoint)object);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(ECPoint eCPoint) {
        int n = 1;
        if (eCPoint == null) {
            return false;
        }
        ECCurve eCCurve = eCPoint2.getCurve();
        ECCurve eCCurve2 = eCPoint.getCurve();
        int n2 = eCCurve == null ? n : 0;
        int n3 = eCCurve2 == null ? n : 0;
        boolean bl = eCPoint2.isInfinity();
        boolean bl2 = eCPoint.isInfinity();
        if (bl || bl2) {
            if (!bl) return (boolean)0;
            if (!bl2) return (boolean)0;
            if (n2 != 0) return (boolean)n;
            if (n3 != 0) return (boolean)n;
            if (!eCCurve.equals(eCCurve2)) return (boolean)0;
            return (boolean)n;
        }
        if (n2 == 0 || n3 == 0) {
            ECPoint eCPoint2;
            if (n2 != 0) {
                eCPoint = eCPoint.normalize();
            } else if (n3 != 0) {
                eCPoint2 = eCPoint2.normalize();
            } else {
                if (!eCCurve.equals(eCCurve2)) return false;
                ECPoint[] arreCPoint = new ECPoint[2];
                arreCPoint[0] = eCPoint2;
                arreCPoint[n] = eCCurve.importPoint(eCPoint);
                eCCurve.normalizeAll(arreCPoint);
                eCPoint2 = arreCPoint[0];
                eCPoint = arreCPoint[n];
            }
        }
        if (!eCPoint2.getXCoord().equals((Object)eCPoint.getXCoord())) return (boolean)0;
        if (!eCPoint2.getYCoord().equals((Object)eCPoint.getYCoord())) return (boolean)0;
        return (boolean)n;
    }

    public ECFieldElement getAffineXCoord() {
        this.checkNormalized();
        return this.getXCoord();
    }

    public ECFieldElement getAffineYCoord() {
        this.checkNormalized();
        return this.getYCoord();
    }

    protected abstract boolean getCompressionYTilde();

    public ECCurve getCurve() {
        return this.curve;
    }

    protected int getCurveCoordinateSystem() {
        if (this.curve == null) {
            return 0;
        }
        return this.curve.getCoordinateSystem();
    }

    public final ECPoint getDetachedPoint() {
        return this.normalize().detach();
    }

    public byte[] getEncoded() {
        return this.getEncoded(this.withCompression);
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] getEncoded(boolean bl) {
        if (this.isInfinity()) {
            return new byte[1];
        }
        ECPoint eCPoint = this.normalize();
        byte[] arrby = eCPoint.getXCoord().getEncoded();
        if (!bl) {
            byte[] arrby2 = eCPoint.getYCoord().getEncoded();
            byte[] arrby3 = new byte[1 + (arrby.length + arrby2.length)];
            arrby3[0] = 4;
            System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)1, (int)arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)(1 + arrby.length), (int)arrby2.length);
            return arrby3;
        }
        byte[] arrby4 = new byte[1 + arrby.length];
        int n = eCPoint.getCompressionYTilde() ? 3 : 2;
        arrby4[0] = n;
        System.arraycopy((Object)arrby, (int)0, (Object)arrby4, (int)1, (int)arrby.length);
        return arrby4;
    }

    protected final ECFieldElement getRawXCoord() {
        return this.x;
    }

    protected final ECFieldElement getRawYCoord() {
        return this.y;
    }

    protected final ECFieldElement[] getRawZCoords() {
        return this.zs;
    }

    public ECFieldElement getX() {
        return this.normalize().getXCoord();
    }

    public ECFieldElement getXCoord() {
        return this.x;
    }

    public ECFieldElement getY() {
        return this.normalize().getYCoord();
    }

    public ECFieldElement getYCoord() {
        return this.y;
    }

    public ECFieldElement getZCoord(int n) {
        if (n < 0 || n >= this.zs.length) {
            return null;
        }
        return this.zs[n];
    }

    public ECFieldElement[] getZCoords() {
        int n = this.zs.length;
        if (n == 0) {
            return this.zs;
        }
        ECFieldElement[] arreCFieldElement = new ECFieldElement[n];
        System.arraycopy((Object)this.zs, (int)0, (Object)arreCFieldElement, (int)0, (int)n);
        return arreCFieldElement;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int hashCode() {
        ECCurve eCCurve = this.getCurve();
        int n = eCCurve == null ? 0 : -1 ^ eCCurve.hashCode();
        if (this.isInfinity()) return n;
        ECPoint eCPoint = this.normalize();
        return n ^ 17 * eCPoint.getXCoord().hashCode() ^ 257 * eCPoint.getYCoord().hashCode();
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean isInfinity() {
        boolean bl;
        block3 : {
            block2 : {
                if (this.x == null || this.y == null) break block2;
                int n = this.zs.length;
                bl = false;
                if (n <= 0) break block3;
                boolean bl2 = this.zs[0].isZero();
                bl = false;
                if (!bl2) break block3;
            }
            bl = true;
        }
        return bl;
    }

    public boolean isNormalized() {
        boolean bl;
        block3 : {
            block2 : {
                int n = this.getCurveCoordinateSystem();
                if (n == 0 || n == 5 || this.isInfinity()) break block2;
                boolean bl2 = this.zs[0].isOne();
                bl = false;
                if (!bl2) break block3;
            }
            bl = true;
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isValid() {
        block5 : {
            block4 : {
                if (this.isInfinity() || this.getCurve() == null) break block4;
                if (!this.satisfiesCurveEquation()) {
                    return false;
                }
                if (!this.satisfiesCofactor()) break block5;
            }
            return true;
        }
        return false;
    }

    public ECPoint multiply(BigInteger bigInteger) {
        return this.getCurve().getMultiplier().multiply(this, bigInteger);
    }

    public abstract ECPoint negate();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ECPoint normalize() {
        if (this.isInfinity()) {
            do {
                return this;
                break;
            } while (true);
        }
        switch (this.getCurveCoordinateSystem()) {
            case 0: 
            case 5: {
                return this;
            }
        }
        ECFieldElement eCFieldElement = this.getZCoord(0);
        if (!eCFieldElement.isOne()) return this.normalize(eCFieldElement.invert());
        return this;
    }

    ECPoint normalize(ECFieldElement eCFieldElement) {
        switch (this.getCurveCoordinateSystem()) {
            default: {
                throw new IllegalStateException("not a projective coordinate system");
            }
            case 1: 
            case 6: {
                return this.createScaledPoint(eCFieldElement, eCFieldElement);
            }
            case 2: 
            case 3: 
            case 4: 
        }
        ECFieldElement eCFieldElement2 = eCFieldElement.square();
        return this.createScaledPoint(eCFieldElement2, eCFieldElement2.multiply(eCFieldElement));
    }

    protected boolean satisfiesCofactor() {
        BigInteger bigInteger = this.curve.getCofactor();
        return bigInteger == null || bigInteger.equals((Object)ECConstants.ONE) || !ECAlgorithms.referenceMultiply(this, bigInteger).isInfinity();
    }

    protected abstract boolean satisfiesCurveEquation();

    public ECPoint scaleX(ECFieldElement eCFieldElement) {
        if (this.isInfinity()) {
            return this;
        }
        return this.getCurve().createRawPoint(this.getRawXCoord().multiply(eCFieldElement), this.getRawYCoord(), this.getRawZCoords(), this.withCompression);
    }

    public ECPoint scaleY(ECFieldElement eCFieldElement) {
        if (this.isInfinity()) {
            return this;
        }
        return this.getCurve().createRawPoint(this.getRawXCoord(), this.getRawYCoord().multiply(eCFieldElement), this.getRawZCoords(), this.withCompression);
    }

    public abstract ECPoint subtract(ECPoint var1);

    public ECPoint threeTimes() {
        return this.twicePlus(this);
    }

    public ECPoint timesPow2(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("'e' cannot be negative");
        }
        while (--n >= 0) {
            ECPoint eCPoint = eCPoint.twice();
        }
        return eCPoint;
    }

    public String toString() {
        if (this.isInfinity()) {
            return "INF";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        stringBuffer.append((Object)this.getRawXCoord());
        stringBuffer.append(',');
        stringBuffer.append((Object)this.getRawYCoord());
        for (int i = 0; i < this.zs.length; ++i) {
            stringBuffer.append(',');
            stringBuffer.append((Object)this.zs[i]);
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    public abstract ECPoint twice();

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this.twice().add(eCPoint);
    }

    public static abstract class AbstractF2m
    extends ECPoint {
        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement) {
            super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        protected boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            ECCurve eCCurve = this.getCurve();
            ECFieldElement eCFieldElement3 = this.x;
            ECFieldElement eCFieldElement4 = eCCurve.getA();
            ECFieldElement eCFieldElement5 = eCCurve.getB();
            int n = eCCurve.getCoordinateSystem();
            if (n == 6) {
                ECFieldElement eCFieldElement6;
                ECFieldElement eCFieldElement7;
                ECFieldElement eCFieldElement8 = this.zs[0];
                boolean bl = eCFieldElement8.isOne();
                if (eCFieldElement3.isZero()) {
                    ECFieldElement eCFieldElement9 = this.y.square();
                    if (bl) return eCFieldElement9.equals((Object)eCFieldElement5);
                    eCFieldElement5 = eCFieldElement5.multiply(eCFieldElement8.square());
                    return eCFieldElement9.equals((Object)eCFieldElement5);
                }
                ECFieldElement eCFieldElement10 = this.y;
                ECFieldElement eCFieldElement11 = eCFieldElement3.square();
                if (bl) {
                    eCFieldElement7 = eCFieldElement10.square().add(eCFieldElement10).add(eCFieldElement4);
                    eCFieldElement6 = eCFieldElement11.square().add(eCFieldElement5);
                    do {
                        return eCFieldElement7.multiply(eCFieldElement11).equals((Object)eCFieldElement6);
                        break;
                    } while (true);
                }
                ECFieldElement eCFieldElement12 = eCFieldElement8.square();
                ECFieldElement eCFieldElement13 = eCFieldElement12.square();
                eCFieldElement7 = eCFieldElement10.add(eCFieldElement8).multiplyPlusProduct(eCFieldElement10, eCFieldElement4, eCFieldElement12);
                eCFieldElement6 = eCFieldElement11.squarePlusProduct(eCFieldElement5, eCFieldElement13);
                return eCFieldElement7.multiply(eCFieldElement11).equals((Object)eCFieldElement6);
            }
            ECFieldElement eCFieldElement14 = this.y;
            ECFieldElement eCFieldElement15 = eCFieldElement14.add(eCFieldElement3).multiply(eCFieldElement14);
            switch (n) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    eCFieldElement2 = eCFieldElement5;
                    eCFieldElement = eCFieldElement15;
                    do {
                        return eCFieldElement.equals((Object)eCFieldElement3.add(eCFieldElement4).multiply(eCFieldElement3.square()).add(eCFieldElement2));
                        break;
                    } while (true);
                }
                case 1: 
            }
            ECFieldElement eCFieldElement16 = this.zs[0];
            if (!eCFieldElement16.isOne()) {
                ECFieldElement eCFieldElement17 = eCFieldElement16.multiply(eCFieldElement16.square());
                ECFieldElement eCFieldElement18 = eCFieldElement15.multiply(eCFieldElement16);
                eCFieldElement4 = eCFieldElement4.multiply(eCFieldElement16);
                eCFieldElement2 = eCFieldElement5.multiply(eCFieldElement17);
                eCFieldElement = eCFieldElement18;
                return eCFieldElement.equals((Object)eCFieldElement3.add(eCFieldElement4).multiply(eCFieldElement3.square()).add(eCFieldElement2));
            }
            eCFieldElement2 = eCFieldElement5;
            eCFieldElement = eCFieldElement15;
            return eCFieldElement.equals((Object)eCFieldElement3.add(eCFieldElement4).multiply(eCFieldElement3.square()).add(eCFieldElement2));
        }
    }

    public static abstract class AbstractFp
    extends ECPoint {
        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement) {
            super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
        }

        @Override
        protected boolean getCompressionYTilde() {
            return this.getAffineYCoord().testBitZero();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        protected boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement eCFieldElement3 = this.curve.getA();
            ECFieldElement eCFieldElement4 = this.curve.getB();
            ECFieldElement eCFieldElement5 = eCFieldElement2.square();
            switch (this.getCurveCoordinateSystem()) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 1: {
                    ECFieldElement eCFieldElement6 = this.zs[0];
                    if (eCFieldElement6.isOne()) return eCFieldElement5.equals((Object)eCFieldElement.square().add(eCFieldElement3).multiply(eCFieldElement).add(eCFieldElement4));
                    ECFieldElement eCFieldElement7 = eCFieldElement6.square();
                    ECFieldElement eCFieldElement8 = eCFieldElement6.multiply(eCFieldElement7);
                    eCFieldElement5 = eCFieldElement5.multiply(eCFieldElement6);
                    eCFieldElement3 = eCFieldElement3.multiply(eCFieldElement7);
                    eCFieldElement4 = eCFieldElement4.multiply(eCFieldElement8);
                }
                case 0: {
                    return eCFieldElement5.equals((Object)eCFieldElement.square().add(eCFieldElement3).multiply(eCFieldElement).add(eCFieldElement4));
                }
                case 2: 
                case 3: 
                case 4: 
            }
            ECFieldElement eCFieldElement9 = this.zs[0];
            if (eCFieldElement9.isOne()) return eCFieldElement5.equals((Object)eCFieldElement.square().add(eCFieldElement3).multiply(eCFieldElement).add(eCFieldElement4));
            ECFieldElement eCFieldElement10 = eCFieldElement9.square();
            ECFieldElement eCFieldElement11 = eCFieldElement10.square();
            ECFieldElement eCFieldElement12 = eCFieldElement10.multiply(eCFieldElement11);
            eCFieldElement3 = eCFieldElement3.multiply(eCFieldElement11);
            eCFieldElement4 = eCFieldElement4.multiply(eCFieldElement12);
            return eCFieldElement5.equals((Object)eCFieldElement.square().add(eCFieldElement3).multiply(eCFieldElement).add(eCFieldElement4));
        }

        @Override
        public ECPoint subtract(ECPoint eCPoint) {
            if (eCPoint.isInfinity()) {
                return this;
            }
            return this.add(eCPoint.negate());
        }
    }

    public static class F2m
    extends AbstractF2m {
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        /*
         * Enabled aggressive block sorting
         */
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
            boolean bl2 = true;
            super(eCCurve, eCFieldElement, eCFieldElement2);
            boolean bl3 = eCFieldElement == null ? bl2 : false;
            if (eCFieldElement2 != null) {
                bl2 = false;
            }
            if (bl3 != bl2) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (eCFieldElement != null) {
                ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (eCCurve != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = bl;
        }

        F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
            super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
            this.withCompression = bl;
        }

        private static void checkPoints(ECPoint eCPoint, ECPoint eCPoint2) {
            if (eCPoint.curve != eCPoint2.curve) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }

        @Override
        public ECPoint add(ECPoint eCPoint) {
            F2m.checkPoints(this, eCPoint);
            return this.addSimple((F2m)eCPoint);
        }

        /*
         * Enabled aggressive block sorting
         */
        public F2m addSimple(F2m f2m) {
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            ECFieldElement eCFieldElement3;
            ECFieldElement eCFieldElement4;
            ECFieldElement eCFieldElement5;
            ECFieldElement eCFieldElement6;
            boolean bl;
            ECFieldElement eCFieldElement7;
            if (this.isInfinity()) {
                return f2m;
            }
            if (f2m.isInfinity()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            int n = eCCurve.getCoordinateSystem();
            ECFieldElement eCFieldElement8 = this.x;
            ECFieldElement eCFieldElement9 = f2m.x;
            switch (n) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement10 = this.y;
                    ECFieldElement eCFieldElement11 = f2m.y;
                    ECFieldElement eCFieldElement12 = eCFieldElement8.add(eCFieldElement9);
                    ECFieldElement eCFieldElement13 = eCFieldElement10.add(eCFieldElement11);
                    if (!eCFieldElement12.isZero()) {
                        ECFieldElement eCFieldElement14 = eCFieldElement13.divide(eCFieldElement12);
                        ECFieldElement eCFieldElement15 = eCFieldElement14.square().add(eCFieldElement14).add(eCFieldElement12).add(eCCurve.getA());
                        return new F2m(eCCurve, eCFieldElement15, eCFieldElement14.multiply(eCFieldElement8.add(eCFieldElement15)).add(eCFieldElement15).add(eCFieldElement10), this.withCompression);
                    }
                    if (!eCFieldElement13.isZero()) return (F2m)eCCurve.getInfinity();
                    return (F2m)this.twice();
                }
                case 1: {
                    ECFieldElement eCFieldElement16 = this.y;
                    ECFieldElement eCFieldElement17 = this.zs[0];
                    ECFieldElement eCFieldElement18 = f2m.y;
                    ECFieldElement eCFieldElement19 = f2m.zs[0];
                    boolean bl2 = eCFieldElement19.isOne();
                    ECFieldElement eCFieldElement20 = eCFieldElement17.multiply(eCFieldElement18);
                    ECFieldElement eCFieldElement21 = bl2 ? eCFieldElement16 : eCFieldElement16.multiply(eCFieldElement19);
                    ECFieldElement eCFieldElement22 = eCFieldElement20.add(eCFieldElement21);
                    ECFieldElement eCFieldElement23 = eCFieldElement17.multiply(eCFieldElement9);
                    ECFieldElement eCFieldElement24 = bl2 ? eCFieldElement8 : eCFieldElement8.multiply(eCFieldElement19);
                    ECFieldElement eCFieldElement25 = eCFieldElement23.add(eCFieldElement24);
                    if (eCFieldElement25.isZero()) {
                        if (!eCFieldElement22.isZero()) return (F2m)eCCurve.getInfinity();
                        return (F2m)this.twice();
                    }
                    ECFieldElement eCFieldElement26 = eCFieldElement25.square();
                    ECFieldElement eCFieldElement27 = eCFieldElement26.multiply(eCFieldElement25);
                    ECFieldElement eCFieldElement28 = bl2 ? eCFieldElement17 : eCFieldElement17.multiply(eCFieldElement19);
                    ECFieldElement eCFieldElement29 = eCFieldElement22.add(eCFieldElement25);
                    ECFieldElement eCFieldElement30 = eCFieldElement29.multiplyPlusProduct(eCFieldElement22, eCFieldElement26, eCCurve.getA()).multiply(eCFieldElement28).add(eCFieldElement27);
                    ECFieldElement eCFieldElement31 = eCFieldElement25.multiply(eCFieldElement30);
                    if (bl2) return new F2m(eCCurve, eCFieldElement31, eCFieldElement22.multiplyPlusProduct(eCFieldElement8, eCFieldElement25, eCFieldElement16).multiplyPlusProduct(eCFieldElement26, eCFieldElement29, eCFieldElement30), new ECFieldElement[]{eCFieldElement27.multiply(eCFieldElement28)}, this.withCompression);
                    {
                        eCFieldElement26 = eCFieldElement26.multiply(eCFieldElement19);
                    }
                    return new F2m(eCCurve, eCFieldElement31, eCFieldElement22.multiplyPlusProduct(eCFieldElement8, eCFieldElement25, eCFieldElement16).multiplyPlusProduct(eCFieldElement26, eCFieldElement29, eCFieldElement30), new ECFieldElement[]{eCFieldElement27.multiply(eCFieldElement28)}, this.withCompression);
                }
                case 6: 
            }
            if (eCFieldElement8.isZero()) {
                if (!eCFieldElement9.isZero()) return f2m.addSimple(this);
                return (F2m)eCCurve.getInfinity();
            }
            ECFieldElement eCFieldElement32 = this.y;
            ECFieldElement eCFieldElement33 = this.zs[0];
            ECFieldElement eCFieldElement34 = f2m.y;
            ECFieldElement eCFieldElement35 = f2m.zs[0];
            boolean bl3 = eCFieldElement33.isOne();
            if (!bl3) {
                eCFieldElement4 = eCFieldElement9.multiply(eCFieldElement33);
                eCFieldElement7 = eCFieldElement34.multiply(eCFieldElement33);
            } else {
                eCFieldElement7 = eCFieldElement34;
                eCFieldElement4 = eCFieldElement9;
            }
            if (!(bl = eCFieldElement35.isOne())) {
                eCFieldElement5 = eCFieldElement8.multiply(eCFieldElement35);
                eCFieldElement3 = eCFieldElement32.multiply(eCFieldElement35);
            } else {
                eCFieldElement5 = eCFieldElement8;
                eCFieldElement3 = eCFieldElement32;
            }
            ECFieldElement eCFieldElement36 = eCFieldElement3.add(eCFieldElement7);
            ECFieldElement eCFieldElement37 = eCFieldElement5.add(eCFieldElement4);
            if (eCFieldElement37.isZero()) {
                if (!eCFieldElement36.isZero()) return (F2m)eCCurve.getInfinity();
                return (F2m)this.twice();
            }
            if (eCFieldElement9.isZero()) {
                ECPoint eCPoint = this.normalize();
                ECFieldElement eCFieldElement38 = eCPoint.getXCoord();
                ECFieldElement eCFieldElement39 = eCPoint.getYCoord();
                ECFieldElement eCFieldElement40 = eCFieldElement39.add(eCFieldElement34).divide(eCFieldElement38);
                eCFieldElement = eCFieldElement40.square().add(eCFieldElement40).add(eCFieldElement38).add(eCCurve.getA());
                if (eCFieldElement.isZero()) {
                    return new F2m(eCCurve, eCFieldElement, eCCurve.getB().sqrt(), this.withCompression);
                }
                eCFieldElement6 = eCFieldElement40.multiply(eCFieldElement38.add(eCFieldElement)).add(eCFieldElement).add(eCFieldElement39).divide(eCFieldElement).add(eCFieldElement);
                eCFieldElement2 = eCCurve.fromBigInteger(ECConstants.ONE);
                return new F2m(eCCurve, eCFieldElement, eCFieldElement6, new ECFieldElement[]{eCFieldElement2}, this.withCompression);
            } else {
                ECFieldElement eCFieldElement41;
                ECFieldElement eCFieldElement42 = eCFieldElement37.square();
                ECFieldElement eCFieldElement43 = eCFieldElement36.multiply(eCFieldElement5);
                eCFieldElement = eCFieldElement43.multiply(eCFieldElement41 = eCFieldElement36.multiply(eCFieldElement4));
                if (eCFieldElement.isZero()) {
                    return new F2m(eCCurve, eCFieldElement, eCCurve.getB().sqrt(), this.withCompression);
                }
                ECFieldElement eCFieldElement44 = eCFieldElement36.multiply(eCFieldElement42);
                if (!bl) {
                    eCFieldElement44 = eCFieldElement44.multiply(eCFieldElement35);
                }
                eCFieldElement6 = eCFieldElement41.add(eCFieldElement42).squarePlusProduct(eCFieldElement44, eCFieldElement32.add(eCFieldElement33));
                eCFieldElement2 = !bl3 ? eCFieldElement44.multiply(eCFieldElement33) : eCFieldElement44;
            }
            return new F2m(eCCurve, eCFieldElement, eCFieldElement6, new ECFieldElement[]{eCFieldElement2}, this.withCompression);
        }

        @Override
        protected ECPoint detach() {
            return new F2m(null, this.getAffineXCoord(), this.getAffineYCoord());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        protected boolean getCompressionYTilde() {
            block6 : {
                block5 : {
                    ECFieldElement eCFieldElement = this.getRawXCoord();
                    if (eCFieldElement.isZero()) break block5;
                    ECFieldElement eCFieldElement2 = this.getRawYCoord();
                    switch (this.getCurveCoordinateSystem()) {
                        default: {
                            return eCFieldElement2.divide(eCFieldElement).testBitZero();
                        }
                        case 5: 
                        case 6: 
                    }
                    if (eCFieldElement2.testBitZero() != eCFieldElement.testBitZero()) break block6;
                }
                return false;
            }
            return true;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public ECFieldElement getYCoord() {
            int n = this.getCurveCoordinateSystem();
            switch (n) {
                default: {
                    return this.y;
                }
                case 5: 
                case 6: 
            }
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            if (this.isInfinity()) return eCFieldElement2;
            if (eCFieldElement.isZero()) return eCFieldElement2;
            eCFieldElement2 = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement);
            if (6 != n) return eCFieldElement2;
            ECFieldElement eCFieldElement3 = this.zs[0];
            if (eCFieldElement3.isOne()) return eCFieldElement2;
            return eCFieldElement2.divide(eCFieldElement3);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint negate() {
            ECFieldElement eCFieldElement;
            if (this.isInfinity() || (eCFieldElement = this.x).isZero()) {
                return this;
            }
            switch (this.getCurveCoordinateSystem()) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement2 = this.y;
                    return new F2m(this.curve, eCFieldElement, eCFieldElement2.add(eCFieldElement), this.withCompression);
                }
                case 1: {
                    ECFieldElement eCFieldElement3 = this.y;
                    ECFieldElement eCFieldElement4 = this.zs[0];
                    return new F2m(this.curve, eCFieldElement, eCFieldElement3.add(eCFieldElement), new ECFieldElement[]{eCFieldElement4}, this.withCompression);
                }
                case 5: {
                    ECFieldElement eCFieldElement5 = this.y;
                    return new F2m(this.curve, eCFieldElement, eCFieldElement5.addOne(), this.withCompression);
                }
                case 6: 
            }
            ECFieldElement eCFieldElement6 = this.y;
            ECFieldElement eCFieldElement7 = this.zs[0];
            return new F2m(this.curve, eCFieldElement, eCFieldElement6.add(eCFieldElement7), new ECFieldElement[]{eCFieldElement7}, this.withCompression);
        }

        @Override
        public ECPoint scaleX(ECFieldElement eCFieldElement) {
            if (this.isInfinity()) {
                return this;
            }
            switch (this.getCurveCoordinateSystem()) {
                default: {
                    return super.scaleX(eCFieldElement);
                }
                case 5: {
                    ECFieldElement eCFieldElement2 = this.getRawXCoord();
                    ECFieldElement eCFieldElement3 = this.getRawYCoord();
                    ECFieldElement eCFieldElement4 = eCFieldElement2.multiply(eCFieldElement);
                    ECFieldElement eCFieldElement5 = eCFieldElement3.add(eCFieldElement2).divide(eCFieldElement).add(eCFieldElement4);
                    return this.getCurve().createRawPoint(eCFieldElement2, eCFieldElement5, this.getRawZCoords(), this.withCompression);
                }
                case 6: 
            }
            ECFieldElement eCFieldElement6 = this.getRawXCoord();
            ECFieldElement eCFieldElement7 = this.getRawYCoord();
            ECFieldElement eCFieldElement8 = this.getRawZCoords()[0];
            ECFieldElement eCFieldElement9 = eCFieldElement6.multiply(eCFieldElement.square());
            ECFieldElement eCFieldElement10 = eCFieldElement7.add(eCFieldElement6).add(eCFieldElement9);
            ECFieldElement eCFieldElement11 = eCFieldElement8.multiply(eCFieldElement);
            return this.getCurve().createRawPoint(eCFieldElement9, eCFieldElement10, new ECFieldElement[]{eCFieldElement11}, this.withCompression);
        }

        @Override
        public ECPoint scaleY(ECFieldElement eCFieldElement) {
            if (this.isInfinity()) {
                return this;
            }
            switch (this.getCurveCoordinateSystem()) {
                default: {
                    return super.scaleY(eCFieldElement);
                }
                case 5: 
                case 6: 
            }
            ECFieldElement eCFieldElement2 = this.getRawXCoord();
            ECFieldElement eCFieldElement3 = this.getRawYCoord().add(eCFieldElement2).multiply(eCFieldElement).add(eCFieldElement2);
            return this.getCurve().createRawPoint(eCFieldElement2, eCFieldElement3, this.getRawZCoords(), this.withCompression);
        }

        @Override
        public ECPoint subtract(ECPoint eCPoint) {
            F2m.checkPoints(this, eCPoint);
            return this.subtractSimple((F2m)eCPoint);
        }

        public F2m subtractSimple(F2m f2m) {
            if (f2m.isInfinity()) {
                return this;
            }
            return this.addSimple((F2m)f2m.negate());
        }

        public F2m tau() {
            if (this.isInfinity()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            int n = eCCurve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (n) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: 
                case 5: {
                    ECFieldElement eCFieldElement2 = this.y;
                    return new F2m(eCCurve, eCFieldElement.square(), eCFieldElement2.square(), this.withCompression);
                }
                case 1: 
                case 6: 
            }
            ECFieldElement eCFieldElement3 = this.y;
            ECFieldElement eCFieldElement4 = this.zs[0];
            ECFieldElement eCFieldElement5 = eCFieldElement.square();
            ECFieldElement eCFieldElement6 = eCFieldElement3.square();
            ECFieldElement[] arreCFieldElement = new ECFieldElement[]{eCFieldElement4.square()};
            return new F2m(eCCurve, eCFieldElement5, eCFieldElement6, arreCFieldElement, this.withCompression);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint twice() {
            ECFieldElement eCFieldElement;
            if (this.isInfinity()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            ECFieldElement eCFieldElement2 = this.x;
            if (eCFieldElement2.isZero()) {
                return eCCurve.getInfinity();
            }
            switch (eCCurve.getCoordinateSystem()) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement3 = this.y.divide(eCFieldElement2).add(eCFieldElement2);
                    ECFieldElement eCFieldElement4 = eCFieldElement3.square().add(eCFieldElement3).add(eCCurve.getA());
                    return new F2m(eCCurve, eCFieldElement4, eCFieldElement2.squarePlusProduct(eCFieldElement4, eCFieldElement3.addOne()), this.withCompression);
                }
                case 1: {
                    ECFieldElement eCFieldElement5 = this.y;
                    ECFieldElement eCFieldElement6 = this.zs[0];
                    boolean bl = eCFieldElement6.isOne();
                    ECFieldElement eCFieldElement7 = bl ? eCFieldElement2 : eCFieldElement2.multiply(eCFieldElement6);
                    if (!bl) {
                        eCFieldElement5 = eCFieldElement5.multiply(eCFieldElement6);
                    }
                    ECFieldElement eCFieldElement8 = eCFieldElement2.square();
                    ECFieldElement eCFieldElement9 = eCFieldElement8.add(eCFieldElement5);
                    ECFieldElement eCFieldElement10 = eCFieldElement7.square();
                    ECFieldElement eCFieldElement11 = eCFieldElement9.add(eCFieldElement7);
                    ECFieldElement eCFieldElement12 = eCFieldElement11.multiplyPlusProduct(eCFieldElement9, eCFieldElement10, eCCurve.getA());
                    return new F2m(eCCurve, eCFieldElement7.multiply(eCFieldElement12), eCFieldElement8.square().multiplyPlusProduct(eCFieldElement7, eCFieldElement12, eCFieldElement11), new ECFieldElement[]{eCFieldElement7.multiply(eCFieldElement10)}, this.withCompression);
                }
                case 6: 
            }
            ECFieldElement eCFieldElement13 = this.y;
            ECFieldElement eCFieldElement14 = this.zs[0];
            boolean bl = eCFieldElement14.isOne();
            ECFieldElement eCFieldElement15 = bl ? eCFieldElement13 : eCFieldElement13.multiply(eCFieldElement14);
            ECFieldElement eCFieldElement16 = bl ? eCFieldElement14 : eCFieldElement14.square();
            ECFieldElement eCFieldElement17 = eCCurve.getA();
            ECFieldElement eCFieldElement18 = bl ? eCFieldElement17 : eCFieldElement17.multiply(eCFieldElement16);
            ECFieldElement eCFieldElement19 = eCFieldElement13.square().add(eCFieldElement15).add(eCFieldElement18);
            if (eCFieldElement19.isZero()) {
                return new F2m(eCCurve, eCFieldElement19, eCCurve.getB().sqrt(), this.withCompression);
            }
            ECFieldElement eCFieldElement20 = eCFieldElement19.square();
            ECFieldElement eCFieldElement21 = bl ? eCFieldElement19 : eCFieldElement19.multiply(eCFieldElement16);
            ECFieldElement eCFieldElement22 = eCCurve.getB();
            if (eCFieldElement22.bitLength() < eCCurve.getFieldSize() >> 1) {
                ECFieldElement eCFieldElement23 = eCFieldElement13.add(eCFieldElement2).square();
                ECFieldElement eCFieldElement24 = eCFieldElement22.isOne() ? eCFieldElement18.add(eCFieldElement16).square() : eCFieldElement18.squarePlusProduct(eCFieldElement22, eCFieldElement16.square());
                ECFieldElement eCFieldElement25 = eCFieldElement23.add(eCFieldElement19).add(eCFieldElement16).multiply(eCFieldElement23).add(eCFieldElement24).add(eCFieldElement20);
                if (eCFieldElement17.isZero()) {
                    eCFieldElement25 = eCFieldElement25.add(eCFieldElement21);
                } else if (!eCFieldElement17.isOne()) {
                    eCFieldElement25 = eCFieldElement25.add(eCFieldElement17.addOne().multiply(eCFieldElement21));
                }
                eCFieldElement = eCFieldElement25;
                return new F2m(eCCurve, eCFieldElement20, eCFieldElement, new ECFieldElement[]{eCFieldElement21}, this.withCompression);
            } else {
                ECFieldElement eCFieldElement26 = bl ? eCFieldElement2 : eCFieldElement2.multiply(eCFieldElement14);
                eCFieldElement = eCFieldElement26.squarePlusProduct(eCFieldElement19, eCFieldElement15).add(eCFieldElement20).add(eCFieldElement21);
            }
            return new F2m(eCCurve, eCFieldElement20, eCFieldElement, new ECFieldElement[]{eCFieldElement21}, this.withCompression);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint twicePlus(ECPoint eCPoint) {
            ECFieldElement eCFieldElement;
            ECCurve eCCurve;
            block12 : {
                block11 : {
                    if (this.isInfinity()) break block11;
                    if (eCPoint.isInfinity()) {
                        return this.twice();
                    }
                    eCCurve = this.getCurve();
                    eCFieldElement = this.x;
                    if (!eCFieldElement.isZero()) break block12;
                }
                return eCPoint;
            }
            switch (eCCurve.getCoordinateSystem()) {
                default: {
                    return this.twice().add(eCPoint);
                }
                case 6: 
            }
            ECFieldElement eCFieldElement2 = eCPoint.x;
            ECFieldElement eCFieldElement3 = eCPoint.zs[0];
            if (eCFieldElement2.isZero() || !eCFieldElement3.isOne()) {
                return this.twice().add(eCPoint);
            }
            ECFieldElement eCFieldElement4 = this.y;
            ECFieldElement eCFieldElement5 = this.zs[0];
            ECFieldElement eCFieldElement6 = eCPoint.y;
            ECFieldElement eCFieldElement7 = eCFieldElement.square();
            ECFieldElement eCFieldElement8 = eCFieldElement4.square();
            ECFieldElement eCFieldElement9 = eCFieldElement5.square();
            ECFieldElement eCFieldElement10 = eCFieldElement4.multiply(eCFieldElement5);
            ECFieldElement eCFieldElement11 = eCCurve.getA().multiply(eCFieldElement9).add(eCFieldElement8).add(eCFieldElement10);
            ECFieldElement eCFieldElement12 = eCFieldElement6.addOne();
            ECFieldElement eCFieldElement13 = eCCurve.getA().add(eCFieldElement12).multiply(eCFieldElement9).add(eCFieldElement8).multiplyPlusProduct(eCFieldElement11, eCFieldElement7, eCFieldElement9);
            ECFieldElement eCFieldElement14 = eCFieldElement2.multiply(eCFieldElement9);
            ECFieldElement eCFieldElement15 = eCFieldElement14.add(eCFieldElement11).square();
            if (eCFieldElement15.isZero()) {
                if (eCFieldElement13.isZero()) {
                    return eCPoint.twice();
                }
                return eCCurve.getInfinity();
            }
            if (eCFieldElement13.isZero()) {
                return new F2m(eCCurve, eCFieldElement13, eCCurve.getB().sqrt(), this.withCompression);
            }
            ECFieldElement eCFieldElement16 = eCFieldElement13.square().multiply(eCFieldElement14);
            ECFieldElement eCFieldElement17 = eCFieldElement13.multiply(eCFieldElement15).multiply(eCFieldElement9);
            return new F2m(eCCurve, eCFieldElement16, eCFieldElement13.add(eCFieldElement15).square().multiplyPlusProduct(eCFieldElement11, eCFieldElement12, eCFieldElement17), new ECFieldElement[]{eCFieldElement17}, this.withCompression);
        }
    }

    public static class Fp
    extends AbstractFp {
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        /*
         * Enabled aggressive block sorting
         */
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean bl) {
            boolean bl2 = true;
            super(eCCurve, eCFieldElement, eCFieldElement2);
            boolean bl3 = eCFieldElement == null ? bl2 : false;
            if (eCFieldElement2 != null) {
                bl2 = false;
            }
            if (bl3 != bl2) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = bl;
        }

        Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] arreCFieldElement, boolean bl) {
            super(eCCurve, eCFieldElement, eCFieldElement2, arreCFieldElement);
            this.withCompression = bl;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint add(ECPoint eCPoint) {
            ECFieldElement[] arreCFieldElement;
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            ECFieldElement eCFieldElement3;
            ECFieldElement eCFieldElement4;
            if (this.isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            if (this == eCPoint) {
                return this.twice();
            }
            ECCurve eCCurve = this.getCurve();
            int n = eCCurve.getCoordinateSystem();
            ECFieldElement eCFieldElement5 = this.x;
            ECFieldElement eCFieldElement6 = this.y;
            ECFieldElement eCFieldElement7 = eCPoint.x;
            ECFieldElement eCFieldElement8 = eCPoint.y;
            switch (n) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement9 = eCFieldElement7.subtract(eCFieldElement5);
                    ECFieldElement eCFieldElement10 = eCFieldElement8.subtract(eCFieldElement6);
                    if (eCFieldElement9.isZero()) {
                        if (!eCFieldElement10.isZero()) return eCCurve.getInfinity();
                        return this.twice();
                    }
                    ECFieldElement eCFieldElement11 = eCFieldElement10.divide(eCFieldElement9);
                    ECFieldElement eCFieldElement12 = eCFieldElement11.square().subtract(eCFieldElement5).subtract(eCFieldElement7);
                    return new Fp(eCCurve, eCFieldElement12, eCFieldElement11.multiply(eCFieldElement5.subtract(eCFieldElement12)).subtract(eCFieldElement6), this.withCompression);
                }
                case 1: {
                    ECFieldElement eCFieldElement13;
                    ECFieldElement eCFieldElement14 = this.zs[0];
                    ECFieldElement eCFieldElement15 = eCPoint.zs[0];
                    boolean bl = eCFieldElement14.isOne();
                    boolean bl2 = eCFieldElement15.isOne();
                    if (!bl) {
                        eCFieldElement8 = eCFieldElement8.multiply(eCFieldElement14);
                    }
                    if (!bl2) {
                        eCFieldElement6 = eCFieldElement6.multiply(eCFieldElement15);
                    }
                    ECFieldElement eCFieldElement16 = eCFieldElement8.subtract(eCFieldElement6);
                    if (!bl) {
                        eCFieldElement7 = eCFieldElement7.multiply(eCFieldElement14);
                    }
                    if (!bl2) {
                        eCFieldElement5 = eCFieldElement5.multiply(eCFieldElement15);
                    }
                    if ((eCFieldElement13 = eCFieldElement7.subtract(eCFieldElement5)).isZero()) {
                        if (!eCFieldElement16.isZero()) return eCCurve.getInfinity();
                        return this.twice();
                    }
                    if (bl) {
                        eCFieldElement14 = eCFieldElement15;
                    } else if (!bl2) {
                        eCFieldElement14 = eCFieldElement14.multiply(eCFieldElement15);
                    }
                    ECFieldElement eCFieldElement17 = eCFieldElement13.square();
                    ECFieldElement eCFieldElement18 = eCFieldElement17.multiply(eCFieldElement13);
                    ECFieldElement eCFieldElement19 = eCFieldElement17.multiply(eCFieldElement5);
                    ECFieldElement eCFieldElement20 = eCFieldElement16.square().multiply(eCFieldElement14).subtract(eCFieldElement18).subtract(this.two(eCFieldElement19));
                    return new Fp(eCCurve, eCFieldElement13.multiply(eCFieldElement20), eCFieldElement19.subtract(eCFieldElement20).multiplyMinusProduct(eCFieldElement16, eCFieldElement6, eCFieldElement18), new ECFieldElement[]{eCFieldElement18.multiply(eCFieldElement14)}, this.withCompression);
                }
                case 2: 
                case 4: 
            }
            ECFieldElement eCFieldElement21 = this.zs[0];
            ECFieldElement eCFieldElement22 = eCPoint.zs[0];
            boolean bl = eCFieldElement21.isOne();
            if (!bl && eCFieldElement21.equals((Object)eCFieldElement22)) {
                ECFieldElement eCFieldElement23 = eCFieldElement5.subtract(eCFieldElement7);
                ECFieldElement eCFieldElement24 = eCFieldElement6.subtract(eCFieldElement8);
                if (eCFieldElement23.isZero()) {
                    if (!eCFieldElement24.isZero()) return eCCurve.getInfinity();
                    return this.twice();
                }
                ECFieldElement eCFieldElement25 = eCFieldElement23.square();
                ECFieldElement eCFieldElement26 = eCFieldElement5.multiply(eCFieldElement25);
                ECFieldElement eCFieldElement27 = eCFieldElement7.multiply(eCFieldElement25);
                ECFieldElement eCFieldElement28 = eCFieldElement26.subtract(eCFieldElement27).multiply(eCFieldElement6);
                eCFieldElement2 = eCFieldElement24.square().subtract(eCFieldElement26).subtract(eCFieldElement27);
                eCFieldElement4 = eCFieldElement26.subtract(eCFieldElement2).multiply(eCFieldElement24).subtract(eCFieldElement28);
                eCFieldElement = eCFieldElement23.multiply(eCFieldElement21);
                eCFieldElement3 = null;
            } else {
                ECFieldElement eCFieldElement29;
                ECFieldElement eCFieldElement30;
                boolean bl3;
                if (bl) {
                    eCFieldElement29 = eCFieldElement8;
                    eCFieldElement30 = eCFieldElement7;
                } else {
                    ECFieldElement eCFieldElement31 = eCFieldElement21.square();
                    eCFieldElement30 = eCFieldElement31.multiply(eCFieldElement7);
                    eCFieldElement29 = eCFieldElement31.multiply(eCFieldElement21).multiply(eCFieldElement8);
                }
                if (!(bl3 = eCFieldElement22.isOne())) {
                    ECFieldElement eCFieldElement32 = eCFieldElement22.square();
                    eCFieldElement5 = eCFieldElement32.multiply(eCFieldElement5);
                    eCFieldElement6 = eCFieldElement32.multiply(eCFieldElement22).multiply(eCFieldElement6);
                }
                ECFieldElement eCFieldElement33 = eCFieldElement5.subtract(eCFieldElement30);
                ECFieldElement eCFieldElement34 = eCFieldElement6.subtract(eCFieldElement29);
                if (eCFieldElement33.isZero()) {
                    if (!eCFieldElement34.isZero()) return eCCurve.getInfinity();
                    return this.twice();
                }
                ECFieldElement eCFieldElement35 = eCFieldElement33.square();
                ECFieldElement eCFieldElement36 = eCFieldElement35.multiply(eCFieldElement33);
                ECFieldElement eCFieldElement37 = eCFieldElement35.multiply(eCFieldElement5);
                ECFieldElement eCFieldElement38 = eCFieldElement34.square().add(eCFieldElement36).subtract(this.two(eCFieldElement37));
                ECFieldElement eCFieldElement39 = eCFieldElement37.subtract(eCFieldElement38).multiplyMinusProduct(eCFieldElement34, eCFieldElement36, eCFieldElement6);
                ECFieldElement eCFieldElement40 = !bl ? eCFieldElement33.multiply(eCFieldElement21) : eCFieldElement33;
                if (!bl3) {
                    eCFieldElement40 = eCFieldElement40.multiply(eCFieldElement22);
                }
                if (eCFieldElement40 == eCFieldElement33) {
                    eCFieldElement = eCFieldElement40;
                    eCFieldElement3 = eCFieldElement35;
                    eCFieldElement4 = eCFieldElement39;
                    eCFieldElement2 = eCFieldElement38;
                } else {
                    eCFieldElement = eCFieldElement40;
                    eCFieldElement4 = eCFieldElement39;
                    eCFieldElement2 = eCFieldElement38;
                    eCFieldElement3 = null;
                }
            }
            if (n == 4) {
                arreCFieldElement = new ECFieldElement[]{eCFieldElement, this.calculateJacobianModifiedW(eCFieldElement, eCFieldElement3)};
                return new Fp(eCCurve, eCFieldElement2, eCFieldElement4, arreCFieldElement, this.withCompression);
            }
            arreCFieldElement = new ECFieldElement[]{eCFieldElement};
            return new Fp(eCCurve, eCFieldElement2, eCFieldElement4, arreCFieldElement, this.withCompression);
        }

        protected ECFieldElement calculateJacobianModifiedW(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            ECFieldElement eCFieldElement3 = this.getCurve().getA();
            if (eCFieldElement3.isZero() || eCFieldElement.isOne()) {
                return eCFieldElement3;
            }
            if (eCFieldElement2 == null) {
                eCFieldElement2 = eCFieldElement.square();
            }
            ECFieldElement eCFieldElement4 = eCFieldElement2.square();
            ECFieldElement eCFieldElement5 = eCFieldElement3.negate();
            if (eCFieldElement5.bitLength() < eCFieldElement3.bitLength()) {
                return eCFieldElement4.multiply(eCFieldElement5).negate();
            }
            return eCFieldElement4.multiply(eCFieldElement3);
        }

        @Override
        protected ECPoint detach() {
            return new Fp(null, this.getAffineXCoord(), this.getAffineYCoord());
        }

        protected ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
            return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
        }

        protected ECFieldElement eight(ECFieldElement eCFieldElement) {
            return this.four(this.two(eCFieldElement));
        }

        protected ECFieldElement four(ECFieldElement eCFieldElement) {
            return this.two(this.two(eCFieldElement));
        }

        protected ECFieldElement getJacobianModifiedW() {
            ECFieldElement eCFieldElement = this.zs[1];
            if (eCFieldElement == null) {
                ECFieldElement[] arreCFieldElement = this.zs;
                arreCFieldElement[1] = eCFieldElement = this.calculateJacobianModifiedW(this.zs[0], null);
            }
            return eCFieldElement;
        }

        @Override
        public ECFieldElement getZCoord(int n) {
            if (n == 1 && 4 == this.getCurveCoordinateSystem()) {
                return this.getJacobianModifiedW();
            }
            return super.getZCoord(n);
        }

        @Override
        public ECPoint negate() {
            if (this.isInfinity()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            if (eCCurve.getCoordinateSystem() != 0) {
                return new Fp(eCCurve, this.x, this.y.negate(), this.zs, this.withCompression);
            }
            return new Fp(eCCurve, this.x, this.y.negate(), this.withCompression);
        }

        protected ECFieldElement three(ECFieldElement eCFieldElement) {
            return this.two(eCFieldElement).add(eCFieldElement);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint threeTimes() {
            ECFieldElement eCFieldElement;
            if (this.isInfinity() || (eCFieldElement = this.y).isZero()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            switch (eCCurve.getCoordinateSystem()) {
                default: {
                    return this.twice().add(this);
                }
                case 0: {
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement eCFieldElement3 = this.two(eCFieldElement);
                    ECFieldElement eCFieldElement4 = eCFieldElement3.square();
                    ECFieldElement eCFieldElement5 = this.three(eCFieldElement2.square()).add(this.getCurve().getA());
                    ECFieldElement eCFieldElement6 = eCFieldElement5.square();
                    ECFieldElement eCFieldElement7 = this.three(eCFieldElement2).multiply(eCFieldElement4).subtract(eCFieldElement6);
                    if (eCFieldElement7.isZero()) {
                        return this.getCurve().getInfinity();
                    }
                    ECFieldElement eCFieldElement8 = eCFieldElement7.multiply(eCFieldElement3).invert();
                    ECFieldElement eCFieldElement9 = eCFieldElement7.multiply(eCFieldElement8).multiply(eCFieldElement5);
                    ECFieldElement eCFieldElement10 = eCFieldElement4.square().multiply(eCFieldElement8).subtract(eCFieldElement9);
                    ECFieldElement eCFieldElement11 = eCFieldElement10.subtract(eCFieldElement9).multiply(eCFieldElement9.add(eCFieldElement10)).add(eCFieldElement2);
                    return new Fp(eCCurve, eCFieldElement11, eCFieldElement2.subtract(eCFieldElement11).multiply(eCFieldElement10).subtract(eCFieldElement), this.withCompression);
                }
                case 4: 
            }
            return this.twiceJacobianModified(false).add(this);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint timesPow2(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("'e' cannot be negative");
            }
            if (n == 0 || this.isInfinity()) {
                return this;
            }
            if (n == 1) {
                return this.twice();
            }
            ECCurve eCCurve = this.getCurve();
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return eCCurve.getInfinity();
            }
            int n2 = eCCurve.getCoordinateSystem();
            ECFieldElement eCFieldElement2 = eCCurve.getA();
            ECFieldElement eCFieldElement3 = this.x;
            ECFieldElement eCFieldElement4 = this.zs.length < 1 ? eCCurve.fromBigInteger(ECConstants.ONE) : this.zs[0];
            if (!eCFieldElement4.isOne()) {
                switch (n2) {
                    case 1: {
                        ECFieldElement eCFieldElement5 = eCFieldElement4.square();
                        eCFieldElement3 = eCFieldElement3.multiply(eCFieldElement4);
                        eCFieldElement = eCFieldElement.multiply(eCFieldElement5);
                        eCFieldElement2 = this.calculateJacobianModifiedW(eCFieldElement4, eCFieldElement5);
                        break;
                    }
                    case 2: {
                        eCFieldElement2 = this.calculateJacobianModifiedW(eCFieldElement4, null);
                        break;
                    }
                    case 4: {
                        eCFieldElement2 = this.getJacobianModifiedW();
                    }
                }
            }
            ECFieldElement eCFieldElement6 = eCFieldElement4;
            ECFieldElement eCFieldElement7 = eCFieldElement2;
            ECFieldElement eCFieldElement8 = eCFieldElement;
            for (int i = 0; i < n; ++i) {
                ECFieldElement eCFieldElement9;
                if (eCFieldElement8.isZero()) {
                    return eCCurve.getInfinity();
                }
                ECFieldElement eCFieldElement10 = this.three(eCFieldElement3.square());
                ECFieldElement eCFieldElement11 = this.two(eCFieldElement8);
                ECFieldElement eCFieldElement12 = eCFieldElement11.multiply(eCFieldElement8);
                ECFieldElement eCFieldElement13 = this.two(eCFieldElement3.multiply(eCFieldElement12));
                ECFieldElement eCFieldElement14 = this.two(eCFieldElement12.square());
                if (!eCFieldElement7.isZero()) {
                    eCFieldElement10 = eCFieldElement10.add(eCFieldElement7);
                    eCFieldElement9 = this.two(eCFieldElement14.multiply(eCFieldElement7));
                } else {
                    eCFieldElement9 = eCFieldElement7;
                }
                ECFieldElement eCFieldElement15 = eCFieldElement10.square().subtract(this.two(eCFieldElement13));
                ECFieldElement eCFieldElement16 = eCFieldElement10.multiply(eCFieldElement13.subtract(eCFieldElement15)).subtract(eCFieldElement14);
                ECFieldElement eCFieldElement17 = eCFieldElement6.isOne() ? eCFieldElement11 : eCFieldElement11.multiply(eCFieldElement6);
                eCFieldElement6 = eCFieldElement17;
                eCFieldElement8 = eCFieldElement16;
                ECFieldElement eCFieldElement18 = eCFieldElement9;
                eCFieldElement3 = eCFieldElement15;
                eCFieldElement7 = eCFieldElement18;
            }
            switch (n2) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement19 = eCFieldElement6.invert();
                    ECFieldElement eCFieldElement20 = eCFieldElement19.square();
                    ECFieldElement eCFieldElement21 = eCFieldElement20.multiply(eCFieldElement19);
                    return new Fp(eCCurve, eCFieldElement3.multiply(eCFieldElement20), eCFieldElement8.multiply(eCFieldElement21), this.withCompression);
                }
                case 1: {
                    return new Fp(eCCurve, eCFieldElement3.multiply(eCFieldElement6), eCFieldElement8, new ECFieldElement[]{eCFieldElement6.multiply(eCFieldElement6.square())}, this.withCompression);
                }
                case 2: {
                    return new Fp(eCCurve, eCFieldElement3, eCFieldElement8, new ECFieldElement[]{eCFieldElement6}, this.withCompression);
                }
                case 4: 
            }
            return new Fp(eCCurve, eCFieldElement3, eCFieldElement8, new ECFieldElement[]{eCFieldElement6, eCFieldElement7}, this.withCompression);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public ECPoint twice() {
            ECFieldElement eCFieldElement;
            ECFieldElement eCFieldElement2;
            ECFieldElement eCFieldElement3;
            if (this.isInfinity()) {
                return this;
            }
            ECCurve eCCurve = this.getCurve();
            ECFieldElement eCFieldElement4 = this.y;
            if (eCFieldElement4.isZero()) {
                return eCCurve.getInfinity();
            }
            int n = eCCurve.getCoordinateSystem();
            ECFieldElement eCFieldElement5 = this.x;
            switch (n) {
                default: {
                    throw new IllegalStateException("unsupported coordinate system");
                }
                case 0: {
                    ECFieldElement eCFieldElement6 = this.three(eCFieldElement5.square()).add(this.getCurve().getA()).divide(this.two(eCFieldElement4));
                    ECFieldElement eCFieldElement7 = eCFieldElement6.square().subtract(this.two(eCFieldElement5));
                    return new Fp(eCCurve, eCFieldElement7, eCFieldElement6.multiply(eCFieldElement5.subtract(eCFieldElement7)).subtract(eCFieldElement4), this.withCompression);
                }
                case 1: {
                    ECFieldElement eCFieldElement8 = this.zs[0];
                    boolean bl = eCFieldElement8.isOne();
                    ECFieldElement eCFieldElement9 = eCCurve.getA();
                    if (!eCFieldElement9.isZero() && !bl) {
                        eCFieldElement9 = eCFieldElement9.multiply(eCFieldElement8.square());
                    }
                    ECFieldElement eCFieldElement10 = eCFieldElement9.add(this.three(eCFieldElement5.square()));
                    ECFieldElement eCFieldElement11 = bl ? eCFieldElement4 : eCFieldElement4.multiply(eCFieldElement8);
                    ECFieldElement eCFieldElement12 = bl ? eCFieldElement4.square() : eCFieldElement11.multiply(eCFieldElement4);
                    ECFieldElement eCFieldElement13 = this.four(eCFieldElement5.multiply(eCFieldElement12));
                    ECFieldElement eCFieldElement14 = eCFieldElement10.square().subtract(this.two(eCFieldElement13));
                    ECFieldElement eCFieldElement15 = this.two(eCFieldElement11);
                    ECFieldElement eCFieldElement16 = eCFieldElement14.multiply(eCFieldElement15);
                    ECFieldElement eCFieldElement17 = this.two(eCFieldElement12);
                    ECFieldElement eCFieldElement18 = eCFieldElement13.subtract(eCFieldElement14).multiply(eCFieldElement10).subtract(this.two(eCFieldElement17.square()));
                    ECFieldElement eCFieldElement19 = bl ? this.two(eCFieldElement17) : eCFieldElement15.square();
                    return new Fp(eCCurve, eCFieldElement16, eCFieldElement18, new ECFieldElement[]{this.two(eCFieldElement19).multiply(eCFieldElement11)}, this.withCompression);
                }
                case 2: {
                    ECFieldElement eCFieldElement20;
                    ECFieldElement eCFieldElement21;
                    ECFieldElement eCFieldElement22 = this.zs[0];
                    boolean bl = eCFieldElement22.isOne();
                    ECFieldElement eCFieldElement23 = eCFieldElement4.square();
                    ECFieldElement eCFieldElement24 = eCFieldElement23.square();
                    ECFieldElement eCFieldElement25 = eCCurve.getA();
                    ECFieldElement eCFieldElement26 = eCFieldElement25.negate();
                    if (eCFieldElement26.toBigInteger().equals((Object)BigInteger.valueOf((long)3L))) {
                        ECFieldElement eCFieldElement27 = bl ? eCFieldElement22 : eCFieldElement22.square();
                        ECFieldElement eCFieldElement28 = this.three(eCFieldElement5.add(eCFieldElement27).multiply(eCFieldElement5.subtract(eCFieldElement27)));
                        eCFieldElement21 = this.four(eCFieldElement23.multiply(eCFieldElement5));
                        eCFieldElement20 = eCFieldElement28;
                    } else {
                        ECFieldElement eCFieldElement29 = this.three(eCFieldElement5.square());
                        if (bl) {
                            eCFieldElement29 = eCFieldElement29.add(eCFieldElement25);
                        } else if (!eCFieldElement25.isZero()) {
                            ECFieldElement eCFieldElement30 = eCFieldElement22.square().square();
                            eCFieldElement29 = eCFieldElement26.bitLength() < eCFieldElement25.bitLength() ? eCFieldElement29.subtract(eCFieldElement30.multiply(eCFieldElement26)) : eCFieldElement29.add(eCFieldElement30.multiply(eCFieldElement25));
                        }
                        ECFieldElement eCFieldElement31 = this.four(eCFieldElement5.multiply(eCFieldElement23));
                        eCFieldElement20 = eCFieldElement29;
                        eCFieldElement21 = eCFieldElement31;
                    }
                    eCFieldElement = eCFieldElement20.square().subtract(this.two(eCFieldElement21));
                    eCFieldElement3 = eCFieldElement21.subtract(eCFieldElement).multiply(eCFieldElement20).subtract(this.eight(eCFieldElement24));
                    ECFieldElement eCFieldElement32 = this.two(eCFieldElement4);
                    eCFieldElement2 = !bl ? eCFieldElement32.multiply(eCFieldElement22) : eCFieldElement32;
                }
                case 4: {
                    return this.twiceJacobianModified(true);
                }
            }
            return new Fp(eCCurve, eCFieldElement, eCFieldElement3, new ECFieldElement[]{eCFieldElement2}, this.withCompression);
        }

        /*
         * Enabled aggressive block sorting
         */
        protected Fp twiceJacobianModified(boolean bl) {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement eCFieldElement3 = this.zs[0];
            ECFieldElement eCFieldElement4 = this.getJacobianModifiedW();
            ECFieldElement eCFieldElement5 = this.three(eCFieldElement.square()).add(eCFieldElement4);
            ECFieldElement eCFieldElement6 = this.two(eCFieldElement2);
            ECFieldElement eCFieldElement7 = eCFieldElement6.multiply(eCFieldElement2);
            ECFieldElement eCFieldElement8 = this.two(eCFieldElement.multiply(eCFieldElement7));
            ECFieldElement eCFieldElement9 = eCFieldElement5.square().subtract(this.two(eCFieldElement8));
            ECFieldElement eCFieldElement10 = this.two(eCFieldElement7.square());
            ECFieldElement eCFieldElement11 = eCFieldElement5.multiply(eCFieldElement8.subtract(eCFieldElement9)).subtract(eCFieldElement10);
            ECFieldElement eCFieldElement12 = bl ? this.two(eCFieldElement10.multiply(eCFieldElement4)) : null;
            ECFieldElement eCFieldElement13 = eCFieldElement3.isOne() ? eCFieldElement6 : eCFieldElement6.multiply(eCFieldElement3);
            return new Fp(this.getCurve(), eCFieldElement9, eCFieldElement11, new ECFieldElement[]{eCFieldElement13, eCFieldElement12}, this.withCompression);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public ECPoint twicePlus(ECPoint eCPoint) {
            if (this == eCPoint) {
                return this.threeTimes();
            }
            if (this.isInfinity()) return eCPoint;
            if (eCPoint.isInfinity()) {
                return this.twice();
            }
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) return eCPoint;
            ECCurve eCCurve = this.getCurve();
            switch (eCCurve.getCoordinateSystem()) {
                default: {
                    return this.twice().add(eCPoint);
                }
                case 0: {
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement eCFieldElement3 = eCPoint.x;
                    ECFieldElement eCFieldElement4 = eCPoint.y;
                    ECFieldElement eCFieldElement5 = eCFieldElement3.subtract(eCFieldElement2);
                    ECFieldElement eCFieldElement6 = eCFieldElement4.subtract(eCFieldElement);
                    if (eCFieldElement5.isZero()) {
                        if (!eCFieldElement6.isZero()) return this;
                        return this.threeTimes();
                    }
                    ECFieldElement eCFieldElement7 = eCFieldElement5.square();
                    ECFieldElement eCFieldElement8 = eCFieldElement6.square();
                    ECFieldElement eCFieldElement9 = eCFieldElement7.multiply(this.two(eCFieldElement2).add(eCFieldElement3)).subtract(eCFieldElement8);
                    if (eCFieldElement9.isZero()) {
                        return eCCurve.getInfinity();
                    }
                    ECFieldElement eCFieldElement10 = eCFieldElement9.multiply(eCFieldElement5).invert();
                    ECFieldElement eCFieldElement11 = eCFieldElement9.multiply(eCFieldElement10).multiply(eCFieldElement6);
                    ECFieldElement eCFieldElement12 = this.two(eCFieldElement).multiply(eCFieldElement7).multiply(eCFieldElement5).multiply(eCFieldElement10).subtract(eCFieldElement11);
                    ECFieldElement eCFieldElement13 = eCFieldElement12.subtract(eCFieldElement11).multiply(eCFieldElement11.add(eCFieldElement12)).add(eCFieldElement3);
                    return new Fp(eCCurve, eCFieldElement13, eCFieldElement2.subtract(eCFieldElement13).multiply(eCFieldElement12).subtract(eCFieldElement), this.withCompression);
                }
                case 4: 
            }
            return this.twiceJacobianModified(false).add(eCPoint);
        }

        protected ECFieldElement two(ECFieldElement eCFieldElement) {
            return eCFieldElement.add(eCFieldElement);
        }
    }

}

