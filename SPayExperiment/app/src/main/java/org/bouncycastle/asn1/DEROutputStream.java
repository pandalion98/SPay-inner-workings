/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;

public class DEROutputStream
extends ASN1OutputStream {
    public DEROutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    ASN1OutputStream getDERSubStream() {
        return this;
    }

    @Override
    ASN1OutputStream getDLSubStream() {
        return this;
    }

    @Override
    public void writeObject(ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1Encodable.toASN1Primitive().toDERObject().encode(this);
            return;
        }
        throw new IOException("null object detected");
    }
}

