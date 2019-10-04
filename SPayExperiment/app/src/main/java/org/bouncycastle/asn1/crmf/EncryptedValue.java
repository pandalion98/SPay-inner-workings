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
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptedValue
extends ASN1Object {
    private DERBitString encSymmKey;
    private DERBitString encValue;
    private AlgorithmIdentifier intendedAlg;
    private AlgorithmIdentifier keyAlg;
    private AlgorithmIdentifier symmAlg;
    private ASN1OctetString valueHint;

    /*
     * Enabled aggressive block sorting
     */
    private EncryptedValue(ASN1Sequence aSN1Sequence) {
        int n2 = 0;
        do {
            if (!(aSN1Sequence.getObjectAt(n2) instanceof ASN1TaggedObject)) {
                this.encValue = DERBitString.getInstance(aSN1Sequence.getObjectAt(n2));
                return;
            }
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Sequence.getObjectAt(n2);
            switch (aSN1TaggedObject.getTagNo()) {
                case 0: {
                    this.intendedAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 1: {
                    this.symmAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 2: {
                    this.encSymmKey = DERBitString.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 3: {
                    this.keyAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                    break;
                }
                case 4: {
                    this.valueHint = ASN1OctetString.getInstance(aSN1TaggedObject, false);
                    break;
                }
            }
            ++n2;
        } while (true);
    }

    public EncryptedValue(AlgorithmIdentifier algorithmIdentifier, AlgorithmIdentifier algorithmIdentifier2, DERBitString dERBitString, AlgorithmIdentifier algorithmIdentifier3, ASN1OctetString aSN1OctetString, DERBitString dERBitString2) {
        if (dERBitString2 == null) {
            throw new IllegalArgumentException("'encValue' cannot be null");
        }
        this.intendedAlg = algorithmIdentifier;
        this.symmAlg = algorithmIdentifier2;
        this.encSymmKey = dERBitString;
        this.keyAlg = algorithmIdentifier3;
        this.valueHint = aSN1OctetString;
        this.encValue = dERBitString2;
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int n2, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, n2, aSN1Encodable));
        }
    }

    public static EncryptedValue getInstance(Object object) {
        if (object instanceof EncryptedValue) {
            return (EncryptedValue)object;
        }
        if (object != null) {
            return new EncryptedValue(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public DERBitString getEncSymmKey() {
        return this.encSymmKey;
    }

    public DERBitString getEncValue() {
        return this.encValue;
    }

    public AlgorithmIdentifier getIntendedAlg() {
        return this.intendedAlg;
    }

    public AlgorithmIdentifier getKeyAlg() {
        return this.keyAlg;
    }

    public AlgorithmIdentifier getSymmAlg() {
        return this.symmAlg;
    }

    public ASN1OctetString getValueHint() {
        return this.valueHint;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        this.addOptional(aSN1EncodableVector, 0, this.intendedAlg);
        this.addOptional(aSN1EncodableVector, 1, this.symmAlg);
        this.addOptional(aSN1EncodableVector, 2, this.encSymmKey);
        this.addOptional(aSN1EncodableVector, 3, this.keyAlg);
        this.addOptional(aSN1EncodableVector, 4, this.valueHint);
        aSN1EncodableVector.add(this.encValue);
        return new DERSequence(aSN1EncodableVector);
    }
}

