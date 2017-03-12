package android.media;

import java.io.IOException;
import java.io.InputStream;

public final class AmrInputStream extends InputStream {
    private static final int SAMPLES_PER_FRAME = 160;
    private static final String TAG = "AmrInputStream";
    private final byte[] mBuf = new byte[320];
    private int mBufIn = 0;
    private int mBufOut = 0;
    private long mGae;
    private InputStream mInputStream;
    private byte[] mOneByte = new byte[1];

    private static native void GsmAmrEncoderCleanup(long j);

    private static native void GsmAmrEncoderDelete(long j);

    private static native int GsmAmrEncoderEncode(long j, byte[] bArr, int i, byte[] bArr2, int i2) throws IOException;

    private static native void GsmAmrEncoderInitialize(long j);

    private static native long GsmAmrEncoderNew();

    static {
        System.loadLibrary("media_jni");
    }

    public AmrInputStream(InputStream inputStream) {
        this.mInputStream = inputStream;
        this.mGae = GsmAmrEncoderNew();
        GsmAmrEncoderInitialize(this.mGae);
    }

    public int read() throws IOException {
        return read(this.mOneByte, 0, 1) == 1 ? this.mOneByte[0] & 255 : -1;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int offset, int length) throws IOException {
        if (this.mGae == 0) {
            throw new IllegalStateException("not open");
        }
        if (this.mBufOut >= this.mBufIn) {
            this.mBufOut = 0;
            this.mBufIn = 0;
            int i = 0;
            while (i < 320) {
                int n = this.mInputStream.read(this.mBuf, i, 320 - i);
                if (n == -1) {
                    return -1;
                }
                i += n;
            }
            this.mBufIn = GsmAmrEncoderEncode(this.mGae, this.mBuf, 0, this.mBuf, 0);
        }
        if (length > this.mBufIn - this.mBufOut) {
            length = this.mBufIn - this.mBufOut;
        }
        System.arraycopy(this.mBuf, this.mBufOut, b, offset, length);
        this.mBufOut += length;
        return length;
    }

    public void close() throws IOException {
        try {
            if (this.mInputStream != null) {
                this.mInputStream.close();
            }
            this.mInputStream = null;
            try {
                if (this.mGae != 0) {
                    GsmAmrEncoderCleanup(this.mGae);
                }
                try {
                    if (this.mGae != 0) {
                        GsmAmrEncoderDelete(this.mGae);
                    }
                    this.mGae = 0;
                } catch (Throwable th) {
                    this.mGae = 0;
                }
            } catch (Throwable th2) {
                this.mGae = 0;
            }
        } catch (Throwable th3) {
            this.mGae = 0;
        }
    }

    protected void finalize() throws Throwable {
        if (this.mGae != 0) {
            close();
            throw new IllegalStateException("someone forgot to close AmrInputStream");
        }
    }
}
