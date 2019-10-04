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
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;

public class CertificateStatus {
    protected Object response;
    protected short statusType;

    public CertificateStatus(short s2, Object object) {
        if (!CertificateStatus.isCorrectType(s2, object)) {
            throw new IllegalArgumentException("'response' is not an instance of the correct type");
        }
        this.statusType = s2;
        this.response = object;
    }

    protected static boolean isCorrectType(short s2, Object object) {
        switch (s2) {
            default: {
                throw new IllegalArgumentException("'statusType' is an unsupported value");
            }
            case 1: 
        }
        return object instanceof OCSPResponse;
    }

    public static CertificateStatus parse(InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        switch (s2) {
            default: {
                throw new TlsFatalAlert(50);
            }
            case 1: 
        }
        return new CertificateStatus(s2, OCSPResponse.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque24(inputStream))));
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.statusType, outputStream);
        switch (this.statusType) {
            default: {
                throw new TlsFatalAlert(80);
            }
            case 1: 
        }
        TlsUtils.writeOpaque24(((OCSPResponse)this.response).getEncoded("DER"), outputStream);
    }

    public OCSPResponse getOCSPResponse() {
        if (!CertificateStatus.isCorrectType((short)1, this.response)) {
            throw new IllegalStateException("'response' is not an OCSPResponse");
        }
        return (OCSPResponse)this.response;
    }

    public Object getResponse() {
        return this.response;
    }

    public short getStatusType() {
        return this.statusType;
    }
}

