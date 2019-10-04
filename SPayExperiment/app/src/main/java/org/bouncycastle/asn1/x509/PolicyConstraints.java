/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public class PolicyConstraints
extends ASN1Object {
    private BigInteger inhibitPolicyMapping;
    private BigInteger requireExplicitPolicyMapping;

    public PolicyConstraints(BigInteger bigInteger, BigInteger bigInteger2) {
        this.requireExplicitPolicyMapping = bigInteger;
        this.inhibitPolicyMapping = bigInteger2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private PolicyConstraints(ASN1Sequence aSN1Sequence) {
        int n2 = 0;
        while (n2 != aSN1Sequence.size()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(n2));
            if (aSN1TaggedObject.getTagNo() == 0) {
                this.requireExplicitPolicyMapping = ASN1Integer.getInstance(aSN1TaggedObject, false).getValue();
            } else {
                if (aSN1TaggedObject.getTagNo() != 1) {
                    throw new IllegalArgumentException("Unknown tag encountered.");
                }
                this.inhibitPolicyMapping = ASN1Integer.getInstance(aSN1TaggedObject, false).getValue();
            }
            ++n2;
        }
        return;
    }

    public static PolicyConstraints fromExtensions(Extensions extensions) {
        return PolicyConstraints.getInstance(extensions.getExtensionParsedValue(Extension.policyConstraints));
    }

    public static PolicyConstraints getInstance(Object object) {
        if (object instanceof PolicyConstraints) {
            return (PolicyConstraints)object;
        }
        if (object != null) {
            return new PolicyConstraints(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public BigInteger getInhibitPolicyMapping() {
        return this.inhibitPolicyMapping;
    }

    public BigInteger getRequireExplicitPolicyMapping() {
        return this.requireExplicitPolicyMapping;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.requireExplicitPolicyMapping != null) {
            aSN1EncodableVector.add(new DERTaggedObject(0, new ASN1Integer(this.requireExplicitPolicyMapping)));
        }
        if (this.inhibitPolicyMapping != null) {
            aSN1EncodableVector.add(new DERTaggedObject(1, new ASN1Integer(this.inhibitPolicyMapping)));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

