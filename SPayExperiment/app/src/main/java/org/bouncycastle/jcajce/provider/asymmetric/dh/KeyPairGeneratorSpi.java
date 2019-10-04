/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.util.Hashtable
 *  javax.crypto.spec.DHParameterSpec
 *  org.bouncycastle.jcajce.provider.config.ProviderConfiguration
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.dh.BCDHPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.dh.BCDHPublicKey;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Integers;

public class KeyPairGeneratorSpi
extends KeyPairGenerator {
    private static Object lock;
    private static Hashtable params;
    int certainty = 20;
    DHBasicKeyPairGenerator engine = new DHBasicKeyPairGenerator();
    boolean initialised = false;
    DHKeyGenerationParameters param;
    SecureRandom random = new SecureRandom();
    int strength = 1024;

    static {
        params = new Hashtable();
        lock = new Object();
    }

    public KeyPairGeneratorSpi() {
        super("DH");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            Integer n2 = Integers.valueOf((int)this.strength);
            if (params.containsKey((Object)n2)) {
                this.param = (DHKeyGenerationParameters)params.get((Object)n2);
            } else {
                DHParameterSpec dHParameterSpec = BouncyCastleProvider.CONFIGURATION.getDHDefaultParameters(this.strength);
                if (dHParameterSpec != null) {
                    this.param = new DHKeyGenerationParameters(this.random, new DHParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), null, dHParameterSpec.getL()));
                } else {
                    Object object;
                    Object object2 = object = lock;
                    synchronized (object2) {
                        if (params.containsKey((Object)n2)) {
                            this.param = (DHKeyGenerationParameters)params.get((Object)n2);
                        } else {
                            DHParametersGenerator dHParametersGenerator = new DHParametersGenerator();
                            dHParametersGenerator.init(this.strength, this.certainty, this.random);
                            this.param = new DHKeyGenerationParameters(this.random, dHParametersGenerator.generateParameters());
                            params.put((Object)n2, (Object)this.param);
                        }
                    }
                }
            }
            this.engine.init(this.param);
            this.initialised = true;
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.engine.generateKeyPair();
        DHPublicKeyParameters dHPublicKeyParameters = (DHPublicKeyParameters)asymmetricCipherKeyPair.getPublic();
        DHPrivateKeyParameters dHPrivateKeyParameters = (DHPrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
        return new KeyPair((PublicKey)new BCDHPublicKey(dHPublicKeyParameters), (PrivateKey)new BCDHPrivateKey(dHPrivateKeyParameters));
    }

    public void initialize(int n2, SecureRandom secureRandom) {
        this.strength = n2;
        this.random = secureRandom;
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec");
        }
        DHParameterSpec dHParameterSpec = (DHParameterSpec)algorithmParameterSpec;
        this.param = new DHKeyGenerationParameters(secureRandom, new DHParameters(dHParameterSpec.getP(), dHParameterSpec.getG(), null, dHParameterSpec.getL()));
        this.engine.init(this.param);
        this.initialised = true;
    }
}

