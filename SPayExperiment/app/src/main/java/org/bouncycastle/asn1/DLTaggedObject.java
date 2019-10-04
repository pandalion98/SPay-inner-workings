/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.StreamUtil;

public class DLTaggedObject
extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES = new byte[0];

    public DLTaggedObject(boolean bl, int n2, ASN1Encodable aSN1Encodable) {
        super(bl, n2, aSN1Encodable);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        int n2 = 160;
        if (this.empty) {
            aSN1OutputStream.writeEncoded(n2, this.tagNo, ZERO_BYTES);
            return;
        }
        ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive().toDLObject();
        if (this.explicit) {
            aSN1OutputStream.writeTag(n2, this.tagNo);
            aSN1OutputStream.writeLength(aSN1Primitive.encodedLength());
            aSN1OutputStream.writeObject(aSN1Primitive);
            return;
        }
        if (!aSN1Primitive.isConstructed()) {
            n2 = 128;
        }
        aSN1OutputStream.writeTag(n2, this.tagNo);
        aSN1OutputStream.writeImplicitObject(aSN1Primitive);
    }

    @Override
    int encodedLength() {
        if (!this.empty) {
            int n2 = this.obj.toASN1Primitive().toDLObject().encodedLength();
            if (this.explicit) {
                return n2 + (StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(n2));
            }
            return n2 - 1 + StreamUtil.calculateTagLength(this.tagNo);
        }
        return 1 + StreamUtil.calculateTagLength(this.tagNo);
    }

    @Override
    boolean isConstructed() {
        if (this.empty || this.explicit) {
            return true;
        }
        return this.obj.toASN1Primitive().toDLObject().isConstructed();
    }
}

