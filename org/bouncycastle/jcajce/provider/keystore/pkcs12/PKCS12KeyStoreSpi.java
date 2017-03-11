package org.bouncycastle.jcajce.provider.keystore.pkcs12;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.BEROutputStream;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.GOST28147Parameters;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.ntt.NTTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.AuthenticatedSafe;
import org.bouncycastle.asn1.pkcs.CertBag;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.pkcs.EncryptedData;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.MacData;
import org.bouncycastle.asn1.pkcs.PBES2Parameters;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.Pfx;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.SafeBag;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jcajce.PKCS12StoreParameter;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.spec.GOST28147ParameterSpec;
import org.bouncycastle.jcajce.spec.PBKDF2KeySpec;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKPKCS12StoreParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class PKCS12KeyStoreSpi extends KeyStoreSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers, BCKeyStore {
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    static final int KEY_SECRET = 2;
    private static final int MIN_ITERATIONS = 1024;
    static final int NULL = 0;
    private static final int SALT_SIZE = 20;
    static final int SEALED = 4;
    static final int SECRET = 3;
    private static final DefaultSecretKeyProvider keySizeProvider;
    private ASN1ObjectIdentifier certAlgorithm;
    private CertificateFactory certFact;
    private IgnoresCaseHashtable certs;
    private Hashtable chainCerts;
    private final JcaJceHelper helper;
    private ASN1ObjectIdentifier keyAlgorithm;
    private Hashtable keyCerts;
    private IgnoresCaseHashtable keys;
    private Hashtable localIds;
    protected SecureRandom random;

    public static class BCPKCS12KeyStore3DES extends PKCS12KeyStoreSpi {
        public BCPKCS12KeyStore3DES() {
            super(new BouncyCastleProvider(), pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }

    public static class BCPKCS12KeyStore extends PKCS12KeyStoreSpi {
        public BCPKCS12KeyStore() {
            super(new BouncyCastleProvider(), pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd40BitRC2_CBC);
        }
    }

    private class CertId {
        byte[] id;

        CertId(PublicKey publicKey) {
            this.id = PKCS12KeyStoreSpi.this.createSubjectKeyId(publicKey).getKeyIdentifier();
        }

        CertId(byte[] bArr) {
            this.id = bArr;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof CertId)) {
                return false;
            }
            return Arrays.areEqual(this.id, ((CertId) obj).id);
        }

        public int hashCode() {
            return Arrays.hashCode(this.id);
        }
    }

    public static class DefPKCS12KeyStore3DES extends PKCS12KeyStoreSpi {
        public DefPKCS12KeyStore3DES() {
            super(null, pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }

    public static class DefPKCS12KeyStore extends PKCS12KeyStoreSpi {
        public DefPKCS12KeyStore() {
            super(null, pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd40BitRC2_CBC);
        }
    }

    private static class DefaultSecretKeyProvider {
        private final Map KEY_SIZES;

        DefaultSecretKeyProvider() {
            Map hashMap = new HashMap();
            hashMap.put(new ASN1ObjectIdentifier("1.2.840.113533.7.66.10"), Integers.valueOf(X509KeyUsage.digitalSignature));
            hashMap.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
            hashMap.put(NISTObjectIdentifiers.id_aes128_CBC, Integers.valueOf(X509KeyUsage.digitalSignature));
            hashMap.put(NISTObjectIdentifiers.id_aes192_CBC, Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
            hashMap.put(NISTObjectIdentifiers.id_aes256_CBC, Integers.valueOf(SkeinMac.SKEIN_256));
            hashMap.put(NTTObjectIdentifiers.id_camellia128_cbc, Integers.valueOf(X509KeyUsage.digitalSignature));
            hashMap.put(NTTObjectIdentifiers.id_camellia192_cbc, Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
            hashMap.put(NTTObjectIdentifiers.id_camellia256_cbc, Integers.valueOf(SkeinMac.SKEIN_256));
            hashMap.put(CryptoProObjectIdentifiers.gostR28147_gcfb, Integers.valueOf(SkeinMac.SKEIN_256));
            this.KEY_SIZES = Collections.unmodifiableMap(hashMap);
        }

        public int getKeySize(AlgorithmIdentifier algorithmIdentifier) {
            Integer num = (Integer) this.KEY_SIZES.get(algorithmIdentifier.getAlgorithm());
            return num != null ? num.intValue() : -1;
        }
    }

    private static class IgnoresCaseHashtable {
        private Hashtable keys;
        private Hashtable orig;

        private IgnoresCaseHashtable() {
            this.orig = new Hashtable();
            this.keys = new Hashtable();
        }

        public Enumeration elements() {
            return this.orig.elements();
        }

        public Object get(String str) {
            String str2 = (String) this.keys.get(str == null ? null : Strings.toLowerCase(str));
            return str2 == null ? null : this.orig.get(str2);
        }

        public Enumeration keys() {
            return this.orig.keys();
        }

        public void put(String str, Object obj) {
            Object obj2;
            if (str == null) {
                obj2 = null;
            } else {
                String toLowerCase = Strings.toLowerCase(str);
            }
            String str2 = (String) this.keys.get(obj2);
            if (str2 != null) {
                this.orig.remove(str2);
            }
            this.keys.put(obj2, str);
            this.orig.put(str, obj);
        }

        public Object remove(String str) {
            String str2 = (String) this.keys.remove(str == null ? null : Strings.toLowerCase(str));
            return str2 == null ? null : this.orig.remove(str2);
        }
    }

    static {
        keySizeProvider = new DefaultSecretKeyProvider();
    }

    public PKCS12KeyStoreSpi(Provider provider, ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1ObjectIdentifier aSN1ObjectIdentifier2) {
        this.helper = new BCJcaJceHelper();
        this.keys = new IgnoresCaseHashtable();
        this.localIds = new Hashtable();
        this.certs = new IgnoresCaseHashtable();
        this.chainCerts = new Hashtable();
        this.keyCerts = new Hashtable();
        this.random = new SecureRandom();
        this.keyAlgorithm = aSN1ObjectIdentifier;
        this.certAlgorithm = aSN1ObjectIdentifier2;
        if (provider != null) {
            try {
                this.certFact = CertificateFactory.getInstance("X.509", provider);
                return;
            } catch (Exception e) {
                throw new IllegalArgumentException("can't create cert factory - " + e.toString());
            }
        }
        this.certFact = CertificateFactory.getInstance("X.509");
    }

    private byte[] calculatePbeMac(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] bArr, int i, char[] cArr, boolean z, byte[] bArr2) {
        SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(aSN1ObjectIdentifier.getId());
        AlgorithmParameterSpec pBEParameterSpec = new PBEParameterSpec(bArr, i);
        BCPBEKey bCPBEKey = (BCPBEKey) createSecretKeyFactory.generateSecret(new PBEKeySpec(cArr));
        bCPBEKey.setTryWrongPKCS12Zero(z);
        Mac createMac = this.helper.createMac(aSN1ObjectIdentifier.getId());
        createMac.init(bCPBEKey, pBEParameterSpec);
        createMac.update(bArr2);
        return createMac.doFinal();
    }

    private Cipher createCipher(int i, char[] cArr, AlgorithmIdentifier algorithmIdentifier) {
        Key generateSecret;
        PBES2Parameters instance = PBES2Parameters.getInstance(algorithmIdentifier.getParameters());
        PBKDF2Params instance2 = PBKDF2Params.getInstance(instance.getKeyDerivationFunc().getParameters());
        AlgorithmIdentifier instance3 = AlgorithmIdentifier.getInstance(instance.getEncryptionScheme());
        SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(instance.getKeyDerivationFunc().getAlgorithm().getId());
        if (instance2.isDefaultPrf()) {
            generateSecret = createSecretKeyFactory.generateSecret(new PBEKeySpec(cArr, instance2.getSalt(), instance2.getIterationCount().intValue(), keySizeProvider.getKeySize(instance3)));
        } else {
            generateSecret = createSecretKeyFactory.generateSecret(new PBKDF2KeySpec(cArr, instance2.getSalt(), instance2.getIterationCount().intValue(), keySizeProvider.getKeySize(instance3), instance2.getPrf()));
        }
        Cipher instance4 = Cipher.getInstance(instance.getEncryptionScheme().getAlgorithm().getId());
        AlgorithmIdentifier.getInstance(instance.getEncryptionScheme());
        ASN1Encodable parameters = instance.getEncryptionScheme().getParameters();
        if (parameters instanceof ASN1OctetString) {
            instance4.init(i, generateSecret, new IvParameterSpec(ASN1OctetString.getInstance(parameters).getOctets()));
        } else {
            GOST28147Parameters instance5 = GOST28147Parameters.getInstance(parameters);
            instance4.init(i, generateSecret, new GOST28147ParameterSpec(instance5.getEncryptionParamSet(), instance5.getIV()));
        }
        return instance4;
    }

    private SubjectKeyIdentifier createSubjectKeyId(PublicKey publicKey) {
        try {
            return new SubjectKeyIdentifier(getDigest(new SubjectPublicKeyInfo((ASN1Sequence) ASN1Primitive.fromByteArray(publicKey.getEncoded()))));
        } catch (Exception e) {
            throw new RuntimeException("error creating key");
        }
    }

    private void doStore(OutputStream outputStream, char[] cArr, boolean z) {
        if (cArr == null) {
            throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
        }
        byte[] bArr;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration keys = this.keys.keys();
        while (keys.hasMoreElements()) {
            PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier;
            Object obj;
            bArr = new byte[SALT_SIZE];
            this.random.nextBytes(bArr);
            String str = (String) keys.nextElement();
            PrivateKey privateKey = (PrivateKey) this.keys.get(str);
            PKCS12PBEParams pKCS12PBEParams = new PKCS12PBEParams(bArr, MIN_ITERATIONS);
            EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(this.keyAlgorithm, pKCS12PBEParams.toASN1Primitive()), wrapKey(this.keyAlgorithm.getId(), privateKey, pKCS12PBEParams, cArr));
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            if (privateKey instanceof PKCS12BagAttributeCarrier) {
                pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) privateKey;
                DERBMPString dERBMPString = (DERBMPString) pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_friendlyName);
                if (dERBMPString == null || !dERBMPString.getString().equals(str)) {
                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str));
                }
                if (pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_localKeyId) == null) {
                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_localKeyId, createSubjectKeyId(engineGetCertificate(str).getPublicKey()));
                }
                Enumeration bagAttributeKeys = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                obj = NULL;
                while (bagAttributeKeys.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) bagAttributeKeys.nextElement();
                    ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
                    aSN1EncodableVector3.add(aSN1ObjectIdentifier);
                    aSN1EncodableVector3.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier)));
                    obj = KEY_PUBLIC;
                    aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
                }
            } else {
                obj = NULL;
            }
            if (obj == null) {
                ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
                Certificate engineGetCertificate = engineGetCertificate(str);
                aSN1EncodableVector4.add(pkcs_9_at_localKeyId);
                aSN1EncodableVector4.add(new DERSet(createSubjectKeyId(engineGetCertificate.getPublicKey())));
                aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
                aSN1EncodableVector4 = new ASN1EncodableVector();
                aSN1EncodableVector4.add(pkcs_9_at_friendlyName);
                aSN1EncodableVector4.add(new DERSet(new DERBMPString(str)));
                aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
            }
            aSN1EncodableVector.add(new SafeBag(pkcs8ShroudedKeyBag, encryptedPrivateKeyInfo.toASN1Primitive(), new DERSet(aSN1EncodableVector2)));
        }
        ASN1Encodable bEROctetString = new BEROctetString(new DERSequence(aSN1EncodableVector).getEncoded(ASN1Encoding.DER));
        byte[] bArr2 = new byte[SALT_SIZE];
        this.random.nextBytes(bArr2);
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.certAlgorithm, new PKCS12PBEParams(bArr2, MIN_ITERATIONS).toASN1Primitive());
        Hashtable hashtable = new Hashtable();
        Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            ASN1EncodableVector aSN1EncodableVector6;
            DERBMPString dERBMPString2;
            Enumeration bagAttributeKeys2;
            try {
                Object obj2;
                str = (String) keys2.nextElement();
                Certificate engineGetCertificate2 = engineGetCertificate(str);
                CertBag certBag = new CertBag(x509Certificate, new DEROctetString(engineGetCertificate2.getEncoded()));
                aSN1EncodableVector6 = new ASN1EncodableVector();
                if (engineGetCertificate2 instanceof PKCS12BagAttributeCarrier) {
                    pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) engineGetCertificate2;
                    dERBMPString2 = (DERBMPString) pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_friendlyName);
                    if (dERBMPString2 == null || !dERBMPString2.getString().equals(str)) {
                        pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str));
                    }
                    if (pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_localKeyId) == null) {
                        pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_localKeyId, createSubjectKeyId(engineGetCertificate2.getPublicKey()));
                    }
                    bagAttributeKeys2 = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                    Object obj3 = NULL;
                    while (bagAttributeKeys2.hasMoreElements()) {
                        ASN1ObjectIdentifier aSN1ObjectIdentifier2 = (ASN1ObjectIdentifier) bagAttributeKeys2.nextElement();
                        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
                        aSN1EncodableVector7.add(aSN1ObjectIdentifier2);
                        aSN1EncodableVector7.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier2)));
                        aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector7));
                        obj3 = KEY_PUBLIC;
                    }
                    obj2 = obj3;
                } else {
                    obj2 = NULL;
                }
                if (obj2 == null) {
                    aSN1EncodableVector4 = new ASN1EncodableVector();
                    aSN1EncodableVector4.add(pkcs_9_at_localKeyId);
                    aSN1EncodableVector4.add(new DERSet(createSubjectKeyId(engineGetCertificate2.getPublicKey())));
                    aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
                    aSN1EncodableVector4 = new ASN1EncodableVector();
                    aSN1EncodableVector4.add(pkcs_9_at_friendlyName);
                    aSN1EncodableVector4.add(new DERSet(new DERBMPString(str)));
                    aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
                }
                aSN1EncodableVector5.add(new SafeBag(certBag, certBag.toASN1Primitive(), new DERSet(aSN1EncodableVector6)));
                hashtable.put(engineGetCertificate2, engineGetCertificate2);
            } catch (CertificateEncodingException e) {
                throw new IOException("Error encoding certificate: " + e.toString());
            }
        }
        keys2 = this.certs.keys();
        while (keys2.hasMoreElements()) {
            try {
                str = (String) keys2.nextElement();
                Certificate certificate = (Certificate) this.certs.get(str);
                Object obj4 = null;
                if (this.keys.get(str) == null) {
                    certBag = new CertBag(x509Certificate, new DEROctetString(certificate.getEncoded()));
                    aSN1EncodableVector6 = new ASN1EncodableVector();
                    if (certificate instanceof PKCS12BagAttributeCarrier) {
                        PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier) certificate;
                        dERBMPString2 = (DERBMPString) pKCS12BagAttributeCarrier2.getBagAttribute(pkcs_9_at_friendlyName);
                        if (dERBMPString2 == null || !dERBMPString2.getString().equals(str)) {
                            pKCS12BagAttributeCarrier2.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str));
                        }
                        bagAttributeKeys2 = pKCS12BagAttributeCarrier2.getBagAttributeKeys();
                        while (bagAttributeKeys2.hasMoreElements()) {
                            aSN1ObjectIdentifier2 = (ASN1ObjectIdentifier) bagAttributeKeys2.nextElement();
                            if (!aSN1ObjectIdentifier2.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                                aSN1EncodableVector7 = new ASN1EncodableVector();
                                aSN1EncodableVector7.add(aSN1ObjectIdentifier2);
                                aSN1EncodableVector7.add(new DERSet(pKCS12BagAttributeCarrier2.getBagAttribute(aSN1ObjectIdentifier2)));
                                aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector7));
                                obj4 = KEY_PUBLIC;
                            }
                        }
                    }
                    if (obj4 == null) {
                        aSN1EncodableVector3 = new ASN1EncodableVector();
                        aSN1EncodableVector3.add(pkcs_9_at_friendlyName);
                        aSN1EncodableVector3.add(new DERSet(new DERBMPString(str)));
                        aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector3));
                    }
                    aSN1EncodableVector5.add(new SafeBag(certBag, certBag.toASN1Primitive(), new DERSet(aSN1EncodableVector6)));
                    hashtable.put(certificate, certificate);
                }
            } catch (CertificateEncodingException e2) {
                throw new IOException("Error encoding certificate: " + e2.toString());
            }
        }
        Enumeration keys3 = this.chainCerts.keys();
        while (keys3.hasMoreElements()) {
            try {
                Certificate certificate2 = (Certificate) this.chainCerts.get((CertId) keys3.nextElement());
                if (hashtable.get(certificate2) == null) {
                    CertBag certBag2 = new CertBag(x509Certificate, new DEROctetString(certificate2.getEncoded()));
                    aSN1EncodableVector7 = new ASN1EncodableVector();
                    if (certificate2 instanceof PKCS12BagAttributeCarrier) {
                        PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier) certificate2;
                        keys2 = pKCS12BagAttributeCarrier3.getBagAttributeKeys();
                        while (keys2.hasMoreElements()) {
                            ASN1ObjectIdentifier aSN1ObjectIdentifier3 = (ASN1ObjectIdentifier) keys2.nextElement();
                            if (!aSN1ObjectIdentifier3.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                                ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
                                aSN1EncodableVector8.add(aSN1ObjectIdentifier3);
                                aSN1EncodableVector8.add(new DERSet(pKCS12BagAttributeCarrier3.getBagAttribute(aSN1ObjectIdentifier3)));
                                aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector8));
                            }
                        }
                    }
                    aSN1EncodableVector5.add(new SafeBag(certBag, certBag2.toASN1Primitive(), new DERSet(aSN1EncodableVector7)));
                }
            } catch (CertificateEncodingException e22) {
                throw new IOException("Error encoding certificate: " + e22.toString());
            }
        }
        EncryptedData encryptedData = new EncryptedData(data, algorithmIdentifier, new BEROctetString(cryptData(true, algorithmIdentifier, cArr, false, new DERSequence(aSN1EncodableVector5).getEncoded(ASN1Encoding.DER))));
        ContentInfo[] contentInfoArr = new ContentInfo[KEY_SECRET];
        contentInfoArr[NULL] = new ContentInfo(data, bEROctetString);
        contentInfoArr[KEY_PUBLIC] = new ContentInfo(encryptedData, encryptedData.toASN1Primitive());
        ASN1Encodable authenticatedSafe = new AuthenticatedSafe(contentInfoArr);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        (z ? new DEROutputStream(byteArrayOutputStream) : new BEROutputStream(byteArrayOutputStream)).writeObject(authenticatedSafe);
        ContentInfo contentInfo = new ContentInfo(data, new BEROctetString(byteArrayOutputStream.toByteArray()));
        bArr = new byte[SALT_SIZE];
        this.random.nextBytes(bArr);
        try {
            (z ? new DEROutputStream(outputStream) : new BEROutputStream(outputStream)).writeObject(new Pfx(contentInfo, new MacData(new DigestInfo(new AlgorithmIdentifier(id_SHA1, DERNull.INSTANCE), calculatePbeMac(id_SHA1, bArr, MIN_ITERATIONS, cArr, false, ((ASN1OctetString) contentInfo.getContent()).getOctets())), bArr, MIN_ITERATIONS)));
        } catch (Exception e3) {
            throw new IOException("error constructing MAC: " + e3.toString());
        }
    }

    private static byte[] getDigest(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        Digest sHA1Digest = new SHA1Digest();
        byte[] bArr = new byte[sHA1Digest.getDigestSize()];
        byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sHA1Digest.update(bytes, NULL, bytes.length);
        sHA1Digest.doFinal(bArr, NULL);
        return bArr;
    }

    protected byte[] cryptData(boolean z, AlgorithmIdentifier algorithmIdentifier, char[] cArr, boolean z2, byte[] bArr) {
        byte[] doFinal;
        ASN1ObjectIdentifier algorithm = algorithmIdentifier.getAlgorithm();
        int i = z ? KEY_PUBLIC : KEY_SECRET;
        if (algorithm.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
            PKCS12PBEParams instance = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
            KeySpec pBEKeySpec = new PBEKeySpec(cArr);
            try {
                SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(algorithm.getId());
                AlgorithmParameterSpec pBEParameterSpec = new PBEParameterSpec(instance.getIV(), instance.getIterations().intValue());
                BCPBEKey bCPBEKey = (BCPBEKey) createSecretKeyFactory.generateSecret(pBEKeySpec);
                bCPBEKey.setTryWrongPKCS12Zero(z2);
                Cipher createCipher = this.helper.createCipher(algorithm.getId());
                createCipher.init(i, bCPBEKey, pBEParameterSpec);
                doFinal = createCipher.doFinal(bArr);
            } catch (Exception e) {
                throw new IOException("exception decrypting data - " + e.toString());
            }
        } else if (algorithm.equals(PKCSObjectIdentifiers.id_PBES2)) {
            try {
                doFinal = createCipher(i, cArr, algorithmIdentifier).doFinal(bArr);
            } catch (Exception e2) {
                throw new IOException("exception decrypting data - " + e2.toString());
            }
        } else {
            throw new IOException("unknown PBE algorithm: " + algorithm);
        }
        return doFinal;
    }

    public Enumeration engineAliases() {
        Hashtable hashtable = new Hashtable();
        Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            String str = (String) keys2.nextElement();
            if (hashtable.get(str) == null) {
                hashtable.put(str, "key");
            }
        }
        return hashtable.keys();
    }

    public boolean engineContainsAlias(String str) {
        return (this.certs.get(str) == null && this.keys.get(str) == null) ? false : true;
    }

    public void engineDeleteEntry(String str) {
        Key key = (Key) this.keys.remove(str);
        Certificate certificate = (Certificate) this.certs.remove(str);
        if (certificate != null) {
            this.chainCerts.remove(new CertId(certificate.getPublicKey()));
        }
        if (key != null) {
            String str2 = (String) this.localIds.remove(str);
            Certificate certificate2 = str2 != null ? (Certificate) this.keyCerts.remove(str2) : certificate;
            if (certificate2 != null) {
                this.chainCerts.remove(new CertId(certificate2.getPublicKey()));
            }
        }
    }

    public Certificate engineGetCertificate(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null alias passed to getCertificate.");
        }
        Certificate certificate = (Certificate) this.certs.get(str);
        if (certificate != null) {
            return certificate;
        }
        String str2 = (String) this.localIds.get(str);
        return str2 != null ? (Certificate) this.keyCerts.get(str2) : (Certificate) this.keyCerts.get(str);
    }

    public String engineGetCertificateAlias(Certificate certificate) {
        Enumeration elements = this.certs.elements();
        Enumeration keys = this.certs.keys();
        while (elements.hasMoreElements()) {
            String str = (String) keys.nextElement();
            if (((Certificate) elements.nextElement()).equals(certificate)) {
                return str;
            }
        }
        elements = this.keyCerts.elements();
        keys = this.keyCerts.keys();
        while (elements.hasMoreElements()) {
            str = (String) keys.nextElement();
            if (((Certificate) elements.nextElement()).equals(certificate)) {
                return str;
            }
        }
        return null;
    }

    public Certificate[] engineGetCertificateChain(String str) {
        Certificate[] certificateArr = null;
        if (str == null) {
            throw new IllegalArgumentException("null alias passed to getCertificateChain.");
        }
        if (engineIsKeyEntry(str)) {
            X509Certificate engineGetCertificate = engineGetCertificate(str);
            if (engineGetCertificate != null) {
                Vector vector = new Vector();
                while (engineGetCertificate != null) {
                    Certificate certificate;
                    X509Certificate x509Certificate = engineGetCertificate;
                    byte[] extensionValue = x509Certificate.getExtensionValue(Extension.authorityKeyIdentifier.getId());
                    if (extensionValue != null) {
                        try {
                            AuthorityKeyIdentifier instance = AuthorityKeyIdentifier.getInstance(new ASN1InputStream(((ASN1OctetString) new ASN1InputStream(extensionValue).readObject()).getOctets()).readObject());
                            certificate = instance.getKeyIdentifier() != null ? (Certificate) this.chainCerts.get(new CertId(instance.getKeyIdentifier())) : null;
                        } catch (IOException e) {
                            throw new RuntimeException(e.toString());
                        }
                    }
                    certificate = null;
                    if (certificate == null) {
                        Principal issuerDN = x509Certificate.getIssuerDN();
                        if (!issuerDN.equals(x509Certificate.getSubjectDN())) {
                            Enumeration keys = this.chainCerts.keys();
                            while (keys.hasMoreElements()) {
                                X509Certificate x509Certificate2 = (X509Certificate) this.chainCerts.get(keys.nextElement());
                                if (x509Certificate2.getSubjectDN().equals(issuerDN)) {
                                    try {
                                        x509Certificate.verify(x509Certificate2.getPublicKey());
                                        x509Certificate = x509Certificate2;
                                        break;
                                    } catch (Exception e2) {
                                    }
                                }
                            }
                        }
                    }
                    Certificate certificate2 = certificate;
                    vector.addElement(engineGetCertificate);
                    if (x509Certificate == engineGetCertificate) {
                        x509Certificate = null;
                    }
                    engineGetCertificate = x509Certificate;
                }
                certificateArr = new Certificate[vector.size()];
                for (int i = NULL; i != certificateArr.length; i += KEY_PUBLIC) {
                    certificateArr[i] = (Certificate) vector.elementAt(i);
                }
            }
        }
        return certificateArr;
    }

    public Date engineGetCreationDate(String str) {
        if (str != null) {
            return (this.keys.get(str) == null && this.certs.get(str) == null) ? null : new Date();
        } else {
            throw new NullPointerException("alias == null");
        }
    }

    public Key engineGetKey(String str, char[] cArr) {
        if (str != null) {
            return (Key) this.keys.get(str);
        }
        throw new IllegalArgumentException("null alias passed to getKey.");
    }

    public boolean engineIsCertificateEntry(String str) {
        return this.certs.get(str) != null && this.keys.get(str) == null;
    }

    public boolean engineIsKeyEntry(String str) {
        return this.keys.get(str) != null;
    }

    public void engineLoad(InputStream inputStream, char[] cArr) {
        if (inputStream != null) {
            if (cArr == null) {
                throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
            }
            InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.mark(10);
            if (bufferedInputStream.read() != 48) {
                throw new IOException("stream does not represent a PKCS12 key store");
            }
            int intValue;
            boolean z;
            Object obj;
            ASN1Sequence aSN1Sequence;
            PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier;
            ASN1OctetString aSN1OctetString;
            bufferedInputStream.reset();
            Pfx instance = Pfx.getInstance((ASN1Sequence) new ASN1InputStream(bufferedInputStream).readObject());
            ContentInfo authSafe = instance.getAuthSafe();
            Vector vector = new Vector();
            if (instance.getMacData() != null) {
                MacData macData = instance.getMacData();
                DigestInfo mac = macData.getMac();
                AlgorithmIdentifier algorithmId = mac.getAlgorithmId();
                byte[] salt = macData.getSalt();
                intValue = macData.getIterationCount().intValue();
                byte[] octets = ((ASN1OctetString) authSafe.getContent()).getOctets();
                try {
                    boolean z2;
                    byte[] calculatePbeMac = calculatePbeMac(algorithmId.getAlgorithm(), salt, intValue, cArr, false, octets);
                    byte[] digest = mac.getDigest();
                    if (Arrays.constantTimeAreEqual(calculatePbeMac, digest)) {
                        z2 = NULL;
                    } else if (cArr.length > 0) {
                        throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                    } else if (Arrays.constantTimeAreEqual(calculatePbeMac(algorithmId.getAlgorithm(), salt, intValue, cArr, true, octets), digest)) {
                        z2 = true;
                    } else {
                        throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                    }
                    z = z2;
                } catch (IOException e) {
                    throw e;
                } catch (Exception e2) {
                    throw new IOException("error constructing MAC: " + e2.toString());
                }
            }
            z = false;
            this.keys = new IgnoresCaseHashtable();
            this.localIds = new Hashtable();
            if (authSafe.getContentType().equals(data)) {
                ContentInfo[] contentInfo = AuthenticatedSafe.getInstance(new ASN1InputStream(((ASN1OctetString) authSafe.getContent()).getOctets()).readObject()).getContentInfo();
                int i = NULL;
                obj = NULL;
                while (i != contentInfo.length) {
                    Object obj2;
                    SafeBag instance2;
                    EncryptedPrivateKeyInfo instance3;
                    PrivateKey unwrapKey;
                    ASN1OctetString aSN1OctetString2;
                    Enumeration objects;
                    ASN1Sequence aSN1Sequence2;
                    ASN1ObjectIdentifier aSN1ObjectIdentifier;
                    ASN1Set aSN1Set;
                    ASN1Encodable aSN1Encodable;
                    ASN1Encodable bagAttribute;
                    String string;
                    String str;
                    ASN1OctetString aSN1OctetString3;
                    String str2;
                    String str3;
                    if (contentInfo[i].getContentType().equals(data)) {
                        aSN1Sequence = (ASN1Sequence) new ASN1InputStream(((ASN1OctetString) contentInfo[i].getContent()).getOctets()).readObject();
                        Object obj3 = obj;
                        int i2 = NULL;
                        while (i2 != aSN1Sequence.size()) {
                            Object obj4;
                            instance2 = SafeBag.getInstance(aSN1Sequence.getObjectAt(i2));
                            if (instance2.getBagId().equals(pkcs8ShroudedKeyBag)) {
                                instance3 = EncryptedPrivateKeyInfo.getInstance(instance2.getBagValue());
                                unwrapKey = unwrapKey(instance3.getEncryptionAlgorithm(), instance3.getEncryptedData(), cArr, z);
                                pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) unwrapKey;
                                Object obj5 = null;
                                aSN1OctetString2 = null;
                                if (instance2.getBagAttributes() != null) {
                                    objects = instance2.getBagAttributes().getObjects();
                                    while (objects.hasMoreElements()) {
                                        aSN1Sequence2 = (ASN1Sequence) objects.nextElement();
                                        aSN1ObjectIdentifier = (ASN1ObjectIdentifier) aSN1Sequence2.getObjectAt(NULL);
                                        aSN1Set = (ASN1Set) aSN1Sequence2.getObjectAt(KEY_PUBLIC);
                                        if (aSN1Set.size() > 0) {
                                            aSN1Encodable = (ASN1Primitive) aSN1Set.getObjectAt(NULL);
                                            bagAttribute = pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier);
                                            if (bagAttribute == null) {
                                                pKCS12BagAttributeCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
                                            } else if (!bagAttribute.toASN1Primitive().equals(aSN1Encodable)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        aSN1Encodable = null;
                                        if (aSN1ObjectIdentifier.equals(pkcs_9_at_friendlyName)) {
                                            string = ((DERBMPString) aSN1Encodable).getString();
                                            this.keys.put(string, unwrapKey);
                                            str = string;
                                            aSN1OctetString3 = aSN1OctetString2;
                                        } else if (aSN1ObjectIdentifier.equals(pkcs_9_at_localKeyId)) {
                                            aSN1OctetString3 = (ASN1OctetString) aSN1Encodable;
                                            str = str2;
                                        } else {
                                            aSN1OctetString3 = aSN1OctetString2;
                                            str = str2;
                                        }
                                        aSN1OctetString2 = aSN1OctetString3;
                                        str2 = str;
                                    }
                                }
                                if (aSN1OctetString2 != null) {
                                    str3 = new String(Hex.encode(aSN1OctetString2.getOctets()));
                                    if (obj5 == null) {
                                        this.keys.put(str3, unwrapKey);
                                    } else {
                                        this.localIds.put(obj5, str3);
                                    }
                                } else {
                                    obj3 = KEY_PUBLIC;
                                    this.keys.put("unmarked", unwrapKey);
                                }
                                obj4 = obj3;
                            } else if (instance2.getBagId().equals(certBag)) {
                                vector.addElement(instance2);
                                obj4 = obj3;
                            } else {
                                System.out.println("extra in data " + instance2.getBagId());
                                System.out.println(ASN1Dump.dumpAsString(instance2));
                                obj4 = obj3;
                            }
                            i2 += KEY_PUBLIC;
                            obj3 = obj4;
                        }
                        obj2 = obj3;
                    } else if (contentInfo[i].getContentType().equals(encryptedData)) {
                        EncryptedData instance4 = EncryptedData.getInstance(contentInfo[i].getContent());
                        aSN1Sequence = (ASN1Sequence) ASN1Primitive.fromByteArray(cryptData(false, instance4.getEncryptionAlgorithm(), cArr, z, instance4.getContent().getOctets()));
                        for (int i3 = NULL; i3 != aSN1Sequence.size(); i3 += KEY_PUBLIC) {
                            instance2 = SafeBag.getInstance(aSN1Sequence.getObjectAt(i3));
                            if (instance2.getBagId().equals(certBag)) {
                                vector.addElement(instance2);
                            } else if (instance2.getBagId().equals(pkcs8ShroudedKeyBag)) {
                                instance3 = EncryptedPrivateKeyInfo.getInstance(instance2.getBagValue());
                                unwrapKey = unwrapKey(instance3.getEncryptionAlgorithm(), instance3.getEncryptedData(), cArr, z);
                                pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) unwrapKey;
                                str2 = null;
                                aSN1OctetString2 = null;
                                objects = instance2.getBagAttributes().getObjects();
                                while (objects.hasMoreElements()) {
                                    aSN1Sequence2 = (ASN1Sequence) objects.nextElement();
                                    aSN1ObjectIdentifier = (ASN1ObjectIdentifier) aSN1Sequence2.getObjectAt(NULL);
                                    aSN1Set = (ASN1Set) aSN1Sequence2.getObjectAt(KEY_PUBLIC);
                                    if (aSN1Set.size() > 0) {
                                        aSN1Encodable = (ASN1Primitive) aSN1Set.getObjectAt(NULL);
                                        bagAttribute = pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier);
                                        if (bagAttribute == null) {
                                            pKCS12BagAttributeCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
                                        } else if (!bagAttribute.toASN1Primitive().equals(aSN1Encodable)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    }
                                    aSN1Encodable = null;
                                    if (aSN1ObjectIdentifier.equals(pkcs_9_at_friendlyName)) {
                                        string = ((DERBMPString) aSN1Encodable).getString();
                                        this.keys.put(string, unwrapKey);
                                        str = string;
                                        aSN1OctetString3 = aSN1OctetString2;
                                    } else if (aSN1ObjectIdentifier.equals(pkcs_9_at_localKeyId)) {
                                        aSN1OctetString3 = (ASN1OctetString) aSN1Encodable;
                                        str = str2;
                                    } else {
                                        aSN1OctetString3 = aSN1OctetString2;
                                        str = str2;
                                    }
                                    aSN1OctetString2 = aSN1OctetString3;
                                    str2 = str;
                                }
                                str3 = new String(Hex.encode(aSN1OctetString2.getOctets()));
                                if (str2 == null) {
                                    this.keys.put(str3, unwrapKey);
                                } else {
                                    this.localIds.put(str2, str3);
                                }
                            } else if (instance2.getBagId().equals(keyBag)) {
                                unwrapKey = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(instance2.getBagValue()));
                                pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) unwrapKey;
                                String str4 = null;
                                aSN1OctetString = null;
                                Enumeration objects2 = instance2.getBagAttributes().getObjects();
                                while (objects2.hasMoreElements()) {
                                    aSN1Sequence2 = ASN1Sequence.getInstance(objects2.nextElement());
                                    ASN1ObjectIdentifier instance5 = ASN1ObjectIdentifier.getInstance(aSN1Sequence2.getObjectAt(NULL));
                                    aSN1Set = ASN1Set.getInstance(aSN1Sequence2.getObjectAt(KEY_PUBLIC));
                                    if (aSN1Set.size() > 0) {
                                        ASN1Primitive aSN1Primitive = (ASN1Primitive) aSN1Set.getObjectAt(NULL);
                                        ASN1Encodable bagAttribute2 = pKCS12BagAttributeCarrier.getBagAttribute(instance5);
                                        if (bagAttribute2 != null) {
                                            if (!bagAttribute2.toASN1Primitive().equals(aSN1Primitive)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        pKCS12BagAttributeCarrier.setBagAttribute(instance5, aSN1Primitive);
                                        if (instance5.equals(pkcs_9_at_friendlyName)) {
                                            string = ((DERBMPString) aSN1Primitive).getString();
                                            this.keys.put(string, unwrapKey);
                                            ASN1OctetString aSN1OctetString4 = aSN1OctetString;
                                            str = string;
                                            aSN1OctetString3 = aSN1OctetString4;
                                        } else {
                                            if (instance5.equals(pkcs_9_at_localKeyId)) {
                                                aSN1OctetString3 = (ASN1OctetString) aSN1Primitive;
                                                str = str4;
                                            }
                                        }
                                        str4 = str;
                                        aSN1OctetString = aSN1OctetString3;
                                    }
                                    aSN1OctetString3 = aSN1OctetString;
                                    str = str4;
                                    str4 = str;
                                    aSN1OctetString = aSN1OctetString3;
                                }
                                str3 = new String(Hex.encode(aSN1OctetString.getOctets()));
                                if (str4 == null) {
                                    this.keys.put(str3, unwrapKey);
                                } else {
                                    this.localIds.put(str4, str3);
                                }
                            } else {
                                System.out.println("extra in encryptedData " + instance2.getBagId());
                                System.out.println(ASN1Dump.dumpAsString(instance2));
                            }
                        }
                        obj2 = obj;
                    } else {
                        System.out.println("extra " + contentInfo[i].getContentType().getId());
                        System.out.println("extra " + ASN1Dump.dumpAsString(contentInfo[i].getContent()));
                        obj2 = obj;
                    }
                    i += KEY_PUBLIC;
                    obj = obj2;
                }
            } else {
                obj = NULL;
            }
            this.certs = new IgnoresCaseHashtable();
            this.chainCerts = new Hashtable();
            this.keyCerts = new Hashtable();
            intValue = NULL;
            while (intValue != vector.size()) {
                SafeBag safeBag = (SafeBag) vector.elementAt(intValue);
                CertBag instance6 = CertBag.getInstance(safeBag.getBagValue());
                if (instance6.getCertId().equals(x509Certificate)) {
                    try {
                        String string2;
                        Certificate generateCertificate = this.certFact.generateCertificate(new ByteArrayInputStream(((ASN1OctetString) instance6.getCertValue()).getOctets()));
                        aSN1OctetString = null;
                        String str5 = null;
                        if (safeBag.getBagAttributes() != null) {
                            Enumeration objects3 = safeBag.getBagAttributes().getObjects();
                            while (objects3.hasMoreElements()) {
                                ASN1OctetString aSN1OctetString5;
                                aSN1Sequence = ASN1Sequence.getInstance(objects3.nextElement());
                                ASN1ObjectIdentifier instance7 = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(NULL));
                                ASN1Set instance8 = ASN1Set.getInstance(aSN1Sequence.getObjectAt(KEY_PUBLIC));
                                if (instance8.size() > 0) {
                                    ASN1Primitive aSN1Primitive2 = (ASN1Primitive) instance8.getObjectAt(NULL);
                                    if (generateCertificate instanceof PKCS12BagAttributeCarrier) {
                                        pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier) generateCertificate;
                                        ASN1Encodable bagAttribute3 = pKCS12BagAttributeCarrier.getBagAttribute(instance7);
                                        if (bagAttribute3 == null) {
                                            pKCS12BagAttributeCarrier.setBagAttribute(instance7, aSN1Primitive2);
                                        } else if (!bagAttribute3.toASN1Primitive().equals(aSN1Primitive2)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    }
                                    if (instance7.equals(pkcs_9_at_friendlyName)) {
                                        string2 = ((DERBMPString) aSN1Primitive2).getString();
                                        aSN1OctetString5 = aSN1OctetString;
                                    } else if (instance7.equals(pkcs_9_at_localKeyId)) {
                                        aSN1OctetString5 = (ASN1OctetString) aSN1Primitive2;
                                        string2 = str5;
                                    }
                                    str5 = string2;
                                    aSN1OctetString = aSN1OctetString5;
                                }
                                string2 = str5;
                                aSN1OctetString5 = aSN1OctetString;
                                str5 = string2;
                                aSN1OctetString = aSN1OctetString5;
                            }
                        }
                        this.chainCerts.put(new CertId(generateCertificate.getPublicKey()), generateCertificate);
                        if (obj == null) {
                            if (aSN1OctetString != null) {
                                this.keyCerts.put(new String(Hex.encode(aSN1OctetString.getOctets())), generateCertificate);
                            }
                            if (str5 != null) {
                                this.certs.put(str5, generateCertificate);
                            }
                        } else if (this.keyCerts.isEmpty()) {
                            string2 = new String(Hex.encode(createSubjectKeyId(generateCertificate.getPublicKey()).getKeyIdentifier()));
                            this.keyCerts.put(string2, generateCertificate);
                            this.keys.put(string2, this.keys.remove("unmarked"));
                        }
                        intValue += KEY_PUBLIC;
                    } catch (Exception e22) {
                        throw new RuntimeException(e22.toString());
                    }
                }
                throw new RuntimeException("Unsupported certificate type: " + instance6.getCertId());
            }
        }
    }

    public void engineSetCertificateEntry(String str, Certificate certificate) {
        if (this.keys.get(str) != null) {
            throw new KeyStoreException("There is a key entry with the name " + str + ".");
        }
        this.certs.put(str, certificate);
        this.chainCerts.put(new CertId(certificate.getPublicKey()), certificate);
    }

    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) {
        int i = NULL;
        if (!(key instanceof PrivateKey)) {
            throw new KeyStoreException("PKCS12 does not support non-PrivateKeys");
        } else if ((key instanceof PrivateKey) && certificateArr == null) {
            throw new KeyStoreException("no certificate chain for private key");
        } else {
            if (this.keys.get(str) != null) {
                engineDeleteEntry(str);
            }
            this.keys.put(str, key);
            if (certificateArr != null) {
                this.certs.put(str, certificateArr[NULL]);
                while (i != certificateArr.length) {
                    this.chainCerts.put(new CertId(certificateArr[i].getPublicKey()), certificateArr[i]);
                    i += KEY_PUBLIC;
                }
            }
        }
    }

    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) {
        throw new RuntimeException("operation not supported");
    }

    public int engineSize() {
        Hashtable hashtable = new Hashtable();
        Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            String str = (String) keys2.nextElement();
            if (hashtable.get(str) == null) {
                hashtable.put(str, "key");
            }
        }
        return hashtable.size();
    }

    public void engineStore(OutputStream outputStream, char[] cArr) {
        doStore(outputStream, cArr, false);
    }

    public void engineStore(LoadStoreParameter loadStoreParameter) {
        if (loadStoreParameter == null) {
            throw new IllegalArgumentException("'param' arg cannot be null");
        } else if ((loadStoreParameter instanceof PKCS12StoreParameter) || (loadStoreParameter instanceof JDKPKCS12StoreParameter)) {
            char[] cArr;
            PKCS12StoreParameter pKCS12StoreParameter = loadStoreParameter instanceof PKCS12StoreParameter ? (PKCS12StoreParameter) loadStoreParameter : new PKCS12StoreParameter(((JDKPKCS12StoreParameter) loadStoreParameter).getOutputStream(), loadStoreParameter.getProtectionParameter(), ((JDKPKCS12StoreParameter) loadStoreParameter).isUseDEREncoding());
            ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
            if (protectionParameter == null) {
                cArr = null;
            } else if (protectionParameter instanceof PasswordProtection) {
                cArr = ((PasswordProtection) protectionParameter).getPassword();
            } else {
                throw new IllegalArgumentException("No support for protection parameter of type " + protectionParameter.getClass().getName());
            }
            doStore(pKCS12StoreParameter.getOutputStream(), cArr, pKCS12StoreParameter.isForDEREncoding());
        } else {
            throw new IllegalArgumentException("No support for 'param' of type " + loadStoreParameter.getClass().getName());
        }
    }

    public void setRandom(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    protected PrivateKey unwrapKey(AlgorithmIdentifier algorithmIdentifier, byte[] bArr, char[] cArr, boolean z) {
        ASN1ObjectIdentifier algorithm = algorithmIdentifier.getAlgorithm();
        try {
            if (algorithm.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
                PKCS12PBEParams instance = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
                KeySpec pBEKeySpec = new PBEKeySpec(cArr);
                SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(algorithm.getId());
                AlgorithmParameterSpec pBEParameterSpec = new PBEParameterSpec(instance.getIV(), instance.getIterations().intValue());
                Key generateSecret = createSecretKeyFactory.generateSecret(pBEKeySpec);
                ((BCPBEKey) generateSecret).setTryWrongPKCS12Zero(z);
                Cipher createCipher = this.helper.createCipher(algorithm.getId());
                createCipher.init(SEALED, generateSecret, pBEParameterSpec);
                return (PrivateKey) createCipher.unwrap(bArr, BuildConfig.FLAVOR, KEY_SECRET);
            } else if (algorithm.equals(PKCSObjectIdentifiers.id_PBES2)) {
                return (PrivateKey) createCipher(SEALED, cArr, algorithmIdentifier).unwrap(bArr, BuildConfig.FLAVOR, KEY_SECRET);
            } else {
                throw new IOException("exception unwrapping private key - cannot recognise: " + algorithm);
            }
        } catch (Exception e) {
            throw new IOException("exception unwrapping private key - " + e.toString());
        }
    }

    protected byte[] wrapKey(String str, Key key, PKCS12PBEParams pKCS12PBEParams, char[] cArr) {
        KeySpec pBEKeySpec = new PBEKeySpec(cArr);
        try {
            SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(str);
            AlgorithmParameterSpec pBEParameterSpec = new PBEParameterSpec(pKCS12PBEParams.getIV(), pKCS12PBEParams.getIterations().intValue());
            Cipher createCipher = this.helper.createCipher(str);
            createCipher.init(SECRET, createSecretKeyFactory.generateSecret(pBEKeySpec), pBEParameterSpec);
            return createCipher.wrap(key);
        } catch (Exception e) {
            throw new IOException("exception encrypting data - " + e.toString());
        }
    }
}
