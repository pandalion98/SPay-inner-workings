package com.mastercard.mcbp.crypto;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.DefaultByteArrayImpl;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AndroidMCBPCryptoService extends MCBPCryptoService {
    private static Cipher RSACipher;

    static {
        RSACipher = null;
    }

    private void initRSAPrivate(RSAPrivateKey rSAPrivateKey) {
        try {
            RSACipher = Cipher.getInstance("RSA");
            RSACipher.init(1, rSAPrivateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new MMPPCryptoException();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
            throw new MMPPCryptoException();
        } catch (InvalidKeyException e3) {
            e3.printStackTrace();
            throw new MMPPCryptoException();
        }
    }

    public ByteArray SHA1(ByteArray byteArray) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(byteArray.getBytes());
            return new DefaultByteArrayImpl(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new MMPPCryptoException();
        }
    }

    public ByteArray SHA256(ByteArray byteArray) {
        try {
            ByteArray defaultByteArrayImpl = new DefaultByteArrayImpl(MessageDigest.getInstance("SHA-256").digest(byteArray.getBytes()));
            return defaultByteArrayImpl;
        } catch (NoSuchAlgorithmException e) {
            throw new MMPPCryptoException();
        } finally {
            Utils.clearByteArray(byteArray);
        }
    }

    public ByteArray RSA(ByteArray byteArray) {
        try {
            return new DefaultByteArrayImpl(RSACipher.doFinal(byteArray.getBytes()));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new MMPPCryptoException();
        } catch (BadPaddingException e2) {
            e2.printStackTrace();
            throw new MMPPCryptoException();
        } catch (Exception e3) {
            e3.printStackTrace();
            throw new MMPPCryptoException();
        }
    }

    public void clearRSAKey(int i) {
        if (i == 0) {
            i = McTACommands.MOP_MC_TA_CMD_CASD_BASE;
        }
        try {
            KeyPairGenerator instance = KeyPairGenerator.getInstance("RSA");
            instance.initialize(i);
            initRSAPrivate((RSAPrivateKey) instance.genKeyPair().getPrivate());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MMPPCryptoException e2) {
            e2.printStackTrace();
        }
    }

    public int initRSAPrivateKey(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3, ByteArray byteArray4, ByteArray byteArray5) {
        try {
            BigInteger bigInteger = new BigInteger(byteArray.getHexString(), 16);
            BigInteger bigInteger2 = new BigInteger(byteArray2.getHexString(), 16);
            BigInteger bigInteger3 = new BigInteger(byteArray3.getHexString(), 16);
            BigInteger bigInteger4 = new BigInteger(byteArray4.getHexString(), 16);
            BigInteger bigInteger5 = new BigInteger(byteArray5.getHexString(), 16);
            BigInteger multiply = bigInteger.multiply(bigInteger2);
            BigInteger modInverse = bigInteger3.modInverse(bigInteger.subtract(BigInteger.ONE));
            initRSAPrivate((RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(multiply, modInverse, modInverse.modInverse(bigInteger.subtract(BigInteger.ONE).multiply(bigInteger2.subtract(BigInteger.ONE)).divide(bigInteger.subtract(BigInteger.ONE).gcd(bigInteger2.subtract(BigInteger.ONE)))), bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5)));
            return multiply.bitLength() / 8;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new MMPPCryptoException();
        } catch (InvalidKeySpecException e2) {
            throw new MMPPCryptoException();
        } catch (MMPPCryptoException e3) {
            throw new MMPPCryptoException();
        } catch (Exception e4) {
            e4.printStackTrace();
            throw new MMPPCryptoException();
        }
    }
}
