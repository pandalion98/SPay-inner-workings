package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encoding;

public class Certificate {
    public static final Certificate EMPTY_CHAIN;
    protected org.bouncycastle.asn1.x509.Certificate[] certificateList;

    static {
        EMPTY_CHAIN = new Certificate(new org.bouncycastle.asn1.x509.Certificate[0]);
    }

    public Certificate(org.bouncycastle.asn1.x509.Certificate[] certificateArr) {
        if (certificateArr == null) {
            throw new IllegalArgumentException("'certificateList' cannot be null");
        }
        this.certificateList = certificateArr;
    }

    public static Certificate parse(InputStream inputStream) {
        int readUint24 = TlsUtils.readUint24(inputStream);
        if (readUint24 == 0) {
            return EMPTY_CHAIN;
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(readUint24, inputStream));
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement(org.bouncycastle.asn1.x509.Certificate.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque24(byteArrayInputStream))));
        }
        org.bouncycastle.asn1.x509.Certificate[] certificateArr = new org.bouncycastle.asn1.x509.Certificate[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            certificateArr[i] = (org.bouncycastle.asn1.x509.Certificate) vector.elementAt(i);
        }
        return new Certificate(certificateArr);
    }

    protected org.bouncycastle.asn1.x509.Certificate[] cloneCertificateList() {
        Object obj = new org.bouncycastle.asn1.x509.Certificate[this.certificateList.length];
        System.arraycopy(this.certificateList, 0, obj, 0, obj.length);
        return obj;
    }

    public void encode(OutputStream outputStream) {
        int i = 0;
        Vector vector = new Vector(this.certificateList.length);
        int i2 = 0;
        for (org.bouncycastle.asn1.x509.Certificate encoded : this.certificateList) {
            Object encoded2 = encoded.getEncoded(ASN1Encoding.DER);
            vector.addElement(encoded2);
            i2 += encoded2.length + 3;
        }
        TlsUtils.checkUint24(i2);
        TlsUtils.writeUint24(i2, outputStream);
        while (i < vector.size()) {
            TlsUtils.writeOpaque24((byte[]) vector.elementAt(i), outputStream);
            i++;
        }
    }

    public org.bouncycastle.asn1.x509.Certificate getCertificateAt(int i) {
        return this.certificateList[i];
    }

    public org.bouncycastle.asn1.x509.Certificate[] getCertificateList() {
        return cloneCertificateList();
    }

    public int getLength() {
        return this.certificateList.length;
    }

    public boolean isEmpty() {
        return this.certificateList.length == 0;
    }
}
