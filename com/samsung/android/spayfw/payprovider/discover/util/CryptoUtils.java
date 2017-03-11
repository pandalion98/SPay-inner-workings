package com.samsung.android.spayfw.payprovider.discover.util;

import com.samsung.android.spayfw.p002b.Log;
import java.security.MessageDigest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.util.a */
public class CryptoUtils {
    public static String aQ(String str) {
        String str2 = null;
        if (str != null) {
            try {
                MessageDigest instance = MessageDigest.getInstance("SHA-256");
                instance.update(str.getBytes());
                str2 = Hex.toHexString(instance.digest());
            } catch (Throwable e) {
                Log.m284c("DCSDK_CryptoUtils", e.getMessage(), e);
            }
        }
        return str2;
    }

    public static byte[] m1056a(String str, String str2, String str3, int i) {
        if ("SHA-256".compareTo(str3) == 0) {
            try {
                PKCS5S2ParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA256Digest());
                pKCS5S2ParametersGenerator.init(str.getBytes("UTF-8"), str2.getBytes(), i);
                return ((KeyParameter) pKCS5S2ParametersGenerator.generateDerivedParameters(SkeinMac.SKEIN_256)).getKey();
            } catch (Throwable e) {
                Log.m284c("DCSDK_CryptoUtils", e.getMessage(), e);
            }
        } else {
            Log.m286e("DCSDK_CryptoUtils", "Only SHA256 MAC Algorithm is supported at the moment");
            return null;
        }
    }
}
