/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Strings;

public class URLAndHash {
    protected byte[] sha1Hash;
    protected String url;

    public URLAndHash(String string, byte[] arrby) {
        if (string == null || string.length() < 1 || string.length() >= 65536) {
            throw new IllegalArgumentException("'url' must have length from 1 to (2^16 - 1)");
        }
        if (arrby != null && arrby.length != 20) {
            throw new IllegalArgumentException("'sha1Hash' must have length == 20, if present");
        }
        this.url = string;
        this.sha1Hash = arrby;
    }

    public static URLAndHash parse(TlsContext tlsContext, InputStream inputStream) {
        byte[] arrby;
        byte[] arrby2 = TlsUtils.readOpaque16(inputStream);
        if (arrby2.length < 1) {
            throw new TlsFatalAlert(47);
        }
        String string = Strings.fromByteArray((byte[])arrby2);
        switch (TlsUtils.readUint8(inputStream)) {
            default: {
                throw new TlsFatalAlert(47);
            }
            case 0: {
                boolean bl = TlsUtils.isTLSv12(tlsContext);
                arrby = null;
                if (!bl) break;
                throw new TlsFatalAlert(47);
            }
            case 1: {
                arrby = TlsUtils.readFully(20, inputStream);
            }
        }
        return new URLAndHash(string, arrby);
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeOpaque16(Strings.toByteArray((String)this.url), outputStream);
        if (this.sha1Hash == null) {
            TlsUtils.writeUint8(0, outputStream);
            return;
        }
        TlsUtils.writeUint8(1, outputStream);
        outputStream.write(this.sha1Hash);
    }

    public byte[] getSHA1Hash() {
        return this.sha1Hash;
    }

    public String getURL() {
        return this.url;
    }
}

