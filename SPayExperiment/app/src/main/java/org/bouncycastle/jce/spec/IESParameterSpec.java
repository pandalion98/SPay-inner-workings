/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.util.Arrays;

public class IESParameterSpec
implements AlgorithmParameterSpec {
    private int cipherKeySize;
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;
    private byte[] nonce;
    private boolean usePointCompression;

    public IESParameterSpec(byte[] arrby, byte[] arrby2, int n) {
        this(arrby, arrby2, n, -1, null, false);
    }

    public IESParameterSpec(byte[] arrby, byte[] arrby2, int n, int n2) {
        this(arrby, arrby2, n, n2, null, false);
    }

    public IESParameterSpec(byte[] arrby, byte[] arrby2, int n, int n2, byte[] arrby3) {
        this(arrby, arrby2, n, n2, arrby3, false);
    }

    /*
     * Enabled aggressive block sorting
     */
    public IESParameterSpec(byte[] arrby, byte[] arrby2, int n, int n2, byte[] arrby3, boolean bl) {
        if (arrby != null) {
            this.derivation = new byte[arrby.length];
            System.arraycopy((Object)arrby, (int)0, (Object)this.derivation, (int)0, (int)arrby.length);
        } else {
            this.derivation = null;
        }
        if (arrby2 != null) {
            this.encoding = new byte[arrby2.length];
            System.arraycopy((Object)arrby2, (int)0, (Object)this.encoding, (int)0, (int)arrby2.length);
        } else {
            this.encoding = null;
        }
        this.macKeySize = n;
        this.cipherKeySize = n2;
        this.nonce = Arrays.clone(arrby3);
        this.usePointCompression = bl;
    }

    public int getCipherKeySize() {
        return this.cipherKeySize;
    }

    public byte[] getDerivationV() {
        return Arrays.clone(this.derivation);
    }

    public byte[] getEncodingV() {
        return Arrays.clone(this.encoding);
    }

    public int getMacKeySize() {
        return this.macKeySize;
    }

    public byte[] getNonce() {
        return Arrays.clone(this.nonce);
    }

    public boolean getPointCompression() {
        return this.usePointCompression;
    }

    public void setPointCompression(boolean bl) {
        this.usePointCompression = bl;
    }
}

