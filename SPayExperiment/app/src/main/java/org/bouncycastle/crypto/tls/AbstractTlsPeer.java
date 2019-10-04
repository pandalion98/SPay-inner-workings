/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsPeer;

public abstract class AbstractTlsPeer
implements TlsPeer {
    @Override
    public void notifyAlertRaised(short s2, short s3, String string, Throwable throwable) {
    }

    @Override
    public void notifyAlertReceived(short s2, short s3) {
    }

    @Override
    public void notifyHandshakeComplete() {
    }

    @Override
    public void notifySecureRenegotiation(boolean bl) {
        if (!bl) {
            throw new TlsFatalAlert(40);
        }
    }

    @Override
    public boolean shouldUseGMTUnixTime() {
        return false;
    }
}

