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
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public class ElGamalUtil {
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof ElGamalPrivateKey) {
            ElGamalPrivateKey elGamalPrivateKey = (ElGamalPrivateKey)privateKey;
            return new ElGamalPrivateKeyParameters(elGamalPrivateKey.getX(), new ElGamalParameters(elGamalPrivateKey.getParameters().getP(), elGamalPrivateKey.getParameters().getG()));
        }
        if (privateKey instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey)privateKey;
            return new ElGamalPrivateKeyParameters(dHPrivateKey.getX(), new ElGamalParameters(dHPrivateKey.getParams().getP(), dHPrivateKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify private key for El Gamal.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof ElGamalPublicKey) {
            ElGamalPublicKey elGamalPublicKey = (ElGamalPublicKey)publicKey;
            return new ElGamalPublicKeyParameters(elGamalPublicKey.getY(), new ElGamalParameters(elGamalPublicKey.getParameters().getP(), elGamalPublicKey.getParameters().getG()));
        }
        if (publicKey instanceof DHPublicKey) {
            DHPublicKey dHPublicKey = (DHPublicKey)publicKey;
            return new ElGamalPublicKeyParameters(dHPublicKey.getY(), new ElGamalParameters(dHPublicKey.getParams().getP(), dHPublicKey.getParams().getG()));
        }
        throw new InvalidKeyException("can't identify public key for El Gamal.");
    }
}

