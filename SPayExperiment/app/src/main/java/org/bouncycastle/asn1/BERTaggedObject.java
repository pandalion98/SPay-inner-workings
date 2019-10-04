/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BEROctetString;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.StreamUtil;

public class BERTaggedObject
extends ASN1TaggedObject {
    public BERTaggedObject(int n2) {
        super(false, n2, new BERSequence());
    }

    public BERTaggedObject(int n2, ASN1Encodable aSN1Encodable) {
        super(true, n2, aSN1Encodable);
    }

    public BERTaggedObject(boolean bl, int n2, ASN1Encodable aSN1Encodable) {
        super(bl, n2, aSN1Encodable);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeTag(160, this.tagNo);
        aSN1OutputStream.write(128);
        if (!this.empty) {
            if (!this.explicit) {
                Enumeration enumeration;
                if (this.obj instanceof ASN1OctetString) {
                    enumeration = this.obj instanceof BEROctetString ? ((BEROctetString)this.obj).getObjects() : new BEROctetString(((ASN1OctetString)this.obj).getOctets()).getObjects();
                } else if (this.obj instanceof ASN1Sequence) {
                    enumeration = ((ASN1Sequence)this.obj).getObjects();
                } else {
                    if (!(this.obj instanceof ASN1Set)) {
                        throw new RuntimeException("not implemented: " + this.obj.getClass().getName());
                    }
                    enumeration = ((ASN1Set)this.obj).getObjects();
                }
                while (enumeration.hasMoreElements()) {
                    aSN1OutputStream.writeObject((ASN1Encodable)enumeration.nextElement());
                }
            } else {
                aSN1OutputStream.writeObject(this.obj);
            }
        }
        aSN1OutputStream.write(0);
        aSN1OutputStream.write(0);
    }

    @Override
    int encodedLength() {
        if (!this.empty) {
            int n2 = this.obj.toASN1Primitive().encodedLength();
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
        return this.obj.toASN1Primitive().toDERObject().isConstructed();
    }
}

