/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.crmf.POPOSigningKeyInput;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class POPOSigningKey
extends ASN1Object {
    private AlgorithmIdentifier algorithmIdentifier;
    private POPOSigningKeyInput poposkInput;
    private DERBitString signature;

    private POPOSigningKey(ASN1Sequence aSN1Sequence) {
        boolean bl = aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject;
        int n2 = 0;
        if (bl) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Sequence.getObjectAt(0);
            if (aSN1TaggedObject.getTagNo() != 0) {
                throw new IllegalArgumentException("Unknown POPOSigningKeyInput tag: " + aSN1TaggedObject.getTagNo());
            }
            this.poposkInput = POPOSigningKeyInput.getInstance(aSN1TaggedObject.getObject());
            n2 = 1;
        }
        int n3 = n2 + 1;
        this.algorithmIdentifier = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n2));
        this.signature = DERBitString.getInstance(aSN1Sequence.getObjectAt(n3));
    }

    public POPOSigningKey(POPOSigningKeyInput pOPOSigningKeyInput, AlgorithmIdentifier algorithmIdentifier, DERBitString dERBitString) {
        this.poposkInput = pOPOSigningKeyInput;
        this.algorithmIdentifier = algorithmIdentifier;
        this.signature = dERBitString;
    }

    public static POPOSigningKey getInstance(Object object) {
        if (object instanceof POPOSigningKey) {
            return (POPOSigningKey)object;
        }
        if (object != null) {
            return new POPOSigningKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static POPOSigningKey getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return POPOSigningKey.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public AlgorithmIdentifier getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }

    public POPOSigningKeyInput getPoposkInput() {
        return this.poposkInput;
    }

    public DERBitString getSignature() {
        return this.signature;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.poposkInput != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.poposkInput));
        }
        aSN1EncodableVector.add(this.algorithmIdentifier);
        aSN1EncodableVector.add(this.signature);
        return new DERSequence(aSN1EncodableVector);
    }
}

