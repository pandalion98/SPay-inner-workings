/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  javax.crypto.spec.DHParameterSpec
 */
package org.bouncycastle.jcajce.provider.config;

import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;

public interface ProviderConfiguration {
    public DHParameterSpec getDHDefaultParameters(int var1);

    public ECParameterSpec getEcImplicitlyCa();
}

