package org.bouncycastle.crypto.tls;

public abstract class AbstractTlsPeer implements TlsPeer {
    public void notifyAlertRaised(short s, short s2, String str, Throwable th) {
    }

    public void notifyAlertReceived(short s, short s2) {
    }

    public void notifyHandshakeComplete() {
    }

    public void notifySecureRenegotiation(boolean z) {
        if (!z) {
            throw new TlsFatalAlert((short) 40);
        }
    }

    public boolean shouldUseGMTUnixTime() {
        return false;
    }
}
