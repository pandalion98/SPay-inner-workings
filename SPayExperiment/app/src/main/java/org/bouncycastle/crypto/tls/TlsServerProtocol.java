/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.AbstractTlsContext;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.CertificateStatus;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.RecordStream;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCompression;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsServer;
import org.bouncycastle.crypto.tls.TlsServerContext;
import org.bouncycastle.crypto.tls.TlsServerContextImpl;
import org.bouncycastle.crypto.tls.TlsSigner;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;

public class TlsServerProtocol
extends TlsProtocol {
    protected CertificateRequest certificateRequest = null;
    protected short clientCertificateType = (short)-1;
    protected TlsKeyExchange keyExchange = null;
    protected TlsHandshakeHash prepareFinishHash = null;
    protected TlsCredentials serverCredentials = null;
    protected TlsServer tlsServer = null;
    TlsServerContextImpl tlsServerContext = null;

    public TlsServerProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
    }

    public void accept(TlsServer tlsServer) {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'tlsServer' cannot be null");
        }
        if (this.tlsServer != null) {
            throw new IllegalStateException("'accept' can only be called once");
        }
        this.tlsServer = tlsServer;
        this.securityParameters = new SecurityParameters();
        this.securityParameters.entity = 0;
        this.tlsServerContext = new TlsServerContextImpl(this.secureRandom, this.securityParameters);
        this.securityParameters.serverRandom = TlsServerProtocol.createRandomBlock(tlsServer.shouldUseGMTUnixTime(), this.tlsServerContext.getNonceRandomGenerator());
        this.tlsServer.init(this.tlsServerContext);
        this.recordStream.init(this.tlsServerContext);
        this.recordStream.setRestrictReadVersion(false);
        this.completeHandshake();
    }

    @Override
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.prepareFinishHash = null;
    }

    protected boolean expectCertificateVerifyMessage() {
        return this.clientCertificateType >= 0 && TlsUtils.hasSigningCapability(this.clientCertificateType);
    }

    @Override
    protected TlsContext getContext() {
        return this.tlsServerContext;
    }

    @Override
    AbstractTlsContext getContextAdmin() {
        return this.tlsServerContext;
    }

    @Override
    protected TlsPeer getPeer() {
        return this.tlsServer;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    protected void handleHandshakeMessage(short var1_1, byte[] var2_2) {
        var3_3 = null;
        var4_4 = new ByteArrayInputStream(var2_2);
        switch (var1_1) {
            default: {
                throw new TlsFatalAlert(10);
            }
            case 1: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 0: 
                }
                this.receiveClientHelloMessage(var4_4);
                this.connection_state = 1;
                this.sendServerHelloMessage();
                this.connection_state = (short)2;
                var5_5 = this.tlsServer.getServerSupplementalData();
                if (var5_5 != null) {
                    this.sendSupplementalDataMessage(var5_5);
                }
                this.connection_state = (short)3;
                this.keyExchange = this.tlsServer.getKeyExchange();
                this.keyExchange.init(this.getContext());
                this.serverCredentials = this.tlsServer.getCredentials();
                if (this.serverCredentials == null) {
                    this.keyExchange.skipServerCredentials();
                } else {
                    this.keyExchange.processServerCredentials(this.serverCredentials);
                    var3_3 = this.serverCredentials.getCertificate();
                    this.sendCertificateMessage(var3_3);
                }
                this.connection_state = (short)4;
                if (var3_3 == null || var3_3.isEmpty()) {
                    this.allowCertificateStatus = false;
                }
                if (this.allowCertificateStatus && (var7_6 = this.tlsServer.getCertificateStatus()) != null) {
                    this.sendCertificateStatusMessage(var7_6);
                }
                this.connection_state = (short)5;
                var6_7 = this.keyExchange.generateServerKeyExchange();
                if (var6_7 != null) {
                    this.sendServerKeyExchangeMessage(var6_7);
                }
                this.connection_state = (short)6;
                if (this.serverCredentials != null) {
                    this.certificateRequest = this.tlsServer.getCertificateRequest();
                    if (this.certificateRequest != null) {
                        this.keyExchange.validateCertificateRequest(this.certificateRequest);
                        this.sendCertificateRequestMessage(this.certificateRequest);
                        TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                    }
                }
                this.connection_state = (short)7;
                this.sendServerHelloDoneMessage();
                this.connection_state = (short)8;
                this.recordStream.getHandshakeHash().sealHashAlgorithms();
                return;
            }
            case 23: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 8: 
                }
                this.tlsServer.processClientSupplementalData(TlsServerProtocol.readSupplementalDataMessage(var4_4));
                this.connection_state = (short)9;
                return;
            }
            case 11: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 8: {
                        this.tlsServer.processClientSupplementalData(null);
                    }
                    case 9: 
                }
                if (this.certificateRequest == null) {
                    throw new TlsFatalAlert(10);
                }
                this.receiveCertificateMessage(var4_4);
                this.connection_state = (short)10;
                return;
            }
            case 16: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 8: {
                        this.tlsServer.processClientSupplementalData(null);
                    }
                    case 9: {
                        if (this.certificateRequest != null) break;
                        this.keyExchange.skipClientCredentials();
                    }
                    case 10: {
                        ** GOTO lbl87
                    }
                }
                if (TlsUtils.isTLSv12(this.getContext())) {
                    throw new TlsFatalAlert(10);
                }
                if (TlsUtils.isSSL(this.getContext())) {
                    if (this.peerCertificate == null) {
                        throw new TlsFatalAlert(10);
                    }
                } else {
                    this.notifyClientCertificate(Certificate.EMPTY_CHAIN);
                }
lbl87: // 3 sources:
                this.receiveClientKeyExchangeMessage(var4_4);
                this.connection_state = (short)11;
                return;
            }
            case 15: {
                switch (this.connection_state) {
                    default: {
                        throw new TlsFatalAlert(10);
                    }
                    case 11: 
                }
                if (!this.expectCertificateVerifyMessage()) {
                    throw new TlsFatalAlert(10);
                }
                this.receiveCertificateVerifyMessage(var4_4);
                this.connection_state = (short)12;
                return;
            }
            case 20: 
        }
        switch (this.connection_state) {
            default: {
                throw new TlsFatalAlert(10);
            }
            case 11: {
                if (!this.expectCertificateVerifyMessage()) break;
                throw new TlsFatalAlert(10);
            }
            case 12: 
        }
        this.processFinishedMessage(var4_4);
        this.connection_state = (short)13;
        if (this.expectSessionTicket) {
            this.sendNewSessionTicketMessage(this.tlsServer.getNewSessionTicket());
            this.sendChangeCipherSpecMessage();
        }
        this.connection_state = (short)14;
        this.sendFinishedMessage();
        this.connection_state = (short)15;
        this.connection_state = (short)16;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void handleWarningMessage(short s2) {
        switch (s2) {
            default: {
                super.handleWarningMessage(s2);
                return;
            }
            case 41: {
                if (!TlsUtils.isSSL(this.getContext()) || this.certificateRequest == null) return;
                this.notifyClientCertificate(Certificate.EMPTY_CHAIN);
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void notifyClientCertificate(Certificate certificate) {
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        }
        if (this.peerCertificate != null) {
            throw new TlsFatalAlert(10);
        }
        this.peerCertificate = certificate;
        if (certificate.isEmpty()) {
            this.keyExchange.skipClientCredentials();
        } else {
            this.clientCertificateType = TlsUtils.getClientCertificateType(certificate, this.serverCredentials.getCertificate());
            this.keyExchange.processClientCertificate(certificate);
        }
        this.tlsServer.notifyClientCertificate(certificate);
    }

    protected void receiveCertificateMessage(ByteArrayInputStream byteArrayInputStream) {
        Certificate certificate = Certificate.parse((InputStream)byteArrayInputStream);
        TlsServerProtocol.assertEmpty(byteArrayInputStream);
        this.notifyClientCertificate(certificate);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void receiveCertificateVerifyMessage(ByteArrayInputStream byteArrayInputStream) {
        DigitallySigned digitallySigned = DigitallySigned.parse(this.getContext(), (InputStream)byteArrayInputStream);
        TlsServerProtocol.assertEmpty(byteArrayInputStream);
        try {
            byte[] arrby;
            if (TlsUtils.isTLSv12(this.getContext())) {
                arrby = this.prepareFinishHash.getFinalHash(digitallySigned.getAlgorithm().getHash());
            } else {
                byte[] arrby2 = this.securityParameters.getSessionHash();
                arrby = arrby2;
            }
            AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(this.peerCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner tlsSigner = TlsUtils.createTlsSigner(this.clientCertificateType);
            tlsSigner.init(this.getContext());
            if (tlsSigner.verifyRawSignature(digitallySigned.getAlgorithm(), digitallySigned.getSignature(), asymmetricKeyParameter, arrby)) return;
            {
                throw new TlsFatalAlert(51);
            }
        }
        catch (Exception exception) {
            throw new TlsFatalAlert(51, exception);
        }
    }

    protected void receiveClientHelloMessage(ByteArrayInputStream byteArrayInputStream) {
        byte[] arrby;
        ProtocolVersion protocolVersion = TlsUtils.readVersion((InputStream)byteArrayInputStream);
        this.recordStream.setWriteVersion(protocolVersion);
        if (protocolVersion.isDTLS()) {
            throw new TlsFatalAlert(47);
        }
        byte[] arrby2 = TlsUtils.readFully(32, (InputStream)byteArrayInputStream);
        if (TlsUtils.readOpaque8((InputStream)byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert(47);
        }
        int n2 = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (n2 < 2 || (n2 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        this.offeredCipherSuites = TlsUtils.readUint16Array(n2 / 2, (InputStream)byteArrayInputStream);
        short s2 = TlsUtils.readUint8((InputStream)byteArrayInputStream);
        if (s2 < 1) {
            throw new TlsFatalAlert(47);
        }
        this.offeredCompressionMethods = TlsUtils.readUint8Array(s2, (InputStream)byteArrayInputStream);
        this.clientExtensions = TlsServerProtocol.readExtensions(byteArrayInputStream);
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        this.getContextAdmin().setClientVersion(protocolVersion);
        this.tlsServer.notifyClientVersion(protocolVersion);
        this.tlsServer.notifyFallback(Arrays.contains((int[])this.offeredCipherSuites, (int)22016));
        this.securityParameters.clientRandom = arrby2;
        this.tlsServer.notifyOfferedCipherSuites(this.offeredCipherSuites);
        this.tlsServer.notifyOfferedCompressionMethods(this.offeredCompressionMethods);
        if (Arrays.contains((int[])this.offeredCipherSuites, (int)255)) {
            this.secure_renegotiation = true;
        }
        if ((arrby = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo)) != null) {
            this.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual((byte[])arrby, (byte[])TlsServerProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert(40);
            }
        }
        this.tlsServer.notifySecureRenegotiation(this.secure_renegotiation);
        if (this.clientExtensions != null) {
            this.tlsServer.processClientExtensions(this.clientExtensions);
        }
    }

    protected void receiveClientKeyExchangeMessage(ByteArrayInputStream byteArrayInputStream) {
        this.keyExchange.processClientKeyExchange((InputStream)byteArrayInputStream);
        TlsServerProtocol.assertEmpty(byteArrayInputStream);
        this.prepareFinishHash = this.recordStream.prepareToFinish();
        this.securityParameters.sessionHash = TlsServerProtocol.getCurrentPRFHash(this.getContext(), this.prepareFinishHash, null);
        TlsServerProtocol.establishMasterSecret(this.getContext(), this.keyExchange);
        this.recordStream.setPendingConnectionState(this.getPeer().getCompression(), this.getPeer().getCipher());
        if (!this.expectSessionTicket) {
            this.sendChangeCipherSpecMessage();
        }
    }

    protected void sendCertificateRequestMessage(CertificateRequest certificateRequest) {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(13);
        certificateRequest.encode((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendCertificateStatusMessage(CertificateStatus certificateStatus) {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(22);
        certificateStatus.encode((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendNewSessionTicketMessage(NewSessionTicket newSessionTicket) {
        if (newSessionTicket == null) {
            throw new TlsFatalAlert(80);
        }
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(4);
        newSessionTicket.encode((OutputStream)handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendServerHelloDoneMessage() {
        byte[] arrby = new byte[4];
        TlsUtils.writeUint8((short)14, arrby, 0);
        TlsUtils.writeUint24(0, arrby, 1);
        this.writeHandshakeMessage(arrby, 0, arrby.length);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void sendServerHelloMessage() {
        boolean bl;
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(2);
        ProtocolVersion protocolVersion = this.tlsServer.getServerVersion();
        if (!protocolVersion.isEqualOrEarlierVersionOf(this.getContext().getClientVersion())) {
            throw new TlsFatalAlert(80);
        }
        this.recordStream.setReadVersion(protocolVersion);
        this.recordStream.setWriteVersion(protocolVersion);
        this.recordStream.setRestrictReadVersion(true);
        this.getContextAdmin().setServerVersion(protocolVersion);
        TlsUtils.writeVersion(protocolVersion, (OutputStream)handshakeMessage);
        handshakeMessage.write(this.securityParameters.serverRandom);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, (OutputStream)handshakeMessage);
        int n2 = this.tlsServer.getSelectedCipherSuite();
        if (!Arrays.contains((int[])this.offeredCipherSuites, (int)n2) || n2 == 0 || CipherSuite.isSCSV(n2) || !TlsUtils.isValidCipherSuiteForVersion(n2, protocolVersion)) {
            throw new TlsFatalAlert(80);
        }
        this.securityParameters.cipherSuite = n2;
        short s2 = this.tlsServer.getSelectedCompressionMethod();
        if (!Arrays.contains((short[])this.offeredCompressionMethods, (short)s2)) {
            throw new TlsFatalAlert(80);
        }
        this.securityParameters.compressionAlgorithm = s2;
        TlsUtils.writeUint16(n2, (OutputStream)handshakeMessage);
        TlsUtils.writeUint8(s2, (OutputStream)handshakeMessage);
        this.serverExtensions = this.tlsServer.getServerExtensions();
        if (this.secure_renegotiation && (bl = TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo) == null)) {
            this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
            this.serverExtensions.put((Object)EXT_RenegotiationInfo, (Object)TlsServerProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
        }
        if (this.securityParameters.extendedMasterSecret) {
            this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
            TlsExtensionsUtils.addExtendedMasterSecretExtension(this.serverExtensions);
        }
        if (this.serverExtensions != null) {
            this.securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(this.serverExtensions);
            this.securityParameters.maxFragmentLength = this.processMaxFragmentLengthExtension(this.clientExtensions, this.serverExtensions, (short)80);
            this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(this.serverExtensions);
            boolean bl2 = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short)80);
            this.allowCertificateStatus = bl2;
            boolean bl3 = this.resumedSession;
            boolean bl4 = false;
            if (!bl3) {
                boolean bl5 = TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsProtocol.EXT_SessionTicket, (short)80);
                bl4 = false;
                if (bl5) {
                    bl4 = true;
                }
            }
            this.expectSessionTicket = bl4;
            TlsServerProtocol.writeExtensions((OutputStream)handshakeMessage, this.serverExtensions);
        }
        if (this.securityParameters.maxFragmentLength >= 0) {
            int n3 = 1 << 8 + this.securityParameters.maxFragmentLength;
            this.recordStream.setPlaintextLimit(n3);
        }
        this.securityParameters.prfAlgorithm = TlsServerProtocol.getPRFAlgorithm(this.getContext(), this.securityParameters.getCipherSuite());
        this.securityParameters.verifyDataLength = 12;
        handshakeMessage.writeToRecordStream();
        this.recordStream.notifyHelloComplete();
    }

    protected void sendServerKeyExchangeMessage(byte[] arrby) {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(12, arrby.length);
        handshakeMessage.write(arrby);
        handshakeMessage.writeToRecordStream();
    }
}

