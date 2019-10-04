/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAKCalculator;
import org.bouncycastle.crypto.signers.RandomDSAKCalculator;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECDSASigner
implements DSA,
ECConstants {
    private final DSAKCalculator kCalculator;
    private ECKeyParameters key;
    private SecureRandom random;

    public ECDSASigner() {
        this.kCalculator = new RandomDSAKCalculator();
    }

    public ECDSASigner(DSAKCalculator dSAKCalculator) {
        this.kCalculator = dSAKCalculator;
    }

    protected BigInteger calculateE(BigInteger bigInteger, byte[] arrby) {
        int n2 = bigInteger.bitLength();
        int n3 = 8 * arrby.length;
        BigInteger bigInteger2 = new BigInteger(1, arrby);
        if (n2 < n3) {
            bigInteger2 = bigInteger2.shiftRight(n3 - n2);
        }
        return bigInteger2;
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public BigInteger[] generateSignature(byte[] arrby) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        BigInteger bigInteger3;
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger4 = eCDomainParameters.getN();
        BigInteger bigInteger5 = this.calculateE(bigInteger4, arrby);
        BigInteger bigInteger6 = ((ECPrivateKeyParameters)this.key).getD();
        if (this.kCalculator.isDeterministic()) {
            this.kCalculator.init(bigInteger4, bigInteger6, arrby);
        } else {
            this.kCalculator.init(bigInteger4, this.random);
        }
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        do {
            bigInteger3 = this.kCalculator.nextK();
        } while ((bigInteger = eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger3).normalize().getAffineXCoord().toBigInteger().mod(bigInteger4)).equals((Object)ZERO) || (bigInteger2 = bigInteger3.modInverse(bigInteger4).multiply(bigInteger5.add(bigInteger6.multiply(bigInteger))).mod(bigInteger4)).equals((Object)ZERO));
        return new BigInteger[]{bigInteger, bigInteger2};
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
                this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
                secureRandom = parametersWithRandom.getRandom();
            } else {
                this.key = (ECPrivateKeyParameters)cipherParameters;
                secureRandom = null;
            }
        } else {
            this.key = (ECPublicKeyParameters)cipherParameters;
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

    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger3 = eCDomainParameters.getN();
        BigInteger bigInteger4 = this.calculateE(bigInteger3, arrby);
        if (bigInteger.compareTo(ONE) < 0 || bigInteger.compareTo(bigInteger3) >= 0) {
            return false;
        }
        if (bigInteger2.compareTo(ONE) < 0 || bigInteger2.compareTo(bigInteger3) >= 0) {
            return false;
        }
        BigInteger bigInteger5 = bigInteger2.modInverse(bigInteger3);
        BigInteger bigInteger6 = bigInteger4.multiply(bigInteger5).mod(bigInteger3);
        BigInteger bigInteger7 = bigInteger.multiply(bigInteger5).mod(bigInteger3);
        ECPoint eCPoint = ECAlgorithms.sumOfTwoMultiplies((ECPoint)eCDomainParameters.getG(), (BigInteger)bigInteger6, (ECPoint)((ECPublicKeyParameters)this.key).getQ(), (BigInteger)bigInteger7).normalize();
        if (eCPoint.isInfinity()) {
            return false;
        }
        return eCPoint.getAffineXCoord().toBigInteger().mod(bigInteger3).equals((Object)bigInteger);
    }
}

