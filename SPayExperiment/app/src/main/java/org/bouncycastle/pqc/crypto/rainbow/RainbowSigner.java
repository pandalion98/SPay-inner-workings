/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RainbowSigner
implements MessageSigner {
    private ComputeInField cf = new ComputeInField();
    RainbowKeyParameters key;
    private SecureRandom random;
    int signableDocumentLength;
    private short[] x;

    private short[] initSign(Layer[] arrlayer, short[] arrs) {
        new short[arrs.length];
        short[] arrs2 = this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB1(), arrs);
        short[] arrs3 = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA1(), arrs2);
        for (int i = 0; i < arrlayer[0].getVi(); ++i) {
            this.x[i] = (short)this.random.nextInt();
            this.x[i] = (short)(255 & this.x[i]);
        }
        return arrs3;
    }

    private short[] makeMessageRepresentative(byte[] arrby) {
        int n = 0;
        short[] arrs = new short[this.signableDocumentLength];
        int n2 = 0;
        do {
            if (n >= arrby.length) {
                return arrs;
            }
            arrs[n] = arrby[n2];
            arrs[n] = (short)(255 & arrs[n]);
            ++n2;
        } while (++n < arrs.length);
        return arrs;
    }

    private short[] verifySignatureIntern(short[] arrs) {
        short[][] arrs2 = ((RainbowPublicKeyParameters)this.key).getCoeffQuadratic();
        short[][] arrs3 = ((RainbowPublicKeyParameters)this.key).getCoeffSingular();
        short[] arrs4 = ((RainbowPublicKeyParameters)this.key).getCoeffScalar();
        short[] arrs5 = new short[arrs2.length];
        int n = arrs3[0].length;
        for (int i = 0; i < arrs2.length; ++i) {
            int n2 = 0;
            for (int j = 0; j < n; ++j) {
                int n3 = n2;
                for (int k = j; k < n; ++k) {
                    short s = GF2Field.multElem(arrs2[i][n3], GF2Field.multElem(arrs[j], arrs[k]));
                    arrs5[i] = GF2Field.addElem(arrs5[i], s);
                    ++n3;
                }
                short s = GF2Field.multElem(arrs3[i][j], arrs[j]);
                arrs5[i] = GF2Field.addElem(arrs5[i], s);
                n2 = n3;
            }
            arrs5[i] = GF2Field.addElem(arrs5[i], arrs4[i]);
        }
        return arrs5;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public byte[] generateSignature(byte[] arrby) {
        boolean bl;
        Layer[] arrlayer = ((RainbowPrivateKeyParameters)this.key).getLayers();
        int n = arrlayer.length;
        this.x = new short[((RainbowPrivateKeyParameters)this.key).getInvA2().length];
        byte[] arrby2 = new byte[arrlayer[n - 1].getViNext()];
        short[] arrs = this.makeMessageRepresentative(arrby);
        do {
            try {
                short[] arrs2;
                short[] arrs3 = this.initSign(arrlayer, arrs);
                int n2 = 0;
                int n3 = 0;
                do {
                    short[] arrs4;
                    int n4;
                    if (n2 < n) {
                        arrs4 = new short[arrlayer[n2].getOi()];
                        new short[arrlayer[n2].getOi()];
                        n4 = n3;
                    } else {
                        short[] arrs5 = this.cf.addVect(((RainbowPrivateKeyParameters)this.key).getB2(), this.x);
                        arrs2 = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters)this.key).getInvA2(), arrs5);
                        break;
                    }
                    for (int i = 0; i < arrlayer[n2].getOi(); ++n4, ++i) {
                        arrs4[i] = arrs3[n4];
                    }
                    short[] arrs6 = this.cf.solveEquation(arrlayer[n2].plugInVinegars(this.x), arrs4);
                    if (arrs6 == null) {
                        throw new Exception("LES is not solveable!");
                    }
                    for (int i = 0; i < arrs6.length; ++i) {
                        this.x[i + arrlayer[n2].getVi()] = arrs6[i];
                    }
                    ++n2;
                    n3 = n4;
                    continue;
                    break;
                } while (true);
                for (int i = 0; i < arrby2.length; ++i) {
                    arrby2[i] = (byte)arrs2[i];
                }
                bl = true;
            }
            catch (Exception exception) {
                bl = false;
            }
        } while (!bl);
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (RainbowPrivateKeyParameters)parametersWithRandom.getParameters();
            } else {
                this.random = new SecureRandom();
                this.key = (RainbowPrivateKeyParameters)cipherParameters;
            }
        } else {
            this.key = (RainbowPublicKeyParameters)cipherParameters;
        }
        this.signableDocumentLength = this.key.getDocLength();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, byte[] arrby2) {
        short[] arrs;
        short[] arrs2 = new short[arrby2.length];
        for (int i = 0; i < arrby2.length; ++i) {
            arrs2[i] = (short)(255 & (short)arrby2[i]);
        }
        short[] arrs3 = this.makeMessageRepresentative(arrby);
        if (arrs3.length != (arrs = this.verifySignatureIntern(arrs2)).length) {
            return false;
        }
        int n = 0;
        boolean bl = true;
        while (n < arrs3.length) {
            bl = bl && arrs3[n] == arrs[n];
            ++n;
        }
        return bl;
    }
}

