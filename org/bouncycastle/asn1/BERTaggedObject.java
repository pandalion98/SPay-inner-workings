package org.bouncycastle.asn1;

import java.util.Enumeration;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;

public class BERTaggedObject extends ASN1TaggedObject {
    public BERTaggedObject(int i) {
        super(false, i, new BERSequence());
    }

    public BERTaggedObject(int i, ASN1Encodable aSN1Encodable) {
        super(true, i, aSN1Encodable);
    }

    public BERTaggedObject(boolean z, int i, ASN1Encodable aSN1Encodable) {
        super(z, i, aSN1Encodable);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeTag(CipherSuite.TLS_DH_RSA_WITH_AES_128_GCM_SHA256, this.tagNo);
        aSN1OutputStream.write((int) X509KeyUsage.digitalSignature);
        if (!this.empty) {
            if (this.explicit) {
                aSN1OutputStream.writeObject(this.obj);
            } else {
                Enumeration objects;
                if (this.obj instanceof ASN1OctetString) {
                    objects = this.obj instanceof BEROctetString ? ((BEROctetString) this.obj).getObjects() : new BEROctetString(((ASN1OctetString) this.obj).getOctets()).getObjects();
                } else if (this.obj instanceof ASN1Sequence) {
                    objects = ((ASN1Sequence) this.obj).getObjects();
                } else if (this.obj instanceof ASN1Set) {
                    objects = ((ASN1Set) this.obj).getObjects();
                } else {
                    throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
                }
                while (objects.hasMoreElements()) {
                    aSN1OutputStream.writeObject((ASN1Encodable) objects.nextElement());
                }
            }
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    int encodedLength() {
        if (this.empty) {
            return StreamUtil.calculateTagLength(this.tagNo) + 1;
        }
        int encodedLength = this.obj.toASN1Primitive().encodedLength();
        return this.explicit ? encodedLength + (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(encodedLength)) : (encodedLength - 1) + StreamUtil.calculateTagLength(this.tagNo);
    }

    boolean isConstructed() {
        return (this.empty || this.explicit) ? true : this.obj.toASN1Primitive().toDERObject().isConstructed();
    }
}
