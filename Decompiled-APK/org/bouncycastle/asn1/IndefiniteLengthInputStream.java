package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.InputStream;

class IndefiniteLengthInputStream extends LimitedInputStream {
    private int _b1;
    private int _b2;
    private boolean _eofOn00;
    private boolean _eofReached;

    IndefiniteLengthInputStream(InputStream inputStream, int i) {
        super(inputStream, i);
        this._eofReached = false;
        this._eofOn00 = true;
        this._b1 = inputStream.read();
        this._b2 = inputStream.read();
        if (this._b2 < 0) {
            throw new EOFException();
        }
        checkForEof();
    }

    private boolean checkForEof() {
        if (!this._eofReached && this._eofOn00 && this._b1 == 0 && this._b2 == 0) {
            this._eofReached = true;
            setParentEofDetect(true);
        }
        return this._eofReached;
    }

    public int read() {
        if (checkForEof()) {
            return -1;
        }
        int read = this._in.read();
        if (read < 0) {
            throw new EOFException();
        }
        int i = this._b1;
        this._b1 = this._b2;
        this._b2 = read;
        return i;
    }

    public int read(byte[] bArr, int i, int i2) {
        if (this._eofOn00 || i2 < 3) {
            return super.read(bArr, i, i2);
        }
        if (this._eofReached) {
            return -1;
        }
        int read = this._in.read(bArr, i + 2, i2 - 2);
        if (read < 0) {
            throw new EOFException();
        }
        bArr[i] = (byte) this._b1;
        bArr[i + 1] = (byte) this._b2;
        this._b1 = this._in.read();
        this._b2 = this._in.read();
        if (this._b2 >= 0) {
            return read + 2;
        }
        throw new EOFException();
    }

    void setEofOn00(boolean z) {
        this._eofOn00 = z;
        checkForEof();
    }
}
