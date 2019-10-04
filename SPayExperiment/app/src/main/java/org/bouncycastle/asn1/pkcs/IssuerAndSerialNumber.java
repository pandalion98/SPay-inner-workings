/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.asn1.pkcs;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.X509Name;

public class IssuerAndSerialNumber
extends ASN1Object {
    ASN1Integer certSerialNumber;
    X500Name name;

    private IssuerAndSerialNumber(ASN1Sequence aSN1Sequence) {
        this.name = X500Name.getInstance(aSN1Sequence.getObjectAt(0));
        this.certSerialNumber = (ASN1Integer)aSN1Sequence.getObjectAt(1);
    }

    public IssuerAndSerialNumber(X500Name x500Name, BigInteger bigInteger) {
        this.name = x500Name;
        this.certSerialNumber = new ASN1Integer(bigInteger);
    }

    public IssuerAndSerialNumber(X509Name x509Name, BigInteger bigInteger) {
        this.name = X500Name.getInstance(x509Name.toASN1Primitive());
        this.certSerialNumber = new ASN1Integer(bigInteger);
    }

    public IssuerAndSerialNumber(X509Name x509Name, ASN1Integer aSN1Integer) {
        this.name = X500Name.getInstance(x509Name.toASN1Primitive());
        this.certSerialNumber = aSN1Integer;
    }

    public static IssuerAndSerialNumber getInstance(Object object) {
        if (object instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber)object;
        }
        if (object != null) {
            return new IssuerAndSerialNumber(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public ASN1Integer getCertificateSerialNumber() {
        return this.certSerialNumber;
    }

    public X500Name getName() {
        return this.name;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.name);
        aSN1EncodableVector.add(this.certSerialNumber);
        return new DERSequence(aSN1EncodableVector);
    }
}

