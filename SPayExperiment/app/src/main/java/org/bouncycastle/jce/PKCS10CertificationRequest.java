/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.security.AlgorithmParameters
 *  java.security.GeneralSecurityException
 *  java.security.InvalidKeyException
 *  java.security.KeyFactory
 *  java.security.NoSuchAlgorithmException
 *  java.security.PrivateKey
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.security.Signature
 *  java.security.SignatureException
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.PSSParameterSpec
 *  java.security.spec.X509EncodedKeySpec
 *  java.util.HashSet
 *  java.util.Hashtable
 *  java.util.Set
 *  javax.security.auth.x500.X500Principal
 */
package org.bouncycastle.jce;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.Strings;

public class PKCS10CertificationRequest
extends CertificationRequest {
    private static Hashtable algorithms = new Hashtable();
    private static Hashtable keyAlgorithms;
    private static Set noParams;
    private static Hashtable oids;
    private static Hashtable params;

    static {
        params = new Hashtable();
        keyAlgorithms = new Hashtable();
        oids = new Hashtable();
        noParams = new HashSet();
        algorithms.put((Object)"MD2WITHRSAENCRYPTION", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
        algorithms.put((Object)"MD2WITHRSA", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"));
        algorithms.put((Object)"MD5WITHRSAENCRYPTION", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        algorithms.put((Object)"MD5WITHRSA", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        algorithms.put((Object)"RSAWITHMD5", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"));
        algorithms.put((Object)"SHA1WITHRSAENCRYPTION", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
        algorithms.put((Object)"SHA1WITHRSA", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
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
        algorithms.put((Object)"RSAWITHSHA1", (Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"));
        algorithms.put((Object)"RIPEMD128WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        algorithms.put((Object)"RIPEMD128WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
        algorithms.put((Object)"RIPEMD160WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        algorithms.put((Object)"RIPEMD160WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
        algorithms.put((Object)"RIPEMD256WITHRSAENCRYPTION", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        algorithms.put((Object)"RIPEMD256WITHRSA", (Object)TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
        algorithms.put((Object)"SHA1WITHDSA", (Object)new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
        algorithms.put((Object)"DSAWITHSHA1", (Object)new ASN1ObjectIdentifier("1.2.840.10040.4.3"));
        algorithms.put((Object)"SHA224WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha224);
        algorithms.put((Object)"SHA256WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha256);
        algorithms.put((Object)"SHA384WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha384);
        algorithms.put((Object)"SHA512WITHDSA", (Object)NISTObjectIdentifiers.dsa_with_sha512);
        algorithms.put((Object)"SHA1WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        algorithms.put((Object)"SHA224WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA224);
        algorithms.put((Object)"SHA256WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA256);
        algorithms.put((Object)"SHA384WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA384);
        algorithms.put((Object)"SHA512WITHECDSA", (Object)X9ObjectIdentifiers.ecdsa_with_SHA512);
        algorithms.put((Object)"ECDSAWITHSHA1", (Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        algorithms.put((Object)"GOST3411WITHGOST3410", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        algorithms.put((Object)"GOST3410WITHGOST3411", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        algorithms.put((Object)"GOST3411WITHECGOST3410", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        algorithms.put((Object)"GOST3411WITHECGOST3410-2001", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        algorithms.put((Object)"GOST3411WITHGOST3410-2001", (Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        oids.put((Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.5"), (Object)"SHA1WITHRSA");
        oids.put((Object)PKCSObjectIdentifiers.sha224WithRSAEncryption, (Object)"SHA224WITHRSA");
        oids.put((Object)PKCSObjectIdentifiers.sha256WithRSAEncryption, (Object)"SHA256WITHRSA");
        oids.put((Object)PKCSObjectIdentifiers.sha384WithRSAEncryption, (Object)"SHA384WITHRSA");
        oids.put((Object)PKCSObjectIdentifiers.sha512WithRSAEncryption, (Object)"SHA512WITHRSA");
        oids.put((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, (Object)"GOST3411WITHGOST3410");
        oids.put((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, (Object)"GOST3411WITHECGOST3410");
        oids.put((Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.4"), (Object)"MD5WITHRSA");
        oids.put((Object)new ASN1ObjectIdentifier("1.2.840.113549.1.1.2"), (Object)"MD2WITHRSA");
        oids.put((Object)new ASN1ObjectIdentifier("1.2.840.10040.4.3"), (Object)"SHA1WITHDSA");
        oids.put((Object)X9ObjectIdentifiers.ecdsa_with_SHA1, (Object)"SHA1WITHECDSA");
        oids.put((Object)X9ObjectIdentifiers.ecdsa_with_SHA224, (Object)"SHA224WITHECDSA");
        oids.put((Object)X9ObjectIdentifiers.ecdsa_with_SHA256, (Object)"SHA256WITHECDSA");
        oids.put((Object)X9ObjectIdentifiers.ecdsa_with_SHA384, (Object)"SHA384WITHECDSA");
        oids.put((Object)X9ObjectIdentifiers.ecdsa_with_SHA512, (Object)"SHA512WITHECDSA");
        oids.put((Object)OIWObjectIdentifiers.sha1WithRSA, (Object)"SHA1WITHRSA");
        oids.put((Object)OIWObjectIdentifiers.dsaWithSHA1, (Object)"SHA1WITHDSA");
        oids.put((Object)NISTObjectIdentifiers.dsa_with_sha224, (Object)"SHA224WITHDSA");
        oids.put((Object)NISTObjectIdentifiers.dsa_with_sha256, (Object)"SHA256WITHDSA");
        keyAlgorithms.put((Object)PKCSObjectIdentifiers.rsaEncryption, (Object)"RSA");
        keyAlgorithms.put((Object)X9ObjectIdentifiers.id_dsa, (Object)"DSA");
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA1);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA224);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA256);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA384);
        noParams.add((Object)X9ObjectIdentifiers.ecdsa_with_SHA512);
        noParams.add((Object)X9ObjectIdentifiers.id_dsa_with_sha1);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha224);
        noParams.add((Object)NISTObjectIdentifiers.dsa_with_sha256);
        noParams.add((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
        noParams.add((Object)CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, DERNull.INSTANCE);
        params.put((Object)"SHA1WITHRSAANDMGF1", (Object)PKCS10CertificationRequest.creatPSSParams(algorithmIdentifier, 20));
        AlgorithmIdentifier algorithmIdentifier2 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, DERNull.INSTANCE);
        params.put((Object)"SHA224WITHRSAANDMGF1", (Object)PKCS10CertificationRequest.creatPSSParams(algorithmIdentifier2, 28));
        AlgorithmIdentifier algorithmIdentifier3 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, DERNull.INSTANCE);
        params.put((Object)"SHA256WITHRSAANDMGF1", (Object)PKCS10CertificationRequest.creatPSSParams(algorithmIdentifier3, 32));
        AlgorithmIdentifier algorithmIdentifier4 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, DERNull.INSTANCE);
        params.put((Object)"SHA384WITHRSAANDMGF1", (Object)PKCS10CertificationRequest.creatPSSParams(algorithmIdentifier4, 48));
        AlgorithmIdentifier algorithmIdentifier5 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, DERNull.INSTANCE);
        params.put((Object)"SHA512WITHRSAANDMGF1", (Object)PKCS10CertificationRequest.creatPSSParams(algorithmIdentifier5, 64));
    }

    public PKCS10CertificationRequest(String string, X500Principal x500Principal, PublicKey publicKey, ASN1Set aSN1Set, PrivateKey privateKey) {
        this(string, PKCS10CertificationRequest.convertName(x500Principal), publicKey, aSN1Set, privateKey, "BC");
    }

    public PKCS10CertificationRequest(String string, X500Principal x500Principal, PublicKey publicKey, ASN1Set aSN1Set, PrivateKey privateKey, String string2) {
        this(string, PKCS10CertificationRequest.convertName(x500Principal), publicKey, aSN1Set, privateKey, string2);
    }

    public PKCS10CertificationRequest(String string, X509Name x509Name, PublicKey publicKey, ASN1Set aSN1Set, PrivateKey privateKey) {
        this(string, x509Name, publicKey, aSN1Set, privateKey, "BC");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PKCS10CertificationRequest(String string, X509Name x509Name, PublicKey publicKey, ASN1Set aSN1Set, PrivateKey privateKey, String string2) {
        Signature signature;
        ASN1ObjectIdentifier aSN1ObjectIdentifier;
        String string3 = Strings.toUpperCase(string);
        ASN1ObjectIdentifier aSN1ObjectIdentifier2 = (ASN1ObjectIdentifier)algorithms.get((Object)string3);
        if (aSN1ObjectIdentifier2 == null) {
            try {
                ASN1ObjectIdentifier aSN1ObjectIdentifier3;
                aSN1ObjectIdentifier = aSN1ObjectIdentifier3 = new ASN1ObjectIdentifier(string3);
            }
            catch (Exception exception) {
                throw new IllegalArgumentException("Unknown signature type requested");
            }
        } else {
            aSN1ObjectIdentifier = aSN1ObjectIdentifier2;
        }
        if (x509Name == null) {
            throw new IllegalArgumentException("subject must not be null");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("public key must not be null");
        }
        this.sigAlgId = noParams.contains((Object)aSN1ObjectIdentifier) ? new AlgorithmIdentifier(aSN1ObjectIdentifier) : (params.containsKey((Object)string3) ? new AlgorithmIdentifier(aSN1ObjectIdentifier, (ASN1Encodable)params.get((Object)string3)) : new AlgorithmIdentifier(aSN1ObjectIdentifier, DERNull.INSTANCE));
        try {
            this.reqInfo = new CertificationRequestInfo(x509Name, new SubjectPublicKeyInfo((ASN1Sequence)ASN1Primitive.fromByteArray(publicKey.getEncoded())), aSN1Set);
            signature = string2 == null ? Signature.getInstance((String)string) : Signature.getInstance((String)string, (String)string2);
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("can't encode public key");
        }
        signature.initSign(privateKey);
        try {
            signature.update(this.reqInfo.getEncoded("DER"));
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("exception encoding TBS cert request - " + (Object)((Object)exception));
        }
        this.sigBits = new DERBitString(signature.sign());
    }

    public PKCS10CertificationRequest(ASN1Sequence aSN1Sequence) {
        super(aSN1Sequence);
    }

    public PKCS10CertificationRequest(byte[] arrby) {
        super(PKCS10CertificationRequest.toDERSequence(arrby));
    }

    private static X509Name convertName(X500Principal x500Principal) {
        try {
            X509Principal x509Principal = new X509Principal(x500Principal.getEncoded());
            return x509Principal;
        }
        catch (IOException iOException) {
            throw new IllegalArgumentException("can't convert name");
        }
    }

    private static RSASSAPSSparams creatPSSParams(AlgorithmIdentifier algorithmIdentifier, int n) {
        return new RSASSAPSSparams(algorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, algorithmIdentifier), new ASN1Integer(n), new ASN1Integer(1L));
    }

    private static String getDigestAlgName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        if (PKCSObjectIdentifiers.md5.equals(aSN1ObjectIdentifier)) {
            return "MD5";
        }
        if (OIWObjectIdentifiers.idSHA1.equals(aSN1ObjectIdentifier)) {
            return "SHA1";
        }
        if (NISTObjectIdentifiers.id_sha224.equals(aSN1ObjectIdentifier)) {
            return "SHA224";
        }
        if (NISTObjectIdentifiers.id_sha256.equals(aSN1ObjectIdentifier)) {
            return "SHA256";
        }
        if (NISTObjectIdentifiers.id_sha384.equals(aSN1ObjectIdentifier)) {
            return "SHA384";
        }
        if (NISTObjectIdentifiers.id_sha512.equals(aSN1ObjectIdentifier)) {
            return "SHA512";
        }
        if (TeleTrusTObjectIdentifiers.ripemd128.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD128";
        }
        if (TeleTrusTObjectIdentifiers.ripemd160.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD160";
        }
        if (TeleTrusTObjectIdentifiers.ripemd256.equals(aSN1ObjectIdentifier)) {
            return "RIPEMD256";
        }
        if (CryptoProObjectIdentifiers.gostR3411.equals(aSN1ObjectIdentifier)) {
            return "GOST3411";
        }
        return aSN1ObjectIdentifier.getId();
    }

    static String getSignatureName(AlgorithmIdentifier algorithmIdentifier) {
        ASN1Encodable aSN1Encodable = algorithmIdentifier.getParameters();
        if (aSN1Encodable != null && !DERNull.INSTANCE.equals(aSN1Encodable) && algorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) {
            RSASSAPSSparams rSASSAPSSparams = RSASSAPSSparams.getInstance(aSN1Encodable);
            return PKCS10CertificationRequest.getDigestAlgName(rSASSAPSSparams.getHashAlgorithm().getObjectId()) + "withRSAandMGF1";
        }
        return algorithmIdentifier.getObjectId().getId();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void setSignatureParameters(Signature signature, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable == null || DERNull.INSTANCE.equals(aSN1Encodable)) return;
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance((String)signature.getAlgorithm(), (Provider)signature.getProvider());
        try {
            algorithmParameters.init(aSN1Encodable.toASN1Primitive().getEncoded("DER"));
        }
        catch (IOException iOException) {
            throw new SignatureException("IOException decoding parameters: " + iOException.getMessage());
        }
        if (!signature.getAlgorithm().endsWith("MGF1")) return;
        try {
            signature.setParameter(algorithmParameters.getParameterSpec(PSSParameterSpec.class));
            return;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new SignatureException("Exception extracting parameters: " + generalSecurityException.getMessage());
        }
    }

    private static ASN1Sequence toDERSequence(byte[] arrby) {
        try {
            ASN1Sequence aSN1Sequence = (ASN1Sequence)new ASN1InputStream(arrby).readObject();
            return aSN1Sequence;
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("badly encoded request");
        }
    }

    @Override
    public byte[] getEncoded() {
        try {
            byte[] arrby = this.getEncoded("DER");
            return arrby;
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException.toString());
        }
    }

    public PublicKey getPublicKey() {
        return this.getPublicKey("BC");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public PublicKey getPublicKey(String string) {
        SubjectPublicKeyInfo subjectPublicKeyInfo = this.reqInfo.getSubjectPublicKeyInfo();
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(subjectPublicKeyInfo).getBytes());
        AlgorithmIdentifier algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
        if (string != null) return KeyFactory.getInstance((String)algorithmIdentifier.getAlgorithm().getId(), (String)string).generatePublic((KeySpec)x509EncodedKeySpec);
        try {
            return KeyFactory.getInstance((String)algorithmIdentifier.getAlgorithm().getId()).generatePublic((KeySpec)x509EncodedKeySpec);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            if (keyAlgorithms.get((Object)algorithmIdentifier.getObjectId()) == null) throw noSuchAlgorithmException;
            String string2 = (String)keyAlgorithms.get((Object)algorithmIdentifier.getObjectId());
            if (string != null) return KeyFactory.getInstance((String)string2, (String)string).generatePublic((KeySpec)x509EncodedKeySpec);
            try {
                return KeyFactory.getInstance((String)string2).generatePublic((KeySpec)x509EncodedKeySpec);
            }
            catch (InvalidKeySpecException invalidKeySpecException) {
                throw new InvalidKeyException("error decoding public key");
            }
            catch (IOException iOException) {
                throw new InvalidKeyException("error decoding public key");
            }
        }
    }

    public boolean verify() {
        return this.verify("BC");
    }

    public boolean verify(String string) {
        return this.verify(this.getPublicKey(string), string);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean verify(PublicKey var1_1, String var2_2) {
        block5 : {
            if (var2_2 != null) ** GOTO lbl5
            try {
                var5_4 = var8_3 = Signature.getInstance((String)PKCS10CertificationRequest.getSignatureName(this.sigAlgId));
                break block5;
lbl5: // 1 sources:
                var5_4 = var7_5 = Signature.getInstance((String)PKCS10CertificationRequest.getSignatureName(this.sigAlgId), (String)var2_2);
            }
            catch (NoSuchAlgorithmException var3_6) {
                if (PKCS10CertificationRequest.oids.get((Object)this.sigAlgId.getObjectId()) == null) throw var3_6;
                var4_7 = (String)PKCS10CertificationRequest.oids.get((Object)this.sigAlgId.getObjectId());
                var5_4 = var2_2 == null ? Signature.getInstance((String)var4_7) : Signature.getInstance((String)var4_7, (String)var2_2);
            }
        }
        this.setSignatureParameters(var5_4, this.sigAlgId.getParameters());
        var5_4.initVerify(var1_1);
        try {
            var5_4.update(this.reqInfo.getEncoded("DER"));
        }
        catch (Exception var6_8) {
            throw new SignatureException("exception encoding TBS cert request - " + (Object)var6_8);
        }
        return var5_4.verify(this.sigBits.getBytes());
    }
}

