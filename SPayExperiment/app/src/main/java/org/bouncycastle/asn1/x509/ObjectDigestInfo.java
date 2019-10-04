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
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class ObjectDigestInfo
extends ASN1Object {
    public static final int otherObjectDigest = 2;
    public static final int publicKey = 0;
    public static final int publicKeyCert = 1;
    AlgorithmIdentifier digestAlgorithm;
    ASN1Enumerated digestedObjectType;
    DERBitString objectDigest;
    ASN1ObjectIdentifier otherObjectTypeID;

    public ObjectDigestInfo(int n2, ASN1ObjectIdentifier aSN1ObjectIdentifier, AlgorithmIdentifier algorithmIdentifier, byte[] arrby) {
        this.digestedObjectType = new ASN1Enumerated(n2);
        if (n2 == 2) {
            this.otherObjectTypeID = aSN1ObjectIdentifier;
        }
        this.digestAlgorithm = algorithmIdentifier;
        this.objectDigest = new DERBitString(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    private ObjectDigestInfo(ASN1Sequence aSN1Sequence) {
        int n2 = 1;
        if (aSN1Sequence.size() > 4 || aSN1Sequence.size() < 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.digestedObjectType = ASN1Enumerated.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() == 4) {
            this.otherObjectTypeID = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(n2));
        } else {
            n2 = 0;
        }
        this.digestAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n2 + 1));
        this.objectDigest = DERBitString.getInstance(aSN1Sequence.getObjectAt(n2 + 2));
    }

    public static ObjectDigestInfo getInstance(Object object) {
        if (object instanceof ObjectDigestInfo) {
            return (ObjectDigestInfo)object;
        }
        if (object != null) {
            return new ObjectDigestInfo(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static ObjectDigestInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return ObjectDigestInfo.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public ASN1Enumerated getDigestedObjectType() {
        return this.digestedObjectType;
    }

    public DERBitString getObjectDigest() {
        return this.objectDigest;
    }

    public ASN1ObjectIdentifier getOtherObjectTypeID() {
        return this.otherObjectTypeID;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.digestedObjectType);
        if (this.otherObjectTypeID != null) {
            aSN1EncodableVector.add(this.otherObjectTypeID);
        }
        aSN1EncodableVector.add(this.digestAlgorithm);
        aSN1EncodableVector.add(this.objectDigest);
        return new DERSequence(aSN1EncodableVector);
    }
}

