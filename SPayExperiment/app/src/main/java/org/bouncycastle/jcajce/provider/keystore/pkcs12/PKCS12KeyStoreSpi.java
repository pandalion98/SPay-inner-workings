/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedInputStream
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.io.PrintStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.Key
 *  java.security.KeyStore
 *  java.security.KeyStore$LoadStoreParameter
 *  java.security.KeyStore$PasswordProtection
 *  java.security.KeyStore$ProtectionParameter
 *  java.security.KeyStoreException
 *  java.security.KeyStoreSpi
 *  java.security.Principal
 *  java.security.PrivateKey
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateFactory
 *  java.security.cert.X509Certificate
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.KeySpec
 *  java.util.Collections
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.HashMap
 *  java.util.Hashtable
 *  java.util.Map
 *  java.util.Vector
 *  javax.crypto.Cipher
 *  javax.crypto.Mac
 *  javax.crypto.SecretKey
 *  javax.crypto.SecretKeyFactory
 *  javax.crypto.spec.IvParameterSpec
 *  javax.crypto.spec.PBEKeySpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jcajce.provider.keystore.pkcs12;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore;
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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.BEROutputStream;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERBitString;
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
import org.bouncycastle.asn1.pkcs.EncryptionScheme;
import org.bouncycastle.asn1.pkcs.KeyDerivationFunc;
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
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jcajce.PKCS12StoreParameter;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.spec.GOST28147ParameterSpec;
import org.bouncycastle.jcajce.spec.PBKDF2KeySpec;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKPKCS12StoreParameter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

public class PKCS12KeyStoreSpi
extends KeyStoreSpi
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers,
BCKeyStore {
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
    private static final DefaultSecretKeyProvider keySizeProvider = new DefaultSecretKeyProvider();
    private ASN1ObjectIdentifier certAlgorithm;
    private CertificateFactory certFact;
    private IgnoresCaseHashtable certs = new IgnoresCaseHashtable();
    private Hashtable chainCerts = new Hashtable();
    private final JcaJceHelper helper = new BCJcaJceHelper();
    private ASN1ObjectIdentifier keyAlgorithm;
    private Hashtable keyCerts = new Hashtable();
    private IgnoresCaseHashtable keys = new IgnoresCaseHashtable();
    private Hashtable localIds = new Hashtable();
    protected SecureRandom random = new SecureRandom();

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public PKCS12KeyStoreSpi(Provider var1_1, ASN1ObjectIdentifier var2_2, ASN1ObjectIdentifier var3_3) {
        super();
        this.keyAlgorithm = var2_2;
        this.certAlgorithm = var3_3;
        if (var1_1 == null) ** GOTO lbl15
        try {
            this.certFact = CertificateFactory.getInstance((String)"X.509", (Provider)var1_1);
            return;
lbl15: // 1 sources:
            this.certFact = CertificateFactory.getInstance((String)"X.509");
            return;
        }
        catch (Exception var4_4) {
            throw new IllegalArgumentException("can't create cert factory - " + var4_4.toString());
        }
    }

    private byte[] calculatePbeMac(ASN1ObjectIdentifier aSN1ObjectIdentifier, byte[] arrby, int n, char[] arrc, boolean bl, byte[] arrby2) {
        SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(aSN1ObjectIdentifier.getId());
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(arrby, n);
        BCPBEKey bCPBEKey = (BCPBEKey)secretKeyFactory.generateSecret((KeySpec)new PBEKeySpec(arrc));
        bCPBEKey.setTryWrongPKCS12Zero(bl);
        Mac mac = this.helper.createMac(aSN1ObjectIdentifier.getId());
        mac.init((Key)bCPBEKey, (AlgorithmParameterSpec)pBEParameterSpec);
        mac.update(arrby2);
        return mac.doFinal();
    }

    /*
     * Enabled aggressive block sorting
     */
    private Cipher createCipher(int n, char[] arrc, AlgorithmIdentifier algorithmIdentifier) {
        PBES2Parameters pBES2Parameters = PBES2Parameters.getInstance(algorithmIdentifier.getParameters());
        PBKDF2Params pBKDF2Params = PBKDF2Params.getInstance(pBES2Parameters.getKeyDerivationFunc().getParameters());
        AlgorithmIdentifier algorithmIdentifier2 = AlgorithmIdentifier.getInstance(pBES2Parameters.getEncryptionScheme());
        SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(pBES2Parameters.getKeyDerivationFunc().getAlgorithm().getId());
        SecretKey secretKey = pBKDF2Params.isDefaultPrf() ? secretKeyFactory.generateSecret((KeySpec)new PBEKeySpec(arrc, pBKDF2Params.getSalt(), pBKDF2Params.getIterationCount().intValue(), keySizeProvider.getKeySize(algorithmIdentifier2))) : secretKeyFactory.generateSecret((KeySpec)new PBKDF2KeySpec(arrc, pBKDF2Params.getSalt(), pBKDF2Params.getIterationCount().intValue(), keySizeProvider.getKeySize(algorithmIdentifier2), pBKDF2Params.getPrf()));
        Cipher cipher = Cipher.getInstance((String)pBES2Parameters.getEncryptionScheme().getAlgorithm().getId());
        AlgorithmIdentifier.getInstance(pBES2Parameters.getEncryptionScheme());
        ASN1Encodable aSN1Encodable = pBES2Parameters.getEncryptionScheme().getParameters();
        if (aSN1Encodable instanceof ASN1OctetString) {
            cipher.init(n, (Key)secretKey, (AlgorithmParameterSpec)new IvParameterSpec(ASN1OctetString.getInstance(aSN1Encodable).getOctets()));
            return cipher;
        }
        GOST28147Parameters gOST28147Parameters = GOST28147Parameters.getInstance(aSN1Encodable);
        cipher.init(n, (Key)secretKey, (AlgorithmParameterSpec)new GOST28147ParameterSpec(gOST28147Parameters.getEncryptionParamSet(), gOST28147Parameters.getIV()));
        return cipher;
    }

    private SubjectKeyIdentifier createSubjectKeyId(PublicKey publicKey) {
        try {
            SubjectKeyIdentifier subjectKeyIdentifier = new SubjectKeyIdentifier(PKCS12KeyStoreSpi.getDigest(new SubjectPublicKeyInfo((ASN1Sequence)ASN1Primitive.fromByteArray(publicKey.getEncoded()))));
            return subjectKeyIdentifier;
        }
        catch (Exception exception) {
            throw new RuntimeException("error creating key");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void doStore(OutputStream outputStream, char[] arrc, boolean bl) {
        MacData macData;
        Enumeration enumeration;
        if (arrc == null) {
            throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumeration2 = this.keys.keys();
        while (enumeration2.hasMoreElements()) {
            boolean bl2;
            byte[] arrby = new byte[20];
            this.random.nextBytes(arrby);
            String string = (String)enumeration2.nextElement();
            PrivateKey privateKey = (PrivateKey)this.keys.get(string);
            PKCS12PBEParams pKCS12PBEParams = new PKCS12PBEParams(arrby, 1024);
            byte[] arrby2 = this.wrapKey(this.keyAlgorithm.getId(), (Key)privateKey, pKCS12PBEParams, arrc);
            EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(this.keyAlgorithm, pKCS12PBEParams.toASN1Primitive()), arrby2);
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            if (privateKey instanceof PKCS12BagAttributeCarrier) {
                PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier)privateKey;
                DERBMPString dERBMPString = (DERBMPString)pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_friendlyName);
                if (dERBMPString == null || !dERBMPString.getString().equals((Object)string)) {
                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(string));
                }
                if (pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_localKeyId) == null) {
                    Certificate certificate = this.engineGetCertificate(string);
                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_localKeyId, this.createSubjectKeyId(certificate.getPublicKey()));
                }
                Enumeration enumeration3 = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                bl2 = false;
                while (enumeration3.hasMoreElements()) {
                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration3.nextElement();
                    ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
                    aSN1EncodableVector3.add(aSN1ObjectIdentifier);
                    aSN1EncodableVector3.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier)));
                    bl2 = true;
                    aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
                }
            } else {
                bl2 = false;
            }
            if (!bl2) {
                ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
                Certificate certificate = this.engineGetCertificate(string);
                aSN1EncodableVector4.add(pkcs_9_at_localKeyId);
                aSN1EncodableVector4.add(new DERSet(this.createSubjectKeyId(certificate.getPublicKey())));
                aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
                ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
                aSN1EncodableVector5.add(pkcs_9_at_friendlyName);
                aSN1EncodableVector5.add(new DERSet(new DERBMPString(string)));
                aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector5));
            }
            aSN1EncodableVector.add(new SafeBag(pkcs8ShroudedKeyBag, encryptedPrivateKeyInfo.toASN1Primitive(), new DERSet(aSN1EncodableVector2)));
        }
        BEROctetString bEROctetString = new BEROctetString(new DERSequence(aSN1EncodableVector).getEncoded("DER"));
        byte[] arrby = new byte[20];
        this.random.nextBytes(arrby);
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        PKCS12PBEParams pKCS12PBEParams = new PKCS12PBEParams(arrby, 1024);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.certAlgorithm, pKCS12PBEParams.toASN1Primitive());
        Hashtable hashtable = new Hashtable();
        Enumeration enumeration4 = this.keys.keys();
        do {
            boolean bl3;
            String string;
            CertBag certBag;
            Certificate certificate;
            ASN1EncodableVector aSN1EncodableVector7;
            block33 : {
                boolean bl4;
                block32 : {
                    if (enumeration4.hasMoreElements()) {
                        try {
                            string = (String)enumeration4.nextElement();
                            certificate = this.engineGetCertificate(string);
                            certBag = new CertBag(x509Certificate, new DEROctetString(certificate.getEncoded()));
                            aSN1EncodableVector7 = new ASN1EncodableVector();
                            if (certificate instanceof PKCS12BagAttributeCarrier) {
                                PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier)certificate;
                                DERBMPString dERBMPString = (DERBMPString)pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_friendlyName);
                                if (dERBMPString == null || !dERBMPString.getString().equals((Object)string)) {
                                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(string));
                                }
                                if (pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_localKeyId) == null) {
                                    pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_localKeyId, this.createSubjectKeyId(certificate.getPublicKey()));
                                }
                                Enumeration enumeration5 = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                                bl4 = false;
                                while (enumeration5.hasMoreElements()) {
                                    ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration5.nextElement();
                                    ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
                                    aSN1EncodableVector8.add(aSN1ObjectIdentifier);
                                    aSN1EncodableVector8.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier)));
                                    aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector8));
                                    bl4 = true;
                                }
                            }
                            break block32;
                        }
                        catch (CertificateEncodingException certificateEncodingException) {
                            throw new IOException("Error encoding certificate: " + certificateEncodingException.toString());
                        }
                    }
                    enumeration = this.certs.keys();
                    break;
                }
                bl3 = false;
                break block33;
                bl3 = bl4;
            }
            if (!bl3) {
                ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
                aSN1EncodableVector9.add(pkcs_9_at_localKeyId);
                aSN1EncodableVector9.add(new DERSet(this.createSubjectKeyId(certificate.getPublicKey())));
                aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector9));
                ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
                aSN1EncodableVector10.add(pkcs_9_at_friendlyName);
                aSN1EncodableVector10.add(new DERSet(new DERBMPString(string)));
                aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector10));
            }
            aSN1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag.toASN1Primitive(), new DERSet(aSN1EncodableVector7)));
            hashtable.put((Object)certificate, (Object)certificate);
        } while (true);
        while (enumeration.hasMoreElements()) {
            try {
                String string = (String)enumeration.nextElement();
                Certificate certificate = (Certificate)this.certs.get(string);
                if (this.keys.get(string) != null) continue;
                CertBag certBag = new CertBag(x509Certificate, new DEROctetString(certificate.getEncoded()));
                ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
                boolean bl5 = certificate instanceof PKCS12BagAttributeCarrier;
                boolean bl6 = false;
                if (bl5) {
                    PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier)certificate;
                    DERBMPString dERBMPString = (DERBMPString)pKCS12BagAttributeCarrier.getBagAttribute(pkcs_9_at_friendlyName);
                    if (dERBMPString == null || !dERBMPString.getString().equals((Object)string)) {
                        pKCS12BagAttributeCarrier.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(string));
                    }
                    Enumeration enumeration6 = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                    while (enumeration6.hasMoreElements()) {
                        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration6.nextElement();
                        if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) continue;
                        ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
                        aSN1EncodableVector12.add(aSN1ObjectIdentifier);
                        aSN1EncodableVector12.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier)));
                        aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                        bl6 = true;
                    }
                }
                if (!bl6) {
                    ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
                    aSN1EncodableVector13.add(pkcs_9_at_friendlyName);
                    aSN1EncodableVector13.add(new DERSet(new DERBMPString(string)));
                    aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                }
                aSN1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag.toASN1Primitive(), new DERSet(aSN1EncodableVector11)));
                hashtable.put((Object)certificate, (Object)certificate);
            }
            catch (CertificateEncodingException certificateEncodingException) {
                throw new IOException("Error encoding certificate: " + certificateEncodingException.toString());
            }
        }
        Enumeration enumeration7 = this.chainCerts.keys();
        while (enumeration7.hasMoreElements()) {
            try {
                CertId certId = (CertId)enumeration7.nextElement();
                Certificate certificate = (Certificate)this.chainCerts.get((Object)certId);
                if (hashtable.get((Object)certificate) != null) continue;
                CertBag certBag = new CertBag(x509Certificate, new DEROctetString(certificate.getEncoded()));
                ASN1EncodableVector aSN1EncodableVector14 = new ASN1EncodableVector();
                if (certificate instanceof PKCS12BagAttributeCarrier) {
                    PKCS12BagAttributeCarrier pKCS12BagAttributeCarrier = (PKCS12BagAttributeCarrier)certificate;
                    Enumeration enumeration8 = pKCS12BagAttributeCarrier.getBagAttributeKeys();
                    while (enumeration8.hasMoreElements()) {
                        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration8.nextElement();
                        if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) continue;
                        ASN1EncodableVector aSN1EncodableVector15 = new ASN1EncodableVector();
                        aSN1EncodableVector15.add(aSN1ObjectIdentifier);
                        aSN1EncodableVector15.add(new DERSet(pKCS12BagAttributeCarrier.getBagAttribute(aSN1ObjectIdentifier)));
                        aSN1EncodableVector14.add(new DERSequence(aSN1EncodableVector15));
                    }
                }
                aSN1EncodableVector6.add(new SafeBag(PKCS12KeyStoreSpi.certBag, certBag.toASN1Primitive(), new DERSet(aSN1EncodableVector14)));
            }
            catch (CertificateEncodingException certificateEncodingException) {
                throw new IOException("Error encoding certificate: " + certificateEncodingException.toString());
            }
        }
        byte[] arrby3 = this.cryptData(true, algorithmIdentifier, arrc, false, new DERSequence(aSN1EncodableVector6).getEncoded("DER"));
        EncryptedData encryptedData = new EncryptedData(data, algorithmIdentifier, new BEROctetString(arrby3));
        ContentInfo[] arrcontentInfo = new ContentInfo[]{new ContentInfo(data, bEROctetString), new ContentInfo(PKCS12KeyStoreSpi.encryptedData, encryptedData.toASN1Primitive())};
        AuthenticatedSafe authenticatedSafe = new AuthenticatedSafe(arrcontentInfo);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream = bl ? new DEROutputStream((OutputStream)byteArrayOutputStream) : new BEROutputStream((OutputStream)byteArrayOutputStream);
        dEROutputStream.writeObject(authenticatedSafe);
        byte[] arrby4 = byteArrayOutputStream.toByteArray();
        ContentInfo contentInfo = new ContentInfo(data, new BEROctetString(arrby4));
        byte[] arrby5 = new byte[20];
        this.random.nextBytes(arrby5);
        byte[] arrby6 = ((ASN1OctetString)contentInfo.getContent()).getOctets();
        try {
            byte[] arrby7 = this.calculatePbeMac(id_SHA1, arrby5, 1024, arrc, false, arrby6);
            macData = new MacData(new DigestInfo(new AlgorithmIdentifier(id_SHA1, DERNull.INSTANCE), arrby7), arrby5, 1024);
        }
        catch (Exception exception) {
            throw new IOException("error constructing MAC: " + exception.toString());
        }
        Pfx pfx = new Pfx(contentInfo, macData);
        DEROutputStream dEROutputStream2 = bl ? new DEROutputStream(outputStream) : new BEROutputStream(outputStream);
        dEROutputStream2.writeObject(pfx);
    }

    private static byte[] getDigest(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        SHA1Digest sHA1Digest = new SHA1Digest();
        byte[] arrby = new byte[sHA1Digest.getDigestSize()];
        byte[] arrby2 = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        sHA1Digest.update(arrby2, 0, arrby2.length);
        sHA1Digest.doFinal(arrby, 0);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected byte[] cryptData(boolean bl, AlgorithmIdentifier algorithmIdentifier, char[] arrc, boolean bl2, byte[] arrby) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = algorithmIdentifier.getAlgorithm();
        int n = bl ? 1 : 2;
        if (aSN1ObjectIdentifier.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
            PKCS12PBEParams pKCS12PBEParams = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
            PBEKeySpec pBEKeySpec = new PBEKeySpec(arrc);
            try {
                SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(aSN1ObjectIdentifier.getId());
                PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(pKCS12PBEParams.getIV(), pKCS12PBEParams.getIterations().intValue());
                BCPBEKey bCPBEKey = (BCPBEKey)secretKeyFactory.generateSecret((KeySpec)pBEKeySpec);
                bCPBEKey.setTryWrongPKCS12Zero(bl2);
                Cipher cipher = this.helper.createCipher(aSN1ObjectIdentifier.getId());
                cipher.init(n, (Key)bCPBEKey, (AlgorithmParameterSpec)pBEParameterSpec);
                return cipher.doFinal(arrby);
            }
            catch (Exception exception) {
                throw new IOException("exception decrypting data - " + exception.toString());
            }
        }
        if (!aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.id_PBES2)) {
            throw new IOException("unknown PBE algorithm: " + aSN1ObjectIdentifier);
        }
        try {
            return this.createCipher(n, arrc, algorithmIdentifier).doFinal(arrby);
        }
        catch (Exception exception) {
            throw new IOException("exception decrypting data - " + exception.toString());
        }
    }

    public Enumeration engineAliases() {
        Hashtable hashtable = new Hashtable();
        Enumeration enumeration = this.certs.keys();
        while (enumeration.hasMoreElements()) {
            hashtable.put(enumeration.nextElement(), (Object)"cert");
        }
        Enumeration enumeration2 = this.keys.keys();
        while (enumeration2.hasMoreElements()) {
            String string = (String)enumeration2.nextElement();
            if (hashtable.get((Object)string) != null) continue;
            hashtable.put((Object)string, (Object)"key");
        }
        return hashtable.keys();
    }

    public boolean engineContainsAlias(String string) {
        return this.certs.get(string) != null || this.keys.get(string) != null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineDeleteEntry(String string) {
        Certificate certificate;
        String string2;
        Key key = (Key)this.keys.remove(string);
        Certificate certificate2 = (Certificate)this.certs.remove(string);
        if (certificate2 != null) {
            this.chainCerts.remove((Object)new CertId(certificate2.getPublicKey()));
        }
        if (key != null && (certificate = (string2 = (String)this.localIds.remove((Object)string)) != null ? (Certificate)this.keyCerts.remove((Object)string2) : certificate2) != null) {
            this.chainCerts.remove((Object)new CertId(certificate.getPublicKey()));
        }
    }

    public Certificate engineGetCertificate(String string) {
        block5 : {
            Certificate certificate;
            block4 : {
                if (string == null) {
                    throw new IllegalArgumentException("null alias passed to getCertificate.");
                }
                certificate = (Certificate)this.certs.get(string);
                if (certificate != null) break block4;
                String string2 = (String)this.localIds.get((Object)string);
                if (string2 == null) break block5;
                certificate = (Certificate)this.keyCerts.get((Object)string2);
            }
            return certificate;
        }
        return (Certificate)this.keyCerts.get((Object)string);
    }

    public String engineGetCertificateAlias(Certificate certificate) {
        Enumeration enumeration = this.certs.elements();
        Enumeration enumeration2 = this.certs.keys();
        while (enumeration.hasMoreElements()) {
            Certificate certificate2 = (Certificate)enumeration.nextElement();
            String string = (String)enumeration2.nextElement();
            if (!certificate2.equals((Object)certificate)) continue;
            return string;
        }
        Enumeration enumeration3 = this.keyCerts.elements();
        Enumeration enumeration4 = this.keyCerts.keys();
        while (enumeration3.hasMoreElements()) {
            Certificate certificate3 = (Certificate)enumeration3.nextElement();
            String string = (String)enumeration4.nextElement();
            if (!certificate3.equals((Object)certificate)) continue;
            return string;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Certificate[] engineGetCertificateChain(String string) {
        Certificate[] arrcertificate;
        block11 : {
            if (string == null) {
                throw new IllegalArgumentException("null alias passed to getCertificateChain.");
            }
            boolean bl = this.engineIsKeyEntry(string);
            arrcertificate = null;
            if (!bl) break block11;
            Certificate certificate = this.engineGetCertificate(string);
            arrcertificate = null;
            if (certificate == null) break block11;
            Vector vector = new Vector();
            do {
                Certificate certificate2;
                block10 : {
                    Certificate certificate3;
                    X509Certificate x509Certificate;
                    Principal principal;
                    block14 : {
                        block13 : {
                            block12 : {
                                Certificate certificate4;
                                if (certificate == null) break block12;
                                x509Certificate = (X509Certificate)certificate;
                                byte[] arrby = x509Certificate.getExtensionValue(Extension.authorityKeyIdentifier.getId());
                                if (arrby == null) break block13;
                                try {
                                    AuthorityKeyIdentifier authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(arrby).readObject()).getOctets()).readObject());
                                    certificate4 = authorityKeyIdentifier.getKeyIdentifier() != null ? (Certificate)this.chainCerts.get((Object)new CertId(authorityKeyIdentifier.getKeyIdentifier())) : null;
                                }
                                catch (IOException iOException) {
                                    throw new RuntimeException(iOException.toString());
                                }
                                certificate3 = certificate4;
                                break block14;
                            }
                            arrcertificate = new Certificate[vector.size()];
                            break;
                        }
                        certificate3 = null;
                    }
                    if (certificate3 == null && !(principal = x509Certificate.getIssuerDN()).equals((Object)x509Certificate.getSubjectDN())) {
                        Enumeration enumeration = this.chainCerts.keys();
                        while (enumeration.hasMoreElements()) {
                            X509Certificate x509Certificate2 = (X509Certificate)this.chainCerts.get(enumeration.nextElement());
                            if (!x509Certificate2.getSubjectDN().equals((Object)principal)) continue;
                            try {
                                x509Certificate.verify(x509Certificate2.getPublicKey());
                                certificate2 = x509Certificate2;
                                break block10;
                            }
                            catch (Exception exception) {
                            }
                        }
                    }
                    certificate2 = certificate3;
                }
                vector.addElement((Object)certificate);
                if (certificate2 == certificate) {
                    certificate2 = null;
                }
                certificate = certificate2;
            } while (true);
            for (int i = 0; i != arrcertificate.length; ++i) {
                arrcertificate[i] = (Certificate)vector.elementAt(i);
            }
        }
        return arrcertificate;
    }

    public Date engineGetCreationDate(String string) {
        if (string == null) {
            throw new NullPointerException("alias == null");
        }
        if (this.keys.get(string) == null && this.certs.get(string) == null) {
            return null;
        }
        return new Date();
    }

    public Key engineGetKey(String string, char[] arrc) {
        if (string == null) {
            throw new IllegalArgumentException("null alias passed to getKey.");
        }
        return (Key)this.keys.get(string);
    }

    public boolean engineIsCertificateEntry(String string) {
        return this.certs.get(string) != null && this.keys.get(string) == null;
    }

    public boolean engineIsKeyEntry(String string) {
        return this.keys.get(string) != null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void engineLoad(InputStream var1_1, char[] var2_2) {
        block65 : {
            block66 : {
                block70 : {
                    block69 : {
                        block67 : {
                            block68 : {
                                block64 : {
                                    if (var1_1 == null) {
                                        return;
                                    }
                                    if (var2_2 == null) {
                                        throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
                                    }
                                    var3_3 = new BufferedInputStream(var1_1);
                                    var3_3.mark(10);
                                    if (var3_3.read() != 48) {
                                        throw new IOException("stream does not represent a PKCS12 key store");
                                    }
                                    var3_3.reset();
                                    var4_4 = Pfx.getInstance((ASN1Sequence)new ASN1InputStream((InputStream)var3_3).readObject());
                                    var5_5 = var4_4.getAuthSafe();
                                    var6_6 = new Vector();
                                    if (var4_4.getMacData() == null) break block67;
                                    var93_7 = var4_4.getMacData();
                                    var94_8 = var93_7.getMac();
                                    var95_9 = var94_8.getAlgorithmId();
                                    var96_10 = var93_7.getSalt();
                                    var97_11 = var93_7.getIterationCount().intValue();
                                    var98_12 = ((ASN1OctetString)var5_5.getContent()).getOctets();
                                    try {
                                        var101_13 = this.calculatePbeMac(var95_9.getAlgorithm(), var96_10, var97_11, var2_2, false, var98_12);
                                        var102_14 = var94_8.getDigest();
                                        if (Arrays.constantTimeAreEqual(var101_13, var102_14)) break block64;
                                        if (var2_2.length > 0) {
                                            throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                                        }
                                        if (!Arrays.constantTimeAreEqual(this.calculatePbeMac(var95_9.getAlgorithm(), var96_10, var97_11, var2_2, true, var98_12), var102_14)) {
                                            throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                                        }
                                    }
                                    catch (IOException var100_15) {
                                        throw var100_15;
                                    }
                                    catch (Exception var99_16) {
                                        throw new IOException("error constructing MAC: " + var99_16.toString());
                                    }
                                    var103_17 = true;
                                    break block68;
                                }
                                var103_17 = false;
                            }
                            var7_18 = var103_17;
                            break block69;
                        }
                        var7_18 = false;
                    }
                    this.keys = new IgnoresCaseHashtable();
                    this.localIds = new Hashtable();
                    if (var5_5.getContentType().equals(PKCS12KeyStoreSpi.data)) break block70;
                    var8_21 = false;
                    break block65;
                }
                var32_19 = AuthenticatedSafe.getInstance(new ASN1InputStream(((ASN1OctetString)var5_5.getContent()).getOctets()).readObject()).getContentInfo();
                var33_20 = 0;
                var8_21 = false;
                block6 : do {
                    block72 : {
                        block71 : {
                            if (var33_20 == var32_19.length) break block65;
                            if (var32_19[var33_20].getContentType().equals(PKCS12KeyStoreSpi.data)) break block71;
                            if (var32_19[var33_20].getContentType().equals(PKCS12KeyStoreSpi.encryptedData)) break;
                            System.out.println("extra " + var32_19[var33_20].getContentType().getId());
                            System.out.println("extra " + ASN1Dump.dumpAsString(var32_19[var33_20].getContent()));
                            var34_22 = var8_21;
                            break block72;
                        }
                        var71_57 = (ASN1Sequence)new ASN1InputStream(((ASN1OctetString)var32_19[var33_20].getContent()).getOctets()).readObject();
                        var72_58 = var8_21;
                        var73_59 = 0;
                        do {
                            block77 : {
                                block75 : {
                                    block76 : {
                                        block73 : {
                                            block74 : {
                                                if (var73_59 == var71_57.size()) break block73;
                                                var74_60 = SafeBag.getInstance(var71_57.getObjectAt(var73_59));
                                                if (!var74_60.getBagId().equals(PKCS12KeyStoreSpi.pkcs8ShroudedKeyBag)) break block74;
                                                var76_62 = EncryptedPrivateKeyInfo.getInstance(var74_60.getBagValue());
                                                var77_63 = this.unwrapKey(var76_62.getEncryptionAlgorithm(), var76_62.getEncryptedData(), var2_2, var7_18);
                                                var78_64 = (PKCS12BagAttributeCarrier)var77_63;
                                                var79_65 = var74_60.getBagAttributes();
                                                var80_66 = null;
                                                var81_67 = null;
                                                if (var79_65 == null) break block75;
                                                var84_69 = var74_60.getBagAttributes().getObjects();
                                                break block76;
                                            }
                                            if (var74_60.getBagId().equals(PKCS12KeyStoreSpi.certBag)) {
                                                var6_6.addElement((Object)var74_60);
                                                var75_61 = var72_58;
                                            } else {
                                                System.out.println("extra in data " + var74_60.getBagId());
                                                System.out.println(ASN1Dump.dumpAsString(var74_60));
                                                var75_61 = var72_58;
                                            }
                                            break block77;
                                        }
                                        var34_22 = var72_58;
                                        break;
                                    }
                                    while (var84_69.hasMoreElements()) {
                                        var85_70 = (ASN1Sequence)var84_69.nextElement();
                                        var86_71 = (ASN1ObjectIdentifier)var85_70.getObjectAt(0);
                                        var87_72 = (ASN1Set)var85_70.getObjectAt(1);
                                        if (var87_72.size() > 0) {
                                            var88_73 = (ASN1Primitive)var87_72.getObjectAt(0);
                                            var92_77 = var78_64.getBagAttribute(var86_71);
                                            if (var92_77 != null) {
                                                if (!var92_77.toASN1Primitive().equals(var88_73)) {
                                                    throw new IOException("attempt to add existing attribute with different value");
                                                }
                                            } else {
                                                var78_64.setBagAttribute(var86_71, var88_73);
                                            }
                                        } else {
                                            var88_73 = null;
                                        }
                                        if (var86_71.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                                            var91_76 = ((DERBMPString)var88_73).getString();
                                            this.keys.put(var91_76, (Object)var77_63);
                                            var90_75 = var91_76;
                                            var89_74 = var80_66;
                                        } else if (var86_71.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                                            var89_74 = (ASN1OctetString)var88_73;
                                            var90_75 = var81_67;
                                        } else {
                                            var89_74 = var80_66;
                                            var90_75 = var81_67;
                                        }
                                        var80_66 = var89_74;
                                        var81_67 = var90_75;
                                    }
                                }
                                if (var80_66 != null) {
                                    var82_68 = new String(Hex.encode(var80_66.getOctets()));
                                    if (var81_67 == null) {
                                        this.keys.put(var82_68, (Object)var77_63);
                                    } else {
                                        this.localIds.put(var81_67, (Object)var82_68);
                                    }
                                } else {
                                    var72_58 = true;
                                    this.keys.put("unmarked", (Object)var77_63);
                                }
                                var75_61 = var72_58;
                            }
                            ++var73_59;
                            var72_58 = var75_61;
                        } while (true);
                    }
lbl135: // 2 sources:
                    do {
                        ++var33_20;
                        var8_21 = var34_22;
                        continue block6;
                        break;
                    } while (true);
                    break;
                } while (true);
                var35_23 = EncryptedData.getInstance(var32_19[var33_20].getContent());
                var36_24 = (ASN1Sequence)ASN1Primitive.fromByteArray(this.cryptData(false, var35_23.getEncryptionAlgorithm(), var2_2, var7_18, var35_23.getContent().getOctets()));
                var37_25 = 0;
                block10 : do {
                    if (var37_25 != var36_24.size()) {
                        var38_26 = SafeBag.getInstance(var36_24.getObjectAt(var37_25));
                        if (var38_26.getBagId().equals(PKCS12KeyStoreSpi.certBag)) {
                            var6_6.addElement((Object)var38_26);
                        } else {
                            if (var38_26.getBagId().equals(PKCS12KeyStoreSpi.pkcs8ShroudedKeyBag)) break;
                            if (var38_26.getBagId().equals(PKCS12KeyStoreSpi.keyBag)) break block66;
                            System.out.println("extra in encryptedData " + var38_26.getBagId());
                            System.out.println(ASN1Dump.dumpAsString(var38_26));
                        }
                    } else {
                        var34_22 = var8_21;
                        ** continue;
                    }
lbl155: // 6 sources:
                    do {
                        ++var37_25;
                        continue block10;
                        break;
                    } while (true);
                    break;
                } while (true);
                var55_42 = EncryptedPrivateKeyInfo.getInstance(var38_26.getBagValue());
                var56_43 = this.unwrapKey(var55_42.getEncryptionAlgorithm(), var55_42.getEncryptedData(), var2_2, var7_18);
                var57_44 = (PKCS12BagAttributeCarrier)var56_43;
                var58_45 = null;
                var59_46 = null;
                var60_47 = var38_26.getBagAttributes().getObjects();
                do {
                    block80 : {
                        block79 : {
                            block81 : {
                                block78 : {
                                    if (!var60_47.hasMoreElements()) break block78;
                                    var63_49 = (ASN1Sequence)var60_47.nextElement();
                                    var64_50 = (ASN1ObjectIdentifier)var63_49.getObjectAt(0);
                                    var65_51 = (ASN1Set)var63_49.getObjectAt(1);
                                    if (var65_51.size() <= 0) break block79;
                                    var66_52 = (ASN1Primitive)var65_51.getObjectAt(0);
                                    var70_56 = var57_44.getBagAttribute(var64_50);
                                    if (var70_56 != null) {
                                        if (!var70_56.toASN1Primitive().equals(var66_52)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    } else {
                                        var57_44.setBagAttribute(var64_50, var66_52);
                                    }
                                    break block80;
                                }
                                var61_48 = new String(Hex.encode(var59_46.getOctets()));
                                if (var58_45 != null) break block81;
                                this.keys.put(var61_48, (Object)var56_43);
                                ** GOTO lbl155
                            }
                            this.localIds.put(var58_45, (Object)var61_48);
                            ** GOTO lbl155
                        }
                        var66_52 = null;
                    }
                    if (var64_50.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                        var69_55 = ((DERBMPString)var66_52).getString();
                        this.keys.put(var69_55, (Object)var56_43);
                        var68_54 = var69_55;
                        var67_53 = var59_46;
                    } else if (var64_50.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                        var67_53 = (ASN1OctetString)var66_52;
                        var68_54 = var58_45;
                    } else {
                        var67_53 = var59_46;
                        var68_54 = var58_45;
                    }
                    var59_46 = var67_53;
                    var58_45 = var68_54;
                } while (true);
            }
            var39_27 = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(var38_26.getBagValue()));
            var40_28 = (PKCS12BagAttributeCarrier)var39_27;
            var41_29 = null;
            var42_30 = null;
            var43_31 = var38_26.getBagAttributes().getObjects();
            do {
                block85 : {
                    block83 : {
                        block86 : {
                            block82 : {
                                block84 : {
                                    if (!var43_31.hasMoreElements()) break block82;
                                    var46_33 = ASN1Sequence.getInstance(var43_31.nextElement());
                                    var47_34 = ASN1ObjectIdentifier.getInstance(var46_33.getObjectAt(0));
                                    var48_35 = ASN1Set.getInstance(var46_33.getObjectAt(1));
                                    if (var48_35.size() <= 0) break block83;
                                    var51_38 = (ASN1Primitive)var48_35.getObjectAt(0);
                                    var52_39 = var40_28.getBagAttribute(var47_34);
                                    if (var52_39 != null) {
                                        if (!var52_39.toASN1Primitive().equals(var51_38)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    } else {
                                        var40_28.setBagAttribute(var47_34, var51_38);
                                    }
                                    if (!var47_34.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) break block84;
                                    var53_40 = ((DERBMPString)var51_38).getString();
                                    this.keys.put(var53_40, (Object)var39_27);
                                    var54_41 = var42_30;
                                    var50_37 = var53_40;
                                    var49_36 = var54_41;
                                    break block85;
                                }
                                if (!var47_34.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) break block83;
                                var49_36 = (ASN1OctetString)var51_38;
                                var50_37 = var41_29;
                                break block85;
                            }
                            var44_32 = new String(Hex.encode(var42_30.getOctets()));
                            if (var41_29 != null) break block86;
                            this.keys.put(var44_32, (Object)var39_27);
                            ** GOTO lbl155
                        }
                        this.localIds.put(var41_29, (Object)var44_32);
                        ** continue;
                    }
                    var49_36 = var42_30;
                    var50_37 = var41_29;
                }
                var41_29 = var50_37;
                var42_30 = var49_36;
            } while (true);
        }
        this.certs = new IgnoresCaseHashtable();
        this.chainCerts = new Hashtable();
        this.keyCerts = new Hashtable();
        var9_78 = 0;
        while (var9_78 != var6_6.size()) {
            block87 : {
                var10_79 = (SafeBag)var6_6.elementAt(var9_78);
                var11_80 = CertBag.getInstance(var10_79.getBagValue());
                if (!var11_80.getCertId().equals(PKCS12KeyStoreSpi.x509Certificate)) {
                    throw new RuntimeException("Unsupported certificate type: " + var11_80.getCertId());
                }
                try {
                    var12_81 = new ByteArrayInputStream(((ASN1OctetString)var11_80.getCertValue()).getOctets());
                    var14_82 = this.certFact.generateCertificate((InputStream)var12_81);
                }
                catch (Exception var13_97) {
                    throw new RuntimeException(var13_97.toString());
                }
                var15_83 = var10_79.getBagAttributes();
                var16_84 = null;
                var17_85 = null;
                if (var15_83 == null) break block87;
                var23_88 = var10_79.getBagAttributes().getObjects();
                while (var23_88.hasMoreElements()) {
                    var24_89 = ASN1Sequence.getInstance(var23_88.nextElement());
                    var25_90 = ASN1ObjectIdentifier.getInstance(var24_89.getObjectAt(0));
                    var26_91 = ASN1Set.getInstance(var24_89.getObjectAt(1));
                    if (var26_91.size() <= 0) ** GOTO lbl-1000
                    var29_94 = (ASN1Primitive)var26_91.getObjectAt(0);
                    if (var14_82 instanceof PKCS12BagAttributeCarrier) {
                        var30_95 = (PKCS12BagAttributeCarrier)var14_82;
                        var31_96 = var30_95.getBagAttribute(var25_90);
                        if (var31_96 != null) {
                            if (!var31_96.toASN1Primitive().equals(var29_94)) {
                                throw new IOException("attempt to add existing attribute with different value");
                            }
                        } else {
                            var30_95.setBagAttribute(var25_90, var29_94);
                        }
                    }
                    if (var25_90.equals(PKCS12KeyStoreSpi.pkcs_9_at_friendlyName)) {
                        var27_92 = ((DERBMPString)var29_94).getString();
                        var28_93 = var17_85;
                    } else if (var25_90.equals(PKCS12KeyStoreSpi.pkcs_9_at_localKeyId)) {
                        var28_93 = (ASN1OctetString)var29_94;
                        var27_92 = var16_84;
                    } else lbl-1000: // 2 sources:
                    {
                        var27_92 = var16_84;
                        var28_93 = var17_85;
                    }
                    var16_84 = var27_92;
                    var17_85 = var28_93;
                }
            }
            this.chainCerts.put((Object)new CertId(var14_82.getPublicKey()), (Object)var14_82);
            if (var8_21) {
                if (this.keyCerts.isEmpty()) {
                    var21_87 = new String(Hex.encode(this.createSubjectKeyId(var14_82.getPublicKey()).getKeyIdentifier()));
                    this.keyCerts.put((Object)var21_87, (Object)var14_82);
                    this.keys.put(var21_87, this.keys.remove("unmarked"));
                }
            } else {
                if (var17_85 != null) {
                    var19_86 = new String(Hex.encode(var17_85.getOctets()));
                    this.keyCerts.put((Object)var19_86, (Object)var14_82);
                }
                if (var16_84 != null) {
                    this.certs.put(var16_84, (Object)var14_82);
                }
            }
            ++var9_78;
        }
    }

    public void engineSetCertificateEntry(String string, Certificate certificate) {
        if (this.keys.get(string) != null) {
            throw new KeyStoreException("There is a key entry with the name " + string + ".");
        }
        this.certs.put(string, (Object)certificate);
        this.chainCerts.put((Object)new CertId(certificate.getPublicKey()), (Object)certificate);
    }

    public void engineSetKeyEntry(String string, Key key, char[] arrc, Certificate[] arrcertificate) {
        int n = 0;
        if (!(key instanceof PrivateKey)) {
            throw new KeyStoreException("PKCS12 does not support non-PrivateKeys");
        }
        if (key instanceof PrivateKey && arrcertificate == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        if (this.keys.get(string) != null) {
            this.engineDeleteEntry(string);
        }
        this.keys.put(string, (Object)key);
        if (arrcertificate != null) {
            this.certs.put(string, (Object)arrcertificate[0]);
            while (n != arrcertificate.length) {
                this.chainCerts.put((Object)new CertId(arrcertificate[n].getPublicKey()), (Object)arrcertificate[n]);
                ++n;
            }
        }
    }

    public void engineSetKeyEntry(String string, byte[] arrby, Certificate[] arrcertificate) {
        throw new RuntimeException("operation not supported");
    }

    public int engineSize() {
        Hashtable hashtable = new Hashtable();
        Enumeration enumeration = this.certs.keys();
        while (enumeration.hasMoreElements()) {
            hashtable.put(enumeration.nextElement(), (Object)"cert");
        }
        Enumeration enumeration2 = this.keys.keys();
        while (enumeration2.hasMoreElements()) {
            String string = (String)enumeration2.nextElement();
            if (hashtable.get((Object)string) != null) continue;
            hashtable.put((Object)string, (Object)"key");
        }
        return hashtable.size();
    }

    public void engineStore(OutputStream outputStream, char[] arrc) {
        this.doStore(outputStream, arrc, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) {
        char[] arrc;
        if (loadStoreParameter == null) {
            throw new IllegalArgumentException("'param' arg cannot be null");
        }
        if (!(loadStoreParameter instanceof PKCS12StoreParameter) && !(loadStoreParameter instanceof JDKPKCS12StoreParameter)) {
            throw new IllegalArgumentException("No support for 'param' of type " + loadStoreParameter.getClass().getName());
        }
        PKCS12StoreParameter pKCS12StoreParameter = loadStoreParameter instanceof PKCS12StoreParameter ? (PKCS12StoreParameter)loadStoreParameter : new PKCS12StoreParameter(((JDKPKCS12StoreParameter)loadStoreParameter).getOutputStream(), loadStoreParameter.getProtectionParameter(), ((JDKPKCS12StoreParameter)loadStoreParameter).isUseDEREncoding());
        KeyStore.ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
        if (protectionParameter == null) {
            arrc = null;
        } else {
            if (!(protectionParameter instanceof KeyStore.PasswordProtection)) {
                throw new IllegalArgumentException("No support for protection parameter of type " + protectionParameter.getClass().getName());
            }
            arrc = ((KeyStore.PasswordProtection)protectionParameter).getPassword();
        }
        this.doStore(pKCS12StoreParameter.getOutputStream(), arrc, pKCS12StoreParameter.isForDEREncoding());
    }

    @Override
    public void setRandom(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    protected PrivateKey unwrapKey(AlgorithmIdentifier algorithmIdentifier, byte[] arrby, char[] arrc, boolean bl) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = algorithmIdentifier.getAlgorithm();
        try {
            if (aSN1ObjectIdentifier.on(PKCSObjectIdentifiers.pkcs_12PbeIds)) {
                PKCS12PBEParams pKCS12PBEParams = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());
                PBEKeySpec pBEKeySpec = new PBEKeySpec(arrc);
                SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(aSN1ObjectIdentifier.getId());
                PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(pKCS12PBEParams.getIV(), pKCS12PBEParams.getIterations().intValue());
                SecretKey secretKey = secretKeyFactory.generateSecret((KeySpec)pBEKeySpec);
                ((BCPBEKey)secretKey).setTryWrongPKCS12Zero(bl);
                Cipher cipher = this.helper.createCipher(aSN1ObjectIdentifier.getId());
                cipher.init(4, (Key)secretKey, (AlgorithmParameterSpec)pBEParameterSpec);
                return (PrivateKey)cipher.unwrap(arrby, "", 2);
            }
            if (aSN1ObjectIdentifier.equals(PKCSObjectIdentifiers.id_PBES2)) {
                PrivateKey privateKey = (PrivateKey)this.createCipher(4, arrc, algorithmIdentifier).unwrap(arrby, "", 2);
                return privateKey;
            }
        }
        catch (Exception exception) {
            throw new IOException("exception unwrapping private key - " + exception.toString());
        }
        throw new IOException("exception unwrapping private key - cannot recognise: " + aSN1ObjectIdentifier);
    }

    protected byte[] wrapKey(String string, Key key, PKCS12PBEParams pKCS12PBEParams, char[] arrc) {
        PBEKeySpec pBEKeySpec = new PBEKeySpec(arrc);
        try {
            SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(string);
            PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(pKCS12PBEParams.getIV(), pKCS12PBEParams.getIterations().intValue());
            Cipher cipher = this.helper.createCipher(string);
            cipher.init(3, (Key)secretKeyFactory.generateSecret((KeySpec)pBEKeySpec), (AlgorithmParameterSpec)pBEParameterSpec);
            byte[] arrby = cipher.wrap(key);
            return arrby;
        }
        catch (Exception exception) {
            throw new IOException("exception encrypting data - " + exception.toString());
        }
    }

    public static class BCPKCS12KeyStore
    extends PKCS12KeyStoreSpi {
        public BCPKCS12KeyStore() {
            super(new BouncyCastleProvider(), pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd40BitRC2_CBC);
        }
    }

    public static class BCPKCS12KeyStore3DES
    extends PKCS12KeyStoreSpi {
        public BCPKCS12KeyStore3DES() {
            super(new BouncyCastleProvider(), pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }

    private class CertId {
        byte[] id;

        CertId(PublicKey publicKey) {
            this.id = PKCS12KeyStoreSpi.this.createSubjectKeyId(publicKey).getKeyIdentifier();
        }

        CertId(byte[] arrby) {
            this.id = arrby;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof CertId)) {
                return false;
            }
            CertId certId = (CertId)object;
            return Arrays.areEqual(this.id, certId.id);
        }

        public int hashCode() {
            return Arrays.hashCode(this.id);
        }
    }

    public static class DefPKCS12KeyStore
    extends PKCS12KeyStoreSpi {
        public DefPKCS12KeyStore() {
            super(null, pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd40BitRC2_CBC);
        }
    }

    public static class DefPKCS12KeyStore3DES
    extends PKCS12KeyStoreSpi {
        public DefPKCS12KeyStore3DES() {
            super(null, pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }

    private static class DefaultSecretKeyProvider {
        private final Map KEY_SIZES;

        DefaultSecretKeyProvider() {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)new ASN1ObjectIdentifier("1.2.840.113533.7.66.10"), (Object)Integers.valueOf(128));
            hashMap.put((Object)PKCSObjectIdentifiers.des_EDE3_CBC.getId(), (Object)Integers.valueOf(192));
            hashMap.put((Object)NISTObjectIdentifiers.id_aes128_CBC, (Object)Integers.valueOf(128));
            hashMap.put((Object)NISTObjectIdentifiers.id_aes192_CBC, (Object)Integers.valueOf(192));
            hashMap.put((Object)NISTObjectIdentifiers.id_aes256_CBC, (Object)Integers.valueOf(256));
            hashMap.put((Object)NTTObjectIdentifiers.id_camellia128_cbc, (Object)Integers.valueOf(128));
            hashMap.put((Object)NTTObjectIdentifiers.id_camellia192_cbc, (Object)Integers.valueOf(192));
            hashMap.put((Object)NTTObjectIdentifiers.id_camellia256_cbc, (Object)Integers.valueOf(256));
            hashMap.put((Object)CryptoProObjectIdentifiers.gostR28147_gcfb, (Object)Integers.valueOf(256));
            this.KEY_SIZES = Collections.unmodifiableMap((Map)hashMap);
        }

        public int getKeySize(AlgorithmIdentifier algorithmIdentifier) {
            Integer n = (Integer)this.KEY_SIZES.get((Object)algorithmIdentifier.getAlgorithm());
            if (n != null) {
                return n;
            }
            return -1;
        }
    }

    private static class IgnoresCaseHashtable {
        private Hashtable keys = new Hashtable();
        private Hashtable orig = new Hashtable();

        private IgnoresCaseHashtable() {
        }

        public Enumeration elements() {
            return this.orig.elements();
        }

        /*
         * Enabled aggressive block sorting
         */
        public Object get(String string) {
            Hashtable hashtable = this.keys;
            String string2 = string == null ? null : Strings.toLowerCase(string);
            String string3 = (String)hashtable.get((Object)string2);
            if (string3 == null) {
                return null;
            }
            return this.orig.get((Object)string3);
        }

        public Enumeration keys() {
            return this.orig.keys();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void put(String string, Object object) {
            String string2 = string == null ? null : Strings.toLowerCase(string);
            String string3 = (String)this.keys.get((Object)string2);
            if (string3 != null) {
                this.orig.remove((Object)string3);
            }
            this.keys.put((Object)string2, (Object)string);
            this.orig.put((Object)string, object);
        }

        /*
         * Enabled aggressive block sorting
         */
        public Object remove(String string) {
            Hashtable hashtable = this.keys;
            String string2 = string == null ? null : Strings.toLowerCase(string);
            String string3 = (String)hashtable.remove((Object)string2);
            if (string3 == null) {
                return null;
            }
            return this.orig.remove((Object)string3);
        }
    }

}

