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
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.spec.DHGenParameterSpec
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.elgamal;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAlgorithmParameterGeneratorSpi;

public class AlgorithmParameterGeneratorSpi
extends BaseAlgorithmParameterGeneratorSpi {
    private int l = 0;
    protected SecureRandom random;
    protected int strength = 1024;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected AlgorithmParameters engineGenerateParameters() {
        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        if (this.random != null) {
            elGamalParametersGenerator.init(this.strength, 20, this.random);
        } else {
            elGamalParametersGenerator.init(this.strength, 20, new SecureRandom());
        }
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();
        try {
            AlgorithmParameters algorithmParameters = this.createParametersInstance("ElGamal");
            algorithmParameters.init((AlgorithmParameterSpec)new DHParameterSpec(elGamalParameters.getP(), elGamalParameters.getG(), this.l));
            return algorithmParameters;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    protected void engineInit(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
        }
        DHGenParameterSpec dHGenParameterSpec = (DHGenParameterSpec)algorithmParameterSpec;
        this.strength = dHGenParameterSpec.getPrimeSize();
        this.l = dHGenParameterSpec.getExponentSize();
        this.random = secureRandom;
    }
}

