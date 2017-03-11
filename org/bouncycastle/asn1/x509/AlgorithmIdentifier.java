package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSequence;

public class AlgorithmIdentifier extends ASN1Object {
    private ASN1ObjectIdentifier objectId;
    private ASN1Encodable parameters;
    private boolean parametersDefined;

    public AlgorithmIdentifier(String str) {
        this.parametersDefined = false;
        this.objectId = new ASN1ObjectIdentifier(str);
    }

    public AlgorithmIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.parametersDefined = false;
        this.objectId = aSN1ObjectIdentifier;
    }

    public AlgorithmIdentifier(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.parametersDefined = false;
        this.parametersDefined = true;
        this.objectId = aSN1ObjectIdentifier;
        this.parameters = aSN1Encodable;
    }

    public AlgorithmIdentifier(ASN1Sequence aSN1Sequence) {
        this.parametersDefined = false;
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.objectId = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() == 2) {
            this.parametersDefined = true;
            this.parameters = aSN1Sequence.getObjectAt(1);
            return;
        }
        this.parameters = null;
    }

    public static AlgorithmIdentifier getInstance(Object obj) {
        return (obj == null || (obj instanceof AlgorithmIdentifier)) ? (AlgorithmIdentifier) obj : obj instanceof ASN1ObjectIdentifier ? new AlgorithmIdentifier((ASN1ObjectIdentifier) obj) : obj instanceof String ? new AlgorithmIdentifier((String) obj) : new AlgorithmIdentifier(ASN1Sequence.getInstance(obj));
    }

    public static AlgorithmIdentifier getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public ASN1ObjectIdentifier getAlgorithm() {
        return new ASN1ObjectIdentifier(this.objectId.getId());
    }

    public ASN1ObjectIdentifier getObjectId() {
        return this.objectId;
    }

    public ASN1Encodable getParameters() {
        return this.parameters;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.objectId);
        if (this.parametersDefined) {
            if (this.parameters != null) {
                aSN1EncodableVector.add(this.parameters);
            } else {
                aSN1EncodableVector.add(DERNull.INSTANCE);
            }
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
