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
 *  java.security.GeneralSecurityException
 *  javax.crypto.Cipher
 */
package org.bouncycastle.jcajce.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;

public class CipherOutputStream
extends FilterOutputStream {
    private final Cipher cipher;
    private final byte[] oneByte = new byte[1];

    public CipherOutputStream(OutputStream outputStream, Cipher cipher) {
        super(outputStream);
        this.cipher = cipher;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void close() {
        IOException iOException;
        block7 : {
            IOException iOException2;
            try {
                byte[] arrby = this.cipher.doFinal();
                iOException2 = null;
                if (arrby != null) {
                    this.out.write(arrby);
                }
            }
            catch (GeneralSecurityException generalSecurityException) {
                iOException2 = new InvalidCipherTextIOException("Error during cipher finalisation", generalSecurityException);
            }
            catch (Exception exception) {
                iOException2 = new IOException("Error closing stream: " + (Object)((Object)exception));
            }
            try {
                this.flush();
                this.out.close();
                iOException = iOException2;
            }
            catch (IOException iOException3) {
                if (iOException2 == null) break block7;
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

    public void write(int n2) {
        this.oneByte[0] = (byte)n2;
        this.write(this.oneByte, 0, 1);
    }

    public void write(byte[] arrby, int n2, int n3) {
        byte[] arrby2 = this.cipher.update(arrby, n2, n3);
        if (arrby2 != null) {
            this.out.write(arrby2);
        }
    }
}

