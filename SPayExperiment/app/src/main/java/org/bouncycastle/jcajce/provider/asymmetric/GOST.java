/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.jcajce.provider.asymmetric.gost.KeyFactorySpi
 *  org.bouncycastle.jcajce.provider.config.ConfigurableProvider
 */
package org.bouncycastle.jcajce.provider.asymmetric;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.gost.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class GOST {
    private static final String PREFIX = "org.bouncycastle.jcajce.provider.asymmetric.gost.";

    public static class Mappings
    extends AsymmetricAlgorithmProvider {
        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("KeyPairGenerator.GOST3410", "org.bouncycastle.jcajce.provider.asymmetric.gost.KeyPairGeneratorSpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.GOST-3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.GOST-3410-94", "GOST3410");
            configurableProvider.addAlgorithm("KeyFactory.GOST3410", "org.bouncycastle.jcajce.provider.asymmetric.gost.KeyFactorySpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.GOST-3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.GOST-3410-94", "GOST3410");
            configurableProvider.addAlgorithm("AlgorithmParameters.GOST3410", "org.bouncycastle.jcajce.provider.asymmetric.gost.AlgorithmParametersSpi");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.GOST3410", "org.bouncycastle.jcajce.provider.asymmetric.gost.AlgorithmParameterGeneratorSpi");
            this.registerOid(configurableProvider, CryptoProObjectIdentifiers.gostR3410_94, "GOST3410", (AsymmetricKeyInfoConverter)new KeyFactorySpi());
            this.registerOidAlgorithmParameters(configurableProvider, CryptoProObjectIdentifiers.gostR3410_94, "GOST3410");
            configurableProvider.addAlgorithm("Signature.GOST3410", "org.bouncycastle.jcajce.provider.asymmetric.gost.SignatureSpi");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST-3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST-3410-94", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST3411withGOST3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST3411WITHGOST3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST3411WithGOST3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.GOST-3410", "GOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.GOST-3410", "GOST3410");
        }
    }

}

