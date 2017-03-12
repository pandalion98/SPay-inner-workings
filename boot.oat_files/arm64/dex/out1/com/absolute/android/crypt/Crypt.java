package com.absolute.android.crypt;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.zip.CRC32;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Crypt {
    public static final String ATTR_StackMapTable = "dummy";
    public static final String CIPHER_ALGORITHM = "DESede";
    public static final String CIPHER_TRANSFORM = "DESede/CBC/PKCS5Padding";
    private static final int a = 8192;

    public static byte[] cryptValue(byte[] bArr, byte[] bArr2, int i, String str, String str2) {
        Cipher cipher = getCipher(bArr, i, str, str2);
        byte[] bArr3 = new byte[cipher.getOutputSize(bArr2.length)];
        int update = cipher.update(bArr2, 0, bArr2.length, bArr3, 0);
        update += cipher.doFinal(bArr3, update);
        if (update >= bArr3.length) {
            return bArr3;
        }
        Object obj = new byte[update];
        System.arraycopy(bArr3, 0, obj, 0, update);
        return obj;
    }

    public static Cipher getCipher(byte[] bArr, int i, String str, String str2) {
        String str3 = CIPHER_ALGORITHM;
        int indexOf = str.indexOf("/");
        if (indexOf != -1) {
            str3 = str.substring(0, indexOf);
        }
        Key generateSecret = SecretKeyFactory.getInstance(str3).generateSecret(new DESedeKeySpec(bArr));
        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(a(str2));
        Cipher instance = Cipher.getInstance(str);
        instance.init(i, generateSecret, ivParameterSpec);
        return instance;
    }

    public static String getDigest(String str, String str2) {
        FileInputStream fileInputStream;
        DigestInputStream digestInputStream;
        Exception e;
        Throwable th;
        DigestInputStream digestInputStream2 = null;
        MessageDigest instance = MessageDigest.getInstance(str);
        try {
            fileInputStream = new FileInputStream(str2);
            try {
                digestInputStream = new DigestInputStream(fileInputStream, instance);
            } catch (Exception e2) {
                e = e2;
                try {
                    throw e;
                } catch (Throwable th2) {
                    th = th2;
                    digestInputStream = digestInputStream2;
                }
            } catch (Throwable th3) {
                th = th3;
                digestInputStream = null;
                if (digestInputStream == null) {
                    try {
                        digestInputStream.close();
                    } catch (Exception e3) {
                    }
                } else if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
            try {
                do {
                } while (digestInputStream.read(new byte[8192]) != -1);
                String EncodeBytesAsHexString = HexUtilities.EncodeBytesAsHexString(digestInputStream.getMessageDigest().digest());
                try {
                    digestInputStream.close();
                } catch (Exception e4) {
                }
                return EncodeBytesAsHexString;
            } catch (Exception e5) {
                e = e5;
                digestInputStream2 = digestInputStream;
                throw e;
            } catch (Throwable th4) {
                th = th4;
                if (digestInputStream == null) {
                    digestInputStream.close();
                } else if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            fileInputStream = null;
            throw e;
        } catch (Throwable th5) {
            th = th5;
            fileInputStream = null;
            digestInputStream = null;
            if (digestInputStream == null) {
                digestInputStream.close();
            } else if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }

    private static byte[] a(String str) {
        byte[] bArr = new byte[str.length()];
        for (int i = 0; i < str.length(); i++) {
            bArr[i] = (byte) str.charAt(i);
        }
        CRC32 crc32 = new CRC32();
        crc32.update(bArr, 0, bArr.length);
        long value = crc32.getValue() & 4294967295L;
        return new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) ((int) ((-16777216 & value) >> 24)), (byte) ((int) ((16711680 & value) >> 16)), (byte) ((int) ((65280 & value) >> 8)), (byte) ((int) (value & 255))};
    }
}
