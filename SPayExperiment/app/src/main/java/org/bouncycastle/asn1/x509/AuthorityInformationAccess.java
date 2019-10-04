/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.GeneralName;

public class AuthorityInformationAccess
extends ASN1Object {
    private AccessDescription[] descriptions;

    public AuthorityInformationAccess(ASN1ObjectIdentifier aSN1ObjectIdentifier, GeneralName generalName) {
        this(new AccessDescription(aSN1ObjectIdentifier, generalName));
    }

    private AuthorityInformationAccess(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1) {
            throw new IllegalArgumentException("sequence may not be empty");
        }
        this.descriptions = new AccessDescription[aSN1Sequence.size()];
        for (int i2 = 0; i2 != aSN1Sequence.size(); ++i2) {
            this.descriptions[i2] = AccessDescription.getInstance(aSN1Sequence.getObjectAt(i2));
        }
    }

    public AuthorityInformationAccess(AccessDescription accessDescription) {
        this.descriptions = new AccessDescription[]{accessDescription};
    }

    public static AuthorityInformationAccess getInstance(Object object) {
        if (object instanceof AuthorityInformationAccess) {
            return (AuthorityInformationAccess)object;
        }
        if (object != null) {
            return new AuthorityInformationAccess(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public AccessDescription[] getAccessDescriptions() {
        return this.descriptions;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 != this.descriptions.length; ++i2) {
            aSN1EncodableVector.add(this.descriptions[i2]);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "AuthorityInformationAccess: Oid(" + this.descriptions[0].getAccessMethod().getId() + ")";
    }
}

