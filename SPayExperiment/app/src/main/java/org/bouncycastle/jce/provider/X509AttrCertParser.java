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
 *  java.util.ArrayList
 *  java.util.Collection
 */
package org.bouncycastle.jce.provider;

import java.io.BufferedInputStream;
import java.io.InputStream;
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
import org.bouncycastle.jce.provider.PEMUtil;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509StreamParserSpi;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import org.bouncycastle.x509.util.StreamParsingException;

public class X509AttrCertParser
extends X509StreamParserSpi {
    private static final PEMUtil PEM_PARSER = new PEMUtil("ATTRIBUTE CERTIFICATE");
    private InputStream currentStream = null;
    private ASN1Set sData = null;
    private int sDataObjectCount = 0;

    private X509AttributeCertificate getCertificate() {
        if (this.sData != null) {
            while (this.sDataObjectCount < this.sData.size()) {
                ASN1Set aSN1Set = this.sData;
                int n = this.sDataObjectCount;
                this.sDataObjectCount = n + 1;
                ASN1Encodable aSN1Encodable = aSN1Set.getObjectAt(n);
                if (!(aSN1Encodable instanceof ASN1TaggedObject) || ((ASN1TaggedObject)aSN1Encodable).getTagNo() != 2) continue;
                return new X509V2AttributeCertificate(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Encodable, false).getEncoded());
            }
        }
        return null;
    }

    private X509AttributeCertificate readDERCertificate(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)new ASN1InputStream(inputStream).readObject();
        if (aSN1Sequence.size() > 1 && aSN1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && aSN1Sequence.getObjectAt(0).equals((Object)PKCSObjectIdentifiers.signedData)) {
            this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)aSN1Sequence.getObjectAt(1), true)).getCertificates();
            return this.getCertificate();
        }
        return new X509V2AttributeCertificate(aSN1Sequence.getEncoded());
    }

    private X509AttributeCertificate readPEMCertificate(InputStream inputStream) {
        ASN1Sequence aSN1Sequence = PEM_PARSER.readPEMObject(inputStream);
        if (aSN1Sequence != null) {
            return new X509V2AttributeCertificate(aSN1Sequence.getEncoded());
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
                    return this.getCertificate();
                }
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
            this.currentStream.mark(10);
            int n = this.currentStream.read();
            X509AttributeCertificate x509AttributeCertificate = null;
            if (n == -1) return x509AttributeCertificate;
            if (n != 48) {
                this.currentStream.reset();
                return this.readPEMCertificate(this.currentStream);
            }
        }
        catch (Exception exception) {
            throw new StreamParsingException(exception.toString(), exception);
        }
        this.currentStream.reset();
        X509AttributeCertificate x509AttributeCertificate = this.readDERCertificate(this.currentStream);
        return x509AttributeCertificate;
    }

    @Override
    public Collection engineReadAll() {
        X509AttributeCertificate x509AttributeCertificate;
        ArrayList arrayList = new ArrayList();
        while ((x509AttributeCertificate = (X509AttributeCertificate)this.engineRead()) != null) {
            arrayList.add((Object)x509AttributeCertificate);
        }
        return arrayList;
    }
}

