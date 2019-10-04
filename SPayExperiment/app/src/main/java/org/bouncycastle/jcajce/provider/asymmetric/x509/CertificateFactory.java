/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.PushbackInputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CRL
 *  java.security.cert.CRLException
 *  java.security.cert.CertPath
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateFactorySpi
 *  java.security.cert.X509Certificate
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactorySpi;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.jcajce.provider.asymmetric.x509.PEMUtil;
import org.bouncycastle.jcajce.provider.asymmetric.x509.PKIXCertPath;
import org.bouncycastle.jcajce.provider.asymmetric.x509.X509CRLObject;
import org.bouncycastle.jcajce.provider.asymmetric.x509.X509CertificateObject;

public class CertificateFactory
extends CertificateFactorySpi {
    private static final PEMUtil PEM_CERT_PARSER = new PEMUtil("CERTIFICATE");
    private static final PEMUtil PEM_CRL_PARSER = new PEMUtil("CRL");
    private InputStream currentCrlStream = null;
    private InputStream currentStream = null;
    private ASN1Set sCrlData = null;
    private int sCrlDataObjectCount = 0;
    private ASN1Set sData = null;
    private int sDataObjectCount = 0;

    private CRL getCRL() {
        if (this.sCrlData == null || this.sCrlDataObjectCount >= this.sCrlData.size()) {
            return null;
        }
        ASN1Set aSN1Set = this.sCrlData;
        int n = this.sCrlDataObjectCount;
        this.sCrlDataObjectCount = n + 1;
        return this.createCRL(CertificateList.getInstance(aSN1Set.getObjectAt(n)));
    }

    private java.security.cert.Certificate getCertificate() {
        if (this.sData != null) {
            while (this.sDataObjectCount < this.sData.size()) {
                ASN1Set aSN1Set = this.sData;
                int n = this.sDataObjectCount;
                this.sDataObjectCount = n + 1;
                ASN1Encodable aSN1Encodable = aSN1Set.getObjectAt(n);
                if (!(aSN1Encodable instanceof ASN1Sequence)) continue;
                return new X509CertificateObject(Certificate.getInstance(aSN1Encodable));
            }
        }
        return null;
    }

    private CRL readDERCRL(ASN1InputStream aSN1InputStream) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1InputStream.readObject();
        if (aSN1Sequence.size() > 1 && aSN1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && aSN1Sequence.getObjectAt(0).equals((Object)PKCSObjectIdentifiers.signedData)) {
            this.sCrlData = SignedData.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(1), true)).getCRLs();
            return this.getCRL();
        }
        return this.createCRL(CertificateList.getInstance(aSN1Sequence));
    }

    private java.security.cert.Certificate readDERCertificate(ASN1InputStream aSN1InputStream) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1InputStream.readObject();
        if (aSN1Sequence.size() > 1 && aSN1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && aSN1Sequence.getObjectAt(0).equals((Object)PKCSObjectIdentifiers.signedData)) {
            this.sData = SignedData.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(1), true)).getCertificates();
            return this.getCertificate();
        }
        return new X509CertificateObject(Certificate.getInstance(aSN1Sequence));
    }

    private CRL readPEMCRL(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = PEM_CRL_PARSER.readPEMObject(inputStream);
        if (aSN1Sequence != null) {
            return this.createCRL(CertificateList.getInstance(aSN1Sequence));
        }
        return null;
    }

    private java.security.cert.Certificate readPEMCertificate(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = PEM_CERT_PARSER.readPEMObject(inputStream);
        if (aSN1Sequence != null) {
            return new X509CertificateObject(Certificate.getInstance(aSN1Sequence));
        }
        return null;
    }

    protected CRL createCRL(CertificateList certificateList) {
        return new X509CRLObject(certificateList);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CRL engineGenerateCRL(InputStream inputStream) {
        if (this.currentCrlStream == null) {
            this.currentCrlStream = inputStream;
            this.sCrlData = null;
            this.sCrlDataObjectCount = 0;
        } else if (this.currentCrlStream != inputStream) {
            this.currentCrlStream = inputStream;
            this.sCrlData = null;
            this.sCrlDataObjectCount = 0;
        }
        try {
            if (this.sCrlData != null) {
                if (this.sCrlDataObjectCount != this.sCrlData.size()) {
                    CRL cRL = this.getCRL();
                    return cRL;
                }
                this.sCrlData = null;
                this.sCrlDataObjectCount = 0;
                return null;
            }
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            int n = pushbackInputStream.read();
            CRL cRL = null;
            if (n == -1) return cRL;
            pushbackInputStream.unread(n);
            if (n == 48) return this.readDERCRL(new ASN1InputStream((InputStream)pushbackInputStream, true));
            return this.readPEMCRL((InputStream)pushbackInputStream);
        }
        catch (CRLException cRLException) {
            throw cRLException;
        }
        catch (Exception exception) {
            throw new CRLException(exception.toString());
        }
    }

    public Collection engineGenerateCRLs(InputStream inputStream) {
        CRL cRL;
        ArrayList arrayList = new ArrayList();
        while ((cRL = this.engineGenerateCRL(inputStream)) != null) {
            arrayList.add((Object)cRL);
        }
        return arrayList;
    }

    public CertPath engineGenerateCertPath(InputStream inputStream) {
        return this.engineGenerateCertPath(inputStream, "PkiPath");
    }

    public CertPath engineGenerateCertPath(InputStream inputStream, String string) {
        return new PKIXCertPath(inputStream, string);
    }

    public CertPath engineGenerateCertPath(List list) {
        for (Object object : list) {
            if (object == null || object instanceof X509Certificate) continue;
            throw new CertificateException("list contains non X509Certificate object while creating CertPath\n" + object.toString());
        }
        return new PKIXCertPath(list);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public java.security.cert.Certificate engineGenerateCertificate(InputStream inputStream) {
        if (this.currentStream == null) {
            this.currentStream = inputStream;
            this.sData = null;
            this.sDataObjectCount = 0;
        } else if (this.currentStream != inputStream) {
            this.currentStream = inputStream;
            this.sData = null;
            this.sDataObjectCount = 0;
        }
        try {
            if (this.sData != null) {
                if (this.sDataObjectCount != this.sData.size()) {
                    java.security.cert.Certificate certificate = this.getCertificate();
                    return certificate;
                }
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            int n = pushbackInputStream.read();
            java.security.cert.Certificate certificate = null;
            if (n == -1) return certificate;
            pushbackInputStream.unread(n);
            if (n == 48) return this.readDERCertificate(new ASN1InputStream((InputStream)pushbackInputStream));
            return this.readPEMCertificate((InputStream)pushbackInputStream);
        }
        catch (Exception exception) {
            throw new ExCertificateException(exception);
        }
    }

    public Collection engineGenerateCertificates(InputStream inputStream) {
        java.security.cert.Certificate certificate;
        ArrayList arrayList = new ArrayList();
        while ((certificate = this.engineGenerateCertificate(inputStream)) != null) {
            arrayList.add((Object)certificate);
        }
        return arrayList;
    }

    public Iterator engineGetCertPathEncodings() {
        return PKIXCertPath.certPathEncodings.iterator();
    }

    private class ExCertificateException
    extends CertificateException {
        private Throwable cause;

        public ExCertificateException(String string, Throwable throwable) {
            super(string);
            this.cause = throwable;
        }

        public ExCertificateException(Throwable throwable) {
            this.cause = throwable;
        }

        public Throwable getCause() {
            return this.cause;
        }
    }

}

