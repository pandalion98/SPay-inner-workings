/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.Time;

public class TBSCertificate
extends ASN1Object {
    Time endDate;
    Extensions extensions;
    X500Name issuer;
    DERBitString issuerUniqueId;
    ASN1Sequence seq;
    ASN1Integer serialNumber;
    AlgorithmIdentifier signature;
    Time startDate;
    X500Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    DERBitString subjectUniqueId;
    ASN1Integer version;

    /*
     * Enabled aggressive block sorting
     */
    private TBSCertificate(ASN1Sequence aSN1Sequence) {
        int n2;
        this.seq = aSN1Sequence;
        if (aSN1Sequence.getObjectAt(0) instanceof DERTaggedObject) {
            this.version = ASN1Integer.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(0), true);
            n2 = 0;
        } else {
            this.version = new ASN1Integer(0L);
            n2 = -1;
        }
        this.serialNumber = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(n2 + 1));
        this.signature = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(n2 + 2));
        this.issuer = X500Name.getInstance(aSN1Sequence.getObjectAt(n2 + 3));
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(n2 + 4);
        this.startDate = Time.getInstance(aSN1Sequence2.getObjectAt(0));
        this.endDate = Time.getInstance(aSN1Sequence2.getObjectAt(1));
        this.subject = X500Name.getInstance(aSN1Sequence.getObjectAt(n2 + 5));
        this.subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(aSN1Sequence.getObjectAt(n2 + 6));
        int n3 = -1 + (aSN1Sequence.size() - (n2 + 6));
        while (n3 > 0) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject)aSN1Sequence.getObjectAt(n3 + (n2 + 6));
            switch (dERTaggedObject.getTagNo()) {
                case 1: {
                    this.issuerUniqueId = DERBitString.getInstance(dERTaggedObject, false);
                    break;
                }
                case 2: {
                    this.subjectUniqueId = DERBitString.getInstance(dERTaggedObject, false);
                    break;
                }
                case 3: {
                    this.extensions = Extensions.getInstance(ASN1Sequence.getInstance(dERTaggedObject, true));
                    break;
                }
            }
            --n3;
        }
        return;
    }

    public static TBSCertificate getInstance(Object object) {
        if (object instanceof TBSCertificate) {
            return (TBSCertificate)object;
        }
        if (object != null) {
            return new TBSCertificate(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public static TBSCertificate getInstance(ASN1TaggedObject aSN1TaggedObject, boolean bl) {
        return TBSCertificate.getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, bl));
    }

    public Time getEndDate() {
        return this.endDate;
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public X500Name getIssuer() {
        return this.issuer;
    }

    public DERBitString getIssuerUniqueId() {
        return this.issuerUniqueId;
    }

    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }

    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }

    public Time getStartDate() {
        return this.startDate;
    }

    public X500Name getSubject() {
        return this.subject;
    }

    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }

    public DERBitString getSubjectUniqueId() {
        return this.subjectUniqueId;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public int getVersionNumber() {
        return 1 + this.version.getValue().intValue();
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}

