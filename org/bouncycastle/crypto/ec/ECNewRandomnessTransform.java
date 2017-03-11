package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECNewRandomnessTransform implements ECPairFactorTransform {
    private ECPublicKeyParameters key;
    private BigInteger lastK;
    private SecureRandom random;

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    public BigInteger getTransformValue() {
        return this.lastK;
    }

    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            if (parametersWithRandom.getParameters() instanceof ECPublicKeyParameters) {
                this.key = (ECPublicKeyParameters) parametersWithRandom.getParameters();
                this.random = parametersWithRandom.getRandom();
                return;
            }
            throw new IllegalArgumentException("ECPublicKeyParameters are required for new randomness transform.");
        } else if (cipherParameters instanceof ECPublicKeyParameters) {
            this.key = (ECPublicKeyParameters) cipherParameters;
            this.random = new SecureRandom();
        } else {
            throw new IllegalArgumentException("ECPublicKeyParameters are required for new randomness transform.");
        }
    }

    public ECPair transform(ECPair eCPair) {
        if (this.key == null) {
            throw new IllegalStateException("ECNewRandomnessTransform not initialised");
        }
        ECDomainParameters parameters = this.key.getParameters();
        BigInteger n = parameters.getN();
        ECMultiplier createBasePointMultiplier = createBasePointMultiplier();
        ECPoint[] eCPointArr = new ECPoint[]{createBasePointMultiplier.multiply(parameters.getG(), n).add(eCPair.getX()), this.key.getQ().multiply(ECUtil.generateK(n, this.random)).add(eCPair.getY())};
        parameters.getCurve().normalizeAll(eCPointArr);
        this.lastK = n;
        return new ECPair(eCPointArr[0], eCPointArr[1]);
    }
}
