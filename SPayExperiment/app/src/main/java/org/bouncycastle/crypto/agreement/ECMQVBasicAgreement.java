/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  org.bouncycastle.math.ec.ECAlgorithms
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECFieldElement
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public class ECMQVBasicAgreement
implements BasicAgreement {
    MQVPrivateParameters privParams;

    /*
     * Enabled aggressive block sorting
     */
    private ECPoint calculateMqvAgreement(ECDomainParameters eCDomainParameters, ECPrivateKeyParameters eCPrivateKeyParameters, ECPrivateKeyParameters eCPrivateKeyParameters2, ECPublicKeyParameters eCPublicKeyParameters, ECPublicKeyParameters eCPublicKeyParameters2, ECPublicKeyParameters eCPublicKeyParameters3) {
        BigInteger bigInteger = eCDomainParameters.getN();
        int n2 = (1 + bigInteger.bitLength()) / 2;
        BigInteger bigInteger2 = ECConstants.ONE.shiftLeft(n2);
        ECCurve eCCurve = eCDomainParameters.getCurve();
        ECPoint[] arreCPoint = new ECPoint[3];
        ECPoint eCPoint = eCPublicKeyParameters == null ? eCDomainParameters.getG().multiply(eCPrivateKeyParameters2.getD()) : eCPublicKeyParameters.getQ();
        arreCPoint[0] = ECAlgorithms.importPoint((ECCurve)eCCurve, (ECPoint)eCPoint);
        arreCPoint[1] = ECAlgorithms.importPoint((ECCurve)eCCurve, (ECPoint)eCPublicKeyParameters2.getQ());
        arreCPoint[2] = ECAlgorithms.importPoint((ECCurve)eCCurve, (ECPoint)eCPublicKeyParameters3.getQ());
        eCCurve.normalizeAll(arreCPoint);
        ECPoint eCPoint2 = arreCPoint[0];
        ECPoint eCPoint3 = arreCPoint[1];
        ECPoint eCPoint4 = arreCPoint[2];
        BigInteger bigInteger3 = eCPoint2.getAffineXCoord().toBigInteger().mod(bigInteger2).setBit(n2);
        BigInteger bigInteger4 = eCPrivateKeyParameters.getD().multiply(bigInteger3).add(eCPrivateKeyParameters2.getD()).mod(bigInteger);
        BigInteger bigInteger5 = eCPoint4.getAffineXCoord().toBigInteger().mod(bigInteger2).setBit(n2);
        BigInteger bigInteger6 = eCDomainParameters.getH().multiply(bigInteger4).mod(bigInteger);
        return ECAlgorithms.sumOfTwoMultiplies((ECPoint)eCPoint3, (BigInteger)bigInteger5.multiply(bigInteger6).mod(bigInteger), (ECPoint)eCPoint4, (BigInteger)bigInteger6);
    }

    @Override
    public BigInteger calculateAgreement(CipherParameters cipherParameters) {
        MQVPublicParameters mQVPublicParameters = (MQVPublicParameters)cipherParameters;
        ECPrivateKeyParameters eCPrivateKeyParameters = this.privParams.getStaticPrivateKey();
        ECPoint eCPoint = this.calculateMqvAgreement(eCPrivateKeyParameters.getParameters(), eCPrivateKeyParameters, this.privParams.getEphemeralPrivateKey(), this.privParams.getEphemeralPublicKey(), mQVPublicParameters.getStaticPublicKey(), mQVPublicParameters.getEphemeralPublicKey()).normalize();
        if (eCPoint.isInfinity()) {
            throw new IllegalStateException("Infinity is not a valid agreement value for MQV");
        }
        return eCPoint.getAffineXCoord().toBigInteger();
    }

    @Override
    public int getFieldSize() {
        return (7 + this.privParams.getStaticPrivateKey().getParameters().getCurve().getFieldSize()) / 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.privParams = (MQVPrivateParameters)cipherParameters;
    }
}

