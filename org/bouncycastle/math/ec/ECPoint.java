package org.bouncycastle.math.ec;

import com.samsung.android.spaytui.SpayTuiTAInfo;
import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class ECPoint {
    protected static ECFieldElement[] EMPTY_ZS;
    protected ECCurve curve;
    protected Hashtable preCompTable;
    protected boolean withCompression;
    protected ECFieldElement f333x;
    protected ECFieldElement f334y;
    protected ECFieldElement[] zs;

    public static abstract class AbstractF2m extends ECPoint {
        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        protected boolean satisfiesCurveEquation() {
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement a = curve.getA();
            ECFieldElement b = curve.getB();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement2;
            Object multiply;
            ECFieldElement eCFieldElement3;
            if (coordinateSystem == 6) {
                eCFieldElement2 = this.zs[0];
                boolean isOne = eCFieldElement2.isOne();
                if (eCFieldElement.isZero()) {
                    a = this.y.square();
                    if (!isOne) {
                        multiply = b.multiply(eCFieldElement2.square());
                    }
                    return a.equals(multiply);
                }
                eCFieldElement3 = this.y;
                eCFieldElement = eCFieldElement.square();
                if (isOne) {
                    eCFieldElement2 = eCFieldElement3.square().add(eCFieldElement3).add(a);
                    multiply = eCFieldElement.square().add(b);
                } else {
                    ECFieldElement square = eCFieldElement2.square();
                    ECFieldElement square2 = square.square();
                    eCFieldElement2 = eCFieldElement3.add(eCFieldElement2).multiplyPlusProduct(eCFieldElement3, a, square);
                    multiply = eCFieldElement.squarePlusProduct(b, square2);
                }
                return eCFieldElement2.multiply(eCFieldElement).equals(multiply);
            }
            eCFieldElement2 = this.y;
            eCFieldElement2 = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement2);
            ECFieldElement eCFieldElement4;
            switch (coordinateSystem) {
                case ECCurve.COORD_AFFINE /*0*/:
                    eCFieldElement4 = eCFieldElement2;
                    eCFieldElement2 = b;
                    multiply = eCFieldElement4;
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    square = this.zs[0];
                    if (!square.isOne()) {
                        eCFieldElement3 = square.multiply(square.square());
                        eCFieldElement2 = eCFieldElement2.multiply(square);
                        a = a.multiply(square);
                        eCFieldElement4 = eCFieldElement2;
                        eCFieldElement2 = b.multiply(eCFieldElement3);
                        b = eCFieldElement4;
                        break;
                    }
                    eCFieldElement4 = eCFieldElement2;
                    eCFieldElement2 = b;
                    b = eCFieldElement4;
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
            return multiply.equals(eCFieldElement.add(a).multiply(eCFieldElement.square()).add(eCFieldElement2));
        }
    }

    public static abstract class AbstractFp extends ECPoint {
        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        protected boolean getCompressionYTilde() {
            return getAffineYCoord().testBitZero();
        }

        protected boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement a = this.curve.getA();
            ECFieldElement b = this.curve.getB();
            Object square = eCFieldElement2.square();
            ECFieldElement eCFieldElement3;
            ECFieldElement square2;
            switch (getCurveCoordinateSystem()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    eCFieldElement3 = this.zs[0];
                    if (!eCFieldElement3.isOne()) {
                        square2 = eCFieldElement3.square();
                        ECFieldElement multiply = eCFieldElement3.multiply(square2);
                        square = square.multiply(eCFieldElement3);
                        a = a.multiply(square2);
                        b = b.multiply(multiply);
                        break;
                    }
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                case org.bouncycastle.math.ec.ECFieldElement.F2m.PPB /*3*/:
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    eCFieldElement3 = this.zs[0];
                    if (!eCFieldElement3.isOne()) {
                        eCFieldElement3 = eCFieldElement3.square();
                        square2 = eCFieldElement3.square();
                        eCFieldElement3 = eCFieldElement3.multiply(square2);
                        a = a.multiply(square2);
                        b = b.multiply(eCFieldElement3);
                        break;
                    }
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
            return square.equals(eCFieldElement.square().add(a).multiply(eCFieldElement).add(b));
        }

        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }
    }

    public static class F2m extends AbstractF2m {
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            Object obj = 1;
            super(eCCurve, eCFieldElement, eCFieldElement2);
            Object obj2 = eCFieldElement == null ? 1 : null;
            if (eCFieldElement2 != null) {
                obj = null;
            }
            if (obj2 != obj) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (eCFieldElement != null) {
                org.bouncycastle.math.ec.ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (eCCurve != null) {
                    org.bouncycastle.math.ec.ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = z;
        }

        F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
            this.withCompression = z;
        }

        private static void checkPoints(ECPoint eCPoint, ECPoint eCPoint2) {
            if (eCPoint.curve != eCPoint2.curve) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }

        public ECPoint add(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return addSimple((F2m) eCPoint);
        }

        public F2m addSimple(F2m f2m) {
            if (isInfinity()) {
                return f2m;
            }
            if (f2m.isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = f2m.x;
            ECFieldElement eCFieldElement3;
            ECFieldElement eCFieldElement4;
            ECFieldElement add;
            ECFieldElement eCFieldElement5;
            ECFieldElement add2;
            ECFieldElement add3;
            switch (coordinateSystem) {
                case ECCurve.COORD_AFFINE /*0*/:
                    eCFieldElement3 = this.y;
                    eCFieldElement4 = f2m.y;
                    add = eCFieldElement.add(eCFieldElement2);
                    eCFieldElement4 = eCFieldElement3.add(eCFieldElement4);
                    if (add.isZero()) {
                        return eCFieldElement4.isZero() ? (F2m) twice() : (F2m) curve.getInfinity();
                    } else {
                        eCFieldElement4 = eCFieldElement4.divide(add);
                        add = eCFieldElement4.square().add(eCFieldElement4).add(add).add(curve.getA());
                        return new F2m(curve, add, eCFieldElement4.multiply(eCFieldElement.add(add)).add(add).add(eCFieldElement3), this.withCompression);
                    }
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    add = this.y;
                    eCFieldElement4 = this.zs[0];
                    eCFieldElement3 = f2m.y;
                    eCFieldElement5 = f2m.zs[0];
                    boolean isOne = eCFieldElement5.isOne();
                    add2 = eCFieldElement4.multiply(eCFieldElement3).add(isOne ? add : add.multiply(eCFieldElement5));
                    add3 = eCFieldElement4.multiply(eCFieldElement2).add(isOne ? eCFieldElement : eCFieldElement.multiply(eCFieldElement5));
                    if (add3.isZero()) {
                        return add2.isZero() ? (F2m) twice() : (F2m) curve.getInfinity();
                    } else {
                        eCFieldElement2 = add3.square();
                        ECFieldElement multiply = eCFieldElement2.multiply(add3);
                        eCFieldElement3 = isOne ? eCFieldElement4 : eCFieldElement4.multiply(eCFieldElement5);
                        ECFieldElement add4 = add2.add(add3);
                        ECFieldElement add5 = add4.multiplyPlusProduct(add2, eCFieldElement2, curve.getA()).multiply(eCFieldElement3).add(multiply);
                        eCFieldElement4 = add3.multiply(add5);
                        if (!isOne) {
                            eCFieldElement2 = eCFieldElement2.multiply(eCFieldElement5);
                        }
                        return new F2m(curve, eCFieldElement4, add2.multiplyPlusProduct(eCFieldElement, add3, add).multiplyPlusProduct(eCFieldElement2, add4, add5), new ECFieldElement[]{multiply.multiply(eCFieldElement3)}, this.withCompression);
                    }
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    if (eCFieldElement.isZero()) {
                        return eCFieldElement2.isZero() ? (F2m) curve.getInfinity() : f2m.addSimple(this);
                    } else {
                        ECFieldElement eCFieldElement6;
                        eCFieldElement5 = this.y;
                        add2 = this.zs[0];
                        eCFieldElement4 = f2m.y;
                        add3 = f2m.zs[0];
                        boolean isOne2 = add2.isOne();
                        if (isOne2) {
                            eCFieldElement3 = eCFieldElement4;
                            add = eCFieldElement2;
                        } else {
                            add = eCFieldElement2.multiply(add2);
                            eCFieldElement3 = eCFieldElement4.multiply(add2);
                        }
                        boolean isOne3 = add3.isOne();
                        if (isOne3) {
                            eCFieldElement6 = eCFieldElement;
                            eCFieldElement = eCFieldElement5;
                        } else {
                            eCFieldElement6 = eCFieldElement.multiply(add3);
                            eCFieldElement = eCFieldElement5.multiply(add3);
                        }
                        eCFieldElement3 = eCFieldElement.add(eCFieldElement3);
                        eCFieldElement = eCFieldElement6.add(add);
                        if (eCFieldElement.isZero()) {
                            return eCFieldElement3.isZero() ? (F2m) twice() : (F2m) curve.getInfinity();
                        } else {
                            if (eCFieldElement2.isZero()) {
                                ECPoint normalize = normalize();
                                add = normalize.getXCoord();
                                eCFieldElement3 = normalize.getYCoord();
                                eCFieldElement = eCFieldElement3.add(eCFieldElement4).divide(add);
                                eCFieldElement4 = eCFieldElement.square().add(eCFieldElement).add(add).add(curve.getA());
                                if (eCFieldElement4.isZero()) {
                                    return new F2m(curve, eCFieldElement4, curve.getB().sqrt(), this.withCompression);
                                }
                                add = eCFieldElement.multiply(add.add(eCFieldElement4)).add(eCFieldElement4).add(eCFieldElement3).divide(eCFieldElement4).add(eCFieldElement4);
                                eCFieldElement2 = curve.fromBigInteger(ECConstants.ONE);
                            } else {
                                eCFieldElement = eCFieldElement.square();
                                eCFieldElement4 = eCFieldElement3.multiply(eCFieldElement6);
                                add = eCFieldElement3.multiply(add);
                                eCFieldElement4 = eCFieldElement4.multiply(add);
                                if (eCFieldElement4.isZero()) {
                                    return new F2m(curve, eCFieldElement4, curve.getB().sqrt(), this.withCompression);
                                }
                                eCFieldElement3 = eCFieldElement3.multiply(eCFieldElement);
                                if (!isOne3) {
                                    eCFieldElement3 = eCFieldElement3.multiply(add3);
                                }
                                add = add.add(eCFieldElement).squarePlusProduct(eCFieldElement3, eCFieldElement5.add(add2));
                                eCFieldElement2 = !isOne2 ? eCFieldElement3.multiply(add2) : eCFieldElement3;
                            }
                            return new F2m(curve, eCFieldElement4, add, new ECFieldElement[]{eCFieldElement2}, this.withCompression);
                        }
                    }
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        protected ECPoint detach() {
            return new F2m(null, getAffineXCoord(), getAffineYCoord());
        }

        protected boolean getCompressionYTilde() {
            ECFieldElement rawXCoord = getRawXCoord();
            if (rawXCoord.isZero()) {
                return false;
            }
            ECFieldElement rawYCoord = getRawYCoord();
            switch (getCurveCoordinateSystem()) {
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    return rawYCoord.testBitZero() != rawXCoord.testBitZero();
                default:
                    return rawYCoord.divide(rawXCoord).testBitZero();
            }
        }

        public ECFieldElement getYCoord() {
            int curveCoordinateSystem = getCurveCoordinateSystem();
            switch (curveCoordinateSystem) {
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    ECFieldElement eCFieldElement = this.x;
                    ECFieldElement eCFieldElement2 = this.y;
                    if (isInfinity() || eCFieldElement.isZero()) {
                        return eCFieldElement2;
                    }
                    eCFieldElement2 = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement);
                    if (6 != curveCoordinateSystem) {
                        return eCFieldElement2;
                    }
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return !eCFieldElement3.isOne() ? eCFieldElement2.divide(eCFieldElement3) : eCFieldElement2;
                default:
                    return this.y;
            }
        }

        public ECPoint negate() {
            if (isInfinity()) {
                return this;
            }
            ECFieldElement eCFieldElement = this.x;
            if (eCFieldElement.isZero()) {
                return this;
            }
            ECFieldElement eCFieldElement2;
            ECFieldElement eCFieldElement3;
            switch (getCurveCoordinateSystem()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    return new F2m(this.curve, eCFieldElement, this.y.add(eCFieldElement), this.withCompression);
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    eCFieldElement2 = this.y;
                    eCFieldElement3 = this.zs[0];
                    return new F2m(this.curve, eCFieldElement, eCFieldElement2.add(eCFieldElement), new ECFieldElement[]{eCFieldElement3}, this.withCompression);
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    return new F2m(this.curve, eCFieldElement, this.y.addOne(), this.withCompression);
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    eCFieldElement2 = this.y;
                    eCFieldElement3 = this.zs[0];
                    return new F2m(this.curve, eCFieldElement, eCFieldElement2.add(eCFieldElement3), new ECFieldElement[]{eCFieldElement3}, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        public ECPoint scaleX(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            ECFieldElement rawXCoord;
            ECFieldElement rawYCoord;
            switch (getCurveCoordinateSystem()) {
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    rawXCoord = getRawXCoord();
                    rawYCoord = getRawYCoord();
                    return getCurve().createRawPoint(rawXCoord, rawYCoord.add(rawXCoord).divide(eCFieldElement).add(rawXCoord.multiply(eCFieldElement)), getRawZCoords(), this.withCompression);
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    rawXCoord = getRawXCoord();
                    rawYCoord = getRawYCoord();
                    ECFieldElement eCFieldElement2 = getRawZCoords()[0];
                    ECFieldElement multiply = rawXCoord.multiply(eCFieldElement.square());
                    rawXCoord = rawYCoord.add(rawXCoord).add(multiply);
                    rawYCoord = eCFieldElement2.multiply(eCFieldElement);
                    return getCurve().createRawPoint(multiply, rawXCoord, new ECFieldElement[]{rawYCoord}, this.withCompression);
                default:
                    return super.scaleX(eCFieldElement);
            }
        }

        public ECPoint scaleY(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    ECFieldElement rawXCoord = getRawXCoord();
                    return getCurve().createRawPoint(rawXCoord, getRawYCoord().add(rawXCoord).multiply(eCFieldElement).add(rawXCoord), getRawZCoords(), this.withCompression);
                default:
                    return super.scaleY(eCFieldElement);
            }
        }

        public ECPoint subtract(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return subtractSimple((F2m) eCPoint);
        }

        public F2m subtractSimple(F2m f2m) {
            return f2m.isInfinity() ? this : addSimple((F2m) f2m.negate());
        }

        public F2m tau() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (coordinateSystem) {
                case ECCurve.COORD_AFFINE /*0*/:
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    return new F2m(curve, eCFieldElement.square(), this.y.square(), this.withCompression);
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    ECFieldElement eCFieldElement2 = this.y;
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    return new F2m(curve, eCFieldElement.square(), eCFieldElement2.square(), new ECFieldElement[]{eCFieldElement3.square()}, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            if (eCFieldElement.isZero()) {
                return curve.getInfinity();
            }
            ECFieldElement add;
            ECFieldElement add2;
            ECFieldElement eCFieldElement2;
            ECFieldElement multiply;
            ECFieldElement square;
            ECFieldElement add3;
            switch (curve.getCoordinateSystem()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    add = this.y.divide(eCFieldElement).add(eCFieldElement);
                    add2 = add.square().add(add).add(curve.getA());
                    return new F2m(curve, add2, eCFieldElement.squarePlusProduct(add2, add.addOne()), this.withCompression);
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    add = this.y;
                    eCFieldElement2 = this.zs[0];
                    boolean isOne = eCFieldElement2.isOne();
                    multiply = isOne ? eCFieldElement : eCFieldElement.multiply(eCFieldElement2);
                    if (!isOne) {
                        add = add.multiply(eCFieldElement2);
                    }
                    eCFieldElement2 = eCFieldElement.square();
                    add = eCFieldElement2.add(add);
                    square = multiply.square();
                    add3 = add.add(multiply);
                    add = add3.multiplyPlusProduct(add, square, curve.getA());
                    add2 = multiply.multiply(add);
                    eCFieldElement2 = eCFieldElement2.square().multiplyPlusProduct(multiply, add, add3);
                    square = multiply.multiply(square);
                    return new F2m(curve, add2, eCFieldElement2, new ECFieldElement[]{square}, this.withCompression);
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    ECFieldElement eCFieldElement3 = this.y;
                    eCFieldElement2 = this.zs[0];
                    boolean isOne2 = eCFieldElement2.isOne();
                    ECFieldElement multiply2 = isOne2 ? eCFieldElement3 : eCFieldElement3.multiply(eCFieldElement2);
                    add = isOne2 ? eCFieldElement2 : eCFieldElement2.square();
                    square = curve.getA();
                    multiply = isOne2 ? square : square.multiply(add);
                    ECFieldElement add4 = eCFieldElement3.square().add(multiply2).add(multiply);
                    if (add4.isZero()) {
                        return new F2m(curve, add4, curve.getB().sqrt(), this.withCompression);
                    }
                    add2 = add4.square();
                    add3 = isOne2 ? add4 : add4.multiply(add);
                    ECFieldElement b = curve.getB();
                    if (b.bitLength() < (curve.getFieldSize() >> 1)) {
                        eCFieldElement = eCFieldElement3.add(eCFieldElement).square();
                        add = eCFieldElement.add(add4).add(add).multiply(eCFieldElement).add(b.isOne() ? multiply.add(add).square() : multiply.squarePlusProduct(b, add.square())).add(add2);
                        if (square.isZero()) {
                            add = add.add(add3);
                        } else if (!square.isOne()) {
                            add = add.add(square.addOne().multiply(add3));
                        }
                        eCFieldElement2 = add;
                    } else {
                        eCFieldElement2 = (isOne2 ? eCFieldElement : eCFieldElement.multiply(eCFieldElement2)).squarePlusProduct(add4, multiply2).add(add2).add(add3);
                    }
                    return new F2m(curve, add2, eCFieldElement2, new ECFieldElement[]{add3}, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        public ECPoint twicePlus(ECPoint eCPoint) {
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return twice();
            }
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            if (eCFieldElement.isZero()) {
                return eCPoint;
            }
            switch (curve.getCoordinateSystem()) {
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    ECFieldElement eCFieldElement2 = eCPoint.f333x;
                    ECFieldElement eCFieldElement3 = eCPoint.zs[0];
                    if (eCFieldElement2.isZero() || !eCFieldElement3.isOne()) {
                        return twice().add(eCPoint);
                    }
                    eCFieldElement3 = this.y;
                    ECFieldElement eCFieldElement4 = this.zs[0];
                    ECFieldElement eCFieldElement5 = eCPoint.f334y;
                    eCFieldElement = eCFieldElement.square();
                    ECFieldElement square = eCFieldElement3.square();
                    ECFieldElement square2 = eCFieldElement4.square();
                    eCFieldElement3 = curve.getA().multiply(square2).add(square).add(eCFieldElement3.multiply(eCFieldElement4));
                    eCFieldElement4 = eCFieldElement5.addOne();
                    eCFieldElement = curve.getA().add(eCFieldElement4).multiply(square2).add(square).multiplyPlusProduct(eCFieldElement3, eCFieldElement, square2);
                    eCFieldElement2 = eCFieldElement2.multiply(square2);
                    eCFieldElement5 = eCFieldElement2.add(eCFieldElement3).square();
                    if (eCFieldElement5.isZero()) {
                        return eCFieldElement.isZero() ? eCPoint.twice() : curve.getInfinity();
                    } else {
                        if (eCFieldElement.isZero()) {
                            return new F2m(curve, eCFieldElement, curve.getB().sqrt(), this.withCompression);
                        }
                        return new F2m(curve, eCFieldElement.square().multiply(eCFieldElement2), eCFieldElement.add(eCFieldElement5).square().multiplyPlusProduct(eCFieldElement3, eCFieldElement4, eCFieldElement.multiply(eCFieldElement5).multiply(square2)), new ECFieldElement[]{eCFieldElement.multiply(eCFieldElement5).multiply(square2)}, this.withCompression);
                    }
                default:
                    return twice().add(eCPoint);
            }
        }
    }

    public static class Fp extends AbstractFp {
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            Object obj = 1;
            super(eCCurve, eCFieldElement, eCFieldElement2);
            Object obj2 = eCFieldElement == null ? 1 : null;
            if (eCFieldElement2 != null) {
                obj = null;
            }
            if (obj2 != obj) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = z;
        }

        Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
            this.withCompression = z;
        }

        public ECPoint add(ECPoint eCPoint) {
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            if (this == eCPoint) {
                return twice();
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement eCFieldElement3 = eCPoint.f333x;
            ECFieldElement eCFieldElement4 = eCPoint.f334y;
            ECFieldElement subtract;
            ECFieldElement subtract2;
            switch (coordinateSystem) {
                case ECCurve.COORD_AFFINE /*0*/:
                    subtract = eCFieldElement3.subtract(eCFieldElement);
                    subtract2 = eCFieldElement4.subtract(eCFieldElement2);
                    if (subtract.isZero()) {
                        return subtract2.isZero() ? twice() : curve.getInfinity();
                    } else {
                        subtract = subtract2.divide(subtract);
                        subtract2 = subtract.square().subtract(eCFieldElement).subtract(eCFieldElement3);
                        return new Fp(curve, subtract2, subtract.multiply(eCFieldElement.subtract(subtract2)).subtract(eCFieldElement2), this.withCompression);
                    }
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    subtract2 = this.zs[0];
                    subtract = eCPoint.zs[0];
                    boolean isOne = subtract2.isOne();
                    boolean isOne2 = subtract.isOne();
                    if (!isOne) {
                        eCFieldElement4 = eCFieldElement4.multiply(subtract2);
                    }
                    if (!isOne2) {
                        eCFieldElement2 = eCFieldElement2.multiply(subtract);
                    }
                    eCFieldElement4 = eCFieldElement4.subtract(eCFieldElement2);
                    if (!isOne) {
                        eCFieldElement3 = eCFieldElement3.multiply(subtract2);
                    }
                    if (!isOne2) {
                        eCFieldElement = eCFieldElement.multiply(subtract);
                    }
                    eCFieldElement3 = eCFieldElement3.subtract(eCFieldElement);
                    if (eCFieldElement3.isZero()) {
                        return eCFieldElement4.isZero() ? twice() : curve.getInfinity();
                    } else {
                        if (isOne) {
                            subtract2 = subtract;
                        } else if (!isOne2) {
                            subtract2 = subtract2.multiply(subtract);
                        }
                        subtract = eCFieldElement3.square();
                        ECFieldElement multiply = subtract.multiply(eCFieldElement3);
                        subtract = subtract.multiply(eCFieldElement);
                        ECFieldElement subtract3 = eCFieldElement4.square().multiply(subtract2).subtract(multiply).subtract(two(subtract));
                        eCFieldElement = eCFieldElement3.multiply(subtract3);
                        subtract = subtract.subtract(subtract3).multiplyMinusProduct(eCFieldElement4, eCFieldElement2, multiply);
                        eCFieldElement4 = multiply.multiply(subtract2);
                        return new Fp(curve, eCFieldElement, subtract, new ECFieldElement[]{eCFieldElement4}, this.withCompression);
                    }
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    ECFieldElement[] eCFieldElementArr;
                    ECFieldElement eCFieldElement5 = this.zs[0];
                    ECFieldElement eCFieldElement6 = eCPoint.zs[0];
                    boolean isOne3 = eCFieldElement5.isOne();
                    if (isOne3 || !eCFieldElement5.equals(eCFieldElement6)) {
                        if (isOne3) {
                            subtract = eCFieldElement4;
                            subtract2 = eCFieldElement3;
                        } else {
                            subtract = eCFieldElement5.square();
                            subtract2 = subtract.multiply(eCFieldElement3);
                            subtract = subtract.multiply(eCFieldElement5).multiply(eCFieldElement4);
                        }
                        boolean isOne4 = eCFieldElement6.isOne();
                        if (!isOne4) {
                            eCFieldElement4 = eCFieldElement6.square();
                            eCFieldElement = eCFieldElement4.multiply(eCFieldElement);
                            eCFieldElement2 = eCFieldElement4.multiply(eCFieldElement6).multiply(eCFieldElement2);
                        }
                        eCFieldElement4 = eCFieldElement.subtract(subtract2);
                        ECFieldElement subtract4 = eCFieldElement2.subtract(subtract);
                        if (eCFieldElement4.isZero()) {
                            return subtract4.isZero() ? twice() : curve.getInfinity();
                        } else {
                            subtract = eCFieldElement4.square();
                            ECFieldElement multiply2 = subtract.multiply(eCFieldElement4);
                            eCFieldElement = subtract.multiply(eCFieldElement);
                            subtract2 = subtract4.square().add(multiply2).subtract(two(eCFieldElement));
                            eCFieldElement = eCFieldElement.subtract(subtract2).multiplyMinusProduct(subtract4, multiply2, eCFieldElement2);
                            eCFieldElement2 = !isOne3 ? eCFieldElement4.multiply(eCFieldElement5) : eCFieldElement4;
                            if (!isOne4) {
                                eCFieldElement2 = eCFieldElement2.multiply(eCFieldElement6);
                            }
                            if (eCFieldElement2 == eCFieldElement4) {
                                eCFieldElement4 = eCFieldElement2;
                                eCFieldElement2 = subtract;
                                subtract = eCFieldElement;
                                eCFieldElement = subtract2;
                            } else {
                                eCFieldElement4 = eCFieldElement2;
                                subtract = eCFieldElement;
                                eCFieldElement2 = null;
                                eCFieldElement = subtract2;
                            }
                        }
                    } else {
                        subtract2 = eCFieldElement.subtract(eCFieldElement3);
                        subtract = eCFieldElement2.subtract(eCFieldElement4);
                        if (subtract2.isZero()) {
                            return subtract.isZero() ? twice() : curve.getInfinity();
                        } else {
                            eCFieldElement4 = subtract2.square();
                            eCFieldElement6 = eCFieldElement.multiply(eCFieldElement4);
                            eCFieldElement = eCFieldElement3.multiply(eCFieldElement4);
                            eCFieldElement2 = eCFieldElement6.subtract(eCFieldElement).multiply(eCFieldElement2);
                            eCFieldElement = subtract.square().subtract(eCFieldElement6).subtract(eCFieldElement);
                            subtract = eCFieldElement6.subtract(eCFieldElement).multiply(subtract).subtract(eCFieldElement2);
                            eCFieldElement4 = subtract2.multiply(eCFieldElement5);
                            eCFieldElement2 = null;
                        }
                    }
                    if (coordinateSystem == 4) {
                        eCFieldElement2 = calculateJacobianModifiedW(eCFieldElement4, eCFieldElement2);
                        eCFieldElementArr = new ECFieldElement[]{eCFieldElement4, eCFieldElement2};
                    } else {
                        eCFieldElementArr = new ECFieldElement[]{eCFieldElement4};
                    }
                    return new Fp(curve, eCFieldElement, subtract, eCFieldElementArr, this.withCompression);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        protected ECFieldElement calculateJacobianModifiedW(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            ECFieldElement a = getCurve().getA();
            if (a.isZero() || eCFieldElement.isOne()) {
                return a;
            }
            if (eCFieldElement2 == null) {
                eCFieldElement2 = eCFieldElement.square();
            }
            ECFieldElement square = eCFieldElement2.square();
            ECFieldElement negate = a.negate();
            return negate.bitLength() < a.bitLength() ? square.multiply(negate).negate() : square.multiply(a);
        }

        protected ECPoint detach() {
            return new Fp(null, getAffineXCoord(), getAffineYCoord());
        }

        protected ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
            return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
        }

        protected ECFieldElement eight(ECFieldElement eCFieldElement) {
            return four(two(eCFieldElement));
        }

        protected ECFieldElement four(ECFieldElement eCFieldElement) {
            return two(two(eCFieldElement));
        }

        protected ECFieldElement getJacobianModifiedW() {
            ECFieldElement eCFieldElement = this.zs[1];
            if (eCFieldElement != null) {
                return eCFieldElement;
            }
            ECFieldElement[] eCFieldElementArr = this.zs;
            eCFieldElement = calculateJacobianModifiedW(this.zs[0], null);
            eCFieldElementArr[1] = eCFieldElement;
            return eCFieldElement;
        }

        public ECFieldElement getZCoord(int i) {
            return (i == 1 && 4 == getCurveCoordinateSystem()) ? getJacobianModifiedW() : super.getZCoord(i);
        }

        public ECPoint negate() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            return curve.getCoordinateSystem() != 0 ? new Fp(curve, this.x, this.y.negate(), this.zs, this.withCompression) : new Fp(curve, this.x, this.y.negate(), this.withCompression);
        }

        protected ECFieldElement three(ECFieldElement eCFieldElement) {
            return two(eCFieldElement).add(eCFieldElement);
        }

        public ECPoint threeTimes() {
            if (isInfinity()) {
                return this;
            }
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return this;
            }
            ECCurve curve = getCurve();
            switch (curve.getCoordinateSystem()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement two = two(eCFieldElement);
                    ECFieldElement square = two.square();
                    ECFieldElement add = three(eCFieldElement2.square()).add(getCurve().getA());
                    ECFieldElement subtract = three(eCFieldElement2).multiply(square).subtract(add.square());
                    if (subtract.isZero()) {
                        return getCurve().getInfinity();
                    }
                    two = subtract.multiply(two).invert();
                    add = subtract.multiply(two).multiply(add);
                    two = square.square().multiply(two).subtract(add);
                    square = two.subtract(add).multiply(add.add(two)).add(eCFieldElement2);
                    return new Fp(curve, square, eCFieldElement2.subtract(square).multiply(two).subtract(eCFieldElement), this.withCompression);
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return twiceJacobianModified(false).add(this);
                default:
                    return twice().add(this);
            }
        }

        public ECPoint timesPow2(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("'e' cannot be negative");
            } else if (i == 0 || isInfinity()) {
                return this;
            } else {
                if (i == 1) {
                    return twice();
                }
                ECCurve curve = getCurve();
                ECFieldElement eCFieldElement = this.y;
                if (eCFieldElement.isZero()) {
                    return curve.getInfinity();
                }
                int coordinateSystem = curve.getCoordinateSystem();
                ECFieldElement a = curve.getA();
                ECFieldElement eCFieldElement2 = this.x;
                ECFieldElement fromBigInteger = this.zs.length < 1 ? curve.fromBigInteger(ECConstants.ONE) : this.zs[0];
                if (!fromBigInteger.isOne()) {
                    switch (coordinateSystem) {
                        case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                            a = fromBigInteger.square();
                            eCFieldElement2 = eCFieldElement2.multiply(fromBigInteger);
                            eCFieldElement = eCFieldElement.multiply(a);
                            a = calculateJacobianModifiedW(fromBigInteger, a);
                            break;
                        case CipherSpiExt.DECRYPT_MODE /*2*/:
                            a = calculateJacobianModifiedW(fromBigInteger, null);
                            break;
                        case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                            a = getJacobianModifiedW();
                            break;
                    }
                }
                int i2 = 0;
                ECFieldElement eCFieldElement3 = fromBigInteger;
                ECFieldElement eCFieldElement4 = a;
                a = eCFieldElement;
                while (i2 < i) {
                    if (a.isZero()) {
                        return curve.getInfinity();
                    }
                    fromBigInteger = three(eCFieldElement2.square());
                    eCFieldElement = two(a);
                    a = eCFieldElement.multiply(a);
                    ECFieldElement two = two(eCFieldElement2.multiply(a));
                    a = two(a.square());
                    if (eCFieldElement4.isZero()) {
                        eCFieldElement2 = eCFieldElement4;
                    } else {
                        fromBigInteger = fromBigInteger.add(eCFieldElement4);
                        eCFieldElement2 = two(a.multiply(eCFieldElement4));
                    }
                    eCFieldElement4 = fromBigInteger.square().subtract(two(two));
                    i2++;
                    eCFieldElement3 = eCFieldElement3.isOne() ? eCFieldElement : eCFieldElement.multiply(eCFieldElement3);
                    a = fromBigInteger.multiply(two.subtract(eCFieldElement4)).subtract(a);
                    ECFieldElement eCFieldElement5 = eCFieldElement2;
                    eCFieldElement2 = eCFieldElement4;
                    eCFieldElement4 = eCFieldElement5;
                }
                switch (coordinateSystem) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        fromBigInteger = eCFieldElement3.invert();
                        eCFieldElement = fromBigInteger.square();
                        return new Fp(curve, eCFieldElement2.multiply(eCFieldElement), a.multiply(eCFieldElement.multiply(fromBigInteger)), this.withCompression);
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        return new Fp(curve, eCFieldElement2.multiply(eCFieldElement3), a, new ECFieldElement[]{eCFieldElement3.multiply(eCFieldElement3.square())}, this.withCompression);
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        return new Fp(curve, eCFieldElement2, a, new ECFieldElement[]{eCFieldElement3}, this.withCompression);
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        return new Fp(curve, eCFieldElement2, a, new ECFieldElement[]{eCFieldElement3, eCFieldElement4}, this.withCompression);
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
            }
        }

        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return curve.getInfinity();
            }
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement2 = this.x;
            ECFieldElement divide;
            ECFieldElement subtract;
            boolean isOne;
            ECFieldElement add;
            ECFieldElement multiply;
            ECFieldElement two;
            switch (coordinateSystem) {
                case ECCurve.COORD_AFFINE /*0*/:
                    divide = three(eCFieldElement2.square()).add(getCurve().getA()).divide(two(eCFieldElement));
                    subtract = divide.square().subtract(two(eCFieldElement2));
                    return new Fp(curve, subtract, divide.multiply(eCFieldElement2.subtract(subtract)).subtract(eCFieldElement), this.withCompression);
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    subtract = this.zs[0];
                    isOne = subtract.isOne();
                    divide = curve.getA();
                    if (!(divide.isZero() || isOne)) {
                        divide = divide.multiply(subtract.square());
                    }
                    add = divide.add(three(eCFieldElement2.square()));
                    multiply = isOne ? eCFieldElement : eCFieldElement.multiply(subtract);
                    divide = isOne ? eCFieldElement.square() : multiply.multiply(eCFieldElement);
                    eCFieldElement2 = four(eCFieldElement2.multiply(divide));
                    eCFieldElement = add.square().subtract(two(eCFieldElement2));
                    two = two(multiply);
                    subtract = eCFieldElement.multiply(two);
                    divide = two(divide);
                    eCFieldElement2 = eCFieldElement2.subtract(eCFieldElement).multiply(add).subtract(two(divide.square()));
                    eCFieldElement = two(isOne ? two(divide) : two.square()).multiply(multiply);
                    return new Fp(curve, subtract, eCFieldElement2, new ECFieldElement[]{eCFieldElement}, this.withCompression);
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    multiply = this.zs[0];
                    isOne = multiply.isOne();
                    add = eCFieldElement.square();
                    two = add.square();
                    subtract = curve.getA();
                    ECFieldElement negate = subtract.negate();
                    if (negate.toBigInteger().equals(BigInteger.valueOf(3))) {
                        divide = isOne ? multiply : multiply.square();
                        subtract = three(eCFieldElement2.add(divide).multiply(eCFieldElement2.subtract(divide)));
                        divide = four(add.multiply(eCFieldElement2));
                        eCFieldElement2 = subtract;
                    } else {
                        divide = three(eCFieldElement2.square());
                        if (isOne) {
                            divide = divide.add(subtract);
                        } else if (!subtract.isZero()) {
                            ECFieldElement square = multiply.square().square();
                            divide = negate.bitLength() < subtract.bitLength() ? divide.subtract(square.multiply(negate)) : divide.add(square.multiply(subtract));
                        }
                        eCFieldElement2 = divide;
                        divide = four(eCFieldElement2.multiply(add));
                    }
                    subtract = eCFieldElement2.square().subtract(two(divide));
                    eCFieldElement2 = divide.subtract(subtract).multiply(eCFieldElement2).subtract(eight(two));
                    divide = two(eCFieldElement);
                    eCFieldElement = !isOne ? divide.multiply(multiply) : divide;
                    return new Fp(curve, subtract, eCFieldElement2, new ECFieldElement[]{eCFieldElement}, this.withCompression);
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return twiceJacobianModified(true);
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        protected Fp twiceJacobianModified(boolean z) {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement eCFieldElement3 = this.zs[0];
            ECFieldElement jacobianModifiedW = getJacobianModifiedW();
            ECFieldElement add = three(eCFieldElement.square()).add(jacobianModifiedW);
            ECFieldElement two = two(eCFieldElement2);
            ECFieldElement multiply = two.multiply(eCFieldElement2);
            eCFieldElement = two(eCFieldElement.multiply(multiply));
            eCFieldElement2 = add.square().subtract(two(eCFieldElement));
            multiply = two(multiply.square());
            add = add.multiply(eCFieldElement.subtract(eCFieldElement2)).subtract(multiply);
            jacobianModifiedW = z ? two(multiply.multiply(jacobianModifiedW)) : null;
            multiply = eCFieldElement3.isOne() ? two : two.multiply(eCFieldElement3);
            return new Fp(getCurve(), eCFieldElement2, add, new ECFieldElement[]{multiply, jacobianModifiedW}, this.withCompression);
        }

        public ECPoint twicePlus(ECPoint eCPoint) {
            if (this == eCPoint) {
                return threeTimes();
            }
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return twice();
            }
            ECFieldElement eCFieldElement = this.y;
            if (eCFieldElement.isZero()) {
                return eCPoint;
            }
            ECCurve curve = getCurve();
            switch (curve.getCoordinateSystem()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    ECFieldElement eCFieldElement2 = this.x;
                    ECFieldElement eCFieldElement3 = eCPoint.f333x;
                    ECFieldElement eCFieldElement4 = eCPoint.f334y;
                    ECFieldElement subtract = eCFieldElement3.subtract(eCFieldElement2);
                    eCFieldElement4 = eCFieldElement4.subtract(eCFieldElement);
                    if (subtract.isZero()) {
                        return eCFieldElement4.isZero() ? threeTimes() : this;
                    } else {
                        ECFieldElement square = subtract.square();
                        ECFieldElement subtract2 = square.multiply(two(eCFieldElement2).add(eCFieldElement3)).subtract(eCFieldElement4.square());
                        if (subtract2.isZero()) {
                            return curve.getInfinity();
                        }
                        ECFieldElement invert = subtract2.multiply(subtract).invert();
                        eCFieldElement4 = subtract2.multiply(invert).multiply(eCFieldElement4);
                        subtract = two(eCFieldElement).multiply(square).multiply(subtract).multiply(invert).subtract(eCFieldElement4);
                        eCFieldElement3 = subtract.subtract(eCFieldElement4).multiply(eCFieldElement4.add(subtract)).add(eCFieldElement3);
                        return new Fp(curve, eCFieldElement3, eCFieldElement2.subtract(eCFieldElement3).multiply(subtract).subtract(eCFieldElement), this.withCompression);
                    }
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    return twiceJacobianModified(false).add(eCPoint);
                default:
                    return twice().add(eCPoint);
            }
        }

        protected ECFieldElement two(ECFieldElement eCFieldElement) {
            return eCFieldElement.add(eCFieldElement);
        }
    }

    static {
        EMPTY_ZS = new ECFieldElement[0];
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this(eCCurve, eCFieldElement, eCFieldElement2, getInitialZCoords(eCCurve));
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
        this.preCompTable = null;
        this.curve = eCCurve;
        this.f333x = eCFieldElement;
        this.f334y = eCFieldElement2;
        this.zs = eCFieldElementArr;
    }

    protected static ECFieldElement[] getInitialZCoords(ECCurve eCCurve) {
        int coordinateSystem = eCCurve == null ? 0 : eCCurve.getCoordinateSystem();
        switch (coordinateSystem) {
            case ECCurve.COORD_AFFINE /*0*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return EMPTY_ZS;
            default:
                ECFieldElement fromBigInteger = eCCurve.fromBigInteger(ECConstants.ONE);
                switch (coordinateSystem) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        return new ECFieldElement[]{fromBigInteger};
                    case org.bouncycastle.math.ec.ECFieldElement.F2m.PPB /*3*/:
                        return new ECFieldElement[]{fromBigInteger, fromBigInteger, fromBigInteger};
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        return new ECFieldElement[]{fromBigInteger, eCCurve.getA()};
                    default:
                        throw new IllegalArgumentException("unknown coordinate system");
                }
        }
    }

    public abstract ECPoint add(ECPoint eCPoint);

    protected void checkNormalized() {
        if (!isNormalized()) {
            throw new IllegalStateException("point not in normal form");
        }
    }

    protected ECPoint createScaledPoint(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        return getCurve().createRawPoint(getRawXCoord().multiply(eCFieldElement), getRawYCoord().multiply(eCFieldElement2), this.withCompression);
    }

    protected abstract ECPoint detach();

    public boolean equals(Object obj) {
        return obj == this ? true : !(obj instanceof ECPoint) ? false : equals((ECPoint) obj);
    }

    public boolean equals(ECPoint eCPoint) {
        boolean z = true;
        if (eCPoint == null) {
            return false;
        }
        ECCurve curve = getCurve();
        ECCurve curve2 = eCPoint.getCurve();
        if (curve == null) {
            int i = 1;
        } else {
            boolean z2 = false;
        }
        if (curve2 == null) {
            int i2 = 1;
        } else {
            boolean z3 = false;
        }
        boolean isInfinity = isInfinity();
        boolean isInfinity2 = eCPoint.isInfinity();
        if (isInfinity || isInfinity2) {
            if (!(isInfinity && isInfinity2 && (i != 0 || i2 != 0 || curve.equals(curve2)))) {
                z = false;
            }
            return z;
        }
        if (i == 0 || i2 == 0) {
            if (i != 0) {
                eCPoint = eCPoint.normalize();
            } else if (i2 != 0) {
                this = normalize();
            } else if (!curve.equals(curve2)) {
                return false;
            } else {
                ECPoint[] eCPointArr = new ECPoint[]{this, curve.importPoint(eCPoint)};
                curve.normalizeAll(eCPointArr);
                this = eCPointArr[0];
                eCPoint = eCPointArr[1];
            }
        }
        if (!(getXCoord().equals(eCPoint.getXCoord()) && getYCoord().equals(eCPoint.getYCoord()))) {
            z = false;
        }
        return z;
    }

    public ECFieldElement getAffineXCoord() {
        checkNormalized();
        return getXCoord();
    }

    public ECFieldElement getAffineYCoord() {
        checkNormalized();
        return getYCoord();
    }

    protected abstract boolean getCompressionYTilde();

    public ECCurve getCurve() {
        return this.curve;
    }

    protected int getCurveCoordinateSystem() {
        return this.curve == null ? 0 : this.curve.getCoordinateSystem();
    }

    public final ECPoint getDetachedPoint() {
        return normalize().detach();
    }

    public byte[] getEncoded() {
        return getEncoded(this.withCompression);
    }

    public byte[] getEncoded(boolean z) {
        if (isInfinity()) {
            return new byte[1];
        }
        ECPoint normalize = normalize();
        Object encoded = normalize.getXCoord().getEncoded();
        if (z) {
            Object obj = new byte[(encoded.length + 1)];
            obj[0] = (byte) (normalize.getCompressionYTilde() ? 3 : 2);
            System.arraycopy(encoded, 0, obj, 1, encoded.length);
            return obj;
        }
        obj = normalize.getYCoord().getEncoded();
        Object obj2 = new byte[((encoded.length + obj.length) + 1)];
        obj2[0] = (byte) 4;
        System.arraycopy(encoded, 0, obj2, 1, encoded.length);
        System.arraycopy(obj, 0, obj2, encoded.length + 1, obj.length);
        return obj2;
    }

    protected final ECFieldElement getRawXCoord() {
        return this.f333x;
    }

    protected final ECFieldElement getRawYCoord() {
        return this.f334y;
    }

    protected final ECFieldElement[] getRawZCoords() {
        return this.zs;
    }

    public ECFieldElement getX() {
        return normalize().getXCoord();
    }

    public ECFieldElement getXCoord() {
        return this.f333x;
    }

    public ECFieldElement getY() {
        return normalize().getYCoord();
    }

    public ECFieldElement getYCoord() {
        return this.f334y;
    }

    public ECFieldElement getZCoord(int i) {
        return (i < 0 || i >= this.zs.length) ? null : this.zs[i];
    }

    public ECFieldElement[] getZCoords() {
        int length = this.zs.length;
        if (length == 0) {
            return this.zs;
        }
        Object obj = new ECFieldElement[length];
        System.arraycopy(this.zs, 0, obj, 0, length);
        return obj;
    }

    public int hashCode() {
        ECCurve curve = getCurve();
        int hashCode = curve == null ? 0 : curve.hashCode() ^ -1;
        if (isInfinity()) {
            return hashCode;
        }
        ECPoint normalize = normalize();
        return (hashCode ^ (normalize.getXCoord().hashCode() * 17)) ^ (normalize.getYCoord().hashCode() * SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI);
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean isInfinity() {
        return this.f333x == null || this.f334y == null || (this.zs.length > 0 && this.zs[0].isZero());
    }

    public boolean isNormalized() {
        int curveCoordinateSystem = getCurveCoordinateSystem();
        return curveCoordinateSystem == 0 || curveCoordinateSystem == 5 || isInfinity() || this.zs[0].isOne();
    }

    public boolean isValid() {
        return (isInfinity() || getCurve() == null) ? true : !satisfiesCurveEquation() ? false : satisfiesCofactor();
    }

    public ECPoint multiply(BigInteger bigInteger) {
        return getCurve().getMultiplier().multiply(this, bigInteger);
    }

    public abstract ECPoint negate();

    public ECPoint normalize() {
        if (isInfinity()) {
            return this;
        }
        switch (getCurveCoordinateSystem()) {
            case ECCurve.COORD_AFFINE /*0*/:
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return this;
            default:
                ECFieldElement zCoord = getZCoord(0);
                return !zCoord.isOne() ? normalize(zCoord.invert()) : this;
        }
    }

    ECPoint normalize(ECFieldElement eCFieldElement) {
        switch (getCurveCoordinateSystem()) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return createScaledPoint(eCFieldElement, eCFieldElement);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
            case org.bouncycastle.math.ec.ECFieldElement.F2m.PPB /*3*/:
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                ECFieldElement square = eCFieldElement.square();
                return createScaledPoint(square, square.multiply(eCFieldElement));
            default:
                throw new IllegalStateException("not a projective coordinate system");
        }
    }

    protected boolean satisfiesCofactor() {
        BigInteger cofactor = this.curve.getCofactor();
        return cofactor == null || cofactor.equals(ECConstants.ONE) || !ECAlgorithms.referenceMultiply(this, cofactor).isInfinity();
    }

    protected abstract boolean satisfiesCurveEquation();

    public ECPoint scaleX(ECFieldElement eCFieldElement) {
        return isInfinity() ? this : getCurve().createRawPoint(getRawXCoord().multiply(eCFieldElement), getRawYCoord(), getRawZCoords(), this.withCompression);
    }

    public ECPoint scaleY(ECFieldElement eCFieldElement) {
        return isInfinity() ? this : getCurve().createRawPoint(getRawXCoord(), getRawYCoord().multiply(eCFieldElement), getRawZCoords(), this.withCompression);
    }

    public abstract ECPoint subtract(ECPoint eCPoint);

    public ECPoint threeTimes() {
        return twicePlus(this);
    }

    public ECPoint timesPow2(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("'e' cannot be negative");
        }
        while (true) {
            i--;
            if (i < 0) {
                return this;
            }
            this = twice();
        }
    }

    public String toString() {
        if (isInfinity()) {
            return "INF";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        stringBuffer.append(getRawXCoord());
        stringBuffer.append(',');
        stringBuffer.append(getRawYCoord());
        for (Object append : this.zs) {
            stringBuffer.append(',');
            stringBuffer.append(append);
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    public abstract ECPoint twice();

    public ECPoint twicePlus(ECPoint eCPoint) {
        return twice().add(eCPoint);
    }
}
