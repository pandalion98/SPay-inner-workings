package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class TlsClientProtocol extends TlsProtocol {
    protected TlsAuthentication authentication;
    protected CertificateRequest certificateRequest;
    protected CertificateStatus certificateStatus;
    protected TlsKeyExchange keyExchange;
    protected byte[] selectedSessionID;
    protected TlsClient tlsClient;
    TlsClientContextImpl tlsClientContext;

    public TlsClientProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsClient = null;
        this.tlsClientContext = null;
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    public void connect(TlsClient tlsClient) {
        if (tlsClient == null) {
            throw new IllegalArgumentException("'tlsClient' cannot be null");
        } else if (this.tlsClient != null) {
            throw new IllegalStateException("'connect' can only be called once");
        } else {
            this.tlsClient = tlsClient;
            this.securityParameters = new SecurityParameters();
            this.securityParameters.entity = 1;
            this.tlsClientContext = new TlsClientContextImpl(this.secureRandom, this.securityParameters);
            this.securityParameters.clientRandom = TlsProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), this.tlsClientContext.getNonceRandomGenerator());
            this.tlsClient.init(this.tlsClientContext);
            this.recordStream.init(this.tlsClientContext);
            TlsSession sessionToResume = tlsClient.getSessionToResume();
            if (sessionToResume != null) {
                SessionParameters exportSessionParameters = sessionToResume.exportSessionParameters();
                if (exportSessionParameters != null) {
                    this.tlsSession = sessionToResume;
                    this.sessionParameters = exportSessionParameters;
                }
            }
            sendClientHelloMessage();
            this.connection_state = (short) 1;
            completeHandshake();
        }
    }

    protected TlsContext getContext() {
        return this.tlsClientContext;
    }

    AbstractTlsContext getContextAdmin() {
        return this.tlsClientContext;
    }

    protected TlsPeer getPeer() {
        return this.tlsClient;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void handleHandshakeMessage(short r8, byte[] r9) {
        /*
        r7 = this;
        r6 = 2;
        r5 = 40;
        r4 = 16;
        r1 = 0;
        r3 = 10;
        r0 = new java.io.ByteArrayInputStream;
        r0.<init>(r9);
        r2 = r7.resumedSession;
        if (r2 == 0) goto L_0x0030;
    L_0x0011:
        r1 = 20;
        if (r8 != r1) goto L_0x0019;
    L_0x0015:
        r1 = r7.connection_state;
        if (r1 == r6) goto L_0x001f;
    L_0x0019:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x001f:
        r7.processFinishedMessage(r0);
        r0 = 15;
        r7.connection_state = r0;
        r7.sendFinishedMessage();
        r0 = 13;
        r7.connection_state = r0;
        r7.connection_state = r4;
    L_0x002f:
        return;
    L_0x0030:
        switch(r8) {
            case 0: goto L_0x02b0;
            case 1: goto L_0x0033;
            case 2: goto L_0x00bb;
            case 3: goto L_0x0033;
            case 4: goto L_0x028f;
            case 5: goto L_0x0033;
            case 6: goto L_0x0033;
            case 7: goto L_0x0033;
            case 8: goto L_0x0033;
            case 9: goto L_0x0033;
            case 10: goto L_0x0033;
            case 11: goto L_0x0039;
            case 12: goto L_0x022b;
            case 13: goto L_0x024d;
            case 14: goto L_0x014e;
            case 15: goto L_0x0033;
            case 16: goto L_0x0033;
            case 17: goto L_0x0033;
            case 18: goto L_0x0033;
            case 19: goto L_0x0033;
            case 20: goto L_0x009b;
            case 21: goto L_0x0033;
            case 22: goto L_0x0079;
            case 23: goto L_0x013a;
            default: goto L_0x0033;
        };
    L_0x0033:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0039:
        r2 = r7.connection_state;
        switch(r2) {
            case 2: goto L_0x0044;
            case 3: goto L_0x0047;
            default: goto L_0x003e;
        };
    L_0x003e:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0044:
        r7.handleSupplementalData(r1);
    L_0x0047:
        r1 = org.bouncycastle.crypto.tls.Certificate.parse(r0);
        r7.peerCertificate = r1;
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = r7.peerCertificate;
        if (r0 == 0) goto L_0x005c;
    L_0x0054:
        r0 = r7.peerCertificate;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x005f;
    L_0x005c:
        r0 = 0;
        r7.allowCertificateStatus = r0;
    L_0x005f:
        r0 = r7.keyExchange;
        r1 = r7.peerCertificate;
        r0.processServerCertificate(r1);
        r0 = r7.tlsClient;
        r0 = r0.getAuthentication();
        r7.authentication = r0;
        r0 = r7.authentication;
        r1 = r7.peerCertificate;
        r0.notifyServerCertificate(r1);
        r0 = 4;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x0079:
        r1 = r7.connection_state;
        switch(r1) {
            case 4: goto L_0x0084;
            default: goto L_0x007e;
        };
    L_0x007e:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0084:
        r1 = r7.allowCertificateStatus;
        if (r1 != 0) goto L_0x008e;
    L_0x0088:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x008e:
        r1 = org.bouncycastle.crypto.tls.CertificateStatus.parse(r0);
        r7.certificateStatus = r1;
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = 5;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x009b:
        r1 = r7.connection_state;
        switch(r1) {
            case 13: goto L_0x00a6;
            case 14: goto L_0x00b0;
            default: goto L_0x00a0;
        };
    L_0x00a0:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x00a6:
        r1 = r7.expectSessionTicket;
        if (r1 == 0) goto L_0x00b0;
    L_0x00aa:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x00b0:
        r7.processFinishedMessage(r0);
        r0 = 15;
        r7.connection_state = r0;
        r7.connection_state = r4;
        goto L_0x002f;
    L_0x00bb:
        r2 = r7.connection_state;
        switch(r2) {
            case 1: goto L_0x00c6;
            default: goto L_0x00c0;
        };
    L_0x00c0:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x00c6:
        r7.receiveServerHelloMessage(r0);
        r7.connection_state = r6;
        r0 = r7.securityParameters;
        r0 = r0.maxFragmentLength;
        if (r0 < 0) goto L_0x00de;
    L_0x00d1:
        r0 = 1;
        r2 = r7.securityParameters;
        r2 = r2.maxFragmentLength;
        r2 = r2 + 8;
        r0 = r0 << r2;
        r2 = r7.recordStream;
        r2.setPlaintextLimit(r0);
    L_0x00de:
        r0 = r7.securityParameters;
        r2 = r7.getContext();
        r3 = r7.securityParameters;
        r3 = r3.getCipherSuite();
        r2 = org.bouncycastle.crypto.tls.TlsProtocol.getPRFAlgorithm(r2, r3);
        r0.prfAlgorithm = r2;
        r0 = r7.securityParameters;
        r2 = 12;
        r0.verifyDataLength = r2;
        r0 = r7.recordStream;
        r0.notifyHelloComplete();
        r0 = r7.resumedSession;
        if (r0 == 0) goto L_0x0127;
    L_0x00ff:
        r0 = r7.securityParameters;
        r1 = r7.sessionParameters;
        r1 = r1.getMasterSecret();
        r1 = org.bouncycastle.util.Arrays.clone(r1);
        r0.masterSecret = r1;
        r0 = r7.recordStream;
        r1 = r7.getPeer();
        r1 = r1.getCompression();
        r2 = r7.getPeer();
        r2 = r2.getCipher();
        r0.setPendingConnectionState(r1, r2);
        r7.sendChangeCipherSpecMessage();
        goto L_0x002f;
    L_0x0127:
        r7.invalidateSession();
        r0 = r7.selectedSessionID;
        r0 = r0.length;
        if (r0 <= 0) goto L_0x002f;
    L_0x012f:
        r0 = new org.bouncycastle.crypto.tls.TlsSessionImpl;
        r2 = r7.selectedSessionID;
        r0.<init>(r2, r1);
        r7.tlsSession = r0;
        goto L_0x002f;
    L_0x013a:
        r1 = r7.connection_state;
        switch(r1) {
            case 2: goto L_0x0145;
            default: goto L_0x013f;
        };
    L_0x013f:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0145:
        r0 = org.bouncycastle.crypto.tls.TlsProtocol.readSupplementalDataMessage(r0);
        r7.handleSupplementalData(r0);
        goto L_0x002f;
    L_0x014e:
        r2 = r7.connection_state;
        switch(r2) {
            case 2: goto L_0x0159;
            case 3: goto L_0x015c;
            case 4: goto L_0x0163;
            case 5: goto L_0x0163;
            case 6: goto L_0x0168;
            case 7: goto L_0x0168;
            default: goto L_0x0153;
        };
    L_0x0153:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r5);
        throw r0;
    L_0x0159:
        r7.handleSupplementalData(r1);
    L_0x015c:
        r2 = r7.keyExchange;
        r2.skipServerCredentials();
        r7.authentication = r1;
    L_0x0163:
        r2 = r7.keyExchange;
        r2.skipServerKeyExchange();
    L_0x0168:
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = 8;
        r7.connection_state = r0;
        r0 = r7.recordStream;
        r0 = r0.getHandshakeHash();
        r0.sealHashAlgorithms();
        r0 = r7.tlsClient;
        r0 = r0.getClientSupplementalData();
        if (r0 == 0) goto L_0x0183;
    L_0x0180:
        r7.sendSupplementalDataMessage(r0);
    L_0x0183:
        r0 = 9;
        r7.connection_state = r0;
        r0 = r7.certificateRequest;
        if (r0 != 0) goto L_0x01fe;
    L_0x018b:
        r0 = r7.keyExchange;
        r0.skipClientCredentials();
        r0 = r1;
    L_0x0191:
        r7.connection_state = r3;
        r7.sendClientKeyExchangeMessage();
        r2 = 11;
        r7.connection_state = r2;
        r2 = r7.recordStream;
        r2 = r2.prepareToFinish();
        r3 = r7.securityParameters;
        r4 = r7.getContext();
        r1 = org.bouncycastle.crypto.tls.TlsProtocol.getCurrentPRFHash(r4, r2, r1);
        r3.sessionHash = r1;
        r1 = r7.getContext();
        r3 = r7.keyExchange;
        org.bouncycastle.crypto.tls.TlsProtocol.establishMasterSecret(r1, r3);
        r1 = r7.recordStream;
        r3 = r7.getPeer();
        r3 = r3.getCompression();
        r4 = r7.getPeer();
        r4 = r4.getCipher();
        r1.setPendingConnectionState(r3, r4);
        if (r0 == 0) goto L_0x01f2;
    L_0x01cc:
        r1 = r0 instanceof org.bouncycastle.crypto.tls.TlsSignerCredentials;
        if (r1 == 0) goto L_0x01f2;
    L_0x01d0:
        r0 = (org.bouncycastle.crypto.tls.TlsSignerCredentials) r0;
        r1 = r7.getContext();
        r3 = org.bouncycastle.crypto.tls.TlsUtils.getSignatureAndHashAlgorithm(r1, r0);
        if (r3 != 0) goto L_0x0222;
    L_0x01dc:
        r1 = r7.securityParameters;
        r1 = r1.getSessionHash();
    L_0x01e2:
        r0 = r0.generateCertificateSignature(r1);
        r1 = new org.bouncycastle.crypto.tls.DigitallySigned;
        r1.<init>(r3, r0);
        r7.sendCertificateVerifyMessage(r1);
        r0 = 12;
        r7.connection_state = r0;
    L_0x01f2:
        r7.sendChangeCipherSpecMessage();
        r7.sendFinishedMessage();
        r0 = 13;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x01fe:
        r0 = r7.authentication;
        r2 = r7.certificateRequest;
        r0 = r0.getClientCredentials(r2);
        if (r0 != 0) goto L_0x0214;
    L_0x0208:
        r2 = r7.keyExchange;
        r2.skipClientCredentials();
        r2 = org.bouncycastle.crypto.tls.Certificate.EMPTY_CHAIN;
        r7.sendCertificateMessage(r2);
        goto L_0x0191;
    L_0x0214:
        r2 = r7.keyExchange;
        r2.processClientCredentials(r0);
        r2 = r0.getCertificate();
        r7.sendCertificateMessage(r2);
        goto L_0x0191;
    L_0x0222:
        r1 = r3.getHash();
        r1 = r2.getFinalHash(r1);
        goto L_0x01e2;
    L_0x022b:
        r2 = r7.connection_state;
        switch(r2) {
            case 2: goto L_0x0236;
            case 3: goto L_0x0239;
            case 4: goto L_0x0240;
            case 5: goto L_0x0240;
            default: goto L_0x0230;
        };
    L_0x0230:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0236:
        r7.handleSupplementalData(r1);
    L_0x0239:
        r2 = r7.keyExchange;
        r2.skipServerCredentials();
        r7.authentication = r1;
    L_0x0240:
        r1 = r7.keyExchange;
        r1.processServerKeyExchange(r0);
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = 6;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x024d:
        r1 = r7.connection_state;
        switch(r1) {
            case 4: goto L_0x0258;
            case 5: goto L_0x0258;
            case 6: goto L_0x025d;
            default: goto L_0x0252;
        };
    L_0x0252:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x0258:
        r1 = r7.keyExchange;
        r1.skipServerKeyExchange();
    L_0x025d:
        r1 = r7.authentication;
        if (r1 != 0) goto L_0x0267;
    L_0x0261:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r5);
        throw r0;
    L_0x0267:
        r1 = r7.getContext();
        r1 = org.bouncycastle.crypto.tls.CertificateRequest.parse(r1, r0);
        r7.certificateRequest = r1;
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = r7.keyExchange;
        r1 = r7.certificateRequest;
        r0.validateCertificateRequest(r1);
        r0 = r7.recordStream;
        r0 = r0.getHandshakeHash();
        r1 = r7.certificateRequest;
        r1 = r1.getSupportedSignatureAlgorithms();
        org.bouncycastle.crypto.tls.TlsUtils.trackHashAlgorithms(r0, r1);
        r0 = 7;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x028f:
        r1 = r7.connection_state;
        switch(r1) {
            case 13: goto L_0x029a;
            default: goto L_0x0294;
        };
    L_0x0294:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x029a:
        r1 = r7.expectSessionTicket;
        if (r1 != 0) goto L_0x02a4;
    L_0x029e:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r3);
        throw r0;
    L_0x02a4:
        r7.invalidateSession();
        r7.receiveNewSessionTicketMessage(r0);
        r0 = 14;
        r7.connection_state = r0;
        goto L_0x002f;
    L_0x02b0:
        org.bouncycastle.crypto.tls.TlsProtocol.assertEmpty(r0);
        r0 = r7.connection_state;
        if (r0 != r4) goto L_0x002f;
    L_0x02b7:
        r0 = r7.getContext();
        r0 = org.bouncycastle.crypto.tls.TlsUtils.isSSL(r0);
        if (r0 == 0) goto L_0x02c7;
    L_0x02c1:
        r0 = new org.bouncycastle.crypto.tls.TlsFatalAlert;
        r0.<init>(r5);
        throw r0;
    L_0x02c7:
        r0 = "Renegotiation not supported";
        r1 = 100;
        r7.raiseWarning(r1, r0);
        goto L_0x002f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.tls.TlsClientProtocol.handleHandshakeMessage(short, byte[]):void");
    }

    protected void handleSupplementalData(Vector vector) {
        this.tlsClient.processServerSupplementalData(vector);
        this.connection_state = (short) 3;
        this.keyExchange = this.tlsClient.getKeyExchange();
        this.keyExchange.init(getContext());
    }

    protected void receiveNewSessionTicketMessage(ByteArrayInputStream byteArrayInputStream) {
        NewSessionTicket parse = NewSessionTicket.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        this.tlsClient.notifyNewSessionTicket(parse);
    }

    protected void receiveServerHelloMessage(ByteArrayInputStream byteArrayInputStream) {
        boolean z = true;
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        if (readVersion.isDTLS()) {
            throw new TlsFatalAlert((short) 47);
        } else if (!readVersion.equals(this.recordStream.getReadVersion())) {
            throw new TlsFatalAlert((short) 47);
        } else if (readVersion.isEqualOrEarlierVersionOf(getContext().getClientVersion())) {
            this.recordStream.setWriteVersion(readVersion);
            getContextAdmin().setServerVersion(readVersion);
            this.tlsClient.notifyServerVersion(readVersion);
            this.securityParameters.serverRandom = TlsUtils.readFully(32, (InputStream) byteArrayInputStream);
            this.selectedSessionID = TlsUtils.readOpaque8(byteArrayInputStream);
            if (this.selectedSessionID.length > 32) {
                throw new TlsFatalAlert((short) 47);
            }
            this.tlsClient.notifySessionID(this.selectedSessionID);
            boolean z2 = this.selectedSessionID.length > 0 && this.tlsSession != null && Arrays.areEqual(this.selectedSessionID, this.tlsSession.getSessionID());
            this.resumedSession = z2;
            int readUint16 = TlsUtils.readUint16(byteArrayInputStream);
            if (!Arrays.contains(this.offeredCipherSuites, readUint16) || readUint16 == 0 || CipherSuite.isSCSV(readUint16) || !TlsUtils.isValidCipherSuiteForVersion(readUint16, readVersion)) {
                throw new TlsFatalAlert((short) 47);
            }
            this.tlsClient.notifySelectedCipherSuite(readUint16);
            short readUint8 = TlsUtils.readUint8(byteArrayInputStream);
            if (Arrays.contains(this.offeredCompressionMethods, readUint8)) {
                this.tlsClient.notifySelectedCompressionMethod(readUint8);
                this.serverExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
                if (TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.serverExtensions) != this.securityParameters.extendedMasterSecret) {
                    throw new TlsFatalAlert((short) 40);
                }
                if (this.serverExtensions != null) {
                    Enumeration keys = this.serverExtensions.keys();
                    while (keys.hasMoreElements()) {
                        Integer num = (Integer) keys.nextElement();
                        if (!num.equals(EXT_RenegotiationInfo)) {
                            if (TlsUtils.getExtensionData(this.clientExtensions, num) == null) {
                                throw new TlsFatalAlert(AlertDescription.unsupported_extension);
                            } else if (!num.equals(TlsExtensionsUtils.EXT_extended_master_secret) && this.resumedSession) {
                            }
                        }
                    }
                }
                byte[] extensionData = TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo);
                if (extensionData != null) {
                    this.secure_renegotiation = true;
                    if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                        throw new TlsFatalAlert((short) 40);
                    }
                }
                this.tlsClient.notifySecureRenegotiation(this.secure_renegotiation);
                Hashtable hashtable = this.clientExtensions;
                Hashtable hashtable2 = this.serverExtensions;
                if (this.resumedSession) {
                    if (readUint16 == this.sessionParameters.getCipherSuite() && readUint8 == this.sessionParameters.getCompressionAlgorithm()) {
                        hashtable = null;
                        hashtable2 = this.sessionParameters.readServerExtensions();
                        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(hashtable2);
                    } else {
                        throw new TlsFatalAlert((short) 47);
                    }
                }
                this.securityParameters.cipherSuite = readUint16;
                this.securityParameters.compressionAlgorithm = readUint8;
                if (hashtable2 != null) {
                    boolean hasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable2);
                    if (!hasEncryptThenMACExtension || TlsUtils.isBlockCipherSuite(readUint16)) {
                        this.securityParameters.encryptThenMAC = hasEncryptThenMACExtension;
                        this.securityParameters.maxFragmentLength = processMaxFragmentLengthExtension(hashtable, hashtable2, (short) 47);
                        this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable2);
                        boolean z3 = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(hashtable2, TlsExtensionsUtils.EXT_status_request, (short) 47);
                        this.allowCertificateStatus = z3;
                        if (this.resumedSession || !TlsUtils.hasExpectedEmptyExtensionData(hashtable2, TlsProtocol.EXT_SessionTicket, (short) 47)) {
                            z = false;
                        }
                        this.expectSessionTicket = z;
                    } else {
                        throw new TlsFatalAlert((short) 47);
                    }
                }
                if (hashtable != null) {
                    this.tlsClient.processServerExtensions(hashtable2);
                    return;
                }
                return;
            }
            throw new TlsFatalAlert((short) 47);
        } else {
            throw new TlsFatalAlert((short) 47);
        }
    }

    protected void sendCertificateVerifyMessage(DigitallySigned digitallySigned) {
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 15);
        digitallySigned.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendClientHelloMessage() {
        short s = (short) 1;
        this.recordStream.setWriteVersion(this.tlsClient.getClientHelloRecordLayerVersion());
        ProtocolVersion clientVersion = this.tlsClient.getClientVersion();
        if (clientVersion.isDTLS()) {
            throw new TlsFatalAlert((short) 80);
        }
        getContextAdmin().setClientVersion(clientVersion);
        byte[] bArr = TlsUtils.EMPTY_BYTES;
        if (this.tlsSession != null) {
            bArr = this.tlsSession.getSessionID();
            if (bArr == null || bArr.length > 32) {
                bArr = TlsUtils.EMPTY_BYTES;
            }
        }
        boolean isFallback = this.tlsClient.isFallback();
        this.offeredCipherSuites = this.tlsClient.getCipherSuites();
        this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
        if (!(bArr.length <= 0 || this.sessionParameters == null || (Arrays.contains(this.offeredCipherSuites, this.sessionParameters.getCipherSuite()) && Arrays.contains(this.offeredCompressionMethods, this.sessionParameters.getCompressionAlgorithm())))) {
            bArr = TlsUtils.EMPTY_BYTES;
        }
        this.clientExtensions = this.tlsClient.getClientExtensions();
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 1);
        TlsUtils.writeVersion(clientVersion, handshakeMessage);
        handshakeMessage.write(this.securityParameters.getClientRandom());
        TlsUtils.writeOpaque8(bArr, handshakeMessage);
        short s2 = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo) == null ? (short) 1 : (short) 0;
        if (Arrays.contains(this.offeredCipherSuites, (int) GF2Field.MASK)) {
            s = (short) 0;
        }
        if (!(s2 == (short) 0 || r1 == (short) 0)) {
            this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, (int) GF2Field.MASK);
        }
        if (isFallback && !Arrays.contains(this.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV)) {
            this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV);
        }
        TlsUtils.writeUint16ArrayWithUint16Length(this.offeredCipherSuites, handshakeMessage);
        TlsUtils.writeUint8ArrayWithUint8Length(this.offeredCompressionMethods, handshakeMessage);
        if (this.clientExtensions != null) {
            TlsProtocol.writeExtensions(handshakeMessage, this.clientExtensions);
        }
        handshakeMessage.writeToRecordStream();
    }

    protected void sendClientKeyExchangeMessage() {
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 16);
        this.keyExchange.generateClientKeyExchange(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
}
