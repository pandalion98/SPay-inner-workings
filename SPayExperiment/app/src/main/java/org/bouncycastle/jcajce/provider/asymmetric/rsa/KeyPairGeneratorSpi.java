/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.RSAKeyGenParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.rsa;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    static final BigInteger defaultPublicExponent = BigInteger.valueOf((long)65537L);
    static final int defaultTests = 112;
    RSAKeyPairGenerator engine;
    RSAKeyGenerationParameters param;

    public KeyPairGeneratorSpi() {
        super("RSA");
        this.engine = new RSAKeyPairGenerator();
        this.param = new RSAKeyGenerationParameters(defaultPublicExponent, new SecureRandom(), 2048, 112);
        this.engine.init(this.param);
    }

    public KeyPairGeneratorSpi(String string) {
        super(string);
    }

    public KeyPair generateKeyPair() {
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        RSAKeyParameters rSAKeyParameters = (RSAKeyParameters)asymmetricCipherKeyPair.getPublic();
        RSAPrivateCrtKeyParameters rSAPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCRSAPublicKey(rSAKeyParameters), (PrivateKey)new BCRSAPrivateCrtKey(rSAPrivateCrtKeyParameters));
    }

    public void initialize(int n, SecureRandom secureRandom) {
        this.param = new RSAKeyGenerationParameters(defaultPublicExponent, secureRandom, n, 112);
        this.engine.init(this.param);
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
        }
        RSAKeyGenParameterSpec rSAKeyGenParameterSpec = (RSAKeyGenParameterSpec)algorithmParameterSpec;
        this.param = new RSAKeyGenerationParameters(rSAKeyGenParameterSpec.getPublicExponent(), secureRandom, rSAKeyGenParameterSpec.getKeysize(), 112);
        this.engine.init(this.param);
    }
}

