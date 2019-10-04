/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 */
package org.bouncycastle.asn1;

import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1StreamParser;

class ConstructedOctetStream
extends InputStream {
    private InputStream _currentStream;
    private boolean _first = true;
    private final ASN1StreamParser _parser;

    ConstructedOctetStream(ASN1StreamParser aSN1StreamParser) {
        this._parser = aSN1StreamParser;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int read() {
        int n2;
        if (this._currentStream == null) {
            if (!this._first) {
                return -1;
            }
            ASN1OctetStringParser aSN1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
            if (aSN1OctetStringParser == null) {
                return -1;
            }
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        while ((n2 = this._currentStream.read()) < 0) {
            ASN1OctetStringParser aSN1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
            if (aSN1OctetStringParser == null) {
                this._currentStream = null;
                return -1;
            }
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int read(byte[] arrby, int n2, int n3) {
        InputStream inputStream = this._currentStream;
        int n4 = 0;
        if (inputStream == null) {
            if (!this._first) {
                return -1;
            }
            ASN1OctetStringParser aSN1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
            if (aSN1OctetStringParser == null) return -1;
            this._first = false;
            this._currentStream = aSN1OctetStringParser.getOctetStream();
        }
        do {
            int n5;
            int n6;
            if ((n5 = this._currentStream.read(arrby, n2 + n4, n3 - n4)) >= 0) {
                n6 = n5 + n4;
                if (n6 == n3) {
                    return n6;
                }
            } else {
                ASN1OctetStringParser aSN1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
                if (aSN1OctetStringParser == null) {
                    this._currentStream = null;
                    if (n4 >= 1) return n4;
                    return -1;
                }
                this._currentStream = aSN1OctetStringParser.getOctetStream();
                n6 = n4;
            }
            n4 = n6;
        } while (true);
    }
}

