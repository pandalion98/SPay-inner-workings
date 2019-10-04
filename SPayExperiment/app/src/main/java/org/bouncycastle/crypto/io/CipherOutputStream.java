/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.FilterOutputStream
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.io.CipherIOException;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;

public class CipherOutputStream
extends FilterOutputStream {
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private BufferedBlockCipher bufferedBlockCipher;
    private final byte[] oneByte = new byte[1];
    private StreamCipher streamCipher;

    public CipherOutputStream(OutputStream outputStream, BufferedBlockCipher bufferedBlockCipher) {
        super(outputStream);
        this.bufferedBlockCipher = bufferedBlockCipher;
    }

    public CipherOutputStream(OutputStream outputStream, StreamCipher streamCipher) {
        super(outputStream);
        this.streamCipher = streamCipher;
    }

    public CipherOutputStream(OutputStream outputStream, AEADBlockCipher aEADBlockCipher) {
        super(outputStream);
        this.aeadBlockCipher = aEADBlockCipher;
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
    public void close() {
        void var3_10;
        block12 : {
            void var2_3;
            this.ensureCapacity(0, true);
            try {
                if (this.bufferedBlockCipher != null) {
                    int n2 = this.bufferedBlockCipher.doFinal(this.buf, 0);
                    Object var2_2 = null;
                    if (n2 != 0) {
                        this.out.write(this.buf, 0, n2);
                    }
                } else {
                    AEADBlockCipher aEADBlockCipher = this.aeadBlockCipher;
                    Object var2_4 = null;
                    if (aEADBlockCipher != null) {
                        int n3 = this.aeadBlockCipher.doFinal(this.buf, 0);
                        Object var2_5 = null;
                        if (n3 != 0) {
                            this.out.write(this.buf, 0, n3);
                            Object var2_6 = null;
                        }
                    }
                }
            }
            catch (InvalidCipherTextException invalidCipherTextException) {
                InvalidCipherTextIOException invalidCipherTextIOException = new InvalidCipherTextIOException("Error finalising cipher data", (Throwable)((Object)invalidCipherTextException));
            }
            catch (Exception exception) {
                CipherIOException cipherIOException = new CipherIOException("Error closing stream: ", exception);
            }
            try {
                this.flush();
                this.out.close();
                void var3_9 = var2_3;
            }
            catch (IOException iOException) {
                if (var2_3 == null) break block12;
                void var3_12 = var2_3;
            }
        }
        if (var3_10 != null) {
            throw var3_10;
        }
    }

    public void flush() {
        this.out.flush();
    }

    public void write(int n2) {
        this.oneByte[0] = (byte)n2;
        if (this.streamCipher != null) {
            this.out.write((int)this.streamCipher.returnByte((byte)n2));
            return;
        }
        this.write(this.oneByte, 0, 1);
    }

    public void write(byte[] arrby) {
        this.write(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(byte[] arrby, int n2, int n3) {
        this.ensureCapacity(n3, false);
        if (this.bufferedBlockCipher != null) {
            int n4 = this.bufferedBlockCipher.processBytes(arrby, n2, n3, this.buf, 0);
            if (n4 == 0) return;
            {
                this.out.write(this.buf, 0, n4);
                return;
            }
        } else {
            if (this.aeadBlockCipher == null) {
                this.streamCipher.processBytes(arrby, n2, n3, this.buf, 0);
                this.out.write(this.buf, 0, n3);
                return;
            }
            int n5 = this.aeadBlockCipher.processBytes(arrby, n2, n3, this.buf, 0);
            if (n5 == 0) return;
            {
                this.out.write(this.buf, 0, n5);
                return;
            }
        }
    }
}

