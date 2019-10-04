/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.AlgorithmParameterGeneratorSpi
 *  java.security.AlgorithmParameters
 *  org.bouncycastle.jcajce.util.BCJcaJceHelper
 *  org.bouncycastle.jcajce.util.JcaJceHelper
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public abstract class BaseAlgorithmParameterGeneratorSpi
extends AlgorithmParameterGeneratorSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();

    protected final AlgorithmParameters createParametersInstance(String string) {
        return this.helper.createAlgorithmParameters(string);
    }
}

