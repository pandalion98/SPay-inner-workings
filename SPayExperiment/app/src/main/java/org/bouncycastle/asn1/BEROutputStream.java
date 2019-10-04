/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROutputStream;

public class BEROutputStream
extends DEROutputStream {
    public BEROutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void writeObject(Object object) {
        if (object == null) {
            this.writeNull();
            return;
        }
        if (object instanceof ASN1Primitive) {
            ((ASN1Primitive)object).encode(this);
            return;
        }
        if (object instanceof ASN1Encodable) {
            ((ASN1Encodable)object).toASN1Primitive().encode(this);
            return;
        }
        throw new IOException("object not BEREncodable");
    }
}

