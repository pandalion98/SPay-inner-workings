/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Generator;
import org.bouncycastle.util.io.Streams;

public abstract class DERGenerator
extends ASN1Generator {
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged = false;

    protected DERGenerator(OutputStream outputStream) {
        super(outputStream);
    }

    public DERGenerator(OutputStream outputStream, int n2, boolean bl) {
        super(outputStream);
        this._tagged = true;
        this._isExplicit = bl;
        this._tagNo = n2;
    }

    private void writeLength(OutputStream outputStream, int n2) {
        if (n2 > 127) {
            int n3 = 1;
            int n4 = n2;
            while ((n4 >>>= 8) != 0) {
                ++n3;
            }
            outputStream.write((int)((byte)(n3 | 128)));
            for (int i2 = 8 * (n3 - 1); i2 >= 0; i2 -= 8) {
                outputStream.write((int)((byte)(n2 >> i2)));
            }
        } else {
            outputStream.write((int)((byte)n2));
        }
    }

    void writeDEREncoded(int n2, byte[] arrby) {
        if (this._tagged) {
            int n3 = 128 | this._tagNo;
            if (this._isExplicit) {
                int n4 = 128 | (32 | this._tagNo);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.writeDEREncoded((OutputStream)byteArrayOutputStream, n2, arrby);
                this.writeDEREncoded(this._out, n4, byteArrayOutputStream.toByteArray());
                return;
            }
            if ((n2 & 32) != 0) {
                this.writeDEREncoded(this._out, n3 | 32, arrby);
                return;
            }
            this.writeDEREncoded(this._out, n3, arrby);
            return;
        }
        this.writeDEREncoded(this._out, n2, arrby);
    }

    void writeDEREncoded(OutputStream outputStream, int n2, InputStream inputStream) {
        this.writeDEREncoded(outputStream, n2, Streams.readAll((InputStream)inputStream));
    }

    void writeDEREncoded(OutputStream outputStream, int n2, byte[] arrby) {
        outputStream.write(n2);
        this.writeLength(outputStream, arrby.length);
        outputStream.write(arrby);
    }
}

