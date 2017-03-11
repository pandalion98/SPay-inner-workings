package org.bouncycastle.asn1.cmp;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Certificate;

public class CMPCertificate extends ASN1Object implements ASN1Choice {
    private ASN1Object otherCert;
    private int otherTagValue;
    private Certificate x509v3PKCert;

    public CMPCertificate(int i, ASN1Object aSN1Object) {
        this.otherTagValue = i;
        this.otherCert = aSN1Object;
    }

    public CMPCertificate(AttributeCertificate attributeCertificate) {
        this(1, attributeCertificate);
    }

    public CMPCertificate(Certificate certificate) {
        if (certificate.getVersionNumber() != 3) {
            throw new IllegalArgumentException("only version 3 certificates allowed");
        }
        this.x509v3PKCert = certificate;
    }

    public static CMPCertificate getInstance(Object obj) {
        if (obj == null || (obj instanceof CMPCertificate)) {
            return (CMPCertificate) obj;
        }
        if (obj instanceof byte[]) {
            try {
                Object fromByteArray = ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (IOException e) {
                throw new IllegalArgumentException("Invalid encoding in CMPCertificate");
            }
        }
        fromByteArray = obj;
        if (fromByteArray instanceof ASN1Sequence) {
            return new CMPCertificate(Certificate.getInstance(fromByteArray));
        }
        if (fromByteArray instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) fromByteArray;
            return new CMPCertificate(aSN1TaggedObject.getTagNo(), aSN1TaggedObject.getObject());
        }
        throw new IllegalArgumentException("Invalid object: " + fromByteArray.getClass().getName());
    }

    public ASN1Object getOtherCert() {
        return this.otherCert;
    }

    public int getOtherCertTag() {
        return this.otherTagValue;
    }

    public AttributeCertificate getX509v2AttrCert() {
        return AttributeCertificate.getInstance(this.otherCert);
    }

    public Certificate getX509v3PKCert() {
        return this.x509v3PKCert;
    }

    public boolean isX509v3PKCert() {
        return this.x509v3PKCert != null;
    }

    public ASN1Primitive toASN1Primitive() {
        return this.otherCert != null ? new DERTaggedObject(true, this.otherTagValue, this.otherCert) : this.x509v3PKCert.toASN1Primitive();
    }
}
