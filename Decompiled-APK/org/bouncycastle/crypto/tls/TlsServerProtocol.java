package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class TlsServerProtocol extends TlsProtocol {
    protected CertificateRequest certificateRequest;
    protected short clientCertificateType;
    protected TlsKeyExchange keyExchange;
    protected TlsHandshakeHash prepareFinishHash;
    protected TlsCredentials serverCredentials;
    protected TlsServer tlsServer;
    TlsServerContextImpl tlsServerContext;

    public TlsServerProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsServer = null;
        this.tlsServerContext = null;
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.clientCertificateType = (short) -1;
        this.prepareFinishHash = null;
    }

    public void accept(TlsServer tlsServer) {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'tlsServer' cannot be null");
        } else if (this.tlsServer != null) {
            throw new IllegalStateException("'accept' can only be called once");
        } else {
            this.tlsServer = tlsServer;
            this.securityParameters = new SecurityParameters();
            this.securityParameters.entity = 0;
            this.tlsServerContext = new TlsServerContextImpl(this.secureRandom, this.securityParameters);
            this.securityParameters.serverRandom = TlsProtocol.createRandomBlock(tlsServer.shouldUseGMTUnixTime(), this.tlsServerContext.getNonceRandomGenerator());
            this.tlsServer.init(this.tlsServerContext);
            this.recordStream.init(this.tlsServerContext);
            this.recordStream.setRestrictReadVersion(false);
            completeHandshake();
        }
    }

    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.prepareFinishHash = null;
    }

    protected boolean expectCertificateVerifyMessage() {
        return this.clientCertificateType >= (short) 0 && TlsUtils.hasSigningCapability(this.clientCertificateType);
    }

    protected TlsContext getContext() {
        return this.tlsServerContext;
    }

    AbstractTlsContext getContextAdmin() {
        return this.tlsServerContext;
    }

    protected TlsPeer getPeer() {
        return this.tlsServer;
    }

    protected void handleHandshakeMessage(short s, byte[] bArr) {
        Certificate certificate = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                switch (this.connection_state) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        receiveClientHelloMessage(byteArrayInputStream);
                        this.connection_state = (short) 1;
                        sendServerHelloMessage();
                        this.connection_state = (short) 2;
                        Vector serverSupplementalData = this.tlsServer.getServerSupplementalData();
                        if (serverSupplementalData != null) {
                            sendSupplementalDataMessage(serverSupplementalData);
                        }
                        this.connection_state = (short) 3;
                        this.keyExchange = this.tlsServer.getKeyExchange();
                        this.keyExchange.init(getContext());
                        this.serverCredentials = this.tlsServer.getCredentials();
                        if (this.serverCredentials == null) {
                            this.keyExchange.skipServerCredentials();
                        } else {
                            this.keyExchange.processServerCredentials(this.serverCredentials);
                            certificate = this.serverCredentials.getCertificate();
                            sendCertificateMessage(certificate);
                        }
                        this.connection_state = (short) 4;
                        if (certificate == null || certificate.isEmpty()) {
                            this.allowCertificateStatus = false;
                        }
                        if (this.allowCertificateStatus) {
                            CertificateStatus certificateStatus = this.tlsServer.getCertificateStatus();
                            if (certificateStatus != null) {
                                sendCertificateStatusMessage(certificateStatus);
                            }
                        }
                        this.connection_state = (short) 5;
                        byte[] generateServerKeyExchange = this.keyExchange.generateServerKeyExchange();
                        if (generateServerKeyExchange != null) {
                            sendServerKeyExchangeMessage(generateServerKeyExchange);
                        }
                        this.connection_state = (short) 6;
                        if (this.serverCredentials != null) {
                            this.certificateRequest = this.tlsServer.getCertificateRequest();
                            if (this.certificateRequest != null) {
                                this.keyExchange.validateCertificateRequest(this.certificateRequest);
                                sendCertificateRequestMessage(this.certificateRequest);
                                TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                            }
                        }
                        this.connection_state = (short) 7;
                        sendServerHelloDoneMessage();
                        this.connection_state = (short) 8;
                        this.recordStream.getHandshakeHash().sealHashAlgorithms();
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case CertStatus.UNREVOKED /*11*/:
                switch (this.connection_state) {
                    case X509KeyUsage.keyAgreement /*8*/:
                        this.tlsServer.processClientSupplementalData(null);
                        break;
                    case NamedCurve.sect283k1 /*9*/:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                if (this.certificateRequest == null) {
                    throw new TlsFatalAlert((short) 10);
                }
                receiveCertificateMessage(byteArrayInputStream);
                this.connection_state = (short) 10;
            case NamedCurve.secp160k1 /*15*/:
                switch (this.connection_state) {
                    case CertStatus.UNREVOKED /*11*/:
                        if (expectCertificateVerifyMessage()) {
                            receiveCertificateVerifyMessage(byteArrayInputStream);
                            this.connection_state = (short) 12;
                            return;
                        }
                        throw new TlsFatalAlert((short) 10);
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case X509KeyUsage.dataEncipherment /*16*/:
                switch (this.connection_state) {
                    case X509KeyUsage.keyAgreement /*8*/:
                        this.tlsServer.processClientSupplementalData(null);
                        break;
                    case NamedCurve.sect283k1 /*9*/:
                        break;
                    case NamedCurve.sect283r1 /*10*/:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                if (this.certificateRequest == null) {
                    this.keyExchange.skipClientCredentials();
                } else if (TlsUtils.isTLSv12(getContext())) {
                    throw new TlsFatalAlert((short) 10);
                } else if (!TlsUtils.isSSL(getContext())) {
                    notifyClientCertificate(Certificate.EMPTY_CHAIN);
                } else if (this.peerCertificate == null) {
                    throw new TlsFatalAlert((short) 10);
                }
                receiveClientKeyExchangeMessage(byteArrayInputStream);
                this.connection_state = (short) 11;
            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                switch (this.connection_state) {
                    case CertStatus.UNREVOKED /*11*/:
                        if (expectCertificateVerifyMessage()) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        break;
                    case CertStatus.UNDETERMINED /*12*/:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                processFinishedMessage(byteArrayInputStream);
                this.connection_state = (short) 13;
                if (this.expectSessionTicket) {
                    sendNewSessionTicketMessage(this.tlsServer.getNewSessionTicket());
                    sendChangeCipherSpecMessage();
                }
                this.connection_state = (short) 14;
                sendFinishedMessage();
                this.connection_state = (short) 15;
                this.connection_state = (short) 16;
            case NamedCurve.secp256r1 /*23*/:
                switch (this.connection_state) {
                    case X509KeyUsage.keyAgreement /*8*/:
                        this.tlsServer.processClientSupplementalData(TlsProtocol.readSupplementalDataMessage(byteArrayInputStream));
                        this.connection_state = (short) 9;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            default:
                throw new TlsFatalAlert((short) 10);
        }
    }

    protected void handleWarningMessage(short s) {
        switch (s) {
            case EACTags.INTERCHANGE_PROFILE /*41*/:
                if (TlsUtils.isSSL(getContext()) && this.certificateRequest != null) {
                    notifyClientCertificate(Certificate.EMPTY_CHAIN);
                }
            default:
                super.handleWarningMessage(s);
        }
    }

    protected void notifyClientCertificate(Certificate certificate) {
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        } else if (this.peerCertificate != null) {
            throw new TlsFatalAlert((short) 10);
        } else {
            this.peerCertificate = certificate;
            if (certificate.isEmpty()) {
                this.keyExchange.skipClientCredentials();
            } else {
                this.clientCertificateType = TlsUtils.getClientCertificateType(certificate, this.serverCredentials.getCertificate());
                this.keyExchange.processClientCertificate(certificate);
            }
            this.tlsServer.notifyClientCertificate(certificate);
        }
    }

    protected void receiveCertificateMessage(ByteArrayInputStream byteArrayInputStream) {
        Certificate parse = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        notifyClientCertificate(parse);
    }

    protected void receiveCertificateVerifyMessage(ByteArrayInputStream byteArrayInputStream) {
        DigitallySigned parse = DigitallySigned.parse(getContext(), byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        try {
            byte[] finalHash = TlsUtils.isTLSv12(getContext()) ? this.prepareFinishHash.getFinalHash(parse.getAlgorithm().getHash()) : this.securityParameters.getSessionHash();
            AsymmetricKeyParameter createKey = PublicKeyFactory.createKey(this.peerCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner createTlsSigner = TlsUtils.createTlsSigner(this.clientCertificateType);
            createTlsSigner.init(getContext());
            if (!createTlsSigner.verifyRawSignature(parse.getAlgorithm(), parse.getSignature(), createKey, finalHash)) {
                throw new TlsFatalAlert((short) 51);
            }
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 51, e);
        }
    }

    protected void receiveClientHelloMessage(ByteArrayInputStream byteArrayInputStream) {
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        this.recordStream.setWriteVersion(readVersion);
        if (readVersion.isDTLS()) {
            throw new TlsFatalAlert((short) 47);
        }
        byte[] readFully = TlsUtils.readFully(32, (InputStream) byteArrayInputStream);
        if (TlsUtils.readOpaque8(byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert((short) 47);
        }
        int readUint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (readUint16 < 2 || (readUint16 & 1) != 0) {
            throw new TlsFatalAlert((short) 50);
        }
        this.offeredCipherSuites = TlsUtils.readUint16Array(readUint16 / 2, byteArrayInputStream);
        short readUint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (readUint8 < (short) 1) {
            throw new TlsFatalAlert((short) 47);
        }
        this.offeredCompressionMethods = TlsUtils.readUint8Array(readUint8, byteArrayInputStream);
        this.clientExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        getContextAdmin().setClientVersion(readVersion);
        this.tlsServer.notifyClientVersion(readVersion);
        this.tlsServer.notifyFallback(Arrays.contains(this.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV));
        this.securityParameters.clientRandom = readFully;
        this.tlsServer.notifyOfferedCipherSuites(this.offeredCipherSuites);
        this.tlsServer.notifyOfferedCompressionMethods(this.offeredCompressionMethods);
        if (Arrays.contains(this.offeredCipherSuites, (int) GF2Field.MASK)) {
            this.secure_renegotiation = true;
        }
        byte[] extensionData = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo);
        if (extensionData != null) {
            this.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert((short) 40);
            }
        }
        this.tlsServer.notifySecureRenegotiation(this.secure_renegotiation);
        if (this.clientExtensions != null) {
            this.tlsServer.processClientExtensions(this.clientExtensions);
        }
    }

    protected void receiveClientKeyExchangeMessage(ByteArrayInputStream byteArrayInputStream) {
        this.keyExchange.processClientKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        this.prepareFinishHash = this.recordStream.prepareToFinish();
        this.securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(getContext(), this.prepareFinishHash, null);
        TlsProtocol.establishMasterSecret(getContext(), this.keyExchange);
        this.recordStream.setPendingConnectionState(getPeer().getCompression(), getPeer().getCipher());
        if (!this.expectSessionTicket) {
            sendChangeCipherSpecMessage();
        }
    }

    protected void sendCertificateRequestMessage(CertificateRequest certificateRequest) {
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 13);
        certificateRequest.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendCertificateStatusMessage(CertificateStatus certificateStatus) {
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 22);
        certificateStatus.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendNewSessionTicketMessage(NewSessionTicket newSessionTicket) {
        if (newSessionTicket == null) {
            throw new TlsFatalAlert((short) 80);
        }
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 4);
        newSessionTicket.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendServerHelloDoneMessage() {
        byte[] bArr = new byte[4];
        TlsUtils.writeUint8((short) 14, bArr, 0);
        TlsUtils.writeUint24(0, bArr, 1);
        writeHandshakeMessage(bArr, 0, bArr.length);
    }

    protected void sendServerHelloMessage() {
        boolean z = false;
        OutputStream handshakeMessage = new HandshakeMessage(this, (short) 2);
        ProtocolVersion serverVersion = this.tlsServer.getServerVersion();
        if (serverVersion.isEqualOrEarlierVersionOf(getContext().getClientVersion())) {
            this.recordStream.setReadVersion(serverVersion);
            this.recordStream.setWriteVersion(serverVersion);
            this.recordStream.setRestrictReadVersion(true);
            getContextAdmin().setServerVersion(serverVersion);
            TlsUtils.writeVersion(serverVersion, handshakeMessage);
            handshakeMessage.write(this.securityParameters.serverRandom);
            TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, handshakeMessage);
            int selectedCipherSuite = this.tlsServer.getSelectedCipherSuite();
            if (!Arrays.contains(this.offeredCipherSuites, selectedCipherSuite) || selectedCipherSuite == 0 || CipherSuite.isSCSV(selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(selectedCipherSuite, serverVersion)) {
                throw new TlsFatalAlert((short) 80);
            }
            this.securityParameters.cipherSuite = selectedCipherSuite;
            short selectedCompressionMethod = this.tlsServer.getSelectedCompressionMethod();
            if (Arrays.contains(this.offeredCompressionMethods, selectedCompressionMethod)) {
                this.securityParameters.compressionAlgorithm = selectedCompressionMethod;
                TlsUtils.writeUint16(selectedCipherSuite, handshakeMessage);
                TlsUtils.writeUint8(selectedCompressionMethod, handshakeMessage);
                this.serverExtensions = this.tlsServer.getServerExtensions();
                if (this.secure_renegotiation) {
                    if (TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo) == null) {
                        this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
                        this.serverExtensions.put(EXT_RenegotiationInfo, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
                    }
                }
                if (this.securityParameters.extendedMasterSecret) {
                    this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.serverExtensions);
                    TlsExtensionsUtils.addExtendedMasterSecretExtension(this.serverExtensions);
                }
                if (this.serverExtensions != null) {
                    this.securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(this.serverExtensions);
                    this.securityParameters.maxFragmentLength = processMaxFragmentLengthExtension(this.clientExtensions, this.serverExtensions, (short) 80);
                    this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(this.serverExtensions);
                    boolean z2 = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short) 80);
                    this.allowCertificateStatus = z2;
                    if (!this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsProtocol.EXT_SessionTicket, (short) 80)) {
                        z = true;
                    }
                    this.expectSessionTicket = z;
                    TlsProtocol.writeExtensions(handshakeMessage, this.serverExtensions);
                }
                if (this.securityParameters.maxFragmentLength >= (short) 0) {
                    this.recordStream.setPlaintextLimit(1 << (this.securityParameters.maxFragmentLength + 8));
                }
                this.securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(getContext(), this.securityParameters.getCipherSuite());
                this.securityParameters.verifyDataLength = 12;
                handshakeMessage.writeToRecordStream();
                this.recordStream.notifyHelloComplete();
                return;
            }
            throw new TlsFatalAlert((short) 80);
        }
        throw new TlsFatalAlert((short) 80);
    }

    protected void sendServerKeyExchangeMessage(byte[] bArr) {
        HandshakeMessage handshakeMessage = new HandshakeMessage((short) 12, bArr.length);
        handshakeMessage.write(bArr);
        handshakeMessage.writeToRecordStream();
    }
}
