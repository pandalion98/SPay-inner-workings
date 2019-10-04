/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyPairGenerator;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey;
import org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPublicKey;
import org.bouncycastle.pqc.jcajce.spec.RainbowParameterSpec;

public class RainbowKeyPairGeneratorSpi
extends KeyPairGenerator {
    RainbowKeyPairGenerator engine = new RainbowKeyPairGenerator();
    boolean initialised = false;
    RainbowKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public RainbowKeyPairGeneratorSpi() {
        super("Rainbow");
    }

    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.param = new RainbowKeyGenerationParameters(this.random, new RainbowParameters(new RainbowParameterSpec().getVi()));
            this.engine.init(this.param);
            this.initialised = true;
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        RainbowPublicKeyParameters rainbowPublicKeyParameters = (RainbowPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        RainbowPrivateKeyParameters rainbowPrivateKeyParameters = (RainbowPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCRainbowPublicKey(rainbowPublicKeyParameters), (PrivateKey)new BCRainbowPrivateKey(rainbowPrivateKeyParameters));
    }

    public void initialize(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof RainbowParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a RainbowParameterSpec");
        }
        this.param = new RainbowKeyGenerationParameters(secureRandom, new RainbowParameters(((RainbowParameterSpec)algorithmParameterSpec).getVi()));
        this.engine.init(this.param);
        this.initialised = true;
    }
}

