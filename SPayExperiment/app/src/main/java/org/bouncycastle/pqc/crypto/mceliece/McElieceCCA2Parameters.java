/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;

public class McElieceCCA2Parameters
extends McElieceParameters {
    public Digest digest;

    public McElieceCCA2Parameters() {
        this.digest = new SHA256Digest();
    }

    public McElieceCCA2Parameters(int n, int n2) {
        super(n, n2);
        this.digest = new SHA256Digest();
    }

    public McElieceCCA2Parameters(Digest digest) {
        this.digest = digest;
    }

    public Digest getDigest() {
        return this.digest;
    }
}

