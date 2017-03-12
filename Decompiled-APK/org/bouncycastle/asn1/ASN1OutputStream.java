package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;

public class ASN1OutputStream {
    private OutputStream os;

    private class ImplicitOutputStream extends ASN1OutputStream {
        private boolean first;

        public ImplicitOutputStream(OutputStream outputStream) {
            super(outputStream);
            this.first = true;
        }

        public void write(int i) {
            if (this.first) {
                this.first = false;
            } else {
                super.write(i);
            }
        }
    }

    public ASN1OutputStream(OutputStream outputStream) {
        this.os = outputStream;
    }

    public void close() {
        this.os.close();
    }

    public void flush() {
        this.os.flush();
    }

    ASN1OutputStream getDERSubStream() {
        return new DEROutputStream(this.os);
    }

    ASN1OutputStream getDLSubStream() {
        return new DLOutputStream(this.os);
    }

    void write(int i) {
        this.os.write(i);
    }

    void write(byte[] bArr) {
        this.os.write(bArr);
    }

    void write(byte[] bArr, int i, int i2) {
        this.os.write(bArr, i, i2);
    }

    void writeEncoded(int i, int i2, byte[] bArr) {
        writeTag(i, i2);
        writeLength(bArr.length);
        write(bArr);
    }

    void writeEncoded(int i, byte[] bArr) {
        write(i);
        writeLength(bArr.length);
        write(bArr);
    }

    void writeImplicitObject(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive != null) {
            aSN1Primitive.encode(new ImplicitOutputStream(this.os));
            return;
        }
        throw new IOException("null object detected");
    }

    void writeLength(int i) {
        if (i > CertificateBody.profileType) {
            int i2 = 1;
            int i3 = i;
            while (true) {
                i3 >>>= 8;
                if (i3 == 0) {
                    break;
                }
                i2++;
            }
            write((byte) (i2 | X509KeyUsage.digitalSignature));
            for (i3 = (i2 - 1) * 8; i3 >= 0; i3 -= 8) {
                write((byte) (i >> i3));
            }
            return;
        }
        write((byte) i);
    }

    protected void writeNull() {
        this.os.write(5);
        this.os.write(0);
    }

    public void writeObject(ASN1Encodable aSN1Encodable) {
        if (aSN1Encodable != null) {
            aSN1Encodable.toASN1Primitive().encode(this);
            return;
        }
        throw new IOException("null object detected");
    }

    void writeTag(int i, int i2) {
        if (i2 < 31) {
            write(i | i2);
            return;
        }
        write(i | 31);
        if (i2 < X509KeyUsage.digitalSignature) {
            write(i2);
            return;
        }
        byte[] bArr = new byte[5];
        int length = bArr.length - 1;
        bArr[length] = (byte) (i2 & CertificateBody.profileType);
        do {
            i2 >>= 7;
            length--;
            bArr[length] = (byte) ((i2 & CertificateBody.profileType) | X509KeyUsage.digitalSignature);
        } while (i2 > CertificateBody.profileType);
        write(bArr, length, bArr.length - length);
    }
}
