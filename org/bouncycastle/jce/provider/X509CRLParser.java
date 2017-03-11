package org.bouncycastle.jce.provider;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.cert.CRL;
import java.util.ArrayList;
import java.util.Collection;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.x509.X509StreamParserSpi;
import org.bouncycastle.x509.util.StreamParsingException;

public class X509CRLParser extends X509StreamParserSpi {
    private static final PEMUtil PEM_PARSER;
    private InputStream currentStream;
    private ASN1Set sData;
    private int sDataObjectCount;

    static {
        PEM_PARSER = new PEMUtil("CRL");
    }

    public X509CRLParser() {
        this.sData = null;
        this.sDataObjectCount = 0;
        this.currentStream = null;
    }

    private CRL getCRL() {
        if (this.sData == null || this.sDataObjectCount >= this.sData.size()) {
            return null;
        }
        ASN1Set aSN1Set = this.sData;
        int i = this.sDataObjectCount;
        this.sDataObjectCount = i + 1;
        return new X509CRLObject(CertificateList.getInstance(aSN1Set.getObjectAt(i)));
    }

    private CRL readDERCRL(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence) new ASN1InputStream(inputStream).readObject();
        if (aSN1Sequence.size() <= 1 || !(aSN1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier) || !aSN1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            return new X509CRLObject(CertificateList.getInstance(aSN1Sequence));
        }
        this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(1), true)).getCRLs();
        return getCRL();
    }

    private CRL readPEMCRL(InputStream inputStream) {
        ASN1Sequence readPEMObject = PEM_PARSER.readPEMObject(inputStream);
        return readPEMObject != null ? new X509CRLObject(CertificateList.getInstance(readPEMObject)) : null;
    }

    public void engineInit(InputStream inputStream) {
        this.currentStream = inputStream;
        this.sData = null;
        this.sDataObjectCount = 0;
        if (!this.currentStream.markSupported()) {
            this.currentStream = new BufferedInputStream(this.currentStream);
        }
    }

    public Object engineRead() {
        try {
            if (this.sData == null) {
                this.currentStream.mark(10);
                int read = this.currentStream.read();
                if (read == -1) {
                    return null;
                }
                if (read != 48) {
                    this.currentStream.reset();
                    return readPEMCRL(this.currentStream);
                }
                this.currentStream.reset();
                return readDERCRL(this.currentStream);
            } else if (this.sDataObjectCount != this.sData.size()) {
                return getCRL();
            } else {
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
        } catch (Throwable e) {
            throw new StreamParsingException(e.toString(), e);
        }
    }

    public Collection engineReadAll() {
        Collection arrayList = new ArrayList();
        while (true) {
            CRL crl = (CRL) engineRead();
            if (crl == null) {
                return arrayList;
            }
            arrayList.add(crl);
        }
    }
}
