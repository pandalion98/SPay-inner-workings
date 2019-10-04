/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.AlgorithmParameterGeneratorSpi
 *  java.security.AlgorithmParameters
 *  java.security.SecureRandom
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public abstract class BaseAlgorithmParameterGenerator
extends AlgorithmParameterGeneratorSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();
    protected SecureRandom random;
    protected int strength = 1024;

    protected final AlgorithmParameters createParametersInstance(String string) {
        return this.helper.createAlgorithmParameters(string);
    }

    protected void engineInit(int n, SecureRandom secureRandom) {
        this.strength = n;
        this.random = secureRandom;
    }
}

