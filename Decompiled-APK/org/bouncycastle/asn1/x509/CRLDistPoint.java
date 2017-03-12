package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;

public class CRLDistPoint extends ASN1Object {
    ASN1Sequence seq;

    private CRLDistPoint(ASN1Sequence aSN1Sequence) {
        this.seq = null;
        this.seq = aSN1Sequence;
    }

    public CRLDistPoint(DistributionPoint[] distributionPointArr) {
        this.seq = null;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != distributionPointArr.length; i++) {
            aSN1EncodableVector.add(distributionPointArr[i]);
        }
        this.seq = new DERSequence(aSN1EncodableVector);
    }

    public static CRLDistPoint getInstance(Object obj) {
        return obj instanceof CRLDistPoint ? (CRLDistPoint) obj : obj != null ? new CRLDistPoint(ASN1Sequence.getInstance(obj)) : null;
    }

    public static CRLDistPoint getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public DistributionPoint[] getDistributionPoints() {
        DistributionPoint[] distributionPointArr = new DistributionPoint[this.seq.size()];
        for (int i = 0; i != this.seq.size(); i++) {
            distributionPointArr[i] = DistributionPoint.getInstance(this.seq.getObjectAt(i));
        }
        return distributionPointArr;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String property = System.getProperty("line.separator");
        stringBuffer.append("CRLDistPoint:");
        stringBuffer.append(property);
        DistributionPoint[] distributionPoints = getDistributionPoints();
        for (int i = 0; i != distributionPoints.length; i++) {
            stringBuffer.append("    ");
            stringBuffer.append(distributionPoints[i]);
            stringBuffer.append(property);
        }
        return stringBuffer.toString();
    }
}
