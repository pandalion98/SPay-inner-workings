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
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.CertificateList;

public class CRLAnnContent
extends ASN1Object {
    private ASN1Sequence content;

    private CRLAnnContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public CRLAnnContent(CertificateList certificateList) {
        this.content = new DERSequence(certificateList);
    }

    public static CRLAnnContent getInstance(Object object) {
        if (object instanceof CRLAnnContent) {
            return (CRLAnnContent)object;
        }
        if (object != null) {
            return new CRLAnnContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CertificateList[] getCertificateLists() {
        CertificateList[] arrcertificateList = new CertificateList[this.content.size()];
        for (int i2 = 0; i2 != arrcertificateList.length; ++i2) {
            arrcertificateList[i2] = CertificateList.getInstance(this.content.getObjectAt(i2));
        }
        return arrcertificateList;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}

