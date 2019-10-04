/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public class ECNRSigner
implements DSA {
    private boolean forSigning;
    private ECKeyParameters key;
    private SecureRandom random;

    @Override
    public BigInteger[] generateSignature(byte[] arrby) {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair;
        ECKeyPairGenerator eCKeyPairGenerator;
        BigInteger bigInteger;
        if (!this.forSigning) {
            throw new IllegalStateException("not initialised for signing");
        }
        BigInteger bigInteger2 = ((ECPrivateKeyParameters)this.key).getParameters().getN();
        int n2 = bigInteger2.bitLength();
        BigInteger bigInteger3 = new BigInteger(1, arrby);
        int n3 = bigInteger3.bitLength();
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)this.key;
        if (n3 > n2) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        do {
            eCKeyPairGenerator = new ECKeyPairGenerator();
            eCKeyPairGenerator.init(new ECKeyGenerationParameters(eCPrivateKeyParameters.getParameters(), this.random));
        } while ((bigInteger = ((ECPublicKeyParameters)(asymmetricCipherKeyPair = eCKeyPairGenerator.generateKeyPair()).getPublic()).getQ().getAffineXCoord().toBigInteger().add(bigInteger3).mod(bigInteger2)).equals((Object)ECConstants.ZERO));
        BigInteger bigInteger4 = eCPrivateKeyParameters.getD();
        return new BigInteger[]{bigInteger, ((ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate()).getD().subtract(bigInteger.multiply(bigInteger4)).mod(bigInteger2)};
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forSigning = bl;
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

    @Override
    public boolean verifySignature(byte[] arrby, BigInteger bigInteger, BigInteger bigInteger2) {
        if (this.forSigning) {
            throw new IllegalStateException("not initialised for verifying");
        }
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)this.key;
        BigInteger bigInteger3 = eCPublicKeyParameters.getParameters().getN();
        int n2 = bigInteger3.bitLength();
        BigInteger bigInteger4 = new BigInteger(1, arrby);
        if (bigInteger4.bitLength() > n2) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        if (bigInteger.compareTo(ECConstants.ONE) < 0 || bigInteger.compareTo(bigInteger3) >= 0) {
            return false;
        }
        if (bigInteger2.compareTo(ECConstants.ZERO) < 0 || bigInteger2.compareTo(bigInteger3) >= 0) {
            return false;
        }
        ECPoint eCPoint = ECAlgorithms.sumOfTwoMultiplies((ECPoint)eCPublicKeyParameters.getParameters().getG(), (BigInteger)bigInteger2, (ECPoint)eCPublicKeyParameters.getQ(), (BigInteger)bigInteger).normalize();
        if (eCPoint.isInfinity()) {
            return false;
        }
        return bigInteger.subtract(eCPoint.getAffineXCoord().toBigInteger()).mod(bigInteger3).equals((Object)bigInteger4);
    }
}

