/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.SecureRandom
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
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
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
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsCipher;
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

public class DTLSServerProtocol
extends DTLSProtocol {
    protected boolean verifyRequests = true;

    public DTLSServerProtocol(SecureRandom secureRandom) {
        super(secureRandom);
    }

    public DTLSTransport accept(TlsServer tlsServer, DatagramTransport datagramTransport) {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'server' cannot be null");
        }
        if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        }
        SecurityParameters securityParameters = new SecurityParameters();
        securityParameters.entity = 0;
        ServerHandshakeState serverHandshakeState = new ServerHandshakeState();
        serverHandshakeState.server = tlsServer;
        serverHandshakeState.serverContext = new TlsServerContextImpl(this.secureRandom, securityParameters);
        securityParameters.serverRandom = TlsProtocol.createRandomBlock(tlsServer.shouldUseGMTUnixTime(), serverHandshakeState.serverContext.getNonceRandomGenerator());
        tlsServer.init(serverHandshakeState.serverContext);
        DTLSRecordLayer dTLSRecordLayer = new DTLSRecordLayer(datagramTransport, serverHandshakeState.serverContext, tlsServer, 22);
        try {
            DTLSTransport dTLSTransport = this.serverHandshake(serverHandshakeState, dTLSRecordLayer);
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

    protected boolean expectCertificateVerifyMessage(ServerHandshakeState serverHandshakeState) {
        return serverHandshakeState.clientCertificateType >= 0 && TlsUtils.hasSigningCapability(serverHandshakeState.clientCertificateType);
    }

    protected byte[] generateCertificateRequest(ServerHandshakeState serverHandshakeState, CertificateRequest certificateRequest) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateRequest.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateCertificateStatus(ServerHandshakeState serverHandshakeState, CertificateStatus certificateStatus) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificateStatus.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateNewSessionTicket(ServerHandshakeState serverHandshakeState, NewSessionTicket newSessionTicket) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        newSessionTicket.encode((OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected byte[] generateServerHello(ServerHandshakeState serverHandshakeState) {
        boolean bl;
        SecurityParameters securityParameters = serverHandshakeState.serverContext.getSecurityParameters();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ProtocolVersion protocolVersion = serverHandshakeState.server.getServerVersion();
        if (!protocolVersion.isEqualOrEarlierVersionOf(serverHandshakeState.serverContext.getClientVersion())) {
            throw new TlsFatalAlert(80);
        }
        serverHandshakeState.serverContext.setServerVersion(protocolVersion);
        TlsUtils.writeVersion(serverHandshakeState.serverContext.getServerVersion(), (OutputStream)byteArrayOutputStream);
        byteArrayOutputStream.write(securityParameters.getServerRandom());
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, (OutputStream)byteArrayOutputStream);
        serverHandshakeState.selectedCipherSuite = serverHandshakeState.server.getSelectedCipherSuite();
        if (!Arrays.contains((int[])serverHandshakeState.offeredCipherSuites, (int)serverHandshakeState.selectedCipherSuite) || serverHandshakeState.selectedCipherSuite == 0 || CipherSuite.isSCSV(serverHandshakeState.selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(serverHandshakeState.selectedCipherSuite, protocolVersion)) {
            throw new TlsFatalAlert(80);
        }
        DTLSServerProtocol.validateSelectedCipherSuite(serverHandshakeState.selectedCipherSuite, (short)80);
        serverHandshakeState.selectedCompressionMethod = serverHandshakeState.server.getSelectedCompressionMethod();
        if (!Arrays.contains((short[])serverHandshakeState.offeredCompressionMethods, (short)serverHandshakeState.selectedCompressionMethod)) {
            throw new TlsFatalAlert(80);
        }
        TlsUtils.writeUint16(serverHandshakeState.selectedCipherSuite, (OutputStream)byteArrayOutputStream);
        TlsUtils.writeUint8(serverHandshakeState.selectedCompressionMethod, (OutputStream)byteArrayOutputStream);
        serverHandshakeState.serverExtensions = serverHandshakeState.server.getServerExtensions();
        if (serverHandshakeState.secure_renegotiation && (bl = TlsUtils.getExtensionData(serverHandshakeState.serverExtensions, TlsProtocol.EXT_RenegotiationInfo) == null)) {
            serverHandshakeState.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(serverHandshakeState.serverExtensions);
            serverHandshakeState.serverExtensions.put((Object)TlsProtocol.EXT_RenegotiationInfo, (Object)TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
        }
        if (securityParameters.extendedMasterSecret) {
            serverHandshakeState.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(serverHandshakeState.serverExtensions);
            TlsExtensionsUtils.addExtendedMasterSecretExtension(serverHandshakeState.serverExtensions);
        }
        if (serverHandshakeState.serverExtensions != null) {
            securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(serverHandshakeState.serverExtensions);
            serverHandshakeState.maxFragmentLength = DTLSServerProtocol.evaluateMaxFragmentLengthExtension(serverHandshakeState.clientExtensions, serverHandshakeState.serverExtensions, (short)80);
            securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(serverHandshakeState.serverExtensions);
            serverHandshakeState.allowCertificateStatus = TlsUtils.hasExpectedEmptyExtensionData(serverHandshakeState.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short)80);
            serverHandshakeState.expectSessionTicket = TlsUtils.hasExpectedEmptyExtensionData(serverHandshakeState.serverExtensions, TlsProtocol.EXT_SessionTicket, (short)80);
            TlsProtocol.writeExtensions((OutputStream)byteArrayOutputStream, serverHandshakeState.serverExtensions);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public boolean getVerifyRequests() {
        return this.verifyRequests;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void notifyClientCertificate(ServerHandshakeState serverHandshakeState, Certificate certificate) {
        if (serverHandshakeState.certificateRequest == null) {
            throw new IllegalStateException();
        }
        if (serverHandshakeState.clientCertificate != null) {
            throw new TlsFatalAlert(10);
        }
        serverHandshakeState.clientCertificate = certificate;
        if (certificate.isEmpty()) {
            serverHandshakeState.keyExchange.skipClientCredentials();
        } else {
            serverHandshakeState.clientCertificateType = TlsUtils.getClientCertificateType(certificate, serverHandshakeState.serverCredentials.getCertificate());
            serverHandshakeState.keyExchange.processClientCertificate(certificate);
        }
        serverHandshakeState.server.notifyClientCertificate(certificate);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void processCertificateVerify(ServerHandshakeState serverHandshakeState, byte[] arrby, TlsHandshakeHash tlsHandshakeHash) {
        boolean bl;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        TlsServerContextImpl tlsServerContextImpl = serverHandshakeState.serverContext;
        DigitallySigned digitallySigned = DigitallySigned.parse(tlsServerContextImpl, (InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        try {
            byte[] arrby2;
            boolean bl2;
            if (TlsUtils.isTLSv12(tlsServerContextImpl)) {
                arrby2 = tlsHandshakeHash.getFinalHash(digitallySigned.getAlgorithm().getHash());
            } else {
                byte[] arrby3 = tlsServerContextImpl.getSecurityParameters().getSessionHash();
                arrby2 = arrby3;
            }
            AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(serverHandshakeState.clientCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner tlsSigner = TlsUtils.createTlsSigner(serverHandshakeState.clientCertificateType);
            tlsSigner.init(tlsServerContextImpl);
            bl = bl2 = tlsSigner.verifyRawSignature(digitallySigned.getAlgorithm(), digitallySigned.getSignature(), asymmetricKeyParameter, arrby2);
        }
        catch (Exception exception) {
            bl = false;
        }
        if (!bl) {
            throw new TlsFatalAlert(51);
        }
    }

    protected void processClientCertificate(ServerHandshakeState serverHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        Certificate certificate = Certificate.parse((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        this.notifyClientCertificate(serverHandshakeState, certificate);
    }

    protected void processClientHello(ServerHandshakeState serverHandshakeState, byte[] arrby) {
        byte[] arrby2;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        ProtocolVersion protocolVersion = TlsUtils.readVersion((InputStream)byteArrayInputStream);
        if (!protocolVersion.isDTLS()) {
            throw new TlsFatalAlert(47);
        }
        byte[] arrby3 = TlsUtils.readFully(32, (InputStream)byteArrayInputStream);
        if (TlsUtils.readOpaque8((InputStream)byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert(47);
        }
        TlsUtils.readOpaque8((InputStream)byteArrayInputStream);
        int n2 = TlsUtils.readUint16((InputStream)byteArrayInputStream);
        if (n2 < 2 || (n2 & 1) != 0) {
            throw new TlsFatalAlert(50);
        }
        serverHandshakeState.offeredCipherSuites = TlsUtils.readUint16Array(n2 / 2, (InputStream)byteArrayInputStream);
        short s2 = TlsUtils.readUint8((InputStream)byteArrayInputStream);
        if (s2 < 1) {
            throw new TlsFatalAlert(47);
        }
        serverHandshakeState.offeredCompressionMethods = TlsUtils.readUint8Array(s2, (InputStream)byteArrayInputStream);
        serverHandshakeState.clientExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
        TlsServerContextImpl tlsServerContextImpl = serverHandshakeState.serverContext;
        SecurityParameters securityParameters = tlsServerContextImpl.getSecurityParameters();
        securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(serverHandshakeState.clientExtensions);
        tlsServerContextImpl.setClientVersion(protocolVersion);
        serverHandshakeState.server.notifyClientVersion(protocolVersion);
        serverHandshakeState.server.notifyFallback(Arrays.contains((int[])serverHandshakeState.offeredCipherSuites, (int)22016));
        securityParameters.clientRandom = arrby3;
        serverHandshakeState.server.notifyOfferedCipherSuites(serverHandshakeState.offeredCipherSuites);
        serverHandshakeState.server.notifyOfferedCompressionMethods(serverHandshakeState.offeredCompressionMethods);
        if (Arrays.contains((int[])serverHandshakeState.offeredCipherSuites, (int)255)) {
            serverHandshakeState.secure_renegotiation = true;
        }
        if ((arrby2 = TlsUtils.getExtensionData(serverHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo)) != null) {
            serverHandshakeState.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual((byte[])arrby2, (byte[])TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert(40);
            }
        }
        serverHandshakeState.server.notifySecureRenegotiation(serverHandshakeState.secure_renegotiation);
        if (serverHandshakeState.clientExtensions != null) {
            serverHandshakeState.server.processClientExtensions(serverHandshakeState.clientExtensions);
        }
    }

    protected void processClientKeyExchange(ServerHandshakeState serverHandshakeState, byte[] arrby) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
        serverHandshakeState.keyExchange.processClientKeyExchange((InputStream)byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected void processClientSupplementalData(ServerHandshakeState serverHandshakeState, byte[] arrby) {
        Vector vector = TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(arrby));
        serverHandshakeState.server.processClientSupplementalData(vector);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected DTLSTransport serverHandshake(ServerHandshakeState serverHandshakeState, DTLSRecordLayer dTLSRecordLayer) {
        CertificateStatus certificateStatus;
        Certificate certificate;
        byte[] arrby;
        SecurityParameters securityParameters = serverHandshakeState.serverContext.getSecurityParameters();
        DTLSReliableHandshake dTLSReliableHandshake = new DTLSReliableHandshake(serverHandshakeState.serverContext, dTLSRecordLayer);
        DTLSReliableHandshake.Message message = dTLSReliableHandshake.receiveMessage();
        ProtocolVersion protocolVersion = dTLSRecordLayer.getDiscoveredPeerVersion();
        serverHandshakeState.serverContext.setClientVersion(protocolVersion);
        if (message.getType() != 1) {
            throw new TlsFatalAlert(10);
        }
        this.processClientHello(serverHandshakeState, message.getBody());
        byte[] arrby2 = this.generateServerHello(serverHandshakeState);
        if (serverHandshakeState.maxFragmentLength >= 0) {
            dTLSRecordLayer.setPlaintextLimit(1 << 8 + serverHandshakeState.maxFragmentLength);
        }
        securityParameters.cipherSuite = serverHandshakeState.selectedCipherSuite;
        securityParameters.compressionAlgorithm = serverHandshakeState.selectedCompressionMethod;
        securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(serverHandshakeState.serverContext, serverHandshakeState.selectedCipherSuite);
        securityParameters.verifyDataLength = 12;
        dTLSReliableHandshake.sendMessage((short)2, arrby2);
        dTLSReliableHandshake.notifyHelloComplete();
        Vector vector = serverHandshakeState.server.getServerSupplementalData();
        if (vector != null) {
            dTLSReliableHandshake.sendMessage((short)23, DTLSServerProtocol.generateSupplementalData(vector));
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
            dTLSReliableHandshake.sendMessage((short)11, DTLSServerProtocol.generateCertificate(certificate));
        }
        if (certificate == null || certificate.isEmpty()) {
            serverHandshakeState.allowCertificateStatus = false;
        }
        if (serverHandshakeState.allowCertificateStatus && (certificateStatus = serverHandshakeState.server.getCertificateStatus()) != null) {
            dTLSReliableHandshake.sendMessage((short)22, this.generateCertificateStatus(serverHandshakeState, certificateStatus));
        }
        if ((arrby = serverHandshakeState.keyExchange.generateServerKeyExchange()) != null) {
            dTLSReliableHandshake.sendMessage((short)12, arrby);
        }
        if (serverHandshakeState.serverCredentials != null) {
            serverHandshakeState.certificateRequest = serverHandshakeState.server.getCertificateRequest();
            if (serverHandshakeState.certificateRequest != null) {
                serverHandshakeState.keyExchange.validateCertificateRequest(serverHandshakeState.certificateRequest);
                dTLSReliableHandshake.sendMessage((short)13, this.generateCertificateRequest(serverHandshakeState, serverHandshakeState.certificateRequest));
                TlsUtils.trackHashAlgorithms(dTLSReliableHandshake.getHandshakeHash(), serverHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
            }
        }
        dTLSReliableHandshake.sendMessage((short)14, TlsUtils.EMPTY_BYTES);
        dTLSReliableHandshake.getHandshakeHash().sealHashAlgorithms();
        DTLSReliableHandshake.Message message2 = dTLSReliableHandshake.receiveMessage();
        if (message2.getType() == 23) {
            this.processClientSupplementalData(serverHandshakeState, message2.getBody());
            message2 = dTLSReliableHandshake.receiveMessage();
        } else {
            serverHandshakeState.server.processClientSupplementalData(null);
        }
        if (serverHandshakeState.certificateRequest == null) {
            serverHandshakeState.keyExchange.skipClientCredentials();
        } else if (message2.getType() == 11) {
            this.processClientCertificate(serverHandshakeState, message2.getBody());
            message2 = dTLSReliableHandshake.receiveMessage();
        } else {
            if (TlsUtils.isTLSv12(serverHandshakeState.serverContext)) {
                throw new TlsFatalAlert(10);
            }
            this.notifyClientCertificate(serverHandshakeState, Certificate.EMPTY_CHAIN);
        }
        if (message2.getType() != 16) {
            throw new TlsFatalAlert(10);
        }
        this.processClientKeyExchange(serverHandshakeState, message2.getBody());
        TlsHandshakeHash tlsHandshakeHash = dTLSReliableHandshake.prepareToFinish();
        securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, tlsHandshakeHash, null);
        TlsProtocol.establishMasterSecret(serverHandshakeState.serverContext, serverHandshakeState.keyExchange);
        dTLSRecordLayer.initPendingEpoch(serverHandshakeState.server.getCipher());
        if (this.expectCertificateVerifyMessage(serverHandshakeState)) {
            this.processCertificateVerify(serverHandshakeState, dTLSReliableHandshake.receiveMessageBody((short)15), tlsHandshakeHash);
        }
        byte[] arrby3 = TlsUtils.calculateVerifyData(serverHandshakeState.serverContext, "client finished", TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, dTLSReliableHandshake.getHandshakeHash(), null));
        this.processFinished(dTLSReliableHandshake.receiveMessageBody((short)20), arrby3);
        if (serverHandshakeState.expectSessionTicket) {
            dTLSReliableHandshake.sendMessage((short)4, this.generateNewSessionTicket(serverHandshakeState, serverHandshakeState.server.getNewSessionTicket()));
        }
        dTLSReliableHandshake.sendMessage((short)20, TlsUtils.calculateVerifyData(serverHandshakeState.serverContext, "server finished", TlsProtocol.getCurrentPRFHash(serverHandshakeState.serverContext, dTLSReliableHandshake.getHandshakeHash(), null)));
        dTLSReliableHandshake.finish();
        serverHandshakeState.server.notifyHandshakeComplete();
        return new DTLSTransport(dTLSRecordLayer);
    }

    public void setVerifyRequests(boolean bl) {
        this.verifyRequests = bl;
    }

    protected static class ServerHandshakeState {
        boolean allowCertificateStatus = false;
        CertificateRequest certificateRequest = null;
        Certificate clientCertificate = null;
        short clientCertificateType = (short)-1;
        Hashtable clientExtensions;
        boolean expectSessionTicket = false;
        TlsKeyExchange keyExchange = null;
        short maxFragmentLength = (short)-1;
        int[] offeredCipherSuites;
        short[] offeredCompressionMethods;
        boolean secure_renegotiation = false;
        int selectedCipherSuite = -1;
        short selectedCompressionMethod = (short)-1;
        TlsServer server = null;
        TlsServerContextImpl serverContext = null;
        TlsCredentials serverCredentials = null;
        Hashtable serverExtensions = null;

        protected ServerHandshakeState() {
        }
    }

}

