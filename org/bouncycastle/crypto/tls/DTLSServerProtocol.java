package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class DTLSServerProtocol extends DTLSProtocol {
    protected boolean verifyRequests;

    protected static class ServerHandshakeState {
        boolean allowCertificateStatus;
        CertificateRequest certificateRequest;
        Certificate clientCertificate;
        short clientCertificateType;
        Hashtable clientExtensions;
        boolean expectSessionTicket;
        TlsKeyExchange keyExchange;
        short maxFragmentLength;
        int[] offeredCipherSuites;
        short[] offeredCompressionMethods;
        boolean secure_renegotiation;
        int selectedCipherSuite;
        short selectedCompressionMethod;
        TlsServer server;
        TlsServerContextImpl serverContext;
        TlsCredentials serverCredentials;
        Hashtable serverExtensions;

        protected ServerHandshakeState() {
            this.server = null;
            this.serverContext = null;
            this.selectedCipherSuite = -1;
            this.selectedCompressionMethod = (short) -1;
            this.secure_renegotiation = false;
            this.maxFragmentLength = (short) -1;
            this.allowCertificateStatus = false;
            this.expectSessionTicket = false;
            this.serverExtensions = null;
            this.keyExchange = null;
            this.serverCredentials = null;
            this.certificateRequest = null;
            this.clientCertificateType = (short) -1;
            this.clientCertificate = null;
        }
    }

    public DTLSServerProtocol(SecureRandom secureRandom) {
        super(secureRandom);
        this.verifyRequests = true;
    }

    public DTLSTransport accept(TlsServer tlsServer, DatagramTransport datagramTransport) {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'server' cannot be null");
        } else if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        } else {
            SecurityParameters securityParameters = new SecurityParameters();
            securityParameters.entity = 0;
            ServerHandshakeState serverHandshakeState = new ServerHandshakeState();
            serverHandshakeState.server = tlsServer;
            serverHandshakeState.serverContext = new TlsServerContextImpl(this.secureRandom, securityParameters);
            securityParameters.serverRandom = TlsProtocol.createRandomBlock(tlsServer.shouldUseGMTUnixTime(), serverHandshakeState.serverContext.getNonceRandomGenerator());
            tlsServer.init(serverHandshakeState.serverContext);
            DTLSRecordLayer dTLSRecordLayer = new DTLSRecordLayer(datagramTransport, serverHandshakeState.serverContext, tlsServer, (short) 22);
            try {
                return serverHandshake(serverHandshakeState, dTLSRecordLayer);
            } catch (TlsFatalAlert e) {
                dTLSRecordLayer.fail(e.getAlertDescription());
                throw e;
            } catch (IOException e2) {
                dTLSRecordLayer.fail((short) 80);
                throw e2;
            } catch (Throwable e3) {
                dTLSRecordLayer.fail((short) 80);
                throw new TlsFatalAlert((short) 80, e3);
            }
        }
    }

    protected boolean expectCertificateVerifyMessage(ServerHandshakeState serverHandshakeState) {
        return serverHandshakeState.clientCertificateType >= (short) 0 && TlsUtils.hasSigningCapability(serverHandshakeState.clientCertificateType);
    }

    protected byte[] generateCertificateRequest(ServerHandshakeState serverHandshakeState, CertificateRequest certificateRequest) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateRequest.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateCertificateStatus(ServerHandshakeState serverHandshakeState, CertificateStatus certificateStatus) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateStatus.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateNewSessionTicket(ServerHandshakeState serverHandshakeState, NewSessionTicket newSessionTicket) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        newSessionTicket.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateServerHello(ServerHandshakeState serverHandshakeState) {
        SecurityParameters securityParameters = serverHandshakeState.serverContext.getSecurityParameters();
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProtocolVersion serverVersion = serverHandshakeState.server.getServerVersion();
        if (serverVersion.isEqualOrEarlierVersionOf(serverHandshakeState.serverContext.getClientVersion())) {
            serverHandshakeState.serverContext.setServerVersion(serverVersion);
            TlsUtils.writeVersion(serverHandshakeState.serverContext.getServerVersion(), byteArrayOutputStream);
            byteArrayOutputStream.write(securityParameters.getServerRandom());
            TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, byteArrayOutputStream);
            serverHandshakeState.selectedCipherSuite = serverHandshakeState.server.getSelectedCipherSuite();
            if (!Arrays.contains(serverHandshakeState.offeredCipherSuites, serverHandshakeState.selectedCipherSuite) || serverHandshakeState.selectedCipherSuite == 0 || CipherSuite.isSCSV(serverHandshakeState.selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(serverHandshakeState.selectedCipherSuite, serverVersion)) {
                throw new TlsFatalAlert((short) 80);
            }
            DTLSProtocol.validateSelectedCipherSuite(serverHandshakeState.selectedCipherSuite, (short) 80);
            serverHandshakeState.selectedCompressionMethod = serverHandshakeState.server.getSelectedCompressionMethod();
            if (Arrays.contains(serverHandshakeState.offeredCompressionMethods, serverHandshakeState.selectedCompressionMethod)) {
                TlsUtils.writeUint16(serverHandshakeState.selectedCipherSuite, byteArrayOutputStream);
                TlsUtils.writeUint8(serverHandshakeState.selectedCompressionMethod, byteArrayOutputStream);
                serverHandshakeState.serverExtensions = serverHandshakeState.server.getServerExtensions();
                if (serverHandshakeState.secure_renegotiation) {
                    if ((TlsUtils.getExtensionData(serverHandshakeState.serverExtensions, TlsProtocol.EXT_RenegotiationInfo) == null ? 1 : null) != null) {
                        serverHandshakeState.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(serverHandshakeState.serverExtensions);
                        serverHandshakeState.serverExtensions.put(TlsProtocol.EXT_RenegotiationInfo, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
                    }
                }
                if (securityParameters.extendedMasterSecret) {
                    serverHandshakeState.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(serverHandshakeState.serverExtensions);
                    TlsExtensionsUtils.addExtendedMasterSecretExtension(serverHandshakeState.serverExtensions);
                }
                if (serverHandshakeState.serverExtensions != null) {
                    securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(serverHandshakeState.serverExtensions);
                    serverHandshakeState.maxFragmentLength = DTLSProtocol.evaluateMaxFragmentLengthExtension(serverHandshakeState.clientExtensions, serverHandshakeState.serverExtensions, (short) 80);
                    securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(serverHandshakeState.serverExtensions);
                    serverHandshakeState.allowCertificateStatus = TlsUtils.hasExpectedEmptyExtensionData(serverHandshakeState.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short) 80);
                    serverHandshakeState.expectSessionTicket = TlsUtils.hasExpectedEmptyExtensionData(serverHandshakeState.serverExtensions, TlsProtocol.EXT_SessionTicket, (short) 80);
                    TlsProtocol.writeExtensions(byteArrayOutputStream, serverHandshakeState.serverExtensions);
                }
                return byteArrayOutputStream.toByteArray();
            }
            throw new TlsFatalAlert((short) 80);
        }
        throw new TlsFatalAlert((short) 80);
    }

    public boolean getVerifyRequests() {
        return this.verifyRequests;
    }

    protected void notifyClientCertificate(ServerHandshakeState serverHandshakeState, Certificate certificate) {
        if (serverHandshakeState.certificateRequest == null) {
            throw new IllegalStateException();
        } else if (serverHandshakeState.clientCertificate != null) {
            throw new TlsFatalAlert((short) 10);
        } else {
            serverHandshakeState.clientCertificate = certificate;
            if (certificate.isEmpty()) {
                serverHandshakeState.keyExchange.skipClientCredentials();
            } else {
                serverHandshakeState.clientCertificateType = TlsUtils.getClientCertificateType(certificate, serverHandshakeState.serverCredentials.getCertificate());
                serverHandshakeState.keyExchange.processClientCertificate(certificate);
            }
            serverHandshakeState.server.notifyClientCertificate(certificate);
        }
    }

    protected void processCertificateVerify(ServerHandshakeState serverHandshakeState, byte[] bArr, TlsHandshakeHash tlsHandshakeHash) {
        boolean z = false;
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Object obj = serverHandshakeState.serverContext;
        DigitallySigned parse = DigitallySigned.parse(obj, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        try {
            byte[] finalHash = TlsUtils.isTLSv12(obj) ? tlsHandshakeHash.getFinalHash(parse.getAlgorithm().getHash()) : obj.getSecurityParameters().getSessionHash();
            AsymmetricKeyParameter createKey = PublicKeyFactory.createKey(serverHandshakeState.clientCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner createTlsSigner = TlsUtils.createTlsSigner(serverHandshakeState.clientCertificateType);
            createTlsSigner.init(obj);
            z = createTlsSigner.verifyRawSignature(parse.getAlgorithm(), parse.getSignature(), createKey, finalHash);
        } catch (Exception e) {
        }
        if (!z) {
            throw new TlsFatalAlert((short) 51);
        }
    }

    protected void processClientCertificate(ServerHandshakeState serverHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Certificate parse = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        notifyClientCertificate(serverHandshakeState, parse);
    }

    protected void processClientHello(ServerHandshakeState serverHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        if (readVersion.isDTLS()) {
            byte[] readFully = TlsUtils.readFully(32, byteArrayInputStream);
            if (TlsUtils.readOpaque8(byteArrayInputStream).length > 32) {
                throw new TlsFatalAlert((short) 47);
            }
            TlsUtils.readOpaque8(byteArrayInputStream);
            int readUint16 = TlsUtils.readUint16(byteArrayInputStream);
            if (readUint16 < 2 || (readUint16 & 1) != 0) {
                throw new TlsFatalAlert((short) 50);
            }
            serverHandshakeState.offeredCipherSuites = TlsUtils.readUint16Array(readUint16 / 2, byteArrayInputStream);
            short readUint8 = TlsUtils.readUint8(byteArrayInputStream);
            if (readUint8 < (short) 1) {
                throw new TlsFatalAlert((short) 47);
            }
            serverHandshakeState.offeredCompressionMethods = TlsUtils.readUint8Array(readUint8, byteArrayInputStream);
            serverHandshakeState.clientExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
            TlsServerContextImpl tlsServerContextImpl = serverHandshakeState.serverContext;
            SecurityParameters securityParameters = tlsServerContextImpl.getSecurityParameters();
            securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(serverHandshakeState.clientExtensions);
            tlsServerContextImpl.setClientVersion(readVersion);
            serverHandshakeState.server.notifyClientVersion(readVersion);
            serverHandshakeState.server.notifyFallback(Arrays.contains(serverHandshakeState.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV));
            securityParameters.clientRandom = readFully;
            serverHandshakeState.server.notifyOfferedCipherSuites(serverHandshakeState.offeredCipherSuites);
            serverHandshakeState.server.notifyOfferedCompressionMethods(serverHandshakeState.offeredCompressionMethods);
            if (Arrays.contains(serverHandshakeState.offeredCipherSuites, (int) GF2Field.MASK)) {
                serverHandshakeState.secure_renegotiation = true;
            }
            byte[] extensionData = TlsUtils.getExtensionData(serverHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo);
            if (extensionData != null) {
                serverHandshakeState.secure_renegotiation = true;
                if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                    throw new TlsFatalAlert((short) 40);
                }
            }
            serverHandshakeState.server.notifySecureRenegotiation(serverHandshakeState.secure_renegotiation);
            if (serverHandshakeState.clientExtensions != null) {
                serverHandshakeState.server.processClientExtensions(serverHandshakeState.clientExtensions);
                return;
            }
            return;
        }
        throw new TlsFatalAlert((short) 47);
    }

    protected void processClientKeyExchange(ServerHandshakeState serverHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        serverHandshakeState.keyExchange.processClientKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected void processClientSupplementalData(ServerHandshakeState serverHandshakeState, byte[] bArr) {
        serverHandshakeState.server.processClientSupplementalData(TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(bArr)));
    }

    protected DTLSTransport serverHandshake(ServerHandshakeState serverHandshakeState, DTLSRecordLayer dTLSRecordLayer) {
        SecurityParameters securityParameters = serverHandshakeState.serverContext.getSecurityParameters();
        DTLSReliableHandshake dTLSReliableHandshake = new DTLSReliableHandshake(serverHandshakeState.serverContext, dTLSRecordLayer);
        Message receiveMessage = dTLSReliableHandshake.receiveMessage();
        serverHandshakeState.serverContext.setClientVersion(dTLSRecordLayer.getDiscoveredPeerVersion());
        if (receiveMessage.getType() == (short) 1) {
            Certificate certificate;
            processClientHello(serverHandshakeState, receiveMessage.getBody());
            byte[] generateServerHello = generateServerHello(serverHandshakeState);
            if (serverHandshakeState.maxFragmentLength >= (short) 0) {
                dTLSRecordLayer.setPlaintextLimit(1 << (serverHandshakeState.maxFragmentLength + 8));
            }
            securityParameters.cipherSuite = serverHandshakeState.selectedCipherSuite;
            securityParameters.compressionAlgorithm = serverHandshakeState.selectedCompressionMethod;
            securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(serverHandshakeState.serverContext, serverHandshakeState.selectedCipherSuite);
            securityParameters.verifyDataLength = 12;
            dTLSReliableHandshake.sendMessage((short) 2, generateServerHello);
            dTLSReliableHandshake.notifyHelloComplete();
            Vector serverSupplementalData = serverHandshakeState.server.getServerSupplementalData();
            if (serverSupplementalData != null) {
                dTLSReliableHandshake.sendMessage((short) 23, DTLSProtocol.generateSupplementalData(serverSupplementalData));
            }
            serverHandshakeState.keyExchange = serverHandshakeState.server.getKeyExchange();
            serverHandshakeState.keyExchange.init(serverHandshakeState.serverContext);
            serverHandshakeState.serverCredentials = serverHandshakeState.server.getCredentials();
            if (serverHandshakeState.serverCredentials == null) {
                serverHandshakeState.keyExchange.skipServerCredentials();
                certificate = null;
            } else {
                serverHandshakeState.keyExchange.processServerCredentials(serverHandshakeState.serverCredentials);
                certificate = serverHandshakeState.serverCredentials.getCertificate();
                dTLSReliableHandshake.sendMessage((short) 11, DTLSProtocol.generateCertificate(certificate));
            }
            if (certificate == null || certificate.isEmpty()) {
                serverHandshakeState.allowCertificateStatus = false;
            }
            if (serverHandshakeState.allowCertificateStatus) {
                CertificateStatus certificateStatus = serverHandshakeState.server.getCertificateStatus();
                if (certificateStatus != null) {
                    dTLSReliableHandshake.sendMessage((short) 22, generateCertificateStatus(serverHandshakeState, certificateStatus));
                }
            }
            generateServerHello = serverHandshakeState.keyExchange.generateServerKeyExchange();
            if (generateServerHello != null) {
                dTLSReliableHandshake.sendMessage((short) 12, generateServerHello);
            }
            if (serverHandshakeState.serverCredentials != null) {
                serverHandshakeState.certificateRequest = serverHandshakeState.server.getCertificateRequest();
                if (serverHandshakeState.certificateRequest != null) {
                    serverHandshakeState.keyExchange.validateCertificateRequest(serverHandshakeState.certificateRequest);
                    dTLSReliableHandshake.sendMessage((short) 13, generateCertificateRequest(serverHandshakeState, serverHandshakeState.certificateRequest));
                    TlsUtils.trackHashAlgorithms(dTLSReliableHandshake.getHandshakeHash(), serverHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
                }
            }
            dTLSReliableHandshake.sendMessage((short) 14, TlsUtils.EMPTY_BYTES);
            dTLSReliableHandshake.getHandshakeHash().sealHashAlgorithms();
            receiveMessage = dTLSReliableHandshake.receiveMessage();
            if (receiveMessage.getType() == (short) 23) {
                processClientSupplementalData(serverHandshakeState, receiveMessage.getBody());
                receiveMessage = dTLSReliableHandshake.receiveMessage();
            } else {
                serverHandshakeState.server.processClientSupplementalData(null);
            }
            if (serverHandshakeState.certificateRequest == null) {
                serverHandshakeState.keyExchange.skipClientCredentials();
            } else if (receiveMessage.getType() == (short) 11) {
                processClientCertificate(serverHandshakeState, receiveMessage.getBody());
                receiveMessage = dTLSReliableHandshake.receiveMessage();
            } else if (TlsUtils.isTLSv12(serverHandshakeState.serverContext)) {
                throw new TlsFatalAlert((short) 10);
            } else {
                notifyClientCertificate(serverHandshakeState, Certificate.EMPTY_CHAIN);
            }
            if (receiveMessage.getType() == (short) 16) {
                processClientKeyExchange(serverHandshakeState, receiveMessage.getBody());
                TlsHandshakeHash prepareToFinish = dTLSReliableHandshake.prepareToFinish();
                securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, prepareToFinish, null);
                TlsProtocol.establishMasterSecret(serverHandshakeState.serverContext, serverHandshakeState.keyExchange);
                dTLSRecordLayer.initPendingEpoch(serverHandshakeState.server.getCipher());
                if (expectCertificateVerifyMessage(serverHandshakeState)) {
                    processCertificateVerify(serverHandshakeState, dTLSReliableHandshake.receiveMessageBody((short) 15), prepareToFinish);
                }
                processFinished(dTLSReliableHandshake.receiveMessageBody((short) 20), TlsUtils.calculateVerifyData(serverHandshakeState.serverContext, ExporterLabel.client_finished, TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                if (serverHandshakeState.expectSessionTicket) {
                    dTLSReliableHandshake.sendMessage((short) 4, generateNewSessionTicket(serverHandshakeState, serverHandshakeState.server.getNewSessionTicket()));
                }
                dTLSReliableHandshake.sendMessage((short) 20, TlsUtils.calculateVerifyData(serverHandshakeState.serverContext, ExporterLabel.server_finished, TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                dTLSReliableHandshake.finish();
                serverHandshakeState.server.notifyHandshakeComplete();
                return new DTLSTransport(dTLSRecordLayer);
            }
            throw new TlsFatalAlert((short) 10);
        }
        throw new TlsFatalAlert((short) 10);
    }

    public void setVerifyRequests(boolean z) {
        this.verifyRequests = z;
    }
}
