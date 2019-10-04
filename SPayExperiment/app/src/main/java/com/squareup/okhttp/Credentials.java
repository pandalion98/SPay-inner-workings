/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.UnsupportedEncodingException
 *  java.lang.AssertionError
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp;

import java.io.UnsupportedEncodingException;
import okio.ByteString;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String string, String string2) {
        try {
            String string3 = ByteString.of((string + ":" + string2).getBytes("ISO-8859-1")).base64();
            String string4 = "Basic " + string3;
            return string4;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError();
        }
    }
}

