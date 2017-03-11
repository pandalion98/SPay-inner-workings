package com.americanexpress.mobilepayments.hceclient.utils.common;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    private static final String UTF_8 = "UTF-8";

    public static String computeSHA256(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(SHA_256);
            instance.update(str.getBytes(UTF_8));
            return Utility.byteArrayToHexString(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e(HCEClientConstants.TAG, BuildConfig.FLAVOR + e.getMessage());
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        } catch (UnsupportedEncodingException e2) {
            Log.e(HCEClientConstants.TAG, BuildConfig.FLAVOR + e2.getMessage());
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }

    public static String computeSHA1(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(SHA_1);
            instance.update(str.getBytes(UTF_8));
            return Utility.byteArrayToHexString(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e(HCEClientConstants.TAG, BuildConfig.FLAVOR + e.getMessage());
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        } catch (UnsupportedEncodingException e2) {
            Log.e(HCEClientConstants.TAG, BuildConfig.FLAVOR + e2.getMessage());
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }

    public static MessageDigest getSHA1MessageDigest() {
        try {
            return MessageDigest.getInstance(SHA_1);
        } catch (NoSuchAlgorithmException e) {
            Log.e(HCEClientConstants.TAG, BuildConfig.FLAVOR + e.getMessage());
            throw new HCEClientException(OperationStatus.SHA_MESSAGE_DIGEST_GENERATION_FAILED);
        }
    }
}
