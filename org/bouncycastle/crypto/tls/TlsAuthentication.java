package org.bouncycastle.crypto.tls;

public interface TlsAuthentication {
    TlsCredentials getClientCredentials(CertificateRequest certificateRequest);

    void notifyServerCertificate(Certificate certificate);
}
