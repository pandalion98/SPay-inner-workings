/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

public class AlertLevel {
    public static final short fatal = 2;
    public static final short warning = 1;

    public static String getName(short s2) {
        switch (s2) {
            default: {
                return "UNKNOWN";
            }
            case 1: {
                return "warning";
            }
            case 2: 
        }
        return "fatal";
    }

    public static String getText(short s2) {
        return AlertLevel.getName(s2) + "(" + s2 + ")";
    }
}

