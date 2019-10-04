/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.DSAParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.dsa;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.params.DSAParameterGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAlgorithmParameterGeneratorSpi;

public class AlgorithmParameterGeneratorSpi
extends BaseAlgorithmParameterGeneratorSpi {
    protected DSAParameterGenerationParameters params;
    protected SecureRandom random;
    protected int strength = 1024;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected AlgorithmParameters engineGenerateParameters() {
        DSAParametersGenerator dSAParametersGenerator = this.strength <= 1024 ? new DSAParametersGenerator() : new DSAParametersGenerator(new SHA256Digest());
        if (this.random == null) {
            this.random = new SecureRandom();
        }
        if (this.strength == 1024) {
            this.params = new DSAParameterGenerationParameters(1024, 160, 80, this.random);
            dSAParametersGenerator.init(this.params);
        } else if (this.strength > 1024) {
            this.params = new DSAParameterGenerationParameters(this.strength, 256, 80, this.random);
            dSAParametersGenerator.init(this.params);
        } else {
            dSAParametersGenerator.init(this.strength, 20, this.random);
        }
        DSAParameters dSAParameters = dSAParametersGenerator.generateParameters();
        try {
            AlgorithmParameters algorithmParameters = this.createParametersInstance("DSA");
            algorithmParameters.init((AlgorithmParameterSpec)new DSAParameterSpec(dSAParameters.getP(), dSAParameters.getQ(), dSAParameters.getG()));
            return algorithmParameters;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    protected void engineInit(int n2, SecureRandom secureRandom) {
        if (n2 < 512 || n2 > 3072) {
            throw new InvalidParameterException("strength must be from 512 - 3072");
        }
        if (n2 <= 1024 && n2 % 64 != 0) {
            throw new InvalidParameterException("strength must be a multiple of 64 below 1024 bits.");
        }
        if (n2 > 1024 && n2 % 1024 != 0) {
            throw new InvalidParameterException("strength must be a multiple of 1024 above 1024 bits.");
        }
        this.strength = n2;
        this.random = secureRandom;
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
    }
}

