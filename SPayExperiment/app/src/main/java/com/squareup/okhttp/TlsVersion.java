/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp;

public final class TlsVersion
extends Enum<TlsVersion> {
    private static final /* synthetic */ TlsVersion[] $VALUES;
    public static final /* enum */ TlsVersion SSL_3_0;
    public static final /* enum */ TlsVersion TLS_1_0;
    public static final /* enum */ TlsVersion TLS_1_1;
    public static final /* enum */ TlsVersion TLS_1_2;
    final String javaName;

    static {
        TLS_1_2 = new TlsVersion("TLSv1.2");
        TLS_1_1 = new TlsVersion("TLSv1.1");
        TLS_1_0 = new TlsVersion("TLSv1");
        SSL_3_0 = new TlsVersion("SSLv3");
        TlsVersion[] arrtlsVersion = new TlsVersion[]{TLS_1_2, TLS_1_1, TLS_1_0, SSL_3_0};
        $VALUES = arrtlsVersion;
    }

    private TlsVersion(String string2) {
        this.javaName = string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static TlsVersion forJavaName(String string) {
        int n2 = -1;
        switch (string.hashCode()) {
            case -503070502: {
                if (!string.equals((Object)"TLSv1.2")) break;
                n2 = 0;
                break;
            }
            case -503070503: {
                if (!string.equals((Object)"TLSv1.1")) break;
                n2 = 1;
                break;
            }
            case 79923350: {
                if (!string.equals((Object)"TLSv1")) break;
                n2 = 2;
                break;
            }
            case 79201641: {
                if (!string.equals((Object)"SSLv3")) break;
                n2 = 3;
                break;
            }
        }
        switch (n2) {
            default: {
                throw new IllegalArgumentException("Unexpected TLS version: " + string);
            }
            case 0: {
                return TLS_1_2;
            }
            case 1: {
                return TLS_1_1;
            }
            case 2: {
                return TLS_1_0;
            }
            case 3: 
        }
        return SSL_3_0;
    }

    public static TlsVersion valueOf(String string) {
        return (TlsVersion)Enum.valueOf(TlsVersion.class, (String)string);
    }

    public static TlsVersion[] values() {
        return (TlsVersion[])$VALUES.clone();
    }

    public String javaName() {
        return this.javaName;
    }
}

