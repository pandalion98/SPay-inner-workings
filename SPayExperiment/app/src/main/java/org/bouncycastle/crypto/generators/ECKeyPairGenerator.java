/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 *  org.bouncycastle.math.ec.WNafUtil
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;
import org.bouncycastle.math.ec.WNafUtil;

public class ECKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator,
ECConstants {
    ECDomainParameters params;
    SecureRandom random;

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger bigInteger;
        BigInteger bigInteger2 = this.params.getN();
        int n2 = bigInteger2.bitLength();
        int n3 = n2 >>> 2;
        while ((bigInteger = new BigInteger(n2, (Random)this.random)).compareTo(TWO) < 0 || bigInteger.compareTo(bigInteger2) >= 0 || WNafUtil.getNafWeight((BigInteger)bigInteger) < n3) {
        }
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(this.createBasePointMultiplier().multiply(this.params.getG(), bigInteger), this.params), new ECPrivateKeyParameters(bigInteger, this.params));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        ECKeyGenerationParameters eCKeyGenerationParameters = (ECKeyGenerationParameters)keyGenerationParameters;
        this.random = eCKeyGenerationParameters.getRandom();
        this.params = eCKeyGenerationParameters.getDomainParameters();
        if (this.random == null) {
            this.random = new SecureRandom();
        }
    }
}

