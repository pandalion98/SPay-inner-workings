/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi
 *  org.bouncycastle.jcajce.provider.config.ConfigurableProvider
 */
package org.bouncycastle.jcajce.provider.asymmetric;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class RSA {
    private static final String PREFIX = "org.bouncycastle.jcajce.provider.asymmetric.rsa.";

    public static class Mappings
    extends AsymmetricAlgorithmProvider {
        private void addDigestSignature(ConfigurableProvider configurableProvider, String string, String string2, ASN1ObjectIdentifier aSN1ObjectIdentifier) {
            String string3 = string + "WITHRSA";
            String string4 = string + "withRSA";
            String string5 = string + "WithRSA";
            String string6 = string + "/" + "RSA";
            String string7 = string + "WITHRSAENCRYPTION";
            String string8 = string + "withRSAEncryption";
            String string9 = string + "WithRSAEncryption";
            configurableProvider.addAlgorithm("Signature." + string3, string2);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string4, string3);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string5, string3);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string7, string3);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string8, string3);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string9, string3);
            configurableProvider.addAlgorithm("Alg.Alias.Signature." + string6, string3);
            if (aSN1ObjectIdentifier != null) {
                configurableProvider.addAlgorithm("Alg.Alias.Signature." + aSN1ObjectIdentifier, string3);
                configurableProvider.addAlgorithm("Alg.Alias.Signature.OID." + aSN1ObjectIdentifier, string3);
            }
        }

        @Override
        public void configure(ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("AlgorithmParameters.OAEP", "org.bouncycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi$OAEP");
            configurableProvider.addAlgorithm("AlgorithmParameters.PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi$PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RSASSA-PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA224withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA256withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA384withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA512withRSA/PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA224WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA256WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA384WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA512WITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.RAWRSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSAPSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSASSA-PSS", "PSS");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.NONEWITHRSAANDMGF1", "PSS");
            configurableProvider.addAlgorithm("Cipher.RSA", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/RAW", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$NoPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/PKCS1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher.1.2.840.113549.1.1.1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher.2.5.8.1.1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding");
            configurableProvider.addAlgorithm("Cipher.RSA/1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding_PrivateOnly");
            configurableProvider.addAlgorithm("Cipher.RSA/2", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$PKCS1v1_5Padding_PublicOnly");
            configurableProvider.addAlgorithm("Cipher.RSA/OAEP", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$OAEPPadding");
            configurableProvider.addAlgorithm("Cipher." + PKCSObjectIdentifiers.id_RSAES_OAEP, "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$OAEPPadding");
            configurableProvider.addAlgorithm("Cipher.RSA/ISO9796-1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi$ISO9796d1Padding");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//RAW", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//NOPADDING", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//PKCS1PADDING", "RSA/PKCS1");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//OAEPPADDING", "RSA/OAEP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RSA//ISO9796-1PADDING", "RSA/ISO9796-1");
            configurableProvider.addAlgorithm("KeyFactory.RSA", "org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.RSA", "org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyPairGeneratorSpi");
            KeyFactorySpi keyFactorySpi = new KeyFactorySpi();
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.rsaEncryption, "RSA", (AsymmetricKeyInfoConverter)keyFactorySpi);
            this.registerOid(configurableProvider, X509ObjectIdentifiers.id_ea_rsa, "RSA", (AsymmetricKeyInfoConverter)keyFactorySpi);
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.id_RSAES_OAEP, "RSA", (AsymmetricKeyInfoConverter)keyFactorySpi);
            this.registerOid(configurableProvider, PKCSObjectIdentifiers.id_RSASSA_PSS, "RSA", (AsymmetricKeyInfoConverter)keyFactorySpi);
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.rsaEncryption, "RSA");
            this.registerOidAlgorithmParameters(configurableProvider, X509ObjectIdentifiers.id_ea_rsa, "RSA");
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.id_RSAES_OAEP, "OAEP");
            this.registerOidAlgorithmParameters(configurableProvider, PKCSObjectIdentifiers.id_RSASSA_PSS, "PSS");
            configurableProvider.addAlgorithm("Signature.RSASSA-PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            configurableProvider.addAlgorithm("Signature." + PKCSObjectIdentifiers.id_RSASSA_PSS, "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            configurableProvider.addAlgorithm("Signature.OID." + PKCSObjectIdentifiers.id_RSASSA_PSS, "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$PSSwithRSA");
            configurableProvider.addAlgorithm("Signature.SHA224WITHRSAANDMGF1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA224withRSA");
            configurableProvider.addAlgorithm("Signature.SHA256WITHRSAANDMGF1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA256withRSA");
            configurableProvider.addAlgorithm("Signature.SHA384WITHRSAANDMGF1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA384withRSA");
            configurableProvider.addAlgorithm("Signature.SHA512WITHRSAANDMGF1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512withRSA");
            configurableProvider.addAlgorithm("Signature.SHA224withRSA/PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA224withRSA");
            configurableProvider.addAlgorithm("Signature.SHA256withRSA/PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA256withRSA");
            configurableProvider.addAlgorithm("Signature.SHA384withRSA/PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA384withRSA");
            configurableProvider.addAlgorithm("Signature.SHA512withRSA/PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512withRSA");
            configurableProvider.addAlgorithm("Signature.RSA", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$noneRSA");
            configurableProvider.addAlgorithm("Signature.RAWRSASSA-PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$nonePSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RAWRSA", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSA", "RSA");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RAWRSAPSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSAPSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSASSA-PSS", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.NONEWITHRSAANDMGF1", "RAWRSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.RSAPSS", "RSASSA-PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA224withRSAandMGF1", "SHA224withRSA/PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA256withRSAandMGF1", "SHA256withRSA/PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA384withRSAandMGF1", "SHA384withRSA/PSS");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA512withRSAandMGF1", "SHA512withRSA/PSS");
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD2")) {
                this.addDigestSignature(configurableProvider, "MD2", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD2", PKCSObjectIdentifiers.md2WithRSAEncryption);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD4")) {
                this.addDigestSignature(configurableProvider, "MD4", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD4", PKCSObjectIdentifiers.md4WithRSAEncryption);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "MD5")) {
                this.addDigestSignature(configurableProvider, "MD5", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$MD5", PKCSObjectIdentifiers.md5WithRSAEncryption);
                configurableProvider.addAlgorithm("Signature.MD5withRSA/ISO9796-2", "org.bouncycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$MD5WithRSAEncryption");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.MD5WithRSA/ISO9796-2", "MD5withRSA/ISO9796-2");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "SHA1")) {
                configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA1withRSA/PSS", "PSS");
                configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.SHA1WITHRSAANDMGF1", "PSS");
                configurableProvider.addAlgorithm("Signature.SHA1withRSA/PSS", "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA1withRSA");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1withRSAandMGF1", "SHA1withRSA/PSS");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WITHRSAANDMGF1", "SHA1withRSA/PSS");
                this.addDigestSignature(configurableProvider, "SHA1", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA1", PKCSObjectIdentifiers.sha1WithRSAEncryption);
                configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WithRSA/ISO9796-2", "SHA1withRSA/ISO9796-2");
                configurableProvider.addAlgorithm("Signature.SHA1withRSA/ISO9796-2", "org.bouncycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$SHA1WithRSAEncryption");
                configurableProvider.addAlgorithm("Alg.Alias.Signature." + OIWObjectIdentifiers.sha1WithRSA, "SHA1WITHRSA");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.OID." + OIWObjectIdentifiers.sha1WithRSA, "SHA1WITHRSA");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1withRSA/X9.31", "SHA1WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WithRSA/X9.31", "SHA1WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Signature.SHA1WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA1WithRSAEncryption");
            }
            this.addDigestSignature(configurableProvider, "SHA224", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA224", PKCSObjectIdentifiers.sha224WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA256", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA256", PKCSObjectIdentifiers.sha256WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA384", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA384", PKCSObjectIdentifiers.sha384WithRSAEncryption);
            this.addDigestSignature(configurableProvider, "SHA512", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$SHA512", PKCSObjectIdentifiers.sha512WithRSAEncryption);
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA224withRSA/X9.31", "SHA224WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA224WithRSA/X9.31", "SHA224WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Signature.SHA224WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA224WithRSAEncryption");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA256withRSA/X9.31", "SHA256WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA256WithRSA/X9.31", "SHA256WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Signature.SHA256WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA256WithRSAEncryption");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA384withRSA/X9.31", "SHA384WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA384WithRSA/X9.31", "SHA384WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Signature.SHA384WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA384WithRSAEncryption");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA512withRSA/X9.31", "SHA512WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.SHA512WithRSA/X9.31", "SHA512WITHRSA/X9.31");
            configurableProvider.addAlgorithm("Signature.SHA512WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$SHA512WithRSAEncryption");
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD128")) {
                this.addDigestSignature(configurableProvider, "RIPEMD128", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD128", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
                this.addDigestSignature(configurableProvider, "RMD128", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD128", null);
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD128withRSA/X9.31", "RIPEMD128WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD128WithRSA/X9.31", "RIPEMD128WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Signature.RIPEMD128WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD128WithRSAEncryption");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD160")) {
                this.addDigestSignature(configurableProvider, "RIPEMD160", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD160", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
                this.addDigestSignature(configurableProvider, "RMD160", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD160", null);
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD160WithRSA/ISO9796-2", "RIPEMD160withRSA/ISO9796-2");
                configurableProvider.addAlgorithm("Signature.RIPEMD160withRSA/ISO9796-2", "org.bouncycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi$RIPEMD160WithRSAEncryption");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD160withRSA/X9.31", "RIPEMD160WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.RIPEMD160WithRSA/X9.31", "RIPEMD160WITHRSA/X9.31");
                configurableProvider.addAlgorithm("Signature.RIPEMD160WITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$RIPEMD160WithRSAEncryption");
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "RIPEMD256")) {
                this.addDigestSignature(configurableProvider, "RIPEMD256", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD256", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
                this.addDigestSignature(configurableProvider, "RMD256", "org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi$RIPEMD256", null);
            }
            if (configurableProvider.hasAlgorithm("MessageDigest", "WHIRLPOOL")) {
                configurableProvider.addAlgorithm("Alg.Alias.Signature.WhirlpoolWithRSA/X9.31", "WHIRLPOOLWITHRSA/X9.31");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.WHIRLPOOLwithRSA/X9.31", "WHIRLPOOLWITHRSA/X9.31");
                configurableProvider.addAlgorithm("Alg.Alias.Signature.WHIRLPOOLWithRSA/X9.31", "WHIRLPOOLWITHRSA/X9.31");
                configurableProvider.addAlgorithm("Signature.WHIRLPOOLWITHRSA/X9.31", "org.bouncycastle.jcajce.provider.asymmetric.rsa.X931SignatureSpi$WhirlpoolWithRSAEncryption");
            }
        }
    }

}

