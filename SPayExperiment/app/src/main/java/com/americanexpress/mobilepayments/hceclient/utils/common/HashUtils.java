/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.io.UnsupportedEncodingException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    private static final String UTF_8 = "UTF-8";

    public static String computeSHA1(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)SHA_1);
            messageDigest.update(string.getBytes(UTF_8));
            String string2 = Utility.byteArrayToHexString(messageDigest.digest());
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.e((String)"core-hceclient", (String)("" + noSuchAlgorithmException.getMessage()));
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e((String)"core-hceclient", (String)("" + unsupportedEncodingException.getMessage()));
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }

    public static String computeSHA256(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)SHA_256);
            messageDigest.update(string.getBytes(UTF_8));
            String string2 = Utility.byteArrayToHexString(messageDigest.digest());
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.e((String)"core-hceclient", (String)("" + noSuchAlgorithmException.getMessage()));
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e((String)"core-hceclient", (String)("" + unsupportedEncodingException.getMessage()));
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }

    public static MessageDigest getSHA1MessageDigest() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)SHA_1);
            return messageDigest;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.e((String)"core-hceclient", (String)("" + noSuchAlgorithmException.getMessage()));
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }
}

