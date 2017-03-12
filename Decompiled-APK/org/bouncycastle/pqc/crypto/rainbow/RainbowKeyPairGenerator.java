package org.bouncycastle.pqc.crypto.rainbow;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RainbowKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private short[][] A1;
    private short[][] A1inv;
    private short[][] A2;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private boolean initialized;
    private Layer[] layers;
    private int numOfLayers;
    private short[][] pub_quadratic;
    private short[] pub_scalar;
    private short[][] pub_singular;
    private RainbowKeyGenerationParameters rainbowParams;
    private SecureRandom sr;
    private int[] vi;

    public RainbowKeyPairGenerator() {
        this.initialized = false;
    }

    private void compactPublicKey(short[][][] sArr) {
        int length = sArr.length;
        int length2 = sArr[0].length;
        this.pub_quadratic = (short[][]) Array.newInstance(Short.TYPE, new int[]{length, ((length2 + 1) * length2) / 2});
        for (int i = 0; i < length; i++) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < length2) {
                int i4 = i3;
                for (i3 = i2; i3 < length2; i3++) {
                    if (i3 == i2) {
                        this.pub_quadratic[i][i4] = sArr[i][i2][i3];
                    } else {
                        this.pub_quadratic[i][i4] = GF2Field.addElem(sArr[i][i2][i3], sArr[i][i3][i2]);
                    }
                    i4++;
                }
                i2++;
                i3 = i4;
            }
        }
    }

    private void computePublicKey() {
        ComputeInField computeInField = new ComputeInField();
        int i = this.vi[this.vi.length - 1] - this.vi[0];
        int i2 = this.vi[this.vi.length - 1];
        short[][][] sArr = (short[][][]) Array.newInstance(Short.TYPE, new int[]{i, i2, i2});
        this.pub_singular = (short[][]) Array.newInstance(Short.TYPE, new int[]{i, i2});
        this.pub_scalar = new short[i];
        int i3 = 0;
        short[] sArr2 = new short[i2];
        for (int i4 = 0; i4 < this.layers.length; i4++) {
            short[][][] coeffAlpha = this.layers[i4].getCoeffAlpha();
            short[][][] coeffBeta = this.layers[i4].getCoeffBeta();
            short[][] coeffGamma = this.layers[i4].getCoeffGamma();
            short[] coeffEta = this.layers[i4].getCoeffEta();
            int length = coeffAlpha[0].length;
            int length2 = coeffBeta[0].length;
            for (int i5 = 0; i5 < length; i5++) {
                int i6;
                int i7;
                for (i6 = 0; i6 < length; i6++) {
                    for (i7 = 0; i7 < length2; i7++) {
                        short[] multVect = computeInField.multVect(coeffAlpha[i5][i6][i7], this.A2[i6 + length2]);
                        sArr[i3 + i5] = computeInField.addSquareMatrix(sArr[i3 + i5], computeInField.multVects(multVect, this.A2[i7]));
                        multVect = computeInField.multVect(this.b2[i7], multVect);
                        this.pub_singular[i3 + i5] = computeInField.addVect(multVect, this.pub_singular[i3 + i5]);
                        multVect = computeInField.multVect(coeffAlpha[i5][i6][i7], this.A2[i7]);
                        multVect = computeInField.multVect(this.b2[i6 + length2], multVect);
                        this.pub_singular[i3 + i5] = computeInField.addVect(multVect, this.pub_singular[i3 + i5]);
                        short multElem = GF2Field.multElem(coeffAlpha[i5][i6][i7], this.b2[i6 + length2]);
                        this.pub_scalar[i3 + i5] = GF2Field.addElem(this.pub_scalar[i3 + i5], GF2Field.multElem(multElem, this.b2[i7]));
                    }
                }
                for (i6 = 0; i6 < length2; i6++) {
                    for (i7 = 0; i7 < length2; i7++) {
                        multVect = computeInField.multVect(coeffBeta[i5][i6][i7], this.A2[i6]);
                        sArr[i3 + i5] = computeInField.addSquareMatrix(sArr[i3 + i5], computeInField.multVects(multVect, this.A2[i7]));
                        multVect = computeInField.multVect(this.b2[i7], multVect);
                        this.pub_singular[i3 + i5] = computeInField.addVect(multVect, this.pub_singular[i3 + i5]);
                        multVect = computeInField.multVect(coeffBeta[i5][i6][i7], this.A2[i7]);
                        multVect = computeInField.multVect(this.b2[i6], multVect);
                        this.pub_singular[i3 + i5] = computeInField.addVect(multVect, this.pub_singular[i3 + i5]);
                        multElem = GF2Field.multElem(coeffBeta[i5][i6][i7], this.b2[i6]);
                        this.pub_scalar[i3 + i5] = GF2Field.addElem(this.pub_scalar[i3 + i5], GF2Field.multElem(multElem, this.b2[i7]));
                    }
                }
                for (i7 = 0; i7 < length2 + length; i7++) {
                    short[] multVect2 = computeInField.multVect(coeffGamma[i5][i7], this.A2[i7]);
                    this.pub_singular[i3 + i5] = computeInField.addVect(multVect2, this.pub_singular[i3 + i5]);
                    this.pub_scalar[i3 + i5] = GF2Field.addElem(this.pub_scalar[i3 + i5], GF2Field.multElem(coeffGamma[i5][i7], this.b2[i7]));
                }
                this.pub_scalar[i3 + i5] = GF2Field.addElem(this.pub_scalar[i3 + i5], coeffEta[i5]);
            }
            i3 += length;
        }
        short[][][] sArr3 = (short[][][]) Array.newInstance(Short.TYPE, new int[]{i, i2, i2});
        short[][] sArr4 = (short[][]) Array.newInstance(Short.TYPE, new int[]{i, i2});
        short[] sArr5 = new short[i];
        for (i6 = 0; i6 < i; i6++) {
            for (i7 = 0; i7 < this.A1.length; i7++) {
                sArr3[i6] = computeInField.addSquareMatrix(sArr3[i6], computeInField.multMatrix(this.A1[i6][i7], sArr[i7]));
                sArr4[i6] = computeInField.addVect(sArr4[i6], computeInField.multVect(this.A1[i6][i7], this.pub_singular[i7]));
                sArr5[i6] = GF2Field.addElem(sArr5[i6], GF2Field.multElem(this.A1[i6][i7], this.pub_scalar[i7]));
            }
            sArr5[i6] = GF2Field.addElem(sArr5[i6], this.b1[i6]);
        }
        this.pub_singular = sArr4;
        this.pub_scalar = sArr5;
        compactPublicKey(sArr3);
    }

    private void generateF() {
        this.layers = new Layer[this.numOfLayers];
        for (int i = 0; i < this.numOfLayers; i++) {
            this.layers[i] = new Layer(this.vi[i], this.vi[i + 1], this.sr);
        }
    }

    private void generateL1() {
        int i;
        int i2 = this.vi[this.vi.length - 1] - this.vi[0];
        this.A1 = (short[][]) Array.newInstance(Short.TYPE, new int[]{i2, i2});
        this.A1inv = (short[][]) null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A1inv == null) {
            for (int i3 = 0; i3 < i2; i3++) {
                for (i = 0; i < i2; i++) {
                    this.A1[i3][i] = (short) (this.sr.nextInt() & GF2Field.MASK);
                }
            }
            this.A1inv = computeInField.inverse(this.A1);
        }
        this.b1 = new short[i2];
        for (i = 0; i < i2; i++) {
            this.b1[i] = (short) (this.sr.nextInt() & GF2Field.MASK);
        }
    }

    private void generateL2() {
        int i = this.vi[this.vi.length - 1];
        this.A2 = (short[][]) Array.newInstance(Short.TYPE, new int[]{i, i});
        this.A2inv = (short[][]) null;
        ComputeInField computeInField = new ComputeInField();
        while (this.A2inv == null) {
            for (int i2 = 0; i2 < i; i2++) {
                int i3;
                for (i3 = 0; i3 < i; i3++) {
                    this.A2[i2][i3] = (short) (this.sr.nextInt() & GF2Field.MASK);
                }
            }
            this.A2inv = computeInField.inverse(this.A2);
        }
        this.b2 = new short[i];
        for (i3 = 0; i3 < i; i3++) {
            this.b2[i3] = (short) (this.sr.nextInt() & GF2Field.MASK);
        }
    }

    private void initializeDefault() {
        initialize(new RainbowKeyGenerationParameters(new SecureRandom(), new RainbowParameters()));
    }

    private void keygen() {
        generateL1();
        generateL2();
        generateF();
        computePublicKey();
    }

    public AsymmetricCipherKeyPair genKeyPair() {
        if (!this.initialized) {
            initializeDefault();
        }
        keygen();
        return new AsymmetricCipherKeyPair(new RainbowPublicKeyParameters(this.vi[this.vi.length - 1] - this.vi[0], this.pub_quadratic, this.pub_singular, this.pub_scalar), new RainbowPrivateKeyParameters(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers));
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        return genKeyPair();
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        initialize(keyGenerationParameters);
    }

    public void initialize(KeyGenerationParameters keyGenerationParameters) {
        this.rainbowParams = (RainbowKeyGenerationParameters) keyGenerationParameters;
        this.sr = new SecureRandom();
        this.vi = this.rainbowParams.getParameters().getVi();
        this.numOfLayers = this.rainbowParams.getParameters().getNumOfLayers();
        this.initialized = true;
    }
}
