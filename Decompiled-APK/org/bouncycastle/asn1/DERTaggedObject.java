package org.bouncycastle.asn1;

import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;

public class DERTaggedObject extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES;

    static {
        ZERO_BYTES = new byte[0];
    }

    public DERTaggedObject(int i, ASN1Encodable aSN1Encodable) {
        super(true, i, aSN1Encodable);
    }

    public DERTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        int i = CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256;
        if (this.empty) {
            aSN1OutputStream.writeEncoded(CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, this.tagNo, ZERO_BYTES);
            return;
        }
        ASN1Primitive toDERObject = this.obj.toASN1Primitive().toDERObject();
        if (this.explicit) {
            aSN1OutputStream.writeTag(CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, this.tagNo);
            aSN1OutputStream.writeLength(toDERObject.encodedLength());
            aSN1OutputStream.writeObject(toDERObject);
            return;
        }
        if (!toDERObject.isConstructed()) {
            i = X509KeyUsage.digitalSignature;
        }
        aSN1OutputStream.writeTag(i, this.tagNo);
        aSN1OutputStream.writeImplicitObject(toDERObject);
    }

    int encodedLength() {
        if (this.empty) {
            return StreamUtil.calculateTagLength(this.tagNo) + 1;
        }
        int encodedLength = this.obj.toASN1Primitive().toDERObject().encodedLength();
        return this.explicit ? encodedLength + (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(encodedLength)) : (encodedLength - 1) + StreamUtil.calculateTagLength(this.tagNo);
    }

    boolean isConstructed() {
        return (this.empty || this.explicit) ? true : this.obj.toASN1Primitive().toDERObject().isConstructed();
    }
}
