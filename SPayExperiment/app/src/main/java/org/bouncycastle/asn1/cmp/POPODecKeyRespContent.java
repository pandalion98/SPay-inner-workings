/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

public class POPODecKeyRespContent
extends ASN1Object {
    private ASN1Sequence content;

    private POPODecKeyRespContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static POPODecKeyRespContent getInstance(Object object) {
        if (object instanceof POPODecKeyRespContent) {
            return (POPODecKeyRespContent)object;
        }
        if (object != null) {
            return new POPODecKeyRespContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Integer[] toASN1IntegerArray() {
        ASN1Integer[] arraSN1Integer = new ASN1Integer[this.content.size()];
        for (int i2 = 0; i2 != arraSN1Integer.length; ++i2) {
            arraSN1Integer[i2] = ASN1Integer.getInstance(this.content.getObjectAt(i2));
        }
        return arraSN1Integer;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}

