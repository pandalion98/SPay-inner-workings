/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.NoSuchAlgorithmException
 *  java.security.NoSuchProviderException
 *  java.security.PrivateKey
 *  java.security.Provider
 *  java.security.SecureRandom
 *  java.security.Security
 *  java.security.Signature
 *  java.util.ArrayList
 *  java.util.Enumeration
 *  java.util.HashSet
 *  java.util.Hashtable
 *  java.util.Iterator
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.Strings;

class X509Util {
    private static Hashtable algorithms = new Hashtable();
    private static Set noParams;
    private static Hashtable params;

    static {
        params = new Hashtable();
        noParams = new HashSet();
        algorithms.put((Object)"MD2WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.md2WithRSAEncryption);
        algorithms.put((Object)"MD2WITHRSA", (Object)PKCSObjectIdentifiers.md2WithRSAEncryption);
        algorithms.put((Object)"MD5WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.md5WithRSAEncryption);
        algorithms.put((Object)"MD5WITHRSA", (Object)PKCSObjectIdentifiers.md5WithRSAEncryption);
        algorithms.put((Object)"SHA1WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.sha1WithRSAEncryption);
        algorithms.put((Object)"SHA1WITHRSA", (Object)PKCSObjectIdentifiers.sha1WithRSAEncryption);
        algorithms.put((Object)"SHA224WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.sha224WithRSAEncryption);
        algorithms.put((Object)"SHA224WITHRSA", (Object)PKCSObjectIdentifiers.sha224WithRSAEncryption);
        algorithms.put((Object)"SHA256WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.sha256WithRSAEncryption);
        algorithms.put((Object)"SHA256WITHRSA", (Object)PKCSObjectIdentifiers.sha256WithRSAEncryption);
        algorithms.put((Object)"SHA384WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.sha384WithRSAEncryption);
        algorithms.put((Object)"SHA384WITHRSA", (Object)PKCSObjectIdentifiers.sha384WithRSAEncryption);
        algorithms.put((Object)"SHA512WITHRSAENCRYPTION", (Object)PKCSObjectIdentifiers.sha512WithRSAEncryption);
        algorithms.put((Object)"SHA512WITHRSA", (Object)PKCSObjectIdentifiers.sha512WithRSAEncryption);
        algorithms.put((Object)"SHA1WITHRSAANDMGF1", (Object)PKCSObjectIdentifiers.id_RSASSA_PSS);
        algorithms.put((Object)"SHA224WITHRSAANDMGF1", (Object)PKCSObjectIdentifiers.id_RSASSA_PSS);
        algorithms.put((Object)"SHA256WITHRSAANDMGF1", (Object)PKCSObjectIdentifiers.id_RSASSA_PSS);
        algorithms.put((Object)"SHA384WITHRSAANDMGF1", (Object)PKCSObjectIdentifiers.id_RSASSA_PSS);
        algorithms.put((Object)"SHA512WITHRSAANDMGF1", (Object)PKCSObjectIdentifiers.id_RSASSA_PSS);
        algorithms.put((Object)"RIPEMD160WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        algorithms.put((Object)"RIPEMD160WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        algorithms.put((Object)"RIPEMD128WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        algorithms.put((Object)"RIPEMD128WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        algorithms.put((Object)"RIPEMD256WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        algorithms.put((Object)"RIPEMD256WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        algorithms.put((Object)"SHA1WITHDSA", (Object)X9ObjectIdentifiers.id_dsa_with_sha1);
        algorithms.put((Object)"DSAWITHSHA1", (Object)X9ObjectIdentifiers.id_dsa_with_sha1);
        algorithms.put((Object)"SHA224WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha224);
        algorithms.put((Object)"SHA256WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha256);
        algorithms.put((Object)"SHA384WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha384);
        algorithms.put((Object)"SHA512WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha512);
        algorithms.put((Object)"SHA1WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        algorithms.put((Object)"ECDSAWITHSHA1", (Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        algorithms.put((Object)"SHA224WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA224);
        algorithms.put((Object)"SHA256WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA256);
        algorithms.put((Object)"SHA384WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA384);
        algorithms.put((Object)"SHA512WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA512);
        algorithms.put((Object)"GOST3411WITHGOST3410", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        algorithms.put((Object)"GOST3411WITHGOST3410-94", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        algorithms.put((Object)"GOST3411WITHECGOST3410", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        algorithms.put((Object)"GOST3411WITHECGOST3410-2001", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        algorithms.put((Object)"GOST3411WITHGOST3410-2001", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA224);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA256);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA384);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA512);
        noParams.add((Object)X9ObjectIdentifiers.id_dsa_with_sha1);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha224);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha256);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha384);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha512);
        noParams.add((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        noParams.add((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        params.put((Object)"SHA1WITHRSAANDMGF1", (Object)X509Util.creatPSSParams(algorithmIdentifier, 20));
        AlgorithmIdentifier algorithmIdentifier2 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, DERNull.INSTANCE);
        params.put((Object)"SHA224WITHRSAANDMGF1", (Object)X509Util.creatPSSParams(algorithmIdentifier2, 28));
        AlgorithmIdentifier algorithmIdentifier3 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, DERNull.INSTANCE);
        params.put((Object)"SHA256WITHRSAANDMGF1", (Object)X509Util.creatPSSParams(algorithmIdentifier3, 32));
        AlgorithmIdentifier algorithmIdentifier4 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, DERNull.INSTANCE);
        params.put((Object)"SHA384WITHRSAANDMGF1", (Object)X509Util.creatPSSParams(algorithmIdentifier4, 48));
        AlgorithmIdentifier algorithmIdentifier5 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, DERNull.INSTANCE);
        params.put((Object)"SHA512WITHRSAANDMGF1", (Object)X509Util.creatPSSParams(algorithmIdentifier5, 64));
    }

    X509Util() {
    }

    /*
     * Enabled aggressive block sorting
     */
    static byte[] calculateSignature(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string, String string2, PrivateKey privateKey, SecureRandom secureRandom, ASN1Encodable aSN1Encodable) {
        if (aSN1ObjectIdentifier == null) {
            throw new IllegalStateException("no signature algorithm specified");
        }
        Signature signature = X509Util.getSignatureInstance(string, string2);
        if (secureRandom != null) {
            signature.initSign(privateKey, secureRandom);
        } else {
            signature.initSign(privateKey);
        }
        signature.update(aSN1Encodable.toASN1Primitive().getEncoded("DER"));
        return signature.sign();
    }

    /*
     * Enabled aggressive block sorting
     */
    static byte[] calculateSignature(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string, PrivateKey privateKey, SecureRandom secureRandom, ASN1Encodable aSN1Encodable) {
        if (aSN1ObjectIdentifier == null) {
            throw new IllegalStateException("no signature algorithm specified");
        }
        Signature signature = X509Util.getSignatureInstance(string);
        if (secureRandom != null) {
            signature.initSign(privateKey, secureRandom);
        } else {
            signature.initSign(privateKey);
        }
        signature.update(aSN1Encodable.toASN1Primitive().getEncoded("DER"));
        return signature.sign();
    }

    static X509Principal convertPrincipal(X500Principal x500Principal) {
        try {
            X509Principal x509Principal = new X509Principal(x500Principal.getEncoded());
            return x509Principal;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("cannot convert principal");
        }
    }

    private static RSASSAPSSparams creatPSSParams(AlgorithmIdentifier algorithmIdentifier, int n) {
        return new RSASSAPSSparams(algorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, algorithmIdentifier), new ASN1Integer(n), new ASN1Integer(1L));
    }

    static Iterator getAlgNames() {
        Enumeration enumeration = algorithms.keys();
        ArrayList arrayList = new ArrayList();
        while (enumeration.hasMoreElements()) {
            arrayList.add(enumeration.nextElement());
        }
        return arrayList.iterator();
    }

    static ASN1ObjectIdentifier getAlgorithmOID(String string) {
        String string2 = Strings.toUpperCase(string);
        if (algorithms.containsKey((Object)string2)) {
            return (ASN1ObjectIdentifier)algorithms.get((Object)string2);
        }
        return new ASN1ObjectIdentifier(string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Implementation getImplementation(String string, String string2) {
        Provider[] arrprovider = Security.getProviders();
        int n = 0;
        do {
            if (n == arrprovider.length) {
                throw new NoSuchAlgorithmException("cannot find implementation " + string2);
            }
            Implementation implementation = X509Util.getImplementation(string, Strings.toUpperCase(string2), arrprovider[n]);
            if (implementation != null) {
                return implementation;
            }
            try {
                X509Util.getImplementation(string, string2, arrprovider[n]);
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
            ++n;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Implementation getImplementation(String string, String string2, Provider provider) {
        String string3;
        String string4 = Strings.toUpperCase(string2);
        while ((string3 = provider.getProperty("Alg.Alias." + string + "." + string4)) != null) {
            string4 = string3;
        }
        String string5 = provider.getProperty(string + "." + string4);
        if (string5 == null) throw new NoSuchAlgorithmException("cannot find implementation " + string4 + " for provider " + provider.getName());
        try {
            Class class_;
            Class class_2;
            ClassLoader classLoader = provider.getClass().getClassLoader();
            if (classLoader != null) {
                class_ = classLoader.loadClass(string5);
                return new Implementation(class_.newInstance(), provider);
            }
            class_ = class_2 = Class.forName((String)string5);
            return new Implementation(class_.newInstance(), provider);
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalStateException("algorithm " + string4 + " in provider " + provider.getName() + " but no class \"" + string5 + "\" found!");
        }
        catch (Exception exception) {
            throw new IllegalStateException("algorithm " + string4 + " in provider " + provider.getName() + " but class \"" + string5 + "\" inaccessible!");
        }
    }

    static Provider getProvider(String string) {
        Provider provider = Security.getProvider((String)string);
        if (provider == null) {
            throw new NoSuchProviderException("Provider " + string + " not found");
        }
        return provider;
    }

    static AlgorithmIdentifier getSigAlgID(ASN1ObjectIdentifier aSN1ObjectIdentifier, String string) {
        if (noParams.contains((Object)aSN1ObjectIdentifier)) {
            return new AlgorithmIdentifier(aSN1ObjectIdentifier);
        }
        String string2 = Strings.toUpperCase(string);
        if (params.containsKey((Object)string2)) {
            return new AlgorithmIdentifier(aSN1ObjectIdentifier, (ASN1Encodable)params.get((Object)string2));
        }
        return new AlgorithmIdentifier(aSN1ObjectIdentifier, DERNull.INSTANCE);
    }

    static Signature getSignatureInstance(String string) {
        return Signature.getInstance((String)string);
    }

    static Signature getSignatureInstance(String string, String string2) {
        if (string2 != null) {
            return Signature.getInstance((String)string, (String)string2);
        }
        return Signature.getInstance((String)string);
    }

    static class Implementation {
        Object engine;
        Provider provider;

        Implementation(Object object, Provider provider) {
            this.engine = object;
            this.provider = provider;
        }

        Object getEngine() {
            return this.engine;
        }

        Provider getProvider() {
            return this.provider;
        }
    }

}

