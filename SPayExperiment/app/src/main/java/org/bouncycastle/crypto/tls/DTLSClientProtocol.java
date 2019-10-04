/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.security.SecureRandom
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
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
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.CertificateStatus;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.DTLSProtocol;
import org.bouncycastle.crypto.tls.DTLSRecordLayer;
import org.bouncycastle.crypto.tls.DTLSReliableHandshake;
import org.bouncycastle.crypto.tls.DTLSTransport;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.DigitallySigned;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsClient;
import org.bouncycastle.crypto.tls.TlsClientContext;
import org.bouncycastle.crypto.tls.TlsClientContextImpl;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsExtensionsUtils;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsHandshakeHash;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.crypto.tls.TlsSessionImpl;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class DTLSClientProtocol
extends DTLSProtocol {
    public DTLSClientProtocol(SecureRandom secureRandom) {
        super(secureRandom);
    }

    protected static byte[] patchClientHelloWithCookie(byte[] arrby, byte[] arrby2) {
        int n2 = 35 + TlsUtils.readUint8(arrby, 34);
        int n3 = n2 + 1;
        byte[] arrby3 = new byte[arrby.length + arrby2.length];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)n2);
        TlsUtils.checkUint8(arrby2.length);
        TlsUtils.writeUint8(arrby2.length, arrby3, n2);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)n3, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby3, (int)(n3 + arrby2.length), (int)(arrby.length - n3));
        return arrby3;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected DTLSTransport clientHandshake(ClientHandshakeState clientHandshakeState, DTLSRecordLayer dTLSRecordLayer) {
        DTLSReliableHandshake.Message message;
        Certificate certificate;
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        DTLSReliableHandshake dTLSReliableHandshake = new DTLSReliableHandshake(clientHandshakeState.clientContext, dTLSRecordLayer);
        byte[] arrby = this.generateClientHello(clientHandshakeState, clientHandshakeState.client);
        dTLSReliableHandshake.sendMessage((short)1, arrby);
        DTLSReliableHandshake.Message message2 = dTLSReliableHandshake.receiveMessage();
        while (message2.getType() == 3) {
            if (!dTLSRecordLayer.resetDiscoveredPeerVersion().isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
                throw new TlsFatalAlert(47);
            }
            byte[] arrby2 = DTLSClientProtocol.patchClientHelloWithCookie(arrby, this.processHelloVerifyRequest(clientHandshakeState, message2.getBody()));
            dTLSReliableHandshake.resetHandshakeMessagesDigest();
            dTLSReliableHandshake.sendMessage((short)1, arrby2);
            message2 = dTLSReliableHandshake.receiveMessage();
        }
        if (message2.getType() != 2) {
            throw new TlsFatalAlert(10);
        }
        this.reportServerVersion(clientHandshakeState, dTLSRecordLayer.getDiscoveredPeerVersion());
        this.processServerHello(clientHandshakeState, message2.getBody());
        if (clientHandshakeState.maxFragmentLength >= 0) {
            dTLSRecordLayer.setPlaintextLimit(1 << 8 + clientHandshakeState.maxFragmentLength);
        }
        securityParameters.cipherSuite = clientHandshakeState.selectedCipherSuite;
        securityParameters.compressionAlgorithm = clientHandshakeState.selectedCompressionMethod;
        securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(clientHandshakeState.clientContext, clientHandshakeState.selectedCipherSuite);
        securityParameters.verifyDataLength = 12;
        dTLSReliableHandshake.notifyHelloComplete();
        boolean bl = clientHandshakeState.selectedSessionID.length > 0 && clientHandshakeState.tlsSession != null && Arrays.areEqual((byte[])clientHandshakeState.selectedSessionID, (byte[])clientHandshakeState.tlsSession.getSessionID());
        if (bl) {
            if (securityParameters.getCipherSuite() == clientHandshakeState.sessionParameters.getCipherSuite() && securityParameters.getCompressionAlgorithm() == clientHandshakeState.sessionParameters.getCompressionAlgorithm()) {
                securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(clientHandshakeState.sessionParameters.readServerExtensions());
                securityParameters.masterSecret = Arrays.clone((byte[])clientHandshakeState.sessionParameters.getMasterSecret());
                dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                byte[] arrby3 = TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "server finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null));
                this.processFinished(dTLSReliableHandshake.receiveMessageBody((short)20), arrby3);
                dTLSReliableHandshake.sendMessage((short)20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "client finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                dTLSReliableHandshake.finish();
                clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                clientHandshakeState.client.notifyHandshakeComplete();
                return new DTLSTransport(dTLSRecordLayer);
            }
            throw new TlsFatalAlert(47);
        }
        this.invalidateSession(clientHandshakeState);
        if (clientHandshakeState.selectedSessionID.length > 0) {
            clientHandshakeState.tlsSession = new TlsSessionImpl(clientHandshakeState.selectedSessionID, null);
        }
        if ((message = dTLSReliableHandshake.receiveMessage()).getType() == 23) {
            this.processServerSupplementalData(clientHandshakeState, message.getBody());
            message = dTLSReliableHandshake.receiveMessage();
        } else {
            clientHandshakeState.client.processServerSupplementalData(null);
        }
        clientHandshakeState.keyExchange = clientHandshakeState.client.getKeyExchange();
        clientHandshakeState.keyExchange.init(clientHandshakeState.clientContext);
        if (message.getType() == 11) {
            Certificate certificate2 = this.processServerCertificate(clientHandshakeState, message.getBody());
            DTLSReliableHandshake.Message message3 = dTLSReliableHandshake.receiveMessage();
            certificate = certificate2;
            message = message3;
        } else {
            clientHandshakeState.keyExchange.skipServerCredentials();
            certificate = null;
        }
        if (certificate == null || certificate.isEmpty()) {
            clientHandshakeState.allowCertificateStatus = false;
        }
        if (message.getType() == 22) {
            this.processCertificateStatus(clientHandshakeState, message.getBody());
            message = dTLSReliableHandshake.receiveMessage();
        }
        if (message.getType() == 12) {
            this.processServerKeyExchange(clientHandshakeState, message.getBody());
            message = dTLSReliableHandshake.receiveMessage();
        } else {
            clientHandshakeState.keyExchange.skipServerKeyExchange();
        }
        if (message.getType() == 13) {
            this.processCertificateRequest(clientHandshakeState, message.getBody());
            TlsUtils.trackHashAlgorithms(dTLSReliableHandshake.getHandshakeHash(), clientHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
            message = dTLSReliableHandshake.receiveMessage();
        }
        if (message.getType() != 14) {
            throw new TlsFatalAlert(10);
        }
        if (message.getBody().length != 0) {
            throw new TlsFatalAlert(50);
        }
        dTLSReliableHandshake.getHandshakeHash().sealHashAlgorithms();
        Vector vector = clientHandshakeState.client.getClientSupplementalData();
        if (vector != null) {
            dTLSReliableHandshake.sendMessage((short)23, DTLSClientProtocol.generateSupplementalData(vector));
        }
        if (clientHandshakeState.certificateRequest != null) {
            clientHandshakeState.clientCredentials = clientHandshakeState.authentication.getClientCredentials(clientHandshakeState.certificateRequest);
            Certificate certificate3 = clientHandshakeState.clientCredentials != null ? clientHandshakeState.clientCredentials.getCertificate() : null;
            if (certificate3 == null) {
                certificate3 = Certificate.EMPTY_CHAIN;
            }
            dTLSReliableHandshake.sendMessage((short)11, DTLSClientProtocol.generateCertificate(certificate3));
        }
        if (clientHandshakeState.clientCredentials != null) {
            clientHandshakeState.keyExchange.processClientCredentials(clientHandshakeState.clientCredentials);
        } else {
            clientHandshakeState.keyExchange.skipClientCredentials();
        }
        dTLSReliableHandshake.sendMessage((short)16, this.generateClientKeyExchange(clientHandshakeState));
        TlsHandshakeHash tlsHandshakeHash = dTLSReliableHandshake.prepareToFinish();
        securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, tlsHandshakeHash, null);
        TlsProtocol.establishMasterSecret(clientHandshakeState.clientContext, clientHandshakeState.keyExchange);
        dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
        if (clientHandshakeState.clientCredentials != null && clientHandshakeState.clientCredentials instanceof TlsSignerCredentials) {
            TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials)clientHandshakeState.clientCredentials;
            SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(clientHandshakeState.clientContext, tlsSignerCredentials);
            byte[] arrby4 = signatureAndHashAlgorithm == null ? securityParameters.getSessionHash() : tlsHandshakeHash.getFinalHash(signatureAndHashAlgorithm.getHash());
            dTLSReliableHandshake.sendMessage((short)15, this.generateCertificateVerify(clientHandshakeState, new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(arrby4))));
        }
        dTLSReliableHandshake.sendMessage((short)20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "client finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
        if (clientHandshakeState.expectSessionTicket) {
            DTLSReliableHandshake.Message message4 = dTLSReliableHandshake.receiveMessage();
            if (message4.getType() != 4) {
                throw new TlsFatalAlert(10);
            }
            this.processNewSessionTicket(clientHandshakeState, message4.getBody());
        }
        byte[] arrby5 = TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, "server finished", TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null));
        this.processFinished(dTLSReliableHandshake.receiveMessageBody((short)20), arrby5);
        dTLSReliableHandshake.finish();
        if (clientHandshakeState.tlsSession != null) {
            clientHandshakeState.sessionParameters = new SessionParameters.Builder().setCipherSuite(securityParameters.cipherSuite).setCompressionAlgorithm(securityParameters.compressionAlgorithm).setMasterSecret(securityParameters.masterSecret).setPeerCertificate(certificate).setPSKIdentity(securityParameters.pskIdentity).setSRPIdentity(securityParameters.srpIdentity).build();
            clientHandshakeState.tlsSession = TlsUtils.importSession(clientHandshakeState.tlsSession.getSessionID(), clientHandshakeState.sessionParameters);
            clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
        }
        clientHandshakeState.client.notifyHandshakeComplete();
        return new DTLSTransport(dTLSRecordLayer);
    }

    public DTLSTransport connect(TlsClient tlsClient, DatagramTransport datagramTransport) {
        SessionParameters sessionParameters;
        if (tlsClient == null) {
            throw new IllegalArgumentException("'client' cannot be null");
        }
        if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        }
        SecurityParameters securityParameters = new SecurityParameters();
        securityParameters.entity = 1;
        ClientHandshakeState clientHandshakeState = new ClientHandshakeState();
        clientHandshakeState.client = tlsClient;
        clientHandshakeState.clientContext = new TlsClientContextImpl(this.secureRandom, securityParameters);
        securityParameters.clientRandom = TlsProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), clientHandshakeState.clientContext.getNonceRandomGenerator());
        tlsClient.init(clientHandshakeState.clientContext);
        DTLSRecordLayer dTLSRecordLayer = new DTLSRecordLayer(datagramTransport, clientHandshakeState.clientContext, tlsClient, 22);
        TlsSession tlsSession = clientHandshakeState.client.getSessionToResume();
        if (tlsSession != null && (sessionParameters = tlsSession.exportSessionParameters()) != null) {
            clientHandshakeState.tlsSession = tlsSession;
            clientHandshakeState.sessionParameters = sessionParameters;
        }
        try {
            DTLSTransport dTLSTransport = this.clientHandshake(clientHandshakeState, dTLSRecordLayer);
            return dTLSTransport;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            dTLSRecordLayer.fail(tlsFatalAlert.getAlertDescription());
            throw tlsFatalAlert;
        }
        catch (IOException iOException) {
            dTLSRecordLayer.fail((short)80);
            throw iOException;
        }
        catch (RuntimeException runtimeException) {
            dTLSRecordLayer.fail((short)80);
            throw new TlsFatalAlert(80, runtimeException);
        }
    }

    protected byte[] generateCertificateVerify(ClientHandshakeState clientHandshakeState, DigitallySigned digitallySigned) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        digitallySigned.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected byte[] generateClientHello(ClientHandshakeState clientHandshakeState, TlsClient tlsClient) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProtocolVersion protocolVersion = tlsClient.getClientVersion();
        if (!protocolVersion.isDTLS()) {
            throw new TlsFatalAlert(80);
        }
        TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
        tlsClientContextImpl.setClientVersion(protocolVersion);
        TlsUtils.writeVersion(protocolVersion, (OutputStream)byteArrayOutputStream);
        SecurityParameters securityParameters = tlsClientContextImpl.getSecurityParameters();
        byteArrayOutputStream.write(securityParameters.getClientRandom());
        byte[] arrby = TlsUtils.EMPTY_BYTES;
        if (clientHandshakeState.tlsSession != null && ((arrby = clientHandshakeState.tlsSession.getSessionID()) == null || arrby.length > 32)) {
            arrby = TlsUtils.EMPTY_BYTES;
        }
        TlsUtils.writeOpaque8(arrby, (OutputStream)byteArrayOutputStream);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, (OutputStream)byteArrayOutputStream);
        boolean bl = tlsClient.isFallback();
        clientHandshakeState.offeredCipherSuites = tlsClient.getCipherSuites();
        clientHandshakeState.clientExtensions = tlsClient.getClientExtensions();
        securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(clientHandshakeState.clientExtensions);
        boolean bl2 = TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo) == null;
        boolean bl3 = !Arrays.contains((int[])clientHandshakeState.offeredCipherSuites, (int)255);
        if (bl2 && bl3) {
            clientHandshakeState.offeredCipherSuites = Arrays.append((int[])clientHandshakeState.offeredCipherSuites, (int)255);
        }
        if (bl && !Arrays.contains((int[])clientHandshakeState.offeredCipherSuites, (int)22016)) {
            clientHandshakeState.offeredCipherSuites = Arrays.append((int[])clientHandshakeState.offeredCipherSuites, (int)22016);
        }
        TlsUtils.writeUint16ArrayWithUint16Length(clientHandshakeState.offeredCipherSuites, (OutputStream)byteArrayOutputStream);
        clientHandshakeState.offeredCompressionMethods = new short[]{0};
        TlsUtils.writeUint8ArrayWithUint8Length(clientHandshakeState.offeredCompressionMethods, (OutputStream)byteArrayOutputStream);
        if (clientHandshakeState.clientExtensions != null) {
            TlsProtocol.writeExtensions((OutputStream)byteArrayOutputStream, clientHandshakeState.clientExtensions);
        }
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateClientKeyExchange(ClientHandshakeState clientHandshakeState) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        clientHandshakeState.keyExchange.generateClientKeyExchange((OutputStream)byteArrayOutputStream);
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

    protected void processCertificateRequest(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        if (clientHandshakeState.authentication == null) {
            throw new TlsFatalAlert(40);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        clientHandshakeState.certificateRequest = CertificateRequest.parse(clientHandshakeState.clientContext, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.validateCertificateRequest(clientHandshakeState.certificateRequest);
    }

    protected void processCertificateStatus(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        if (!clientHandshakeState.allowCertificateStatus) {
            throw new TlsFatalAlert(10);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        clientHandshakeState.certificateStatus = CertificateStatus.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected byte[] processHelloVerifyRequest(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        ProtocolVersion protocolVersion = TlsUtils.readVersion((InputStream)byteArrayInputStream);
        byte[] arrby2 = TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!protocolVersion.isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
            throw new TlsFatalAlert(47);
        }
        if (!ProtocolVersion.DTLSv12.isEqualOrEarlierVersionOf(protocolVersion) && arrby2.length > 32) {
            throw new TlsFatalAlert(47);
        }
        return arrby2;
    }

    protected void processNewSessionTicket(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        NewSessionTicket newSessionTicket = NewSessionTicket.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.client.notifyNewSessionTicket(newSessionTicket);
    }

    protected Certificate processServerCertificate(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        Certificate certificate = Certificate.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.processServerCertificate(certificate);
        clientHandshakeState.authentication = clientHandshakeState.client.getAuthentication();
        clientHandshakeState.authentication.notifyServerCertificate(certificate);
        return certificate;
    }

    protected void processServerHello(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        ProtocolVersion protocolVersion = TlsUtils.readVersion((InputStream)byteArrayInputStream);
        this.reportServerVersion(clientHandshakeState, protocolVersion);
        securityParameters.serverRandom = TlsUtils.readFully(32, (InputStream)byteArrayInputStream);
        clientHandshakeState.selectedSessionID = TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        if (clientHandshakeState.selectedSessionID.length > 32) {
            throw new TlsFatalAlert(47);
        }
        clientHandshakeState.client.notifySessionID(clientHandshakeState.selectedSessionID);
        clientHandshakeState.selectedCipherSuite = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (!Arrays.contains((int[])clientHandshakeState.offeredCipherSuites, (int)clientHandshakeState.selectedCipherSuite) || clientHandshakeState.selectedCipherSuite == 0 || CipherSuite.isSCSV(clientHandshakeState.selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(clientHandshakeState.selectedCipherSuite, protocolVersion)) {
            throw new TlsFatalAlert(47);
        }
        DTLSClientProtocol.validateSelectedCipherSuite(clientHandshakeState.selectedCipherSuite, (short)47);
        clientHandshakeState.client.notifySelectedCipherSuite(clientHandshakeState.selectedCipherSuite);
        clientHandshakeState.selectedCompressionMethod = TlsUtils.readUint8((InputStream)byteArrayInputStream);
        if (!Arrays.contains((short[])clientHandshakeState.offeredCompressionMethods, (short)clientHandshakeState.selectedCompressionMethod)) {
            throw new TlsFatalAlert(47);
        }
        clientHandshakeState.client.notifySelectedCompressionMethod(clientHandshakeState.selectedCompressionMethod);
        Hashtable hashtable = TlsProtocol.readExtensions(byteArrayInputStream);
        if (TlsExtensionsUtils.hasExtendedMasterSecretExtension(hashtable) != securityParameters.extendedMasterSecret) {
            throw new TlsFatalAlert(40);
        }
        if (hashtable != null) {
            boolean bl;
            Enumeration enumeration = hashtable.keys();
            while (enumeration.hasMoreElements()) {
                Integer n2 = (Integer)enumeration.nextElement();
                if (n2.equals((Object)TlsProtocol.EXT_RenegotiationInfo)) continue;
                if (TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, n2) == null) {
                    throw new TlsFatalAlert(110);
                }
                if (!n2.equals((Object)TlsExtensionsUtils.EXT_extended_master_secret)) continue;
            }
            byte[] arrby2 = (byte[])hashtable.get((Object)TlsProtocol.EXT_RenegotiationInfo);
            if (arrby2 != null) {
                clientHandshakeState.secure_renegotiation = true;
                if (!Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                    throw new TlsFatalAlert(40);
                }
            }
            if ((bl = TlsExtensionsUtils.hasEncryptThenMACExtension(hashtable)) && !TlsUtils.isBlockCipherSuite(clientHandshakeState.selectedCipherSuite)) {
                throw new TlsFatalAlert(47);
            }
            securityParameters.encryptThenMAC = bl;
            clientHandshakeState.maxFragmentLength = DTLSClientProtocol.evaluateMaxFragmentLengthExtension(clientHandshakeState.clientExtensions, hashtable, (short)47);
            securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(hashtable);
            clientHandshakeState.allowCertificateStatus = TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsExtensionsUtils.EXT_status_request, (short)47);
            clientHandshakeState.expectSessionTicket = TlsUtils.hasExpectedEmptyExtensionData(hashtable, TlsProtocol.EXT_SessionTicket, (short)47);
        }
        clientHandshakeState.client.notifySecureRenegotiation(clientHandshakeState.secure_renegotiation);
        if (clientHandshakeState.clientExtensions != null) {
            clientHandshakeState.client.processServerExtensions(hashtable);
        }
    }

    protected void processServerKeyExchange(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        clientHandshakeState.keyExchange.processServerKeyExchange((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected void processServerSupplementalData(ClientHandshakeState clientHandshakeState, byte[] arrby) {
        Vector vector = TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(arrby));
        clientHandshakeState.client.processServerSupplementalData(vector);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void reportServerVersion(ClientHandshakeState clientHandshakeState, ProtocolVersion protocolVersion) {
        TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
        ProtocolVersion protocolVersion2 = tlsClientContextImpl.getServerVersion();
        if (protocolVersion2 == null) {
            tlsClientContextImpl.setServerVersion(protocolVersion);
            clientHandshakeState.client.notifyServerVersion(protocolVersion);
            return;
        } else {
            if (protocolVersion2.equals(protocolVersion)) return;
            {
                throw new TlsFatalAlert(47);
            }
        }
    }

    protected static class ClientHandshakeState {
        boolean allowCertificateStatus = false;
        TlsAuthentication authentication = null;
        CertificateRequest certificateRequest = null;
        CertificateStatus certificateStatus = null;
        TlsClient client = null;
        TlsClientContextImpl clientContext = null;
        TlsCredentials clientCredentials = null;
        Hashtable clientExtensions = null;
        boolean expectSessionTicket = false;
        TlsKeyExchange keyExchange = null;
        short maxFragmentLength = (short)-1;
        int[] offeredCipherSuites = null;
        short[] offeredCompressionMethods = null;
        boolean secure_renegotiation = false;
        int selectedCipherSuite = -1;
        short selectedCompressionMethod = (short)-1;
        byte[] selectedSessionID = null;
        SessionParameters sessionParameters = null;
        SessionParameters.Builder sessionParametersBuilder = null;
        TlsSession tlsSession = null;

        protected ClientHandshakeState() {
        }
    }

}

