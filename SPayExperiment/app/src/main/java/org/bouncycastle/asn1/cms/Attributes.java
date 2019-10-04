/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.asn1.cms.Attribute;

public class Attributes
extends ASN1Object {
    private ASN1Set attributes;

    public Attributes(ASN1EncodableVector aSN1EncodableVector) {
        this.attributes = new DLSet(aSN1EncodableVector);
    }

    private Attributes(ASN1Set aSN1Set) {
        this.attributes = aSN1Set;
    }

    public static Attributes getInstance(Object object) {
        if (object instanceof Attributes) {
            return (Attributes)object;
        }
        if (object != null) {
            return new Attributes(ASN1Set.getInstance(object));
        }
        return null;
    }

    public Attribute[] getAttributes() {
        Attribute[] arrattribute = new Attribute[this.attributes.size()];
        for (int i2 = 0; i2 != arrattribute.length; ++i2) {
            arrattribute[i2] = Attribute.getInstance(this.attributes.getObjectAt(i2));
        }
        return arrattribute;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.attributes;
    }
}

