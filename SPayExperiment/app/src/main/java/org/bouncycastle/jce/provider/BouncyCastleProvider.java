/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.InternalError
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AccessController
 *  java.security.PrivateKey
 *  java.security.PrivilegedAction
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.util.HashMap
 *  java.util.Map
 */
package org.bouncycastle.jce.provider;

import java.security.AccessController;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.jce.provider.BouncyCastleProviderConfiguration;

public final class BouncyCastleProvider
extends Provider
implements ConfigurableProvider {
    private static final String[] ASYMMETRIC_CIPHERS;
    private static final String[] ASYMMETRIC_GENERIC;
    private static final String ASYMMETRIC_PACKAGE = "org.bouncycastle.jcajce.provider.asymmetric.";
    public static final ProviderConfiguration CONFIGURATION;
    private static final String[] DIGESTS;
    private static final String DIGEST_PACKAGE = "org.bouncycastle.jcajce.provider.digest.";
    private static final String[] KEYSTORES;
    private static final String KEYSTORE_PACKAGE = "org.bouncycastle.jcajce.provider.keystore.";
    public static final String PROVIDER_NAME = "BC";
    private static final String[] SYMMETRIC_CIPHERS;
    private static final String[] SYMMETRIC_GENERIC;
    private static final String[] SYMMETRIC_MACS;
    private static final String SYMMETRIC_PACKAGE = "org.bouncycastle.jcajce.provider.symmetric.";
    private static String info;
    private static final Map keyInfoConverters;

    static {
        info = "BouncyCastle Security Provider v1.52";
        CONFIGURATION = new BouncyCastleProviderConfiguration();
        keyInfoConverters = new HashMap();
        SYMMETRIC_GENERIC = new String[]{"PBEPBKDF2", "PBEPKCS12"};
        SYMMETRIC_MACS = new String[]{"SipHash"};
        SYMMETRIC_CIPHERS = new String[]{"AES", "ARC4", "Blowfish", "Camellia", "CAST5", "CAST6", "ChaCha", "DES", "DESede", "GOST28147", "Grainv1", "Grain128", "HC128", "HC256", "IDEA", "Noekeon", "RC2", "RC5", "RC6", "Rijndael", "Salsa20", "SEED", "Serpent", "Shacal2", "Skipjack", "TEA", "Twofish", "Threefish", "VMPC", "VMPCKSA3", "XTEA", "XSalsa20"};
        ASYMMETRIC_GENERIC = new String[]{"X509", "IES"};
        ASYMMETRIC_CIPHERS = new String[]{"DSA", "DH", "EC", "RSA", "GOST", "ECGOST", "ElGamal", "DSTU4145"};
        DIGESTS = new String[]{"GOST3411", "MD2", "MD4", "MD5", "SHA1", "RIPEMD128", "RIPEMD160", "RIPEMD256", "RIPEMD320", "SHA224", "SHA256", "SHA384", "SHA512", "SHA3", "Skein", "SM3", "Tiger", "Whirlpool"};
        KEYSTORES = new String[]{PROVIDER_NAME, "PKCS12"};
    }

    public BouncyCastleProvider() {
        super(PROVIDER_NAME, 1.52, info);
        AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction(){

            public Object run() {
                BouncyCastleProvider.this.setup();
                return null;
            }
        });
    }

    public static PrivateKey getPrivateKey(PrivateKeyInfo privateKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter)keyInfoConverters.get((Object)privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePrivate(privateKeyInfo);
    }

    public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter)keyInfoConverters.get((Object)subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePublic(subjectPublicKeyInfo);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void loadAlgorithms(String string, String[] arrstring) {
        int n = 0;
        while (n != arrstring.length) {
            Class class_;
            try {
                Class class_2;
                Class class_3;
                ClassLoader classLoader = this.getClass().getClassLoader();
                class_ = classLoader != null ? (class_3 = classLoader.loadClass(string + arrstring[n] + "$Mappings")) : (class_2 = Class.forName((String)(string + arrstring[n] + "$Mappings")));
            }
            catch (ClassNotFoundException classNotFoundException) {
                class_ = null;
            }
            if (class_ != null) {
                ((AlgorithmProvider)class_.newInstance()).configure(this);
            }
            ++n;
        }
        return;
        catch (Exception exception) {
            throw new InternalError("cannot create instance of " + string + arrstring[n] + "$Mappings : " + (Object)((Object)exception));
        }
    }

    private void setup() {
        this.loadAlgorithms(DIGEST_PACKAGE, DIGESTS);
        this.loadAlgorithms(SYMMETRIC_PACKAGE, SYMMETRIC_GENERIC);
        this.loadAlgorithms(SYMMETRIC_PACKAGE, SYMMETRIC_MACS);
        this.loadAlgorithms(SYMMETRIC_PACKAGE, SYMMETRIC_CIPHERS);
        this.loadAlgorithms(ASYMMETRIC_PACKAGE, ASYMMETRIC_GENERIC);
        this.loadAlgorithms(ASYMMETRIC_PACKAGE, ASYMMETRIC_CIPHERS);
        this.loadAlgorithms(KEYSTORE_PACKAGE, KEYSTORES);
        this.put((Object)"X509Store.CERTIFICATE/COLLECTION", (Object)"org.bouncycastle.jce.provider.X509StoreCertCollection");
        this.put((Object)"X509Store.ATTRIBUTECERTIFICATE/COLLECTION", (Object)"org.bouncycastle.jce.provider.X509StoreAttrCertCollection");
        this.put((Object)"X509Store.CRL/COLLECTION", (Object)"org.bouncycastle.jce.provider.X509StoreCRLCollection");
        this.put((Object)"X509Store.CERTIFICATEPAIR/COLLECTION", (Object)"org.bouncycastle.jce.provider.X509StoreCertPairCollection");
        this.put((Object)"X509Store.CERTIFICATE/LDAP", (Object)"org.bouncycastle.jce.provider.X509StoreLDAPCerts");
        this.put((Object)"X509Store.CRL/LDAP", (Object)"org.bouncycastle.jce.provider.X509StoreLDAPCRLs");
        this.put((Object)"X509Store.ATTRIBUTECERTIFICATE/LDAP", (Object)"org.bouncycastle.jce.provider.X509StoreLDAPAttrCerts");
        this.put((Object)"X509Store.CERTIFICATEPAIR/LDAP", (Object)"org.bouncycastle.jce.provider.X509StoreLDAPCertPairs");
        this.put((Object)"X509StreamParser.CERTIFICATE", (Object)"org.bouncycastle.jce.provider.X509CertParser");
        this.put((Object)"X509StreamParser.ATTRIBUTECERTIFICATE", (Object)"org.bouncycastle.jce.provider.X509AttrCertParser");
        this.put((Object)"X509StreamParser.CRL", (Object)"org.bouncycastle.jce.provider.X509CRLParser");
        this.put((Object)"X509StreamParser.CERTIFICATEPAIR", (Object)"org.bouncycastle.jce.provider.X509CertPairParser");
        this.put((Object)"Cipher.BROKENPBEWITHMD5ANDDES", (Object)"org.bouncycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithMD5AndDES");
        this.put((Object)"Cipher.BROKENPBEWITHSHA1ANDDES", (Object)"org.bouncycastle.jce.provider.BrokenJCEBlockCipher$BrokePBEWithSHA1AndDES");
        this.put((Object)"Cipher.OLDPBEWITHSHAANDTWOFISH-CBC", (Object)"org.bouncycastle.jce.provider.BrokenJCEBlockCipher$OldPBEWithSHAAndTwofish");
        this.put((Object)"CertPathValidator.RFC3281", (Object)"org.bouncycastle.jce.provider.PKIXAttrCertPathValidatorSpi");
        this.put((Object)"CertPathBuilder.RFC3281", (Object)"org.bouncycastle.jce.provider.PKIXAttrCertPathBuilderSpi");
        this.put((Object)"CertPathValidator.RFC3280", (Object)"org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi");
        this.put((Object)"CertPathBuilder.RFC3280", (Object)"org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi");
        this.put((Object)"CertPathValidator.PKIX", (Object)"org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi");
        this.put((Object)"CertPathBuilder.PKIX", (Object)"org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi");
        this.put((Object)"CertStore.Collection", (Object)"org.bouncycastle.jce.provider.CertStoreCollectionSpi");
        this.put((Object)"CertStore.LDAP", (Object)"org.bouncycastle.jce.provider.X509LDAPCertStoreSpi");
        this.put((Object)"CertStore.Multi", (Object)"org.bouncycastle.jce.provider.MultiCertStoreSpi");
        this.put((Object)"Alg.Alias.CertStore.X509LDAP", (Object)"LDAP");
    }

    @Override
    public void addAlgorithm(String string, String string2) {
        if (this.containsKey((Object)string)) {
            throw new IllegalStateException("duplicate provider key (" + string + ") found");
        }
        this.put((Object)string, (Object)string2);
    }

    @Override
    public void addKeyInfoConverter(ASN1ObjectIdentifier aSN1ObjectIdentifier, AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        keyInfoConverters.put((Object)aSN1ObjectIdentifier, (Object)asymmetricKeyInfoConverter);
    }

    @Override
    public boolean hasAlgorithm(String string, String string2) {
        return this.containsKey((Object)(string + "." + string2)) || this.containsKey((Object)("Alg.Alias." + string + "." + string2));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void setParameter(String string, Object object) {
        ProviderConfiguration providerConfiguration;
        ProviderConfiguration providerConfiguration2 = providerConfiguration = CONFIGURATION;
        synchronized (providerConfiguration2) {
            ((BouncyCastleProviderConfiguration)CONFIGURATION).setParameter(string, object);
            return;
        }
    }

}

