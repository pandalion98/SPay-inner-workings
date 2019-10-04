/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.esf.OcspResponsesID;

public class OcspListID
extends ASN1Object {
    private ASN1Sequence ocspResponses;

    private OcspListID(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 1) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.ocspResponses = (ASN1Sequence)aSN1Sequence.getObjectAt(0);
        Enumeration enumeration = this.ocspResponses.getObjects();
        while (enumeration.hasMoreElements()) {
            OcspResponsesID.getInstance(enumeration.nextElement());
        }
    }

    public OcspListID(OcspResponsesID[] arrocspResponsesID) {
        this.ocspResponses = new DERSequence(arrocspResponsesID);
    }

    public static OcspListID getInstance(Object object) {
        if (object instanceof OcspListID) {
            return (OcspListID)object;
        }
        if (object != null) {
            return new OcspListID(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public OcspResponsesID[] getOcspResponses() {
        OcspResponsesID[] arrocspResponsesID = new OcspResponsesID[this.ocspResponses.size()];
        for (int i2 = 0; i2 < arrocspResponsesID.length; ++i2) {
            arrocspResponsesID[i2] = OcspResponsesID.getInstance(this.ocspResponses.getObjectAt(i2));
        }
        return arrocspResponsesID;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.ocspResponses);
    }
}

