/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;

public class RainbowKeysToParams {
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof BCRainbowPrivateKey) {
            BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)privateKey;
            return new RainbowPrivateKeyParameters(bCRainbowPrivateKey.getInvA1(), bCRainbowPrivateKey.getB1(), bCRainbowPrivateKey.getInvA2(), bCRainbowPrivateKey.getB2(), bCRainbowPrivateKey.getVi(), bCRainbowPrivateKey.getLayers());
        }
        throw new InvalidKeyException("can't identify Rainbow private key.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof BCRainbowPublicKey) {
            BCRainbowPublicKey bCRainbowPublicKey = (BCRainbowPublicKey)publicKey;
            return new RainbowPublicKeyParameters(bCRainbowPublicKey.getDocLength(), bCRainbowPublicKey.getCoeffQuadratic(), bCRainbowPublicKey.getCoeffSingular(), bCRainbowPublicKey.getCoeffScalar());
        }
        throw new InvalidKeyException("can't identify Rainbow public key: " + publicKey.getClass().getName());
    }
}

