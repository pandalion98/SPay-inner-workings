package org.bouncycastle.crypto.tls;

public interface TlsCipherFactory {
    TlsCipher createCipher(TlsContext tlsContext, int i, int i2);
}
