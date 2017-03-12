package org.bouncycastle.asn1;

import java.io.InputStream;

class ConstructedOctetStream extends InputStream {
    private InputStream _currentStream;
    private boolean _first;
    private final ASN1StreamParser _parser;

    ConstructedOctetStream(ASN1StreamParser aSN1StreamParser) {
        this._first = true;
        this._parser = aSN1StreamParser;
    }

    public int read() {
        ASN1OctetStringParser aSN1OctetStringParser;
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            aSN1OctetStringParser = (ASN1OctetStringParser) this._parser.readObject();
            if (aSN1OctetStringParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        while (true) {
            int read = this._currentStream.read();
            if (read >= 0) {
                return read;
            }
            aSN1OctetStringParser = (ASN1OctetStringParser) this._parser.readObject();
            if (aSN1OctetStringParser == null) {
                this._currentStream = null;
                return -1;
            }
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        ASN1OctetStringParser aSN1OctetStringParser;
        int i3 = 0;
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            aSN1OctetStringParser = (ASN1OctetStringParser) this._parser.readObject();
            if (aSN1OctetStringParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        while (true) {
            int read = this._currentStream.read(bArr, i + i3, i2 - i3);
            if (read >= 0) {
                read += i3;
                if (read == i2) {
                    return read;
                }
            } else {
                aSN1OctetStringParser = (ASN1OctetStringParser) this._parser.readObject();
                if (aSN1OctetStringParser == null) {
                    break;
                }
                this._currentStream = aSN1OctetStringParser.getOctetStream();
                read = i3;
            }
            i3 = read;
        }
        this._currentStream = null;
        if (i3 < 1) {
            i3 = -1;
        }
        return i3;
    }
}
