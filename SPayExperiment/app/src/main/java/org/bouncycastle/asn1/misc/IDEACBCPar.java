/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

public class IDEACBCPar
extends ASN1Object {
    ASN1OctetString iv;

    public IDEACBCPar(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 1) {
            this.iv = (ASN1OctetString)aSN1Sequence.getObjectAt(0);
            return;
        }
        this.iv = null;
    }

    public IDEACBCPar(byte[] arrby) {
        this.iv = new DEROctetString(arrby);
    }

    public static IDEACBCPar getInstance(Object object) {
        if (object instanceof IDEACBCPar) {
            return (IDEACBCPar)object;
        }
        if (object != null) {
            return new IDEACBCPar(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public byte[] getIV() {
        if (this.iv != null) {
            return this.iv.getOctets();
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.iv != null) {
            aSN1EncodableVector.add(this.iv);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

