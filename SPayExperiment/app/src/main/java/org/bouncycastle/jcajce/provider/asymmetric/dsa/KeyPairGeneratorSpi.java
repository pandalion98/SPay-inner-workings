/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.DSAParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.BCDSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.BCDSAPublicKey;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    int certainty = 20;
    DSAKeyPairGenerator engine = new DSAKeyPairGenerator();
    boolean initialised = false;
    DSAKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public KeyPairGeneratorSpi() {
        super("DSA");
    }

    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            DSAParametersGenerator dSAParametersGenerator = new DSAParametersGenerator();
            dSAParametersGenerator.init(this.strength, this.certainty, this.random);
            this.param = new DSAKeyGenerationParameters(this.random, dSAParametersGenerator.generateParameters());
            this.engine.init(this.param);
            this.initialised = true;
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        DSAPublicKeyParameters dSAPublicKeyParameters = (DSAPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        DSAPrivateKeyParameters dSAPrivateKeyParameters = (DSAPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCDSAPublicKey(dSAPublicKeyParameters), (PrivateKey)new BCDSAPrivateKey(dSAPrivateKeyParameters));
    }

    public void initialize(int n2, SecureRandom secureRandom) {
        if (n2 < 512 || n2 > 4096 || n2 < 1024 && n2 % 64 != 0 || n2 >= 1024 && n2 % 1024 != 0) {
            throw new InvalidParameterException("strength must be from 512 - 4096 and a multiple of 1024 above 1024");
        }
        this.strength = n2;
        this.random = secureRandom;
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
        }
        DSAParameterSpec dSAParameterSpec = (DSAParameterSpec)algorithmParameterSpec;
        this.param = new DSAKeyGenerationParameters(secureRandom, new DSAParameters(dSAParameterSpec.getP(), dSAParameterSpec.getQ(), dSAParameterSpec.getG()));
        this.engine.init(this.param);
        this.initialised = true;
    }
}

