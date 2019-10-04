/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 */
package org.bouncycastle.util.io;

import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream
extends InputStream {
    private final InputStream input;
    private final OutputStream output;

    public TeeInputStream(InputStream inputStream, OutputStream outputStream) {
        this.input = inputStream;
        this.output = outputStream;
    }

    public void close() {
        this.input.close();
        this.output.close();
    }

    public OutputStream getOutputStream() {
        return this.output;
    }

    public int read() {
        int n = this.input.read();
        if (n >= 0) {
            this.output.write(n);
        }
        return n;
    }

    public int read(byte[] arrby) {
        return this.read(arrby, 0, arrby.length);
    }

    public int read(byte[] arrby, int n, int n2) {
        int n3 = this.input.read(arrby, n, n2);
        if (n3 > 0) {
            this.output.write(arrby, n, n3);
        }
        return n3;
    }
}

