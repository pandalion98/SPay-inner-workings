package javolution.io;

import com.google.android.gms.location.places.Place;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.Reader;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;

public final class UTF8ByteBufferReader extends Reader {
    private ByteBuffer _byteBuffer;
    private int _code;
    private int _moreBytes;

    public UTF8ByteBufferReader setInput(ByteBuffer byteBuffer) {
        if (this._byteBuffer != null) {
            throw new IllegalStateException("Reader not closed or reset");
        }
        this._byteBuffer = byteBuffer;
        return this;
    }

    public boolean ready() {
        if (this._byteBuffer != null) {
            return this._byteBuffer.hasRemaining();
        }
        throw new IOException("Reader closed");
    }

    public void close() {
        if (this._byteBuffer != null) {
            reset();
        }
    }

    public int read() {
        if (this._byteBuffer == null) {
            throw new IOException("Reader closed");
        } else if (!this._byteBuffer.hasRemaining()) {
            return -1;
        } else {
            byte b = this._byteBuffer.get();
            if (b >= null) {
                return b;
            }
            return read2(b);
        }
    }

    private int read2(byte b) {
        if (b >= null) {
            try {
                if (this._moreBytes == 0) {
                    return b;
                }
            } catch (BufferUnderflowException e) {
                throw new CharConversionException("Incomplete Sequence");
            }
        }
        if ((b & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) == X509KeyUsage.digitalSignature && this._moreBytes != 0) {
            this._code = (this._code << 6) | (b & 63);
            int i = this._moreBytes - 1;
            this._moreBytes = i;
            if (i == 0) {
                return this._code;
            }
            return read2(this._byteBuffer.get());
        } else if ((b & 224) == CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256 && this._moreBytes == 0) {
            this._code = b & 31;
            this._moreBytes = 1;
            return read2(this._byteBuffer.get());
        } else if ((b & 240) == 224 && this._moreBytes == 0) {
            this._code = b & 15;
            this._moreBytes = 2;
            return read2(this._byteBuffer.get());
        } else if ((b & 248) == 240 && this._moreBytes == 0) {
            this._code = b & 7;
            this._moreBytes = 3;
            return read2(this._byteBuffer.get());
        } else if ((b & 252) == 248 && this._moreBytes == 0) {
            this._code = b & 3;
            this._moreBytes = 4;
            return read2(this._byteBuffer.get());
        } else if ((b & 254) == 252 && this._moreBytes == 0) {
            this._code = b & 1;
            this._moreBytes = 5;
            return read2(this._byteBuffer.get());
        } else {
            throw new CharConversionException("Invalid UTF-8 Encoding");
        }
    }

    public int read(char[] cArr, int i, int i2) {
        if (this._byteBuffer == null) {
            throw new IOException("Reader closed");
        }
        int i3 = i + i2;
        int remaining = this._byteBuffer.remaining();
        if (remaining <= 0) {
            return -1;
        }
        int i4 = i;
        int i5 = remaining;
        while (i4 < i3) {
            remaining = i5 - 1;
            if (i5 <= 0) {
                return i4 - i;
            }
            byte b = this._byteBuffer.get();
            if (b >= null) {
                i5 = i4 + 1;
                cArr[i4] = (char) b;
            } else if (i4 < i3 - 1) {
                int read2 = read2(b);
                remaining = this._byteBuffer.remaining();
                if (read2 < PKIFailureInfo.notAuthorized) {
                    i5 = i4 + 1;
                    cArr[i4] = (char) read2;
                } else if (read2 <= 1114111) {
                    int i6 = i4 + 1;
                    cArr[i4] = (char) (((read2 - PKIFailureInfo.notAuthorized) >> 10) + 55296);
                    i5 = i6 + 1;
                    cArr[i6] = (char) (((read2 - PKIFailureInfo.notAuthorized) & Place.TYPE_SUBLOCALITY_LEVEL_1) + 56320);
                } else {
                    throw new CharConversionException("Cannot convert U+" + Integer.toHexString(read2) + " to char (code greater than U+10FFFF)");
                }
            } else {
                this._byteBuffer.position(this._byteBuffer.position() - 1);
                i5 = remaining + 1;
                return i4 - i;
            }
            i4 = i5;
            i5 = remaining;
        }
        return i2;
    }

    public void read(Appendable appendable) {
        if (this._byteBuffer == null) {
            throw new IOException("Reader closed");
        }
        while (this._byteBuffer.hasRemaining()) {
            byte b = this._byteBuffer.get();
            if (b >= null) {
                appendable.append((char) b);
            } else {
                int read2 = read2(b);
                if (read2 < PKIFailureInfo.notAuthorized) {
                    appendable.append((char) read2);
                } else if (read2 <= 1114111) {
                    appendable.append((char) (((read2 - PKIFailureInfo.notAuthorized) >> 10) + 55296));
                    appendable.append((char) (((read2 - PKIFailureInfo.notAuthorized) & Place.TYPE_SUBLOCALITY_LEVEL_1) + 56320));
                } else {
                    throw new CharConversionException("Cannot convert U+" + Integer.toHexString(read2) + " to char (code greater than U+10FFFF)");
                }
            }
        }
    }

    public void reset() {
        this._byteBuffer = null;
        this._code = 0;
        this._moreBytes = 0;
    }

    public UTF8ByteBufferReader setByteBuffer(ByteBuffer byteBuffer) {
        return setInput(byteBuffer);
    }
}
