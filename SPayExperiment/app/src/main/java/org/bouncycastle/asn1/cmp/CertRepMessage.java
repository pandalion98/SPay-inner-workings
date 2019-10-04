/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.CMPCertificate;
import org.bouncycastle.asn1.cmp.CertResponse;

public class CertRepMessage
extends ASN1Object {
    private ASN1Sequence caPubs;
    private ASN1Sequence response;

    private CertRepMessage(ASN1Sequence aSN1Sequence) {
        int n2 = aSN1Sequence.size();
        int n3 = 0;
        if (n2 > 1) {
            this.caPubs = ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(0), true);
            n3 = 1;
        }
        this.response = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(n3));
    }

    public CertRepMessage(CMPCertificate[] arrcMPCertificate, CertResponse[] arrcertResponse) {
        int n2 = 0;
        if (arrcertResponse == null) {
            throw new IllegalArgumentException("'response' cannot be null");
        }
        if (arrcMPCertificate != null) {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            for (int i2 = 0; i2 < arrcMPCertificate.length; ++i2) {
                aSN1EncodableVector.add(arrcMPCertificate[i2]);
            }
            this.caPubs = new DERSequence(aSN1EncodableVector);
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        while (n2 < arrcertResponse.length) {
            aSN1EncodableVector.add(arrcertResponse[n2]);
            ++n2;
        }
        this.response = new DERSequence(aSN1EncodableVector);
    }

    public static CertRepMessage getInstance(Object object) {
        if (object instanceof CertRepMessage) {
            return (CertRepMessage)object;
        }
        if (object != null) {
            return new CertRepMessage(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CMPCertificate[] getCaPubs() {
        if (this.caPubs == null) {
            return null;
        }
        CMPCertificate[] arrcMPCertificate = new CMPCertificate[this.caPubs.size()];
        for (int i2 = 0; i2 != arrcMPCertificate.length; ++i2) {
            arrcMPCertificate[i2] = CMPCertificate.getInstance(this.caPubs.getObjectAt(i2));
        }
        return arrcMPCertificate;
    }

    public CertResponse[] getResponse() {
        CertResponse[] arrcertResponse = new CertResponse[this.response.size()];
        for (int i2 = 0; i2 != arrcertResponse.length; ++i2) {
            arrcertResponse[i2] = CertResponse.getInstance(this.response.getObjectAt(i2));
        }
        return arrcertResponse;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.caPubs != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.caPubs));
        }
        aSN1EncodableVector.add(this.response);
        return new DERSequence(aSN1EncodableVector);
    }
}

