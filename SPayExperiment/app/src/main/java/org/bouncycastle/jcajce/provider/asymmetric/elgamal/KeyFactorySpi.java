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
 *  javax.crypto.interfaces.DHPrivateKey
 *  javax.crypto.interfaces.DHPublicKey
 *  javax.crypto.spec.DHParameterSpec
 *  javax.crypto.spec.DHPrivateKeySpec
 *  javax.crypto.spec.DHPublicKeySpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi;
import org.bouncycastle.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.spec.ElGamalPrivateKeySpec;
import org.bouncycastle.jce.spec.ElGamalPublicKeySpec;

public class KeyFactorySpi
extends BaseKeyFactorySpi {
    @Override
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof ElGamalPrivateKeySpec) {
            return new BCElGamalPrivateKey((ElGamalPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof DHPrivateKeySpec) {
            return new BCElGamalPrivateKey((DHPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }

    @Override
    protected PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof ElGamalPublicKeySpec) {
            return new BCElGamalPublicKey((ElGamalPublicKeySpec)keySpec);
        }
        if (keySpec instanceof DHPublicKeySpec) {
            return new BCElGamalPublicKey((DHPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }

    @Override
    protected KeySpec engineGetKeySpec(Key key, Class class_) {
        if (class_.isAssignableFrom(DHPrivateKeySpec.class) && key instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey)key;
            return new DHPrivateKeySpec(dHPrivateKey.getX(), dHPrivateKey.getParams().getP(), dHPrivateKey.getParams().getG());
        }
        if (class_.isAssignableFrom(DHPublicKeySpec.class) && key instanceof DHPublicKey) {
            DHPublicKey dHPublicKey = (DHPublicKey)key;
            return new DHPublicKeySpec(dHPublicKey.getY(), dHPublicKey.getParams().getP(), dHPublicKey.getParams().getG());
        }
        return super.engineGetKeySpec(key, class_);
    }

    protected Key engineTranslateKey(Key key) {
        if (key instanceof DHPublicKey) {
            return new BCElGamalPublicKey((DHPublicKey)key);
        }
        if (key instanceof DHPrivateKey) {
            return new BCElGamalPrivateKey((DHPrivateKey)key);
        }
        if (key instanceof ElGamalPublicKey) {
            return new BCElGamalPublicKey((ElGamalPublicKey)key);
        }
        if (key instanceof ElGamalPrivateKey) {
            return new BCElGamalPrivateKey((ElGamalPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
        }
        if (aSN1ObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
        }
        if (aSN1ObjectIdentifier.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        if (aSN1ObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        if (aSN1ObjectIdentifier.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        throw new IOException("algorithm identifier " + aSN1ObjectIdentifier + " in key not recognised");
    }
}

