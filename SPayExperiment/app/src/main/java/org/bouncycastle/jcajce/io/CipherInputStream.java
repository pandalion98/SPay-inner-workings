/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterInputStream
 *  java.io.InputStream
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.GeneralSecurityException
 *  javax.crypto.Cipher
 */
package org.bouncycastle.jcajce.io;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;

public class CipherInputStream
extends FilterInputStream {
    private byte[] buf;
    private int bufOff;
    private final Cipher cipher;
    private boolean finalized = false;
    private final byte[] inputBuffer = new byte[512];
    private int maxBuf;

    public CipherInputStream(InputStream inputStream, Cipher cipher) {
        super(inputStream);
        this.cipher = cipher;
    }

    private byte[] finaliseCipher() {
        try {
            this.finalized = true;
            byte[] arrby = this.cipher.doFinal();
            return arrby;
        }
        catch (GeneralSecurityException generalSecurityException) {
            throw new InvalidCipherTextIOException("Error finalising cipher", generalSecurityException);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int nextChunk() {
        block4 : {
            if (!this.finalized) {
                this.bufOff = 0;
                this.maxBuf = 0;
                do {
                    if (this.maxBuf != 0) {
                        return this.maxBuf;
                    }
                    int n2 = this.in.read(this.inputBuffer);
                    if (n2 == -1) {
                        this.buf = this.finaliseCipher();
                        if (this.buf == null || this.buf.length == 0) break;
                        break block4;
                    }
                    this.buf = this.cipher.update(this.inputBuffer, 0, n2);
                    if (this.buf == null) continue;
                    this.maxBuf = this.buf.length;
                } while (true);
            }
            return -1;
        }
        this.maxBuf = this.buf.length;
        return this.maxBuf;
    }

    public int available() {
        return this.maxBuf - this.bufOff;
    }

    public void close() {
        this.in.close();
        this.bufOff = 0;
        this.maxBuf = 0;
        return;
        finally {
            if (!this.finalized) {
                this.finaliseCipher();
            }
        }
    }

    public void mark(int n2) {
    }

    public boolean markSupported() {
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
    }

    public long skip(long l2) {
        if (l2 <= 0L) {
            return 0L;
        }
        int n2 = (int)Math.min((long)l2, (long)this.available());
        this.bufOff = n2 + this.bufOff;
        return n2;
    }
}

