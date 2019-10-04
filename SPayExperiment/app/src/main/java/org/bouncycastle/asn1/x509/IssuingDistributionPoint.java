/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.System
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Boolean;
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
import org.bouncycastle.asn1.x509.ReasonFlags;

public class IssuingDistributionPoint
extends ASN1Object {
    private DistributionPointName distributionPoint;
    private boolean indirectCRL;
    private boolean onlyContainsAttributeCerts;
    private boolean onlyContainsCACerts;
    private boolean onlyContainsUserCerts;
    private ReasonFlags onlySomeReasons;
    private ASN1Sequence seq;

    /*
     * Enabled aggressive block sorting
     */
    private IssuingDistributionPoint(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
        int n2 = 0;
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("unknown tag in IssuingDistributionPoint");
                }
                case 0: {
                    this.distributionPoint = DistributionPointName.getInstance(aSN1TaggedObject, true);
                    break;
                }
                case 1: {
                    this.onlyContainsUserCerts = ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue();
                    break;
                }
                case 2: {
                    this.onlyContainsCACerts = ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue();
                    break;
                }
                case 3: {
                    this.onlySomeReasons = new ReasonFlags(ReasonFlags.getInstance(aSN1TaggedObject, false));
                    break;
                }
                case 4: {
                    this.indirectCRL = ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue();
                    break;
                }
                case 5: {
                    this.onlyContainsAttributeCerts = ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue();
                }
            }
            ++n2;
        }
        return;
    }

    public IssuingDistributionPoint(DistributionPointName distributionPointName, boolean bl, boolean bl2) {
        this(distributionPointName, false, false, null, bl, bl2);
    }

    public IssuingDistributionPoint(DistributionPointName distributionPointName, boolean bl, boolean bl2, ReasonFlags reasonFlags, boolean bl3, boolean bl4) {
        this.distributionPoint = distributionPointName;
        this.indirectCRL = bl3;
        this.onlyContainsAttributeCerts = bl4;
        this.onlyContainsCACerts = bl2;
        this.onlyContainsUserCerts = bl;
        this.onlySomeReasons = reasonFlags;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (distributionPointName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, distributionPointName));
        }
        if (bl) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, ASN1Boolean.getInstance(true)));
        }
        if (bl2) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 2, ASN1Boolean.getInstance(true)));
        }
        if (reasonFlags != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 3, reasonFlags));
        }
        if (bl3) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 4, ASN1Boolean.getInstance(true)));
        }
        if (bl4) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 5, ASN1Boolean.getInstance(true)));
        }
        this.seq = new DERSequence(aSN1EncodableVector);
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

    private String booleanToString(boolean bl) {
        if (bl) {
            return "true";
        }
        return "false";
    }

    public static IssuingDistributionPoint getInstance(Object object) {
        if (object instanceof IssuingDistributionPoint) {
            return (IssuingDistributionPoint)object;
        }
        if (object != null) {
            return new IssuingDistributionPoint(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static IssuingDistributionPoint getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return IssuingDistributionPoint.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }

    public ReasonFlags getOnlySomeReasons() {
        return this.onlySomeReasons;
    }

    public boolean isIndirectCRL() {
        return this.indirectCRL;
    }

    public boolean onlyContainsAttributeCerts() {
        return this.onlyContainsAttributeCerts;
    }

    public boolean onlyContainsCACerts() {
        return this.onlyContainsCACerts;
    }

    public boolean onlyContainsUserCerts() {
        return this.onlyContainsUserCerts;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }

    public String toString() {
        String string = System.getProperty((String)"line.separator");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("IssuingDistributionPoint: [");
        stringBuffer.append(string);
        if (this.distributionPoint != null) {
            this.appendObject(stringBuffer, string, "distributionPoint", this.distributionPoint.toString());
        }
        if (this.onlyContainsUserCerts) {
            this.appendObject(stringBuffer, string, "onlyContainsUserCerts", this.booleanToString(this.onlyContainsUserCerts));
        }
        if (this.onlyContainsCACerts) {
            this.appendObject(stringBuffer, string, "onlyContainsCACerts", this.booleanToString(this.onlyContainsCACerts));
        }
        if (this.onlySomeReasons != null) {
            this.appendObject(stringBuffer, string, "onlySomeReasons", this.onlySomeReasons.toString());
        }
        if (this.onlyContainsAttributeCerts) {
            this.appendObject(stringBuffer, string, "onlyContainsAttributeCerts", this.booleanToString(this.onlyContainsAttributeCerts));
        }
        if (this.indirectCRL) {
            this.appendObject(stringBuffer, string, "indirectCRL", this.booleanToString(this.indirectCRL));
        }
        stringBuffer.append("]");
        stringBuffer.append(string);
        return stringBuffer.toString();
    }
}

