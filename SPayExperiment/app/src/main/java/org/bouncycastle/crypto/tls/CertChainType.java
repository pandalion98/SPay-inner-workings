/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public class CertChainType {
    public static final short individual_certs = 0;
    public static final short pkipath = 1;

    public static boolean isValid(short s2) {
        return s2 >= 0 && s2 <= 1;
    }
}

