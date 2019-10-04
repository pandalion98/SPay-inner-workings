/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Integers
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.AbstractTlsContext;
import org.bouncycastle.crypto.tls.ByteQueue;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.RecordStream;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.crypto.tls.SupplementalDataEntry;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsInputStream;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsOutputStream;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsSessionImpl;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Integers;

public abstract class TlsProtocol {
    protected static final short CS_CERTIFICATE_REQUEST = 7;
    protected static final short CS_CERTIFICATE_STATUS = 5;
    protected static final short CS_CERTIFICATE_VERIFY = 12;
    protected static final short CS_CLIENT_CERTIFICATE = 10;
    protected static final short CS_CLIENT_FINISHED = 13;
    protected static final short CS_CLIENT_HELLO = 1;
    protected static final short CS_CLIENT_KEY_EXCHANGE = 11;
    protected static final short CS_CLIENT_SUPPLEMENTAL_DATA = 9;
    protected static final short CS_END = 16;
    protected static final short CS_SERVER_CERTIFICATE = 4;
    protected static final short CS_SERVER_FINISHED = 15;
    protected static final short CS_SERVER_HELLO = 2;
    protected static final short CS_SERVER_HELLO_DONE = 8;
    protected static final short CS_SERVER_KEY_EXCHANGE = 6;
    protected static final short CS_SERVER_SESSION_TICKET = 14;
    protected static final short CS_SERVER_SUPPLEMENTAL_DATA = 3;
    protected static final short CS_START = 0;
    protected static final Integer EXT_RenegotiationInfo = Integers.valueOf((int)65281);
    protected static final Integer EXT_SessionTicket = Integers.valueOf((int)35);
    private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
    private ByteQueue alertQueue = new ByteQueue(2);
    protected boolean allowCertificateStatus = false;
    private volatile boolean appDataReady = false;
    private ByteQueue applicationDataQueue = new ByteQueue();
    protected Hashtable clientExtensions = null;
    private volatile boolean closed = false;
    protected short connection_state = 0;
    protected boolean expectSessionTicket = false;
    private byte[] expected_verify_data = null;
    private volatile boolean failedWithError = false;
    private ByteQueue handshakeQueue = new ByteQueue();
    protected int[] offeredCipherSuites = null;
    protected short[] offeredCompressionMethods = null;
    protected Certificate peerCertificate = null;
    protected boolean receivedChangeCipherSpec = false;
    RecordStream recordStream;
    protected boolean resumedSession = false;
    protected SecureRandom secureRandom;
    protected boolean secure_renegotiation = false;
    protected SecurityParameters securityParameters = null;
    protected Hashtable serverExtensions = null;
    protected SessionParameters sessionParameters = null;
    private volatile boolean splitApplicationDataRecords = true;
    private TlsInputStream tlsInputStream = null;
    private TlsOutputStream tlsOutputStream = null;
    protected TlsSession tlsSession = null;

    public TlsProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        this.recordStream = new RecordStream(this, inputStream, outputStream);
        this.secureRandom = secureRandom;
    }

    protected static void assertEmpty(ByteArrayInputStream byteArrayInputStream) {
        if (byteArrayInputStream.available() > 0) {
            throw new TlsFatalAlert(50);
        }
    }

    protected static byte[] createRandomBlock(boolean bl, RandomGenerator randomGenerator) {
        byte[] arrby = new byte[32];
        randomGenerator.nextBytes(arrby);
        if (bl) {
            TlsUtils.writeGMTUnixTime(arrby, 0);
        }
        return arrby;
    }

    protected static byte[] createRenegotiationInfo(byte[] arrby) {
        return TlsUtils.encodeOpaque8(arrby);
    }

    protected static void establishMasterSecret(TlsContext tlsContext, TlsKeyExchange tlsKeyExchange) {
        byte[] arrby = tlsKeyExchange.generatePremasterSecret();
        try {
            tlsContext.getSecurityParameters().masterSecret = TlsUtils.calculateMasterSecret(tlsContext, arrby);
            return;
        }
        finally {
            if (arrby != null) {
                Arrays.fill((byte[])arrby, (byte)0);
            }
        }
    }

    protected static byte[] getCurrentPRFHash(TlsContext tlsContext, TlsHandshakeHash tlsHandshakeHash, byte[] arrby) {
        Digest digest = tlsHandshakeHash.forkPRFHash();
        if (arrby != null && TlsUtils.isSSL(tlsContext)) {
            digest.update(arrby, 0, arrby.length);
        }
        byte[] arrby2 = new byte[digest.getDigestSize()];
        digest.doFinal(arrby2, 0);
        return arrby2;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected static int getPRFAlgorithm(TlsContext tlsContext, int n2) {
        boolean bl = TlsUtils.isTLSv12(tlsContext);
        switch (n2) {
            default: {
                if (!bl) return 0;
                return 1;
            }
            case 59: 
            case 60: 
            case 61: 
            case 62: 
            case 63: 
            case 64: 
            case 103: 
            case 104: 
            case 105: 
            case 106: 
            case 107: 
            case 156: 
            case 158: 
            case 160: 
            case 162: 
            case 164: 
            case 168: 
            case 170: 
            case 172: 
            case 186: 
            case 187: 
            case 188: 
            case 189: 
            case 190: 
            case 191: 
            case 192: 
            case 193: 
            case 194: 
            case 195: 
            case 196: 
            case 197: 
            case 49187: 
            case 49189: 
            case 49191: 
            case 49193: 
            case 49195: 
            case 49197: 
            case 49199: 
            case 49201: 
            case 49266: 
            case 49268: 
            case 49270: 
            case 49272: 
            case 49274: 
            case 49276: 
            case 49278: 
            case 49280: 
            case 49282: 
            case 49284: 
            case 49286: 
            case 49288: 
            case 49290: 
            case 49292: 
            case 49294: 
            case 49296: 
            case 49298: 
            case 49308: 
            case 49309: 
            case 49310: 
            case 49311: 
            case 49312: 
            case 49313: 
            case 49314: 
            case 49315: 
            case 49316: 
            case 49317: 
            case 49318: 
            case 49319: 
            case 49320: 
            case 49321: 
            case 49322: 
            case 49323: 
            case 49324: 
            case 49325: 
            case 49326: 
            case 49327: 
            case 52243: 
            case 52244: 
            case 52245: {
                if (!bl) throw new TlsFatalAlert(47);
                return 1;
            }
            case 157: 
            case 159: 
            case 161: 
            case 163: 
            case 165: 
            case 169: 
            case 171: 
            case 173: 
            case 49188: 
            case 49190: 
            case 49192: 
            case 49194: 
            case 49196: 
            case 49198: 
            case 49200: 
            case 49202: 
            case 49267: 
            case 49269: 
            case 49271: 
            case 49273: 
            case 49275: 
            case 49277: 
            case 49279: 
            case 49281: 
            case 49283: 
            case 49285: 
            case 49287: 
            case 49289: 
            case 49291: 
            case 49293: 
            case 49295: 
            case 49297: 
            case 49299: {
                if (!bl) throw new TlsFatalAlert(47);
                return 2;
            }
            case 175: 
            case 177: 
            case 179: 
            case 181: 
            case 183: 
            case 185: 
            case 49208: 
            case 49211: 
            case 49301: 
            case 49303: 
            case 49305: 
            case 49307: 
        }
        if (!bl) return 0;
        return 2;
    }

    private void processAlert() {
        while (this.alertQueue.available() >= 2) {
            byte[] arrby = this.alertQueue.removeData(2, 0);
            short s2 = arrby[0];
            short s3 = arrby[1];
            this.getPeer().notifyAlertReceived(s2, s3);
            if (s2 == 2) {
                this.invalidateSession();
                this.failedWithError = true;
                this.closed = true;
                this.recordStream.safeClose();
                throw new IOException(TLS_ERROR_MESSAGE);
            }
            if (s3 == 0) {
                this.handleClose(false);
            }
            this.handleWarningMessage(s3);
        }
    }

    private void processApplicationData() {
    }

    private void processChangeCipherSpec(byte[] arrby, int n2, int n3) {
        for (int i2 = 0; i2 < n3; ++i2) {
            if (TlsUtils.readUint8(arrby, n2 + i2) != 1) {
                throw new TlsFatalAlert(50);
            }
            if (this.receivedChangeCipherSpec || this.alertQueue.available() > 0 || this.handshakeQueue.available() > 0) {
                throw new TlsFatalAlert(10);
            }
            this.recordStream.receivedReadCipherSpec();
            this.receivedChangeCipherSpec = true;
            this.handleChangeCipherSpecMessage();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void processHandshake() {
        do {
            if (this.handshakeQueue.available() < 4) return;
            var2_2 = new byte[4];
            this.handshakeQueue.read(var2_2, 0, 4, 0);
            var3_3 = TlsUtils.readUint8(var2_2, 0);
            var4_4 = TlsUtils.readUint24(var2_2, 1);
            if (this.handshakeQueue.available() < var4_4 + 4) return;
            var5_5 = this.handshakeQueue.removeData(var4_4, 4);
            switch (var3_3) {
                default: {
                    ** GOTO lbl16
                }
                case 20: {
                    if (this.expected_verify_data == null) {
                        var6_6 = this.getContext().isServer() == false;
                        this.expected_verify_data = this.createVerifyData(var6_6);
                    }
lbl16: // 4 sources:
                    this.recordStream.updateHandshakeData(var2_2, 0, 4);
                    this.recordStream.updateHandshakeData(var5_5, 0, var4_4);
                }
                case 0: 
            }
            this.handleHandshakeMessage(var3_3, var5_5);
            var1_1 = true;
        } while (var1_1);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected static Hashtable readExtensions(ByteArrayInputStream byteArrayInputStream) {
        if (byteArrayInputStream.available() < 1) {
            return null;
        }
        byte[] arrby = TlsUtils.readOpaque16((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(arrby);
        Hashtable hashtable = new Hashtable();
        do {
            if (byteArrayInputStream2.available() <= 0) return hashtable;
        } while (hashtable.put((Object)Integers.valueOf((int)TlsUtils.readUint16((InputStream)byteArrayInputStream2)), (Object)TlsUtils.readOpaque16((InputStream)byteArrayInputStream2)) == null);
        throw new TlsFatalAlert(47);
    }

    protected static Vector readSupplementalDataMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] arrby = TlsUtils.readOpaque24((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(arrby);
        Vector vector = new Vector();
        while (byteArrayInputStream2.available() > 0) {
            vector.addElement((Object)new SupplementalDataEntry(TlsUtils.readUint16((InputStream)byteArrayInputStream2), TlsUtils.readOpaque16((InputStream)byteArrayInputStream2)));
        }
        return vector;
    }

    protected static void writeExtensions(OutputStream outputStream, Hashtable hashtable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Enumeration enumeration = hashtable.keys();
        while (enumeration.hasMoreElements()) {
            Integer n2 = (Integer)enumeration.nextElement();
            int n3 = n2;
            byte[] arrby = (byte[])hashtable.get((Object)n2);
            TlsUtils.checkUint16(n3);
            TlsUtils.writeUint16(n3, (OutputStream)byteArrayOutputStream);
            TlsUtils.writeOpaque16(arrby, (OutputStream)byteArrayOutputStream);
        }
        TlsUtils.writeOpaque16(byteArrayOutputStream.toByteArray(), outputStream);
    }

    protected static void writeSupplementalData(OutputStream outputStream, Vector vector) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            SupplementalDataEntry supplementalDataEntry = (SupplementalDataEntry)vector.elementAt(i2);
            int n2 = supplementalDataEntry.getDataType();
            TlsUtils.checkUint16(n2);
            TlsUtils.writeUint16(n2, (OutputStream)byteArrayOutputStream);
            TlsUtils.writeOpaque16(supplementalDataEntry.getData(), (OutputStream)byteArrayOutputStream);
        }
        TlsUtils.writeOpaque24(byteArrayOutputStream.toByteArray(), outputStream);
    }

    protected int applicationDataAvailable() {
        return this.applicationDataQueue.available();
    }

    protected void cleanupHandshake() {
        if (this.expected_verify_data != null) {
            Arrays.fill((byte[])this.expected_verify_data, (byte)0);
            this.expected_verify_data = null;
        }
        this.securityParameters.clear();
        this.peerCertificate = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.clientExtensions = null;
        this.serverExtensions = null;
        this.resumedSession = false;
        this.receivedChangeCipherSpec = false;
        this.secure_renegotiation = false;
        this.allowCertificateStatus = false;
        this.expectSessionTicket = false;
    }

    public void close() {
        this.handleClose(true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void completeHandshake() {
        boolean bl;
        block9 : {
            block8 : {
                bl = true;
                try {
                    while (this.connection_state != 16) {
                        if (this.closed) {
                            // empty if block
                        }
                        this.safeReadRecord();
                    }
                    this.recordStream.finaliseHandshake();
                    if (TlsUtils.isTLSv11(this.getContext())) break block8;
                    break block9;
                }
                catch (Throwable throwable) {
                    this.cleanupHandshake();
                    throw throwable;
                }
            }
            bl = false;
        }
        this.splitApplicationDataRecords = bl;
        if (!this.appDataReady) {
            this.appDataReady = true;
            this.tlsInputStream = new TlsInputStream(this);
            this.tlsOutputStream = new TlsOutputStream(this);
        }
        if (this.tlsSession != null) {
            if (this.sessionParameters == null) {
                this.sessionParameters = new SessionParameters.Builder().setCipherSuite(this.securityParameters.cipherSuite).setCompressionAlgorithm(this.securityParameters.compressionAlgorithm).setMasterSecret(this.securityParameters.masterSecret).setPeerCertificate(this.peerCertificate).setPSKIdentity(this.securityParameters.pskIdentity).setSRPIdentity(this.securityParameters.srpIdentity).setServerExtensions(this.serverExtensions).build();
                this.tlsSession = new TlsSessionImpl(this.tlsSession.getSessionID(), this.sessionParameters);
            }
            this.getContextAdmin().setResumableSession(this.tlsSession);
        }
        this.getPeer().notifyHandshakeComplete();
        this.cleanupHandshake();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected byte[] createVerifyData(boolean bl) {
        byte[] arrby;
        TlsContext tlsContext = this.getContext();
        String string = bl ? "server finished" : "client finished";
        if (bl) {
            arrby = TlsUtils.SSL_SERVER;
            return TlsUtils.calculateVerifyData(tlsContext, string, TlsProtocol.getCurrentPRFHash(tlsContext, this.recordStream.getHandshakeHash(), arrby));
        }
        arrby = TlsUtils.SSL_CLIENT;
        return TlsUtils.calculateVerifyData(tlsContext, string, TlsProtocol.getCurrentPRFHash(tlsContext, this.recordStream.getHandshakeHash(), arrby));
    }

    protected void failWithError(short s2, short s3, String string, Throwable throwable) {
        if (!this.closed) {
            this.closed = true;
            if (s2 == 2) {
                this.invalidateSession();
                this.failedWithError = true;
            }
            this.raiseAlert(s2, s3, string, throwable);
            this.recordStream.safeClose();
            if (s2 != 2) {
                return;
            }
        }
        throw new IOException(TLS_ERROR_MESSAGE);
    }

    protected void flush() {
        this.recordStream.flush();
    }

    protected abstract TlsContext getContext();

    abstract AbstractTlsContext getContextAdmin();

    public InputStream getInputStream() {
        return this.tlsInputStream;
    }

    public OutputStream getOutputStream() {
        return this.tlsOutputStream;
    }

    protected abstract TlsPeer getPeer();

    protected void handleChangeCipherSpecMessage() {
    }

    protected void handleClose(boolean bl) {
        if (!this.closed) {
            if (bl && !this.appDataReady) {
                this.raiseWarning((short)90, "User canceled handshake");
            }
            this.failWithError((short)1, (short)0, "Connection closed", null);
        }
    }

    protected abstract void handleHandshakeMessage(short var1, byte[] var2);

    protected void handleWarningMessage(short s2) {
    }

    protected void invalidateSession() {
        if (this.sessionParameters != null) {
            this.sessionParameters.clear();
            this.sessionParameters = null;
        }
        if (this.tlsSession != null) {
            this.tlsSession.invalidate();
            this.tlsSession = null;
        }
    }

    protected boolean isClosed() {
        return this.closed;
    }

    protected void processFinishedMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] arrby = TlsUtils.readFully(this.expected_verify_data.length, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!Arrays.constantTimeAreEqual((byte[])this.expected_verify_data, (byte[])arrby)) {
            throw new TlsFatalAlert(51);
        }
    }

    protected short processMaxFragmentLengthExtension(Hashtable hashtable, Hashtable hashtable2, short s2) {
        short s3 = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable2);
        if (s3 >= 0 && !this.resumedSession && s3 != TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable)) {
            throw new TlsFatalAlert(s2);
        }
        return s3;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void processRecord(short s2, byte[] arrby, int n2, int n3) {
        switch (s2) {
            default: {
                return;
            }
            case 21: {
                this.alertQueue.addData(arrby, n2, n3);
                this.processAlert();
                return;
            }
            case 23: {
                if (!this.appDataReady) {
                    throw new TlsFatalAlert(10);
                }
                this.applicationDataQueue.addData(arrby, n2, n3);
                this.processApplicationData();
                return;
            }
            case 20: {
                this.processChangeCipherSpec(arrby, n2, n3);
                return;
            }
            case 22: {
                this.handshakeQueue.addData(arrby, n2, n3);
                this.processHandshake();
                return;
            }
            case 24: {
                if (this.appDataReady) return;
                throw new TlsFatalAlert(10);
            }
        }
    }

    protected void raiseAlert(short s2, short s3, String string, Throwable throwable) {
        this.getPeer().notifyAlertRaised(s2, s3, string, throwable);
        byte[] arrby = new byte[]{(byte)s2, (byte)s3};
        this.safeWriteRecord((short)21, arrby, 0, 2);
    }

    protected void raiseWarning(short s2, String string) {
        this.raiseAlert((short)1, s2, string, null);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    protected int readApplicationData(byte[] var1_1, int var2_2, int var3_3) {
        if (var3_3 >= 1) ** GOTO lbl4
        return 0;
lbl-1000: // 1 sources:
        {
            this.safeReadRecord();
lbl4: // 2 sources:
            if (this.applicationDataQueue.available() == 0) continue;
            var4_4 = Math.min((int)var3_3, (int)this.applicationDataQueue.available());
            this.applicationDataQueue.removeData(var1_1, var2_2, var4_4, 0);
            return var4_4;
            ** while (!this.closed)
        }
lbl9: // 1 sources:
        if (this.failedWithError == false) return -1;
        throw new IOException("Internal TLS error, this could be an attack");
    }

    protected void safeReadRecord() {
        try {
            if (!this.recordStream.readRecord()) {
                throw new EOFException();
            }
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            if (!this.closed) {
                this.failWithError((short)2, tlsFatalAlert.getAlertDescription(), "Failed to read record", (Throwable)((Object)tlsFatalAlert));
            }
            throw tlsFatalAlert;
        }
        catch (IOException iOException) {
            if (!this.closed) {
                this.failWithError((short)2, (short)80, "Failed to read record", iOException);
            }
            throw iOException;
        }
        catch (RuntimeException runtimeException) {
            if (!this.closed) {
                this.failWithError((short)2, (short)80, "Failed to read record", runtimeException);
            }
            throw runtimeException;
        }
    }

    protected void safeWriteRecord(short s2, byte[] arrby, int n2, int n3) {
        try {
            this.recordStream.writeRecord(s2, arrby, n2, n3);
            return;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            if (!this.closed) {
                this.failWithError((short)2, tlsFatalAlert.getAlertDescription(), "Failed to write record", (Throwable)((Object)tlsFatalAlert));
            }
            throw tlsFatalAlert;
        }
        catch (IOException iOException) {
            if (!this.closed) {
                this.failWithError((short)2, (short)80, "Failed to write record", iOException);
            }
            throw iOException;
        }
        catch (RuntimeException runtimeException) {
            if (!this.closed) {
                this.failWithError((short)2, (short)80, "Failed to write record", runtimeException);
            }
            throw runtimeException;
        }
    }

    protected void sendCertificateMessage(Certificate certificate) {
        ProtocolVersion protocolVersion;
        if (certificate == null) {
            certificate = Certificate.EMPTY_CHAIN;
        }
        if (certificate.isEmpty() && !this.getContext().isServer() && (protocolVersion = this.getContext().getServerVersion()).isSSL()) {
            this.raiseWarning((short)41, protocolVersion.toString() + " client didn't provide credentials");
            return;
        }
        HandshakeMessage handshakeMessage = new HandshakeMessage(11);
        certificate.encode((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendChangeCipherSpecMessage() {
        byte[] arrby = new byte[]{1};
        this.safeWriteRecord((short)20, arrby, 0, arrby.length);
        this.recordStream.sentWriteCipherSpec();
    }

    protected void sendFinishedMessage() {
        byte[] arrby = this.createVerifyData(this.getContext().isServer());
        HandshakeMessage handshakeMessage = new HandshakeMessage(20, arrby.length);
        handshakeMessage.write(arrby);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendSupplementalDataMessage(Vector vector) {
        HandshakeMessage handshakeMessage = new HandshakeMessage(23);
        TlsProtocol.writeSupplementalData((OutputStream)handshakeMessage, vector);
        handshakeMessage.writeToRecordStream();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void writeData(byte[] arrby, int n2, int n3) {
        if (this.closed) {
            if (this.failedWithError) {
                throw new IOException("Internal TLS error, this could be an attack");
            }
            throw new IOException("Sorry, connection has been closed, you cannot write more data");
        }
        int n4 = n3;
        int n5 = n2;
        while (n4 > 0) {
            if (this.splitApplicationDataRecords) {
                this.safeWriteRecord((short)23, arrby, n5, 1);
                ++n5;
                --n4;
            }
            if (n4 <= 0) continue;
            int n6 = Math.min((int)n4, (int)this.recordStream.getPlaintextLimit());
            this.safeWriteRecord((short)23, arrby, n5, n6);
            n5 += n6;
            n4 -= n6;
        }
        return;
    }

    protected void writeHandshakeMessage(byte[] arrby, int n2, int n3) {
        while (n3 > 0) {
            int n4 = Math.min((int)n3, (int)this.recordStream.getPlaintextLimit());
            this.safeWriteRecord((short)22, arrby, n2, n4);
            n2 += n4;
            n3 -= n4;
        }
    }

    class HandshakeMessage
    extends ByteArrayOutputStream {
        HandshakeMessage(short s2) {
            this(s2, 60);
        }

        HandshakeMessage(short s2, int n2) {
            super(n2 + 4);
            TlsUtils.writeUint8(s2, (OutputStream)this);
            this.count = 3 + this.count;
        }

        void writeToRecordStream() {
            int n2 = -4 + this.count;
            TlsUtils.checkUint24(n2);
            TlsUtils.writeUint24(n2, this.buf, 1);
            TlsProtocol.this.writeHandshakeMessage(this.buf, 0, this.count);
            this.buf = null;
        }
    }

}

