/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.tls.AbstractTlsContext;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsClientContext;

class TlsClientContextImpl
extends AbstractTlsContext
implements TlsClientContext {
    TlsClientContextImpl(SecureRandom secureRandom, SecurityParameters securityParameters) {
        super(secureRandom, securityParameters);
    }

    @Override
    public boolean isServer() {
        return false;
    }
}

