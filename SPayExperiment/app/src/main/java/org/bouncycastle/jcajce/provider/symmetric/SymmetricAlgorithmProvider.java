/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.symmetric;

import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

abstract class SymmetricAlgorithmProvider
extends AlgorithmProvider {
    SymmetricAlgorithmProvider() {
    }

    protected void addGMacAlgorithm(ConfigurableProvider configurableProvider, String string, String string2, String string3) {
        configurableProvider.addAlgorithm("Mac." + string + "-GMAC", string2);
        configurableProvider.addAlgorithm("Alg.Alias.Mac." + string + "GMAC", string + "-GMAC");
        configurableProvider.addAlgorithm("KeyGenerator." + string + "-GMAC", string3);
        configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator." + string + "GMAC", string + "-GMAC");
    }

    protected void addPoly1305Algorithm(ConfigurableProvider configurableProvider, String string, String string2, String string3) {
        configurableProvider.addAlgorithm("Mac.POLY1305-" + string, string2);
        configurableProvider.addAlgorithm("Alg.Alias.Mac.POLY1305" + string, "POLY1305-" + string);
        configurableProvider.addAlgorithm("KeyGenerator.POLY1305-" + string, string3);
        configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.POLY1305" + string, "POLY1305-" + string);
    }
}

