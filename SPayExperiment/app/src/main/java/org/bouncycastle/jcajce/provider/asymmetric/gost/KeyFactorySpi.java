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
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.gost;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.gost.BCGOST3410PrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.gost.BCGOST3410PublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import org.bouncycastle.jce.interfaces.GOST3410PrivateKey;
import org.bouncycastle.jce.interfaces.GOST3410PublicKey;
import org.bouncycastle.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeySpec;

public class KeyFactorySpi
extends BaseKeyFactorySpi {
    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof GOST3410PrivateKeySpec) {
            return new BCGOST3410PrivateKey((GOST3410PrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof GOST3410PublicKeySpec) {
            return new BCGOST3410PublicKey((GOST3410PublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }

    @Override
    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(GOST3410PublicKeySpec.class) && key instanceof GOST3410PublicKey) {
            GOST3410PublicKey gOST3410PublicKey = (GOST3410PublicKey)key;
            GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = gOST3410PublicKey.getParameters().getPublicKeyParameters();
            return new GOST3410PublicKeySpec(gOST3410PublicKey.getY(), gOST3410PublicKeyParameterSetSpec.getP(), gOST3410PublicKeyParameterSetSpec.getQ(), gOST3410PublicKeyParameterSetSpec.getA());
        }
        if (class_.isAssignableFrom(GOST3410PrivateKeySpec.class) && key instanceof GOST3410PrivateKey) {
            GOST3410PrivateKey gOST3410PrivateKey = (GOST3410PrivateKey)key;
            GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = gOST3410PrivateKey.getParameters().getPublicKeyParameters();
            return new GOST3410PrivateKeySpec(gOST3410PrivateKey.getX(), gOST3410PublicKeyParameterSetSpec.getP(), gOST3410PublicKeyParameterSetSpec.getQ(), gOST3410PublicKeyParameterSetSpec.getA());
        }
        return super.engineGetKeySpec(key, class_);
    }

    protected Key engineTranslateKey(Key key) {
        if (key instanceof GOST3410PublicKey) {
            return new BCGOST3410PublicKey((GOST3410PublicKey)key);
        }
        if (key instanceof GOST3410PrivateKey) {
            return new BCGOST3410PrivateKey((GOST3410PrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_94)) {
            return new BCGOST3410PrivateKey(privateKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_94)) {
            return new BCGOST3410PublicKey(subjectPublicKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }
}

