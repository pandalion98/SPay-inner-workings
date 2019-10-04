/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AlgorithmParametersSpi
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;

public abstract class BaseAlgorithmParameters
extends AlgorithmParametersSpi {
    protected AlgorithmParameterSpec engineGetParameterSpec(Class class_) {
        if (class_ == null) {
            throw new NullPointerException("argument to getParameterSpec must not be null");
        }
        return this.localEngineGetParameterSpec(class_);
    }

    protected boolean isASN1FormatString(String string) {
        return string == null || string.equals((Object)"ASN.1");
    }

    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(Class var1);
}

