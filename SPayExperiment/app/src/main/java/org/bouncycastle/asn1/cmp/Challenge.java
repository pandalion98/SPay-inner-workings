/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class Challenge
extends ASN1Object {
    private ASN1OctetString challenge;
    private AlgorithmIdentifier owf;
    private ASN1OctetString witness;

    /*
     * Enabled aggressive block sorting
     */
    private Challenge(ASN1Sequence aSN1Sequence) {
        int n2;
        if (aSN1Sequence.size() == 3) {
            n2 = 1;
            this.owf = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            n2 = 0;
        }
        int n3 = n2 + 1;
        this.witness = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n2));
        this.challenge = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(n3));
    }

    public Challenge(AlgorithmIdentifier algorithmIdentifier, byte[] arrby, byte[] arrby2) {
        this.owf = algorithmIdentifier;
        this.witness = new DEROctetString(arrby);
        this.challenge = new DEROctetString(arrby2);
    }

    public Challenge(byte[] arrby, byte[] arrby2) {
        this(null, arrby, arrby2);
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(aSN1Encodable);
        }
    }

    public static Challenge getInstance(Object object) {
        if (object instanceof Challenge) {
            return (Challenge)object;
        }
        if (object != null) {
            return new Challenge(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public byte[] getChallenge() {
        return this.challenge.getOctets();
    }

    public AlgorithmIdentifier getOwf() {
        return this.owf;
    }

    public byte[] getWitness() {
        return this.witness.getOctets();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        this.addOptional(aSN1EncodableVector, this.owf);
        aSN1EncodableVector.add(this.witness);
        aSN1EncodableVector.add(this.challenge);
        return new DERSequence(aSN1EncodableVector);
    }
}

