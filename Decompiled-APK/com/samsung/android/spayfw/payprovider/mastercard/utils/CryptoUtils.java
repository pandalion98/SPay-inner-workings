package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoUtils {
    private static final String FIXED_8_BYTES_FOR_ACCOUNT_HASH = "123CCB2F30BA420B";
    private static final String TAG = "CryptoUtils";

    public enum ShaConstants {
        SHA1(1, "SHA-1"),
        SHA256(2, "SHA-256");
        
        private int id;
        private String name;

        private ShaConstants(int i, String str) {
            this.id = i;
            this.name = str;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    public static byte[] getShaDigest(String str, ShaConstants shaConstants) {
        byte[] bArr = null;
        if (!(TextUtils.isEmpty(str) || shaConstants == null)) {
            try {
                byte[] bytes = str.getBytes("UTF8");
                MessageDigest instance = MessageDigest.getInstance(shaConstants.getName());
                instance.update(bytes);
                bArr = instance.digest();
            } catch (NoSuchAlgorithmException e) {
                Log.m285d(TAG, "Exception in getShaDigest" + e.getMessage());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                Log.m285d(TAG, "Exception in getShaDigest" + e2.getMessage());
            }
        }
        return bArr;
    }

    public static byte[] getAccountIdHash(String str) {
        Log.m285d(TAG, "accountId= " + str);
        try {
            byte[] bytes = str.getBytes("UTF8");
            byte[] convertHexStringToByteArray = convertHexStringToByteArray(FIXED_8_BYTES_FOR_ACCOUNT_HASH);
            ByteBuffer allocate = ByteBuffer.allocate(bytes.length + convertHexStringToByteArray.length);
            allocate.put(bytes);
            allocate.put(convertHexStringToByteArray);
            bytes = strongHash(allocate.array());
            Log.m285d(TAG, "Strong Hash= " + convertbyteToHexString(bytes));
            allocate = ByteBuffer.allocate(32);
            allocate.put(convertHexStringToByteArray);
            Log.m285d(TAG, "fixed8BytesHash= " + convertbyteToHexString(convertHexStringToByteArray));
            allocate.put(bytes, 8, 24);
            Log.m285d(TAG, "accountIdHash= " + convertbyteToHexString(allocate.array()));
            return allocate.array();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.m285d(TAG, "Exception in  getAccountIdHash: " + e.getMessage());
            return null;
        } catch (NullPointerException e2) {
            e2.printStackTrace();
            Log.m285d(TAG, "NPE in  getAccountIdHash: " + e2.getMessage());
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            Log.m285d(TAG, "Exception in  getAccountIdHash: " + e3.getMessage());
            return null;
        }
    }

    public static byte[] generateRndBytes(int i) {
        if (i < 1) {
            Log.m285d(TAG, "Invalid input length");
            return null;
        }
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static boolean verifyAccountIdHash(String str, byte[] bArr) {
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length != 32) {
            Log.m285d(TAG, "Invalid data. Cannot be null or less than 64 length");
            return false;
        }
        ByteBuffer allocate = ByteBuffer.allocate(32);
        byte[] bArr2 = new byte[8];
        byte[] bArr3 = new byte[24];
        allocate.put(bArr);
        allocate.rewind();
        allocate.get(bArr2, 0, 8);
        Log.m285d(TAG, "fixed8Bytes = " + convertbyteToHexString(bArr2));
        allocate.get(bArr3, 0, 24);
        Log.m285d(TAG, "expectedBytes = " + convertbyteToHexString(bArr3));
        byte[] bArr4 = null;
        try {
            bArr4 = str.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.m285d(TAG, "email = " + str);
        allocate.clear();
        if (bArr4 == null) {
            Log.m285d(TAG, "Invalid data. Email should be UTF8 encoded.");
            return false;
        }
        ByteBuffer allocate2 = ByteBuffer.allocate(bArr4.length + bArr2.length);
        allocate2.put(bArr4);
        allocate2.put(bArr2);
        bArr4 = strongHash(allocate2.array());
        allocate2.clear();
        allocate2 = ByteBuffer.allocate(24);
        allocate2.put(bArr4, 8, 24);
        bArr4 = allocate2.array();
        Log.m285d(TAG, "expectedBytes = " + convertbyteToHexString(bArr3));
        Log.m285d(TAG, "actual last 24 bytes=" + convertbyteToHexString(bArr4));
        boolean equals = Arrays.equals(bArr3, bArr4);
        Log.m285d(TAG, "Result=" + equals);
        return equals;
    }

    private static byte[] strongHash(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Log.m285d(TAG, "Invalid data. Cannot be null or empty for strongerHash");
            return null;
        }
        byte[] bArr2 = bArr;
        for (int i = 0; i < 5000; i++) {
            bArr2 = getShaDigest(bArr2, ShaConstants.SHA256);
            if (bArr2 == null || bArr2.length != 32) {
                Log.m285d(TAG, "Err during hashing operation:");
                if (bArr2 == null) {
                    return null;
                }
                Log.m285d(TAG, "Err during hashing operation: Current Hash length=" + bArr2.length);
                return null;
            }
        }
        byte[] shaDigest = getShaDigest(bArr, ShaConstants.SHA256);
        if (shaDigest == null || shaDigest.length != 32) {
            Log.m285d(TAG, "Err during hashing operation:");
            if (shaDigest == null) {
                return null;
            }
            Log.m285d(TAG, "Err during hashing operation: dataToHashInSha256 length=" + shaDigest.length);
            return null;
        }
        ByteBuffer allocate = ByteBuffer.allocate(64);
        allocate.put(shaDigest);
        allocate.put(bArr2);
        return getShaDigest(allocate.array(), ShaConstants.SHA256);
    }

    public static byte[] getShaDigest(byte[] bArr, ShaConstants shaConstants) {
        byte[] bArr2 = null;
        if (bArr.length > 0) {
            try {
                MessageDigest instance = MessageDigest.getInstance(shaConstants.getName());
                instance.update(bArr);
                bArr2 = instance.digest();
            } catch (NoSuchAlgorithmException e) {
                Log.m285d(TAG, "Exception in getShaDigest" + e.getMessage());
                e.printStackTrace();
            }
        }
        return bArr2;
    }

    public static String convertbyteToHexString(byte[] bArr) {
        if (bArr == null || bArr.length < 1) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bArr.length; i++) {
            stringBuilder.append(String.format("%02X", new Object[]{Byte.valueOf(bArr[i])}));
        }
        return stringBuilder.toString();
    }

    public static byte[] convertHexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
