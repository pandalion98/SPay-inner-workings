package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class CertificateStatusRequest {
    protected Object request;
    protected short statusType;

    public CertificateStatusRequest(short s, Object obj) {
        if (isCorrectType(s, obj)) {
            this.statusType = s;
            this.request = obj;
            return;
        }
        throw new IllegalArgumentException("'request' is not an instance of the correct type");
    }

    protected static boolean isCorrectType(short s, Object obj) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return obj instanceof OCSPStatusRequest;
            default:
                throw new IllegalArgumentException("'statusType' is an unsupported value");
        }
    }

    public static CertificateStatusRequest parse(InputStream inputStream) {
        short readUint8 = TlsUtils.readUint8(inputStream);
        switch (readUint8) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new CertificateStatusRequest(readUint8, OCSPStatusRequest.parse(inputStream));
            default:
                throw new TlsFatalAlert((short) 50);
        }
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.statusType, outputStream);
        switch (this.statusType) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                ((OCSPStatusRequest) this.request).encode(outputStream);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public OCSPStatusRequest getOCSPStatusRequest() {
        if (isCorrectType((short) 1, this.request)) {
            return (OCSPStatusRequest) this.request;
        }
        throw new IllegalStateException("'request' is not an OCSPStatusRequest");
    }

    public Object getRequest() {
        return this.request;
    }

    public short getStatusType() {
        return this.statusType;
    }
}
