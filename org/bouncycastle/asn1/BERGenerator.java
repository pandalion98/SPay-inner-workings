package org.bouncycastle.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.jce.X509KeyUsage;

public class BERGenerator extends ASN1Generator {
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged;

    protected BERGenerator(OutputStream outputStream) {
        super(outputStream);
        this._tagged = false;
    }

    public BERGenerator(OutputStream outputStream, int i, boolean z) {
        super(outputStream);
        this._tagged = false;
        this._tagged = true;
        this._isExplicit = z;
        this._tagNo = i;
    }

    private void writeHdr(int i) {
        this._out.write(i);
        this._out.write(X509KeyUsage.digitalSignature);
    }

    public OutputStream getRawOutputStream() {
        return this._out;
    }

    protected void writeBERBody(InputStream inputStream) {
        while (true) {
            int read = inputStream.read();
            if (read >= 0) {
                this._out.write(read);
            } else {
                return;
            }
        }
    }

    protected void writeBEREnd() {
        this._out.write(0);
        this._out.write(0);
        if (this._tagged && this._isExplicit) {
            this._out.write(0);
            this._out.write(0);
        }
    }

    protected void writeBERHeader(int i) {
        if (this._tagged) {
            int i2 = this._tagNo | X509KeyUsage.digitalSignature;
            if (this._isExplicit) {
                writeHdr(i2 | 32);
                writeHdr(i);
                return;
            } else if ((i & 32) != 0) {
                writeHdr(i2 | 32);
                return;
            } else {
                writeHdr(i2);
                return;
            }
        }
        writeHdr(i);
    }
}
