/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

public class Commitment {
    private final byte[] commitment;
    private final byte[] secret;

    public Commitment(byte[] arrby, byte[] arrby2) {
        this.secret = arrby;
        this.commitment = arrby2;
    }

    public byte[] getCommitment() {
        return this.commitment;
    }

    public byte[] getSecret() {
        return this.secret;
    }
}

