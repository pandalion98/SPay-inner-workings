/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsCipherFactory;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;

public class AbstractTlsCipherFactory
implements TlsCipherFactory {
    @Override
    public TlsCipher createCipher(TlsContext tlsContext, int n2, int n3) {
        throw new TlsFatalAlert(80);
    }
}

