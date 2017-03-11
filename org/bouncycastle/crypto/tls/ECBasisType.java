package org.bouncycastle.crypto.tls;

public class ECBasisType {
    public static final short ec_basis_pentanomial = (short) 2;
    public static final short ec_basis_trinomial = (short) 1;

    public static boolean isValid(short s) {
        return s >= ec_basis_trinomial && s <= ec_basis_pentanomial;
    }
}
