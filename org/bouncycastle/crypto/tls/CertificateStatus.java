package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ocsp.OCSPResponse;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class CertificateStatus {
    protected Object response;
    protected short statusType;

    public CertificateStatus(short s, Object obj) {
        if (isCorrectType(s, obj)) {
            this.statusType = s;
            this.response = obj;
            return;
        }
        throw new IllegalArgumentException("'response' is not an instance of the correct type");
    }

    protected static boolean isCorrectType(short s, Object obj) {
        switch (s) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return obj instanceof OCSPResponse;
            default:
                throw new IllegalArgumentException("'statusType' is an unsupported value");
        }
    }

    public static CertificateStatus parse(InputStream inputStream) {
        short readUint8 = TlsUtils.readUint8(inputStream);
        switch (readUint8) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return new CertificateStatus(readUint8, OCSPResponse.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque24(inputStream))));
            default:
                throw new TlsFatalAlert((short) 50);
        }
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.statusType, outputStream);
        switch (this.statusType) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                TlsUtils.writeOpaque24(((OCSPResponse) this.response).getEncoded(ASN1Encoding.DER), outputStream);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public OCSPResponse getOCSPResponse() {
        if (isCorrectType((short) 1, this.response)) {
            return (OCSPResponse) this.response;
        }
        throw new IllegalStateException("'response' is not an OCSPResponse");
    }

    public Object getResponse() {
        return this.response;
    }

    public short getStatusType() {
        return this.statusType;
    }
}
