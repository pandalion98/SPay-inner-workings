/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Character
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.security.SecureRandom
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.text.TextUtils;

import com.samsung.android.spayfw.b.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoUtils {
    private static final String FIXED_8_BYTES_FOR_ACCOUNT_HASH = "123CCB2F30BA420B";
    private static final String TAG = "CryptoUtils";

    public static byte[] convertHexStringToByteArray(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    public static String convertbyteToHexString(byte[] arrby) {
        if (arrby == null || arrby.length < 1) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            Object[] arrobject = new Object[]{arrby[i2]};
            stringBuilder.append(String.format((String)"%02X", (Object[])arrobject));
        }
        return stringBuilder.toString();
    }

    public static byte[] generateRndBytes(int n2) {
        if (n2 < 1) {
            Log.d(TAG, "Invalid input length");
            return null;
        }
        SecureRandom secureRandom = new SecureRandom();
        byte[] arrby = new byte[n2];
        secureRandom.nextBytes(arrby);
        return arrby;
    }

    public static byte[] getAccountIdHash(String string) {
        ByteBuffer byteBuffer;
        Log.d(TAG, "accountId= " + string);
        try {
            byte[] arrby = string.getBytes("UTF8");
            byte[] arrby2 = CryptoUtils.convertHexStringToByteArray(FIXED_8_BYTES_FOR_ACCOUNT_HASH);
            ByteBuffer byteBuffer2 = ByteBuffer.allocate((int)(arrby.length + arrby2.length));
            byteBuffer2.put(arrby);
            byteBuffer2.put(arrby2);
            byte[] arrby3 = CryptoUtils.strongHash(byteBuffer2.array());
            Log.d(TAG, "Strong Hash= " + CryptoUtils.convertbyteToHexString(arrby3));
            byteBuffer = ByteBuffer.allocate((int)32);
            byteBuffer.put(arrby2);
            Log.d(TAG, "fixed8BytesHash= " + CryptoUtils.convertbyteToHexString(arrby2));
            byteBuffer.put(arrby3, 8, 24);
            Log.d(TAG, "accountIdHash= " + CryptoUtils.convertbyteToHexString(byteBuffer.array()));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            Log.d(TAG, "Exception in  getAccountIdHash: " + unsupportedEncodingException.getMessage());
            return null;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            Log.d(TAG, "NPE in  getAccountIdHash: " + nullPointerException.getMessage());
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Log.d(TAG, "Exception in  getAccountIdHash: " + exception.getMessage());
            return null;
        }
        return byteBuffer.array();
    }

    public static byte[] getShaDigest(String string, ShaConstants shaConstants) {
        if (TextUtils.isEmpty((CharSequence)string) || shaConstants == null) {
            return null;
        }
        try {
            byte[] arrby = string.getBytes("UTF8");
            MessageDigest messageDigest = MessageDigest.getInstance((String)shaConstants.getName());
            messageDigest.update(arrby);
            byte[] arrby2 = messageDigest.digest();
            return arrby2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.d(TAG, "Exception in getShaDigest" + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();
            return null;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            Log.d(TAG, "Exception in getShaDigest" + unsupportedEncodingException.getMessage());
            return null;
        }
    }

    public static byte[] getShaDigest(byte[] arrby, ShaConstants shaConstants) {
        if (arrby.length <= 0) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)shaConstants.getName());
            messageDigest.update(arrby);
            byte[] arrby2 = messageDigest.digest();
            return arrby2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.d(TAG, "Exception in getShaDigest" + noSuchAlgorithmException.getMessage());
            noSuchAlgorithmException.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static byte[] strongHash(byte[] arrby) {
        if (arrby == null || arrby.length == 0) {
            Log.d(TAG, "Invalid data. Cannot be null or empty for strongerHash");
            return null;
        }
        byte[] arrby2 = arrby;
        for (int i2 = 0; i2 < 5000; ++i2) {
            if ((arrby2 = CryptoUtils.getShaDigest(arrby2, ShaConstants.SHA256)) != null && arrby2.length == 32) continue;
            Log.d(TAG, "Err during hashing operation:");
            if (arrby2 == null) return null;
            Log.d(TAG, "Err during hashing operation: Current Hash length=" + arrby2.length);
            return null;
        }
        byte[] arrby3 = CryptoUtils.getShaDigest(arrby, ShaConstants.SHA256);
        if (arrby3 != null && arrby3.length == 32) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)64);
            byteBuffer.put(arrby3);
            byteBuffer.put(arrby2);
            return CryptoUtils.getShaDigest(byteBuffer.array(), ShaConstants.SHA256);
        }
        Log.d(TAG, "Err during hashing operation:");
        if (arrby3 == null) return null;
        Log.d(TAG, "Err during hashing operation: dataToHashInSha256 length=" + arrby3.length);
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean verifyAccountIdHash(String string, byte[] arrby) {
        byte[] arrby2;
        if (TextUtils.isEmpty((CharSequence)string) || arrby == null || arrby.length != 32) {
            Log.d(TAG, "Invalid data. Cannot be null or less than 64 length");
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)32);
        byte[] arrby3 = new byte[8];
        byte[] arrby4 = new byte[24];
        byteBuffer.put(arrby);
        byteBuffer.rewind();
        byteBuffer.get(arrby3, 0, 8);
        Log.d(TAG, "fixed8Bytes = " + CryptoUtils.convertbyteToHexString(arrby3));
        byteBuffer.get(arrby4, 0, 24);
        Log.d(TAG, "expectedBytes = " + CryptoUtils.convertbyteToHexString(arrby4));
        try {
            byte[] arrby5;
            arrby2 = arrby5 = string.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            arrby2 = null;
        }
        Log.d(TAG, "email = " + string);
        byteBuffer.clear();
        if (arrby2 == null) {
            Log.d(TAG, "Invalid data. Email should be UTF8 encoded.");
            return false;
        }
        ByteBuffer byteBuffer2 = ByteBuffer.allocate((int)(arrby2.length + arrby3.length));
        byteBuffer2.put(arrby2);
        byteBuffer2.put(arrby3);
        byte[] arrby6 = CryptoUtils.strongHash(byteBuffer2.array());
        byteBuffer2.clear();
        ByteBuffer byteBuffer3 = ByteBuffer.allocate((int)24);
        byteBuffer3.put(arrby6, 8, 24);
        byte[] arrby7 = byteBuffer3.array();
        Log.d(TAG, "expectedBytes = " + CryptoUtils.convertbyteToHexString(arrby4));
        Log.d(TAG, "actual last 24 bytes=" + CryptoUtils.convertbyteToHexString(arrby7));
        boolean bl = Arrays.equals((byte[])arrby4, (byte[])arrby7);
        Log.d(TAG, "Result=" + bl);
        return bl;
    }

    public static final class ShaConstants
    extends Enum<ShaConstants> {
        private static final /* synthetic */ ShaConstants[] $VALUES;
        public static final /* enum */ ShaConstants SHA1 = new ShaConstants(1, "SHA-1");
        public static final /* enum */ ShaConstants SHA256 = new ShaConstants(2, "SHA-256");
        private int id;
        private String name;

        static {
            ShaConstants[] arrshaConstants = new ShaConstants[]{SHA1, SHA256};
            $VALUES = arrshaConstants;
        }

        private ShaConstants(int n3, String string2) {
            this.id = n3;
            this.name = string2;
        }

        public static ShaConstants valueOf(String string) {
            return (ShaConstants)Enum.valueOf(ShaConstants.class, (String)string);
        }

        public static ShaConstants[] values() {
            return (ShaConstants[])$VALUES.clone();
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setId(int n2) {
            this.id = n2;
        }

        public void setName(String string) {
            this.name = string;
        }
    }

}

