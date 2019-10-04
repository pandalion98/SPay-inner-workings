/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.IESEngine;
import org.bouncycastle.jce.spec.IESParameterSpec;

public class IESUtil {
    public static IESParameterSpec guessParameterSpec(IESEngine iESEngine) {
        if (iESEngine.getCipher() == null) {
            return new IESParameterSpec(null, null, 128);
        }
        if (iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"DES") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"RC2") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"RC5-32") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"RC5-64")) {
            return new IESParameterSpec(null, null, 64, 64);
        }
        if (iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"SKIPJACK")) {
            return new IESParameterSpec(null, null, 80, 80);
        }
        if (iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals((Object)"GOST28147")) {
            return new IESParameterSpec(null, null, 256, 256);
        }
        return new IESParameterSpec(null, null, 128, 128);
    }
}

