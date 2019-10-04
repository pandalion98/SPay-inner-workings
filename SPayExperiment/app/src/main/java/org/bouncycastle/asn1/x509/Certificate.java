/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.Time;

public class Certificate
extends ASN1Object {
    ASN1Sequence seq;
    DERBitString sig;
    AlgorithmIdentifier sigAlgId;
    TBSCertificate tbsCert;

    private Certificate(ASN1Sequence aSN1Sequence) {
        this.seq = aSN1Sequence;
        if (aSN1Sequence.size() == 3) {
            this.tbsCert = TBSCertificate.getInstance(aSN1Sequence.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
            this.sig = DERBitString.getInstance(aSN1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for a certificate");
    }

    public static Certificate getInstance(Object object) {
        if (object instanceof Certificate) {
            return (Certificate)object;
        }
        if (object != null) {
            return new Certificate(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static Certificate getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return Certificate.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public Time getEndDate() {
        return this.tbsCert.getEndDate();
    }

    public X500Name getIssuer() {
        return this.tbsCert.getIssuer();
    }

    public ASN1Integer getSerialNumber() {
        return this.tbsCert.getSerialNumber();
    }

    public DERBitString getSignature() {
        return this.sig;
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }

    public Time getStartDate() {
        return this.tbsCert.getStartDate();
    }

    public X500Name getSubject() {
        return this.tbsCert.getSubject();
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.tbsCert.getSubjectPublicKeyInfo();
    }

    public TBSCertificate getTBSCertificate() {
        return this.tbsCert;
    }

    public ASN1Integer getVersion() {
        return this.tbsCert.getVersion();
    }

    public int getVersionNumber() {
        return this.tbsCert.getVersionNumber();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}

