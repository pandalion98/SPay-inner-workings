package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

class TlsInputStream extends InputStream {
    private byte[] buf;
    private TlsProtocol handler;

    TlsInputStream(TlsProtocol tlsProtocol) {
        this.buf = new byte[1];
        this.handler = null;
        this.handler = tlsProtocol;
    }

    public int available() {
        return this.handler.applicationDataAvailable();
    }

    public void close() {
        this.handler.close();
    }

    public int read() {
        return read(this.buf) < 0 ? -1 : this.buf[0] & GF2Field.MASK;
    }

    public int read(byte[] bArr, int i, int i2) {
        return this.handler.readApplicationData(bArr, i, i2);
    }
}
