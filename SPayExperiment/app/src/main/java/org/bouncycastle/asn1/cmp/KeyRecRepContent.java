/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Enumeration
 */
package org.bouncycastle.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.CMPCertificate;
import org.bouncycastle.asn1.cmp.CertifiedKeyPair;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;

public class KeyRecRepContent
extends ASN1Object {
    private ASN1Sequence caCerts;
    private ASN1Sequence keyPairHist;
    private CMPCertificate newSigCert;
    private PKIStatusInfo status;

    private KeyRecRepContent(ASN1Sequence aSN1Sequence) {
        Enumeration enumeration = aSN1Sequence.getObjects();
        this.status = PKIStatusInfo.getInstance(enumeration.nextElement());
        block5 : while (enumeration.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(enumeration.nextElement());
            switch (aSN1TaggedObject.getTagNo()) {
                default: {
                    throw new IllegalArgumentException("unknown tag number: " + aSN1TaggedObject.getTagNo());
                }
                case 0: {
                    this.newSigCert = CMPCertificate.getInstance(aSN1TaggedObject.getObject());
                    continue block5;
                }
                case 1: {
                    this.caCerts = ASN1Sequence.getInstance(aSN1TaggedObject.getObject());
                    continue block5;
                }
                case 2: 
            }
            this.keyPairHist = ASN1Sequence.getInstance(aSN1TaggedObject.getObject());
        }
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int n2, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, n2, aSN1Encodable));
        }
    }

    public static KeyRecRepContent getInstance(Object object) {
        if (object instanceof KeyRecRepContent) {
            return (KeyRecRepContent)object;
        }
        if (object != null) {
            return new KeyRecRepContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public CMPCertificate[] getCaCerts() {
        if (this.caCerts == null) {
            return null;
        }
        CMPCertificate[] arrcMPCertificate = new CMPCertificate[this.caCerts.size()];
        for (int i2 = 0; i2 != arrcMPCertificate.length; ++i2) {
            arrcMPCertificate[i2] = CMPCertificate.getInstance(this.caCerts.getObjectAt(i2));
        }
        return arrcMPCertificate;
    }

    public CertifiedKeyPair[] getKeyPairHist() {
        if (this.keyPairHist == null) {
            return null;
        }
        CertifiedKeyPair[] arrcertifiedKeyPair = new CertifiedKeyPair[this.keyPairHist.size()];
        for (int i2 = 0; i2 != arrcertifiedKeyPair.length; ++i2) {
            arrcertifiedKeyPair[i2] = CertifiedKeyPair.getInstance(this.keyPairHist.getObjectAt(i2));
        }
        return arrcertifiedKeyPair;
    }

    public CMPCertificate getNewSigCert() {
        return this.newSigCert;
    }

    public PKIStatusInfo getStatus() {
        return this.status;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.status);
        this.addOptional(aSN1EncodableVector, 0, this.newSigCert);
        this.addOptional(aSN1EncodableVector, 1, this.caCerts);
        this.addOptional(aSN1EncodableVector, 2, this.keyPairHist);
        return new DERSequence(aSN1EncodableVector);
    }
}

