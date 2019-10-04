/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.util.encoders;

import org.bouncycastle.util.encoders.Translator;

public class BufferedDecoder {
    protected byte[] buf;
    protected int bufOff;
    protected Translator translator;

    public BufferedDecoder(Translator translator, int n) {
        this.translator = translator;
        if (n % translator.getEncodedBlockSize() != 0) {
            throw new IllegalArgumentException("buffer size not multiple of input block size");
        }
        this.buf = new byte[n];
        this.bufOff = 0;
    }

    public int processByte(byte by, byte[] arrby, int n) {
        byte[] arrby2 = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        arrby2[n2] = by;
        int n3 = this.bufOff;
        int n4 = this.buf.length;
        int n5 = 0;
        if (n3 == n4) {
            int n6 = this.translator.decode(this.buf, 0, this.buf.length, arrby, n);
            this.bufOff = 0;
            n5 = n6;
        }
        return n5;
    }

    public int processBytes(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n4 = this.buf.length - this.bufOff;
        int n5 = 0;
        if (n2 > n4) {
            System.arraycopy((Object)arrby, (int)n, (Object)this.buf, (int)this.bufOff, (int)n4);
            int n6 = 0 + this.translator.decode(this.buf, 0, this.buf.length, arrby2, n3);
            this.bufOff = 0;
            int n7 = n2 - n4;
            int n8 = n + n4;
            int n9 = n3 + n6;
            int n10 = n7 - n7 % this.buf.length;
            int n11 = n6 + this.translator.decode(arrby, n8, n10, arrby2, n9);
            n2 = n7 - n10;
            n = n8 + n10;
            n5 = n11;
        }
        if (n2 != 0) {
            System.arraycopy((Object)arrby, (int)n, (Object)this.buf, (int)this.bufOff, (int)n2);
            this.bufOff = n2 + this.bufOff;
        }
        return n5;
    }
}

