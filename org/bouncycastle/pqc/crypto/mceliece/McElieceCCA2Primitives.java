package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
import org.bouncycastle.pqc.math.linearalgebra.Vector;

public final class McElieceCCA2Primitives {
    private McElieceCCA2Primitives() {
    }

    public static GF2Vector[] decryptionPrimitive(McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters, GF2Vector gF2Vector) {
        int k = mcElieceCCA2PrivateKeyParameters.getK();
        Permutation p = mcElieceCCA2PrivateKeyParameters.getP();
        GF2mField field = mcElieceCCA2PrivateKeyParameters.getField();
        PolynomialGF2mSmallM goppaPoly = mcElieceCCA2PrivateKeyParameters.getGoppaPoly();
        GF2Matrix h = mcElieceCCA2PrivateKeyParameters.getH();
        Vector vector = (GF2Vector) gF2Vector.multiply(p.computeInverse());
        Vector syndromeDecode = GoppaCode.syndromeDecode((GF2Vector) h.rightMultiply(vector), field, goppaPoly, mcElieceCCA2PrivateKeyParameters.getQInv());
        GF2Vector gF2Vector2 = (GF2Vector) ((GF2Vector) vector.add(syndromeDecode)).multiply(p);
        GF2Vector gF2Vector3 = (GF2Vector) syndromeDecode.multiply(p);
        gF2Vector2 = gF2Vector2.extractRightVector(k);
        return new GF2Vector[]{gF2Vector2, gF2Vector3};
    }

    public static GF2Vector encryptionPrimitive(McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters, GF2Vector gF2Vector, GF2Vector gF2Vector2) {
        return (GF2Vector) mcElieceCCA2PublicKeyParameters.getMatrixG().leftMultiplyLeftCompactForm(gF2Vector).add(gF2Vector2);
    }
}
