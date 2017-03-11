package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.rainbow.util.ComputeInField;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RainbowSigner implements MessageSigner {
    private ComputeInField cf;
    RainbowKeyParameters key;
    private SecureRandom random;
    int signableDocumentLength;
    private short[] f440x;

    public RainbowSigner() {
        this.cf = new ComputeInField();
    }

    private short[] initSign(Layer[] layerArr, short[] sArr) {
        short[] sArr2 = new short[sArr.length];
        short[] multiplyMatrix = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters) this.key).getInvA1(), this.cf.addVect(((RainbowPrivateKeyParameters) this.key).getB1(), sArr));
        for (int i = 0; i < layerArr[0].getVi(); i++) {
            this.f440x[i] = (short) this.random.nextInt();
            this.f440x[i] = (short) (this.f440x[i] & GF2Field.MASK);
        }
        return multiplyMatrix;
    }

    private short[] makeMessageRepresentative(byte[] bArr) {
        int i = 0;
        short[] sArr = new short[this.signableDocumentLength];
        int i2 = 0;
        while (i < bArr.length) {
            sArr[i] = (short) bArr[i2];
            sArr[i] = (short) (sArr[i] & GF2Field.MASK);
            i2++;
            i++;
            if (i >= sArr.length) {
                break;
            }
        }
        return sArr;
    }

    private short[] verifySignatureIntern(short[] sArr) {
        short[][] coeffQuadratic = ((RainbowPublicKeyParameters) this.key).getCoeffQuadratic();
        short[][] coeffSingular = ((RainbowPublicKeyParameters) this.key).getCoeffSingular();
        short[] coeffScalar = ((RainbowPublicKeyParameters) this.key).getCoeffScalar();
        short[] sArr2 = new short[coeffQuadratic.length];
        int length = coeffSingular[0].length;
        for (int i = 0; i < coeffQuadratic.length; i++) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                int i4 = i3;
                for (i3 = i2; i3 < length; i3++) {
                    sArr2[i] = GF2Field.addElem(sArr2[i], GF2Field.multElem(coeffQuadratic[i][i4], GF2Field.multElem(sArr[i2], sArr[i3])));
                    i4++;
                }
                sArr2[i] = GF2Field.addElem(sArr2[i], GF2Field.multElem(coeffSingular[i][i2], sArr[i2]));
                i2++;
                i3 = i4;
            }
            sArr2[i] = GF2Field.addElem(sArr2[i], coeffScalar[i]);
        }
        return sArr2;
    }

    public byte[] generateSignature(byte[] bArr) {
        Layer[] layers = ((RainbowPrivateKeyParameters) this.key).getLayers();
        int length = layers.length;
        this.f440x = new short[((RainbowPrivateKeyParameters) this.key).getInvA2().length];
        byte[] bArr2 = new byte[layers[length - 1].getViNext()];
        short[] makeMessageRepresentative = makeMessageRepresentative(bArr);
        Object obj;
        do {
            try {
                short[] sArr;
                short[] initSign = initSign(layers, makeMessageRepresentative);
                int i = 0;
                int i2 = 0;
                while (i < length) {
                    short[] sArr2 = new short[layers[i].getOi()];
                    sArr = new short[layers[i].getOi()];
                    int i3 = i2;
                    for (i2 = 0; i2 < layers[i].getOi(); i2++) {
                        sArr2[i2] = initSign[i3];
                        i3++;
                    }
                    sArr2 = this.cf.solveEquation(layers[i].plugInVinegars(this.f440x), sArr2);
                    if (sArr2 == null) {
                        throw new Exception("LES is not solveable!");
                    }
                    for (i2 = 0; i2 < sArr2.length; i2++) {
                        this.f440x[layers[i].getVi() + i2] = sArr2[i2];
                    }
                    i++;
                    i2 = i3;
                }
                sArr = this.cf.multiplyMatrix(((RainbowPrivateKeyParameters) this.key).getInvA2(), this.cf.addVect(((RainbowPrivateKeyParameters) this.key).getB2(), this.f440x));
                for (i2 = 0; i2 < bArr2.length; i2++) {
                    bArr2[i2] = (byte) sArr[i2];
                }
                i2 = 1;
                continue;
            } catch (Exception e) {
                obj = null;
                continue;
            }
        } while (obj == null);
        return bArr2;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (!z) {
            this.key = (RainbowPublicKeyParameters) cipherParameters;
        } else if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (RainbowPrivateKeyParameters) parametersWithRandom.getParameters();
        } else {
            this.random = new SecureRandom();
            this.key = (RainbowPrivateKeyParameters) cipherParameters;
        }
        this.signableDocumentLength = this.key.getDocLength();
    }

    public boolean verifySignature(byte[] bArr, byte[] bArr2) {
        int i;
        short[] sArr = new short[bArr2.length];
        for (i = 0; i < bArr2.length; i++) {
            sArr[i] = (short) (((short) bArr2[i]) & GF2Field.MASK);
        }
        short[] makeMessageRepresentative = makeMessageRepresentative(bArr);
        short[] verifySignatureIntern = verifySignatureIntern(sArr);
        if (makeMessageRepresentative.length != verifySignatureIntern.length) {
            return false;
        }
        boolean z = true;
        for (i = 0; i < makeMessageRepresentative.length; i++) {
            z = z && makeMessageRepresentative[i] == verifySignatureIntern[i];
        }
        return z;
    }
}
