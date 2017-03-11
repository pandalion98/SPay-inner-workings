package javolution.io;

import android.support.v4.internal.view.SupportMenu;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;

public final class UTF8ByteBufferWriter extends Writer {
    private ByteBuffer _byteBuffer;
    private char _highSurrogate;

    public UTF8ByteBufferWriter setOutput(ByteBuffer byteBuffer) {
        if (this._byteBuffer != null) {
            throw new IllegalStateException("Writer not closed or reset");
        }
        this._byteBuffer = byteBuffer;
        return this;
    }

    public void write(char c) {
        if (c < '\ud800' || c > '\udfff') {
            write((int) c);
        } else if (c < '\udc00') {
            this._highSurrogate = c;
        } else {
            write((((this._highSurrogate - 55296) << 10) + (c - 56320)) + PKIFailureInfo.notAuthorized);
        }
    }

    public void write(int i) {
        if ((i & -128) == 0) {
            this._byteBuffer.put((byte) i);
        } else {
            write2(i);
        }
    }

    private void write2(int i) {
        if ((i & -2048) == 0) {
            this._byteBuffer.put((byte) ((i >> 6) | CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
            this._byteBuffer.put((byte) ((i & 63) | X509KeyUsage.digitalSignature));
        } else if ((SupportMenu.CATEGORY_MASK & i) == 0) {
            this._byteBuffer.put((byte) ((i >> 12) | 224));
            this._byteBuffer.put((byte) (((i >> 6) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) ((i & 63) | X509KeyUsage.digitalSignature));
        } else if ((-14680064 & i) == 0) {
            this._byteBuffer.put((byte) ((i >> 18) | 240));
            this._byteBuffer.put((byte) (((i >> 12) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 6) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) ((i & 63) | X509KeyUsage.digitalSignature));
        } else if ((-201326592 & i) == 0) {
            this._byteBuffer.put((byte) ((i >> 24) | 248));
            this._byteBuffer.put((byte) (((i >> 18) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 12) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 6) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) ((i & 63) | X509KeyUsage.digitalSignature));
        } else if ((PKIFailureInfo.systemUnavail & i) == 0) {
            this._byteBuffer.put((byte) ((i >> 30) | 252));
            this._byteBuffer.put((byte) (((i >> 24) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 18) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 12) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) (((i >> 6) & 63) | X509KeyUsage.digitalSignature));
            this._byteBuffer.put((byte) ((i & 63) | X509KeyUsage.digitalSignature));
        } else {
            throw new CharConversionException("Illegal character U+" + Integer.toHexString(i));
        }
    }

    public void write(char[] cArr, int i, int i2) {
        int i3 = i + i2;
        while (i < i3) {
            int i4 = i + 1;
            char c = cArr[i];
            if (c < '\u0080') {
                this._byteBuffer.put((byte) c);
            } else {
                write(c);
            }
            i = i4;
        }
    }

    public void write(String str, int i, int i2) {
        int i3 = i + i2;
        while (i < i3) {
            int i4 = i + 1;
            char charAt = str.charAt(i);
            if (charAt < '\u0080') {
                this._byteBuffer.put((byte) charAt);
            } else {
                write(charAt);
            }
            i = i4;
        }
    }

    public void write(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            char charAt = charSequence.charAt(i);
            if (charAt < '\u0080') {
                this._byteBuffer.put((byte) charAt);
            } else {
                write(charAt);
            }
            i = i2;
        }
    }

    public void flush() {
        if (this._byteBuffer == null) {
            throw new IOException("Writer closed");
        }
    }

    public void close() {
        if (this._byteBuffer != null) {
            reset();
        }
    }

    public void reset() {
        this._byteBuffer = null;
        this._highSurrogate = '\u0000';
    }

    public UTF8ByteBufferWriter setByteBuffer(ByteBuffer byteBuffer) {
        return setOutput(byteBuffer);
    }
}
