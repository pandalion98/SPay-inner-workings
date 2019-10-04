/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DLOutputStream;

public class ASN1OutputStream {
    private OutputStream os;

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

    void write(int n2) {
        this.os.write(n2);
    }

    void write(byte[] arrby) {
        this.os.write(arrby);
    }

    void write(byte[] arrby, int n2, int n3) {
        this.os.write(arrby, n2, n3);
    }

    void writeEncoded(int n2, int n3, byte[] arrby) {
        this.writeTag(n2, n3);
        this.writeLength(arrby.length);
        this.write(arrby);
    }

    void writeEncoded(int n2, byte[] arrby) {
        this.write(n2);
        this.writeLength(arrby.length);
        this.write(arrby);
    }

    void writeImplicitObject(ASN1Primitive aSN1Primitive) {
        if (aSN1Primitive != null) {
            aSN1Primitive.encode(new ImplicitOutputStream(this.os));
            return;
        }
        throw new IOException("null object detected");
    }

    void writeLength(int n2) {
        if (n2 > 127) {
            int n3 = 1;
            int n4 = n2;
            while ((n4 >>>= 8) != 0) {
                ++n3;
            }
            this.write((byte)(n3 | 128));
            for (int i2 = 8 * (n3 - 1); i2 >= 0; i2 -= 8) {
                this.write((byte)(n2 >> i2));
            }
        } else {
            this.write((byte)n2);
        }
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

    void writeTag(int n2, int n3) {
        if (n3 < 31) {
            this.write(n2 | n3);
            return;
        }
        this.write(n2 | 31);
        if (n3 < 128) {
            this.write(n3);
            return;
        }
        byte[] arrby = new byte[5];
        int n4 = -1 + arrby.length;
        arrby[n4] = (byte)(n3 & 127);
        do {
            arrby[--n4] = (byte)(128 | (n3 >>= 7) & 127);
        } while (n3 > 127);
        this.write(arrby, n4, arrby.length - n4);
    }

    private class ImplicitOutputStream
    extends ASN1OutputStream {
        private boolean first;

        public ImplicitOutputStream(OutputStream outputStream) {
            super(outputStream);
            this.first = true;
        }

        @Override
        public void write(int n2) {
            if (this.first) {
                this.first = false;
                return;
            }
            super.write(n2);
        }
    }

}

