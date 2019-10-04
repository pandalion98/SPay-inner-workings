/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.X509Certificate
 */
package org.bouncycastle.x509;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.x509.ExtCertificateEncodingException;

public class X509CertificatePair {
    private X509Certificate forward;
    private X509Certificate reverse;

    public X509CertificatePair(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
        this.forward = x509Certificate;
        this.reverse = x509Certificate2;
    }

    public X509CertificatePair(CertificatePair certificatePair) {
        if (certificatePair.getForward() != null) {
            this.forward = new X509CertificateObject(certificatePair.getForward());
        }
        if (certificatePair.getReverse() != null) {
            this.reverse = new X509CertificateObject(certificatePair.getReverse());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        boolean bl = true;
        if (object == null) {
            return false;
        }
        if (!(object instanceof X509CertificatePair)) return false;
        X509CertificatePair x509CertificatePair = (X509CertificatePair)object;
        boolean bl2 = this.forward != null ? this.forward.equals((Object)x509CertificatePair.forward) : (x509CertificatePair.forward != null ? false : bl);
        boolean bl3 = this.reverse != null ? this.reverse.equals((Object)x509CertificatePair.reverse) : (x509CertificatePair.reverse != null ? false : bl);
        if (!bl2) return false;
        if (!bl3) return false;
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getEncoded() {
        Certificate certificate;
        Certificate certificate2;
        block8 : {
            if (this.forward != null) {
                certificate = Certificate.getInstance(new ASN1InputStream(this.forward.getEncoded()).readObject());
                if (certificate == null) {
                    throw new CertificateEncodingException("unable to get encoding for forward");
                }
            } else {
                certificate = null;
            }
            X509Certificate x509Certificate = this.reverse;
            certificate2 = null;
            if (x509Certificate == null || (certificate2 = Certificate.getInstance(new ASN1InputStream(this.reverse.getEncoded()).readObject())) != null) break block8;
            throw new CertificateEncodingException("unable to get encoding for reverse");
        }
        try {
            return new CertificatePair(certificate, certificate2).getEncoded("DER");
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new ExtCertificateEncodingException(illegalArgumentException.toString(), illegalArgumentException);
        }
        catch (IOException iOException) {
            throw new ExtCertificateEncodingException(iOException.toString(), iOException);
        }
    }

    public X509Certificate getForward() {
        return this.forward;
    }

    public X509Certificate getReverse() {
        return this.reverse;
    }

    public int hashCode() {
        int n = -1;
        if (this.forward != null) {
            n ^= this.forward.hashCode();
        }
        if (this.reverse != null) {
            n = n * 17 ^ this.reverse.hashCode();
        }
        return n;
    }
}

