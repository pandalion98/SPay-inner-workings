/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class AEADParameters
implements CipherParameters {
    private byte[] associatedText;
    private KeyParameter key;
    private int macSize;
    private byte[] nonce;

    public AEADParameters(KeyParameter keyParameter, int n2, byte[] arrby) {
        this(keyParameter, n2, arrby, null);
    }

    public AEADParameters(KeyParameter keyParameter, int n2, byte[] arrby, byte[] arrby2) {
        this.key = keyParameter;
        this.nonce = arrby;
        this.macSize = n2;
        this.associatedText = arrby2;
    }

    public byte[] getAssociatedText() {
        return this.associatedText;
    }

    public KeyParameter getKey() {
        return this.key;
    }

    public int getMacSize() {
        return this.macSize;
    }

    public byte[] getNonce() {
        return this.nonce;
    }
}

