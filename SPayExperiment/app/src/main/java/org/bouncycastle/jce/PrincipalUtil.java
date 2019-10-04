/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.CRLException
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.X509CRL
 *  java.security.cert.X509Certificate
 */
package org.bouncycastle.jce;

import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;

public class PrincipalUtil {
    public static X509Principal getIssuerX509Principal(X509CRL x509CRL) {
        try {
            X509Principal x509Principal = new X509Principal(X509Name.getInstance(TBSCertList.getInstance(ASN1Primitive.fromByteArray(x509CRL.getTBSCertList())).getIssuer()));
            return x509Principal;
        }
        catch (IOException iOException) {
            throw new CRLException(iOException.toString());
        }
    }

    public static X509Principal getIssuerX509Principal(X509Certificate x509Certificate) {
        try {
            X509Principal x509Principal = new X509Principal(X509Name.getInstance(TBSCertificateStructure.getInstance(ASN1Primitive.fromByteArray(x509Certificate.getTBSCertificate())).getIssuer()));
            return x509Principal;
        }
        catch (IOException iOException) {
            throw new CertificateEncodingException(iOException.toString());
        }
    }

    public static X509Principal getSubjectX509Principal(X509Certificate x509Certificate) {
        try {
            X509Principal x509Principal = new X509Principal(X509Name.getInstance(TBSCertificateStructure.getInstance(ASN1Primitive.fromByteArray(x509Certificate.getTBSCertificate())).getSubject()));
            return x509Principal;
        }
        catch (IOException iOException) {
            throw new CertificateEncodingException(iOException.toString());
        }
    }
}

