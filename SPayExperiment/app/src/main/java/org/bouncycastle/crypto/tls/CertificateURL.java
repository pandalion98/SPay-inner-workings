/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.crypto.tls.CertChainType;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.crypto.tls.URLAndHash;

public class CertificateURL {
    protected short type;
    protected Vector urlAndHashList;

    public CertificateURL(short s2, Vector vector) {
        if (!CertChainType.isValid(s2)) {
            throw new IllegalArgumentException("'type' is not a valid CertChainType value");
        }
        if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("'urlAndHashList' must have length > 0");
        }
        this.type = s2;
        this.urlAndHashList = vector;
    }

    public static CertificateURL parse(TlsContext tlsContext, InputStream inputStream) {
        short s2 = TlsUtils.readUint8(inputStream);
        if (!CertChainType.isValid(s2)) {
            throw new TlsFatalAlert(50);
        }
        int n2 = TlsUtils.readUint16(inputStream);
        if (n2 < 1) {
            throw new TlsFatalAlert(50);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(n2, inputStream));
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement((Object)URLAndHash.parse(tlsContext, (InputStream)byteArrayInputStream));
        }
        return new CertificateURL(s2, vector);
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.type, outputStream);
        ListBuffer16 listBuffer16 = new ListBuffer16();
        for (int i2 = 0; i2 < this.urlAndHashList.size(); ++i2) {
            ((URLAndHash)this.urlAndHashList.elementAt(i2)).encode((OutputStream)listBuffer16);
        }
        listBuffer16.encodeTo(outputStream);
    }

    public short getType() {
        return this.type;
    }

    public Vector getURLAndHashList() {
        return this.urlAndHashList;
    }

    class ListBuffer16
    extends ByteArrayOutputStream {
        ListBuffer16() {
            TlsUtils.writeUint16(0, (OutputStream)this);
        }

        void encodeTo(OutputStream outputStream) {
            int n2 = -2 + this.count;
            TlsUtils.checkUint16(n2);
            TlsUtils.writeUint16(n2, this.buf, 0);
            outputStream.write(this.buf, 0, this.count);
            this.buf = null;
        }
    }

}

