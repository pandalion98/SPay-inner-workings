/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CertificateEncodingException
 */
package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;

class ExtCertificateEncodingException
extends CertificateEncodingException {
    Throwable cause;

    ExtCertificateEncodingException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    public Throwable getCause() {
        return this.cause;
    }
}

