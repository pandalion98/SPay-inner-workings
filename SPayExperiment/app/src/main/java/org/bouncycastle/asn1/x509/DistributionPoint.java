/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
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
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.ReasonFlags;

public class DistributionPoint
extends ASN1Object {
    GeneralNames cRLIssuer;
    DistributionPointName distributionPoint;
    ReasonFlags reasons;

    /*
     * Enabled aggressive block sorting
     */
    public DistributionPoint(ASN1Sequence aSN1Sequence) {
        int n2 = 0;
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            switch (aSN1TaggedObject.getTagNo()) {
                case 0: {
                    this.distributionPoint = DistributionPointName.getInstance(aSN1TaggedObject, true);
                    break;
                }
                case 1: {
                    this.reasons = new ReasonFlags(DERBitString.getInstance(aSN1TaggedObject, false));
                    break;
                }
                case 2: {
                    this.cRLIssuer = GeneralNames.getInstance(aSN1TaggedObject, false);
                    break;
                }
            }
            ++n2;
        }
        return;
    }

    public DistributionPoint(DistributionPointName distributionPointName, ReasonFlags reasonFlags, GeneralNames generalNames) {
        this.distributionPoint = distributionPointName;
        this.reasons = reasonFlags;
        this.cRLIssuer = generalNames;
    }

    private void appendObject(StringBuffer stringBuffer, String string, String string2, String string3) {
        stringBuffer.append("    ");
        stringBuffer.append(string2);
        stringBuffer.append(":");
        stringBuffer.append(string);
        stringBuffer.append("    ");
        stringBuffer.append("    ");
        stringBuffer.append(string3);
        stringBuffer.append(string);
    }

    public static DistributionPoint getInstance(Object object) {
        if (object == null || object instanceof DistributionPoint) {
            return (DistributionPoint)object;
        }
        if (object instanceof ASN1Sequence) {
            return new DistributionPoint((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("Invalid DistributionPoint: " + object.getClass().getName());
    }

    public static DistributionPoint getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return DistributionPoint.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public GeneralNames getCRLIssuer() {
        return this.cRLIssuer;
    }

    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }

    public ReasonFlags getReasons() {
        return this.reasons;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.distributionPoint != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, this.distributionPoint));
        }
        if (this.reasons != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.reasons));
        }
        if (this.cRLIssuer != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 2, this.cRLIssuer));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        String string = System.getProperty((String)"line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DistributionPoint: [");
        stringBuffer.append(string);
        if (this.distributionPoint != null) {
            this.appendObject(stringBuffer, string, "distributionPoint", this.distributionPoint.toString());
        }
        if (this.reasons != null) {
            this.appendObject(stringBuffer, string, "reasons", this.reasons.toString());
        }
        if (this.cRLIssuer != null) {
            this.appendObject(stringBuffer, string, "cRLIssuer", this.cRLIssuer.toString());
        }
        stringBuffer.append("]");
        stringBuffer.append(string);
        return stringBuffer.toString();
    }
}

