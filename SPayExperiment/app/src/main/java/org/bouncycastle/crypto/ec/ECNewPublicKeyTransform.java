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
import org.bouncycastle.crypto.ec.ECPair;
import org.bouncycastle.crypto.ec.ECPairTransform;
import org.bouncycastle.crypto.ec.ECUtil;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECNewPublicKeyTransform
implements ECPairTransform {
    private ECPublicKeyParameters key;
    private SecureRandom random;

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            if (!(parametersWithRandom.getParameters() instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for new public key transform.");
            }
            this.key = (ECPublicKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
            return;
        }
        if (!(cipherParameters instanceof ECPublicKeyParameters)) {
            throw new IllegalArgumentException("ECPublicKeyParameters are required for new public key transform.");
        }
        this.key = (ECPublicKeyParameters)cipherParameters;
        this.random = new SecureRandom();
    }

    @Override
    public ECPair transform(ECPair eCPair) {
        if (this.key == null) {
            throw new IllegalStateException("ECNewPublicKeyTransform not initialised");
        }
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger = eCDomainParameters.getN();
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        BigInteger bigInteger2 = ECUtil.generateK(bigInteger, this.random);
        ECPoint[] arreCPoint = new ECPoint[]{eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger2), this.key.getQ().multiply(bigInteger2).add(eCPair.getY())};
        eCDomainParameters.getCurve().normalizeAll(arreCPoint);
        return new ECPair(arreCPoint[0], arreCPoint[1]);
    }
}

