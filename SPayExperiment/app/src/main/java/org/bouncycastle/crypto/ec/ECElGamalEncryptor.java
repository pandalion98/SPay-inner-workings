/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 */
package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ec.ECEncryptor;
import org.bouncycastle.crypto.ec.ECPair;
import org.bouncycastle.crypto.ec.ECUtil;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECElGamalEncryptor
implements ECEncryptor {
    private ECPublicKeyParameters key;
    private SecureRandom random;

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    @Override
    public ECPair encrypt(ECPoint eCPoint) {
        if (this.key == null) {
            throw new IllegalStateException("ECElGamalEncryptor not initialised");
        }
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger = ECUtil.generateK(eCDomainParameters.getN(), this.random);
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        ECPoint[] arreCPoint = new ECPoint[]{eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger), this.key.getQ().multiply(bigInteger).add(eCPoint)};
        eCDomainParameters.getCurve().normalizeAll(arreCPoint);
        return new ECPair(arreCPoint[0], arreCPoint[1]);
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            if (!(parametersWithRandom.getParameters() instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for encryption.");
            }
            this.key = (ECPublicKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
            return;
        }
        if (!(cipherParameters instanceof ECPublicKeyParameters)) {
            throw new IllegalArgumentException("ECPublicKeyParameters are required for encryption.");
        }
        this.key = (ECPublicKeyParameters)cipherParameters;
        this.random = new SecureRandom();
    }
}

