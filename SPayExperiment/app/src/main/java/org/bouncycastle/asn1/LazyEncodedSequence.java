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
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.LazyConstructionEnumeration;
import org.bouncycastle.asn1.StreamUtil;

class LazyEncodedSequence
extends ASN1Sequence {
    private byte[] encoded;

    LazyEncodedSequence(byte[] arrby) {
        this.encoded = arrby;
    }

    private void parse() {
        LazyConstructionEnumeration lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
        while (lazyConstructionEnumeration.hasMoreElements()) {
            this.seq.addElement(lazyConstructionEnumeration.nextElement());
        }
        this.encoded = null;
    }

    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        if (this.encoded != null) {
            aSN1OutputStream.writeEncoded(48, this.encoded);
            return;
        }
        super.toDLObject().encode(aSN1OutputStream);
    }

    @Override
    int encodedLength() {
        if (this.encoded != null) {
            return 1 + StreamUtil.calculateBodyLength(this.encoded.length) + this.encoded.length;
        }
        return super.toDLObject().encodedLength();
    }

    @Override
    public ASN1Encodable getObjectAt(int n2) {
        LazyEncodedSequence lazyEncodedSequence = this;
        synchronized (lazyEncodedSequence) {
            if (this.encoded != null) {
                this.parse();
            }
            ASN1Encodable aSN1Encodable = super.getObjectAt(n2);
            return aSN1Encodable;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public Enumeration getObjects() {
        LazyEncodedSequence lazyEncodedSequence = this;
        synchronized (lazyEncodedSequence) {
            block6 : {
                if (this.encoded != null) break block6;
                Enumeration enumeration = super.getObjects();
                return enumeration;
            }
            Enumeration enumeration = new LazyConstructionEnumeration(this.encoded);
            return enumeration;
        }
    }

    @Override
    public int size() {
        LazyEncodedSequence lazyEncodedSequence = this;
        synchronized (lazyEncodedSequence) {
            if (this.encoded != null) {
                this.parse();
            }
            int n2 = super.size();
            return n2;
        }
    }

    @Override
    ASN1Primitive toDERObject() {
        if (this.encoded != null) {
            this.parse();
        }
        return super.toDERObject();
    }

    @Override
    ASN1Primitive toDLObject() {
        if (this.encoded != null) {
            this.parse();
        }
        return super.toDLObject();
    }
}

