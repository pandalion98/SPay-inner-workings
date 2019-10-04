/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.HeartbeatMessageType;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public class HeartbeatMessage {
    protected int paddingLength;
    protected byte[] payload;
    protected short type;

    public HeartbeatMessage(short s2, byte[] arrby, int n2) {
        if (!HeartbeatMessageType.isValid(s2)) {
            throw new IllegalArgumentException("'type' is not a valid HeartbeatMessageType value");
        }
        if (arrby == null || arrby.length >= 65536) {
            throw new IllegalArgumentException("'payload' must have length < 2^16");
        }
        if (n2 < 16) {
            throw new IllegalArgumentException("'paddingLength' must be at least 16");
        }
        this.type = s2;
        this.payload = arrby;
        this.paddingLength = n2;
    }

    public static HeartbeatMessage parse(InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        if (!HeartbeatMessageType.isValid(s2)) {
            throw new TlsFatalAlert(47);
        }
        int n2 = TlsUtils.readUint16(inputStream);
        PayloadBuffer payloadBuffer = new PayloadBuffer();
        Streams.pipeAll((InputStream)inputStream, (OutputStream)payloadBuffer);
        byte[] arrby = payloadBuffer.toTruncatedByteArray(n2);
        if (arrby == null) {
            return null;
        }
        return new HeartbeatMessage(s2, arrby, payloadBuffer.size() - arrby.length);
    }

    public void encode(TlsContext tlsContext, OutputStream outputStream) {
        TlsUtils.writeUint8(this.type, outputStream);
        TlsUtils.checkUint16(this.payload.length);
        TlsUtils.writeUint16(this.payload.length, outputStream);
        outputStream.write(this.payload);
        byte[] arrby = new byte[this.paddingLength];
        tlsContext.getNonceRandomGenerator().nextBytes(arrby);
        outputStream.write(arrby);
    }

    static class PayloadBuffer
    extends ByteArrayOutputStream {
        PayloadBuffer() {
        }

        byte[] toTruncatedByteArray(int n2) {
            int n3 = n2 + 16;
            if (this.count < n3) {
                return null;
            }
            return Arrays.copyOf((byte[])this.buf, (int)n2);
        }
    }

}

