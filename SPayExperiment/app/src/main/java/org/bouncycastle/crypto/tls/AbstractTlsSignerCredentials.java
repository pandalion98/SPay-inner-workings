/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.AbstractTlsCredentials;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;

public abstract class AbstractTlsSignerCredentials
extends AbstractTlsCredentials
implements TlsSignerCredentials {
    @Override
    public SignatureAndHashAlgorithm getSignatureAndHashAlgorithm() {
        throw new IllegalStateException("TlsSignerCredentials implementation does not support (D)TLS 1.2+");
    }
}

