package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public abstract class BaseAlgorithmParameterGenerator extends AlgorithmParameterGeneratorSpi {
    private final JcaJceHelper helper;
    protected SecureRandom random;
    protected int strength;

    public BaseAlgorithmParameterGenerator() {
        this.helper = new BCJcaJceHelper();
        this.strength = SkeinMac.SKEIN_1024;
    }

    protected final AlgorithmParameters createParametersInstance(String str) {
        return this.helper.createAlgorithmParameters(str);
    }

    protected void engineInit(int i, SecureRandom secureRandom) {
        this.strength = i;
        this.random = secureRandom;
    }
}
