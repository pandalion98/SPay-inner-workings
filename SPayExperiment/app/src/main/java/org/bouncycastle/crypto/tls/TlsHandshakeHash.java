/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.tls.TlsContext;

public interface TlsHandshakeHash
extends Digest {
    public Digest forkPRFHash();

    public byte[] getFinalHash(short var1);

    public void init(TlsContext var1);

    public TlsHandshakeHash notifyPRFDetermined();

    public void sealHashAlgorithms();

    public TlsHandshakeHash stopTracking();

    public void trackHashAlgorithm(short var1);
}

