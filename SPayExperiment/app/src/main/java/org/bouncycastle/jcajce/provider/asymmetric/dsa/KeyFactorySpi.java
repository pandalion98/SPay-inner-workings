/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.interfaces.DSAParams
 *  java.security.interfaces.DSAPrivateKey
 *  java.security.interfaces.DSAPublicKey
 *  java.security.spec.DSAPrivateKeySpec
 *  java.security.spec.DSAPublicKeySpec
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.BCDSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.BCDSAPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSAUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;

public class KeyFactorySpi
extends BaseKeyFactorySpi {
    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof DSAPrivateKeySpec) {
            return new BCDSAPrivateKey((DSAPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof DSAPublicKeySpec) {
            return new BCDSAPublicKey((DSAPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }

    @Override
    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(DSAPublicKeySpec.class) && key instanceof DSAPublicKey) {
            DSAPublicKey dSAPublicKey = (DSAPublicKey)key;
            return new DSAPublicKeySpec(dSAPublicKey.getY(), dSAPublicKey.getParams().getP(), dSAPublicKey.getParams().getQ(), dSAPublicKey.getParams().getG());
        }
        if (class_.isAssignableFrom(DSAPrivateKeySpec.class) && key instanceof DSAPrivateKey) {
            DSAPrivateKey dSAPrivateKey = (DSAPrivateKey)key;
            return new DSAPrivateKeySpec(dSAPrivateKey.getX(), dSAPrivateKey.getParams().getP(), dSAPrivateKey.getParams().getQ(), dSAPrivateKey.getParams().getG());
        }
        return super.engineGetKeySpec(key, class_);
    }

    protected Key engineTranslateKey(Key key) {
        if (key instanceof DSAPublicKey) {
            return new BCDSAPublicKey((DSAPublicKey)key);
        }
        if (key instanceof DSAPrivateKey) {
            return new BCDSAPrivateKey((DSAPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (DSAUtil.isDsaOid(aSN1ObjectIdentifier)) {
            return new BCDSAPrivateKey(privateKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (DSAUtil.isDsaOid(aSN1ObjectIdentifier)) {
            return new BCDSAPublicKey(subjectPublicKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }
}

