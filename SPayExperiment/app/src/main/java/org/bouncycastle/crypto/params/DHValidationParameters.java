/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.util.Arrays;

public class DHValidationParameters {
    private int counter;
    private byte[] seed;

    public DHValidationParameters(byte[] arrby, int n2) {
        this.seed = arrby;
        this.counter = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        DHValidationParameters dHValidationParameters;
        block3 : {
            block2 : {
                if (!(object instanceof DHValidationParameters)) break block2;
                dHValidationParameters = (DHValidationParameters)object;
                if (dHValidationParameters.counter == this.counter) break block3;
            }
            return false;
        }
        return Arrays.areEqual((byte[])this.seed, (byte[])dHValidationParameters.seed);
    }

    public int getCounter() {
        return this.counter;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    public int hashCode() {
        return this.counter ^ Arrays.hashCode((byte[])this.seed);
    }
}

