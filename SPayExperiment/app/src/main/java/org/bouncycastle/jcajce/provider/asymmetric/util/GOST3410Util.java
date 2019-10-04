/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.PrivateKey
 *  java.security.PublicKey
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import org.bouncycastle.jce.interfaces.GOST3410PrivateKey;
import org.bouncycastle.jce.interfaces.GOST3410PublicKey;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public class GOST3410Util {
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) {
        if (privateKey instanceof GOST3410PrivateKey) {
            GOST3410PrivateKey gOST3410PrivateKey = (GOST3410PrivateKey)privateKey;
            GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = gOST3410PrivateKey.getParameters().getPublicKeyParameters();
            return new GOST3410PrivateKeyParameters(gOST3410PrivateKey.getX(), new GOST3410Parameters(gOST3410PublicKeyParameterSetSpec.getP(), gOST3410PublicKeyParameterSetSpec.getQ(), gOST3410PublicKeyParameterSetSpec.getA()));
        }
        throw new InvalidKeyException("can't identify GOST3410 private key.");
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) {
        if (publicKey instanceof GOST3410PublicKey) {
            GOST3410PublicKey gOST3410PublicKey = (GOST3410PublicKey)publicKey;
            GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = gOST3410PublicKey.getParameters().getPublicKeyParameters();
            return new GOST3410PublicKeyParameters(gOST3410PublicKey.getY(), new GOST3410Parameters(gOST3410PublicKeyParameterSetSpec.getP(), gOST3410PublicKeyParameterSetSpec.getQ(), gOST3410PublicKeyParameterSetSpec.getA()));
        }
        throw new InvalidKeyException("can't identify GOST3410 public key: " + publicKey.getClass().getName());
    }
}

