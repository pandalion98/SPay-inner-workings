/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.InputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.String
 *  org.bouncycastle.util.io.Streams
 */
package org.bouncycastle.asn1;

import java.io.EOFException;
import java.io.InputStream;
import org.bouncycastle.asn1.LimitedInputStream;
import org.bouncycastle.util.io.Streams;

class DefiniteLengthInputStream
extends LimitedInputStream {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final int _originalLength;
    private int _remaining;

    DefiniteLengthInputStream(InputStream inputStream, int n2) {
        super(inputStream, n2);
        if (n2 < 0) {
            throw new IllegalArgumentException("negative lengths not allowed");
        }
        this._originalLength = n2;
        this._remaining = n2;
        if (n2 == 0) {
            this.setParentEofDetect(true);
        }
    }

    @Override
    int getRemaining() {
        return this._remaining;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int read() {
        int n2;
        if (this._remaining == 0) {
            return -1;
        }
        int n3 = this._in.read();
        if (n3 < 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        this._remaining = n2 = -1 + this._remaining;
        if (n2 != 0) return n3;
        this.setParentEofDetect(true);
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int read(byte[] arrby, int n2, int n3) {
        int n4;
        if (this._remaining == 0) {
            return -1;
        }
        int n5 = Math.min((int)n3, (int)this._remaining);
        int n6 = this._in.read(arrby, n2, n5);
        if (n6 < 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        this._remaining = n4 = this._remaining - n6;
        if (n4 != 0) return n6;
        this.setParentEofDetect(true);
        return n6;
    }

    byte[] toByteArray() {
        int n2;
        if (this._remaining == 0) {
            return EMPTY_BYTES;
        }
        byte[] arrby = new byte[this._remaining];
        this._remaining = n2 = this._remaining - Streams.readFully((InputStream)this._in, (byte[])arrby);
        if (n2 != 0) {
            throw new EOFException("DEF length " + this._originalLength + " object truncated by " + this._remaining);
        }
        this.setParentEofDetect(true);
        return arrby;
    }
}

