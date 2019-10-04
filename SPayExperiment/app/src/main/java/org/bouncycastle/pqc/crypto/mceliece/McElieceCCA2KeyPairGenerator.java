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
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2m;

public class McElieceCCA2KeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";
    private int fieldPoly;
    private boolean initialized = false;
    private int m;
    private McElieceCCA2KeyGenerationParameters mcElieceCCA2Params;
    private int n;
    private SecureRandom random;
    private int t;

    private void initializeDefault() {
        this.init(new McElieceCCA2KeyGenerationParameters(new SecureRandom(), new McElieceCCA2Parameters()));
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
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
        int n = gF2Matrix3.getNumRows();
        return new AsymmetricCipherKeyPair(new McElieceCCA2PublicKeyParameters(OID, this.n, this.t, gF2Matrix3, this.mcElieceCCA2Params.getParameters()), new McElieceCCA2PrivateKeyParameters(OID, this.n, n, gF2mField, polynomialGF2mSmallM, permutation, gF2Matrix, arrpolynomialGF2mSmallM, this.mcElieceCCA2Params.getParameters()));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.mcElieceCCA2Params = (McElieceCCA2KeyGenerationParameters)keyGenerationParameters;
        this.random = new SecureRandom();
        this.m = this.mcElieceCCA2Params.getParameters().getM();
        this.n = this.mcElieceCCA2Params.getParameters().getN();
        this.t = this.mcElieceCCA2Params.getParameters().getT();
        this.fieldPoly = this.mcElieceCCA2Params.getParameters().getFieldPoly();
        this.initialized = true;
    }
}

