/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  org.bouncycastle.util.encoders.Hex
 */
package com.samsung.android.spayfw.payprovider.discover.util;

import com.samsung.android.spayfw.b.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class a {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] a(String string, String string2, String string3, int n2) {
        if ("SHA-256".compareTo(string3) == 0) {
            try {
                PKCS5S2ParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA256Digest());
                pKCS5S2ParametersGenerator.init(string.getBytes("UTF-8"), string2.getBytes(), n2);
                return ((KeyParameter)pKCS5S2ParametersGenerator.generateDerivedParameters(256)).getKey();
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                Log.c("DCSDK_CryptoUtils", unsupportedEncodingException.getMessage(), unsupportedEncodingException);
                do {
                    return null;
                    break;
                } while (true);
            }
        }
        Log.e("DCSDK_CryptoUtils", "Only SHA256 MAC Algorithm is supported at the moment");
        return null;
    }

    public static String aQ(String string) {
        if (string == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            messageDigest.update(string.getBytes());
            String string2 = Hex.toHexString((byte[])messageDigest.digest());
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.c("DCSDK_CryptoUtils", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return null;
        }
    }
}

