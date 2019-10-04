/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

public class GMSSRandom {
    private Digest messDigestTree;

    public GMSSRandom(Digest digest) {
        this.messDigestTree = digest;
    }

    private void addByteArrays(byte[] arrby, byte[] arrby2) {
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            int n2 = n + ((255 & arrby[i]) + (255 & arrby2[i]));
            arrby[i] = (byte)n2;
            n = (byte)(n2 >> 8);
        }
    }

    private void addOne(byte[] arrby) {
        int n = 1;
        for (int i = 0; i < arrby.length; ++i) {
            int n2 = n + (255 & arrby[i]);
            arrby[i] = (byte)n2;
            n = (byte)(n2 >> 8);
        }
    }

    public byte[] nextSeed(byte[] arrby) {
        new byte[arrby.length];
        this.messDigestTree.update(arrby, 0, arrby.length);
        byte[] arrby2 = new byte[this.messDigestTree.getDigestSize()];
        this.messDigestTree.doFinal(arrby2, 0);
        this.addByteArrays(arrby, arrby2);
        this.addOne(arrby);
        return arrby2;
    }
}

