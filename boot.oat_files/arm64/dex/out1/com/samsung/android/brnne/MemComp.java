package com.samsung.android.brnne;

public class MemComp {
    public static final int ERROR = -13;
    public static final int ERR_ALREADY_INITIALIZED = -21;
    public static final int ERR_INVALID_BS = -202;
    public static final int ERR_NOT_INITIALIZED = -22;
    public static final int ERR_OUT_OF_MEMORY = -102;
    public static final int ERR_UNKNOWN = -1999;
    public static final int ERR_UNSUPPORTED = -103;
    public static final int OK = 0;
    private long streamHandle = -1;

    private native int brnnCompressImpl(byte[] bArr, int i, byte[] bArr2, int i2, long j);

    private native int brnnDecompressImpl(byte[] bArr, int i, byte[] bArr2, int i2, long j);

    private native int brnnGetDecDstLength(byte[] bArr);

    private native long createBrnnStream();

    private native void endImpl(long j);

    static {
        System.loadLibrary("brnne_jni");
    }

    public synchronized int initStream() {
        int i;
        if (this.streamHandle != -1) {
            i = -21;
        } else {
            this.streamHandle = createBrnnStream();
            if (this.streamHandle < 0) {
                i = -13;
            } else {
                i = 0;
            }
        }
        return i;
    }

    public synchronized int compressBuffer(byte[] srcBuffer, int srcSize, byte[] dstBuffer, int dstSize) {
        int i;
        if (this.streamHandle == -1) {
            i = -22;
        } else {
            checkOffsetOfBuffer(srcBuffer, srcSize);
            checkOffsetOfBuffer(dstBuffer, dstSize);
            i = brnnCompressImpl(srcBuffer, srcSize, dstBuffer, dstSize, this.streamHandle);
        }
        return i;
    }

    public synchronized int decompressBuffer(byte[] srcBuffer, int srcSize, byte[] dstBuffer, int dstSize) {
        int i;
        if (this.streamHandle == -1) {
            i = -22;
        } else {
            checkOffsetOfBuffer(srcBuffer, srcSize);
            checkOffsetOfBuffer(dstBuffer, dstSize);
            i = brnnDecompressImpl(srcBuffer, srcSize, dstBuffer, dstSize, this.streamHandle);
        }
        return i;
    }

    public synchronized int getOriginalSize(byte[] srcBuffer) {
        return brnnGetDecDstLength(srcBuffer);
    }

    public synchronized void exitStream() {
        if (this.streamHandle != -1) {
            endImpl(this.streamHandle);
            this.streamHandle = -1;
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        exitStream();
    }

    private void checkOffsetOfBuffer(byte[] buffer, int offset) {
        if (offset < 0 || offset > buffer.length) {
            throw new ArrayIndexOutOfBoundsException("length : " + buffer.length + "; index : " + offset);
        }
    }
}
