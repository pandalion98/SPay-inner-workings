/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.CharConversionException
 *  java.io.IOException
 *  java.io.Reader
 *  java.lang.Appendable
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.String
 *  java.nio.Buffer
 *  java.nio.BufferUnderflowException
 *  java.nio.ByteBuffer
 */
package javolution.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.Reader;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class UTF8ByteBufferReader
extends Reader {
    private ByteBuffer _byteBuffer;
    private int _code;
    private int _moreBytes;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int read2(byte by) {
        block20 : {
            block19 : {
                block18 : {
                    block17 : {
                        block16 : {
                            block15 : {
                                if (by >= 0) {
                                    if (this._moreBytes != 0) break block15;
                                    return by;
                                }
                            }
                            if ((by & 192) == 128) {
                                int n2;
                                if (this._moreBytes == 0) break block16;
                                this._code = this._code << 6 | by & 63;
                                this._moreBytes = n2 = -1 + this._moreBytes;
                                if (n2 != 0) return this.read2(this._byteBuffer.get());
                                return this._code;
                            }
                        }
                        if ((by & 224) == 192) {
                            if (this._moreBytes != 0) break block17;
                            this._code = by & 31;
                            this._moreBytes = 1;
                            return this.read2(this._byteBuffer.get());
                        }
                    }
                    if ((by & 240) == 224) {
                        if (this._moreBytes != 0) break block18;
                        this._code = by & 15;
                        this._moreBytes = 2;
                        return this.read2(this._byteBuffer.get());
                    }
                }
                if ((by & 248) == 240) {
                    if (this._moreBytes != 0) break block19;
                    this._code = by & 7;
                    this._moreBytes = 3;
                    return this.read2(this._byteBuffer.get());
                }
            }
            if ((by & 252) == 248) {
                if (this._moreBytes != 0) break block20;
                this._code = by & 3;
                this._moreBytes = 4;
                return this.read2(this._byteBuffer.get());
            }
        }
        if ((by & 254) != 252) throw new CharConversionException("Invalid UTF-8 Encoding");
        try {
            if (this._moreBytes != 0) throw new CharConversionException("Invalid UTF-8 Encoding");
            this._code = by & 1;
            this._moreBytes = 5;
            return this.read2(this._byteBuffer.get());
        }
        catch (BufferUnderflowException bufferUnderflowException) {
            throw new CharConversionException("Incomplete Sequence");
        }
    }

    public void close() {
        if (this._byteBuffer != null) {
            this.reset();
        }
    }

    public int read() {
        if (this._byteBuffer != null) {
            if (this._byteBuffer.hasRemaining()) {
                byte by = this._byteBuffer.get();
                if (by >= 0) {
                    return by;
                }
                return this.read2(by);
            }
            return -1;
        }
        throw new IOException("Reader closed");
    }

    /*
     * Enabled aggressive block sorting
     */
    public int read(char[] arrc, int n2, int n3) {
        if (this._byteBuffer == null) {
            throw new IOException("Reader closed");
        }
        int n4 = n2 + n3;
        int n5 = this._byteBuffer.remaining();
        if (n5 <= 0) {
            return -1;
        }
        int n6 = n2;
        int n7 = n5;
        while (n6 < n4) {
            int n8;
            int n9 = n7 - 1;
            if (n7 <= 0) {
                return n6 - n2;
            }
            byte by = this._byteBuffer.get();
            if (by >= 0) {
                n8 = n6 + 1;
                arrc[n6] = (char)by;
            } else {
                if (n6 >= n4 - 1) {
                    this._byteBuffer.position(-1 + this._byteBuffer.position());
                    n9 + 1;
                    return n6 - n2;
                }
                int n10 = this.read2(by);
                n9 = this._byteBuffer.remaining();
                if (n10 < 65536) {
                    n8 = n6 + 1;
                    arrc[n6] = (char)n10;
                } else {
                    if (n10 > 1114111) {
                        throw new CharConversionException("Cannot convert U+" + Integer.toHexString((int)n10) + " to char (code greater than U+10FFFF)");
                    }
                    int n11 = n6 + 1;
                    arrc[n6] = (char)(55296 + (n10 - 65536 >> 10));
                    n8 = n11 + 1;
                    arrc[n11] = (char)(56320 + (1023 & n10 - 65536));
                }
            }
            n6 = n8;
            n7 = n9;
        }
        return n3;
    }

    public void read(Appendable appendable) {
        if (this._byteBuffer == null) {
            throw new IOException("Reader closed");
        }
        while (this._byteBuffer.hasRemaining()) {
            byte by = this._byteBuffer.get();
            if (by >= 0) {
                appendable.append((char)by);
                continue;
            }
            int n2 = this.read2(by);
            if (n2 < 65536) {
                appendable.append((char)n2);
                continue;
            }
            if (n2 <= 1114111) {
                appendable.append((char)(55296 + (n2 - 65536 >> 10)));
                appendable.append((char)(56320 + (1023 & n2 - 65536)));
                continue;
            }
            throw new CharConversionException("Cannot convert U+" + Integer.toHexString((int)n2) + " to char (code greater than U+10FFFF)");
        }
    }

    public boolean ready() {
        if (this._byteBuffer != null) {
            return this._byteBuffer.hasRemaining();
        }
        throw new IOException("Reader closed");
    }

    public void reset() {
        this._byteBuffer = null;
        this._code = 0;
        this._moreBytes = 0;
    }

    public UTF8ByteBufferReader setByteBuffer(ByteBuffer byteBuffer) {
        return this.setInput(byteBuffer);
    }

    public UTF8ByteBufferReader setInput(ByteBuffer byteBuffer) {
        if (this._byteBuffer != null) {
            throw new IllegalStateException("Reader not closed or reset");
        }
        this._byteBuffer = byteBuffer;
        return this;
    }
}

