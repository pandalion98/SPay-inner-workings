/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  javax.crypto.interfaces.PBEKey
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.jcajce;

import javax.crypto.interfaces.PBEKey;
import org.bouncycastle.jcajce.PKCS12Key;
import org.bouncycastle.util.Arrays;

public class PKCS12KeyWithParameters
extends PKCS12Key
implements PBEKey {
    private final int iterationCount;
    private final byte[] salt;

    public PKCS12KeyWithParameters(char[] arrc, byte[] arrby, int n2) {
        super(arrc);
        this.salt = Arrays.clone((byte[])arrby);
        this.iterationCount = n2;
    }

    public int getIterationCount() {
        return this.iterationCount;
    }

    public byte[] getSalt() {
        return this.salt;
    }
}

