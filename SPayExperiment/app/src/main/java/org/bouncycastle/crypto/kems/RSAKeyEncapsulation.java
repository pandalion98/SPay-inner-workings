/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.kems;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.KeyEncapsulation;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class RSAKeyEncapsulation
implements KeyEncapsulation {
    private static final BigInteger ONE;
    private static final BigInteger ZERO;
    private DerivationFunction kdf;
    private RSAKeyParameters key;
    private SecureRandom rnd;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    public RSAKeyEncapsulation(DerivationFunction derivationFunction, SecureRandom secureRandom) {
        this.kdf = derivationFunction;
        this.rnd = secureRandom;
    }

    public CipherParameters decrypt(byte[] arrby, int n2) {
        return this.decrypt(arrby, 0, arrby.length, n2);
    }

    @Override
    public CipherParameters decrypt(byte[] arrby, int n2, int n3, int n4) {
        if (!this.key.isPrivate()) {
            throw new IllegalArgumentException("Private key required for decryption");
        }
        BigInteger bigInteger = this.key.getModulus();
        BigInteger bigInteger2 = this.key.getExponent();
        byte[] arrby2 = new byte[n3];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby2, (int)0, (int)arrby2.length);
        return this.generateKey(bigInteger, new BigInteger(1, arrby2).modPow(bigInteger2, bigInteger), n4);
    }

    public CipherParameters encrypt(byte[] arrby, int n2) {
        return this.encrypt(arrby, 0, n2);
    }

    @Override
    public CipherParameters encrypt(byte[] arrby, int n2, int n3) {
        if (this.key.isPrivate()) {
            throw new IllegalArgumentException("Public key required for encryption");
        }
        BigInteger bigInteger = this.key.getModulus();
        BigInteger bigInteger2 = this.key.getExponent();
        BigInteger bigInteger3 = BigIntegers.createRandomInRange((BigInteger)ZERO, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)this.rnd);
        BigInteger bigInteger4 = bigInteger3.modPow(bigInteger2, bigInteger);
        byte[] arrby2 = BigIntegers.asUnsignedByteArray((int)((7 + bigInteger.bitLength()) / 8), (BigInteger)bigInteger4);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        return this.generateKey(bigInteger, bigInteger3, n3);
    }

    protected KeyParameter generateKey(BigInteger bigInteger, BigInteger bigInteger2, int n2) {
        byte[] arrby = BigIntegers.asUnsignedByteArray((int)((7 + bigInteger.bitLength()) / 8), (BigInteger)bigInteger2);
        this.kdf.init(new KDFParameters(arrby, null));
        byte[] arrby2 = new byte[n2];
        this.kdf.generateBytes(arrby2, 0, arrby2.length);
        return new KeyParameter(arrby2);
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof RSAKeyParameters)) {
            throw new IllegalArgumentException("RSA key required");
        }
        this.key = (RSAKeyParameters)cipherParameters;
    }
}

