/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsUtils;

public class CertificateRequest {
    protected Vector certificateAuthorities;
    protected short[] certificateTypes;
    protected Vector supportedSignatureAlgorithms;

    public CertificateRequest(short[] arrs, Vector vector, Vector vector2) {
        this.certificateTypes = arrs;
        this.supportedSignatureAlgorithms = vector;
        this.certificateAuthorities = vector2;
    }

    public static CertificateRequest parse(TlsContext tlsContext, InputStream inputStream) {
        int n2 = TlsUtils.readUint8(inputStream);
        short[] arrs = new short[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrs[i2] = TlsUtils.readUint8(inputStream);
        }
        boolean bl = TlsUtils.isTLSv12(tlsContext);
        Vector vector = null;
        if (bl) {
            vector = TlsUtils.parseSupportedSignatureAlgorithms(false, inputStream);
        }
        Vector vector2 = new Vector();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readOpaque16(inputStream));
        while (byteArrayInputStream.available() > 0) {
            vector2.addElement((Object)X500Name.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque16((InputStream)byteArrayInputStream))));
        }
        return new CertificateRequest(arrs, vector, vector2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void encode(OutputStream outputStream) {
        int n2 = 0;
        if (this.certificateTypes == null || this.certificateTypes.length == 0) {
            TlsUtils.writeUint8(0, outputStream);
        } else {
            TlsUtils.writeUint8ArrayWithUint8Length(this.certificateTypes, outputStream);
        }
        if (this.supportedSignatureAlgorithms != null) {
            TlsUtils.encodeSupportedSignatureAlgorithms(this.supportedSignatureAlgorithms, false, outputStream);
        }
        if (this.certificateAuthorities == null || this.certificateAuthorities.isEmpty()) {
            TlsUtils.writeUint16(0, outputStream);
            return;
        } else {
            byte[] arrby;
            Vector vector = new Vector(this.certificateAuthorities.size());
            int n3 = 0;
            for (int i2 = 0; i2 < this.certificateAuthorities.size(); n3 += 2 + arrby.length, ++i2) {
                arrby = ((X500Name)this.certificateAuthorities.elementAt(i2)).getEncoded("DER");
                vector.addElement((Object)arrby);
            }
            TlsUtils.checkUint16(n3);
            TlsUtils.writeUint16(n3, outputStream);
            while (n2 < vector.size()) {
                TlsUtils.writeOpaque16((byte[])vector.elementAt(n2), outputStream);
                ++n2;
            }
        }
    }

    public Vector getCertificateAuthorities() {
        return this.certificateAuthorities;
    }

    public short[] getCertificateTypes() {
        return this.certificateTypes;
    }

    public Vector getSupportedSignatureAlgorithms() {
        return this.supportedSignatureAlgorithms;
    }
}

