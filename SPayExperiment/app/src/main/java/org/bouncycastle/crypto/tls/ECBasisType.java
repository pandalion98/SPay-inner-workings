/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

public class ECBasisType {
    public static final short ec_basis_pentanomial = 2;
    public static final short ec_basis_trinomial = 1;

    public static boolean isValid(short s2) {
        return s2 >= 1 && s2 <= 2;
    }
}

