/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
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
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

public class ECDHBasicAgreement
implements BasicAgreement {
    private ECPrivateKeyParameters key;

    @Override
    public BigInteger calculateAgreement(CipherParameters cipherParameters) {
        ECPoint eCPoint = ((ECPublicKeyParameters)cipherParameters).getQ().multiply(this.key.getD()).normalize();
        if (eCPoint.isInfinity()) {
            throw new IllegalStateException("Infinity is not a valid agreement value for ECDH");
        }
        return eCPoint.getAffineXCoord().toBigInteger();
    }

    @Override
    public int getFieldSize() {
        return (7 + this.key.getParameters().getCurve().getFieldSize()) / 8;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
}

