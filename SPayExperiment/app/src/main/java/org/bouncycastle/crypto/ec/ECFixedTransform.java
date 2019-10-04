/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECMultiplier
 *  org.bouncycastle.math.ec.ECPoint
 *  org.bouncycastle.math.ec.FixedPointCombMultiplier
 */
package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.ec.ECPair;
import org.bouncycastle.crypto.ec.ECPairFactorTransform;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class ECFixedTransform
implements ECPairFactorTransform {
    private BigInteger k;
    private ECPublicKeyParameters key;

    public ECFixedTransform(BigInteger bigInteger) {
        this.k = bigInteger;
    }

    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }

    @Override
    public BigInteger getTransformValue() {
        return this.k;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ECPublicKeyParameters)) {
            throw new IllegalArgumentException("ECPublicKeyParameters are required for fixed transform.");
        }
        this.key = (ECPublicKeyParameters)cipherParameters;
    }

    @Override
    public ECPair transform(ECPair eCPair) {
        if (this.key == null) {
            throw new IllegalStateException("ECFixedTransform not initialised");
        }
        ECDomainParameters eCDomainParameters = this.key.getParameters();
        BigInteger bigInteger = eCDomainParameters.getN();
        ECMultiplier eCMultiplier = this.createBasePointMultiplier();
        BigInteger bigInteger2 = this.k.mod(bigInteger);
        ECPoint[] arreCPoint = new ECPoint[]{eCMultiplier.multiply(eCDomainParameters.getG(), bigInteger2).add(eCPair.getX()), this.key.getQ().multiply(bigInteger2).add(eCPair.getY())};
        eCDomainParameters.getCurve().normalizeAll(arreCPoint);
        return new ECPair(arreCPoint[0], arreCPoint[1]);
    }
}

