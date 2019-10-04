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
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.pqc.asn1.RainbowPrivateKey;
import org.bouncycastle.pqc.asn1.RainbowPublicKey;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
import org.bouncycastle.pqc.jcajce.spec.RainbowPrivateKeySpec;
import org.bouncycastle.pqc.jcajce.spec.RainbowPublicKeySpec;

public class RainbowKeyFactorySpi
extends KeyFactorySpi
implements AsymmetricKeyInfoConverter {
    public PrivateKey engineGeneratePrivate(KeySpec keySpec) {
        if (keySpec instanceof RainbowPrivateKeySpec) {
            return new BCRainbowPrivateKey((RainbowPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            byte[] arrby = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
            try {
                PrivateKey privateKey = this.generatePrivate(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrby)));
                return privateKey;
            }
            catch (Exception exception) {
                throw new InvalidKeySpecException(exception.toString());
            }
        }
        throw new InvalidKeySpecException("Unsupported key specification: " + (Object)keySpec.getClass() + ".");
    }

    public PublicKey engineGeneratePublic(KeySpec keySpec) {
        if (keySpec instanceof RainbowPublicKeySpec) {
            return new BCRainbowPublicKey((RainbowPublicKeySpec)keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            byte[] arrby = ((X509EncodedKeySpec)keySpec).getEncoded();
            try {
                PublicKey publicKey = this.generatePublic(SubjectPublicKeyInfo.getInstance(arrby));
                return publicKey;
            }
            catch (Exception exception) {
                throw new InvalidKeySpecException(exception.toString());
            }
        }
        throw new InvalidKeySpecException("Unknown key specification: " + (Object)keySpec + ".");
    }

    public final KeySpec engineGetKeySpec(Key key, Class class_) {
        if (key instanceof BCRainbowPrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
            if (RainbowPrivateKeySpec.class.isAssignableFrom(class_)) {
                BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)key;
                return new RainbowPrivateKeySpec(bCRainbowPrivateKey.getInvA1(), bCRainbowPrivateKey.getB1(), bCRainbowPrivateKey.getInvA2(), bCRainbowPrivateKey.getB2(), bCRainbowPrivateKey.getVi(), bCRainbowPrivateKey.getLayers());
            }
        } else if (key instanceof BCRainbowPublicKey) {
            if (X509EncodedKeySpec.class.isAssignableFrom(class_)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
            if (RainbowPublicKeySpec.class.isAssignableFrom(class_)) {
                BCRainbowPublicKey bCRainbowPublicKey = (BCRainbowPublicKey)key;
                return new RainbowPublicKeySpec(bCRainbowPublicKey.getDocLength(), bCRainbowPublicKey.getCoeffQuadratic(), bCRainbowPublicKey.getCoeffSingular(), bCRainbowPublicKey.getCoeffScalar());
            }
        } else {
            throw new InvalidKeySpecException("Unsupported key type: " + (Object)key.getClass() + ".");
        }
        throw new InvalidKeySpecException("Unknown key specification: " + (Object)class_ + ".");
    }

    public final Key engineTranslateKey(Key key) {
        if (key instanceof BCRainbowPrivateKey || key instanceof BCRainbowPublicKey) {
            return key;
        }
        throw new InvalidKeyException("Unsupported key type");
    }

    @Override
    public PrivateKey generatePrivate(PrivateKeyInfo privateKeyInfo) {
        RainbowPrivateKey rainbowPrivateKey = RainbowPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
        return new BCRainbowPrivateKey(rainbowPrivateKey.getInvA1(), rainbowPrivateKey.getB1(), rainbowPrivateKey.getInvA2(), rainbowPrivateKey.getB2(), rainbowPrivateKey.getVi(), rainbowPrivateKey.getLayers());
    }

    @Override
    public PublicKey generatePublic(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        RainbowPublicKey rainbowPublicKey = RainbowPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
        return new BCRainbowPublicKey(rainbowPublicKey.getDocLength(), rainbowPublicKey.getCoeffQuadratic(), rainbowPublicKey.getCoeffSingular(), rainbowPublicKey.getCoeffScalar());
    }
}

