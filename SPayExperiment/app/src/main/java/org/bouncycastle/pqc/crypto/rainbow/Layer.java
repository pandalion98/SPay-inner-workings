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
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
import org.bouncycastle.util.Arrays;

public class Layer {
    private short[][][] coeff_alpha;
    private short[][][] coeff_beta;
    private short[] coeff_eta;
    private short[][] coeff_gamma;
    private int oi;
    private int vi;
    private int viNext;

    public Layer(byte by, byte by2, short[][][] arrs, short[][][] arrs2, short[][] arrs3, short[] arrs4) {
        this.vi = by & 255;
        this.viNext = by2 & 255;
        this.oi = this.viNext - this.vi;
        this.coeff_alpha = arrs;
        this.coeff_beta = arrs2;
        this.coeff_gamma = arrs3;
        this.coeff_eta = arrs4;
    }

    public Layer(int n, int n2, SecureRandom secureRandom) {
        this.vi = n;
        this.viNext = n2;
        this.oi = n2 - n;
        int[] arrn = new int[]{this.oi, this.oi, this.vi};
        this.coeff_alpha = (short[][][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        int[] arrn2 = new int[]{this.oi, this.vi, this.vi};
        this.coeff_beta = (short[][][])Array.newInstance((Class)Short.TYPE, (int[])arrn2);
        int[] arrn3 = new int[]{this.oi, this.viNext};
        this.coeff_gamma = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn3);
        this.coeff_eta = new short[this.oi];
        int n3 = this.oi;
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < this.oi; ++j) {
                for (int k = 0; k < this.vi; ++k) {
                    this.coeff_alpha[i][j][k] = (short)(255 & secureRandom.nextInt());
                }
            }
        }
        for (int i = 0; i < n3; ++i) {
            for (int j = 0; j < this.vi; ++j) {
                for (int k = 0; k < this.vi; ++k) {
                    this.coeff_beta[i][j][k] = (short)(255 & secureRandom.nextInt());
                }
            }
        }
        int n4 = 0;
        do {
            if (n4 >= n3) break;
            for (int i = 0; i < this.viNext; ++i) {
                this.coeff_gamma[n4][i] = (short)(255 & secureRandom.nextInt());
            }
            ++n4;
        } while (true);
        for (int i = 0; i < n3; ++i) {
            this.coeff_eta[i] = (short)(255 & secureRandom.nextInt());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        Layer layer;
        return object != null && object instanceof Layer && this.vi == (layer = (Layer)object).getVi() && this.viNext == layer.getViNext() && this.oi == layer.getOi() && RainbowUtil.equals(this.coeff_alpha, layer.getCoeffAlpha()) && RainbowUtil.equals(this.coeff_beta, layer.getCoeffBeta()) && RainbowUtil.equals(this.coeff_gamma, layer.getCoeffGamma()) && RainbowUtil.equals(this.coeff_eta, layer.getCoeffEta());
    }

    public short[][][] getCoeffAlpha() {
        return this.coeff_alpha;
    }

    public short[][][] getCoeffBeta() {
        return this.coeff_beta;
    }

    public short[] getCoeffEta() {
        return this.coeff_eta;
    }

    public short[][] getCoeffGamma() {
        return this.coeff_gamma;
    }

    public int getOi() {
        return this.oi;
    }

    public int getVi() {
        return this.vi;
    }

    public int getViNext() {
        return this.viNext;
    }

    public int hashCode() {
        return 37 * (37 * (37 * (37 * (37 * (37 * this.vi + this.viNext) + this.oi) + Arrays.hashCode(this.coeff_alpha)) + Arrays.hashCode(this.coeff_beta)) + Arrays.hashCode(this.coeff_gamma)) + Arrays.hashCode(this.coeff_eta);
    }

    public short[][] plugInVinegars(short[] arrs) {
        int[] arrn = new int[]{this.oi, 1 + this.oi};
        short[][] arrs2 = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        short[] arrs3 = new short[this.oi];
        for (int i = 0; i < this.oi; ++i) {
            for (int j = 0; j < this.vi; ++j) {
                for (int k = 0; k < this.vi; ++k) {
                    short s = GF2Field.multElem(GF2Field.multElem(this.coeff_beta[i][j][k], arrs[j]), arrs[k]);
                    arrs3[i] = GF2Field.addElem(arrs3[i], s);
                }
            }
        }
        for (int i = 0; i < this.oi; ++i) {
            for (int j = 0; j < this.oi; ++j) {
                for (int k = 0; k < this.vi; ++k) {
                    short s = GF2Field.multElem(this.coeff_alpha[i][j][k], arrs[k]);
                    arrs2[i][j] = GF2Field.addElem(arrs2[i][j], s);
                }
            }
        }
        for (int i = 0; i < this.oi; ++i) {
            for (int j = 0; j < this.vi; ++j) {
                short s = GF2Field.multElem(this.coeff_gamma[i][j], arrs[j]);
                arrs3[i] = GF2Field.addElem(arrs3[i], s);
            }
        }
        for (int i = 0; i < this.oi; ++i) {
            for (int j = this.vi; j < this.viNext; ++j) {
                arrs2[i][j - this.vi] = GF2Field.addElem(this.coeff_gamma[i][j], arrs2[i][j - this.vi]);
            }
        }
        int n = 0;
        do {
            int n2 = this.oi;
            if (n >= n2) break;
            arrs3[n] = GF2Field.addElem(arrs3[n], this.coeff_eta[n]);
            ++n;
        } while (true);
        for (int i = 0; i < this.oi; ++i) {
            arrs2[i][this.oi] = arrs3[i];
        }
        return arrs2;
    }
}

