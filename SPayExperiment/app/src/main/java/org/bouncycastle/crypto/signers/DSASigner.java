/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.RandomDSAKCalculator;

public class DSASigner
implements DSA {
    private final DSAKCalculator kCalculator;
    private DSAKeyParameters key;
    private SecureRandom random;

    public DSASigner() {
        this.kCalculator = new RandomDSAKCalculator();
    }

    public DSASigner(DSAKCalculator dSAKCalculator) {
        this.kCalculator = dSAKCalculator;
    }

    private BigInteger calculateE(BigInteger bigInteger, byte[] arrby) {
        if (bigInteger.bitLength() >= 8 * arrby.length) {
            return new BigInteger(1, arrby);
        }
        byte[] arrby2 = new byte[bigInteger.bitLength() / 8];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby2.length);
        return new BigInteger(1, arrby2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public BigInteger[] generateSignature(byte[] arrby) {
        DSAParameters dSAParameters = this.key.getParameters();
        BigInteger bigInteger = dSAParameters.getQ();
        BigInteger bigInteger2 = this.calculateE(bigInteger, arrby);
        BigInteger bigInteger3 = ((DSAPrivateKeyParameters)this.key).getX();
        if (this.kCalculator.isDeterministic()) {
            this.kCalculator.init(bigInteger, bigInteger3, arrby);
        } else {
            this.kCalculator.init(bigInteger, this.random);
        }
        BigInteger bigInteger4 = this.kCalculator.nextK();
        BigInteger bigInteger5 = dSAParameters.getG().modPow(bigInteger4, dSAParameters.getP()).mod(bigInteger);
        return new BigInteger[]{bigInteger5, bigInteger4.modInverse(bigInteger).multiply(bigInteger2.add(bigInteger3.multiply(bigInteger5))).mod(bigInteger)};
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        SecureRandom secureRandom;
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.key = (DSAPrivateKeyParameters)parametersWithRandom.getParameters();
                secureRandom = parametersWithRandom.getRandom();
            } else {
                this.key = (DSAPrivateKeyParameters)cipherParameters;
                secureRandom = null;
            }
        } else {
            this.key = (DSAPublicKeyParameters)cipherParameters;
            secureRandom = null;
        }
        boolean bl2 = bl && !this.kCalculator.isDeterministic();
        this.random = this.initSecureRandom(bl2, secureRandom);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected SecureRandom initSecureRandom(boolean bl, SecureRandom secureRandom) {
        if (!bl) {
            return null;
        }
        if (secureRandom != null) return secureRandom;
        return new SecureRandom();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        DSAParameters dSAParameters = this.key.getParameters();
        BigInteger bigInteger3 = dSAParameters.getQ();
        BigInteger bigInteger4 = this.calculateE(bigInteger3, arrby);
        BigInteger bigInteger5 = BigInteger.valueOf((long)0L);
        if (bigInteger5.compareTo(bigInteger) >= 0 || bigInteger3.compareTo(bigInteger) <= 0 || bigInteger5.compareTo(bigInteger2) >= 0 || bigInteger3.compareTo(bigInteger2) <= 0) {
            return false;
        }
        BigInteger bigInteger6 = bigInteger2.modInverse(bigInteger3);
        BigInteger bigInteger7 = bigInteger4.multiply(bigInteger6).mod(bigInteger3);
        BigInteger bigInteger8 = bigInteger.multiply(bigInteger6).mod(bigInteger3);
        BigInteger bigInteger9 = dSAParameters.getP();
        return dSAParameters.getG().modPow(bigInteger7, bigInteger9).multiply(((DSAPublicKeyParameters)this.key).getY().modPow(bigInteger8, bigInteger9)).mod(bigInteger9).mod(bigInteger3).equals((Object)bigInteger);
    }
}

