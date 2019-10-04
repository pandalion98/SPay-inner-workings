/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 *  java.util.Vector
 */
package org.bouncycastle.asn1;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;

public class ASN1EncodableVector {
    Vector v = new Vector();

    public void add(ASN1Encodable aSN1Encodable) {
        this.v.addElement((Object)aSN1Encodable);
    }

    public void addAll(ASN1EncodableVector aSN1EncodableVector) {
        Enumeration enumeration = aSN1EncodableVector.v.elements();
        while (enumeration.hasMoreElements()) {
            this.v.addElement(enumeration.nextElement());
        }
    }

    public ASN1Encodable get(int n2) {
        return (ASN1Encodable)this.v.elementAt(n2);
    }

    public int size() {
        return this.v.size();
    }
}

