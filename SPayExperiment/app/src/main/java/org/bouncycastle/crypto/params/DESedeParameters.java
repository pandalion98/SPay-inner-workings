/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.params.DESParameters;

public class DESedeParameters
extends DESParameters {
    public static final int DES_EDE_KEY_LENGTH = 24;

    public DESedeParameters(byte[] arrby) {
        super(arrby);
        if (DESedeParameters.isWeakKey(arrby, 0, arrby.length)) {
            throw new IllegalArgumentException("attempt to create weak DESede key");
        }
    }

    public static boolean isWeakKey(byte[] arrby, int n2) {
        return DESedeParameters.isWeakKey(arrby, n2, arrby.length - n2);
    }

    public static boolean isWeakKey(byte[] arrby, int n2, int n3) {
        while (n2 < n3) {
            if (DESParameters.isWeakKey(arrby, n2)) {
                return true;
            }
            n2 += 8;
        }
        return false;
    }
}

