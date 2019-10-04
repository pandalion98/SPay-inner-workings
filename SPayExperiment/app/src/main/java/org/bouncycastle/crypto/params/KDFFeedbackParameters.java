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

public final class KDFFeedbackParameters
implements DerivationParameters {
    private static final int UNUSED_R = -1;
    private final byte[] fixedInputData;
    private final byte[] iv;
    private final byte[] ki;
    private final int r;
    private final boolean useCounter;

    /*
     * Enabled aggressive block sorting
     */
    private KDFFeedbackParameters(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2, boolean bl) {
        if (arrby == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone((byte[])arrby);
        this.fixedInputData = arrby3 == null ? new byte[0] : Arrays.clone((byte[])arrby3);
        this.r = n2;
        this.iv = arrby2 == null ? new byte[0] : Arrays.clone((byte[])arrby2);
        this.useCounter = bl;
    }

    public static KDFFeedbackParameters createWithCounter(byte[] arrby, byte[] arrby2, byte[] arrby3, int n2) {
        if (n2 != 8 && n2 != 16 && n2 != 24 && n2 != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        return new KDFFeedbackParameters(arrby, arrby2, arrby3, n2, true);
    }

    public static KDFFeedbackParameters createWithoutCounter(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        return new KDFFeedbackParameters(arrby, arrby2, arrby3, -1, false);
    }

    public byte[] getFixedInputData() {
        return Arrays.clone((byte[])this.fixedInputData);
    }

    public byte[] getIV() {
        return this.iv;
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

