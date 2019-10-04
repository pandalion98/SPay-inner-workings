/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.MessageDigest
 */
package org.bouncycastle.jcajce.provider.digest;

import java.security.MessageDigest;
import org.bouncycastle.crypto.Digest;

public class BCMessageDigest
extends MessageDigest {
    protected Digest digest;

    protected BCMessageDigest(Digest digest) {
        super(digest.getAlgorithmName());
        this.digest = digest;
    }

    public byte[] engineDigest() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        return arrby;
    }

    public void engineReset() {
        this.digest.reset();
    }

    public void engineUpdate(byte by) {
        this.digest.update(by);
    }

    public void engineUpdate(byte[] arrby, int n, int n2) {
        this.digest.update(arrby, n, n2);
    }
}

