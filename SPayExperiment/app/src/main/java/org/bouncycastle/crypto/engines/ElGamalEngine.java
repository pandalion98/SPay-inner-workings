/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ElGamalKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.BigIntegers;

public class ElGamalEngine
implements AsymmetricBlockCipher {
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int bitSize;
    private boolean forEncryption;
    private ElGamalKeyParameters key;
    private SecureRandom random;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
        TWO = BigInteger.valueOf((long)2L);
    }

    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return (-1 + this.bitSize) / 8;
        }
        return 2 * ((7 + this.bitSize) / 8);
    }

    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return 2 * ((7 + this.bitSize) / 8);
        }
        return (-1 + this.bitSize) / 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (ElGamalKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        } else {
            this.key = (ElGamalKeyParameters)cipherParameters;
            this.random = new SecureRandom();
        }
        this.forEncryption = bl;
        this.bitSize = this.key.getParameters().getP().bitLength();
        if (bl) {
            if (this.key instanceof ElGamalPublicKeyParameters) return;
            {
                throw new IllegalArgumentException("ElGamalPublicKeyParameters are required for encryption.");
            }
        } else {
            if (this.key instanceof ElGamalPrivateKeyParameters) return;
            {
                throw new IllegalArgumentException("ElGamalPrivateKeyParameters are required for decryption.");
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] processBlock(byte[] arrby, int n2, int n3) {
        BigInteger bigInteger;
        if (this.key == null) {
            throw new IllegalStateException("ElGamal engine not initialised");
        }
        int n4 = this.forEncryption ? (7 + (-1 + this.bitSize)) / 8 : this.getInputBlockSize();
        if (n3 > n4) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        BigInteger bigInteger2 = this.key.getParameters().getP();
        if (this.key instanceof ElGamalPrivateKeyParameters) {
            byte[] arrby2 = new byte[n3 / 2];
            byte[] arrby3 = new byte[n3 / 2];
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)arrby2.length);
            System.arraycopy((Object)arrby, (int)(n2 + arrby2.length), (Object)arrby3, (int)0, (int)arrby3.length);
            BigInteger bigInteger3 = new BigInteger(1, arrby2);
            BigInteger bigInteger4 = new BigInteger(1, arrby3);
            ElGamalPrivateKeyParameters elGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters)this.key;
            return BigIntegers.asUnsignedByteArray((BigInteger)bigInteger3.modPow(bigInteger2.subtract(ONE).subtract(elGamalPrivateKeyParameters.getX()), bigInteger2).multiply(bigInteger4).mod(bigInteger2));
        }
        if (n2 != 0 || n3 != arrby.length) {
            byte[] arrby4 = new byte[n3];
            System.arraycopy((Object)arrby, (int)n2, (Object)arrby4, (int)0, (int)n3);
            arrby = arrby4;
        }
        if ((bigInteger = new BigInteger(1, arrby)).compareTo(bigInteger2) >= 0) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters)this.key;
        int n5 = bigInteger2.bitLength();
        BigInteger bigInteger5 = new BigInteger(n5, (Random)this.random);
        while (bigInteger5.equals((Object)ZERO) || bigInteger5.compareTo(bigInteger2.subtract(TWO)) > 0) {
            bigInteger5 = new BigInteger(n5, (Random)this.random);
        }
        BigInteger bigInteger6 = this.key.getParameters().getG().modPow(bigInteger5, bigInteger2);
        BigInteger bigInteger7 = bigInteger.multiply(elGamalPublicKeyParameters.getY().modPow(bigInteger5, bigInteger2)).mod(bigInteger2);
        byte[] arrby5 = bigInteger6.toByteArray();
        byte[] arrby6 = bigInteger7.toByteArray();
        byte[] arrby7 = new byte[this.getOutputBlockSize()];
        if (arrby5.length > arrby7.length / 2) {
            System.arraycopy((Object)arrby5, (int)1, (Object)arrby7, (int)(arrby7.length / 2 - (-1 + arrby5.length)), (int)(-1 + arrby5.length));
        } else {
            System.arraycopy((Object)arrby5, (int)0, (Object)arrby7, (int)(arrby7.length / 2 - arrby5.length), (int)arrby5.length);
        }
        if (arrby6.length > arrby7.length / 2) {
            System.arraycopy((Object)arrby6, (int)1, (Object)arrby7, (int)(arrby7.length - (-1 + arrby6.length)), (int)(-1 + arrby6.length));
            return arrby7;
        }
        System.arraycopy((Object)arrby6, (int)0, (Object)arrby7, (int)(arrby7.length - arrby6.length), (int)arrby6.length);
        return arrby7;
    }
}

