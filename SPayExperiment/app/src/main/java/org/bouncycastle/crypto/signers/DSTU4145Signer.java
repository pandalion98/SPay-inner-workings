/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 *  org.bouncycastle.util.Arrays
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
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.util.Arrays;

public class DSTU4145Signer
implements DSA {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private ECKeyParameters key;
    private SecureRandom random;

    private static BigInteger fieldElement2Integer(BigInteger bigInteger, ECFieldElement eCFieldElement) {
        return DSTU4145Signer.truncate(eCFieldElement.toBigInteger(), -1 + bigInteger.bitLength());
    }

    private static BigInteger generateRandomInteger(BigInteger bigInteger, SecureRandom secureRandom) {
        return new BigInteger(-1 + bigInteger.bitLength(), (Random)secureRandom);
    }

    private static ECFieldElement hash2FieldElement(ECCurve eCCurve, byte[] arrby) {
        return eCCurve.fromBigInteger(DSTU4145Signer.truncate(new BigInteger(1, Arrays.reverse((byte[])arrby)), eCCurve.getFieldSize()));
    }

    private static BigInteger truncate(BigInteger bigInteger, int n2) {
        if (bigInteger.bitLength() > n2) {
            bigInteger = bigInteger.mod(ONE.shiftLeft(n2));
        }
        return bigInteger;
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
        ECFieldElement eCFieldElement;
        BigInteger bigInteger3;
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        ECCurve eCCurve = eCDomainParameters.getCurve();
        ECFieldElement eCFieldElement2 = DSTU4145Signer.hash2FieldElement(eCCurve, arrby);
        ECFieldElement eCFieldElement3 = eCFieldElement2.isZero() ? eCCurve.fromBigInteger(ONE) : eCFieldElement2;
        BigInteger bigInteger4 = eCDomainParameters.getN();
        BigInteger bigInteger5 = ((ECPrivateKeyParameters)this.key).getD();
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        do {
            bigInteger = DSTU4145Signer.generateRandomInteger(bigInteger4, this.random);
        } while ((eCFieldElement = eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger).normalize().getAffineXCoord()).isZero() || (bigInteger3 = DSTU4145Signer.fieldElement2Integer(bigInteger4, eCFieldElement3.multiply(eCFieldElement))).signum() == 0 || (bigInteger2 = bigInteger3.multiply(bigInteger5).add(bigInteger).mod(bigInteger4)).signum() == 0);
        return new BigInteger[]{bigInteger3, bigInteger2};
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        if (!bl) {
            this.key = (ECPublicKeyParameters)cipherParameters;
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            cipherParameters2 = parametersWithRandom.getParameters();
        } else {
            this.random = new SecureRandom();
            cipherParameters2 = cipherParameters;
        }
        this.key = (ECPrivateKeyParameters)cipherParameters2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger.signum() <= 0) return false;
        if (bigInteger2.signum() <= 0) {
            return false;
        }
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger3 = eCDomainParameters.getN();
        if (bigInteger.compareTo(bigInteger3) >= 0) return false;
        if (bigInteger2.compareTo(bigInteger3) >= 0) return false;
        ECCurve eCCurve = eCDomainParameters.getCurve();
        ECFieldElement eCFieldElement = DSTU4145Signer.hash2FieldElement(eCCurve, arrby);
        ECFieldElement eCFieldElement2 = eCFieldElement.isZero() ? eCCurve.fromBigInteger(ONE) : eCFieldElement;
        ECPoint eCPoint = ECAlgorithms.sumOfTwoMultiplies((ECPoint)eCDomainParameters.getG(), (BigInteger)bigInteger2, (ECPoint)((ECPublicKeyParameters)this.key).getQ(), (BigInteger)bigInteger).normalize();
        if (eCPoint.isInfinity()) return false;
        if (DSTU4145Signer.fieldElement2Integer(bigInteger3, eCFieldElement2.multiply(eCPoint.getAffineXCoord())).compareTo(bigInteger) != 0) return false;
        return true;
    }
}

