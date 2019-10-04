/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.asn1.isismtt.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class CertHash
extends ASN1Object {
    private byte[] certificateHash;
    private AlgorithmIdentifier hashAlgorithm;

    private CertHash(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        this.certificateHash = DEROctetString.getInstance(aSN1Sequence.getObjectAt(1)).getOctets();
    }

    public CertHash(AlgorithmIdentifier algorithmIdentifier, byte[] arrby) {
        this.hashAlgorithm = algorithmIdentifier;
        this.certificateHash = new byte[arrby.length];
        System.arraycopy((Object)arrby, (int)0, (Object)this.certificateHash, (int)0, (int)arrby.length);
    }

    public static CertHash getInstance(Object object) {
        if (object == null || object instanceof CertHash) {
            return (CertHash)object;
        }
        if (object instanceof ASN1Sequence) {
            return new CertHash((ASN1Sequence)object);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + object.getClass().getName());
    }

    public byte[] getCertificateHash() {
        return this.certificateHash;
    }

    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.hashAlgorithm);
        aSN1EncodableVector.add(new DEROctetString(this.certificateHash));
        return new DERSequence(aSN1EncodableVector);
    }
}

