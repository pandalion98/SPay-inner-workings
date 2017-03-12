package org.bouncycastle.asn1;

import java.util.Enumeration;

class LazyEncodedSequence extends ASN1Sequence {
    private byte[] encoded;

    LazyEncodedSequence(byte[] bArr) {
        this.encoded = bArr;
    }

    private void parse() {
        Enumeration lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
        while (lazyConstructionEnumeration.hasMoreElements()) {
            this.seq.addElement(lazyConstructionEnumeration.nextElement());
        }
        this.encoded = null;
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        if (this.encoded != null) {
            aSN1OutputStream.writeEncoded(48, this.encoded);
        } else {
            super.toDLObject().encode(aSN1OutputStream);
        }
    }

    int encodedLength() {
        return this.encoded != null ? (StreamUtil.calculateBodyLength(this.encoded.length) + 1) + this.encoded.length : super.toDLObject().encodedLength();
    }

    public synchronized ASN1Encodable getObjectAt(int i) {
        if (this.encoded != null) {
            parse();
        }
        return super.getObjectAt(i);
    }

    public synchronized Enumeration getObjects() {
        return this.encoded == null ? super.getObjects() : new LazyConstructionEnumeration(this.encoded);
    }

    public synchronized int size() {
        if (this.encoded != null) {
            parse();
        }
        return super.size();
    }

    ASN1Primitive toDERObject() {
        if (this.encoded != null) {
            parse();
        }
        return super.toDERObject();
    }

    ASN1Primitive toDLObject() {
        if (this.encoded != null) {
            parse();
        }
        return super.toDLObject();
    }
}
