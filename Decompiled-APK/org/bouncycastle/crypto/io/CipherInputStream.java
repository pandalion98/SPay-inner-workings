package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.SkippingCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class CipherInputStream extends FilterInputStream {
    private static final int INPUT_BUF_SIZE = 2048;
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private int bufOff;
    private BufferedBlockCipher bufferedBlockCipher;
    private boolean finalized;
    private byte[] inBuf;
    private byte[] markBuf;
    private int markBufOff;
    private long markPosition;
    private int maxBuf;
    private SkippingCipher skippingCipher;
    private StreamCipher streamCipher;

    public CipherInputStream(InputStream inputStream, BufferedBlockCipher bufferedBlockCipher) {
        this(inputStream, bufferedBlockCipher, (int) INPUT_BUF_SIZE);
    }

    public CipherInputStream(InputStream inputStream, BufferedBlockCipher bufferedBlockCipher, int i) {
        super(inputStream);
        this.bufferedBlockCipher = bufferedBlockCipher;
        this.inBuf = new byte[i];
        this.skippingCipher = bufferedBlockCipher instanceof SkippingCipher ? (SkippingCipher) bufferedBlockCipher : null;
    }

    public CipherInputStream(InputStream inputStream, StreamCipher streamCipher) {
        this(inputStream, streamCipher, (int) INPUT_BUF_SIZE);
    }

    public CipherInputStream(InputStream inputStream, StreamCipher streamCipher, int i) {
        super(inputStream);
        this.streamCipher = streamCipher;
        this.inBuf = new byte[i];
        this.skippingCipher = streamCipher instanceof SkippingCipher ? (SkippingCipher) streamCipher : null;
    }

    public CipherInputStream(InputStream inputStream, AEADBlockCipher aEADBlockCipher) {
        this(inputStream, aEADBlockCipher, (int) INPUT_BUF_SIZE);
    }

    public CipherInputStream(InputStream inputStream, AEADBlockCipher aEADBlockCipher, int i) {
        super(inputStream);
        this.aeadBlockCipher = aEADBlockCipher;
        this.inBuf = new byte[i];
        this.skippingCipher = aEADBlockCipher instanceof SkippingCipher ? (SkippingCipher) aEADBlockCipher : null;
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

    private void finaliseCipher() {
        try {
            this.finalized = true;
            ensureCapacity(0, true);
            if (this.bufferedBlockCipher != null) {
                this.maxBuf = this.bufferedBlockCipher.doFinal(this.buf, 0);
            } else if (this.aeadBlockCipher != null) {
                this.maxBuf = this.aeadBlockCipher.doFinal(this.buf, 0);
            } else {
                this.maxBuf = 0;
            }
        } catch (Throwable e) {
            throw new InvalidCipherTextIOException("Error finalising cipher", e);
        } catch (Exception e2) {
            throw new IOException("Error finalising cipher " + e2);
        }
    }

    private int nextChunk() {
        if (this.finalized) {
            return -1;
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        while (this.maxBuf == 0) {
            int read = this.in.read(this.inBuf);
            if (read == -1) {
                finaliseCipher();
                return this.maxBuf == 0 ? -1 : this.maxBuf;
            } else {
                ensureCapacity(read, false);
                if (this.bufferedBlockCipher != null) {
                    this.maxBuf = this.bufferedBlockCipher.processBytes(this.inBuf, 0, read, this.buf, 0);
                } else {
                    try {
                        if (this.aeadBlockCipher != null) {
                            this.maxBuf = this.aeadBlockCipher.processBytes(this.inBuf, 0, read, this.buf, 0);
                        } else {
                            this.streamCipher.processBytes(this.inBuf, 0, read, this.buf, 0);
                            this.maxBuf = read;
                        }
                    } catch (Throwable e) {
                        throw new CipherIOException("Error processing stream ", e);
                    }
                }
            }
        }
        return this.maxBuf;
    }

    public int available() {
        return this.maxBuf - this.bufOff;
    }

    public void close() {
        try {
            this.in.close();
            this.bufOff = 0;
            this.maxBuf = 0;
            this.markBufOff = 0;
            this.markPosition = 0;
            if (this.markBuf != null) {
                Arrays.fill(this.markBuf, (byte) 0);
                this.markBuf = null;
            }
            if (this.buf != null) {
                Arrays.fill(this.buf, (byte) 0);
                this.buf = null;
            }
            Arrays.fill(this.inBuf, (byte) 0);
        } finally {
            if (!this.finalized) {
                finaliseCipher();
            }
        }
    }

    public void mark(int i) {
        this.in.mark(i);
        if (this.skippingCipher != null) {
            this.markPosition = this.skippingCipher.getPosition();
        }
        if (this.buf != null) {
            this.markBuf = new byte[this.buf.length];
            System.arraycopy(this.buf, 0, this.markBuf, 0, this.buf.length);
        }
        this.markBufOff = this.bufOff;
    }

    public boolean markSupported() {
        return this.skippingCipher != null ? this.in.markSupported() : false;
    }

    public int read() {
        if (this.bufOff >= this.maxBuf && nextChunk() < 0) {
            return -1;
        }
        byte[] bArr = this.buf;
        int i = this.bufOff;
        this.bufOff = i + 1;
        return bArr[i] & GF2Field.MASK;
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) {
        if (this.bufOff >= this.maxBuf && nextChunk() < 0) {
            return -1;
        }
        int min = Math.min(i2, available());
        System.arraycopy(this.buf, this.bufOff, bArr, i, min);
        this.bufOff += min;
        return min;
    }

    public void reset() {
        if (this.skippingCipher == null) {
            throw new IOException("cipher must implement SkippingCipher to be used with reset()");
        }
        this.in.reset();
        this.skippingCipher.seekTo(this.markPosition);
        if (this.markBuf != null) {
            this.buf = this.markBuf;
        }
        this.bufOff = this.markBufOff;
    }

    public long skip(long j) {
        if (j <= 0) {
            return 0;
        }
        int available;
        if (this.skippingCipher != null) {
            available = available();
            if (j <= ((long) available)) {
                this.bufOff = (int) (((long) this.bufOff) + j);
                return j;
            }
            this.bufOff = this.maxBuf;
            long skip = this.in.skip(j - ((long) available));
            if (skip == this.skippingCipher.skip(skip)) {
                return skip + ((long) available);
            }
            throw new IOException("Unable to skip cipher " + skip + " bytes.");
        }
        available = (int) Math.min(j, (long) available());
        this.bufOff += available;
        return (long) available;
    }
}
