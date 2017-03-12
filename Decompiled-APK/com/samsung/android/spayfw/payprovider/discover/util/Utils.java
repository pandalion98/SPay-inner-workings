package com.samsung.android.spayfw.payprovider.discover.util;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.util.b */
public class Utils {
    public static String byteArrayToHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & GF2Field.MASK)}));
        }
        return stringBuilder.toString();
    }
}
