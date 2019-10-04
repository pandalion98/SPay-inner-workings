/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.Throwable
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.interfaces.RSAPrivateCrtKey
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.interfaces.RSAPublicKey
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.RSAPrivateCrtKeySpec
 *  java.security.spec.RSAPrivateKeySpec
 *  java.security.spec.RSAPublicKeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.bouncycastle.jcajce.provider.asymmetric.util.ExtendedInvalidKeySpecException;

public class KeyFactorySpi
extends BaseKeyFactorySpi {
    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                PrivateKey privateKey = this.generatePrivate(PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
                return privateKey;
            }
            catch (Exception exception) {
                try {
                    BCRSAPrivateCrtKey bCRSAPrivateCrtKey = new BCRSAPrivateCrtKey(org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
                    return bCRSAPrivateCrtKey;
                }
                catch (Exception exception2) {
                    throw new ExtendedInvalidKeySpecException("unable to process key spec: " + exception.toString(), exception);
                }
            }
        }
        if (keySpec instanceof RSAPrivateCrtKeySpec) {
            return new BCRSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
        }
        if (keySpec instanceof RSAPrivateKeySpec) {
            return new BCRSAPrivateKey((RSAPrivateKeySpec)keySpec);
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof RSAPublicKeySpec) {
            return new BCRSAPublicKey((RSAPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }

    @Override
    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(RSAPublicKeySpec.class) && key instanceof RSAPublicKey) {
            RSAPublicKey rSAPublicKey = (RSAPublicKey)key;
            return new RSAPublicKeySpec(rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
        }
        if (class_.isAssignableFrom(RSAPrivateKeySpec.class) && key instanceof RSAPrivateKey) {
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey)key;
            return new RSAPrivateKeySpec(rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent());
        }
        if (class_.isAssignableFrom(RSAPrivateCrtKeySpec.class) && key instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey)key;
            return new RSAPrivateCrtKeySpec(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient());
        }
        return super.engineGetKeySpec(key, class_);
    }

    protected Key engineTranslateKey(Key key) {
        if (key instanceof RSAPublicKey) {
            return new BCRSAPublicKey((RSAPublicKey)key);
        }
        if (key instanceof RSAPrivateCrtKey) {
            return new BCRSAPrivateCrtKey((RSAPrivateCrtKey)key);
        }
        if (key instanceof RSAPrivateKey) {
            return new BCRSAPrivateKey((RSAPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (RSAUtil.isRsaOid(aSN1ObjectIdentifier)) {
            org.bouncycastle.asn1.pkcs.RSAPrivateKey rSAPrivateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
            if (rSAPrivateKey.getCoefficient().intValue() == 0) {
                return new BCRSAPrivateKey(rSAPrivateKey);
            }
            return new BCRSAPrivateCrtKey(privateKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (RSAUtil.isRsaOid(aSN1ObjectIdentifier)) {
            return new BCRSAPublicKey(subjectPublicKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }
}

