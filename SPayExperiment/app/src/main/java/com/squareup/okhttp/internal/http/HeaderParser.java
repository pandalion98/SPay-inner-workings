/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp.internal.http;

public final class HeaderParser {
    private HeaderParser() {
    }

    public static int parseSeconds(String string, int n2) {
        long l2;
        block3 : {
            try {
                l2 = Long.parseLong((String)string);
                if (l2 > Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                }
                if (l2 >= 0L) break block3;
                return 0;
            }
            catch (NumberFormatException numberFormatException) {
                return n2;
            }
        }
        return (int)l2;
    }

    public static int skipUntil(String string, int n2, String string2) {
        while (n2 < string.length() && string2.indexOf((int)string.charAt(n2)) == -1) {
            ++n2;
        }
        return n2;
    }

    public static int skipWhitespace(String string, int n2) {
        char c2;
        while (n2 < string.length() && ((c2 = string.charAt(n2)) == ' ' || c2 == '\t')) {
            ++n2;
        }
        return n2;
    }
}

