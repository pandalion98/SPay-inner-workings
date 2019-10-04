/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 */
package org.bouncycastle.crypto.io;

import java.io.OutputStream;
import org.bouncycastle.crypto.Signer;

public class SignerOutputStream
extends OutputStream {
    protected Signer signer;

    public SignerOutputStream(Signer signer) {
        this.signer = signer;
    }

    public Signer getSigner() {
        return this.signer;
    }

    public void write(int n2) {
        this.signer.update((byte)n2);
    }

    public void write(byte[] arrby, int n2, int n3) {
        this.signer.update(arrby, n2, n3);
    }
}

