/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECGOST3410Signer
implements DSA {
    ECKeyParameters key;
    SecureRandom random;

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    @Override
    public BigInteger[] generateSignature(byte[] arrby) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        byte[] arrby2 = new byte[arrby.length];
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            arrby2[i2] = arrby[-1 + arrby2.length - i2];
        }
        BigInteger bigInteger4 = new BigInteger(1, arrby2);
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger5 = eCDomainParameters.getN();
        BigInteger bigInteger6 = ((ECPrivateKeyParameters)this.key).getD();
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        while ((bigInteger2 = new BigInteger(bigInteger5.bitLength(), (Random)this.random)).equals((Object)ECConstants.ZERO) || (bigInteger = eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger2).normalize().getAffineXCoord().toBigInteger().mod(bigInteger5)).equals((Object)ECConstants.ZERO) || (bigInteger3 = bigInteger2.multiply(bigInteger4).add(bigInteger6.multiply(bigInteger)).mod(bigInteger5)).equals((Object)ECConstants.ZERO)) {
        }
        return new BigInteger[]{bigInteger, bigInteger3};
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (bl) {
            if (cipherParameters instanceof ParametersWithRandom) {
                ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
                return;
            }
            this.random = new SecureRandom();
            this.key = (ECPrivateKeyParameters)cipherParameters;
            return;
        }
        this.key = (ECPublicKeyParameters)cipherParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        ECPoint eCPoint;
        BigInteger bigInteger3;
        block4 : {
            block3 : {
                byte[] arrby2 = new byte[arrby.length];
                for (int i2 = 0; i2 != arrby2.length; ++i2) {
                    arrby2[i2] = arrby[-1 + arrby2.length - i2];
                }
                BigInteger bigInteger4 = new BigInteger(1, arrby2);
                bigInteger3 = this.key.getParameters().getN();
                if (bigInteger.compareTo(ECConstants.ONE) < 0 || bigInteger.compareTo(bigInteger3) >= 0 || bigInteger2.compareTo(ECConstants.ONE) < 0 || bigInteger2.compareTo(bigInteger3) >= 0) break block3;
                BigInteger bigInteger5 = bigInteger4.modInverse(bigInteger3);
                BigInteger bigInteger6 = bigInteger2.multiply(bigInteger5).mod(bigInteger3);
                BigInteger bigInteger7 = bigInteger3.subtract(bigInteger).multiply(bigInteger5).mod(bigInteger3);
                eCPoint = ECAlgorithms.sumOfTwoMultiplies((ECPoint)this.key.getParameters().getG(), (BigInteger)bigInteger6, (ECPoint)((ECPublicKeyParameters)this.key).getQ(), (BigInteger)bigInteger7).normalize();
                if (!eCPoint.isInfinity()) break block4;
            }
            return false;
        }
        return eCPoint.getAffineXCoord().toBigInteger().mod(bigInteger3).equals((Object)bigInteger);
    }
}

