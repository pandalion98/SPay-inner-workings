/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.crypto.tls.TlsSession;
import org.bouncycastle.util.Arrays;

class TlsSessionImpl
implements TlsSession {
    final byte[] sessionID;
    SessionParameters sessionParameters;

    TlsSessionImpl(byte[] arrby, SessionParameters sessionParameters) {
        if (arrby == null) {
            throw new IllegalArgumentException("'sessionID' cannot be null");
        }
        if (arrby.length < 1 || arrby.length > 32) {
            throw new IllegalArgumentException("'sessionID' must have length between 1 and 32 bytes, inclusive");
        }
        this.sessionID = Arrays.clone((byte[])arrby);
        this.sessionParameters = sessionParameters;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public SessionParameters exportSessionParameters() {
        TlsSessionImpl tlsSessionImpl = this;
        synchronized (tlsSessionImpl) {
            block4 : {
                SessionParameters sessionParameters = this.sessionParameters;
                if (sessionParameters != null) break block4;
                return null;
            }
            SessionParameters sessionParameters = this.sessionParameters.copy();
            return sessionParameters;
        }
    }

    @Override
    public byte[] getSessionID() {
        TlsSessionImpl tlsSessionImpl = this;
        synchronized (tlsSessionImpl) {
            byte[] arrby = this.sessionID;
            return arrby;
        }
    }

    @Override
    public void invalidate() {
        TlsSessionImpl tlsSessionImpl = this;
        synchronized (tlsSessionImpl) {
            if (this.sessionParameters != null) {
                this.sessionParameters.clear();
                this.sessionParameters = null;
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean isResumable() {
        TlsSessionImpl tlsSessionImpl = this;
        synchronized (tlsSessionImpl) {
            SessionParameters sessionParameters = this.sessionParameters;
            if (sessionParameters == null) return false;
            return true;
        }
    }
}

