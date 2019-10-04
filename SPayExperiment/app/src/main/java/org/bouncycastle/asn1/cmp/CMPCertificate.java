/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.cmp;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Certificate;

public class CMPCertificate
extends ASN1Object
implements ASN1Choice {
    private ASN1Object otherCert;
    private int otherTagValue;
    private Certificate x509v3PKCert;

    public CMPCertificate(int n2, ASN1Object aSN1Object) {
        this.otherTagValue = n2;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static CMPCertificate getInstance(Object object) {
        Object object2;
        if (object == null || object instanceof CMPCertificate) {
            return (CMPCertificate)object;
        }
        if (object instanceof byte[]) {
            try {
                ASN1Primitive aSN1Primitive = ASN1Primitive.fromByteArray((byte[])object);
                object2 = aSN1Primitive;
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException("Invalid encoding in CMPCertificate");
            }
        } else {
            object2 = object;
        }
        if (object2 instanceof ASN1Sequence) {
            return new CMPCertificate(Certificate.getInstance(object2));
        }
        if (object2 instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)object2;
            return new CMPCertificate(aSN1TaggedObject.getTagNo(), aSN1TaggedObject.getObject());
        }
        throw new IllegalArgumentException("Invalid object: " + object2.getClass().getName());
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

    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.otherCert != null) {
            return new DERTaggedObject(true, this.otherTagValue, this.otherCert);
        }
        return this.x509v3PKCert.toASN1Primitive();
    }
}

