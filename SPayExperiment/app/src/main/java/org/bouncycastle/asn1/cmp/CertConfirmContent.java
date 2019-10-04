/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cmp.CertStatus;

public class CertConfirmContent
extends ASN1Object {
    private ASN1Sequence content;

    private CertConfirmContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static CertConfirmContent getInstance(Object object) {
        if (object instanceof CertConfirmContent) {
            return (CertConfirmContent)object;
        }
        if (object != null) {
            return new CertConfirmContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public CertStatus[] toCertStatusArray() {
        CertStatus[] arrcertStatus = new CertStatus[this.content.size()];
        for (int i2 = 0; i2 != arrcertStatus.length; ++i2) {
            arrcertStatus[i2] = CertStatus.getInstance(this.content.getObjectAt(i2));
        }
        return arrcertStatus;
    }
}

