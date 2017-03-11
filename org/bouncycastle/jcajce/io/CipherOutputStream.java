package org.bouncycastle.jcajce.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Cipher;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;

public class CipherOutputStream extends FilterOutputStream {
    private final Cipher cipher;
    private final byte[] oneByte;

    public CipherOutputStream(OutputStream outputStream, Cipher cipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.cipher = cipher;
    }

    public void close() {
        IOException iOException;
        IOException iOException2 = null;
        try {
            byte[] doFinal = this.cipher.doFinal();
            if (doFinal != null) {
                this.out.write(doFinal);
            }
        } catch (Throwable e) {
            iOException2 = new InvalidCipherTextIOException("Error during cipher finalisation", e);
        } catch (Exception e2) {
            iOException2 = new IOException("Error closing stream: " + e2);
        }
        try {
            flush();
            this.out.close();
            iOException = iOException2;
        } catch (IOException e3) {
            iOException = e3;
            if (iOException2 != null) {
                iOException = iOException2;
            }
        }
        if (iOException != null) {
            throw iOException;
        }
    }

    public void flush() {
        this.out.flush();
    }

    public void write(int i) {
        this.oneByte[0] = (byte) i;
        write(this.oneByte, 0, 1);
    }

    public void write(byte[] bArr, int i, int i2) {
        byte[] update = this.cipher.update(bArr, i, i2);
        if (update != null) {
            this.out.write(update);
        }
    }
}
