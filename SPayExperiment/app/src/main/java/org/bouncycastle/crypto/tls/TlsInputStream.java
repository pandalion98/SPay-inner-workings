/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import org.bouncycastle.crypto.tls.TlsProtocol;

class TlsInputStream
extends InputStream {
    private byte[] buf = new byte[1];
    private TlsProtocol handler = null;

    TlsInputStream(TlsProtocol tlsProtocol) {
        this.handler = tlsProtocol;
    }

    public int available() {
        return this.handler.applicationDataAvailable();
    }

    public void close() {
        this.handler.close();
    }

    public int read() {
        if (this.read(this.buf) < 0) {
            return -1;
        }
        return 255 & this.buf[0];
    }

    public int read(byte[] arrby, int n2, int n3) {
        return this.handler.readApplicationData(arrby, n2, n3);
    }
}

