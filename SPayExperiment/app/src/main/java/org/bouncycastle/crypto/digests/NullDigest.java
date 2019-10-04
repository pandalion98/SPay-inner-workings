/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.digests;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.Digest;

public class NullDigest
implements Digest {
    private ByteArrayOutputStream bOut = new ByteArrayOutputStream();

    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte[] arrby2 = this.bOut.toByteArray();
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        this.reset();
        return arrby2.length;
    }

    @Override
    public String getAlgorithmName() {
        return "NULL";
    }

    @Override
    public int getDigestSize() {
        return this.bOut.size();
    }

    @Override
    public void reset() {
        this.bOut.reset();
    }

    @Override
    public void update(byte by) {
        this.bOut.write((int)by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.bOut.write(arrby, n2, n3);
    }
}

