/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 */
package org.bouncycastle.util.io;

import java.io.OutputStream;

public class TeeOutputStream
extends OutputStream {
    private OutputStream output1;
    private OutputStream output2;

    public TeeOutputStream(OutputStream outputStream, OutputStream outputStream2) {
        this.output1 = outputStream;
        this.output2 = outputStream2;
    }

    public void close() {
        this.output1.close();
        this.output2.close();
    }

    public void flush() {
        this.output1.flush();
        this.output2.flush();
    }

    public void write(int n) {
        this.output1.write(n);
        this.output2.write(n);
    }

    public void write(byte[] arrby) {
        this.output1.write(arrby);
        this.output2.write(arrby);
    }

    public void write(byte[] arrby, int n, int n2) {
        this.output1.write(arrby, n, n2);
        this.output2.write(arrby, n, n2);
    }
}

