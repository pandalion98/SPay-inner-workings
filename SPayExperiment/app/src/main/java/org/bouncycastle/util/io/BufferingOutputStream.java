/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.util.io;

import java.io.OutputStream;
import org.bouncycastle.util.Arrays;

public class BufferingOutputStream
extends OutputStream {
    private final byte[] buf;
    private int bufOff;
    private final OutputStream other;

    public BufferingOutputStream(OutputStream outputStream) {
        this.other = outputStream;
        this.buf = new byte[4096];
    }

    public BufferingOutputStream(OutputStream outputStream, int n) {
        this.other = outputStream;
        this.buf = new byte[n];
    }

    public void close() {
        this.flush();
        this.other.close();
    }

    public void flush() {
        this.other.write(this.buf, 0, this.bufOff);
        this.bufOff = 0;
        Arrays.fill(this.buf, (byte)0);
    }

    public void write(int n) {
        byte[] arrby = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        arrby[n2] = (byte)n;
        if (this.bufOff == this.buf.length) {
            this.flush();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(byte[] arrby, int n, int n2) {
        if (n2 < this.buf.length - this.bufOff) {
            System.arraycopy((Object)arrby, (int)n, (Object)this.buf, (int)this.bufOff, (int)n2);
            this.bufOff = n2 + this.bufOff;
            return;
        } else {
            int n3;
            int n4 = this.buf.length - this.bufOff;
            System.arraycopy((Object)arrby, (int)n, (Object)this.buf, (int)this.bufOff, (int)n4);
            this.bufOff = n4 + this.bufOff;
            this.flush();
            int n5 = n + n4;
            for (n3 = n2 - n4; n3 >= this.buf.length; n5 += this.buf.length, n3 -= this.buf.length) {
                this.other.write(arrby, n5, this.buf.length);
            }
            if (n3 <= 0) return;
            {
                System.arraycopy((Object)arrby, (int)n5, (Object)this.buf, (int)this.bufOff, (int)n3);
                this.bufOff = n3 + this.bufOff;
                return;
            }
        }
    }
}

