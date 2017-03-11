package org.bouncycastle.jcajce.provider.asymmetric.util;

import org.bouncycastle.crypto.engines.IESEngine;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.spec.IESParameterSpec;

public class IESUtil {
    public static IESParameterSpec guessParameterSpec(IESEngine iESEngine) {
        return iESEngine.getCipher() == null ? new IESParameterSpec(null, null, X509KeyUsage.digitalSignature) : (iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("DES") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("RC2") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("RC5-32") || iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("RC5-64")) ? new IESParameterSpec(null, null, 64, 64) : iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("SKIPJACK") ? new IESParameterSpec(null, null, 80, 80) : iESEngine.getCipher().getUnderlyingCipher().getAlgorithmName().equals("GOST28147") ? new IESParameterSpec(null, null, SkeinMac.SKEIN_256, SkeinMac.SKEIN_256) : new IESParameterSpec(null, null, X509KeyUsage.digitalSignature, X509KeyUsage.digitalSignature);
    }
}
