/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCredentials;

public abstract class ServerOnlyTlsAuthentication
implements TlsAuthentication {
    @Override
    public final TlsCredentials getClientCredentials(CertificateRequest certificateRequest) {
        return null;
    }
}

