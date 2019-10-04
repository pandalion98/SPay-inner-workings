/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.test;

public final class NumberParsing {
    private NumberParsing() {
    }

    public static int decodeIntFromHex(String string) {
        if (string.charAt(1) == 'x' || string.charAt(1) == 'X') {
            return Integer.parseInt((String)string.substring(2), (int)16);
        }
        return Integer.parseInt((String)string, (int)16);
    }

    public static long decodeLongFromHex(String string) {
        if (string.charAt(1) == 'x' || string.charAt(1) == 'X') {
            return Long.parseLong((String)string.substring(2), (int)16);
        }
        return Long.parseLong((String)string, (int)16);
    }
}

