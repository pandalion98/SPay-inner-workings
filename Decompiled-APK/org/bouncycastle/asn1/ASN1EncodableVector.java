package org.bouncycastle.asn1;

import java.util.Enumeration;
import java.util.Vector;

public class ASN1EncodableVector {
    Vector f21v;

    public ASN1EncodableVector() {
        this.f21v = new Vector();
    }

    public void add(ASN1Encodable aSN1Encodable) {
        this.f21v.addElement(aSN1Encodable);
    }

    public void addAll(ASN1EncodableVector aSN1EncodableVector) {
        Enumeration elements = aSN1EncodableVector.f21v.elements();
        while (elements.hasMoreElements()) {
            this.f21v.addElement(elements.nextElement());
        }
    }

    public ASN1Encodable get(int i) {
        return (ASN1Encodable) this.f21v.elementAt(i);
    }

    public int size() {
        return this.f21v.size();
    }
}
