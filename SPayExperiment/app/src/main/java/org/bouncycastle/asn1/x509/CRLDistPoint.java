/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.DistributionPoint;

public class CRLDistPoint
extends ASN1Object {
    ASN1Sequence seq = null;

    private CRLDistPoint(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
    }

    public CRLDistPoint(DistributionPoint[] arrdistributionPoint) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != arrdistributionPoint.length; ++i2) {
            aSN1EncodableVector.add(arrdistributionPoint[i2]);
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public static CRLDistPoint getInstance(Object object) {
        if (object instanceof CRLDistPoint) {
            return (CRLDistPoint)object;
        }
        if (object != null) {
            return new CRLDistPoint(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static CRLDistPoint getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return CRLDistPoint.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public DistributionPoint[] getDistributionPoints() {
        DistributionPoint[] arrdistributionPoint = new DistributionPoint[this.seq.size()];
        for (int i2 = 0; i2 != this.seq.size(); ++i2) {
            arrdistributionPoint[i2] = DistributionPoint.getInstance(this.seq.getObjectAt(i2));
        }
        return arrdistributionPoint;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String string = System.getProperty((String)"line.separator");
        stringBuffer.append("CRLDistPoint:");
        stringBuffer.append(string);
        DistributionPoint[] arrdistributionPoint = this.getDistributionPoints();
        for (int i2 = 0; i2 != arrdistributionPoint.length; ++i2) {
            stringBuffer.append("    ");
            stringBuffer.append((Object)arrdistributionPoint[i2]);
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }
}

