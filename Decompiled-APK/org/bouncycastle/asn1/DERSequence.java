package org.bouncycastle.asn1;

import java.util.Enumeration;

public class DERSequence extends ASN1Sequence {
    private int bodyLength;

    public DERSequence() {
        this.bodyLength = -1;
    }

    public DERSequence(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
        this.bodyLength = -1;
    }

    public DERSequence(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector);
        this.bodyLength = -1;
    }

    public DERSequence(ASN1Encodable[] aSN1EncodableArr) {
        super(aSN1EncodableArr);
        this.bodyLength = -1;
    }

    private int getBodyLength() {
        if (this.bodyLength < 0) {
            Enumeration objects = getObjects();
            int i = 0;
            while (objects.hasMoreElements()) {
                i = ((ASN1Encodable) objects.nextElement()).toASN1Primitive().toDERObject().encodedLength() + i;
            }
            this.bodyLength = i;
        }
        return this.bodyLength;
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        ASN1OutputStream dERSubStream = aSN1OutputStream.getDERSubStream();
        int bodyLength = getBodyLength();
        aSN1OutputStream.write(48);
        aSN1OutputStream.writeLength(bodyLength);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dERSubStream.writeObject((ASN1Encodable) objects.nextElement());
        }
    }

    int encodedLength() {
        int bodyLength = getBodyLength();
        return bodyLength + (StreamUtil.calculateBodyLength(bodyLength) + 1);
    }
}
