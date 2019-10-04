/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 */
package org.bouncycastle.asn1;

import java.io.InputStream;
import org.bouncycastle.asn1.IndefiniteLengthInputStream;

abstract class LimitedInputStream
extends InputStream {
    protected final InputStream _in;
    private int _limit;

    LimitedInputStream(InputStream inputStream, int n2) {
        this._in = inputStream;
        this._limit = n2;
    }

    int getRemaining() {
        return this._limit;
    }

    protected void setParentEofDetect(boolean bl) {
        if (this._in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)this._in).setEofOn00(bl);
        }
    }
}

