/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 */
package org.bouncycastle.crypto.io;

import java.io.OutputStream;
import org.bouncycastle.crypto.Digest;

public class DigestOutputStream
extends OutputStream {
    protected Digest digest;

    public DigestOutputStream(Digest digest) {
        this.digest = digest;
    }

    public byte[] getDigest() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        return arrby;
    }

    public void write(int n2) {
        this.digest.update((byte)n2);
    }

    public void write(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }
}

