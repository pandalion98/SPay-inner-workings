/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Hashtable
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.CertificateStatus;
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsServerContext;

public interface TlsServer
extends TlsPeer {
    public CertificateRequest getCertificateRequest();

    public CertificateStatus getCertificateStatus();

    public TlsCredentials getCredentials();

    public TlsKeyExchange getKeyExchange();

    public NewSessionTicket getNewSessionTicket();

    public int getSelectedCipherSuite();

    public short getSelectedCompressionMethod();

    public Hashtable getServerExtensions();

    public Vector getServerSupplementalData();

    public ProtocolVersion getServerVersion();

    public void init(TlsServerContext var1);

    public void notifyClientCertificate(Certificate var1);

    public void notifyClientVersion(ProtocolVersion var1);

    public void notifyFallback(boolean var1);

    public void notifyOfferedCipherSuites(int[] var1);

    public void notifyOfferedCompressionMethods(short[] var1);

    public void processClientExtensions(Hashtable var1);

    public void processClientSupplementalData(Vector var1);
}

