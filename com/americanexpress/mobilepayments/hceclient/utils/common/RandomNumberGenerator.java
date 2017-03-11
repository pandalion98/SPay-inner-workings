package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.security.SecureRandom;

public class RandomNumberGenerator {
    public static byte[] random(int i) {
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }
}
