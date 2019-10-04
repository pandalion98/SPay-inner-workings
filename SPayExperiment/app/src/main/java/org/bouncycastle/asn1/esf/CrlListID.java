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
import org.bouncycastle.asn1.esf.CrlValidatedID;

public class CrlListID
extends ASN1Object {
    private ASN1Sequence crls;

    private CrlListID(ASN1Sequence aSN1Sequence) {
        this.crls = (ASN1Sequence)aSN1Sequence.getObjectAt(0);
        Enumeration enumeration = this.crls.getObjects();
        while (enumeration.hasMoreElements()) {
            CrlValidatedID.getInstance(enumeration.nextElement());
        }
    }

    public CrlListID(CrlValidatedID[] arrcrlValidatedID) {
        this.crls = new DERSequence(arrcrlValidatedID);
    }

    public static CrlListID getInstance(Object object) {
        if (object instanceof CrlListID) {
            return (CrlListID)object;
        }
        if (object != null) {
            return new CrlListID(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CrlValidatedID[] getCrls() {
        CrlValidatedID[] arrcrlValidatedID = new CrlValidatedID[this.crls.size()];
        for (int i2 = 0; i2 < arrcrlValidatedID.length; ++i2) {
            arrcrlValidatedID[i2] = CrlValidatedID.getInstance(this.crls.getObjectAt(i2));
        }
        return arrcrlValidatedID;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.crls);
    }
}

