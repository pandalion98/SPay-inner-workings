/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsCredentials;

public interface TlsSignerCredentials
extends TlsCredentials {
    public byte[] generateCertificateSignature(byte[] var1);

    public SignatureAndHashAlgorithm getSignatureAndHashAlgorithm();
}

