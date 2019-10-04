/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CertificateEncodingException
 */
package org.bouncycastle.jce.exception;

import java.security.cert.CertificateEncodingException;
import org.bouncycastle.jce.exception.ExtException;

public class ExtCertificateEncodingException
extends CertificateEncodingException
implements ExtException {
    private Throwable cause;

    public ExtCertificateEncodingException(String string, Throwable throwable) {
        super(string);
        this.cause = throwable;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}

