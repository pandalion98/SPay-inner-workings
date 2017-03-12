package org.bouncycastle.util.io;

import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends InputStream {
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
        int read = this.input.read();
        if (read >= 0) {
            this.output.write(read);
        }
        return read;
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) {
        int read = this.input.read(bArr, i, i2);
        if (read > 0) {
            this.output.write(bArr, i, read);
        }
        return read;
    }
}
