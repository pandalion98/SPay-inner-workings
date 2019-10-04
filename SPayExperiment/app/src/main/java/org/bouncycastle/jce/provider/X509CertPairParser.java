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
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.CertificatePair;
import org.bouncycastle.x509.X509CertificatePair;
import org.bouncycastle.x509.X509StreamParserSpi;
import org.bouncycastle.x509.util.StreamParsingException;

public class X509CertPairParser
extends X509StreamParserSpi {
    private InputStream currentStream = null;

    private X509CertificatePair readDERCrossCertificatePair(InputStream inputStream) {
        return new X509CertificatePair(CertificatePair.getInstance((ASN1Sequence)new ASN1InputStream(inputStream).readObject()));
    }

    @Override
    public void engineInit(InputStream inputStream) {
        this.currentStream = inputStream;
        if (!this.currentStream.markSupported()) {
            this.currentStream = new BufferedInputStream(this.currentStream);
        }
    }

    @Override
    public Object engineRead() {
        block3 : {
            try {
                this.currentStream.mark(10);
                if (this.currentStream.read() != -1) break block3;
                return null;
            }
            catch (Exception exception) {
                throw new StreamParsingException(exception.toString(), exception);
            }
        }
        this.currentStream.reset();
        X509CertificatePair x509CertificatePair = this.readDERCrossCertificatePair(this.currentStream);
        return x509CertificatePair;
    }

    @Override
    public Collection engineReadAll() {
        X509CertificatePair x509CertificatePair;
        ArrayList arrayList = new ArrayList();
        while ((x509CertificatePair = (X509CertificatePair)this.engineRead()) != null) {
            arrayList.add((Object)x509CertificatePair);
        }
        return arrayList;
    }
}

