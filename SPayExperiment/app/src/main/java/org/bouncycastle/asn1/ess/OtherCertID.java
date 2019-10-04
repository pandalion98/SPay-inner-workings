/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.x509.IssuerSerial;

public class OtherCertID
extends ASN1Object {
    private IssuerSerial issuerSerial;
    private ASN1Encodable otherCertHash;

    /*
     * Enabled aggressive block sorting
     */
    private OtherCertID(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.otherCertHash = aSN1Sequence.getObjectAt(0).toASN1Primitive() instanceof ASN1OctetString ? ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(0)) : DigestInfo.getInstance(aSN1Sequence.getObjectAt(0));
        if (aSN1Sequence.size() > 1) {
            this.issuerSerial = IssuerSerial.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public OtherCertID(AlgorithmIdentifier algorithmIdentifier, byte[] arrby) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, arrby);
    }

    public OtherCertID(AlgorithmIdentifier algorithmIdentifier, byte[] arrby, IssuerSerial issuerSerial) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, arrby);
        this.issuerSerial = issuerSerial;
    }

    public static OtherCertID getInstance(Object object) {
        if (object instanceof OtherCertID) {
            return (OtherCertID)object;
        }
        if (object != null) {
            return new OtherCertID(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public AlgorithmIdentifier getAlgorithmHash() {
        if (this.otherCertHash.toASN1Primitive() instanceof ASN1OctetString) {
            return new AlgorithmIdentifier("1.3.14.3.2.26");
        }
        return DigestInfo.getInstance(this.otherCertHash).getAlgorithmId();
    }

    public byte[] getCertHash() {
        if (this.otherCertHash.toASN1Primitive() instanceof ASN1OctetString) {
            return ((ASN1OctetString)this.otherCertHash.toASN1Primitive()).getOctets();
        }
        return DigestInfo.getInstance(this.otherCertHash).getDigest();
    }

    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.otherCertHash);
        if (this.issuerSerial != null) {
            aSN1EncodableVector.add(this.issuerSerial);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}

