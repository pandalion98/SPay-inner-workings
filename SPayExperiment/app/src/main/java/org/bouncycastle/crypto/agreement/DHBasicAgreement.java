/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.agreement;

import java.math.BigInteger;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class DHBasicAgreement
implements BasicAgreement {
    private DHParameters dhParams;
    private DHPrivateKeyParameters key;

    @Override
    public BigInteger calculateAgreement(CipherParameters cipherParameters) {
        DHPublicKeyParameters dHPublicKeyParameters = (DHPublicKeyParameters)cipherParameters;
        if (!dHPublicKeyParameters.getParameters().equals(this.dhParams)) {
            throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
        }
        return dHPublicKeyParameters.getY().modPow(this.key.getX(), this.dhParams.getP());
    }

    @Override
    public int getFieldSize() {
        return (7 + this.key.getParameters().getP().bitLength()) / 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (!(asymmetricKeyParameter instanceof DHPrivateKeyParameters)) {
            throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
        }
        this.key = (DHPrivateKeyParameters)asymmetricKeyParameter;
        this.dhParams = this.key.getParameters();
    }
}

