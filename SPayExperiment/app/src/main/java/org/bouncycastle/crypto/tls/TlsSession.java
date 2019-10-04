/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.SessionParameters;

public interface TlsSession {
    public SessionParameters exportSessionParameters();

    public byte[] getSessionID();

    public void invalidate();

    public boolean isResumable();
}

