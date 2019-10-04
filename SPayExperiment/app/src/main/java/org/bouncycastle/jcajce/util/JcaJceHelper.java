/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AlgorithmParameterGenerator
 *  java.security.AlgorithmParameters
 *  java.security.KeyFactory
 *  java.security.KeyPairGenerator
 *  java.security.MessageDigest
 *  java.security.Signature
 *  java.security.cert.CertificateFactory
 *  javax.crypto.Cipher
 *  javax.crypto.KeyAgreement
 *  javax.crypto.KeyGenerator
 *  javax.crypto.Mac
 *  javax.crypto.SecretKeyFactory
 */
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

public interface JcaJceHelper {
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator(String var1);

    public AlgorithmParameters createAlgorithmParameters(String var1);

    public CertificateFactory createCertificateFactory(String var1);

    public Cipher createCipher(String var1);

    public MessageDigest createDigest(String var1);

    public KeyAgreement createKeyAgreement(String var1);

    public KeyFactory createKeyFactory(String var1);

    public KeyGenerator createKeyGenerator(String var1);

    public KeyPairGenerator createKeyPairGenerator(String var1);

    public Mac createMac(String var1);

    public SecretKeyFactory createSecretKeyFactory(String var1);

    public Signature createSignature(String var1);
}

