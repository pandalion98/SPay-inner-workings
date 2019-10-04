/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.reflect.Array
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RainbowKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private short[][] A1;
    private short[][] A1inv;
    private short[][] A2;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private boolean initialized = false;
    private Layer[] layers;
    private int numOfLayers;
    private short[][] pub_quadratic;
    private short[] pub_scalar;
    private short[][] pub_singular;
    private RainbowKeyGenerationParameters rainbowParams;
    private SecureRandom sr;
    private int[] vi;

    /*
     * Enabled aggressive block sorting
     */
    private void compactPublicKey(short[][][] arrs) {
        int n = arrs.length;
        int n2 = arrs[0].length;
        int[] arrn = new int[]{n, n2 * (n2 + 1) / 2};
        this.pub_quadratic = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        int n3 = 0;
        block0 : while (n3 < n) {
            int n4 = 0;
            int n5 = 0;
            do {
                int n6;
                if (n4 < n2) {
                    n6 = n5;
                } else {
                    ++n3;
                    continue block0;
                }
                for (int i = n4; i < n2; ++n6, ++i) {
                    this.pub_quadratic[n3][n6] = i == n4 ? arrs[n3][n4][i] : GF2Field.addElem(arrs[n3][n4][i], arrs[n3][i][n4]);
                }
                ++n4;
                n5 = n6;
            } while (true);
            break;
        }
        return;
    }

    private void computePublicKey() {
        ComputeInField computeInField = new ComputeInField();
        int n = this.vi[-1 + this.vi.length] - this.vi[0];
        int n2 = this.vi[-1 + this.vi.length];
        int[] arrn = new int[]{n, n2, n2};
        short[][][] arrs = (short[][][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        int[] arrn2 = new int[]{n, n2};
        this.pub_singular = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn2);
        this.pub_scalar = new short[n];
        int n3 = 0;
        new short[n2];
        for (int i = 0; i < this.layers.length; ++i) {
            short[][][] arrs2 = this.layers[i].getCoeffAlpha();
            short[][][] arrs3 = this.layers[i].getCoeffBeta();
            short[][] arrs4 = this.layers[i].getCoeffGamma();
            short[] arrs5 = this.layers[i].getCoeffEta();
            int n4 = arrs2[0].length;
            int n5 = arrs3[0].length;
            for (int j = 0; j < n4; ++j) {
                for (int k = 0; k < n4; ++k) {
                    for (int i2 = 0; i2 < n5; ++i2) {
                        short[] arrs6 = computeInField.multVect(arrs2[j][k][i2], this.A2[k + n5]);
                        arrs[n3 + j] = computeInField.addSquareMatrix(arrs[n3 + j], computeInField.multVects(arrs6, this.A2[i2]));
                        short[] arrs7 = computeInField.multVect(this.b2[i2], arrs6);
                        this.pub_singular[n3 + j] = computeInField.addVect(arrs7, this.pub_singular[n3 + j]);
                        short[] arrs8 = computeInField.multVect(arrs2[j][k][i2], this.A2[i2]);
                        short[] arrs9 = computeInField.multVect(this.b2[k + n5], arrs8);
                        this.pub_singular[n3 + j] = computeInField.addVect(arrs9, this.pub_singular[n3 + j]);
                        short s = GF2Field.multElem(arrs2[j][k][i2], this.b2[k + n5]);
                        this.pub_scalar[n3 + j] = GF2Field.addElem(this.pub_scalar[n3 + j], GF2Field.multElem(s, this.b2[i2]));
                    }
                }
                for (int k = 0; k < n5; ++k) {
                    for (int i3 = 0; i3 < n5; ++i3) {
                        short[] arrs10 = computeInField.multVect(arrs3[j][k][i3], this.A2[k]);
                        arrs[n3 + j] = computeInField.addSquareMatrix(arrs[n3 + j], computeInField.multVects(arrs10, this.A2[i3]));
                        short[] arrs11 = computeInField.multVect(this.b2[i3], arrs10);
                        this.pub_singular[n3 + j] = computeInField.addVect(arrs11, this.pub_singular[n3 + j]);
                        short[] arrs12 = computeInField.multVect(arrs3[j][k][i3], this.A2[i3]);
                        short[] arrs13 = computeInField.multVect(this.b2[k], arrs12);
                        this.pub_singular[n3 + j] = computeInField.addVect(arrs13, this.pub_singular[n3 + j]);
                        short s = GF2Field.multElem(arrs3[j][k][i3], this.b2[k]);
                        this.pub_scalar[n3 + j] = GF2Field.addElem(this.pub_scalar[n3 + j], GF2Field.multElem(s, this.b2[i3]));
                    }
                }
                for (int k = 0; k < n5 + n4; ++k) {
                    short[] arrs14 = computeInField.multVect(arrs4[j][k], this.A2[k]);
                    this.pub_singular[n3 + j] = computeInField.addVect(arrs14, this.pub_singular[n3 + j]);
                    this.pub_scalar[n3 + j] = GF2Field.addElem(this.pub_scalar[n3 + j], GF2Field.multElem(arrs4[j][k], this.b2[k]));
                }
                this.pub_scalar[n3 + j] = GF2Field.addElem(this.pub_scalar[n3 + j], arrs5[j]);
            }
            n3 += n4;
        }
        int[] arrn3 = new int[]{n, n2, n2};
        short[][][] arrs15 = (short[][][])Array.newInstance((Class)Short.TYPE, (int[])arrn3);
        int[] arrn4 = new int[]{n, n2};
        short[][] arrs16 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn4);
        short[] arrs17 = new short[n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < this.A1.length; ++j) {
                arrs15[i] = computeInField.addSquareMatrix(arrs15[i], computeInField.multMatrix(this.A1[i][j], arrs[j]));
                arrs16[i] = computeInField.addVect(arrs16[i], computeInField.multVect(this.A1[i][j], this.pub_singular[j]));
                arrs17[i] = GF2Field.addElem(arrs17[i], GF2Field.multElem(this.A1[i][j], this.pub_scalar[j]));
            }
            arrs17[i] = GF2Field.addElem(arrs17[i], this.b1[i]);
        }
        this.pub_singular = arrs16;
        this.pub_scalar = arrs17;
        this.compactPublicKey(arrs15);
    }

    private void generateF() {
        this.layers = new Layer[this.numOfLayers];
        for (int i = 0; i < this.numOfLayers; ++i) {
            this.layers[i] = new Layer(this.vi[i], this.vi[i + 1], this.sr);
        }
    }

    private void generateL1() {
        int n = this.vi[-1 + this.vi.length] - this.vi[0];
        int[] arrn = new int[]{n, n};
        this.A1 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        this.A1inv = null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A1inv == null) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    this.A1[i][j] = (short)(255 & this.sr.nextInt());
                }
            }
            this.A1inv = computeInField.inverse(this.A1);
        }
        this.b1 = new short[n];
        for (int i = 0; i < n; ++i) {
            this.b1[i] = (short)(255 & this.sr.nextInt());
        }
    }

    private void generateL2() {
        int n = this.vi[-1 + this.vi.length];
        int[] arrn = new int[]{n, n};
        this.A2 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        this.A2inv = null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A2inv == null) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    this.A2[i][j] = (short)(255 & this.sr.nextInt());
                }
            }
            this.A2inv = computeInField.inverse(this.A2);
        }
        this.b2 = new short[n];
        for (int i = 0; i < n; ++i) {
            this.b2[i] = (short)(255 & this.sr.nextInt());
        }
    }

    private void initializeDefault() {
        this.initialize(new RainbowKeyGenerationParameters(new SecureRandom(), new RainbowParameters()));
    }

    private void keygen() {
        this.generateL1();
        this.generateL2();
        this.generateF();
        this.computePublicKey();
    }

    public AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            this.initializeDefault();
        }
        this.keygen();
        RainbowPrivateKeyParameters rainbowPrivateKeyParameters = new RainbowPrivateKeyParameters(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
        return new AsymmetricCipherKeyPair(new RainbowPublicKeyParameters(this.vi[-1 + this.vi.length] - this.vi[0], this.pub_quadratic, this.pub_singular, this.pub_scalar), rainbowPrivateKeyParameters);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        return this.genKeyPair();
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.initialize(keyGenerationParameters);
    }

    public void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.rainbowParams = (RainbowKeyGenerationParameters)keyGenerationParameters;
        this.sr = new SecureRandom();
        this.vi = this.rainbowParams.getParameters().getVi();
        this.numOfLayers = this.rainbowParams.getParameters().getNumOfLayers();
        this.initialized = true;
    }
}

