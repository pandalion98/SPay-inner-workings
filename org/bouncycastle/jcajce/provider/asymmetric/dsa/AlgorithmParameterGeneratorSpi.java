package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAlgorithmParameterGeneratorSpi;

public class AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGeneratorSpi {
    protected DSAParameterGenerationParameters params;
    protected SecureRandom random;
    protected int strength;

    public AlgorithmParameterGeneratorSpi() {
        this.strength = SkeinMac.SKEIN_1024;
    }

    protected AlgorithmParameters engineGenerateParameters() {
        DSAParametersGenerator dSAParametersGenerator = this.strength <= SkeinMac.SKEIN_1024 ? new DSAParametersGenerator() : new DSAParametersGenerator(new SHA256Digest());
        if (this.random == null) {
            this.random = new SecureRandom();
        }
        if (this.strength == SkeinMac.SKEIN_1024) {
            this.params = new DSAParameterGenerationParameters(SkeinMac.SKEIN_1024, CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, 80, this.random);
            dSAParametersGenerator.init(this.params);
        } else if (this.strength > SkeinMac.SKEIN_1024) {
            this.params = new DSAParameterGenerationParameters(this.strength, SkeinMac.SKEIN_256, 80, this.random);
            dSAParametersGenerator.init(this.params);
        } else {
            dSAParametersGenerator.init(this.strength, 20, this.random);
        }
        DSAParameters generateParameters = dSAParametersGenerator.generateParameters();
        try {
            AlgorithmParameters createParametersInstance = createParametersInstance("DSA");
            createParametersInstance.init(new DSAParameterSpec(generateParameters.getP(), generateParameters.getQ(), generateParameters.getG()));
            return createParametersInstance;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected void engineInit(int i, SecureRandom secureRandom) {
        if (i < SkeinMac.SKEIN_512 || i > 3072) {
            throw new InvalidParameterException("strength must be from 512 - 3072");
        } else if (i <= SkeinMac.SKEIN_1024 && i % 64 != 0) {
            throw new InvalidParameterException("strength must be a multiple of 64 below 1024 bits.");
        } else if (i <= SkeinMac.SKEIN_1024 || i % SkeinMac.SKEIN_1024 == 0) {
            this.strength = i;
            this.random = secureRandom;
        } else {
            throw new InvalidParameterException("strength must be a multiple of 1024 above 1024 bits.");
        }
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
    }
}
