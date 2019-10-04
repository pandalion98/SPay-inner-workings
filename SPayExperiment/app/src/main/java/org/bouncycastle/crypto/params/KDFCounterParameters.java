/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.util.Arrays;

public final class KDFCounterParameters
implements DerivationParameters {
    private byte[] fixedInputDataCounterPrefix;
    private byte[] fixedInputDataCounterSuffix;
    private byte[] ki;
    private int r;

    public KDFCounterParameters(byte[] arrby, byte[] arrby2, int n2) {
        this(arrby, null, arrby2, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public KDFCounterParameters(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2) {
        if (arrby == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone((byte[])arrby);
        this.fixedInputDataCounterPrefix = arrby2 == null ? new byte[0] : Arrays.clone((byte[])arrby2);
        this.fixedInputDataCounterSuffix = arrby3 == null ? new byte[0] : Arrays.clone((byte[])arrby3);
        if (n2 != 8 && n2 != 16 && n2 != 24 && n2 != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = n2;
    }

    public byte[] getFixedInputData() {
        return Arrays.clone((byte[])this.fixedInputDataCounterSuffix);
    }

    public byte[] getFixedInputDataCounterPrefix() {
        return Arrays.clone((byte[])this.fixedInputDataCounterPrefix);
    }

    public byte[] getFixedInputDataCounterSuffix() {
        return Arrays.clone((byte[])this.fixedInputDataCounterSuffix);
    }

    public byte[] getKI() {
        return this.ki;
    }

    public int getR() {
        return this.r;
    }
}

