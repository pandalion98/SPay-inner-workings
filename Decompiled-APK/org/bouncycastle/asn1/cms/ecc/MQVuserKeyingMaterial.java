package org.bouncycastle.asn1.cms.ecc;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.OriginatorPublicKey;

public class MQVuserKeyingMaterial extends ASN1Object {
    private ASN1OctetString addedukm;
    private OriginatorPublicKey ephemeralPublicKey;

    private MQVuserKeyingMaterial(ASN1Sequence aSN1Sequence) {
        this.ephemeralPublicKey = OriginatorPublicKey.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() > 1) {
            this.addedukm = ASN1OctetString.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(1), true);
        }
    }

    public MQVuserKeyingMaterial(OriginatorPublicKey originatorPublicKey, ASN1OctetString aSN1OctetString) {
        this.ephemeralPublicKey = originatorPublicKey;
        this.addedukm = aSN1OctetString;
    }

    public static MQVuserKeyingMaterial getInstance(Object obj) {
        if (obj == null || (obj instanceof MQVuserKeyingMaterial)) {
            return (MQVuserKeyingMaterial) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new MQVuserKeyingMaterial((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid MQVuserKeyingMaterial: " + obj.getClass().getName());
    }

    public static MQVuserKeyingMaterial getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public ASN1OctetString getAddedukm() {
        return this.addedukm;
    }

    public OriginatorPublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.ephemeralPublicKey);
        if (this.addedukm != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.addedukm));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
