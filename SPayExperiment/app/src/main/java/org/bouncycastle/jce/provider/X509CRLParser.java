/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.BufferedInputStream
 *  java.io.InputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.cert.CRL
 *  java.util.ArrayList
 *  java.util.Collection
 */
package org.bouncycastle.jce.provider;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.cert.CRL;
import java.util.ArrayList;
import java.util.Collection;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.jce.provider.PEMUtil;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.x509.X509StreamParserSpi;
import org.bouncycastle.x509.util.StreamParsingException;

public class X509CRLParser
extends X509StreamParserSpi {
    private static final PEMUtil PEM_PARSER = new PEMUtil("CRL");
    private InputStream currentStream = null;
    private ASN1Set sData = null;
    private int sDataObjectCount = 0;

    private CRL getCRL() {
        if (this.sData == null || this.sDataObjectCount >= this.sData.size()) {
            return null;
        }
        ASN1Set aSN1Set = this.sData;
        int n = this.sDataObjectCount;
        this.sDataObjectCount = n + 1;
        return new X509CRLObject(CertificateList.getInstance(aSN1Set.getObjectAt(n)));
    }

    private CRL readDERCRL(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)new ASN1InputStream(inputStream).readObject();
        if (aSN1Sequence.size() > 1 && aSN1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && aSN1Sequence.getObjectAt(0).equals((Object)PKCSObjectIdentifiers.signedData)) {
            this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(1), true)).getCRLs();
            return this.getCRL();
        }
        return new X509CRLObject(CertificateList.getInstance(aSN1Sequence));
    }

    private CRL readPEMCRL(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = PEM_PARSER.readPEMObject(inputStream);
        if (aSN1Sequence != null) {
            return new X509CRLObject(CertificateList.getInstance(aSN1Sequence));
        }
        return null;
    }

    @Override
    public void engineInit(InputStream inputStream) {
        this.currentStream = inputStream;
        this.sData = null;
        this.sDataObjectCount = 0;
        if (!this.currentStream.markSupported()) {
            this.currentStream = new BufferedInputStream(this.currentStream);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Object engineRead() {
        try {
            if (this.sData != null) {
                if (this.sDataObjectCount != this.sData.size()) {
                    return this.getCRL();
                }
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
            this.currentStream.mark(10);
            int n = this.currentStream.read();
            CRL cRL = null;
            if (n == -1) return cRL;
            if (n != 48) {
                this.currentStream.reset();
                return this.readPEMCRL(this.currentStream);
            }
        }
        catch (Exception exception) {
            throw new StreamParsingException(exception.toString(), exception);
        }
        this.currentStream.reset();
        CRL cRL = this.readDERCRL(this.currentStream);
        return cRL;
    }

    @Override
    public Collection engineReadAll() {
        CRL cRL;
        ArrayList arrayList = new ArrayList();
        while ((cRL = (CRL)this.engineRead()) != null) {
            arrayList.add((Object)cRL);
        }
        return arrayList;
    }
}

