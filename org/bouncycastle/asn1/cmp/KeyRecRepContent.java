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
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class KeyRecRepContent extends ASN1Object {
    private ASN1Sequence caCerts;
    private ASN1Sequence keyPairHist;
    private CMPCertificate newSigCert;
    private PKIStatusInfo status;

    private KeyRecRepContent(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        this.status = PKIStatusInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            switch (instance.getTagNo()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    this.newSigCert = CMPCertificate.getInstance(instance.getObject());
                    break;
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.caCerts = ASN1Sequence.getInstance(instance.getObject());
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.keyPairHist = ASN1Sequence.getInstance(instance.getObject());
                    break;
                default:
                    throw new IllegalArgumentException("unknown tag number: " + instance.getTagNo());
            }
        }
    }

    private void addOptional(ASN1EncodableVector aSN1EncodableVector, int i, ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, i, aSN1Encodable));
        }
    }

    public static KeyRecRepContent getInstance(Object obj) {
        return obj instanceof KeyRecRepContent ? (KeyRecRepContent) obj : obj != null ? new KeyRecRepContent(ASN1Sequence.getInstance(obj)) : null;
    }

    public CMPCertificate[] getCaCerts() {
        if (this.caCerts == null) {
            return null;
        }
        CMPCertificate[] cMPCertificateArr = new CMPCertificate[this.caCerts.size()];
        for (int i = 0; i != cMPCertificateArr.length; i++) {
            cMPCertificateArr[i] = CMPCertificate.getInstance(this.caCerts.getObjectAt(i));
        }
        return cMPCertificateArr;
    }

    public CertifiedKeyPair[] getKeyPairHist() {
        if (this.keyPairHist == null) {
            return null;
        }
        CertifiedKeyPair[] certifiedKeyPairArr = new CertifiedKeyPair[this.keyPairHist.size()];
        for (int i = 0; i != certifiedKeyPairArr.length; i++) {
            certifiedKeyPairArr[i] = CertifiedKeyPair.getInstance(this.keyPairHist.getObjectAt(i));
        }
        return certifiedKeyPairArr;
    }

    public CMPCertificate getNewSigCert() {
        return this.newSigCert;
    }

    public PKIStatusInfo getStatus() {
        return this.status;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.status);
        addOptional(aSN1EncodableVector, 0, this.newSigCert);
        addOptional(aSN1EncodableVector, 1, this.caCerts);
        addOptional(aSN1EncodableVector, 2, this.keyPairHist);
        return new DERSequence(aSN1EncodableVector);
    }
}
