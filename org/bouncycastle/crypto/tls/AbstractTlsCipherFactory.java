package org.bouncycastle.crypto.tls;

public class AbstractTlsCipherFactory implements TlsCipherFactory {
    public TlsCipher createCipher(TlsContext tlsContext, int i, int i2) {
        throw new TlsFatalAlert((short) 80);
    }
}
