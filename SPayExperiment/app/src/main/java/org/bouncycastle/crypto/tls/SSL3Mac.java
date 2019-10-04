/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

public class SSL3Mac
implements Mac {
    static final byte[] IPAD = SSL3Mac.genPad((byte)54, 48);
    private static final byte IPAD_BYTE = 54;
    static final byte[] OPAD = SSL3Mac.genPad((byte)92, 48);
    private static final byte OPAD_BYTE = 92;
    private Digest digest;
    private int padLength;
    private byte[] secret;

    public SSL3Mac(Digest digest) {
        this.digest = digest;
        if (digest.getDigestSize() == 20) {
            this.padLength = 40;
            return;
        }
        this.padLength = 48;
    }

    private static byte[] genPad(byte by, int n2) {
        byte[] arrby = new byte[n2];
        Arrays.fill((byte[])arrby, (byte)by);
        return arrby;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        this.digest.update(this.secret, 0, this.secret.length);
        this.digest.update(OPAD, 0, this.padLength);
        this.digest.update(arrby2, 0, arrby2.length);
        int n3 = this.digest.doFinal(arrby, n2);
        this.reset();
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/SSL3MAC";
    }

    @Override
    public int getMacSize() {
        return this.digest.getDigestSize();
    }

    public Digest getUnderlyingDigest() {
        return this.digest;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.secret = Arrays.clone((byte[])((KeyParameter)cipherParameters).getKey());
        this.reset();
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.digest.update(this.secret, 0, this.secret.length);
        this.digest.update(IPAD, 0, this.padLength);
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }
}

