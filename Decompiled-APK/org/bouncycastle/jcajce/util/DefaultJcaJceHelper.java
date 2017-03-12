package org.bouncycastle.jcajce.util;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;

public class DefaultJcaJceHelper implements JcaJceHelper {
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(String str) {
        return AlgorithmParameterGenerator.getInstance(str);
    }

    public AlgorithmParameters createAlgorithmParameters(String str) {
        return AlgorithmParameters.getInstance(str);
    }

    public CertificateFactory createCertificateFactory(String str) {
        return CertificateFactory.getInstance(str);
    }

    public Cipher createCipher(String str) {
        return Cipher.getInstance(str);
    }

    public MessageDigest createDigest(String str) {
        return MessageDigest.getInstance(str);
    }

    public KeyAgreement createKeyAgreement(String str) {
        return KeyAgreement.getInstance(str);
    }

    public KeyFactory createKeyFactory(String str) {
        return KeyFactory.getInstance(str);
    }

    public KeyGenerator createKeyGenerator(String str) {
        return KeyGenerator.getInstance(str);
    }

    public KeyPairGenerator createKeyPairGenerator(String str) {
        return KeyPairGenerator.getInstance(str);
    }

    public Mac createMac(String str) {
        return Mac.getInstance(str);
    }

    public SecretKeyFactory createSecretKeyFactory(String str) {
        return SecretKeyFactory.getInstance(str);
    }

    public Signature createSignature(String str) {
        return Signature.getInstance(str);
    }
}
