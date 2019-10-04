/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.StreamUtil;

public class DLSequence
extends ASN1Sequence {
    private int bodyLength = -1;

    public DLSequence() {
    }

    public DLSequence(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
    }

    public DLSequence(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector);
    }

    public DLSequence(ASN1Encodable[] arraSN1Encodable) {
        super(arraSN1Encodable);
    }

    private int getBodyLength() {
        if (this.bodyLength < 0) {
            Enumeration enumeration = this.getObjects();
            int n2 = 0;
            while (enumeration.hasMoreElements()) {
                n2 += ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().toDLObject().encodedLength();
            }
            this.bodyLength = n2;
        }
        return this.bodyLength;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        ASN1OutputStream aSN1OutputStream2 = aSN1OutputStream.getDLSubStream();
        int n2 = this.getBodyLength();
        aSN1OutputStream.write(48);
        aSN1OutputStream.writeLength(n2);
        Enumeration enumeration = this.getObjects();
        while (enumeration.hasMoreElements()) {
            aSN1OutputStream2.writeObject((ASN1Encodable)enumeration.nextElement());
        }
    }

    @Override
    int encodedLength() {
        int n2 = this.getBodyLength();
        return n2 + (1 + StreamUtil.calculateBodyLength(n2));
    }
}

