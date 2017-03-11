package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

public class AuthorityInformationAccess extends ASN1Object {
    private AccessDescription[] descriptions;

    public AuthorityInformationAccess(ASN1ObjectIdentifier aSN1ObjectIdentifier, GeneralName generalName) {
        this(new AccessDescription(aSN1ObjectIdentifier, generalName));
    }

    private AuthorityInformationAccess(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1) {
            throw new IllegalArgumentException("sequence may not be empty");
        }
        this.descriptions = new AccessDescription[aSN1Sequence.size()];
        for (int i = 0; i != aSN1Sequence.size(); i++) {
            this.descriptions[i] = AccessDescription.getInstance(aSN1Sequence.getObjectAt(i));
        }
    }

    public AuthorityInformationAccess(AccessDescription accessDescription) {
        this.descriptions = new AccessDescription[]{accessDescription};
    }

    public static AuthorityInformationAccess getInstance(Object obj) {
        return obj instanceof AuthorityInformationAccess ? (AuthorityInformationAccess) obj : obj != null ? new AuthorityInformationAccess(ASN1Sequence.getInstance(obj)) : null;
    }

    public AccessDescription[] getAccessDescriptions() {
        return this.descriptions;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != this.descriptions.length; i++) {
            aSN1EncodableVector.add(this.descriptions[i]);
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public String toString() {
        return "AuthorityInformationAccess: Oid(" + this.descriptions[0].getAccessMethod().getId() + ")";
    }
}
