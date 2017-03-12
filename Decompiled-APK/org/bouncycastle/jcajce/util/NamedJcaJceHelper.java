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

public class NamedJcaJceHelper implements JcaJceHelper {
    protected final String providerName;

    public NamedJcaJceHelper(String str) {
        this.providerName = str;
    }

    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(String str) {
        return AlgorithmParameterGenerator.getInstance(str, this.providerName);
    }

    public AlgorithmParameters createAlgorithmParameters(String str) {
        return AlgorithmParameters.getInstance(str, this.providerName);
    }

    public CertificateFactory createCertificateFactory(String str) {
        return CertificateFactory.getInstance(str, this.providerName);
    }

    public Cipher createCipher(String str) {
        return Cipher.getInstance(str, this.providerName);
    }

    public MessageDigest createDigest(String str) {
        return MessageDigest.getInstance(str, this.providerName);
    }

    public KeyAgreement createKeyAgreement(String str) {
        return KeyAgreement.getInstance(str, this.providerName);
    }

    public KeyFactory createKeyFactory(String str) {
        return KeyFactory.getInstance(str, this.providerName);
    }

    public KeyGenerator createKeyGenerator(String str) {
        return KeyGenerator.getInstance(str, this.providerName);
    }

    public KeyPairGenerator createKeyPairGenerator(String str) {
        return KeyPairGenerator.getInstance(str, this.providerName);
    }

    public Mac createMac(String str) {
        return Mac.getInstance(str, this.providerName);
    }

    public SecretKeyFactory createSecretKeyFactory(String str) {
        return SecretKeyFactory.getInstance(str, this.providerName);
    }

    public Signature createSignature(String str) {
        return Signature.getInstance(str, this.providerName);
    }
}
