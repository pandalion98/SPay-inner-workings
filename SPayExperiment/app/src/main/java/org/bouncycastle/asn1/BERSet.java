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
import org.bouncycastle.asn1.ASN1Set;

public class BERSet
extends ASN1Set {
    public BERSet() {
    }

    public BERSet(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable);
    }

    public BERSet(ASN1EncodableVector aSN1EncodableVector) {
        super(aSN1EncodableVector, false);
    }

    public BERSet(ASN1Encodable[] arraSN1Encodable) {
        super(arraSN1Encodable, false);
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.write(49);
        aSN1OutputStream.write(128);
        Enumeration enumeration = this.getObjects();
        while (enumeration.hasMoreElements()) {
            aSN1OutputStream.writeObject((ASN1Encodable)enumeration.nextElement());
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    @Override
    int encodedLength() {
        Enumeration enumeration = this.getObjects();
        int n2 = 0;
        while (enumeration.hasMoreElements()) {
            n2 += ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().encodedLength();
        }
        return 2 + (n2 + 2);
    }
}

