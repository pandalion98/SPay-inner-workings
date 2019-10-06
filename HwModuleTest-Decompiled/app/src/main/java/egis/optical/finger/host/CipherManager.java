package egis.optical.finger.host;

import android.util.Base64;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherManager {
    private static final String PREFIX = "yu";
    private static final String TAG = "FpCsaClientLib_CipherManager";
    private CipherType cipherMode;

    /* renamed from: mD */
    private String f40mD;

    /* renamed from: mE */
    private String f41mE;

    /* renamed from: mN */
    private String f42mN;
    private SecretKeySpec mSecKey;
    private AlgorithmParameterSpec mSpec;

    public enum CipherType {
        AES,
        RSA
    }

    public CipherManager(CipherType cipherMode2) {
        this.cipherMode = cipherMode2;
    }

    public void init(byte[] keys, byte[] iv) {
        if (this.cipherMode == CipherType.AES) {
            this.mSecKey = new SecretKeySpec(keys, "AES");
            this.mSpec = new IvParameterSpec(iv);
        }
    }

    public void init(String mod, String pub, String sec) {
        this.f42mN = mod;
        this.f41mE = pub;
        this.f40mD = sec;
    }

    private byte[] extendKey(String key) {
        if (key == null) {
            Log.e(TAG, "extendKey key == null");
            FPNativeBase.lastErrCode = 2080;
            return null;
        }
        byte[] bKey = new byte[32];
        int i = 0;
        for (int i2 = 0; i2 < bKey.length; i2++) {
            bKey[i2] = 65;
        }
        byte[] bData = key.getBytes();
        while (i < key.length() && i < bKey.length) {
            bKey[i] = bData[i];
            i++;
        }
        return bKey;
    }

    public boolean setKey(String key) {
        byte[] bKey = extendKey(key);
        if (bKey == null) {
            Log.e(TAG, "setKey bKey == null");
            FPNativeBase.lastErrCode = 2081;
            return false;
        }
        this.mSecKey = null;
        this.mSecKey = new SecretKeySpec(bKey, "AES");
        if (this.mSecKey != null) {
            return true;
        }
        Log.e(TAG, "setKey mSecKey == null");
        FPNativeBase.lastErrCode = 2082;
        return false;
    }

    public byte[] encryption(String plaintext) {
        return encryption(plaintext.getBytes());
    }

    public byte[] encryption(byte[] plaintext) {
        if (plaintext == null) {
            Log.e(TAG, "encryption plaintext == null");
            FPNativeBase.lastErrCode = 2083;
            return null;
        }
        byte[] encryptedData = null;
        if (this.cipherMode == CipherType.AES) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(1, this.mSecKey, this.mSpec);
                encryptedData = cipher.doFinal(plaintext);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e2) {
                e2.printStackTrace();
            } catch (InvalidKeyException e3) {
                e3.printStackTrace();
            } catch (InvalidAlgorithmParameterException e4) {
                e4.printStackTrace();
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
            } catch (BadPaddingException e6) {
                e6.printStackTrace();
            }
        } else if (this.cipherMode == CipherType.RSA) {
            try {
                PublicKey pubKey = readPubKey();
                Cipher cipher2 = Cipher.getInstance("RSA");
                cipher2.init(1, pubKey);
                reverse(plaintext);
                encryptedData = cipher2.doFinal(plaintext);
                reverse(encryptedData);
            } catch (IOException e7) {
                e7.printStackTrace();
            } catch (NoSuchAlgorithmException e8) {
                e8.printStackTrace();
            } catch (NoSuchPaddingException e9) {
                e9.printStackTrace();
            } catch (InvalidKeyException e10) {
                e10.printStackTrace();
            } catch (IllegalBlockSizeException e11) {
                e11.printStackTrace();
            } catch (BadPaddingException e12) {
                e12.printStackTrace();
            }
        }
        return encryptedData;
    }

    public byte[] decryption(String ciphertext) {
        return decryption(ciphertext.getBytes());
    }

    public byte[] decryption(byte[] ciphertext) {
        if (ciphertext == null) {
            Log.e(TAG, "decryption data == null");
            FPNativeBase.lastErrCode = 2084;
            return null;
        }
        byte[] decryptedData = {110, 111};
        if (this.cipherMode == CipherType.AES) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                cipher.init(2, this.mSecKey, this.mSpec);
                decryptedData = cipher.doFinal(ciphertext);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e2) {
                e2.printStackTrace();
            } catch (InvalidKeyException e3) {
                e3.printStackTrace();
            } catch (InvalidAlgorithmParameterException e4) {
                e4.printStackTrace();
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
            } catch (BadPaddingException e6) {
                e6.printStackTrace();
            }
        } else if (this.cipherMode == CipherType.RSA) {
            try {
                PrivateKey privKey = readPrivKey();
                Cipher cipher2 = Cipher.getInstance("RSA");
                cipher2.init(2, privKey);
                reverse(ciphertext);
                decryptedData = cipher2.doFinal(ciphertext);
                reverse(decryptedData);
            } catch (IOException e7) {
                e7.printStackTrace();
            } catch (NoSuchAlgorithmException e8) {
                e8.printStackTrace();
            } catch (NoSuchPaddingException e9) {
                e9.printStackTrace();
            } catch (InvalidKeyException e10) {
                e10.printStackTrace();
            } catch (IllegalBlockSizeException e11) {
                e11.printStackTrace();
            } catch (BadPaddingException e12) {
                e12.printStackTrace();
            }
        }
        return decryptedData;
    }

    private PublicKey readPubKey() throws IOException {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(this.f42mN, 16), new BigInteger(this.f41mE, 16)));
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        }
    }

    private PrivateKey readPrivKey() throws IOException {
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(new BigInteger(this.f42mN, 16), new BigInteger(this.f40mD, 16)));
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        }
    }

    private void reverse(byte[] array) {
        if (array != null) {
            int j = array.length - 1;
            for (int i = 0; j > i; i++) {
                byte tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                j--;
            }
        }
    }

    public String packageData(String data) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        sb.append(data);
        return sb.toString();
    }

    public String unpackageData(String data) {
        if (!data.startsWith(PREFIX)) {
            return null;
        }
        return data.substring(PREFIX.length());
    }

    public String encryptData(String data) {
        if (data == null) {
            Log.e(TAG, "encryptData fail, data is null");
            FPNativeBase.lastErrCode = 2085;
            return null;
        }
        byte[] b = encryption(data);
        if (b != null) {
            return Base64.encodeToString(b, 0);
        }
        FPNativeBase.lastErrCode = 2086;
        return null;
    }

    public String decryptData(String data) {
        byte[] b = decryption(Base64.decode(data.getBytes(), 0));
        String str = null;
        if (b == null) {
            FPNativeBase.lastErrCode = 2087;
            return null;
        }
        try {
            str = new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public boolean validateData(String data) {
        if (data == null || data.length() <= 0) {
            Log.e(TAG, "validateData invalid data");
            FPNativeBase.lastErrCode = 2088;
            return false;
        } else if (data.startsWith(PREFIX)) {
            return true;
        } else {
            return false;
        }
    }
}
