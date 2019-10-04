/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.CharConversionException
 *  java.io.IOException
 *  java.io.Writer
 *  java.lang.CharSequence
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.String
 *  java.nio.ByteBuffer
 */
package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

public final class UTF8ByteBufferWriter
extends Writer {
    private ByteBuffer _byteBuffer;
    private char _highSurrogate;

    private void write2(int n2) {
        if ((n2 & -2048) == 0) {
            this._byteBuffer.put((byte)(192 | n2 >> 6));
            this._byteBuffer.put((byte)(128 | n2 & 63));
            return;
        }
        if ((-65536 & n2) == 0) {
            this._byteBuffer.put((byte)(224 | n2 >> 12));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 6));
            this._byteBuffer.put((byte)(128 | n2 & 63));
            return;
        }
        if ((-14680064 & n2) == 0) {
            this._byteBuffer.put((byte)(240 | n2 >> 18));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 12));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 6));
            this._byteBuffer.put((byte)(128 | n2 & 63));
            return;
        }
        if ((-201326592 & n2) == 0) {
            this._byteBuffer.put((byte)(248 | n2 >> 24));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 18));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 12));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 6));
            this._byteBuffer.put((byte)(128 | n2 & 63));
            return;
        }
        if ((Integer.MIN_VALUE & n2) == 0) {
            this._byteBuffer.put((byte)(252 | n2 >> 30));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 24));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 18));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 12));
            this._byteBuffer.put((byte)(128 | 63 & n2 >> 6));
            this._byteBuffer.put((byte)(128 | n2 & 63));
            return;
        }
        throw new CharConversionException("Illegal character U+" + Integer.toHexString((int)n2));
    }

    public void close() {
        if (this._byteBuffer != null) {
            this.reset();
        }
    }

    public void flush() {
        if (this._byteBuffer == null) {
            throw new IOException("Writer closed");
        }
    }

    public void reset() {
        this._byteBuffer = null;
        this._highSurrogate = '\u0000';
    }

    public UTF8ByteBufferWriter setByteBuffer(ByteBuffer byteBuffer) {
        return this.setOutput(byteBuffer);
    }

    public UTF8ByteBufferWriter setOutput(ByteBuffer byteBuffer) {
        if (this._byteBuffer != null) {
            throw new IllegalStateException("Writer not closed or reset");
        }
        this._byteBuffer = byteBuffer;
        return this;
    }

    public void write(char c2) {
        if (c2 < '\ud800' || c2 > '\udfff') {
            this.write((int)c2);
            return;
        }
        if (c2 < '\udc00') {
            this._highSurrogate = c2;
            return;
        }
        this.write(65536 + ((this._highSurrogate - 55296 << 10) + (c2 - 56320)));
    }

    public void write(int n2) {
        if ((n2 & -128) == 0) {
            this._byteBuffer.put((byte)n2);
            return;
        }
        this.write2(n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(CharSequence charSequence) {
        int n2 = charSequence.length();
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3 + 1;
            char c2 = charSequence.charAt(n3);
            if (c2 < '') {
                this._byteBuffer.put((byte)c2);
            } else {
                this.write(c2);
            }
            n3 = n4;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(String string, int n2, int n3) {
        int n4 = n2 + n3;
        while (n2 < n4) {
            int n5 = n2 + 1;
            char c2 = string.charAt(n2);
            if (c2 < '') {
                this._byteBuffer.put((byte)c2);
            } else {
                this.write(c2);
            }
            n2 = n5;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void write(char[] arrc, int n2, int n3) {
        int n4 = n2 + n3;
        while (n2 < n4) {
            int n5 = n2 + 1;
            char c2 = arrc[n2];
            if (c2 < '') {
                this._byteBuffer.put((byte)c2);
            } else {
                this.write(c2);
            }
            n2 = n5;
        }
        return;
    }
}

