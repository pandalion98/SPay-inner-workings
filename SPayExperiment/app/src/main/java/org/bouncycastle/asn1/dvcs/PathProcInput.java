/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.dvcs;

import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.PolicyInformation;

public class PathProcInput
extends ASN1Object {
    private PolicyInformation[] acceptablePolicySet;
    private boolean explicitPolicyReqd = false;
    private boolean inhibitAnyPolicy = false;
    private boolean inhibitPolicyMapping = false;

    public PathProcInput(PolicyInformation[] arrpolicyInformation) {
        this.acceptablePolicySet = arrpolicyInformation;
    }

    public PathProcInput(PolicyInformation[] arrpolicyInformation, boolean bl, boolean bl2, boolean bl3) {
        this.acceptablePolicySet = arrpolicyInformation;
        this.inhibitPolicyMapping = bl;
        this.explicitPolicyReqd = bl2;
        this.inhibitAnyPolicy = bl3;
    }

    private static PolicyInformation[] fromSequence(ASN1Sequence aSN1Sequence) {
        PolicyInformation[] arrpolicyInformation = new PolicyInformation[aSN1Sequence.size()];
        for (int i2 = 0; i2 != arrpolicyInformation.length; ++i2) {
            arrpolicyInformation[i2] = PolicyInformation.getInstance(aSN1Sequence.getObjectAt(i2));
        }
        return arrpolicyInformation;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static PathProcInput getInstance(Object object) {
        if (object instanceof PathProcInput) {
            return (PathProcInput)object;
        }
        if (object == null) {
            return null;
        }
        ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(object);
        PathProcInput pathProcInput = new PathProcInput(PathProcInput.fromSequence(ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(0))));
        int n2 = 1;
        while (n2 < aSN1Sequence.size()) {
            ASN1Encodable aSN1Encodable = aSN1Sequence.getObjectAt(n2);
            if (aSN1Encodable instanceof ASN1Boolean) {
                pathProcInput.setInhibitPolicyMapping(ASN1Boolean.getInstance(aSN1Encodable).isTrue());
            } else if (aSN1Encodable instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Encodable);
                switch (aSN1TaggedObject.getTagNo()) {
                    default: {
                        break;
                    }
                    case 0: {
                        pathProcInput.setExplicitPolicyReqd(ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue());
                        break;
                    }
                    case 1: {
                        pathProcInput.setInhibitAnyPolicy(ASN1Boolean.getInstance(aSN1TaggedObject, false).isTrue());
                    }
                }
            }
            ++n2;
        }
        return pathProcInput;
    }

    public static PathProcInput getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return PathProcInput.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    private void setExplicitPolicyReqd(boolean bl) {
        this.explicitPolicyReqd = bl;
    }

    private void setInhibitAnyPolicy(boolean bl) {
        this.inhibitAnyPolicy = bl;
    }

    private void setInhibitPolicyMapping(boolean bl) {
        this.inhibitPolicyMapping = bl;
    }

    public PolicyInformation[] getAcceptablePolicySet() {
        return this.acceptablePolicySet;
    }

    public boolean isExplicitPolicyReqd() {
        return this.explicitPolicyReqd;
    }

    public boolean isInhibitAnyPolicy() {
        return this.inhibitAnyPolicy;
    }

    public boolean isInhibitPolicyMapping() {
        return this.inhibitPolicyMapping;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i2 = 0; i2 != this.acceptablePolicySet.length; ++i2) {
            aSN1EncodableVector2.add(this.acceptablePolicySet[i2]);
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        if (this.inhibitPolicyMapping) {
            aSN1EncodableVector.add(new ASN1Boolean(this.inhibitPolicyMapping));
        }
        if (this.explicitPolicyReqd) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, new ASN1Boolean(this.explicitPolicyReqd)));
        }
        if (this.inhibitAnyPolicy) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, new ASN1Boolean(this.inhibitAnyPolicy)));
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "PathProcInput: {\nacceptablePolicySet: " + this.acceptablePolicySet + "\n" + "inhibitPolicyMapping: " + this.inhibitPolicyMapping + "\n" + "explicitPolicyReqd: " + this.explicitPolicyReqd + "\n" + "inhibitAnyPolicy: " + this.inhibitAnyPolicy + "\n" + "}\n";
    }
}

