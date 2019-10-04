/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterOutputStream
 *  java.io.OutputStream
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package org.bouncycastle.util.test;

import java.io.FilterOutputStream;
import java.io.OutputStream;

public class UncloseableOutputStream
extends FilterOutputStream {
    public UncloseableOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void close() {
        throw new RuntimeException("close() called on UncloseableOutputStream");
    }

    public void write(byte[] arrby, int n, int n2) {
        this.out.write(arrby, n, n2);
    }
}

