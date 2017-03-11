package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.SessionParameters.Builder;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class DTLSClientProtocol extends DTLSProtocol {

    protected static class ClientHandshakeState {
        boolean allowCertificateStatus;
        TlsAuthentication authentication;
        CertificateRequest certificateRequest;
        CertificateStatus certificateStatus;
        TlsClient client;
        TlsClientContextImpl clientContext;
        TlsCredentials clientCredentials;
        Hashtable clientExtensions;
        boolean expectSessionTicket;
        TlsKeyExchange keyExchange;
        short maxFragmentLength;
        int[] offeredCipherSuites;
        short[] offeredCompressionMethods;
        boolean secure_renegotiation;
        int selectedCipherSuite;
        short selectedCompressionMethod;
        byte[] selectedSessionID;
        SessionParameters sessionParameters;
        Builder sessionParametersBuilder;
        TlsSession tlsSession;

        protected ClientHandshakeState() {
            this.client = null;
            this.clientContext = null;
            this.tlsSession = null;
            this.sessionParameters = null;
            this.sessionParametersBuilder = null;
            this.offeredCipherSuites = null;
            this.offeredCompressionMethods = null;
            this.clientExtensions = null;
            this.selectedSessionID = null;
            this.selectedCipherSuite = -1;
            this.selectedCompressionMethod = (short) -1;
            this.secure_renegotiation = false;
            this.maxFragmentLength = (short) -1;
            this.allowCertificateStatus = false;
            this.expectSessionTicket = false;
            this.keyExchange = null;
            this.authentication = null;
            this.certificateStatus = null;
            this.certificateRequest = null;
            this.clientCredentials = null;
        }
    }

    public DTLSClientProtocol(SecureRandom secureRandom) {
        super(secureRandom);
    }

    protected static byte[] patchClientHelloWithCookie(byte[] bArr, byte[] bArr2) {
        int readUint8 = TlsUtils.readUint8(bArr, 34) + 35;
        int i = readUint8 + 1;
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, readUint8);
        TlsUtils.checkUint8(bArr2.length);
        TlsUtils.writeUint8(bArr2.length, bArr3, readUint8);
        System.arraycopy(bArr2, 0, bArr3, i, bArr2.length);
        System.arraycopy(bArr, i, bArr3, bArr2.length + i, bArr.length - i);
        return bArr3;
    }

    protected DTLSTransport clientHandshake(ClientHandshakeState clientHandshakeState, DTLSRecordLayer dTLSRecordLayer) {
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        DTLSReliableHandshake dTLSReliableHandshake = new DTLSReliableHandshake(clientHandshakeState.clientContext, dTLSRecordLayer);
        byte[] generateClientHello = generateClientHello(clientHandshakeState, clientHandshakeState.client);
        dTLSReliableHandshake.sendMessage((short) 1, generateClientHello);
        Message receiveMessage = dTLSReliableHandshake.receiveMessage();
        while (receiveMessage.getType() == (short) 3) {
            if (dTLSRecordLayer.resetDiscoveredPeerVersion().isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
                byte[] patchClientHelloWithCookie = patchClientHelloWithCookie(generateClientHello, processHelloVerifyRequest(clientHandshakeState, receiveMessage.getBody()));
                dTLSReliableHandshake.resetHandshakeMessagesDigest();
                dTLSReliableHandshake.sendMessage((short) 1, patchClientHelloWithCookie);
                receiveMessage = dTLSReliableHandshake.receiveMessage();
            } else {
                throw new TlsFatalAlert((short) 47);
            }
        }
        if (receiveMessage.getType() == (short) 2) {
            short s;
            reportServerVersion(clientHandshakeState, dTLSRecordLayer.getDiscoveredPeerVersion());
            processServerHello(clientHandshakeState, receiveMessage.getBody());
            if (clientHandshakeState.maxFragmentLength >= (short) 0) {
                dTLSRecordLayer.setPlaintextLimit(1 << (clientHandshakeState.maxFragmentLength + 8));
            }
            securityParameters.cipherSuite = clientHandshakeState.selectedCipherSuite;
            securityParameters.compressionAlgorithm = clientHandshakeState.selectedCompressionMethod;
            securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(clientHandshakeState.clientContext, clientHandshakeState.selectedCipherSuite);
            securityParameters.verifyDataLength = 12;
            dTLSReliableHandshake.notifyHelloComplete();
            if (clientHandshakeState.selectedSessionID.length <= 0 || clientHandshakeState.tlsSession == null || !Arrays.areEqual(clientHandshakeState.selectedSessionID, clientHandshakeState.tlsSession.getSessionID())) {
                boolean z = false;
            } else {
                s = (short) 1;
            }
            if (s == (short) 0) {
                Certificate processServerCertificate;
                invalidateSession(clientHandshakeState);
                if (clientHandshakeState.selectedSessionID.length > 0) {
                    clientHandshakeState.tlsSession = new TlsSessionImpl(clientHandshakeState.selectedSessionID, null);
                }
                receiveMessage = dTLSReliableHandshake.receiveMessage();
                if (receiveMessage.getType() == (short) 23) {
                    processServerSupplementalData(clientHandshakeState, receiveMessage.getBody());
                    receiveMessage = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.client.processServerSupplementalData(null);
                }
                clientHandshakeState.keyExchange = clientHandshakeState.client.getKeyExchange();
                clientHandshakeState.keyExchange.init(clientHandshakeState.clientContext);
                if (receiveMessage.getType() == (short) 11) {
                    processServerCertificate = processServerCertificate(clientHandshakeState, receiveMessage.getBody());
                    receiveMessage = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.keyExchange.skipServerCredentials();
                    processServerCertificate = null;
                }
                if (processServerCertificate == null || processServerCertificate.isEmpty()) {
                    clientHandshakeState.allowCertificateStatus = false;
                }
                if (receiveMessage.getType() == (short) 22) {
                    processCertificateStatus(clientHandshakeState, receiveMessage.getBody());
                    receiveMessage = dTLSReliableHandshake.receiveMessage();
                }
                if (receiveMessage.getType() == (short) 12) {
                    processServerKeyExchange(clientHandshakeState, receiveMessage.getBody());
                    receiveMessage = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.keyExchange.skipServerKeyExchange();
                }
                if (receiveMessage.getType() == (short) 13) {
                    processCertificateRequest(clientHandshakeState, receiveMessage.getBody());
                    TlsUtils.trackHashAlgorithms(dTLSReliableHandshake.getHandshakeHash(), clientHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
                    receiveMessage = dTLSReliableHandshake.receiveMessage();
                }
                if (receiveMessage.getType() != (short) 14) {
                    throw new TlsFatalAlert((short) 10);
                } else if (receiveMessage.getBody().length != 0) {
                    throw new TlsFatalAlert((short) 50);
                } else {
                    dTLSReliableHandshake.getHandshakeHash().sealHashAlgorithms();
                    Vector clientSupplementalData = clientHandshakeState.client.getClientSupplementalData();
                    if (clientSupplementalData != null) {
                        dTLSReliableHandshake.sendMessage((short) 23, DTLSProtocol.generateSupplementalData(clientSupplementalData));
                    }
                    if (clientHandshakeState.certificateRequest != null) {
                        clientHandshakeState.clientCredentials = clientHandshakeState.authentication.getClientCredentials(clientHandshakeState.certificateRequest);
                        Certificate certificate = clientHandshakeState.clientCredentials != null ? clientHandshakeState.clientCredentials.getCertificate() : null;
                        if (certificate == null) {
                            certificate = Certificate.EMPTY_CHAIN;
                        }
                        dTLSReliableHandshake.sendMessage((short) 11, DTLSProtocol.generateCertificate(certificate));
                    }
                    if (clientHandshakeState.clientCredentials != null) {
                        clientHandshakeState.keyExchange.processClientCredentials(clientHandshakeState.clientCredentials);
                    } else {
                        clientHandshakeState.keyExchange.skipClientCredentials();
                    }
                    dTLSReliableHandshake.sendMessage((short) 16, generateClientKeyExchange(clientHandshakeState));
                    TlsHandshakeHash prepareToFinish = dTLSReliableHandshake.prepareToFinish();
                    securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, prepareToFinish, null);
                    TlsProtocol.establishMasterSecret(clientHandshakeState.clientContext, clientHandshakeState.keyExchange);
                    dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                    if (clientHandshakeState.clientCredentials != null && (clientHandshakeState.clientCredentials instanceof TlsSignerCredentials)) {
                        TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials) clientHandshakeState.clientCredentials;
                        SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(clientHandshakeState.clientContext, tlsSignerCredentials);
                        dTLSReliableHandshake.sendMessage((short) 15, generateCertificateVerify(clientHandshakeState, new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(signatureAndHashAlgorithm == null ? securityParameters.getSessionHash() : prepareToFinish.getFinalHash(signatureAndHashAlgorithm.getHash())))));
                    }
                    dTLSReliableHandshake.sendMessage((short) 20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.client_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                    if (clientHandshakeState.expectSessionTicket) {
                        receiveMessage = dTLSReliableHandshake.receiveMessage();
                        if (receiveMessage.getType() == (short) 4) {
                            processNewSessionTicket(clientHandshakeState, receiveMessage.getBody());
                        } else {
                            throw new TlsFatalAlert((short) 10);
                        }
                    }
                    processFinished(dTLSReliableHandshake.receiveMessageBody((short) 20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.server_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                    dTLSReliableHandshake.finish();
                    if (clientHandshakeState.tlsSession != null) {
                        clientHandshakeState.sessionParameters = new Builder().setCipherSuite(securityParameters.cipherSuite).setCompressionAlgorithm(securityParameters.compressionAlgorithm).setMasterSecret(securityParameters.masterSecret).setPeerCertificate(processServerCertificate).setPSKIdentity(securityParameters.pskIdentity).setSRPIdentity(securityParameters.srpIdentity).build();
                        clientHandshakeState.tlsSession = TlsUtils.importSession(clientHandshakeState.tlsSession.getSessionID(), clientHandshakeState.sessionParameters);
                        clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                    }
                    clientHandshakeState.client.notifyHandshakeComplete();
                    return new DTLSTransport(dTLSRecordLayer);
                }
            } else if (securityParameters.getCipherSuite() == clientHandshakeState.sessionParameters.getCipherSuite() && securityParameters.getCompressionAlgorithm() == clientHandshakeState.sessionParameters.getCompressionAlgorithm()) {
                securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(clientHandshakeState.sessionParameters.readServerExtensions());
                securityParameters.masterSecret = Arrays.clone(clientHandshakeState.sessionParameters.getMasterSecret());
                dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                processFinished(dTLSReliableHandshake.receiveMessageBody((short) 20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.server_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                dTLSReliableHandshake.sendMessage((short) 20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.client_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                dTLSReliableHandshake.finish();
                clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                clientHandshakeState.client.notifyHandshakeComplete();
                return new DTLSTransport(dTLSRecordLayer);
            } else {
                throw new TlsFatalAlert((short) 47);
            }
        }
        throw new TlsFatalAlert((short) 10);
    }

    public DTLSTransport connect(TlsClient tlsClient, DatagramTransport datagramTransport) {
        if (tlsClient == null) {
            throw new IllegalArgumentException("'client' cannot be null");
        } else if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        } else {
            SecurityParameters securityParameters = new SecurityParameters();
            securityParameters.entity = 1;
            ClientHandshakeState clientHandshakeState = new ClientHandshakeState();
            clientHandshakeState.client = tlsClient;
            clientHandshakeState.clientContext = new TlsClientContextImpl(this.secureRandom, securityParameters);
            securityParameters.clientRandom = TlsProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), clientHandshakeState.clientContext.getNonceRandomGenerator());
            tlsClient.init(clientHandshakeState.clientContext);
            DTLSRecordLayer dTLSRecordLayer = new DTLSRecordLayer(datagramTransport, clientHandshakeState.clientContext, tlsClient, (short) 22);
            TlsSession sessionToResume = clientHandshakeState.client.getSessionToResume();
            if (sessionToResume != null) {
                SessionParameters exportSessionParameters = sessionToResume.exportSessionParameters();
                if (exportSessionParameters != null) {
                    clientHandshakeState.tlsSession = sessionToResume;
                    clientHandshakeState.sessionParameters = exportSessionParameters;
                }
            }
            try {
                return clientHandshake(clientHandshakeState, dTLSRecordLayer);
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

    protected byte[] generateCertificateVerify(ClientHandshakeState clientHandshakeState, DigitallySigned digitallySigned) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        digitallySigned.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateClientHello(ClientHandshakeState clientHandshakeState, TlsClient tlsClient) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProtocolVersion clientVersion = tlsClient.getClientVersion();
        if (clientVersion.isDTLS()) {
            int i;
            TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
            tlsClientContextImpl.setClientVersion(clientVersion);
            TlsUtils.writeVersion(clientVersion, byteArrayOutputStream);
            SecurityParameters securityParameters = tlsClientContextImpl.getSecurityParameters();
            byteArrayOutputStream.write(securityParameters.getClientRandom());
            byte[] bArr = TlsUtils.EMPTY_BYTES;
            if (clientHandshakeState.tlsSession != null) {
                bArr = clientHandshakeState.tlsSession.getSessionID();
                if (bArr == null || bArr.length > 32) {
                    bArr = TlsUtils.EMPTY_BYTES;
                }
            }
            TlsUtils.writeOpaque8(bArr, byteArrayOutputStream);
            TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, byteArrayOutputStream);
            boolean isFallback = tlsClient.isFallback();
            clientHandshakeState.offeredCipherSuites = tlsClient.getCipherSuites();
            clientHandshakeState.clientExtensions = tlsClient.getClientExtensions();
            securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(clientHandshakeState.clientExtensions);
            if (TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo) == null) {
                i = 1;
            } else {
                short s = (short) 0;
            }
            if (Arrays.contains(clientHandshakeState.offeredCipherSuites, (int) GF2Field.MASK)) {
                short s2 = (short) 0;
            } else {
                int i2 = 1;
            }
            if (!(i == 0 || r3 == 0)) {
                clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, (int) GF2Field.MASK);
            }
            if (isFallback && !Arrays.contains(clientHandshakeState.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV)) {
                clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, (int) CipherSuite.TLS_FALLBACK_SCSV);
            }
            TlsUtils.writeUint16ArrayWithUint16Length(clientHandshakeState.offeredCipherSuites, byteArrayOutputStream);
            clientHandshakeState.offeredCompressionMethods = new short[]{(short) 0};
            TlsUtils.writeUint8ArrayWithUint8Length(clientHandshakeState.offeredCompressionMethods, byteArrayOutputStream);
            if (clientHandshakeState.clientExtensions != null) {
                TlsProtocol.writeExtensions(byteArrayOutputStream, clientHandshakeState.clientExtensions);
            }
            return byteArrayOutputStream.toByteArray();
        }
        throw new TlsFatalAlert((short) 80);
    }

    protected byte[] generateClientKeyExchange(ClientHandshakeState clientHandshakeState) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        clientHandshakeState.keyExchange.generateClientKeyExchange(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected void invalidateSession(ClientHandshakeState clientHandshakeState) {
        if (clientHandshakeState.sessionParameters != null) {
            clientHandshakeState.sessionParameters.clear();
            clientHandshakeState.sessionParameters = null;
        }
        if (clientHandshakeState.tlsSession != null) {
            clientHandshakeState.tlsSession.invalidate();
            clientHandshakeState.tlsSession = null;
        }
    }

    protected void processCertificateRequest(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        if (clientHandshakeState.authentication == null) {
            throw new TlsFatalAlert((short) 40);
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        clientHandshakeState.certificateRequest = CertificateRequest.parse(clientHandshakeState.clientContext, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.validateCertificateRequest(clientHandshakeState.certificateRequest);
    }

    protected void processCertificateStatus(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        if (clientHandshakeState.allowCertificateStatus) {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            clientHandshakeState.certificateStatus = CertificateStatus.parse(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return;
        }
        throw new TlsFatalAlert((short) 10);
    }

    protected byte[] processHelloVerifyRequest(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        byte[] readOpaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!readVersion.isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
            throw new TlsFatalAlert((short) 47);
        } else if (ProtocolVersion.DTLSv12.isEqualOrEarlierVersionOf(readVersion) || readOpaque8.length <= 32) {
            return readOpaque8;
        } else {
            throw new TlsFatalAlert((short) 47);
        }
    }

    protected void processNewSessionTicket(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        NewSessionTicket parse = NewSessionTicket.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.client.notifyNewSessionTicket(parse);
    }

    protected Certificate processServerCertificate(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Certificate parse = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.processServerCertificate(parse);
        clientHandshakeState.authentication = clientHandshakeState.client.getAuthentication();
        clientHandshakeState.authentication.notifyServerCertificate(parse);
        return parse;
    }

    protected void processServerHello(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ProtocolVersion readVersion = TlsUtils.readVersion(byteArrayInputStream);
        reportServerVersion(clientHandshakeState, readVersion);
        securityParameters.serverRandom = TlsUtils.readFully(32, byteArrayInputStream);
        clientHandshakeState.selectedSessionID = TlsUtils.readOpaque8(byteArrayInputStream);
        if (clientHandshakeState.selectedSessionID.length > 32) {
            throw new TlsFatalAlert((short) 47);
        }
        clientHandshakeState.client.notifySessionID(clientHandshakeState.selectedSessionID);
        clientHandshakeState.selectedCipherSuite = TlsUtils.readUint16(byteArrayInputStream);
        if (!Arrays.contains(clientHandshakeState.offeredCipherSuites, clientHandshakeState.selectedCipherSuite) || clientHandshakeState.selectedCipherSuite == 0 || CipherSuite.isSCSV(clientHandshakeState.selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(clientHandshakeState.selectedCipherSuite, readVersion)) {
            throw new TlsFatalAlert((short) 47);
        }
        DTLSProtocol.validateSelectedCipherSuite(clientHandshakeState.selectedCipherSuite, (short) 47);
        clientHandshakeState.client.notifySelectedCipherSuite(clientHandshakeState.selectedCipherSuite);
        clientHandshakeState.selectedCompressionMethod = TlsUtils.readUint8(byteArrayInputStream);
        if (Arrays.contains(clientHandshakeState.offeredCompressionMethods, clientHandshakeState.selectedCompressionMethod)) {
            clientHandshakeState.client.notifySelectedCompressionMethod(clientHandshakeState.selectedCompressionMethod);
            Hashtable readExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
            if (TlsExtensionsUtils.hasExtendedMasterSecretExtension(readExtensions) != securityParameters.extendedMasterSecret) {
                throw new TlsFatalAlert((short) 40);
            }
            if (readExtensions != null) {
                Enumeration keys = readExtensions.keys();
                while (keys.hasMoreElements()) {
                    Integer num = (Integer) keys.nextElement();
                    if (!num.equals(TlsProtocol.EXT_RenegotiationInfo)) {
                        if (TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, num) == null) {
                            throw new TlsFatalAlert(AlertDescription.unsupported_extension);
                        } else if (num.equals(TlsExtensionsUtils.EXT_extended_master_secret)) {
                        }
                    }
                }
                byte[] bArr2 = (byte[]) readExtensions.get(TlsProtocol.EXT_RenegotiationInfo);
                if (bArr2 != null) {
                    clientHandshakeState.secure_renegotiation = true;
                    if (!Arrays.constantTimeAreEqual(bArr2, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                        throw new TlsFatalAlert((short) 40);
                    }
                }
                boolean hasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(readExtensions);
                if (!hasEncryptThenMACExtension || TlsUtils.isBlockCipherSuite(clientHandshakeState.selectedCipherSuite)) {
                    securityParameters.encryptThenMAC = hasEncryptThenMACExtension;
                    clientHandshakeState.maxFragmentLength = DTLSProtocol.evaluateMaxFragmentLengthExtension(clientHandshakeState.clientExtensions, readExtensions, (short) 47);
                    securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(readExtensions);
                    clientHandshakeState.allowCertificateStatus = TlsUtils.hasExpectedEmptyExtensionData(readExtensions, TlsExtensionsUtils.EXT_status_request, (short) 47);
                    clientHandshakeState.expectSessionTicket = TlsUtils.hasExpectedEmptyExtensionData(readExtensions, TlsProtocol.EXT_SessionTicket, (short) 47);
                } else {
                    throw new TlsFatalAlert((short) 47);
                }
            }
            clientHandshakeState.client.notifySecureRenegotiation(clientHandshakeState.secure_renegotiation);
            if (clientHandshakeState.clientExtensions != null) {
                clientHandshakeState.client.processServerExtensions(readExtensions);
                return;
            }
            return;
        }
        throw new TlsFatalAlert((short) 47);
    }

    protected void processServerKeyExchange(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        clientHandshakeState.keyExchange.processServerKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected void processServerSupplementalData(ClientHandshakeState clientHandshakeState, byte[] bArr) {
        clientHandshakeState.client.processServerSupplementalData(TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(bArr)));
    }

    protected void reportServerVersion(ClientHandshakeState clientHandshakeState, ProtocolVersion protocolVersion) {
        TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
        ProtocolVersion serverVersion = tlsClientContextImpl.getServerVersion();
        if (serverVersion == null) {
            tlsClientContextImpl.setServerVersion(protocolVersion);
            clientHandshakeState.client.notifyServerVersion(protocolVersion);
        } else if (!serverVersion.equals(protocolVersion)) {
            throw new TlsFatalAlert((short) 47);
        }
    }
}
