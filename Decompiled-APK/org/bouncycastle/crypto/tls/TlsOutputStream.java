package org.bouncycastle.crypto.tls;

import java.io.OutputStream;

class TlsOutputStream extends OutputStream {
    private byte[] buf;
    private TlsProtocol handler;

    TlsOutputStream(TlsProtocol tlsProtocol) {
        this.buf = new byte[1];
        this.handler = tlsProtocol;
    }

    public void close() {
        this.handler.close();
    }

    public void flush() {
        this.handler.flush();
    }

    public void write(int i) {
        this.buf[0] = (byte) i;
        write(this.buf, 0, 1);
    }

    public void write(byte[] bArr, int i, int i2) {
        this.handler.writeData(bArr, i, i2);
    }
}
