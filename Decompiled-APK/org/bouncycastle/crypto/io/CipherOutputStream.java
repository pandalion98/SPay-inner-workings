package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.modes.AEADBlockCipher;

public class CipherOutputStream extends FilterOutputStream {
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private BufferedBlockCipher bufferedBlockCipher;
    private final byte[] oneByte;
    private StreamCipher streamCipher;

    public CipherOutputStream(OutputStream outputStream, BufferedBlockCipher bufferedBlockCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.bufferedBlockCipher = bufferedBlockCipher;
    }

    public CipherOutputStream(OutputStream outputStream, StreamCipher streamCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.streamCipher = streamCipher;
    }

    public CipherOutputStream(OutputStream outputStream, AEADBlockCipher aEADBlockCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.aeadBlockCipher = aEADBlockCipher;
    }

    private void ensureCapacity(int i, boolean z) {
        if (z) {
            if (this.bufferedBlockCipher != null) {
                i = this.bufferedBlockCipher.getOutputSize(i);
            } else if (this.aeadBlockCipher != null) {
                i = this.aeadBlockCipher.getOutputSize(i);
            }
        } else if (this.bufferedBlockCipher != null) {
            i = this.bufferedBlockCipher.getUpdateOutputSize(i);
        } else if (this.aeadBlockCipher != null) {
            i = this.aeadBlockCipher.getUpdateOutputSize(i);
        }
        if (this.buf == null || this.buf.length < i) {
            this.buf = new byte[i];
        }
    }

    public void close() {
        IOException iOException;
        ensureCapacity(0, true);
        IOException iOException2 = null;
        try {
            int doFinal;
            if (this.bufferedBlockCipher != null) {
                doFinal = this.bufferedBlockCipher.doFinal(this.buf, 0);
                if (doFinal != 0) {
                    this.out.write(this.buf, 0, doFinal);
                }
            } else if (this.aeadBlockCipher != null) {
                doFinal = this.aeadBlockCipher.doFinal(this.buf, 0);
                if (doFinal != 0) {
                    this.out.write(this.buf, 0, doFinal);
                }
            }
        } catch (Throwable e) {
            iOException2 = new InvalidCipherTextIOException("Error finalising cipher data", e);
        } catch (Throwable e2) {
            iOException2 = new CipherIOException("Error closing stream: ", e2);
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
        if (this.streamCipher != null) {
            this.out.write(this.streamCipher.returnByte((byte) i));
        } else {
            write(this.oneByte, 0, 1);
        }
    }

    public void write(byte[] bArr) {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) {
        ensureCapacity(i2, false);
        int processBytes;
        if (this.bufferedBlockCipher != null) {
            processBytes = this.bufferedBlockCipher.processBytes(bArr, i, i2, this.buf, 0);
            if (processBytes != 0) {
                this.out.write(this.buf, 0, processBytes);
            }
        } else if (this.aeadBlockCipher != null) {
            processBytes = this.aeadBlockCipher.processBytes(bArr, i, i2, this.buf, 0);
            if (processBytes != 0) {
                this.out.write(this.buf, 0, processBytes);
            }
        } else {
            this.streamCipher.processBytes(bArr, i, i2, this.buf, 0);
            this.out.write(this.buf, 0, i2);
        }
    }
}
