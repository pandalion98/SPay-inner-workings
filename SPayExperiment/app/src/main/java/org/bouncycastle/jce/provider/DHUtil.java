/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  javax.crypto.interfaces.DHPrivateKey
 *  javax.crypto.interfaces.DHPublicKey
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jce.provider;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;

public class DHUtil {
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey)privateKey;
            return new DHPrivateKeyParameters(dHPrivateKey.getX(), new DHParameters(dHPrivateKey.getParams().getP(), dHPrivateKey.getParams().getG(), null, dHPrivateKey.getParams().getL()));
        }
        throw new InvalidKeyException("can't identify DH private key.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof DHPublicKey) {
            DHPublicKey dHPublicKey = (DHPublicKey)publicKey;
            return new DHPublicKeyParameters(dHPublicKey.getY(), new DHParameters(dHPublicKey.getParams().getP(), dHPublicKey.getParams().getG(), null, dHPublicKey.getParams().getL()));
        }
        throw new InvalidKeyException("can't identify DH public key.");
    }
}

