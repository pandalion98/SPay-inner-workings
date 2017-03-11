package org.bouncycastle.asn1;

import com.google.android.gms.location.LocationStatusCodes;
import java.io.OutputStream;

public class BEROctetStringGenerator extends BERGenerator {

    private class BufferedBEROctetStream extends OutputStream {
        private byte[] _buf;
        private DEROutputStream _derOut;
        private int _off;

        BufferedBEROctetStream(byte[] bArr) {
            this._buf = bArr;
            this._off = 0;
            this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
        }

        public void close() {
            if (this._off != 0) {
                Object obj = new byte[this._off];
                System.arraycopy(this._buf, 0, obj, 0, this._off);
                DEROctetString.encode(this._derOut, obj);
            }
            BEROctetStringGenerator.this.writeBEREnd();
        }

        public void write(int i) {
            byte[] bArr = this._buf;
            int i2 = this._off;
            this._off = i2 + 1;
            bArr[i2] = (byte) i;
            if (this._off == this._buf.length) {
                DEROctetString.encode(this._derOut, this._buf);
                this._off = 0;
            }
        }

        public void write(byte[] bArr, int i, int i2) {
            while (i2 > 0) {
                int min = Math.min(i2, this._buf.length - this._off);
                System.arraycopy(bArr, i, this._buf, this._off, min);
                this._off += min;
                if (this._off >= this._buf.length) {
                    DEROctetString.encode(this._derOut, this._buf);
                    this._off = 0;
                    i += min;
                    i2 -= min;
                } else {
                    return;
                }
            }
        }
    }

    public BEROctetStringGenerator(OutputStream outputStream) {
        super(outputStream);
        writeBERHeader(36);
    }

    public BEROctetStringGenerator(OutputStream outputStream, int i, boolean z) {
        super(outputStream, i, z);
        writeBERHeader(36);
    }

    public OutputStream getOctetOutputStream() {
        return getOctetOutputStream(new byte[LocationStatusCodes.GEOFENCE_NOT_AVAILABLE]);
    }

    public OutputStream getOctetOutputStream(byte[] bArr) {
        return new BufferedBEROctetStream(bArr);
    }
}
