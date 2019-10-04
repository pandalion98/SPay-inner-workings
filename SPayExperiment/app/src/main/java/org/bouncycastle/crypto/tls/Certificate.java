/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.crypto.tls.TlsUtils;

public class Certificate {
    public static final Certificate EMPTY_CHAIN = new Certificate(new org.bouncycastle.asn1.x509.Certificate[0]);
    protected org.bouncycastle.asn1.x509.Certificate[] certificateList;

    public Certificate(org.bouncycastle.asn1.x509.Certificate[] arrcertificate) {
        if (arrcertificate == null) {
            throw new IllegalArgumentException("'certificateList' cannot be null");
        }
        this.certificateList = arrcertificate;
    }

    public static Certificate parse(InputStream inputStream) {
        int n2 = TlsUtils.readUint24(inputStream);
        if (n2 == 0) {
            return EMPTY_CHAIN;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(n2, inputStream));
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement((Object)org.bouncycastle.asn1.x509.Certificate.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque24((InputStream)byteArrayInputStream))));
        }
        org.bouncycastle.asn1.x509.Certificate[] arrcertificate = new org.bouncycastle.asn1.x509.Certificate[vector.size()];
        for (int i2 = 0; i2 < vector.size(); ++i2) {
            arrcertificate[i2] = (org.bouncycastle.asn1.x509.Certificate)vector.elementAt(i2);
        }
        return new Certificate(arrcertificate);
    }

    protected org.bouncycastle.asn1.x509.Certificate[] cloneCertificateList() {
        org.bouncycastle.asn1.x509.Certificate[] arrcertificate = new org.bouncycastle.asn1.x509.Certificate[this.certificateList.length];
        System.arraycopy((Object)this.certificateList, (int)0, (Object)arrcertificate, (int)0, (int)arrcertificate.length);
        return arrcertificate;
    }

    public void encode(OutputStream outputStream) {
        int n2 = 0;
        Vector vector = new Vector(this.certificateList.length);
        int n3 = 0;
        for (int i2 = 0; i2 < this.certificateList.length; ++i2) {
            byte[] arrby = this.certificateList[i2].getEncoded("DER");
            vector.addElement((Object)arrby);
            n3 += 3 + arrby.length;
        }
        TlsUtils.checkUint24(n3);
        TlsUtils.writeUint24(n3, outputStream);
        while (n2 < vector.size()) {
            TlsUtils.writeOpaque24((byte[])vector.elementAt(n2), outputStream);
            ++n2;
        }
    }

    public org.bouncycastle.asn1.x509.Certificate getCertificateAt(int n2) {
        return this.certificateList[n2];
    }

    public org.bouncycastle.asn1.x509.Certificate[] getCertificateList() {
        return this.cloneCertificateList();
    }

    public int getLength() {
        return this.certificateList.length;
    }

    public boolean isEmpty() {
        return this.certificateList.length == 0;
    }
}

