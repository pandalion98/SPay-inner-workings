/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.GOST3410KeyParameters;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class GOST3410Signer
implements DSA {
    GOST3410KeyParameters key;
    SecureRandom random;

    @Override
    public BigInteger[] generateSignature(byte[] arrby) {
        BigInteger bigInteger;
        byte[] arrby2 = new byte[arrby.length];
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            arrby2[i2] = arrby[-1 + arrby2.length - i2];
        }
        BigInteger bigInteger2 = new BigInteger(1, arrby2);
        GOST3410Parameters gOST3410Parameters = this.key.getParameters();
        while ((bigInteger = new BigInteger(gOST3410Parameters.getQ().bitLength(), (Random)this.random)).compareTo(gOST3410Parameters.getQ()) >= 0) {
        }
        BigInteger bigInteger3 = gOST3410Parameters.getA().modPow(bigInteger, gOST3410Parameters.getP()).mod(gOST3410Parameters.getQ());
        return new BigInteger[]{bigInteger3, bigInteger.multiply(bigInteger2).add(((GOST3410PrivateKeyParameters)this.key).getX().multiply(bigInteger3)).mod(gOST3410Parameters.getQ())};
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (GOST3410PrivateKeyParameters)parametersWithRandom.getParameters();
                return;
            }
            this.random = new SecureRandom();
            this.key = (GOST3410PrivateKeyParameters)cipherParameters;
            return;
        }
        this.key = (GOST3410PublicKeyParameters)cipherParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        byte[] arrby2 = new byte[arrby.length];
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            arrby2[i2] = arrby[-1 + arrby2.length - i2];
        }
        BigInteger bigInteger3 = new BigInteger(1, arrby2);
        GOST3410Parameters gOST3410Parameters = this.key.getParameters();
        BigInteger bigInteger4 = BigInteger.valueOf((long)0L);
        if (bigInteger4.compareTo(bigInteger) >= 0 || gOST3410Parameters.getQ().compareTo(bigInteger) <= 0 || bigInteger4.compareTo(bigInteger2) >= 0 || gOST3410Parameters.getQ().compareTo(bigInteger2) <= 0) {
            return false;
        }
        BigInteger bigInteger5 = bigInteger3.modPow(gOST3410Parameters.getQ().subtract(new BigInteger("2")), gOST3410Parameters.getQ());
        BigInteger bigInteger6 = bigInteger2.multiply(bigInteger5).mod(gOST3410Parameters.getQ());
        BigInteger bigInteger7 = gOST3410Parameters.getQ().subtract(bigInteger).multiply(bigInteger5).mod(gOST3410Parameters.getQ());
        return gOST3410Parameters.getA().modPow(bigInteger6, gOST3410Parameters.getP()).multiply(((GOST3410PublicKeyParameters)this.key).getY().modPow(bigInteger7, gOST3410Parameters.getP())).mod(gOST3410Parameters.getP()).mod(gOST3410Parameters.getQ()).equals((Object)bigInteger);
    }
}

