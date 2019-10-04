/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.interfaces.ECPrivateKey
 *  java.security.interfaces.ECPublicKey
 *  java.security.spec.ECParameterSpec
 *  java.security.spec.ECPoint
 *  java.security.spec.ECPrivateKeySpec
 *  java.security.spec.ECPublicKeySpec
 *  java.security.spec.EllipticCurve
 *  java.security.spec.KeySpec
 *  org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.jce.spec.ECParameterSpec
 *  org.bouncycastle.jce.spec.ECPrivateKeySpec
 *  org.bouncycastle.jce.spec.ECPublicKeySpec
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.jcajce.provider.asymmetric.dstu;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.KeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.ua.UAObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.BCDSTU4145PrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.dstu.BCDSTU4145PublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;

public class KeyFactorySpi
extends BaseKeyFactorySpi {
    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof org.bouncycastle.jce.spec.ECPrivateKeySpec) {
            return new BCDSTU4145PrivateKey((org.bouncycastle.jce.spec.ECPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof ECPrivateKeySpec) {
            return new BCDSTU4145PrivateKey((ECPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof ECPublicKeySpec) {
            return new BCDSTU4145PublicKey((ECPublicKeySpec)keySpec);
        }
        if (keySpec instanceof java.security.spec.ECPublicKeySpec) {
            return new BCDSTU4145PublicKey((java.security.spec.ECPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }

    @Override
    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(java.security.spec.ECPublicKeySpec.class) && key instanceof ECPublicKey) {
            ECPublicKey eCPublicKey = (ECPublicKey)key;
            if (eCPublicKey.getParams() != null) {
                return new java.security.spec.ECPublicKeySpec(eCPublicKey.getW(), eCPublicKey.getParams());
            }
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new java.security.spec.ECPublicKeySpec(eCPublicKey.getW(), EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCParameterSpec));
        }
        if (class_.isAssignableFrom(ECPrivateKeySpec.class) && key instanceof ECPrivateKey) {
            ECPrivateKey eCPrivateKey = (ECPrivateKey)key;
            if (eCPrivateKey.getParams() != null) {
                return new ECPrivateKeySpec(eCPrivateKey.getS(), eCPrivateKey.getParams());
            }
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new ECPrivateKeySpec(eCPrivateKey.getS(), EC5Util.convertSpec((EllipticCurve)EC5Util.convertCurve((ECCurve)eCParameterSpec.getCurve(), (byte[])eCParameterSpec.getSeed()), (org.bouncycastle.jce.spec.ECParameterSpec)eCParameterSpec));
        }
        if (class_.isAssignableFrom(ECPublicKeySpec.class) && key instanceof ECPublicKey) {
            ECPublicKey eCPublicKey = (ECPublicKey)key;
            if (eCPublicKey.getParams() != null) {
                return new ECPublicKeySpec(EC5Util.convertPoint((ECParameterSpec)eCPublicKey.getParams(), (ECPoint)eCPublicKey.getW(), (boolean)false), EC5Util.convertSpec((ECParameterSpec)eCPublicKey.getParams(), (boolean)false));
            }
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new ECPublicKeySpec(EC5Util.convertPoint((ECParameterSpec)eCPublicKey.getParams(), (ECPoint)eCPublicKey.getW(), (boolean)false), eCParameterSpec);
        }
        if (class_.isAssignableFrom(org.bouncycastle.jce.spec.ECPrivateKeySpec.class) && key instanceof ECPrivateKey) {
            ECPrivateKey eCPrivateKey = (ECPrivateKey)key;
            if (eCPrivateKey.getParams() != null) {
                return new org.bouncycastle.jce.spec.ECPrivateKeySpec(eCPrivateKey.getS(), EC5Util.convertSpec((ECParameterSpec)eCPrivateKey.getParams(), (boolean)false));
            }
            org.bouncycastle.jce.spec.ECParameterSpec eCParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new org.bouncycastle.jce.spec.ECPrivateKeySpec(eCPrivateKey.getS(), eCParameterSpec);
        }
        return super.engineGetKeySpec(key, class_);
    }

    protected Key engineTranslateKey(Key key) {
        throw new InvalidKeyException("key type unknown");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(UAObjectIdentifiers.dstu4145le) || aSN1ObjectIdentifier.equals(UAObjectIdentifiers.dstu4145be)) {
            return new BCDSTU4145PrivateKey(privateKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(UAObjectIdentifiers.dstu4145le) || aSN1ObjectIdentifier.equals(UAObjectIdentifiers.dstu4145be)) {
            return new BCDSTU4145PublicKey(subjectPublicKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }
}

