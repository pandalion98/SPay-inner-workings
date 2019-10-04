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

public final class KDFDoublePipelineIterationParameters
implements DerivationParameters {
    private static final int UNUSED_R = 32;
    private final byte[] fixedInputData;
    private final byte[] ki;
    private final int r;
    private final boolean useCounter;

    /*
     * Enabled aggressive block sorting
     */
    private KDFDoublePipelineIterationParameters(byte[] arrby, byte[] arrby2, int n2, boolean bl) {
        if (arrby == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone((byte[])arrby);
        this.fixedInputData = arrby2 == null ? new byte[0] : Arrays.clone((byte[])arrby2);
        if (n2 != 8 && n2 != 16 && n2 != 24 && n2 != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = n2;
        this.useCounter = bl;
    }

    public static KDFDoublePipelineIterationParameters createWithCounter(byte[] arrby, byte[] arrby2, int n2) {
        return new KDFDoublePipelineIterationParameters(arrby, arrby2, n2, true);
    }

    public static KDFDoublePipelineIterationParameters createWithoutCounter(byte[] arrby, byte[] arrby2) {
        return new KDFDoublePipelineIterationParameters(arrby, arrby2, 32, false);
    }

    public byte[] getFixedInputData() {
        return Arrays.clone((byte[])this.fixedInputData);
    }

    public byte[] getKI() {
        return this.ki;
    }

    public int getR() {
        return this.r;
    }

    public boolean useCounter() {
        return this.useCounter;
    }
}

