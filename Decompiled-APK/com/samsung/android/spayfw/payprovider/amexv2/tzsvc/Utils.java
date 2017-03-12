package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.util.Base64;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.i */
public class Utils {
    private static final char[] HEX_ARRAY;

    static {
        HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    }

    public static final String toBase64(byte[] bArr) {
        return (bArr == null || bArr.length == 0) ? null : Base64.encodeToString(bArr, 2);
    }

    public static final byte[] fromBase64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        return Base64.decode(str, 0);
    }

    public static final String convertToPem(byte[] bArr, boolean z) {
        String str = "-----BEGIN CERTIFICATE-----" + Base64.encodeToString(bArr, 2).replaceAll("(.{64})", "$1") + "-----END CERTIFICATE-----";
        if (z) {
            return Base64.encodeToString(str.getBytes(), 2);
        }
        return str;
    }

    static String[] splitPem(String str) {
        String str2 = "-----BEGIN CERTIFICATE-----";
        String[] split = str.split(str2);
        String[] strArr = new String[(split.length - 1)];
        for (int i = 1; i < split.length; i++) {
            strArr[i - 1] = Utils.stripPem(str2 + split[i]);
        }
        return strArr;
    }

    public static final String stripPem(String str) {
        return str.replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR).replace("\n", BuildConfig.FLAVOR);
    }

    public static final List<byte[]> getDerChain(String str) {
        List<byte[]> arrayList = new ArrayList();
        String[] splitPem = Utils.splitPem(str);
        for (String stripPem : splitPem) {
            arrayList.add(Base64.decode(Utils.stripPem(stripPem), 0));
        }
        return arrayList;
    }

    public static final String encodeHex(byte[] bArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr = new char[(length << 1)];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr[i] = HEX_ARRAY[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = HEX_ARRAY[bArr[i2] & 15];
        }
        return new String(cArr);
    }
}
