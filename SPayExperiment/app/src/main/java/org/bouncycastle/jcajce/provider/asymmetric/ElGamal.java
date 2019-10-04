/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyFactorySpi
 *  org.bouncycastle.jcajce.provider.config.ConfigurableProvider
 */
package org.bouncycastle.jcajce.provider.asymmetric;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class ElGamal {
    private static final String PREFIX = "org.bouncycastle.jcajce.provider.asymmetric.elgamal.";

    public static class Mappings
    extends AsymmetricAlgorithmProvider {
        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.ELGAMAL", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.AlgorithmParameterGeneratorSpi");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.ElGamal", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.AlgorithmParameterGeneratorSpi");
            configurableProvider.addAlgorithm("AlgorithmParameters.ELGAMAL", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.AlgorithmParametersSpi");
            configurableProvider.addAlgorithm("AlgorithmParameters.ElGamal", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.AlgorithmParametersSpi");
            configurableProvider.addAlgorithm("Cipher.ELGAMAL", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Cipher.ElGamal", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.ELGAMAL/ECB/PKCS1PADDING", "ELGAMAL/PKCS1");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.ELGAMAL/NONE/PKCS1PADDING", "ELGAMAL/PKCS1");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.ELGAMAL/NONE/NOPADDING", "ELGAMAL");
            configurableProvider.addAlgorithm("Cipher.ELGAMAL/PKCS1", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("KeyFactory.ELGAMAL", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyFactorySpi");
            configurableProvider.addAlgorithm("KeyFactory.ElGamal", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyFactorySpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.ELGAMAL", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyPairGeneratorSpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.ElGamal", "org.bouncycastle.jcajce.provider.asymmetric.elgamal.KeyPairGeneratorSpi");
            KeyFactorySpi keyFactorySpi = new KeyFactorySpi();
            this.registerOid(configurableProvider, OIWObjectIdentifiers.elGamalAlgorithm, "ELGAMAL", (AsymmetricKeyInfoConverter)keyFactorySpi);
            this.registerOidAlgorithmParameters(configurableProvider, OIWObjectIdentifiers.elGamalAlgorithm, "ELGAMAL");
        }
    }

}

