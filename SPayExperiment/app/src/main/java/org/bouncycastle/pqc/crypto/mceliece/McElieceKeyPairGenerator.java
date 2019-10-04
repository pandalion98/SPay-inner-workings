/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2m;

public class McElieceKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";
    private int fieldPoly;
    private boolean initialized = false;
    private int m;
    private McElieceKeyGenerationParameters mcElieceParams;
    private int n;
    private SecureRandom random;
    private int t;

    private AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        GF2mField gF2mField = new GF2mField(this.m, this.fieldPoly);
        PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(gF2mField, this.t, 'I', this.random);
        PolynomialGF2mSmallM[] arrpolynomialGF2mSmallM = new PolynomialRingGF2m(gF2mField, polynomialGF2mSmallM).getSquareRootMatrix();
        GF2Matrix gF2Matrix = GoppaCode.createCanonicalCheckMatrix(gF2mField, polynomialGF2mSmallM);
        GoppaCode.MaMaPe maMaPe = GoppaCode.computeSystematicForm(gF2Matrix, this.random);
        GF2Matrix gF2Matrix2 = maMaPe.getSecondMatrix();
        Permutation permutation = maMaPe.getPermutation();
        GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix2.computeTranspose();
        GF2Matrix gF2Matrix4 = gF2Matrix3.extendLeftCompactForm();
        int n = gF2Matrix3.getNumRows();
        GF2Matrix[] arrgF2Matrix = GF2Matrix.createRandomRegularMatrixAndItsInverse(n, this.random);
        Permutation permutation2 = new Permutation(this.n, this.random);
        GF2Matrix gF2Matrix5 = (GF2Matrix)((GF2Matrix)arrgF2Matrix[0].rightMultiply(gF2Matrix4)).rightMultiply(permutation2);
        return new AsymmetricCipherKeyPair(new McEliecePublicKeyParameters(OID, this.n, this.t, gF2Matrix5, this.mcElieceParams.getParameters()), new McEliecePrivateKeyParameters(OID, this.n, n, gF2mField, polynomialGF2mSmallM, arrgF2Matrix[1], permutation, permutation2, gF2Matrix, arrpolynomialGF2mSmallM, this.mcElieceParams.getParameters()));
    }

    private void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.mcElieceParams = (McElieceKeyGenerationParameters)keyGenerationParameters;
        this.random = new SecureRandom();
        this.m = this.mcElieceParams.getParameters().getM();
        this.n = this.mcElieceParams.getParameters().getN();
        this.t = this.mcElieceParams.getParameters().getT();
        this.fieldPoly = this.mcElieceParams.getParameters().getFieldPoly();
        this.initialized = true;
    }

    private void initializeDefault() {
        this.initialize(new McElieceKeyGenerationParameters(new SecureRandom(), new McElieceParameters()));
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }
}

