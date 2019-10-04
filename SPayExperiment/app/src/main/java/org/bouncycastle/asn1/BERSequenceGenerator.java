/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 */
package org.bouncycastle.asn1;

import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.BERGenerator;
import org.bouncycastle.asn1.BEROutputStream;

public class BERSequenceGenerator
extends BERGenerator {
    public BERSequenceGenerator(OutputStream outputStream) {
        super(outputStream);
        this.writeBERHeader(48);
    }

    public BERSequenceGenerator(OutputStream outputStream, int n2, boolean bl) {
        super(outputStream, n2, bl);
        this.writeBERHeader(48);
    }

    public void addObject(ASN1Encodable aSN1Encodable) {
        aSN1Encodable.toASN1Primitive().encode(new BEROutputStream(this._out));
    }

    public void close() {
        this.writeBEREnd();
    }
}

