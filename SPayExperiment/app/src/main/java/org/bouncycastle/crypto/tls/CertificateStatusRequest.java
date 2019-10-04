/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.OCSPStatusRequest;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;

public class CertificateStatusRequest {
    protected Object request;
    protected short statusType;

    public CertificateStatusRequest(short s2, Object object) {
        if (!CertificateStatusRequest.isCorrectType(s2, object)) {
            throw new IllegalArgumentException("'request' is not an instance of the correct type");
        }
        this.statusType = s2;
        this.request = object;
    }

    protected static boolean isCorrectType(short s2, Object object) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("'statusType' is an unsupported value");
            }
            case 1: 
        }
        return object instanceof OCSPStatusRequest;
    }

    public static CertificateStatusRequest parse(InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        switch (s2) {
            default: {
                throw new TlsFatalAlert(50);
            }
            case 1: 
        }
        return new CertificateStatusRequest(s2, OCSPStatusRequest.parse(inputStream));
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.statusType, outputStream);
        switch (this.statusType) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 1: 
        }
        ((OCSPStatusRequest)this.request).encode(outputStream);
    }

    public OCSPStatusRequest getOCSPStatusRequest() {
        if (!CertificateStatusRequest.isCorrectType((short)1, this.request)) {
            throw new IllegalStateException("'request' is not an OCSPStatusRequest");
        }
        return (OCSPStatusRequest)this.request;
    }

    public Object getRequest() {
        return this.request;
    }

    public short getStatusType() {
        return this.statusType;
    }
}

