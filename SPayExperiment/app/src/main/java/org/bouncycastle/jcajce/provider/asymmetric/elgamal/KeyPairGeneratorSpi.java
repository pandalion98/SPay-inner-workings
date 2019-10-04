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
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.BCElGamalPublicKey;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    int certainty = 20;
    ElGamalKeyPairGenerator engine = new ElGamalKeyPairGenerator();
    boolean initialised = false;
    ElGamalKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    public KeyPairGeneratorSpi() {
        super("ElGamal");
    }

    /*
     * Enabled aggressive block sorting
     */
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            DHParameterSpec dHParameterSpec = BouncyCastleProvider.CONFIGURATION.getDHDefaultParameters(this.strength);
            if (dHParameterSpec != null) {
                this.param = new ElGamalKeyGenerationParameters(this.random, new ElGamalParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), dHParameterSpec.getL()));
            } else {
                ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
                elGamalParametersGenerator.init(this.strength, this.certainty, this.random);
                this.param = new ElGamalKeyGenerationParameters(this.random, elGamalParametersGenerator.generateParameters());
            }
            this.engine.init(this.param);
            this.initialised = true;
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        ElGamalPrivateKeyParameters elGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCElGamalPublicKey(elGamalPublicKeyParameters), (PrivateKey)new BCElGamalPrivateKey(elGamalPrivateKeyParameters));
    }

    public void initialize(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof ElGamalParameterSpec) && !(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec or an ElGamalParameterSpec");
        }
        if (algorithmParameterSpec instanceof ElGamalParameterSpec) {
            ElGamalParameterSpec elGamalParameterSpec = (ElGamalParameterSpec)algorithmParameterSpec;
            this.param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(elGamalParameterSpec.getP(), elGamalParameterSpec.getG()));
        } else {
            DHParameterSpec dHParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            this.param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), dHParameterSpec.getL()));
        }
        this.engine.init(this.param);
        this.initialised = true;
    }
}

