/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 */
package org.bouncycastle.asn1;

import java.io.OutputStream;
import org.bouncycastle.asn1.BERGenerator;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;

public class BEROctetStringGenerator
extends BERGenerator {
    public BEROctetStringGenerator(OutputStream outputStream) {
        super(outputStream);
        this.writeBERHeader(36);
    }

    public BEROctetStringGenerator(OutputStream outputStream, int n2, boolean bl) {
        super(outputStream, n2, bl);
        this.writeBERHeader(36);
    }

    public OutputStream getOctetOutputStream() {
        return this.getOctetOutputStream(new byte[1000]);
    }

    public OutputStream getOctetOutputStream(byte[] arrby) {
        return new BufferedBEROctetStream(arrby);
    }

    private class BufferedBEROctetStream
    extends OutputStream {
        private byte[] _buf;
        private DEROutputStream _derOut;
        private int _off;

        BufferedBEROctetStream(byte[] arrby) {
            this._buf = arrby;
            this._off = 0;
            this._derOut = new DEROutputStream(BEROctetStringGenerator.this._out);
        }

        public void close() {
            if (this._off != 0) {
                byte[] arrby = new byte[this._off];
                System.arraycopy((Object)this._buf, (int)0, (Object)arrby, (int)0, (int)this._off);
                DEROctetString.encode(this._derOut, arrby);
            }
            BEROctetStringGenerator.this.writeBEREnd();
        }

        public void write(int n2) {
            byte[] arrby = this._buf;
            int n3 = this._off;
            this._off = n3 + 1;
            arrby[n3] = (byte)n2;
            if (this._off == this._buf.length) {
                DEROctetString.encode(this._derOut, this._buf);
                this._off = 0;
            }
        }

        public void write(byte[] arrby, int n2, int n3) {
            do {
                int n4;
                block4 : {
                    block3 : {
                        if (n3 <= 0) break block3;
                        n4 = Math.min((int)n3, (int)(this._buf.length - this._off));
                        System.arraycopy((Object)arrby, (int)n2, (Object)this._buf, (int)this._off, (int)n4);
                        this._off = n4 + this._off;
                        if (this._off >= this._buf.length) break block4;
                    }
                    return;
                }
                DEROctetString.encode(this._derOut, this._buf);
                this._off = 0;
                n2 += n4;
                n3 -= n4;
            } while (true);
        }
    }

}

