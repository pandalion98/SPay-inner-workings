/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.HeartbeatMode;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;

public class HeartbeatExtension {
    protected short mode;

    public HeartbeatExtension(short s2) {
        if (!HeartbeatMode.isValid(s2)) {
            throw new IllegalArgumentException("'mode' is not a valid HeartbeatMode value");
        }
        this.mode = s2;
    }

    public static HeartbeatExtension parse(InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        if (!HeartbeatMode.isValid(s2)) {
            throw new TlsFatalAlert(47);
        }
        return new HeartbeatExtension(s2);
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.mode, outputStream);
    }

    public short getMode() {
        return this.mode;
    }
}

