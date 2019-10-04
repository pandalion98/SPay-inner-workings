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
import org.bouncycastle.crypto.tls.NewSessionTicket;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsClientContext;
import org.bouncycastle.crypto.tls.TlsKeyExchange;
import org.bouncycastle.crypto.tls.TlsPeer;
import org.bouncycastle.crypto.tls.TlsSession;

public interface TlsClient
extends TlsPeer {
    public TlsAuthentication getAuthentication();

    public int[] getCipherSuites();

    public Hashtable getClientExtensions();

    public ProtocolVersion getClientHelloRecordLayerVersion();

    public Vector getClientSupplementalData();

    public ProtocolVersion getClientVersion();

    public short[] getCompressionMethods();

    public TlsKeyExchange getKeyExchange();

    public TlsSession getSessionToResume();

    public void init(TlsClientContext var1);

    public boolean isFallback();

    public void notifyNewSessionTicket(NewSessionTicket var1);

    public void notifySelectedCipherSuite(int var1);

    public void notifySelectedCompressionMethod(short var1);

    public void notifyServerVersion(ProtocolVersion var1);

    public void notifySessionID(byte[] var1);

    public void processServerExtensions(Hashtable var1);

    public void processServerSupplementalData(Vector var1);
}

