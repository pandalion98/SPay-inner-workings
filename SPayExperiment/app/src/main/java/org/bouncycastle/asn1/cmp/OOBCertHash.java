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
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.crmf.CertId;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class OOBCertHash
extends ASN1Object {
    private CertId certId;
    private AlgorithmIdentifier hashAlg;
    private DERBitString hashVal;

    /*
     * Enabled aggressive block sorting
     */
    private OOBCertHash(ASN1Sequence aSN1Sequence) {
        int n2 = -1 + aSN1Sequence.size();
        int n3 = n2 - 1;
        this.hashVal = DERBitString.getInstance(aSN1Sequence.getObjectAt(n2));
        int n4 = n3;
        while (n4 >= 0) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Sequence.getObjectAt(n4);
            if (aSN1TaggedObject.getTagNo() == 0) {
                this.hashAlg = AlgorithmIdentifier.getInstance(aSN1TaggedObject, true);
            } else {
                this.certId = CertId.getInstance(aSN1TaggedObject, true);
            }
            --n4;
        }
        return;
    }

    public OOBCertHash(AlgorithmIdentifier algorithmIdentifier, CertId certId, DERBitString dERBitString) {
        this.hashAlg = algorithmIdentifier;
        this.certId = certId;
        this.hashVal = dERBitString;
    }

    public OOBCertHash(AlgorithmIdentifier algorithmIdentifier, CertId certId, byte[] arrby) {
        this(algorithmIdentifier, certId, new DERBitString(arrby));
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int n2, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, n2, aSN1Encodable));
        }
    }

    public static OOBCertHash getInstance(Object object) {
        if (object instanceof OOBCertHash) {
            return (OOBCertHash)object;
        }
        if (object != null) {
            return new OOBCertHash(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CertId getCertId() {
        return this.certId;
    }

    public AlgorithmIdentifier getHashAlg() {
        return this.hashAlg;
    }

    public DERBitString getHashVal() {
        return this.hashVal;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        this.addOptional(aSN1EncodableVector, 0, this.hashAlg);
        this.addOptional(aSN1EncodableVector, 1, this.certId);
        aSN1EncodableVector.add(this.hashVal);
        return new DERSequence(aSN1EncodableVector);
    }
}

