/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.util.Base64
 *  java.lang.Object
 *  java.lang.String
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 */
package com.samsung.android.spayfw.fraud.a;

import android.os.Build;
import android.util.Base64;
import com.samsung.android.spayfw.b.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class e {
    public static final byte[] salt;

    /*
     * Enabled aggressive block sorting
     */
    static {
        byte[] arrby = Build.SERIAL != null ? Build.SERIAL.getBytes() : "This is a Samsung device".getBytes();
        salt = arrby;
    }

    public static String b(String string, byte[] arrby) {
        byte[] arrby2;
        MessageDigest messageDigest;
        if (string == null) {
            return "null";
        }
        try {
            messageDigest = MessageDigest.getInstance((String)"SHA-256");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            Log.c("FraudConstant", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return "null";
        }
        messageDigest.reset();
        messageDigest.update(arrby);
        arrby2 = messageDigest.digest(string.getBytes());
        for (int i2 = 0; i2 < 3; ++i2) {
            messageDigest.reset();
            arrby2 = messageDigest.digest(arrby2);
        }
        return Base64.encodeToString((byte[])arrby2, (int)2);
    }
}

