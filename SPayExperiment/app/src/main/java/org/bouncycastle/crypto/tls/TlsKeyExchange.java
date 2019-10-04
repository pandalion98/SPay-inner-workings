/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsCredentials;

public interface TlsKeyExchange {
    public void generateClientKeyExchange(OutputStream var1);

    public byte[] generatePremasterSecret();

    public byte[] generateServerKeyExchange();

    public void init(TlsContext var1);

    public void processClientCertificate(Certificate var1);

    public void processClientCredentials(TlsCredentials var1);

    public void processClientKeyExchange(InputStream var1);

    public void processServerCertificate(Certificate var1);

    public void processServerCredentials(TlsCredentials var1);

    public void processServerKeyExchange(InputStream var1);

    public boolean requiresServerKeyExchange();

    public void skipClientCredentials();

    public void skipServerCredentials();

    public void skipServerKeyExchange();

    public void validateCertificateRequest(CertificateRequest var1);
}

