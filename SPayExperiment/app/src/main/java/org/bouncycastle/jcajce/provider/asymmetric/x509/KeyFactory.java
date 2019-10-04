/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.KeyFactorySpi
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.X509EncodedKeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyFactory
extends KeyFactorySpi {
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            PrivateKeyInfo privateKeyInfo;
            block4 : {
                try {
                    privateKeyInfo = PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded());
                    PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(privateKeyInfo);
                    if (privateKey == null) break block4;
                    return privateKey;
                }
                catch (Exception exception) {
                    throw new InvalidKeySpecException(exception.toString());
                }
            }
            throw new InvalidKeySpecException("no factory found for OID: " + privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }

    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof X509EncodedKeySpec) {
            SubjectPublicKeyInfo subjectPublicKeyInfo;
            block4 : {
                try {
                    subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(((X509EncodedKeySpec)keySpec).getEncoded());
                    PublicKey publicKey = BouncyCastleProvider.getPublicKey(subjectPublicKeyInfo);
                    if (publicKey == null) break block4;
                    return publicKey;
                }
                catch (Exception exception) {
                    throw new InvalidKeySpecException(exception.toString());
                }
            }
            throw new InvalidKeySpecException("no factory found for OID: " + subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }

    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(PKCS8EncodedKeySpec.class) && key.getFormat().equals((Object)"PKCS#8")) {
            return new PKCS8EncodedKeySpec(key.getEncoded());
        }
        if (class_.isAssignableFrom(X509EncodedKeySpec.class) && key.getFormat().equals((Object)"X.509")) {
            return new X509EncodedKeySpec(key.getEncoded());
        }
        throw new InvalidKeySpecException("not implemented yet " + (Object)key + " " + (Object)class_);
    }

    protected Key engineTranslateKey(Key key) {
        throw new InvalidKeyException("not implemented yet " + (Object)key);
    }
}

