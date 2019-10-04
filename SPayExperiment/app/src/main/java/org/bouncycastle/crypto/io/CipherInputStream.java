/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.SkippingCipher;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.io.CipherIOException;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.util.Arrays;

public class CipherInputStream
extends FilterInputStream {
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
        this(inputStream, bufferedBlockCipher, 2048);
    }

    /*
     * Enabled aggressive block sorting
     */
    public CipherInputStream(InputStream inputStream, BufferedBlockCipher bufferedBlockCipher, int n2) {
        super(inputStream);
        this.bufferedBlockCipher = bufferedBlockCipher;
        this.inBuf = new byte[n2];
        SkippingCipher skippingCipher = bufferedBlockCipher instanceof SkippingCipher ? (SkippingCipher)((Object)bufferedBlockCipher) : null;
        this.skippingCipher = skippingCipher;
    }

    public CipherInputStream(InputStream inputStream, StreamCipher streamCipher) {
        this(inputStream, streamCipher, 2048);
    }

    /*
     * Enabled aggressive block sorting
     */
    public CipherInputStream(InputStream inputStream, StreamCipher streamCipher, int n2) {
        super(inputStream);
        this.streamCipher = streamCipher;
        this.inBuf = new byte[n2];
        SkippingCipher skippingCipher = streamCipher instanceof SkippingCipher ? (SkippingCipher)((Object)streamCipher) : null;
        this.skippingCipher = skippingCipher;
    }

    public CipherInputStream(InputStream inputStream, AEADBlockCipher aEADBlockCipher) {
        this(inputStream, aEADBlockCipher, 2048);
    }

    /*
     * Enabled aggressive block sorting
     */
    public CipherInputStream(InputStream inputStream, AEADBlockCipher aEADBlockCipher, int n2) {
        super(inputStream);
        this.aeadBlockCipher = aEADBlockCipher;
        this.inBuf = new byte[n2];
        SkippingCipher skippingCipher = aEADBlockCipher instanceof SkippingCipher ? (SkippingCipher)((Object)aEADBlockCipher) : null;
        this.skippingCipher = skippingCipher;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ensureCapacity(int n2, boolean bl) {
        if (bl) {
            if (this.bufferedBlockCipher != null) {
                n2 = this.bufferedBlockCipher.getOutputSize(n2);
            } else if (this.aeadBlockCipher != null) {
                n2 = this.aeadBlockCipher.getOutputSize(n2);
            }
        } else if (this.bufferedBlockCipher != null) {
            n2 = this.bufferedBlockCipher.getUpdateOutputSize(n2);
        } else if (this.aeadBlockCipher != null) {
            n2 = this.aeadBlockCipher.getUpdateOutputSize(n2);
        }
        if (this.buf == null || this.buf.length < n2) {
            this.buf = new byte[n2];
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void finaliseCipher() {
        try {
            this.finalized = true;
            this.ensureCapacity(0, true);
            if (this.bufferedBlockCipher != null) {
                this.maxBuf = this.bufferedBlockCipher.doFinal(this.buf, 0);
                return;
            }
            if (this.aeadBlockCipher != null) {
                this.maxBuf = this.aeadBlockCipher.doFinal(this.buf, 0);
                return;
            }
            this.maxBuf = 0;
            return;
        }
        catch (InvalidCipherTextException invalidCipherTextException) {
            throw new InvalidCipherTextIOException("Error finalising cipher", (Throwable)((Object)invalidCipherTextException));
        }
        catch (Exception exception) {
            throw new IOException("Error finalising cipher " + (Object)((Object)exception));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int nextChunk() {
        if (this.finalized) {
            return -1;
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        while (this.maxBuf == 0) {
            int n2 = this.in.read(this.inBuf);
            if (n2 == -1) {
                this.finaliseCipher();
                if (this.maxBuf == 0) {
                    return -1;
                }
                return this.maxBuf;
            }
            try {
                this.ensureCapacity(n2, false);
                if (this.bufferedBlockCipher != null) {
                    this.maxBuf = this.bufferedBlockCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
                    continue;
                }
                if (this.aeadBlockCipher != null) {
                    this.maxBuf = this.aeadBlockCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
                    continue;
                }
            }
            catch (Exception exception) {
                throw new CipherIOException("Error processing stream ", exception);
            }
            this.streamCipher.processBytes(this.inBuf, 0, n2, this.buf, 0);
            this.maxBuf = n2;
        }
        return this.maxBuf;
    }

    public int available() {
        return this.maxBuf - this.bufOff;
    }

    public void close() {
        this.in.close();
        this.bufOff = 0;
        this.maxBuf = 0;
        this.markBufOff = 0;
        this.markPosition = 0L;
        if (this.markBuf != null) {
            Arrays.fill((byte[])this.markBuf, (byte)0);
            this.markBuf = null;
        }
        if (this.buf != null) {
            Arrays.fill((byte[])this.buf, (byte)0);
            this.buf = null;
        }
        Arrays.fill((byte[])this.inBuf, (byte)0);
        return;
        finally {
            if (!this.finalized) {
                this.finaliseCipher();
            }
        }
    }

    public void mark(int n2) {
        this.in.mark(n2);
        if (this.skippingCipher != null) {
            this.markPosition = this.skippingCipher.getPosition();
        }
        if (this.buf != null) {
            this.markBuf = new byte[this.buf.length];
            System.arraycopy((Object)this.buf, (int)0, (Object)this.markBuf, (int)0, (int)this.buf.length);
        }
        this.markBufOff = this.bufOff;
    }

    public boolean markSupported() {
        if (this.skippingCipher != null) {
            return this.in.markSupported();
        }
        return false;
    }

    public int read() {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        byte[] arrby = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        return 255 & arrby[n2];
    }

    public int read(byte[] arrby) {
        return this.read(arrby, 0, arrby.length);
    }

    public int read(byte[] arrby, int n2, int n3) {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        int n4 = Math.min((int)n3, (int)this.available());
        System.arraycopy((Object)this.buf, (int)this.bufOff, (Object)arrby, (int)n2, (int)n4);
        this.bufOff = n4 + this.bufOff;
        return n4;
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

    public long skip(long l2) {
        if (l2 <= 0L) {
            return 0L;
        }
        if (this.skippingCipher != null) {
            int n2 = this.available();
            if (l2 <= (long)n2) {
                this.bufOff = (int)(l2 + (long)this.bufOff);
                return l2;
            }
            this.bufOff = this.maxBuf;
            long l3 = this.in.skip(l2 - (long)n2);
            if (l3 != this.skippingCipher.skip(l3)) {
                throw new IOException("Unable to skip cipher " + l3 + " bytes.");
            }
            return l3 + (long)n2;
        }
        int n3 = (int)Math.min((long)l2, (long)this.available());
        this.bufOff = n3 + this.bufOff;
        return n3;
    }
}

