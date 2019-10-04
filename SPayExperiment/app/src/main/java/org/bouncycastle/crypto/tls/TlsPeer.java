/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCompression;

public interface TlsPeer {
    public TlsCipher getCipher();

    public TlsCompression getCompression();

    public void notifyAlertRaised(short var1, short var2, String var3, Throwable var4);

    public void notifyAlertReceived(short var1, short var2);

    public void notifyHandshakeComplete();

    public void notifySecureRenegotiation(boolean var1);

    public boolean shouldUseGMTUnixTime();
}

