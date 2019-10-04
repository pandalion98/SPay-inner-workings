/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.util.Arrays;

public class DSAValidationParameters {
    private int counter;
    private byte[] seed;
    private int usageIndex;

    public DSAValidationParameters(byte[] arrby, int n2) {
        this(arrby, n2, -1);
    }

    public DSAValidationParameters(byte[] arrby, int n2, int n3) {
        this.seed = arrby;
        this.counter = n2;
        this.usageIndex = n3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        DSAValidationParameters dSAValidationParameters;
        block3 : {
            block2 : {
                if (!(object instanceof DSAValidationParameters)) break block2;
                dSAValidationParameters = (DSAValidationParameters)object;
                if (dSAValidationParameters.counter == this.counter) break block3;
            }
            return false;
        }
        return Arrays.areEqual((byte[])this.seed, (byte[])dSAValidationParameters.seed);
    }

    public int getCounter() {
        return this.counter;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public int getUsageIndex() {
        return this.usageIndex;
    }

    public int hashCode() {
        return this.counter ^ Arrays.hashCode((byte[])this.seed);
    }
}

