/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.InvalidKeyException
 *  java.security.Key
 *  java.security.KeyFactory
 *  java.security.KeyPair
 *  java.security.KeyPairGenerator
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.security.PrivateKey
 *  java.security.interfaces.RSAPrivateKey
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  java.security.spec.RSAPrivateCrtKeySpec
 *  javax.crypto.BadPaddingException
 *  javax.crypto.Cipher
 *  javax.crypto.IllegalBlockSizeException
 *  javax.crypto.NoSuchPaddingException
 */
package com.mastercard.mcbp.crypto;

import com.mastercard.mcbp.crypto.MCBPCryptoService;
import com.mastercard.mcbp.crypto.MMPPCryptoException;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.Utils;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AndroidMCBPCryptoService
extends MCBPCryptoService {
    private static Cipher RSACipher = null;

    private void initRSAPrivate(RSAPrivateKey rSAPrivateKey) {
        try {
            RSACipher = Cipher.getInstance((String)"RSA");
            RSACipher.init(1, (Key)rSAPrivateKey);
            return;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            throw new MMPPCryptoException();
        }
        catch (NoSuchPaddingException noSuchPaddingException) {
            noSuchPaddingException.printStackTrace();
            throw new MMPPCryptoException();
        }
        catch (InvalidKeyException invalidKeyException) {
            invalidKeyException.printStackTrace();
            throw new MMPPCryptoException();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public ByteArray RSA(ByteArray byteArray) {
        try {
            return new DefaultByteArrayImpl(RSACipher.doFinal(byteArray.getBytes()));
        }
        catch (IllegalBlockSizeException illegalBlockSizeException) {
            illegalBlockSizeException.printStackTrace();
            do {
                throw new MMPPCryptoException();
                break;
            } while (true);
        }
        catch (BadPaddingException badPaddingException) {
            badPaddingException.printStackTrace();
            throw new MMPPCryptoException();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new MMPPCryptoException();
        }
    }

    @Override
    public ByteArray SHA1(ByteArray byteArray) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-1");
            messageDigest.update(byteArray.getBytes());
            DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(messageDigest.digest());
            return defaultByteArrayImpl;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new MMPPCryptoException();
        }
    }

    @Override
    public ByteArray SHA256(ByteArray byteArray) {
        try {
            DefaultByteArrayImpl defaultByteArrayImpl = new DefaultByteArrayImpl(MessageDigest.getInstance((String)"SHA-256").digest(byteArray.getBytes()));
            return defaultByteArrayImpl;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new MMPPCryptoException();
        }
        finally {
            Utils.clearByteArray(byteArray);
        }
    }

    public void clearRSAKey(int n2) {
        if (n2 == 0) {
            n2 = 768;
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance((String)"RSA");
            keyPairGenerator.initialize(n2);
            this.initRSAPrivate((RSAPrivateKey)keyPairGenerator.genKeyPair().getPrivate());
            return;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            return;
        }
        catch (MMPPCryptoException mMPPCryptoException) {
            mMPPCryptoException.printStackTrace();
            return;
        }
    }

    @Override
    public int initRSAPrivateKey(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5) {
        try {
            BigInteger bigInteger = new BigInteger(byteArray.getHexString(), 16);
            BigInteger bigInteger2 = new BigInteger(byteArray2.getHexString(), 16);
            BigInteger bigInteger3 = new BigInteger(byteArray3.getHexString(), 16);
            BigInteger bigInteger4 = new BigInteger(byteArray4.getHexString(), 16);
            BigInteger bigInteger5 = new BigInteger(byteArray5.getHexString(), 16);
            BigInteger bigInteger6 = bigInteger.multiply(bigInteger2);
            BigInteger bigInteger7 = bigInteger3.modInverse(bigInteger.subtract(BigInteger.ONE));
            BigInteger bigInteger8 = bigInteger7.modInverse(bigInteger.subtract(BigInteger.ONE).multiply(bigInteger2.subtract(BigInteger.ONE)).divide(bigInteger.subtract(BigInteger.ONE).gcd(bigInteger2.subtract(BigInteger.ONE))));
            this.initRSAPrivate((RSAPrivateKey)KeyFactory.getInstance((String)"RSA").generatePrivate((KeySpec)new RSAPrivateCrtKeySpec(bigInteger6, bigInteger7, bigInteger8, bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5)));
            int n2 = bigInteger6.bitLength() / 8;
            return n2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
            throw new MMPPCryptoException();
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new MMPPCryptoException();
        }
        catch (MMPPCryptoException mMPPCryptoException) {
            throw new MMPPCryptoException();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new MMPPCryptoException();
        }
    }
}

