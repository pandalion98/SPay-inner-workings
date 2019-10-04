/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.esf.CrlOcspRef;

public class CompleteRevocationRefs
extends ASN1Object {
    private ASN1Sequence crlOcspRefs;

    private CompleteRevocationRefs(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        while (enumeration.hasMoreElements()) {
            CrlOcspRef.getInstance(enumeration.nextElement());
        }
        this.crlOcspRefs = aSN1Sequence;
    }

    public CompleteRevocationRefs(CrlOcspRef[] arrcrlOcspRef) {
        this.crlOcspRefs = new DERSequence(arrcrlOcspRef);
    }

    public static CompleteRevocationRefs getInstance(Object object) {
        if (object instanceof CompleteRevocationRefs) {
            return (CompleteRevocationRefs)object;
        }
        if (object != null) {
            return new CompleteRevocationRefs(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CrlOcspRef[] getCrlOcspRefs() {
        CrlOcspRef[] arrcrlOcspRef = new CrlOcspRef[this.crlOcspRefs.size()];
        for (int i2 = 0; i2 < arrcrlOcspRef.length; ++i2) {
            arrcrlOcspRef[i2] = CrlOcspRef.getInstance(this.crlOcspRefs.getObjectAt(i2));
        }
        return arrcrlOcspRef;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.crlOcspRefs;
    }
}

