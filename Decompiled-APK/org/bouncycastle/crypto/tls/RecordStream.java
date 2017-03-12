package org.bouncycastle.crypto.tls;

import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;

class RecordStream {
    private static int DEFAULT_PLAINTEXT_LIMIT;
    private ByteArrayOutputStream buffer;
    private int ciphertextLimit;
    private int compressedLimit;
    private TlsProtocol handler;
    private TlsHandshakeHash handshakeHash;
    private InputStream input;
    private OutputStream output;
    private TlsCipher pendingCipher;
    private TlsCompression pendingCompression;
    private int plaintextLimit;
    private TlsCipher readCipher;
    private TlsCompression readCompression;
    private long readSeqNo;
    private ProtocolVersion readVersion;
    private boolean restrictReadVersion;
    private TlsCipher writeCipher;
    private TlsCompression writeCompression;
    private long writeSeqNo;
    private ProtocolVersion writeVersion;

    static {
        DEFAULT_PLAINTEXT_LIMIT = PKIFailureInfo.badPOP;
    }

    RecordStream(TlsProtocol tlsProtocol, InputStream inputStream, OutputStream outputStream) {
        this.pendingCompression = null;
        this.readCompression = null;
        this.writeCompression = null;
        this.pendingCipher = null;
        this.readCipher = null;
        this.writeCipher = null;
        this.readSeqNo = 0;
        this.writeSeqNo = 0;
        this.buffer = new ByteArrayOutputStream();
        this.handshakeHash = null;
        this.readVersion = null;
        this.writeVersion = null;
        this.restrictReadVersion = true;
        this.handler = tlsProtocol;
        this.input = inputStream;
        this.output = outputStream;
        this.readCompression = new TlsNullCompression();
        this.writeCompression = this.readCompression;
    }

    private static void checkLength(int i, int i2, short s) {
        if (i > i2) {
            throw new TlsFatalAlert(s);
        }
    }

    private static void checkType(short s, short s2) {
        switch (s) {
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
            case NamedCurve.secp224r1 /*21*/:
            case NamedCurve.secp256k1 /*22*/:
            case NamedCurve.secp256r1 /*23*/:
            case NamedCurve.secp384r1 /*24*/:
            default:
                throw new TlsFatalAlert(s2);
        }
    }

    private byte[] getBufferContents() {
        byte[] toByteArray = this.buffer.toByteArray();
        this.buffer.reset();
        return toByteArray;
    }

    byte[] decodeAndVerify(short s, InputStream inputStream, int i) {
        checkLength(i, this.ciphertextLimit, (short) 22);
        byte[] readFully = TlsUtils.readFully(i, inputStream);
        TlsCipher tlsCipher = this.readCipher;
        long j = this.readSeqNo;
        this.readSeqNo = 1 + j;
        byte[] decodeCiphertext = tlsCipher.decodeCiphertext(j, s, readFully, 0, readFully.length);
        checkLength(decodeCiphertext.length, this.compressedLimit, (short) 22);
        OutputStream decompress = this.readCompression.decompress(this.buffer);
        if (decompress != this.buffer) {
            decompress.write(decodeCiphertext, 0, decodeCiphertext.length);
            decompress.flush();
            decodeCiphertext = getBufferContents();
        }
        checkLength(decodeCiphertext.length, this.plaintextLimit, (short) 30);
        if (decodeCiphertext.length >= 1 || s == (short) 23) {
            return decodeCiphertext;
        }
        throw new TlsFatalAlert((short) 47);
    }

    void finaliseHandshake() {
        if (this.readCompression == this.pendingCompression && this.writeCompression == this.pendingCompression && this.readCipher == this.pendingCipher && this.writeCipher == this.pendingCipher) {
            this.pendingCompression = null;
            this.pendingCipher = null;
            return;
        }
        throw new TlsFatalAlert((short) 40);
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
        this.readCipher = new TlsNullCipher(tlsContext);
        this.writeCipher = this.readCipher;
        this.handshakeHash = new DeferredHash();
        this.handshakeHash.init(tlsContext);
        setPlaintextLimit(DEFAULT_PLAINTEXT_LIMIT);
    }

    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }

    TlsHandshakeHash prepareToFinish() {
        TlsHandshakeHash tlsHandshakeHash = this.handshakeHash;
        this.handshakeHash = this.handshakeHash.stopTracking();
        return tlsHandshakeHash;
    }

    boolean readRecord() {
        byte[] readAllOrNothing = TlsUtils.readAllOrNothing(5, this.input);
        if (readAllOrNothing == null) {
            return false;
        }
        short readUint8 = TlsUtils.readUint8(readAllOrNothing, 0);
        checkType(readUint8, (short) 10);
        if (this.restrictReadVersion) {
            ProtocolVersion readVersion = TlsUtils.readVersion(readAllOrNothing, 1);
            if (this.readVersion == null) {
                this.readVersion = readVersion;
            } else if (!readVersion.equals(this.readVersion)) {
                throw new TlsFatalAlert((short) 47);
            }
        } else if ((TlsUtils.readVersionRaw(readAllOrNothing, 1) & -256) != McTACommands.MOP_MC_TA_CMD_CASD_BASE) {
            throw new TlsFatalAlert((short) 47);
        }
        readAllOrNothing = decodeAndVerify(readUint8, this.input, TlsUtils.readUint16(readAllOrNothing, 3));
        this.handler.processRecord(readUint8, readAllOrNothing, 0, readAllOrNothing.length);
        return true;
    }

    void receivedReadCipherSpec() {
        if (this.pendingCompression == null || this.pendingCipher == null) {
            throw new TlsFatalAlert((short) 40);
        }
        this.readCompression = this.pendingCompression;
        this.readCipher = this.pendingCipher;
        this.readSeqNo = 0;
    }

    void safeClose() {
        try {
            this.input.close();
        } catch (IOException e) {
        }
        try {
            this.output.close();
        } catch (IOException e2) {
        }
    }

    void sentWriteCipherSpec() {
        if (this.pendingCompression == null || this.pendingCipher == null) {
            throw new TlsFatalAlert((short) 40);
        }
        this.writeCompression = this.pendingCompression;
        this.writeCipher = this.pendingCipher;
        this.writeSeqNo = 0;
    }

    void setPendingConnectionState(TlsCompression tlsCompression, TlsCipher tlsCipher) {
        this.pendingCompression = tlsCompression;
        this.pendingCipher = tlsCipher;
    }

    void setPlaintextLimit(int i) {
        this.plaintextLimit = i;
        this.compressedLimit = this.plaintextLimit + SkeinMac.SKEIN_1024;
        this.ciphertextLimit = this.compressedLimit + SkeinMac.SKEIN_1024;
    }

    void setReadVersion(ProtocolVersion protocolVersion) {
        this.readVersion = protocolVersion;
    }

    void setRestrictReadVersion(boolean z) {
        this.restrictReadVersion = z;
    }

    void setWriteVersion(ProtocolVersion protocolVersion) {
        this.writeVersion = protocolVersion;
    }

    void updateHandshakeData(byte[] bArr, int i, int i2) {
        this.handshakeHash.update(bArr, i, i2);
    }

    void writeRecord(short s, byte[] bArr, int i, int i2) {
        if (this.writeVersion != null) {
            checkType(s, (short) 80);
            checkLength(i2, this.plaintextLimit, (short) 80);
            if (i2 >= 1 || s == (short) 23) {
                Object encodePlaintext;
                if (s == (short) 22) {
                    updateHandshakeData(bArr, i, i2);
                }
                OutputStream compress = this.writeCompression.compress(this.buffer);
                TlsCipher tlsCipher;
                long j;
                if (compress == this.buffer) {
                    tlsCipher = this.writeCipher;
                    j = this.writeSeqNo;
                    this.writeSeqNo = j + 1;
                    encodePlaintext = tlsCipher.encodePlaintext(j, s, bArr, i, i2);
                } else {
                    compress.write(bArr, i, i2);
                    compress.flush();
                    byte[] bufferContents = getBufferContents();
                    checkLength(bufferContents.length, i2 + SkeinMac.SKEIN_1024, (short) 80);
                    tlsCipher = this.writeCipher;
                    j = this.writeSeqNo;
                    this.writeSeqNo = 1 + j;
                    encodePlaintext = tlsCipher.encodePlaintext(j, s, bufferContents, 0, bufferContents.length);
                }
                checkLength(encodePlaintext.length, this.ciphertextLimit, (short) 80);
                byte[] bArr2 = new byte[(encodePlaintext.length + 5)];
                TlsUtils.writeUint8(s, bArr2, 0);
                TlsUtils.writeVersion(this.writeVersion, bArr2, 1);
                TlsUtils.writeUint16(encodePlaintext.length, bArr2, 3);
                System.arraycopy(encodePlaintext, 0, bArr2, 5, encodePlaintext.length);
                this.output.write(bArr2);
                this.output.flush();
                return;
            }
            throw new TlsFatalAlert((short) 80);
        }
    }
}
