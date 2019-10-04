/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERGenerator;
import org.bouncycastle.asn1.DEROutputStream;

public class DERSequenceGenerator
extends DERGenerator {
    private final ByteArrayOutputStream _bOut = new ByteArrayOutputStream();

    public DERSequenceGenerator(OutputStream outputStream) {
        super(outputStream);
    }

    public DERSequenceGenerator(OutputStream outputStream, int n2, boolean bl) {
        super(outputStream, n2, bl);
    }

    public void addObject(ASN1Encodable aSN1Encodable) {
        aSN1Encodable.toASN1Primitive().encode(new DEROutputStream((OutputStream)this._bOut));
    }

    public void close() {
        this.writeDEREncoded(48, this._bOut.toByteArray());
    }

    @Override
    public OutputStream getRawOutputStream() {
        return this._bOut;
    }
}

