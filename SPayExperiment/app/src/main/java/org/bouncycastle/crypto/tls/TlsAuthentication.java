/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsCredentials;

public interface TlsAuthentication {
    public TlsCredentials getClientCredentials(CertificateRequest var1);

    public void notifyServerCertificate(Certificate var1);
}

