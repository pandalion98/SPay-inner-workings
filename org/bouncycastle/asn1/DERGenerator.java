package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.io.Streams;

public abstract class DERGenerator extends ASN1Generator {
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged;

    protected DERGenerator(OutputStream outputStream) {
        super(outputStream);
        this._tagged = false;
    }

    public DERGenerator(OutputStream outputStream, int i, boolean z) {
        super(outputStream);
        this._tagged = false;
        this._tagged = true;
        this._isExplicit = z;
        this._tagNo = i;
    }

    private void writeLength(OutputStream outputStream, int i) {
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
            outputStream.write((byte) (i2 | X509KeyUsage.digitalSignature));
            for (i3 = (i2 - 1) * 8; i3 >= 0; i3 -= 8) {
                outputStream.write((byte) (i >> i3));
            }
            return;
        }
        outputStream.write((byte) i);
    }

    void writeDEREncoded(int i, byte[] bArr) {
        if (this._tagged) {
            int i2 = this._tagNo | X509KeyUsage.digitalSignature;
            if (this._isExplicit) {
                i2 = (this._tagNo | 32) | X509KeyUsage.digitalSignature;
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                writeDEREncoded(byteArrayOutputStream, i, bArr);
                writeDEREncoded(this._out, i2, byteArrayOutputStream.toByteArray());
                return;
            } else if ((i & 32) != 0) {
                writeDEREncoded(this._out, i2 | 32, bArr);
                return;
            } else {
                writeDEREncoded(this._out, i2, bArr);
                return;
            }
        }
        writeDEREncoded(this._out, i, bArr);
    }

    void writeDEREncoded(OutputStream outputStream, int i, InputStream inputStream) {
        writeDEREncoded(outputStream, i, Streams.readAll(inputStream));
    }

    void writeDEREncoded(OutputStream outputStream, int i, byte[] bArr) {
        outputStream.write(i);
        writeLength(outputStream, bArr.length);
        outputStream.write(bArr);
    }
}
