/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.OriginatorIdentifierOrKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class KeyAgreeRecipientInfo
extends ASN1Object {
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private OriginatorIdentifierOrKey originator;
    private ASN1Sequence recipientEncryptedKeys;
    private ASN1OctetString ukm;
    private ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    public KeyAgreeRecipientInfo(ASN1Sequence aSN1Sequence) {
        int n2;
        this.version = (ASN1Integer)aSN1Sequence.getObjectAt(0);
        this.originator = OriginatorIdentifierOrKey.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(1), true);
        if (aSN1Sequence.getObjectAt(2) instanceof ASN1TaggedObject) {
            this.ukm = ASN1OctetString.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(2), true);
            n2 = 3;
        } else {
            n2 = 2;
        }
        int n3 = n2 + 1;
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n2));
        n3 + 1;
        this.recipientEncryptedKeys = (ASN1Sequence)aSN1Sequence.getObjectAt(n3);
    }

    public KeyAgreeRecipientInfo(OriginatorIdentifierOrKey originatorIdentifierOrKey, ASN1OctetString aSN1OctetString, AlgorithmIdentifier algorithmIdentifier, ASN1Sequence aSN1Sequence) {
        this.version = new ASN1Integer(3L);
        this.originator = originatorIdentifierOrKey;
        this.ukm = aSN1OctetString;
        this.keyEncryptionAlgorithm = algorithmIdentifier;
        this.recipientEncryptedKeys = aSN1Sequence;
    }

    public static KeyAgreeRecipientInfo getInstance(Object object) {
        if (object instanceof KeyAgreeRecipientInfo) {
            return (KeyAgreeRecipientInfo)object;
        }
        if (object != null) {
            return new KeyAgreeRecipientInfo(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static KeyAgreeRecipientInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return KeyAgreeRecipientInfo.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }

    public OriginatorIdentifierOrKey getOriginator() {
        return this.originator;
    }

    public ASN1Sequence getRecipientEncryptedKeys() {
        return this.recipientEncryptedKeys;
    }

    public ASN1OctetString getUserKeyingMaterial() {
        return this.ukm;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.originator));
        if (this.ukm != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.ukm));
        }
        aSN1EncodableVector.add(this.keyEncryptionAlgorithm);
        aSN1EncodableVector.add(this.recipientEncryptedKeys);
        return new DERSequence(aSN1EncodableVector);
    }
}

