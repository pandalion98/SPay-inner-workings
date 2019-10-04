/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import java.security.SecureRandom;

public class RandomNumberGenerator {
    public static byte[] random(int n2) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] arrby = new byte[n2];
        secureRandom.nextBytes(arrby);
        return arrby;
    }
}

