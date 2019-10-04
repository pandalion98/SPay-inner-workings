/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.InputStream
 */
package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.InputStream;
import org.bouncycastle.asn1.LimitedInputStream;

class IndefiniteLengthInputStream
extends LimitedInputStream {
    private int _b1;
    private int _b2;
    private boolean _eofOn00 = true;
    private boolean _eofReached = false;

    IndefiniteLengthInputStream(InputStream inputStream, int n2) {
        super(inputStream, n2);
        this._b1 = inputStream.read();
        this._b2 = inputStream.read();
        if (this._b2 < 0) {
            throw new EOFException();
        }
        this.checkForEof();
    }

    private boolean checkForEof() {
        if (!this._eofReached && this._eofOn00 && this._b1 == 0 && this._b2 == 0) {
            this._eofReached = true;
            this.setParentEofDetect(true);
        }
        return this._eofReached;
    }

    public int read() {
        if (this.checkForEof()) {
            return -1;
        }
        int n2 = this._in.read();
        if (n2 < 0) {
            throw new EOFException();
        }
        int n3 = this._b1;
        this._b1 = this._b2;
        this._b2 = n2;
        return n3;
    }

    public int read(byte[] arrby, int n2, int n3) {
        if (this._eofOn00 || n3 < 3) {
            return super.read(arrby, n2, n3);
        }
        if (this._eofReached) {
            return -1;
        }
        int n4 = this._in.read(arrby, n2 + 2, n3 - 2);
        if (n4 < 0) {
            throw new EOFException();
        }
        arrby[n2] = (byte)this._b1;
        arrby[n2 + 1] = (byte)this._b2;
        this._b1 = this._in.read();
        this._b2 = this._in.read();
        if (this._b2 < 0) {
            throw new EOFException();
        }
        return n4 + 2;
    }

    void setEofOn00(boolean bl) {
        this._eofOn00 = bl;
        this.checkForEof();
    }
}

