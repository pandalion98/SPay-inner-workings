/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 */
package org.bouncycastle.asn1;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Generator;

public class BERGenerator
extends ASN1Generator {
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged = false;

    protected BERGenerator(OutputStream outputStream) {
        super(outputStream);
    }

    public BERGenerator(OutputStream outputStream, int n2, boolean bl) {
        super(outputStream);
        this._tagged = true;
        this._isExplicit = bl;
        this._tagNo = n2;
    }

    private void writeHdr(int n2) {
        this._out.write(n2);
        this._out.write(128);
    }

    @Override
    public OutputStream getRawOutputStream() {
        return this._out;
    }

    protected void writeBERBody(InputStream inputStream) {
        int n2;
        while ((n2 = inputStream.read()) >= 0) {
            this._out.write(n2);
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

    protected void writeBERHeader(int n2) {
        if (this._tagged) {
            int n3 = 128 | this._tagNo;
            if (this._isExplicit) {
                this.writeHdr(n3 | 32);
                this.writeHdr(n2);
                return;
            }
            if ((n2 & 32) != 0) {
                this.writeHdr(n3 | 32);
                return;
            }
            this.writeHdr(n3);
            return;
        }
        this.writeHdr(n2);
    }
}

