/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 */
package org.bouncycastle.crypto.tls;

import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsProtocol;

class TlsOutputStream
extends OutputStream {
    private byte[] buf = new byte[1];
    private TlsProtocol handler;

    TlsOutputStream(TlsProtocol tlsProtocol) {
        this.handler = tlsProtocol;
    }

    public void close() {
        this.handler.close();
    }

    public void flush() {
        this.handler.flush();
    }

    public void write(int n2) {
        this.buf[0] = (byte)n2;
        this.write(this.buf, 0, 1);
    }

    public void write(byte[] arrby, int n2, int n3) {
        this.handler.writeData(arrby, n2, n3);
    }
}

