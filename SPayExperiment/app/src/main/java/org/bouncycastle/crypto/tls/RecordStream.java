/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.DeferredHash;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCompression;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsNullCipher;
import org.bouncycastle.crypto.tls.TlsNullCompression;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsUtils;

class RecordStream {
    private static int DEFAULT_PLAINTEXT_LIMIT = 16384;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private int ciphertextLimit;
    private int compressedLimit;
    private TlsProtocol handler;
    private TlsHandshakeHash handshakeHash = null;
    private InputStream input;
    private OutputStream output;
    private TlsCipher pendingCipher = null;
    private TlsCompression pendingCompression = null;
    private int plaintextLimit;
    private TlsCipher readCipher = null;
    private TlsCompression readCompression = null;
    private long readSeqNo = 0L;
    private ProtocolVersion readVersion = null;
    private boolean restrictReadVersion = true;
    private TlsCipher writeCipher = null;
    private TlsCompression writeCompression = null;
    private long writeSeqNo = 0L;
    private ProtocolVersion writeVersion = null;

    RecordStream(TlsProtocol tlsProtocol, InputStream inputStream, OutputStream outputStream) {
        this.handler = tlsProtocol;
        this.input = inputStream;
        this.output = outputStream;
        this.writeCompression = this.readCompression = new TlsNullCompression();
    }

    private static void checkLength(int n2, int n3, short s2) {
        if (n2 > n3) {
            throw new TlsFatalAlert(s2);
        }
    }

    private static void checkType(short s2, short s3) {
        switch (s2) {
            default: {
                throw new TlsFatalAlert(s3);
            }
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
        }
    }

    private byte[] getBufferContents() {
        byte[] arrby = this.buffer.toByteArray();
        this.buffer.reset();
        return arrby;
    }

    byte[] decodeAndVerify(short s2, InputStream inputStream, int n2) {
        RecordStream.checkLength(n2, this.ciphertextLimit, (short)22);
        byte[] arrby = TlsUtils.readFully(n2, inputStream);
        TlsCipher tlsCipher = this.readCipher;
        long l2 = this.readSeqNo;
        this.readSeqNo = 1L + l2;
        byte[] arrby2 = tlsCipher.decodeCiphertext(l2, s2, arrby, 0, arrby.length);
        RecordStream.checkLength(arrby2.length, this.compressedLimit, (short)22);
        OutputStream outputStream = this.readCompression.decompress((OutputStream)this.buffer);
        if (outputStream != this.buffer) {
            outputStream.write(arrby2, 0, arrby2.length);
            outputStream.flush();
            arrby2 = this.getBufferContents();
        }
        RecordStream.checkLength(arrby2.length, this.plaintextLimit, (short)30);
        if (arrby2.length < 1 && s2 != 23) {
            throw new TlsFatalAlert(47);
        }
        return arrby2;
    }

    void finaliseHandshake() {
        if (this.readCompression != this.pendingCompression || this.writeCompression != this.pendingCompression || this.readCipher != this.pendingCipher || this.writeCipher != this.pendingCipher) {
            throw new TlsFatalAlert(40);
        }
        this.pendingCompression = null;
        this.pendingCipher = null;
    }

    void flush() {
        this.output.flush();
    }

    TlsHandshakeHash getHandshakeHash() {
        return this.handshakeHash;
    }

    int getPlaintextLimit() {
        return this.plaintextLimit;
    }

    ProtocolVersion getReadVersion() {
        return this.readVersion;
    }

    void init(TlsContext tlsContext) {
        this.writeCipher = this.readCipher = new TlsNullCipher(tlsContext);
        this.handshakeHash = new DeferredHash();
        this.handshakeHash.init(tlsContext);
        this.setPlaintextLimit(DEFAULT_PLAINTEXT_LIMIT);
    }

    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }

    TlsHandshakeHash prepareToFinish() {
        TlsHandshakeHash tlsHandshakeHash = this.handshakeHash;
        this.handshakeHash = this.handshakeHash.stopTracking();
        return tlsHandshakeHash;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean readRecord() {
        byte[] arrby = TlsUtils.readAllOrNothing(5, this.input);
        if (arrby == null) {
            return false;
        }
        short s2 = TlsUtils.readUint8(arrby, 0);
        RecordStream.checkType(s2, (short)10);
        if (!this.restrictReadVersion) {
            if ((-256 & TlsUtils.readVersionRaw(arrby, 1)) != 768) {
                throw new TlsFatalAlert(47);
            }
        } else {
            ProtocolVersion protocolVersion = TlsUtils.readVersion(arrby, 1);
            if (this.readVersion == null) {
                this.readVersion = protocolVersion;
            } else if (!protocolVersion.equals(this.readVersion)) {
                throw new TlsFatalAlert(47);
            }
        }
        int n2 = TlsUtils.readUint16(arrby, 3);
        byte[] arrby2 = this.decodeAndVerify(s2, this.input, n2);
        this.handler.processRecord(s2, arrby2, 0, arrby2.length);
        return true;
    }

    void receivedReadCipherSpec() {
        if (this.pendingCompression == null || this.pendingCipher == null) {
            throw new TlsFatalAlert(40);
        }
        this.readCompression = this.pendingCompression;
        this.readCipher = this.pendingCipher;
        this.readSeqNo = 0L;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void safeClose() {
        try {
            this.input.close();
        }
        catch (IOException iOException) {}
        try {
            this.output.close();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    void sentWriteCipherSpec() {
        if (this.pendingCompression == null || this.pendingCipher == null) {
            throw new TlsFatalAlert(40);
        }
        this.writeCompression = this.pendingCompression;
        this.writeCipher = this.pendingCipher;
        this.writeSeqNo = 0L;
    }

    void setPendingConnectionState(TlsCompression tlsCompression, TlsCipher tlsCipher) {
        this.pendingCompression = tlsCompression;
        this.pendingCipher = tlsCipher;
    }

    void setPlaintextLimit(int n2) {
        this.plaintextLimit = n2;
        this.compressedLimit = 1024 + this.plaintextLimit;
        this.ciphertextLimit = 1024 + this.compressedLimit;
    }

    void setReadVersion(ProtocolVersion protocolVersion) {
        this.readVersion = protocolVersion;
    }

    void setRestrictReadVersion(boolean bl) {
        this.restrictReadVersion = bl;
    }

    void setWriteVersion(ProtocolVersion protocolVersion) {
        this.writeVersion = protocolVersion;
    }

    void updateHandshakeData(byte[] arrby, int n2, int n3) {
        this.handshakeHash.update(arrby, n2, n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    void writeRecord(short s2, byte[] arrby, int n2, int n3) {
        OutputStream outputStream;
        byte[] arrby2;
        if (this.writeVersion == null) {
            return;
        }
        RecordStream.checkType(s2, (short)80);
        RecordStream.checkLength(n3, this.plaintextLimit, (short)80);
        if (n3 < 1 && s2 != 23) {
            throw new TlsFatalAlert(80);
        }
        if (s2 == 22) {
            this.updateHandshakeData(arrby, n2, n3);
        }
        if ((outputStream = this.writeCompression.compress((OutputStream)this.buffer)) == this.buffer) {
            TlsCipher tlsCipher = this.writeCipher;
            long l2 = this.writeSeqNo;
            this.writeSeqNo = l2 + 1L;
            arrby2 = tlsCipher.encodePlaintext(l2, s2, arrby, n2, n3);
        } else {
            outputStream.write(arrby, n2, n3);
            outputStream.flush();
            byte[] arrby3 = this.getBufferContents();
            RecordStream.checkLength(arrby3.length, n3 + 1024, (short)80);
            TlsCipher tlsCipher = this.writeCipher;
            long l3 = this.writeSeqNo;
            this.writeSeqNo = 1L + l3;
            arrby2 = tlsCipher.encodePlaintext(l3, s2, arrby3, 0, arrby3.length);
        }
        RecordStream.checkLength(arrby2.length, this.ciphertextLimit, (short)80);
        byte[] arrby4 = new byte[5 + arrby2.length];
        TlsUtils.writeUint8(s2, arrby4, 0);
        TlsUtils.writeVersion(this.writeVersion, arrby4, 1);
        TlsUtils.writeUint16(arrby2.length, arrby4, 3);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby4, (int)5, (int)arrby2.length);
        this.output.write(arrby4);
        this.output.flush();
    }
}

