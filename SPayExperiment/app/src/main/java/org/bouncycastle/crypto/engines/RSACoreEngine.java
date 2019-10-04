/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

class RSACoreEngine {
    private boolean forEncryption;
    private RSAKeyParameters key;

    RSACoreEngine() {
    }

    public BigInteger convertInput(byte[] arrby, int n2, int n3) {
        BigInteger bigInteger;
        if (n3 > 1 + this.getInputBlockSize()) {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        if (n3 == 1 + this.getInputBlockSize() && !this.forEncryption) {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        if (n2 != 0 || n3 != arrby.length) {
            byte[] arrby2 = new byte[n3];
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)n3);
            arrby = arrby2;
        }
        if ((bigInteger = new BigInteger(1, arrby)).compareTo(this.key.getModulus()) >= 0) {
            throw new DataLengthException("input too large for RSA cipher.");
        }
        return bigInteger;
    }

    public byte[] convertOutput(BigInteger bigInteger) {
        byte[] arrby = bigInteger.toByteArray();
        if (this.forEncryption) {
            if (arrby[0] == 0 && arrby.length > this.getOutputBlockSize()) {
                byte[] arrby2 = new byte[-1 + arrby.length];
                System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
                return arrby2;
            }
            if (arrby.length < this.getOutputBlockSize()) {
                byte[] arrby3 = new byte[this.getOutputBlockSize()];
                System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)(arrby3.length - arrby.length), (int)arrby.length);
                return arrby3;
            }
        } else if (arrby[0] == 0) {
            byte[] arrby4 = new byte[-1 + arrby.length];
            System.arraycopy((Object)arrby, (int)1, (Object)arrby4, (int)0, (int)arrby4.length);
            return arrby4;
        }
        return arrby;
    }

    public int getInputBlockSize() {
        int n2 = this.key.getModulus().bitLength();
        if (this.forEncryption) {
            return -1 + (n2 + 7) / 8;
        }
        return (n2 + 7) / 8;
    }

    public int getOutputBlockSize() {
        int n2 = this.key.getModulus().bitLength();
        if (this.forEncryption) {
            return (n2 + 7) / 8;
        }
        return -1 + (n2 + 7) / 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.key = cipherParameters instanceof ParametersWithRandom ? (RSAKeyParameters)((ParametersWithRandom)cipherParameters).getParameters() : (RSAKeyParameters)cipherParameters;
        this.forEncryption = bl;
    }

    public BigInteger processBlock(BigInteger bigInteger) {
        if (this.key instanceof RSAPrivateCrtKeyParameters) {
            RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)this.key;
            BigInteger bigInteger2 = rSAPrivateCrtKeyParameters.getP();
            BigInteger bigInteger3 = rSAPrivateCrtKeyParameters.getQ();
            BigInteger bigInteger4 = rSAPrivateCrtKeyParameters.getDP();
            BigInteger bigInteger5 = rSAPrivateCrtKeyParameters.getDQ();
            BigInteger bigInteger6 = rSAPrivateCrtKeyParameters.getQInv();
            BigInteger bigInteger7 = bigInteger.remainder(bigInteger2).modPow(bigInteger4, bigInteger2);
            BigInteger bigInteger8 = bigInteger.remainder(bigInteger3).modPow(bigInteger5, bigInteger3);
            return bigInteger7.subtract(bigInteger8).multiply(bigInteger6).mod(bigInteger2).multiply(bigInteger3).add(bigInteger8);
        }
        return bigInteger.modPow(this.key.getExponent(), this.key.getModulus());
    }
}

