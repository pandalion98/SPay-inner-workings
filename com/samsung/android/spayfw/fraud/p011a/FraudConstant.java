package com.samsung.android.spayfw.fraud.p011a;

import android.os.Build;
import android.util.Base64;
import com.samsung.android.spayfw.p002b.Log;
import java.security.MessageDigest;

/* renamed from: com.samsung.android.spayfw.fraud.a.e */
public class FraudConstant {
    public static final byte[] salt;

    static {
        byte[] bytes;
        if (Build.SERIAL != null) {
            bytes = Build.SERIAL.getBytes();
        } else {
            bytes = "This is a Samsung device".getBytes();
        }
        salt = bytes;
    }

    public static String m697b(String str, byte[] bArr) {
        if (str == null) {
            return "null";
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.reset();
            instance.update(bArr);
            byte[] digest = instance.digest(str.getBytes());
            for (int i = 0; i < 3; i++) {
                instance.reset();
                digest = instance.digest(digest);
            }
            return Base64.encodeToString(digest, 2);
        } catch (Throwable e) {
            Log.m284c("FraudConstant", e.getMessage(), e);
            return "null";
        }
    }
}
