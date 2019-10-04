/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterInputStream
 *  java.io.InputStream
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import org.bouncycastle.crypto.Digest;

public class DigestInputStream
extends FilterInputStream {
    protected Digest digest;

    public DigestInputStream(InputStream inputStream, Digest digest) {
        super(inputStream);
        this.digest = digest;
    }

    public Digest getDigest() {
        return this.digest;
    }

    public int read() {
        int n2 = this.in.read();
        if (n2 >= 0) {
            this.digest.update((byte)n2);
        }
        return n2;
    }

    public int read(byte[] arrby, int n2, int n3) {
        int n4 = this.in.read(arrby, n2, n3);
        if (n4 > 0) {
            this.digest.update(arrby, n2, n4);
        }
        return n4;
    }
}

