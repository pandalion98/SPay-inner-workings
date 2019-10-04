/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.ProtocolException
 *  java.net.UnknownServiceException
 *  java.security.cert.CertificateException
 *  java.util.Arrays
 *  java.util.List
 *  javax.net.ssl.SSLHandshakeException
 *  javax.net.ssl.SSLPeerUnverifiedException
 *  javax.net.ssl.SSLProtocolException
 *  javax.net.ssl.SSLSocket
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.internal.Internal;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;

public final class ConnectionSpecSelector {
    private final List<ConnectionSpec> connectionSpecs;
    private boolean isFallback;
    private boolean isFallbackPossible;
    private int nextModeIndex = 0;

    public ConnectionSpecSelector(List<ConnectionSpec> list) {
        this.connectionSpecs = list;
    }

    private boolean isFallbackPossible(SSLSocket sSLSocket) {
        for (int i2 = this.nextModeIndex; i2 < this.connectionSpecs.size(); ++i2) {
            if (!((ConnectionSpec)this.connectionSpecs.get(i2)).isCompatible(sSLSocket)) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ConnectionSpec configureSecureSocket(SSLSocket sSLSocket) {
        ConnectionSpec connectionSpec;
        block2 : {
            int n2 = this.nextModeIndex;
            int n3 = this.connectionSpecs.size();
            for (int i2 = n2; i2 < n3; ++i2) {
                connectionSpec = (ConnectionSpec)this.connectionSpecs.get(i2);
                if (!connectionSpec.isCompatible(sSLSocket)) continue;
                this.nextModeIndex = i2 + 1;
                break block2;
            }
            connectionSpec = null;
        }
        if (connectionSpec == null) {
            throw new UnknownServiceException("Unable to find acceptable protocols. isFallback=" + this.isFallback + ", modes=" + this.connectionSpecs + ", supported protocols=" + Arrays.toString((Object[])sSLSocket.getEnabledProtocols()));
        }
        this.isFallbackPossible = this.isFallbackPossible(sSLSocket);
        Internal.instance.apply(connectionSpec, sSLSocket, this.isFallback);
        return connectionSpec;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean connectionFailed(IOException iOException) {
        boolean bl;
        this.isFallback = bl = true;
        if (iOException instanceof ProtocolException) {
            return false;
        }
        if (iOException instanceof InterruptedIOException) return false;
        if (iOException instanceof SSLHandshakeException) {
            if (iOException.getCause() instanceof CertificateException) return false;
        }
        if (iOException instanceof SSLPeerUnverifiedException) return false;
        if (!(iOException instanceof SSLHandshakeException)) {
            if (!(iOException instanceof SSLProtocolException)) return false;
        }
        if (!this.isFallbackPossible) return false;
        return bl;
    }
}

