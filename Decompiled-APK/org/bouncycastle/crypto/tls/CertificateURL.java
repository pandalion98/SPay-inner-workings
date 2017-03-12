package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class CertificateURL {
    protected short type;
    protected Vector urlAndHashList;

    class ListBuffer16 extends ByteArrayOutputStream {
        ListBuffer16() {
            TlsUtils.writeUint16(0, this);
        }

        void encodeTo(OutputStream outputStream) {
            int i = this.count - 2;
            TlsUtils.checkUint16(i);
            TlsUtils.writeUint16(i, this.buf, 0);
            outputStream.write(this.buf, 0, this.count);
            this.buf = null;
        }
    }

    public CertificateURL(short s, Vector vector) {
        if (!CertChainType.isValid(s)) {
            throw new IllegalArgumentException("'type' is not a valid CertChainType value");
        } else if (vector == null || vector.isEmpty()) {
            throw new IllegalArgumentException("'urlAndHashList' must have length > 0");
        } else {
            this.type = s;
            this.urlAndHashList = vector;
        }
    }

    public static CertificateURL parse(TlsContext tlsContext, InputStream inputStream) {
        short readUint8 = TlsUtils.readUint8(inputStream);
        if (CertChainType.isValid(readUint8)) {
            int readUint16 = TlsUtils.readUint16(inputStream);
            if (readUint16 < 1) {
                throw new TlsFatalAlert((short) 50);
            }
            InputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(readUint16, inputStream));
            Vector vector = new Vector();
            while (byteArrayInputStream.available() > 0) {
                vector.addElement(URLAndHash.parse(tlsContext, byteArrayInputStream));
            }
            return new CertificateURL(readUint8, vector);
        }
        throw new TlsFatalAlert((short) 50);
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.type, outputStream);
        OutputStream listBuffer16 = new ListBuffer16();
        for (int i = 0; i < this.urlAndHashList.size(); i++) {
            ((URLAndHash) this.urlAndHashList.elementAt(i)).encode(listBuffer16);
        }
        listBuffer16.encodeTo(outputStream);
    }

    public short getType() {
        return this.type;
    }

    public Vector getURLAndHashList() {
        return this.urlAndHashList;
    }
}
