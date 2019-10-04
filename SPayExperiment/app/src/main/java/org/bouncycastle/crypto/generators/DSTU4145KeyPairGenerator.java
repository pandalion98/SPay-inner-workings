/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class DSTU4145KeyPairGenerator
extends ECKeyPairGenerator {
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = super.generateKeyPair();
        ECPublicKeyParameters eCPublicKeyParameters = (ECPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        ECPrivateKeyParameters eCPrivateKeyParameters = (ECPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(eCPublicKeyParameters.getQ().negate(), eCPublicKeyParameters.getParameters()), eCPrivateKeyParameters);
    }
}

